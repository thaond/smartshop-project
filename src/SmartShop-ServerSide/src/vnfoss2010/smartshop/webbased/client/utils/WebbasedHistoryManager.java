package vnfoss2010.smartshop.webbased.client.utils;

import vnfoss2010.smartshop.webbased.client.LoadingDialog;
import vnfoss2010.smartshop.webbased.client.RelatedProductsPanel;
import vnfoss2010.smartshop.webbased.client.UserProfileLeftPanel;
import vnfoss2010.smartshop.webbased.client.ViewProductPanel;
import vnfoss2010.smartshop.webbased.client.WebbasedService;
import vnfoss2010.smartshop.webbased.client.WebbasedServiceAsync;
import vnfoss2010.smartshop.webbased.share.WMedia;
import vnfoss2010.smartshop.webbased.share.WPage;
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
	 * Handle history token, form: product;{id} or page;{id} 
	 * (Ex: product;12 or page;123)
	 * 
	 * @param value
	 */
	private void onHistoryChange(String value) {
		String[] arr = value.split(";");
		System.out.println(value);
		if (arr.length == 2) {
			String type = arr[0];
			LoadingDialog.getInstance().setVisible(true);
			int id = Integer.parseInt(arr[1]);
			if (type.equals("product")){
				System.out.println("Show Product + " + id);
				service.getProduct(id, new AsyncCallback<WProduct>() {

					@Override
					public void onSuccess(WProduct result) {
						// TODO Testing with localhost
						if (result != null) {
							result.lat = 10.123;
							result.lng = 106.43;
							if (!WebbasedUtils
									.isEmptyOrNull(result.userInfo.avatarLink))
								result.userInfo.avatarLink = result.userInfo.avatarLink
										.replaceAll("10.0.2.2", "localhost");
							for (WMedia media : result.setMedias) {
								media.link = media.link.replaceAll("10.0.2.2",
										"localhost");
							}
							for (WProduct product : result.listRelatedProduct) {
								for (WMedia media : product.setMedias) {
									media.link = media.link.replaceAll("10.0.2.2",
											"localhost");
								}
							}
							System.out.println(result);
						}
						///////////////////////////////////
						RelatedProductsPanel.getInstance().setVisible(true);
						if (result == null){
							UserProfileLeftPanel.getInstance().clearData();
							RelatedProductsPanel.getInstance().clearData();
						}else{
							UserProfileLeftPanel.getInstance().showData(
									result.userInfo);
							RelatedProductsPanel.getInstance().showData(
									result.listRelatedProduct);
						}
						ViewProductPanel.getInstance().showProduct(result);
						LoadingDialog.getInstance().setVisible(false);
					}

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
						LoadingDialog.getInstance().setVisible(false);
					}
				});
			}else if (type.equals("page")){
				System.out.println("Show Page + " + id);
				service.getPage(id, new AsyncCallback<WPage>() {

					@Override
					public void onSuccess(WPage result) {
						// TODO Testing with localhost
//						if (result != null) {
//							if (!WebbasedUtils
//									.isEmptyOrNull(result.userInfo.avatarLink))
//								result.userInfo.avatarLink = result.userInfo.avatarLink
//										.replaceAll("10.0.2.2", "localhost");
//							System.out.println(result);
//						}
						///////////////////////////////////

						RelatedProductsPanel.getInstance().setVisible(false);
						if (result == null){
							UserProfileLeftPanel.getInstance().clearData();
						}else{
							UserProfileLeftPanel.getInstance().showData(
									result.userInfo);
						}
						ViewProductPanel.getInstance().showPage(result);
						LoadingDialog.getInstance().setVisible(false);
					}

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
						LoadingDialog.getInstance().setVisible(false);
					}
				});
			}
		}
	}

}
