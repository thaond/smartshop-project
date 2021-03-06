package com.smartshop.docs.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TreePanel extends VerticalPanel {
	private static TreePanel instance = null;

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
		ItemNode itemUserComment = new ItemNode("Ý kiến người sử dụng",
				"user-comment", "/doc/user-comment.html");
		ItemNode itemFeasibility = new ItemNode("Tính khả thi của dự án",
				"feasibility", "/doc/feasibility.html");
		ItemNode itemIdeaTechnology = new ItemNode("Ý tưởng & Công nghệ",
				"idea-technology", "/doc/idea-technology.html");

		// 5. Ứng dụng Android
		Tree treeAndroid = new Tree();
		TreeItem rootAndroid = new TreeItem(
				"Hướng dẫn sử dụng ứng dụng trên Android");
		ItemNode itemInstall = new ItemNode("Download và cài đặt sản phẩm",
				"android-install", "/doc/1_cai_dat_ung_dung.htm");
		ItemNode itemMainForm = new ItemNode("Màn hình chính",
				"android-main-form", "/doc/2_man_hinh_chinh.htm");
		ItemNode itemLoginForm = new ItemNode("Màn hình đăng nhập",
				"android-login-form", "/doc/3_man_hinh_dang_nhap.htm");
		ItemNode itemAndProduct = new ItemNode("Tính năng tìm kiếm sản phẩm",
				"android-product", "/doc/4_tim_kiem_san_pham.htm");
		ItemNode itemAndPage = new ItemNode(
				"Tính năng tìm kiếm các bài viết từ hệ thống SmartShop",
				"android-page", "/doc/5_tim_kiem_bai_viet.htm");
		ItemNode itemUser = new ItemNode("Các tính năng dành cho người dùng",
				"android-user", "/doc/6_chuc_nang_nguoi_dung.htm");
		ItemNode itemNotification = new ItemNode(
				"Tính năng tự động thông báo (Notification)",
				"android-notification", "/doc/7_notification.htm");

		rootAndroid.addItem(itemInstall);
		rootAndroid.addItem(itemMainForm);
		rootAndroid.addItem(itemLoginForm);
		rootAndroid.addItem(itemAndProduct);
		rootAndroid.addItem(itemAndPage);
		rootAndroid.addItem(itemUser);
		rootAndroid.addItem(itemNotification);
		rootAndroid.setState(true);

		treeAndroid.addItem(rootAndroid);

		// 6. Dành cho nhà phát triển
		Tree treeDev = new Tree();
		TreeItem rootDev = new TreeItem("Dành cho nhà phát triển");
		ItemNode itemDevIntro = new ItemNode("Giới thiệu", "dev-intro",
				"/doc/dev-intro.html");
		ItemNode itemDevSignUp = new ItemNode("Đăng ký API Key", "dev-sign-up",
				"/doc/signup_apikey.html");
		ItemNode itemUserAccount = new ItemNode("Thông tin tài khoản",
				"dev-user-account", "/doc/dev-user-account.html");
		ItemNode itemCategory = new ItemNode("Danh mục sản phẩm",
				"dev-categories", "/doc/dev-categories.html");
		ItemNode itemMap = new ItemNode("Các dịch vụ bản đồ", "dev-map",
				"/doc/dev-map.html");
		ItemNode itemSMSEmail = new ItemNode(
				"Các dịch vụ gửi tin nhắn và email", "dev-sms-mail",
				"/doc/dev-sms-mail.html");
		ItemNode itemProduct = new ItemNode("Sản phẩm", "dev-product",
				"/doc/dev-product.html");
		ItemNode itemSubcribe = new ItemNode("Dịch vụ thông tin",
				"dev-subcribe", "/doc/dev-subcribe.html");
		ItemNode itemPage = new ItemNode("Trang thông tin", "dev-page",
				"/doc/dev-page.html");
		ItemNode itemFullServices = new ItemNode("ĐẦY ĐỦ DỊCH VỤ <img src=\"./images/new.png\" border=0 />", "dev-full-services", null);

		rootDev.addItem(itemDevIntro);
		rootDev.addItem(itemDevSignUp);
		rootDev.addItem(itemUserAccount);
		rootDev.addItem(itemProduct);
		rootDev.addItem(itemCategory);
		rootDev.addItem(itemPage);
		rootDev.addItem(itemSubcribe);
		rootDev.addItem(itemMap);
		rootDev.addItem(itemSMSEmail);
		rootDev.addItem(itemFullServices);
		rootDev.setState(true);
		treeDev.addItem(rootDev);

		ItemNode itemProductRoadMap = new ItemNode("Hướng phát triển",
				"roadmap", "/doc/product_roadmap.html");
		ItemNode itemSponsor = new ItemNode("Nhà tài trợ", "sponsor",
				"/doc/sponsor.html");
		ItemNode itemLicense = new ItemNode("Bản quyền", "license",
				"/doc/license.html");

		HTML hr1 = new HTML("<hr/>");
		hr1.setWidth("300px");
		HTML hr2 = new HTML("<hr/>");
		hr2.setWidth("300px");
		HTML hr3 = new HTML("<hr/>");
		hr3.setWidth("300px");

		add(itemIntro);
		add(itemOverview);
		add(itemUserComment);
		add(itemFeasibility);
		add(itemIdeaTechnology);

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
