package com.cms.util;


	import javax.mail.*;
	import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import javax.mail.search.FlagTerm;

import java.io.*;
	import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

	/**
	 * Created by rightHead on 2016/8/15.
	 */
	public class ReadMail {

	    private String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	    private List<Object> list;
	    private String cate = "E:/";
	    private String name, pass;  //  邮箱地址 和 授权码

	    public List<Object> getList() {
	        return list;
	    }

	    public void setParameter(String name, String pass, String road){
	        cate = road;
	        this.name = name;
	        this.pass = pass;
	    }
	    public void start(){


	        list = new ArrayList<>();
	        readMails(name, pass);

	    }

	    public boolean connect(){
	        return connectTest(name, pass);
	    }
	    private boolean connectTest(String username, String pass){
	        //Security.addProvider(new Provider());
	        Properties props = System.getProperties();
	        props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
	        props.setProperty("mail.imap.socketFactory.fallback", "false");
	        props.setProperty("mail.store.protocol", "imap");
	        props.setProperty("mail.imap.host", "imap.qq.com");
	        props.setProperty("mail.imap.port", "993");
	        props.setProperty("mail.imap.socketFactory.port", "993");

	        Session session = Session.getDefaultInstance(props,null);

	        URLName urln = new URLName("imap","imap.qq.com",993,null,
	                username, pass);
	        boolean f = false;

	        Folder inbox = null;
	        try{
	            Store store = session.getStore(urln);
	            store.connect();
	            inbox = store.getFolder("INBOX");
	            inbox.open(Folder.READ_ONLY);
	            f = inbox.exists();
	        }catch (MessagingException e){
	            e.printStackTrace();
	        }
	        return f;
	    }
	    public Map<String, Object> readMails(String username,String pass){
	    	Map<String, Object> map = new HashMap<String, Object>();
	    	ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
	        Properties props = System.getProperties();
	        props.setProperty("mail.imap.socketFactory.class", SSL_FACTORY);
	        props.setProperty("mail.imap.socketFactory.fallback", "false");
	        props.setProperty("mail.store.protocol", "imap");
	        props.setProperty("mail.imap.host", "mail.taijicoin.nz");
	        props.setProperty("mail.imap.port", "993");
	        props.setProperty("mail.imap.socketFactory.port", "993");

	        Session session = Session.getDefaultInstance(props,null);

	        URLName urln = new URLName("imap","mail.taijicoin.nz",993,null,
	                username, pass);

	        Store store = null;
	        String msg = "";

	        Folder inbox = null;
	        Message[] messages = null;
	        try {
	            store = session.getStore(urln);
	            store.connect();
	            inbox = store.getFolder("INBOX");
	            inbox.open(Folder.READ_ONLY);
	            FetchProfile profile = new FetchProfile();
	            profile.add(FetchProfile.Item.ENVELOPE);
	            messages = inbox.getMessages();
	            inbox.fetch(messages, profile);
	            map.put("total",messages.length);
	            map.put("unread",inbox.getUnreadMessageCount());
	            map.put("new",inbox.getNewMessageCount());
	            System.out.println(messages.length);
	            System.out.println(inbox.getUnreadMessageCount() + "封未读邮件！");
	            System.out.println(inbox.getNewMessageCount() + "封新邮件!");
	            System.out.println("你好，即将遍历消息内容！");
	            for (Message m : messages){
	            	
	            	HashMap<String, Object> mp = new HashMap<String, Object>();
	            	String isRead = "Unread";
	                if (m.getFlags().contains(Flags.Flag.SEEN)){
	                    isRead = "Seen";
	                }
	                mp.put("status", isRead);
	                mp.put("send_date", m.getSentDate());
	                Address[] froms = m.getFrom();
	    	        if(froms != null) {
	    	            System.out.println("即将打印发件人地址。。。");
	    	            InternetAddress addr = (InternetAddress)froms[0];
	    	            System.out.println("发件人地址:" + addr.getAddress());
	    	            mp.put("sender_address", addr.getAddress());
	    	            mp.put("sender_showname", addr.getPersonal());
	    	            System.out.println("发件人显示名:" + addr.getPersonal());
	    	        }
	    	        System.out.println("邮件主题:" + m.getSubject());
	    	        mp.put("subject", m.getSubject());
	    	        
	    	        ArrayList<Object> ct = new ArrayList<Object>();
//	    	        Object o = m.getContent();
//	    	        if(o instanceof Multipart) {
//	    	            System.out.println("Multipart");
//	    	            Multipart multipart = (Multipart) o ;
//	    	            System.out.println("邮件共有" + multipart.getCount() + "部分组成");
//	    		        System.out.println("即将一次处理各部分");
//	    		        // 依次处理各个部分
//	    		        for (int j = 0, n = multipart.getCount(); j < n; j++) {
//	    		            System.out.println("处理第" + j + "部分");
//	    		            Part part = multipart.getBodyPart(j);//解包, 取出 MultiPart的各个部分, 每部分可能是邮件内容,
//	    		            // 也可能是另一个小包裹(MultipPart)
//	    		            // 判断此包裹内容是不是一个小包裹, 一般这一部分是 正文 Content-Type: multipart/alternative
//	    		            if (part.getContent() instanceof Multipart) {
//	    		                System.out.println("此部分仍有小包裹，将迭代处理！");
//	    		                Multipart p = (Multipart) part.getContent();// 转成小包裹
//	    		                //递归迭代
//	    		                //reMultipart(p);
//	    		            } else {
//	    		            	ct.add(part.getContent());
//	    		            	if(part.getContentType().startsWith("TEXT/PLAIN")) {
//	    	    	                System.out.println("text内容：" + part.getContent());
//	    	    	                //bean.setUrl((String) part.getContent());
//	    	    	            } else {
//	    	    	                System.out.println("HTML内容：" + part.getContent());
//	    	    	            }
//	    		            }
//	    		        }
//	    	        } else if (o instanceof Part){
//	    	            System.out.println("Part");
//	    	            Part part = (Part) o;
//	    	            ct.add(part.getContent());
//	    	            if(part.getContentType().startsWith("TEXT/PLAIN")) {
//	    	                System.out.println("text内容：" + part.getContent());
//	    	                //bean.setUrl((String) part.getContent());
//	    	            } else {
//	    	                System.out.println("HTML内容：" + part.getContent());
//	    	            }
//	    	        } else {
//	    	        	ct.add(m.getContent());
//	    	            System.out.println("既不是Multipart也不是Part");
//	    	            System.out.println("类型" + m.getContentType());
//	    	            System.out.println("内容" + m.getContent());
//	    	        }
	    	        mp.put("content", ct);
	                //System.out.println("发现一封未读邮件即将分析！");
	                //mailReceiver(m);
	                //System.out.println("————-————————————————————————————");
	      /*          IMAPMessage ms = (IMAPMessage) m;
	                System.out.println("11");
	                String subject = MimeUtility.decodeText(ms.getSubject());
	                System.out.println("22");*/
	                //bean = new Bean();
	                
	                //list.add(bean);
	       //         m.setFlag(Flags.Flag.SEEN, true);
	    	        list.add(mp);
	                
	            }
	            map.put("list", list);

	        } catch (MessagingException e) {
	            // TODO Auto-generated catch block
	            msg = "0";
	        } finally{
	            try {
	                inbox.close(false);
	                store.close();
	            } catch (MessagingException e) {
	                // TODO Auto-generated catch block
	                //e.printStackTrace();
	            }finally{
	        		return map;
	            }
	        }
	    }

	    private void mailReceiver(Message msg)throws Exception{
	        // 发件人信息
	        System.out.println("分析邮件内容");
	        System.out.println("获取发件人地址");
	        Address[] froms = msg.getFrom();
	        if(froms != null) {
	            System.out.println("即将打印发件人地址。。。");
	            InternetAddress addr = (InternetAddress)froms[0];
	            System.out.println("发件人地址:" + addr.getAddress());
	            //bean.setPersonAddress(addr.getAddress());
	            //bean.setPersonName(addr.getPersonal());
	            System.out.println("发件人显示名:" + addr.getPersonal());
	        }
	        System.out.println("邮件主题:" + msg.getSubject());
	        //bean.setTheme(msg.getSubject());
	        // getContent() 是获取包裹内容, Part相当于外包装
	        System.out.println("即将分析邮件内容。。。");
	        Object o = msg.getContent();
	        if(o instanceof Multipart) {
	            System.out.println("Multipart");
	            Multipart multipart = (Multipart) o ;
	            reMultipart(multipart);
	        } else if (o instanceof Part){
	            System.out.println("Part");
	            Part part = (Part) o;
	            rePart(part);
	        } else {
	            System.out.println("既不是Multipart也不是Part");
	            System.out.println("类型" + msg.getContentType());
	            System.out.println("内容" + msg.getContent());
	        }
	    }
	    /**
	     * @param part 解析内容
	     * @throws Exception
	     */
	    private void rePart(Part part) throws MessagingException, IOException{
	        System.out.println("进入rePart");
	            if(part.getContentType().startsWith("TEXT/PLAIN")) {
	                System.out.println("text内容：" + part.getContent());
	                //bean.setUrl((String) part.getContent());
	            } else {
	                System.out.println("HTML内容：" + part.getContent());
	            }
	    }

	    /**
	     * @param multipart // 接卸包裹（含所有邮件内容(包裹+正文+附件)）
	     * @throws Exception
	     */
	    private void reMultipart(Multipart multipart) throws Exception {
	        System.out.println("邮件共有" + multipart.getCount() + "部分组成");
	        System.out.println("即将一次处理各部分");
	        // 依次处理各个部分
	        for (int j = 0, n = multipart.getCount(); j < n; j++) {
	            System.out.println("处理第" + j + "部分");
	            Part part = multipart.getBodyPart(j);//解包, 取出 MultiPart的各个部分, 每部分可能是邮件内容,
	            // 也可能是另一个小包裹(MultipPart)
	            // 判断此包裹内容是不是一个小包裹, 一般这一部分是 正文 Content-Type: multipart/alternative
	            if (part.getContent() instanceof Multipart) {
	                System.out.println("此部分仍有小包裹，将迭代处理！");
	                Multipart p = (Multipart) part.getContent();// 转成小包裹
	                //递归迭代
	                reMultipart(p);
	            } else {
	                rePart(part);
	            }
	        }
	    }
		//dannel modify below:
		public String hostConvertor(String mail_host) {
	    	String host;
			switch (mail_host) {
			case "gmail":
				host = "smtp.gmail.com";
				break;
			case "outlook":
				host = "smtp.office365.com";
				break;
			default:
				host = null;
				break;
			}
	    	return host;
		}
	    public Map<String, Object> getMails(String mail_host,String username,String pass){
	    	Map<String, Object> map = new HashMap<String, Object>();
	    	ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			String host = hostConvertor(mail_host);//get host dannel modify
	        // 准备连接服务器的会话信息 
	        Properties props = new Properties(); 
	        props.setProperty("mail.store.protocol", "imap"); 
	        // props.setProperty("mail.imap.host", "mail.taijicoin.nz"); 
	        //props.setProperty("mail.imap.host", "imap.gmail.com"); 
			props.setProperty("mail.imap.host", host);//dannel modify
	        props.setProperty("mail.imap.port", "993");
	        
	        /**  QQ邮箱需要建立ssl连接 */
	        props.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	        props.setProperty("mail.imap.socketFactory.fallback", "false");
	        props.setProperty("mail.imap.starttls.enable","true");
	        props.setProperty("mail.imap.socketFactory.port", "993");
	        Store store = null;
	        Folder inbox = null;
	        try {
	        // 创建Session实例对象 
	        Session session = Session.getInstance(props);
			URLName urln = new URLName("imap", host, 993, null,username,pass);//dannel modify
	        // URLName urln = new URLName("imap", "mail.taijicoin.nz", 993, null,username,pass);
	        //URLName urln = new URLName("imap", "imap.gmail.com", 993, null,username,pass);
	        // 创建IMAP协议的Store对象 
	         store = session.getStore(urln);
	        store.connect();

	        // 获得收件箱 
	         inbox = store.getFolder("INBOX"); 
	        // 以读写模式打开收件箱 
	        inbox.open(Folder.READ_WRITE); 

	        // 获得收件箱的邮件列表 
	        // Message[] messages = inbox.getMessages(); 
			Message[] messages = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));//dannel modify for only shows unread mail inbox

	        // 打印不同状态的邮件数量 
	        System.out.println("收件箱中共" + messages.length + "封邮件!"); 
	        System.out.println("收件箱中共" + inbox.getUnreadMessageCount() + "封未读邮件!"); 
	        System.out.println("收件箱中共" + inbox.getNewMessageCount() + "封新邮件!"); 
	        System.out.println("收件箱中共" + inbox.getDeletedMessageCount() + "封已删除邮件!"); 

	        System.out.println("------------------------开始解析邮件----------------------------------"); 
	        
	        	map.put("total",messages.length);
	            map.put("unread",inbox.getUnreadMessageCount());
	            map.put("new",inbox.getNewMessageCount());
	            System.out.println(messages.length);
	            System.out.println(inbox.getUnreadMessageCount() + "封未读邮件！");
	            System.out.println(inbox.getNewMessageCount() + "封新邮件!");
	            System.out.println("你好，即将遍历消息内容！");
	            BufferedReader reader = new BufferedReader(new InputStreamReader(
	          	      System.in));
	            for (Message m : messages){
	            	
	            	HashMap<String, Object> mp = new HashMap<String, Object>();
	            	String isRead = "Unread";
	                if (m.getFlags().contains(Flags.Flag.SEEN)){
	                    isRead = "Seen";
	                }
	                mp.put("status", isRead);
	                mp.put("send_date", m.getSentDate());
	                Address[] froms = m.getFrom();
	    	        if(froms != null) {
	    	            //System.out.println("即将打印发件人地址。。。");
	    	            InternetAddress addr = (InternetAddress)froms[0];
	    	            //System.out.println("发件人地址:" + addr.getAddress());
	    	            mp.put("sender_address", addr.getAddress());
	    	            mp.put("sender_showname", addr.getPersonal());
	    	            //System.out.println("发件人显示名:" + addr.getPersonal());
	    	        }
	    	        System.out.println("邮件主题:" + m.getSubject());
	    	        mp.put("subject", m.getSubject());
	    	        
	    	        ArrayList<Object> ct = new ArrayList<Object>();
	    	        Object o = m.getContent();
	    	        if(o instanceof Multipart) {
	    	            //System.out.println("Multipart");
	    	            Multipart multipart = (Multipart) o ;
	    	            //System.out.println("邮件共有" + multipart.getCount() + "部分组成");
	    		        //System.out.println("即将一次处理各部分");
	    		        // 依次处理各个部分
	    		        for (int j = 0, n = multipart.getCount(); j < n; j++) {
	    		            //System.out.println("处理第" + j + "部分");
	    		            Part part = multipart.getBodyPart(j);//解包, 取出 MultiPart的各个部分, 每部分可能是邮件内容,
	    		            // 也可能是另一个小包裹(MultipPart)
	    		            // 判断此包裹内容是不是一个小包裹, 一般这一部分是 正文 Content-Type: multipart/alternative
	    		            if (part.getContent() instanceof Multipart) {
	    		                //System.out.println("此部分仍有小包裹，将迭代处理！");
	    		                Multipart p = (Multipart) part.getContent();// 转成小包裹
	    		                //递归迭代
	    		                //reMultipart(p);
	    		            } else {
	    		            	ct.add(part.getContent());
	    		            	if(part.getContentType().startsWith("TEXT/PLAIN")) {
	    	    	                //System.out.println("text内容：" + part.getContent());
	    	    	                //bean.setUrl((String) part.getContent());
	    	    	            } else {
	    	    	                //System.out.println("HTML内容：" + part.getContent());
	    	    	            }
	    		            }
	    		        }
	    	        } else if (o instanceof Part){
	    	            //System.out.println("Part");
	    	            Part part = (Part) o;
	    	            ct.add(part.getContent());
	    	            if(part.getContentType().startsWith("TEXT/PLAIN")) {
	    	                //System.out.println("text内容：" + part.getContent());
	    	                //bean.setUrl((String) part.getContent());
	    	            } else {
	    	                //System.out.println("HTML内容：" + part.getContent());
	    	            }
	    	        } else {
	    	        	ct.add(m.getContent());
	    	            //System.out.println("既不是Multipart也不是Part");
	    	            //System.out.println("类型" + m.getContentType());
	    	            //System.out.println("内容" + m.getContent());
	    	        }
	    	       // StringBuffer content = new StringBuffer(30);
	                //解析邮件正文
	                //getMailTextContent(m, content);
	    	       // String content = m.getContent().toString();
	    	        //ct.add(content);
	    	        mp.put("content", ct);
	    	        //writePart(m);
//	                String line = reader.readLine();
//	                if ("YES".equals(line)) {
//	                   m.writeTo(System.out);
//	                } else if ("QUIT".equals(line)) {
//	                   break;
//	                }
	                //System.out.println("发现一封未读邮件即将分析！");
	                //mailReceiver(m);
	                //System.out.println("————-————————————————————————————");
	      /*          IMAPMessage ms = (IMAPMessage) m;
	                System.out.println("11");
	                String subject = MimeUtility.decodeText(ms.getSubject());
	                System.out.println("22");*/
	                //bean = new Bean();
	                
	                //list.add(bean);
	       //         m.setFlag(Flags.Flag.SEEN, true);
	    	        list.add(mp);
	                
	            }
	            System.out.println("------------------------结束----------------------------------"); 
	            map.put("list", list);

	        } catch (MessagingException e) {
	            // TODO Auto-generated catch block
	        } finally{
	            try {
	                inbox.close(false);
	                store.close();
	            } catch (MessagingException e) {
	                // TODO Auto-generated catch block
	                //e.printStackTrace();
	            }finally{
	        		return map;
	            }
	        }
	        // 解析邮件 
	       /** for (Message message : messages) { 
	            IMAPMessage msg = (IMAPMessage) message; 
	            String subject = MimeUtility.decodeText(msg.getSubject()); 
	            System.out.println("[" + subject + "]未读，是否需要阅读此邮件（yes/no）？"); 
	            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)); 
	            String answer = reader.readLine();  
	            if ("yes".equalsIgnoreCase(answer)) { 
	                // 第二个参数如果设置为true，则将修改反馈给服务器。false则不反馈给服务器 
	                msg.setFlag(Flag.SEEN, true);   //设置已读标志 
	            } 
	        } */

	    } 
	    /*
	     * This method checks for content-type 
	     * based on which, it processes and
	     * fetches the content of the message
	     */
	     public static void writePart(Part p) throws Exception {
	        if (p instanceof Message)
	           //Call methos writeEnvelope
	           writeEnvelope((Message) p);

	        System.out.println("----------------------------");
	        System.out.println("CONTENT-TYPE: " + p.getContentType());

	        //check if the content is plain text
	        if (p.isMimeType("text/plain")) {
	           System.out.println("This is plain text");
	           System.out.println("---------------------------");
	           System.out.println((String) p.getContent());
	        } 
	        //check if the content has attachment
	        else if (p.isMimeType("multipart/*")) {
	           System.out.println("This is a Multipart");
	           System.out.println("---------------------------");
	           Multipart mp = (Multipart) p.getContent();
	           int count = mp.getCount();
	           for (int i = 0; i < count; i++)
	              writePart(mp.getBodyPart(i));
	        } 
	        //check if the content is a nested message
	        else if (p.isMimeType("message/rfc822")) {
	           System.out.println("This is a Nested Message");
	           System.out.println("---------------------------");
	           writePart((Part) p.getContent());
	        } 
	        //check if the content is an inline image
	        else if (p.isMimeType("image/jpeg")) {
	           System.out.println("--------> image/jpeg");
	           Object o = p.getContent();

	           InputStream x = (InputStream) o;
	           // Construct the required byte array
	           System.out.println("x.length = " + x.available());
	           int i = 0;
	           byte[] bArray = new byte[x.available()];

	           while ((i = (int) ((InputStream) x).available()) > 0) {
	              int result = (int) (((InputStream) x).read(bArray));
	              if (result == -1)
	              break;
	           }
	           FileOutputStream f2 = new FileOutputStream("/tmp/image.jpg");
	           f2.write(bArray);
	        } 
	        else if (p.getContentType().contains("image/")) {
	           System.out.println("content type" + p.getContentType());
	           File f = new File("image" + new Date().getTime() + ".jpg");
	           DataOutputStream output = new DataOutputStream(
	              new BufferedOutputStream(new FileOutputStream(f)));
	              com.sun.mail.util.BASE64DecoderStream test = 
	                   (com.sun.mail.util.BASE64DecoderStream) p
	                    .getContent();
	           byte[] buffer = new byte[1024];
	           int bytesRead;
	           while ((bytesRead = test.read(buffer)) != -1) {
	              output.write(buffer, 0, bytesRead);
	           }
	        } 
	        else {
	           Object o = p.getContent();
	           if (o instanceof String) {
	              System.out.println("This is a string");
	              System.out.println("---------------------------");
	              System.out.println((String) o);
	           } 
	           else if (o instanceof InputStream) {
	              System.out.println("This is just an input stream");
	              System.out.println("---------------------------");
	              InputStream is = (InputStream) o;
	              is = (InputStream) o;
	              int c;
	              while ((c = is.read()) != -1)
	                 System.out.write(c);
	           } 
	           else {
	              System.out.println("This is an unknown type");
	              System.out.println("---------------------------");
	              System.out.println(o.toString());
	           }
	        }

	     }
	     /*
	      * This method would print FROM,TO and SUBJECT of the message
	      */
	      public static void writeEnvelope(Message m) throws Exception {
	         System.out.println("This is the message envelope");
	         System.out.println("---------------------------");
	         Address[] a;

	         // FROM
	         if ((a = m.getFrom()) != null) {
	            for (int j = 0; j < a.length; j++)
	            System.out.println("FROM: " + a[j].toString());
	         }

	         // TO
	         if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
	            for (int j = 0; j < a.length; j++)
	            System.out.println("TO: " + a[j].toString());
	         }

	         // SUBJECT
	         if (m.getSubject() != null)
	            System.out.println("SUBJECT: " + m.getSubject());

	      }
	    /**
	     * 获得邮件文本内容
	     *
	     * @param part    邮件体
	     * @param content 存储邮件文本内容的字符串
	     * @throws MessagingException
	     * @throws IOException
	     */
	    public static void getMailTextContent(Part part, StringBuffer content) throws MessagingException, IOException {
	        //如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
	        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
	        if (part.isMimeType("text/*") && !isContainTextAttach) {
	            content.append(part.getContent().toString());
	            System.out.println(part.getContent().toString());
	        } else if (part.isMimeType("message/rfc822")) {
	            getMailTextContent((Part) part.getContent(), content);
	        } else if (part.isMimeType("multipart/*")) {
	            Multipart multipart = (Multipart) part.getContent();
	            int partCount = multipart.getCount();
	            for (int i = 0; i < partCount; i++) {
	                BodyPart bodyPart = multipart.getBodyPart(i);
	                getMailTextContent(bodyPart, content);
	            }
	        }
	    }
	    public static void recvMail(final String username,final String password)  {
	        //String username = "robinsonhood15@gmail.com";
	    	//String password = "paulbob123";

	        Properties props = new Properties();
	        props.put("mail.store.protocol", "imap");     // 协议类型
	        props.put("mail.imap.host", "imap.gmail.com"); // imap 主机
	        props.put("mail.imap.port", 993);             // imap 端口
	        props.put("mail.imap.ssl.enable", true);      // SSL 套接层

	        // 获取 session 邮件会话
	        Session session = Session.getInstance(props, new Authenticator() {
	            @Override
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(username, password);
	            }
	        });

	        Store store = null;
	        Folder folder = null;
	        try {
	        	store = session.getStore(); // 获取 "邮箱"
	 	        store.connect(); // 连接 "邮箱"

	 	        folder = store.getFolder("INBOX"); // 获取 "收件箱"
	 	        folder.open(Folder.READ_ONLY); // 打开 "收件箱" (只读)
	        // 获取全部邮件
	        // Message[] messages = folder.getMessages();
	        // 获取未读邮件
	        Message[] messages = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
	        System.out.println("number of Email: " + messages.length);
	        for (int i = 0; i < messages.length; i++) {
	            Message message = messages[i];
	            Object content = message.getContent();
	            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	            System.out.println("num: " + (i + 1));
	            System.out.println("from: " + MimeUtility.decodeText(message.getFrom()[0].toString()));
	            System.out.println("subject: " + MimeUtility.decodeText(message.getSubject()));
	            if (content instanceof String) { // 如果邮件内容是 String
	                System.out.println("content: " + content);
	            } else if (content instanceof Multipart) { // 如果邮件内容是 Multipart
	                Multipart multipart = (Multipart) content;
	                int count = multipart.getCount();
	                System.out.println("content: [multipart] [number of part: " + count + "]");
	                for (int j = 0; j < count; j++) {
	                    Part part = multipart.getBodyPart(j);
	                    String disp = part.getDisposition();
	                    if (disp == null && part.getHeader("Content-ID") == null) // 如果是正文
	                        System.out.println("====================== inline ======================");
	                    else // 如果是附件
	                        System.out.println("====================== attach ======================");
	                    dumpPart(part);
	                }
	            } else { // 邮件内容是未知类型
	                System.out.println("content: <unknown content type>");
	            }
	            // 将未读邮件设为已读状态
	            message.setFlag(Flags.Flag.SEEN, true);
	        }
	        }
	        catch(MessagingException| IOException e) {
	        	
	        }
	        finally {
	        //folder.close(); // 关闭 "收件箱"
	        //store.close();  // 关闭 "邮箱"
		        try {
	                folder.close(false);
	                store.close();
	            } catch (MessagingException e) {
	                // TODO Auto-generated catch block
	                //e.printStackTrace();
	            }
	        }
	    }

	    public static void dumpPart(Part part) throws MessagingException, IOException {
	        System.out.println("Content-Type: " + part.getContentType().split(";")[0]);
	        if (part.isMimeType("text/*")) {
	            System.out.println(part.getContent());
	        } else if (part.isMimeType("image/*") || part.isMimeType("audio/*") || part.isMimeType("video/*") || part.isMimeType("application/*")) {
	            String fileName = part.getFileName();
	            if (fileName == null) {
	                fileName = part.getHeader("Content-ID")[0];
	                if (fileName.startsWith("<"))
	                    fileName = fileName.substring(1, fileName.length() - 1);
	            }
	            fileName = part.getContentType().split(";")[0].replace("/", "-") + "_" +
	                       UUID.randomUUID().toString().replace("-", "") + "_" + fileName;
	            System.out.println("Saved-FileName: " + fileName);
	            InputStream input = part.getInputStream();
	            FileOutputStream output = new FileOutputStream(fileName);
	            int nbuf = 0;
	            byte[] buf = new byte[4096];
	            while ((nbuf = input.read(buf)) != -1)
	                output.write(buf, 0, nbuf);
	            input.close();
	            output.close();
	        } else {
	            System.out.println("unknown type ...");
	        }
	    }

	    public static void main(String[] args) {
	    	//SendMail.send("robinsonhood1978@gmail.com", "12345678",0);
	    	//(String host,final String from,final String passwd,String to,String subject,String htmlbody)
//	    	String host = "mail.taijicoin.nz";
	    	//String from = "robin@taijicoin.nz";
	    	//String passwd = "111111";
	    	//String from = "robinsonhood15@gmail.com";
	    	//String passwd = "paulbob123";
	    	String from = "fynco.storage@gmail.com";
	    	String passwd = "fynco321";
//	    	String to = "robinsonhood1978@gmail.com";
//	    	String subject = "Welcome you to our house";
//	    	String htmlbody = "hello,when will you come to my house?";
	    	ReadMail mail = new ReadMail();
	    	// Map<String, Object> map = mail.getMails(from,passwd);
	    	//mail.recvMail(from, passwd);
//	    	for (Message m : messages){
//	    		
//                try {
//                	Address[] froms = m.getFrom();
//        	        if(froms != null) {
//        	            System.out.println("即将打印发件人地址。。。");
//        	            InternetAddress addr = (InternetAddress)froms[0];
//        	            System.out.println("发件人地址:" + addr.getAddress());
//        	            //bean.setPersonAddress(addr.getAddress());
//        	            //bean.setPersonName(addr.getPersonal());
//        	            System.out.println("发件人显示名:" + addr.getPersonal());
//        	        }
//                	System.out.println("邮件主题:" + m.getSubject());
//					//if (m.getFlags().contains(Flags.Flag.SEEN)){
//					    //continue;
//					//}
//				} catch (MessagingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//      
           }

	   // }
	    
	}
