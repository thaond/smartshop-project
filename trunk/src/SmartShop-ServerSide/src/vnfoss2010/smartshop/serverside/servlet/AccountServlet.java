package vnfoss2010.smartshop.serverside.servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vnfoss2010.smartshop.serverside.database.entity.UserInfo;

public class AccountServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
		
	}

	private void doRegister(HttpServletRequest req, HttpServletResponse resp) {
		//String username, String password, String firstName,
//		String lastName, String phone, String email, Date birthday,
//		String address, double lat, double lng
	}
	
	
}
