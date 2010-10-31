package com.smartshop.docs.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartshop.docs.share.GoogleUser;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface RPCServiceAsync {
	void getGoogleAccountLink(AsyncCallback<GoogleUser> callback);

	void signUpAPIKey(String source, String email,
			AsyncCallback<String> callback);
}
