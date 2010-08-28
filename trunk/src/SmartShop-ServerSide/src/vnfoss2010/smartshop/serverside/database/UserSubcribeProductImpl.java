package vnfoss2010.smartshop.serverside.database;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.jdo.PersistenceManager;

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
//		for (int i = 0; i < pa.size(); i++) {
//			GeocellQuery<String> baseQuery = new GeocellQuery<String>(
//					"setCategoryKeys.contains(catKey" + i + ")",
//					"String catKey" + i, Arrays.asList(pa.get(i)));

			GeocellQuery<String> baseQuery = new GeocellQuery<String>(
					"setCategoryKeys.contains(catKey)", "String catKey", pa);
		//		}

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

	public static UserSubcribeProductImpl instance() {
		if (instance == null) {
			instance = new UserSubcribeProductImpl();
		}
		return instance;
	}
}
