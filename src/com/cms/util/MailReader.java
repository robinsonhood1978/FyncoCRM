package com.cms.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.mail.util.MimeMessageParser;

public class MailReader {

	private static SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmsssSSS");
	private static SimpleDateFormat ym = new SimpleDateFormat("yyyyMM");
	private static String uploadroot = "/upload/";
	
	private static String creatFolder(String path) {
		String folderName=ym.format(new Date());
		String folderPath = path+uploadroot+folderName+"/receiver/";
		File dir = new File(folderPath);
		if(!dir.exists()){
			dir.mkdir();
		}
		return folderName;
	} 
	/**
     * @Title: parseMessage
     * @Description: 解析邮件
     * @param messages
     *            要解析的邮件列表
	 * @throws Exception 
     */
    public static ArrayList<HashMap<String, Object>> parseMessage(String path, Message... messages)
            throws Exception {
        if (messages == null || messages.length < 1) {
            throw new MessagingException("未找到要解析的邮件!");
        }
//        String saveFilePath = creatFolder(path);
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        // 解析所有邮件
        for (int i = messages.length; 0 < i; i--) {
        	HashMap<String, Object> mp = new HashMap<String, Object>();
            MimeMessage msg = (MimeMessage) messages[i-1];
            System.out.println("------------------解析第" + msg.getMessageNumber()
                    + "封邮件-------------------- ");
            String isRead = "Unread";
            if (msg.getFlags().contains(Flags.Flag.SEEN)){
                isRead = "Seen";
            }
            mp.put("status", isRead);
            mp.put("send_date", msg.getSentDate());
            Address[] froms = msg.getFrom();
	        if(froms != null) {
	            //System.out.println("即将打印发件人地址。。。");
	            InternetAddress addr = (InternetAddress)froms[0];
	            //System.out.println("发件人地址:" + addr.getAddress());
	            mp.put("sender_address", addr.getAddress());
	            String person = addr.getPersonal();
	            if (person != null) {
	                person = MimeUtility.decodeText(person) + " ";
	            } else {
	                person = "";
	            }
	            mp.put("sender_showname", person);
	            //System.out.println("发件人显示名:" + addr.getPersonal());
	        }
	        mp.put("subject", getSubject(msg));
            System.out.println("主题: " + getSubject(msg));
            System.out.println("发件人: " + getFrom(msg));
            System.out.println("收件人：" + getReceiveAddress(msg, null));
            System.out.println("发送时间：" + getSentDate(msg, null));
            System.out.println("是否已读：" + isSeen(msg));
            System.out.println("邮件优先级：" + getPriority(msg));
            System.out.println("是否需要回执：" + isReplySign(msg));
            System.out.println("邮件大小：" + msg.getSize() * 1024 + "kb");
            boolean isContainerAttachment = isContainAttachment(msg);
            System.out.println("是否包含附件：" + isContainerAttachment);
            System.out.println("邮件ID：" + msg.getMessageID());
            if (isContainerAttachment) {
                saveAttachment(msg, path+"/upload/" + msg.getSubject() + "_"
                        + i + "_"); // 保存附件
            }
//            if(getFrom(msg).equals(todelfrom) && msg.getSubject().matches(todelsubject)){
//            	                msg.setFlag(Flags.Flag.DELETED, true); // set the DELETED flag
//            	                delresult = true;
//            }
            StringBuffer content = new StringBuffer(30);
            ArrayList<String> ct = new ArrayList<String>();
            getMailTextContent(msg, ct);
            replacePicturePth(msg,ct,i);
            mp.put("content", ct);
            list.add(mp);
            System.out.println("邮件正文："
                    + (ct));
            System.out.println("------------------第" + msg.getMessageNumber()
                    + "封邮件解析结束-------------------- ");
            System.out.println();
        }
        return list;
    }

