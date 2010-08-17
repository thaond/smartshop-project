package vnfoss2010.smartshop.serverside.services.test;

import java.util.Map;

import com.google.appengine.repackaged.org.json.JSONObject;

import vnfoss2010.smartshop.serverside.database.DatabaseServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

public class DeleteUserInfosService extends BaseRestfulService {
	private DatabaseServiceImpl db = DatabaseServiceImpl.getInstance();

	public DeleteUserInfosService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		ServiceResult<Void> result = db.deleteAllUserInfos();

		return new JSONObject().put("ok", result.isOK()).put("message",
				result.getMessage()).toString();
	}

}
