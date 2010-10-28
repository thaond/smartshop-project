package vnfoss2010.smartshop.serverside.services.page;

import java.util.Map;
import java.util.Set;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.PageServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

public class TagFriendToPageService extends BaseRestfulService {

	PageServiceImpl dbPage = PageServiceImpl.getInstance();

	public TagFriendToPageService(String serviceName) {
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
		long pageID = Long.parseLong(getParameterWithThrow("pageID", params,
				json));
		String[] usernames = getParameterWithThrow("usernames", params, json)
				.split(",");
		String username = getParameterWithThrow("username", params, json);
		ServiceResult<Set<String>> result = dbPage.tagFriendToPage(pageID,
				usernames, username);

		if (result.getResult() != null) {
			jsonReturn.add("setFriendTaggedID",
					Global.gsonWithDate.toJsonTree(result.getResult()));	
		}
		
		jsonReturn.addProperty("errCode", result.isOK() ? 0 : 1);
		jsonReturn.addProperty("message", result.getMessage());
		return jsonReturn.toString();
	}
}
