package com.appspot.smartshop.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
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
//				result.points = new GeoPoint[len + 1];
				result.points = new LinkedList<GeoPoint>();
				JSONObject leg = null;
//				JSONObject location = null;
				
				// get instructions
				for (int i = 0; i < len; ++i) {
					leg = arrSteps.getJSONObject(i);
//					location = leg.getJSONObject("start_location");
					String encoded = leg.getJSONObject("polyline").getString("points");
					result.points.addAll(decodePoly(encoded));
					
					
					result.instructions[i] = leg.getString("html_instructions");
//					result.points[i] = new GeoPoint(
//							(int) (location.getDouble("lat") * 1E6),
//							(int) (location.getDouble("lng") * 1E6));
				}
				
//				location = leg.getJSONObject("end_location");
//				result.points[len] = new GeoPoint(
//						(int) (location.getDouble("lat") * 1E6),
//						(int) (location.getDouble("lng") * 1E6));
				
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
	
	private static List<GeoPoint> decodePoly(String encoded) {
	    List<GeoPoint> poly = new ArrayList<GeoPoint>();
	    int index = 0, len = encoded.length();
	    int lat = 0, lng = 0;

	    while (index < len) {
	        int b, shift = 0, result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lat += dlat;

	        shift = 0;
	        result = 0;
	        do {
	            b = encoded.charAt(index++) - 63;
	            result |= (b & 0x1f) << shift;
	            shift += 5;
	        } while (b >= 0x20);
	        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
	        lng += dlng;

	        GeoPoint p = new GeoPoint((int) (((double) lat / 1E5) * 1E6),
	             (int) (((double) lng / 1E5) * 1E6));
	        poly.add(p);
	    }

	    return poly;
	}

	
	public static GeoPoint locationToGeopoint(String locationName) {
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
