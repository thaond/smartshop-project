package com.appspot.smartshop.map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.view.MotionEvent;

import com.appspot.smartshop.R;
import com.appspot.smartshop.utils.Global;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class DirectionOverlay extends Overlay {
	public static final int DIRECTION_COLOR = Color.CYAN;
	public static final int ARROW_SIZE = 5;
	
	public GeoPoint[] points;
	private static Paint paint;

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		System.out.println("direction overlay draw");
		super.draw(canvas, mapView, shadow);
		
		if (paint == null) {
			paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(DIRECTION_COLOR);
			paint.setStrokeWidth(2f);
			paint.setStyle(Style.STROKE);
			paint.setStrokeJoin(Join.BEVEL);
		}
		
		// ---translate the GeoPoint to screen pixels---
		int len = points.length;
		Point[] screenPoints = new Point[len];
		for (int i = 0; i < len; ++i){
			screenPoints[i] = new Point();
			mapView.getProjection().toPixels(points[i], screenPoints[i]);
		}
		
		for (int i = 0; i < len - 1; ++i) {
			canvas.drawLine(
					screenPoints[i].x, screenPoints[i].y,
					screenPoints[i + 1].x, screenPoints[i + 1].y,
					paint);
		}
	}
}
