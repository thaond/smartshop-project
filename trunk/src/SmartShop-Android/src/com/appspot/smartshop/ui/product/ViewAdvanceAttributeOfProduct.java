package com.appspot.smartshop.ui.product;

import java.util.LinkedList;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.UserDefineAttributeAdapter;
import com.appspot.smartshop.dom.UserDefineAttribute;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ViewAdvanceAttributeOfProduct extends Activity {
	public UserDefineAttributeAdapter adapter;
	public ListView listAttribute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_advance_attribute_of_product);
		listAttribute = (ListView) findViewById(R.id.listAdvanceAttributeOfProduct);
		adapter = new UserDefineAttributeAdapter(this,
				R.layout.advance_attribute_of_product_item,
				new LinkedList<UserDefineAttribute>());
		listAttribute.setAdapter(adapter);
	}

}
