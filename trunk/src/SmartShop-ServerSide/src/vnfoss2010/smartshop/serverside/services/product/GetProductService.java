package vnfoss2010.smartshop.serverside.services.product;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.CategoryServiceImpl;
import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.MissingParameterException;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class GetProductService extends BaseRestfulService {
	ProductServiceImpl dbProduct = ProductServiceImpl.getInstance();
	CategoryServiceImpl dbCategory = CategoryServiceImpl.instance();
	
	private final static Logger log = Logger
	.getLogger(AccountServiceImpl.class.getName());

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
		ServiceResult<Product> productResult = dbProduct.findProduct(id);
		
		JsonObject jsonReturn = new JsonObject();
		Gson gson = new Gson();
		
		if (productResult.isOK() == false) {
			jsonReturn.addProperty("errCode", 1);
			jsonReturn.addProperty("message", productResult.getMessage());
		} else {
			product = productResult.getResult();
			
			Global.log(log, product.toString());
			ServiceResult<Set<Category>> catsResult = dbCategory
					.findCategories(product.getSetCategoryKeys());

			if (catsResult.isOK() == false) {
				jsonReturn.addProperty("errCode", 1);
				jsonReturn.addProperty("message", catsResult.getMessage());
			} else {
				Set<Category> categoryList = catsResult.getResult();
				jsonReturn.addProperty("errCode", 1);
				jsonReturn.addProperty("message", Global.messages.getString("find_product_successfully"));
				jsonReturn.add("listCategories", gson.toJsonTree(categoryList));
				jsonReturn.add("product", gson.toJsonTree(product));
			}
		}
		
		Global.log(log, jsonReturn.toString());
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
