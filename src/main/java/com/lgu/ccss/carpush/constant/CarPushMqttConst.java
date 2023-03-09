package com.lgu.ccss.carpush.constant;

public class CarPushMqttConst {
	public static String MQTT_TYPE_CODE_SINGLE			= "0001";
	public static String MQTT_TYPE_CODE_NOTICE			= "0002";
	public static String MQTT_TYPE_STR_SINGLE			= "single";
	public static String MQTT_TYPE_STR_NOTICE			= "notice";
	
	public static String MQTT_SEND_TYPE_PRIORITY	="0";
	public static String MQTT_SEND_TYPE_NORMAL		="1";
	
	public static String MQTT_TOPIC_KEEP_ALIVE		= "service/alive";
	public static String MQTT_TOPIC_REPORT 			= "service/report";
	
	public static String MQTT_TOPIC_DEVICE_BASE		= "device/";
	public static String MQTT_TOPIC_SPLASH 			= "/";
	
	
	public static String MQTT_MSG_TYPE_DELIVERY_REPORT	= "rcvReport";
	public static String MQTT_MSG_TYPE_READ_REPORT		= "readReport";
	
	public static String MQTT_MSG_RCV_STATUS_DELIVERED	= "delivered";
	public static String MQTT_MSG_RCV_STATUS_EXPIRED	= "expired";
	public static String MQTT_MSG_RCV_STATUS_REJECTED	= "rejected";
	
	public static String MQTT_MSG_READ_STATUS_READ		= "read";
	public static String MQTT_MSG_READ_STATUS_DELETE	= "deleted";
	
	public static String MQTT_REPORT_CTN			= "ctn";
	public static String MQTT_REPORT_TYPE			= "type";
//	public static String MQTT_REPORT_SUB_TYPE		= "sub-type";
	public static String MQTT_REPORT_DELIV_STATUS	= "deliveryStatus";
	public static String MQTT_REPORT_READ_STATUS	= "readStatus";
	public static String MQTT_REPORT_BAD_STATUS		= "BAD";
	public static String MQTT_REPORT_MSG_ID			= "msgId";
	public static String MQTT_REPORT_TSTAMP			= "tstamp";
	
	public static String MQTT_BOOL_INT_TRUE			= "1";
	public static String MQTT_BOOL_INT_FALSE		= "0";
	public static String MQTT_BOOL_STR_TRUE			= "true";
	public static String MQTT_BOOL_STR_FALSE		= "false";
}
