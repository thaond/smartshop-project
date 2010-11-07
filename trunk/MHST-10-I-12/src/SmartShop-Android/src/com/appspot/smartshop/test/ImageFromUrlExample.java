package com.appspot.smartshop.test;

import java.io.InputStream;
import java.net.URL;

import com.appspot.smartshop.R;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageFromUrlExample extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_load_image_from_url);
		ImageView imgView = (ImageView) findViewById(R.id.ImageView01);
		Drawable drawable = LoadImageFromWebOperations("http://hangxachtayusa.net/img/p/195-265-thickbox.jpg");
		imgView.setImageDrawable(drawable);
	}
	private Drawable LoadImageFromWebOperations(String url) {
		try {
			InputStream is = (InputStream) new URL(url).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		} catch (Exception e) {
			System.out.println("Exc=" + e);
			return null;
		}
	}
}
