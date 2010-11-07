package vnfoss2010.smartshop.serverside.services.comment;

import java.util.Map;

import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

public class DeleteCommentService extends BaseRestfulService {
	public DeleteCommentService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		return null;
	}
}
