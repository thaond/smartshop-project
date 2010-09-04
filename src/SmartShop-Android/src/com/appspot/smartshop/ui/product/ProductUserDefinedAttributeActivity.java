package com.appspot.smartshop.ui.product;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.AttributeAdapter;
import com.appspot.smartshop.dom.Attribute;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;

public class ProductUserDefinedAttributeActivity extends Activity {
	
	public static Set<Attribute> setAttributes = new HashSet<Attribute>();

	private ListView listAttributes;
	private AttributeAdapter adapter;
	private LayoutInflater inflater;
	private View view;
	private Builder dialogBuilder;
	private AlertDialog dialog;
	private HashMap<String, String> mapCategories = new HashMap<String, String>();
	private Attribute attribute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_user_defined_attribute);
		
		setAttributes.clear();
		
		Button btnAddNewAttribute = (Button) findViewById(R.id.btnAddAttribute);
		btnAddNewAttribute.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addNewAttribute();
			}
		});
		
		listAttributes = (ListView) findViewById(R.id.listAttributes);
		adapter = new AttributeAdapter(this, 0, new LinkedList<Attribute>());
		listAttributes.setAdapter(adapter);
	}

	protected void addNewAttribute() {
		if (adapterParentCategories != null) {
			showParentCategoriesDialog();
			return;
		}
		
		task = new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void updateUI() {
				adapterParentCategories = new ArrayAdapter<String>(
						ProductUserDefinedAttributeActivity.this,
		                android.R.layout.simple_list_item_single_choice, parentCategories);
				showParentCategoriesDialog();
			}
			
			@Override
			public void loadData() {
				RestClient.getData(URLConstant.GET_PARENT_CATEGORIES, new JSONParser() {
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						JSONArray arr = json.getJSONArray("categories");
						
						int len = arr.length();
						parentCategories = new String[len];
						String name, key_cat;
						for (int i = 0; i < len; ++i) {
							name = arr.getJSONObject(i).getString("name");
							key_cat = arr.getJSONObject(i).getString("key_cat");
							
							parentCategories[i] = name;
							mapCategories.put(name, key_cat);
						}
					}
					
					@Override
					public void onFailure(String message) {
						task.cancel(true);
					}
				});
			}
		});
		
		task.execute();
	}

	private SimpleAsyncTask task;
	private String[] parentCategories = null;
	private ListView listParentCategories;
	private ArrayAdapter<String> adapterParentCategories;
	private void showParentCategoriesDialog() {
		// setup view
		if (inflater == null) {
			inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		if (view == null) {
			view = inflater.inflate(R.layout.parent_categories_dialog, null);
		}
		
		// name and value of attribute
		final EditText txtName = (EditText) view.findViewById(R.id.txtName);
		final EditText txtValue = (EditText) view.findViewById(R.id.txtValue);
		
		// list parent categories
		listParentCategories = (ListView) view.findViewById(R.id.listParentCategories);
        listParentCategories.setItemsCanFocus(false);
        listParentCategories.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listParentCategories.setAdapter(adapterParentCategories);
        listParentCategories.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String attrName = parentCategories[position];
				
				attribute = new Attribute();
				attribute.key_cat = mapCategories.get(attrName);
				attribute.username = Global.username;
			}
		});
        // ok button
        Button btnOk = (Button) view.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				attribute.name = txtName.getText().toString();
				attribute.value = txtValue.getText().toString();
				
				setAttributes.add(attribute);
				adapter.add(attribute);
				
				dialog.dismiss();
			}
		});

		// create dialog
		dialogBuilder = new AlertDialog.Builder(ProductUserDefinedAttributeActivity.this);
		ViewGroup parent = ((ViewGroup) view.getParent());
		if (parent != null) {
			parent.removeView(view);
		}
		dialogBuilder.setView(view);
		dialog = dialogBuilder.create();
		dialog.show();
	}
}
