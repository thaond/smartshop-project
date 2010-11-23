package vnfoss2010.smartshop.serverside.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Logger;

import vnfoss2010.smartshop.serverside.Global;

public class SendSMS {

	static Logger log = Logger.getLogger(SendSMS.class.getName());
	private static String BULK_SMS_USERNAME = "tamvo123";
	private static String BULK_SMS_PASSWORD = "reborn";
	private static String SUCCESS_MESSAGE = "0|IN_PROGRESS";
	public static final int SMS_SIZE = 160;

	static public String stringToHex(String s) {
		char[] chars = s.toCharArray();
		String next;
		StringBuffer output = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			next = Integer.toHexString((int) chars[i]);
			// Unfortunately, toHexString doesn't pad with zeroes, so we have
			// to.
			for (int j = 0; j < (4 - next.length()); j++) {
				output.append("0");
			}
			output.append(next);
		}
		return output.toString();
	}

	/*
	 * Note the suggested encoding for certain parameters, notably the username
	 * and password.
	 * 
	 * Please remember that 16bit support is a route-specific feature. Please
	 * contact us if you need to confirm the status of a particular route.
	 * 
	 * Also, mobile handsets only implement partial support for non- Latin
	 * characters in various languages and will generally only support languages
	 * of the area of their distribution. Please do not expect e.g. a handset
	 * sold in South America to display Arabic text.
	 */
	public static boolean sendSMS(String message, String phoneNumber) {
		try {
			// Construct data
			String data = "";

			data += "username="
					+ URLEncoder.encode(BULK_SMS_USERNAME, "ISO-8859-1");
			data += "&password="
					+ URLEncoder.encode(BULK_SMS_PASSWORD, "ISO-8859-1");
			data += "&message=" + stringToHex(message);
			data += "&dca=16bit";
			data += "&want_report=0";
			data += "&msisdn=" + phoneNumber;

			// Send data
			URL url = new URL(
					"http://usa.bulksms.com:5567/eapi/submission/send_sms/2/2.0");
			/*
			 * If your firewall blocks access to port 5567, you can fall back to
			 * port 80: URL url = new
			 * URL("http://bulksms.vsms.net/eapi/submission/send_sms/2/2.0");
			 * (See FAQ for more details.)
			 */
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());
			wr.write(data);
			wr.flush();
			Global.log(log, "data :" + data);

			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line;
			String totalLine = "";
			while ((line = rd.readLine()) != null) {
				// Print the response output...
				totalLine += line;
			}
			if (totalLine.indexOf(SUCCESS_MESSAGE) == -1) {
				return false;
			}
			wr.close();
			rd.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		sendSMS("test bulk", "84979995859");
	}
}
