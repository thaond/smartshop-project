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

	public UserSubcribeProduct(){};
}
