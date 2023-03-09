package com.lgu.ccss.carpush.service.worker;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lgu.ccss.carpush.config.CarPushConf;
import com.lgu.ccss.carpush.constant.CarPushMqttConst;
import com.lgu.ccss.common.util.SslUtil;

@Component
public class MQTTConnection implements MqttCallback{
	private final Logger logger = LoggerFactory.getLogger(MQTTConnection.class);
	
	private MqttAsyncClient client;
	private MqttConnectOptions conOpt;
	
//	@Autowired
	MQTTMessageCtrl mqttMessageCtl;

	public MQTTConnection() {
	}
	public MQTTConnection(MQTTMessageCtrl mqttMessageCtl) {
		this.mqttMessageCtl = mqttMessageCtl;
	}
	public boolean connectionStatus() {
		if(client == null) {
			return false;
		}else {
			logger.debug(" client.isConnected() : "+ client.isConnected());
			return client.isConnected();
		}
	}
	
	public boolean connectionMqtt(CarPushConf carPushConf,String mqttId,String mqttPw) {
		logger.info("MQTT ID [ "+mqttId+" ]");
		conOpt = new MqttConnectOptions();
		conOpt.setCleanSession(true);
		
		if(mqttPw != null ) {
			conOpt.setPassword(mqttPw.toCharArray());
    	}
    	if(mqttId != null) {
    		conOpt.setUserName(mqttId);
    	}
    	conOpt.setKeepAliveInterval(0);
    	
    	try {
			conOpt.setSocketFactory(SslUtil.getSocketFactory(carPushConf.getCrtFilePath(), null, null, null));	
			client = new MqttAsyncClient(carPushConf.getMqttUrl(),mqttId, new MemoryPersistence());
			
			IMqttToken mqttToken;
			mqttToken = client.connect(conOpt);
			mqttToken.waitForCompletion(carPushConf.getTimeOut());

			client.setCallback(this);
			logger.info("### ConnectMqtt :"+Thread.currentThread().getName());

			return true;
    	} catch (Exception e) {
			logger.error("connectionMqtt [ Exception ]",e);
			return false;
		}	
	}
	
	public void setSubscribe() {
		try {
			client.subscribe(CarPushMqttConst.MQTT_TOPIC_KEEP_ALIVE, 0);
			client.subscribe(CarPushMqttConst.MQTT_TOPIC_REPORT, 0);
		} catch (MqttException e) {
			logger.error("ERROR [setSubscribe]",e);
		}
		logger.info("### Complete Subscribe!"+Thread.currentThread().getName());
	}
	
	public boolean sendPushMessage(String topic, String string) {
		
		logger.info("### sendPushMessage | Topic : "+topic + " | Content : "+string);
		
		MqttMessage message = new MqttMessage(string.getBytes());
		message.setQos(1);
		IMqttDeliveryToken token;
		try {
			token = client.publish(topic, message);
			token.waitForCompletion(5000);
			return true;
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			logger.error("SendPushMessage Exception!",e);
			return false;
		}
	}
	
	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		logger.error("ConnectionLost!", cause);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) {
		// TODO Auto-generated method stub
		logger.info("MessageArrived Process START!!");
		String reportJson = new String(message.getPayload());	
		logger.info("MessagePayload : "+reportJson);
		mqttMessageCtl.processReportData(reportJson);
		logger.info("MessageArrived Process E N D!!");
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		logger.info("deliveryComplete");
	}

}
