package com.appspot.smartshop.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.LoginFilter.UsernameFilterGMail;

import com.appspot.smartshop.dom.UserInfo;
import com.appspot.smartshop.mock.MockUserInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Global {

	/*
	 * Update interval
	 */
	public static int UPDATE_INTERVAL = 60000;

	/*
	 * User
	 */
	public static String username = MockUserInfo.getInstance().username;
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
	public static Intent intent = new Intent();
	public static DateFormat df = new SimpleDateFormat(NORMAL_DATE);
	public static DateFormat dfFull = new SimpleDateFormat(
			NORMAL_DATE_WITH_HOUR);
	public static InputFilter[] uneditableInputFilters = new InputFilter[] { new InputFilter() {
		public CharSequence filter(CharSequence src, int start, int end,
				Spanned dst, int dstart, int dend) {
			return src.length() < 1 ? dst.subSequence(dstart, dend) : "";
		}
	} };

	public static InputFilter[] usernameInputFilters = new InputFilter[] { new UsernameFilterGMail() };

	/*
	 * Intent key
	 */
	public static final String USER_INFO = "user_info";
	public static final String CAN_EDIT_USER_PROFILE = "can_edit_user_profile";
	public static final String USER_PRODUCT_LIST_TYPE = "product_list_type";

	public static final String PRODUCT_INFO = "product_info";
	public static final String CAN_EDIT_PRODUCT_INFO = "can_edit_product_info";

	public static final String PRODUCT_LIST_TYPE = "product_list_type";
	public static final String PAGE = "page";
	public static final String ID_OF_COMMENTS = "id_of_comments";
	public static final String TYPE_OF_COMMENTS = "type_of_comments";
	public static final String USER_NAME = "user_name";
	public static final String PAGES_LIST_TYPE = "pages_list_type";
	public static final String TYPE = "type";
	public static final String CATEGORY_INFO = "category_info";
	public static final String SELECTED_CATEGORIES = "selected_categories";
	
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
	
}