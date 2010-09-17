package com.appspot.smartshop.mock;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.appspot.smartshop.dom.MyNotification;

public class MockNotification {
	public static final String TAG = "[MockNotification]";
	public static final int NUM_OF_NOTIFICATION = 10;

	public static List<MyNotification> getInstance() {
		Log.d(TAG, "MockNotification has created");
		List<MyNotification> list = new LinkedList<MyNotification>();
		for (int i = 0; i < NUM_OF_NOTIFICATION; i++) {
			MyNotification notification = new MyNotification("Co " + i
					+ "  san pham may tinh hop voi nhu cau cua ban", new Date(
					89, 11, 11), "loi");
			list.add(notification);
			Log.d(TAG, list.get(i).content);
		}

		return list;
	}
}
