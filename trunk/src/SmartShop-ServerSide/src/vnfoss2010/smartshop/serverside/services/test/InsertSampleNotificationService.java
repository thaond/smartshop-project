package vnfoss2010.smartshop.serverside.services.test;

import java.util.ArrayList;
import java.util.Map;

import vnfoss2010.smartshop.serverside.database.NotificationServiceImpl;
import vnfoss2010.smartshop.serverside.database.entity.Notification;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.test.SampleData;

public class InsertSampleNotificationService extends BaseRestfulService {
	NotificationServiceImpl db = NotificationServiceImpl.getInstance();

	public InsertSampleNotificationService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		ArrayList<Notification> pages = SampleData.getSampleNotifications();
		for (Notification no : pages) {
			db.insertNotification(no).getResult();
		}

		return "Done";
	}
}
