package com.smartshop.docs.share;

import java.io.Serializable;

public class GoogleUser implements Serializable {
	private static final long serialVersionUID = 1L;
	public boolean isAdmin;
	public boolean isLogin;
	public String linkLogin;
	public String linkLogout;
	public String username;
	public String email;
	public String authDomain;
	public String nickName;
	public String userId;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GoogleUser [authDomain=" + authDomain + ", email=" + email
				+ ", isAdmin=" + isAdmin + ", isLogin=" + isLogin
				+ ", linkLogin=" + linkLogin + ", linkLogout=" + linkLogout
				+ ", nickName=" + nickName + ", userId=" + userId
				+ ", username=" + username + "]";
	}

}
