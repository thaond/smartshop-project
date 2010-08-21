package vnfoss2010.smartshop.serverside.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;

import com.google.appengine.api.datastore.DatastoreNeedIndexException;
import com.google.appengine.api.datastore.DatastoreTimeoutException;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.entity.Attribute;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;

public class ProductServiceImpl {
	private static ProductServiceImpl instance;

	private final static Logger log = Logger.getLogger(ProductServiceImpl.class
			.getName());

	private ProductServiceImpl() {
	}
	
	// PRODUCT
	/**
	 * Insert new product into database
	 * 
	 * @return id in the datastore
	 */
	public ServiceResult<Long> insertProduct(Product product) {
		preventSQLInjProduct(product);
		ServiceResult<Long> result = new ServiceResult<Long>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (product == null) {
			result.setMessage(Global.messages.getString("cannot_handle_with_null"));
		}

		try {
			pm.flush();
			product = pm.makePersistent(product);
			if (product == null) {
				result.setMessage(Global.messages.getString("insert_product_fail"));
			} else {
				result.setResult(product.getId());
				result.setMessage(Global.messages
						.getString("insert_product_successfully"));
				result.setOK(true);
			}
		} catch (Exception e) {
			Global.log(log, Arrays.toString(e.getStackTrace()));
			result.setMessage(Global.messages.getString("insert_product_fail"));
		}

		return result;
	}
	
	//TODO
	public ServiceResult<Void> updateProduct(Product product){
		return null;
	}

