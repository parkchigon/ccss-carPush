package com.lgu.ccss.carpush.model;

public class MQTTReportVO {
	private String ctn;
	private String type;
	private String msgId;
	private String tstamp;
	private String subType;
	
	public String getCtn() {
		return ctn;
	}
	public void setCtn(String ctn) {
		this.ctn = ctn;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubType() {
		return subType;
	}
	public void setSubType(String subType) {
		this.subType = subType;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getTstamp() {
		return tstamp;
	}
	public void setTstamp(String tstamp) {
		this.tstamp = tstamp;
	}
	@Override
	public String toString() {
		return "MQTTReportVO [ctn=" + ctn + ", type=" + type + ", subType=" + subType + ", msgId=" + msgId + ", tstamp="
				+ tstamp + "]";
	}
	
	
}
