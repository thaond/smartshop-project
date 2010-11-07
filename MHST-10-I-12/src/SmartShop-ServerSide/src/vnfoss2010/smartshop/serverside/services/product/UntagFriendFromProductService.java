package vnfoss2010.smartshop.serverside.services.product;

import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

public class UntagFriendFromProductService extends BaseRestfulService {
	ProductServiceImpl dbProduct = ProductServiceImpl.getInstance();
	private final static Logger log = Logger
			.getLogger(UntagFriendFromProductService.class.getName());

	public UntagFriendFromProductService(String serviceName) {
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
		long productID = Long.parseLong(getParameterWithThrow("productID",
				params, json));
		String usernames[] = getParameterWithThrow("usernames", params, json)
				.split(",");
		String username = getParameterWithThrow("username", params, json);
		ServiceResult<Set<String>> result = dbProduct.tagFriendToProduct(
				productID, usernames, username, false);
		if (result.getResult() != null) {
			jsonReturn.add("setFriendTaggedID", Global.gsonWithDate.toJsonTree(
					result.getResult()));
		}
		jsonReturn.addProperty("errCode", result.isOK() ? 0 : 1);
		jsonReturn.addProperty("message", result.getMessage());
		return jsonReturn.toString();
	}

}
