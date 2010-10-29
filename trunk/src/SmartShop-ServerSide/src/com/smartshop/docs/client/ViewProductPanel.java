package vnfoss2010.smartshop.webbased.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author VoMinhTam
 */
public class ViewProductPanel extends VerticalPanel {
	private static ViewProductPanel instance = null;

	// TODO
	// public static User

	public static ViewProductPanel getInstance() {
		if (instance == null)
			instance = new ViewProductPanel();

		return instance;
	}

	private ViewProductPanel() {
		initUI();
	}

	private void initUI() {
		Label lblTitle = new Label("Title");
		lblTitle.setStyleName("content-title");
		
		
	}
}
