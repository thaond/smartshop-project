package com.appspot.smartshop.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.SearchByCategoryAdapter;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;

public class ExpandableFeatureSelector extends ExpandableListActivity {
	private static final String NAME = "NAME";
	public static boolean isClicked = false;
	private ExpandableListAdapter mAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		List<Map<String, String>> groupData = new ArrayList<Map<String, String>>();
		List<List<Map<String, String>>> childData = new ArrayList<List<Map<String, String>>>();
		
		List<Map<String, String>> list = null;
		Map<String, String> map = null;
		
		for (int i = 0; i < 3; ++i) {
			list = new ArrayList<Map<String, String>>();
			map = new HashMap<String, String>();
			
			map.put("checked", "checked");
			map.put("unchecked", "unchecked");
			list.add(map);
			childData.add(list);
		}
		
		
		for (String database_name : AvailableCategories) {
			Map<String, String> curGroupMap = new HashMap<String, String>();
			groupData.add(curGroupMap);
			curGroupMap.put(NAME, database_name);

			List<Map<String, String>> children = new ArrayList<Map<String, String>>();
			for (String download_option : AvailableFeatures) {
				Map<String, String> curChildMap = new HashMap<String, String>();
				children.add(curChildMap);
				curChildMap.put(NAME, download_option);
			}
			childData.add(children);
		}

		mAdapter = new SearchByCategoryAdapter(this, groupData,
				android.R.layout.simple_expandable_list_item_1,
				new String[] { NAME }, new int[] { android.R.id.text1 },
				childData, 0,
				new String[] { NAME }, new int[] { android.R.id.text1 });
		setListAdapter(mAdapter);

		final ListView listView = getExpandableListView();
		listView.setItemsCanFocus(false);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
	}

	private static final String[] AvailableCategories = new String[] { "Books",
			"Clothes", "Cars" };

	private static final String[] AvailableFeatures = new String[] { "Damaged",
			"Used" };
}
