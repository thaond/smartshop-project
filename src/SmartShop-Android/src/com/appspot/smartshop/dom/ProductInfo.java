package com.appspot.smartshop.dom;


import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class ProductInfo implements Serializable {
	public long id;
	public String name = null;
	public double price;
	public String description = null;
	public boolean isVAT;
	public int quantity;
	public Date datePost = null;
	public String warranty = null;
	public String origin = null;
	public String address = null;
	public double lat;
	public double lng;
	public String username = null;
	public Set<Long> setPagesId;
	public Set<String> setCategoryKeys;
	public Set<Attribute> setAttributes;
	public ProductInfo(String name, double price, String description){
		this.name = name;
		this.price = price;
		this.description = description;
	}
	public ProductInfo() {}
}
