package com.appspot.smartshop.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.appspot.smartshop.R;
import com.appspot.smartshop.dom.ChildItemInSearchByCategory;

public class SearchByCategoryAdapter extends SimpleExpandableListAdapter {
	public Context _context;

	public SearchByCategoryAdapter(Context context,
			List<? extends Map<String, ?>> groupData, int groupLayout,
			String[] groupFrom, int[] groupTo,
			List<? extends List<? extends Map<String, ?>>> childData,
			int childLayout, String[] childFrom, int[] childTo) {
		super(context, groupData, groupLayout, groupFrom, groupTo, childData,
				childLayout, childFrom, childTo);
		_context = context;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) _context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.child_category, null);
		
		final Map<String, String> child = (Map<String, String>) getChild(groupPosition, childPosition);
		
		final TextView text = (TextView) view.findViewById(R.id.txtchildCategory);
		text.setText(child.get("unchecked"));
		CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkBox);
//		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				text.setText(isChecked ? child.get("checked") : child.get("unchecked"));
//			}
//		});
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				System.out.println("ok?");
			}
		});

		return view;
	}
}
