package com.appspot.smartshop.mock;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;

import com.appspot.smartshop.dom.SmartshopNotification;

public class MockNotification {
	public static final String TAG = "[MockNotification]";
	public static final int NUM_OF_NOTIFICATION = 10;

	public static List<SmartshopNotification> getInstance() {
		Log.d(TAG, "MockNotification has created");
		List<SmartshopNotification> list = new LinkedList<SmartshopNotification>();
		for (int i = 0; i < NUM_OF_NOTIFICATION; i++) {
//			SmartshopNotification notification = new SmartshopNotification("Co " + i
//					+ "  san pham may tinh hop voi nhu cau cua ban", new Date(
//					89, 11, 11), "loi");
//			list.add(notification);
			Log.d(TAG, list.get(i).content);
		}

		return list;
	}
}
