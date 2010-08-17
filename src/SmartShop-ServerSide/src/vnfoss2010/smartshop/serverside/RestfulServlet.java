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

package vnfoss2010.smartshop.serverside;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.HelloService;
import vnfoss2010.smartshop.serverside.services.account.EditProfileService;
import vnfoss2010.smartshop.serverside.services.account.LoginService;
import vnfoss2010.smartshop.serverside.services.account.RegisterService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.services.exception.UndefinedServiceException;
import vnfoss2010.smartshop.serverside.services.product.RegisterProductService;
import vnfoss2010.smartshop.serverside.test.SampleDataRestfull;

/**
 * @author H&#7912;A PHAN Minh Hi&#7871;u (rockerhieu@gmail.com)
 */
@SuppressWarnings("serial")
public class RestfulServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		process(req, resp, null);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		InputStream is = req.getInputStream();
		DataInputStream dis = new DataInputStream(is);
		String content = "";
		while (dis.available() > 0) {
			content += dis.readLine() + "\n";
		}
		process(req, resp, content);

		try {
			dis.close();
		} catch (Exception ex) {
		}
	}

	public void process(HttpServletRequest req, HttpServletResponse resp,
			String content) throws IOException {
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer = resp.getWriter();
		
		try {
			String apiKey = req.getParameter("api");
			// TODO is this apiKey valid?

			String serviceName = req.getParameter("service");

			Class<BaseRestfulService> service = mServices.get(serviceName);
			if (service == null) {
				throw new UndefinedServiceException(serviceName);
			}

			String r = service.getConstructor(String.class).newInstance(
					serviceName).process(req.getParameterMap(), content);

			// response
			writer.print(r);
		} catch (RestfulException ex) {
			writer.print(ex.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
			writer.print("{\"error\":\"InternalServerException\"}");
		}
	}

	public static Hashtable<String, Class> mServices = new Hashtable<String, Class>();
	static {
		// TODO: put RestfulService into mServices here using #putServiceMethods
		mServices.put("hello", HelloService.class);
		mServices.put("account-register", RegisterService.class);
		mServices.put("account-editprofile", EditProfileService.class);
		mServices.put("account-login", LoginService.class);
		mServices.put("registerproduct", RegisterProductService.class);
		mServices.put("sampledata", SampleDataRestfull.class);
	}
}
