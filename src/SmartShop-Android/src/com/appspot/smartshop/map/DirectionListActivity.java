package com.appspot.smartshop.map;

import com.appspot.smartshop.R;
import com.appspot.smartshop.adapter.DirectionListAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class DirectionListActivity extends Activity {
	
	private static DirectionListActivity instance = null;
	
	public DirectionListActivity() {
		instance = this;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.direction_list);
		
		// lat, lng info from HomeActivity
		Bundle bundle = getIntent().getExtras();
		
		// direction result
		final DirectionResult directionResult = MapService.getDirectionResult(
				bundle.getDouble("lat1"), bundle.getDouble("lng1"),
				bundle.getDouble("lat2"), bundle.getDouble("lng2"));
		
		// listview
		String[] instructions = directionResult.instructions;
		DirectionListAdapter adapter = new DirectionListAdapter(
				this, R.layout.direction_list_item, instructions);
		ListView listDirection = (ListView) findViewById(R.id.listDirection);
		listDirection.setAdapter(adapter);
		
		// duration and distance
		String duration = directionResult.duration;
		String distance = directionResult.distance;
		if (duration != null) {
			TextView txtDuration = (TextView) findViewById(R.id.txtDuration);
			txtDuration.setText(duration);
		}
		if (distance != null) {
			TextView txtdistance = (TextView) findViewById(R.id.txtDistance);
			txtdistance.setText(distance);
		}
		
		// button show direction on map
		Button btnShowDirectionOnMap = (Button) findViewById(R.id.btnShowDirectionOnMap);
		if (directionResult.hasError) {
			btnShowDirectionOnMap.setText(getString(R.string.errGetDirection));
		} else {
			btnShowDirectionOnMap.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MapDialog.createDirectionOnMapDialog(instance, directionResult).show();
				}
			});
		}
	}
}