	public ServiceResult<Long> insertProduct(Product product,
			List<String> listCategories, List<Attribute> listAttributes) {
		preventSQLInjProduct(product);
		ServiceResult<Long> result = new ServiceResult<Long>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (product == null) {
			result.setMessage(Global.messages.getString("cannot_handle_with_null"));
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
				result.setMessage(Global.messages.getString("insert_product_fail"));
			} else {
				result.setResult(product.getId());
				result.setMessage(Global.messages
						.getString("insert_product_successfully"));
				result.setOK(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage(Global.messages.getString("insert_list_userinfos_fail"));
		}

		return result;
	}

	public ServiceResult<Product> findProduct(Long id) {
		ServiceResult<Product> result = new ServiceResult<Product>();
		Product product = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		product = pm.getObjectById(Product.class, id);
		if (product == null) {
			result.setOK(false);
			result.setMessage(Global.messages.getString("no_found_product"));
		} else {
			result.setOK(true);
			result.setResult(product);
		}
		return result;
	}

	public static ProductServiceImpl getInstance() {
		if (instance == null) {
			instance = new ProductServiceImpl();
		}
		return instance;
	}

	enum SearchCriteria {
		DATE_POST_ASC, DATE_POST_DESC, PRICE_ASC, PRICE_DESC, NUM_BUYER_ASC, NUM_BUYER_DESC
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
	@SuppressWarnings("unchecked")
	public ServiceResult<List<Product>> getListProductByCriteria(int maximum,
			int[] criterias) {
		ServiceResult<List<Product>> result = new ServiceResult<List<Product>>();

		String query = "";
		for (int criteria : criterias) {
			switch (criteria) {
			case 0:
				query += ("date_post asc ");
				break;

			case 1:
				query += ("date_post desc ");
				break;

			case 2:
				query += ("price asc ");
				break;

			case 3:
				query += ("price desc ");
				break;

			// TODO
			case 4:

				break;

			case 5:

				break;

			default:
				break;
			}
		}
		query = "select from " + Product.class.getName() + " order by " + query
				+ ((maximum == 0) ? "" : (" limit " + maximum));

		Global.log(log, query);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<Product> listProducts = (List<Product>) pm.newQuery(query)
				.execute();
		if (listProducts.size() > 0) {
			result.setOK(true);
			result.setMessage(Global.messages
					.getString("search_product_by_criteria_successfully"));
			result.setResult(listProducts);
		} else {
			result.setOK(false);
			result.setMessage(Global.messages
					.getString("search_product_by_criteria_fail"));
		}

		return result;
	}
	
	/**
	 * 
	 * @param maximum
	 *            = 0 means get as much as possible
	 * @param criterias
	 *            : String as list of integers, seperated by comma (|)
	 *            <i>Ex:</i>1,3,4
	 * @param cat_keys List categories you want to search in, separated by ,
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ServiceResult<List<Product>> getListProductByCriteriaInCategories(int maximum,
			int[] criterias,String... cat_keys) {
		ServiceResult<List<Product>> result = new ServiceResult<List<Product>>();

		String queryString = "";
		for (int criteria : criterias) {
			switch (criteria) {
			case 0:
				queryString += ("date_post asc ");
				break;

			case 1:
				queryString += ("date_post desc ");
				break;

			case 2:
				queryString += ("price asc ");
				break;

			case 3:
				queryString += ("price desc ");
				break;

			// TODO
			case 4:

				break;

			case 5:

				break;

			default:
				break;
			}
		}
		
		queryString = "select from " + Product.class.getName() + " order by " + queryString
				+ ((maximum == 0) ? "" : (" limit " + maximum));

		Global.log(log, queryString);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Query query = pm.newQuery(queryString);
		query.setFilter("setCategoryKeys.contains(catKey)" );
		query.declareParameters("String catKey");
		
		List<Product> listProducts = (List<Product>) query.execute(Arrays.asList(cat_keys));

		if (listProducts.size() > 0) {
			result.setOK(true);
			result.setMessage(Global.messages
					.getString("search_product_by_criteria_in_cat_successfully"));
			result.setResult(listProducts);
		} else {
			result.setOK(false);
			result.setMessage(Global.messages
					.getString("search_product_by_criteria_in_cat_fail"));
		}

		return result;
	}
	
	public ServiceResult<List<Product>> searchProdcutLike(String queryString) {
		queryString = DatabaseUtils.preventSQLInjection(queryString);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("SELECT FROM " + Product.class.getName()
				+ " WHERE ");
		Set<String> queryTokens = SearchJanitorUtils
				.getTokensForIndexingOrQuery(queryString,
						Global.MAXIMUM_NUMBER_OF_WORDS_TO_SEARCH);
		List<String> parametersForSearch = new ArrayList<String>(queryTokens);
		StringBuffer declareParametersBuffer = new StringBuffer();
		int parameterCounter = 0;

		while (parameterCounter < queryTokens.size()) {

			queryBuffer.append("fts == param" + parameterCounter);
			declareParametersBuffer.append("String param" + parameterCounter);

			if (parameterCounter + 1 < queryTokens.size()) {
				queryBuffer.append(" && ");
				declareParametersBuffer.append(", ");
			}
			parameterCounter++;
		}

		Query query = pm.newQuery(queryBuffer.toString());
		query.declareParameters(declareParametersBuffer.toString());

		List<Product> listProducts = null;
		ServiceResult<List<Product>> result = new ServiceResult<List<Product>>();

		try {
			listProducts = (List<Product>) query
					.executeWithArray(parametersForSearch.toArray());
			
			//TODO return basic information
			if (listProducts.size()>0){
				result.setResult(listProducts);
				result.setOK(true);
				result.setMessage(Global.messages.getString("search_product_by_query_successfully"));
			}else{
				result.setOK(false);
				result.setMessage(Global.messages.getString("search_product_by_query_fail"));
			}
		} catch (DatastoreTimeoutException e) {
			log.severe(e.getMessage());
			log.severe("datastore timeout at: " + queryString);// +
			result.setOK(false);
			result.setMessage(Global.messages.getString("have_problem"));
			// " - timestamp: "
			// +
			// discreteTimestamp);
		} catch (DatastoreNeedIndexException e) {
			log.severe(e.getMessage());
			log.severe("datastore need index exception at: " + queryString);// +
			result.setOK(false);
			result.setMessage(Global.messages.getString("have_problem"));
			// " - timestamp: "
			// +
			// discreteTimestamp);
		}

		return result;
	}
	
	public static void updateFTSStuffForUserInfo(Product product) {
		StringBuffer sb = new StringBuffer();
		sb.append(product.getName() + " " + product.getAddress());
		
		for (Attribute att : product.getAttributeSets()){
			sb.append(att.getName());
		}
		Set<String> new_ftsTokens = SearchJanitorUtils
				.getTokensForIndexingOrQuery(sb.toString(),
						Global.MAX_NUMBER_OF_WORDS_TO_PUT_IN_INDEX);
		Set<String> ftsTokens = product.getFts();
		ftsTokens.clear();

		for (String token : new_ftsTokens) {
			ftsTokens.add(token);
		}
	}
	
	public static void preventSQLInjProduct(Product product) {
		product.setName(DatabaseUtils.preventSQLInjection(product.getName()));
		product.setAddress(DatabaseUtils.preventSQLInjection(product.getAddress()));
	}
}
