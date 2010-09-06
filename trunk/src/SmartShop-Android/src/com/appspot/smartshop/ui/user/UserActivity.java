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

import sv.skunkworks.showtimes.lib.asynchronous.HttpService;
import sv.skunkworks.showtimes.lib.asynchronous.ServiceCallback;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.gson.JsonObject;

public class UserActivity extends MapActivity {
	public static final String TAG = "[UserActivity]";

	private static final String PATTERN_EMAIL = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String PATTERN_LONGER_6 = "^[_A-Za-z0-9-]{6}[_A-Za-z0-9-]*";

	private static final int FILE_BROWSER_ID = 0;
	private static final int IMAGE_CAPTURE_ID = 1;

	public static final int REGISTER_USER = 0;
	public static final int EDIT_USER_PROFILE = 1;
	public static final int VIEW_USER_PROFILE = 2;

	static final int DATE_DIALOG_ID = 0;

	private int mode = REGISTER_USER;

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
	// private EditText txtAvatar;
	private TextView lblBirthday;
	private Button btnPhoto;
	private Button btnBrowser;

	private EditText txtBirthday;

	private int mYear;
	private int mMonth;
	private int mDay;

	private double lat;
	private double lng;

	private UserInfo userInfo = null;

	private String[] imageFilter = new String[] { "jpg", "jpe", "jpeg", "jpg",
			"bmp", "ico", "gif", "png" };
	private InputStream inputStreamAvatar;
	private String fileName;

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year - 1900;
			mMonth = monthOfYear;
			mDay = dayOfMonth;

			txtBirthday
					.setText(Global.df.format(new Date(mYear, mMonth, mDay)));
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

		// lblAvatar = (TextView) findViewById(R.id.lblAvatar);
		// lblAvatar.setWidth(labelWidth);
		// txtAvatar = (EditText) findViewById(R.id.txtAvatar);
		// txtAvatar.setWidth(textWidth);

		lblPhoneNumber = (TextView) findViewById(R.id.lblPhoneNumber);
		lblPhoneNumber.setWidth(labelWidth);
		txtPhoneNumber = (EditText) findViewById(R.id.txtPhoneNumber);
		txtPhoneNumber.setWidth(textWidth);

		lblBirthday = (TextView) findViewById(R.id.lblBirthday);
		lblBirthday.setWidth(labelWidth);
		txtBirthday = (EditText) findViewById(R.id.txtBirthday);
		txtBirthday.setWidth(textWidth);

		btnPhoto = (Button) findViewById(R.id.btnPhoto);
		btnBrowser = (Button) findViewById(R.id.btnBrowser);

		// setup data for text field if in edit/view user profile mode
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mode = EDIT_USER_PROFILE;
			userInfo = (UserInfo) bundle.get(Global.USER_INFO);

			// fill user info to form
			txtUsername.setText(userInfo.username);
			txtPassword.setText(userInfo.password);
			txtConfirm.setText(userInfo.password);
			txtFirstName.setText(userInfo.first_name);
			txtLastName.setText(userInfo.last_name);
			txtEmail.setText(userInfo.email);
			txtAddress.setText(userInfo.address);
			txtPhoneNumber.setText(userInfo.phone);
			txtBirthday.setText(Global.df.format(userInfo.birthday));
			// TODO (vo.mita.ov): display avatar of user

			// some fields of user info must be uneditable
			txtUsername.setFilters(Global.uneditableInputFilters);
			txtEmail.setFilters(Global.uneditableInputFilters);
			txtPhoneNumber.setFilters(Global.uneditableInputFilters);

