package com.appspot.smartshop.ui;

import java.util.LinkedList;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.ProductAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class SearchProductActivity extends Activity{
	private ListView listViewProduct;
	private ProductAdapter productAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_product_form);
		Button btnSearchProduct = (Button)findViewById(R.id.btnSearch);
		btnSearchProduct.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		listViewProduct = (ListView)findViewById(R.id.listProduct);
//		productAdapter = new ProductAdapter(this, R.layout.product_list_item, new LinkedList<ProductInfo>()){};
	}

}
