package vnfoss2010.smartshop.serverside.test;

import java.util.ArrayList;
import java.util.Map;

import vnfoss2010.smartshop.serverside.database.CategoryServiceImpl;
import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

public class SampleDataRestfull extends BaseRestfulService {
	public SampleDataRestfull(String serviceName) {
		super(serviceName);
	}
	private AccountServiceImpl db = AccountServiceImpl.getInstance();
	private CategoryServiceImpl dbcat = CategoryServiceImpl.getInstance();

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		ArrayList<Category> categories = SampleData.getSampleCategories();
		ArrayList<UserInfo> userInfo = SampleData.getSampleListUserInfos();

		db.insertAllUserInfos(userInfo);
		for (Category category : categories){
			dbcat.insertCategory(category);
		}
		
		return "hello from sample";
	}
}
