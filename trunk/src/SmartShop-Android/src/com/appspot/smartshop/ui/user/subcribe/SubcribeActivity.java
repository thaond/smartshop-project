package com.appspot.smartshop.ui.user.subcribe;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.UserSubcribeProduct;
import com.appspot.smartshop.map.LocationOverlay;
import com.appspot.smartshop.map.MapService;
import com.appspot.smartshop.map.MyLocationCallback;
import com.appspot.smartshop.map.MyLocationListener;
import com.appspot.smartshop.ui.BaseUIActivity;
import com.appspot.smartshop.utils.CategoriesDialog;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.StringUtils;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.CategoriesDialog.CategoriesDialogListener;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class SubcribeActivity extends MapActivity {

	public static final String TAG = "[CreateSubcribeActivity]";
	public static final String POST_PARAM = "{lat:%f,lng:%f,distance:%f,limit:0,q:%s}";
	// radius of earth = 6400km
	public static final float LATITUDE_TO_METER = 111701f; // 180 lat
	public static final float LONGTITUDE_TO_METER = 111701f; // 360 long

	private MapView mapView;
	private EditText txtSearch;
	private EditText txtRadius;

	private MapController mapController;

	private static final int SHOW_LOCATION = 1;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			System.out.println("update map");
			mapView.invalidate();
		}
	};

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.create_subcribe);

		BaseUIActivity.initHeader(this);

		// TODO for test
		Global.application = this;

		// text view
		txtSearch = (EditText) findViewById(R.id.txtSearch);
		txtRadius = (EditText) findViewById(R.id.txtRadius);

		// map view
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		// overlay
		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		locationOverlay = new LocationOverlay();
		listOfOverlays.add(locationOverlay);

		// controller
		mapController = mapView.getController();
		mapController.setZoom(16);

		// location listener
		myLocationListener = new MyLocationListener(this,
				new MyLocationCallback() {

					@Override
					public void onSuccess(GeoPoint point) {
						if (point != null) {
							mapController.setCenter(point);
							locationOverlay.point = point;

							// update map
							Message message = handler.obtainMessage();
							message.obj = point;
							message.what = SHOW_LOCATION;
							handler.sendMessage(message);
						}
					}
				});
		// TODO stop find current location on start SubcribeActivity
		// myLocationListener.findCurrentLocation();

		// category button
		Button btnCategory = (Button) findViewById(R.id.btnCategories);
		btnCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CategoriesDialog.showCategoriesDialog(SubcribeActivity.this,
						new CategoriesDialogListener() {

							@Override
							public void onCategoriesDialogClose(
									Set<String> categories) {
								if (editMode) {
									newSubcribe.categoryList = new LinkedList<String>(
											categories);
								} else {
									subcribe.categoryList = new LinkedList<String>(
											categories);
								}
							}
						});
			}
		});

		// checkboxes
		chSms = (CheckBox) findViewById(R.id.chSms);
		chEmail = (CheckBox) findViewById(R.id.chEmail);
		chPush = (CheckBox) findViewById(R.id.chPush);
		chActive = (CheckBox) findViewById(R.id.chActive);

		// date
		TextView txtDate = (TextView) findViewById(R.id.txtDate);

		/****************************** Get subcribe from intent *****************/
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			editMode = true;

			subcribe = (UserSubcribeProduct) bundle.get(Global.SUBCRIBE_INFO);

			txtSearch.setText(subcribe.q);
			txtRadius.setText(subcribe.radius + "");
			txtDate.setText(Global.dfFull.format(subcribe.date));
			chActive.setChecked(subcribe.isActive);

			CategoriesDialog.selectedCat = subcribe.categoryList;

			if (subcribe.isSendMail())
				chEmail.setChecked(true);
			if (subcribe.isSendSMS())
				chSms.setChecked(true);
			if (subcribe.isPushNotification())
				chPush.setChecked(true);

		} else {
			chActive.setVisibility(View.GONE);
			txtDate.setVisibility(View.GONE);
		}

		// subcribe button
		Button btnSubcribe = (Button) findViewById(R.id.btnSubcribe);
		if (editMode) {
			btnSubcribe.setText(getString(R.string.subcribe_update));
		}
		btnSubcribe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (editMode) {
					updateSubcribe();
				} else {
					subcribeProducts();
				}
			}
		});
	}

	private boolean editMode = false;

	protected void updateSubcribe() {
		newSubcribe = new UserSubcribeProduct();

		// get subcribe query
		String query = txtSearch.getText().toString();
		newSubcribe.q = query;

		// get subcribe radius
		try {
			if (!StringUtils.isEmptyOrNull(txtRadius.getText().toString())) {
				radius = Double.parseDouble(txtRadius.getText().toString());
			} else {
				radius = 0;
			}
		} catch (NumberFormatException ex) {
			Toast.makeText(this,
					getString(R.string.errorSearchProductsOnMapRadius),
					Toast.LENGTH_LONG).show();
			return;
		}
		newSubcribe.radius = radius;

		// get subcribe lat, long
		if (locationOverlay.point != null) {
			// Toast.makeText(this,
			// getString(R.string.missing_subcribe_location),
			// Toast.LENGTH_SHORT).show();
			// return;
			newSubcribe.lat = (double) locationOverlay.point.getLatitudeE6() / 1E6;
			newSubcribe.lng = (double) locationOverlay.point.getLongitudeE6() / 1E6;
		}

		// other info of subcribe
		newSubcribe.date = subcribe.date;
		newSubcribe.isActive = chActive.isChecked();
		newSubcribe.userName = Global.userInfo.username;
		boolean email = chEmail.isChecked();
		boolean sms = chSms.isChecked();
		boolean push = chPush.isChecked();

		int type = 0;
		if (email)
			type = (int) Math.pow(2, UserSubcribeProduct.EMAIL);
		if (sms)
			type = type | (int) Math.pow(2, UserSubcribeProduct.SMS);
		if (push)
			type = type
					| (int) Math.pow(2, UserSubcribeProduct.PUSH_NOTIFICATION);

		// if (email && sms) {
		// newSubcribe.type_notification =
		// UserSubcribeProduct.EMAIL_AND_SMS_NOTIFICATION;
		// } else if (email) {
		// newSubcribe.type_notification =
		// UserSubcribeProduct.EMAIL_NOTIFICATION;
		// } else if (sms) {
		// newSubcribe.type_notification =
		// UserSubcribeProduct.SMS_NOTIFICCATION;
		// }

		// create new subcribe
		task = new SimpleAsyncTask(this, new DataLoader() {

			@Override
			public void updateUI() {
			}

			@Override
			public void loadData() {
				String param = Global.gsonWithHour.toJson(newSubcribe,
						UserSubcribeProduct.class);

				String url = String.format(URLConstant.EDIT_SUBCRIBE, Global.getSession());
				RestClient.postData(url, param, new JSONParser() {

							@Override
							public void onSuccess(JSONObject json)
									throws JSONException {
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

	@Override
	protected void onStop() {
		super.onStop();
		CategoriesDialog.selectedCat.clear();
	}

	private UserSubcribeProduct subcribe = new UserSubcribeProduct();
	private SimpleAsyncTask task;
	private double radius;

	protected void subcribeProducts() {
		// get subcribe query
		String query = txtSearch.getText().toString();
		subcribe.q = query;

		// get subcribe radius
		try {
			radius = Double.parseDouble(txtRadius.getText().toString());
		} catch (NumberFormatException ex) {
			Toast.makeText(this,
					getString(R.string.errorSearchProductsOnMapRadius),
					Toast.LENGTH_LONG).show();
			return;
		}
		subcribe.radius = radius;

		// get subcribe lat, long
		if (locationOverlay.point == null) {
			Toast.makeText(this, getString(R.string.missing_subcribe_location),
					Toast.LENGTH_SHORT).show();
			return;
		}
		subcribe.lat = (double) locationOverlay.point.getLatitudeE6() / 1E6;
		subcribe.lng = (double) locationOverlay.point.getLongitudeE6() / 1E6;

		// other info of subcribe
		subcribe.date = new Date();
		subcribe.isActive = true;
		subcribe.userName = Global.userInfo.username;

		boolean email = chEmail.isChecked();
		boolean sms = chSms.isChecked();
		boolean push = chPush.isChecked();

		int type = 0;
		if (email)
			type = (int) Math.pow(2, UserSubcribeProduct.EMAIL);
		if (sms)
			type = type | (int) Math.pow(2, UserSubcribeProduct.SMS);
		if (push)
			type = type
					| (int) Math.pow(2, UserSubcribeProduct.PUSH_NOTIFICATION);

		// boolean email = chEmail.isChecked();
		// boolean sms = chSms.isChecked();
		// if (email && sms) {
		// subcribe.type_notification =
		// UserSubcribeProduct.EMAIL_AND_SMS_NOTIFICATION;
		// } else if (email) {
		// subcribe.type_notification = UserSubcribeProduct.EMAIL_NOTIFICATION;
		// } else if (sms) {
		// subcribe.type_notification = UserSubcribeProduct.SMS_NOTIFICCATION;
		// }

		// create new subcribe
		task = new SimpleAsyncTask(this, new DataLoader() {

			@Override
			public void updateUI() {
			}

			@Override
			public void loadData() {
				String param = Global.gsonWithHour.toJson(subcribe,
						UserSubcribeProduct.class);

				String url = String.format(URLConstant.CREATE_SUBCRIBE, Global.getSession());
				RestClient.postData(url, param,
						new JSONParser() {

							@Override
							public void onSuccess(JSONObject json)
									throws JSONException {
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

	public static final int MENU_CURRENT_LOCATION = 0;
	public static final int MENU_SEARCH_LOCATION = 1;
	private MyLocationListener myLocationListener;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_CURRENT_LOCATION, 0,
				getString(R.string.my_current_location));
		menu.add(0, MENU_SEARCH_LOCATION, 0,
				getString(R.string.search_location));
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_CURRENT_LOCATION:
			myLocationListener.findCurrentLocation();
			break;

		case MENU_SEARCH_LOCATION:
			openSearchLocationDialog();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private LayoutInflater inflater;
	private Builder dialogBuilder;
	private AlertDialog dialog;
	private LocationOverlay locationOverlay;
	private CheckBox chSms;
	private CheckBox chEmail;
	private CheckBox chPush;
	private CheckBox chActive;
	private UserSubcribeProduct newSubcribe = new UserSubcribeProduct();

	private void openSearchLocationDialog() {
		if (inflater == null) {
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		View view = inflater.inflate(R.layout.search_location_dialog, null);

		// Edit text
		final EditText txtSearch = (EditText) view.findViewById(R.id.txtSearch);

		// search button
		Button btnSearch = (Button) view.findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String query = txtSearch.getText().toString();
				if (query == null || query.trim().equals("")) {
					Toast.makeText(SubcribeActivity.this,
							getString(R.string.warn_search_location_empty),
							Toast.LENGTH_SHORT).show();
					dialog.dismiss();
				} else {
					searchLocation(query.trim());
				}
			}
		});

		dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setView(view);
		dialog = dialogBuilder.create();

		dialog.show();
	}

	private void searchLocation(String query) {
		GeoPoint point = MapService.locationToGeopoint(query);
		if (point == null) {
			Toast.makeText(this,
					getString(R.string.warn_cannot_find_search_location),
					Toast.LENGTH_SHORT).show();
			dialog.dismiss();
			return;
		}

		mapController.setCenter(point);
		locationOverlay.point = point;
		dialog.dismiss();
	}
}
