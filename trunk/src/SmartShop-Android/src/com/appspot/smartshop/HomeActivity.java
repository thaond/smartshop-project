package com.appspot.smartshop;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.appspot.smartshop.map.DirectionListActivity;
import com.appspot.smartshop.mock.MockPage;
import com.appspot.smartshop.mock.MockCategory;
import com.appspot.smartshop.mock.MockUserInfo;
import com.appspot.smartshop.ui.page.PageActivity;
import com.appspot.smartshop.ui.page.PagesListActivity;
import com.appspot.smartshop.ui.page.ViewPageActivity;
import com.appspot.smartshop.ui.product.PostProductActivity;
import com.appspot.smartshop.ui.product.SearchByCategoryActivity;
import com.appspot.smartshop.ui.product.SearchProductActivity;
import com.appspot.smartshop.ui.product.ViewSingleProduct;
import com.appspot.smartshop.ui.user.UserActivity;
import com.appspot.smartshop.ui.user.UserProfileActivity;
import com.appspot.smartshop.utils.Global;

import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;


public class HomeActivity extends Activity {
	public static final String TAG = "[HomeActivity]";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);

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
		testPostProduct();
	}

	protected void test2() {
	}

	protected void test3() {
	}
	
	private void testUserProfile() {
		Intent intent = new Intent(this, UserProfileActivity.class);
		intent.putExtra(Global.USER_NAME, Global.username);
		startActivity(intent);
	}
	
	private void testEditPage() {
		Intent intent = new Intent(this, PageActivity.class);
		intent.putExtra(Global.PAGE, MockPage.getInstance());
		startActivity(intent);
	}
	
	private void testPagesList() {
		Intent intent = new Intent(this, PagesListActivity.class);
		startActivity(intent);
	}
	
	private void testCreatePage() {
		Intent intent = new Intent(this, PageActivity.class);
		startActivity(intent);
	}
	
	private void testViewPage() {
		Intent intent = new Intent(this, ViewPageActivity.class);
		intent.putExtra("page", MockPage.getInstance());


		startActivity(intent);
	}
	
	private void testAsyncTask() {
		new AsyncTask<Void, Void, Void>() {
			private final ProgressDialog dialog = new ProgressDialog(Global.application);
			
			protected void onPreExecute() {
				dialog.setCancelable(false);
				dialog.setCanceledOnTouchOutside(false);
				dialog.setMessage("Loading...");
				dialog.show();
			};

			@Override
			protected Void doInBackground(Void... params) {
				loadJson();
				return null;
			}
			
			protected void onPostExecute(Void result) {
				if (dialog != null) {
					dialog.setMessage("Finish");
					dialog.dismiss();
				}
			};
			
		}.execute();
	}
	
	private void loadJson() {
		String url = "http://search.twitter.com/trends.json";
		RestClient.getData(url, new JSONParser() {
			
			@Override
			public void onSuccess(JSONObject json) throws JSONException {
				System.out.println("as_of = " + json.getString("as_of"));
				JSONArray arrTrends = json.getJSONArray("trends");
				int len = arrTrends.length();
				JSONObject obj = null;
				for (int i = 0; i < len; ++i) {
					obj = arrTrends.getJSONObject(i);
					System.out.println("name = " + obj.getString("name"));
					System.out.println("url = " + obj.getString("url"));
				}
			}
			@Override
			public void onFailure(String message) {
				Log.e(TAG, "fail");
				Log.e(TAG, message);
			}
		});
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
	private void testViewProduct() {
		Intent intent = new Intent(this, ViewSingleProduct.class);
		startActivity(intent);
	}
	private void testPostProduct() {
		Intent intent = new Intent(this, PostProductActivity.class);
		startActivity(intent);
	}
	private void searchByEnterQuery() {
		Intent intent = new Intent(this, SearchProductActivity.class);
		startActivity(intent);
	}
	private void searchByCategory() {
		Intent intent = new Intent(this, SearchByCategoryActivity.class);
		intent.putExtra(Global.CATEGORY_INFO, MockCategory.getInstance());
		intent.putExtra(Global.TYPE, MainActivity.PRODUCT);
		startActivity(intent);
		
	}
}