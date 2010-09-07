package com.appspot.smartshop.ui.product;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.appspot.smartshop.R;
import com.appspot.smartshop.ui.product.vatgia.SearchVatgiaActivity;

public class SearchProductsTabActivity extends TabActivity {
	public static final String TAG = "[SearchProductsTabActivity]";
	public static final String TAB_SMARTSHOP_PRODUCTS = "smartshop_products";
	public static final String TAB_VATGIA_PRODUCTS = "vatgia_products";
	private TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.smartshop);

		tabHost = getTabHost();
		Intent intent;

		// Smartshop products tab
		intent = new Intent(this, ProductsListActivity.class);
		tabHost.addTab(tabHost.newTabSpec(TAB_SMARTSHOP_PRODUCTS).setIndicator(
				getString(R.string.tab_smartshop_products)).setContent(intent));

		// Vatgia products tab
		intent = new Intent(this, SearchVatgiaActivity.class);
		tabHost.addTab(tabHost.newTabSpec(TAB_VATGIA_PRODUCTS).setIndicator(
				getString(R.string.tab_vatgia_products)).setContent(intent));
		
		// current tab is Smartshop products list
		tabHost.setCurrentTab(0);	

		// listeners
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				if (tabId.equals(TAB_SMARTSHOP_PRODUCTS)) {
					Log.d(TAG, "tab smartshop products");
				} else if (tabId.equals(TAB_VATGIA_PRODUCTS)) {
					Log.d(TAG, "tab vatgia products");
				}
			}
		});
		// set up size for tab
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 40;
		}
	}
}
