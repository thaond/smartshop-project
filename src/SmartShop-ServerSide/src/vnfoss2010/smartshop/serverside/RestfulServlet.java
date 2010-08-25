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
import vnfoss2010.smartshop.serverside.services.account.AddFriendsService;
import vnfoss2010.smartshop.serverside.services.account.EditProfileService;
import vnfoss2010.smartshop.serverside.services.account.GetUserInfoService;
import vnfoss2010.smartshop.serverside.services.account.LoginService;
import vnfoss2010.smartshop.serverside.services.account.RegisterService;
import vnfoss2010.smartshop.serverside.services.account.SearchUsernameService;
import vnfoss2010.smartshop.serverside.services.comment.CreateCommentService;
import vnfoss2010.smartshop.serverside.services.comment.DeleteCommentService;
import vnfoss2010.smartshop.serverside.services.comment.GetCommentService;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.services.exception.UndefinedServiceException;
import vnfoss2010.smartshop.serverside.services.map.GeocoderService;
import vnfoss2010.smartshop.serverside.services.map.ReserveGeocoderService;
import vnfoss2010.smartshop.serverside.services.page.CreatePageService;
import vnfoss2010.smartshop.serverside.services.page.EditPageService;
import vnfoss2010.smartshop.serverside.services.page.GetPageService;
import vnfoss2010.smartshop.serverside.services.page.TagProductToPageService;
import vnfoss2010.smartshop.serverside.services.page.UntagProductFromPageService;
import vnfoss2010.smartshop.serverside.services.product.EditProductService;
import vnfoss2010.smartshop.serverside.services.product.GetBuyedProductByUserService;
import vnfoss2010.smartshop.serverside.services.product.GetInterestedProductByUserService;
import vnfoss2010.smartshop.serverside.services.product.GetListProductByCriteriaInCategoryService;
import vnfoss2010.smartshop.serverside.services.product.GetListProductByCriteriaService;
import vnfoss2010.smartshop.serverside.services.product.GetProductService;
import vnfoss2010.smartshop.serverside.services.product.GetSelledProductByUserService;
import vnfoss2010.smartshop.serverside.services.product.RegisterProductService;
import vnfoss2010.smartshop.serverside.services.product.SearchProductPromixity;
import vnfoss2010.smartshop.serverside.services.product.SearchProductService;

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
		mServices.put("account-search", SearchUsernameService.class);
		mServices.put("account-addfriend", AddFriendsService.class);
		mServices.put("account-getuser", GetUserInfoService.class);
		
		mServices.put("registerproduct", RegisterProductService.class);
		mServices.put("searchproductproximity", SearchProductPromixity.class);
		mServices.put("get-product", GetProductService.class);
		mServices.put("editproduct", EditProductService.class);
		mServices.put("create-page", CreatePageService.class);
		mServices.put("product-search-criteria", GetListProductByCriteriaService.class);
		mServices.put("product-search-criteria-cat", GetListProductByCriteriaInCategoryService.class);
		mServices.put("product-search", SearchProductService.class);mServices.put("product-get-buyed-product", GetBuyedProductByUserService.class);
		mServices.put("product-get-selled-product", GetSelledProductByUserService.class);
		mServices.put("product-get-interested-product", GetInterestedProductByUserService.class);

		// page
		mServices.put("create-page", CreatePageService.class);
		mServices.put("edit-page", EditPageService.class);
		mServices.put("get-page", GetPageService.class);
		mServices.put("tag-product-to-page", TagProductToPageService.class);
		mServices.put("untag-product-to-page", UntagProductFromPageService.class);

		// comment
		mServices.put("create-comment", CreateCommentService.class);
		mServices.put("get-comment", GetCommentService.class);
		mServices.put("delete-comment", DeleteCommentService.class);
		mServices.put("map-geocoder", GeocoderService.class);
		mServices.put("map-regeocoder", ReserveGeocoderService.class);
		
		// page
		mServices.put("create-page", CreatePageService.class);
		mServices.put("edit-page", EditPageService.class);
		mServices.put("get-page", GetPageService.class);
		mServices.put("tag-product-to-page", TagProductToPageService.class);
		mServices.put("untag-product-to-page", UntagProductFromPageService.class);
		
		// mServices.put("sampledata", SampleDataRestfull.class);
	}
}