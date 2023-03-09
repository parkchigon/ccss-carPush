package com.lgu.ccss.carpush.service.worker;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lgu.ccss.carpush.constant.CarPushMqttConst;
import com.lgu.ccss.carpush.constant.CarPushStatusConst;
import com.lgu.ccss.carpush.mapper.CarPushDao;
import com.lgu.ccss.carpush.model.MQTTReportVO;

@Component
public class MQTTMessageCtrl {
	private final Logger logger = LoggerFactory.getLogger(MQTTMessageCtrl.class);
	
	@Autowired
	CarPushDao carPushDao;
	
	public MQTTReportVO getMQTTReport(String reportJson) {
		MQTTReportVO mqttReportVo = new MQTTReportVO();
		JSONObject reportJsonObj = new JSONObject();
		JSONParser parser = new JSONParser();
		try {
			reportJsonObj = (JSONObject) parser.parse(reportJson);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			logger.error("ParseException ",e);
			return null;
		}
		String type = (String)reportJsonObj.get(CarPushMqttConst.MQTT_REPORT_TYPE);
		mqttReportVo.setCtn((String)reportJsonObj.get(CarPushMqttConst.MQTT_REPORT_CTN));
		mqttReportVo.setType(type);
		
		if(type.equals("rcvReport")) {
			mqttReportVo.setSubType((String)reportJsonObj.get(CarPushMqttConst.MQTT_REPORT_DELIV_STATUS));
		}else if(type.equals("readReport")) {
			mqttReportVo.setSubType((String)reportJsonObj.get(CarPushMqttConst.MQTT_REPORT_READ_STATUS));
		}else {
			mqttReportVo.setSubType((String)reportJsonObj.get(CarPushMqttConst.MQTT_REPORT_BAD_STATUS));
		}
		
		mqttReportVo.setMsgId((String) reportJsonObj.get(CarPushMqttConst.MQTT_REPORT_MSG_ID));
		mqttReportVo.setTstamp((String)reportJsonObj.get(CarPushMqttConst.MQTT_REPORT_TSTAMP));
		return mqttReportVo;
	}
	
	public void processReportData(String message) {
		MQTTReportVO mqttReportVo = getMQTTReport(message);
		String msgStatus = "";
		if(mqttReportVo == null) {
			logger.info("Arrived Message is Invalied!");
			return;
		}

		logger.info("Report Type : " +mqttReportVo.getType());
		if(mqttReportVo.getType().equals(CarPushMqttConst.MQTT_MSG_TYPE_DELIVERY_REPORT)){
			if(mqttReportVo.getSubType().equals(CarPushMqttConst.MQTT_MSG_RCV_STATUS_DELIVERED)) {
				msgStatus = CarPushStatusConst.PUSH_STATUS_DELIVER_REPORT_DELIVERED;
			}else if(mqttReportVo.getSubType().equals(CarPushMqttConst.MQTT_MSG_RCV_STATUS_EXPIRED)){
				msgStatus = CarPushStatusConst.PUSH_STATUS_DELIVER_REPORT_EXPIRED;
			}else if(mqttReportVo.getSubType().equals(CarPushMqttConst.MQTT_MSG_RCV_STATUS_REJECTED)){
				msgStatus = CarPushStatusConst.PUSH_STATUS_DELIVER_REPORT_REJECTED;
			}else {
				msgStatus = CarPushStatusConst.PUSH_STATUS_DELIVER_REPORT_FAIL;
			}
		}else if(mqttReportVo.getType().equals(CarPushMqttConst.MQTT_MSG_TYPE_READ_REPORT)) {
			if(mqttReportVo.getSubType().equals(CarPushMqttConst.MQTT_MSG_READ_STATUS_READ)) {
				msgStatus = CarPushStatusConst.PUSH_STATUS_READ_REPORT_READ;
			}else if(mqttReportVo.getSubType().equals(CarPushMqttConst.MQTT_MSG_READ_STATUS_DELETE)){
				msgStatus = CarPushStatusConst.PUSH_STATUS_READ_REPORT_DELETE;
			}else {
				msgStatus = CarPushStatusConst.PUSH_STATUS_READ_REPORT_FAIL;
			}
		}else {
			logger.info("Process Report Data! check : " +mqttReportVo.getType());
			logger.info("Invalid Report Type!");
			return;
		}

		logger.debug("Parse ReportVO Data ["+mqttReportVo.toString() + "] MSG_STATUS ["+msgStatus+"]");
		String msgId = mqttReportVo.getMsgId();
		carPushDao.carPushUpdateReport(msgId, msgStatus);
		return;
	}	
}
