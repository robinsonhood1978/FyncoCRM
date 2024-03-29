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
	public static void send( String from, String passwd,String to,String subject,String htmlbody) {
		String host = "smtp.taijicoin.nz";
		send( host,  from,  passwd, to, subject, htmlbody);
	}
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
    	String htmlbody = "hello,when will you come to my house?";
    	SendMail.send(host, from, passwd, to, subject, htmlbody);
    }

}