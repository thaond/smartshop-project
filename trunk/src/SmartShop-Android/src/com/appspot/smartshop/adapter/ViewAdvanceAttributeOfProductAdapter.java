package com.appspot.smartshop.adapter;

import java.util.List;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.UserDefineAttribute;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class ViewAdvanceAttributeOfProductAdapter extends ArrayAdapter<UserDefineAttribute>{
	public int resourceId;
	private Context context;
	public ViewAdvanceAttributeOfProductAdapter(Context context,
			int textViewResourceId, List<UserDefineAttribute> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
		this.context = context;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext()
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(resourceId, null);
		UserDefineAttribute product = (UserDefineAttribute)this.getItem(position);
		EditText txtNameOfAttribute = (EditText) view.findViewById(R.id.txtViewAdvanceAttribute);
		txtNameOfAttribute.setText(product.getNewAttribute());
		EditText txtValueOfAttribute = (EditText) view.findViewById(R.id.txtViewAdvanceAttributeOfProduct);
		txtValueOfAttribute.setText(product.getValueOfNewAttribute());
		return view;
	}

}
