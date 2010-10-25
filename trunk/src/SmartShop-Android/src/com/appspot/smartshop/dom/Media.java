package com.appspot.smartshop.dom;

import java.io.InputStream;

import android.graphics.Bitmap;

import com.google.gson.annotations.Exclude;

public class Media {
	public String name;
	public String link;
	public String mime_type;
	public String description;
	@Exclude
	public InputStream inputStream;
	@Exclude
	public Bitmap bitmap;

	public Media(String name, String link, String mimeType, String description) {
		this.name = name;
		this.link = link;
		mime_type = mimeType;
		this.description = description;
	}

	public Media() {
	}

}
