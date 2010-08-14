package vnfoss2010.smartshop.serverside.services.account;

import java.util.Date;
import java.util.Map;

import com.google.appengine.repackaged.org.json.JSONObject;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.DatabaseServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

public class LoginService extends BaseRestfulService {
	DatabaseServiceImpl db = DatabaseServiceImpl.getInstance();

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

		JSONObject jsonReturn = new JSONObject();

		ServiceResult<UserInfo> result = db.login(username, password);
		if (result.isOK()) {
			jsonReturn.put("errCode", 0);
			jsonReturn.put("message", result.getMessage());
			
			result.getResult().toJSON(jsonReturn);
		} else {
			jsonReturn.put("errCode", 1);
			jsonReturn.put("message", result.getMessage());
		}
		return jsonReturn.toString();
	}

}
