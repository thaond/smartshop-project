package com.appspot.smartshop.map;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;

public class MyLocationListener implements LocationListener {
	public static final String TAG = "[MyLocationListener]";
	
	private Context context;
	public GeoPoint point;

	public MyLocationListener(Context context) {
		this.context = context;
	}
	
	@Override
	public void onLocationChanged(Location loc) {
		point = new GeoPoint((int) (loc.getLatitude() * 1E6), (int) (loc.getLongitude() * 1E6));
		Log.d(TAG, "current location = " + point);
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

}