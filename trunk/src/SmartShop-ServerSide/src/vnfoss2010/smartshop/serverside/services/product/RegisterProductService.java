package vnfoss2010.smartshop.serverside.services.product;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.CategoryServiceImpl;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.UserSubcribeProductImpl;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.UserSubcribeProduct;
import vnfoss2010.smartshop.serverside.notification.NotificationUtils;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.beoui.geocell.GeocellManager;
import com.google.appengine.repackaged.org.json.JSONObject;

public class RegisterProductService extends BaseRestfulService {

	private final static Logger log = Logger
			.getLogger(RegisterProductService.class.getName());

	private CategoryServiceImpl dbcat;
	private ProductServiceImpl dbProduct = ProductServiceImpl.getInstance();
	private UserSubcribeProductImpl dbSubscribe = UserSubcribeProductImpl
			.getInstance();

	public RegisterProductService(String serviceName) {
		super(serviceName);

		dbcat = CategoryServiceImpl.getInstance();
		dbProduct = ProductServiceImpl.getInstance();
		dbSubscribe = UserSubcribeProductImpl.getInstance();
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		JSONObject jsonReturn = new JSONObject();
		JSONObject json = null;
		try {
			json = new JSONObject(content);
		} catch (Exception e) {
		}
		Product product = Global.gsonWithDate.fromJson(content, Product.class);
		log.log(Level.SEVERE, product + "");

		ServiceResult<Set<Category>> listCategories = dbcat
				.findCategories(product.getSetCategoryKeys());
		if (listCategories.isOK() == false) {
			jsonReturn.put("errCode", 1);
			jsonReturn.put("message", listCategories.getMessage());
		} else {
			product.setGeocells(GeocellManager.generateGeoCell(product
					.getLocation()));

			ServiceResult<Long> result = dbProduct.insertProduct(product);

			// Scan for subscribed user
			Set<String> setKeys = Global.mapSession.keySet();
			for (String s : setKeys) {
				ServiceResult<List<UserSubcribeProduct>> resultListUserSubscribeProduct = dbSubscribe
						.getUserSubscribeProductByUsernameEnhanced(s, 1, null);

				if (resultListUserSubscribeProduct.isOK()) {
					for (UserSubcribeProduct subcribe : resultListUserSubscribeProduct
							.getResult()) {
						if (subcribe.isPushNotification()) {
							if (isMatchProductAndUserSubscribeProduct(product,
									subcribe)) {
								// Push Notification for this user right now
								log.log(Level.SEVERE, "Push for user: " + s);

								// boolean b = sendPush(s, subcribe
								// .getDescription().substring(
								// 0,
								// Math.min(40, subcribe
								// .getDescription()
								// .length())),
								// Global.gsonWithDate.toJson(product));

								ServiceResult<String> result2 = NotificationUtils
										.sendNotification(
												s,
												subcribe
														.getDescription()
														.substring(
																0,
																Math
																		.min(
																				40,
																				subcribe
																						.getDescription()
																						.length())),
												Global.gsonWithDate
														.toJson(product));

								log.log(Level.SEVERE, "" + result2.getResult());
								if (!result2.isOK())
									log.log(Level.SEVERE,
											"Cannot push notification for user: "
													+ s);
							}
						}
					}
				}
			}

			jsonReturn.put("errCode", result.isOK() ? 0 : 1);
			jsonReturn.put("message", result.getMessage());
			if (result.isOK()) {
				jsonReturn.put("product_id", result.getResult());
			}
		}
		return jsonReturn.toString();
	}

	private boolean isMatchProductAndUserSubscribeProduct(Product product,
			UserSubcribeProduct subcribe) {
		// whether match??
		// Point center = subcribe.getLocation();
		// Double maxDistance = subcribe.getRadius();
		//		
		// if (center==null || maxDistance == null || maxDistance == 0 ||
		// product.getLat()==0 || product.getLng() ==0 ){
		//			
		// }else{
		// if (UtilsFunction.distance(center.getLat(), center.getLon(),
		// product.getLat(), product.getLng()) < subcribe.getRadius()){
		// UtilsFunction.
		// }
		// }

		return true;
	}
}
