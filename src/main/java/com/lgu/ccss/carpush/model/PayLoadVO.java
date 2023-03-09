package com.lgu.ccss.carpush.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayLoadVO {
	private String ctn;
	private String type;
	private String msgId;
	private String title;
	private String expiration;
	private String rcvReport;
	private String readReport;
	
	private Object content;
	
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
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getExpiration() {
		return expiration;
	}
	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}
	public String getRcvReport() {
		return rcvReport;
	}
	public void setRcvReport(String rcvReport) {
		this.rcvReport = rcvReport;
	}
	public String getReadReport() {
		return readReport;
	}
	public void setReadReport(String readReport) {
		this.readReport = readReport;
	}
	
	public Object getContent() {
		return content;
	}
	public void setContent(Object content) {
		this.content = content;
	}
	
//	public String toNoticeJSONString() {
//		String jsonStr = "{"
//				+ "\"type\":\""+type+"\","
//				+ "\"msg-id\":\""+msgId+"\",\"title\":\""+title+"\","
//				+ "\"expiration\":\""+expiration+"\",\"rcv-report\":\""+rcvReport+"\","
//				+ "\"read-report\":\""+readReport+"\",\"txt\":\""+txt+"\"}";
//		return jsonStr;
//	}
//	public String toSingleJSONString() {
//		String jsonStr = "{"
//				+ "\"ctn\":\""+ctn+"\",\"type\":\""+type+"\","
//				+ "\"msg-id\":\""+msgId+"\",\"title\":\""+title+"\","
//				+ "\"expiration\":\""+expiration+"\",\"rcv-report\":\""+rcvReport+"\","
//				+ "\"read-report\":\""+readReport+"\",\"content\":\""+content+"\"}";
//		return jsonStr;
//	}
//	@Override
//	public String toString() {
//		return "SinglePayloadVO [ctn=" + ctn + ", type=" + type + ", msgId=" + msgId + ", title=" + title
//				+ ", expiration=" + expiration + ", rcvReport=" + rcvReport + ", readReport=" + readReport + ", txt="
//				+ txt + "]";
//	}
}
