package com.appspot.smartshop.utils;

import java.io.OutputStream;

import com.google.gson.Gson;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.LoginFilter.UsernameFilterGMail;
import android.view.Display;
import android.view.WindowManager;


public class Utils {
	private static final int AVATAR_WIDTH = 32;
	private static final int AVATAR_HEIGHT = 32;

	public OutputStream scaleImage(Bitmap bitmap) {
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(
				bitmap, AVATAR_WIDTH, AVATAR_HEIGHT, false);
		
		// TODO (condorhero01): what output stream?
		OutputStream outputStream = null;
		scaledBitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
		
		return outputStream;
	}
	
	private static int screenWidth = -1;
	public static int getScreenWidth() {
		if (screenWidth == -1) {
			Display display = ((WindowManager) Global.application.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			screenWidth = display.getWidth();
		}
		
		return screenWidth;
	}
	
	public static int random(int n) {
		return (int) (Math.random() * n);
	}
	
	public static Gson gson = new Gson();
}
