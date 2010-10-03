package com.smartshop.docs.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ContentPanel extends VerticalPanel {

	private static ContentPanel instance = null;
	private HTML html;

	public static ContentPanel getInstance() {
		if (instance == null) {
			instance = new ContentPanel();
		}

		return instance;
	}

	private ContentPanel() {
		html = new HTML(
				"<a href=\"http://google-web-toolkit.googlecode.com/svn/javadoc/2.0/com/google/gwt/user/client/ui/RootLayoutPanel.html\">RootLayoutPanel</a></h3><p>This panel is a singleton that serves as a root container to which all otherlayout panels should be attached (see RequiresResize and ProvidesResize <a href=\"#Resize\">below</a> for details). It extends<a href=\"http://google-web-toolkit.googlecode.com/svn/javadoc/2.0/com/google/gwt/user/client/ui/LayoutPanel.html\">LayoutPanel</a>, and thus you can position any number of children witharbitrary constraints.</p><p>You most commonly use <a href=\"http://google-web-toolkit.googlecode.com/svn/javadoc/2.0/com/google/gwt/user/client/ui/RootLayoutPanel.html\">RootLayoutPanel</a> as a container for another panel, as inthe following snippet, which causes a <a href=\"http://google-web-toolkit.googlecode.com/svn/javadoc/2.0/com/google/gwt/user/client/ui/DockLayoutPanel.html\">DockLayoutPanel</a> to fill the browser'sclient area:</p><img src=\"images/first.png\">");
		add(html);
	}

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
	}
}
