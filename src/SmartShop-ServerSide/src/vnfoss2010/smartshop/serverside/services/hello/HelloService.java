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
package vnfoss2010.smartshop.serverside.services.hello;

import java.util.Map;

import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.RestfulException;
import com.google.appengine.repackaged.org.json.JSONObject;

/**
 * @author H&#7912;A PHAN Minh Hi&#7871;u (rockerhieu@gmail.com)
 */
public class HelloService extends BaseRestfulService {

    public HelloService(String name) {
        super(name);
        // mRequiredParameters = new String[] {"name"};
    }

    @Override
    public String process(Map<String, String[]> params, String content)
            throws RestfulException, Exception {
        JSONObject json = null;
        try {
            json = new JSONObject(content);
        } catch (Exception ex) {
        }
        String name = getParameter("name", params, json);
        if (name == null)
            throw missingParameter("name");
        return new JSONObject()
                .put("message", "Hello " + name + "!")
                .toString();
    }
}
