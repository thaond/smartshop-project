package com.appspot.smartshop.dom;

import java.io.InputStream;

import com.google.gson.annotations.Exclude;

public class Media {
	public String name;
	public String link; 
	@Exclude
	public InputStream inputStream;
}
