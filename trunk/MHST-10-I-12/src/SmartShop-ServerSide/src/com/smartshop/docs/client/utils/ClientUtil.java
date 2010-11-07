package com.smartshop.docs.client.utils;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.Window;
import com.smartshop.docs.client.ItemNode;
import com.smartshop.docs.share.GoogleUser;

public class ClientUtil {
	public static GoogleUser googleUser;
	public static Map<String, ItemNode> mapNode = new HashMap<String, ItemNode>();

	public static int getScreenWidth() {
		return Window.getClientWidth();
	}

	public static int getScreenHeight() {
		return Window.getClientHeight();
	}
}
