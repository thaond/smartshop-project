package vnfoss2010.smartshop.serverside.services.usersubcribeproduct;

import java.util.Date;
import java.util.List;
import java.util.Map;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.UserSubcribeProductImpl;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.UserSubcribeProduct;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GetProductInSubcribeRange extends BaseRestfulService {
	ProductServiceImpl dbProduct = ProductServiceImpl.getInstance();
	UserSubcribeProductImpl dbSubcribe = UserSubcribeProductImpl.getInstance();

	public GetProductInSubcribeRange(String serviceName) {
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
		Long subID = Long.parseLong(getParameterWithThrow("id", params, json));
		long fromRecord = 0;
		try {
			fromRecord = Long.parseLong(getParameter("from", params, json));
		} catch (Exception e) {
		}
		
		long toRecord = 0;
		try {
			toRecord = Long.parseLong(getParameter("to", params, json));
		} catch (Exception e) {
		}
		
		Date lastUpdate = null;
		try {
			lastUpdate = Global.dfFull.parse(getParameter("lastUpdate", params, json));
		} catch (Exception e) {
		}

		ServiceResult<UserSubcribeProduct> searchResult = dbSubcribe
				.findSubcribe(subID);
		if (searchResult.isOK()) {
			ServiceResult<List<Product>> result = dbSubcribe
					.searchProductInSubcribeRange(searchResult.getResult(), fromRecord, toRecord, lastUpdate);
			if (result.isOK()) {
				Gson gson = Global.gsonDateWithoutHour;
				JsonArray productArray = new JsonArray();
				for (Product product : result.getResult()) {
					productArray.add(gson.toJsonTree(product));
				}
				jsonReturn.addProperty("errCode", 0);
				jsonReturn.add("products", productArray);
				jsonReturn.addProperty("message", result.getMessage());
			} else {
				jsonReturn.addProperty("errCode", 1);
				jsonReturn.addProperty("message", result.getMessage());
			}
		} else {
			jsonReturn.addProperty("errCode", 1);
			jsonReturn.addProperty("message", searchResult.getMessage());
		}
		return jsonReturn.toString();
	}
}
