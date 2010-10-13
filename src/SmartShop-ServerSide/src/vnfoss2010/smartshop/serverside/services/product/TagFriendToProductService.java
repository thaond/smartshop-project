package vnfoss2010.smartshop.serverside.services.product;

import java.util.Map;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;
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
		String username = getParameterWithThrow("username", params, json);
		ServiceResult<Void> result = dbProduct.tagFriendToProduct(productID,
				username,true);
		if (result.isOK()) {
			jsonReturn.put("errCode", 0);
		} else {
			jsonReturn.put("errCode", 1);
		}
		jsonReturn.put("message", result.getMessage());
		return jsonReturn.toString();
	}
}
