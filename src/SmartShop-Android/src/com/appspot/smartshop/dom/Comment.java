package com.appspot.smartshop.dom;

public class Comment {
	public Long id;

	public String content;

	public String type;

	public int type_id;

	public String username;

	public Comment() {
	}

	public Comment(String content, String type, int typeId, String username) {
		this.content = content;
		this.type = type;
		type_id = typeId;
		this.username = username;
	}
}
