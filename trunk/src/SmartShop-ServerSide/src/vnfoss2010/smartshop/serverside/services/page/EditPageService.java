package vnfoss2010.smartshop.serverside.services.page;

import java.util.Map;

import com.google.appengine.repackaged.org.json.JSONObject;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.PageServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

public class EditPageService extends BaseRestfulService {
	PageServiceImpl dbPage = PageServiceImpl.getInstance();

	public EditPageService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		JSONObject jsonReturn = new JSONObject();
		Page page = Global.gsonWithDate.fromJson(content, Page.class);
		ServiceResult<Void> result = dbPage.updatePage(page);
		if (result.isOK()) {
			jsonReturn.put("errCode", 0);
		} else {
			jsonReturn.put("errCode", 1);
		}
		jsonReturn.put("message", result.getMessage());

		return jsonReturn.toString();
	}
}
