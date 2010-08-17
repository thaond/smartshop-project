package com.appspot.smartshop;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.WindowManager;

public class Global {

	public static Activity currentActivity = null;
	
	private static int screenWidth = -1;
	public static int getScreenWidth() {
		if (screenWidth == -1) {
			Display display = ((WindowManager) currentActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			screenWidth = display.getWidth();
		}
		
		return screenWidth;
	}
}
