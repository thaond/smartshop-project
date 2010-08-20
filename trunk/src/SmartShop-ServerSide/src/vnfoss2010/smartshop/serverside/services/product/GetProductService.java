package vnfoss2010.smartshop.serverside.services.product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import vnfoss2010.smartshop.serverside.database.CategoryServiceImpl;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Attribute;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.MissingParameterException;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GetProductService extends BaseRestfulService {
	ProductServiceImpl dbProduct = ProductServiceImpl.instance();
	CategoryServiceImpl dbCategory = CategoryServiceImpl.instance();

	public GetProductService(String serviceName) {
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
		Product product = null;
		ArrayList<Category> categoryList = null;
		ServiceResult<Product> productResult = dbProduct.findProduct(id);
		JsonObject jsonReturn = new JsonObject();
		Gson gson = new Gson();
		if (productResult.isOK() == false) {
			jsonReturn.add("errCode", gson.toJsonTree(1));
			jsonReturn.add("message", gson.toJsonTree(productResult.getMessage()));
		} else {
			product = productResult.getResult();
			ServiceResult<ArrayList<Category>> catsResult = dbCategory
					.findCategories(product.getSetCategoryKeys());

			if (catsResult.isOK() == false) {
				jsonReturn.add("errCode", gson.toJsonTree(1));
				jsonReturn.add("message",gson.toJsonTree(catsResult.getMessage()));
			} else {
				categoryList = catsResult.getResult();
				product.setSetCategoryKeys(null);
				product.setGeocells(null);
			}
		}
		if (jsonReturn.has("errCode") == false) {
			JsonArray catJsonArray = new JsonArray();
			for (Category category : categoryList) {
				catJsonArray.add(gson.toJsonTree(category));
			}

			jsonReturn = (JsonObject) gson.toJsonTree(product);
			jsonReturn.add("cats", catJsonArray);
		}
		return jsonReturn.toString();

		// old
		// jsonReturn.put("errCode", 0);
		// jsonReturn.put("id", product.getId());
		// jsonReturn.put("name", product.getName());
		// jsonReturn.put("lat", product.getLat());
		// jsonReturn.put("lng", product.getLng());
		// jsonReturn.put("price", product.getPrice());
		// jsonReturn.put("quantity", product.getQuantity());
		// jsonReturn.put("address", product.getAddress());
		// jsonReturn.put("origin", product.getOrigin());
		// jsonReturn.put("atts", product.getSetAttributes());
		//
		// String[] keyCats = new String[product.getSetCategoryKeys().size()];
		// product.getSetCategoryKeys().toArray(keyCats);
		// ArrayList<Category> listCategory = new ArrayList<Category>();
		// for (int i = 0; i < keyCats.length; i++) {
		// ServiceResult<Category> categoryResult = dbCategory
		// .findCategory(keyCats[i]);
		// listCategory.add(categoryResult.getResult());
		// }
		// jsonReturn.put("categories", listCategory);
		// jsonReturn.put("username", product.getUsername());
		// jsonReturn.put("warranty", product.getWarranty());
		// }

		// return jsonReturn.toString();
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
}
