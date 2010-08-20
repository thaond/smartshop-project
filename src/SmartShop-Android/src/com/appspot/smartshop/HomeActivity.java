package com.appspot.smartshop;


import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.appspot.smartshop.dom.Page;
import com.appspot.smartshop.map.DirectionListActivity;
import com.appspot.smartshop.ui.page.ViewCommentsActivity;
import com.appspot.smartshop.ui.page.ViewPageActivity;
import com.appspot.smartshop.utils.Global;

public class HomeActivity extends Activity {
	public static final String TAG = "[HomeActivity]";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Global.application = this;
        
        Button btn1 = (Button) findViewById(R.id.Button01);
        btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				test1();
			}
		});
        
        Button btn2 = (Button) findViewById(R.id.Button02);
        btn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				test2();
			}
		});
    }

	protected void test2() {
		Intent intent = new Intent(this, ViewCommentsActivity.class);
		
		startActivity(intent);
	}
	
	private void testViewPage() {
		Page page = new Page();
		page.content = "hàng việt nam chất lượng cao";
		page.name = "hàng dổm";
		page.page_view = 100;
		page.date_post = new Date();
		
		Intent intent = new Intent(this, ViewPageActivity.class);
		intent.putExtra("page", page);
		
		startActivity(intent);
	}

	protected void test1() {
		double lat1, lat2, lng1, lng2;
		lat1 = 10.781189;
		lng1 = 106.6513;
		lat2 = 10.77769;
		lng2 = 106.660978;
		Intent intent = new Intent(this, DirectionListActivity.class);
		intent.putExtra("lat1", lat1);
		intent.putExtra("lat2", lat2);
		intent.putExtra("lng1", lng1);
		intent.putExtra("lng2", lng2);
		Log.d(TAG, "before startActivity(intent)");
		startActivity(intent);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");
	}
}