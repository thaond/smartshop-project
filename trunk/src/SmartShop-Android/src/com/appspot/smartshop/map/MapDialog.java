package com.appspot.smartshop.map;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.DirectionAdapter;
import com.appspot.smartshop.utils.Global;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapDialog {

	private static MapView mapView;
	private static MapController mapController;
	private static LayoutInflater inflater;
	private static AlertDialog dialog;
	private static LocationOverlay locationOverlay;
	private static DirectionOverlay directionOverlay;
	private static View view;
	private static AlertDialog.Builder dialogBuilder;

	public static AlertDialog createLocationDialog(Context context, GeoPoint point,
			final UserLocationListener listener) {
		// setup view
		if (inflater == null) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		if (view == null) {
			view = inflater.inflate(R.layout.map_dialog, null);
			mapView = (MapView) view.findViewById(R.id.mapview);
		}
		
		// setting for map view
		mapView = (MapView) view.findViewById(R.id.mapview);

		// overlay
		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		locationOverlay = new LocationOverlay();
		locationOverlay.point = point;
		listOfOverlays.add(locationOverlay);
		
		// controller
		mapController = mapView.getController();
		if (point != null) {
			mapController.setCenter(point);
		}
		mapController.setZoom(17);

		// redraw the whole view
		mapView.invalidate();
		
		// dialog title
		TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
		txtTitle.setText(context.getString(
				point != null ? R.string.titleChoooseUserLoction : R.string.errCantGetLocation));
		
		// listener 
		Button btnOk = (Button) view.findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.processUserLocation(locationOverlay.point);
				}
				dialog.cancel();
			}
		});

		// create dialog
		dialogBuilder = new AlertDialog.Builder(context);
		ViewGroup parent = ((ViewGroup) view.getParent());
		if (parent != null) {
			parent.removeView(view);
		}
		dialogBuilder.setView(view);
		
		dialog = dialogBuilder.create(); 
		return dialog;
	}
	
	public static AlertDialog createDirectionOnMapDialog(final Activity activity, 
			DirectionResult directionResult) {
		
		// setup view
		if (inflater == null) {
			inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		if (view == null) {
			view = inflater.inflate(R.layout.map_dialog, null);
			mapView = (MapView) view.findViewById(R.id.mapview);
		}
			
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

		// redraw mao
		mapView.invalidate();
		
		// listener 
		Button btnOk = (Button) view.findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		// create dialog
		dialogBuilder = new AlertDialog.Builder(activity);
		ViewGroup parent = ((ViewGroup) view.getParent());
		if (parent != null) {
			parent.removeView(view);
		}
		dialogBuilder.setView(view);
		
		dialog = dialogBuilder.create(); 
		return dialog;
	}
	
	public interface UserLocationListener {
		void processUserLocation(GeoPoint point);
	}
}
