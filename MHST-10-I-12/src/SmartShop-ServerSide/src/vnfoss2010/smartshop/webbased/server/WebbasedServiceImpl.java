package vnfoss2010.smartshop.webbased.server;

import java.util.List;

import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.CommentServiceImpl;
import vnfoss2010.smartshop.serverside.database.PageServiceImpl;
import vnfoss2010.smartshop.serverside.database.ProductServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.entity.Comment;
import vnfoss2010.smartshop.serverside.database.entity.Page;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;
import vnfoss2010.smartshop.webbased.client.WebbasedService;
import vnfoss2010.smartshop.webbased.share.WGoogleUser;
import vnfoss2010.smartshop.webbased.share.WPage;
import vnfoss2010.smartshop.webbased.share.WProduct;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.smartshop.docs.server.RPCServiceImpl;
import com.smartshop.docs.share.GoogleUser;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class WebbasedServiceImpl extends RemoteServiceServlet implements
		WebbasedService {

	private PageServiceImpl dbPage;
	private ProductServiceImpl dbProduct;
	private AccountServiceImpl dbAccount;
	private CommentServiceImpl dbComment;

	public String greetServer(String input) throws IllegalArgumentException {
		return "Hello";
	}
		
	public WebbasedServiceImpl(){
		super();
		
		dbProduct = ProductServiceImpl.getInstance();
		dbAccount = AccountServiceImpl.getInstance();
		dbComment = CommentServiceImpl.getInstance();
		dbPage = PageServiceImpl.getInstance();
	}

	@Override
	public WProduct getProduct(long productId) {
		WProduct wProduct = null;

		ServiceResult<Product> resultProduct = dbProduct.findProduct(productId);
		if (resultProduct.isOK()) {
			// Load UserInfo
			ServiceResult<UserInfo> resultUserInfo = dbAccount
					.getUserInfo(resultProduct.getResult().getUsername());
			if (resultUserInfo.isOK()) {
				wProduct = resultProduct.getResult().cloneObject();
				wProduct.userInfo = resultUserInfo.getResult().cloneObject();
			}

			// Load comments
			ServiceResult<List<Comment>> resultComment = dbComment.getComment(
					productId, "product");
			if (resultComment.isOK()) {
				for (Comment comment : resultComment.getResult()) {
					wProduct.listComments.add(comment.cloneObject());
				}
			}

			// Load related products
			ServiceResult<List<Product>> resultRelatedProduct = dbProduct
					.getRelatedProducts(productId, 5);
			if (resultRelatedProduct.isOK()) {
				for (Product product : resultRelatedProduct.getResult()) {
					wProduct.listRelatedProduct.add(product.cloneObject());
				}
			}
		}

		return wProduct;
	}

	private RPCServiceImpl rpcServiceImpl = new RPCServiceImpl();

	@Override
	public WGoogleUser getGoogleAccountLink() {
		return cloneObject(rpcServiceImpl.getGoogleAccountLink());
	}
	
	public WGoogleUser cloneObject(GoogleUser g) {
		WGoogleUser wg = new WGoogleUser();
		wg.isAdmin = g.isAdmin;
		wg.isLogin = g.isLogin;
		wg.linkLogin = g.linkLogin;
		wg.linkLogout = g.linkLogout;
		wg.username = g.username;
		wg.email = g.email;
		wg.authDomain = g.authDomain;
		wg.nickName = g.nickName;
		wg.userId = g.userId;

		return wg;
	}

	@Override
	public WPage getPage(long pageId) {
		WPage wPage = null;

		ServiceResult<Page> resultPage = dbPage.findPage(pageId);
		if (resultPage.isOK()) {
			// Load UserInfo
			ServiceResult<UserInfo> resultUserInfo = dbAccount
					.getUserInfo(resultPage.getResult().getUsername());
			if (resultUserInfo.isOK()) {
				wPage = resultPage.getResult().cloneObject();
				wPage.userInfo = resultUserInfo.getResult().cloneObject();
			}

			// Load comments
			ServiceResult<List<Comment>> resultComment = dbComment.getComment(
					pageId, "page");
			if (resultComment.isOK()) {
				for (Comment comment : resultComment.getResult()) {
					wPage.listComments.add(comment.cloneObject());
				}
			}
		}

		return wPage;
	}
}
