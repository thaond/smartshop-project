package com.appspot.smartshop.map;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.location.Location;
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
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.map.MyLocation.LocationResult;
import com.appspot.smartshop.map.MyLocationListener.MyLocationCallback;
import com.appspot.smartshop.mock.MockProduct;
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
	
	private ProductsOverlay productsOverlay;

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
		MyLocationListener myLocationListener = new MyLocationListener(this, new MyLocationCallback() {
			
			@Override
			public void onSuccess(GeoPoint point) {
				mapController.setCenter(point);
				productsOverlay.center = point;
				mapView.invalidate();
			}
			
			@Override
			public void onFailure() {
			}
		});
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
		locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		// get last location of user
		Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		GeoPoint lastPoint = lastLocation == null ? null : 
			new GeoPoint(
				(int) (lastLocation.getLatitude() * 1E6), 
				(int) (lastLocation.getLongitude() * 1E6));
		
		// overlay
		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		productsOverlay = new ProductsOverlay(this);
		productsOverlay.products = new LinkedList<ProductInfo>();
		productsOverlay.center = lastPoint;
		listOfOverlays.add(productsOverlay);
		
		// controller
		mapController = mapView.getController();
		mapController.setZoom(16);
		if (lastPoint != null) {
			mapController.setCenter(lastPoint);
		}
		
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
		float radius = ProductsOverlay.NO_RADIUS;
		try {
			radius = Integer.parseInt(txtRadius.getText().toString());
		} catch (NumberFormatException ex) {
			Toast.makeText(this, getString(R.string.errorSearchProductsOnMapRadius), 
					Toast.LENGTH_LONG).show();
			return;
		}
		
		// TODO (condorhero01): request products list based on center point and radius
		Log.d(TAG, "query = " + query);
		Log.d(TAG, "radius = " + radius);
		productsOverlay.radius = radius;
		productsOverlay.products = MockProduct.getSearchOnMapProducts();
		mapView.invalidate();
	}

}
