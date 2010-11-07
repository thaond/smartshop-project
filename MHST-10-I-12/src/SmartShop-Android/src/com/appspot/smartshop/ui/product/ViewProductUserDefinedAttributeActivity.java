package com.appspot.smartshop.ui.product;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.AttributeAdapter;
import com.appspot.smartshop.dom.Attribute;
import com.appspot.smartshop.utils.Global;

public class ViewProductUserDefinedAttributeActivity extends Activity {
	public static final String TAG = "[ViewProductUserDefinedAttributeActivity]";
	protected static Set<Attribute> setAttributes = new HashSet<Attribute>();
	public static boolean canEdit = false;
	private ListView listAttributes;
	private AttributeAdapter adapter;
	public LinkedList<Attribute> result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "view product userdefined attribute");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_user_defined_attribute);
		Bundle bundle = getIntent().getExtras();
		canEdit = bundle.getBoolean(Global.CAN_EDIT_PRODUCT_INFO);
		Button btnAddNewAttribute = (Button) findViewById(R.id.btnAddAttribute);
		if (canEdit==true) {
			//use btnAddNewAttribute as a button to load data from ListView
			btnAddNewAttribute.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					loadDataFromListView();
				}
			});
		}else{
			btnAddNewAttribute.setVisibility(View.GONE);
		}
		
		listAttributes = (ListView) findViewById(R.id.listAttributes);
		adapter = new AttributeAdapter(this, 0, new LinkedList<Attribute>(setAttributes));
		listAttributes.setAdapter(adapter);

	}

	protected void loadDataFromListView() {
		for(int i = 0 ; i< adapter.getCount();i++){
			result.add(adapter.getItem(i));//TODO: update data of product
		}
		//TODO: update data oF product from result
		
	}
}
