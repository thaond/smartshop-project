package com.appspot.smartshop.utils;

public class URLConstant {
	public static final String HOST = "http://10.0.2.2:8888/";
	/************************ Error code and message */
	public static final String ERROR_CODE = "errCode";
	public static final String MESSAGE = "message";

	/************************ URL */
	/************************ User Service */
	public static final String LOGIN = HOST + "/api/asd/account-login/?username=%s&password=%s";
	public static final String REGISTER = HOST + "/api/asd/account-register/";
	public static final String EDIT_PROFILE = HOST + "/api/asd/account-editprofile/";
	public static final String POST_PRODUCT = HOST + "/api/asd/registerproduct/ ";
	public static final String GET_PRODUCT_BY_QUERY = HOST + "/api/asd/procut-search/?q=%s";
	public static final String GET_USER_INFO = HOST + "/api/asd/account-getuser/?username=%s";
	public static final String ADD_NEW_COMMENT = HOST + "/api/asd/create-comment/";
	public static final String GET_NEWEST_PRODUCTS = HOST + "/api/asd/product-search-criteria-cat/?status=0&criterias=1";
	public static final String GET_CHEAPEST_PRODUCTS = HOST + "/api/asd/product-search-criteria-cat/?status=0&criterias=2";
}
