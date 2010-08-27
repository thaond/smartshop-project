package vnfoss2010.smartshop.serverside.services.notification;

import java.util.Map;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.NotificationServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Notification;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;

public class EditNotificationService extends BaseRestfulService {
	private NotificationServiceImpl dbNotification = NotificationServiceImpl.getInstance();

	public EditNotificationService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		JSONObject jsonReturn = new JSONObject();
		Notification page = Global.gsonWithDate.fromJson(content, Notification.class);
		ServiceResult<Void> result = dbNotification.editNotification(page);
		if (result.isOK()) {
			jsonReturn.put("errCode", 0);
		} else {
			jsonReturn.put("errCode", 1);
		}
		jsonReturn.put("message", result.getMessage());

		return jsonReturn.toString();
	}
}
