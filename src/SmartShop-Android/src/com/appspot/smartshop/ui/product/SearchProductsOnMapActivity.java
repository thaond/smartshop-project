package com.appspot.smartshop.ui.product;

import java.util.List;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.smartshop.R;
import com.appspot.smartshop.map.LocationOverlay;
import com.appspot.smartshop.map.MyLocationListener;
import com.appspot.smartshop.utils.Global;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class SearchProductsOnMapActivity extends MapActivity {
	public static final String TAG = "[SearchProductsOnMapActivity]";

	private MapView mapView;
	private EditText txtSearch;
	private EditText txtRadius;
	private TextView txtLocation;

	private LocationOverlay locationOverlay;

	private MapController mapController;

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
		txtLocation = (TextView) findViewById(R.id.txtLocation);
		
		// map view
		mapView = (MapView) findViewById(R.id.mapview);
		// location listener
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		MyLocationListener myLocationListener = new MyLocationListener(this);
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
		GeoPoint point = myLocationListener.point;
		// overlay
		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		locationOverlay = new LocationOverlay();
		locationOverlay.point = point;
		listOfOverlays.add(locationOverlay);
		// controller
		mapController = mapView.getController();
		// TODO (condorhero01): center map screen to current location of users
		mapController.setCenter(new GeoPoint(15931751,107746581));
		mapController.setZoom(13);
		// redraw the whole view
		mapView.invalidate();
		
		// search button
		Button btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchProductsOnMap();
			}
		});
	}

	protected void searchProductsOnMap() {
		// check valid of input
		String query = txtSearch.getText().toString();
		if (query == null || query.trim().equals("")) {
			Toast.makeText(this, getString(R.string.errorSearchProductsOnMapQueryEmpty), 
					Toast.LENGTH_LONG).show();
			return;
		}
		String radius = txtRadius.getText().toString();
		try {
			Integer.parseInt(radius);
		} catch (NumberFormatException ex) {
			Toast.makeText(this, getString(R.string.errorSearchProductsOnMapRadius), 
					Toast.LENGTH_LONG).show();
			return;
		}
		
		// TODO (condorhero01): request products list based on center point and radius
		Log.d(TAG, "query = " + query);
		Log.d(TAG, "radius = " + radius);
	}

}
