package com.appspot.smartshop.ui.user.notification;

import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.appspot.smartshop.R;
import com.appspot.smartshop.utils.Global;

public class NotificationMessageView extends Activity {
	public TextView txtContentNotification;
	public Button btnOut;
	public String content;
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.view_detail_of_notification);
	        txtContentNotification = (TextView) findViewById(R.id.txtNotification);
	        content = getIntent().getExtras().getString(Global.CONTENT_NOTIFICATION);
	        txtContentNotification.setText(content);
	        btnOut = (Button) findViewById(R.id.btnOut);
	        btnOut.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
				}
			});
	        
	        // look up the notification manager service
	        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	        
	        // cancel the notification that we started in IncomingMessage
	        nm.cancel(R.string.add_new_comment);
	    }
}
