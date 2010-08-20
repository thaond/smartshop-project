package vnfoss2010.smartshop.serverside;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadHandlerServlet extends HttpServlet {

	private Logger log = null;

	public UploadHandlerServlet() {
		log = Logger.getLogger("asd");
		log.info("asdasd");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		process(req, resp);
	}

	private void process(HttpServletRequest req, HttpServletResponse resp) throws IOException {

//		resp.setContentType("text/plain");
//		resp.setCharacterEncoding("UTF-8");
//
//		PrintWriter out = resp.getWriter();
//			out.println("Hello");
		
		
		java.util.Enumeration e = req.getHeaderNames();
		while (e.hasMoreElements()) {
			String headerName = (String) e.nextElement();
			System.out.println(headerName + " = " + req.getHeader(headerName));
		}
	}

}
