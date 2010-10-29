package com.appspot.smartshop.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.appspot.smartshop.R;
import com.appspot.smartshop.SmartShopActivity;
import com.appspot.smartshop.utils.Utils;

/**
 * @author VoMinhTam
 */
public abstract class BaseUIActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		onCreatePre();
		
		Button btnHome = (Button) findViewById(R.id.btnHome);
		btnHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utils.returnHomeActivity(BaseUIActivity.this);
			}
		});
		
		Button btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		onCreatePost(savedInstanceState);
	}
	
	/**
	 * Add setContentView
	 */
	protected abstract void onCreatePre();
	
	/**
	 * Initialize other things here
	 */
	protected abstract void onCreatePost(Bundle savedInstanceState);
}
