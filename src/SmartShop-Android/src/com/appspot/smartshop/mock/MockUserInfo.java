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
		userInfo.lat = 10.771766;
		userInfo.lng = 106.664969;
		
		return userInfo;
	}
	
	public static UserInfo[] getUsers() {
		UserInfo[] users = new UserInfo[4];
		
		users[0] = getInstance();
		
		users[1] = new UserInfo();
		users[1].username = "vo.mita.ov";
		users[1].password = "pass";
		users[1].address = "43 vương văn huống bình tân hcm";
		users[1].phone = "0984 432 486";
		users[1].birthday = new Date(1989 - 1900, 11, 22);
		users[1].email = "vo.mita.ov@gmail.com";
		users[1].first_name = "Võ";
		users[1].last_name = "Minh Tâm";
		
		users[2] = new UserInfo();
		users[2].username = "trongnghia89";
		users[2].password = "pass";
		users[2].address = "166/17 phạm phú thứ quận 6 hcm";
		users[2].phone = "0984 432 486";
		users[2].birthday = new Date(1989 - 1900, 0, 1);
		users[2].email = "aizikko@gmail.com";
		users[2].first_name = "Lê";
		users[2].last_name = "Trọng Nghĩa";
		
		users[3] = new UserInfo();
		users[3].username = "vanloi999";
		users[3].password = "pass";
		users[3].address = "497 hòa hảo quận 10 hcm";
		users[3].phone = "0984 432 486";
		users[3].birthday = new Date(1989 - 1900, 0, 1);
		users[3].email = "vanloi999@gmail.com";
		users[3].first_name = "Nguyễn";
		users[3].last_name = "Văn Lợi";
		
		return users;
	}
	
	public static UserInfo getUser(String username) {
		UserInfo[] userInfos = getUsers();
		for (UserInfo userInfo : userInfos) {
			if (userInfo.username.equals(username)) {
				return userInfo;
			}
		}
		
		return null;
	}
}
