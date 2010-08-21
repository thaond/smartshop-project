package vnfoss2010.smartshop.serverside.database;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;

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

}
