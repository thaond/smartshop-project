package vnfoss2010.smartshop.serverside.services.test;

import java.util.Map;

import com.google.appengine.repackaged.org.json.JSONObject;

import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.test.SampleData;

public class InsertUserInfosService extends BaseRestfulService {
	private AccountServiceImpl db = AccountServiceImpl.getInstance();

	public InsertUserInfosService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		ServiceResult<Void> result = db.insertAllUserInfos(SampleData
				.getSampleListUserInfos());

		return new JSONObject().put("ok", result.isOK()).put("message",
				result.getMessage()).toString();
	}

}
