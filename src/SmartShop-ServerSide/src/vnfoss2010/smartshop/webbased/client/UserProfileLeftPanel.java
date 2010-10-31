package vnfoss2010.smartshop.webbased.client;

import org.mortbay.jetty.handler.ErrorHandler;

import vnfoss2010.smartshop.webbased.client.utils.WebbasedUtils;
import vnfoss2010.smartshop.webbased.share.WUserInfo;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LoadListener;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author VoMinhTam
 */
public class UserProfileLeftPanel extends VerticalPanel {
	private static UserProfileLeftPanel instance = null;
	private Image imgAvatar;
	private HTML lblUsername;
	private HTML lblName;
	private HTML lblEmail;
	private HTML htmlStar;
	private String STRING_STAR = "<b>Đánh giá: </b>"
			+ "_1 <img src=\"./webbased/star.png\"/> (_2/_3)";

	public static final String URL_NO_AVATAR = "./webbased/no_avatar.png";

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

		imgAvatar = new Image(URL_NO_AVATAR);
		imgAvatar.setStyleName("img-avatar");
		imgAvatar.addLoadListener(new LoadListener() {
			
			@Override
			public void onLoad(Widget sender) {
			}
			
			@Override
			public void onError(Widget sender) {
				imgAvatar.setUrl(URL_NO_AVATAR);
			}
		});
		lblUsername = new HTML("Username");
		lblName = new HTML("Name");
		lblEmail = new HTML("Email");
		htmlStar = new HTML();

		vPanel.add(imgAvatar);
		vPanel.add(lblUsername);
		vPanel.add(lblName);
		vPanel.add(lblEmail);
		vPanel.add(htmlStar);
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		add(vPanel);

		setSize(WebbasedUtils.getScreenWidth() * .19 + "px", WebbasedUtils
				.getScreenHeight()
				- 30 + "px");
		// setStyleName("user-profile-left-panel");
	}

	public void showData(WUserInfo userInfo) {
		if (WebbasedUtils.isEmptyOrNull(userInfo.avatarLink))
			imgAvatar.setUrl(URL_NO_AVATAR);
		else
			imgAvatar.setUrl(userInfo.avatarLink);

		lblUsername.setHTML("<b>Username</b>: " + userInfo.username);
		lblName.setHTML("<b>Name</b>: " + userInfo.first_name + " "
				+ userInfo.last_name);
		lblEmail.setHTML("<b>Email</b>: " + userInfo.email);
		double aver = 0;
		try {
			aver = userInfo.sum_star / userInfo.count_vote;
		} catch (Exception e) {
		}

		String strAver = NumberFormat.getFormat("0.0").format(aver);
		htmlStar.setHTML(STRING_STAR.replaceAll("_1", strAver + "").replaceAll(
				"_2", userInfo.sum_star + "").replaceAll("_3",
				userInfo.count_vote + ""));
	}
}
