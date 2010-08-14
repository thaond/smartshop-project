package com.appspot.smartshop.ui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class RegisterUserActivity extends MapActivity {
	public static final String TAG = "RegisterUserActivity";
	
	private TextView lblUsername;
	private EditText txtUsername;
	private TextView lblPassword;
	private EditText txtPassword;
	private TextView lblConfirm;
	private EditText txtConfirm;
	private TextView lblFirstName;
	private EditText txtFirstName;
	private TextView lblLastName;
	private EditText txtLastName;
	private TextView lblEmail;
	private EditText txtEmail;
	private TextView lblAddress;
	private EditText txtAddress;
	private TextView lblPhoneNumber;
	private EditText txtPhoneNumber;
	private TextView lblAvatar;
	private EditText txtAvatar;
	private TextView lblBirthday;
	private DatePicker dbBirthday;
	private MapView mapView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_user);
		
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
		
		lblConfirm = (TextView) findViewById(R.id.lblConfirm);
		lblConfirm.setWidth(labelWidth);
		txtConfirm = (EditText) findViewById(R.id.txtConfirm);
		txtConfirm.setWidth(textWidth);
		
		lblFirstName = (TextView) findViewById(R.id.lblFirstName);
		lblFirstName.setWidth(labelWidth);
		txtFirstName = (EditText) findViewById(R.id.txtFirstName);
		txtFirstName.setWidth(textWidth);
		
		lblLastName = (TextView) findViewById(R.id.lblLastName);
		lblLastName.setWidth(labelWidth);
		txtLastName = (EditText) findViewById(R.id.txtLastName);
		txtLastName.setWidth(textWidth);
		
		lblEmail = (TextView) findViewById(R.id.lblEmail);
		lblEmail.setWidth(labelWidth);
		txtEmail = (EditText) findViewById(R.id.txtEmail);
		txtEmail.setWidth(textWidth);
		
		lblAddress = (TextView) findViewById(R.id.lblAddress);
		lblAddress.setWidth(labelWidth);
		txtAddress = (EditText) findViewById(R.id.txtAddress);
		txtAddress.setWidth(textWidth);
		
		lblAvatar = (TextView) findViewById(R.id.lblAvatar);
		lblAvatar.setWidth(labelWidth);
		txtAvatar = (EditText) findViewById(R.id.txtAvatar);
		txtAvatar.setWidth(textWidth);
		
		lblPhoneNumber = (TextView) findViewById(R.id.lblPhoneNumber);
		lblPhoneNumber.setWidth(labelWidth);
		txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
		txtPhoneNumber.setWidth(textWidth);
		
		lblBirthday = (TextView) findViewById(R.id.lblBirthday);
		lblBirthday.setWidth(labelWidth);
		
		// buttons
		Button btnRegister = (Button) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				register();
			}
		});
		
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cancel();
			}
		});
		
		Button btnTagAddressOnMap = (Button) findViewById(R.id.btnTagAddressOnMap);
		btnTagAddressOnMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tagAddressOnMap();
			}
		});
		
		// date picker
		dbBirthday = (DatePicker) findViewById(R.id.dpBirthday);
		
		// mapview
//		mapView = (MapView) findViewById(R.id.mapview);
	}

	protected void tagAddressOnMap() {
	}

	protected void cancel() {
	}

	protected void register() {
		Log.d(TAG, "Birthday = " + dbBirthday.getDayOfMonth() + ", " +  dbBirthday.getMonth()
				+ ", " + dbBirthday.getYear());
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
