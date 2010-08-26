package vnfoss2010.smartshop.serverside.services.comment;

import java.util.Map;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.CommentServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Comment;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;

import com.google.appengine.repackaged.org.json.JSONObject;

public class CreateCommentService extends BaseRestfulService {
	private static CommentServiceImpl dbComment = CommentServiceImpl.getInstance();

	public CreateCommentService(String serviceName) {
		super(serviceName);
	}

	@Override
	public String process(Map<String, String[]> params, String content)
			throws Exception, RestfulException {
		JSONObject jsonReturn = new JSONObject();

		Comment comment = Global.gsonWithDate.fromJson(content, Comment.class);
		if (dbComment.validateCommentTypeID(comment.getType(), comment
				.getType_id()) == false) {
			jsonReturn.put("errCode", 1);
			jsonReturn.put("message", "comment type or type id is incorrect");
		} else {
			ServiceResult<Long> result = dbComment.insertComment(comment);
			if (result.isOK()) {
				jsonReturn.put("errCode", 0);
				jsonReturn.put("id", result.getResult());
			} else {
				jsonReturn.put("errCode", 1);
			}
			jsonReturn.put("message", result.getMessage());
		}
		return jsonReturn.toString();
	}
}
