package com.appspot.smartshop.dom;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

public class UserInfo implements Serializable {
	public String username = null;
	public String password = null;
	public String oldPassword = null;
	public String first_name = null;
	public String last_name = null;
	public String phone = null;
	public String email = null;
	public Date birthday = null;
	public String address = null;
	public String avatarLink = null;
	public String lang = null;;
	public String country = null;
	public double lat;
	public double lng;
	public int sum_star;
	public int count_vote;
	public double gmt;
	public int type;
	
	public Set<String> setFriendsUsername;
	public Set<String> fts;
	public UserInfo() {
	}
}
