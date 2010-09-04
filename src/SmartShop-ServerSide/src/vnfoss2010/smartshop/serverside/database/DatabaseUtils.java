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
import vnfoss2010.smartshop.serverside.database.entity.Attribute;
import vnfoss2010.smartshop.serverside.database.entity.Category;
import vnfoss2010.smartshop.serverside.database.entity.Comment;
import vnfoss2010.smartshop.serverside.database.entity.Media;
import vnfoss2010.smartshop.serverside.database.entity.Notification;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.Statistic;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;
import vnfoss2010.smartshop.serverside.database.entity.UserSubcribeProduct;
import vnfoss2010.smartshop.serverside.utils.SearchCapable;
import vnfoss2010.smartshop.serverside.utils.SearchJanitorUtils;
import vnfoss2010.smartshop.serverside.utils.UtilsFunction;

import com.google.appengine.api.datastore.DatastoreNeedIndexException;
import com.google.appengine.api.datastore.DatastoreTimeoutException;

/**
 * This class provides functions which 're useful for the database
 * 
 * @author VoMinhTam
 */
public class DatabaseUtils {

	private final static Logger log = Logger.getLogger(DatabaseUtils.class
			.getName());
	private static Policy policy = null;

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
	 * Search any searchable entity (a class must be extends
	 * {@link SearchCapable}
	 * 
	 * @return List of records that fit the query string
	 */
	public static <T extends SearchCapable> List<T> searchByQuery(
			String queryString, Class<T> entity) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		StringBuffer queryBuffer = new StringBuffer();
		queryBuffer.append("SELECT FROM " + entity.getName() + " WHERE ");
		Set<String> queryTokens = SearchJanitorUtils
				.getTokensForIndexingOrQuery(queryString,
						Global.MAXIMUM_NUMBER_OF_WORDS_TO_SEARCH);
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

		List<T> list = null;
		try {
			list = (List<T>) query.executeWithArray(parametersForSearch
					.toArray());
		} catch (DatastoreTimeoutException e) {
			log.severe(e.getMessage());
			log.severe("datastore timeout at: " + queryString);
		} catch (DatastoreNeedIndexException e) {
			log.severe(e.getMessage());
			log.severe("datastore need index exception at: " + queryString);
		}

		List<T> result = UtilsFunction.cloneList(list);
		try {
			pm.close();
		} catch (Exception e) {
		}

		return result;
	}

	public static boolean deleteAllDatabase() {
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			Query query = pm.newQuery(Attribute.class);
			query.deletePersistentAll();

			query = pm.newQuery(Category.class);
			query.deletePersistentAll();

			query = pm.newQuery(Comment.class);
			query.deletePersistentAll();

			query = pm.newQuery(Media.class);
			query.deletePersistentAll();

			query = pm.newQuery(Notification.class);
			query.deletePersistentAll();

			query = pm.newQuery(Page.class);
			query.deletePersistentAll();

			query = pm.newQuery(Product.class);
			query.deletePersistentAll();

			query = pm.newQuery(Statistic.class);
			query.deletePersistentAll();

			query = pm.newQuery(UserInfo.class);
			query.deletePersistentAll();

			query = pm.newQuery(UserSubcribeProduct.class);
			query.deletePersistentAll();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
