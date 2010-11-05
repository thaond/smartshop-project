package vnfoss2010.smartshop.webbased.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * @author VoMinhTam
 */
public class LoadingDialog extends PopupPanel {
	private static LoadingDialog instance = null;

	public LoadingDialog() {
		add(new HTML("<i>Loading...</i>"));
	}

	public static LoadingDialog getInstance() {
		if (instance == null)
			instance = new LoadingDialog();
		return instance;
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		
		if (visible){
			setModal(true);
			center();
			show();
		}else{
			setModal(false);
		}
	}
}
