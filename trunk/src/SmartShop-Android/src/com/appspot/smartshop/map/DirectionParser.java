package com.appspot.smartshop.map;

import org.json.JSONException;
import org.json.JSONObject;

import com.appspot.smartshop.utils.JSONParser;
import com.google.gson.JsonObject;

public class DirectionParser extends JSONParser {
	public DirectionResult result = new DirectionResult();
	
	public int jsonType = JSONParser.GOOGLE_DIRECTION_JSON;

	@Override
	public void onFailure(String message) {
	}

	@Override
	public void onSuccess(JSONObject json) throws JSONException {
	}
}
