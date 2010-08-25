package com.appspot.smartshop.mock;

import com.appspot.smartshop.utils.Utils;
import com.google.android.maps.GeoPoint;

public class MockLocation {
	private static int NUM_OF_POINTS = 20;

	public static GeoPoint[] getPoints() {
		GeoPoint[] points = new GeoPoint[NUM_OF_POINTS];
		for (int i = 0; i < NUM_OF_POINTS; i++) {
			points[i] = new GeoPoint(10775386 + i * Utils.random(10000),
					106660938 + i * Utils.random(10000));
		}
		return points;
	}
}