package com.appspot.smartshop.ui.user;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.smartshop.R;
import com.appspot.smartshop.utils.JSONParser;
import com.appspot.smartshop.utils.RestClient;
import com.appspot.smartshop.utils.URLConstant;
import com.appspot.smartshop.utils.Utils;

public class SendEmailToAdminActivity extends Activity {

	private TextView lblTitle;
	private EditText txtTitle;
	private TextView lblSender;
	private EditText txtSender;
	private TextView lblContent;
	private EditText txtContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send_email_to_admin);
		
		int width = Utils.getScreenWidth();
		int labelWidth = width * 2 / 5;
		int textWidth = width - labelWidth;
		
		lblTitle = (TextView) findViewById(R.id.lblTitle);
		txtTitle = (EditText) findViewById(R.id.txtTitle);
		lblSender = (TextView) findViewById(R.id.lblSender);
		txtSender = (EditText) findViewById(R.id.txtSender);
		lblContent = (TextView) findViewById(R.id.lblContent);
		txtContent = (EditText) findViewById(R.id.txtContent);
		
		lblTitle.setWidth(labelWidth);
		lblSender.setWidth(labelWidth);
		
		txtTitle.setWidth(textWidth);
		txtSender.setWidth(textWidth);
		
		Button btnSend = (Button) findViewById(R.id.btnSend);
		btnSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendEmailToAdmin();
			}
		});
	}
	
	protected void sendEmailToAdmin() {
		String sender = txtSender.getText().toString();
		String title = txtTitle.getText().toString();
		String content = txtContent.getText().toString();
		
		// check user input
		if (sender == null || sender.trim().equals("")) {
			Toast.makeText(this, getString(R.string.warn_email_sender_empty), Toast.LENGTH_SHORT).show();
			return;
		}
		if (title == null || title.trim().equals("")) {
			Toast.makeText(this, getString(R.string.warn_email_title_empty), Toast.LENGTH_SHORT).show();
			return;
		}
		if (content == null || content.trim().equals("")) {
			Toast.makeText(this, getString(R.string.warn_email_content_empty), Toast.LENGTH_SHORT).show();
			return;
		}
		
		// construct url
		String url = String.format(URLConstant.SEND_EMAIL_TO_ADMIN,
				sender, title, content);
		
		// send email
		RestClient.getData(url, new JSONParser() {
			
			@Override
			public void onSuccess(JSONObject json) throws JSONException {
			}
			
			@Override
			public void onFailure(String message) {
				Toast.makeText(SendEmailToAdminActivity.this, message, Toast.LENGTH_SHORT).show();
			}
		});
	}
}
