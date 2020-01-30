package com.cms.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {
	// public static void send( String from, String passwd,String to,String subject,String htmlbody) {
	// 	String host = "smtp.taijicoin.nz";
	// 	send( host,  from,  passwd, to, subject, htmlbody);
	// }
    // dannel modify
	public static void send(String host,final String from,final String passwd,String to,String subject,String htmlbody) {
		// Recipient's email ID needs to be mentioned.
        //String to = "robinsonhood1978@gmail.com";

        // Sender's email ID needs to be mentioned
        //String from = "fynco.storage@gmail.com";
		//String from = "robin@taijicoin.nz";
        // Assuming you are sending email from through gmails smtp
        //String host = "smtp.gmail.com";
		//String host = "smtp.taijicoin.nz";
        // Get system properties
        // Properties properties = System.getProperties();
        
        // // Setup mail server
        // properties.put("mail.smtp.host", host);
        // properties.put("mail.smtp.port", "465");
        // properties.put("mail.smtp.ssl.enable", "true");
        // properties.put("mail.smtp.auth", "true");
        Properties properties = hostSetUp(host);

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                //return new PasswordAuthentication("fynco.storage@gmail.com", "fynco321");
            	return new PasswordAuthentication(from, passwd);

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject(subject);

            // Now set the actual message
            //message.setText("This is actual message");
         // Send the actual HTML message.
            message.setContent(htmlbody,"text/html");
            
            

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

	}
    // dannel modify a new method below:
	public static Properties hostSetUp(String mail_host) {
		Properties properties = System.getProperties();
		String host = null, port = null;
		switch (mail_host) {
		case "smtp.gmail.com":
			host = "smtp.gmail.com";
			port = "465";
			properties.put("mail.smtp.ssl.enable", "true");
			properties.put("mail.smtp.starttls.enable","false");
			break;
		case "smtp.office365.com":
			host = "smtp.office365.com";
			port = "587";
			properties.put("mail.smtp.starttls.enable","true");
			properties.put("mail.smtp.ssl.enable", "false");
			break;
		default:
			break;
		}
		properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
//        properties.put("mail.smtp.starttls.enable","true");
//        properties.put("mail.smtp.port", "465");
//        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
		return properties;
	}

	public static void send(String to,String verifycode,int type) {
		// Recipient's email ID needs to be mentioned.
        //String to = "robinsonhood1978@gmail.com";

        // Sender's email ID needs to be mentioned
        //String from = "fynco.storage@gmail.com";
		String from = "robin@taijicoin.nz";
        // Assuming you are sending email from through gmails smtp
        //String host = "smtp.gmail.com";
		String host = "smtp.taijicoin.nz";
        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                //return new PasswordAuthentication("fynco.storage@gmail.com", "fynco321");
            	return new PasswordAuthentication("robin@taijicoin.nz", "111111");

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("[Fynco] - Verify your email address");

            // Now set the actual message
            //message.setText("This is actual message");
         // Send the actual HTML message.
            switch(type){
            	case 0:
            		message.setContent(
                            " <!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" + 
                            "<html xmlns=\"http://www.w3.org/1999/xhtml\" dir=\"ltr\"> \n" + 
                            "<head>\n" + 
                            "<style type=\"text/css\">\n" + 
                            " .link:link, .link:active, .link:visited {\n" + 
                            "       color:#2672ec !important;\n" + 
                            "       text-decoration:none !important;\n" + 
                            " }\n" + 
                            "\n" + 
                            " .link:hover {\n" + 
                            "       color:#4284ee !important;\n" + 
                            "       text-decoration:none !important;\n" + 
                            " }\n" + 
                            "</style>\n" + 
                            "<title></title>\n" + 
                            "</head>\n" + 
                            "<body>\n" + 
                            "<table dir=\"ltr\">\n" + 
                            "      <tr><td id=\"i1\" style=\"padding:0; font-family:'Segoe UI Semibold', 'Segoe UI Bold', 'Segoe UI', 'Helvetica Neue Medium', Arial, sans-serif; font-size:17px; color:#707070;\">Fynco Team</td></tr>\n" + 
                            "      <tr><td id=\"i2\" style=\"padding:0; font-family:'Segoe UI Light', 'Segoe UI', 'Helvetica Neue Medium', Arial, sans-serif; font-size:41px; color:#2672ec;\">Verify your email address</td></tr>\n" + 
                            "      <tr><td id=\"i4\" style=\"padding:0; padding-top:25px; font-family:'Segoe UI', Tahoma, Verdana, Arial, sans-serif; font-size:14px; color:#2a2a2a;\">To finish setting up your Fynco account password, we just need to make sure this is your email address.</td></tr>\n" + 
                            "      <tr><td style=\"padding:0; padding-top:25px; font-family:'Segoe UI', Tahoma, Verdana, Arial, sans-serif; font-size:14px; color:#2a2a2a;\">To verify your email address, use this security code: <span style=\"font-family:'Segoe UI Bold', 'Segoe UI Semibold', 'Segoe UI', 'Helvetica Neue Medium', Arial, sans-serif; font-size:14px; font-weight:bold; color:#2a2a2a;\">"
                            +verifycode+ "</span></td></tr>\n" + 
                            "      <tr><td id=\"i6\" style=\"padding:0; padding-top:25px; font-family:'Segoe UI', Tahoma, Verdana, Arial, sans-serif; font-size:14px; color:#2a2a2a;\">If you did not request this code, you can safely ignore this email. Someone may have typed your email address by mistake.</td></tr>\n" + 
                            "      <tr><td style=\"padding:0; padding-top:25px; font-family:'Segoe UI', Tahoma, Verdana, Arial, sans-serif; font-size:14px; color:#2a2a2a;\">Thank you!</td></tr>\n" + 
                            "      <tr><td id=\"i8\" style=\"padding:0; font-family:'Segoe UI', Tahoma, Verdana, Arial, sans-serif; font-size:14px; color:#2a2a2a;\">Fynco account team</td></tr>\n" + 
                            "</table>\n" + 
                            "</body>\n" + 
                            "</html>",
                           "text/html");
            		break;
            	case 1:
            		message.setContent(
                            " <!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" + 
                            "<html xmlns=\"http://www.w3.org/1999/xhtml\" dir=\"ltr\"> \n" + 
                            "<head>\n" + 
                            "<style type=\"text/css\">\n" + 
                            " .link:link, .link:active, .link:visited {\n" + 
                            "       color:#2672ec !important;\n" + 
                            "       text-decoration:none !important;\n" + 
                            " }\n" + 
                            "\n" + 
                            " .link:hover {\n" + 
                            "       color:#4284ee !important;\n" + 
                            "       text-decoration:none !important;\n" + 
                            " }\n" + 
                            "</style>\n" + 
                            "<title></title>\n" + 
                            "</head>\n" + 
                            "<body>\n" + 
                            "<table dir=\"ltr\">\n" + 
                            "      <tr><td id=\"i1\" style=\"padding:0; font-family:'Segoe UI Semibold', 'Segoe UI Bold', 'Segoe UI', 'Helvetica Neue Medium', Arial, sans-serif; font-size:17px; color:#707070;\">Fynco Team</td></tr>\n" + 
                            "      <tr><td id=\"i2\" style=\"padding:0; font-family:'Segoe UI Light', 'Segoe UI', 'Helvetica Neue Medium', Arial, sans-serif; font-size:41px; color:#2672ec;\">your access code</td></tr>\n" + 
                            "      <tr><td id=\"i4\" style=\"padding:0; padding-top:25px; font-family:'Segoe UI', Tahoma, Verdana, Arial, sans-serif; font-size:14px; color:#2a2a2a;\">To setting up your Fynco account password, we just need to make sure this is your email address.</td></tr>\n" + 
                            "      <tr><td style=\"padding:0; padding-top:25px; font-family:'Segoe UI', Tahoma, Verdana, Arial, sans-serif; font-size:14px; color:#2a2a2a;\">To verify your email address, use this access code: <span style=\"font-family:'Segoe UI Bold', 'Segoe UI Semibold', 'Segoe UI', 'Helvetica Neue Medium', Arial, sans-serif; font-size:14px; font-weight:bold; color:#2a2a2a;\">"
                            +verifycode+ "</span></td></tr>\n" + 
                            "      <tr><td id=\"i6\" style=\"padding:0; padding-top:25px; font-family:'Segoe UI', Tahoma, Verdana, Arial, sans-serif; font-size:14px; color:#2a2a2a;\">If you did not request this code, you can safely ignore this email. Someone may have typed your email address by mistake.</td></tr>\n" + 
                            "      <tr><td style=\"padding:0; padding-top:25px; font-family:'Segoe UI', Tahoma, Verdana, Arial, sans-serif; font-size:14px; color:#2a2a2a;\">Thank you!</td></tr>\n" + 
                            "      <tr><td id=\"i8\" style=\"padding:0; font-family:'Segoe UI', Tahoma, Verdana, Arial, sans-serif; font-size:14px; color:#2a2a2a;\">Fynco account team</td></tr>\n" + 
                            "</table>\n" + 
                            "</body>\n" + 
                            "</html>",
                           "text/html");
            			break;
            }
            
            

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

	}
    public static void main(String[] args) {
    	//SendMail.send("robinsonhood1978@gmail.com", "12345678",0);
    	//(String host,final String from,final String passwd,String to,String subject,String htmlbody)
    	//String host = "smtp.taijicoin.nz";
    	String host = "smtp.gmail.com";
    	//String from = "robin@taijicoin.nz";
    	String from = "robinsonhood15@gmail.com";
    	String passwd = "paulbob123";
    	String to = "robinsonhood1978@gmail.com";
    	String subject = "Welcome you to our house";
    	String htmlbody = "<!DOCTYPE html>\n" + 
    			"<html lang=\"en-NZ\">\n" + 
    			"<head>\n" + 
    			"<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" + 
    			"<title>Hotshot- high speed broadband</title>\n" + 
    			"</head>\n" + 
    			"<body leftmargin=\"0\" marginwidth=\"0\" topmargin=\"0\" marginheight=\"0\" offset=\"0\" style=\"padding: 0;\">\n" + 
    			"    <div id=\"wrapper\" dir=\"ltr\" style=\"background-color: #f7f7f7; margin: 0; padding: 70px 0; width: 100%; -webkit-text-size-adjust: none;\">\n" + 
    			"      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\"><tr>\n" + 
    			"<td align=\"center\" valign=\"top\">\n" + 
    			"            <div id=\"template_header_image\">\n" + 
    			"                          </div>\n" + 
    			"            <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"template_container\" style=\"background-color: #ffffff; border: 1px solid #dedede; box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1); border-radius: 3px;\">\n" + 
    			"<tr>\n" + 
    			"<td align=\"center\" valign=\"top\">\n" + 
    			"                  <!-- Header -->\n" + 
    			"                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"template_header\" style='background-color: #eb5c24; color: #ffffff; border-bottom: 0; font-weight: bold; line-height: 100%; vertical-align: middle; font-family: \"Helvetica Neue\", Helvetica, Roboto, Arial, sans-serif; border-radius: 3px 3px 0 0;'><tr>\n" + 
    			"<td id=\"header_wrapper\" style=\"padding: 36px 48px; display: block;\">\n" + 
    			"                        <h1 style='font-family: \"Helvetica Neue\", Helvetica, Roboto, Arial, sans-serif; font-size: 30px; font-weight: 300; line-height: 150%; margin: 0; text-align: left; text-shadow: 0 1px 0 #ef7d50; color: #ffffff;'>Order Failed: #5725</h1>\n" + 
    			"                      </td>\n" + 
    			"                    </tr></table>\n" + 
    			"<!-- End Header -->\n" + 
    			"</td>\n" + 
    			"              </tr>\n" + 
    			"<tr>\n" + 
    			"<td align=\"center\" valign=\"top\">\n" + 
    			"                  <!-- Body -->\n" + 
    			"                  <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" id=\"template_body\"><tr>\n" + 
    			"<td valign=\"top\" id=\"body_content\" style=\"background-color: #ffffff;\">\n" + 
    			"                        <!-- Content -->\n" + 
    			"                        <table border=\"0\" cellpadding=\"20\" cellspacing=\"0\" width=\"100%\"><tr>\n" + 
    			"<td valign=\"top\" style=\"padding: 48px 48px 0;\">\n" + 
    			"                              <div id=\"body_content_inner\" style='color: #636363; font-family: \"Helvetica Neue\", Helvetica, Roboto, Arial, sans-serif; font-size: 14px; line-height: 150%; text-align: left;'>\n" + 
    			"\n" + 
    			"<p style=\"margin: 0 0 16px;\">Payment for order #5725 from test test has failed. The order was as follows:</p>\n" + 
    			"\n" + 
    			"\n" + 
    			"<h2 style='color: #eb5c24; display: block; font-family: \"Helvetica Neue\", Helvetica, Roboto, Arial, sans-serif; font-size: 18px; font-weight: bold; line-height: 130%; margin: 0 0 18px; text-align: left;'>\n" + 
    			"  <a class=\"link\" href=\"https://test3.hotshot.nz/wp-admin/post.php?post=5725&amp;action=edit\" style=\"font-weight: normal; text-decoration: underline; color: #eb5c24;\">[Order #5725]</a> (11/19/2019)</h2>\n" + 
    			"\n" + 
    			"<div style=\"margin-bottom: 40px;\">\n" + 
    			"  \n" + 
    			"</div>\n" + 
    			"\n" + 
    			"\n" + 
    			"<p style=\"margin: 0 0 16px;\">\n" + 
    			"Hopefully they’ll be back. Read more about <a href=\"https://docs.woocommerce.com/document/managing-orders/\" style=\"color: #eb5c24; font-weight: normal; text-decoration: underline;\">troubleshooting failed payments</a>.</p>\n" + 
    			"                              </div>\n" + 
    			"                            </td>\n" + 
    			"                          </tr></table>\n" + 
    			"<!-- End Content -->\n" + 
    			"</td>\n" + 
    			"                    </tr></table>\n" + 
    			"<!-- End Body -->\n" + 
    			"</td>\n" + 
    			"              </tr>\n" + 
    			"<tr>\n" + 
    			"<td align=\"center\" valign=\"top\">\n" + 
    			"                  <!-- Footer -->\n" + 
    			"                  <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"600\" id=\"template_footer\"><tr>\n" + 
    			"<td valign=\"top\" style=\"padding: 0; border-radius: 6px;\">\n" + 
    			"                        <table border=\"0\" cellpadding=\"10\" cellspacing=\"0\" width=\"100%\"><tr>\n" + 
    			"<td colspan=\"2\" valign=\"middle\" id=\"credit\" style='border-radius: 6px; border: 0; color: #f39d7c; font-family: \"Helvetica Neue\", Helvetica, Roboto, Arial, sans-serif; font-size: 12px; line-height: 125%; text-align: center; padding: 0 48px 48px 48px;'>\n" + 
    			"                              <p>HotShot Broadband – Make Broadband Better</p>\n" + 
    			"                            </td>\n" + 
    			"                          </tr></table>\n" + 
    			"</td>\n" + 
    			"                    </tr></table>\n" + 
    			"<!-- End Footer -->\n" + 
    			"</td>\n" + 
    			"              </tr>\n" + 
    			"</table>\n" + 
    			"</td>\n" + 
    			"        </tr></table>\n" + 
    			"</div>\n" + 
    			"  </body>\n" + 
    			"</html>";
    	SendMail.send(host, from, passwd, to, subject, htmlbody);
    }

}