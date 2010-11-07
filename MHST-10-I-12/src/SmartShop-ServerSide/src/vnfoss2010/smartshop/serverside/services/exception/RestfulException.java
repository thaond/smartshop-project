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
package vnfoss2010.smartshop.serverside.services.exception;

import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;

/**
 * @author H&#7912;A PHAN Minh Hi&#7871;u (rockerhieu@gmail.com)
 */
public class RestfulException extends Exception {

    private static final long serialVersionUID = -2489066639297825465L;

    public JSONObject toJSONObject() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("message", this.getClass().getSimpleName());
        obj.put("errCode", 1);
        return obj;
    }

    @Override
    public String toString() {
        try {
            return toJSONObject().toString();
        } catch (JSONException e) {
            return "{}";
        }
    }
}
