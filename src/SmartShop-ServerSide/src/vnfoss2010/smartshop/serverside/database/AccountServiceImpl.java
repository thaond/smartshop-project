package vnfoss2010.smartshop.serverside.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.authentication.SessionObject;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Comment;
import vnfoss2010.smartshop.serverside.database.entity.Media;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;
import vnfoss2010.smartshop.serverside.utils.StringUtils;
import vnfoss2010.smartshop.serverside.utils.UtilsFunction;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * The server side implementation of the RPC service.<br>
 */
public class AccountServiceImpl {
	private static final long serialVersionUID = 1L;
	private static AccountServiceImpl instance;
	private static NotificationServiceImpl dbNoti;

	public static UserService userService = UserServiceFactory.getUserService();
	private final static Logger log = Logger.getLogger(AccountServiceImpl.class
			.getName());

	/**
	 * Default constructor<br>
	 * Modifier should be public because if modifier is private, an exception
	 * will be thrown<br>
	 * <code>Class {@link org.mortbay.jetty.servlet.Holder} can not access a member of class {@link AccountServiceImpl} with modifiers "private"</code>
	 */
	public AccountServiceImpl() {
		instance = this;
		dbNoti = NotificationServiceImpl.getInstance();
	}

	/**
	 * You should use this method to create an instance of this class.<br/>
	 * <b>Note:</b> <code>Singleton</code> is used here
	 * 
	 * @return {@link AccountServiceImpl} instance
	 */
	public static AccountServiceImpl getInstance() {
		if (instance == null)
			instance = new AccountServiceImpl();
		return instance;
	}

