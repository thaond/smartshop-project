package vnfoss2010.smartshop.webbased.client.utils;

import vnfoss2010.smartshop.webbased.client.UserProfileLeftPanel;
import vnfoss2010.smartshop.webbased.client.ViewProductPanel;
import vnfoss2010.smartshop.webbased.client.WebbasedService;
import vnfoss2010.smartshop.webbased.client.WebbasedServiceAsync;
import vnfoss2010.smartshop.webbased.share.WMedia;
import vnfoss2010.smartshop.webbased.share.WProduct;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author VoMinhTam
 */
public class WebbasedHistoryManager implements ValueChangeHandler<String> {
	private static WebbasedHistoryManager instance = null;

	protected WebbasedServiceAsync service = WebbasedService.Util.getInstance();

	/**
	 * You should use this method to create an instance of this class.<br/>
	 * <b>Note:</b> <code>Singleton</code> is used here
	 * 
	 * @return
	 */
	public static WebbasedHistoryManager getInstance() {
		if (instance == null)
			instance = new WebbasedHistoryManager();
		return instance;
	}

	private WebbasedHistoryManager() {
	}

	@Override
	public void onValueChange(ValueChangeEvent<String> event) {
		System.out.println(event.getValue());
		onHistoryChange(event.getValue());
	}

	/**
	 * Handle history token. Ex: product;<id>
	 * 
	 * @param value
	 */
	private void onHistoryChange(String value) {
		String[] arr = value.split(";");
		if (arr.length >= 2) {
			String type = arr[0];
			int id = Integer.parseInt(arr[1]);
			
			System.out.println(value);

			service.getProduct(id, new AsyncCallback<WProduct>() {

				@Override
				public void onSuccess(WProduct result) {
					//TODO Testing with localhost
					result.lat = 10.123;
					result.lng = 106.43;
					result.userInfo.avatarLink = result.userInfo.avatarLink.replaceAll("10.0.2.2", "localhost");
					for (WMedia media : result.setMedias){
						media.link = media.link.replaceAll("10.0.2.2", "localhost");
					}
					System.out.println(result);
					
					UserProfileLeftPanel.getInstance()
							.showData(result.userInfo);
					ViewProductPanel.getInstance().showData(result);
				}

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}
			});
		}
	}

}
