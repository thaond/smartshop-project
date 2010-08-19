package com.appspot.smartshop;

import com.appspot.smartshop.map.DirectionListActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Global.currentActivity = this;
        
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
		startActivity(intent);
	}
}