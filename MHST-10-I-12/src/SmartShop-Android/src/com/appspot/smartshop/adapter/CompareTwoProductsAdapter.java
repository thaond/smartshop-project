package com.appspot.smartshop.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.VatgiaProductDetailAdapter.ViewHolder;
import com.appspot.smartshop.dom.Pair;
import com.appspot.smartshop.utils.Utils;

public class CompareTwoProductsAdapter extends BaseExpandableListAdapter {
	private final int WIDTH = (Utils.getScreenWidth() - 30) / 2;
	
	public String[] groups;
	public Pair[] children;
	private LayoutInflater inflater;

	public CompareTwoProductsAdapter(Context context, String[] groups, Pair[] children) {
		this.groups = groups;
		this.children = children;
		inflater = LayoutInflater.from(context);
	}

	public Object getChild(int groupPosition, int childPosition) {
		return children[childPosition];
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return 1;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.parent_category, null);
		}
		
		TextView textView = (TextView) convertView.findViewById(R.id.txtParentCategory);
		textView.setText(groups[groupPosition]);
		
		return convertView;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.compare_two_products_list_item, null);
			
			holder = new ViewHolder();
			holder.txtProductOne = (TextView) convertView.findViewById(R.id.txtProductOne);
			holder.txtProductTwo = (TextView) convertView.findViewById(R.id.txtProductTwo);
			holder.txtProductOne.setWidth(WIDTH);
			holder.txtProductTwo.setWidth(WIDTH);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.txtProductOne.setText(children[groupPosition].name);
		holder.txtProductTwo.setText(children[groupPosition].value);
		
		return convertView;
	}

	public Object getGroup(int groupPosition) {
		return groups[groupPosition];
	}

	public int getGroupCount() {
		return groups.length;
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public boolean hasStableIds() {
		return true;
	}
	
	static class ViewHolder {
		public TextView txtProductOne;
		public TextView txtProductTwo;
	}
}


