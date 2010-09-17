package vnfoss2010.smartshop.serverside.services.usersubcribeproduct;

import java.util.Map;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.UserSubcribeProductImpl;
import vnfoss2010.smartshop.serverside.database.entity.UserSubcribeProduct;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

public class GetSubcribe extends BaseRestfulService {
	private static UserSubcribeProductImpl dbSub = UserSubcribeProductImpl
			.instance();

	public GetSubcribe(String serviceName) {
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
		Long id = Long.parseLong(getParameterWithThrow("id", params, json));
		try {
			ServiceResult<UserSubcribeProduct> serviceResult = dbSub
					.findSubcribe(id);
			if (serviceResult.isOK() == false) {
				throw new Exception(serviceResult.getMessage());

			}

			jsonReturn.addProperty("errCode", 0);
			jsonReturn.add("subcribe",
					Global.gsonWithDate.toJsonTree(serviceResult.getResult()));
			jsonReturn.addProperty("message", serviceResult.getMessage());
		} catch (Exception e) {
			jsonReturn.addProperty("errCode", 1);
			jsonReturn.addProperty("message", e.getMessage());
		}
		return jsonReturn.toString();
	}

}
