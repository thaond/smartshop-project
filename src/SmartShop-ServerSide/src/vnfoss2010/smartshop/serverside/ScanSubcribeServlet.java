package vnfoss2010.smartshop.serverside;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vnfoss2010.smartshop.serverside.database.AccountServiceImpl;
import vnfoss2010.smartshop.serverside.database.ServiceResult;
import vnfoss2010.smartshop.serverside.database.UserSubcribeProductImpl;
import vnfoss2010.smartshop.serverside.database.entity.Product;
import vnfoss2010.smartshop.serverside.database.entity.UserInfo;
import vnfoss2010.smartshop.serverside.database.entity.UserSubcribeProduct;
import vnfoss2010.smartshop.serverside.mail.MailUtils;

public class ScanSubcribeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ScanSubcribeServlet.class
			.getName());
	AccountServiceImpl dbAccount = AccountServiceImpl.getInstance();
	UserSubcribeProductImpl dbSubcribe = UserSubcribeProductImpl.instance();

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		process(req, resp, null);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		process(req, resp, null);
	}

	public void process(HttpServletRequest req, HttpServletResponse resp,
			String content) throws IOException {
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter writer = resp.getWriter();

		try {
			ServiceResult<List<UserSubcribeProduct>> searchSubsResult = dbSubcribe
					.findActiveSubcribe();
			if (searchSubsResult.isOK()) {
				for (UserSubcribeProduct subcribe : searchSubsResult
						.getResult()) {
					ServiceResult<List<Product>> searchProducts = dbSubcribe
							.searchProductInSubcribeRange(subcribe);
					if (searchProducts.isOK()) {
						if (searchProducts.getResult().size() > 0) {
							ServiceResult<UserInfo> searchUser = dbAccount
									.getUserInfo(subcribe.getUserName());
							if (searchUser.isOK()) {
								UserInfo userInfo = searchUser.getResult();
								sendMail(userInfo, searchProducts.getResult(),
										subcribe);
							} else {
								writer
										.println("Exception khi search user cua subcribe "
												+ subcribe.getId()
												+ " : "
												+ searchUser.getMessage());
							}
						}
					} else {
						writer.println("Exception khi search product cua sub "
								+ subcribe.getId() + " : "
								+ searchProducts.getMessage());
					}

				}
			} else {
				writer.println(searchSubsResult.getMessage());
			}

			// response
			// writer.print(r);
		} catch (Exception ex) {
			ex.printStackTrace();
			writer.print("{\"error\":\"InternalServerException\"}");
		}
	}

//	private String sendMails(UserInfo userInfo, List<Product> products,
//			UserSubcribeProduct subcribe) {
//		String title = "Base on subcribe id[" + subcribe.getId()
//				+ "], We send you an email";
//		String content = "";
//		for (Product product : products) {
//			content += "[" + product.getId() + "], name" + product.getName()
//					+ ", price : " + product.getPrice() + ", description : "
//					+ product.getDescription() + "<br/>";
//		}
//		return title + "_" + content + "_" + userInfo.getEmail();
//	}

	private void sendMail(UserInfo userInfo, List<Product> products,
			UserSubcribeProduct subcribe) {
		String title = "Base on subcribe id[" + subcribe.getId()
				+ "], We send you an email";
		String content = "";
		for (Product product : products) {
			content += "[" + product.getId() + "], name" + product.getName()
					+ ", price : " + product.getPrice() + ", description : "
					+ product.getDescription();
		}
		MailUtils.sendEmail("admins", userInfo.getEmail(), title, content);
	}
}
