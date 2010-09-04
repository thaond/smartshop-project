package com.appspot.smartshop.ui.product;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.AttributeAdapter;
import com.appspot.smartshop.dom.Attribute;

public class ViewProductUserDefinedAttributeActivity extends Activity {
	public static final String TAG = "[ViewProductUserDefinedAttributeActivity]";
	protected static Set<Attribute> setAttributes = new HashSet<Attribute>();

	private ListView listAttributes;
	private AttributeAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "view product userdefined attribute");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_user_defined_attribute);
		
		Button btnAddNewAttribute = (Button) findViewById(R.id.btnAddAttribute);
		if (ViewProductActivity.canEditProductInfo != null && ViewProductActivity.canEditProductInfo == true) {
			btnAddNewAttribute.setVisibility(View.GONE);
		}
		
		listAttributes = (ListView) findViewById(R.id.listAttributes);
		adapter = new AttributeAdapter(this, 0, new LinkedList<Attribute>(setAttributes));
		listAttributes.setAdapter(adapter);
	}
}