    /**
     * @Title: deleteMessage
     * @Description: 解析邮件
     * @param messages
     *            要解析的邮件列表
     * @throws MessagingException
     * @throws IOException
     *             void
     */
    public static void deleteMessage(Message... messages)
            throws MessagingException, IOException {
        if (messages == null || messages.length < 1) {
            throw new MessagingException("未找到要解析的邮件!");
        }
        // 解析所有邮件
        for (int i = 0, count = messages.length; i < count; i++) {

            // 邮件删除
            Message message = messages[i];
            String subject = message.getSubject();
            // set the DELETE flag to true
            message.setFlag(Flags.Flag.DELETED, true);
            System.out.println("Marked DELETE for message: " + subject);

        }
    }

    /**
     * @Title: getSubject
     * @Description: 获得邮件主题
     * @param msg
     *            邮件内容
     * @return 解码后的邮件主题
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     *             String
     */
    public static String getSubject(MimeMessage msg)
            throws UnsupportedEncodingException, MessagingException {
        return MimeUtility.decodeText(msg.getSubject());
    }

    /**
     * @Title: getFrom
     * @Description: 获得邮件发件人
     * @param msg
     *            邮件内容
     * @return 姓名 <Email地址>
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     *             String
     */
    public static String getFrom(MimeMessage msg) throws MessagingException,
            UnsupportedEncodingException {
        String from = "";
        Address[] froms = msg.getFrom();
        if (froms.length < 1) {
            throw new MessagingException("没有发件人!");
        }
        InternetAddress address = (InternetAddress) froms[0];
        String person = address.getPersonal();
        if (person != null) {
            person = MimeUtility.decodeText(person) + " ";
        } else {
            person = "";
        }
        from = person + "<" + address.getAddress() + ">";

        return from;
    }

    /**
     * @Title: getReceiveAddress
     * @Description: 根据收件人类型，获取邮件收件人、抄送和密送地址。如果收件人类型为空，则获得所有的收件人
     *               <p>
     *               Message.RecipientType.TO 收件人
     *               </p>
     *               <p>
     *               Message.RecipientType.CC 抄送
     *               </p>
     *               <p>
     *               Message.RecipientType.BCC 密送
     *               </p>
     * @param msg
     *            邮件内容
     * @param type
     *            收件人类型
     * @return 收件人1 <邮件地址1>, 收件人2 <邮件地址2>, ...
     * @throws MessagingException
     *             String
     */
    public static String getReceiveAddress(MimeMessage msg,
            Message.RecipientType type) throws MessagingException {
        StringBuffer receiveAddress = new StringBuffer();
        Address[] addresss = null;
        if (type == null) {
            addresss = msg.getAllRecipients();
        } else {
            addresss = msg.getRecipients(type);
        }

        if (addresss == null || addresss.length < 1) {
            throw new MessagingException("没有收件人!");
        }
        for (Address address : addresss) {
            InternetAddress internetAddress = (InternetAddress) address;
            receiveAddress.append(internetAddress.toUnicodeString())
                    .append(",");
        }

        receiveAddress.deleteCharAt(receiveAddress.length() - 1); // 删除最后一个逗号

        return receiveAddress.toString();
    }

    /**
     * @Title: getSentDate
     * @Description: 获得邮件发送时间
     * @param msg
     *            邮件内容
     * @param pattern
     *            日期格式
     * @return yyyy年mm月dd日 星期X HH:mm
     * @throws MessagingException
     *             String
     */
    public static String getSentDate(MimeMessage msg, String pattern)
            throws MessagingException {
        Date receivedDate = msg.getSentDate();
        if (receivedDate == null) {
            return "";
        }
        if (pattern == null || "".equals(pattern)) {
            pattern = "yyyy年MM月dd日 E HH:mm ";
        }
        return new SimpleDateFormat(pattern).format(receivedDate);
    }

