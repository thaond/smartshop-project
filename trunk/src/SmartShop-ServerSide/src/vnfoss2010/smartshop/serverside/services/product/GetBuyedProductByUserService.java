package vnfoss2010.smartshop.serverside.services.product;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.utils.UtilsFunction;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GetBuyedProductByUserService extends BaseRestfulService {
	ProductServiceImpl dbProduct = ProductServiceImpl.getInstance();

	private final static Logger log = Logger.getLogger(AccountServiceImpl.class
			.getName());

	public GetBuyedProductByUserService(String serviceName) {
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

		String username = getParameterWithThrow("username", params, json);
		Integer limit = 0;
		try {
			limit = Integer.parseInt(getParameter("limit", params, json));
		} catch (Exception e) {
		}
		
		//Query
		String q = null;
		try {
			q = getParameter("q", params, json);
			q = UtilsFunction.removeViSign(q);
		} catch (Exception e) {
		}
		
		ServiceResult<List<Product>> productResult = dbProduct.getListBuyedProductsByUsername(username, q, limit);

		JsonObject jsonReturn = new JsonObject();
		Gson gson = new Gson();

		jsonReturn.addProperty("errCode", productResult.isOK()?0:1);
		jsonReturn.addProperty("message", productResult.getMessage());
		
		if (productResult.isOK() == true) {
			jsonReturn.add("products", Global.gsonDateWithoutHour.toJsonTree(productResult.getResult()));
		}

		Global.log(log, jsonReturn.toString());
		return jsonReturn.toString();
	}
}
