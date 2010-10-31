package vnfoss2010.smartshop.webbased.client;

import vnfoss2010.smartshop.webbased.client.utils.WebbasedHistoryManager;
import vnfoss2010.smartshop.webbased.client.utils.WebbasedUtils;
import vnfoss2010.smartshop.webbased.share.WGoogleUser;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebBasedEntryPoint implements EntryPoint {
	private HorizontalPanel pnlHHeader;
	private HTML linkLogin;
	private Label lblUser;
	private VerticalPanel pnlMain;

	private WebbasedServiceAsync serviceAsync = WebbasedService.Util
			.getInstance();

	public void onModuleLoad() {
		linkLogin = new HTML();
		linkLogin.addStyleName("lbl-login");

		lblUser = new Label();
		lblUser.addStyleName("lbl-user");

		pnlHHeader = new HorizontalPanel();
		pnlHHeader.addStyleName("panel-header");
		pnlHHeader.setWidth((Window.getClientWidth() - 25) + "px");
		pnlHHeader.add(linkLogin);
		pnlHHeader.add(lblUser);

		initOther();

		HorizontalSplitPanel hSplit = new HorizontalSplitPanel();
		int width = WebbasedUtils.getScreenWidth();
		int height = WebbasedUtils.getScreenHeight() - 30;
		hSplit.setSize(width + "px", height + "px");
		hSplit.setSplitPosition("20%");

		HorizontalPanel pnlContent = new HorizontalPanel();
		ViewProductPanel.getInstance().setWidth(width * .57 + "px");
		pnlContent.add(ViewProductPanel.getInstance());
		RelatedProductsPanel.getInstance().setWidth(width * .2 + "px");
		pnlContent.add(RelatedProductsPanel.getInstance());

		hSplit.setLeftWidget(UserProfileLeftPanel.getInstance());
		hSplit.setRightWidget(pnlContent);

		// Add History Listener
		History.addValueChangeHandler(WebbasedHistoryManager.getInstance());
		History.fireCurrentHistoryState();

		// Return the content
		pnlMain = new VerticalPanel();
		pnlMain.add(pnlHHeader);
		pnlMain.add(hSplit);

		RootPanel.get().add(pnlMain);

	}

	private void initOther() {
		serviceAsync.getGoogleAccountLink(new AsyncCallback<WGoogleUser>() {

			@Override
			public void onSuccess(WGoogleUser result) {
				if (result.isLogin) {
					lblUser.setText(result.email);
					linkLogin.setHTML("<a href=\'" + result.linkLogout
							+ "\'>Log out" + "<a/>");
				} else {
					lblUser.setText("");
					linkLogin.setHTML("<a href=\'" + result.linkLogin
							+ "\'>Log in" + "<a/>");
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}
}
