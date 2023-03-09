package com.lgu.ccss.carpush.scheduler.send;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.lgu.ccss.carpush.service.send.SendService;
import com.lgu.ccss.carpush.service.status.StatusInitService;

@Service
public class SendScheduler {
	private final Logger logger = LoggerFactory.getLogger(SendScheduler.class);
	
	@Autowired
	private SendService carPushService;
	@Autowired
	private StatusInitService statusInitService;
	
	boolean isFirst = true;
	
	@Scheduled(fixedRateString  = "${delay.time}")
	public void startWork() {
		try {
			logger.info("###### START CAR PUSH DAEMON #####");
			if(isFirst) {
				logger.debug("FirstTime!! CAR PUSH Data Status Init!");
				statusInitService.doTask();
				logger.debug("CAR PUSH Message Data Init! Success!!");
				isFirst = false;
			}
			carPushService.doTask();

		} catch (Exception e) {
			logger.error("{}", e);

		} finally {
			logger.info("###### END CAR PUSH DAEMON #####");
		}
	}
}
