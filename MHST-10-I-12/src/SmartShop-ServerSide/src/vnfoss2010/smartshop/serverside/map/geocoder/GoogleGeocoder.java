package vnfoss2010.smartshop.serverside.map.geocoder;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.net.HttpRequest;
import vnfoss2010.smartshop.serverside.utils.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GoogleGeocoder {
	private static String URL = "http://maps.google.com/maps/geo?output=json";

	public static List<Placemark> geocode(String address, String country)
			throws Exception {
		URL = (URL + "&q=" + URLEncoder.encode(address, "UTF-8") + "&key=" + Global.GOOGLE_APP_ID);

		String output = HttpRequest.get(URL).content;
		JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(output).getAsJsonObject();

		List<Placemark> listPlacemarks = new ArrayList<Placemark>();

		JsonArray jsonArrayPlacemark = json.getAsJsonArray("Placemark");
		for (int i = 0; i < jsonArrayPlacemark.size(); i++) {
			Placemark p = new Placemark();
			JsonObject jsonPlacemark = jsonArrayPlacemark.getAsJsonObject(i);
			JsonObject jsonCountry = jsonPlacemark.getAsJsonObject(
					"AddressDetails").getAsJsonObject("Country");

			p.setFullAddress(jsonPlacemark.get("address").getAsString());
			p.setCountryCode(jsonCountry.getAsString("CountryNameCode"));
			p.setCountryName(jsonCountry.getAsString("CountryName"));
			p.setCity(jsonCountry.getAsJsonObject("AdministrativeArea")
					.getAsString("AdministrativeAreaName"));

			try {
				p.setZipCode(jsonCountry.getAsJsonObject("AdministrativeArea")
						.getAsJsonObject("SubAdministrativeArea")
						.getAsJsonObject("PostalCode").getAsString(
								"PostalCodeNumber"));
			} catch (Exception e) {
			}

			JsonArray jsonPoint = jsonPlacemark.getAsJsonObject("Point")
					.getAsJsonArray("coordinates");
			p.setPoint(new Point(jsonPoint.get(0).getAsDouble(), jsonPoint.get(
					1).getAsDouble(), jsonPoint.get(2).getAsDouble()));

			if (!StringUtils.isEmptyOrNull(country)) {
				if (p.getCountryName().equals(country)) {
					listPlacemarks.add(p);
				}
			} else {
				listPlacemarks.add(p);
			}

		}

		return listPlacemarks;
	}

	public static void main(String[] args) throws Exception {
		// System.out.println(GoogleGeocoder.geocode("268 Lý Thường Kiệt"));
		// System.out.println(TestGeocode.geocode("94103"));
	}
}