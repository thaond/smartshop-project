package vnfoss2010.smartshop.serverside.services.page;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;

import vnfoss2010.smartshop.serverside.database.CategoryServiceImpl;
import vnfoss2010.smartshop.serverside.database.PageServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.MissingParameterException;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.utils.DateUtil;

import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CreatePageService extends BaseRestfulService {
	CategoryServiceImpl dbCat = CategoryServiceImpl.instance();
	PageServiceImpl dbPage = PageServiceImpl.instance();

	public CreatePageService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		JSONObject jsonReturn = new JSONObject();

		Gson gson;
		GsonBuilder builder = new GsonBuilder();
		gson = builder.setDateFormat("dd/MM/yyyy hh:mm").create();
		Page page = gson.fromJson(content, Page.class);
		ServiceResult<Boolean> result = dbPage.insertPage(page);
		jsonReturn.put("errCode", result.isOK() ? 0 : 1);
		jsonReturn.put("message", result.getMessage());

		return jsonReturn.toString();
	}
}
