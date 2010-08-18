package vnfoss2010.smartshop.serverside.services.product;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.smartcardio.ATR;

import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONObject;

import vnfoss2010.smartshop.serverside.database.AttributeServiceImpl;
import vnfoss2010.smartshop.serverside.database.CategoryServiceImpl;
import vnfoss2010.smartshop.serverside.database.DatabaseServiceImpl;
import vnfoss2010.smartshop.serverside.database.PMF;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Attribute;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.MissingParameterException;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

public class RegisterProductService extends BaseRestfulService {
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
			String catKey = getParameterWithThrow("catId", params, anAttJSON);
			Attribute attribute = null;
			ServiceResult<Boolean> resultInsertAtt = null;
			if (dbcat.findCategory(catKey).isOK()) {
				attribute = new Attribute(catKey, getParameterWithThrow("name",
						params, anAttJSON), getParameterWithThrow("value",
						params, anAttJSON), userName);
				resultInsertAtt = dbatt.insertAttribute(attribute);
				if (resultInsertAtt.isOK()) {
					attsList.add(attribute);
				}
			}

			if (resultInsertAtt == null || resultInsertAtt.isOK() == false) {
				throw missingParameter("catKey in attributes");
			}
		}
//		PMF.get().getPersistenceManager().flush();
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
