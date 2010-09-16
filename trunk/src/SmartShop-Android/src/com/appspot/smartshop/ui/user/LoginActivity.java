package com.appspot.smartshop.ui.user;

import org.apache.commons.logging.Log;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.SmartShopActivity;
import com.appspot.smartshop.dom.UserInfo;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.StringUtils;
import com.appspot.smartshop.utils.URLConstant;

public class LoginActivity extends Activity {
	public static final String TAG = "LoginActivity";
	
	private TextView lblUsername;
	private EditText txtUsername;
	private TextView lblPassword;
	private EditText txtPassword;
	private String lastActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = display.getWidth(); 
		int labelWidth = (int) (width * 0.25);
		int textWidth = width - labelWidth;
		
		// set up textviews
		lblUsername = (TextView) findViewById(R.id.lblUsername);
		lblUsername.setWidth(labelWidth);
		txtUsername = (EditText) findViewById(R.id.txtUsername);
		txtUsername.setWidth(textWidth);
		
		lblPassword = (TextView) findViewById(R.id.lblPassword);
		lblPassword.setWidth(labelWidth);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		txtPassword.setWidth(textWidth);
		
		// butons
		Button btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				login();
			}
		});
		
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		lastActivity = getIntent().getStringExtra(Global.LOGIN_LAST_ACTIVITY);
	}

	private SimpleAsyncTask task;
	protected void login() {
		String username = txtUsername.getText().toString();
		String pass = txtPassword.getText().toString();
		final String url = String.format(URLConstant.LOGIN, username, pass);
		
		task = new SimpleAsyncTask(getString(R.string.loading_when_login), this, new DataLoader() {
			
			@Override
			public void updateUI() {
			}
			
			@Override
			public void loadData() {
				RestClient.getData(url, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						Global.isLogin = true;
						Global.userInfo = Global.gsonDateWithoutHour.fromJson(json.get("userinfo").toString(), UserInfo.class);
						Global.username = Global.userInfo.username;
						Global.lat = Global.userInfo.lat;
						Global.lng = Global.userInfo.lng;
						System.out.println(Global.username);
						
						if (StringUtils.isEmptyOrNull(lastActivity)){
							Intent intent = new Intent(LoginActivity.this, SmartShopActivity.class);
							startActivity(intent);
						}else{
							if (lastActivity.equals(Global.VIEW_PROFILE_ACTIVITY)){
								Intent intent = new Intent(LoginActivity.this, ViewProfileActivity.class);
								startActivity(intent);
							}
						}
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
}
