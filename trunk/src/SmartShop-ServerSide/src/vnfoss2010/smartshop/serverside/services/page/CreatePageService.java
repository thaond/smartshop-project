package vnfoss2010.smartshop.serverside.services.page;

import java.util.Map;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.CategoryServiceImpl;
import vnfoss2010.smartshop.serverside.database.PageServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CreatePageService extends BaseRestfulService {
	CategoryServiceImpl dbCat = CategoryServiceImpl.instance();
	PageServiceImpl dbPage = PageServiceImpl.getInstance();

	public CreatePageService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		JSONObject jsonReturn = new JSONObject();

		Gson gson;
		gson = Global.gsonWithDate;
		Page page = gson.fromJson(content, Page.class);
		ServiceResult<Long> result = dbPage.insertPage(page);
		if (result.isOK()) {
			jsonReturn.put("errCode", 0);
			jsonReturn.put("pageid", result.getResult());
		} else {
			jsonReturn.put("errCode", 1);
		}
		jsonReturn.put("message", result.getMessage());

		return jsonReturn.toString();
	}
}
