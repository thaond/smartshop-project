package vnfoss2010.smartshop.serverside.services.product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import vnfoss2010.smartshop.serverside.database.AttributeServiceImpl;
import vnfoss2010.smartshop.serverside.database.CategoryServiceImpl;
import vnfoss2010.smartshop.serverside.database.DatabaseServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Attribute;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.MissingParameterException;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.beoui.geocell.GeocellManager;
import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.Gson;

public class RegisterProductService extends BaseRestfulService {

	private final static Logger log = Logger
			.getLogger(RegisterProductService.class.getName());

	private AttributeServiceImpl attributeImpl = AttributeServiceImpl
			.getInstance();
	private CategoryServiceImpl dbcat = CategoryServiceImpl.instance();
	private AttributeServiceImpl dbatt = AttributeServiceImpl.getInstance();

	private DatabaseServiceImpl db = DatabaseServiceImpl.getInstance();

	public RegisterProductService(String serviceName) {
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
		Gson gson = new Gson();
		Product product = gson.fromJson(content, Product.class);
		// log.log(Level.SEVERE, product.getSetAttributes().size() + "");
		// JSONArray array = getJSONArrayWithThrow("setAttributes", json);
		// for (int i = 0; i < array.length(); i++) {
		// log.log(Level.SEVERE, array.getString(i) + "");
		// log.log(Level.SEVERE, gson.fromJson(array.getString(i),
		// Attribute.class).toString()
		// + "___gson");
		// }

		ServiceResult<ArrayList<Category>> listCategories = dbcat
				.findCategories(product.getSetCategoryKeys());
		if (listCategories.isOK() == false) {
			jsonReturn.put("errCode", 1);
			jsonReturn.put("message", listCategories.getMessage());
		}

		if (jsonReturn.has("errCode") == false) {
			ServiceResult<Long> result = db.insertProduct(product);

			jsonReturn.put("errCode", result.isOK() ? 0 : 1);
			jsonReturn.put("message", result.getMessage());
		}
		return jsonReturn.toString();
		// return "here " + result.getMessage() + "_" + result.getResult();
	}

	private String getParameterWithThrow(String parameterName,
			Map<String, String[]> params, JSONObject json)
			throws MissingParameterException {
		String result = getParameter(parameterName, params, json);
		if (result == null) {
			throw missingParameter(parameterName);
		}
		return result;
	}

	private JSONArray getJSONArrayWithThrow(String parameterName,
			JSONObject json) throws MissingParameterException {
		JSONArray jsonArray = getJSONArray(parameterName, json);
		if (jsonArray == null) {
			throw missingParameter(parameterName);
		}
		return jsonArray;
	}
}
