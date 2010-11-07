package vnfoss2010.smartshop.webbased.client;

import vnfoss2010.smartshop.webbased.share.WGoogleUser;
import vnfoss2010.smartshop.webbased.share.WPage;
import vnfoss2010.smartshop.webbased.share.WProduct;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface WebbasedServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void getProduct(long productId, AsyncCallback<WProduct> callback);

	void getGoogleAccountLink(AsyncCallback<WGoogleUser> callback);

	void getPage(long pageId, AsyncCallback<WPage> callback);
}
