package vnfoss2010.smartshop.serverside.database;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.UserSubcribeProduct;

import com.beoui.geocell.GeocellManager;
import com.beoui.geocell.model.GeocellQuery;
import com.beoui.geocell.model.Point;

public class UserSubcribeProductImpl {
	private static UserSubcribeProductImpl instance;

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
			e.printStackTrace();
			result.setMessage("insert product fail " + e.getMessage());
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
			e.printStackTrace();
			result.setOK(false);
		}

		return result;
	}

	public ServiceResult<List<UserSubcribeProduct>> findActiveSubcribe() {
		ServiceResult<List<UserSubcribeProduct>> result = new ServiceResult<List<UserSubcribeProduct>>();
		try {
			String queryStr = "SELECT FROM "
					+ UserSubcribeProduct.class.getName() + " where isActive == true";
			
			PersistenceManager pm = PMF.get().getPersistenceManager();
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
			e.printStackTrace();
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
			result.setMessage("exception " + e.getMessage());
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

	public static UserSubcribeProductImpl instance() {
		if (instance == null) {
			instance = new UserSubcribeProductImpl();
		}
		return instance;
	}
}
