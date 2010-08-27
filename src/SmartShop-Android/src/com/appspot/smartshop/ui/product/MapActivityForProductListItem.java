package com.appspot.smartshop.ui.product;

import com.appspot.smartshop.R;
import com.appspot.smartshop.R.id;
import com.appspot.smartshop.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class MapActivityForProductListItem extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_list_item);
		Button btnMap = (Button)findViewById(R.id.btnMap);
		//TODO: Process btnMap event
	}

}
