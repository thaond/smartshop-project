package vnfoss2010.smartshop.serverside.services.test;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.beoui.geocell.GeocellManager;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.test.SampleData;

public class InsertSampleProductService extends BaseRestfulService {
	Logger log = Logger.getLogger(InsertSampleProductService.class.getName());

	public InsertSampleProductService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		ProductServiceImpl db = ProductServiceImpl.getInstance();

		List<Product> list = SampleData.getSampleProducts();
		for (Product product : list) {
			product.setGeocells(GeocellManager.generateGeoCell(product
					.getLocation()));
			db.insertProduct(product);
		}

		return "Done";
	}

}
