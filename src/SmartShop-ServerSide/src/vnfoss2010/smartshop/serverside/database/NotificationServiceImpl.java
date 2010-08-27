package vnfoss2010.smartshop.serverside.database;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.entity.Notification;

public class NotificationServiceImpl {
	private static NotificationServiceImpl instance;
	public static final String TYPE_PRODUCT = "product";
	public static final String TYPE_PAGE = "page";
	private AccountServiceImpl dbAccount = AccountServiceImpl.getInstance();

	private static Logger log = Logger.getLogger(NotificationServiceImpl.class
			.getName());

	public void Nofication() {
		instance = this;
	}

	public ServiceResult<Long> insertNotification(Notification n) {
		ServiceResult<Long> result = new ServiceResult<Long>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (n == null) {
			result.setMessage(Global.messages
					.getString("cannot_handle_with_null"));
		}

		if (dbAccount.isExist(n.getUsername()).isOK()) {
			n = pm.makePersistent(n);

			if (n == null) {
				result.setMessage(Global.messages
						.getString("insert_nofitification_successfully"));
			} else {
				result.setResult(n.getId());
				result.setMessage(Global.messages
						.getString("insert_nofitification_successfully"));
				result.setOK(true);
			}
		} else {
			result.setMessage(Global.messages.getString("not_found") + " "
					+ n.getUsername());
		}

		pm.close();
		return result;
	}

	public ServiceResult<List<Notification>> getListNotificationsByUsername(
			String username, int limit, int type) {
		ServiceResult<List<Notification>> result = new ServiceResult<List<Notification>>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (dbAccount.isExist(username).isOK()) {
			Query query = pm.newQuery(Notification.class);
			query.setFilter("username == us");
			query.declareParameters("String us");
			if (limit > 0)
				query.setRange(0, limit);
			if (type == 1) {
				query.setFilter("isNew = true");
			} else if (type == 2) {
				query.setFilter("isNew = false");
			}
			List<Notification> listNotifications = (List<Notification>) query
					.execute(username);

			if (listNotifications.size() > 0) {
				result.setOK(true);
				result
						.setMessage(String.format(Global.messages
								.getString("get_notifications_by_username_successfully"), username));
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
				n.setDate(nof.getDate());
				n.setNew(nof.isNew());
				n.setUsername(nof.getUsername());
				
				pm.refresh(n);
				result.setMessage(Global.messages.getString("edit_notification_successfully"));
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
			query.setFilter("username = us");
			query.declareParameters("String us");
			query.setFilter("isNew = true");
			List<Notification> list = (List<Notification>) query.execute(username);
			for (Notification n : list){
				n.setNew(false);
			}
			pm.refreshAll(list);
			
			result.setOK(true);
			result.setMessage(Global.messages.getString("mark_as_read_successfully"));
		} else {
			result.setMessage(Global.messages.getString("not_found") + " "
					+ username);
		}

		pm.close();
		return result;
	}

	public ServiceResult<Void> deleteAllNoticationsBy(String username) {
		ServiceResult<Void> result = new ServiceResult<Void>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (dbAccount.isExist(username).isOK()) {
			Query query = pm.newQuery(Notification.class);
			query.setFilter("username = us");
			query.declareParameters("String us");
			List<Notification> list = (List<Notification>) query.execute(username);
			if (list == null){
				result.setMessage(String.format(Global.messages.getString("delete_all_notifications_by_username_fail"), username));
				result.setOK(false);
			}else{
				pm.deletePersistentAll(list);
				result.setMessage(String.format(Global.messages.getString("delete_all_notifications_by_username_successfully"), username));
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
		result.setMessage(Global.messages.getString("delete_all_notifications_successfully"));
		return result;
	}

	public static NotificationServiceImpl getInstance() {
		if (instance == null) {
			instance = new NotificationServiceImpl();
		}
		return instance;
	}
}
