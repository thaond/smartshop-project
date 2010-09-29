package com.appspot.smartshop.mock;

import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.appspot.smartshop.dom.UserInfo;

public class MockAddFriend {
	public static final String TAG = "MockAddFriend";
	public static int numOfFriend = 10;

	public static List<UserInfo> getInstance() {
		List<UserInfo> list = new LinkedList<UserInfo>();
		for (int i = 0; i < numOfFriend; i++) {
			list.add(new UserInfo(" first name " + i, "last name" + i,
					"email " + i, "address " + i));
			Log.d(TAG, list.get(i).first_name);
		}
		return list;
	}
}
