package com.appspot.smartshop.ui.product.vatgia;

import java.net.URLEncoder;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.NProductVatgiaAdapter;
import com.appspot.smartshop.dom.NProductVatGia;
import com.appspot.smartshop.facebook.utils.FacebookUtils;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.Global;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.appspot.smartshop.utils.URLConstant;

public class SearchVatgiaActivity extends Activity {
	public static final String TAG = "[SearchVatgiaActivity]";
	//set up variable for facebook connection
	//end set up variable for facebook connection
	
	private static final int NO_PAGE = 0;
	
	private EditText txtSearch;
	private ListView listProducts;
	private NProductVatgiaAdapter adapter;
	private TextView txtCurrentPage;
	private TextView txtNumOfPages;
	private LinkedList<NProductVatGia> products;
	
	private int currentPage = 1;
	private int numOfPages = NO_PAGE;
	private boolean first = true;
	
	private String query = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_vatgia);
		// search field
		txtSearch = (EditText) findViewById(R.id.txtSearch);
		
		// buttons
		Button btnSearch = (Button) findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				query = txtSearch.getText().toString(); 
				query = URLEncoder.encode(query);
				if (query != null && !query.trim().equals("")) {
					searchProductsByQuery(URLEncoder.encode(query));	
				}
			}
		});
		
		Button btnNext = (Button) findViewById(R.id.btnNext);
		btnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (numOfPages != NO_PAGE) {
					if (currentPage < numOfPages && (query != null || !query.trim().equals(""))) {
						currentPage++;
						searchProductsByQuery(query);
					}
				}
			}
		});
		
		Button btnPrev = (Button) findViewById(R.id.btnPrev);
		btnPrev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (numOfPages != NO_PAGE) {
					if (currentPage > 1 && (query != null || !query.trim().equals(""))) {
						currentPage--;
						searchProductsByQuery(query);
					}
				}
			}
		});
		
		// list view
		listProducts = (ListView) findViewById(R.id.listVatgiapProducts);
		
		// text fields
		txtCurrentPage = (TextView) findViewById(R.id.txtCurrentPage);
		txtNumOfPages = (TextView) findViewById(R.id.txtNumOfPages);
	}

	private SimpleAsyncTask task;
	private String url;
	protected void searchProductsByQuery(final String query) {
		
		task = new SimpleAsyncTask(this, new DataLoader() {
			
			@Override
			public void updateUI() {
				txtCurrentPage.setText("" + currentPage);
				if (numOfPages != NO_PAGE) {
					txtNumOfPages.setText("" + numOfPages);
				}
				
				adapter = new NProductVatgiaAdapter(SearchVatgiaActivity.this, 0, products, new FacebookUtils(SearchVatgiaActivity.this));
				listProducts.setAdapter(adapter);
			}
			
			@Override
			public void loadData() {
				url = String.format(URLConstant.GET_VATGIA_PRODUCTS, query, currentPage);
				RestClient.getData(url, new JSONParser() {
					
					@Override
					public void onSuccess(JSONObject json) throws JSONException {
						if (first) {
							numOfPages = json.getInt("numOfPages");
							first = false;
						}
						
						JSONArray arr = json.getJSONArray("results");
						products = Global.gsonDateWithoutHour.fromJson(arr.toString(), 
								NProductVatGia.getType());
					}
					
					@Override
					public void onFailure(String message) {
						task.hasData = false;
						task.message = message;
						task.cancel(true);
					}
				});
			}
		});
		task.execute();
	}
}
