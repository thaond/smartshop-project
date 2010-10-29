package vnfoss2010.smartshop.webbased.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebBasedEntryPoint implements EntryPoint {
	public void onModuleLoad() {
		
		UserProfileLeftPanel userProfileLeftPanel = UserProfileLeftPanel.getInstance();
		RootPanel.get().add(userProfileLeftPanel);
	}
}
