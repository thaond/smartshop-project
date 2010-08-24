package com.appspot.smartshop.ui.product;

import java.util.LinkedList;
import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.UserDefineAttributeAdapter;
import com.appspot.smartshop.adapter.ViewAdvanceAttributeOfProductAdapter;
import com.appspot.smartshop.dom.UserDefineAttribute;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RadioButton;

public class ViewAdvanceAttributeOfProduct extends Activity {
	public UserDefineAttributeAdapter adapter;
	public ListView listAttribute;
	public ViewAdvanceAttributeOfProductAdapter productAdapter;
	public RadioButton btnRadioLatest, btnBestSeller, btnPrice;
	public ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinkedList<UserDefineAttribute> linkedList = new LinkedList<UserDefineAttribute>();
		linkedList.add(new UserDefineAttribute("test2", "test2"));
		linkedList.add(new UserDefineAttribute("test3", "test3"));

		setContentView(R.layout.view_advance_attribute_of_product);
		productAdapter = new ViewAdvanceAttributeOfProductAdapter(this,
				R.layout.view_advance_attribute_of_product_item, linkedList);
		// mRadioGroup = (RadioGroup) findViewById(R.id.menu);
		// btnRadioLatest = (RadioButton) findViewById(R.id.radioLatest);
		listView = (ListView) findViewById(R.id.listNewAttributeOfProduct);
		productAdapter.add(new UserDefineAttribute("test1", "test1"));
		// TODO:(condohero01) load user define attribute from server and put it
		// into productAdapter(String, String)
		listView.setAdapter(productAdapter);

	}
}
