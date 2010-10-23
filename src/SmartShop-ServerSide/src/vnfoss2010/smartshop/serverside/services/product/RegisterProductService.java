package vnfoss2010.smartshop.serverside.services.product;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.utils.StringUtils;
import vnfoss2010.smartshop.serverside.utils.UtilsFunction;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.Point;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.Gson;

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
//			Set<String> setKeys = Global.mapSession.keySet();
//			for (String s : setKeys) {
//				ServiceResult<List<UserSubcribeProduct>> resultListUserSubscribeProduct = dbSubscribe
//						.getUserSubscribeProductByUsernameEnhanced(s, 1, null);
//
//				if (resultListUserSubscribeProduct.isOK()) {
//					for (UserSubcribeProduct subcribe : resultListUserSubscribeProduct
//							.getResult()) {
//						if (subcribe.isPushNotification()) {
//							if (isMatchProductAndUserSubscribeProduct(product,
//									subcribe)) {
//								// Push Notification for this user right now
//								log.log(Level.SEVERE, "Push for user: " + s);
//
//								boolean b = sendPush(s, subcribe
//										.getDescription().substring(
//												0,
//												Math.min(40, subcribe
//														.getDescription()
//														.length())),
//										Global.gsonWithDate.toJson(product));
//								if (!b)
//									log.log(Level.SEVERE,
//											"Cannot push notification for user: "
//													+ s);
//							}
//						}
//					}
//				}
//			}

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

	private boolean sendPush(String userKey, String title, String body)
			throws IOException {
		String urlString = "http://notify.xtify.com/api/1.0/SdkNotification";
		String content = String.format(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
						+ "<sdk-notification>"
						+ "<actionType>LAUNCH_APP</actionType>" + "<appId>"
						+ Global.XTIFY_API_KEY + "</appId>"
						+ "<userKey>%s</userKey>"
						+ "<notificationTitle>%s</notificationTitle>"
						+ "<notificationBody>%s</notificationBody>"
						+ "</sdk-notification>", userKey, title, body);
		String result = null;
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
		}
		HttpURLConnection urlConn = null;
		OutputStream out = null;
		BufferedReader in = null;
		if (url != null) {
			try {
				urlConn = (HttpURLConnection) url.openConnection();
				urlConn.addRequestProperty("Content-Type", "application/xml");
				urlConn.setRequestMethod("PUT");
				urlConn.setDoOutput(true);
				urlConn.setDoInput(true);
				urlConn.connect();
				// Write content data to server
				out = urlConn.getOutputStream();
				out.write(content.getBytes());
				out.flush();
				// Check response code
				if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					in = new BufferedReader(new InputStreamReader(urlConn
							.getInputStream()), 8192);
					StringBuffer strBuff = new StringBuffer();
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						strBuff.append(inputLine);
					}
					result = strBuff.toString();
					System.err.println(result);
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			} finally {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
				if (urlConn != null) {
					urlConn.disconnect();
				}
			}
		}// end if

		if (!StringUtils.isEmptyOrNull(result) && result.contains("SUCCESS"))
			return true;
		return false;
	}
}
