package com.appspot.smartshop.dom;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public class Comment {
	public Long id;
	public String content;
	public String type;
	public long type_id;
	public String username;
	public Date date_post;

	public Comment() {
	}

	public Comment(String content, String type, int typeId, String username) {
		this.content = content;
		this.type = type;
		type_id = typeId;
		this.username = username;
	}

	public static Type getType() {
		return new TypeToken<List<Comment>>() {
		}.getType();
	}
}
