package com.appspot.smartshop.map;

import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.utils.CategoriesDialog;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class SearchProductsOnMapActivity extends MapActivity {
	public static final String TAG = "[SearchProductsOnMapActivity]";
	public static final String POST_PARAM = "{lat:%f,lng:%f,distance:%f,limit:0,q:%s}";
	// radius of earth = 6400km
	public static final float LATITUDE_TO_METER = 111701f;	// 180 lat
	public static final float LONGTITUDE_TO_METER = 111701f;	// 360 long

	private MapView mapView;
	private EditText txtSearch;
	private EditText txtRadius;
	
	private ProductsOverlay productsOverlay;

	private MapController mapController;
	
	private static final int SHOW_LOCATION = 1;
	private static final int DONT_SHOW_LOCATION = 0;
	private Handler handler = new Handler () {
		@Override
		public void handleMessage(Message msg) {
			System.out.println("update map");
			
			GeoPoint point = (GeoPoint) msg.obj;
			String currentLocation = String.format(getString(R.string.your_current_location), 
					(double) point.getLongitudeE6() / 1E6,
					(double) point.getLatitudeE6() / 1E6);
			if (msg.what == SHOW_LOCATION) {
				Toast.makeText(SearchProductsOnMapActivity.this, 
						currentLocation, Toast.LENGTH_SHORT).show();
			}
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
		setContentView(R.layout.search_products_on_map);
		// TODO for test
		Global.application = this;
		
		// text view
		txtSearch = (EditText)findViewById(R.id.txtSearch);
		txtRadius = (EditText) findViewById(R.id.txtRadius);
		
		// map view
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		// overlay
		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		productsOverlay = new ProductsOverlay(this);
		productsOverlay.products = new LinkedList<ProductInfo>();
		listOfOverlays.add(productsOverlay);
		
		// controller
		mapController = mapView.getController();
		mapController.setZoom(16);
		
		// location listener
		myLocationListener = new MyLocationListener(this, new MyLocationCallback() {
			
			@Override
			public void onSuccess(GeoPoint point) {
				productsOverlay.mode = ProductsOverlay.SEARCH_NEARBY_CURRENT_LOCATION;
				if (point != null) {
					mapController.setCenter(point);
					productsOverlay.center = point;
					
					// update map
					Message message = handler.obtainMessage();
					message.obj = point;
					message.what = SHOW_LOCATION;
					handler.sendMessage(message);
				}
			}
		});
		myLocationListener.findCurrentLocation();
		
		// search button
		Button btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchProductsOnMap();
			}
		});
	}

	private SimpleAsyncTask task;
	private float radius;
	protected void searchProductsOnMap() {
		// check valid of input
		final String query = txtSearch.getText().toString();
		if (query == null || query.trim().equals("")) {
			Toast.makeText(this, getString(R.string.errorSearchProductsOnMapQueryEmpty), 
					Toast.LENGTH_LONG).show();
			return;
		}
		radius = ProductsOverlay.NO_RADIUS;
		try {
			radius = Integer.parseInt(txtRadius.getText().toString());
		} catch (NumberFormatException ex) {
			Toast.makeText(this, getString(R.string.errorSearchProductsOnMapRadius), 
					Toast.LENGTH_LONG).show();
			return;
		}
		
		productsOverlay.radius = radius;
		
		task = new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void updateUI() {
			}
			
			@Override
			public void loadData() {
				if (productsOverlay.center == null) {
					task.hasData = false;
					task.message = getString(R.string.errCannotFindCurrentLocation);
					task.cancel(true);
					
					return;
				}
				
				double lat = (double)productsOverlay.center.getLatitudeE6() / 1E6;
				double lng = (double)productsOverlay.center.getLongitudeE6() / 1E6;
				String param = String.format(POST_PARAM, lat, lng, radius, query);
				
				RestClient.postData(URLConstant.GET_PRODUCTS_BY_LOCATION, param, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						JSONArray arr = json.getJSONArray("products");
						productsOverlay.products = Global.gsonWithHour.fromJson(arr.toString(), ProductInfo.getType());
						Log.d(TAG, "found " + arr.length() + " product(s)");
						if (productsOverlay.products.size() == 0) {
							task.hasData = false;
							task.message = getString(R.string.warnNoProductFound);
							
							// update map
							productsOverlay.radius = ProductsOverlay.NO_RADIUS;
							Message message = handler.obtainMessage();
							message.obj = productsOverlay.center;
							message.what = DONT_SHOW_LOCATION;
							handler.sendMessage(message);
							return;
						}
						
						mapController.animateTo(productsOverlay.center);
						int spanLat = (int) (2 * (radius / LATITUDE_TO_METER) * 1E6);
						int spanLng = (int) (2 * (radius / LONGTITUDE_TO_METER) * 1E6);
						mapController.zoomToSpan(spanLat, spanLng);
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
		menu.add(0, MENU_CURRENT_LOCATION, 0, getString(R.string.my_current_location));
		menu.add(0, MENU_SEARCH_LOCATION, 0, getString(R.string.search_location));
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_CURRENT_LOCATION:
			productsOverlay.mode = ProductsOverlay.SEARCH_NEARBY_CURRENT_LOCATION;
			myLocationListener.findCurrentLocation();
			break;
			
		case MENU_SEARCH_LOCATION:
			productsOverlay.mode = ProductsOverlay.SEARCH_NEARBY_LOCATION;
			openSearchLocationDialog();
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	private LayoutInflater inflater;
	private Builder dialogBuilder;
	private AlertDialog dialog;
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
					Toast.makeText(SearchProductsOnMapActivity.this, 
							getString(R.string.warn_search_location_empty), Toast.LENGTH_SHORT).show();
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
			Toast.makeText(this, getString(R.string.warn_cannot_find_search_location), 
					Toast.LENGTH_SHORT).show();
			dialog.dismiss();
			return;
		}
		
		mapController.setCenter(point);
		productsOverlay.center = point;
		dialog.dismiss();
	}
}
