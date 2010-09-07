package com.appspot.smartshop.ui.product;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.CommentAdapter;
import com.appspot.smartshop.adapter.CompanyAdapter;
import com.appspot.smartshop.dom.Comment;
import com.appspot.smartshop.dom.Company;
import com.appspot.smartshop.ui.comment.ViewCommentsActivity;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;

public class SearchVatGia extends Activity{
	public static final String TAG = "[SearchVatGia]";
	private ListView listCompany;
	private CompanyAdapter companyAdapter;
	private LayoutInflater inflater;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_product_vatgia);
		listCompany = (ListView) findViewById(R.id.listCompanies);
		companyAdapter = new CompanyAdapter(this, R.layout.company_item, new LinkedList<Company>());
		loadCompany();
		
	}
	private SimpleAsyncTask task;
	private void loadCompany() {
		task = new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void loadData() {
		
				
				String param = null;
				RestClient.postData(URLConstant.GET_COMMENTS, param, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						//TODO
						Log.d(TAG, "not implement yet");
					}	
					
					@Override
					public void onFailure(String message) {
						task.cancel(true);
					}
				});
			}

			@Override
			public void updateUI() {
				//TODO
				Log.d(TAG, "not implement yet");
				LinkedList<Company> listDataOfCompany = null;
				companyAdapter = new CompanyAdapter(SearchVatGia.this, R.layout.company_item, listDataOfCompany);
				listCompany.setAdapter(companyAdapter);
			}
		});
		task.execute();
	}
}
