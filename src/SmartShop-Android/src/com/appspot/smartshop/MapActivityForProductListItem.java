package com.appspot.smartshop;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

public class MapActivityForProductListItem extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_list_item);
		Button btnMap = (Button)findViewById(R.id.btnMap);
		//TODO: xu ly su kien cho button Map, tim dia diem cho mon hang
	}

}
