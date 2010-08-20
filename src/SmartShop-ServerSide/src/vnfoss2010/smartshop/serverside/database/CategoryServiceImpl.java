package vnfoss2010.smartshop.serverside.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Set;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;

import vnfoss2010.smartshop.serverside.database.entity.Category;

public class CategoryServiceImpl {
	private static CategoryServiceImpl instance;
	private ResourceBundle messages;

	public CategoryServiceImpl() {
		messages = ResourceBundle
				.getBundle("vnfoss2010/smartshop/serverside.localization/MessagesBundle");
	}

	public ServiceResult<Category> findCategory(String catKey) {
		ServiceResult<Category> result = new ServiceResult<Category>();
		Category category = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			category = (Category) pm.getObjectById(Category.class, catKey);
		} catch (NucleusObjectNotFoundException e) {
		} catch (JDOObjectNotFoundException e) {
		}
		if (category == null) {
			result.setOK(false);
			result.setMessage("Can't find category");
		} else {
			result.setOK(true);
			result.setResult(category);
		}
		return result;
	}

	public ServiceResult<Boolean> insertCategory(Category category) {
		ServiceResult<Boolean> result = new ServiceResult<Boolean>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (category == null) {
			result.setMessage(messages.getString("cannot_handle_with_null"));
		}

		try {
			category = pm.makePersistent(category);
			if (category == null) {
				result.setMessage(messages.getString("insert_product_fail"));
			} else {
				result.setResult(true);
				result.setMessage(messages
						.getString("insert_product_successfully"));
				result.setOK(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage(messages.getString("insert_list_userinfos_fail"));
		}

		return result;
	}

	public ServiceResult<ArrayList<Category>> findCategories(Set<String> catKeys) {
		catKeys.iterator();
		ArrayList<String> listCatKey = new ArrayList<String>();
		for (String catKey : catKeys) {
			listCatKey.add(catKey);
		}
		return findCategories(listCatKey);
	}

	public ServiceResult<ArrayList<Category>> findCategories(
			ArrayList<String> catKeys) {
		ServiceResult<ArrayList<Category>> result = new ServiceResult<ArrayList<Category>>();

		ArrayList<Category> listCats = new ArrayList<Category>();

		for (String catKey : catKeys) {
			ServiceResult<Category> catResult = findCategory(catKey);
			if (catResult.isOK()) {
				listCats.add(catResult.getResult());
			} else {
				listCats.clear();
				break;
			}
		}

		if (listCats.isEmpty()) {
			result.setOK(false);
			result.setMessage("Can't find list category");
		} else {
			result.setOK(true);
			result.setResult(listCats);
			result.setMessage("Find list category successfully");
		}

		return result;
	}

	public static CategoryServiceImpl instance() {
		if (instance == null) {
			instance = new CategoryServiceImpl();
		}
		return instance;
	}
}
