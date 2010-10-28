package vnfoss2010.smartshop.serverside.services.notification;

import java.util.List;
import java.util.Map;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.NotificationServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Notification;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

/**
 * 
 * @author VoMinhTam
 */
public class GetNotificationsByUsernameService extends BaseRestfulService {
	private NotificationServiceImpl dbNotification = NotificationServiceImpl.getInstance();

	public GetNotificationsByUsernameService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		JSONObject json = null;
		try {
			json = new JSONObject(content);
		} catch (Exception e) {
		}
		
		String username = getParameter("username", params, json);
		int limit = 0;
		try {
			limit = Integer.parseInt(getParameter("limit", params, json));
		} catch (Exception e) {
		}
		
		int type=0;
		try {
			type= Integer.parseInt(getParameter("type", params, json));
		} catch (Exception e) {
		}
		
		long lastupdate = 0;
		try {
			lastupdate= Long.parseLong(getParameter("lastupdate", params, json));
		} catch (Exception e) {
		}

		JsonObject jsonReturn = new JsonObject();

		ServiceResult<List<Notification>> result = dbNotification.getListNotificationsByUsername(username, limit, type, lastupdate);
		if (result.isOK()) {
			jsonReturn.addProperty("errCode", 0);
			jsonReturn.addProperty("message", result.getMessage());
			
			jsonReturn.add("notifications", Global.gsonWithDate.toJsonTree(result.getResult()));
		} else {
			jsonReturn.addProperty("errCode", 1);
			jsonReturn.addProperty("message", result.getMessage());
		}
		return jsonReturn.toString();
	}

}
