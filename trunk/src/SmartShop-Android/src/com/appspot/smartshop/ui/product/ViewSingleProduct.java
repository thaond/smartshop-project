package com.appspot.smartshop.ui.product;

import com.appspot.smartshop.mock.MockProductInfo;
import com.appspot.smartshop.utils.Global;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;

public class ViewSingleProduct extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final TabHost tabHost = getTabHost();
		// TODO:condohero01 load data of product from server and put them into
		// basic attribute and advance attribute
		Intent basicIntent = new Intent(this, ViewBasicAttributeOfProduct.class);
		basicIntent
				.putExtra(Global.PRODUCT_INFO, MockProductInfo.getInstance());
		Log.d("Test", "Tab is not created");

		tabHost.addTab(tabHost.newTabSpec("Basic").setIndicator(
				"Basic Features").setContent(basicIntent));
		tabHost.addTab(tabHost.newTabSpec("User Define").setIndicator(
				"Advanced Fearture").setContent(
				new Intent(this, ViewAdvanceAttributeOfProduct.class)));
		Log.d("Test", "Tab is created");
	}

}
