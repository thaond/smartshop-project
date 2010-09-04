package com.appspot.smartshop.map;

import com.google.android.maps.GeoPoint;

public abstract class MyLocationCallback {
	public abstract void onSuccess(GeoPoint point);
}
