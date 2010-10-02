package com.smartshop.docs.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MyContentPanel extends VerticalPanel {
	
	private static MyContentPanel instance = null;
	private Label label;
	private HTML html;
	
	public static MyContentPanel getInstance() {
		if (instance == null) {
			instance = new MyContentPanel();
		}
		
		return instance;
	}

	private MyContentPanel() {
		label = new Label("This is a label");
		label.getElement().getStyle().setBackgroundColor("blue");
		label.getElement().getStyle().setFontSize(30.0, Unit.PX);
		label.getElement().getStyle().setWidth(Window.getClientWidth() - 310, Unit.PX);
		
		html = new HTML("<a href=\"http://google-web-toolkit.googlecode.com/svn/javadoc/2.0/com/google/gwt/user/client/ui/RootLayoutPanel.html\">RootLayoutPanel</a></h3><p>This panel is a singleton that serves as a root container to which all otherlayout panels should be attached (see RequiresResize and ProvidesResize <a href=\"#Resize\">below</a> for details). It extends<a href=\"http://google-web-toolkit.googlecode.com/svn/javadoc/2.0/com/google/gwt/user/client/ui/LayoutPanel.html\">LayoutPanel</a>, and thus you can position any number of children witharbitrary constraints.</p><p>You most commonly use <a href=\"http://google-web-toolkit.googlecode.com/svn/javadoc/2.0/com/google/gwt/user/client/ui/RootLayoutPanel.html\">RootLayoutPanel</a> as a container for another panel, as inthe following snippet, which causes a <a href=\"http://google-web-toolkit.googlecode.com/svn/javadoc/2.0/com/google/gwt/user/client/ui/DockLayoutPanel.html\">DockLayoutPanel</a> to fill the browser'sclient area:</p><img src=\"images/first.png\">");
		
		add(label);
		add(html);
	}
	
	void showMockNewContent() {
		label.setText("new title");
		html.setHTML("new content");
	}
}
