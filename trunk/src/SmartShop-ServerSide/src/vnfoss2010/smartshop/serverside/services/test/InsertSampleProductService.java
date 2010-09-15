package vnfoss2010.smartshop.serverside.services.test;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import vnfoss2010.smartshop.serverside.database.NotificationServiceImpl;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Notification;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.test.SampleData;

import com.beoui.geocell.GeocellManager;

public class InsertSampleProductService extends BaseRestfulService {
	Logger log = Logger.getLogger(InsertSampleProductService.class.getName());

	public InsertSampleProductService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		ProductServiceImpl db = ProductServiceImpl.getInstance();
		NotificationServiceImpl dbNo = NotificationServiceImpl.getInstance();

		List<Product> list = SampleData.getSampleProducts();
		for (Product product : list) {
			product.setGeocells(GeocellManager.generateGeoCell(product
					.getLocation()));
			Long productId = db.insertProduct(product).getResult();
			
			ServiceResult<Long> result;
			if (product.getUsername().equals("tam")) {
				result = dbNo.insertNotification(new Notification(
						"nghia đã comment vào sản phẩm của bạn", new Date(),
						true, "product", productId, "tam"));
				result = dbNo.insertNotification(new Notification(
						"duc đã comment vào sản phẩm của bạn", new Date(),
						false, "product", productId, "tam"));
			} else if (product.getUsername().equals("nghia")) {
				result = dbNo.insertNotification(new Notification(
						"tam đã comment vào sản phẩm của bạn", new Date(),
						true, "product", productId, "nghia"));
				result = dbNo.insertNotification(new Notification(
						"duc đã comment vào sản phẩm của bạn", new Date(),
						false, "product", productId, "nghia"));
			} else if (product.getUsername().equals("duc")) {
				result = dbNo.insertNotification(new Notification(
						"nghia đã comment vào sản phẩm của bạn", new Date(),
						true, "product", productId, "duc"));
				result = dbNo.insertNotification(new Notification(
						"loi đã comment vào sản phẩm của bạn", new Date(),
						false, "product", productId, "duc"));
			} else if (product.getUsername().equals("hieu")) {
				result = dbNo.insertNotification(new Notification(
						"nghia đã comment vào sản phẩm của bạn", new Date(),
						true, "product", productId, "loi"));
				result = dbNo.insertNotification(new Notification(
						"duc đã comment vào sản phẩm của bạn", new Date(),
						false, "product", productId, "loi"));
			} else if (product.getUsername().equals("loi")) {
				result = dbNo.insertNotification(new Notification(
						"nghia đã comment vào sản phẩm của bạn", new Date(),
						true, "product", productId, "hieu"));
				result = dbNo.insertNotification(new Notification(
						"duc đã comment vào sản phẩm của bạn", new Date(),
						false, "product", productId, "hieu"));
			}
		}

		return "Done";
	}

}
