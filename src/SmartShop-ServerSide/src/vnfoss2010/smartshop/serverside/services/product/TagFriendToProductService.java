package vnfoss2010.smartshop.serverside.services.product;

import java.util.Map;

import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;

public class TagFriendToProductService extends BaseRestfulService {
	ProductServiceImpl dbProduct = ProductServiceImpl.getInstance();

	public TagFriendToProductService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		JSONObject jsonReturn = new JSONObject();
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
		ServiceResult<Void> result = dbProduct.tagFriendToProduct(productID,
				usernames, username, true);
		if (result.isOK()) {
			jsonReturn.put("errCode", 0);
		} else {
			jsonReturn.put("errCode", 1);
		}
		jsonReturn.put("message", result.getMessage());
		return jsonReturn.toString();
	}
}
