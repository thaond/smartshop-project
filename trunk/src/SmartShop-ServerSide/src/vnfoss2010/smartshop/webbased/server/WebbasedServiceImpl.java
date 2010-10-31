package vnfoss2010.smartshop.webbased.server;

import java.util.List;

import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.CommentServiceImpl;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Comment;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;
import vnfoss2010.smartshop.webbased.client.WebbasedService;
import vnfoss2010.smartshop.webbased.share.WProduct;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class WebbasedServiceImpl extends RemoteServiceServlet implements
		WebbasedService {

	private ProductServiceImpl dbProduct;
	private AccountServiceImpl dbAccount;
	private CommentServiceImpl dbComment;

	public String greetServer(String input) throws IllegalArgumentException {
		return "Hello";
	}

	@Override
	public WProduct getProduct(long productId) {
		WProduct wProduct = null;
		dbProduct = ProductServiceImpl.getInstance();
		dbAccount = AccountServiceImpl.getInstance();
		dbComment = CommentServiceImpl.getInstance();

		ServiceResult<Product> resultProduct = dbProduct.findProduct(productId);
		if (resultProduct.isOK()) {
			ServiceResult<UserInfo> resultUserInfo = dbAccount
					.getUserInfo(resultProduct.getResult().getUsername());
			if (resultUserInfo.isOK()) {
				wProduct = resultProduct.getResult().cloneObject();
				wProduct.userInfo = resultUserInfo.getResult().cloneObject();
			}

			ServiceResult<List<Comment>> resultComment = dbComment.getComment(
					productId, "product");
			if (resultComment.isOK()){
				for (Comment comment : resultComment.getResult()){
					wProduct.listComments.add(comment.cloneObject());
				}
			}
		}

		return wProduct;
	}
	
}
