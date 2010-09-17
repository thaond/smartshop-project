package vnfoss2010.smartshop.serverside.services.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.CategoryServiceImpl;
import vnfoss2010.smartshop.serverside.database.NotificationServiceImpl;
import vnfoss2010.smartshop.serverside.database.PageServiceImpl;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.UserSubcribeProductImpl;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Notification;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.UserSubcribeProduct;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.test.SampleData;
import vnfoss2010.smartshop.serverside.test.SampleDataNghia;

import com.beoui.geocell.GeocellManager;

public class InsertAllSampleDataService extends BaseRestfulService {
	NotificationServiceImpl dbNo = NotificationServiceImpl.getInstance();

	public InsertAllSampleDataService(String serviceName) {
		super(serviceName);
	}
	
	private Logger log = Logger.getLogger(InsertAllSampleDataService.class.getName());

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		// Insert sample userinfos
		AccountServiceImpl db = AccountServiceImpl.getInstance();
		ServiceResult<Void> result = db.insertAllUserInfos(SampleData
				.getSampleListUserInfos());

		// Insert sample products
		ProductServiceImpl dbProduct = ProductServiceImpl.getInstance();
		List<Product> list = SampleData.getSampleProducts();
		for (Product product : list) {
			product.setGeocells(GeocellManager.generateGeoCell(product
					.getLocation()));

			Long productId = dbProduct.insertProduct(product).getResult();

			log.log(Level.SEVERE, product.getUsername());
			if (product.getUsername().equals("tam")) {
				dbNo.insertNotification(new Notification(
						"nghia đã comment vào sản phẩm của bạn", new Date(),
						true, "product", productId, "tam"));
				dbNo.insertNotification(new Notification(
						"duc đã comment vào sản phẩm của bạn", new Date(),
						false, "product", productId, "tam"));
			} else if (product.getUsername().equals("nghia")) {
				dbNo.insertNotification(new Notification(
						"tam đã comment vào sản phẩm của bạn", new Date(),
						true, "product", productId, "nghia"));
				dbNo.insertNotification(new Notification(
						"duc đã comment vào sản phẩm của bạn", new Date(),
						false, "product", productId, "nghia"));
			} else if (product.getUsername().equals("duc")) {
				dbNo.insertNotification(new Notification(
						"nghia đã comment vào sản phẩm của bạn", new Date(),
						true, "product", productId, "duc"));
				dbNo.insertNotification(new Notification(
						"loi đã comment vào sản phẩm của bạn", new Date(),
						false, "product", productId, "duc"));
			} else if (product.getUsername().equals("loi")) {
				dbNo.insertNotification(new Notification(
						"nghia đã comment vào sản phẩm của bạn", new Date(),
						true, "product", productId, "loi"));
				dbNo.insertNotification(new Notification(
						"duc đã comment vào sản phẩm của bạn", new Date(),
						false, "product", productId, "loi"));
			} else if (product.getUsername().equals("hieu")) {
				dbNo.insertNotification(new Notification(
						"nghia đã comment vào sản phẩm của bạn", new Date(),
						true, "product", productId, "hieu"));
				dbNo.insertNotification(new Notification(
						"duc đã comment vào sản phẩm của bạn", new Date(),
						false, "product", productId, "hieu"));
			}
		}

		// Insert sample pages
		PageServiceImpl dbPage = PageServiceImpl.getInstance();
		ArrayList<Page> pages = SampleDataNghia.getSamplePages();
		for (Page page : pages) {
			Long pageId = dbPage.insertPage(page).getResult();

			if (page.getUsername().equals("tam")) {
				dbNo.insertNotification(new Notification(
						"nghia đã comment vào bài viết của bạn", new Date(),
						true, "page", pageId, "tam"));
				dbNo.insertNotification(new Notification(
						"duc đã comment vào bài viết của bạn", new Date(),
						false, "page", pageId, "tam"));
			} else if (page.getUsername().equals("nghia")) {
				dbNo.insertNotification(new Notification(
						"tam đã comment vào bài viết của bạn", new Date(),
						true, "page", pageId, "nghia"));
				dbNo.insertNotification(new Notification(
						"duc đã comment vào bài viết của bạn", new Date(),
						false, "page", pageId, "nghia"));
			} else if (page.getUsername().equals("duc")) {
				dbNo.insertNotification(new Notification(
						"nghia đã comment vào bài viết của bạn", new Date(),
						true, "page", pageId, "duc"));
				dbNo.insertNotification(new Notification(
						"loi đã comment vào bài viết của bạn", new Date(),
						false, "page", pageId, "duc"));
			} else if (page.getUsername().equals("loi")) {
				dbNo.insertNotification(new Notification(
						"nghia đã comment vào bài viết của bạn", new Date(),
						true, "page", pageId, "loi"));
				dbNo.insertNotification(new Notification(
						"duc đã comment vào bài viết của bạn", new Date(),
						false, "page", pageId, "loi"));
			} else if (page.getUsername().equals("hieu")) {
				dbNo.insertNotification(new Notification(
						"nghia đã comment vào bài viết của bạn", new Date(),
						true, "page", pageId, "hieu"));
				dbNo.insertNotification(new Notification(
						"duc đã comment vào bài viết của bạn", new Date(),
						false, "page", pageId, "hieu"));
			}
		}

		// Insert sample categories
		CategoryServiceImpl dbCat = CategoryServiceImpl.getInstance();
		ArrayList<Category> categories = SampleData.getSampleCategories();
		for (Category category : categories) {
			dbCat.insertCategory(category);
		}

		//Insert sample notifications
		NotificationServiceImpl dbNo = NotificationServiceImpl.getInstance();
		ArrayList<Notification> nots = SampleData.getSampleNotifications();
		for (Notification no : nots){
			dbNo.insertNotification(no);
		}
		
		//Insert sample user subscribe 
		UserSubcribeProductImpl dbUserSub = UserSubcribeProductImpl.getInstance();
		ArrayList<UserSubcribeProduct> userSubs = SampleData.getSampleUserSubcribeProducts();
		for (UserSubcribeProduct u : userSubs){
			dbUserSub.insertSubcribe(u);
		}
		return "Done";
	}

}
