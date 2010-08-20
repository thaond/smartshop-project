package vnfoss2010.smartshop.serverside.services.product;

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
		Product product2 = gson.fromJson(content, Product.class);

		log.info(product2.toString());
		log.log(Level.SEVERE, product2.toString());

		String userName = getParameterWithThrow("username", params, json);

		Product product = new Product();
		product.setAddress(getParameterWithThrow("address", params, json));

		product.setIs_vat(Boolean.getBoolean((getParameterWithThrow("is_vat",
				params, json))));
		product.setLat((Double.parseDouble(getParameterWithThrow("lat", params,
				json))));
		product.setLng(Double.parseDouble((getParameterWithThrow("lng", params,
				json))));
		product.setName((getParameterWithThrow("name", params, json)));
		product.setOrigin((getParameterWithThrow("origin", params, json)));
		product.setPrice(Double.parseDouble((getParameterWithThrow("price",
				params, json))));
		product.setQuantity(Integer.parseInt((getParameterWithThrow("quantity",
				params, json))));
		product.setUsername(userName);

		JSONArray jsonAttArray = getJSONArrayWithThrow("atts", json);
		int attsLength = jsonAttArray.length();
		HashSet<Attribute> attsList = new HashSet<Attribute>();
		for (int i = 0; i < attsLength; i++) {
			JSONObject anAttJSON = jsonAttArray.getJSONObject(i);
			String catKey = getParameterWithThrow("key_cat", params, anAttJSON);
			Attribute attribute = null;
			ServiceResult<Boolean> resultInsertAtt = null;

			attribute = new Attribute(catKey, getParameterWithThrow("name",
					params, anAttJSON), getParameterWithThrow("value", params,
					anAttJSON), userName);
			resultInsertAtt = dbatt.insertAttribute(attribute);
			if (resultInsertAtt.isOK()) {
				attsList.add(attribute);
			}

			if (resultInsertAtt == null || resultInsertAtt.isOK() == false) {
				throw missingParameter("catKey in attributes");
			}
		}
		// PMF.get().getPersistenceManager().flush();
		product.setSetAttributes(attsList);

		JSONArray jsonCatArray = getJSONArrayWithThrow("cats", json);
		HashSet<String> catSet = new HashSet<String>();
		for (int i = 0; i < jsonCatArray.length(); i++) {
			JSONObject jsonCat = jsonCatArray.getJSONObject(i);
			String catID = (String) jsonCat.get("id");
			ServiceResult<Category> resultFindCat = dbcat.findCategory(catID);
			if (resultFindCat == null || resultFindCat.isOK() == false) {
				throw missingParameter("cats id");
			}
			catSet.add(catID);
		}
		product.setSetCategoryKeys(catSet);
		product.setGeocells(GeocellManager.generateGeoCell(product
				.getLocation()));

		ServiceResult<Long> result = db.insertProduct(product);

		jsonReturn.put("errCode", result.isOK() ? 0 : 1);
		jsonReturn.put("message", result.getMessage());
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
