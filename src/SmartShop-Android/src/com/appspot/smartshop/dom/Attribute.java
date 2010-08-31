package com.appspot.smartshop.dom;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public class Attribute {
	public String key_cat;
	public String name;
	public String value;
	public String username;

	public Attribute() {
	}
	
	public static Type getType() {
		return new TypeToken<List<Attribute>>() {}.getType();
	}
}
