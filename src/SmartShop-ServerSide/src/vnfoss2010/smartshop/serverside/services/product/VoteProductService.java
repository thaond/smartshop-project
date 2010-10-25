package vnfoss2010.smartshop.serverside.services.product;

import java.util.Map;
import java.util.logging.Logger;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

public class VoteProductService extends BaseRestfulService {
	ProductServiceImpl dbProduct = ProductServiceImpl.getInstance();

	private final static Logger log = Logger.getLogger(AccountServiceImpl.class
			.getName());

	public VoteProductService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		JSONObject json = null;
		try {
			json = new JSONObject(content);
		} catch (Exception e) {
		}

		Long id = new Long(getParameterWithThrow("id", params, json));
		int star = 0;
		try {
			star = Integer.parseInt(getParameter("star", params, json));
		} catch (Exception e) {
		}
		
		ServiceResult<Void> result = dbProduct.voteProduct(id, star);
		JsonObject jsonReturn = new JsonObject();

		if (result.isOK() == false) {
			jsonReturn.addProperty("errCode", 1);
			jsonReturn.addProperty("message", result.getMessage());
		} else {
			jsonReturn.addProperty("errCode", 0);
			jsonReturn.addProperty("message", result.getMessage());
		}

		Global.log(log, jsonReturn.toString());
		return jsonReturn.toString();

	}
}
