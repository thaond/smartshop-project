package com.appspot.smartshop;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.appspot.smartshop.ui.page.PagesListActivity;
import com.appspot.smartshop.ui.product.SearchProductActivity;

public class MainActivity extends TabActivity {
	public static final String TAB_LIST_PRODUCTS = "list_products";
	public static final String TAB_LIST_PAGES = "list_pages";
	public static final String TAB_SEARCH_PRODUCTS = "search_products";

	private TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smartshop);
		
		tabHost = getTabHost();
		Intent intent;

		// Products tab
		intent = new Intent(this, SearchProductActivity.class);
		tabHost.addTab(tabHost.newTabSpec(TAB_LIST_PRODUCTS).setIndicator(
				getString(R.string.tab_list_products)).setContent(intent));
		
		// Pages tab
		intent = new Intent(this, PagesListActivity.class);
		tabHost.addTab(tabHost.newTabSpec(TAB_LIST_PAGES).setIndicator(
				getString(R.string.tab_list_pages)).setContent(intent));
		
		// listeners
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				if (tabId.equals(TAB_LIST_PRODUCTS)) {
					loadProductsList();
				} else if (tabId.equals(TAB_LIST_PAGES)) {
					loadPagesList();
				}
			}
		});
	}

	protected void loadPagesList() {
		// TODO (condorhero01): request pages list
	}

	protected void loadProductsList() {
		// TODO (condorehro01): request products list
	}
}
