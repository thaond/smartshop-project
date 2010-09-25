package com.appspot.smartshop.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;

import android.app.Activity;

import com.appspot.smartshop.dom.UserInfo;
import com.appspot.smartshop.mock.MockUserInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Global {

	/*
	 * Update interval
	 */
	public static int UPDATE_INTERVAL = 60000;
	public static boolean isStop = false;

	/*
	 * User
	 */
	// TODO test 
	public static String username = "duc";
	public static boolean isLogin = false;	
	public static UserInfo userInfo = null;

	/*
	 * Misc
	 */
	public static final int SUCCESS = 0;
	public static final int ERROR = 1;
	
	public static final String NORMAL_DATE = "dd-MM-yyyy";
	public static final String NORMAL_DATE_WITH_HOUR = "dd/MM/yyyy hh:mm:ss";
	public static final Gson gsonDateWithoutHour = new GsonBuilder()
			.setDateFormat(NORMAL_DATE).excludeFieldsWithExcludeAnnotation()
			.create();
	public static final Gson gsonWithHour = new GsonBuilder().setDateFormat(
			NORMAL_DATE_WITH_HOUR).excludeFieldsWithExcludeAnnotation()
			.create();
	public static Activity application = null; // point to HomeActivity
	public static DateFormat df = new SimpleDateFormat(NORMAL_DATE);
	public static DateFormat dfFull = new SimpleDateFormat(
			NORMAL_DATE_WITH_HOUR);
	public static DateFormat dfTimeStamp = new SimpleDateFormat("yyyyMMddHHmmssSS");
	
	// (key_cat, name) of categories
	public static HashMap<String, String> mapParentCategories = new HashMap<String, String>();
	public static HashMap<String, String> mapChildrenCategories = new HashMap<String, String>();
	
	// (key_cat, child categories)
	public static LinkedList<String[]> listCategories = new LinkedList<String[]>();

	/*
	 * Intent key
	 */
	public static final String USER_INFO = "user_info";
	public static final String CAN_EDIT_USER_PROFILE = "can_edit_user_profile";
	
	public static final String PRODUCTS_OF_USER = "products_of_user";

	public static final String PRODUCT_INFO = "product_info";
	public static final String CAN_EDIT_PRODUCT_INFO = "can_edit_product_info";

	public static final String PAGE = "page";
	public static final String PAGES_TYPE = "pages_type";
	public static final String PAGES_OF_USER = "pages_of_user";
	
	public static final String PRODUCTS_TYPE = "products_ype";
	public static final String USER_NAME = "user_name";
	public static final String TYPE = "type";
	public static final String LAT_OF_USER = "lat";
	public static final String LONG_OF_USER ="lng";
	
	public static final String ID_OF_COMMENTS = "id_of_comments";
	public static final String TYPE_OF_COMMENTS = "type_of_comments";
	
	public static final String CATEGORY_INFO = "category_info";
	public static final String SELECTED_CATEGORIES = "selected_categories";
	
	public static final String FILE_INTENT_ID = "file";
	public static final String BYTE_ARRAY_INTENT_ID = "byte_array";
	public static final String FILTER_FILE = "filter";
	
	public static final String VATGIA_URL_LIST_SHOP = "vatgia_url_list_shop";
	
	public static final String LOGIN_LAST_ACTIVITY = "last";
	
	public static final String SUBCRIBE_ID = "subcribe_id";
	/*
	 * ACTIVITY ACTION NAME
	 */
	public static final String USER_ACTIVITY = "User";
	public static final String LOGIN_ACTIVITY = "Login";
	public static final String TEST_ACTIVITY = "Test";
	public static final String POST_PRODUCT_ACTIVITY = "PostProduct";
	public static final String POST_PRODUCT_ACTIVITY_BASIC_ATTRIBUTE = "PostProductBasicAttribute";
	public static final String POST_PRODUCT_ACTIVITY_USER_DEFINE = "PostProductUserDefine";
	public static final String SEARCH_PRODUCT_ACTIVITY = "SearchProduct";
	public static final String SEARCH_BY_ACTIVITY = "SearchByCategory";
	public static final String PAGE_ACTIVITY = "Page";
	public static final String VIEW_PAGE_ACTIVITY = "ViewPage";
	public static final String VIEW_COMMENTS_ACTIVITY = "ViewComments";
	public static final String PRODUCTS_LIST_ACTIVITY = "ProductsList";
	public static final String VIEW_SINGLE_ACTIVITY = "ViewSingle";
	public static final String VIEW_BASIC_ATTRIBUTE_OF_PRODUCT = "ViewBasicAttributeOfProduct";
	public static final String VIEW_ADVANCE_ATTRIBUTE_OF_PRODUCT = "ViewAdvanceAttributeOfProduct";
	public static final String PAGES_LIST_ACTIVITY = "PagesList";
	public static final String USER_PROFILE_ACTIVITY = "UserProfile";
	public static final String USER_PRODUCT_LIST_ACTIVITY = "UserProductList";
	public static final String IMAGE_FROM_URL_EXAMPLE = "ImageFromUrlExample";
	public static final String SEARCH_PRODUCTS_ON_MAP_ACTIVITY = "SearchProductsOnMap";
	public static final String MAIN_ACTIVITY = "Main";
	public static final String DIRECTION_LIST_ACTIVITY = "DirectionList";
	public static final String UPLOAD_ACTIVITY = "Upload";
	public static final String FILE_BROWSER_ACTIVITY = "FileBrowser";
	public static final String IMAGE_CAPURE_ACTIVITY = "ImageCapture";
	public static final String VIEW_PROFILE_ACTIVITY = "ViewProfile";
	public static final String TITLE_NOTIFICATION = "TitleNotification";
	public static final String CONTENT_NOTIFICATION = "ContentNotification";
}
