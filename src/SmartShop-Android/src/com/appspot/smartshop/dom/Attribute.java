package com.appspot.smartshop.dom;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public class Attribute implements Serializable {
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
