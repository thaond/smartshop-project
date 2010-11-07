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

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;
import vnfoss2010.smartshop.serverside.utils.SearchJanitorUtils;
import vnfoss2010.smartshop.serverside.utils.StringUtils;

public class PageServiceImpl {
	private static PageServiceImpl instance;
	private static NotificationServiceImpl dbNoti;
	private static AccountServiceImpl dbAccount;
	private final static Logger log = Logger.getLogger(PageServiceImpl.class
			.getName());

	private PageServiceImpl() {
		instance = this;
		dbNoti = NotificationServiceImpl.getInstance();
		dbAccount = AccountServiceImpl.getInstance();
	}

	public ServiceResult<Page> findPage(Long id) {
		ServiceResult<Page> result = findPage(id, false);
		return result;
	}

	public ServiceResult<Page> findPage(Long id, boolean isIncreaseView) {
		ServiceResult<Page> result = new ServiceResult<Page>();
		Page page = null;
		PersistenceManager pm = null;
		try {
			pm = PMF.get().getPersistenceManager();
			page = pm.getObjectById(Page.class, id);
			if (page == null) {
				result.setOK(false);
				result.setMessage(Global.messages.getString("no_page_found"));
			} else {
				result.setMessage(Global.messages.getString("page_found"));
				result.setOK(true);
				result.setResult(page);
				if (isIncreaseView) {
					page.setPage_view(page.getPage_view() + 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage(Global.messages.getString("find_page_fail"));
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public ServiceResult<Set<String>> tagFriendToPage(long pageID,
			String[] usernames, String username) {
		ServiceResult<Set<String>> result = new ServiceResult<Set<String>>();
		result.setOK(true);
		PersistenceManager pm = null;
		UserInfo user = null;
		Page page = null;
		try {
			pm = PMF.get().getPersistenceManager();
			page = pm.getObjectById(Page.class, pageID);
			if (page == null) {
				result.setOK(false);
				result.setMessage(Global.messages.getString("no_page_found"));
			} else {
				if (!page.getUsername().equals(username)) {
					result.setOK(false);
					result.setMessage(String.format(Global.messages
							.getString("no_permission_to_tag_to_page"),
							username));
				} else {
					ServiceResult<Void> notiResult = null;
					for (String user2Tag : usernames) {
						try {
							if (page.getSetFriendsTaggedID().contains(user2Tag)) {
								result.setOK(false);
								result.setMessage(result.getMessage()
										+ ";"
										+ String.format(
												Global.messages
														.getString("already_tag_user_to_page"),
												user2Tag, pageID));
							} else {
								user = pm.getObjectById(UserInfo.class,
										user2Tag);
								page.getSetFriendsTaggedID().add(user2Tag);
								user.getSetPageTaggedID().add(pageID);
								result.setMessage(result.getMessage()
										+ ";"
										+ String.format(
												Global.messages
														.getString("tag_user_to_page_successfully"),
												user2Tag, pageID));
								// notification
								notiResult = dbNoti.insertWhenTagUserToPage(
										pageID, user2Tag, true);
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
													.getString("tag_user_to_page_fail"),
											user2Tag, pageID));
						}
					}
					// if (result.isOK()) {
					// result.setMessage("Tag friends thanh cong");
					// }
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage(Global.messages
					.getString("tag_friend_to_page_fail"));
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (result.isOK()) {
			result.setResult(page.getSetFriendsTaggedID());
		}
		return result;
	}

	public ServiceResult<Set<String>> untagFriendFromPage(long pageID,
			String[] usernames, String username) {
		ServiceResult<Set<String>> result = new ServiceResult<Set<String>>();
		result.setOK(true);
		UserInfo user = null;
		PersistenceManager pm = null;
		Page page = null;
		try {
			pm = PMF.get().getPersistenceManager();
			page = pm.getObjectById(Page.class, pageID);
			if (page == null) {
				result.setOK(false);
				result.setMessage(Global.messages.getString("no_page_found"));
			} else {
				if (!page.getUsername().equals(username)) {
					result.setOK(false);
					result.setMessage(String.format(Global.messages
							.getString("no_permission_to_untag_from_page"),
							username));
				} else {
					ServiceResult<Void> notiResult = null;
					for (String user2Tag : usernames) {
						try {
							if (!page.getSetFriendsTaggedID()
									.contains(user2Tag)) {
								result.setOK(false);
								result.setMessage(result.getMessage()
										+ ";"
										+ String.format(
												Global.messages
														.getString("user_not_yet_tag_to_page"),
												user2Tag, pageID));
							} else {
								user = pm.getObjectById(UserInfo.class,
										user2Tag);
								page.getSetFriendsTaggedID().remove(user2Tag);
								user.getSetPageTaggedID().remove(pageID);
								result.setMessage(result.getMessage()
										+ ";"
										+ String.format(
												Global.messages
														.getString("untag_user_from_page_successfully"),
												user2Tag, pageID));
								// notification
								notiResult = dbNoti.insertWhenTagUserToPage(
										pageID, user2Tag, false);
								if (notiResult.isOK() == false) {
									result.setMessage(result.getMessage()
											+ ";Notification Exception:"
											+ notiResult.getMessage());
								}
							}
						} catch (Exception e) {
							result.setOK(false);
							result.setMessage(result.getMessage()
									+ ";"
									+ String.format(
											Global.messages
													.getString("untag_user_from_page_fail"),
											user2Tag, pageID));
						}
					}
					// result.setMessage("unTag friends thanh cong");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage(Global.messages
					.getString("untag_friend_from_page_fail"));
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (result.isOK()) {
			result.setResult(page.getSetFriendsTaggedID());
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
				result.setMessage(Global.messages.getString("insert_page_fail"));
			} else {
				result.setResult(page.getId());
				result.setMessage(Global.messages
						.getString("insert_page_successfully"));
				result.setOK(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage(Global.messages.getString("insert_page_fail"));
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
				result.setMessage(Global.messages
						.getString("no_page_or_product"));
			} else {
				page.getSetProduct().add(productID);
				product.getSetPagesID().add(pageID);
				result.setOK(true);
				result.setMessage(Global.messages
						.getString("tag_product_into_page_successfully"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage(Global.messages
					.getString("tag_product_into_page_fail"));
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (result.isOK()) {
			ServiceResult<Void> notiResult = dbNoti
					.insertWhenUserTagProductToPage(pageID, productID, true);
			if (notiResult.isOK() == false) {
				result.setMessage(result.getMessage()
						+ ";Notification Exception:" + notiResult.getMessage());
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
				result.setMessage(Global.messages
						.getString("no_page_or_product"));
			} else {
				page.getSetProduct().remove(productID);
				product.getSetPagesID().remove(pageID);
				result.setOK(true);
				result.setMessage(Global.messages
						.getString("untag_product_into_page_successfully"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage(Global.messages
					.getString("untag_product_into_page_fail"));
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (result.isOK()) {
			ServiceResult<Void> notiResult = dbNoti
					.insertWhenUserTagProductToPage(pageID, productID, false);
			if (notiResult.isOK() == false) {
				result.setMessage(result.getMessage()
						+ ";Notification Exception:" + notiResult.getMessage());
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
				result.setMessage(Global.messages.getString("no_page_found"));
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
				result.setMessage(Global.messages
						.getString("update_page_successfully"));
			}
		} catch (Exception e) {
			result.setMessage(Global.messages.getString("update_page_fail"));
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
			int[] criterias, String username, String q, String... cat_keys) {
		ServiceResult<List<Page>> result = new ServiceResult<List<Page>>();

		StringBuilder where = new StringBuilder();
		StringBuilder orderBy = new StringBuilder();
		String query = "";
		List<Object> listParameters = new ArrayList<Object>();

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
					orderBy.append("page_view asc ");
					break;

				case 5:
					orderBy.append("page_view desc ");
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
		List<Page> listPages = null;

		if (StringUtils.isEmptyOrNull(q)) {
			if (!StringUtils.isEmptyOrNull(username)) {
				if (cat_keys != null) {
					if (!StringUtils.isEmptyOrNull(where.toString()))
						where.append(" && ");
					where.append("setCategoryKeys.contains(catKey) && username==us ");
					listParameters.add(Arrays.asList(cat_keys));
					listParameters.add(username);

					query = "select from " + Page.class.getName() + " where ("
							+ where.toString() + ") order by "
							+ orderBy.toString()
							+ ((maximum == 0) ? "" : (" limit " + maximum));
					queryObj = pm.newQuery(query);
					queryObj.declareParameters("String catKey, String us");
					listPages = (List<Page>) queryObj
							.executeWithArray(listParameters.toArray());
				} else {
					if (!StringUtils.isEmptyOrNull(where.toString()))
						where.append(" && ");
					where.append("username==us ");
					listParameters.add(username);

					query = "select from " + Page.class.getName() + " where ("
							+ where.toString() + ") order by "
							+ orderBy.toString()
							+ ((maximum == 0) ? "" : (" limit " + maximum));

					queryObj = pm.newQuery(query);
					queryObj.declareParameters("String us");

					listPages = (List<Page>) queryObj
							.executeWithArray(listParameters.toArray());
				}
			} else { // end if q>username
				// Duplicate
				if (cat_keys != null) {
					if (!StringUtils.isEmptyOrNull(where.toString()))
						where.append(" && ");
					where.append("setCategoryKeys.contains(catKey) ");
					query = "select from " + Page.class.getName() + " where ("
							+ where.toString() + ") order by "
							+ orderBy.toString()
							+ ((maximum == 0) ? "" : (" limit " + maximum));
					queryObj = pm.newQuery(query);
					queryObj.declareParameters("String catKey");
					listParameters.add(Arrays.asList(cat_keys));
					listPages = (List<Page>) queryObj
							.executeWithArray(listParameters.toArray());
				} else {
					query = "select from "
							+ Page.class.getName()
							+ (StringUtils.isEmptyOrNull(where.toString()) ? ""
									: " where (" + where.toString() + ")")
							+ " order by " + orderBy.toString()
							+ ((maximum == 0) ? "" : (" limit " + maximum));
					queryObj = pm.newQuery(query);
					listPages = (List<Page>) queryObj.execute();
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

					query = "select from " + Page.class.getName() + " where ("
							+ where.toString() + ") order by "
							+ orderBy.toString()
							+ ((maximum == 0) ? "" : (" limit " + maximum));
					queryObj = pm.newQuery(query);
					queryObj.declareParameters("String catKey, String us"
							+ declareParametersBuffer.toString());

					listPages = (List<Page>) queryObj
							.executeWithArray(listParameters.toArray());

				} else {
					if (!StringUtils.isEmptyOrNull(where.toString()))
						where.append(" && ");
					where.append("username==us && ");
					where.append(queryBuffer.toString());

					listParameters.add(username);
					// listParameters.add(parametersForSearch.toArray());
					for (Object str : parametersForSearch)
						listParameters.add(str);

					query = "select from " + Page.class.getName() + " where ("
							+ where.toString() + ") order by "
							+ orderBy.toString()
							+ ((maximum == 0) ? "" : (" limit " + maximum));

					queryObj = pm.newQuery(query);
					queryObj.declareParameters("String us"
							+ declareParametersBuffer.toString());

					listPages = (List<Page>) queryObj
							.executeWithArray(listParameters.toArray());
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

					query = "select from " + Page.class.getName() + " where ("
							+ where.toString() + ") order by "
							+ orderBy.toString()
							+ ((maximum == 0) ? "" : (" limit " + maximum));
					queryObj = pm.newQuery(query);
					queryObj.declareParameters("String catKey"
							+ declareParametersBuffer.toString());

					listPages = (List<Page>) queryObj
							.executeWithArray(listParameters.toArray());
				} else {
					if (!StringUtils.isEmptyOrNull(where.toString()))
						where.append(" && ");
					where.append(queryBuffer.toString());
					// listParameters.add(parametersForSearch.toArray());
					for (Object str : parametersForSearch)
						listParameters.add(str);

					query = "select from " + Page.class.getName() + " where ("
							+ where.toString() + ") order by "
							+ orderBy.toString()
							+ ((maximum == 0) ? "" : (" limit " + maximum));

					queryObj = pm.newQuery(query);
					queryObj.declareParameters(declareParametersBuffer
							.toString());

					listPages = (List<Page>) queryObj
							.executeWithArray(listParameters.toArray());
				}
			}
		}// end else q

		if (listPages.size() > 0) {
			result.setOK(true);
			result.setMessage(Global.messages
					.getString("search_page_by_criteria_successfully"));
			result.setResult(listPages);
		} else {
			result.setOK(false);
			result.setMessage(Global.messages
					.getString("search_page_by_criteria_fail"));
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
				result.setMessage(String.format(
						Global.messages.getString("get_pages_by_username_fail"),
						username));
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
