package vnfoss2010.smartshop.serverside.services.usersubcribeproduct;

import com.beoui.geocell.GeocellManager;
import com.google.gson.JsonObject;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.UserSubcribeProductImpl;
import vnfoss2010.smartshop.serverside.database.entity.UserSubcribeProduct;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;

public class EditSubcribe extends BaseRestfulService {
	UserSubcribeProductImpl dbSubcribe = UserSubcribeProductImpl.getInstance();

	public EditSubcribe(String serviceName) {
		super(serviceName);
	}

	public String process(java.util.Map<String, String[]> params, String content)
			throws Exception,
			vnfoss2010.smartshop.serverside.services.exception.RestfulException {
		JsonObject jsonResult = new JsonObject();
		UserSubcribeProduct subcribe = Global.gsonWithDate.fromJson(content,
				UserSubcribeProduct.class);
		subcribe.setGeocells(GeocellManager.generateGeoCell(subcribe
				.getLocation()));
		ServiceResult<Void> result = dbSubcribe.editSubcribe(subcribe);
		if (result.isOK()) {
			jsonResult.addProperty("errCode", 0);
		} else {
			jsonResult.addProperty("errCode", 1);
		}
		jsonResult.addProperty("message", result.getMessage());
		return jsonResult.toString();
	}
}
