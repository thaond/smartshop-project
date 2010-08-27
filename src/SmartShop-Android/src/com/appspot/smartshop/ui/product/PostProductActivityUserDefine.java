package com.appspot.smartshop.ui.product;

import java.util.LinkedList;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.UserDefineAttributeAdapter;
import com.appspot.smartshop.dom.UserDefineAttribute;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class PostProductActivityUserDefine extends Activity {
	public ListView listViewAttribute;
	public UserDefineAttributeAdapter userDefineAttributeAdapter;
	public EditText newAtt;
	public EditText newValueAtt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_define_attribute);
		
		Button add = (Button) findViewById(R.id.btnAdd);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				userDefineAttributeAdapter.add(new UserDefineAttribute(
						"", ""));
				listViewAttribute.setAdapter(userDefineAttributeAdapter);
				
			//TODO:(vanloi999) get data from 2 editText (name of attribute and valueOfNewAttribute) and send them to server
			}
		});
		Button btnOK = (Button) findViewById(R.id.btnSelect);
		btnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//TODO:(vanloi999) post data to server
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
