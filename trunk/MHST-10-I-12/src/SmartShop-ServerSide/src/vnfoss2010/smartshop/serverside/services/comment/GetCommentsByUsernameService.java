package vnfoss2010.smartshop.serverside.services.comment;

import java.util.List;
import java.util.Map;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.CommentServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Comment;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.google.gson.JsonObject;

/**
 * 
 * @author VoMinhTam
 */
public class GetCommentsByUsernameService extends BaseRestfulService {
	CommentServiceImpl db = CommentServiceImpl.getInstance();

	public GetCommentsByUsernameService(String serviceName) {
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

		JsonObject jsonReturn = new JsonObject();

		ServiceResult<List<Comment>> result = db.getListPageFromUsername(username);
		if (result.isOK()) {
			jsonReturn.addProperty("errCode", 0);
			jsonReturn.addProperty("message", result.getMessage());
			
			jsonReturn.add("comments", Global.gsonWithDate.toJsonTree(result.getResult()));
		} else {
			jsonReturn.addProperty("errCode", 1);
			jsonReturn.addProperty("message", result.getMessage());
		}
		return jsonReturn.toString();
	}

}
