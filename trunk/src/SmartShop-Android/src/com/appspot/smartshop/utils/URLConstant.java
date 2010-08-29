package com.appspot.smartshop.utils;

public class URLConstant {
	public static final String HOST = "http://10.0.2.2:8888/";
	
	/************************ Error code and message ********************/
	public static final String ERROR_CODE = "errCode";
	public static final String MESSAGE = "message";

	/************************ URL ***************************************/
	public static final String ADD_NEW_COMMENT = HOST + "/api/asd/create-comment/";
	
	public static final String GET_PRODUCT_BY_QUERY = HOST + "/api/asd/product-search/?q=%s";
	public static final String POST_PRODUCT = HOST + "/api/asd/registerproduct/ ";
	public static final String GET_PRODUCTS = HOST + "/api/asd/product-search-criteria-cat/?status=0";;
	public static final String GET_PRODUCTS_BY_CRITERIA = HOST + "/api/asd/product-search-criteria-cat/?status=0&criterias=%s";
	public static final String GET_PRODUCTS_BY_CATEGORIES = HOST + "/api/asd/product-search-criteria-cat/?status=0&criterias=%s&cat_keys=%s";
	
	public static final String LOGIN = HOST + "/api/asd/account-login/?username=%s&password=%s";
	public static final String REGISTER = HOST + "/api/asd/account-register/";
	public static final String EDIT_PROFILE = HOST + "/api/asd/account-editprofile/";
	public static final String GET_USER_INFO = HOST + "/api/asd/account-getuser/?username=%s";
}
