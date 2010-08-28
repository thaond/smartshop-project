package com.appspot.smartshop.ui.user;

import sv.skunkworks.showtimes.lib.asynchronous.HttpService;
import sv.skunkworks.showtimes.lib.asynchronous.ServiceCallback;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.UserInfo;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.URLConstant;
import com.google.gson.JsonObject;

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
		String username = txtUsername.getText().toString();
		String pass = txtPassword.getText().toString();
		HttpService.getResource(String.format(URLConstant.LOGIN, username, pass), 
			true, new ServiceCallback() {
			
			@Override
			public void onSuccess(JsonObject result) {
				int errCode = Integer.parseInt(result.getAsString("errCode"));
				String message = result.getAsString("message");
				switch (errCode) {
				case Global.SUCCESS:
					Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
					Global.userInfo = Global.gsonDateWithoutHour.fromJson(result.get("userinfo"), UserInfo.class);
					Global.isLogin = true;
					finish();
					break;

				default:
					Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
					Global.isLogin = false;
					break;
				}
			}
			
			@Override
			public void onFailure(Exception ex) {
				ex.printStackTrace();
			}
		});
	}
}
