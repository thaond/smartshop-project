package com.appspot.smartshop.ui.product;

import android.app.ExpandableListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.appspot.smartshop.R;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.dom.CategoryInfo;
import com.google.android.maps.MapView.LayoutParams;

/**
 * Demonstrates expandable lists using a custom {@link ExpandableListAdapter}
 * from {@link BaseExpandableListAdapter}.
 */
public class SearchByCategory extends ExpandableListActivity {

	ExpandableListAdapter mAdapter;
	private int type;
	public String childSeledted = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// TODO (vanloi999): type (page or product)
		// type = getIntent().getExtras().getInt(Global.TYPE);

		Bundle bundle = getIntent().getExtras();
		CategoryInfo categoryInfo = (CategoryInfo) bundle
				.get(Global.CATEGORY_INFO);
		String[] groups = categoryInfo.parentCategory;
		String[][] children = categoryInfo.childrenCategory;
		// Set up our adapter

		mAdapter = new MyExpandableListAdapter(groups, children);
			
		setListAdapter(mAdapter);
		registerForContextMenu(getExpandableListView());
	}
	public final static int MENU_SEARCH_BY_CATEGORY =0;
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_SEARCH_BY_CATEGORY, 0, R.string.search_by_categories).setIcon(R.drawable.category);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case MENU_SEARCH_BY_CATEGORY:
				//TODO: vanloi999 process the result that user selected in child category
				Log.d("Child Item Seledted",childSeledted);
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A simple adapter which maintains an ArrayList of photo resource Ids. Each
	 * photo is displayed as an image. This adapter supports clearing the list
	 * of photos and adding a new photo.
	 * 
	 */
	public class MyExpandableListAdapter extends BaseExpandableListAdapter {
		// TODO: vanloi999 Load category from database and put it into groups
		// and children
		public String[] groups;
		public String[][] children;

		public MyExpandableListAdapter(String[] groups, String[][] children) {
			this.groups = groups;
			this.children = children;
		}

		public Object getChild(int groupPosition, int childPosition) {
			return children[groupPosition][childPosition];
		}

		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			return children[groupPosition].length;
		}

		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getBaseContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.parent_category, null);
			TextView textView = (TextView) view
					.findViewById(R.id.txtParentCategory);
			textView.setText(groups[groupPosition]);
			return view;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getBaseContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.child_category, null);
			final TextView txtView = (TextView) convertView
					.findViewById(R.id.txtchildCategory);
			txtView.setText(children[groupPosition][childPosition]);
			final CheckBox ch1 = (CheckBox) convertView.findViewById(R.id.checkBox);
			ch1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (ch1.isChecked()) {
						if(childSeledted.length()==0){
							childSeledted += (String) txtView.getText();
						}else{
							childSeledted += ", " + (String) txtView.getText();
							//TODO: vanloi999 post request to server
						}
						Log.d("TAG", childSeledted);
					}
				}
			});
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
	}
}
