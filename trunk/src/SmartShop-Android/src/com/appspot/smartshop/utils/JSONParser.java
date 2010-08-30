package com.appspot.smartshop.utils;

import com.google.gson.JsonObject;

public interface JSONParser {
	void onSuccess(JsonObject json);
	void onFailure(String message);
}
