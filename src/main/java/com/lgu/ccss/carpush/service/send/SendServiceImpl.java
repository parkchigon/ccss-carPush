package com.lgu.ccss.carpush.service.send;

import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lgu.ccss.carpush.config.CarPushConf;
import com.lgu.ccss.carpush.constant.CarPushStatusConst;
import com.lgu.ccss.carpush.mapper.CarPushDao;
import com.lgu.ccss.carpush.model.CarPushVO;
import com.lgu.ccss.carpush.service.worker.MQTTConnection;
import com.lgu.ccss.carpush.service.worker.MQTTMessageCtrl;
import com.lgu.ccss.carpush.service.worker.MQTTWorkerThread;
import com.lgu.common.tlo.TloWriter;

@Service
public class SendServiceImpl implements SendService{
	private final Logger logger = LoggerFactory.getLogger(SendServiceImpl.class);
	
	@Autowired
	CarPushDao carPushDao;
	@Autowired
	CarPushConf carPushConf;
	@Autowired
	private TloWriter tloWriter;
	
	@Autowired
	MQTTMessageCtrl mqttMessageCtl;
	
	MQTTConnection mqttSendConnection;
	
	int vectorSize = 1000;
	
	public static MQTTConnection mqttSession= null;
	public static Vector<CarPushVO> msgVector		= null;  // 벡터 크기

	public static MQTTWorkerThread workerThreadArr = null;
	Thread mqttThreadGrp[] = null;
	
	@Override
	public void doTask() throws Exception {
		logger.info("SendService doTask!");
		if(msgVector == null) {
			logger.info("msgVector create : "+vectorSize);
			msgVector= new Vector<CarPushVO>();
			msgVector.setSize(vectorSize);
			msgVector.clear();
		}
		
		// MQTT 커넥션 체크
		if(mqttSendConnection == null) {
			mqttSendConnection = new MQTTConnection();
		}

		logger.info("Connection check : "+mqttSendConnection.connectionStatus());
		if(!mqttSendConnection.connectionStatus()) {
			mqttSendConnection.connectionMqtt(carPushConf,carPushConf.getMqttSendId(),carPushConf.getMqttSendPw());
			mqttSession = mqttSendConnection;
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		} else {
			if( workerThreadArr == null ){
				makeWorkerThread();
			}
		}
		
		logger.info("getMessage!");
		getMessage();
	}
	
	public void makeWorkerThread() {
		int threadCnt = carPushConf.getThreadCnt();
		mqttThreadGrp = new Thread[threadCnt];
		for(int i=0; i< threadCnt;i++) {
			workerThreadArr = new MQTTWorkerThread(carPushDao,tloWriter);
			mqttThreadGrp[i] = new Thread(workerThreadArr);
			mqttThreadGrp[i].setName("CarPush ["+i+"] Thread");
			mqttThreadGrp[i].start();
		}
	}
	
	public void getMessage() {
		//현재 남아 있는 메시지 큐의 크기가 쓰레드 개수 보다 작을 때만 큐 가지고 옴.
		int threadCnt = carPushConf.getThreadCnt();
		if(msgVector.size() >= threadCnt) {
			return;
		}
		List<CarPushVO> targetList = carPushDao.getTargetList(carPushConf.getRowCount());
		if( targetList == null || targetList.size() == 0) {
			return;
		}	
		logger.info("getMessage Size ["+targetList.size()+" ]");
		// 조회 메시지 벡터에 삽입
		for( int i = 0; i < targetList.size(); i++ ){
			// DB에 처리중 상태 업데이트
			CarPushVO carPushVo = targetList.get(i);
			carPushVo.setSendTryCnt(0);
			if(carPushDao.carPushUpdateStatus(carPushVo, CarPushStatusConst.PUSH_STATUS_SEND_MSG_ING)) {
				msgVector.add(targetList.get(i));
			}
		}
	}
}
