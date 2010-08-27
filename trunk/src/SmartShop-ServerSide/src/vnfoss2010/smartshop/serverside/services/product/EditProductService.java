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

import com.beoui.geocell.GeocellManager;
import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.Gson;

public class EditProductService extends BaseRestfulService {
	ProductServiceImpl dbProduct = ProductServiceImpl.getInstance();
	CategoryServiceImpl dbCat = CategoryServiceImpl.getInstance();

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
		ProductServiceImpl.updateFTSStuffForProduct(editProduct);
		log.log(Level.SEVERE, editProduct.toString());

		ServiceResult<Set<Category>> listCategory = dbCat
				.findCategories(editProduct.getSetCategoryKeys());
		if (listCategory.isOK()) {
			editProduct.setGeocells(GeocellManager.generateGeoCell(editProduct
					.getLocation()));
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
}