package vnfoss2010.smartshop.webbased.client;

import vnfoss2010.smartshop.serverside.database.entity.UserInfo;
import vnfoss2010.smartshop.webbased.client.utils.WebbasedHistoryManager;
import vnfoss2010.smartshop.webbased.client.utils.WebbasedUtils;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
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

	public void onModuleLoad() {

		// UserProfileLeftPanel userProfileLeftPanel =
		// UserProfileLeftPanel.getInstance();
		// RootPanel.get().add(userProfileLeftPanel);
		
		linkLogin = new HTML("erwerwer");
		linkLogin.addStyleName("lbl-login");
		
		lblUser = new Label("ưterterte");
		lblUser.addStyleName("lbl-user");
		
		pnlHHeader = new HorizontalPanel();
		pnlHHeader.addStyleName("panel-header");
		pnlHHeader.setWidth((Window.getClientWidth()-25) + "px");
		pnlHHeader.add(linkLogin);
		pnlHHeader.add(lblUser);

		HorizontalSplitPanel hSplit = new HorizontalSplitPanel();
		hSplit.ensureDebugId("cwHorizontalSplitPanel");
		hSplit.setSize(WebbasedUtils.getScreenWidth() +"px", WebbasedUtils.getScreenHeight() + "px");
		hSplit.setSplitPosition("20%");

		// Add some content
		String randomText = "Trong lịch sử 60 năm của Miss World, chưa từng có người đẹp Nauy nào đăng quang. Nhưng năm nay, càng về cuối cuộc thi, cô gái Mariann Birkedal đến từ đất nước Bắc Âu càng được nhiều người đặt cược đoạt danh hiệu này. Theo nhà cái William Hill (Anh), Mariann Birkedal dẫn đầu với tỷ lệ cược 4 ăn 5. Còn ở nhà cái Ladbrokes (Anh), cô cũng được tỷ lệ cược cao nhất là 10 ăn 11.";
		for (int i = 0; i < 2; i++) {
			randomText += randomText;
		}
		hSplit.setLeftWidget(UserProfileLeftPanel.getInstance());
		hSplit.setRightWidget(ViewProductPanel.getInstance());

		//Add History Listener
		History.addValueChangeHandler(WebbasedHistoryManager.getInstance());
		History.fireCurrentHistoryState();

		// Return the content
		pnlMain = new VerticalPanel();
		pnlMain.add(pnlHHeader);
		pnlMain.add(hSplit);
		
		RootPanel.get().add(pnlMain);

	}
}
