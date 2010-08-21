package com.appspot.smartshop.ui.user;

import java.util.Calendar;
import java.util.Date;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import com.appspot.smartshop.dom.UserInfo;
import com.appspot.smartshop.map.MapDialog;
import com.appspot.smartshop.map.MapService;
import com.appspot.smartshop.map.MapDialog.UserLocationListener;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.Utils;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;

public class UserActivity extends MapActivity {
	public static final String TAG = "[UserActivity]";
	
	public static final int REGISTER_USER = 0;
	public static final int EDIT_USER_PROFILE = 1;
	public static final int VIEW_USER_PROFILE = 2;
	
	static final int DATE_DIALOG_ID = 0;
	
	private int mode = REGISTER_USER;
	
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
	
	private EditText txtBirthday;

	private int mYear;
    private int mMonth;
    private int mDay;
    
    private double lat;
    private double lng;
    
    private UserInfo userInfo = null;
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
    new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                int dayOfMonth) {
            mYear = year - 1900;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            
            txtBirthday.setText(Global.df.format(new Date(mYear, mMonth, mDay)));
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user);
		
		// label width
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		int width = display.getWidth(); 
		int labelWidth = (int) (width * 0.4);
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
		txtBirthday = (EditText) findViewById(R.id.txtBirthday);
		txtBirthday.setWidth(textWidth);
		
		txtBirthday.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
		
		// setup data for text field if in edit/view user profile mode
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mode = EDIT_USER_PROFILE;
			userInfo = (UserInfo) bundle.get(Global.USER_INFO);
			
			txtUsername.setText(userInfo.username);
			txtPassword.setText(userInfo.password);
			txtConfirm.setText(userInfo.password);
			txtFirstName.setText(userInfo.first_name);
			txtLastName.setText(userInfo.last_name);
			txtEmail.setText(userInfo.email);
			txtAddress.setText(userInfo.address);
			txtPhoneNumber.setText(userInfo.phone);
			txtBirthday.setText(Global.df.format(userInfo.birthday));
			// TODO (condorhero01): display avatar
			
			// TODO (condohero01): some fields of user info must be uneditable
			txtUsername.setFilters(Utils.uneditableInputFilters);
			txtEmail.setFilters(Utils.uneditableInputFilters);
			txtPhoneNumber.setFilters(Utils.uneditableInputFilters);
			
			// not allow to edit text when in VIEW_USER_PROFILE MODE
			Boolean canEditUserProfile = bundle.getBoolean(Global.CAN_EDIT_USER_PROFILE);
			if (!(canEditUserProfile != null && canEditUserProfile == true)) {
				mode = VIEW_USER_PROFILE;
				txtUsername.setEnabled(false);
			}
		} else {
			
		}
		
		// buttons
		Button btnRegister = (Button) findViewById(R.id.btnRegister);
		if (mode != REGISTER_USER) {
			btnRegister.setText(getString(R.string.lblUpdate));
		}
		btnRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (mode) {
				case REGISTER_USER:
					registerNewUser();
					break;
					
				case EDIT_USER_PROFILE:
					editUserProfile();
					break;
					
				case VIEW_USER_PROFILE:
					break;
				}
			}
		});
		
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		Button btnTagAddressOnMap = (Button) findViewById(R.id.btnTagAddressOnMap);
		btnTagAddressOnMap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tagAddressOnMap();
			}
		});
	}
	
	private void collectUserInfo() {
		userInfo = new UserInfo();
		
		// update user info
		userInfo.username = txtUsername.getText().toString();
		userInfo.password = txtPassword.getText().toString();	// TODO (condorhero01): hash MD5 pass?
		userInfo.first_name = txtFirstName.getText().toString();
		userInfo.last_name = txtLastName.getText().toString();
		userInfo.email = txtEmail.getText().toString();
		userInfo.address = txtAddress.getText().toString();
		userInfo.phone = txtPhoneNumber.getText().toString();
		userInfo.birthday = new Date(mYear, mMonth, mDay);
		Log.d(TAG, "birthday = " + userInfo.birthday);
		userInfo.lat = lat;
		userInfo.lng = lng;
		// TODO (condorhero01): get avatar link of user
	}
	
	protected void registerNewUser() {
		collectUserInfo();
		
		// TODO (condorhero01): request to server to register new user
	}

	protected void editUserProfile() {
		collectUserInfo();
		
		// TODO (condorhero01): request to server to update user info
	}

	protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
            	Calendar cal = Calendar.getInstance();
            	mDay = cal.get(Calendar.DAY_OF_MONTH);
            	mMonth = cal.get(Calendar.MONTH);
            	mYear = cal.get(Calendar.YEAR) - 18;
            	
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
        }
        
        return null;
    }
	
	@Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
        }
    }

	protected void chooseBirthday() {
		showDialog(DATE_DIALOG_ID);
	}

	protected void tagAddressOnMap() {
		String location = txtAddress.getText().toString();
    	
    	MapDialog.createLocationDialog(this, MapService.locationToGeopoint(location), 
    			new UserLocationListener() {
					
					@Override
					public void processUserLocation(GeoPoint point) {
						if (mode == REGISTER_USER) {
							if (point != null) {
								lat = point.getLatitudeE6();
								lng = point.getLongitudeE6();
							}
							Log.d(TAG, "user location = " + point);
						}
					}
				}).show();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
