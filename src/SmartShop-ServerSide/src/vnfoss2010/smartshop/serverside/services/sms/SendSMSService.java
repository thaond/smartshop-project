package vnfoss2010.smartshop.serverside.services.sms;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.PersistenceManager;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.PMF;
import vnfoss2010.smartshop.serverside.database.entity.Session;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

@SuppressWarnings("serial")
public class SendSMSService extends BaseRestfulService {
	
	public SendSMSService(String serviceName) {
		super(serviceName);
	}

	/*
	 * Constants
	 */
	private static final String USER_AGENT			= "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.9.1.7) Gecko/20091221 Firefox/3.5.7";
	private static final String ROUTE_ID1			= "RouteID=route.neo_srv01;";
	private static final String ROUTE_ID2			= "RouteID=route.neo_srv02;";
	private static final String AUTHENTICATE_URL	= "http://www.mobifone.com.vn/web/vn/users/authenticate.jsp";
	private static final String SMS_URL				= "http://www.mobifone.com.vn/web/vn/sms/result.jsp";
	private static final String SMS_REFERER			= "http://www.mobifone.com.vn/web/vn/sms/";
	private static final String ADV_URL				= "http://www.mobifone.com.vn/web/vn/sms/";
	private static final String ADV_REFERER			= "http://www.mobifone.com.vn/web/vn/";
	
	private Logger log = Logger.getLogger("Server");
	
	/*
	 * For Debug
	 */
	public static String lastConsole = "";
	public static Date lastTime = new Date();
	
	/*
	 * Save Session
	 */
	public void saveSession(String username, String jsessionid)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Session session = null;
        try {
        	session = pm.getObjectById(Session.class, username);
        }
        catch (Exception e)
        {
        	System.out.print(e.getMessage());
        }
        finally {
        }

