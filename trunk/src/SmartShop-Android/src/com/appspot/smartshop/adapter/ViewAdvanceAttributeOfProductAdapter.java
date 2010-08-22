package com.appspot.smartshop.adapter;

import java.util.List;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.UserDefineAttribute;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ViewAdvanceAttributeOfProductAdapter extends ArrayAdapter<UserDefineAttribute>{
	public int resourceId;
	public ViewAdvanceAttributeOfProductAdapter(Context context,
			int textViewResourceId, List<UserDefineAttribute> objects) {
		super(context, textViewResourceId, objects);
		resourceId = textViewResourceId;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext()
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(resourceId, null);
		UserDefineAttribute attribute = (UserDefineAttribute)this.getItem(position);
		TextView txtNameOfAttribute = (TextView) view.findViewById(R.id.txtViewAdvanceAttribute);
		txtNameOfAttribute.setText(attribute.newAttribute);
		TextView txtValueOfAttribute = (TextView) view.findViewById(R.id.txtViewAdvanceAttributeOfProduct);
		txtNameOfAttribute.setText(attribute.valueOfNewAttribute);
		return view;
	}

}
