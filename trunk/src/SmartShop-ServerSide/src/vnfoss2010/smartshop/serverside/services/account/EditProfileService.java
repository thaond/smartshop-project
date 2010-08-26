package vnfoss2010.smartshop.serverside.services.account;

import java.util.Date;
import java.util.Map;

import com.google.appengine.repackaged.org.json.JSONObject;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

public class EditProfileService extends BaseRestfulService {
	AccountServiceImpl db = AccountServiceImpl.getInstance();

	public EditProfileService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		UserInfo userInfo = Global.gsonDateWithoutHour.fromJson(content, UserInfo.class);
		Global.log(null, userInfo + "");
		AccountServiceImpl.updateFTSStuffForUserInfo(userInfo);

		JSONObject jsonReturn = new JSONObject();
		ServiceResult<Void> result = db.editProfile(userInfo);
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
