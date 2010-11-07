package vnfoss2010.smartshop.serverside.services.map;

import java.util.Map;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.map.geocoder.GoogleReserveGeocoder;
import vnfoss2010.smartshop.serverside.map.geocoder.YahooPlaceFinder;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.utils.StringUtils;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

public class ReserveGeocoderService extends BaseRestfulService {

	public ReserveGeocoderService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		JSONObject json = null;
		try {
			json = new JSONObject(content);
		} catch (Exception e) {
		}

		Double lat = Double.parseDouble(getParameterWithThrow("lat", params,
				json));
		Double lng = Double.parseDouble(getParameterWithThrow("lng", params,
				json));
		String req = lat + ",+" + lng;

		String src = getParameter("src", params, json);
		String country = getParameter("country", params, json);

		JsonObject jsonReturn = new JsonObject();
		if (!StringUtils.isEmptyOrNull(src)) {
			String[] srcs = src.split(",");
			for (String s : srcs) {
				if (s.equals("yahoo"))
					jsonReturn
							.add("yahoo", Global.gsonDateWithoutHour
									.toJsonTree(YahooPlaceFinder.geocode(req,
											country)));
				if (s.equals("google"))
					jsonReturn.add("google", Global.gsonDateWithoutHour
							.toJsonTree(GoogleReserveGeocoder.regeocode(lat,
									lng)));
			}
		} else {
			jsonReturn.add("yahoo", Global.gsonDateWithoutHour.toJsonTree(YahooPlaceFinder
					.geocode(req, country)));
			jsonReturn.add("google", Global.gsonDateWithoutHour
					.toJsonTree(GoogleReserveGeocoder.regeocode(lat, lng)));
		}

		return jsonReturn.toString();
	}
}
