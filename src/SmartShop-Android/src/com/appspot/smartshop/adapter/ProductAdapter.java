package com.appspot.smartshop.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.R.id;
import com.appspot.smartshop.dom.ProductInfo;

public class ProductAdapter extends ArrayAdapter<ProductInfo>{
	private int resourceId;
	public ProductAdapter(Context context, int textViewResourceId,
			List<ProductInfo> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		resourceId = textViewResourceId;
	}
	@Override 
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(resourceId, null);
		ProductInfo product = (ProductInfo)getItem(position);
		TextView productName = (TextView)view.findViewById(R.id.txtProductName);
		productName.setText(product.name);
		TextView productPrice = (TextView)view.findViewById(R.id.txtProductPrice);
		productPrice.setText(product.price);
		TextView productDescription = (TextView)view.findViewById(R.id.txtDescription);
		productDescription.setText(product.description);
		return view;
	}
}