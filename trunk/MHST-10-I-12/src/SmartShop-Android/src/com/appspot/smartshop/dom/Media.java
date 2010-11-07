package com.appspot.smartshop.dom;

import java.io.InputStream;
import java.io.Serializable;

import android.graphics.drawable.Drawable;

import com.appspot.smartshop.utils.Utils;
import com.google.gson.annotations.Exclude;

public class Media implements Serializable{
	private static final long serialVersionUID = 1L;
	public String name;
	public String link;
	public String mime_type;
	public String description;
	@Exclude
	public InputStream inputStream;
//	@Exclude
//	private Drawable bitmap;

	public Media(String name, String link, String mimeType, String description) {
		this.name = name;
		this.link = link;
		mime_type = mimeType;
		this.description = description;
	}

	public Media() {
	}
	
	public Drawable getDrawable(){
//		if (bitmap==null)
//			bitmap = Utils.loadImage(link);
//		return bitmap;
		return Utils.loadImage(link);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Media [description=" + description + ", link=" + link
				+ ", mime_type=" + mime_type + ", name=" + name + "]";
	}
}
