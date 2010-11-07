package com.appspot.smartshop.ui.product;

import java.util.ArrayList;
import java.util.LinkedList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.utils.Global;

public class SelectTwoProductActivity extends Activity {
	
	private Spinner spinner1;
	private Spinner spinner2;
	private int position1 = 0;
	private int position2 = 0;
	
	private ArrayList<ProductInfo> products;
	private LinkedList<String> productNames = new LinkedList<String>();
	
	private Button btnCompare;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_two_products);
		
		// get list products from Intent
		products = (ArrayList<ProductInfo>) getIntent().getSerializableExtra(Global.PRODUCTS);
		
		// list of product name
		for (ProductInfo productInfo : products) {
			productNames.add(productInfo.name);
		}
		
		// spinner 1
		Spinner s1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(
        		this, android.R.layout.simple_spinner_item, productNames);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adapter1);
        s1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				position1 = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
        
        // spinner 2
        Spinner s2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
        		this, android.R.layout.simple_spinner_item, productNames);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s2.setAdapter(adapter2);
        s2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				position2 = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
        
        // button
        btnCompare = (Button) findViewById(R.id.btnCompare);
        btnCompare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onCompareButtonClick();
			}
		});
	}
	
	private void onCompareButtonClick() {
		if (position1 != -1 && position2 != -1 && position1 != position2) {
			Intent intent = new Intent(this, CompareTwoProductsActivity.class);
			intent.putExtra(Global.PRODUCT_1, products.get(position1));
			intent.putExtra(Global.PRODUCT_2, products.get(position2));
			startActivity(intent);
		} else {
			Toast.makeText(this, getString(R.string.warn_choose_2_different_products), 
					Toast.LENGTH_SHORT).show();
		}
	}
}
