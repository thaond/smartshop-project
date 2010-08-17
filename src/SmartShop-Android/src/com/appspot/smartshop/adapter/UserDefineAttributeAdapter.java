package com.appspot.smartshop.adapter;

import java.util.List;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.ProductInfo;
import com.appspot.smartshop.dom.UserDefineAttribute;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

public class UserDefineAttributeAdapter extends
		ArrayAdapter<UserDefineAttribute> {
	public int resourceId;

	public UserDefineAttributeAdapter(Context context, int textViewResourceId,
			List<UserDefineAttribute> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		resourceId = textViewResourceId;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(resourceId, null);
		UserDefineAttribute attribute = (UserDefineAttribute) getItem(position);
		EditText newAttribute = (EditText) view
				.findViewById(R.id.txtNewAttribute);
		newAttribute.setText(attribute.newAttribute);
		EditText valueOfNewAttribute = (EditText) view
				.findViewById(R.id.txtValueOfNewAttribute);
		valueOfNewAttribute.setText(attribute.valueOfNewAttribute);

		return view;
	}

}
