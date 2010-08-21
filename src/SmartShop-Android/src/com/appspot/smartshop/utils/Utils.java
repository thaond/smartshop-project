package com.appspot.smartshop.utils;

import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.InputFilter;
import android.text.Spanned;
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
	
	public static InputFilter[] uneditableInputFilters = new InputFilter[] {new InputFilter() {
		public CharSequence filter(CharSequence src, int start, int end,
				Spanned dst, int dstart, int dend) {
			return src.length() < 1 ? dst.subSequence(dstart, dend) : "";
		}
	}};
}
