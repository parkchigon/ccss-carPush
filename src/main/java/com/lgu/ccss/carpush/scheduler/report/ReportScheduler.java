package com.lgu.ccss.carpush.scheduler.report;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.lgu.ccss.carpush.service.report.ReportService;

@Service
public class ReportScheduler {
	private final Logger logger = LoggerFactory.getLogger(ReportScheduler.class);
	
	@Autowired
	private ReportService carPushService;

	@Scheduled(fixedRateString  = "${delay.report.time}")
	public void startWork() {
		try {
			carPushService.doTask();
		} catch (Exception e) {
			logger.error("{}", e);
		} finally {
			
		}
	}
}
