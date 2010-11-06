package com.appspot.smartshop.map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.DirectionAdapter;
import com.appspot.smartshop.utils.DataLoader;
import com.appspot.smartshop.utils.SimpleAsyncTask;
import com.google.android.maps.MapActivity;

public class DirectionListActivity extends MapActivity {
	public static final String TAG = "[DirectionListActivity]";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.direction_list);
		
		// GUI
		final ListView listDirection = (ListView) findViewById(R.id.listDirection);
		final TextView txtDuration = (TextView) findViewById(R.id.txtDuration);
		final TextView txtdistance = (TextView) findViewById(R.id.txtDistance);
		
		// start an AsyncTask to get direction result
		new SimpleAsyncTask(this, new DataLoader() {
			
			private String[] instructions;
			private String duration;
			private String distance;
			private DirectionResult directionResult;

			@Override
			public void updateUI() {
				// listview
				DirectionAdapter adapter = new DirectionAdapter(
						DirectionListActivity.this, R.layout.direction_list_item, instructions);
				listDirection.setAdapter(adapter);
				
				// duration and distance
				if (duration != null) {
					txtDuration.setText(duration);
				}
				if (distance != null) {
					txtdistance.setText(distance);
				}
			}
			
			@Override
			public void loadData() {
				// direction result
				directionResult = DirectionOnMapActivity.directionResult;
				
				// instructions
				instructions = directionResult.instructions;
				
				// duration and distance
				duration = directionResult.duration;
				distance = directionResult.distance;
			}
		}).execute();
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
