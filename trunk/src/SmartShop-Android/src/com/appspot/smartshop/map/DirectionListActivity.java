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
		
		// lat, lng info from HomeActivity
		final Bundle bundle = getIntent().getExtras();
		
		// GUI
		final ListView listDirection = (ListView) findViewById(R.id.listDirection);
		final Button btnShowDirectionOnMap = (Button) findViewById(R.id.btnShowDirectionOnMap);
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
				
				// button show direction on map
				if (directionResult.hasError) {
					btnShowDirectionOnMap.setText(getString(R.string.errGetDirection));
				} else {
					btnShowDirectionOnMap.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
//							MapDialog.createDirectionOnMapDialog(
//									DirectionListActivity.this, directionResult).show();
							Intent intent = new Intent(DirectionListActivity.this, DirectionOnMapActivity.class);
							DirectionOnMapActivity.directionResult = directionResult;
							startActivity(intent);
						}
					});
				}
			}
			
			@Override
			public void loadData() {
				// direction result
				directionResult = MapService.getDirectionResult(
						bundle.getDouble("lat1"), bundle.getDouble("lng1"),
						bundle.getDouble("lat2"), bundle.getDouble("lng2"));
				
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
