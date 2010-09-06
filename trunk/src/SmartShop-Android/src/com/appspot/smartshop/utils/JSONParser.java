package com.appspot.smartshop.utils;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class JSONParser {
	public static final int SMART_SHOP_JSON = 0;
	public static final int GOOGLE_DIRECTION_JSON = 1;
	
	protected int jsonType = SMART_SHOP_JSON;
	public abstract void onSuccess(JSONObject json) throws JSONException;
	public abstract void onFailure(String message);
}
