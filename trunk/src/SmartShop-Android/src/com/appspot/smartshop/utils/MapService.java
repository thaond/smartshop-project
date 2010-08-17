package com.appspot.smartshop.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class MapService {
	
	public static final String GET_DIRECTION_URL = "http://maps.google.com/maps/api/directions/json?origin=%f,%f&destination=%f,%f&sensor=true&language=vi&units=metric";

	public static String[] getDirectionInstructions(
			float latitude1, float longtitude1,
			float latitude2, float longtitude2) {
		String url = String.format(GET_DIRECTION_URL, latitude1, longtitude1, latitude2, longtitude2);
		RestClient.parse(url, new JSONParser() {
			
			@Override
			public void process(JSONObject json) throws JSONException {
				/*
				 * routes[]
						legs[]
							steps[]
								html_instructions
				 */
				JSONArray arrRoutes = json.getJSONArray("routes");
				for (int k = 0; k < arrRoutes.length(); ++k) {
					JSONArray arrLegs = arrRoutes.getJSONObject(k).getJSONArray("legs");
					for (int i = 0; i < arrLegs.length(); ++i) {
						JSONArray arrSteps = arrLegs.getJSONObject(i).getJSONArray("steps");
						for (int j = 0; j < arrSteps.length(); ++j) {
							String instruction = arrSteps.getJSONObject(j).getString("html_instructions");
						}
					}
				}
				
				
			}
		});
		
		return null;
	}
}
