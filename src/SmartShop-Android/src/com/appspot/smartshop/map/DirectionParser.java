package com.appspot.smartshop.map;

import org.json.JSONException;
import org.json.JSONObject;

import com.appspot.smartshop.utils.JSONParser;

public class DirectionParser implements JSONParser {
	public DirectionResult result = new DirectionResult();

	@Override
	public void onFailure(String message) {
	}

	@Override
	public void onSuccess(JSONObject json) throws JSONException {
	}
}
