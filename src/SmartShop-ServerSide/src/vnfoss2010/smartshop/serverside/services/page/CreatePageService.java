package vnfoss2010.smartshop.serverside.services.page;

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
		JSONObject json = null;
		try {
			json = new JSONObject(content);
		} catch (Exception e) {
		}
		Page page = new Page();
		page.setContent(getParameterWithThrow("content", params, json));
		page.setDate_post(DateUtil.parseDate(getParameterWithThrow("date",
				params, json)));
		page.setName(getParameterWithThrow("name", params, json));
		page.setUsername(getParameterWithThrow("username", params, json));
		JSONArray jsonCatArray = getJSONArrayWithThrow("cats", json);
		HashSet<String> catSet = new HashSet<String>();
		for (int i = 0; i < jsonCatArray.length(); i++) {
			JSONObject jsonCat = jsonCatArray.getJSONObject(i);
			String catID = (String) jsonCat.get("id");
			ServiceResult<Category> resultFindCat = dbCat.findCategory(catID);
			if (resultFindCat == null || resultFindCat.isOK() == false) {
				throw missingParameter("cats id");
			}
			catSet.add(catID);
		}
		page.setSetCategoryKeys(catSet);
		page.setLast_modified(DateUtil.parseDate(getParameterWithThrow("date",
				params, json)));
		ServiceResult<Boolean> result = dbPage.insertPage(page);
		jsonReturn.put("errCode", result.isOK() ? 0 : 1);
		jsonReturn.put("message", result.getMessage());

		return jsonReturn.toString();
	}

	private String getParameterWithThrow(String parameterName,
			Map<String, String[]> params, JSONObject json)
			throws MissingParameterException {
		String result = getParameter(parameterName, params, json);
		if (result == null) {
			throw missingParameter(parameterName);
		}
		return result;
	}

	private JSONArray getJSONArrayWithThrow(String parameterName,
			JSONObject json) throws MissingParameterException {
		JSONArray jsonArray = getJSONArray(parameterName, json);
		if (jsonArray == null) {
			throw missingParameter(parameterName);
		}
		return jsonArray;
	}
}
