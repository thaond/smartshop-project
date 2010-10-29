package vnfoss2010.smartshop.webbased.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author VoMinhTam
 */
public class UserProfileLeftPanel extends VerticalPanel {
	private static UserProfileLeftPanel instance = null;
	
	//TODO
//	public static User

	public static UserProfileLeftPanel getInstance() {
		if (instance == null)
			instance = new UserProfileLeftPanel();

		return instance;
	}

	private UserProfileLeftPanel() {
		initUI();
	}

	private void initUI() {
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setSpacing(5);

		Image imgAvatar = new Image("./images/no_avatar.png");
		Label lblUsername = new Label("Username");
		Label lblName = new Label("Name");
		Label lblEmail = new Label("Email");
		String tmp = "%.1f <img href=\"./star.png\"/> (%d/%d)";
		HTML htmlStar = new HTML("2.1 <img src=\"./images/star.png\"/> (10/2)");

		vPanel.add(imgAvatar);
		vPanel.add(lblUsername);
		vPanel.add(lblName);
		vPanel.add(lblEmail);
		vPanel.add(htmlStar);
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		add(vPanel);

		setSize("250px", "700px");
		setStyleName("user-profile-left-panel");
	}

}
