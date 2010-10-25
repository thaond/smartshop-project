package com.smartshop.docs.client.rpc;

import vnfoss2010.smartshop.serverside.database.ServiceResult;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.smartshop.docs.share.GoogleUser;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("../smartshop/util")
public interface RPCService extends RemoteService {
	GoogleUser getGoogleAccountLink();
	
	public ServiceResult<String> signUpAPIKey(String source, String email);
	
	public static class Util {
		public static RPCServiceAsync getInstance() {
			return GWT.create(RPCService.class);
		}
	}
}
