package com.appspot.smartshop.utils;

public class URLConstant {
	/************************ Error code and message */
	public static final String ERROR_CODE = "errCode";
	public static final String MESSAGE = "message";
	
	/************************ URL */
	public static final String BASE_URL = "http://10.0.2.2:8888";
	public static final String LOGIN = BASE_URL + "/api/asd/account-login/?username=%s&password=%s";
	public static final String POST_PRODUCT = BASE_URL + "/api/asd/registerproduct/ ";
	public static final String GET_PRODUCT_BY_QUERY = BASE_URL + "/api/asd/procut-search/?q=%s";
	public static final String GET_USER_INFO = BASE_URL + "/api/asd/account-getuser/?username=%s";
	public static final String ADD_NEW_COMMENT = BASE_URL + "/api/asd/create-comment/";
	public static final String GET_NEWEST_PRODUCTS = BASE_URL + "/api/asd/product-search-criteria-cat/?status=0&criterias=1";
	public static final String GET_CHEAPEST_PRODUCTS = BASE_URL + "/api/asd/product-search-criteria-cat/?status=0&criterias=2";
}
