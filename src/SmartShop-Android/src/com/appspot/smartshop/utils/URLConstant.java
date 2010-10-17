package com.appspot.smartshop.utils;

public class URLConstant {
	public static final String HOST = "http://10.0.2.2:8888";
	
	/************************ Error code and message ********************/
	public static final String ERROR_CODE = "errCode";
	public static final String MESSAGE = "message";
	public static final String ERROR = "error";

	/************************ URL ***************************************/
	
	public static final String POST_PRODUCT = HOST + "/api/asd/registerproduct/";
	public static final String GET_PRODUCTS = HOST + "/api/asd/product-search-criteria-cat/?status=1";;
	public static final String GET_PRODUCTS_BY_CRITERIA = HOST + "/api/asd/product-search-criteria-cat/?status=0&criterias=%s";
	public static final String GET_PRODUCTS_BY_LOCATION = HOST + "/api/asd/searchproductproximity/";
	
	public static final String CREATE_PAGE = HOST + "/api/asd/create-page/";
	public static final String EDIT_PAGE = HOST + "/api/asd/page-edit/";
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
	
	public static final String GET_VATGIA_PRODUCTS = HOST + "/api/asd/parser-vatgia-keyword/?keyword=%s&page=%d";
	public static final String GET_DETAIL_OF_VATGIA_PRODUCT = HOST + "/api/asd/parser-vatgia-each-product-n/?url=%s";
	
	public static final String SEND_EMAIL_TO_ADMIN = HOST + "/api/asd/mail-send-to-admin/?sender=%s&title=%s&cont=%s";
	public static final String SEND_EMAIL = HOST + "/api/asd/mail-send-to-admin/?sender=%s&title=%s&cont=%s&to=%s";
	
	public static final String GET_NOTIFICATIONS = HOST + "/api/asd/noti-get-by/" ;
	public static final String MARK_AS_READ_ALL_NOTIFICATIONS = HOST + "/api/asd/noti-mark-as-read/";
	
	public static final String GET_USER_SUBCRIBES = HOST + "/api/asd/get-subscribe-by-user/?username=%s&mode=0";
	public static final String GET_PRODUCTS_OF_SUBCRIBE = HOST + "/api/asd/get-products-in-sub-range/?id=%d";
	public static final String CREATE_SUBCRIBE = HOST + "/api/asd/create-subcribe/";
	public static final String EDIT_SUBCRIBE = HOST + "/api/asd/edit-subcribe/";
	
	/********* Image Hosting */
	public static final String HOST_IMG = "http://10.0.2.2/testupload/";
	public static final String URL_UPLOAD_AVATAR = HOST_IMG + "myupload.php?username=%s";
	public static final String URL_UPLOAD_IMG_PRODUCT = HOST_IMG + "uploadimages.php";
	
	public static final String SEARCH_FRIEND_BY_QUERY = HOST + "/api/asd/account-search/?q=";
	public static final String ADD_FRIENDS_TO_LIST = HOST + "/api/asd/account-addfriend/";
}

