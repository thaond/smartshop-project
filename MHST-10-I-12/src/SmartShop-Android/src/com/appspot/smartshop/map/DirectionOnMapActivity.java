package com.appspot.smartshop.map;

import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.appspot.smartshop.R;
import com.appspot.smartshop.ui.product.ProductsListActivity;
import com.appspot.smartshop.ui.product.SelectTwoProductActivity;
import com.appspot.smartshop.utils.CategoriesDialog;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.CategoriesDialog.CategoriesDialogListener;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class DirectionOnMapActivity extends MapActivity {
	
	private static MapView mapView;
	private static MapController mapController;
	private static DirectionOverlay directionOverlay;
	public static DirectionResult directionResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.direction);
		
		// setup view
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
			
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
	
	public static final int MENU_VIEW_DIRECTION_DETAIL = 1;
	public static final int MENU_VIEW_ROAD_DETAIL = 2;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_VIEW_DIRECTION_DETAIL, 0,
				getString(R.string.view_direction_detail));
		menu.add(0, MENU_VIEW_ROAD_DETAIL, 0,
				getString(R.string.view_road_detail));
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_VIEW_ROAD_DETAIL:
			if (directionResult.points.size() >= 2) {
				int lat1 = directionResult.points.get(0).getLatitudeE6();
				int lng1 = directionResult.points.get(0).getLongitudeE6();
				int lat2 = directionResult.points.get(1).getLatitudeE6();
				int lng2 = directionResult.points.get(1).getLongitudeE6();
				
				mapController.animateTo(new GeoPoint((lat1 + lat2) / 2, (lng1 + lng2) / 2) );
				mapController.zoomToSpan(Math.abs(lat1 - lat2), Math.abs(lng1 - lng2));
			}
			break;
			
		case MENU_VIEW_DIRECTION_DETAIL:
			Intent intent = new Intent(getApplicationContext(), DirectionListActivity.class);
			startActivity(intent);
			break;
		}

		return super.onOptionsItemSelected(item);
	}
}
