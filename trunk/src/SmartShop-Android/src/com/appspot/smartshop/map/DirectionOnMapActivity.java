package com.appspot.smartshop.map;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.appspot.smartshop.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class DirectionOnMapActivity extends MapActivity {
	
	private static MapView mapView;
	private static MapController mapController;
	private static DirectionOverlay directionOverlay;
	protected static DirectionResult directionResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.direction);
		
		// setup view
		mapView = (MapView) findViewById(R.id.mapview);
			
		// overlay
		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		if (directionOverlay == null) {
			directionOverlay = new DirectionOverlay();
		}
		directionOverlay.points = directionResult.points;
		listOfOverlays.add(directionOverlay);
		
		// calculate lat, lng span
		int lattitude;
		int longtitude;
		int maxLat = Integer.MIN_VALUE;
		int minLat = Integer.MAX_VALUE;
		int maxLong = Integer.MIN_VALUE;
		int minLong = Integer.MAX_VALUE;
		for (GeoPoint p: directionOverlay.points) {
			lattitude = p.getLatitudeE6();
			longtitude = p.getLongitudeE6();
			
			if (minLat > lattitude) {
				minLat = lattitude;
			}
			if (maxLat < lattitude) {
				maxLat = lattitude;
			}
			if (minLong > longtitude) {
				minLong = longtitude;
			}
			if (maxLong < longtitude) {
				maxLong = longtitude;
			}
		}
		
		// controller
		mapController = mapView.getController();
		GeoPoint center = new GeoPoint((maxLat + minLat) / 2, (maxLong + minLong) / 2);
		mapController.animateTo(center);
		mapController.zoomToSpan(maxLat - minLat, maxLong - minLong);

		// redraw map
		mapView.invalidate();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
