package vnfoss2010.smartshop.serverside.mail;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.database.ServiceResult;

public abstract class MailUtils {
	private static Logger logger = Logger.getLogger(MailUtils.class.getName());

	public static ServiceResult<Void> sendEmailToAdmin(String sender, String title, String content) {
		ServiceResult<Void> result = new ServiceResult<Void>();
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		content = "Sender: " + sender + "\n\n" + content;

		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("vo.mita.ov@gmail.com")); // This is
			// terrible
			// thing
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					"admins"));

			msg.setSubject(title, "UTF-8");
			msg.setText(content, "UTF-8");
			msg.setHeader("Content-Type", "text/html; charset=UTF-8");

			Transport.send(msg);
			
			result.setOK(true);
			result.setMessage(Global.messages.getString("send_mail_successfully"));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			result.setMessage(Global.messages.getString("send_mail_fail"));
		}

		return result;
	}

	public static ServiceResult<Void> sendEmail(String from, String to, String title,
			String content) {
		ServiceResult<Void> result = new ServiceResult<Void>();
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			msg.setSubject(title, "UTF-8");
			msg.setText(content, "UTF-8");
			msg.setHeader("Content-Type", "text/html; charset=UTF-8");

			Transport.send(msg);
			
			result.setOK(true);
			result.setMessage(Global.messages.getString("send_mail_successfully"));
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage());
			result.setMessage(Global.messages.getString("send_mail_fail"));
		}
		return result;
	}
}
