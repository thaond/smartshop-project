package com.appspot.smartshop.ui.user;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import org.anddev.android.filebrowser.AndroidFileBrowser;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.UserInfo;
import com.appspot.smartshop.map.MapDialog;
import com.appspot.smartshop.map.MapService;
import com.appspot.smartshop.map.MapDialog.UserLocationListener;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.StringUtils;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;
import com.appspot.smartshop.utils.capture.ImageCaptureActivity;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;

public class RegisterUserActivity extends MapActivity {
	public static final String TAG = "[RegisterUserActivity]";

	private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//	private static final String PATTERN_LONGER_6 = "^[_A-Za-z0-9-]{6}[_A-Za-z0-9-]*";

	private static final int FILE_BROWSER_ID = 0;
	private static final int IMAGE_CAPTURE_ID = 1;

	static final int DATE_DIALOG_ID = 0;

	private TextView lblUsername;
	private EditText txtUsername;
	private TextView lblOldPassword;
	private EditText txtOldPassword;
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
	private ImageView imgAvatar;
	private EditText txtAvatar;
	private TextView lblBirthday;
	private EditText txtBirthday;

	private int mYear;
	private int mMonth;
	private int mDay;

	private double lat;
	private double lng;

	private UserInfo userInfo = null;

	private InputStream inputStreamAvatar;
	private String fileName;

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

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
		int width = Utils.getScreenWidth();
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
		txtAvatar.setWidth(labelWidth);
		
		imgAvatar = (ImageView) findViewById(R.id.imgAvatar);

		lblPhoneNumber = (TextView) findViewById(R.id.lblPhoneNumber);
		lblPhoneNumber.setWidth(labelWidth);
		txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
		txtPhoneNumber.setWidth(textWidth);

