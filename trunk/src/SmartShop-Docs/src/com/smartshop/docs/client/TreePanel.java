package com.smartshop.docs.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartshop.docs.client.utils.ClientUtil;

public class TreePanel extends VerticalPanel {
	private static TreePanel instance = null;
	private List<TreeItem> listRoot = new ArrayList<TreeItem>();

	public static TreePanel getInstance() {
		if (instance == null)
			instance = new TreePanel();
		return instance;
	}

	private TreePanel() {
		ItemNode itemIntro = new ItemNode("Giới thiệu", "intro", "/doc/introduction.html");
		ItemNode itemOverview = new ItemNode("Tổng quan hệ thống", "overview", "/doc/overview.html");
		ItemNode itemProductRoadMap = new ItemNode("Hướng phát triển", "roadmap", "/doc/product_roadmap.html");
		
		// 5. Ứng dụng Android
		Tree tree = new Tree();
		TreeItem treeAndroid = new TreeItem("Ứng dụng Android");
		ItemNode itemIntroAnd = new ItemNode("Giới thiệu sản phẩm", "android-intro", "/doc/android-intro.html");
		ItemNode itemDownload = new ItemNode("Download", "android-download", "/doc/android-download.html");
		TreeItem itemGuide = new TreeItem("Hướng dẫn sử dụng");

		treeAndroid.addItem(itemIntroAnd);
		treeAndroid.addItem(itemDownload);
		treeAndroid.addItem(itemGuide);
		tree.addItem(treeAndroid);

		// Add into map to manage history token
		
		ClientUtil.mapNode.put("intro", itemIntro);
		ClientUtil.mapNode.put("overview", itemOverview);
		ClientUtil.mapNode.put("roadmap", itemProductRoadMap);

		HTML hr = new HTML("<hr/>");
		hr.setWidth("300px");
		
		add(itemIntro);
		add(itemOverview);
		add(hr);
		add(tree);
		add(hr);
		add(itemProductRoadMap);
	}
}
