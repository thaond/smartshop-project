package com.appspot.smartshop.utils;

public class URLConstant {
	public static final String HOST = "http://10.0.2.2:8888";
	
	/************************ Error code and message ********************/
	public static final String ERROR_CODE = "errCode";
	public static final String MESSAGE = "message";
	public static final String ERROR = "error";

	/************************ URL ***************************************/
	
	public static final String POST_PRODUCT = HOST + "/api/asd/registerproduct/";
	public static final String GET_PRODUCTS_BY_QUERY = HOST + "/api/asd/product-search/?q=%s";
	public static final String GET_PRODUCTS = HOST + "/api/asd/product-search-criteria-cat/?status=1";;
	public static final String GET_PRODUCTS_BY_CRITERIA = HOST + "/api/asd/product-search-criteria-cat/?status=0&criterias=%s";
	public static final String GET_PRODUCTS_BY_LOCATION = HOST + "/api/asd/searchproductproximity/";
	
	public static final String CREATE_PAGE = HOST + "/api/asd/create-page/";
	public static final String EDIT_PAGE = HOST + "/api/asd/edit-page/";
	public static final String GET_PAGES_OF_USER = HOST + "/api/asd/page-get-by-username/?username=%s";
	public static final String GET_PAGES = HOST + "/api/asd/page-search-criteria/?status=1";
	public static final String GET_PAGES_BY_CRITERIA = HOST + "/api/asd/page-search-criteria/?status=1&criteria=%s";
	
	public static final String LOGIN = HOST + "/api/asd/account-login/?username=%s&password=%s";
	public static final String REGISTER = HOST + "/api/asd/account-register/";
	public static final String EDIT_PROFILE = HOST + "/api/asd/account-editprofile/";
	public static final String GET_USER_INFO = HOST + "/api/asd/account-getuser/?username=%s";
	public static final String GET_INTERESTED_PRODUCTS_OF_USER = HOST + "/api/asd/product-get-interested-product/?username=%s&limit=0";
	public static final String GET_SELLED_PRODUCTS_OF_USER = HOST + "/api/asd/product-get-selled-product/?username=%s&limit=0";
	public static final String GET_BUYED_PRODUCTS_OF_USER = HOST + "/api/asd/product-get-buyed-product/?username=%s&limit=0";
	
	public static final String ADD_NEW_COMMENT = HOST + "/api/asd/create-comment/";
	public static final String GET_COMMENTS = HOST + "/api/asd/get-comment/";
	
	public static final String GET_PARENT_CATEGORIES = HOST + "/api/asd/category-get-sub/";
	public static final String GET_CHILD_CATEGORIES = HOST + "/api/asd/category-get-sub/?parent_id=%s";
	
	/********* Image Hosting */
	public static final String HOST_IMG = HOST + "/testupload/myupload.php?username=%s";
}
