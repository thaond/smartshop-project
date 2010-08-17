package com.appspot.smartshop.map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appspot.smartshop.Global;
import com.appspot.smartshop.R;
import com.appspot.smartshop.utils.RestClient;
import com.google.android.maps.GeoPoint;

import android.util.Log;

public class MapService {
	
	public static final String GET_DIRECTION_URL = "http://maps.google.com/maps/api/directions/json?" +
			"origin=%f,%f" +
			"&destination=%f,%f" +
			"&sensor=true&language=vi&units=metric";

	public static DirectionResult getDirectionInstructions(
			double latitude1, double longtitude1,
			double latitude2, double longtitude2) {
		String url = String.format(GET_DIRECTION_URL, latitude1, longtitude1, latitude2, longtitude2);
		
		// parse direction
		DirectionParser directionParser = new DirectionParser() {
			@Override
			public void process(JSONObject json) throws JSONException {
				// process response status
				String status = json.getString("status");
				if (!status.equals("OK")) {
					String error = null;
					
					if (status.equals("NOT_FOUND") || status.equals("ZERO_RESULTS")) {
						error = Global.currentActivity.getString(R.string.errCantGetDirection);
					} else {
						error = Global.currentActivity.getString(R.string.errGetDirection);
					}
					
					result.instructions = new String[] {error};
					return;
				}
				
				/*
				 * routes[]
						legs[]
							steps[]
								html_instructions
				 */
				JSONArray arrRoutes = json.getJSONArray("routes");
				
				// no routes found
				if (arrRoutes.isNull(0)) {
					result.instructions = new String[] {Global.currentActivity.getString(R.string.errCantGetDirection)};
					return;
				}
				
				JSONArray arrLegs = arrRoutes.getJSONObject(0).getJSONArray("legs");
				JSONArray arrSteps = arrLegs.getJSONObject(0).getJSONArray("steps");
				int len = arrSteps.length();
				result.instructions = new String[len];
				result.points = new GeoPoint[len];
				JSONObject leg = null;
				JSONObject location = null;
				for (int i = 0; i < len; ++i) {
					leg = arrSteps.getJSONObject(i);
					location = leg.getJSONObject(i == len - 1 ? "end_location" : "start_location");
					
					result.instructions[i] = leg.getString("html_instructions");
					result.points[i] = new GeoPoint(
							(int) (location.getDouble("lat") * 1E6),
							(int) (location.getDouble("lng") * 1E6));
					System.out.println(result.points[i]);
				}
				
//				for (int k = 0; k < arrRoutes.length(); ++k) {
//					JSONArray arrLegs = arrRoutes.getJSONObject(k).getJSONArray("legs");
//					for (int i = 0; i < arrLegs.length(); ++i) {
//						JSONArray arrSteps = arrLegs.getJSONObject(i).getJSONArray("steps");
//						for (int j = 0; j < arrSteps.length(); ++j) {
//							String instruction = arrSteps.getJSONObject(j).getString("html_instructions");
//						}
//					}
//				}
			}
		};
		
		// return direction result
		RestClient.parse(url, directionParser);
		return directionParser.result;
	}
}
