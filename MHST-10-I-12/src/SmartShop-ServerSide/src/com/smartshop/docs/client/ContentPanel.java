package com.smartshop.docs.client;

import pl.rmalinowski.gwt2swf.client.ui.SWFWidget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartshop.docs.client.rpc.RPCService;
import com.smartshop.docs.client.rpc.RPCServiceAsync;
import com.smartshop.docs.client.utils.ClientUtil;

public class ContentPanel extends VerticalPanel {

	private static ContentPanel instance = null;
	private HTML html;
	private SWFWidget swfWidget;
	private TextBox txtEmail;
	private TextBox txtSource;
	private VerticalPanel pnlSignUpAPIKey;
	private Button btnSignUp;

	private RPCServiceAsync serviceAsync = RPCService.Util.getInstance();

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
		swfWidget.setPixelSize(600, 400);

		pnlSignUpAPIKey = new VerticalPanel();
		pnlSignUpAPIKey.setStyleName("code");
		pnlSignUpAPIKey.setWidth((int) ClientUtil.getScreenWidth() * .7 + "px");
		txtSource = new TextBox();
		txtEmail = new TextBox();
		btnSignUp = new Button("Sinh API Key");

		btnSignUp.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// Get API Key
				serviceAsync.signUpAPIKey(txtSource.getText(), txtEmail
						.getText(), new AsyncCallback<String>() {

					@Override
					public void onSuccess(String result) {
						// Redirect to display API Key
						showAPIKey(result);
					}

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
						Window.alert(caught.getMessage());
					}
				});
			}
		});

		HorizontalPanel pnlSource = new HorizontalPanel();
		pnlSource.addStyleName("sign-up-api");
		HTML html1 = new HTML(
				"Địa chỉ website hoặc nền tảng mà bạn dự định phát triển:");
		html1.setWidth("370px");
		pnlSource.add(html1);
		pnlSource.add(txtSource);

		HorizontalPanel pnlEmail = new HorizontalPanel();
		pnlEmail.addStyleName("sign-up-api");
		HTML html2 = new HTML("Email của bạn:");
		html2.setWidth("370px");
		pnlEmail.add(html2);
		pnlEmail.add(txtEmail);

		HorizontalPanel pnlSignUp = new HorizontalPanel();
		pnlSignUp.addStyleName("sign-up-api");
		HTML html3 = new HTML();
		html3.setWidth("370px");
		pnlSignUp.add(html3);
		pnlSignUp.add(btnSignUp);

		pnlSignUpAPIKey.add(pnlSource);
		pnlSignUpAPIKey.add(pnlEmail);
		pnlSignUpAPIKey.add(pnlSignUp);

		add(html);
		add(swfWidget);
		add(pnlSignUpAPIKey);
	}

	/**
	 * 
	 * @param node
	 * @param isDisplayFlash
	 *            : display flash player in case the user show
	 *            <code>intro</code>
	 */
	public void showData(ItemNode node) {
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

		if (node.getToken().equals("intro"))
			swfWidget.setVisible(true);
		else
			swfWidget.setVisible(false);

		if (node.getToken().equals("dev-sign-up"))
			pnlSignUpAPIKey.setVisible(true);
		else
			pnlSignUpAPIKey.setVisible(false);
	}

	public void showAPIKey(final String apiKey) {
		if (apiKey.equals("")){
			Window.alert("Không thể lấy API Key. Xin vui lòng thử lại sau.");
			return;
		}
		
		pnlSignUpAPIKey.setVisible(false);	
		RequestBuilder rb = new RequestBuilder(RequestBuilder.GET, "/doc/show_apikey.html");
		try {
			rb.sendRequest(null, new RequestCallback() {
				public void onError(final Request request, final Throwable e) {
					e.printStackTrace();
				}

				public void onResponseReceived(final Request request,
						final Response response) {
					html.setHTML(response.getText().replaceAll("APIKEY", apiKey));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
