package com.appspot.smartshop.utils;

public class URLConstant {
	public static final String HOST = "http://10.0.2.2:8888";

	/************************ Error code and message ********************/
	public static final String ERROR_CODE = "errCode";
	public static final String MESSAGE = "message";
	public static final String ERROR = "error";

	/************************ URL ***************************************/
	public static final String GET_PRODUCT_BY_ID = HOST
			+ "/api/asd/get-product/?id=%d";

	public static String POST_PRODUCT = HOST
			+ "/api/asd/registerproduct/?session=%s";
	public static final String GET_PRODUCTS = HOST
			+ "/api/asd/product-search-criteria-cat/?status=1";;
	public static final String GET_PRODUCTS_BY_CRITERIA = HOST
			+ "/api/asd/product-search-criteria-cat/?status=0&criterias=%s";
	public static final String GET_PRODUCTS_BY_LOCATION = HOST
			+ "/api/asd/searchproductproximity/";

	public static String CREATE_PAGE = HOST
			+ "/api/asd/create-page/?session=%s";
	public static String EDIT_PAGE = HOST
			+ "/api/asd/page-edit/?session=%s";
	public static final String GET_PAGES_OF_USER = HOST
			+ "/api/asd/page-get-by-username/?username=%s";
	public static final String GET_PAGES = HOST
			+ "/api/asd/page-search-criteria/?status=1";
	public static final String GET_PAGES_BY_CRITERIA = HOST
			+ "/api/asd/page-search-criteria/?status=1&criteria=%s";
	public static final String GET_PAGE_BY_ID = HOST
			+ "/api/asd/get-page/?id=%d";

	public static final String LOGIN = HOST
			+ "/api/asd/account-login/?username=%s&password=%s&userkey=%s";
	public static final String LOGOUT = HOST 
			+ "/api/asd/account-logout/?session=%s";

	public static final String REGISTER = HOST + "/api/asd/account-register/";
	public static String EDIT_PROFILE = HOST
			+ "/api/asd/account-editprofile/?session=%s";
	public static final String GET_USER_INFO = HOST
			+ "/api/asd/account-getuser/?username=%s";
	public static final String GET_USER_INFO_SESSION = HOST
			+ "/api/asd/account-getuser/?session=%s";
	public static final String GET_INTERESTED_PRODUCTS_OF_USER = HOST
			+ "/api/asd/product-get-interested-product/?username=%s&limit=0";
	public static final String GET_SELLED_PRODUCTS_OF_USER = HOST
			+ "/api/asd/product-get-selled-product/?username=%s&limit=0";
	public static final String GET_BUYED_PRODUCTS_OF_USER = HOST
			+ "/api/asd/product-get-buyed-product/?username=%s&limit=0";

	public static String ADD_NEW_COMMENT = HOST
			+ "/api/asd/create-comment/?session=%s";
	public static final String GET_COMMENTS = HOST + "/api/asd/get-comment/";

	public static final String GET_PARENT_CATEGORIES = HOST
			+ "/api/asd/category-get-sub/";
	public static final String GET_CHILD_CATEGORIES = HOST
			+ "/api/asd/category-get-sub/?parent_id=%s";

	public static final String GET_VATGIA_PRODUCTS = HOST
			+ "/api/asd/parser-vatgia-keyword/?keyword=%s&page=%d";
	public static final String GET_DETAIL_OF_VATGIA_PRODUCT = HOST
			+ "/api/asd/parser-vatgia-each-product-n/?url=%s";

	public static String SEND_EMAIL_TO_ADMIN = HOST
			+ "/api/asd/mail-send-to-admin/?sender=%s&title=%s&cont=%s&session=%s";
	public static String SEND_EMAIL = HOST
			+ "/api/asd/mail-send-to-admin/?sender=%s&title=%s&cont=%s&to=%s&session=%s";

	public static final String GET_NOTIFICATIONS = HOST
			+ "/api/asd/noti-get-by/?username=%s&type=%d";
	public static String MARK_NOTIFICATION_AS_READ = HOST
			+ "/api/asd/noti-mark-some-as-read/?session=%s&ids=%d";

	public static final String GET_USER_SUBCRIBES = HOST
			+ "/api/asd/get-subscribe-by-user/?username=%s&mode=0";
	public static final String GET_PRODUCTS_OF_SUBCRIBE = HOST
			+ "/api/asd/get-products-in-sub-range/?id=%d";
	public static String CREATE_SUBCRIBE = HOST
			+ "/api/asd/create-subcribe/?session=%s";
	public static String EDIT_SUBCRIBE = HOST
			+ "/api/asd/edit-subcribe/?session=%s";

	public static final String SEARCH_FRIEND_BY_QUERY = HOST
			+ "/api/asd/account-search/?q=";

	public static String ADD_FRIENDS_TO_LIST = HOST
			+ "/api/asd/account-addfriend/?session=%s";	

	public static final String RATE_PRODUCT = HOST
			+ "/api/asd/product-vote/?id=%d&star=%d";

	public static final String TAG_FRIEND_TO_PRODUCT = HOST
			+ "/api/asd/tag-friend-to-product/?session=%s&productID=%d&usernames=%s";
	public static final String UNTAG_FRIEND_FROM_PRODUCT = HOST
			+ "/api/asd/untag-friend-from-product/?session=%s&productID=%d&usernames=%s";

	public static final String TAG_FRIEND_TO_PAGE = HOST
			+ "/api/asd/tag-friend-to-page/?session=%s&pageID=%d&usernames=%s";
	public static final String UNTAG_FRIEND_FROM_PAGE = HOST
			+ "/api/asd/untag-friend-from-page/?session=%s&pageID=%d&usernames=%s";
	/********* Image Hosting */
	public static final String HOST_IMG = "http://10.0.2.2/testupload/";
	public static final String URL_UPLOAD_AVATAR = HOST_IMG
			+ "myupload.php?username=%s";
	public static final String URL_UPLOAD_IMG_PRODUCT = HOST_IMG
			+ "uploadimages.php";
}
