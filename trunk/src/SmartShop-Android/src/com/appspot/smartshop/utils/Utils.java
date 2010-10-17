package com.appspot.smartshop.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.Display;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.gson.Gson;


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
	
	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void createOKDialog(Context context, String title,
			String text, DialogInterface.OnClickListener clickListener) {
		AlertDialog ad = new AlertDialog.Builder(context).setPositiveButton("OK",
				clickListener).setTitle(title).setMessage(text).create();
		ad.setCancelable(false);
		ad.show();
	}
	
	public static void setEditableEditText(EditText editText, boolean isEditable){
		if (!isEditable) {
			editText.setFilters(new InputFilter[] { new InputFilter() {
				@Override
				public CharSequence filter(CharSequence source, int start,
						int end, Spanned dest, int dstart, int dend) {
					return source.length() < 1 ? dest.subSequence(dstart, dend)
							: "";
				}
			} });
		} else {
			editText.setFilters(new InputFilter[] { new InputFilter() {
				@Override
				public CharSequence filter(CharSequence source, int start,
						int end, Spanned dest, int dstart, int dend) {
					return null;
				}
			} });
		}
	}
	
	public static String getMD5(String pass){
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			byte[] data = pass.getBytes();
			m.update(data, 0, data.length);
			BigInteger i = new BigInteger(1, m.digest());	
			return String.format("%1$032x", i);
		} catch (NoSuchAlgorithmException e) {
		}
		
		return null;
	}
	
//	move_uploaded_file(
//			  $_FILES["file"]["tmp_name"],
//			  $dir . preg_replace('/[^a-z0-9_\-\.]/i', '_', $_FILES["file"]["name"])
//			);
}
