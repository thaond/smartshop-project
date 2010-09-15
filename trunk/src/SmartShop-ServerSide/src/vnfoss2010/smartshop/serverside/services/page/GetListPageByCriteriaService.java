package vnfoss2010.smartshop.serverside.services.page;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.PageServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.utils.StringUtils;
import vnfoss2010.smartshop.serverside.utils.UtilsFunction;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

/**
 * 
 * @author VoMinhTam
 */
public class GetListPageByCriteriaService extends BaseRestfulService {

	PageServiceImpl dbProduct = PageServiceImpl.getInstance();
	private final static Logger log = Logger.getLogger(AccountServiceImpl.class
			.getName());

	public GetListPageByCriteriaService(String serviceName) {
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
		int[] criteriaIDs = null;
		if (criterias != null) {
			String[] arr = criterias.split(",");
			criteriaIDs = new int[arr.length];
			for (int i = 0; i < criteriaIDs.length; i++) {
				criteriaIDs[i] = Integer.parseInt(arr[i].trim());
			}
		}

		String cat_keys = getParameter("cat_keys", params, json);
		String username = null;
		try {
			username = getParameter("username", params, json);
		} catch (Exception e) {
		}

		// Query
		String q = null;
		try {
			q = getParameter("q", params, json);
			q = UtilsFunction.removeViSign(q);
		} catch (Exception e) {
		}

		ServiceResult<List<Page>> result = dbProduct.getListPageByCriteria(
				maximum, criteriaIDs, username, q, StringUtils
						.isEmptyOrNull(cat_keys) ? null : cat_keys.split(","));
		JsonObject jsonReturn = new JsonObject();

		jsonReturn.addProperty("errCode", result.isOK() ? 0 : 1);
		jsonReturn.addProperty("message", result.getMessage());

		if (result.isOK()) {
			jsonReturn.add("pages", Global.gsonWithDate.toJsonTree(result.getResult()));
		}

		Global.log(log, jsonReturn.toString());
		return jsonReturn.toString();
	}

}
