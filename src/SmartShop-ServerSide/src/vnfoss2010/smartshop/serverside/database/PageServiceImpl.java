package vnfoss2010.smartshop.serverside.database;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;
import vnfoss2010.smartshop.serverside.utils.StringUtils;

public class PageServiceImpl {
	private static PageServiceImpl instance;

	private final static Logger log = Logger.getLogger(ProductServiceImpl.class
			.getName());

	private PageServiceImpl() {
		instance = this;
	}

	public ServiceResult<Page> findPage(Long id) {
		ServiceResult<Page> result = new ServiceResult<Page>();
		Page page = null;
		PersistenceManager pm = null;
		try {
			pm = PMF.get().getPersistenceManager();
			page = pm.getObjectById(Page.class, id);
			if (page == null) {
				result.setOK(false);
				result.setMessage(Global.messages
						.getString("khong tim thay page"));
			} else {
				result.setMessage("Tim thay page");
				result.setOK(true);
				result.setResult(page);
			}
		} catch (Exception e) {
			result.setMessage("khong tim thay page " + e.getMessage());
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public ServiceResult<Long> insertPage(Page page) {
		// updateFTSStuffForPage(page);
		page.updateFTS();
		ServiceResult<Long> result = new ServiceResult<Long>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			page = pm.makePersistent(page);
			if (page == null) {
				result.setMessage("khong the insert page");
			} else {
				result.setResult(page.getId());
				result.setMessage("insert page thanh cong");
				result.setOK(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage("khong the insert page " + e.getMessage());
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public ServiceResult<Void> tagProductToPage(Long pageID, Long productID) {
		ServiceResult<Void> result = new ServiceResult<Void>();
		Page page = null;
		Product product = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			page = pm.getObjectById(Page.class, pageID);
			product = pm.getObjectById(Product.class, productID);
			if (page == null || product == null) {
				result.setMessage("Khong the tim thay page hay product");
			} else {
				page.getSetProduct().add(productID);
				product.getSetPagesID().add(pageID);
				result.setOK(true);
				result.setMessage("Tag product to page thanh cong");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage("khong the insert page " + e.getMessage());
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public ServiceResult<Void> untagProductToPage(Long pageID, Long productID) {
		ServiceResult<Void> result = new ServiceResult<Void>();
		Page page = null;
		Product product = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			page = pm.getObjectById(Page.class, pageID);
			product = pm.getObjectById(Product.class, productID);
			if (page == null || product == null) {
				result.setMessage("Khong the tim thay page hay product");
			} else {
				page.getSetProduct().remove(productID);
				product.getSetPagesID().remove(pageID);
				result.setOK(true);
				result.setMessage("Tag product to page thanh cong");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage("khong the insert page " + e.getMessage());
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public ServiceResult<Void> updatePage(Page editPage) {
		ServiceResult<Void> result = new ServiceResult<Void>();
		Page page = null;
		PersistenceManager pm = null;
		try {
			pm = PMF.get().getPersistenceManager();
			page = pm.getObjectById(Page.class, editPage.getId());
			if (page == null) {
				result.setOK(false);
				result.setMessage("Khong tim thay page");
			} else {
				page.setContent(editPage.getContent());
				page.setDate_post(editPage.getDate_post());
				page.setLast_modified(editPage.getLast_modified());
				page.setLink_thumbnail(editPage.getLink_thumbnail());
				page.setName(editPage.getName());
				page.setPage_view(editPage.getPage_view());
				page.setSetCategoryKeys(editPage.getSetCategoryKeys());
				page.setSetProduct(editPage.getSetProduct());
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

	public ServiceResult<List<Page>> searchPageLike(String queryString) {
		queryString = DatabaseUtils.preventSQLInjection(queryString);

		List<Page> listPages = null;
		ServiceResult<List<Page>> result = new ServiceResult<List<Page>>();

		listPages = DatabaseUtils.searchByQuery(queryString, Page.class);
		if (listPages.size() > 0) {
			result.setResult(listPages);
			result.setOK(true);
			result.setMessage(Global.messages
					.getString("search_page_by_query_successfully"));
		} else {
			result.setOK(false);
			result.setMessage(Global.messages
					.getString("search_page_by_query_fail"));
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public ServiceResult<List<Page>> getListPageByCriteria(int maximum,
			int[] criterias, String username, String... cat_keys) {
		ServiceResult<List<Page>> result = new ServiceResult<List<Page>>();

		String query = "";
		if (criterias != null) {
			query = " order by ";
			for (int criteria : criterias) {
				switch (criteria) {
				case 0:
					query += ("date_post asc ");
					break;

				case 1:
					query += ("date_post desc ");
					break;

				case 2:
					query += ("last_modified asc ");
					break;

				case 3:
					query += ("last_modified desc ");
					break;

				case 4:
					query += ("page_view asc ");
					break;

				case 5:
					query += ("page_view desc ");
					break;

				default:
					break;
				}
			}
		} else {
			query = " order by date_post desc ";
		}

		query = "select from " + Page.class.getName() + query
				+ ((maximum == 0) ? "" : (" limit " + maximum));

		Global.log(log, query);
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query queryObj = pm.newQuery(query);
		queryObj.setFilter("setCategoryKeys.contains(catKey)");

		List<Page> listPages = null;
		if (!StringUtils.isEmptyOrNull(username)) {
			if (cat_keys != null) {
				queryObj.setFilter("setCategoryKeys.contains(catKey)");
				queryObj.setFilter("username==us");
				queryObj.declareParameters("String catKey, String us");

				log.log(Level.SEVERE, Arrays.toString(cat_keys) + "");
				listPages = (List<Page>) queryObj.execute(Arrays
						.asList(cat_keys), username);
			} else {
				queryObj.setFilter("username==us");
				queryObj.declareParameters("String us");
				listPages = (List<Page>) queryObj.execute(username);
			}
		} else {
			// Duplicate
			if (cat_keys != null) {
				queryObj.setFilter("setCategoryKeys.contains(catKey)");
				queryObj.declareParameters("String catKey");
				log.log(Level.SEVERE, Arrays.toString(cat_keys) + "");
				listPages = (List<Page>) queryObj.execute(Arrays
						.asList(cat_keys));
			} else {
				log.log(Level.SEVERE, "query = " + query);
				// Fix bug: The first sort property must be the same as the
				// property to which the inequality filter is applied
				// query = query.replace("order by ",
				// "order by quantity desc ");
				listPages = (List<Page>) queryObj.execute();
			}
		}

		if (listPages.size() > 0) {
			result.setOK(true);
			result
					.setMessage(Global.messages
							.getString("search_product_by_criteria_in_cat_successfully"));
			result.setResult(listPages);
		} else {
			result.setOK(false);
			result.setMessage(Global.messages
					.getString("search_product_by_criteria_in_cat_fail"));
		}

		return result;
	}

	public ServiceResult<List<Page>> getListPageFromUsername(String username) {
		username = DatabaseUtils.preventSQLInjection(username);
		ServiceResult<List<Page>> result = new ServiceResult<List<Page>>();
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
			Query query = pm.newQuery(Page.class);
			query.setFilter("username == us");
			query.declareParameters("String us");
			query.setOrdering("date_post DESC");
			List<Page> listPages = (List<Page>) query.execute(username);

			if (listPages.size() > 0) {
				result.setOK(true);
				result.setMessage(String.format(Global.messages
						.getString("get_pages_by_username_successfully"),
						username));
				result.setResult(listPages);
			} else {
				result.setOK(false);
				result.setMessage(String.format(Global.messages
						.getString("get_pages_by_username_fail"), username));
			}
		}
		pm.close();
		return result;

	}

	public static void preventSQLInjPage(Page page) {
		page.setName(DatabaseUtils.preventSQLInjection(page.getName()));
		page.setContent(DatabaseUtils.preventSQLInjection(page.getContent()));
	}

	public static PageServiceImpl getInstance() {
		if (instance == null) {
			instance = new PageServiceImpl();
		}
		return instance;
	}
}
