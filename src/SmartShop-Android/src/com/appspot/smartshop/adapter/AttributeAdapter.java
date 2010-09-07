package com.appspot.smartshop.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.Attribute;
import com.appspot.smartshop.ui.product.ViewProductUserDefinedAttributeActivity;
import com.appspot.smartshop.utils.Utils;

public class AttributeAdapter extends ArrayAdapter<Attribute> {
	
	public AttributeAdapter(Context context, int textViewResourceId,
			Attribute[] objects) {
		super(context, textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
	}

	private LayoutInflater inflater;
	private static int ATTR_NAME_WIDTH = Utils.getScreenWidth() / 4;
	private static int ATTR_VALUE_WIDTH = Utils.getScreenWidth() * 3 / 4;
	
	public AttributeAdapter(Context context, int textViewResourceId,
			List<Attribute> objects) {
		super(context, textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.attribute_list_item, null);
			
			holder = new ViewHolder();
			holder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			holder.txtValue = (EditText) convertView.findViewById(R.id.txtValue);
			holder.txtName.setWidth(ATTR_NAME_WIDTH);
			holder.txtValue.setWidth(ATTR_VALUE_WIDTH);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Attribute attr = (Attribute) getItem(position);
		holder.txtName.setText(attr.name);
		holder.txtValue.setText(attr.value);
		if(ViewProductUserDefinedAttributeActivity.canEdit==false){
			Utils.setEditableEditText(holder.txtValue, false);
//			holder.txtValue.setFilters(Global.uneditableInputFilters);
		}
		
		return convertView;
	}
	
	public void addNewAtribute(Attribute attr) {
		add(attr);
		notifyDataSetChanged();
	}

	static class ViewHolder {
		TextView txtName;
		EditText txtValue;
	}
}
