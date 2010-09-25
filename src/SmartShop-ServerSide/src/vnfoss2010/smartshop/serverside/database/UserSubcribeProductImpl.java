package vnfoss2010.smartshop.serverside.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.dom.Subscribe_ListProduct;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.UserSubcribeProduct;
import vnfoss2010.smartshop.serverside.utils.Predicate;
import vnfoss2010.smartshop.serverside.utils.SearchJanitorUtils;
import vnfoss2010.smartshop.serverside.utils.StringUtils;
import vnfoss2010.smartshop.serverside.utils.UtilsFunction;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.GeocellQuery;
import com.beoui.geocell.model.Point;

public class UserSubcribeProductImpl {
	private static UserSubcribeProductImpl instance;
	private AccountServiceImpl dbAccount = AccountServiceImpl.getInstance();
	static Logger log = Logger.getLogger(UserSubcribeProductImpl.class
			.getName());

	public ServiceResult<UserSubcribeProduct> findSubcribe(Long id) {
		ServiceResult<UserSubcribeProduct> result = new ServiceResult<UserSubcribeProduct>();
		UserSubcribeProduct subcribe = null;
		PersistenceManager pm = null;
			pm = PMF.get().getPersistenceManager();
			try {
				subcribe = pm.getObjectById(UserSubcribeProduct.class, id);
			} catch (JDOObjectNotFoundException e) {
			}
			if (subcribe == null) {
				result.setOK(false);
				result.setMessage(Global.messages
						.getString("no_found_subscribe"));
			} else {
				result.setOK(true);
				result.setResult(subcribe);
				result.setMessage(Global.messages
						.getString("get_subscribe_successfully"));
			}
			try {
				pm.close();
			} catch (Exception e) {
			}
		return result;
	}

