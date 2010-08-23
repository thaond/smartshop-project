package vnfoss2010.smartshop.serverside.services.map;

import java.util.Map;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.map.geocoder.YahooGeocoder;
import vnfoss2010.smartshop.serverside.map.geocoder.YahooPlaceFinder;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.utils.StringUtils;

public class GeocoderService extends BaseRestfulService{

	public GeocoderService(String serviceName) {
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
		
		String adr = getParameterWithThrow("adr", params, json);
		String src = getParameter("src", params, json);
		String country = getParameter("country", params, json);
		
		JsonObject jsonReturn = new JsonObject();
		if (!StringUtils.isEmptyOrNull(src)){
			String[] srcs = src.split(",");
			for (String s : srcs){
				if (s.equals("yahoo"))
					jsonReturn.add("yahoo", Global.gson.toJsonTree(YahooPlaceFinder.geocode(adr, country)));
				if (s.equals("google"))
					jsonReturn.add("google", Global.gson.toJsonTree(YahooPlaceFinder.geocode(adr, country)));
			}
		}else{
			jsonReturn.add("yahoo", Global.gson.toJsonTree(YahooPlaceFinder.geocode(adr, country)));
			jsonReturn.add("google", Global.gson.toJsonTree(YahooPlaceFinder.geocode(adr, country)));
		}
		
		return jsonReturn.toString();
	}
}
