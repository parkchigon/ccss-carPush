package com.lgu.ccss.carpush.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CarPushConf {

	// System Properties
	@Value("#{systemProperties.SERVER_ID}")
	private String svrId;
	@Value("#{config['catPush.systemId']}")
	private String systemId;

	@Value("#{config['carPush.vectorSize']}")
	private int vectorSize;
	@Value("#{config['carPush.threadCnt']}")
	private int threadCnt;
	@Value("#{config['db.rowCount']}")
	private int rowCount;
	@Value("#{config['carPush.mqtt.send.id']}")
	private String mqttSendId;
	@Value("#{config['carPush.mqtt.send.pw']}")
	private String mqttSendPw;
	@Value("#{config['carPush.mqtt.report.id']}")
	private String mqttReportId;
	@Value("#{config['carPush.mqtt.report.pw']}")
	private String mqttReportPw;
	@Value("#{config['carPush.mqtt.url']}")
	private String mqttUrl;
	@Value("#{config['carPush.mqtt.timeout']}")
	private int timeOut;
	@Value("#{config['carPush.mqtt.crtFilePath']}")
	private String crtFilePath;
	
	
	
	
	public String getSvrId() {
		return svrId;
	}
	public void setSvrId(String svrId) {
		this.svrId = svrId;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public int getVectorSize() {
		return vectorSize;
	}
	public void setVectorSize(int vectorSize) {
		this.vectorSize = vectorSize;
	}
	public int getThreadCnt() {
		return threadCnt;
	}
	public void setThreadCnt(int threadCnt) {
		this.threadCnt = threadCnt;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public String getMqttSendId() {
		return mqttSendId;
	}
	public void setMqttSendId(String mqttSendId) {
		this.mqttSendId = mqttSendId;
	}
	public String getMqttSendPw() {
		return mqttSendPw;
	}
	public void setMqttSendPw(String mqttSendPw) {
		this.mqttSendPw = mqttSendPw;
	}
	public String getMqttReportId() {
		return mqttReportId;
	}
	public void setMqttReportId(String mqttReportId) {
		this.mqttReportId = mqttReportId;
	}
	public String getMqttReportPw() {
		return mqttReportPw;
	}
	public void setMqttReportPw(String mqttReportPw) {
		this.mqttReportPw = mqttReportPw;
	}
	public String getMqttUrl() {
		return mqttUrl;
	}
	public void setMqttUrl(String mqttUrl) {
		this.mqttUrl = mqttUrl;
	}
	public int getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}
	public String getCrtFilePath() {
		return crtFilePath;
	}
	public void setCrtFilePath(String crtFilePath) {
		this.crtFilePath = crtFilePath;
	}
	
}
