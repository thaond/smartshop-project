package vnfoss2010.smartshop.serverside.services.product;

import java.util.Map;

import com.google.appengine.repackaged.org.json.JSONObject;

import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

public class CreateProductService extends BaseRestfulService {
	public CreateProductService(String serviceName) {
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

		JSONObject jsonReturn = new JSONObject();
		Product product = new Product();
		product.setAddress(getParameter("address", params, json));
		product.setIs_vat(Boolean.getBoolean((getParameter("is_vat", params, json))));
		product.setLat((Double.parseDouble(getParameter("", params, json))));
		product.setLng(Double.parseDouble((getParameter("", params, json))));
		product.setName((getParameter("", params, json)));
		product.setOrigin((getParameter("", params, json)));
		product.setPrice(Double.parseDouble((getParameter("", params, json))));
		product.setQuantity(Integer.parseInt((getParameter("", params, json))));
		
		
		
		return null;
	}
}
