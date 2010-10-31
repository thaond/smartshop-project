package com.smartshop.docs.server;

import java.util.Date;

import javax.jdo.PersistenceManager;

import vnfoss2010.smartshop.serverside.database.PMF;
import vnfoss2010.smartshop.serverside.database.entity.APIKey;
import vnfoss2010.smartshop.serverside.utils.UtilsFunction;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.smartshop.docs.client.rpc.RPCService;
import com.smartshop.docs.share.Config;
import com.smartshop.docs.share.GoogleUser;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class RPCServiceImpl extends RemoteServiceServlet implements RPCService {
	public static UserService userService = UserServiceFactory.getUserService();

	@Override
	public GoogleUser getGoogleAccountLink() {
		GoogleUser googleUser = new GoogleUser();
		googleUser.linkLogin = userService.createLoginURL(Config.HOST_NAME);
		googleUser.linkLogout = userService.createLogoutURL(Config.HOST_NAME);
		googleUser.isLogin = userService.isUserLoggedIn();
		if (userService.isUserLoggedIn()) {
			googleUser.isAdmin = userService.isUserAdmin();

			User user = userService.getCurrentUser();
			googleUser.email = user.getEmail();
			googleUser.authDomain = user.getAuthDomain();
			googleUser.nickName = user.getNickname();
			googleUser.userId = user.getUserId();
		}
		return googleUser;
	}

	@Override
	public String signUpAPIKey(String source, String email) {
		String result = "";
		PersistenceManager pm = PMF.get().getPersistenceManager();

		APIKey apiKey = new APIKey(source, email, new Date());
		apiKey = pm.makePersistent(apiKey);

		if (apiKey != null) {
			result = UtilsFunction.encrypt(source);
		}

		try {
			pm.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}
