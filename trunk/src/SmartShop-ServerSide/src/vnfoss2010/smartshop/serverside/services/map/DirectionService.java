package vnfoss2010.smartshop.serverside.services.map;

import java.util.Map;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.map.direction.Direction;
import vnfoss2010.smartshop.serverside.net.HttpRequest;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;

public class DirectionService extends BaseRestfulService {
	public static final String GET_DIRECTION_URL = "http://maps.google.com/maps/api/directions/json?"
			+ "origin=%s"
			+ "&destination=%s"
			+ "&sensor=true";

	public DirectionService(String serviceName) {
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

		String origin = getParameterWithThrow("origin", params, json);
		String destination = getParameterWithThrow("destination", params, json);
		String travel_mode = getParameter("mode", params, json);
		String units = getParameter("units", params, json);
		String language = getParameter("language", params, json);

		StringBuilder strBuilderURL = new StringBuilder(String.format(
				GET_DIRECTION_URL, origin, destination));
		if (travel_mode.length() > 0)
			strBuilderURL.append("&mode=" + travel_mode);
		
		if (units.length() > 0)
			strBuilderURL.append("&units=" + units);
		else
			strBuilderURL.append("&units=metric");
		
		if (language.length() > 0)
			strBuilderURL.append("&language=" + language);
		else
			strBuilderURL.append("&language=vi");
		
		String result = HttpRequest.get(strBuilderURL.toString()).content;
		Direction direction = Global.gsonDateWithoutHour.fromJson(result,
				Direction.class);

		return Global.gsonDateWithoutHour.toJson(direction);
	}
}