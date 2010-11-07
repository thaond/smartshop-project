package vnfoss2010.smartshop.serverside.services.cat;

import java.util.List;
import java.util.Map;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.CategoryServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

public class GetSubCategoriesService extends BaseRestfulService{

	public GetSubCategoriesService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		CategoryServiceImpl db = CategoryServiceImpl.getInstance();
		
		JSONObject json = null;
		try {
			json = new JSONObject(content);
		} catch (Exception e) {
		}
		
		String parent_id = getParameter("parent_id", params, json);
		ServiceResult<List<Category>> result = db.getCategories(parent_id);
		
		JsonObject jsonReturn = new JsonObject();
		jsonReturn.addProperty("message", result.getMessage());
		jsonReturn.addProperty("errCode", result.isOK()?0:1);
		
		if (result.isOK()){
			jsonReturn.add("categories", Global.gsonDateWithoutHour.toJsonTree(result.getResult()));
		}
		
		return jsonReturn.toString();
	}

}
