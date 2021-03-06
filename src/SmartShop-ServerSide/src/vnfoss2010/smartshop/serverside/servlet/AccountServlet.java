package vnfoss2010.smartshop.serverside.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vnfoss2010.smartshop.serverside.database.entity.UserInfo;
import vnfoss2010.smartshop.serverside.test.SampleDataNghia;

public class AccountServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
		
		process(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doPost(req, resp);
		doRegister(req, resp);
//		process(req, resp);
	}
	
	private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		PrintWriter out = resp.getWriter();
		out.print("Hello");
		out.close();
	}

	private void doRegister(HttpServletRequest req, HttpServletResponse resp) {
		//String username, String password, String firstName,
//		String lastName, String phone, String email, Date birthday,
//		String address, double lat, double lng
		ArrayList<UserInfo> userInfos =  SampleDataNghia.getSampleListUserInfos();
		try {
			resp.getWriter().print("In do register");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
