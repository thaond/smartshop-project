package vnfoss2010.smartshop.serverside.services.product;

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
		
		String query = getParameterWithThrow("q", params, json);
		query = UtilsFunction.removeViSign(query);

		JsonObject jsonReturn = new JsonObject();

		ServiceResult<List<Product>> result = db.searchProductLike(query);
		if (result.isOK()) {
			jsonReturn.addProperty("errCode", 0);
			jsonReturn.addProperty("message", result.getMessage());
			
			jsonReturn.add("products", Global.gsonWithDate.toJsonTree(result.getResult()));
		} else {
			jsonReturn.addProperty("errCode", 1);
			jsonReturn.addProperty("message", result.getMessage());
		}
		return jsonReturn.toString();
	}

}
