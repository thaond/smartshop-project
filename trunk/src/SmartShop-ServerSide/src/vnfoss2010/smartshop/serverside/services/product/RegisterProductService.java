package vnfoss2010.smartshop.serverside.services.product;

import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.AttributeServiceImpl;
import vnfoss2010.smartshop.serverside.database.CategoryServiceImpl;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.beoui.geocell.GeocellManager;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.Gson;

public class RegisterProductService extends BaseRestfulService {

	private final static Logger log = Logger
			.getLogger(RegisterProductService.class.getName());

	private CategoryServiceImpl dbcat = CategoryServiceImpl.getInstance();

	private ProductServiceImpl dbProduct = ProductServiceImpl.getInstance();

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
		Product product = Global.gsonWithDate.fromJson(content, Product.class);
		log.log(Level.SEVERE, product+"");
		
		ServiceResult<Set<Category>> listCategories = dbcat
				.findCategories(product.getSetCategoryKeys());
		if (listCategories.isOK() == false) {
			jsonReturn.put("errCode", 1);
			jsonReturn.put("message", listCategories.getMessage());
		} else {
			product.setGeocells(GeocellManager.generateGeoCell(product
					.getLocation()));

			ServiceResult<Long> result = dbProduct.insertProduct(product);
			jsonReturn.put("errCode", result.isOK() ? 0 : 1);
			jsonReturn.put("message", result.getMessage());
			if (result.isOK()) {
				jsonReturn.put("product_id", result.getResult());
			}
		}
		return jsonReturn.toString();
	}
}
