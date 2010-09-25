package com.appspot.smartshop.dom;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public class UserSubcribeProduct {
	public Long id;
	public Double lat;
	public Double lng;
	public Double radius;
	public String description;
	public boolean isActive = true;
	public Date date;
	public String userName;
	public String q;
	public List<String> categoryList;
	public boolean isNew;
	public int type_notification;

	public UserSubcribeProduct(){};
	
	public static Type getType() {
		return new TypeToken<List<UserSubcribeProduct>>() {}.getType();
	}
}
