package com.appspot.smartshop.mock;

import java.util.LinkedList;

import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.utils.Utils;
import com.google.android.maps.GeoPoint;

public class MockProduct {
	private static final int NUM_OF_PRODUCTS = 20;	

	public static LinkedList<ProductInfo> getProducts() {
		LinkedList<ProductInfo> list = new LinkedList<ProductInfo>();
		
		ProductInfo productInfo;
		int len = MockLocation.getPoints().length;
		for (int i = 0; i < NUM_OF_PRODUCTS; ++i) {
			productInfo = new ProductInfo("name " + i, i * 1E4, "description " + i);
			
			productInfo.id = i;
			
			GeoPoint point = MockLocation.getPoints()[Utils.random(len)];
			productInfo.lat = point.getLatitudeE6();
			productInfo.lng = point.getLongitudeE6();
			
			productInfo.username = MockUserInfo.getUsers()[Utils.random(4)].username;
			
			list.add(productInfo);
		}
		
		return list;
	}
	
	public static LinkedList<ProductInfo> getSearchOnMapProducts() {
		LinkedList<ProductInfo> list = new LinkedList<ProductInfo>();
		
		ProductInfo productInfo = null;
		
		productInfo = new ProductInfo("product 1", 10000, "description 1");
		productInfo.id = 1;
		productInfo.lat = 10771766;
		productInfo.lng = 106664969;
		productInfo.username = MockUserInfo.getUsers()[Utils.random(4)].username;
		list.add(productInfo);
		
		productInfo = new ProductInfo("product 2", 20000, "description 2");
		productInfo.id = 2;
		productInfo.lat = 10770227;
		productInfo.lng = 106668466;
		productInfo.username = MockUserInfo.getUsers()[Utils.random(4)].username;
		list.add(productInfo);
		
		productInfo = new ProductInfo("product 3", 30000, "description 3");
		productInfo.id = 3;
		productInfo.lat = 10766054;
		productInfo.lng = 106664025;
		productInfo.username = MockUserInfo.getUsers()[Utils.random(4)].username;
		list.add(productInfo);
		
		productInfo = new ProductInfo("product 4", 40000, "description 4");
		productInfo.id = 4;
		productInfo.lat = 10774380;
		productInfo.lng = 106662050;
		productInfo.username = MockUserInfo.getUsers()[Utils.random(4)].username;
		list.add(productInfo);
		
		return list;
	}
}
