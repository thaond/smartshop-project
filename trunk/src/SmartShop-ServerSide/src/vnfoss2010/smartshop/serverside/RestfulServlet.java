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
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vnfoss2010.smartshop.serverside.authentication.SessionObject;
import vnfoss2010.smartshop.serverside.database.DatabaseUtils;
import vnfoss2010.smartshop.serverside.database.entity.APIKey;
import vnfoss2010.smartshop.serverside.services.BaseRestfulService;
import vnfoss2010.smartshop.serverside.services.HelloService;
import vnfoss2010.smartshop.serverside.services.account.AddFriendsService;
import vnfoss2010.smartshop.serverside.services.account.EditProfileService;
import vnfoss2010.smartshop.serverside.services.account.GetUserInfoService;
import vnfoss2010.smartshop.serverside.services.account.LoginService;
import vnfoss2010.smartshop.serverside.services.account.RegisterService;
import vnfoss2010.smartshop.serverside.services.account.SearchUsernameService;
import vnfoss2010.smartshop.serverside.services.cat.GetAllCategoriesService;
import vnfoss2010.smartshop.serverside.services.cat.GetSubCategoriesService;
import vnfoss2010.smartshop.serverside.services.comment.CreateCommentService;
import vnfoss2010.smartshop.serverside.services.comment.DeleteCommentService;
import vnfoss2010.smartshop.serverside.services.comment.GetCommentService;
import vnfoss2010.smartshop.serverside.services.comment.GetCommentsByUsernameService;
import vnfoss2010.smartshop.serverside.services.exception.InvalidAPIKeyException;
import vnfoss2010.smartshop.serverside.services.exception.InvalidSessionException;
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.services.exception.SessionExpiredException;
import vnfoss2010.smartshop.serverside.services.exception.UndefinedServiceException;
import vnfoss2010.smartshop.serverside.services.mail.SendEmailService;
import vnfoss2010.smartshop.serverside.services.mail.SendEmailToAdminService;
import vnfoss2010.smartshop.serverside.services.map.DirectionService;
import vnfoss2010.smartshop.serverside.services.map.GeocoderService;
import vnfoss2010.smartshop.serverside.services.map.ReserveGeocoderService;
import vnfoss2010.smartshop.serverside.services.notification.DeleteAllNotificationsService;
import vnfoss2010.smartshop.serverside.services.notification.DeleteNotificationsByUsernameService;
import vnfoss2010.smartshop.serverside.services.notification.EditNotificationService;
import vnfoss2010.smartshop.serverside.services.notification.GetNotificationsByUsernameService;
import vnfoss2010.smartshop.serverside.services.notification.InsertNotificationService;
import vnfoss2010.smartshop.serverside.services.notification.MarkAsReadNotificationsByUsernameService;
import vnfoss2010.smartshop.serverside.services.page.CreatePageService;
import vnfoss2010.smartshop.serverside.services.page.EditPageService;
import vnfoss2010.smartshop.serverside.services.page.GetListPageByCriteriaService;
import vnfoss2010.smartshop.serverside.services.page.GetPageService;
import vnfoss2010.smartshop.serverside.services.page.GetPagesByUsernameService;
import vnfoss2010.smartshop.serverside.services.page.SearchPageByQueryService;
import vnfoss2010.smartshop.serverside.services.parser.vatgia.NProductInfoService;
import vnfoss2010.smartshop.serverside.services.parser.vatgia.ProductInfoService;
import vnfoss2010.smartshop.serverside.services.parser.vatgia.SearchKeywordService;
import vnfoss2010.smartshop.serverside.services.product.EditProductService;
import vnfoss2010.smartshop.serverside.services.product.GetBuyedProductByUserService;
import vnfoss2010.smartshop.serverside.services.product.GetInterestedProductByUserService;
import vnfoss2010.smartshop.serverside.services.product.GetListProductByCriteriaInCategoryService;
import vnfoss2010.smartshop.serverside.services.product.GetProductService;
import vnfoss2010.smartshop.serverside.services.product.GetProductsByUsernameService;
import vnfoss2010.smartshop.serverside.services.product.GetSelledProductByUserService;
import vnfoss2010.smartshop.serverside.services.product.RegisterProductService;
import vnfoss2010.smartshop.serverside.services.product.SearchProductPromixity;
import vnfoss2010.smartshop.serverside.services.product.SearchProductService;
import vnfoss2010.smartshop.serverside.services.sms.SendSMSService;
import vnfoss2010.smartshop.serverside.services.sms.SendSMSToService;
import vnfoss2010.smartshop.serverside.services.test.InsertCategoryService;
import vnfoss2010.smartshop.serverside.services.test.InsertPageService;
import vnfoss2010.smartshop.serverside.services.test.InsertSampleProductService;
import vnfoss2010.smartshop.serverside.services.test.InsertUserInfosService;
import vnfoss2010.smartshop.serverside.services.usersubcribeproduct.CreateSubcribeProduct;
import vnfoss2010.smartshop.serverside.services.usersubcribeproduct.EditSubcribe;
import vnfoss2010.smartshop.serverside.services.usersubcribeproduct.FindUserSubcribes;
import vnfoss2010.smartshop.serverside.services.usersubcribeproduct.GetProductInSubcribeRange;
import vnfoss2010.smartshop.serverside.services.usersubcribeproduct.GetProductInSubcribeRangeByUsername;
import vnfoss2010.smartshop.serverside.services.usersubcribeproduct.GetSubcribe;
import vnfoss2010.smartshop.serverside.services.usersubcribeproduct.GetUserSubscribeProductByUsername;
import vnfoss2010.smartshop.serverside.utils.StringUtils;
import vnfoss2010.smartshop.serverside.utils.UtilsFunction;

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
			String api = req.getParameter("api");
			String serviceName = req.getParameter("service");

			if (!Global.listAPIKeys.contains(api)){
				APIKey apiKey = DatabaseUtils.getAPIKey(UtilsFunction.decrypt(api));
				if (apiKey==null)
					throw new InvalidAPIKeyException(serviceName);
				else
					Global.listAPIKeys.add(api);
			}

			String r;
			Class<BaseRestfulService> service = unAuthorizedServices
					.get(serviceName);
			if (service == null) {
				service = authorizedServices.get(serviceName);
				if (service == null)
					throw new UndefinedServiceException(serviceName);

				// Check whether valid session or not
				String sessionId = req.getParameter("session");
				SessionObject so = StringUtils.isEmptyOrNull(sessionId) ? null
						: UtilsFunction.getSessionObject(sessionId);
				if (so == null) {
					// Invalid session
					throw new InvalidSessionException(serviceName);
				} else if (new Date().getTime() - so.timeStamp > Global.SESSION_EXPRIED) {
					// Session expired
					throw new SessionExpiredException(serviceName);
				}

				Map hashMap = new HashMap(req.getParameterMap());
				hashMap.put("username", new String[]{so.username});
				r = service.getConstructor(String.class).newInstance(
						serviceName).process(hashMap, content);
				
			}else{
				r = service.getConstructor(String.class).newInstance(
						serviceName).process(req.getParameterMap(), content);
			}
			

			// response
			writer.print(r);
		} catch (RestfulException ex) {
			writer.print(ex.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
			writer.print("{\"error\":\"InternalServerException\"}");
		}
	}

	public static Hashtable<String, Class> unAuthorizedServices = new Hashtable<String, Class>();
	static {
		unAuthorizedServices.put("hello", HelloService.class);
		unAuthorizedServices.put("account-register", RegisterService.class);
		unAuthorizedServices.put("account-login", LoginService.class);
		// unAuthorizedServices.put("account-addfriend",
		// AddFriendsService.class);
		unAuthorizedServices.put("account-search", SearchUsernameService.class);
		unAuthorizedServices.put("account-getuser", GetUserInfoService.class);
		// unAuthorizedServices.put("account-editprofile",
		// EditProfileService.class);

		// category
		unAuthorizedServices.put("category-get-all",
				GetAllCategoriesService.class);
		unAuthorizedServices.put("category-get-sub",
				GetSubCategoriesService.class);

		// product
		unAuthorizedServices.put("searchproductproximity",
				SearchProductPromixity.class);
		unAuthorizedServices.put("get-product", GetProductService.class);
		// unAuthorizedServices.put("registerproduct",
		// RegisterProductService.class);
		// unAuthorizedServices.put("editproduct", EditProductService.class);

		// page
		// unAuthorizedServices.put("product-search-criteria",
		// GetListProductByCriteriaService.class);
		unAuthorizedServices.put("product-search-criteria-cat",
				GetListProductByCriteriaInCategoryService.class);
		unAuthorizedServices.put("product-search", SearchProductService.class);
		unAuthorizedServices.put("product-get-buyed-product",
				GetBuyedProductByUserService.class);
		unAuthorizedServices.put("product-get-selled-product",
				GetSelledProductByUserService.class);
		unAuthorizedServices.put("product-get-interested-product",
				GetInterestedProductByUserService.class);
		unAuthorizedServices.put("product-get-by-username",
				GetProductsByUsernameService.class);

		// page
		unAuthorizedServices.put("get-page", GetPageService.class);
		unAuthorizedServices.put("page-search", SearchPageByQueryService.class);
		unAuthorizedServices.put("page-search-criteria",
				GetListPageByCriteriaService.class);
		unAuthorizedServices.put("page-get-by-username",
				GetPagesByUsernameService.class);
		// unAuthorizedServices.put("create-page", CreatePageService.class);
		// unAuthorizedServices.put("page-edit", EditPageService.class);
		// unAuthorizedServices.put("tag-product-to-page",
		// TagProductToPageService.class);
		// unAuthorizedServices.put("untag-product-to-page",
		// UntagProductFromPageService.class);

		// comment
		// unAuthorizedServices.put("create-comment",
		// CreateCommentService.class);
		unAuthorizedServices.put("get-comment", GetCommentService.class);
		// unAuthorizedServices.put("delete-comment",
		// DeleteCommentService.class);
		unAuthorizedServices.put("comment-get-by-username",
				GetCommentsByUsernameService.class);

		// notification
		unAuthorizedServices
				.put("noti-insert", InsertNotificationService.class);
		// unAuthorizedServices.put("noti-delete-all-by",
		// DeleteNotificationsByUsernameService.class);
		unAuthorizedServices.put("noti-delete-all",
				DeleteAllNotificationsService.class);
		// unAuthorizedServices.put("noti-edit", EditNotificationService.class);
		unAuthorizedServices.put("noti-get-by",
				GetNotificationsByUsernameService.class);
		// unAuthorizedServices.put("noti-mark-as-read",
		// MarkAsReadNotificationsByUsernameService.class);

		// map
		unAuthorizedServices.put("map-geocoder", GeocoderService.class);
		unAuthorizedServices
				.put("map-regeocoder", ReserveGeocoderService.class);
		unAuthorizedServices.put("map-direction", DirectionService.class);

		// mail
		// unAuthorizedServices.put("mail-send-to-admin",
		// SendEmailToAdminService.class);
		// unAuthorizedServices.put("mail-send", SendEmailService.class);

		// sms
		// unAuthorizedServices.put("sms-send-from-to", SendSMSService.class);
		// unAuthorizedServices.put("sms-send-to", SendSMSToService.class);

		// user subcribe
		// unAuthorizedServices
		// .put("create-subcribe", CreateSubcribeProduct.class);
		unAuthorizedServices.put("get-products-in-sub-range",
				GetProductInSubcribeRange.class);
		// unAuthorizedServices.put("edit-subcribe", EditSubcribe.class);
		unAuthorizedServices.put("get-subscribe-by-user",
				GetUserSubscribeProductByUsername.class);
		unAuthorizedServices.put("get-subscribe-product-by-user",
				GetProductInSubcribeRangeByUsername.class);
		unAuthorizedServices.put("find-subcribes", FindUserSubcribes.class);
		unAuthorizedServices.put("get-subcribe", GetSubcribe.class);

		unAuthorizedServices.put("sampledata-cat", InsertCategoryService.class);
		unAuthorizedServices.put("sampledata-product",
				InsertSampleProductService.class);
		unAuthorizedServices.put("sampledata-user",
				InsertUserInfosService.class);
		unAuthorizedServices.put("sampledata-page", InsertPageService.class);

		// parser
		unAuthorizedServices.put("parser-vatgia-each-product",
				ProductInfoService.class);
		unAuthorizedServices.put("parser-vatgia-each-product-n",
				NProductInfoService.class);
		unAuthorizedServices.put("parser-vatgia-keyword",
				SearchKeywordService.class);
		
		//other
		unAuthorizedServices.put("get-api-key", GetAPIKeyService.class);
	}

	public static Hashtable<String, Class> authorizedServices = new Hashtable<String, Class>();
	static {
		authorizedServices.put("account-editprofile", EditProfileService.class);
		authorizedServices.put("account-addfriend", AddFriendsService.class);

		// product
		authorizedServices.put("registerproduct", RegisterProductService.class);
		authorizedServices.put("editproduct", EditProductService.class);

		// page
		authorizedServices.put("create-page", CreatePageService.class);
		authorizedServices.put("page-edit", EditPageService.class);

		// comment
		authorizedServices.put("create-comment", CreateCommentService.class);
		authorizedServices.put("get-comment", GetCommentService.class);
		authorizedServices.put("delete-comment", DeleteCommentService.class);
		authorizedServices.put("comment-get-by-username",
				GetCommentsByUsernameService.class);

		// notification
		authorizedServices.put("noti-delete-all-by",
				DeleteNotificationsByUsernameService.class);
		authorizedServices.put("noti-edit", EditNotificationService.class);
		authorizedServices.put("noti-mark-as-read",
				MarkAsReadNotificationsByUsernameService.class);

		// mail
		authorizedServices.put("mail-send-to-admin",
				SendEmailToAdminService.class);
		authorizedServices.put("mail-send", SendEmailService.class);

		// sms
		authorizedServices.put("sms-send-from-to", SendSMSService.class);
		authorizedServices.put("sms-send-to", SendSMSToService.class);

		// user subcribe
		authorizedServices.put("create-subcribe", CreateSubcribeProduct.class);
		authorizedServices.put("edit-subcribe", EditSubcribe.class);

		authorizedServices.put("sampledata-cat", InsertCategoryService.class);
		authorizedServices.put("sampledata-product",
				InsertSampleProductService.class);
		authorizedServices.put("sampledata-user", InsertUserInfosService.class);
		authorizedServices.put("sampledata-page", InsertPageService.class);
	}
}
