package com.appspot.smartshop.map;

import com.google.android.maps.GeoPoint;

public class DirectionResult {
	public String[] instructions;
	public GeoPoint[] points;
	public String duration;
	public String distance;
	public boolean hasError = false;
}
