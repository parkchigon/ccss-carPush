package com.lgu.ccss.carpush.service.worker;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lgu.ccss.carpush.constant.CarPushDataMapConst;
import com.lgu.ccss.carpush.constant.CarPushMqttConst;
import com.lgu.ccss.carpush.constant.CarPushStatusConst;
import com.lgu.ccss.carpush.mapper.CarPushDao;
import com.lgu.ccss.carpush.model.CarPushVO;
import com.lgu.ccss.carpush.model.PayLoadVO;
import com.lgu.ccss.carpush.service.send.SendServiceImpl;
import com.lgu.ccss.common.tlo.TloConst;
import com.lgu.ccss.common.tlo.TloData;
import com.lgu.ccss.common.tlo.TloUtil;
import com.lgu.ccss.common.util.DateUtils;
import com.lgu.common.tlo.TloWriter;
import com.lgu.common.util.JsonUtil;


public class MQTTWorkerThread implements Runnable{
	private final static Logger logger = LoggerFactory.getLogger(MQTTWorkerThread.class);
	
	CarPushDao carPushDao;
	String mqttId;
	String mqttPw;
	TloWriter tloWriter;
	
	public MQTTWorkerThread(CarPushDao carPushDao,TloWriter tloWriter) {
		this.carPushDao = carPushDao;
		this.tloWriter=tloWriter;
	}
	@Override
	public void run() {
		while(true) {
			try {
				CarPushVO pushMsgVo = null;
				
				synchronized(SendServiceImpl.msgVector){
					if(SendServiceImpl.msgVector.size() > 0){
						pushMsgVo=(CarPushVO)SendServiceImpl.msgVector.remove(0);
					}
				}
				if(pushMsgVo == null) {
					Thread.sleep(300);
					continue;
				}
				logger.info("CALL run ======================> CALL RUN ");

				makePushMsg(pushMsgVo);

				logger.info("CALL run ======================> RUN FINISH");
			} catch (InterruptedException e) {
				// TODO: handle exception
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void makePushMsg(CarPushVO pushMsgVo) {
		logger.info("processMessage pushMsgVo({})"+pushMsgVo);
		setTloData(pushMsgVo);
		String msgType = pushMsgVo.getMsgType();
		PayLoadVO payload = new PayLoadVO();
		payload.setMsgId(pushMsgVo.getMsgId());
		payload.setTitle(pushMsgVo.getMsgTitle());
		
		String expDt = pushMsgVo.getExpDt();
		if(expDt == "" || expDt == null) {
			expDt = DateUtils.getCurrentDate(Calendar.HOUR, 1, "yyyyMMddHHmm");
		}
		payload.setExpiration(expDt);
		
		if(pushMsgVo.getRcvReport().equals(CarPushMqttConst.MQTT_BOOL_INT_TRUE)) {
			payload.setRcvReport(CarPushMqttConst.MQTT_BOOL_STR_TRUE);
		}else {
			payload.setRcvReport(CarPushMqttConst.MQTT_BOOL_STR_FALSE);
		}
		if(pushMsgVo.getReadReport().equals(CarPushMqttConst.MQTT_BOOL_INT_TRUE)) {
			payload.setReadReport(CarPushMqttConst.MQTT_BOOL_STR_TRUE);
		}else {
			payload.setReadReport(CarPushMqttConst.MQTT_BOOL_STR_FALSE);
		}

		String payloadStr = "";
		try {
			Object content = JsonUtil.unmarshallingJson(pushMsgVo.getMsgCont(), Object.class);
			if(CarPushMqttConst.MQTT_TYPE_CODE_SINGLE.equalsIgnoreCase(msgType)) {
				payload.setCtn(pushMsgVo.getRecvPhoneNo());
				payload.setType(CarPushMqttConst.MQTT_TYPE_STR_SINGLE);
				
			}else if(CarPushMqttConst.MQTT_TYPE_CODE_NOTICE.equalsIgnoreCase(msgType)){
				payload.setType(CarPushMqttConst.MQTT_TYPE_STR_NOTICE);
			}
			payload.setContent(content);
			payloadStr = JsonUtil.marshallingJson(payload);
			logger.info("payload : "+payloadStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("IOException",e);
		}
		
		sendPushMsg(pushMsgVo,payloadStr);
	}
	public void sendPushMsg(CarPushVO pushData,String payloadStr){
		String msgType = pushData.getMsgType();
		String topic = CarPushMqttConst.MQTT_TOPIC_DEVICE_BASE+pushData.getDeviceType().toLowerCase()+CarPushMqttConst.MQTT_TOPIC_SPLASH;
		
		if(CarPushMqttConst.MQTT_TYPE_CODE_SINGLE.equalsIgnoreCase(msgType)) {
			topic +=pushData.getRecvPhoneNo();
		}
		
		String resultStatus = CarPushStatusConst.PUSH_STATUS_SEND_MSG_FAIL;
		
		int retryCnt = pushData.getSendTryCnt();
		logger.info("####################### Send Push Message Start ##########################");
		if(SendServiceImpl.mqttSession.sendPushMessage(topic,payloadStr)) {
			logger.info("####################### Send Push Message End ##########################");
			logger.info("### Push Message 발송 완료 ###");
			// ### 상태값 전송성공으로 업데이트
			resultStatus = CarPushStatusConst.PUSH_STATUS_SEND_MSG_SUCCESS;
		}else {
			logger.info("####################### Send Push Message End ##########################");
			logger.error("### Push Message 발송 실패 ### ");
			// 실패시 큐에 적재
			if(retryCnt < 3) {
				pushData.setSendTryCnt(retryCnt+1);
				SendServiceImpl.msgVector.add(pushData);
				return;
			}
		}
		setTloData(pushData, resultStatus);
		// 최종 DB상태 업데이트
		carPushDao.carPushProcessFinish(pushData, resultStatus);
		logger.info( "sendPushMsg Deliver Message END");
	}

	public static void setTloData(CarPushVO pushData) {
		Map<String, String> tlo = new HashMap<String, String>();
		tlo.put(TloData.LOG_TYPE, TloConst.LOG_TYPE_SVC);
		tlo.put(TloData.SID, pushData.getOrgNo());
		tlo.put(TloData.REQ_TIME, pushData.getRegDt());
		tlo.put(TloData.CARRIER_TYPE, TloConst.CARRIER_TYPE_ETC);
		tlo.put(TloData.CARPUSH_SVC_CLASS, TloConst.CP01);
		tlo.put(TloData.CARPUSH_REQ_TIME, TloData.getNowDate());
		
		CarPushDataMapConst.tloMap.put(pushData.getMsgId(), tlo);
		
	}
	private void setTloData(CarPushVO pushData,String resultCode) {
		if (pushData == null) {
			logger.debug("############# PUSH DATA VO NUll !!!!");
			return;
		}
		Map<String, String> tlo = (Map<String, String>) CarPushDataMapConst.tloMap.get(pushData.getMsgId());
		String statusCode;
		if(resultCode.equals(CarPushStatusConst.PUSH_STATUS_SEND_MSG_SUCCESS)) {
			statusCode = CarPushStatusConst.PUSH_RESULT_CODE_SEND_SUCCESS;
		}else {
			statusCode = CarPushStatusConst.PUSH_RESULT_CODE_SEND_FAIL;
		}
		
		tlo.put(TloData.CARPUSH_RES_TIME, TloData.getNowDate());
		tlo.put(TloData.RESULT_CODE, statusCode);
		tlo.put(TloData.RSP_TIME, TloData.getNowDate());
		tlo.put(TloData.CARPUSH_RESULT_CODE, resultCode);
		
		logger.debug("tlo : "+CarPushDataMapConst.tloMap.get(pushData.getMsgId())+"tlocheck : "+CarPushDataMapConst.tloMap.containsKey(pushData.getMsgId())+"TloData.getNowDate() : "+TloData.getNowDate() + "| statusCode : "+statusCode + " | TloData.getNowDate() : "+TloData.getNowDate() + " | resultCode : "+resultCode );
		
		TloUtil.setTloData(tlo);
		tloWriter.write(tlo);
		CarPushDataMapConst.tloMap.remove(pushData.getMsgId());
		logger.debug("############# TLO WRITE  DONE !!!!");
	}
}
