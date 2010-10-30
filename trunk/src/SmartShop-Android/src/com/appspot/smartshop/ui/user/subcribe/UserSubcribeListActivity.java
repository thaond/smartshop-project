package com.appspot.smartshop.ui.user.subcribe;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.UserSubcribeAdapter;
import com.appspot.smartshop.dom.UserSubcribeProduct;
import com.appspot.smartshop.facebook.Facebook;
import com.appspot.smartshop.facebook.LoginButton;
import com.appspot.smartshop.facebook.SessionEvents;
import com.appspot.smartshop.facebook.SessionStore;
import com.appspot.smartshop.facebook.Util;
import com.appspot.smartshop.facebook.SessionEvents.AuthListener;
import com.appspot.smartshop.facebook.SessionEvents.LogoutListener;
import com.appspot.smartshop.ui.BaseUIActivity;
import com.appspot.smartshop.ui.product.ProductsListActivity;
import com.appspot.smartshop.ui.product.ProductsListActivity.SampleAuthListener;
import com.appspot.smartshop.ui.product.ProductsListActivity.SampleLogoutListener;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;

public class UserSubcribeListActivity extends BaseUIActivity {
	public static final String TAG = "[UserSubcribeListActivity]";

	private List<UserSubcribeProduct> subcribes;
	private UserSubcribeAdapter adapter;
	private LoginButton mLoginButton;
	@Override
	protected void onCreatePre() {
		setContentView(R.layout.subcribe_list);
	}

	@Override
	protected void onCreatePost(Bundle savedInstanceState) {
		Button btnAddSubcribe = (Button) findViewById(R.id.btnAddSubcribe);
		mLoginButton = (LoginButton) findViewById(R.id.loginFACE);
		if (Global.APP_ID == null) {
			Util.showAlert(this, "Warning", "Facebook Applicaton ID must be "
					+ "specified before running");
		}
		//set up variable for facebook connection
		Global.mFacebook = new Facebook();
		SessionStore.restore(Global.mFacebook, this);
		SessionEvents.addAuthListener(new SampleAuthListener());
		SessionEvents.addLogoutListener(new SampleLogoutListener());
		mLoginButton.init(Global.mFacebook, Global.PERMISSIONS);
		if(Global.mFacebook.isSessionValid()){
			mLoginButton.setVisibility(View.GONE);
		}
		btnAddSubcribe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addNewSubcribe();
			}
		});

		listSubcribe = (ListView) findViewById(R.id.listSubcribe);
		loadSubcribeList();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		loadSubcribeList();
	}

	private SimpleAsyncTask task;
	private ListView listSubcribe;

	private void loadSubcribeList() {
		task = new SimpleAsyncTask(this, new DataLoader() {

			@Override
			public void updateUI() {
				adapter = new UserSubcribeAdapter(
						UserSubcribeListActivity.this, 0, subcribes);
				listSubcribe.setAdapter(adapter);
			}

			@Override
			public void loadData() {
				// TODO remove Global.userInfo.username=duc after test
				String url = String.format(URLConstant.GET_USER_SUBCRIBES,
						Global.userInfo.username);

				RestClient.getData(url, new JSONParser() {

					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						JSONArray arr = json
								.getJSONArray("usersubscribeproducts");
						subcribes = Global.gsonWithHour.fromJson(
								arr.toString(), UserSubcribeProduct.getType());
						Log.d(TAG, "load " + subcribes.size() + " subcribes");
					}

					@Override
					public void onFailure(String message) {
						task.hasData = false;
						task.message = message;
						task.cancel(true);
					}
				});
			}
		});
		task.execute();
	}

	protected void addNewSubcribe() {
		// TODO add new subcribe
		Log.d(TAG, "[ADD NEW SUBCRIBE]");
		Intent intent = new Intent(this, SubcribeActivity.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, getString(R.string.return_to_home)).setIcon(R.drawable.home);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == 0) {
			Utils.returnHomeActivity(this);
		}
		return super.onOptionsItemSelected(item);
	}
	public class SampleAuthListener implements AuthListener {

		public void onAuthSucceed() {
			Toast.makeText(UserSubcribeListActivity.this,
					getString(R.string.loginFacebookSuccess),
					Toast.LENGTH_SHORT).show();
		}

		public void onAuthFail(String error) {
			Toast.makeText(UserSubcribeListActivity.this,
					getString(R.string.loginFacebookFail), Toast.LENGTH_SHORT)
					.show();
		}
	}

	public class SampleLogoutListener implements LogoutListener {
		public void onLogoutBegin() {
			Toast.makeText(UserSubcribeListActivity.this,
					getString(R.string.logoutFacebookLoading),
					Toast.LENGTH_SHORT).show();
		}

		public void onLogoutFinish() {
			Toast.makeText(UserSubcribeListActivity.this,
					getString(R.string.logoutFacebookSuccess),
					Toast.LENGTH_SHORT).show();
		}
	}
}
