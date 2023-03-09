package com.lgu.ccss.carpush.service.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lgu.ccss.carpush.config.CarPushConf;

@Component
public class MQTTReport {
	
	MQTTConnection mqttConnection = null;
	@Autowired
	CarPushConf carPushConf;
	@Autowired
	MQTTMessageCtrl mqttMessageCtl;
	
	public void startWorker() {
		// TODO Auto-generated method stub
		mqttConnection = new MQTTConnection(mqttMessageCtl);
		mqttConnection.connectionMqtt(carPushConf,carPushConf.getMqttReportId(),carPushConf.getMqttReportPw());
		mqttConnection.setSubscribe();
	}
	
	public boolean connectionCheck() {
		if(mqttConnection == null) {
			return false;
		}
		return mqttConnection.connectionStatus();
	}
}
