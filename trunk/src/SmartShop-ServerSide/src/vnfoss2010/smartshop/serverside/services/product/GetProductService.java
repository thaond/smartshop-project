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

public class GetProductService extends BaseRestfulService {
	ProductServiceImpl dbProduct = ProductServiceImpl.instance();
	CategoryServiceImpl dbCategory = CategoryServiceImpl.instance();

	public GetProductService(String serviceName) {
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
		Long id = new Long(getParameterWithThrow("id", params, json));
		Product product = null;
		ArrayList<Category> categoryList = null;
		ServiceResult<Product> productResult = dbProduct.findProduct(id);
		if (productResult.isOK() == false) {
			jsonReturn.put("errCode", 1);
			jsonReturn.put("message", productResult.getMessage());
		} else {
			product = productResult.getResult();
			categoryList = new ArrayList<Category>();
			for (String catKey : product.getSetCategoryKeys()) {
				ServiceResult<Category> categoryResult = dbCategory
						.findCategory(catKey);
				if (categoryResult.isOK()) {
					categoryList.add(categoryResult.getResult());
				} else {
					jsonReturn.put("errCode", 1);
					jsonReturn.put("message", categoryResult.getMessage());
					break;
				}
			}
		}
		if (jsonReturn.has("errCode") == false) {
			jsonReturn.put("errCode", 0);
			jsonReturn.put("id", product.getId());
			jsonReturn.put("name", product.getName());
			jsonReturn.put("lat", product.getLat());
			jsonReturn.put("lng", product.getLng());
			jsonReturn.put("price", product.getPrice());
			jsonReturn.put("quantity", product.getQuantity());
			jsonReturn.put("address", product.getAddress());
			jsonReturn.put("origin", product.getOrigin());
			jsonReturn.put("atts", product.getSetAttributes());

			String[] keyCats = new String[product.getSetCategoryKeys().size()];
			product.getSetCategoryKeys().toArray(keyCats);
			ArrayList<Category> listCategory = new ArrayList<Category>();
			for (int i = 0; i < keyCats.length; i++) {
				ServiceResult<Category> categoryResult = dbCategory
						.findCategory(keyCats[i]);
				listCategory.add(categoryResult.getResult());
			}
			jsonReturn.put("categories", listCategory);
			jsonReturn.put("username", product.getUsername());
			jsonReturn.put("warranty", product.getWarranty());
		}

		return jsonReturn.toString();
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
