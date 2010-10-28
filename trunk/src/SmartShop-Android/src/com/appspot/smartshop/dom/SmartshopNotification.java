package com.appspot.smartshop.dom;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import com.appspot.smartshop.R;
import com.appspot.smartshop.utils.Global;
import com.google.gson.reflect.TypeToken;

public class SmartshopNotification implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final int ADD_FRIEND = 1;
	public static final int TAG_PRODUCT = 2;
	public static final int UNTAG_PRODUCT = 3;
	public static final int TAG_PAGE = 4;
	public static final int UNTAG_PAGE = 5;
	public static final int ADD_COMMENT_PRODUCT = 6;
	public static final int ADD_COMMENT_PAGE = 7;
	public static final int TAG_PRODUCT_TO_PAGE = 8;

	public int id;
	public int type;
	public String content;
	public long timestamp;
	public boolean isNew;
	public String username;
	public String detail;
	public String jsonOutput;

	public SmartshopNotification() {
	}

	public SmartshopNotification(String content, long date, String username) {
		this.content = content;
		this.timestamp = date;
		this.username = username;
		this.isNew = false;
	}

	public String getTitle() {
		switch (type) {
		case ADD_FRIEND:
			return Global.application.getString(R.string.add_fiend);
		case TAG_PRODUCT:
			return Global.application.getString(R.string.tag_product);
		case TAG_PAGE:
			return Global.application.getString(R.string.tag_page);
		case UNTAG_PRODUCT:
			return Global.application.getString(R.string.untag_product);
		case UNTAG_PAGE:
			return Global.application.getString(R.string.untag_page);
		case ADD_COMMENT_PRODUCT:
			return Global.application.getString(R.string.add_comment_product);
		case ADD_COMMENT_PAGE:
			return Global.application.getString(R.string.add_comment_page);
		case TAG_PRODUCT_TO_PAGE:
			return Global.application.getString(R.string.tag_product_to_page);
		default:
			return Global.application.getString(R.string.notice);
		}
	}

	public static Type getType() {
		return new TypeToken<List<SmartshopNotification>>() {
		}.getType();
	}
}