	// USERINFO
	public ServiceResult<Void> insertUserInfo(UserInfo userInfo) {
		preventSQLInjUserInfo(userInfo);
		userInfo.updateFTS();
		// AccountServiceImpl.updateFTSStuffForUserInfo(userInfo);
		ServiceResult<Void> result = new ServiceResult<Void>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (userInfo == null || userInfo.getUsername() == null) {
			result.setMessage(Global.messages
					.getString("cannot_handle_with_null"));
			return result;
		}

		// if (userInfo.getPassword() == null
		// || userInfo.getPassword().length() < 6) {
		// result.setMessage(Global.messages
		// .getString("password_length_at_least_6_characters"));
		// return result;
		// }

		try {
			UserInfo tmp = null;
			boolean isNotFound = false;
			try {
				tmp = pm.getObjectById(UserInfo.class, userInfo.getUsername());
			} catch (NucleusObjectNotFoundException e) {
				isNotFound = true;
			} catch (JDOObjectNotFoundException e) {
				isNotFound = true;
			}

			if (isNotFound || tmp == null) {
				userInfo.setPassword(DatabaseUtils.md5(userInfo.getPassword()));
				pm.makePersistent(userInfo);
				result.setOK(true);
				result.setMessage(Global.messages
						.getString("register_successfully"));
			} else {
				result.setOK(false);
				result.setMessage(Global.messages
						.getString("username_already_exist"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setMessage(Global.messages.getString("register_fail"));
		} finally {
			try {
				pm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				result.setMessage(Global.messages.getString("register_fail"));
			}
		}
		return result;
	}

	public ServiceResult<UserInfo> getUserInfo(String username) {
		username = DatabaseUtils.preventSQLInjection(username);

		ServiceResult<UserInfo> result = new ServiceResult<UserInfo>();

		if (username == null || username.equals("")) {
			result.setMessage(Global.messages
					.getString("cannot_handle_with_null"));
			return result;
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
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
				result.setMessage(Global.messages
						.getString("get_userinfo_successfully"));
				result.setResult(userInfo);
				result.setOK(true);
			}
		} catch (Exception ex) {
			result.setMessage(Global.messages.getString("get_userinfo_fail"));
			result.setOK(false);
			// log.log(Level.SEVERE, s, ex);
			ex.printStackTrace();
		} finally {
			try {
				pm.close();
			} catch (Exception ex) {
				result.setOK(false);
				result.setMessage(Global.messages
						.getString("get_userinfo_fail"));
				log.log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
		return result;
	}

	public ServiceResult<Void> editProfile(UserInfo userInfo) {
		// Prevent SQL Injection
		// preventSQLInjUserInfo(userInfo);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ServiceResult<Void> result = new ServiceResult<Void>();

		if (userInfo == null || userInfo.getUsername() == null) {
			result.setMessage(Global.messages
					.getString("cannot_handle_with_null"));
			return result;
		}

		try {
			boolean isNotFound = false;
			UserInfo tmp = null;
			try {
				tmp = pm.getObjectById(UserInfo.class, userInfo.getUsername());
			} catch (NucleusObjectNotFoundException e) {
				isNotFound = true;
			} catch (JDOObjectNotFoundException e) {
				isNotFound = true;
			}

			if (isNotFound || tmp == null) {
				// UserInfo is not existed
				result.setMessage(userInfo.getUsername() + " "
						+ Global.messages.getString("doesnot_exist"));
			} else {
				if (!StringUtils.isEmptyOrNull(tmp.getOld_password())
						&& !tmp.getPassword().equals(
								DatabaseUtils.md5(userInfo.getOld_password()))) {
					// Intent to change password
					result.setMessage(Global.messages
							.getString("password_doesnot_match"));
				}
				// else if (StringUtils.isEmptyOrNull(tmp.getPassword())) {
				// result
				// .setMessage(Global.messages
				// .getString("password_length_at_least_6_characters"));
				// return result;
				// }
				else {
					if (userInfo.getPassword().length() > 0) {
						tmp.setPassword(DatabaseUtils.md5(userInfo
								.getPassword()));
					}

					tmp.setFirst_name(userInfo.getFirst_name());
					tmp.setLast_name(userInfo.getLast_name());
					tmp.setPhone(userInfo.getPhone());
					tmp.setEmail(userInfo.getEmail());
					tmp.setAddress(userInfo.getAddress());
					tmp.setLang(userInfo.getLang());
					tmp.setCountry(userInfo.getCountry());
					tmp.setBirthday(userInfo.getBirthday());
					tmp.setLat(userInfo.getLat());
					tmp.setLng(userInfo.getLng());
					tmp.setGmt(userInfo.getGmt());

					pm.refresh(tmp);
					result.setOK(true);
					result.setMessage(Global.messages
							.getString("edit_profile_successfully"));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setMessage(Global.messages.getString("edit_profile_fail"));
		} finally {
			try {
				pm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				result.setMessage(Global.messages
						.getString("edit_profile_fail"));
			}
		}
		return result;
	}

	/**
	 * Insert an ArrayList of UserInfos into datastore
	 */
	public ServiceResult<Void> insertAllUserInfos(
			ArrayList<UserInfo> listUserInfos) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ServiceResult<Void> result = new ServiceResult<Void>();

		try {
			for (UserInfo userInfo : listUserInfos) {
				if (userInfo == null || userInfo.getUsername() == null) {
					continue;
				}
				// AccountServiceImpl.updateFTSStuffForUserInfo(userInfo);
				userInfo.updateFTS();
				userInfo.setPassword(DatabaseUtils.md5(userInfo.getPassword()));
				pm.makePersistent(userInfo);
			} // end for loop

			result.setOK(true);
			result.setMessage(Global.messages
					.getString("insert_list_userinfos_successfully"));
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setMessage(Global.messages
					.getString("insert_list_userinfos_fail"));
		} finally {
			try {
				pm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				result.setMessage(Global.messages
						.getString("insert_list_userinfos_fail"));
			}
		}

		return result;
	}

	public ServiceResult<Void> deleteAllUserInfos() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ServiceResult<Void> result = new ServiceResult<Void>();

		try {
			// Delete by query
			Query query = pm.newQuery(UserInfo.class);
			query.deletePersistentAll();

			result.setMessage(Global.messages
					.getString("delete_all_userinfos_successfully"));
			result.setOK(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setMessage(Global.messages
					.getString("delete_all_userinfos_fail"));
			log.log(Level.SEVERE, ex.getMessage(), ex);
		} finally {
			try {
				pm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				result.setOK(false);
				result.setMessage(Global.messages
						.getString("delete_all_userinfos_fail"));
			}
		}
		return result;
	}

	public ServiceResult<List<UserInfo>> getAllUserInfos() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String queryStr = "SELECT FROM " + UserInfo.class.getName();
		Query query = pm.newQuery(queryStr);
		List<UserInfo> listUserInfos = (List<UserInfo>) query.execute();
		ServiceResult<List<UserInfo>> result = new ServiceResult<List<UserInfo>>();
		result.setOK(true);
		result.setResult(listUserInfos);

		return result;
	}

	/**
	 * 
	 * @param username
	 * @param password
	 *            in MD5 form
	 * @return
	 */
	public ServiceResult<UserInfo> login(String username, String password,
			String userkey) {
		username = DatabaseUtils.preventSQLInjection(username);
		password = DatabaseUtils.preventSQLInjection(password);// DatabaseUtils.md5

		ServiceResult<UserInfo> result = new ServiceResult<UserInfo>();

		if (username == null || username.equals("")) {
			result.setMessage(Global.messages
					.getString("cannot_handle_with_null"));
			return result;
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
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
				if (userInfo.getPassword().equals(password)) {
					// Store session here
					String sessionId = UtilsFunction.getAlphaNumeric(32);
					if (Global.mapSession.containsKey(username)) {
						((SessionObject) Global.mapSession.get(username)).set(
								username, sessionId, new Date().getTime());
					} else {
						Global.mapSession.put(username, new SessionObject(
								username, sessionId, new Date().getTime()));
					}
					userInfo.setSessionId(sessionId);
					userInfo.setLogin(true);
					userInfo.setUserkey(userkey);

					result.setMessage(Global.messages
							.getString("login_successfully"));
					result.setResult(userInfo);
					result.setOK(true);
				} else {
					result.setMessage(Global.messages
							.getString("wrong_password"));
				}
			}
		} catch (Exception ex) {
			result.setMessage(Global.messages.getString("login_fail"));
			result.setOK(false);
			// log.log(Level.SEVERE, s, ex);
			ex.printStackTrace();
		} finally {
			try {
				pm.close();
			} catch (Exception ex) {
				result.setOK(false);
				result.setMessage(Global.messages.getString("login_fail"));
				log.log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
		return result;
	}

	public ServiceResult<Void> logout(String username) {
		username = DatabaseUtils.preventSQLInjection(username);

		ServiceResult<Void> result = new ServiceResult<Void>();

		if (username == null || username.equals("")) {
			result.setMessage(Global.messages
					.getString("cannot_handle_with_null"));
			return result;
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			boolean isNotFound = false;
			UserInfo userInfo = null;
			try {
				userInfo = pm.getObjectById(UserInfo.class, username);
			} catch (JDOObjectNotFoundException e) {
				isNotFound = true;
			} catch (NucleusObjectNotFoundException n) {
				isNotFound = true;
			}

			if (isNotFound || userInfo == null) {
				result.setMessage(Global.messages.getString("not_found")
						+ username);
			} else {
				userInfo.setLogin(false);
				SessionObject so = Global.mapSession.get(username);
				if (so!=null)
					userInfo.setLastLogin(so.timeStamp);
				
				Global.mapSession.remove(username);
				result.setMessage(Global.messages
						.getString("logout_successfully"));
				result.setOK(true);
			}
		} catch (Exception ex) {
			result.setMessage(Global.messages.getString("logout_fail"));
			result.setOK(false);
		} finally {
			try {
				pm.close();
			} catch (Exception ex) {
				result.setOK(false);
				result.setMessage(Global.messages.getString("logout_fail"));
				log.log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
		return result;
	}

	public ServiceResult<Void> isExist(String username) {
		username = DatabaseUtils.preventSQLInjection(username);
		ServiceResult<Void> result = new ServiceResult<Void>();

		if (username == null || username.equals("")) {
			result.setMessage(Global.messages
					.getString("cannot_handle_with_null"));
			return result;
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			boolean isNotFound = false;
			UserInfo userInfo = null;
			try {
				userInfo = pm.getObjectById(UserInfo.class, username);
			} catch (NucleusObjectNotFoundException e) {
				isNotFound = true;
			} catch (JDOObjectNotFoundException e) {
				isNotFound = true;
			}
			if (isNotFound || userInfo == null) {
				result.setOK(false);
				result.setMessage(Global.messages
						.getString("username_not_exist"));
			} else {
				// Exist this username in the datastore
				result.setOK(true);
				result.setMessage(Global.messages
						.getString("username_already_exist"));
			}
		} catch (Exception ex) {
			result.setOK(false);
			result.setMessage(Global.messages.getString("have_problem"));
			ex.printStackTrace();
			// log.log(Level.SEVERE, s, ex);
		} finally {
			try {
				pm.close();
			} catch (Exception ex) {
				result.setMessage(Global.messages.getString("have_problem"));
				ex.printStackTrace();
			}
		}
		return result;
	}

	public ServiceResult<List<UserInfo>> searchUsernamesLike(String queryString) {
		queryString = DatabaseUtils.preventSQLInjection(queryString);
		List<UserInfo> listUserInfos = null;
		ServiceResult<List<UserInfo>> result = new ServiceResult<List<UserInfo>>();

		listUserInfos = DatabaseUtils
				.searchByQuery(queryString, UserInfo.class);

		if (listUserInfos.size() > 0) {
			result.setResult(new ArrayList<UserInfo>());
			for (UserInfo userInfo : listUserInfos) {
				// Just return basic information
				result.getResult().add(
						new UserInfo(userInfo.getUsername(), userInfo
								.getFirst_name(), userInfo.getLast_name()));
			}
			result.setMessage(Global.messages
					.getString("search_username_successfully"));
			result.setOK(true);
		} else {
			result.setOK(false);
			result
					.setMessage(Global.messages
							.getString("search_username_fail"));
		}
		return result;
	}

	public ServiceResult<Void> addFriends(String username, String... friends) {
		username = DatabaseUtils.preventSQLInjection(username);
		ServiceResult<Void> result = new ServiceResult<Void>();
		ArrayList<String> addedSuccessUName = new ArrayList<String>();
		UserInfo userInfo = null;

		if (username == null || username.equals("")) {
			result.setMessage(Global.messages
					.getString("cannot_handle_with_null"));
			return result;
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			boolean isNotFound = false;
			try {
				userInfo = pm.getObjectById(UserInfo.class, username);
			} catch (NucleusObjectNotFoundException e) {
				isNotFound = true;
			} catch (JDOObjectNotFoundException e) {
				isNotFound = true;
			}
			if (isNotFound || userInfo == null) {
				result.setMessage(Global.messages.getString("not_found") + " "
						+ username);
				result.setOK(false);
			} else {
				// Exist this username in the datastore
				for (int i = 0; i < friends.length; i++) {
					isNotFound = false;

					String friend = DatabaseUtils
							.preventSQLInjection(friends[i]);
					UserInfo userInfo2 = null;
					try {
						userInfo2 = pm.getObjectById(UserInfo.class, friend);
					} catch (NucleusObjectNotFoundException e) {
						isNotFound = true;
					} catch (JDOObjectNotFoundException e) {
						isNotFound = true;
					}

					if (isNotFound || userInfo == null) {
						continue;
					}

					userInfo.getSetFriendsUsername().add(friend);
					addedSuccessUName.add(friend);
				}

				result.setOK(true);
				result.setMessage(Global.messages
						.getString("add_list_friends_successfully"));
			}
		} catch (Exception ex) {
			// ex.printStackTrace();
			result.setMessage(Global.messages
					.getString("add_list_friends_fail"));
			Global.log(log, Arrays.toString(ex.getStackTrace()));
		} finally {
			try {
				pm.close();
			} catch (Exception ex) {
				Global.log(log, Arrays.toString(ex.getStackTrace()));
				result.setMessage(Global.messages
						.getString("add_list_friends_fail"));
			}
		}
		if (result.isOK()) {
			List<ServiceResult<Long>> notiResults = dbNoti
					.insertWhenUserAddFriend(userInfo.getUsername(),
							addedSuccessUName);
			for (ServiceResult<Long> notiResult : notiResults) {
				if (notiResult.isOK() == false) {
					result.setMessage(result.getMessage()
							+ ";Notification Exception:"
							+ notiResult.getMessage());
				}
			}
		}
		return result;
	}

	// PAGES
	public ServiceResult<Long> insertPage(Page page, List<String> listCategories) {
		preventSQLInjPage(page);
		ServiceResult<Long> result = new ServiceResult<Long>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (page == null) {
			result.setMessage(Global.messages
					.getString("cannot_handle_with_null"));
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
					page.getSetCategoryKeys().add(cat);
				}
			}

			page = pm.makePersistent(page);
			if (page == null) {
				result
						.setMessage(Global.messages
								.getString("insert_page_fail"));
			} else {
				result.setResult(page.getId());
				result.setMessage(Global.messages
						.getString("insert_page_successfully"));
				result.setOK(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage(Global.messages.getString("insert_page_fail"));
		}

		return result;
	}

	// COMMENTS
	/**
	 * Insert new Comment into database
	 * 
	 * @return id in the datastore
	 */
	public ServiceResult<Long> insertComments(Comment comment) {
		preventSQLInjComment(comment);
		ServiceResult<Long> result = new ServiceResult<Long>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (comment == null) {
			result.setMessage(Global.messages
					.getString("cannot_handle_with_null"));
		}

		try {
			comment = pm.makePersistent(comment);
			if (comment == null) {
				result.setMessage(Global.messages
						.getString("insert_comment_fail"));
			} else {
				result.setResult(comment.getId());
				result.setMessage(Global.messages
						.getString("insert_comment_successfully"));
				result.setOK(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage(Global.messages.getString("insert_comment_fail"));
		}

		return result;
	}

	public void setLogout(String username) {
		UserInfo userInfo = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		boolean isNotFound = false;
		try {
			userInfo = pm.getObjectById(UserInfo.class, username);
		} catch (JDOObjectNotFoundException e) {
			isNotFound = true;
		} catch (NucleusObjectNotFoundException n) {
			isNotFound = true;
		}

		if (isNotFound || userInfo == null) {
		} else {
			userInfo.setLogin(false);
		}

		pm.close();
	}

	private void preventSQLInjMedia(Media media) {
		media.setName(DatabaseUtils.preventSQLInjection(media.getName()));
		media.setDescription(DatabaseUtils.preventSQLInjection(media
				.getDescription()));
	}

	// STUFF
	public String getGoogleAccountLoginLink() {
		return userService.createLoginURL(Global.HOST_NAME);
	}

	public String getGoogleAccountLogoutLink() {
		return userService.createLogoutURL(Global.HOST_NAME);
	}

	private void preventSQLInjUserInfo(UserInfo userInfo) {
		userInfo.setUsername(DatabaseUtils.preventSQLInjection(userInfo
				.getUsername()));
		userInfo.setPassword(DatabaseUtils.preventSQLInjection(userInfo
				.getPassword()));
		userInfo.setFirst_name(DatabaseUtils.preventSQLInjection(userInfo
				.getFirst_name()));
		userInfo.setLast_name(DatabaseUtils.preventSQLInjection(userInfo
				.getLast_name()));
		userInfo.setPhone(DatabaseUtils
				.preventSQLInjection(userInfo.getPhone()));
		userInfo.setEmail(DatabaseUtils
				.preventSQLInjection(userInfo.getEmail()));
		userInfo.setAddress(DatabaseUtils.preventSQLInjection(userInfo
				.getAddress()));
		userInfo.setLang(DatabaseUtils.preventSQLInjection(userInfo.getLang()));
		userInfo.setCountry(DatabaseUtils.preventSQLInjection(userInfo
				.getCountry()));
	}

	public static void preventSQLInjPage(Page page) {
		page.setName(DatabaseUtils.preventSQLInjection(page.getName()));
		page.setContent(DatabaseUtils.preventSQLInjection(page.getContent()));
	}

	public static void preventSQLInjComment(Comment comment) {
		comment.setContent(DatabaseUtils.preventSQLInjection(comment
				.getContent()));
	}

	final int DURATION_IN_S = 60 * 60 * 24; // duration remembering login: 1 day
}
