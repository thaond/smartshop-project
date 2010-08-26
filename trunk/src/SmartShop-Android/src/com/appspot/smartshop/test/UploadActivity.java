package com.appspot.smartshop.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.appspot.smartshop.R;

public class UploadActivity extends Activity {

	private DataOutputStream dos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.upload);

		Button btnUpload = (Button) findViewById(R.id.btnTest);
		btnUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Thread t = new Thread(new Runnable() {
					
					@Override
					public void run() {
						doFileUpload();
					}
				});
				t.start();
			}
		});

	}

	private void doFileUpload() {

		HttpURLConnection conn = null;
		dos = null;
		DataInputStream inStream = null;

		// String exsistingFileName = "samplefile.txt";
		String exsistingFileName = "/sdcard/a.jpg";
		// Is this the place are you doing something wrong.

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		int bytesRead, bytesAvailable, bufferSize;

		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		String responseFromServer = "";
		String urlString = "http://10.0.2.2/testupload/myupload.php";

		try {
			// ------------------ CLIENT REQUEST

			Log.e("MediaPlayer", "Inside second Method");

			FileInputStream fileInputStream = new FileInputStream(new File(
					exsistingFileName));
			// InputStream fileInputStream =
			// getAssets().open(exsistingFileName);

			// open a URL connection to the Servlet

			URL url = new URL(urlString);

			// Open a HTTP connection to the URL

			conn = (HttpURLConnection) url.openConnection();

			// Allow Inputs
			conn.setDoInput(true);

			// Allow Outputs
			conn.setDoOutput(true);

			// Don't use a cached copy.
			conn.setUseCaches(false);

			// Use a post method.
			conn.setRequestMethod("POST");

			conn.setRequestProperty("Connection", "Keep-Alive");

			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos
					.writeBytes("Content-Disposition:\"form-data\"; name=\"uploadedfile\";filename=\""
							+ exsistingFileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);

			Log.e("MediaPlayer", "Headers are written");

			// create a buffer of maximum size

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form...

			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// close streams
			Log.e("MediaPlayer", "File is written");
			fileInputStream.close();
			dos.flush();
//			dos.close();

		} catch (MalformedURLException ex) {
			Log.e("MediaPlayer", "error: " + ex.getMessage(), ex);
		}

		catch (IOException ioe) {
			Log.e("MediaPlayer", "error: " + ioe.getMessage(), ioe);
		}

		// ------------------ read the SERVER RESPONSE

		try {
			inStream = new DataInputStream(conn.getInputStream());
			String str;

			while ((str = inStream.readLine()) != null) {
				Log.e("MediaPlayer", "Server Response" + str);
			}
			inStream.close();

		} catch (IOException ioex) {
			Log.e("MediaPlayer", "error: " + ioex.getMessage(), ioex);
		}

		if (dos != null)
			try {
				dos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
