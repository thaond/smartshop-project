package com.appspot.smartshop.map;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class MyLocation {
	public static final int TIME_OUT = 10000;
	public static final String TAG = "[MyLocation]";
	private static MyLocation instance = null;
	
	private Timer timer;
	private LocationManager locationManager;
	private LocationResult locationResult;
	boolean gpsEnabled = false;
	boolean networkEnabled = false;
	
	public static MyLocation getInstance() {
		if (instance == null) {
			instance = new MyLocation();
		}
		
		return instance;
	}
	
	private MyLocation() {
		instance = this;
	}

	public boolean getLocation(Context context, LocationResult result) {
		// I use LocationResult callback class to pass location value from
		// MyLocation to user code.
		locationResult = result;
		if (locationManager == null) {
			locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		}

		// exceptions will be thrown if provider is not permitted.
		try {
			gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}

		// don't start listeners if no provider is enabled
		if (!gpsEnabled && !networkEnabled) {
			return false;
		}

		if (gpsEnabled) {
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
					locationListenerGps);
		}
		if (networkEnabled) {
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
					locationListenerNetwork);
		}
		
		timer = new Timer();
		timer.schedule(new GetLastLocationTimer(), TIME_OUT);
		return true;
	}

	LocationListener locationListenerGps = new LocationListener() {
		public void onLocationChanged(Location location) {
			Log.d(TAG, "Current location found");
			timer.cancel();
			locationResult.gotLocation(location);
			locationManager.removeUpdates(this);
			locationManager.removeUpdates(locationListenerNetwork);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	LocationListener locationListenerNetwork = new LocationListener() {
		public void onLocationChanged(Location location) {
			Log.d(TAG, "Current location found");
			timer.cancel();
			locationResult.gotLocation(location);
			locationManager.removeUpdates(this);
			locationManager.removeUpdates(locationListenerGps);
		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	class GetLastLocationTimer extends TimerTask {
		@Override
		public void run() {
			locationManager.removeUpdates(locationListenerGps);
			locationManager.removeUpdates(locationListenerNetwork);

			Location networkLocation = null, gpsLocation = null;
			if (gpsEnabled) {
				gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			}
			if (networkEnabled) {
				networkLocation = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}

			// if there are both values use the latest one
			if (gpsLocation != null && networkLocation != null) {
				if (gpsLocation.getTime() > networkLocation.getTime())
					locationResult.gotLocation(gpsLocation);
				else
					locationResult.gotLocation(networkLocation);
				return;
			}

			if (gpsLocation != null) {
				locationResult.gotLocation(gpsLocation);
				return;
			}
			if (networkLocation != null) {
				locationResult.gotLocation(networkLocation);
				return;
			}
			locationResult.gotLocation(null);
			
			Log.d(TAG, "GetLastLocationTimer stop");
		}
	}

	public static abstract class LocationResult {
		public abstract void gotLocation(Location location);
	}
}
