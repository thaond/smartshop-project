package com.appspot.smartshop.utils;

import org.json.JSONException;
import org.json.JSONObject;

public interface JSONParser {
	void process(JSONObject json) throws JSONException;
}
