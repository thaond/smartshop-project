package com.appspot.smartshop.dom;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import com.google.gson.reflect.TypeToken;

public class UserSubcribeProduct implements Serializable{
	public static final int EMAIL = 0;
	public static final int SMS = 1;
	public static final int PUSH_NOTIFICATION = 2;
	
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
	public int type_notification;
	public boolean isNew;

	public UserSubcribeProduct(){};
	
	private String toBinaryString() {
		String tmp = Integer.toBinaryString(type_notification);
		for (int i = tmp.length(); i < 3; i++) {
			tmp = "0" + tmp;
		}
		return tmp;
	}

	public boolean isSendMail() {
		if (toBinaryString().charAt(3 - 1 - EMAIL) == '1')
			return true;

		return false;
	}

	public boolean isSendSMS() {
		if (toBinaryString().charAt(3 - 1 - SMS) == '1')
			return true;
		return false;
	}

	public boolean isPushNotification() {
		if (toBinaryString().charAt(3 - 1 - PUSH_NOTIFICATION) == '1')
			return true;

		return false;
	}
	
	public static Type getType() {
		return new TypeToken<List<UserSubcribeProduct>>() {}.getType();
	}
}
