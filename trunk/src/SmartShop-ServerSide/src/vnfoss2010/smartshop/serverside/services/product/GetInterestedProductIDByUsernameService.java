package vnfoss2010.smartshop.serverside.services.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.utils.UtilsFunction;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

public class GetInterestedProductIDByUsernameService extends BaseRestfulService {
	ProductServiceImpl db = ProductServiceImpl.getInstance();

	public GetInterestedProductIDByUsernameService(String serviceName) {
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

		String username = getParameter("username", params, json);

		Integer limit = 0;
		try {
			limit = Integer.parseInt(getParameter("limit", params, json));
		} catch (Exception e) {
		}

		// Query
		String q = null;
		try {
			q = getParameter("q", params, json);
			q = UtilsFunction.removeViSign(q);
		} catch (Exception e) {
		}

		JsonObject jsonReturn = new JsonObject();

		ServiceResult<List<Product>> result = db
				.getListInterestedProductsByUsername(username, q, limit);
		if (result.isOK()) {
			jsonReturn.addProperty("errCode", 0);
			jsonReturn.addProperty("message", result.getMessage());
			ArrayList<Long> ids = new ArrayList<Long>();
			for (Product product : result.getResult()){
				ids.add(product.getId());
			}
			jsonReturn.add("productsid",
					Global.gsonWithDate.toJsonTree(ids));
		} else {
			jsonReturn.addProperty("errCode", 1);
			jsonReturn.addProperty("message", result.getMessage());
		}
		return jsonReturn.toString();
	}

}