    /**
     * @Title: isContainAttachment
     * @Description: 判断邮件中是否包含附件
     * @param part
     *            邮件内容
     * @return 邮件中存在附件返回true，不存在返回false
     * @throws MessagingException
     * @throws IOException
     *             boolean
     */
    public static boolean isContainAttachment(Part part)
            throws MessagingException, IOException {
        boolean flag = false;
        if (part.isMimeType("multipart/*")) {
            MimeMultipart multipart = (MimeMultipart) part.getContent();
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                BodyPart bodyPart = multipart.getBodyPart(i);
                String disp = bodyPart.getDisposition();
                boolean isHasAttachment = (disp != null && (disp
                        .equalsIgnoreCase(Part.ATTACHMENT) || disp
                        .equalsIgnoreCase(Part.INLINE)));
                if (isHasAttachment) {
                    flag = true;
                } else if (bodyPart.isMimeType("multipart/*")) {
                    flag = isContainAttachment(bodyPart);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("application") != -1) {
                        flag = true;
                    }

                    if (contentType.indexOf("name") != -1) {
                        flag = true;
                    }
                }

                if (flag) {
                    break;
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            flag = isContainAttachment((Part) part.getContent());
        }
        return flag;
    }

    /**
     * @Title: isSeen
     * @Description: 判断邮件是否已读
     * @param msg
     *            邮件内容
     * @return 如果邮件已读返回true,否则返回false
     * @throws MessagingException
     *             boolean
     */
    public static boolean isSeen(MimeMessage msg) throws MessagingException {
        return msg.getFlags().contains(Flags.Flag.SEEN);
    }

    /**
     * @Title: isReplySign
     * @Description: 判断邮件是否需要阅读回执
     * @param msg
     *            邮件内容
     * @return 需要回执返回true,否则返回false
     * @throws MessagingException
     *             boolean
     */
    public static boolean isReplySign(MimeMessage msg)
            throws MessagingException {
        boolean replySign = false;
        String[] headers = msg.getHeader("Disposition-Notification-To");
        if (headers != null) {
            replySign = true;
        }
        return replySign;
    }

    /**
     * @Title: getPriority
     * @Description: 获得邮件的优先级
     * @param msg
     *            邮件内容
     * @return 1(High):紧急 3:普通(Normal) 5:低(Low)
     * @throws MessagingException
     *             String
     */
    public static String getPriority(MimeMessage msg) throws MessagingException {
        String priority = "普通";
        String[] headers = msg.getHeader("X-Priority");
        if (headers != null) {
            String headerPriority = headers[0];
            if (headerPriority.indexOf("1") != -1
                    || headerPriority.indexOf("High") != -1) {
                priority = "紧急";
            } else if (headerPriority.indexOf("5") != -1
                    || headerPriority.indexOf("Low") != -1) {
                priority = "低";
            } else {
                priority = "普通";
            }
        }
        return priority;
    }

