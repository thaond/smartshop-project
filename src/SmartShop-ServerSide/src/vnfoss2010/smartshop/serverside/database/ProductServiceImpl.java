package vnfoss2010.smartshop.serverside.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.entity.Attribute;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Media;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;
import vnfoss2010.smartshop.serverside.utils.Predicate;
import vnfoss2010.smartshop.serverside.utils.SearchJanitorUtils;
import vnfoss2010.smartshop.serverside.utils.StringUtils;
import vnfoss2010.smartshop.serverside.utils.UtilsFunction;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.GeocellQuery;
import com.beoui.geocell.model.Point;

public class ProductServiceImpl {
	private static ProductServiceImpl instance;
	private AccountServiceImpl dbAccount;
	private NotificationServiceImpl dbNoti;

	private final static Logger log = Logger.getLogger(ProductServiceImpl.class
			.getName());

	private ProductServiceImpl() {
		instance = this;
		dbAccount = AccountServiceImpl.getInstance();
		dbNoti = NotificationServiceImpl.getInstance();
	}

	// PRODUCT
	/**
	 * Insert new product into database
	 * 
	 * @return id in the datastore
	 */
	public ServiceResult<Long> insertProduct(Product product) {
		preventSQLInjProduct(product);
		// ProductServiceImpl.updateFTSStuffForProduct(product);
		product.updateFTS();
		ServiceResult<Long> result = new ServiceResult<Long>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (product == null) {
			result.setMessage(Global.messages
					.getString("cannot_handle_with_null"));
		}

		log.log(Level.SEVERE, "Before: " + product.toString());

		try {
			// Make persistence for attribute
			for (int i = 0; i < product.getAttributeSets().size(); i++) {
				Attribute a = pm.makePersistent(product.getAttributeSets().get(
						i));
				if (a != null)
					product.getListAttributeKeys().add(a.getId());
			}

			// Make persistence for Media
			java.util.Iterator<Media> iterator = product.getSetMedias()
					.iterator();
			while (iterator.hasNext()) {
				Media m = iterator.next();
				m = pm.makePersistent(m);
				if (m != null)
					product.getSetMediaKeys().add(m.getId());
			}

			// pm.flush();
			product = pm.makePersistent(product);
			log.log(Level.SEVERE, "After: " + product.toString());

			if (product == null) {
				result.setMessage(Global.messages
						.getString("insert_product_fail"));
			} else {
				result.setResult(product.getId());
				result.setMessage(Global.messages
						.getString("insert_product_successfully"));
				result.setOK(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Global.log(log, e.getMessage());
			Global.log(log, Arrays.toString(e.getStackTrace()));

			result.setMessage(Global.messages.getString("insert_product_fail"));
		}

		return result;
	}

	public ServiceResult<Long> insertProduct(Product product,
			List<String> listCategories, List<Attribute> listAttributes) {
		preventSQLInjProduct(product);
		// ProductServiceImpl.updateFTSStuffForProduct(product);
		product.updateFTS();
		ServiceResult<Long> result = new ServiceResult<Long>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		if (product == null) {
			result.setMessage(Global.messages
					.getString("cannot_handle_with_null"));
		}

		try {
			if (listCategories != null) {
				//
				for (String cat : listCategories) {
					Category tmp = null;
					boolean isNotFound = false;
					try {
						tmp = (Category) pm.getObjectById(cat);
					} catch (NucleusObjectNotFoundException e) {
						isNotFound = true;
					} catch (JDOObjectNotFoundException e) {
						isNotFound = true;
					}

					if (isNotFound || tmp == null)
						continue;
					product.getSetCategoryKeys().add(cat);
				}
			}

			if (listAttributes != null) {
				for (Attribute att : listAttributes) {
					product.getAttributeSets().add(att);
				}
			}

			product = pm.makePersistent(product);
			if (product == null) {
				result.setMessage(Global.messages
						.getString("insert_product_fail"));
			} else {
				result.setResult(product.getId());
				result.setMessage(Global.messages
						.getString("insert_product_successfully"));
				result.setOK(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage(Global.messages
					.getString("insert_list_userinfos_fail"));
		}

		return result;
	}

	public ServiceResult<Product> findProduct(Long id) {
		ServiceResult<Product> result = findProduct(id, false);
		return result;
	}

	public ServiceResult<Product> findProduct(Long id, boolean isIncreaseView) {
		ServiceResult<Product> result = new ServiceResult<Product>();
		Product product = null;
		PersistenceManager pm = null;
		try {
			pm = PMF.get().getPersistenceManager();
			try {
				product = pm.getObjectById(Product.class, id);
			} catch (Exception e) {
			}
			if (product == null) {
				result.setOK(false);
				result.setMessage(Global.messages.getString("no_found_product"));
			} else {
				result.setOK(true);
				getReferenedField(pm, product);
				result.setResult(product);
				if (isIncreaseView) {
					product.setProduct_view(product.getProduct_view() + 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setOK(false);
			result.setMessage(Global.messages.getString("no_found_product"));
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
			}

		}
		return result;
	}

	public ServiceResult<Void> updateProduct(Product editProduct) {
		ServiceResult<Void> result = new ServiceResult<Void>();
		Product product = null;
		PersistenceManager pm = null;
		try {
			pm = PMF.get().getPersistenceManager();
			product = pm.getObjectById(Product.class, editProduct.getId());
			if (product == null) {
				result.setOK(false);
				result.setMessage(Global.messages.getString("no_found_product"));
			} else {
				product.setAddress(editProduct.getAddress());
				product.setAttributeSets(editProduct.getAttributeSets());
				product.setLat(editProduct.getLat());
				product.setLng(editProduct.getLng());
				product.setName(editProduct.getName());
				product.setOrigin(editProduct.getOrigin());
				product.setPrice(editProduct.getPrice());
				product.setQuantity(editProduct.getQuantity());
				product.setWarranty(editProduct.getWarranty());
				product.setSetCategoryKeys(editProduct.getSetCategoryKeys());
				product.setGeocells(editProduct.getGeocells());
				result.setOK(true);
				result.setMessage(Global.messages
						.getString("update_product_successfully"));
			}
		} catch (Exception e) {
			result.setMessage(Global.messages.getString("update_product_fail")
					+ " " + e.getMessage());
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

	public static ProductServiceImpl getInstance() {
		if (instance == null) {
			instance = new ProductServiceImpl();
		}
		return instance;
	}

	/**
	 * 
	 * @param maximum
	 *            = 0 means get as much as possible
	 * @param criterias
	 *            : String as list of integers, seperated by comma (,)
	 *            <i>Ex:</i>1,3,4
	 * @return
	 */
	// @SuppressWarnings("unchecked")
	// public ServiceResult<List<Product>> getListProductByCriteria(int maximum,
	// int[] criterias) {
	// ServiceResult<List<Product>> result = new ServiceResult<List<Product>>();
	//
	// String query = "";
	// for (int criteria : criterias) {
	// switch (criteria) {
	// case 0:
	// query += ("date_post asc ");
	// break;
	//
	// case 1:
	// query += ("date_post desc ");
	// break;
	//
	// case 2:
	// query += ("price asc ");
	// break;
	//
	// case 3:
	// query += ("price desc ");
	// break;
	//
	// case 4:
	// query += ("product_view asc ");
	// break;
	//
	// case 5:
	// query += ("product_view desc ");
	// break;
	//
	// default:
	// break;
	// }
	// }
	// query = "select from " + Product.class.getName() + " order by " + query
	// + ((maximum == 0) ? "" : (" limit " + maximum));
	//
	// Global.log(log, query);
	// PersistenceManager pm = PMF.get().getPersistenceManager();
	// List<Product> listProducts = (List<Product>) pm.newQuery(query)
	// .execute();
	// if (listProducts.size() > 0) {
	// result.setOK(true);
	// result.setMessage(Global.messages
	// .getString("search_product_by_criteria_successfully"));
	// result.setResult(listProducts);
	// } else {
	// result.setOK(false);
	// result.setMessage(Global.messages
	// .getString("search_product_by_criteria_fail"));
	// }
	//
	// return result;
	// }

	/**
	 * 
	 * @param maximum
	 *            = 0 means get as much as possible
	 * @param criterias
	 *            : String as list of integers, seperated by comma (|)
	 *            <i>Ex:</i>1,3,4
	 * @param cat_keys
	 *            List categories you want to search in, separated by ,
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ServiceResult<List<Product>> getListProductByCriteriaInCategories(
			int maximum, int[] criterias, int status, String q,
			String username, double priceRange[], String... cat_keys) {
		ServiceResult<List<Product>> result = new ServiceResult<List<Product>>();
		StringBuilder where = new StringBuilder();
		StringBuilder orderBy = new StringBuilder();
		String query = "";
		List<Object> listParameters = new ArrayList<Object>();

		if (priceRange != null && priceRange.length > 0
				&& priceRange.length <= 2 && priceRange[0] >= 0
				&& priceRange[1] >= 0) {
			where.append("price>" + priceRange[0] + " ");
			if (priceRange[1] > priceRange[0]) {
				where.append("&& price<" + priceRange[1] + " ");
			}
			orderBy.append("price asc ");
		}

		// switch (status) {
		// case 0:// List all products (both sold and non-sold)
		// break;
		//
		// case 1:
		// if (!StringUtils.isEmptyOrNull(where.toString()))
		// where.append(" && ");
		// where.append("quantity>0 ");
		// orderBy.append("quantity desc ");
		// break;
		//
		// case 2:
		// if (!StringUtils.isEmptyOrNull(where.toString()))
		// where.append(" && ");
		// where.append("quantity==0 ");
		// orderBy.append("quantity desc ");
		// break;
		//
		// default:
		// break;
		// }

		if (criterias != null) {
			for (int criteria : criterias) {
				switch (criteria) {
				case 0:
					orderBy.append("date_post asc ");
					break;

				case 1:
					orderBy.append("date_post desc ");
					break;

				case 2:
					orderBy.append("price asc ");
					break;

				case 3:
					orderBy.append("price desc ");
					break;

				case 4:
					orderBy.append("product_view asc ");
					break;

				case 5:
					orderBy.append("product_view desc ");
					break;

				case 6:
					orderBy.append("quantity asc ");
					break;

				case 7:
					orderBy.append("quantity desc ");
					break;

				default:
					break;
				}
			}
		} else {
			orderBy.append("date_post desc ");
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query queryObj = null;
		List<Long> listIds = null;

		if (StringUtils.isEmptyOrNull(q)) {
			if (!StringUtils.isEmptyOrNull(username)) {
				if (cat_keys != null) {
					if (!StringUtils.isEmptyOrNull(where.toString()))
						where.append(" && ");
					where.append("setCategoryKeys.contains(catKey) && username==us ");
					listParameters.add(Arrays.asList(cat_keys));
					listParameters.add(username);

					query = "select id from " + Product.class.getName()
							+ " where (" + where.toString() + ") order by "
							+ orderBy.toString()
							+ ((maximum == 0) ? "" : (" limit " + maximum));
					queryObj = pm.newQuery(query);
					queryObj.declareParameters("String catKey, String us");
					listIds = (List<Long>) queryObj
							.executeWithArray(listParameters.toArray());
				} else {
					if (!StringUtils.isEmptyOrNull(where.toString()))
						where.append(" && ");
					where.append("username==us ");
					listParameters.add(username);

					query = "select id from " + Product.class.getName()
							+ " where (" + where.toString() + ") order by "
							+ orderBy.toString()
							+ ((maximum == 0) ? "" : (" limit " + maximum));

					queryObj = pm.newQuery(query);
					queryObj.declareParameters("String us");

					listIds = (List<Long>) queryObj
							.executeWithArray(listParameters.toArray());
				}
			} else { // end if q>username
				// Duplicate
				if (cat_keys != null) {
					if (!StringUtils.isEmptyOrNull(where.toString()))
						where.append(" && ");
					where.append("setCategoryKeys.contains(catKey) ");
					query = "select id from " + Product.class.getName()
							+ " where (" + where.toString() + ") order by "
							+ orderBy.toString()
							+ ((maximum == 0) ? "" : (" limit " + maximum));
					queryObj = pm.newQuery(query);
					queryObj.declareParameters("String catKey");
					listParameters.add(Arrays.asList(cat_keys));
					listIds = (List<Long>) queryObj
							.executeWithArray(listParameters.toArray());
				} else {
					query = "select id from "
							+ Product.class.getName()
							+ (StringUtils.isEmptyOrNull(where.toString()) ? ""
									: " where (" + where.toString() + ")")
							+ " order by " + orderBy.toString()
							+ ((maximum == 0) ? "" : (" limit " + maximum));
					Global.log(log, query);
					queryObj = pm.newQuery(query);
					listIds = (List<Long>) queryObj.execute();
				}
			}// end else q>username
		} else {// end if q
			// Prepare to search
			StringBuffer queryBuffer = new StringBuffer();
			Set<String> queryTokens = SearchJanitorUtils
					.getTokensForIndexingOrQuery(q,
							Global.MAXIMUM_NUMBER_OF_WORDS_TO_SEARCH);
			List<Object> parametersForSearch = new ArrayList<Object>(
					queryTokens);
			StringBuffer declareParametersBuffer = new StringBuffer(", ");
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
			if (!StringUtils.isEmptyOrNull(username)) {
				if (cat_keys != null) {
					if (!StringUtils.isEmptyOrNull(where.toString()))
						where.append(" && ");
					where.append("setCategoryKeys.contains(catKey) && username==us && ");
					where.append(queryBuffer.toString());

					listParameters.add(Arrays.asList(cat_keys));
					listParameters.add(username);
					// listParameters.add(parametersForSearch.toArray());
					for (Object str : parametersForSearch)
						listParameters.add(str);

					query = "select id from " + Product.class.getName()
							+ " where (" + where.toString() + ") order by "
							+ orderBy.toString()
							+ ((maximum == 0) ? "" : (" limit " + maximum));
					queryObj = pm.newQuery(query);
					queryObj.declareParameters("String catKey, String us"
							+ declareParametersBuffer.toString());

					// log.log(Level.SEVERE, "482 " + query);
				} else {
					if (!StringUtils.isEmptyOrNull(where.toString()))
						where.append(" && ");
					where.append("username==us && ");
					where.append(queryBuffer.toString());

					listParameters.add(username);
					// listParameters.add(parametersForSearch.toArray());
					for (Object str : parametersForSearch)
						listParameters.add(str);

					query = "select id from " + Product.class.getName()
							+ " where (" + where.toString() + ") order by "
							+ orderBy.toString()
							+ ((maximum == 0) ? "" : (" limit " + maximum));

					// log.log(Level.SEVERE, "510 " + query);

					queryObj = pm.newQuery(query);
					queryObj.declareParameters("String us"
							+ declareParametersBuffer.toString());
				}
			} else {
				// Duplicate
				if (cat_keys != null) {
					if (!StringUtils.isEmptyOrNull(where.toString()))
						where.append(" && ");
					where.append("setCategoryKeys.contains(catKey) && ");
					where.append(queryBuffer.toString());

					listParameters.add(Arrays.asList(cat_keys));
					// listParameters.add(parametersForSearch.toArray());
					for (Object str : parametersForSearch)
						listParameters.add(str);

					query = "select id from " + Product.class.getName()
							+ " where (" + where.toString() + ") order by "
							+ orderBy.toString()
							+ ((maximum == 0) ? "" : (" limit " + maximum));
					queryObj = pm.newQuery(query);
					queryObj.declareParameters("String catKey"
							+ declareParametersBuffer.toString());
				} else {
					if (!StringUtils.isEmptyOrNull(where.toString()))
						where.append(" && ");
					where.append(queryBuffer.toString());
					// listParameters.add(parametersForSearch.toArray());
					for (Object str : parametersForSearch)
						listParameters.add(str);

					query = "select id from " + Product.class.getName()
							+ " where (" + where.toString() + ") order by "
							+ orderBy.toString()
							+ ((maximum == 0) ? "" : (" limit " + maximum));

					queryObj = pm.newQuery(query);
					queryObj.declareParameters(declareParametersBuffer
							.toString());
				}
			}

			listIds = (List<Long>) queryObj.executeWithArray(listParameters
					.toArray());
		}// end else q
		log.log(Level.SEVERE, query);

		// Product p = pm.getObjectById(Product.class, listIds.get(0));
		// log.log(Level.SEVERE, p +"");

		if (listIds.size() > 0) {
			result.setOK(true);
			result.setMessage(Global.messages
					.getString("search_product_by_criteria_in_cat_successfully"));

			List<Product> listProducts = getListProductFromIds(listIds);

			switch (status) {
			case 0:// List all products (both sold and non-sold)
				break;

			case 1:
				listProducts = UtilsFunction.filter(listProducts,
						new Predicate<Product>() {

							@Override
							public boolean apply(Product type) {
								return type.getQuantity() > 0;
							}
						});
				break;

			case 2:
				listProducts = UtilsFunction.filter(listProducts,
						new Predicate<Product>() {

							@Override
							public boolean apply(Product type) {
								return type.getQuantity() == 0;
							}
						});
				break;

			default:
				break;
			}

			for (Product p : listProducts) {
				getReferenedField(pm, p);
			}

			result.setResult(listProducts);
		} else {
			result.setOK(false);
			result.setMessage(Global.messages
					.getString("search_product_by_criteria_in_cat_fail"));
		}

		return result;
	}

	public ServiceResult<List<Product>> searchProductLike(String queryString) {
		queryString = DatabaseUtils.preventSQLInjection(queryString);
		PersistenceManager pm = PMF.get().getPersistenceManager();

		List<Product> listProducts = null;
		ServiceResult<List<Product>> result = new ServiceResult<List<Product>>();

		listProducts = DatabaseUtils.searchByQuery(queryString, Product.class);

		if (listProducts.size() > 0) {
			result.setResult(listProducts);

			for (Product p : listProducts) {
				getReferenedField(pm, p);
			}

			result.setOK(true);
			result.setMessage(Global.messages
					.getString("search_product_by_query_successfully"));
		} else {
			result.setOK(false);
			result.setMessage(Global.messages
					.getString("search_product_by_query_fail"));
		}

		return result;
	}

	public ServiceResult<List<Product>> getListBuyedProductsByUsername(
			String username, String q, int limit) {
		username = DatabaseUtils.preventSQLInjection(username);
		ServiceResult<List<Product>> result = new ServiceResult<List<Product>>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (username == null || username.equals("")) {
			result.setMessage(Global.messages
					.getString("cannot_handle_with_null"));
			return result;
		}

		boolean isNotFound = false;
		UserInfo userInfo = null;
		try {
			userInfo = pm.getObjectById(UserInfo.class, username);
		} catch (JDOObjectNotFoundException e) {
			isNotFound = true;
		} catch (NucleusObjectNotFoundException e) {
			isNotFound = true;
		}

		if (isNotFound || userInfo == null) {
			// Not found userinfo
			result.setMessage(Global.messages.getString("not_found") + " "
					+ username);
		} else {
			Query queryObject;
			List<Product> listProducts;

			if (StringUtils.isEmptyOrNull(q)) {
				// Query here
				queryObject = pm.newQuery(Product.class);
				queryObject.setOrdering("date_post DESC");
				queryObject.setFilter("listBuyers.contains(us)");
				queryObject.declareParameters("String us");
				if (limit > 0)
					queryObject.setRange(0, limit);

				listProducts = (List<Product>) queryObject.execute(username);
			} else {
				// Prepare to search
				StringBuffer queryBuffer = new StringBuffer();
				Set<String> queryTokens = SearchJanitorUtils
						.getTokensForIndexingOrQuery(q,
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

				String query = "select from "
						+ Product.class.getName()
						+ " where ("
						+ queryBuffer.toString()
						+ " && listBuyers.contains(us)) order by date_post DESC"
						+ ((limit == 0) ? "" : (" limit " + limit));
				declareParametersBuffer.append(", String us");

				log.log(Level.SEVERE, query);

				queryObject = pm.newQuery(query);
				queryObject.declareParameters(declareParametersBuffer
						.toString());
				parametersForSearch.add(username);
				log.log(Level.SEVERE,
						"ParaforSearch: "
								+ Arrays.toString(parametersForSearch.toArray()));

				listProducts = (List<Product>) queryObject
						.executeWithArray(parametersForSearch.toArray());

			}

			if (listProducts.size() > 0) {
				result.setOK(true);
				result.setMessage(String.format(
						Global.messages
								.getString("get_list_buyed_product_by_username_successfully"),
						username));
				result.setResult(listProducts);

				for (Product p : listProducts) {
					getReferenedField(pm, p);
				}
			} else {
				result.setOK(false);
				result.setMessage(String.format(Global.messages
						.getString("get_list_buyed_product_by_username_fail"),
						username));
			}
		}
		try {
			pm.close();
		} catch (Exception ex) {
			result.setOK(false);
			result.setMessage(String.format(Global.messages
					.getString("get_list_buyed_product_by_username_fail"),
					username));
			log.log(Level.SEVERE, ex.getMessage(), ex);
		}

		return result;
	}

	public ServiceResult<List<Product>> getListSelledProductsByUsername(
			String username, String q, int limit) {
		ServiceResult<List<Product>> result = new ServiceResult<List<Product>>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (username == null || username.equals("")) {
			result.setMessage(Global.messages
					.getString("cannot_handle_with_null"));
			return result;
		}

		try {
			boolean isNotFound = false;
			UserInfo userInfo = null;
			try {
				userInfo = pm.getObjectById(UserInfo.class, username);
			} catch (JDOObjectNotFoundException e) {
				isNotFound = true;
			} catch (NucleusObjectNotFoundException e) {
				isNotFound = true;
			}

			if (isNotFound || userInfo == null) {
				// Not found userinfo
				result.setMessage(Global.messages.getString("not_found") + " "
						+ username);
			} else {
				Query queryObject;
				List<Product> listProducts;

				if (StringUtils.isEmptyOrNull(q)) {
					// Query here
					Query query = pm.newQuery(Product.class);
					query.setOrdering("date_post DESC");
					query.setFilter("username == u_seller");
					query.declareParameters("String u_seller");
					if (limit > 0)
						query.setRange(0, limit);

					listProducts = (List<Product>) query.execute(username);
				} else {
					// Prepare to search
					StringBuffer queryBuffer = new StringBuffer();
					Set<String> queryTokens = SearchJanitorUtils
							.getTokensForIndexingOrQuery(q,
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

					String query = "select from "
							+ Product.class.getName()
							+ " where ("
							+ queryBuffer.toString()
							+ " && username == u_seller) order by date_post DESC"
							+ ((limit == 0) ? "" : (" limit " + limit));
					declareParametersBuffer.append(", String u_seller");

					log.log(Level.SEVERE, query);

					queryObject = pm.newQuery(query);
					queryObject.declareParameters(declareParametersBuffer
							.toString());
					parametersForSearch.add(username);
					log.log(Level.SEVERE,
							"ParaforSearch: "
									+ Arrays.toString(parametersForSearch
											.toArray()));

					listProducts = (List<Product>) queryObject
							.executeWithArray(parametersForSearch.toArray());

				}

				if (listProducts.size() > 0) {
					result.setOK(true);
					result.setMessage(String.format(
							Global.messages
									.getString("get_list_selled_product_by_username_successfully"),
							username));

					for (Product p : listProducts) {
						getReferenedField(pm, p);
					}

					result.setResult(listProducts);
				} else {
					result.setOK(false);
					result.setMessage(String.format(
							Global.messages
									.getString("get_list_selled_product_by_username_fail"),
							username));
				}
			}
		} catch (Exception ex) {
			result.setMessage(Global.messages.getString("not_found" + " "
					+ username));
			result.setOK(false);
			// log.log(Level.SEVERE, s, ex);
			ex.printStackTrace();
		} finally {
			try {
				pm.close();
			} catch (Exception ex) {
				result.setOK(false);
				result.setMessage(String.format(Global.messages
						.getString("get_list_selled_product_by_username_fail"),
						username));
				log.log(Level.SEVERE, ex.getMessage(), ex);
			}
		}

		return result;
	}

	// TODO Rewrite this method
	public ServiceResult<List<Product>> getListInterestedProductsByUsername(
			String username, String q, int limit) {
		ServiceResult<List<Product>> result = new ServiceResult<List<Product>>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<Product> listProducts;
		listProducts = new ArrayList<Product>();

		ServiceResult<UserInfo> resultGetUser = dbAccount.getUserInfo(username);
		if (!resultGetUser.isOK()) {
			result.setOK(false);
			result.setMessage(Global.messages.getString("not_found") + " "
					+ username);
			return result;
		}
		UserInfo userInfo = resultGetUser.getResult();
		// Query here
		if (limit == 0)
			limit = userInfo.getListInterestedProductID().size();

		Query queryObject;

		log.log(Level.SEVERE, "" + userInfo.getListInterestedProductID());

		if (StringUtils.isEmptyOrNull(q)) {
			// Query here
			queryObject = pm.newQuery(Product.class);
			queryObject.setOrdering("date_post DESC");
			queryObject.setFilter("listInterestedUsername.contains(us)");
			queryObject.declareParameters("String us");
			if (limit > 0)
				queryObject.setRange(0, limit);

			listProducts.addAll((List<Product>) queryObject.execute(username));

		} else {
			// // Prepare to search
			StringBuffer queryBuffer = new StringBuffer();
			Set<String> queryTokens = SearchJanitorUtils
					.getTokensForIndexingOrQuery(q,
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

			String query = "select from "
					+ Product.class.getName()
					+ " where ("
					+ queryBuffer.toString()
					+ " && listInterestedUsername.contains(us)) order by date_post DESC"
					+ ((limit == 0) ? "" : (" limit " + limit));
			declareParametersBuffer.append(",String us");

			log.log(Level.SEVERE, query);
			queryObject = pm.newQuery(query);
			queryObject.declareParameters(declareParametersBuffer.toString());
			parametersForSearch.add(username);

			listProducts = (List<Product>) queryObject
					.executeWithArray(parametersForSearch.toArray());
		}

		if (listProducts.size() > 0) {
			result.setOK(true);
			result.setMessage(String.format(
					Global.messages
							.getString("get_list_interested_product_by_username_successfully"),
					username));

			for (Product p : listProducts) {
				getReferenedField(pm, p);
			}

			result.setResult(listProducts);
		} else {
			result.setOK(false);
			result.setMessage(String.format(Global.messages
					.getString("get_list_interested_product_by_username_fail"),
					username));
		}

		try {
			pm.close();
		} catch (Exception ex) {
			result.setOK(false);
			result.setMessage(String.format(Global.messages
					.getString("get_list_interested_product_by_username_fail"),
					username));
			log.log(Level.SEVERE, ex.getMessage(), ex);
		}

		return result;
	}

	public ServiceResult<List<Product>> getListProductFromUsername(
			String username, String q, int limit) {
		username = DatabaseUtils.preventSQLInjection(username);
		ServiceResult<List<Product>> result = new ServiceResult<List<Product>>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (username == null || username.equals("")) {
			result.setMessage(Global.messages
					.getString("cannot_handle_with_null"));
			return result;
		}
		boolean isNotFound = false;
		UserInfo userInfo = null;
		try {
			userInfo = pm.getObjectById(UserInfo.class, username);
		} catch (JDOObjectNotFoundException e) {
			isNotFound = true;
		} catch (NucleusObjectNotFoundException e) {
			isNotFound = true;
		}

		if (isNotFound || userInfo == null) {
			// Not found userinfo
			result.setMessage(Global.messages.getString("not_found") + " "
					+ username);
		} else {
			if (limit == 0)
				limit = Integer.MAX_VALUE;

			Query queryObject;
			List<Product> listProducts;

			if (StringUtils.isEmptyOrNull(q)) {
				// Query here
				Query query = pm.newQuery(Product.class);
				query.setFilter("username == us");
				query.declareParameters("String us");
				query.setOrdering("date_post DESC");
				listProducts = (List<Product>) query.execute(username);
			} else {
				// Prepare to search
				StringBuffer queryBuffer = new StringBuffer();
				Set<String> queryTokens = SearchJanitorUtils
						.getTokensForIndexingOrQuery(q,
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

				String query = "select from " + Product.class.getName()
						+ " where (" + queryBuffer.toString()
						+ " && username == us) order by date_post DESC"
						+ ((limit == 0) ? "" : (" limit " + limit));
				declareParametersBuffer.append(", String us");

				log.log(Level.SEVERE, query);
				queryObject = pm.newQuery(query);
				queryObject.declareParameters(declareParametersBuffer
						.toString());
				parametersForSearch.add(username);

				listProducts = (List<Product>) queryObject
						.executeWithArray(parametersForSearch.toArray());
			}

			if (listProducts.size() > 0) {
				result.setOK(true);
				result.setMessage(String.format(Global.messages
						.getString("get_products_by_username_successfully"),
						username));

				for (Product p : listProducts) {
					getReferenedField(pm, p);
				}

				result.setResult(listProducts);
			} else {
				result.setOK(false);
				result.setMessage(String.format(Global.messages
						.getString("get_products_by_username_fail"), username));
			}
		}
		pm.close();
		return result;

	}

	public ServiceResult<List<Product>> searchProductPromixity(Point center,
			Double maxDistance) {
		ServiceResult<List<Product>> result = new ServiceResult<List<Product>>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<Object> pa = new ArrayList<Object>();
		GeocellQuery baseQuery = new GeocellQuery(" ", " ", pa);

		List<Product> listProduct = null;
		try {
			listProduct = GeocellManager.proximityFetch(center, 40,
					maxDistance, Product.class, baseQuery, pm);
			if (listProduct != null) {
				result.setOK(true);

				for (Product p : listProduct) {
					getReferenedField(pm, p);
				}

				result.setResult(listProduct);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setOK(false);
		}

		return result;
	}

	public ServiceResult<Set<String>> tagFriendToProduct(long productID,
			String usernames[], String username, boolean isTag) {
		ServiceResult<Set<String>> result = new ServiceResult<Set<String>>();
		result.setOK(true);
		UserInfo user = null;
		Product product = null;
		ServiceResult<Void> notiResult = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			product = pm.getObjectById(Product.class, productID);
			if (product == null) {
				result.setMessage(Global.messages.getString("no_found_product"));
			} else {
				if (!product.getUsername().equals(username)) {
					result.setOK(false);
					result.setMessage(String.format(
							Global.messages
									.getString(isTag ? "no_permission_to_tag_to_product"
											: "no_permission_to_untag_from_product"),
							username, productID));
				} else {
					if (isTag) {
						for (String user2Tag : usernames) {
							try {
								if (product.getSetFriendsTaggedID().contains(
										user2Tag)) {
									result.setOK(false);
									result.setMessage(result.getMessage()
											+ ";"
											+ String.format(
													Global.messages
															.getString("already_tag_user_to_product"),
													user2Tag, productID));
								} else {
									user = pm.getObjectById(UserInfo.class,
											user2Tag);
									user.getSetProductTaggedID().add(productID);
									product.getSetFriendsTaggedID().add(
											username);
									result.setMessage(result.getMessage()
											+ ";"
											+ String.format(
													Global.messages
															.getString("tag_user_to_product_successfully"),
													user2Tag, productID));
									notiResult = dbNoti
											.insertWhenTagUserToProduct(
													productID, user2Tag, isTag);

									if (notiResult.isOK() == false) {
										result.setMessage(result.getMessage()
												+ ";Notification Exception:"
												+ notiResult.getMessage());
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
								result.setOK(false);
								result.setMessage(result.getMessage()
										+ ";"
										+ String.format(
												Global.messages
														.getString("tag_user_to_product_fail"),
												user2Tag, productID));
							}
						}
					} else {
						for (String user2Tag : usernames) {
							try {
								if (!product.getSetFriendsTaggedID().contains(
										user2Tag)) {
									result.setOK(false);
									result.setMessage(result.getMessage()
											+ ";"
											+ String.format(
													Global.messages
															.getString("user_not_yet_tag_to_product"),
													user2Tag, productID));
								} else {
									user = pm.getObjectById(UserInfo.class,
											user2Tag);
									user.getSetProductTaggedID().remove(
											productID);
									product.getSetFriendsTaggedID().remove(
											user2Tag);
									result.setMessage(result.getMessage()
											+ ";"
											+ String.format(
													Global.messages
															.getString("untag_user_from_product_successfully"),
													user2Tag, productID));
									notiResult = dbNoti
											.insertWhenTagUserToProduct(
													productID, user2Tag, isTag);

									if (notiResult.isOK() == false) {
										result.setMessage(result.getMessage()
												+ ";Notification Exception:"
												+ notiResult.getMessage());
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
								result.setOK(false);
								result.setMessage(result.getMessage()
										+ ";"
										+ String.format(
												Global.messages
														.getString("untag_user_from_product_fail"),
												user2Tag, productID));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage(String.format(Global.messages
					.getString(isTag ? "tag_friend_to_product_fail"
							: "untag_friend_from_product_fail"), productID));
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (result.isOK()) {
			result.setResult(product.getSetFriendsTaggedID());
		}
		return result;
	}

	public ServiceResult<Void> voteProduct(Long productId, int star) {
		ServiceResult<Void> result = new ServiceResult<Void>();

		Product product = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			product = pm.getObjectById(Product.class, productId);
		} catch (Exception e) {
		}
		if (product == null) {
			result.setOK(false);
			result.setMessage(Global.messages.getString("no_found_product"));
		} else {
			product.setCount_vote(product.getCount_vote() + 1);
			product.setSum_star(product.getSum_star() + star);

			result.setOK(true);
			result.setMessage(Global.messages
					.getString("vote_product_successfully"));
		}
		try {
			pm.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	// TODO
	/**
	 * Username marks productid as interested product for later review
	 * 
	 * @param from
	 *            : In case the user 's interested many products, just load from
	 *            record <code>from</code> and with the maximum record is
	 *            <code>maxRecord</code>
	 * @param maxRecord
	 *            : The maximum number of record may be returned Guide: use
	 *            setRange(from, from+maxRecord);
	 */
	public ServiceResult<List<Long>> markAsInterestedProduct(String username,
			String[] productIdStrs, boolean isInterest) {
		ServiceResult<List<Long>> result = new ServiceResult<List<Long>>();
		result.setOK(true);
		UserInfo user = null;
		Product product = null;
		ServiceResult<Void> notiResult = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			user = pm.getObjectById(UserInfo.class, username);
			if (user == null) {
				result.setMessage(Global.messages.getString("not_found") + " "
						+ username);
			} else {
				if (isInterest) {
					for (String productIdStr : productIdStrs) {
//						try {
							product = pm.getObjectById(Product.class,
									Long.parseLong(productIdStr.trim()));
							if (product == null) {
								result.setOK(false);
								result.setMessage(result.getMessage()
										+ ";"
										+ String.format(
												Global.messages
														.getString("no_found_product_have_id"),
												productIdStr));
							} else {
								if (product.getListInterestedUsername()
										.contains(username)) {
									result.setOK(false);
									result.setMessage(result.getMessage()
											+ ";"
											+ String.format(
													Global.messages
															.getString("user_already_like_product"),
													username, productIdStr));
								} else {
									product.getListInterestedUsername().add(
											username);
									user.getListInterestedProductID().add(
											product.getId());
									result.setMessage(result.getMessage()
											+ ";"
											+ String.format(
													Global.messages
															.getString("user_like_product_successfully"),
													productIdStr));
								}
							}
						// } catch (Exception e) {
						// result.setOK(false);
						// Global.log(log, e.getMessage());
						// result.setMessage(result.getMessage()
						// + ";"
						// + String.format(
						// Global.messages
						// .getString("no_found_product_have_id"),
						// productIdStr));
						// }
					}
				} else {
					for (String productIdStr : productIdStrs) {
//						try {
							product = pm.getObjectById(Product.class,
									Long.parseLong(productIdStr.trim()));
							if (product == null) {
								result.setOK(false);
								result.setMessage(result.getMessage()
										+ ";"
										+ String.format(
												Global.messages
														.getString("no_found_product_have_id"),
												productIdStr));
							} else {
								if (!product.getListInterestedUsername()
										.contains(username)) {
									result.setMessage(result.getMessage()
											+ ";"
											+ String.format(
													Global.messages
															.getString("user_not_yet_like_product"),
													productIdStr));
									result.setOK(false);
								} else {
									product.getListInterestedUsername().remove(
											username);
									user.getListInterestedProductID().remove(
											product.getId());
									result.setMessage(result.getMessage()
											+ ";"
											+ String.format(
													Global.messages
															.getString("user_unlike_product_successfully"),
													productIdStr));
								}
							}
//						} catch (Exception e) {
//							result.setOK(false);
//							result.setMessage(result.getMessage()
//									+ ";"
//									+ String.format(
//											Global.messages
//													.getString("no_found_product_have_id"),
//											productIdStr));
//						}
					}
				}
				result.setResult(user.getListInterestedProductID());
			}

		} catch (Exception e) {
			result.setOK(false);
			Global.log(log, e.getMessage());
			result.setMessage(String.format(
					Global.messages.getString("user_like_product_fail")));
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * Get related producted with productId to recommend for user
	 * 
	 * @param productId
	 * @return
	 */
	public ServiceResult<List<Product>> getRelatedProducts(Long productId,
			int limit) {
		ServiceResult<List<Product>> result;

		Product product = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			product = pm.getObjectById(Product.class, productId);
		} catch (Exception e) {
		}

		if (product == null) {
			result = new ServiceResult<List<Product>>();
			result.setOK(false);
			result.setMessage(Global.messages.getString("no_found_product"));
		} else {
			result = searchProductLike(product.getName());

			Object[] tmp = product.getSetCategoryKeys().toArray();
			if (tmp != null && tmp.length > 0) {
				String[] catKeys = new String[tmp.length];
				for (int i = 0; i < tmp.length; i++) {
					catKeys[i] = tmp[i].toString();
				}
				ServiceResult<List<Product>> result2 = getListProductByCriteriaInCategories(
						limit, new int[] { 0 }, 1, null, null, null, catKeys);
				if (result2.isOK()) {
					result.getResult().addAll(result2.getResult());
					log.log(Level.SEVERE, "new result; " + result2.getResult());
				}
			}
			if (limit > result.getResult().size())
				limit = result.getResult().size();
			if (limit == 0)
				limit = result.getResult().size();

			result.setResult(result.getResult().subList(0, limit));

			// Remove duplicate record
			HashSet<Product> hashSet = new HashSet<Product>(result.getResult());
			result.getResult().clear();
			result.getResult().addAll(new ArrayList<Product>(hashSet));

			for (Product p : result.getResult()) {
				getReferenedField(pm, p);
			}

			if (result.isOK()) {
				result.getResult().remove(product);
				result.setMessage(Global.messages
						.getString("get_related_products_successfully"));
			}
		}
		try {
			pm.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static void preventSQLInjProduct(Product product) {
		// product.setName(DatabaseUtils.preventSQLInjection(product.getName()));
		// product.setAddress(DatabaseUtils.preventSQLInjection(product
		// .getAddress()));
	}

	private List<Product> getListProductFromIds(List<Long> listIds) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<Product> listProducts = new ArrayList<Product>();
		for (Long i : listIds) {
			Product product = pm.getObjectById(Product.class, i);
			product.getAttributeSets();
			listProducts.add(product);
		}

		return listProducts;
	}

	private void getReferenedField(PersistenceManager pm, Product product) {
		for (Long k : product.getListAttributeKeys()) {
			Attribute a = pm.getObjectById(Attribute.class, k);
			if (a != null)
				product.getAttributeSets().add(a);
		}

		for (Long k : product.getSetMediaKeys()) {
			Media m = pm.getObjectById(Media.class, k);
			if (m != null)
				product.getSetMedias().add(m);
		}
	}

}
