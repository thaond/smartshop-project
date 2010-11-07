package vnfoss2010.smartshop.serverside.services.product;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.AttributeServiceImpl;
import vnfoss2010.smartshop.serverside.database.CategoryServiceImpl;
import vnfoss2010.smartshop.serverside.database.NotificationServiceImpl;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.UserSubcribeProductImpl;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;
import vnfoss2010.smartshop.serverside.database.entity.UserSubcribeProduct;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.utils.SearchJanitorUtils;
import vnfoss2010.smartshop.serverside.utils.UtilsFunction;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.Point;
import com.google.appengine.repackaged.org.json.JSONObject;

public class RegisterProductService extends BaseRestfulService {

	private final static Logger log = Logger
			.getLogger(RegisterProductService.class.getName());

	private CategoryServiceImpl dbcat;
	private AccountServiceImpl dbAccount;
	private ProductServiceImpl dbProduct;
	private UserSubcribeProductImpl dbSubscribe;
	private AttributeServiceImpl dbAttribute;
	private NotificationServiceImpl dbNoti;

	public RegisterProductService(String serviceName) {
		super(serviceName);

		dbcat = CategoryServiceImpl.getInstance();
		dbProduct = ProductServiceImpl.getInstance();
		dbSubscribe = UserSubcribeProductImpl.getInstance();
		dbAttribute = AttributeServiceImpl.getInstance();
		dbAccount = AccountServiceImpl.getInstance();
		dbNoti = NotificationServiceImpl.getInstance();
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
		String username = getParameterWithThrow("username", params, json);
		Product product = Global.gsonWithDate.fromJson(content, Product.class);
		product.setUsername(username);

		if (!product.getSetCategoryKeys().isEmpty()) {
			ServiceResult<Set<Category>> listCategories = dbcat
					.findCategories(product.getSetCategoryKeys());
			if (listCategories.isOK() == false) {
				jsonReturn.put("errCode", 1);
				jsonReturn.put("message", listCategories.getMessage());

				return jsonReturn.toString();
			}
		}

		product.setGeocells(GeocellManager.generateGeoCell(product
				.getLocation()));
		ServiceResult<Long> result = dbProduct.insertProduct(product);

		if (result.isOK()) {
			jsonReturn.put("product_id", result.getResult());

			// Scan for subscribed user
			// Set<String> setKeys = Global.mapSession.keySet();
			ServiceResult<List<UserInfo>> allUserService = dbAccount
					.getAllUserInfos();
			if (allUserService.isOK()) {
				for (UserInfo user : allUserService.getResult()) {
					if (user.getUsername().equals(product.getUsername())) {
						continue;
					}

					ServiceResult<List<UserSubcribeProduct>> resultListUserSubscribeProduct = dbSubscribe
							.getUserSubscribeProductByUsernameEnhanced(
									user.getUsername(), 1, null);
					if (resultListUserSubscribeProduct.isOK()) {
						for (UserSubcribeProduct subcribe : resultListUserSubscribeProduct
								.getResult()) {
							// if (subcribe.isPushNotification()) {
							long isMatch = isMatchProductAndUserSubscribeProduct(
									product, subcribe);
							if (isMatch != -1) {
								ServiceResult<Void> insertNotiService = dbNoti
										.insertWhenSubscribeMatched(
												subcribe.getId(),
												product.getId(),
												subcribe.getUsername());
								if (!insertNotiService.isOK()) {
									result.setMessage(result.getMessage()
											+ ";exception1:"
											+ insertNotiService.getMessage());
								}
							}
							// }
						}
					} else {
						result.setMessage(result.getMessage() + ";exception2:"
								+ resultListUserSubscribeProduct.getMessage()
								+ " " + user.getUsername());
					}
				}
			} else {
				result.setMessage(result.getMessage() + ";exception3:"
						+ allUserService.getMessage());
			}
		}
		jsonReturn.put("errCode", result.isOK() ? 0 : 1);
		jsonReturn.put("message", result.getMessage());
		return jsonReturn.toString();
	}

	private long isMatchProductAndUserSubscribeProduct(Product product,
			UserSubcribeProduct subcribe) {
		Set<String> queryTokens = SearchJanitorUtils
				.getTokensForIndexingOrQuery(subcribe.getKeyword(),
						Global.MAXIMUM_NUMBER_OF_WORDS_TO_SEARCH);
		for (String subcribeFTS : queryTokens) {
			if (!product.getFts().contains(subcribeFTS)) {
				return -1;
			}
		}
		for (String subCategory : subcribe.getCategoryList()) {
			if (!(product.getSetCategoryKeys().contains(subCategory) || product
					.getAttributeSets().contains(subCategory))) {
				return -1;
			}
		}

		// whether match??
		Point center = subcribe.getLocation();
		Double maxDistance = subcribe.getRadius();
		if (!(center == null || maxDistance == null || maxDistance == 0
				|| product.getLat() == 0 || product.getLng() == 0)) {
			if (UtilsFunction.distance(center.getLat(), center.getLon(),
					product.getLat(), product.getLng()) <= maxDistance) {
				return subcribe.getId();
			} else {
				return -1;
			}
		}
		return subcribe.getId();
	}
}
