package com.appspot.smartshop.map;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.appspot.smartshop.map.MyLocation.GetLastLocationTimer;
import com.google.android.maps.GeoPoint;

public class MyLocationListener implements LocationListener {
	public static final String TAG = "[MyLocationListener]";
	private static final int NUM_OF_REQUEST = 20;
	
	private Context context;
	private MyLocationCallback callback;
	public GeoPoint point;
	private LocationManager locationManager;

	public MyLocationListener(Context context, MyLocationCallback callback) {
		this.context = context;
		this.callback = callback;
		
		locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, this);
	}
	
	public void getCurrentLocation() {
		new Thread() {
			@Override
			public void run() {
				Log.d(TAG, "start get current location info");
				for (int i = 0; i < NUM_OF_REQUEST; ++i) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {}
					
					if (point != null) {
						callback.onGetCurrentLocation(point);
						break;
					}
				}
				Log.d(TAG, "end get current location info");
			}
		}.start();
		
		Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastLocation != null) {
			callback.onGetCurrentLocation(new GeoPoint(
					(int) (lastLocation.getLatitude() * 1E6), 
					(int) (lastLocation.getLongitude() * 1E6)));
			return;
		}
	}
	
	@Override
	public void onLocationChanged(Location loc) {
		point = new GeoPoint((int) (loc.getLatitude() * 1E6), (int) (loc.getLongitude() * 1E6));
//		if (callback != null) {
//			callback.onGetCurrentLocation(point);
//		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.d(TAG, "gps disabled");
		Toast.makeText(context, "Gps Disabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.d(TAG, "gps enabled");
		Toast.makeText(context, "Gps Enabled", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public interface MyLocationCallback {
		void onGetCurrentLocation(GeoPoint point);
	}
}