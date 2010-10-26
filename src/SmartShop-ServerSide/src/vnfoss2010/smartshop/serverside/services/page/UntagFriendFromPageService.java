package vnfoss2010.smartshop.serverside.services.page;

import java.util.Map;

import vnfoss2010.smartshop.serverside.database.PageServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;

public class UntagFriendFromPageService extends BaseRestfulService{

	PageServiceImpl dbPage = PageServiceImpl.getInstance();	
	public UntagFriendFromPageService(String serviceName) {
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
		long pageID = Long.parseLong(getParameterWithThrow("pageID", params,
				json));
		String[] usernames = getParameterWithThrow("usernames", params, json)
				.split(",");
		String username = getParameterWithThrow("username", params, json);
		ServiceResult<Void> result = dbPage.untagFriendFromPage(pageID, usernames,
				username);

		jsonReturn.put("errCode", result.isOK() ? 0 : 1);
		jsonReturn.put("message", result.getMessage());
		return jsonReturn.toString();
	}

}
