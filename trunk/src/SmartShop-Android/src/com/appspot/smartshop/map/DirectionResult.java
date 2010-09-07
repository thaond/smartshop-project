package com.appspot.smartshop.map;

import java.util.List;

import com.google.android.maps.GeoPoint;

public class DirectionResult {
	public String[] instructions;
	public List<GeoPoint> points;
	public String duration;
	public String distance;
	public boolean hasError = false;
}
