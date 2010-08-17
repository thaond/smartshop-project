package com.appspot.smartshop.ui.product;

import java.util.LinkedList;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.UserDefineAttributeAdapter;
import com.appspot.smartshop.dom.UserDefineAttribute;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class PostProductActivityUserDefine extends Activity {
	public ListView listViewAttribute;
	public UserDefineAttributeAdapter userDefineAttributeAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_define_attribute);
		Button add = (Button) findViewById(R.id.btnAdd);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				userDefineAttributeAdapter.add(new UserDefineAttribute(
						"", ""));
				listViewAttribute.setAdapter(userDefineAttributeAdapter);

			}
		});
		Button clear = (Button) findViewById(R.id.btnClear);
		clear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				userDefineAttributeAdapter.clear();
				listViewAttribute.setAdapter(userDefineAttributeAdapter);
				
			}
		});
		listViewAttribute = (ListView) findViewById(R.id.listUserDefineAttritube);
		userDefineAttributeAdapter = new UserDefineAttributeAdapter(this,
				R.layout.user_define_attribute_item,
				new LinkedList<UserDefineAttribute>());
	}

}
