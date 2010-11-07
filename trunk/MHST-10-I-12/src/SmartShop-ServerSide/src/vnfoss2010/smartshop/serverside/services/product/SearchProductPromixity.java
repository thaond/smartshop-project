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
import vnfoss2010.smartshop.serverside.utils.StringUtils;
import vnfoss2010.smartshop.serverside.utils.UtilsFunction;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.GeocellQuery;
import com.beoui.geocell.model.Point;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

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

		int limit = 0;
		try {
			limit = Integer.parseInt(getParameter("limit", params, json));
		} catch (Exception e) {
		}

		String q = getParameter("q", params, json);
		if (!StringUtils.isEmptyOrNull(q))
			q = UtilsFunction.removeViSign(q);

		log.log(Level.SEVERE, "Query: " + q);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<Object> pa = new ArrayList<Object>();
		GeocellQuery baseQuery = new GeocellQuery(" ", " ", pa);

		List<Product> objects = null;
		try {
			objects = GeocellManager.proximityFetchWithQuery(center, limit,
					maxDistance, q, Product.class, pm,
					GeocellManager.MAX_GEOCELL_RESOLUTION);
		} catch (Exception e) {
			// We catch excption here because we have not configured the
			// PersistentManager (and so the queries won't work)
			e.printStackTrace();
			return e.toString();
		}
		
		JsonObject jsonReturn = new JsonObject();
		if (objects!=null){
			//Success
			jsonReturn.addProperty("errCode", 0);
			jsonReturn.addProperty("message", Global.messages.getString("search_product_successfully"));
			jsonReturn.add("products", Global.gsonWithDate.toJsonTree(objects));
		}else{
			jsonReturn.addProperty("errCode", 1);
			jsonReturn.addProperty("message", Global.messages.getString("search_product_fail"));
		}
		return jsonReturn.toString();
	}
}
