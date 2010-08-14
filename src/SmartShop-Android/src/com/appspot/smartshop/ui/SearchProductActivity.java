package com.appspot.smartshop.ui;

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

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		listViewProduct = (ListView) findViewById(R.id.listProduct);
		productAdapter = new ProductAdapter(this, R.layout.product_list_item,
				new LinkedList<ProductInfo>()) {

		};
		listViewProduct.setAdapter(productAdapter);
		//Code de test ma thoi
		for (int i = 1; i <= 12; i++) {
			productAdapter.add(new ProductInfo("name" + i, "price" + i,
					"description" + i));
		}
		Button btnAdvancedSearchProduct = (Button)findViewById(R.id.btnAdvancedSearch);

		//TODO: Xu Ly su kien cho button AdvancedSearch 	
	}
}
