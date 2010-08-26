package com.appspot.smartshop.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.appspot.smartshop.mock.MockUserInfo;

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
	public static Activity application = null;	// point to HomeActivity
	public static DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	public static DateFormat dfFull = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
	public static InputFilter[] uneditableInputFilters = new InputFilter[] {new InputFilter() {
		public CharSequence filter(CharSequence src, int start, int end,
				Spanned dst, int dstart, int dend) {
			return src.length() < 1 ? dst.subSequence(dstart, dend) : "";
		}
	}};
	
	public static InputFilter[] usernameInputFilters = new InputFilter[] {new UsernameFilterGMail()};
	
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
	public static final String USER_NAME = "user_name";
	public static final String PAGES_LIST_TYPE = "pages_list_type";
	public static final String TYPE = "type";
	public static final String CATEGORY_INFO = "category_info";
}
