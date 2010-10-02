package com.acmelabs;

import com.xtify.android.sdk.PersistentLocationManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AcmeLabsActivity extends Activity {
	private PersistentLocationManager persistentLocationManager;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final Context context = this;

        Thread xtifyThread = new Thread(new Runnable() {
			@Override
			public void run() {
        		persistentLocationManager = new PersistentLocationManager(context);
        		persistentLocationManager.setNotificationIcon(R.drawable.notification);
        		persistentLocationManager.setNotificationDetailsIcon(R.drawable.icon);
        		boolean trackLocation = persistentLocationManager.isTrackingLocation();
        		boolean deliverNotifications = persistentLocationManager.isDeliveringNotifications();
        		if (trackLocation || deliverNotifications) {
        			persistentLocationManager.startService();
        		}
			}
        });
        xtifyThread.start(); // to avoid Android's application-not-responding dialog box, do non-essential work in another thread
    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, 0, 0, "Settings");
		menu.findItem(0).setIcon(R.drawable.menu_preferences);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case 0: {
				persistentLocationManager.showSettingsActivity(this, true);
				return true;
			}
		}
		return false;
	}
}