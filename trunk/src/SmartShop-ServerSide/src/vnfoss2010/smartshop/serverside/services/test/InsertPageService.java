package vnfoss2010.smartshop.serverside.services.test;

import java.util.ArrayList;
import java.util.Map;

import vnfoss2010.smartshop.serverside.database.PageServiceImpl;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.test.SampleDataNghia;

public class InsertPageService extends BaseRestfulService {
	PageServiceImpl db = PageServiceImpl.getInstance();
	public InsertPageService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		ArrayList<Page> pages = SampleDataNghia.getSamplePages();
		for (Page page : pages){
			db.insertPage(page);
		}

		return "Done";
	}
}
