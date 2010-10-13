package vnfoss2010.smartshop.serverside.services.product;

import java.util.Map;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

public class GetTaggedProductFromUser extends BaseRestfulService {
	AccountServiceImpl dbAccount = AccountServiceImpl.getInstance();

	public GetTaggedProductFromUser(String serviceName) {
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
		ServiceResult<UserInfo> accountResult = dbAccount.getUserInfo(username);
		if (accountResult.isOK()) {
			jsonReturn.addProperty("errCode", 0);
			jsonReturn.add("productIDs", Global.gsonWithDate
					.toJsonTree(accountResult.getResult()
							.getSetProductTaggedID()));
			jsonReturn.addProperty("message", Global.messages.getString("get_tagged_products_from_user_successfully"));
		} else {
			jsonReturn.addProperty("message", accountResult.getMessage());
			jsonReturn.addProperty("errCode", 1);
		}
		return jsonReturn.toString();
	}
}
