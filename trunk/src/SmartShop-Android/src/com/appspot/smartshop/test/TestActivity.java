package com.appspot.smartshop.test;

import java.io.IOException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

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
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.DirectionListAdapter;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;

public class TestActivity extends MapActivity {
	public static final String TAG = "TestActivity";
	
	private MapView mapView;
	private MyLocationOverlay myLocationOverlay;
	private LocationManager locationManager;

	private ListView listDirection;

	private ArrayAdapter<String> adapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// TODO test
		testCreateJSONObject2();
	}
	
	void testMap() {
		AlertDialog.Builder builder;
		AlertDialog alertDialog;

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.test,
		                               (ViewGroup) findViewById(R.id.layout_root));
		
		mapView = (MapView) view.findViewById(R.id.mapview);
		mapView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println("click");
			}
		});
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

		builder = new AlertDialog.Builder(this);
		builder.setView(view);
		alertDialog = builder.create();

		alertDialog.show();
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
		adapter = new DirectionListAdapter(this, R.layout.direction_list_item);
		
		String url = "http://maps.google.com/maps/api/directions/json?origin=10.774802,106.664475&destination=10.770881,106.67308&sensor=true&language=vi&units=metric";
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
							Log.d(TAG, "instruction = " + instruction);
							adapter.add(instruction);
						}
					}
				}
				
				listDirection.setAdapter(adapter);
				
			}
		});
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}