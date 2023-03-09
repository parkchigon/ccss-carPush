package com.lgu.ccss.carpush.service.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lgu.ccss.carpush.mapper.CarPushDao;
import com.lgu.ccss.carpush.service.worker.MQTTReport;

@Service
public class ReportServiceImpl implements ReportService{
	private final Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

	@Autowired
	CarPushDao carPushDao;
	@Autowired
	MQTTReport mqttReport;

	@Override
	public void doTask() throws Exception {
		// TODO Auto-generated method stub
		logger.debug("CarPushService doTask!");
		
		if(!mqttReport.connectionCheck()) {
			logger.info("###### START CAR PUSH REPORT RECIEVER DAEMON #####");
			logger.debug("MQTT Start!");
			mqttReport.startWorker();
			logger.info("###### END CAR PUSH REPORT RECIEVER DAEMON #####");
		}else {
			logger.debug("MQTT Listener is Running~!");
		}		
		
	}
}
