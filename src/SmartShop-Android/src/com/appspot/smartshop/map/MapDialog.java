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
import android.widget.TextView;

import com.appspot.smartshop.Global;
import com.appspot.smartshop.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapDialog {

	private static MapView mapView;
	private static MapController mapController;
	private static LayoutInflater inflater;
	private static AlertDialog.Builder dialogBuilder;
	private static View view;
	private static AlertDialog dialog;
	
	private static LocationOverlay locationOverlay;
	private static DirectionOverlay directionOverlay;

	public static AlertDialog createLocationDialog(Activity activity, GeoPoint point,
			final UserLocationListener listener) {
		// setup view
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.user_location_dialog,
				(ViewGroup) activity.findViewById(R.id.layout_root));
//		view.setMinimumWidth(Global.getScreenWidth() - 50);
		
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
		mapController.setCenter(point);
		mapController.setZoom(15);

		// redraw the whole view
		mapView.invalidate();

		// create dialog
		dialogBuilder = new AlertDialog.Builder(activity);
		dialogBuilder.setView(view);
		
		// dialog title
		TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
		txtTitle.setText(activity.getString(R.string.titleChoooseUserLoction));
		
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
	
	public interface UserLocationListener {
		void processUserLocation(GeoPoint point);
	}
}
