package com.appspot.smartshop.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import android.util.Log;

public class RestClient {
	public static final String TAG = "[RestClient]";
	public static JSONParser jsonParser = null;
	private static HttpClient httpClient;

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
			httpClient = new DefaultHttpClient();
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

		try {
			JSONObject json = new JSONObject(result);
			jsonParser.onSuccess(json);
		} catch (JSONException e) {
			e.printStackTrace();
			jsonParser.onFailure(e.getMessage());
		}
	}

	public static void getData(String url, JSONParser parser) {
		Log.d(TAG, url);
		
		jsonParser = parser;

		if (httpClient == null) {
			httpClient = new DefaultHttpClient();
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

		try {
			JSONObject json = new JSONObject(result);
			jsonParser.onSuccess(json);
		} catch (JSONException e) {
			e.printStackTrace();
			jsonParser.onFailure(e.getMessage());
		}
	}

}
