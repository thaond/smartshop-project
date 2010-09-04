package com.appspot.smartshop.ui.product;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.appspot.smartshop.R;

public class PostProductActivity extends TabActivity {
	public static final String PRODUCT_BASIC_INFO = "basic";
	public static final String PRODUCT_USER_DEFINED_INFO = "user_defined";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final TabHost tabHost = getTabHost();

		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 40;
		}
		
		tabHost.addTab(tabHost.newTabSpec(PRODUCT_BASIC_INFO).setIndicator(
				getString(R.string.product_basic_info)).setContent(
				new Intent(this, ProductBasicAttributeActivity.class)));
		tabHost.addTab(tabHost.newTabSpec(PRODUCT_USER_DEFINED_INFO).setIndicator(
				getString(R.string.product_user_defined_info)).setContent(
				new Intent(this, ProductUserDefinedAttributeActivity.class)));
	}
}
