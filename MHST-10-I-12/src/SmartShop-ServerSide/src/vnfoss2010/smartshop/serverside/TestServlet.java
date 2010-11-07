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
import vnfoss2010.smartshop.serverside.services.exception.RestfulException;
import vnfoss2010.smartshop.serverside.services.exception.UndefinedServiceException;
import vnfoss2010.smartshop.serverside.services.test.DeleteDatabaseService;
import vnfoss2010.smartshop.serverside.services.test.DeleteUserInfosService;
import vnfoss2010.smartshop.serverside.services.test.InsertAllSampleDataService;
import vnfoss2010.smartshop.serverside.services.test.InsertCategoryService;
import vnfoss2010.smartshop.serverside.services.test.InsertPageService;
import vnfoss2010.smartshop.serverside.services.test.InsertSampleNotificationService;
import vnfoss2010.smartshop.serverside.services.test.InsertSampleProductService;
import vnfoss2010.smartshop.serverside.services.test.InsertUserInfosService;
import vnfoss2010.smartshop.serverside.services.test.TestProductAtt;

public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
		mServices.put("insert-userinfos", InsertUserInfosService.class);
		mServices.put("delete-userinfos", DeleteUserInfosService.class);
		mServices.put("insert-page", InsertPageService.class);
		mServices.put("insert-cats", InsertCategoryService.class);
		mServices.put("insert-products", InsertSampleProductService.class);
		mServices.put("insert-notifications", InsertSampleNotificationService.class);
		mServices.put("delete-database", DeleteDatabaseService.class);
		mServices.put("insert-all-sample-data", InsertAllSampleDataService.class);
		
		mServices.put("test-pro-att", TestProductAtt.class);
	}
}
