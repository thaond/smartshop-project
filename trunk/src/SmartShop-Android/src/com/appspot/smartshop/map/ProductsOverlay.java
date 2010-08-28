package com.appspot.smartshop.map;

import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.ui.product.ViewSingleProduct;
import com.appspot.smartshop.utils.Global;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class ProductsOverlay extends Overlay{
	public static final float NO_RADIUS = 0.0f;
	
	public LinkedList<ProductInfo> products;
	public GeoPoint center;
	public float radius = NO_RADIUS;
	private Paint paint;
	private Context context;
	
	public ProductsOverlay(Context context) {
		this.context = context;
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		
		if (paint == null) {
			paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setStrokeWidth(5.0f);
		}
		
		// center of map
		Point centerScreen = new Point();
		if (center != null) {
			mapView.getProjection().toPixels(center, centerScreen);
			Bitmap bmp = BitmapFactory.decodeResource(
					Global.application.getResources(), R.drawable.home);
			canvas.drawBitmap(bmp, centerScreen.x, centerScreen.y, null);
		}
		
		// draw radius
		if (radius != NO_RADIUS) {
			// TODO (condorhero01): calculate radius based on screen width and screen height
			paint.setColor(Color.parseColor("#ffcccc"));
			float screenRadius = mapView.getProjection().metersToEquatorPixels(radius);
			canvas.drawCircle(centerScreen.x, centerScreen.y, screenRadius, paint);
		}
		
		// found products
		paint.setColor(Color.BLUE);
		for (ProductInfo productInfo : products) {
			Point screenPoint = new Point();
			mapView.getProjection().toPixels(new GeoPoint(productInfo.lat, productInfo.lng), screenPoint);
			
			canvas.drawPoint(screenPoint.x, screenPoint.y, paint);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e, MapView mapView) {
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			GeoPoint touchPoint = mapView.getProjection().fromPixels((int) e.getX(), (int) e.getY());
			for (ProductInfo productInfo : products) {
				double distance = Math.sqrt(Math.pow(touchPoint.getLatitudeE6() - productInfo.lat, 2) 
						+ Math.pow(touchPoint.getLongitudeE6() - productInfo.lng, 2));
				if (distance < 1000) {
					Intent intent = new Intent(context, ViewSingleProduct.class);
					intent.putExtra(Global.PRODUCT_INFO, productInfo);
					
					context.startActivity(intent);
					break;
				}
			}
			
			return true;
		}
		
		return false;
	}
}
