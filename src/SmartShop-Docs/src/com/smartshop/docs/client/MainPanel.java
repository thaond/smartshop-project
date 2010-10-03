package com.smartshop.docs.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Label;
import com.smartshop.docs.client.rpc.RPCService;
import com.smartshop.docs.client.rpc.RPCServiceAsync;
import com.smartshop.docs.client.utils.ClientUtil;
import com.smartshop.docs.share.GoogleUser;

public class MainPanel extends FlowPanel {

	private RPCServiceAsync rpcServiceAsync = RPCService.Util.getInstance();
	private HTML linkLogin;
	private Label lblUser;
	private HorizontalPanel pnlHHeader;
	private HorizontalSplitPanel pnlMain;
	private Label lblTitle;

	public MainPanel() {
		initUI();
		initOther();
		// add to root
		add(pnlHHeader);
		add(lblTitle);
		add(pnlMain);
	}

	private void initUI() {
		// title panel
		linkLogin = new HTML();
		linkLogin.addStyleName("lbl-login");
		
		lblUser = new Label();
		lblUser.addStyleName("lbl-user");

		pnlHHeader = new HorizontalPanel();
		pnlHHeader.addStyleName("panel-header");
		pnlHHeader.setWidth((Window.getClientWidth()-25) + "px");
		pnlHHeader.add(linkLogin);
		pnlHHeader.add(lblUser);

		lblTitle = new Label("Hướng dẫn sử dụng và phát triển SmartShop");
		lblTitle.addStyleName("title");
		lblTitle.getElement().getStyle().setBackgroundColor("#BFCFFE");

		pnlMain = new HorizontalSplitPanel();
		pnlMain.setSplitPosition("300px");

		pnlMain.setLeftWidget(TreePanel.getInstance());
		pnlMain.setRightWidget(ContentPanel.getInstance());
	}

	private void initOther() {
		rpcServiceAsync.getGoogleAccountLink(new AsyncCallback<GoogleUser>() {

			@Override
			public void onSuccess(GoogleUser result) {
				ClientUtil.googleUser = result;
				if (ClientUtil.googleUser.isLogin) {
					lblUser.setText(result.email);
					linkLogin.setHTML("<a href=\'" + ClientUtil.googleUser.linkLogout + "\'>Log out" + "<a/>");
				} else {
					lblUser.setText("");
					linkLogin.setHTML("<a href=\'" + ClientUtil.googleUser.linkLogin + "\'>Log in" + "<a/>");
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}
	
	
}
