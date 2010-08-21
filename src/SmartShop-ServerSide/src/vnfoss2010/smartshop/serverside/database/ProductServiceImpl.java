package vnfoss2010.smartshop.serverside.database;

import javax.jdo.PersistenceManager;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.entity.Product;

public class ProductServiceImpl {
	private static ProductServiceImpl instance;

	private ProductServiceImpl() {
	}

	public ServiceResult<Product> findProduct(Long id) {
		ServiceResult<Product> result = new ServiceResult<Product>();
		Product product = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		product = pm.getObjectById(Product.class, id);
		if (product == null) {
			result.setOK(false);
			result.setMessage(Global.messages.getString("no_found_product"));
		} else {
			result.setOK(true);
			result.setResult(product);
		}
		return result;
	}

	public static ProductServiceImpl instance() {
		if (instance == null) {
			instance = new ProductServiceImpl();
		}
		return instance;
	}
}
