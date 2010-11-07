package com.appspot.smartshop.mock;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;

import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.map.MapService;
import com.appspot.smartshop.utils.Utils;
import com.google.android.maps.GeoPoint;

public class MockProduct {
	private static final int NUM_OF_PRODUCTS = 20;	

	public static LinkedList<ProductInfo> getProducts() {
		String[] usernames = {"hieu", "tam", "nghia", "duc", "loi"};
		String[] origins = {"vietnam", "campuchia", "lao", "thai lan", "my"};
		String[] cats = {"lap", "net", "soft", "comp"};
		LinkedList<ProductInfo> list = new LinkedList<ProductInfo>();
		
		ProductInfo productInfo;
		int len = MockLocation.getPoints().length;
		for (int i = 1; i <= NUM_OF_PRODUCTS; ++i) {
			productInfo = new ProductInfo("name " + i, i * 1E4, "description " + i);
			
			GeoPoint point = MockLocation.getPoints()[Utils.random(len)];
			productInfo.lat = (double)point.getLatitudeE6() / 1E6;
			productInfo.lng = (double)point.getLongitudeE6() / 1E6;
			productInfo.username = usernames[Utils.random(5)];
			productInfo.date_post = new Date(2010 - 1900, Utils.random(12), Utils.random(31));
			productInfo.is_vat = Utils.random(10) > 5 ? true : false;
			productInfo.origin = origins[Utils.random(5)];
			productInfo.price = Utils.random(1000);
			productInfo.quantity = Utils.random(100);
			productInfo.warranty = "12 thang";
			productInfo.setCategoryKeys = new HashSet<String>();
			int numCats = 1 + Utils.random(4);
			for (int j = 0; j < numCats; ++j) {
				productInfo.setCategoryKeys.add(cats[j]);
			}
			
			list.add(productInfo);
		}
		
		return list;
	}
	
	public static LinkedList<ProductInfo> getSearchOnMapProducts() {
		LinkedList<ProductInfo> list = new LinkedList<ProductInfo>();
		
		ProductInfo productInfo = null;
		
		productInfo = new ProductInfo("product 1", 10000, "description 1");
		productInfo.lat = 10.771766;
		productInfo.lng = 106.664969;
		productInfo.username = MockUserInfo.getUsers()[Utils.random(4)].username;
		list.add(productInfo);
		
		productInfo = new ProductInfo("product 2", 20000, "description 2");
		productInfo.lat = 10.770227;
		productInfo.lng = 106.668466;
		productInfo.username = MockUserInfo.getUsers()[Utils.random(4)].username;
		list.add(productInfo);
		
		productInfo = new ProductInfo("product 3", 30000, "description 3");
		productInfo.lat = 10.766054;
		productInfo.lng = 106.664025;
		productInfo.username = MockUserInfo.getUsers()[Utils.random(4)].username;
		list.add(productInfo);
		
		productInfo = new ProductInfo("product 4", 40000, "description 4");
		productInfo.lat = 10.774380;
		productInfo.lng = 106.662050;
		productInfo.username = MockUserInfo.getUsers()[Utils.random(4)].username;
		list.add(productInfo);
		
		return list;
	}
}
