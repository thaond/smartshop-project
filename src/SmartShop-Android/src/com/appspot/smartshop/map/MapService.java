package com.appspot.smartshop.map;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.appspot.smartshop.R;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.RestClient;
import com.google.android.maps.GeoPoint;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MapService {
	public static final String TAG = "[MapService]";
	public static final String GET_DIRECTION_URL = "http://maps.google.com/maps/api/directions/json?" +
			"origin=%f,%f" +
			"&destination=%f,%f" +
			"&sensor=true&language=vi&units=metric";

	public static DirectionResult getDirectionResult(
			double latitude1, double longtitude1,
			double latitude2, double longtitude2) {
		String url = String.format(GET_DIRECTION_URL, latitude1, longtitude1, latitude2, longtitude2);
		
		// parse direction
		DirectionParser directionParser = new DirectionParser() {
			@Override
			public void onSuccess(JSONObject json) throws JSONException {
				// process response status
				String status = json.getString("status");
				if (!status.equals("OK")) {
					String error = null;
					
					if (status.equals("NOT_FOUND") || status.equals("ZERO_RESULTS")) {
						error = Global.application.getString(R.string.errCantGetDirection);
					} else {
						error = Global.application.getString(R.string.errGetDirection);
					}
					
					result.instructions = new String[] {error};
					result.hasError = true;
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
				if (arrRoutes.length()==0) {
					result.instructions = new String[] {
							Global.application.getString(R.string.errCantGetDirection)};
					result.hasError = true;
					return;
				}
				
				JSONArray arrLegs = arrRoutes.getJSONObject(0).getJSONArray("legs");
				JSONObject firstLeg = arrLegs.getJSONObject(0);
				JSONArray arrSteps = firstLeg.getJSONArray("steps");
				int len = arrSteps.length();
				result.instructions = new String[len];
				result.points = new GeoPoint[len + 1];
				JSONObject leg = null;
				JSONObject location = null;
				
				// get instructions
				for (int i = 0; i < len; ++i) {
					leg = arrSteps.getJSONObject(i);
					location = leg.getJSONObject("start_location");
					
					result.instructions[i] = leg.getString("html_instructions");
					result.points[i] = new GeoPoint(
							(int) (location.getDouble("lat") * 1E6),
							(int) (location.getDouble("lng") * 1E6));
				}
				
				location = leg.getJSONObject("end_location");
				result.points[len] = new GeoPoint(
						(int) (location.getDouble("lat") * 1E6),
						(int) (location.getDouble("lng") * 1E6));
				
//				for (int k = 0; k < arrRoutes.length(); ++k) {
//					JSONArray arrLegs = arrRoutes.getJSONObject(k).getJSONArray("legs");
//					for (int i = 0; i < arrLegs.length(); ++i) {
//						JSONArray arrSteps = arrLegs.getJSONObject(i).getJSONArray("steps");
//						for (int j = 0; j < arrSteps.length(); ++j) {
//							String instruction = arrSteps.getJSONObject(j).getString("html_instructions");
//						}
//					}
//				}
				
				// distance and duration info
				JSONObject distance = firstLeg.getJSONObject("distance");
				if (distance != null) {
					result.distance = distance.getString("text");
				}
				JSONObject duration = firstLeg.getJSONObject("duration");
				if (duration != null) {
					result.duration = duration.getString("text");
				}
			}

			@Override
			public void onFailure(String message) {
				String error = Global.application.getString(R.string.errGetDirection);
				
				result.instructions = new String[] {error};
				result.hasError = true;
			}
		};
		
		// return direction result
		RestClient.getData(url, directionParser);
		return directionParser.result;
	}
	
	public static GeoPoint locationToGeopoint(String locationName) {
		// TODO (condorhero01): change to geocoder from server
		Geocoder geocoder = new Geocoder(Global.application);
		try {
			Log.d(TAG, "Find address of " + locationName);
			List<Address> addresses = geocoder.getFromLocationName(locationName, 5);
			if (addresses != null && addresses.size() > 0) {
				Address add = addresses.get(0);
				return new GeoPoint((int) (add.getLatitude() * 1E6), (int) (add.getLongitude() * 1E6));
			} else {
				Log.d(TAG, "No address found");
				return null;
			}
		} catch (IOException e) {
			Log.e(TAG, "Cannot get location from server");
			e.printStackTrace();
		}
		
		return null;
	}
}
