package vnfoss2010.smartshop.serverside.services.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.PMF;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

public class TestProductAtt extends BaseRestfulService{
	private Logger log = Logger.getLogger(TestProductAtt.class.getName());
	
	public TestProductAtt(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery("select id from " + Product.class.getName());
//		query.setResult("id, name, price, is_vat, quantity, date_post, warranty, origin, lat, lng, address, address, description, product_view, username, listBuyers, setPagesID, setCategoryKeys, attributeSets");
		List<Long> list = (List<Long>) query.execute();
		log.log(Level.SEVERE, "listIn" + (list));
		
		List<Product> listProducts = new ArrayList<Product>();
		for (Long i : list){
			listProducts.add(pm.getObjectById(Product.class, i));
		}
		log.log(Level.SEVERE, "listProducts" + (listProducts));
		
//		List<Product> listProducts = (List<Product>) query.execute();
		
		log.log(Level.SEVERE, Global.gsonWithDate.toJsonTree(listProducts) + "");
		return null;
	}

}
