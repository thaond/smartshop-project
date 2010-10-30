package com.appspot.smartshop.dom;


import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.gson.reflect.TypeToken;

public class ProductInfo implements Serializable {
	public Long id;
	public int product_view;
	public String name = null;
	public double price;
	public String description = null;
	public boolean isVAT;
	public int quantity;
	public Date date_post = null;
	public String warranty = null;
	public String origin = null;
	public String address = null;
	public double lat;
	public double lng;
	public int sum_star;
	public int count_vote;
	public String username = null;
	public List<Media> setMedias;
	public Set<Long> setPagesId;
	public Set<String> setCategoryKeys;
	public Set<Attribute> attributeSets;
	public ProductInfo(String name, double price, String description){
		this.name = name;
		this.price = price;
		this.description = description; 
	}
	public ProductInfo() {}
	@Override
	public String toString() {
//		return "ProductInfo [address=" + address + ", datePost=" + date_post
//				+ ", description=" + description + ", id=" + id + ", isVAT="
//				+ isVAT + ", lat=" + lat + ", lng=" + lng + ", name=" + name
//				+ ", origin=" + origin + ", price=" + price + ", product_view="
//				+ product_view + ", quantity=" + quantity + ", setAttributes="
//				+ attributeSets + ", setCategoryKeys=" + setCategoryKeys
//				+ ", setPagesId=" + setPagesId + ", username=" + username
//				+ ", warranty=" + warranty + "]";
		return name;
	}
	
	public static Type getType() {
		return new TypeToken<List<ProductInfo>>() {}.getType();
	}
}
