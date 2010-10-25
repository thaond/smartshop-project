package vnfoss2010.smartshop.serverside.notification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.ServiceResult;

public class NotificationUtils {
	public static ServiceResult<String> sendNotification(String userKey,
			String title, String bodyContent) {
		ServiceResult<String> sResult = new ServiceResult<String>();

		String urlString = "http://notify.xtify.com/api/1.0/SdkNotification";
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<sdk-notification>" + "<actionType>LAUNCH_APP</actionType>"
				+ "<appId>" + Global.XTIFY_API_KEY + "</appId>" + "<userKey>"
				+ userKey + "</userKey>" + "<notificationBody>" + bodyContent
				+ "</notificationBody>" + "<notificationTitle>" + title
				+ " </notificationTitle>" + "</sdk-notification>";

		Global.log(null, "Content: " + content);

		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		HttpURLConnection urlConn = null;
		OutputStream out = null;
		BufferedReader in = null;
		if (url != null) {
			try {
				urlConn = (HttpURLConnection) url.openConnection();
				urlConn.addRequestProperty("Content-Type", "application/xml");
				urlConn.setRequestMethod("PUT");
				urlConn.setDoOutput(true);
				urlConn.setInstanceFollowRedirects(false);

				urlConn.connect();
				// Write content data to server
				out = urlConn.getOutputStream();
				out.write(content.getBytes());
				out.flush();

				// Check response code
				if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					in = new BufferedReader(new InputStreamReader(urlConn
							.getInputStream()), 8192);
					StringBuffer strBuff = new StringBuffer();
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						strBuff.append(inputLine);
					}
					sResult.setOK(true);
					sResult.setResult(strBuff.toString());
					sResult.setMessage(Global.messages.getString("send_push_notification_successfully"));
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
				sResult.setOK(false);
				sResult.setMessage(e.getMessage());
			} finally {
				try {
					if (in != null) {
						in.close();
					}
					if (out != null) {
						out.close();
					}
					if (urlConn != null) {
						urlConn.disconnect();
					}
				} catch (IOException e) {
					e.printStackTrace();
					
					sResult.setOK(false);
					sResult.setMessage(e.getMessage());
				}
			}
		}
		return sResult;
	}
}
