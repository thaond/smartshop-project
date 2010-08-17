package com.appspot.smartshop.ui.product;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class PostProductActivity extends TabActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		final TabHost tabHost = getTabHost();

		tabHost.addTab(tabHost.newTabSpec("Basic Features").setIndicator(
				"Basic Features").setContent(
				new Intent(this, PostProductActivityBasicAttribute.class)));

		tabHost.addTab(tabHost.newTabSpec("User Define").setIndicator(
				"Advanced Fearture").setContent(
				new Intent(this, PostProductActivityUserDefine.class)));

	}

}
