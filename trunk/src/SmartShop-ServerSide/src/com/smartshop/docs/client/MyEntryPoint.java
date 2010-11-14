package com.smartshop.docs.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MyEntryPoint implements EntryPoint{
	@Override
	public void onModuleLoad() {
		RootPanel.get().add(MainPanel.getInstance());
		
		History.addValueChangeHandler(HistoryManager.getInstance());
		History.fireCurrentHistoryState();
		
		//RootPanel.get().add(FullServicesPanel.getInstance());
	}

	void testImage() {
		Image image = new Image("images/first.png");
		RootPanel.get().add(image);
	}

	void testMain() {
		HorizontalSplitPanel main = new HorizontalSplitPanel();
		main.setSplitPosition("300px");

		main.setLeftWidget(TreePanel.getInstance());
		main.setRightWidget(ContentPanel.getInstance());

		RootPanel.get().add(main);
	}

	void testSplitPanel() {
		HorizontalSplitPanel panel = new HorizontalSplitPanel();
		panel.setSplitPosition("300px");

		panel.setLeftWidget(createTree());
		panel.setRightWidget(new Button("right"));

		RootPanel.get().add(panel);

	}

	void testContent() {
		VerticalPanel panel = new VerticalPanel();

		Label label = new Label("This is a label");
		label.getElement().getStyle().setBackgroundColor("blue");
		label.getElement().getStyle().setFontSize(30.0, Unit.PX);
		label.getElement().getStyle()
				.setWidth(Window.getClientWidth(), Unit.PX);

		HTML html = new HTML("The content");

		panel.add(label);
		panel.add(html);
		panel.add(label);
		panel.add(html);

		RootPanel.get().add(panel);
	}

	VerticalPanel createContentPanel() {
		VerticalPanel panel = new VerticalPanel();

		Label label = new Label("This is a label");
		label.getElement().getStyle().setBackgroundColor("blue");
		label.getElement().getStyle().setFontSize(30.0, Unit.PX);
		label.getElement().getStyle().setWidth(Window.getClientWidth() - 310,
				Unit.PX);

		HTML html = new HTML("The content");

		panel.add(label);
		panel.add(html);

		return panel;
	}

	VerticalPanel createTree() {
		VerticalPanel panel = new VerticalPanel();

		Label label = new Label("Header");
		DOM.setElementAttribute(label.getElement(), "font-size", "30");

		// Create a tree with a few items in it.
		TreeItem root = new TreeItem("root");
		root.addItem("item0");
		root.addItem("item1");
		root.addItem("item2");

		// Add a CheckBox to the tree
		TreeItem item = new TreeItem(new CheckBox("item3"));
		root.addItem(item);

		// Add a link
		TreeItem link = new TreeItem(new Anchor("link", "http://google.com.vn"));
		root.addItem(link);

		// add a tree item
		TreeItem subTree = new TreeItem("sub");
		subTree.addItem("one");
		subTree.addItem("two");
		root.addItem(subTree);

		Tree t = new Tree();
		t.addItem(root);

		// Add it to the root panel.
		panel.add(label);
		panel.add(t);

		return panel;
	}

	void testTree() {
		VerticalPanel panel = new VerticalPanel();

		Label label = new Label("Header");
		DOM.setElementAttribute(label.getElement(), "font-size", "30");

		// Create a tree with a few items in it.
		TreeItem root = new TreeItem("root");
		root.addItem("item0");
		root.addItem("item1");
		root.addItem("item2");

		// Add a CheckBox to the tree
		TreeItem item = new TreeItem(new CheckBox("item3"));
		root.addItem(item);

		// Add a link
		TreeItem link = new TreeItem(new Anchor("link", "http://google.com.vn"));
		root.addItem(link);

		// add a tree item
		TreeItem subTree = new TreeItem("sub");
		subTree.addItem("one");
		subTree.addItem("two");
		root.addItem(subTree);

		Tree t = new Tree();
		t.addItem(root);

		// Add it to the root panel.
		panel.add(label);
		panel.add(t);
		RootPanel.get().add(panel);
	}
}
