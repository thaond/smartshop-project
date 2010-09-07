package com.appspot.smartshop.ui.product.vatgia;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.appspot.smartshop.R;
import com.appspot.smartshop.utils.Global;

public class VatgiaTabActivity extends TabActivity{
	public static final String TAG = "[VatgiaTabActivity]";
	public static final String TAB_VATGIA_COMPANIES = "vatgia_companies";
	public static final String TAB_VATGIA_PRODUCT_DETAIL = "vatgia_product_detail";
	private TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// url of listshop
		VatgiaCompaniesActivity.url = getIntent().getExtras().getString(Global.VATGIA_URL_LIST_SHOP);
		
		tabHost = getTabHost();
		Intent intent;

		// Smartshop products tab
		intent = new Intent(this, VatgiaCompaniesActivity.class);
		tabHost.addTab(tabHost.newTabSpec(TAB_VATGIA_COMPANIES).setIndicator(
				getString(R.string.tab_vatgia_companies)).setContent(intent));

		// Vatgia products tab
		intent = new Intent(this, VatgiaProductDetailActivity.class);
		tabHost.addTab(tabHost.newTabSpec(TAB_VATGIA_PRODUCT_DETAIL).setIndicator(
				getString(R.string.tab_vatgia_product_detail)).setContent(intent));
		
		// current tab is Smartshop products list
		tabHost.setCurrentTab(0);	

		// set up size for tab
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 40;
		}
	}
}
