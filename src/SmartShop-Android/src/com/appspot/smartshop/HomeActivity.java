package com.appspot.smartshop;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.appspot.smartshop.dom.Page;
import com.appspot.smartshop.map.DirectionListActivity;
import com.appspot.smartshop.mock.MockUserInfo;
import com.appspot.smartshop.ui.page.ViewCommentsActivity;
import com.appspot.smartshop.ui.page.ViewPageActivity;
import com.appspot.smartshop.ui.user.UserActivity;
import com.appspot.smartshop.utils.Global;

public class HomeActivity extends Activity {
	public static final String TAG = "[HomeActivity]";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Global.application = this;

		Button btn1 = (Button) findViewById(R.id.Button01);
		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				test1();
			}
		});

		Button btn2 = (Button) findViewById(R.id.Button02);
		btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				test2();
			}
		});

		Button btn3 = (Button) findViewById(R.id.Button03);
		btn3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				test3();
			}
		});
	}

	// TODO (condorhero01): place test function into test1, test2, or test3 to
	// test UI
	// should adjust the button's text in main.xml file as name of the test
	protected void test1() {
		testRegisterForm();
	}

	protected void test2() {
		testViewUserInfo();
	}

	protected void test3() {
		testEditUserInfo();
	}

	private void testEditUserInfo() {
		Intent intent = new Intent(this, UserActivity.class);
		intent.putExtra(Global.USER_INFO, MockUserInfo.getInstance());
		intent.putExtra(Global.CAN_EDIT_USER_PROFILE, true);
		startActivity(intent);
	}

	private void testViewUserInfo() {
		Intent intent = new Intent(this, UserActivity.class);
		intent.putExtra(Global.USER_INFO, MockUserInfo.getInstance());
		startActivity(intent);
	}

	private void testDirectionList() {
		double lat1, lat2, lng1, lng2;
		lat1 = 10.781189;
		lng1 = 106.6513;
		lat2 = 10.77769;
		lng2 = 106.660978;
		Intent intent = new Intent(this, DirectionListActivity.class);
		intent.putExtra("lat1", lat1);
		intent.putExtra("lat2", lat2);
		intent.putExtra("lng1", lng1);
		intent.putExtra("lng2", lng2);
		Log.d(TAG, "before startActivity(intent)");
		startActivity(intent);
	}

	private void testRegisterForm() {
		Intent intent = new Intent(this, UserActivity.class);
		startActivity(intent);
	}

	private void testGmapIntent() {
		String url = "http://maps.google.com/maps?saddr=10.775495,106.661181&daddr=10.76072,106.661021";
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(intent);
	}

	private void testViewPage() {
		Page page = new Page();
		page.content = "hàng việt nam chất lượng cao";
		page.name = "hàng dổm";
		page.page_view = 100;
		page.date_post = new Date();

		Intent intent = new Intent(this, ViewPageActivity.class);
		intent.putExtra("page", page);

		startActivity(intent);
	}
}