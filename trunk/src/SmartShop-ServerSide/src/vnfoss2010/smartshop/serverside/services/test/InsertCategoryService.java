package vnfoss2010.smartshop.serverside.services.test;

import java.util.ArrayList;
import java.util.Map;

import vnfoss2010.smartshop.serverside.database.CategoryServiceImpl;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.test.SampleData;

public class InsertCategoryService extends BaseRestfulService{
	private CategoryServiceImpl db = CategoryServiceImpl.instance();

	public InsertCategoryService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		ArrayList<Category> categories = SampleData.getSampleCategories();
		for (Category category : categories){
			db.insertCategory(category);
		}

		return "Done";
	}

}
