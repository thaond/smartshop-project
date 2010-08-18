package vnfoss2010.smartshop.serverside.database;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServlet;

import org.datanucleus.exceptions.NucleusObjectNotFoundException;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.entity.Attribute;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Comment;
import vnfoss2010.smartshop.serverside.database.entity.Media;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;

import com.google.appengine.api.datastore.DatastoreNeedIndexException;
import com.google.appengine.api.datastore.DatastoreTimeoutException;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * The server side implementation of the RPC service.<br>
 */
public class DatabaseServiceImpl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static DatabaseServiceImpl instance;
	private ResourceBundle messages;

	public static UserService userService = UserServiceFactory.getUserService();
	private final static Logger log = Logger
			.getLogger(DatabaseServiceImpl.class.getName());

	/**
	 * Default constructor<br>
	 * Modifier should be public because if modifier is private, an exception
	 * will be thrown<br>
	 * <code>Class {@link org.mortbay.jetty.servlet.Holder} can not access a member of class {@link DatabaseServiceImpl} with modifiers "private"</code>
	 */
	public DatabaseServiceImpl() {
		instance = this;
		messages = ResourceBundle
				.getBundle("vnfoss2010/smartshop/serverside.localization/MessagesBundle");
		// getResponse().setContentLength(512);
	}

	/**
	 * You should use this method to create an instance of this class.<br/>
	 * <b>Note:</b> <code>Singleton</code> is used here
	 * 
	 * @return {@link DatabaseServiceImpl} instance
	 */
	public static DatabaseServiceImpl getInstance() {
		if (instance == null)
			instance = new DatabaseServiceImpl();
		return instance;
	}

	// USERINFO
	public ServiceResult<Void> insertUserInfo(UserInfo userInfo) {
		preventSQLInjUserInfo(userInfo);
		ServiceResult<Void> result = new ServiceResult<Void>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (userInfo == null || userInfo.getUsername() == null) {
			result.setMessage(messages.getString("cannot_handle_with_null"));
			return result;
		}

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
				userInfo.setPassword(md5(userInfo.getPassword()));
				pm.makePersistent(userInfo);
				result.setOK(true);
			} else {
				result.setOK(false);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setMessage(messages.getString("register_fail"));
		} finally {
			try {
				pm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				result.setMessage(messages.getString("register_fail"));
			}
		}
		return result;
	}

	public ServiceResult<Void> editProfile(UserInfo userInfo) {
		// Prevent SQL Injection
		preventSQLInjUserInfo(userInfo);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ServiceResult<Void> result = new ServiceResult<Void>();

		if (userInfo == null || userInfo.getUsername() == null) {
			result.setMessage(messages.getString("cannot_handle_with_null"));
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
						+ messages.getString("doesnot_exist"));
			} else {
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
				if (userInfo.getPassword() == null
						|| userInfo.getPassword().toString().length() == 0) {
					result.setOK(true);
				} else if (userInfo.getPassword().equals(
						userInfo.getOldPassword())) {
					userInfo.setPassword(md5(userInfo.getPassword()));
					result.setOK(true);
				} else {
					result.setMessage(messages
							.getString("password_doesnot_match"));
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setMessage(messages.getString("edit_profile_fail"));
		} finally {
			try {
				pm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				result.setMessage(messages.getString("edit_profile_fail"));
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

				userInfo.setPassword(md5(userInfo.getPassword()));
				pm.makePersistent(userInfo);
			} // end for loop

			result.setOK(true);
			result.setMessage(messages
					.getString("insert_list_userinfos_successfully"));
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setMessage(messages.getString("insert_list_userinfos_fail"));
		} finally {
			try {
				pm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				result.setMessage(messages
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

			result.setMessage(messages
					.getString("delete_all_userinfos_successfully"));
			result.setOK(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setMessage(messages.getString("delete_all_userinfos_fail"));
			log.log(Level.SEVERE, ex.getMessage(), ex);
		} finally {
			try {
				pm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				result.setOK(false);
				result.setMessage(messages
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

	public ServiceResult<UserInfo> login(String username, String password) {
		username = preventSQLInjection(username);
		password = md5(preventSQLInjection(password));

		ServiceResult<UserInfo> result = new ServiceResult<UserInfo>();

		if (username == null || username.equals("")) {
			result.setMessage(messages.getString("cannot_handle_with_null"));
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
				result.setMessage(messages.getString("not_found") + " "
						+ username);
			} else {
				if (userInfo.getPassword().equals(password)) {
					result.setMessage(messages.getString("login_successfully"));
					result.setResult(userInfo);
					result.setOK(true);
				} else {
					result.setMessage(messages.getString("wrong_password"));
				}
			}
		} catch (Exception ex) {
			result.setMessage(messages.getString("login_fail"));
			result.setOK(false);
			// log.log(Level.SEVERE, s, ex);
			ex.printStackTrace();
		} finally {
			try {
				pm.close();
			} catch (Exception ex) {
				result.setOK(false);
				result.setMessage(messages.getString("login_fail"));
				log.log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
		return result;
	}

	public ServiceResult<Void> logout(String username) {
		username = preventSQLInjection(username);

		ServiceResult<Void> result = new ServiceResult<Void>();

		if (username == null || username.equals("")) {
			result.setMessage(messages.getString("cannot_handle_with_null"));
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
				result.setMessage(messages.getString("not_found") + username);
			} else {
				result.setMessage(messages.getString("logout_successfully"));
				// TODO
				// userInfo.setOnline(false);
				// if (userInfo.getTypeCus() == 1) {
				// result.setResult(true);
				// }
				result.setOK(true);
			}
		} catch (Exception ex) {
			result.setMessage(messages.getString("logout_fail"));
			result.setOK(false);
			// log.log(Level.SEVERE, s, ex);
			// ex.printStackTrace();
		} finally {
			try {
				pm.close();
			} catch (Exception ex) {
				result.setOK(false);
				result.setMessage(messages.getString("logout_fail"));
				log.log(Level.SEVERE, ex.getMessage(), ex);
			}
		}
		return result;
	}

	public ServiceResult<Boolean> isExist(String username) {
		username = preventSQLInjection(username);
		ServiceResult<Boolean> result = new ServiceResult<Boolean>();

		if (username == null || username.equals("")) {
			result.setMessage(messages.getString("cannot_handle_with_null"));
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
				result.setResult(false);
				result.setMessage(messages.getString("username_not_exist"));
			} else {
				// Exist this username in the datastore
				result.setResult(true);
				result.setMessage(messages.getString("username_already_exist"));
			}
			result.setOK(true);
		} catch (Exception ex) {
			result.setMessage(messages.getString("have_problem"));
			ex.printStackTrace();
			// log.log(Level.SEVERE, s, ex);
		} finally {
			try {
				pm.close();
			} catch (Exception ex) {
				result.setMessage(messages.getString("have_problem"));
				ex.printStackTrace();
			}
		}
		return result;
	}

	public ServiceResult<List<UserInfo>> searchUsernamesLike(String queryString) {
		queryString = preventSQLInjection(queryString);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("SELECT FROM " + UserInfo.class.getName()
				+ " WHERE ");
		Set<String> queryTokens = SearchJanitorUtils
				.getTokensForIndexingOrQuery(queryString,
						MAXIMUM_NUMBER_OF_WORDS_TO_SEARCH);
		List<String> parametersForSearch = new ArrayList<String>(queryTokens);
		StringBuffer declareParametersBuffer = new StringBuffer();
		int parameterCounter = 0;

		while (parameterCounter < queryTokens.size()) {

			queryBuffer.append("fts == param" + parameterCounter);
			declareParametersBuffer.append("String param" + parameterCounter);

			if (parameterCounter + 1 < queryTokens.size()) {
				queryBuffer.append(" && ");
				declareParametersBuffer.append(", ");
			}
			parameterCounter++;
		}

		Query query = pm.newQuery(queryBuffer.toString());
		query.declareParameters(declareParametersBuffer.toString());

		List<UserInfo> listUserInfos = null;
		ServiceResult<List<UserInfo>> result = new ServiceResult<List<UserInfo>>();

		try {
			listUserInfos = (List<UserInfo>) query
					.executeWithArray(parametersForSearch.toArray());
			result.setResult(new ArrayList<UserInfo>());
			for (UserInfo userInfo : listUserInfos) {
				// Just return basic information
				result.getResult().add(
						new UserInfo(userInfo.getUsername(), userInfo
								.getFirst_name(), userInfo.getLast_name()));
			}
			result.setOK(true);
		} catch (DatastoreTimeoutException e) {
			log.severe(e.getMessage());
			log.severe("datastore timeout at: " + queryString);// +
			result.setOK(false);
			result.setMessage(messages.getString("have_problem"));
			// " - timestamp: "
			// +
			// discreteTimestamp);
		} catch (DatastoreNeedIndexException e) {
			log.severe(e.getMessage());
			log.severe("datastore need index exception at: " + queryString);// +
			result.setOK(false);
			result.setMessage(messages.getString("have_problem"));
			// " - timestamp: "
			// +
			// discreteTimestamp);
		}

		return result;
	}

	public ServiceResult<Void> addFriend(String username, String friend) {
		username = preventSQLInjection(username);
		friend = preventSQLInjection(friend);
		ServiceResult<Void> result = new ServiceResult<Void>();

		if (username == null || username.equals("") || friend == null
				|| friend.equals("")) {
			result.setMessage(messages.getString("cannot_handle_with_null"));
			return result;
		}

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			boolean isNotFound = false;
			UserInfo userInfo1 = null;
			try {
				userInfo1 = pm.getObjectById(UserInfo.class, username);
			} catch (NucleusObjectNotFoundException e) {
				isNotFound = true;
			} catch (JDOObjectNotFoundException e) {
				isNotFound = true;
			}
			if (isNotFound || userInfo1 == null) {
				result.setMessage(messages.getString("not_found") + " "
						+ username);
				result.setOK(false);
			} else {
				// Exist this username in the datastore
				isNotFound = false;
				UserInfo userInfo2 = null;
				try {
					userInfo2 = pm.getObjectById(UserInfo.class, username);
				} catch (NucleusObjectNotFoundException e) {
					isNotFound = true;
				} catch (JDOObjectNotFoundException e) {
					isNotFound = true;
				}
				if (isNotFound || userInfo2 == null) {
					result.setMessage(messages.getString("not_found") + " "
							+ friend);
					result.setOK(false);
				} else {
					userInfo1.getSetFriendsUsername().add(friend);
					userInfo2.getSetFriendsUsername().add(username);
				}
				result.setOK(true);
			}
		} catch (Exception ex) {
			result.setMessage(messages.getString("add_friend_fail"));
			ex.printStackTrace();
			// log.log(Level.SEVERE, s, ex);
		} finally {
			try {
				pm.close();
			} catch (Exception ex) {
				result.setMessage(messages.getString("add_friend_fail"));
				ex.printStackTrace();
			}
		}
		return result;
	}

	public ServiceResult<Void> addFriends(String username, List<String> friends) {
		username = preventSQLInjection(username);
		ServiceResult<Void> result = new ServiceResult<Void>();

		if (username == null || username.equals("")) {
			result.setMessage(messages.getString("cannot_handle_with_null"));
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
				result.setMessage(messages.getString("not_found") + " "
						+ username);
				result.setOK(false);
			} else {
				// Exist this username in the datastore
				for (int i = 0; i < friends.size(); i++) {
					isNotFound = false;

					UserInfo userInfo2 = null;
					try {
						userInfo2 = pm.getObjectById(UserInfo.class, username);
					} catch (NucleusObjectNotFoundException e) {
						isNotFound = true;
					} catch (JDOObjectNotFoundException e) {
						isNotFound = true;
					}

					if (isNotFound || userInfo == null) {
						continue;
					}

					userInfo.getSetFriendsUsername().add(
							preventSQLInjection(friends.get(i)));
				}

				result.setOK(true);
				result.setMessage(messages
						.getString("add_list_friends_successfully"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result.setMessage(messages.getString("add_list_friends_fail"));
			// log.log(Level.SEVERE, s, ex);
		} finally {
			try {
				pm.close();
			} catch (Exception ex) {
				ex.printStackTrace();
				result.setMessage(messages.getString("add_list_friends_fail"));
			}
		}
		return result;
	}

	public static List<UserInfo> searchUserInfo(String queryString,
			PersistenceManager pm) {

		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("SELECT FROM " + UserInfo.class.getName()
				+ " WHERE ");

		Set<String> queryTokens = SearchJanitorUtils
				.getTokensForIndexingOrQuery(queryString,
						MAXIMUM_NUMBER_OF_WORDS_TO_SEARCH);

		List<String> parametersForSearch = new ArrayList<String>(queryTokens);
		StringBuffer declareParametersBuffer = new StringBuffer();
		int parameterCounter = 0;

		while (parameterCounter < queryTokens.size()) {

			queryBuffer.append("fts == param" + parameterCounter);
			declareParametersBuffer.append("String param" + parameterCounter);

			if (parameterCounter + 1 < queryTokens.size()) {
				queryBuffer.append(" && ");
				declareParametersBuffer.append(", ");
			}
			parameterCounter++;
		}
		Query query = pm.newQuery(queryBuffer.toString());
		query.declareParameters(declareParametersBuffer.toString());
		List<UserInfo> result = null;

		try {
			result = (List<UserInfo>) query
					.executeWithArray(parametersForSearch.toArray());

		} catch (DatastoreTimeoutException e) {
			log.severe(e.getMessage());
			log.severe("datastore timeout at: " + queryString);// +
			// " - timestamp: "
			// +
			// discreteTimestamp);
		} catch (DatastoreNeedIndexException e) {
			log.severe(e.getMessage());
			log.severe("datastore need index exception at: " + queryString);// +
			// " - timestamp: "
			// +
			// discreteTimestamp);
		}
		return result;

	}

	// PRODUCT
	/**
	 * Insert new product into database
	 * 
	 * @return id in the datastore
	 */
	public ServiceResult<Long> insertProduct(Product product) {
		preventSQLInjProduct(product);
		ServiceResult<Long> result = new ServiceResult<Long>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (product == null) {
//			result.setMessage(messages.getString("cannot_handle_with_null"));
			result.setMessage("khong the null");
		}

		try {
			pm.flush();
			product = pm.makePersistent(product);
			if (product == null) {
//				result.setMessage(messages.getString("insert_product_fail"));
				result.setMessage(messages.getString("khong thanh cong"));
			} else {
				result.setResult(product.getId());
				result.setMessage(messages
						.getString("insert_product_successfully"));
				result.setOK(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
//			result.setMessage(messages.getString("insert_list_userinfos_fail"));
			result.setMessage(e.getMessage() + "  exception");
		}

		return result;
	}

	public ServiceResult<Long> insertProduct(Product product,
			List<String> listCategories, List<Attribute> listAttributes) {
		preventSQLInjProduct(product);
		ServiceResult<Long> result = new ServiceResult<Long>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (product == null) {
			result.setMessage(messages.getString("cannot_handle_with_null"));
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
					product.getSetCategoryKeys().add(cat);
				}
			}

			if (listAttributes != null) {
				for (Attribute att : listAttributes) {
					product.getSetAttributes().add(att);
				}
			}

			product = pm.makePersistent(product);
			if (product == null) {
				result.setMessage(messages.getString("insert_product_fail"));
			} else {
				result.setResult(product.getId());
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

	// PAGES
	public ServiceResult<Long> insertPage(Page page, List<String> listCategories) {
		preventSQLInjPage(page);
		ServiceResult<Long> result = new ServiceResult<Long>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (page == null) {
			result.setMessage(messages.getString("cannot_handle_with_null"));
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
				result.setMessage(messages.getString("insert_page_fail"));
			} else {
				result.setResult(page.getId());
				result.setMessage(messages
						.getString("insert_page_successfully"));
				result.setOK(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage(messages.getString("insert_page_fail"));
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
			result.setMessage(messages.getString("cannot_handle_with_null"));
		}

		try {
			comment = pm.makePersistent(comment);
			if (comment == null) {
				result.setMessage(messages.getString("insert_comment_fail"));
			} else {
				result.setResult(comment.getId());
				result.setMessage(messages
						.getString("insert_comment_successfully"));
				result.setOK(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage(messages.getString("insert_comment_fail"));
		}

		return result;
	}

	// MEDIA
	/**
	 * Insert new media into database
	 * 
	 * @return id in the datastore
	 */
	public ServiceResult<Long> insertMedia(Media media) {
		preventSQLInjMedia(media);
		ServiceResult<Long> result = new ServiceResult<Long>();
		PersistenceManager pm = PMF.get().getPersistenceManager();

		if (media == null) {
			result.setMessage(messages.getString("cannot_handle_with_null"));
		}

		try {
			media = pm.makePersistent(media);
			if (media == null) {
				result.setMessage(messages.getString("insert_media_fail"));
			} else {
				result.setResult(media.getId());
				result.setMessage(messages
						.getString("insert_media_successfully"));
				result.setOK(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage(messages.getString("insert_media_fail"));
		}

		return result;
	}

	private void preventSQLInjMedia(Media media) {
		media.setName(preventSQLInjection(media.getName()));
		media.setDescription(preventSQLInjection(media.getDescription()));
	}

	// STUFF
	private static Policy policy = null;

	public String getGoogleAccountLoginLink() {
		return userService.createLoginURL(Global.HOST_NAME);
	}

	public String getGoogleAccountLogoutLink() {
		return userService.createLogoutURL(Global.HOST_NAME);
	}

	private void preventSQLInjUserInfo(UserInfo userInfo) {
		userInfo.setUsername(preventSQLInjection(userInfo.getUsername()));
		userInfo.setPassword(preventSQLInjection(userInfo.getPassword()));
		userInfo.setFirst_name(preventSQLInjection(userInfo.getFirst_name()));
		userInfo.setLast_name(preventSQLInjection(userInfo.getLast_name()));
		userInfo.setPhone(preventSQLInjection(userInfo.getPhone()));
		userInfo.setEmail(preventSQLInjection(userInfo.getEmail()));
		userInfo.setAddress(preventSQLInjection(userInfo.getAddress()));
		userInfo.setLang(preventSQLInjection(userInfo.getLang()));
		userInfo.setCountry(preventSQLInjection(userInfo.getCountry()));
	}

	public static void preventSQLInjProduct(Product product) {
		product.setName(preventSQLInjection(product.getName()));
		product.setAddress(preventSQLInjection(product.getAddress()));
	}

	public static void preventSQLInjPage(Page page) {
		page.setName(preventSQLInjection(page.getName()));
		page.setContent(preventSQLInjection(page.getContent()));
	}

	public static void preventSQLInjComment(Comment comment) {
		comment.setContent(preventSQLInjection(comment.getContent()));
	}

	public static final int MAXIMUM_NUMBER_OF_WORDS_TO_SEARCH = 5;
	public static final int MAX_NUMBER_OF_WORDS_TO_PUT_IN_INDEX = 200;
	final int DURATION_IN_S = 60 * 60 * 24; // duration remembering login: 1 day

	/**
	 * Stuff function to help preventSQLInjection do a job
	 * 
	 * @return {@link Policy}
	 * @author VoMinhTam
	 * @see <a
	 *      href="http://www.owasp.org/index.php/Category:OWASP_AntiSamy_Project">OWASP_AntiSamy</a>
	 */
	private static Policy getAntiSamyPolicy() {
		Policy policy = null;
		try {
			File file = new File("otherconfigs/antisamy-anythinggoes-1.3.xml");
			policy = Policy.getInstance(file);
		} catch (PolicyException pe) {
			log.log(Level.SEVERE, "getAntiSamyPolicy:" + pe.getMessage(), pe);
		}
		return policy;
	}

	/**
	 * The function to prevent SQLInjection which may be dangerous to our
	 * datastore. I haven't digged whether SQL injection affect to datastore,
	 * but because datastore also support JDOQL, kind of SQL-like, to query so I
	 * think it's also affectted.<br />
	 * <i>Library:</i> antisamy <br />
	 * <b>Note:</b> Every input from client should be check by this function for
	 * more security
	 * 
	 * @param <code>dirtyInput:</code> input from client
	 * @return output that doesn't contain any dirty things, such as: escapse
	 *         character, boolean, ...
	 * @author VoMinhTam
	 * @see <a
	 *      href="http://www.owasp.org/index.php/Category:OWASP_AntiSamy_Project">OWASP_AntiSamy</a>
	 */
	public static String preventSQLInjection(String dirtyInput) {
		if (dirtyInput == null)
			return null;
		CleanResults cr = null;
		try {
			if (policy == null)
				policy = getAntiSamyPolicy();
			AntiSamy as = new AntiSamy();
			cr = as.scan(dirtyInput, policy);
		} catch (Exception ex) {
			log.log(Level.SEVERE, "preventSQLInj:" + ex.getMessage(), ex);
		}
		return cr.getCleanHTML(); // some custom function
	}

	/**
	 * This function help generate an md5 string which corespondent to input
	 * string. It's especially helpful for security in password.<br />
	 * 
	 * <b>Usage:</b>
	 * <ol>
	 * <li><i>Store:</i> before store password in datastore, plz let password go
	 * through this function <br />
	 * <ii><i>Match:</i> get password input from client and let it go throught
	 * this function, and check whether it matchs md5 from datastore or not
	 * <br/>
	 * </ ol>
	 * 
	 * @param <code>value</code>
	 * @return md5 of this value
	 */
	public static String md5(String value) {
		MessageDigest algorithm = null;

		try {
			algorithm = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException nsae) {
			log.severe("[MD5]Cannot find digest algorithm");
			return null;
		}

		byte[] defaultBytes = value.getBytes();
		algorithm.reset();
		algorithm.update(defaultBytes);
		byte messageDigest[] = algorithm.digest();
		StringBuffer hexString = new StringBuffer();

		for (int i = 0; i < messageDigest.length; i++) {
			String hex = Integer.toHexString(0xFF & messageDigest[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	public static void updateFTSStuffForUserInfo(UserInfo userInfo) {
		StringBuffer sb = new StringBuffer();
		sb.append(userInfo.getUsername() + " " + userInfo.getFirst_name() + " "
				+ userInfo.getLast_name());
		Set<String> new_ftsTokens = SearchJanitorUtils
				.getTokensForIndexingOrQuery(sb.toString(),
						MAX_NUMBER_OF_WORDS_TO_PUT_IN_INDEX);
		Set<String> ftsTokens = userInfo.getFts();
		ftsTokens.clear();

		for (String token : new_ftsTokens) {
			ftsTokens.add(token);
		}
	}

}