        try {
        	if (session == null)
        	{
        		 session = new Session(username, jsessionid, new Date());
                 pm.makePersistent(session);
        	}
        	else
        	{
        		session.setCreationDate(new Date());
        		session.setJsessionid(jsessionid);
        	}
        }
        catch (Exception e)
        {
        	System.out.print(e.getMessage());
        }
        finally {
            pm.close();
        }
	}
	
	/*
	 * Delete Session
	 */
	public void deleteSession(String username)
	{
		saveSession(username, null);
	}
	
	/*
	 * Delete Session
	 */
	public String getJSessionId(String username)
	{
		PersistenceManager pm = PMF.get().getPersistenceManager();

		String jsessionid = null;
        try {
        	Session session = pm.getObjectById(Session.class, username);
        	if (session != null)
        	{
        		Date date = session.getCreationDate();
        		long delta = new Date().getTime() - date.getTime();
        		final long maxMinute = 25;
        		if ((long)delta < maxMinute*60*1000)
        		{
        			jsessionid = session.getJsessionid();
        		}
        		session.setCreationDate(new Date());
        	}
        } 
        catch (Exception e)
        {
        	System.out.print(e.getMessage());
        }
        finally {
            pm.close();
        }
		return jsessionid;
	}
	
	/*
	 * Send Message
	 */
	public boolean sendSms(String username, String password, String toPhone, String message)
	{
		if(sendSms(username, password, toPhone, message, ROUTE_ID1))
			return true;
		else
			return sendSms(username, password, toPhone, message, ROUTE_ID2);
	}
	
	/*
	 * Send Message
	 */
	public boolean sendSms(String username, String password, String toPhone, String message, String routeID)
	{
		boolean result = false;
		try {
			///////////////////////////////////////////
			// Login
			String jsessionid = getJSessionId(username);
			if (jsessionid == null)
			{
				lastConsole += "perform login\n\n-------------------------------------\n\n";
				String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
			    data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

			    // Send data
			    URL url = new URL(AUTHENTICATE_URL);
			    URLConnection conn = url.openConnection();
			    conn.setDoOutput(true);
			    conn.addRequestProperty("Cookie", routeID);
			    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			    wr.write(data);
			    wr.flush();

			    // Get the response
			    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			    String line;
			    String response = "";
			    while ((line = rd.readLine()) != null) {
			    	response += line;
			    }
			    
			    lastConsole += "Header: " + conn.getHeaderFields() + "\n";
			    lastConsole += "Login Done:\n" + response + "\n\n-------------------------------------\n\n";
			    
			    // Get JSESSIONID
			    String cookieHeader = "";
			    Map responseMap = conn.getHeaderFields();
			    for (Iterator iterator = responseMap.keySet().iterator(); iterator.hasNext();) {
			    	String key = (String) iterator.next();

			    	if (key.equals("set-cookie"))
			    	{
						List values = (List) responseMap.get(key);
						for (int i = 0; i < values.size(); i++) {
							Object o = values.get(i);
							cookieHeader += o + ",";
						}
			    	}
			    }
			    
			    wr.close();
			    rd.close();
			    
			    Pattern pattern = Pattern.compile("JSESSIONID=([\\.a-zA-Z_0-9]+);.*?path=([/\\.a-zA-Z_0-9]+),", Pattern.CASE_INSENSITIVE);
			    Matcher matcher = pattern.matcher(cookieHeader);
			    if (matcher.matches())
			    {
			    	jsessionid = matcher.group(1);
				}
			}
			String cookies = routeID+"JSESSIONID="+jsessionid+";username="+username+";";
			if (jsessionid != null)
			{
				lastConsole += "jsessionid  = " + jsessionid + "\n";
				saveSession(username, jsessionid);
			}
			else
			{
				lastConsole += "Error jsessionid is null\n";
				return result;
			}
			lastConsole += "cookies = " + cookies + "\n\n-------------------------------------\n\n";
			
			/////////////////////////////////////////////
		    // Get AdvString
		    String advStr = "(www.mobifone.com.vn)";
		    URL url = new URL(ADV_URL);
		    URLConnection conn = url.openConnection();
		    conn.addRequestProperty("User-Agent", USER_AGENT);
		    conn.addRequestProperty("Referer", ADV_REFERER);
		    conn.addRequestProperty("Cookie", cookies);

		    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String response = "";
		    String line;
		    while ((line = rd.readLine()) != null) {
		    	response += line;
		    }
		    
		    lastConsole += "Get AdvString  = " + response + "\n\n-------------------------------------\n\n";
		    
		    Pattern pattern = Pattern.compile("javascript\\:checkdata\\([0-9]+,'([\\.\\(\\)\\?;,a-zA-Z_0-9 /\\-\\%]+)'\\)", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		    Matcher matcher = pattern.matcher(response);
		    if(matcher.find())
		    {
		    	advStr = matcher.group(1);
			}

		    lastConsole += "\n\nAdvString  = " + advStr + "\n\n-------------------------------------\n\n";
		    
		    ////////////////////////////////////////////
		    // Send Sms
		    String data = URLEncoder.encode("smsTplId", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8");
		    data += "&" + URLEncoder.encode("mSelect", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8");
		    data += "&" + URLEncoder.encode("pbList", "UTF-8") + "=" + URLEncoder.encode("", "UTF-8");
		    data += "&" + URLEncoder.encode("CCode", "UTF-8") + "=" + URLEncoder.encode("84", "UTF-8");
		    data += "&" + URLEncoder.encode("phonenum", "UTF-8") + "=" + URLEncoder.encode(toPhone, "UTF-8");
		    data += "&" + URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(message + advStr, "UTF-8");
		    data += "&" + URLEncoder.encode("advFlg", "UTF-8") + "=" + URLEncoder.encode("ON", "UTF-8");
		    data += "&" + URLEncoder.encode("chargeFlg", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");

		    log.log(Level.SEVERE, "POST: " + data);
		    url = new URL(SMS_URL);
		    conn = url.openConnection();
		    conn.setDoOutput(true);
		    conn.addRequestProperty("User-Agent", USER_AGENT);
		    conn.addRequestProperty("Referer", SMS_REFERER);
		    conn.addRequestProperty("Cookie", cookies);
		    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(data);
		    wr.flush();

		    // Get the response
		    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    response = "";
		    while ((line = rd.readLine()) != null) {
		    	response += line;
		    }
		    
		    lastConsole += "Sent Sms:\n" + response + "\n\n-------------------------------------\n\n";

		    pattern = Pattern.compile(toPhone.substring(3)+"[\\s]+", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		    matcher = pattern.matcher(response);
		    if(matcher.find())
		    {
		    	result = true;
			}
		    
		    wr.close();
		    rd.close();
		    
		    log.log(Level.SEVERE, lastConsole);
		}
		catch (Exception e) {
			lastConsole += "\n\n--------------------------------\n\nException: "+ e.getMessage() + "\n\n";
			lastConsole += "\n\n--------------------------------\n\nTrace: "+ e.getStackTrace() + "\n\n";
			System.out.print(e.getMessage());
		}
		return result;
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		JsonObject jsonReturn = new JsonObject();
		JSONObject json = null;
		try {
			json = new JSONObject(content);
		} catch (Exception e) {
		}
		
		lastConsole = "";
		lastTime = new Date();
		
		String userName = getParameter("username", params, json);
		String password = getParameter("password", params, json);
		String toPhone = getParameter("toPhone", params, json);
		String message = getParameter("message", params, json);

		if(userName!=null && password!=null && toPhone!=null && message!=null)
		{
			if (sendSms(userName, password, toPhone, message))
			{
				jsonReturn.addProperty("errCode", 0);
				jsonReturn.addProperty("message", Global.messages.getString("lack_para"));
			}
			else
			{
				jsonReturn.addProperty("errCode", 1);
				jsonReturn.addProperty("message", Global.messages.getString("send_sms_fail"));
			}
		}
		else
		{
			jsonReturn.addProperty("errCode", 1);
			jsonReturn.addProperty("message", Global.messages.getString("lack_para"));
		}
		
		return jsonReturn.toString();
	}
	
}
