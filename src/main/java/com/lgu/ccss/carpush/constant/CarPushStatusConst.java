package com.lgu.ccss.carpush.constant;

public class CarPushStatusConst {
	public static String PUSH_STATUS_SEND_MSG_READY 			= "0000";
	public static String PUSH_STATUS_SEND_MSG_ING 				= "0001";
	public static String PUSH_STATUS_SEND_MSG_SUCCESS			= "0002";
	public static String PUSH_STATUS_SEND_MSG_FAIL				= "0003";
	
	public static String PUSH_STATUS_DELIVER_REPORT_DELIVERED 	= "1002";
	public static String PUSH_STATUS_DELIVER_REPORT_FAIL		= "1003";
	public static String PUSH_STATUS_DELIVER_REPORT_EXPIRED		= "1004";
	public static String PUSH_STATUS_DELIVER_REPORT_REJECTED	= "1005";
	
	public static String PUSH_STATUS_READ_REPORT_READ	 		= "2002";
	public static String PUSH_STATUS_READ_REPORT_FAIL			= "2003";
	public static String PUSH_STATUS_READ_REPORT_DELETE			= "2004";
	
	public static String PUSH_RESULT_CODE_SEND_SUCCESS 	= "2S000000";
	public static String PUSH_RESULT_CODE_SEND_FAIL	 	= "5S000000";
}