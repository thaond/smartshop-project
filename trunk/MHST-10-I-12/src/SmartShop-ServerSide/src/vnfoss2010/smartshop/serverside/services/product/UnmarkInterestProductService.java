package vnfoss2010.smartshop.serverside.services.product;

import java.util.List;
import java.util.Map;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

public class UnmarkInterestProductService extends BaseRestfulService {

	ProductServiceImpl dbProduct = ProductServiceImpl.getInstance();

	public UnmarkInterestProductService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		JsonObject jsonReturn = new JsonObject();
		JSONObject json = null;
		try {
			json = new JSONObject(content);
		} catch (Exception e) {
		}
		String username = getParameterWithThrow("username", params, json);
		String products[] = getParameterWithThrow("productsid", params, json)
				.split(",");
		ServiceResult<List<Long>> result = dbProduct.markAsInterestedProduct(
				username, products,false);
		if (result.isOK()) {
			jsonReturn.add("prodictsid",
					Global.gsonWithDate.toJsonTree(result.getResult()));
			jsonReturn.addProperty("errCode", 0);
		} else {
			jsonReturn.addProperty("errCode", 1);
		}
		jsonReturn.addProperty("message", result.getMessage());
		return jsonReturn.toString();
	}

}
