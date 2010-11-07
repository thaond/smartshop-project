package vnfoss2010.smartshop.serverside.services.account;

import java.util.Map;

import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

public class LogoutService extends BaseRestfulService {
	AccountServiceImpl db = AccountServiceImpl.getInstance();

	public LogoutService(String serviceName) {
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

		JsonObject jsonReturn = new JsonObject();

		ServiceResult<Void> result = db.logout(username);
		if (result.isOK()) {
			jsonReturn.addProperty("errCode", 0);
			jsonReturn.addProperty("message", result.getMessage());
		} else {
			jsonReturn.addProperty("errCode", 1);
			jsonReturn.addProperty("message", result.getMessage());
		}
		return jsonReturn.toString();
	}

}
