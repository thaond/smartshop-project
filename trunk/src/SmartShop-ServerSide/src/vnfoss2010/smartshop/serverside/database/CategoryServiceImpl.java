package vnfoss2010.smartshop.serverside.database;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.entity.Category;

public class CategoryServiceImpl {
	private static CategoryServiceImpl instance;

	public CategoryServiceImpl() {
		instance = this;
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
			result.setMessage(Global.messages.getString("no_found_category"));
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
			result.setMessage(Global.messages.getString("cannot_handle_with_null"));
		}

		try {
			category = pm.makePersistent(category);
			if (category == null) {
				result.setMessage(Global.messages.getString("insert_product_fail"));
			} else {
				result.setResult(true);
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

	public ServiceResult<Set<Category>> findCategories(Set<String> catKeys) {
		catKeys.iterator();
		ServiceResult<Set<Category>> result = new ServiceResult<Set<Category>>();
		Set<Category> setCategories = new HashSet<Category>();

		for (String catKey : catKeys) {
			ServiceResult<Category> catResult = findCategory(catKey);
			if (catResult.isOK()) {
				setCategories.add(catResult.getResult());
			}
		}

		if (setCategories.isEmpty()) {
			result.setOK(false);
			result.setMessage(Global.messages.getString("no_found_list_category"));
		} else {
			result.setOK(true);
			result.setResult(setCategories);
			result.setMessage(Global.messages.getString("get_list_category_successfully"));
		}

		return result;
	}
	
	public ServiceResult<List<Category>> getCategories() {
		ServiceResult<List<Category>> result = new ServiceResult<List<Category>>();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Category.class);
		
		List<Category> listCategories = (List<Category>) query.execute();
		if (listCategories!= null && listCategories.size()>0){
			result.setOK(true);
			result.setMessage(Global.messages.getString("get_categories_successfully"));
			result.setResult(listCategories);
		}else{
			result.setMessage(Global.messages.getString("get_categories_fail"));
		}

		return result;
	}
	
	public ServiceResult<List<Category>> getCategories(String cat_parent) {
		ServiceResult<List<Category>> result = new ServiceResult<List<Category>>();
		List<Category> setCategories = new ArrayList<Category>();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(Category.class);
		query.setFilter("parent_id == cat");
		query.declareParameters("String cat");
		
		List<Category> listCategories = (List<Category>) query.execute(cat_parent);
		if (listCategories!= null && listCategories.size()>0){
			result.setOK(true);
			result.setMessage(Global.messages.getString("get_categories_successfully"));
			result.setResult(listCategories);
		}else{
			result.setMessage(Global.messages.getString("get_categories_fail"));
		}
		
		return result;
	}

	public static CategoryServiceImpl getInstance() {
		if (instance == null) {
			instance = new CategoryServiceImpl();
		}
		return instance;
	}
}
