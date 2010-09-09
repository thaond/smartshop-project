package com.appspot.smartshop.dom;

import java.util.Date;
import java.util.List;

public class UserSubcribeProduct {
	public Long id;
	public Double lat;
	public Double lng;
	public Double radius;
	public String description;
	public boolean isActive = true;
	public Date date;
	public String userName;
	public List<String> categoryList;

	public UserSubcribeProduct(Double lat, Double lng, Double radius,
			String desc, boolean isActive, Date date, String userName,
			List<String> categoryList) {
		this.lat = lat;
		this.lng = lng;
		this.radius = radius;
		this.description = desc;
		this.isActive = isActive;
		this.date = date;
		this.userName = userName;
		this.categoryList = categoryList;
	}
	public UserSubcribeProduct(){};
}
