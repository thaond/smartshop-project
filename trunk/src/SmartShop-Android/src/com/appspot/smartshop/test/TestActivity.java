package com.appspot.smartshop.test;

import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import com.appspot.smartshop.R;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class TestActivity extends MapActivity {
	public static final String TAG = "TestActivity";
	
	private MapView mapView;
	private MyLocationOverlay myLocationOverlay;
	private LocationManager locationManager;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		AlertDialog.Builder builder;
		AlertDialog alertDialog;

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.test,
		                               (ViewGroup) findViewById(R.id.layout_root));
		
		mapView = (MapView) view.findViewById(R.id.mapview);
		mapView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("click");
			}
		});
		mapView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					System.out.println("touch");
					return true;
				}
				
				return false;
			}
		});
		
		Geocoder geocoder = new Geocoder(this);
		try {
			List<Address> addresses = geocoder.getFromLocationName("268 lý thường kiệt quận 10 hcm", 10);
			if (addresses != null && addresses.size() > 0) {
				for (Address a : addresses) {
					Log.d(TAG, a.toString());
				}
				Log.d(TAG, "found " + addresses.size() + " addresses");
			} else {
				Log.d(TAG, "No address found");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		builder = new AlertDialog.Builder(this);
		builder.setView(view);
		alertDialog = builder.create();

		alertDialog.show();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}