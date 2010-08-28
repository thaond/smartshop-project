package com.appspot.smartshop.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.appspot.smartshop.mock.MockUserInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.app.Activity;
import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.LoginFilter.UsernameFilterGMail;
import android.view.Display;
import android.view.WindowManager;

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

	/*
	 * Misc
	 */
	public static final String NORMAL_DATE = "dd-MM-yyyy";
	public static final String NORMAL_DATE_WITH_HOUR = "dd/MM/yyyy hh:mm:ss";
	public static final Gson gsonDateWithoutHour = new GsonBuilder()
			.setDateFormat(NORMAL_DATE).excludeFieldsWithExcludeAnnotation()
			.create();
	public static final Gson gsonWithDate = new GsonBuilder().setDateFormat(
			NORMAL_DATE_WITH_HOUR).excludeFieldsWithExcludeAnnotation()
			.create();
	public static Activity application = null; // point to HomeActivity
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
}
