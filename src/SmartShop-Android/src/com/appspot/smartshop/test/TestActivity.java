package com.appspot.smartshop.test;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.dom.UserInfo;
import com.appspot.smartshop.map.MapDialog;
import com.appspot.smartshop.map.MapService;
import com.appspot.smartshop.map.MapDialog.UserLocationListener;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.gson.Gson;

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

		Global.application = this;
		testGetCurrentLocation(); // TODO (condorhero01): place test function
									// here
	}

	void testGetCurrentLocation() {
		LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

		LocationListener mlocListener = new MyLocationListener();

		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
	}

	class MyLocationListener implements LocationListener

	{

		@Override
		public void onLocationChanged(Location loc)

		{

			loc.getLatitude();
			loc.getLongitude();
			String Text = "My current location is: " + "Latitud = "
					+ loc.getLatitude() + "Longitud = " + loc.getLongitude();

			Toast.makeText(getApplicationContext(),

			Text,

			Toast.LENGTH_SHORT).show();

		}

		@Override
		public void onProviderDisabled(String provider)

		{

			Toast.makeText(getApplicationContext(),

			"Gps Disabled",

			Toast.LENGTH_SHORT).show();

		}

		@Override
		public void onProviderEnabled(String provider)

		{

			Toast.makeText(getApplicationContext(),

			"Gps Enabled",

			Toast.LENGTH_SHORT).show();

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
		}

	}/* End of Class MyLocationListener */

	void testGson4() {
		UserInfo userInfo = new UserInfo();
		userInfo.birthday = new Date(2010, 8, 11); // month = 8 means September
		String json = new Gson().toJson(userInfo);
		System.out.println(json);

		userInfo = new Gson().fromJson(json, UserInfo.class);
		System.out.println(Global.df.format(userInfo.birthday));
	}

	void testGson3() {
		UserInfo userInfo = new UserInfo();
		userInfo.birthday = new Date();
		String json = new Gson().toJson(userInfo);
		System.out.println(json);

		userInfo = new Gson().fromJson(json, UserInfo.class);
		System.out.println(Global.df.format(userInfo.birthday));
	}

	void testGson2() {
		Foo foo = new Foo(); // must have assignment field = null to
								// non-primitive field
		foo.username = "foo";
		String json = new Gson().toJson(foo);

		System.out.println(json);

		foo = new Gson().fromJson(json, Foo.class);
		System.out.println(foo);
	}

	void testGson() {
		Gson gson = new Gson();
		ProductInfo productInfo = new ProductInfo("laptop", 1000, "good");
		String json = gson.toJson(productInfo);
		System.out.println(json);

		ProductInfo productInfo2 = gson.fromJson(json, ProductInfo.class);
		System.out.println(productInfo2.name);
		System.out.println(productInfo2.description);
		System.out.println(productInfo2.price);
	}

	void testUserLocationDialog() {
		String[] locations = new String[] { "117 thành thái quận 10 hcm",
				"166/17 phạm phú thứ quận 6 hcm",
				"43 vương văn huống quận bình tân hcm", "tào lao",
				"ffsdfsdfdsfsfs", "new york" };
		GeoPoint point = MapService.locationToGeopoint(locations[1]);
		MapDialog.createLocationDialog(this, point, new UserLocationListener() {

			@Override
			public void processUserLocation(GeoPoint point) {
				Log.d(TAG, "user location = " + point);
			}
		}).show();
	}

	void testParseJSON() {
		String json = "{" + "  \"query\": \"Pizza\", "
				+ "  \"locations\": [ 94043, 90210 ] " + "}";

		JSONObject object;
		try {
			object = (JSONObject) new JSONTokener(json).nextValue();
			// String query = object.getString("query");
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
			 * {"menu": { "id": "file", "value": "File", "popup": { "menuitem":
			 * [ {"value": "New", "onclick": "CreateNewDoc()"}, {"value":
			 * "Open", "onclick": "OpenDoc()"}, {"value": "Close", "onclick":
			 * "CloseDoc()"} ] } }}
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
				System.out.println(menuitemArray.getJSONObject(i).getString(
						"value").toString());
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
			// JSONStringer js = new JSONStringer();
			// // Begin array
			// js.object().key("arr")
			// .value(
			// new JSONStringer().array()
			// .object().key("name").value("jason").
			// key("email").value("jason@gmail.com").endObject()
			// .object().key("name").value("alex").
			// key("email").value("alex@hotmail.com").endObject()
			// .endArray()
			// ).endObject();
			// // end array
			// System.out.println(js.toString());

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

		System.out.println("creating 1000 json object in = "
				+ (System.currentTimeMillis() - start) + " ms");
	}

	void testRestClient() {
		String url = "http://search.twitter.com/trends.json";
		RestClient.loadData(url, new JSONParser() {

			@Override
			public void onSuccess(JSONObject json) throws JSONException {
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

			@Override
			public void onFailure(String message) {
				Log.e(TAG, "fail");
				Log.e(TAG, message);
			}
		});
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}

class Foo {
	public Foo() {
	}

	public String username = null;
	public String pass = null;
	public Date birthday = null;

	@Override
	public String toString() {
		return "username = " + username + ", pass = " + pass + ", birthday = "
				+ birthday;
	}
}
