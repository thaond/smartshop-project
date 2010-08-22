package com.appspot.smartshop.ui.product;

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
		Log.d("Test", "Tab is not created");
		tabHost.addTab(tabHost.newTabSpec("Basic").setIndicator(
				"Basic Features").setContent(
				new Intent(this, ViewBasicAttributeOfProduct.class)));

		tabHost.addTab(tabHost.newTabSpec("User Define").setIndicator(
				"Advanced Fearture").setContent(
				new Intent(this, ViewAdvanceAttributeOfProduct.class)));
		Log.d("Test", "Tab is created");
	}

}
