package com.appspot.smartshop.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;

import com.appspot.smartshop.R;
import com.appspot.smartshop.utils.Global;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class LocationOverlay extends Overlay {
	public static final int OVERLAY_WIDTH = 32; 
	public GeoPoint point;
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		
		if (point == null) {
			return;
		}
		
		Point screenPts = new Point();
		mapView.getProjection().toPixels(point, screenPts);

		Bitmap bmp = BitmapFactory.decodeResource(
				Global.application.getResources(), R.drawable.home);
		canvas.drawBitmap(bmp, screenPts.x, screenPts.y, null);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e, MapView mapView) {
		point = mapView.getProjection().fromPixels(
				(int) e.getX() - OVERLAY_WIDTH / 2, (int) e.getY() - OVERLAY_WIDTH / 2);
		mapView.invalidate();
		return super.onTouchEvent(e, mapView);
	}
}
