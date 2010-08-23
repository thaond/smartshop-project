package com.appspot.smartshop.dom;

import java.util.Set;

public class ProductInfo {
	public String name;
	public String price;
	public String description;
	public Long id;
	public boolean is_vat;
	public int quantity;
	public String warranty;
	public String origin;
	public String address;
	public double lat;
	public double lng;
	public String username;
	public Set<Long> setPagesId;
	public Set<String> setCategoryKeys;
	public Set<Attribute> setAttributes;
	
	public ProductInfo(String name, String price, String description){
		this.name = name;
		this.price = price;
		this.description = description;
	}
	public ProductInfo() {}
}
