package vnfoss2010.smartshop.serverside.services.comment;

import java.util.List;
import java.util.Map;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.CommentServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Comment;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.MissingParameterException;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

public class GetCommentService extends BaseRestfulService {
	private static CommentServiceImpl dbComment = CommentServiceImpl.instance();

	public GetCommentService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		JSONObject json = null;
		JsonObject jsonReturn = new JsonObject();
		try {
			json = new JSONObject(content);
		} catch (Exception e) {
		}
		String commentType = getParameterWithThrow("type", params, json);
		Long typeID = Long.parseLong(getParameterWithThrow("type_id", params,
				json));
		ServiceResult<List<Comment>> result = dbComment.getComment(typeID,
				commentType);
		if (result.isOK()) {
			jsonReturn.addProperty("errCode", 0);
			jsonReturn.add("comments", Global.gsonDateWithoutHour.toJsonTree(result
					.getResult()));
		} else {
			jsonReturn.addProperty("errCode", 1);
		}
		jsonReturn.addProperty("message", result.getMessage());

		return jsonReturn.toString();
	}
}
