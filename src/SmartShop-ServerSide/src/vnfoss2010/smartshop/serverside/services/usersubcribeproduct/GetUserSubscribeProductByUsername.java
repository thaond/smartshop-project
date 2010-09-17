package vnfoss2010.smartshop.serverside.services.usersubcribeproduct;

import java.util.Date;
import java.util.List;
import java.util.Map;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.UserSubcribeProductImpl;
import vnfoss2010.smartshop.serverside.database.entity.UserSubcribeProduct;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

public class GetUserSubscribeProductByUsername extends BaseRestfulService {
	ProductServiceImpl dbProduct = ProductServiceImpl.getInstance();
	UserSubcribeProductImpl dbSubcribe = UserSubcribeProductImpl.getInstance();

	public GetUserSubscribeProductByUsername(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		JsonObject jsonReturn = new JsonObject();
		JSONObject json = null;
		try {
			json = new JSONObject(content);
		} catch (Exception ex) {
		}
		String username = getParameterWithThrow("username", params, json);
		int mode = 0;
		try {
			mode = Integer.parseInt(getParameter("mode", params, json));
		} catch (Exception e) {
		}
		String fromDateStr = getParameter("fromDate", params, json);
		String toDateStr = getParameter("toDate", params, json);
		
		Date fromDate = null, toDate = null;
		if (fromDateStr != null) {
			fromDate = Global.dfFull.parse(fromDateStr);
		}
		if (toDateStr != null) {
			toDate = Global.dfFull.parse(toDateStr);
		}
		if (fromDate != null && toDate != null && fromDate.after(toDate)) {
			throw new Exception(Global.messages.getString("invalid_date_format"));
		}
		
		ServiceResult<List<UserSubcribeProduct>> result = dbSubcribe
				.getUserSubscribeProductByUsername(username, fromDate, toDate, mode);
		if (result.isOK()) {
			jsonReturn.addProperty("errCode", 0);
			jsonReturn.addProperty("message", result.getMessage());
			jsonReturn.add("usersubscribeproducts", Global.gsonWithDate
					.toJsonTree(result.getResult()));
		} else {
			jsonReturn.addProperty("errCode", 1);
			jsonReturn.addProperty("message", result.getMessage());
		}
		return jsonReturn.toString();
	}
}
