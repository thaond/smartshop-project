package com.appspot.smartshop.test;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.R.integer;
import android.app.AlertDialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.appspot.smartshop.Global;
import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.DirectionListAdapter;
import com.appspot.smartshop.map.DirectionOverlay;
import com.appspot.smartshop.map.MapService;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

public class TestActivity extends MapActivity {
	public static final String TAG = "TestActivity";
	
	private MapView mapView;
	private MyLocationOverlay myLocationOverlay;
	private LocationManager locationManager;

	private ListView listDirection;

	private ArrayAdapter<String> adapter;

	private MapController mapController;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO test
		Global.currentActivity = this;
		testMap();
	}
	
	void testMap() {
		AlertDialog.Builder builder;
		AlertDialog alertDialog;

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.test,
		                               (ViewGroup) findViewById(R.id.layout_root));
		
		// setting for map view
		mapView = (MapView) view.findViewById(R.id.mapview);
		
		// controller
		mapController = mapView.getController();
		String coordinates[] = { "10.773267", "106.659501" };
		double lat = Double.parseDouble(coordinates[0]);
		double lng = Double.parseDouble(coordinates[1]);

		GeoPoint point = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
//		mapController.animateTo(point);
//		mapController.setZoom(16);
		
		// add an overlay
		GeoPoint[] points = new GeoPoint[] {
			point,
			new GeoPoint((int) (10.76734 * 1E6), (int) (106.659025 * 1E6)),
			new GeoPoint((int) (10.767719 * 1E6), (int) (106.66675 * 1E6)),
		};
		
		DirectionOverlay directionOverlay = new DirectionOverlay();
		directionOverlay.points = MapService.getDirectionInstructions(
				10.775242,106.661611,10.762257,106.656718).points;
		List<Overlay> listOfOverlays = mapView.getOverlays();
		listOfOverlays.clear();
		listOfOverlays.add(directionOverlay);
		
		// calculate lat, long span
		int maxLat = Integer.MIN_VALUE;
		int minLat = Integer.MAX_VALUE;
		int maxLong = Integer.MIN_VALUE;
		int minLong = Integer.MAX_VALUE;
		int lattitude;
		int longtitude;
		for (GeoPoint p: directionOverlay.points) {
			lattitude = p.getLatitudeE6();
			longtitude = p.getLongitudeE6();
			
			if (minLat > lattitude) {
				minLat = lattitude;
			}
			if (maxLat < lattitude) {
				maxLat = lattitude;
			}
			if (minLong > longtitude) {
				minLong = longtitude;
			}
			if (maxLong < longtitude) {
				maxLong = longtitude;
			}
		}
		
		GeoPoint center = new GeoPoint((maxLat + minLat) / 2, (maxLong + minLong) / 2);
		mapController.animateTo(center);
		mapController.zoomToSpan(maxLat - minLat, maxLong - minLong);

		// redraw the whole view
		mapView.invalidate();
		
		mapView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					System.out.println("touch");
					return true;
				}
				
				return false;
			}
		});

		builder = new AlertDialog.Builder(this);
		builder.setView(view);
		alertDialog = builder.create();

		alertDialog.show();
	}
	
	void testGeocoder() {
		Geocoder geocoder = new Geocoder(this);
		try {
			List<Address> addresses = geocoder.getFromLocationName(
					"268 lý thường kiệt quận 10 hcm", 10);
			if (addresses != null && addresses.size() > 0) {
				for (Address a : addresses) {
					Log.d(TAG, a.toString());
				}
				Log.d(TAG, "found " + addresses.size() + " addresses");
			} else {
				Log.d(TAG, "No address found");
			}
		} catch (IOException e) {
			Log.e(TAG, "Cannot get location from server");
			e.printStackTrace();
		}
	}
	
	void testParseJSON() {
		String json = "{" + "  \"query\": \"Pizza\", "
				+ "  \"locations\": [ 94043, 90210 ] " + "}";

		JSONObject object;
		try {
			object = (JSONObject) new JSONTokener(json).nextValue();
//			String query = object.getString("query");
			JSONArray locations = object.getJSONArray("locations");
			int len = locations.length();
			for (int i = 0; i < len; ++i) {
				System.out.println(locations.get(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	void testParseJSON2() {
		JSONObject jObject;
		String jString = "{\"menu\": {\"id\": \"file\", \"value\": \"File\", \"popup\": { \"menuitem\": [ {\"value\": \"New\",   \"onclick\": \"CreateNewDoc()\"}, {\"value\": \"Open\", \"onclick\": \"OpenDoc()\"}, {\"value\": \"Close\", \"onclick\": \"CloseDoc()\"}]}}}";
		try {
			/*
			 * {"menu": {
					  "id": "file",
					  "value": "File",
					  "popup": {
					    "menuitem": [
					      {"value": "New", "onclick": "CreateNewDoc()"},
					      {"value": "Open", "onclick": "OpenDoc()"},
					      {"value": "Close", "onclick": "CloseDoc()"}
					    ]
					  }
					}}

			 */
			jObject = new JSONObject(jString);

			JSONObject menuObject = jObject.getJSONObject("menu");
			String attributeId = menuObject.getString("id");
			System.out.println(attributeId);

			String attributeValue = menuObject.getString("value");
			System.out.println(attributeValue);

			JSONObject popupObject = menuObject.getJSONObject("popup");
			JSONArray menuitemArray = popupObject.getJSONArray("menuitem");

			for (int i = 0; i < 3; i++) {
				System.out.println(menuitemArray.getJSONObject(i)
						.getString("value").toString());
				System.out.println(menuitemArray.getJSONObject(i).getString(
						"onclick").toString());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	void testParseEmptyJSONArray() {
		String str = "{\"arr\":[]}";
		try {
			JSONArray arr = new JSONObject(str).getJSONArray("arr");
			System.out.println(arr.isNull(0));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	void testCreateJSONObject() {
		try {
//			JSONStringer js = new JSONStringer();
//			// Begin array
//			js.object().key("arr")
//			.value(
//					new JSONStringer().array()
//						.object().key("name").value("jason").
//						    key("email").value("jason@gmail.com").endObject()
//						.object().key("name").value("alex").
//						    key("email").value("alex@hotmail.com").endObject()
//						.endArray()
//			).endObject();
//			// end array
//			System.out.println(js.toString());
			
			JSONObject obj = new JSONObject();
			
			obj.put("single", "one");
			
			JSONObject mul = new JSONObject();
			mul.put("name", "name");
			mul.put("email", "email");
			obj.put("multiple", mul);
			
			JSONArray arr = new JSONArray();
			arr.put("val1");
			arr.put("val2");
			arr.put("val3");
			obj.put("array", arr);
			
			System.out.println(obj.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	void testCreateJSONObject2() {
		long start = System.currentTimeMillis();
		try {
			for (int i = 0; i < 1000; ++i) {
				JSONObject obj = new JSONObject();
				
				obj.put("single", "one");
				
				JSONObject mul = new JSONObject();
				mul.put("name", "name");
				mul.put("email", "email");
				obj.put("multiple", mul);
				
				JSONArray arr = new JSONArray();
				arr.put("val1");
				arr.put("val2");
				arr.put("val3");
				obj.put("array", arr);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		System.out.println("creating 1000 json object in = " + (System.currentTimeMillis() - start) + " ms");
	}
	
	void testRestClient() {
		String url = "http://search.twitter.com/trends.json";
		RestClient.parse(url, new JSONParser() {
			
			@Override
			public void process(JSONObject json) throws JSONException {
				System.out.println("as_of = " + json.getString("as_of"));
				JSONArray arrTrends = json.getJSONArray("trends");
				int len = arrTrends.length();
				JSONObject obj = null;
				for (int i = 0; i < len; ++i) {
					obj = arrTrends.getJSONObject(i);
					System.out.println("name = " + obj.getString("name"));
					System.out.println("url = " + obj.getString("url"));
				}
				
			}
		});
	}
	
	void testGoogleMapDirectionApi() {
		setContentView(R.layout.direction_list);
		listDirection = (ListView) findViewById(R.id.listDirection);
		
		// direction found
		String[] directions = MapService.getDirectionInstructions(
				10.787325f, 106.606493f,10.7f, 106.711378f).instructions;
		
		// no direction found
//		String[] directions = MapService.getDirectionInstructions(
//				10.787325f, 0f,10.7f, 106.711378f);
		
		adapter = new DirectionListAdapter(this, R.layout.direction_list_item, directions);
		listDirection.setAdapter(adapter);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}