	public ServiceResult<Long> insertSubcribe(
			UserSubcribeProduct userSubcribeProduct) {
		ServiceResult<Long> result = new ServiceResult<Long>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (userSubcribeProduct == null) {
			result.setMessage(Global.messages
					.getString("cannot_handle_with_null"));
		}

		try {
			// pm.flush();
			userSubcribeProduct = pm.makePersistent(userSubcribeProduct);
			if (userSubcribeProduct == null) {
				result.setMessage(Global.messages
						.getString("insert_subscribe_fail"));
			} else {
				result.setResult(userSubcribeProduct.getId());
				result.setMessage(Global.messages
						.getString("insert_subscribe_successfully"));
				result.setOK(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage(Global.messages
					.getString("insert_subscribe_fail"));
		}
		return result;
	}

	public ServiceResult<List<Product>> searchProductInSubcribeRange(
			UserSubcribeProduct subcribe, long fromRecord, long toRecord,
			final Date lastUpdate) {
		Point center = subcribe.getLocation();
		Double maxDistance = subcribe.getRadius();

		ServiceResult<List<Product>> result = new ServiceResult<List<Product>>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<String> pa = subcribe.getCategoryList();
		// List<Object> para = new ArrayList<Object>();
		// para.add(subcribe.getCategoryList());
		// for (int i = 0; i < pa.size(); i++) {
		// GeocellQuery<String> baseQuery = new GeocellQuery<String>(
		// "setCategoryKeys.contains(catKey" + i + ")",
		// "String catKey" + i, Arrays.asList(pa.get(i)));
		// }

		List<Product> listProduct = null;
		GeocellQuery baseQuery = null;

		Predicate<Product> isLargerDatePost = new Predicate<Product>() {

			@Override
			public boolean apply(Product type) {
				if (lastUpdate == null
						|| type.getDate_post().compareTo(lastUpdate) > 0) {
					return true;
				}
				return false;
			}
		};

		if (StringUtils.isEmptyOrNull(subcribe.getKeyword())) {
			String where = "setCategoryKeys.contains(catKey)";
			String decleare = "String catKey";
			// if (lastUpdate!=null){
			// baseQuery.setDeclearedImports("import java.util.Date");
			// where = where + " && date_post>last";
			// decleare = decleare + ", Date last";
			// para.add(lastUpdate);
			// }
			baseQuery = new GeocellQuery<String>(where, decleare, pa);
			baseQuery.setRange(fromRecord, toRecord);

			listProduct = GeocellManager.proximityFetchNew(center, 40,
					maxDistance, Product.class, baseQuery, pm,
					GeocellManager.MAX_GEOCELL_RESOLUTION);
		} else {
			// Prepare to search
			StringBuffer queryBuffer = new StringBuffer();
			Set<String> queryTokens = SearchJanitorUtils
					.getTokensForIndexingOrQuery(subcribe.getKeyword(),
							Global.MAXIMUM_NUMBER_OF_WORDS_TO_SEARCH);
			List<Object> parametersForSearch = new ArrayList<Object>(
					queryTokens);
			StringBuffer declareParametersBuffer = new StringBuffer();
			int parameterCounter = 0;

			while (parameterCounter < queryTokens.size()) {
				queryBuffer.append("fts == param" + parameterCounter);
				declareParametersBuffer.append("String param"
						+ parameterCounter);

				if (parameterCounter + 1 < queryTokens.size()) {
					queryBuffer.append(" && ");
					declareParametersBuffer.append(", ");
				}
				parameterCounter++;
			}
			// //////

			if (pa.isEmpty()) {
				// String importStr = null;
				// if (lastUpdate!=null){
				// queryBuffer.append(" && date_post>last");
				// importStr = "import java.util.Date";
				// declareParametersBuffer.append(", Date last");
				// parametersForSearch.add(lastUpdate);
				// }
				baseQuery = new GeocellQuery(queryBuffer.toString(),
						declareParametersBuffer.toString(), parametersForSearch);
				baseQuery.setRange(fromRecord, toRecord);

				listProduct = GeocellManager.proximityFetchNew(center, 40,
						maxDistance, Product.class, baseQuery, pm,
						GeocellManager.MAX_GEOCELL_RESOLUTION);

			} else {
				// parametersForSearch.add(0, pa);
				// String importStr = null;
				// if (lastUpdate!=null){
				// baseQuery.setDeclearedImports("import java.util.Date");
				// queryBuffer.append(" && date_post>last");
				// importStr = "import java.util.Date";
				// declareParametersBuffer.append(", Date last");
				// parametersForSearch.add(lastUpdate);
				// }

				baseQuery = new GeocellQuery(queryBuffer.toString(),
						declareParametersBuffer.toString(), parametersForSearch);
				// baseQuery.setDeclearedImports(importStr);
				baseQuery.setRange(fromRecord, toRecord);

				listProduct = GeocellManager.proximityFetchNew(center, 40,
						maxDistance, Product.class, baseQuery, pm,
						GeocellManager.MAX_GEOCELL_RESOLUTION);
				if (!listProduct.isEmpty()) {
					ArrayList<Product> tmp = new ArrayList<Product>();
					for (Product product : listProduct) {
						if (UtilsFunction.intersection(
								product.getSetCategoryKeys(), pa).isEmpty()) {
							tmp.add(product);
						}
					}
					listProduct.removeAll(tmp);
				}
			}
		}
		
		if (lastUpdate != null) {
			// filter
			listProduct = UtilsFunction.filter(listProduct, isLargerDatePost);
		}

		try {
			if (listProduct != null) {
				result.setOK(true);
				result.setResult(listProduct);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setOK(false);
		}

		return result;
	}

	public ServiceResult<List<Subscribe_ListProduct>> getSubscribeProductByUsername(
			String username, long fromRecord, long toRecord, Date lastUpdate) {
		ServiceResult<List<Subscribe_ListProduct>> result = new ServiceResult<List<Subscribe_ListProduct>>();
		List<Subscribe_ListProduct> list = new ArrayList<Subscribe_ListProduct>();
		ServiceResult<List<UserSubcribeProduct>> resultSub = getUserSubscribeProductByUsername(
				username, null, null, 1);
		if (resultSub.isOK()) {
			for (UserSubcribeProduct subscribe : resultSub.getResult()) {
				ServiceResult<List<Product>> tmp = searchProductInSubcribeRange(
						subscribe, fromRecord, toRecord, lastUpdate);
				if (tmp.isOK()) {
					list.add(new Subscribe_ListProduct(subscribe.getId(), tmp.getResult()));
				}
			}

			if (list.isEmpty()) {
				result.setOK(false);
				result.setMessage(String.format(Global.messages
						.getString("no_found_subscribe_product_by_username"),
						username));
			} else {
				result.setOK(true);
				result.setResult(list);
				result
						.setMessage(String
								.format(
										Global.messages
												.getString("get_subscribe_product_by_username_successfully"),
										username));
			}
		} else {
			result.setMessage(resultSub.getMessage());
			result.setOK(false);
		}

		return result;
	}

	public ServiceResult<List<UserSubcribeProduct>> findActiveSubcribe() {
		ServiceResult<List<UserSubcribeProduct>> result = new ServiceResult<List<UserSubcribeProduct>>();
		try {
			PersistenceManager pm = PMF.get().getPersistenceManager();
			Query query = pm.newQuery(UserSubcribeProduct.class);
			query.setFilter("isActive == true");
			List<UserSubcribeProduct> listSub = (List<UserSubcribeProduct>) query
					.execute(query);
			if (listSub == null || listSub.size() == 0) {
				result.setOK(false);
				result.setMessage(Global.messages
						.getString("no_found_subscribe"));
			} else {
				result.setOK(true);
				result.setResult(listSub);
				result.setMessage(Global.messages
						.getString("get_list_subscribe_successfully"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @param username
	 * @param mode
	 *            <ul>
	 *            <li>mode = 0: Get all {@link UserSubcribeProduct} of username
	 *            <li>mode = 1: Get active {@link UserSubcribeProduct} of
	 *            username
	 *            <li>mode = 2: Get inactive {@link UserSubcribeProduct} of
	 *            username
	 *            </ul>
	 * @return
	 */
	public ServiceResult<List<UserSubcribeProduct>> getUserSubscribeProductByUsername(
			String username, Date fromDate, Date toDate, int mode) {
		ServiceResult<List<UserSubcribeProduct>> result = new ServiceResult<List<UserSubcribeProduct>>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (dbAccount.isExist(username).isOK()) {
			Query query = pm.newQuery(UserSubcribeProduct.class);
			if (fromDate != null || toDate != null)
				query.declareImports("import java.util.Date");
			switch (mode) {
			case 0:
				query.setFilter("username == us"
						+ (fromDate == null ? "" : " && date >= fromDate")
						+ (toDate == null ? "" : " && date <= toDate"));
				break;

			case 1:
				query.setFilter("username == us && isActive == true"
						+ (fromDate == null ? "" : " && date >= fromDate")
						+ (toDate == null ? "" : " && date <= toDate"));
				break;

			case 2:
				query.setFilter("username == us && isActive == false"
						+ (fromDate == null ? "" : " && date >= fromDate")
						+ (toDate == null ? "" : " && date <= toDate"));
				break;

			default:
				break;
			}

			query.declareParameters("String us"
					+ (fromDate == null ? "" : ", Date fromDate")
					+ (toDate == null ? "" : ", Date toDate"));
			List<Object> listParameters = new ArrayList<Object>();
			listParameters.add(username);
			if (fromDate != null && toDate != null) {
				listParameters.add(fromDate);
				listParameters.add(toDate);
			}
			List<UserSubcribeProduct> list = (List<UserSubcribeProduct>) query
					.executeWithArray(listParameters.toArray());

			if (list == null || list.size() == 0) {
				result.setOK(false);
				result.setMessage(Global.messages
						.getString("no_found_subscribe"));
			} else {
				result.setOK(true);
				result.setResult(list);
				result
						.setMessage(String
								.format(
										Global.messages
												.getString("get_list_subscribe_by_username_successfully"),
										username));
			}
		} else {
			result.setOK(false);
			result.setMessage(Global.messages.getString("not_found") + " "
					+ username);
		}

		return result;
	}

	public ServiceResult<Void> editSubcribe(UserSubcribeProduct editSubcribe) {
		ServiceResult<Void> result = new ServiceResult<Void>();
		UserSubcribeProduct subcribe = null;
		PersistenceManager pm = null;
		try {
			pm = PMF.get().getPersistenceManager();
			subcribe = pm.getObjectById(UserSubcribeProduct.class, editSubcribe
					.getId());
			if (subcribe == null) {
				result.setOK(false);
				result.setMessage(Global.messages
						.getString("no_found_subscribe"));
			} else {
				subcribe.setActive(editSubcribe.isActive());
				subcribe.setCategoryList(editSubcribe.getCategoryList());
				subcribe.setDate(editSubcribe.getDate());
				subcribe.setDescription(editSubcribe.getDescription());
				subcribe.setGeocells(editSubcribe.getGeocells());
				subcribe.setLat(editSubcribe.getLat());
				subcribe.setLng(editSubcribe.getLng());
				subcribe.setRadius(editSubcribe.getRadius());
				result.setOK(true);
				result.setMessage(Global.messages
						.getString("edit_subscribe_successfully"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage(Global.messages.getString("edit_subscribe_fail"));
			result.setOK(false);
			e.printStackTrace();
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public ServiceResult<List<UserSubcribeProduct>> findSubcribe(
			String username, Date fromDate, Date toDate) {
		ServiceResult<List<UserSubcribeProduct>> result = new ServiceResult<List<UserSubcribeProduct>>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			String queryStr = "select from "
					+ UserSubcribeProduct.class.getName()
					+ " where username == us"
					+ (fromDate == null ? "" : " && date >= fromDate")
					+ (toDate == null ? "" : " && date <= toDate");
			Query query = pm.newQuery(queryStr);
			query.declareParameters("String us"
					+ (fromDate == null ? "" : ", Date fromDate")
					+ (toDate == null ? "" : ", Date toDate"));
			query.declareImports("import java.util.Date");
			List<Object> listParameters = new ArrayList<Object>();
			listParameters.add(username);
			if (fromDate != null) {
				listParameters.add(fromDate);
			}
			if (toDate != null) {
				listParameters.add(toDate);
			}
			Global.log(log, "message " + query);
			Global.log(log, "message " + listParameters.toArray()[0]);
			Object queryResult = query.executeWithArray(listParameters
					.toArray());
			Global.log(log, "message " + queryResult);
			List<UserSubcribeProduct> list = (List<UserSubcribeProduct>) queryResult;
			if (list == null) {
				result.setOK(false);
				result.setMessage("Khong the tim");
			} else {
				result.setResult(list);
				result.setOK(true);
				result.setMessage("Tim thanh cong");
			}
		} catch (Exception e) {
			result.setMessage("Exception " + e.getMessage());
			result.setOK(false);
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public static UserSubcribeProductImpl getInstance() {
		if (instance == null) {
			instance = new UserSubcribeProductImpl();
		}
		return instance;
	}
}
