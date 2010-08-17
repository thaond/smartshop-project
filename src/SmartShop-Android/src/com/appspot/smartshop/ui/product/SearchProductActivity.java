package com.appspot.smartshop.ui.product;

import java.util.LinkedList;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.ProductAdapter;
import com.appspot.smartshop.dom.ProductInfo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class SearchProductActivity extends Activity {
	private ListView listViewProduct;
	private ProductAdapter productAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_product_form);
		Button btnSearchProduct = (Button) findViewById(R.id.btnSearch);
		btnSearchProduct.setOnClickListener(new OnClickListener() {
			int i = 0;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				productAdapter.add(new ProductInfo("name" + i, "price" + i,
//						"description" + i));
//				i++;
//				listViewProduct.setAdapter(productAdapter);
			}
		});
		//listViewProduct lay adapter, adapter lay tu product_list_item
		listViewProduct = (ListView) findViewById(R.id.listProduct);
		productAdapter = new ProductAdapter(this, R.layout.product_list_item,
				new LinkedList<ProductInfo>()) {

		};
		Button btnAdvancedSearchProduct = (Button)findViewById(R.id.btnAdvancedSearch);

		//TODO: Xu Ly su kien cho button AdvancedSearch 	
	}
}
