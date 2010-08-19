package com.appspot.smartshop.utils;

import org.json.JSONException;
import org.json.JSONObject;

public interface JSONParser {
	void onSuccess(JSONObject json) throws JSONException;
	void onFailure(String message);
}
