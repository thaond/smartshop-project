package vnfoss2010.smartshop.serverside.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.swing.text.html.StyleSheet.ListPainter;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.UserSubcribeProduct;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.GeocellQuery;
import com.beoui.geocell.model.Point;

public class UserSubcribeProductImpl {
	private static UserSubcribeProductImpl instance;
	static Logger log = Logger.getLogger(UserSubcribeProductImpl.class
			.getName());

	public ServiceResult<UserSubcribeProduct> findSubcribe(Long id) {
		ServiceResult<UserSubcribeProduct> result = new ServiceResult<UserSubcribeProduct>();
		UserSubcribeProduct subcribe = null;
		PersistenceManager pm = null;
		try {
			pm = PMF.get().getPersistenceManager();
			subcribe = pm.getObjectById(UserSubcribeProduct.class, id);
			if (subcribe == null) {
				result.setOK(false);
				result.setMessage("Khong tim thay subcribe");
			} else {
				result.setMessage("Tim thay subcribe");
				result.setOK(true);
				result.setResult(subcribe);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage("exception " + e.getMessage());
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

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
				result.setMessage("insert subcribe fail");
			} else {
				result.setResult(userSubcribeProduct.getId());
				result.setMessage("insert subcribe success");
				result.setOK(true);
			}
		} catch (Exception e) {
			result.setOK(false);
			result.setMessage("Exception " + e.getMessage());
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public ServiceResult<List<Product>> searchProductInSubcribeRange(
			UserSubcribeProduct subcribe) {
		Point center = subcribe.getLocation();
		Double maxDistance = subcribe.getRadius();

		ServiceResult<List<Product>> result = new ServiceResult<List<Product>>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		List<String> pa = subcribe.getCategoryList();
		// for (int i = 0; i < pa.size(); i++) {
		// GeocellQuery<String> baseQuery = new GeocellQuery<String>(
		// "setCategoryKeys.contains(catKey" + i + ")",
		// "String catKey" + i, Arrays.asList(pa.get(i)));

		GeocellQuery<String> baseQuery = new GeocellQuery<String>(
				"setCategoryKeys.contains(catKey)", "String catKey", pa);
		// }

		List<Product> listProduct = null;
		try {
			listProduct = GeocellManager.proximityFetch(center, 40,
					maxDistance, Product.class, baseQuery, pm);
			if (listProduct != null) {
				result.setOK(true);
				result.setResult(listProduct);
			}
		} catch (Exception e) {
			result.setOK(false);
			result.setMessage("Exception " + e.getMessage());
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public ServiceResult<List<UserSubcribeProduct>> findActiveSubcribe() {
		ServiceResult<List<UserSubcribeProduct>> result = new ServiceResult<List<UserSubcribeProduct>>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			String queryStr = "SELECT FROM "
					+ UserSubcribeProduct.class.getName()
					+ " where isActive == true";

			Query query = pm.newQuery(queryStr);
			List<UserSubcribeProduct> listSub = (List<UserSubcribeProduct>) query
					.execute(query);
			if (listSub == null) {
				result.setOK(false);
				result.setMessage("Tim subcribe co loi");
			} else {
				result.setOK(true);
				result.setResult(listSub);
			}
		} catch (Exception e) {
			result.setOK(false);
			result.setMessage("Exception " + e.getMessage());
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public ServiceResult<Void> editSubcribe(UserSubcribeProduct editSubcribe) {
		ServiceResult<Void> result = new ServiceResult<Void>();
		UserSubcribeProduct subcribe = null;
		PersistenceManager pm = null;
		try {
			pm = PMF.get().getPersistenceManager();
			subcribe = pm.getObjectById(UserSubcribeProduct.class,
					editSubcribe.getId());
			if (subcribe == null) {
				result.setOK(false);
				result.setMessage("Khong tim thay page");
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
				result.setMessage("Update thanh cong");
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

	public ServiceResult<List<UserSubcribeProduct>> findSubcribe(
			String userName, Date fromDate, Date toDate) {
		ServiceResult<List<UserSubcribeProduct>> result = new ServiceResult<List<UserSubcribeProduct>>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			String queryStr = "select from "
					+ UserSubcribeProduct.class.getName()
					+ " where userName == us"
					+ (fromDate == null ? "" : " && date >= fromDate")
					+ (toDate == null ? "" : " && date <= toDate");
			Query query = pm.newQuery(queryStr);
			query.declareParameters("String us"
					+ (fromDate == null ? "" : ", Date fromDate")
					+ (toDate == null ? "" : ", Date toDate"));
			query.declareImports("import java.util.Date");
			List<Object> listParameters = new ArrayList<Object>();
			listParameters.add(userName);
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

	public static UserSubcribeProductImpl instance() {
		if (instance == null) {
			instance = new UserSubcribeProductImpl();
		}
		return instance;
	}
}
