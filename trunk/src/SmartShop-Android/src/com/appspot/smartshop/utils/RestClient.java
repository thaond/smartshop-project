package com.appspot.smartshop.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class RestClient {

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

	public static void loadData(String url, JSONParser parser) {
		jsonParser = parser;

		if (httpClient == null) {
			httpClient = new DefaultHttpClient();
		}
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = null;
		HttpEntity entity = null;
		String result = null;
		InputStream instream = null;

		try {
			response = httpClient.execute(httpget);
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
