package vnfoss2010.smartshop.serverside.map.geocoder;

import java.util.ArrayList;
import java.util.List;

import vnfoss2010.smartshop.serverside.net.HttpRequest;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GoogleReserveGeocoder {
	private static String URL = "http://maps.google.com/maps/api/geocode/json?latlng=%s,%s&sensor=false";

	public static List<Placemark> regeocode(Double lat, Double lng)
			throws Exception {
		URL = String.format(URL, lat, lng);

		String output = HttpRequest.get(URL).content;
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(output).getAsJsonObject();

		List<Placemark> listPlacemarks = new ArrayList<Placemark>();

		JsonArray jsonArrayResults = json.getAsJsonArray("results");
		for (int i = 0; i < jsonArrayResults.size(); i++) {
			Placemark p = new Placemark();

			p.setFullAddress(jsonArrayResults.getAsJsonObject(i).getAsString(
					"formatted_address"));
			listPlacemarks.add(p);
		}

		return listPlacemarks;
	}

	public static void main(String[] args) throws Exception {
		 System.out.println(GoogleReserveGeocoder.regeocode(10.12312,106.123));
	}
}