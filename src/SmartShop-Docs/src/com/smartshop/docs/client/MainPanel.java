package com.smartshop.docs.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MainPanel extends FlowPanel {

	public MainPanel() {
		
		// title panel
		Label label = new Label("This is a label");
		label.getElement().getStyle().setBackgroundColor("green");
		label.getElement().getStyle().setFontSize(40.0, Unit.PX);
		label.getElement().getStyle().setWidth(Window.getClientWidth(), Unit.PX);
		
		// main panel
		HorizontalSplitPanel main = new HorizontalSplitPanel();
		main.setSplitPosition("300px");
		
		main.setLeftWidget(new MyTreePanel());
		main.setRightWidget(MyContentPanel.getInstance());
		
		// add to root
		add(label);
		add(main);
	}
}
