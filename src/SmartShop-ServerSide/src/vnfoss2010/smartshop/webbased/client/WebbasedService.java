package vnfoss2010.smartshop.webbased.client;

import vnfoss2010.smartshop.webbased.share.WProduct;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("../smartshop/webbased")
public interface WebbasedService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
	
	public WProduct getProduct(long productId);
	
	public static class Util {
		public static WebbasedServiceAsync getInstance() {
			return GWT.create(WebbasedService.class);
		}
	}
}
