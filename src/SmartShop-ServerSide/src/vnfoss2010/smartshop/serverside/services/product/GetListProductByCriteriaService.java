package vnfoss2010.smartshop.serverside.services.product;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Deprecated
/**
 * Please use {@link GetListProductByCriteriaInCategoryService} instead
 */
public class GetListProductByCriteriaService extends BaseRestfulService {

	ProductServiceImpl dbProduct = ProductServiceImpl.getInstance();
	private final static Logger log = Logger
			.getLogger(AccountServiceImpl.class.getName());

	public GetListProductByCriteriaService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		JSONObject json = null;
		Gson gson = new Gson();
		try {
			json = new JSONObject(content);
		} catch (Exception e) {
		}
		
		int maximum = 0;
		try {
			maximum = Integer.parseInt(getParameter("maximum", params, json));
		} catch (Exception e) {
		}
		
		String criterias = getParameter("criterias", params, json);
		int status = 0;
		try {
			status = Integer.parseInt(getParameter("status", params, json));
		} catch (Exception e) {
		}
		
		String[] arr = criterias.split(",");
		int[] criteriaIDs = new int[arr.length];
		for (int i=0;i<criteriaIDs.length;i++){
			criteriaIDs[i] = Integer.parseInt(arr[i].trim());
		}
		
		String username = null;
		try {
			username = getParameter("username", params, json);
		} catch (Exception e) {
		}
		
		//Query
		String q = null;
		try {
			q = getParameter("q", params, json);
		} catch (Exception e) {
		}
		
		ServiceResult<List<Product>> result  = dbProduct.getListProductByCriteriaInCategories(maximum, criteriaIDs, status , q, username);
		JsonObject jsonReturn = new JsonObject();
		
		jsonReturn.addProperty("errCode", result.isOK() ? 0 : 1);
		jsonReturn.addProperty("message", result.getMessage());
		
		if (result.isOK()){
			jsonReturn.add("products", gson.toJsonTree(result.getResult()));
		}
		
		Global.log(log, jsonReturn.toString());
		return jsonReturn.toString();
	}

}
