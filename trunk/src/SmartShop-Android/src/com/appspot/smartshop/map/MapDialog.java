package com.appspot.smartshop.map;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appspot.smartshop.Global;
import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.DirectionListAdapter;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapDialog {

	private static MapView mapView;
	private static MapController mapController;
	private static LayoutInflater inflater;
	private static AlertDialog.Builder dialogBuilder;
	private static AlertDialog dialog;
	
	private static LocationOverlay locationOverlay;
	private static DirectionOverlay directionOverlay;
	private static View view;

	public static AlertDialog createLocationDialog(Activity activity, GeoPoint point,
			final UserLocationListener listener) {
		// setup view
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.map_dialog,
				(ViewGroup) activity.findViewById(R.id.layout_root));
		
		// setting for map view
		mapView = (MapView) view.findViewById(R.id.mapview);

		// overlay
		List<Overlay> listOfOverlays = mapView.getOverlays();
		if (locationOverlay == null) {
			locationOverlay = new LocationOverlay();
		}
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

		// create dialog
		dialogBuilder = new AlertDialog.Builder(activity);
		dialogBuilder.setView(view);
		
		// dialog title
		TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
		txtTitle.setText(activity.getString(
				point != null ? R.string.titleChoooseUserLoction : R.string.errCantGetLocation));
		
		// listener 
		Button btnOk = (Button) view.findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.processUserLocation(locationOverlay.point);
				dialog.cancel();
			}
		});
		
		dialog = dialogBuilder.create(); 
		return dialog;
	}
	
	public static AlertDialog createDirectionListDialog(final Activity activity, 
			final double latitude1, final double longtitude1,
			final double latitude2, final double longtitude2) {
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.direction_list,
				(ViewGroup) activity.findViewById(R.id.layout_root));
		
		// direction result
		final DirectionResult directionResult = MapService.getDirectionResult(
				latitude1, longtitude1, latitude2, longtitude2);
		
		// listview
		String[] instructions = directionResult.instructions;
		DirectionListAdapter adapter = new DirectionListAdapter(
				activity, R.layout.direction_list_item, instructions);
		ListView listDirection = (ListView) view.findViewById(R.id.listDirection);
		listDirection.setAdapter(adapter);
		
		// duration and distance
		String duration = directionResult.duration;
		String distance = directionResult.distance;
		if (duration != null) {
			TextView txtDuration = (TextView) view.findViewById(R.id.txtDuration);
			txtDuration.setText(duration);
		}
		if (distance != null) {
			TextView txtdistance = (TextView) view.findViewById(R.id.txtDistance);
			txtdistance.setText(distance);
		}
		
		// button show direction on map
		Button btnShowDirectionOnMap = (Button) view.findViewById(R.id.btnShowDirectionOnMap);
		if (directionResult.hasError) {
			btnShowDirectionOnMap.setText(activity.getString(R.string.errGetDirection));
		} else {
			btnShowDirectionOnMap.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MapDialog.createDirectionOnMapDialog(activity, directionResult).show();
				}
			});
		}
	
		// create dialog
		dialogBuilder = new AlertDialog.Builder(activity);
		dialogBuilder.setView(view);
//		dialog = dialogBuilder.create(); 
		return dialogBuilder.create();
	}

	static AlertDialog createDirectionOnMapDialog(Activity context, 
			DirectionResult directionResult) {
//		if (dialog != null) {
//			return dialog;
//		}
		
		// setup view
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		if (view == null) {
		ViewGroup root = (ViewGroup) context.findViewById(R.id.layout_root);
		if (mapView == null) {
			view = inflater.inflate(R.layout.map_dialog, root);
			mapView = (MapView) view.findViewById(R.id.mapview);
		} else {
			root.removeView(view);
			view = inflater.inflate(R.layout.map_dialog, root);
			mapView = (MapView) view.findViewById(R.id.mapview);
		}
			
			// setting for map view
//		}

		// overlay
		List<Overlay> listOfOverlays = mapView.getOverlays();
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

		// redraw the whole view
		mapView.invalidate();

		// create dialog
		dialogBuilder = new AlertDialog.Builder(context);
		dialogBuilder.setView(view);
		
		// listener 
		Button btnOk = (Button) view.findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.hide();
			}
		});
		
		dialog = dialogBuilder.create(); 
		return dialog;
	}
	
	public interface UserLocationListener {
		void processUserLocation(GeoPoint point);
	}
}
