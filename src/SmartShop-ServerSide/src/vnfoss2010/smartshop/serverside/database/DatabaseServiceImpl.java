package vnfoss2010.smartshop.serverside.database;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;

import com.google.appengine.api.datastore.DatastoreNeedIndexException;
import com.google.appengine.api.datastore.DatastoreTimeoutException;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * The server side implementation of the RPC service.<br>
 * 
 * Useful link:
 * <ul>
 * <li><a href="http://developerlife.com/tutorials/?p=230">Using Servlet
 * Sessions in GWT</a>
 * <li><a href=
 * "http://code.google.com/p/google-web-toolkit-incubator/wiki/LoginSecurityFAQ"
 * >Login Security</a>
 * </ul>
 * 
 * @author tamvo
 */
@SuppressWarnings("serial")
public class DatabaseServiceImpl {

	private static DatabaseServiceImpl instance;

	public static UserService userService = UserServiceFactory.getUserService();

	public static final int MAXIMUM_NUMBER_OF_WORDS_TO_SEARCH = 5;
	public static final int MAX_NUMBER_OF_WORDS_TO_PUT_IN_INDEX = 200;

	/**
	 * Default constructor<br>
	 * Modifier should be public because if modifier is private, an exception
	 * will be thrown<br>
	 * <code>Class {@link org.mortbay.jetty.servlet.Holder} can not access a member of class {@link DatabaseServiceImpl} with modifiers "private"</code>
	 */
	public DatabaseServiceImpl() {
		instance = this;
		// getResponse().setContentLength(512);
	}

	final int DURATION_IN_S = 60 * 60 * 24; // duration remembering login: 1 day

	// (calculate in seconds)

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

	private static Policy policy = null;
	private final static Logger log = Logger
			.getLogger(DatabaseServiceImpl.class.getName());

	public String greetServer(String input) throws IllegalArgumentException {
		// Verify that the input is valid.
		// if (!FieldVerifier.isValidName(input)) {
		// // If the input is not valid, throw an IllegalArgumentException back
		// to
		// // the client.
		// throw new IllegalArgumentException(
		// "Name must be at least 4 characters long");
		// }
		//
		// String serverInfo = getServletContext().getServerInfo();
		// String userAgent = getThreadLocalRequest().getHeader("User-Agent");
		// return "Hello, " + input + "!<br><br>I am running " + serverInfo
		// + ".<br><br>It looks like you are using:<br>" + userAgent;

		input = preventSQLInjection(input);
		return "Hello";
	}

	public String getGoogleAccountLoginLink() {
		return userService.createLoginURL(Global.HOST_NAME);
	}

	public String getGoogleAccountLogoutLink() {
		return userService.createLogoutURL(Global.HOST_NAME);
	}

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

	public static List<UserInfo> searchGuestBookEntries(String queryString,
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

	/**
	 * Get current Session of client
	 * 
	 * @return
	 */
	// private HttpSession getSession() {
	// return this.getThreadLocalRequest().getSession();
	// }
	//
	// private HttpServletResponse getResponse() {
	// return this.getThreadLocalResponse();
	// }
	//
	// private HttpServletRequest getRequest() {
	// return this.getThreadLocalRequest();
	// }
}
