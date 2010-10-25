package com.smartshop.docs.client.rpc;

import vnfoss2010.smartshop.serverside.database.ServiceResult;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartshop.docs.share.GoogleUser;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface RPCServiceAsync {
	void getGoogleAccountLink(AsyncCallback<GoogleUser> callback);

	void signUpAPIKey(String source, String email,
			AsyncCallback<ServiceResult<String>> callback);
}
