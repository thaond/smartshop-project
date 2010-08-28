package com.appspot.smartshop.ui.user;

import sv.skunkworks.showtimes.lib.asynchronous.HttpService;
import sv.skunkworks.showtimes.lib.asynchronous.ServiceCallback;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.UserInfo;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.URLConstant;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LoginActivity extends Activity {
	public static final String TAG = "LoginActivity";
	
	private TextView lblUsername;
	private EditText txtUsername;
	private TextView lblPassword;
	private EditText txtPassword;

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
	}

	protected void login() {
		// TODO (condorhero01): mock login, assign Global.isLogin = true when click Login button
		Global.isLogin = true;
		
		// TODO (condorhero01): request login service
		String username = txtUsername.getText().toString();
		String pass = txtPassword.getText().toString();
		HttpService.getResource(String.format(URLConstant.LOGIN, username, pass), 
			true, new ServiceCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				Log.d(TAG, result);
				
				JsonParser parser = new JsonParser();
				JsonObject json = parser.parse(result).getAsJsonObject();
				String errCode = json.getAsString("errCode");
				Log.d(TAG, "errCode: " + errCode);
				UserInfo userInfo = Global.gsonDateWithoutHour.fromJson(json.get("userinfo"), UserInfo.class);
				Log.d(TAG, "UserInfo: " + userInfo);
			}
			
			@Override
			public void onFailure(Exception ex) {
				ex.printStackTrace();
			}
		});
		
		finish();
	}
}