		lblBirthday = (TextView) findViewById(R.id.lblBirthday);
		lblBirthday.setWidth(labelWidth);
		txtBirthday = (EditText) findViewById(R.id.txtBirthday);
		txtBirthday.setWidth(textWidth);
		txtBirthday.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					showDialog(DATE_DIALOG_ID);
					return true;
				}
				
				return false;
			}
		});

		lblOldPassword = (TextView) findViewById(R.id.lblOldPassword);
		lblOldPassword.setWidth(labelWidth);
		txtOldPassword = (EditText) findViewById(R.id.txtOldPassword);
		txtOldPassword.setWidth(textWidth);

		lblOldPassword.setVisibility(View.GONE);
		txtOldPassword.setVisibility(View.GONE);

		/********************************** Buttons ********************************/
		// Register button
		Button btnRegister = (Button) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				registerNewUser();
			}
		});

		// Cancel button
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// Tag address on map button
		Button btnTagAddressOnMap = (Button) findViewById(R.id.btnTagAddressOnMap);
		btnTagAddressOnMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tagAddressOnMap();
			}
		});
		
		// Browser sdcard button
		Button btnBrowser = (Button) findViewById(R.id.btnBrowser);
		btnBrowser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (inputStreamAvatar != null) {
					DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							startFileBrowserActivity();
						}
					};
					DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
						}
					};

					// Show an Alert with the ButtonListeners we created
					AlertDialog ad = new AlertDialog.Builder(RegisterUserActivity.this)
							.setTitle(getString(R.string.notice))
							.setMessage(
									getString(R.string.do_you_want_to_change_avatar))
							.setPositiveButton(getString(R.string.yes),
									okButtonListener).setNegativeButton(
									getString(R.string.no),
									cancelButtonListener).create();
					ad.show();
				} else {
					startFileBrowserActivity();
				}
			}
		});

		// photo button
		Button btnPhoto = (Button) findViewById(R.id.btnPhoto);
		btnPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (inputStreamAvatar != null) {
					DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							startImageCaptureActivity();
						}
					};
					DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
						}
					};

					// Show an Alert with the ButtonListeners we created
					AlertDialog ad = new AlertDialog.Builder(RegisterUserActivity.this)
							.setTitle(getString(R.string.notice))
							.setMessage(
									getString(R.string.do_you_want_to_change_avatar))
							.setPositiveButton(getString(R.string.yes),
									okButtonListener).setNegativeButton(
									getString(R.string.no),
									cancelButtonListener).create();
					ad.show();
				} else {
					startImageCaptureActivity();
				}
			}
		});
		
		Button btnHome = (Button) findViewById(R.id.btnHome);
		btnHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Utils.returnHomeActivity(RegisterUserActivity.this);
			}
		});
		
		Button btnBack = (Button) findViewById(R.id.btnBack);
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void startFileBrowserActivity(){
		Intent intent = new Intent(RegisterUserActivity.this,
				AndroidFileBrowser.class);
		intent.setAction(Global.FILE_BROWSER_ACTIVITY);
		intent.putExtra(Global.FILTER_FILE, Global.IMAGE_FILTER_EXTENSION);
		startActivityForResult(intent, FILE_BROWSER_ID);
	}
	
	private void startImageCaptureActivity(){
		Intent intent = new Intent(RegisterUserActivity.this,
				ImageCaptureActivity.class);
		intent.setAction(Global.IMAGE_CAPURE_ACTIVITY);
		startActivityForResult(intent, IMAGE_CAPTURE_ID);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println("Result");
		switch (requestCode) {
		case FILE_BROWSER_ID:
			if (resultCode == Activity.RESULT_OK) {
				if (data != null) {
					File file = (File) data
							.getSerializableExtra(Global.FILE_INTENT_ID);
					if (file != null) {
						try {
							inputStreamAvatar = new FileInputStream(file);
							fileName = file.getName();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
					
					txtAvatar.setText(fileName);
					Drawable drawable = Drawable.createFromStream(inputStreamAvatar, "avatar");
					imgAvatar.setBackgroundDrawable(drawable);
				}

			}
			break;

		case IMAGE_CAPTURE_ID:
			// Return array of byte
			if (resultCode == RESULT_OK) {
				if (data != null) {
					byte[] arrBytes = data
							.getByteArrayExtra(Global.BYTE_ARRAY_INTENT_ID);
					if (arrBytes != null) {
						inputStreamAvatar = new ByteArrayInputStream(arrBytes);
						fileName = Global.dfTimeStamp.format(new Date())
								+ ".jpg";
						
						// TODO display image from sdcard
						txtAvatar.setText(fileName);
						Drawable drawable = Drawable.createFromStream(inputStreamAvatar, "avatar");
						imgAvatar.setBackgroundDrawable(drawable);
					}
				}
			}
			break;

		default:
			break;
		}
	} 

	/**
	 * 
	 * @return Server Response
	 */
	private String doFileUpload() {
		DataOutputStream dos;
		HttpURLConnection conn = null;
		dos = null;
		DataInputStream inStream = null;

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		int bytesRead, bytesAvailable, bufferSize;

		byte[] buffer;
		int maxBufferSize = 6 * 1024 * 1024;

		try {
			// ------------------ CLIENT REQUEST

			// open a URL connection to the Servlet
			String urlString = String.format(URLConstant.URL_UPLOAD_AVATAR,
					userInfo.username).replaceAll(" ", "+");
			URL url = new URL(urlString);
			Log.w("Upload", urlString);

			// Open a HTTP connection to the URL
			conn = (HttpURLConnection) url.openConnection();

			// Allow Inputs
			conn.setDoInput(true);

			// Allow Outputs
			conn.setDoOutput(true); 

			// Don't use a cached copy.
			conn.setUseCaches(false);

			// Use a post method.
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos
					.writeBytes("Content-Disposition:\"form-data\"; name=\"uploadedfile\";filename=\""
							+ fileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);
			Log.e("MediaPlayer", "Headers are written");

			// create a buffer of maximum size
			bytesAvailable = inputStreamAvatar.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form...
			bytesRead = inputStreamAvatar.read(buffer, 0, bufferSize); 

			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = inputStreamAvatar.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = inputStreamAvatar.read(buffer, 0, bufferSize);
			}

			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// close streams
			Log.e(TAG, "File is written");
			inputStreamAvatar.close();
			dos.flush();

		} catch (MalformedURLException ex) {
			Log.e(TAG, "error: " + ex.getMessage(), ex);
		}

		catch (IOException ioe) {
			Log.e(TAG, "error: " + ioe.getMessage(), ioe);
		}

		// ------------------ read the SERVER RESPONSE
		try {
			inStream = new DataInputStream(conn.getInputStream());
			String str;
			StringBuilder strBuilder = new StringBuilder();

			while ((str = inStream.readLine()) != null) {
				strBuilder.append(str);
			}

			Log.d(TAG, "Host Image response: " + strBuilder.toString());
			inStream.close();

			return strBuilder.toString();
		} catch (IOException ioex) {
			Log.e(TAG, "error: " + ioex.getMessage(), ioex);
		}

		if (dos != null)
			try {
				dos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		return null;
	}

	private void collectUserInfo() {
		userInfo = new UserInfo();
		// update user info
		userInfo.username = txtUsername.getText().toString();
		userInfo.oldPassword = txtOldPassword.getText().toString();
		// md5 is served in server
		userInfo.password = txtPassword.getText().toString();
		userInfo.first_name = txtFirstName.getText().toString();
		userInfo.last_name = txtLastName.getText().toString();
		userInfo.email = txtEmail.getText().toString();
		userInfo.address = txtAddress.getText().toString();
		userInfo.phone = txtPhoneNumber.getText().toString();
		userInfo.birthday = new Date(mYear, mMonth, mDay);
		userInfo.lat = lat;
		userInfo.lng = lng;
	}

	protected void registerNewUser() {
		// some fields are invalid
		String errorMsg = getErrorMessage();
		Log.d(TAG, "[validate form] " + errorMsg);
		if (errorMsg != null) {
			//Show dialog
			Utils.createOKDialog(this, getString(R.string.error), errorMsg, null);
			return;
		}
		
		collectUserInfo();

		// TODO (vo.mita.ov): upload avatar of user
		if (StringUtils.isEmptyOrNull(userInfo.avatarLink) && inputStreamAvatar != null) {
			userInfo.avatarLink = URLConstant.HOST + "/image_host/avatar/NT" + (int)(Math.random()*9+1) + ".jpg";//Just testing
			/*
			//String response = doFileUpload();
			//Parsing image upload response
			String response = "0:" + URLConstant.HOST + "/image_host/avatar/NT" + (int)(Math.random()*9+1) + ".jpg";

			Log.e("Upload", response);
			String[] data = response.split(":");
			int errCode = -1;
			try {
				errCode = Integer.parseInt(data[0]);
			} catch (Exception e) {
			}
			switch (errCode) {
			case 0:
				// Upload successfully
				userInfo.avatarLink = data[1];
				break;

			case 1:
				Toast.makeText(this, data[1], Toast.LENGTH_SHORT);
				break;

			default:
				DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						registerNewUser();
					}
				};
				DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						inputStreamAvatar = null;
						fileName = null;
					}
				};

				// Show an Alert with the ButtonListeners we created
				AlertDialog ad = new AlertDialog.Builder(this).setTitle(
						getString(R.string.notice)).setMessage(
						getString(R.string.cant_upload_image_try_again))
						.setPositiveButton(getString(R.string.yes),
								okButtonListener).setNegativeButton(
								getString(R.string.no), cancelButtonListener)
						.create();
				ad.show();
				break;
			}
			*/
		}
		
		sendRegisterRequest();
	}
	
	private void sendRegisterRequest(){
		// register user
		RestClient.postData(URLConstant.REGISTER, Global.gsonDateWithoutHour
				.toJson(userInfo), new JSONParser() {

			@Override
			public void onFailure(String message) {
				Log.e(TAG, message);
			}

			@Override
			public void onSuccess(JSONObject json) throws JSONException {
				int errCode = json.getInt("errCode");
				String message = json.getString("message");
				switch (errCode) {
				case Global.SUCCESS:
					Toast.makeText(RegisterUserActivity.this, message,
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(RegisterUserActivity.this,
							LoginActivity.class);
					startActivity(intent);

					break;

				case Global.ERROR:
					Toast.makeText(RegisterUserActivity.this, message,
							Toast.LENGTH_SHORT).show();
					Global.isLogin = false;

				default: 
					Toast.makeText(RegisterUserActivity.this,
							getString(R.string.err_register_message),
							Toast.LENGTH_SHORT).show();
					Global.isLogin = false;
					break;
				}
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

			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
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
		GeoPoint point = null;
		
		// check location
		String location = txtAddress.getText().toString();
		if (location == null || location.trim().equals("")) {
			Toast.makeText(
					this,
					getString(R.string.warn_must_enter_address),
					Toast.LENGTH_SHORT
			).show();
			return;
		}
		point = MapService.locationToGeopoint(location);

		// show dialog map
		MapDialog.createLocationDialog(this, point, new UserLocationListener() {

			@Override
			public void processUserLocation(GeoPoint point) {
				if (point != null) {
					lat = point.getLatitudeE6();
					lng = point.getLongitudeE6();
				}
				Log.d(TAG, "user location = " + point);
			}
		}).show();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private String getErrorMessage() {
		String result = null;

		if (StringUtils.isEmptyOrNull(txtUsername.getText().toString())) {
			result = getString(R.string.username_invalid);
		} 
		else if (StringUtils.isEmptyOrNull(txtPassword.getText().toString())) {
			result = getString(R.string.password_invalid);
		} 
		else if (StringUtils.isEmptyOrNull(txtConfirm.getText().toString())
				|| !txtPassword.getText().toString().equals(txtConfirm.getText().toString())) {
			result = getString(R.string.password_not_match);
		} 
		else if (StringUtils.isEmptyOrNull(txtFirstName.getText().toString())) {
			result = getString(R.string.first_name_invalid);
		} 
		else if (StringUtils.isEmptyOrNull(txtEmail.getText().toString())
				|| !Pattern.matches(PATTERN_EMAIL, txtEmail.getText().toString())) {
			result = getString(R.string.email_invalid);
		}

//		if (result != null) {
//			Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
//		}
		
		return result;
	}
}

