package vnfoss2010.smartshop.serverside.services.account;

import java.util.Map;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

public class GetUserInfoService extends BaseRestfulService {

	private AccountServiceImpl db = AccountServiceImpl.getInstance();

	public GetUserInfoService(String serviceName) {
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
		ServiceResult<UserInfo> result = db.getUserInfo(username);
		JsonObject jsonReturn = new JsonObject();
		
		jsonReturn.addProperty("errCode", result.isOK()?0:1);
		jsonReturn.addProperty("message", result.getMessage());
		
		if (result.isOK()){
			jsonReturn.add("userinfo", Global.gsonDateWithoutHour.toJsonTree(result.getResult()));
		}
		
		return jsonReturn.toString();
	}
}
