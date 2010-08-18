package vnfoss2010.smartshop.serverside.services.product;

import java.util.ArrayList;
import java.util.Map;

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
		if (jsonReturn.has("errCode") == false){
			jsonReturn.put("errCode", 0);
			jsonReturn.put("id", product.getId());
			jsonReturn.put("", product.getName());
			jsonReturn.put("", product.getLat());
			jsonReturn.put("", product.getLng());
			jsonReturn.put("", product.getPrice());
			jsonReturn.put("", product.getQuantity());
			jsonReturn.put("", product.getAddress());
			jsonReturn.put("", product.getOrigin());
			JSONArray jsonAtts = new JSONArray();
			for (Attribute att : product.getSetAttributes()){
				JSONObject jsonAtt = new JSONObject();
				jsonAtt.put("id", att.getId());
				jsonAtt.put("key_cat", att.getKey_cat());
				jsonAtt.put("username", att.getUsername());
				jsonAtt.put("value", att.getValue());
				// parse att to JSON
			}
//			jsonReturn.put("", product.get);
			
			
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
