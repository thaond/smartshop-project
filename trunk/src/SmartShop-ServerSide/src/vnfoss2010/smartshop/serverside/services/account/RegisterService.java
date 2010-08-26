package vnfoss2010.smartshop.serverside.services.account;

import java.util.Map;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;

public class RegisterService extends BaseRestfulService{
	
	private AccountServiceImpl db = AccountServiceImpl.getInstance();

	public RegisterService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		UserInfo userInfo = Global.gsonDateWithoutHour.fromJson(content, UserInfo.class);
		AccountServiceImpl.updateFTSStuffForUserInfo(userInfo);
		
//		JSONObject json = null;
//		try {
//			json = new JSONObject(content);
//		} catch (Exception e) {
//		}

//		UserInfo userInfo = new UserInfo();
//		userInfo.setUsername(getParameter("username", params, json));
//		userInfo.setPassword(getParameter("password", params, json));
//		userInfo.setFirst_name(getParameter("first_name", params, json));
//		userInfo.setLast_name(getParameter("last_name", params, json));
//		userInfo.setPhone(getParameter("phone", params, json));
//		userInfo.setEmail(getParameter("email", params, json));
//		userInfo.setAddress(getParameter("address", params, json));
//
//		Date birthday = null;
//		try {
//			birthday = Global.df.parse(getParameter("birthday", params, json));
//		} catch (Exception e) {
//		}
//		userInfo.setBirthday(birthday);
//
//		double lat = 0;
//		try {
//			lat = Double.parseDouble(getParameter("lat", params, json));
//		} catch (Exception e) {
//		}
//		userInfo.setLat(lat);
//
//		double lng = 0;
//		try {
//			lng = Double.parseDouble(getParameter("lng", params, json));
//		} catch (Exception e) {
//		}
//		userInfo.setLng(lng);

		JSONObject jsonReturn = new JSONObject();
		ServiceResult<Void> result = db.insertUserInfo(userInfo);
		if (result.isOK()) {
			jsonReturn.put("errCode", 0);
			jsonReturn.put("message", result.getMessage());
		} else {
			jsonReturn.put("errCode", 1);
			jsonReturn.put("message", result.getMessage());
		}
		return jsonReturn.toString();
	}

}
