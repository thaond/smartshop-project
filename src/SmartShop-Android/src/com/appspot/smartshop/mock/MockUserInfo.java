package com.appspot.smartshop.mock;

import java.util.Date;

import com.appspot.smartshop.dom.UserInfo;

public class MockUserInfo {

	public static UserInfo getInstance() {
		UserInfo userInfo = new UserInfo();
		
		userInfo.username = "condorhero89";
		userInfo.password = "pass";
		userInfo.address = "117 thành thái quận 10 hcm";
		userInfo.phone = "0984 432 486";
		userInfo.birthday = new Date(1989 - 1900, 7, 11);
		userInfo.email = "condorher01@gmail.com";
		userInfo.first_name = "Cao Tiến";
		userInfo.last_name = "Đức";
		
		return userInfo;
	}
}
