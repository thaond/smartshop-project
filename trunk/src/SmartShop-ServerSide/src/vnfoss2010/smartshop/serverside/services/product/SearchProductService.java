package vnfoss2010.smartshop.serverside.services.product;

import java.util.List;
import java.util.Map;

import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class SearchProductService extends BaseRestfulService {
	ProductServiceImpl db = ProductServiceImpl.getInstance();

	public SearchProductService(String serviceName) {
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
		
		String query = getParameter("q", params, json);

		JsonObject jsonReturn = new JsonObject();
		Gson gson = new Gson();

		ServiceResult<List<Product>> result = db.searchProductLike(query);
		if (result.isOK()) {
			jsonReturn.addProperty("errCode", 0);
			jsonReturn.addProperty("message", result.getMessage());
			
			jsonReturn.add("products", gson.toJsonTree(result.getResult()));
		} else {
			jsonReturn.addProperty("errCode", 1);
			jsonReturn.addProperty("message", result.getMessage());
		}
		return jsonReturn.toString();
	}

}
