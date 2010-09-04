package vnfoss2010.smartshop.serverside.services.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.CategoryServiceImpl;
import vnfoss2010.smartshop.serverside.database.PageServiceImpl;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.test.SampleData;
import vnfoss2010.smartshop.serverside.test.SampleDataNghia;

import com.beoui.geocell.GeocellManager;

public class InsertAllSampleDataService extends BaseRestfulService {
	public InsertAllSampleDataService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		//Insert sample userinfos
		AccountServiceImpl db = AccountServiceImpl.getInstance();
		ServiceResult<Void> result = db.insertAllUserInfos(SampleData
				.getSampleListUserInfos());
		
		//Insert sample products
		ProductServiceImpl dbProduct = ProductServiceImpl.getInstance();
		List<Product> list = SampleData.getSampleProducts();
		for (Product product : list) {
			product.setGeocells(GeocellManager.generateGeoCell(product
					.getLocation()));
			dbProduct.insertProduct(product);
		}
		
		//Insert sample pages
		PageServiceImpl dbPage = PageServiceImpl.getInstance();
		ArrayList<Page> pages = SampleDataNghia.getSamplePages();
		for (Page page : pages){
			dbPage.insertPage(page);
		}
		
		//Insert sample categories
		CategoryServiceImpl dbCat = CategoryServiceImpl.getInstance();
		ArrayList<Category> categories = SampleData.getSampleCategories();
		for (Category category : categories){
			dbCat.insertCategory(category);
		}
		
		return "Done";
	}

}
