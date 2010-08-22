package vnfoss2010.smartshop.serverside.services.page;

import java.util.Map;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

import vnfoss2010.smartshop.serverside.database.PageServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.MissingParameterException;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

public class TagProductToPageService extends BaseRestfulService {
	private PageServiceImpl dbPage = PageServiceImpl.instance();

	public TagProductToPageService(String serviceName) {
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

		Long productID = Long.parseLong(getParameterWithThrow("productID",
				params, json));
		Long pageID = Long.parseLong(getParameterWithThrow("pageID", params,
				json));

		ServiceResult<Void> result = dbPage.tagProductToPage(pageID, productID);
		if (result.isOK()) {
			jsonReturn.addProperty("errCode", 0);
		} else {
			jsonReturn.addProperty("errCode", 1);
		}
		jsonReturn.addProperty("message", result.getMessage());

		return jsonReturn.toString();
	}

}