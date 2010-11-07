package vnfoss2010.smartshop.serverside.services.account;

import java.util.Map;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

public class GetUserTaggedFromProduct extends BaseRestfulService {
	ProductServiceImpl dbProduct = ProductServiceImpl.getInstance();

	public GetUserTaggedFromProduct(String serviceName) {
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
		long id = Long.parseLong(getParameterWithThrow("id", params, json));
		ServiceResult<Product> accountResult = dbProduct.findProduct(id);
		if (accountResult.isOK()) {
			jsonReturn.addProperty("errCode", 0);
			jsonReturn.add("productIDs", Global.gsonWithDate
					.toJsonTree(accountResult.getResult()
							.getSetFriendsTaggedID()));
			jsonReturn.addProperty("message", Global.messages
					.getString("get_tagged_usernames_from_product_successfully"));
		} else {
			jsonReturn.addProperty("message", accountResult.getMessage());
			jsonReturn.addProperty("errCode", 1);
		}
		return jsonReturn.toString();
	}

}
