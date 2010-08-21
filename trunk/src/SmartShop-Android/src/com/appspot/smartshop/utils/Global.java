package com.appspot.smartshop.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

public class Global {

	public static Activity application = null;	// point to HomeActivity
	public static DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	public static DateFormat dfFull = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
	
	/*
	 * Intent key 
	 */
	public static final String USER_INFO = "user_info";
	public static final String CAN_EDIT_USER_PROFILE = "can_edit_user_profile";
}
