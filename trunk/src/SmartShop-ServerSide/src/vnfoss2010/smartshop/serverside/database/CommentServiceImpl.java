package vnfoss2010.smartshop.serverside.database;

import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import vnfoss2010.smartshop.serverside.database.entity.Comment;

public class CommentServiceImpl {
	private static CommentServiceImpl instance;
	public static final String TYPE_PRODUCT = "product";
	public static final String TYPE_PAGE = "page";
	private static ProductServiceImpl dbProduct = ProductServiceImpl
			.getInstance();
	private static PageServiceImpl dbPage = PageServiceImpl.getInstance();

	private static Logger log = Logger.getLogger(CommentServiceImpl.class
			.getName());

	public ServiceResult<Long> insertComment(Comment comment) {
		ServiceResult<Long> result = new ServiceResult<Long>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			comment = pm.makePersistent(comment);
			if (comment == null) {
				result.setMessage("khong the insert Comment");
			} else {
				result.setResult(comment.getId());
				result.setMessage("insert Comment thanh cong");
				result.setOK(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setMessage("khong the insert comment " + e.getMessage());
		} finally {
			try {
				pm.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public boolean validateTypeComment(Comment comment) {
		String type = comment.getType();
		if (type.equals(TYPE_PAGE)) {
			return true;
		}
		if (type.equals(TYPE_PRODUCT)) {
			return true;
		}
		return false;
	}

	public boolean validateCommentTypeID(String type, Long typeID) {
		if (type.equals(TYPE_PAGE)) {
			return dbPage.findPage(typeID).isOK();
		}
		if (type.equals(TYPE_PRODUCT)) {
			return dbProduct.findProduct(typeID).isOK();
		}
		return false;
	}

	public ServiceResult<List<Comment>> getComment(Long typeID, String type) {
		ServiceResult<List<Comment>> result = new ServiceResult<List<Comment>>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery("select from " + Comment.class.getName());
		query.setFilter("type_id == " + typeID);
		query.setResultClass(Comment.class);
		List<Comment> queryResult = (List<Comment>) query.execute();
		if (queryResult == null){
			result.setOK(false);
			result.setMessage("Tim Comment ko thanh cong");
		}else {
			result.setOK(true);
			result.setResult(queryResult);
			result.setMessage("Tim Comment thanh cong");
		}
		return result;
	}

	public static CommentServiceImpl instance() {
		if (instance == null) {
			instance = new CommentServiceImpl();
		}
		return instance;
	}
}
