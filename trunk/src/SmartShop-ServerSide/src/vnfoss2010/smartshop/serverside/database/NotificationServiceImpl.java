package vnfoss2010.smartshop.serverside.database;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.entity.Comment;
import vnfoss2010.smartshop.serverside.database.entity.Notification;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;

public class NotificationServiceImpl {
	private static NotificationServiceImpl instance;

	private AccountServiceImpl dbAccount;
	private ProductServiceImpl dbProduct;
	private PageServiceImpl dbPage;

	private static Logger log = Logger.getLogger(NotificationServiceImpl.class
			.getName());

	public NotificationServiceImpl() {
		instance = this;
		dbAccount = AccountServiceImpl.getInstance();
		dbProduct = ProductServiceImpl.getInstance();
		dbPage = PageServiceImpl.getInstance();
	}

	public ServiceResult<Long> insertNotification(Notification n) {
		ServiceResult<Long> result = new ServiceResult<Long>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (n == null) {
			result.setMessage(Global.messages
					.getString("cannot_handle_with_null"));
		}
		try {
			if (dbAccount.isExist(n.getUsername()).isOK()) {
				n = pm.makePersistent(n);

				if (n == null) {
					result.setMessage(Global.messages
							.getString("insert_nofitification_fail"));
				} else {
					result.setResult(n.getId());
					result.setMessage(Global.messages
							.getString("insert_nofitification_successfully"));
					result.setOK(true);
					// NotificationUtils.sendNotification(
					// Global.dfFull.format(n.getTimestamp()), n.getContent());
				}
			} else {
				result.setMessage(Global.messages.getString("not_found") + " "
						+ n.getUsername());
				// NotificationUtils.sendNotification(
				// Global.dfFull.format(n.getTimestamp()), n.getContent());
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setOK(false);
			result.setMessage(Global.messages
					.getString("insert_nofitification_fail"));
		} finally {

			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public ServiceResult<List<Notification>> getListNotificationsByUsername(
			String username, int limit, int type, long lastupdate) {
		ServiceResult<List<Notification>> result = new ServiceResult<List<Notification>>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (dbAccount.isExist(username).isOK()) {
			Query query = pm.newQuery(Notification.class);
			String filter = "username == us";
			String declear = "String us";
			if (limit > 0)
				query.setRange(0, limit);

			if (type == 1) {
				filter += " && isNew == true";
			} else if (type == 2) {
				filter += " && isNew == false";
			}

			List<Notification> listNotifications;
			if (lastupdate > 0) {
				filter += " && timestamp >= lu";
				declear += ", Long lu";
				query.setFilter(filter);
				query.declareParameters(declear);
				log.log(Level.SEVERE, filter + " | " + declear);
				listNotifications = (List<Notification>) query.execute(
						username, lastupdate);
			} else {
				log.log(Level.SEVERE, filter + " | " + declear);
				query.setFilter(filter);
				query.declareParameters(declear);
				listNotifications = (List<Notification>) query
						.execute(username);
			}

			if (listNotifications.size() > 0) {
				result.setOK(true);
				result
						.setMessage(String
								.format(
										Global.messages
												.getString("get_notifications_by_username_successfully"),
										username));
				result.setResult(listNotifications);
			} else {
				result
						.setMessage(Global.messages
								.getString("no_notifications"));
			}
		} else {
			result.setMessage(Global.messages.getString("not_found") + " "
					+ username);
		}

		pm.close();
		return result;
	}

	public ServiceResult<Void> editNotification(Notification nof) {
		ServiceResult<Void> result = new ServiceResult<Void>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (dbAccount.isExist(nof.getUsername()).isOK()) {
			Notification n = null;
			boolean isNotFound = false;
			try {
				n = pm.getObjectById(Notification.class, nof.getId());
			} catch (Exception e) {
				isNotFound = true;
			}

			if (n == null || isNotFound) {
				result.setMessage(Global.messages
						.getString("no_found_notification"));
			} else {
				n.setContent(nof.getContent());
				n.setTimestamp(nof.getTimestamp());
				n.setNew(nof.isNew());
				n.setUsername(nof.getUsername());

				pm.refresh(n);
				result.setMessage(Global.messages
						.getString("edit_notification_successfully"));
			}
		} else {
			result.setMessage(Global.messages.getString("not_found") + " "
					+ nof.getUsername());
		}

		pm.close();
		return result;
	}

	public ServiceResult<Void> markAsReadAll(String username) {
		ServiceResult<Void> result = new ServiceResult<Void>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (dbAccount.isExist(username).isOK()) {
			Query query = pm.newQuery(Notification.class);
			query.setFilter("username == us && isNew == true");
			query.declareParameters("String us");
			List<Notification> list = (List<Notification>) query
					.execute(username);
			for (Notification n : list) {
				n.setNew(false);
			}

			result.setOK(true);
			result.setMessage(Global.messages
					.getString("mark_as_read_successfully"));
		} else {
			result.setMessage(Global.messages.getString("not_found") + " "
					+ username);
		}

		pm.close();
		return result;
	}

	public ServiceResult<Void> insertWhenUserComment(Comment comment) {
		ServiceResult<Void> result = new ServiceResult<Void>();
		Notification noti = new Notification();
		noti.setTimestamp(comment.getDatePost().getTime());
		noti.setNew(true);
		if (comment.getType().equals("product")) {
			noti.setType(Notification.ADD_COMMENT_PRODUCT);
			noti.setContent(String.format(Global.messages
					.getString("notification_comment_content"), comment
					.getUsername(), Global.messages.getString("product")));

			ServiceResult<Product> productResult = dbProduct
					.findProduct(comment.getType_id());
			if (productResult.isOK()) {
				noti.setUsername(productResult.getResult().getUsername());
			} else {
				result.setOK(false);
				result.setMessage(productResult.getMessage());
				return result;
			}
		} else {
			noti.setType(Notification.ADD_COMMENT_PAGE);
			noti.setContent(String.format(Global.messages
					.getString("notification_comment_content"), comment
					.getUsername(), Global.messages.getString("page")));

			ServiceResult<Page> searchResult = dbPage.findPage(comment
					.getType_id());
			if (searchResult.isOK()) {
				noti.setUsername(searchResult.getResult().getUsername());
			} else {
				result.setOK(false);
				result.setMessage(searchResult.getMessage());
				return result;
			}
		}

		ServiceResult<Long> insertResult = insertNotification(noti);
		result.setOK(insertResult.isOK());
		result.setMessage(insertResult.getMessage());

		return result;
	}

	public ServiceResult<Void> insertWhenTagUserToProduct(long productId,
			String username, boolean isTag) {
		ServiceResult<Void> result = new ServiceResult<Void>();
		ServiceResult<Product> productResult = dbProduct.findProduct(productId);
		if (productResult.isOK() == false) {
			result.setOK(false);
			result.setMessage(productResult.getMessage());
			return result;
		}

		Notification noti = new Notification();
		noti.setUsername(username);
		if (isTag) {
			noti.setContent(String.format(Global.messages
					.getString("notification_tag_user_to_product_content"),
					productResult.getResult().getUsername(), productId));
			noti.setType(Notification.TAG_PRODUCT);
		} else {
			noti.setContent(String.format(Global.messages
					.getString("notification_untag_user_from_product_content"),
					productResult.getResult().getUsername(), productId));
			noti.setType(Notification.UNTAG_PRODUCT);
		}
		noti.setTimestamp(System.currentTimeMillis());
		noti.setNew(true);

		ServiceResult<Long> insertResult = insertNotification(noti);
		result.setOK(insertResult.isOK());
		result.setMessage(insertResult.getMessage());
		return result;
	}

	public ServiceResult<Void> insertWhenUserTagProductToPage(long pageID,
			long productID, boolean isTag) {
		ServiceResult<Void> result = new ServiceResult<Void>();
		ServiceResult<Product> productResult = dbProduct.findProduct(productID);
		ServiceResult<Page> pageResult = dbPage.findPage(pageID);
		if (productResult.isOK() == false) {
			result.setOK(false);
			result.setMessage(productResult.getMessage());
			return result;
		}
		if (pageResult.isOK() == false) {
			result.setOK(false);
			result.setMessage(pageResult.getMessage());
			return result;
		}
		Notification noti = new Notification();
		noti.setUsername(pageResult.getResult().getUsername());
		if (isTag) {
			noti.setContent(String.format(Global.messages
					.getString("notification_tag_page_content"), productResult
					.getResult().getUsername(), productID, pageID));
		} else {
			noti
					.setContent(String.format(Global.messages
							.getString("notification_untag_page_content"),
							productResult.getResult().getUsername(), productID,
							pageID));
		}
		noti.setTimestamp(System.currentTimeMillis());
		noti.setNew(true);
		noti.setType(Notification.TAG_PRODUCT_TO_PAGE);
		ServiceResult<Long> insertResult = insertNotification(noti);
		result.setOK(insertResult.isOK());
		result.setMessage(insertResult.getMessage());

		for (long id : pageResult.getResult().getSetProduct()) {
			if (id == productID) {
				continue;
			}
			ServiceResult<Product> findResult = dbProduct.findProduct(id);
			if (findResult.isOK() == false) {
				result.setMessage(result.getMessage()
						+ ";Exception khi product id " + id
						+ "da duoc tag trc do:" + findResult.getMessage());
				result.setOK(false);
			} else {
				noti.setId(null);
				noti.setUsername(findResult.getResult().getUsername());
				insertNotification(noti);
			}
		}
		return result;
	}

	public List<ServiceResult<Long>> insertWhenUserAddFriend(String userName,
			List<String> addedUnames) {
		List<ServiceResult<Long>> result = new ArrayList<ServiceResult<Long>>();
		for (String addedUname : addedUnames) {
			ServiceResult<UserInfo> resultAddedUserInfo = dbAccount
					.getUserInfo(addedUname);
			if (!resultAddedUserInfo.isOK())
				continue;
			String addedUserkey = resultAddedUserInfo.getResult().getUserkey();

			Notification noti = new Notification();
			noti.setTimestamp(System.currentTimeMillis());
			noti.setNew(true);
			noti.setContent(String.format(Global.messages
					.getString("notification_add_friend_conttent"), userName));
			noti.setUsername(addedUname);
			noti.setType(Notification.ADD_FRIEND);

			// if (UtilsFunction.isOnline(addedUname)) {
			// // Send Xtify message ^^
			// noti.setNew(false);
			// NotificationUtils.sendNotification(addedUserkey,
			// NotificationUtils.ADD_FRIEND, Global.messages
			// .getString("add_friend"),
			// String.format(Global.messages
			// .getString("notification_add_friend_conttent"),
			// userName));
			// log.log(Level.SEVERE, "Send xtify " + addedUname);
			// }
			result.add(insertNotification(noti));
		}
		return result;
	}

	public ServiceResult<Void> insertWhenTagUserToPage(long pageId,
			String username, boolean isTag) {
		ServiceResult<Void> result = new ServiceResult<Void>();
		ServiceResult<Page> pageResult = dbPage.findPage(pageId);
		if (pageResult.isOK() == false) {
			result.setOK(false);
			result.setMessage(pageResult.getMessage());
			return result;
		}

		Notification noti = new Notification();
		noti.setUsername(username);
		if (isTag) {
			noti.setContent(String.format(Global.messages
					.getString("notification_tag_user_to_page_content"),
					pageResult.getResult().getUsername(), pageId));
			noti.setType(Notification.TAG_PAGE);
		} else {
			noti.setContent(String.format(Global.messages
					.getString("notification_untag_user_from_page_content"),
					pageResult.getResult().getUsername(), pageId));
			noti.setType(Notification.UNTAG_PAGE);
		}
		noti.setTimestamp(System.currentTimeMillis());
		noti.setNew(true);
		ServiceResult<Long> insertResult = insertNotification(noti);
		result.setOK(insertResult.isOK());
		result.setMessage(insertResult.getMessage());

		insertNotification(noti);
		return result;
	}

	public ServiceResult<Void> deleteAllNoticationsBy(String username) {
		ServiceResult<Void> result = new ServiceResult<Void>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (dbAccount.isExist(username).isOK()) {
			Query query = pm.newQuery(Notification.class);
			query.setFilter("username == us");
			query.declareParameters("String us");
			List<Notification> list = (List<Notification>) query
					.execute(username);
			if (list == null) {
				result
						.setMessage(String
								.format(
										Global.messages
												.getString("delete_all_notifications_by_username_fail"),
										username));
				result.setOK(false);
			} else {
				pm.deletePersistentAll(list);
				result
						.setMessage(String
								.format(
										Global.messages
												.getString("delete_all_notifications_by_username_successfully"),
										username));
				result.setOK(true);
			}
		} else {
			result.setMessage(Global.messages.getString("not_found") + " "
					+ username);
		}

		pm.close();
		return result;
	}

	public ServiceResult<Void> deletAllNotifications() {
		ServiceResult<Void> result = new ServiceResult<Void>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query query = pm.newQuery(Notification.class);
		List<Notification> list = (List<Notification>) query.execute();
		pm.deletePersistentAll(list);
		pm.close();

		result.setOK(true);
		result.setMessage(Global.messages
				.getString("delete_all_notifications_successfully"));
		return result;
	}

	public static NotificationServiceImpl getInstance() {
		if (instance == null) {
			instance = new NotificationServiceImpl();
		}
		return instance;
	}
}
