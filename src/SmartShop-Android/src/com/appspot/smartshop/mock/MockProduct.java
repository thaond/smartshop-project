package com.appspot.smartshop.mock;

import java.util.ArrayList;

import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.utils.Utils;
import com.google.android.maps.GeoPoint;

public class MockProduct {
	private static final int NUM_OF_PRODUCTS = 20;	

	public static ArrayList<ProductInfo> getProducts() {
		ArrayList<ProductInfo> list = new ArrayList<ProductInfo>();
		
		ProductInfo productInfo;
		int len = MockLocation.getPoints().length;
		for (int i = 0; i < NUM_OF_PRODUCTS; ++i) {
			productInfo = new ProductInfo("name " + i, i * 1E4, "description " + i);
			
			GeoPoint point = MockLocation.getPoints()[Utils.random(len)];
			productInfo.lat = point.getLatitudeE6();
			productInfo.lng = point.getLongitudeE6();
			
			list.add(productInfo);
		}
		
		return list;
	}
}
