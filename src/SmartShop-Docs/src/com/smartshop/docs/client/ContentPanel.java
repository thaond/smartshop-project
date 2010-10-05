package com.smartshop.docs.client;

import pl.rmalinowski.gwt2swf.client.ui.SWFWidget;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ContentPanel extends VerticalPanel {

	private static ContentPanel instance = null;
	private HTML html;
	private SWFWidget swfWidget;

	public static ContentPanel getInstance() {
		if (instance == null) {
			instance = new ContentPanel();
		}

		return instance;
	}

	private ContentPanel() {
		html = new HTML();
		swfWidget = new SWFWidget("res/SmartShop Introduction.swf");
		swfWidget.addStyleName("flash-intro");
		swfWidget.setPixelSize(800, 600);

		add(html);
		add(swfWidget);
	}

	public void showData(ItemNode node, boolean isDisplayFlash) {
		RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, node
				.getFilePath());
		try {
			rb.sendRequest(null, new RequestCallback() {
				public void onError(final Request request, final Throwable e) {
					e.printStackTrace();
				}

				public void onResponseReceived(final Request request,
						final Response response) {
					html.setHTML(response.getText());
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		swfWidget.setVisible(isDisplayFlash);
	}

}
