package com.cms.util;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.util.MimeMessageParser;

import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.pop3.POP3Message;

public class MailRec   {

	public static void main(String[] args) throws Exception {

 
        Properties props = new Properties();

        
        //props.setProperty("mail.debug", "false");
        
        
        props.put("mail.store.protocol", "imap");     // 协议类型
        props.put("mail.imap.host", "mail.taijicoin.nz"); // imap 主机
        props.put("mail.imap.port", 993);             // imap 端口
        props.put("mail.imap.ssl.enable", true);      // SSL 套接层

        
        Session session = Session.getDefaultInstance(props);
 
        Store store = null;
        Folder folder = null;
      String username = "robin@taijicoin.nz";
    	String password = "111111";
        try {
            store = session.getStore("imap");
            store.connect(username, password);
 
            folder = store.getFolder("INBOX");
            folder.open(Folder.READ_ONLY);
 
            int size = folder.getMessageCount();
            System.out.println( size );
            Message[] msgs = folder.getMessages();

            for(int i=0;i<size;i++){
      	  
            	 Message message = (IMAPMessage)msgs[i];
            	 MimeMessageParser parser = new MimeMessageParser((MimeMessage) message).parse();
                 System.out.println(i+"    getContentType: " + message.getContentType());
                 System.out.println(i+"    getSubject: " + message.getSubject()   ) ;
                 System.out.println(i+"    getFrom: " + parser.getFrom()  ) ;
                 System.out.println(i+"    getHtmlContent: " + parser.getHtmlContent()  ) ;
                 System.out.println(i+"    getAttachmentList: " + parser.getAttachmentList().size()  ) ;
            }
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        } finally {
            try {
                if (folder != null) {
                    folder.close(false);
                }
                if (store != null) {
                    store.close();
                }
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
 
        System.out.println("OK");
    }
	
 
}