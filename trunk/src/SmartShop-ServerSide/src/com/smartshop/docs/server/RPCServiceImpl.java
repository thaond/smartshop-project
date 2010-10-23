package com.smartshop.docs.server;

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
		if (userService.isUserLoggedIn()){
			googleUser.isAdmin = userService.isUserAdmin();

			User user = userService.getCurrentUser();
			googleUser.email = user.getEmail();
			googleUser.authDomain = user.getAuthDomain();
			googleUser.nickName = user.getNickname();
			googleUser.userId = user.getUserId();			
		}
		return googleUser;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html
	 *            the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}
}
