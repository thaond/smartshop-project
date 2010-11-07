/**
 * Copyright 2010 BkitMobile
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package vnfoss2010.smartshop.serverside.services;

import java.util.Map;

import vnfoss2010.smartshop.serverside.services.exception.MissingParameterException;

import com.google.appengine.repackaged.org.json.JSONArray;
import com.google.appengine.repackaged.org.json.JSONObject;

/**
 * @author H&#7912;A PHAN Minh Hi&#7871;u (rockerhieu@gmail.com)
 */
public abstract class BaseRestfulService implements RestfulService {
	protected String[] mRequiredParameters;
	protected String mServiceName;

	public BaseRestfulService(String serviceName) {
		super();
		mServiceName = serviceName;
	}

	public BaseRestfulService(String[] requiredMethods) {
		this.mRequiredParameters = requiredMethods;
	}

	protected MissingParameterException checkParams(Map params) {
		if (mRequiredParameters != null) {
			for (String param : mRequiredParameters) {
				if (params.containsKey(param))
					continue;
				return missingParameter(param);
			}
		}
		return null;
	}

	protected MissingParameterException missingParameter(String paramName) {
		return new MissingParameterException(mServiceName, paramName);
	}

	protected String getParameter(String key, Map<String, String[]> params,
			JSONObject json) {
		try {
			if (json != null && json.has(key))
				return json.get(key).toString();
			if (params != null && params.containsKey(key)) {
				String[] arrParam = params.get(key);
				return arrParam == null ? null
						: ((arrParam.length > 0) ? arrParam[0] : null);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	protected String getParameterWithThrow(String parameterName,
			Map<String, String[]> params, JSONObject json)
			throws MissingParameterException {
		String result = getParameter(parameterName, params, json);
		if (result == null) {
			throw missingParameter(parameterName);
		}
		return result;
	}
	
	protected JSONArray getJSONArrayWithThrow(String parameterName,
			JSONObject json) throws MissingParameterException {
		JSONArray jsonArray = getJSONArray(parameterName, json);
		if (jsonArray == null) {
			throw missingParameter(parameterName);
		}
		return jsonArray;
	}
	
	protected JSONArray getJSONArray(String key, JSONObject json) {
		JSONArray result = null;
		try {
			if (json != null && json.has(key))
				result = json.getJSONArray(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
}
