package com.appspot.smartshop.ui.user;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
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

public class RegisterUserActivity extends MapActivity {
	public static final String TAG = "RegisterUserActivity";
	
	static final int DATE_DIALOG_ID = 1;
	static final int MAP_DIALOG_ID = 2;
	
	static final int MAX_LOCATION_RESULTS = 5;
	
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
	
	private int mYear;
    private int mMonth;
    private int mDay;
	
	private DatePickerDialog.OnDateSetListener mDateSetListener =
    new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear + 1;
            mDay = dayOfMonth;
            
            // get birthday info
            Log.d(TAG, "birthday: " + mDay + ", " + mMonth + ", " + mYear);
        }
    };

	private AlertDialog.Builder dialogBuilder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_user);
		
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
		
		Button btnBirthday = (Button) findViewById(R.id.btnBirthday);
		btnBirthday.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				chooseBirthday();
			}
		});
	}
	
	protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
            	Calendar cal = Calendar.getInstance();
            	mDay = cal.get(Calendar.DAY_OF_MONTH);
            	mMonth = cal.get(Calendar.MONTH);
            	mYear = cal.get(Calendar.YEAR) - 18;
            	
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
                
            case MAP_DIALOG_ID:
            	String location = txtAddress.getText().toString();
            	
            	Geocoder geocoder = new Geocoder(this);
        		try {
        			Log.d(TAG, "location = " + location);
        			List<Address> addresses = geocoder.getFromLocationName(location, MAX_LOCATION_RESULTS);
        			if (addresses != null && addresses.size() > 0) {
//        				for (Address a : addresses) {
//        					Log.d(TAG, a.toString());
//        				}
        				Address add = addresses.get(0);
        				Log.d(TAG, "lat = " + add.getLatitude() + ", long = " + add.getLongitude());
        				
        				Log.d(TAG, "found " + addresses.size() + " addresses");
        			} else {
        				Log.d(TAG, "No address found");
        			}
        		} catch (IOException e) {
        			if (dialogBuilder == null) {
        				dialogBuilder = new AlertDialog.Builder(this);
        			}
        			dialogBuilder.setMessage(getString(R.string.errGetLocation))
    			       .setCancelable(false)
    			       .setPositiveButton(getString(R.string.lblOk), new DialogInterface.OnClickListener() {
    			           public void onClick(DialogInterface dialog, int id) {
    			        	   dialog.cancel();
    			           }
    			       });
        			
        			return dialogBuilder.create();
        		}
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
		showDialog(MAP_DIALOG_ID);
	}

	protected void cancel() {
	}

	protected void register() {
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
