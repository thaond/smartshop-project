package com.smartshop.docs.server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.smartshop.docs.client.ContentPanel;

/**
 * @author VoMinhTam
 */
public class GetAPIKeyServlet extends HttpServlet {

	/**
	 * 
	 */
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
//		resp.setContentType("text/plain");
//		resp.setCharacterEncoding("UTF-8");
//		PrintWriter writer = resp.getWriter();
//
//		String source = req.getParameter("source");
//		writer.print(DatabaseUtils.registerAPIKey(source));
		
		ContentPanel.getInstance().showAPIKey("asdhakds");
	}
}
