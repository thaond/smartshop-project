package vnfoss2010.smartshop.serverside.services.product;

import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.CategoryServiceImpl;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

public class GetProductService extends BaseRestfulService {
	ProductServiceImpl dbProduct = ProductServiceImpl.getInstance();
	CategoryServiceImpl dbCategory = CategoryServiceImpl.getInstance();

	private final static Logger log = Logger.getLogger(AccountServiceImpl.class
			.getName());

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
				product.setSetCategoryKeys(null);
				product.setGeocells(null);
				Set<Category> categoryList = catsResult.getResult();
				for (Category category : catsResult.getResult()){
					category.setListPages(null);
					category.setListProducts(null);
				}
				jsonReturn.addProperty("errCode", 0);
				jsonReturn.addProperty("message", Global.messages
						.getString("find_product_successfully"));
				jsonReturn.add("listCategories", Global.gsonWithDate.toJsonTree(categoryList));
				jsonReturn.add("product", Global.gsonWithDate.toJsonTree(product));
			}
		}

		Global.log(log, jsonReturn.toString());
		return jsonReturn.toString();

	}
}
