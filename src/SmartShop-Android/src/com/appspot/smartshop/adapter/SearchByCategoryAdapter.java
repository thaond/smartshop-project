//package com.appspot.smartshop.adapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//<<<<<<< .mine
//=======
//import android.content.Context;
//import android.util.Log;
//import android.view.LayoutInflater;
//>>>>>>> .r97
//import android.view.View;
//import android.view.ViewGroup;
//<<<<<<< .mine
//=======
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.SimpleExpandableListAdapter;
//import android.widget.TextView;
//>>>>>>> .r97
//import android.widget.CompoundButton.OnCheckedChangeListener;
//
//<<<<<<< .mine
//import android.widget.BaseExpandableListAdapter;
//
//public class SearchByCategoryAdapter extends BaseExpandableListAdapter {
//
//
//
//	@Override
//	public long getChildId(int groupPosition, int childPosition) {
//		// TODO Auto-generated method stub
//		return 0;
//=======
//import com.appspot.smartshop.R;
//import com.appspot.smartshop.dom.ChildItemInSearchByCategory;
//
//public class SearchByCategoryAdapter extends SimpleExpandableListAdapter {
//	public Context _context;
//
//	public SearchByCategoryAdapter(Context context,
//			List<? extends Map<String, ?>> groupData, int groupLayout,
//			String[] groupFrom, int[] groupTo,
//			List<? extends List<? extends Map<String, ?>>> childData,
//			int childLayout, String[] childFrom, int[] childTo) {
//		super(context, groupData, groupLayout, groupFrom, groupTo, childData,
//				childLayout, childFrom, childTo);
//		_context = context;
//>>>>>>> .r97
//	}
//
//	@Override
//	public View getChildView(int groupPosition, int childPosition,
//			boolean isLastChild, View convertView, ViewGroup parent) {
//<<<<<<< .mine
//		// TODO Auto-generated method stub
//		return null;
//	}
//=======
//		LayoutInflater inflater = (LayoutInflater) _context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View view = inflater.inflate(R.layout.child_category, null);
//		
//		final Map<String, String> child = (Map<String, String>) getChild(groupPosition, childPosition);
//		
//		final TextView text = (TextView) view.findViewById(R.id.txtchildCategory);
//		text.setText(child.get("unchecked"));
//		CheckBox checkbox = (CheckBox) view.findViewById(R.id.checkBox);
////		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
////			
////			@Override
////			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////				text.setText(isChecked ? child.get("checked") : child.get("unchecked"));
////			}
////		});
//		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				System.out.println("ok?");
//			}
//		});
//>>>>>>> .r97
//
//<<<<<<< .mine
//	@Override
//	public int getChildrenCount(int groupPosition) {
//		// TODO Auto-generated method stub
//		return 0;
//=======
//		return view;
//>>>>>>> .r97
//	}
//
//	@Override
//	public Object getGroup(int groupPosition) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public int getGroupCount() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public long getGroupId(int groupPosition) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public View getGroupView(int groupPosition, boolean isExpanded,
//			View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public boolean hasStableIds() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean isChildSelectable(int groupPosition, int childPosition) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public Object getChild(int groupPosition, int childPosition) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//}