			// not allow to edit text when in VIEW_USER_PROFILE MODE
			Boolean canEditUserProfile = bundle
					.getBoolean(Global.CAN_EDIT_USER_PROFILE);
			if (!(canEditUserProfile != null && canEditUserProfile == true)) {
				Log.d(TAG, "view user profile");
				mode = VIEW_USER_PROFILE;
				txtPassword.setVisibility(View.GONE);
				txtConfirm.setVisibility(View.GONE);
				lblPassword.setVisibility(View.GONE);
				lblConfirm.setVisibility(View.GONE);
				
				txtFirstName.setFilters(Global.uneditableInputFilters);
				txtLastName.setFilters(Global.uneditableInputFilters);
				txtAddress.setFilters(Global.uneditableInputFilters);
			} else {
				Log.d(TAG, "edit user profile");
			}

		}

		/********************************** Listeners *******************************/
		// allow change birthday in register and view profile mode
		if (mode != VIEW_USER_PROFILE) {
			txtBirthday.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showDialog(DATE_DIALOG_ID);
				}
			});

			btnBrowser.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (inputStreamAvatar != null) {
						DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// TODO AndroidFileBrowser activity
								Intent intent = new Intent(UserActivity.this,
										AndroidFileBrowser.class);
								intent
										.putExtra(Global.FILTER_FILE,
												imageFilter);
								intent.setAction(Global.FILE_BROWSER_ACTIVITY);
								startActivityForResult(intent, FILE_BROWSER_ID);
							}
						};
						DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							}
						};

						// Show an Alert with the ButtonListeners we created
						AlertDialog ad = new AlertDialog.Builder(
								UserActivity.this)
								.setTitle(getString(R.string.notice))
								.setMessage(
										getString(R.string.do_you_want_to_change_avatar))
								.setPositiveButton(getString(R.string.yes),
										okButtonListener).setNegativeButton(
										getString(R.string.no),
										cancelButtonListener).create();
						ad.show();
					} else {
						Intent intent = new Intent(UserActivity.this,
								AndroidFileBrowser.class);
						intent.setAction(Global.FILE_BROWSER_ACTIVITY);
						intent.putExtra(Global.FILTER_FILE, imageFilter);
						startActivityForResult(intent, FILE_BROWSER_ID);
					}
				}
			});

			btnPhoto.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (inputStreamAvatar != null) {
						DialogInterface.OnClickListener okButtonListener = new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								Intent intent = new Intent(UserActivity.this,
										ImageCaptureActivity.class);
								intent.setAction(Global.IMAGE_CAPURE_ACTIVITY);
								startActivityForResult(intent, IMAGE_CAPTURE_ID);
							}
						};
						DialogInterface.OnClickListener cancelButtonListener = new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							}
						};

						// Show an Alert with the ButtonListeners we created
						AlertDialog ad = new AlertDialog.Builder(
								UserActivity.this)
								.setTitle(getString(R.string.notice))
								.setMessage(
										getString(R.string.do_you_want_to_change_avatar))
								.setPositiveButton(getString(R.string.yes),
										okButtonListener).setNegativeButton(
										getString(R.string.no),
										cancelButtonListener).create();
						ad.show();
					} else {
						Intent intent = new Intent(UserActivity.this,
								ImageCaptureActivity.class);
						intent.setAction(Global.IMAGE_CAPURE_ACTIVITY);
						startActivityForResult(intent, IMAGE_CAPTURE_ID);
					}
				}
			});
		} else {
			btnBrowser.setVisibility(TextView.GONE);
			btnPhoto.setVisibility(TextView.GONE);
		}

		if (mode != EDIT_USER_PROFILE) {
			lblOldPassword = (TextView) findViewById(R.id.lblOldPassword);
			lblOldPassword.setVisibility(TextView.GONE);
			txtOldPassword = (EditText) findViewById(R.id.txtOldPassword);
			txtOldPassword.setVisibility(TextView.GONE);
		}

		// input value
		txtUsername.setFilters(Global.usernameInputFilters);
		txtFirstName.setFilters(Global.usernameInputFilters);
		txtLastName.setFilters(Global.usernameInputFilters);

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
		if (mode != REGISTER_USER) {
			btnTagAddressOnMap.setText(getString(R.string.btnViewAddressOnMap));
		}
		btnTagAddressOnMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				tagAddressOnMap();
			}
		});
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
		int maxBufferSize = 1 * 1024 * 1024;

		try {
			// ------------------ CLIENT REQUEST

			// open a URL connection to the Servlet
			URL url = new URL(String.format(URLConstant.HOST_IMG,
					userInfo.username));

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
		// TODO (vo.mita.ov): get avatar link of user
	}

	protected void registerNewUser() {
		// if (StringUtils.isEmptyOrNull(err = getErrorMessage())) {
		collectUserInfo();

		String response = doFileUpload();

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

//		Log.d(TAG, Global.gsonDateWithoutHour.toJson(userInfo));
		RestClient.postData(URLConstant.REGISTER, Global.gsonDateWithoutHour
				.toJson(userInfo), new JSONParser() {

			@Override
			public void onFailure(String message) {
//				Log.e(TAG, message);
			}

			@Override
			public void onSuccess(JSONObject json) throws JSONException {
//				int errCode = json.getInt("errCode");
//				String message = json.getString("message");
//				switch (errCode) {
//				case Global.SUCCESS:
//					Toast.makeText(UserActivity.this, message,
//							Toast.LENGTH_SHORT).show();
//					Global.intent.setAction(Global.LOGIN_ACTIVITY);
//					startActivity(Global.intent);
//
//					break;
//
//				default:
//					Toast.makeText(UserActivity.this, message,
//							Toast.LENGTH_SHORT).show();
//					Global.isLogin = false;
//					break;
//				}
			}
		});
	}

	protected void editUserProfile() {
		String err = "";
		if (StringUtils.isEmptyOrNull(err = getErrorMessage())) {
			collectUserInfo();
			HttpService.postResource(URLConstant.EDIT_PROFILE,
					Global.gsonDateWithoutHour.toJson(userInfo), true,
					new ServiceCallback(this) {

						@Override
						public void onSuccess(JsonObject json) {
							int errCode = Integer.parseInt(json
									.getAsString("errCode"));
							String message = json.getAsString("message");
							switch (errCode) {
							case Global.SUCCESS:
								Toast.makeText(UserActivity.this, message,
										Toast.LENGTH_SHORT).show();
								break;

							default:
								Toast.makeText(UserActivity.this, message,
										Toast.LENGTH_SHORT).show();
								break;
							}
						}
					});
		} else {
			Toast.makeText(UserActivity.this, err, Toast.LENGTH_SHORT).show();
		}
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			if (mode == REGISTER_USER) {
				Calendar cal = Calendar.getInstance();
				mDay = cal.get(Calendar.DAY_OF_MONTH);
				mMonth = cal.get(Calendar.MONTH);
				mYear = cal.get(Calendar.YEAR) - 18;
			} else {
				mDay = userInfo.birthday.getDate();
				mMonth = userInfo.birthday.getMonth();
				mYear = userInfo.birthday.getYear() + 1900;
			}

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
		if (Global.isLogin) {
			point = new GeoPoint((int) (Global.userInfo.lat * 1E6), (int) (Global.userInfo.lng * 1E6));
		} else if (userInfo != null) {
			point = new GeoPoint((int) (userInfo.lat * 1E6), (int) (userInfo.lng * 1E6));
		} else {
			String location = txtAddress.getText().toString();
			if (location == null || location.trim().equals("")) {
				Toast.makeText(this, getString(R.string.warn_must_enter_address), 
						Toast.LENGTH_SHORT).show();
				return;
			}
			point = MapService.locationToGeopoint(location);
		}
		Log.d(TAG, "user point = " + point);

		MapDialog.createLocationDialog(this, point,
				new UserLocationListener() {

					@Override
					public void processUserLocation(GeoPoint point) {
						if (mode == REGISTER_USER || mode == EDIT_USER_PROFILE) {
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

	private String getErrorMessage() {
		String result = null;

		if (StringUtils.isEmptyOrNull(txtUsername.getText().toString())
				|| !Pattern.matches(PATTERN_LONGER_6, txtUsername.getText()
						.toString()))
			result = getString(R.string.username_longer_6_chars);
		else if (StringUtils.isEmptyOrNull(txtPassword.getText().toString())
				|| !Pattern.matches(PATTERN_LONGER_6, txtPassword.getText()
						.toString()))
			result = getString(R.string.password_longer_6_chars);
		else if (StringUtils.isEmptyOrNull(txtConfirm.getText().toString())
				|| !txtPassword.getText().toString().equals(
						txtConfirm.getText().toString()))
			result = getString(R.string.password_not_match);
		else if (StringUtils.isEmptyOrNull(txtFirstName.getText().toString())
				|| !Pattern.matches(PATTERN_LONGER_6, txtFirstName.getText()
						.toString()))
			result = getString(R.string.first_name_longer_6_chars);
		else if (StringUtils.isEmptyOrNull(txtEmail.getText().toString())
				|| !Pattern.matches(PATTERN_EMAIL, txtEmail.getText()
						.toString()))
			result = getString(R.string.email_invalid);

		return result;
	}
}
