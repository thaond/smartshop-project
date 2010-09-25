package vnfoss2010.smartshop.serverside.services.usersubcribeproduct;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.UserSubcribeProductImpl;
import vnfoss2010.smartshop.serverside.database.dom.Subscribe_ListProduct;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

public class GetProductInSubcribeRangeByUsername extends BaseRestfulService {
	Logger log = Logger.getLogger("GetProductInSubcribeRangeByUsername");
	ProductServiceImpl dbProduct = ProductServiceImpl.getInstance();
	UserSubcribeProductImpl dbSubcribe = UserSubcribeProductImpl.getInstance();

	public GetProductInSubcribeRangeByUsername(String serviceName) {
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
		
		ServiceResult<List<Subscribe_ListProduct>> result = dbSubcribe.getSubscribeProductByUsername(username, fromRecord, toRecord, lastUpdate);
		if (result.isOK()){
			jsonReturn.addProperty("errCode", 0);
			jsonReturn.addProperty("message", result.getMessage());
			jsonReturn.add("subscribe_listproduct", Global.gsonWithDate.toJsonTree(result.getResult()));
		}else{
			jsonReturn.addProperty("errCode", 0);
			jsonReturn.addProperty("message", result.getMessage());
		}
		return jsonReturn.toString();
	}
}
