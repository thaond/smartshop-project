package vnfoss2010.smartshop.serverside.services.account;

import java.util.Map;
import java.util.Set;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.authentication.SessionObject;
import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.utils.UtilsFunction;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

public class LoginService extends BaseRestfulService {
	AccountServiceImpl db = AccountServiceImpl.getInstance();

	public LoginService(String serviceName) {
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
		
		String username = getParameter("username", params, json);
		String password = getParameter("password", params, json);
		String userkey = getParameter("userkey", params, json);

		JsonObject jsonReturn = new JsonObject();
		
		UtilsFunction.clearExpiredSession();
		
		ServiceResult<UserInfo> result = db.login(username, password, userkey);
		if (result.isOK()) {
			jsonReturn.addProperty("errCode", 0);
			jsonReturn.addProperty("message", result.getMessage());
			
			jsonReturn.add("userinfo", Global.gsonDateWithoutHour.toJsonTree(result.getResult()));
		} else {
			jsonReturn.addProperty("errCode", 1);
			jsonReturn.addProperty("message", result.getMessage());
		}
		return jsonReturn.toString();
	}

}
