package vnfoss2010.smartshop.serverside.services.page;

import java.util.Map;
import java.util.Set;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.CategoryServiceImpl;
import vnfoss2010.smartshop.serverside.database.PageServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.MissingParameterException;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class GetPageService extends BaseRestfulService {
	private PageServiceImpl dbPage = PageServiceImpl.instance();
	private CategoryServiceImpl dbCat = CategoryServiceImpl.instance();

	public GetPageService(String serviceName) {
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
		Gson gson = Global.gson;
		Long id = Long.parseLong(getParameterWithThrow("id", params, json));

		ServiceResult<Page> pageResult = dbPage.findPage(id);
		ServiceResult serviceResult = pageResult;
		if (pageResult.isOK()) {
			// TODO option get cats and atts
			ServiceResult<Set<Category>> categoriesResult = dbCat
					.findCategories(pageResult.getResult().getSetCategoryKeys());
			if (categoriesResult.isOK()) {
				jsonReturn.addProperty("errCode", 0);
				jsonReturn.add("categories", gson.toJsonTree(categoriesResult
						.getResult()));
				pageResult.getResult().setSetCategoryKeys(null);
				jsonReturn.add("page", gson.toJsonTree(pageResult.getResult()));
			} else {
				jsonReturn.addProperty("errCode", 1);
				serviceResult = categoriesResult;
			}
		} else {
			jsonReturn.addProperty("errCode", 1);
		}
		jsonReturn.addProperty("message", serviceResult.getMessage());
		return jsonReturn.toString();
	}

}
