package com.appspot.smartshop.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.Utils;

/**
 * @author VoMinhTam
 */
public abstract class BaseUIActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		onCreatePre();

		initHeader(this);

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

	public static void initHeader(final Activity activity) {
		Button btnHome = (Button) activity.findViewById(R.id.btnHome);
		btnHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utils.returnHomeActivity(activity);
			}
		});

		Button btnBack = (Button) activity.findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.finish();
			}
		});

		TextView lblName = (TextView) activity.findViewById(R.id.lblUser);
		if (Utils.isLogined())
			lblName.setText(Global.userInfo.username);
		else
			lblName.setVisibility(View.GONE);
	}
}
