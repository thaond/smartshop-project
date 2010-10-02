package com.smartshop.docs.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MyTreePanel extends VerticalPanel {

	public MyTreePanel() {
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
		Anchor anchor = new Anchor("CLICK THIS LINK");
		anchor.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				MyContentPanel.getInstance().showMockNewContent();
			}
		});
		TreeItem link = new TreeItem(anchor);
		root.addItem(link);

		// add a tree item
		TreeItem subTree = new TreeItem("sub");
		subTree.addItem("one");
		subTree.addItem("two");
		root.addItem(subTree);

		Tree t = new Tree();
		t.addItem(root);

		// Add it to the root panel.
		add(label);
		add(t);
	}
}
