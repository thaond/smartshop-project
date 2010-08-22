package vnfoss2010.smartshop.serverside.services.product;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import vnfoss2010.smartshop.serverside.database.CategoryServiceImpl;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.MissingParameterException;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.Gson;

public class EditProductService extends BaseRestfulService {
	ProductServiceImpl dbProduct = ProductServiceImpl.getInstance();
	CategoryServiceImpl dbCat = CategoryServiceImpl.instance();

	private final static Logger log = Logger.getLogger(EditProductService.class
			.getName());

	public EditProductService(String serviceName) {
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
		Product editProduct = gson.fromJson(content, Product.class);
		log.log(Level.SEVERE, editProduct.toString());

		ServiceResult<Set<Category>> listCategory = dbCat
				.findCategories(editProduct.getSetCategoryKeys());
		if (listCategory.isOK()) {
			ServiceResult<Void> editResult = dbProduct
					.updateProduct(editProduct);
			jsonReturn.put("errCode", editResult.isOK() ? 0 : 1);
			jsonReturn.put("message", editResult.getMessage());
		} else {
			jsonReturn.put("errCode", 1);
			jsonReturn.put("message", "Can't find category");
		}

		// return value;
		return jsonReturn.toString();
		// return null;
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