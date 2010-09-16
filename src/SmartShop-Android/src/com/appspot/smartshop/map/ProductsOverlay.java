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
import com.appspot.smartshop.ui.product.ViewProductActivity;
import com.appspot.smartshop.utils.Global;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class ProductsOverlay extends Overlay{
	public static final float NO_RADIUS = 0.0f;
	public static final float LATITUDE_TO_METER = 111200F;
	public static final float LONGTITUDE_TO_METER = 71470F;
	
	public static final int SEARCH_NEARBY_CURRENT_LOCATION = 0;
	public static final int SEARCH_NEARBY_LOCATION = 1;
	
	private static final Bitmap STAR_ICON = BitmapFactory.decodeResource(
			Global.application.getResources(), R.drawable.star);
	private static final int CIRCLE_COLOR = Color.parseColor("#83A6A800");
	private static final int OVERLAY_WIDTH = 32; 
	
	public int mode = SEARCH_NEARBY_CURRENT_LOCATION;
	public LinkedList<ProductInfo> products;
	public GeoPoint center;
	private Paint paint;
	private Context context;
	public float radius = NO_RADIUS;
	
	public ProductsOverlay(Context context) {
		this.context = context;
	}
	
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);
		System.out.println("[PRODUCTS OVERLAY DRAW]");
		
		if (paint == null) {
			paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setStrokeWidth(4.0f);
		}
		
		// center of map
		Point centerScreen = new Point();
		if (center != null) {
			mapView.getProjection().toPixels(center, centerScreen);
			Bitmap bmp = BitmapFactory.decodeResource(
					Global.application.getResources(), R.drawable.home);
			canvas.drawBitmap(bmp, centerScreen.x, centerScreen.y, null);
			
			// draw circle
			if (radius != NO_RADIUS) {
				float realRadius = mapView.getProjection().metersToEquatorPixels(radius);
				paint.setColor(CIRCLE_COLOR);
				canvas.drawCircle(centerScreen.x, centerScreen.y, realRadius, paint);
			}
		}
		
		// found products
		paint.setColor(Color.BLUE);
		for (ProductInfo productInfo : products) {
			Point screenPoint = new Point();
			mapView.getProjection().toPixels(
					new GeoPoint((int) (productInfo.lat * 1E6), (int) (productInfo.lng  * 1E6)), screenPoint);
			
//			canvas.drawPoint(screenPoint.x, screenPoint.y, paint);
			canvas.drawBitmap(STAR_ICON, screenPoint.x, screenPoint.y, null);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e, MapView mapView) {
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			switch (mode) {
			case SEARCH_NEARBY_CURRENT_LOCATION:
				if (products == null || products.size() == 0) {
					return false;
				}
				
				GeoPoint touchPoint = mapView.getProjection().fromPixels((int) e.getX(), (int) e.getY());
				double minDistance = Double.MAX_VALUE;
				int index = 0;
				int touchIndex = 0;
				for (ProductInfo productInfo : products) {
					double distance = 
						Math.sqrt(Math.pow(touchPoint.getLatitudeE6() - productInfo.lat * 1E6, 2) 
							+ Math.pow(touchPoint.getLongitudeE6() - productInfo.lng * 1E6, 2));
					if (distance < minDistance) {
						minDistance = distance;
						touchIndex = index;
					}
					
					index++;
				}
				
				Intent intent = new Intent(context, ViewProductActivity.class);
				intent.putExtra(Global.PRODUCT_INFO, products.get(touchIndex));
				context.startActivity(intent);
				
				return true;
				
			case SEARCH_NEARBY_LOCATION:
				radius = NO_RADIUS;
				center = mapView.getProjection().fromPixels(
						(int) e.getX() - OVERLAY_WIDTH / 2, (int) e.getY() - OVERLAY_WIDTH / 2);
				mapView.invalidate();
				break;
				
			default:
				
				break;
			}
		}
		
		return false;
	}
}
