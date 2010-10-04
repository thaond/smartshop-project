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
		ItemNode itemIntro = new ItemNode("Giới thiệu", "intro",
				"/doc/introduction.html");
		ItemNode itemOverview = new ItemNode("Tổng quan hệ thống", "overview",
				"/doc/overview.html");
		ItemNode itemProductRoadMap = new ItemNode("Hướng phát triển",
				"roadmap", "/doc/product_roadmap.html");
		ItemNode itemSponsor = new ItemNode("Nhà tài trợ", "sponsor",
				"/doc/sponsor.html");
		ItemNode itemLicense = new ItemNode("Bản quyền", "license",
				"/doc/license.html");

		// 5. Ứng dụng Android
		Tree treeAndroid = new Tree();
		TreeItem rootAndroid = new TreeItem("Ứng dụng Android");
		ItemNode itemIntroAnd = new ItemNode("Giới thiệu sản phẩm",
				"android-intro", "/doc/android-intro.html");
		ItemNode itemDownload = new ItemNode("Download", "android-download",
				"/doc/android-download.html");
		TreeItem itemGuide = new TreeItem("Hướng dẫn sử dụng");

		rootAndroid.addItem(itemIntroAnd);
		rootAndroid.addItem(itemDownload);
		rootAndroid.addItem(itemGuide);
		treeAndroid.addItem(rootAndroid);

		// 6. Dành cho nhà phát triển
		Tree treeDev = new Tree();
		TreeItem rootDev = new TreeItem("Dành cho nhà phát triển");
		ItemNode itemDevIntro = new ItemNode("Giới thiệu", "intro-dev",
				"/doc/dev-intro.html");
		ItemNode itemDevSignUp = new ItemNode("Đăng ký API Key", "dev-sign-up",
				"/doc/signup_apikey.html");

		rootDev.addItem(itemDevIntro);
		rootDev.addItem(itemDevSignUp);
		treeDev.addItem(rootDev);

		HTML hr1 = new HTML("<hr/>");
		hr1.setWidth("300px");
		HTML hr2 = new HTML("<hr/>");
		hr2.setWidth("300px");
		HTML hr3 = new HTML("<hr/>");
		hr3.setWidth("300px");

		add(itemIntro);
		add(itemOverview);

		add(hr1);
		add(treeAndroid);
		
		add(hr2);
		add(treeDev);

		add(hr3);
		add(itemProductRoadMap);
		add(itemSponsor);
		add(itemLicense);
	}
}
