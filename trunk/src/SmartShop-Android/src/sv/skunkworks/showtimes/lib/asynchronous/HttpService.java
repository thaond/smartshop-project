/**
 * Copyright 2010 Hieu Rocker & Tien Trum
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sv.skunkworks.showtimes.lib.asynchronous;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Contains some stuffs for sending <code>HTTP</code> request.
 *
 * @author H&#7912;A PHAN Minh Hi&#7871;u (rockerhieu@gmail.com)
 */
public class HttpService {

    /**
     * Constructs a {@link HttpService} instance.
     */
    public HttpService() {
    }

    /**
     * Send a <code>HTTP GET</code> to <code>url</code> and retrieve the string
     * content.
     *
     * @param url
     *            URL of the webpage you want to retrieve content.
     * @param blocking
     *            Indicate <code>HTTP GET</code> will be sent synchronous or
     *            asynchronous. Pass <code>true</code> if want to send
     *            <code>HTTP GET</code> in the same thread of caller, otherwise
     *            pass <code>false</code>.
     * @param callback
     *            A callback instance that is being called as soon as the remote
     *            service returns the result (a String object contains textual
     *            content) of this method invocation.
     */
    public static void getResource(final String url, final boolean blocking,
            final ServiceCallback callback) {
        Log.d("URL",url);
        callback.onUpdating();
        try {
        	final JsonObject response = getContent(url);
            if (blocking) {
            	if (callback.handleMessage(response)) {
            		callback.onSuccess(response);
            	}
            } else {
                ThreadPool.getInstance().execute(new TaskPool() {
                    public void run() {
                        try {
                        	if (callback.handleMessage(response)) {
                        		callback.onSuccess(response);
                        	}
                        } catch (Exception ex) {
                            callback.onFailure(ex);
                        }
                    };
                }, null);
            }
        } catch (Exception ex) {
            callback.onFailure(ex);
        } finally {
        	callback.onEndUpdating();
        }
    }
    
    /**
     * Send a <code>HTTP POST</code> to <code>url</code> and retrieve the string
     * content.
     *
     * @param url
     *            URL of the webpage you want to retrieve content.
     * @param blocking
     *            Indicate <code>HTTP GET</code> will be sent synchronous or
     *            asynchronous. Pass <code>true</code> if want to send
     *            <code>HTTP GET</code> in the same thread of caller, otherwise
     *            pass <code>false</code>.
     * @param callback
     *            A callback instance that is being called as soon as the remote
     *            service returns the result (a String object contains textual
     *            content) of this method invocation.
     */
//    public static void postResource(final String url, String content, final boolean blocking,
//            final ServiceCallback callback) {
//        Log.d("URL",url);
//        callback.onUpdating();
//        try {
//        	final JsonObject response = postContent(url, content);
//            if (blocking) {
//            	if (callback.handleMessage(response)) {
//            		callback.onSuccess(response);
//            	}
//            } else {
//                ThreadPool.getInstance().execute(new TaskPool() {
//                    public void run() {
//                        try {
//                        	if (callback.handleMessage(response)) {
//                        		callback.onSuccess(response);
//                        	}
//                        } catch (Exception ex) {
//                            callback.onFailure(ex);
//                        }
//                    };
//                }, null);
//            }
//        } catch (Exception ex) {
//            callback.onFailure(ex);
//        } finally {
//        	callback.onEndUpdating();
//        }
//    }

    /**
     * Get the textual content of a webpage through <code>HTTP GET</code>.
     */
    private static JsonObject getContent(String urlResource) {
        String result = "";
        try {
            URL url = new URL(urlResource);
            URLConnection con = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(con
                    .getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                result += line + "\n";
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JsonParser parser = new JsonParser();
		JsonObject json = parser.parse(result).getAsJsonObject();
        return json;
    }
    
    /**
     * Get the textual content of a webpage through <code>HTTP POST</code>.
     */
//    private static HttpClient httpClient;
//    private static JsonObject postContent(String urlResource, String content, cal) {
//		if (httpClient == null) {
//			httpClient = new DefaultHttpClient();
//		}
//		
//		HttpPost httpPost = new HttpPost(urlResource);
//		try {
//			httpPost.setEntity(new StringEntity(content));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		
//		HttpResponse response = null;
//		HttpEntity entity = null;
//		String result = null;
//		InputStream instream = null;
//		
//		try {
//			response = httpClient.execute(httpPost);
//			entity = response.getEntity();
//
//			if (entity != null) {
//				instream = entity.getContent();
//				result = convertStreamToString(instream);
//				instream.close();
//			}
//
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//			jsonParser.onFailure(e.getMessage());
//			return;
//		} catch (IOException e) {
//			e.printStackTrace();
//			jsonParser.onFailure(e.getMessage());
//			return;
//		}
//		
//		
//        String result = "";
//        try {
//            URL url = new URL(urlResource);
//            URLConnection con = url.openConnection();
//            con.setDoOutput(true);
//            
//            OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
//            wr.write(content);
//            wr.flush();
//            
//            BufferedReader rd = new BufferedReader(new InputStreamReader(con
//                    .getInputStream()));
//            String line;
//            while ((line = rd.readLine()) != null) {
//                result += line + "\n";
//            }
//            wr.close();
//            rd.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        
//        JsonParser parser = new JsonParser();
//		JsonObject json = parser.parse(result).getAsJsonObject();
//        return json;
//    }
    
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
}
