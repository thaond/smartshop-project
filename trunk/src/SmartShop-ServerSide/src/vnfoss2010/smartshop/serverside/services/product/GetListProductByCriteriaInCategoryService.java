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
import vnfoss2010.smartshop.serverside.utils.StringUtils;
import vnfoss2010.smartshop.serverside.utils.UtilsFunction;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

public class GetListProductByCriteriaInCategoryService extends
		BaseRestfulService {

	ProductServiceImpl dbProduct = ProductServiceImpl.getInstance();
	private final static Logger log = Logger.getLogger(AccountServiceImpl.class
			.getName());

	public GetListProductByCriteriaInCategoryService(String serviceName) {
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

		String[] priceRangeStr;
		double[] priceRange = new double[2];
		try {
			priceRangeStr = getParameter("pricerange", params, json).split(",");
			priceRange[0] = Double.parseDouble(priceRangeStr[0]);
			priceRange[1] = Double.parseDouble(priceRangeStr[1]);
		} catch (Exception e) {
		}

		int[] criteriaIDs = null;
		if (criterias != null) {
			String[] arr = criterias.split(",");
			criteriaIDs = new int[arr.length];
			for (int i = 0; i < criteriaIDs.length; i++) {
				criteriaIDs[i] = Integer.parseInt(arr[i].trim());
			}
		}

		String username = null;
		try {
			username = getParameter("username", params, json);
		} catch (Exception e) {
		}

		String cat_keys = getParameter("cat_keys", params, json);
		Global.log(log, "Cat keys : " + cat_keys);

		// Query
		String q = null;
		try {
			q = getParameter("q", params, json);
			q = UtilsFunction.removeViSign(q);
		} catch (Exception e) {
		}

		ServiceResult<List<Product>> result = dbProduct
				.getListProductByCriteriaInCategories(
						maximum,
						criteriaIDs,
						status,
						q,
						username,
						priceRange,
						StringUtils.isEmptyOrNull(cat_keys) ? null : cat_keys
								.split(","));
		JsonObject jsonReturn = new JsonObject();

		jsonReturn.addProperty("errCode", result.isOK() ? 0 : 1);
		jsonReturn.addProperty("message", result.getMessage());

		if (result.isOK()) {
			jsonReturn.add("products",
					Global.gsonWithDate.toJsonTree(result.getResult()));
		}

		Global.log(log, jsonReturn.toString());
		return jsonReturn.toString();
	}

}
