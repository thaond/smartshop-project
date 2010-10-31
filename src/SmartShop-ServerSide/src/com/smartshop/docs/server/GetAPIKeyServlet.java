package com.smartshop.docs.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vnfoss2010.smartshop.serverside.database.DatabaseUtils;

/**
 * @author VoMinhTam
 */
public class GetAPIKeyServlet extends HttpServlet {

	private static final long serialVersionUID = 3408305526638544185L;

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
//		resp.setContentType("text/plain");
//		resp.setCharacterEncoding("UTF-8");
//		PrintWriter writer = resp.getWriter();
//
//		String source = req.getParameter("source");
//		String api = DatabaseUtils.getAPIKey(source).get;
//		
//		resp.setContentType("text/plain");
//        PrintWriter out= resp.getWriter();
//        int siteIndex = Math.abs(random.nextInt())%sites.size();
//        String site = (String) sites.elementAt(siteIndex);
//        res.setStatus(res.SC_MOVED_TEMPORARILY);
//        res.setHeader("Location", site);
	}
}
