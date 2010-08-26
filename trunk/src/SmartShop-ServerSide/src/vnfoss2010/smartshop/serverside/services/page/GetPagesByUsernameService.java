package vnfoss2010.smartshop.serverside.services.page;

import java.util.List;
import java.util.Map;

import vnfoss2010.smartshop.serverside.database.PageServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * 
 * @author VoMinhTam
 */
public class GetPagesByUsernameService extends BaseRestfulService {
	PageServiceImpl db = PageServiceImpl.getInstance();

	public GetPagesByUsernameService(String serviceName) {
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
		Gson gson = new Gson();

		ServiceResult<List<Page>> result = db.getListPageFromUsername(username);
		if (result.isOK()) {
			jsonReturn.addProperty("errCode", 0);
			jsonReturn.addProperty("message", result.getMessage());
			
			jsonReturn.add("pages", gson.toJsonTree(result.getResult()));
		} else {
			jsonReturn.addProperty("errCode", 1);
			jsonReturn.addProperty("message", result.getMessage());
		}
		return jsonReturn.toString();
	}

}
