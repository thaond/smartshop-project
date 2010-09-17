package vnfoss2010.smartshop.serverside.services.usersubcribeproduct;

import java.util.List;
import java.util.Map;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.UserSubcribeProductImpl;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

public class GetProductInSubcribeRangeByUsername extends BaseRestfulService {
	ProductServiceImpl dbProduct = ProductServiceImpl.getInstance();
	UserSubcribeProductImpl dbSubcribe = UserSubcribeProductImpl.getInstance();

	public GetProductInSubcribeRangeByUsername(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		JsonObject jsonReturn = new JsonObject();
		JSONObject json = null;
		try {
			json = new JSONObject(content);
		} catch (Exception ex) {
		}
		String username = getParameterWithThrow("username", params, json);
		
		ServiceResult<List<Product>> result = dbSubcribe.getSubscribeProductByUsername(username);
		if (result.isOK()){
			jsonReturn.addProperty("errCode", 0);
			jsonReturn.addProperty("message", result.getMessage());
			jsonReturn.add("products", Global.gsonWithDate.toJsonTree(result.getResult()));
		}else{
			jsonReturn.addProperty("errCode", 0);
			jsonReturn.addProperty("message", result.getMessage());
		}
		return jsonReturn.toString();
	}
}
