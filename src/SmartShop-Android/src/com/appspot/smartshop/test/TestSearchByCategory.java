package com.appspot.smartshop.test;

import com.appspot.smartshop.R;
import com.appspot.smartshop.ui.product.UseThisExpandableListForImplementSearchByCategory;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

public class TestSearchByCategory extends Activity {
	public ExpandableListView expandableList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		UseThisExpandableListForImplementSearchByCategory list = null;
		list.setContentView(R.layout.test_expandable_list);
		//list.onChildClick(list, v, groupPosition, childPosition, id)
		
		
		
		
	}

}
