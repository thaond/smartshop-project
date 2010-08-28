package vnfoss2010.smartshop.serverside.services.test;

import java.util.List;
import java.util.Map;

import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.test.SampleData;

public class InsertSampleProductService extends BaseRestfulService{

	public InsertSampleProductService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		ProductServiceImpl db = ProductServiceImpl.getInstance();
		
		List<Product> list = SampleData.getSampleProducts();
		for (Product product : list){
			db.insertProduct(product);
		}
		
		return "Done";
	}

}
