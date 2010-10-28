package vnfoss2010.smartshop.serverside.services.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import vnfoss2010.smartshop.serverside.database.NotificationServiceImpl;
import vnfoss2010.smartshop.serverside.database.PageServiceImpl;
import vnfoss2010.smartshop.serverside.database.entity.Notification;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.test.SampleDataNghia;

public class InsertPageService extends BaseRestfulService {
	PageServiceImpl db = PageServiceImpl.getInstance();
	NotificationServiceImpl dbNo = NotificationServiceImpl.getInstance();

	public InsertPageService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		ArrayList<Page> pages = SampleDataNghia.getSamplePages();
		for (Page page : pages) {
			Long pageId = db.insertPage(page).getResult();

//			if (page.getUsername().equals("tam")) {
//				dbNo.insertNotification(new Notification(
//						"nghia đã comment vào bài viết của bạn", new Date(),
//						true, "page", pageId, "tam"));
//				dbNo.insertNotification(new Notification(
//						"duc đã comment vào bài viết của bạn", new Date(),
//						false, "page", pageId, "tam"));
//			} else if (page.getUsername().equals("nghia")) {
//				dbNo.insertNotification(new Notification(
//						"tam đã comment vào bài viết của bạn", new Date(),
//						true, "page", pageId, "nghia"));
//				dbNo.insertNotification(new Notification(
//						"duc đã comment vào bài viết của bạn", new Date(),
//						false, "page", pageId, "nghia"));
//			} else if (page.getUsername().equals("duc")) {
//				dbNo.insertNotification(new Notification(
//						"nghia đã comment vào bài viết của bạn", new Date(),
//						true, "page", pageId, "duc"));
//				dbNo.insertNotification(new Notification(
//						"loi đã comment vào bài viết của bạn", new Date(),
//						false, "page", pageId, "duc"));
//			} else if (page.getUsername().equals("loi")) {
//				dbNo.insertNotification(new Notification(
//						"nghia đã comment vào bài viết của bạn", new Date(),
//						true, "page", pageId, "loi"));
//				dbNo.insertNotification(new Notification(
//						"duc đã comment vào bài viết của bạn", new Date(),
//						false, "page", pageId, "loi"));
//			} else if (page.getUsername().equals("hieu")) {
//				dbNo.insertNotification(new Notification(
//						"nghia đã comment vào bài viết của bạn", new Date(),
//						true, "page", pageId, "hieu"));
//				dbNo.insertNotification(new Notification(
//						"duc đã comment vào bài viết của bạn", new Date(),
//						false, "page", pageId, "hieu"));
//			}
		}

		return "Done";
	}
}
