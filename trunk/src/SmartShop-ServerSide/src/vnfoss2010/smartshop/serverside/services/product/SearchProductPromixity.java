package vnfoss2010.smartshop.serverside.services.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.PMF;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.GeocellQuery;
import com.beoui.geocell.model.Point;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

public class SearchProductPromixity extends BaseRestfulService {
	private final static Logger log = Logger
			.getLogger(SearchProductPromixity.class.getName());

	public SearchProductPromixity(String serviceName) {
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
		Point center = new Point(Double.parseDouble(getParameterWithThrow(
				"lat", params, json)), Double
				.parseDouble(getParameterWithThrow("lng", params, json)));
		Double maxDistance = Double.parseDouble(getParameterWithThrow(
				"distance", params, json));
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<Object> pa = new ArrayList<Object>();
		GeocellQuery baseQuery = new GeocellQuery(" ", " ", pa);

		List<Product> objects = null;
		try {
			objects = GeocellManager.proximityFetch(center, 40, maxDistance,
					Product.class, baseQuery, pm);
		} catch (Exception e) {
			// We catch excption here because we have not configured the
			// PersistentManager (and so the queries won't work)
			e.printStackTrace();
			return e.toString();
		}
		Gson gson = Global.gsonDateWithoutHour;
		JsonArray productArray = new JsonArray();
		for (Product product : objects) {
			productArray.add(gson.toJsonTree(product));
		}
		return productArray.toString();
	}
}
