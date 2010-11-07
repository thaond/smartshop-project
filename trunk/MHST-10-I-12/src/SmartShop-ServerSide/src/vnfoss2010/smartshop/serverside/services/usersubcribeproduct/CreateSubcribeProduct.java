package vnfoss2010.smartshop.serverside.services.usersubcribeproduct;

import java.util.Map;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.UserSubcribeProductImpl;
import vnfoss2010.smartshop.serverside.database.entity.UserSubcribeProduct;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.beoui.geocell.GeocellManager;
import com.google.appengine.repackaged.org.json.JSONObject;

public class CreateSubcribeProduct extends BaseRestfulService {
	UserSubcribeProductImpl dbUserSubcribe = UserSubcribeProductImpl
			.getInstance();

	public CreateSubcribeProduct(String serviceName) {
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
		UserSubcribeProduct subcribe = Global.gsonWithDate.fromJson(content,
				UserSubcribeProduct.class);
		String message = "";
		if (subcribe.getLat() == null || subcribe.getLng() == null) {
			jsonReturn.put("errCode", 1);
			jsonReturn.put("message", message);
		} else {
			subcribe.setGeocells(GeocellManager.generateGeoCell(subcribe
					.getLocation()));
			subcribe.setUsername(getParameterWithThrow("username", params, json));
			ServiceResult<Long> result = dbUserSubcribe
					.insertSubcribe(subcribe);
			if (result.isOK()) {
				jsonReturn.put("errCode", 0);
				jsonReturn.put("id", result.getResult());
			} else {
				jsonReturn.put("errCode", 1);
			}
			jsonReturn.put("message", result.getMessage());
		}
		return jsonReturn.toString();
	}

}
