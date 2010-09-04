package vnfoss2010.smartshop.serverside.services.test;

import java.util.Map;

import vnfoss2010.smartshop.serverside.database.DatabaseUtils;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

public class DeleteDatabaseService extends BaseRestfulService {
	public DeleteDatabaseService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		boolean b = DatabaseUtils.deleteAllDatabase();

		return String.valueOf(b);
	}

}
