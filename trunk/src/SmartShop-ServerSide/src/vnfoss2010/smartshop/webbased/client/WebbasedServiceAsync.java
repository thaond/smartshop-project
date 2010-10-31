package vnfoss2010.smartshop.webbased.client;

import vnfoss2010.smartshop.webbased.share.WProduct;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface WebbasedServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void getProduct(long productId, AsyncCallback<WProduct> callback);
}