    /**
     * @Title: getMailTextContent
     * @Description: 获得邮件文本内容
     * @param part
     *            邮件体
     * @param content
     *            存储邮件文本内容的字符串
     * @throws MessagingException
     * @throws IOException
     *             void
     */
    public static void getMailTextContent(Part part, ArrayList<String> content)
            throws MessagingException, IOException {
        // 如果是文本类型的附件，通过getContent方法可以取到文本内容，但这不是我们需要的结果，所以在这里要做判断
        boolean isContainTextAttach = part.getContentType().indexOf("name") > 0;
        if (part.isMimeType("text/*") && !isContainTextAttach) {
        	System.out.println("this is :"+part.getContentType()+" | "+part.getContent().toString());
            content.add(part.getContent().toString());
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

    public static void replacePicturePth(MimeMessage msg, ArrayList<String> content, int i) throws Exception {
    	MimeMessageParser parser = new MimeMessageParser(msg).parse();
    	
    	//邮件内容中的图片处理
 		List<DataSource> attachments = parser.getAttachmentList();
 		System.out.println("picturess:"+attachments.size());
 		//文件预览中无法显示不能包含/ 和-
 	    Set<String> cids =  (Set<String>) parser.getContentIds();
 		Iterator<String> it = cids.iterator();
 	    while (it.hasNext()) {
 	      String cid = it.next();
 	      System.out.println(cid);
 	      DataSource ds = parser.findAttachmentByCid(cid);
 	      String replaceStr = "/upload/"+msg.getSubject() + "_"+ i + "_"+ds.getName();

// 	      Collections.replaceAll(content, "cid:"+cid, replaceStr);
 	     if(ds != null){
 	    	attachments.remove(ds);
 	    	replaceAllElement("cid:"+cid,replaceStr,content);
 	     }
 	      
// 	      content.replaceAll(e ->e.replace("cid:"+cid,replaceStr));
 	      // contentStr判断是否含有cid,如果含有需要处理，如果不包含，不需要处理
// 	      for (int j = 0; j < content.size(); j++) {
// 	    	 if(content.get(j).contains(cid)) {
//     		 	ds = parser.findAttachmentByCid(cid);
// 	 		      if(ds != null){
// 	 		    	  System.out.println(ds.getName());
// 	 		    	  // to do 将邮件内容中的图片上传到文件存储服务器
// 	 		    	  String replaceStr = "/Users/deng/Downloads/"+msg.getSubject() + "_"+ i + "_"+ds.getName();//得到  上传到文件存储服务器后的地址
// 	 		    	  attachments.remove(ds);
// 	 		    	  System.out.println(replaceStr);
// 	 		    	  content.content.get(j).replace("cid:"+cid,replaceStr);
// 	 		    	  // look at there at 2020-03-05
// 	 		      } 
// 	 	      }
// 	      }
 	    }
	}
    
    public static void replaceAllElement(String reg,String where,ArrayList<String> content) {
    	for (int i = 0; i < content.size(); i++) {
    		if (content.get(i).contains(reg)) {
    			content.set(i, content.get(i).replace(reg, where));
			}
		}
	}
    
    /**
     * @Title: saveAttachment
     * @Description: 保存附件
     * @param part
     *            邮件中多个组合体中的其中一个组合体
     * @param destDir
     *            附件保存目录
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     * @throws FileNotFoundException
     * @throws IOException
     *             void
     */
    public static void saveAttachment(Part part, String destDir)
            throws UnsupportedEncodingException, MessagingException,
            FileNotFoundException, IOException {
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent(); // 复杂体邮件
            // 复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                // 获得复杂体邮件中其中一个邮件体
                BodyPart bodyPart = multipart.getBodyPart(i);
                // 某一个邮件体也有可能是由多个邮件体组成的复杂体
                String disp = bodyPart.getDisposition();
                boolean isHasAttachment = (disp != null && (disp
                        .equalsIgnoreCase(Part.ATTACHMENT) || disp
                        .equalsIgnoreCase(Part.INLINE)));
                if (isHasAttachment) {
                    InputStream is = bodyPart.getInputStream();
                    saveFile(is, destDir, decodeText(bodyPart.getFileName()));
//                    System.out.println("----附件："
//                            + decodeText(bodyPart.getFileName()) + ","
//                            + " 保存路径为" + destDir);
                    System.out.println(destDir +decodeText(bodyPart.getFileName()));
                } else if (bodyPart.isMimeType("multipart/*")) {
                    saveAttachment(bodyPart, destDir);
                } else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("name") != -1
                            || contentType.indexOf("application") != -1) {
                        saveFile(bodyPart.getInputStream(), destDir,
                                decodeText(bodyPart.getFileName()));
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachment((Part) part.getContent(), destDir);
        }
    }

    /**
     * @Title: saveFile
     * @Description: 读取输入流中的数据保存至指定目录
     * @param is
     *            输入流
     * @param destDir
     *            文件存储目录
     * @param fileName
     *            文件名
     * @throws FileNotFoundException
     * @throws IOException
     *             void
     */
    private static void saveFile(InputStream is, String destDir, String fileName)
            throws FileNotFoundException, IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(new File(destDir + fileName)));
        int len = -1;
        while ((len = bis.read()) != -1) {
            bos.write(len);
            bos.flush();
        }
        bos.close();
        bis.close();
    }

    /**
     * @Title: decodeText
     * @Description: 文本解码
     * @param encodeText
     *            解码MimeUtility.encodeText(String text)方法编码后的文本
     * @return 解码后的文本
     * @throws UnsupportedEncodingException
     *             String
     */
    public static String decodeText(String encodeText)
            throws UnsupportedEncodingException {
        if (encodeText == null || "".equals(encodeText)) {
            return "";
        } else {
            return MimeUtility.decodeText(encodeText);
        }
    }
}
