package com.appspot.smartshop.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.appspot.smartshop.R;

public class RestClient {
	public static final String TAG = "[RestClient]";
	public static JSONParser jsonParser = null;
	private static HttpClient httpClient;
	public static int CONNECTION_TIMEOUT = 10000;
	public static int SOCKET_TIMEOUT = 30000;

	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	public static void postData(String url, String jsonParam, JSONParser parser) {
		Log.d(TAG, url); 
				
		jsonParser = parser;

		if (httpClient == null) {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);

			httpClient = new DefaultHttpClient(httpParameters);
		}
		
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setEntity(new StringEntity(jsonParam));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		HttpResponse response = null;
		HttpEntity entity = null;
		String result = null;
		InputStream instream = null;
		
		try {
			response = httpClient.execute(httpPost);
			entity = response.getEntity();

			if (entity != null) {
				instream = entity.getContent();
				result = convertStreamToString(instream);
				instream.close();
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			jsonParser.onFailure(e.getMessage());
			return;
		} catch (IOException e) {
			e.printStackTrace();
			jsonParser.onFailure(e.getMessage());
			return;
		} 
		
		JSONObject json = null;
		
		switch (jsonParser.jsonType) {
		case JSONParser.SMART_SHOP_JSON:
			try {
				json = new JSONObject(result);
				Log.d(TAG, json.toString());
			} catch (JSONException e) {
				e.printStackTrace();
				jsonParser.onFailure(e.getMessage());
				return;
			} 
			
			try {
				int errorCode = json.getInt(URLConstant.ERROR_CODE);
				String message = json.getString(URLConstant.MESSAGE);
				if (errorCode == 1) {
					jsonParser.onFailure(message);
					return;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				jsonParser.onFailure(Global.application.getString(R.string.errServer));
				return;
			}
			
			try {
				jsonParser.onSuccess(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
			
		case JSONParser.GOOGLE_DIRECTION_JSON:
			try {
				json = new JSONObject(result);
				Log.d(TAG, json.toString());
				jsonParser.onSuccess(json);
			} catch (JSONException e) {
				e.printStackTrace();
				jsonParser.onFailure(e.getMessage());
			}
			break;
		}
		
	}

	public static void getData(String url, JSONParser parser) {
		Log.d(TAG, url);
		
		jsonParser = parser;

		if (httpClient == null) {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);

			httpClient = new DefaultHttpClient(httpParameters);
		}
		HttpGet httpGet = new HttpGet(url);
		HttpResponse response = null;
		HttpEntity entity = null;
		String result = null;
		InputStream instream = null;

		try {
			response = httpClient.execute(httpGet);
			entity = response.getEntity();

			if (entity != null) {
				instream = entity.getContent();
				result = convertStreamToString(instream);
				instream.close();
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
			jsonParser.onFailure(e.getMessage());
			return;
		} catch (IOException e) {
			e.printStackTrace();
			jsonParser.onFailure(e.getMessage());
			return;
		} 
		
		JSONObject json = null;
		
		switch (jsonParser.jsonType) {
		case JSONParser.SMART_SHOP_JSON:
			try {
				json = new JSONObject(result);
				Log.d(TAG, json.toString());
			} catch (JSONException e) {
				e.printStackTrace();
				jsonParser.onFailure(e.getMessage());
				return;
			} 
			
			try {
				int errorCode = json.getInt(URLConstant.ERROR_CODE);
				String message = json.getString(URLConstant.MESSAGE);
				if (errorCode == 1) {
					jsonParser.onFailure(message);
					return;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				jsonParser.onFailure(Global.application.getString(R.string.errServer));
				return;
			}
			
			try {
				jsonParser.onSuccess(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			break;
			
		case JSONParser.GOOGLE_DIRECTION_JSON:
			try {
				json = new JSONObject(result);
				Log.d(TAG, json.toString());
				jsonParser.onSuccess(json);
			} catch (JSONException e) {
				e.printStackTrace();
				jsonParser.onFailure(e.getMessage());
			}
			break;
		}
		
	}

}
