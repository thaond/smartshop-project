package com.appspot.smartshop.utils;

public class URLConstant {
	/************************ Error code and message */
	public static final String ERROR_CODE = "errCode";
	public static final String MESSAGE = "message";
	
	/************************ URL */
	public static final String BASE_URL = "http://10.0.2.2:8888";
	public static final String LOGIN = BASE_URL + "/api/asd/account-login/?username=%s&password=%s";
	public static final String POST_PRODUCT = BASE_URL + "/api/asd/registerproduct/ ";
}
