package com.appspot.smartshop.dom;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.google.gson.reflect.TypeToken;

public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	public String username = null;
	public Long userId = null;
	public String password = null;
	public String oldPassword = null;
	public String first_name = null;
	public String last_name = null;
	public String phone = null;
	public String email = null;
	public Date birthday = null;
	public String address = null;
	public String avatarLink = null;
	public String lang = null;
	public String country = null;
	public double lat;
	public double lng;
	public int sum_star;
	public int count_vote;
	public double gmt;
	public int type;
	public String sessionId;

	public Set<String> setFriendsUsername;

	public UserInfo() {
	}

	@Override
	public String toString() {
		return "UserInfo [address=" + address + ", avatarLink=" + avatarLink
				+ ", birthday=" + birthday + ", count_vote=" + count_vote
				+ ", country=" + country + ", email=" + email + ", first_name="
				+ first_name + ", gmt=" + gmt + ", lang=" + lang
				+ ", last_name=" + last_name + ", lat=" + lat + ", lng=" + lng
				+ ", oldPassword=" + oldPassword + ", password=" + password
				+ ", phone=" + phone + ", setFriendsUsername="
				+ setFriendsUsername + ", sum_star=" + sum_star + ", type="
				+ type + ", username=" + username + "]";
	}

	public static Type getType() {
		return new TypeToken<List<UserInfo>>() {
		}.getType();
	}

	public UserInfo(String firstName, String lastName, String email,
			String address) {
		this.first_name = firstName;
		this.last_name = lastName;
		this.email = email;
		this.address = address;
	}
}
