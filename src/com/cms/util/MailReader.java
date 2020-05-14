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
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.mail.util.MimeMessageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.safety.Whitelist;

import com.cms.admin.user.User;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import net.sf.json.JSONArray;

public class MailReader {

	private static SimpleDateFormat sfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmsssSSS");
//	private static SimpleDateFormat ym = new SimpleDateFormat("yyyyMM");
	private static String uploadroot = "/upload/receiver/";
	private static HashMap<String, String> fName = new HashMap<String, String>();
	
	private static String getFolderName(String path, int creator) {
//		String folderName=ym.format(new Date())+String.valueOf(creator);
		String folderName=String.valueOf(creator);
		String folderPath = path+uploadroot;
		creatFolder(folderPath);// create an email receiver folder while it is not occur.
		creatFolder(folderPath+folderName);//create a folder to store email data of current month.
		return folderName;
	} 
	private static boolean creatFolder(String path) {
		File dir = new File(path);
		if(!dir.exists()){
			return dir.mkdir();
		}
		return false;
	}
	
	public static String downLoadAttach(String path,int creator,String fileName, int id,Message messages) throws Exception {
		if (messages == null) {
            throw new MessagingException("未找到要解析的邮件!");
        }
		MimeMessage msg = (MimeMessage) messages;
		String saveFilePath = getFolderName(path, creator);
		String viewPath = uploadroot + saveFilePath +"/";
		System.out.println("step1");
        return downloadAttachs(msg, viewPath, path, fileName); // 保存附件
	}
	/**
     * @Title: parseMessage
     * @Description: 解析邮件
     * @param messages
     *            要解析的邮件列表
	 * @throws Exception 
     */
    public static void parseMessage(String path,int creator,int folderId, Message... messages)
            throws Exception {
        if (messages == null || messages.length < 1) {
            throw new MessagingException("未找到要解析的邮件!");
        }
        fName = new HashMap<String, String>();
        String saveFilePath = getFolderName(path, creator);
       
        // 解析所有邮件
        for (int i = messages.length; 0 < i; i--) {
        	HashMap<String, Object> mp = new HashMap<String, Object>();
            MimeMessage msg = (MimeMessage) messages[i-1];
            if (messageInDB(msg.getMessageID(), creator)) {
//            System.out.println("------------------解析第" + msg.getMessageNumber()
//                    + "封邮件-------------------- ");
            mp.put("flag", msg.getFlags().toString());
            mp.put("type", folderId);
            mp.put("status", 1);
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
	                person = "UnKnow Person";
	            }
	            mp.put("sender_showname", person);
	        }

	        mp.put("subject", getSubject(msg));
	        System.out.println(msg.getSubject());
	        JSONArray recarr = JSONArray.fromObject(getReceiveAddress(msg, null));
	        mp.put("receiver", recarr.toString());

//            boolean isContainerAttachment = isContainAttachment(msg);
            mp.put("messageId", msg.getMessageID());
            String viewPath = uploadroot + saveFilePath +"/";
            ArrayList<String> ct = new ArrayList<String>();
            ArrayList<String> attaList = new ArrayList<String>();
            saveAttachment(msg, viewPath, path, ct, attaList); // 保存附件

//            System.out.println("-----fininal download attachment-----");
            
            mp.put("attachment", JSONArray.fromObject(attaList).toString());
            mp.put("content", JSONArray.fromObject(ct).toString());
    
//            System.out.println("邮件正文："
//                    + mp.get("content"));
//            System.out.println("attachment:"+mp.get("attachment"));
//            System.out.println("type:"+mp.get("type"));
//            System.out.println("sender_address:"+mp.get("sender_address"));
//            System.out.println("receiver:"+mp.get("receiver"));
//            System.out.println("status:"+mp.get("status"));
//            System.out.println("messageId:"+mp.get("messageId"));
//            System.out.println("flag:"+mp.get("flag"));
//            System.out.println("sender_showname:"+mp.get("sender_showname"));
//            System.out.println("send_date:"+sfDate.format(mp.get("send_date")));
            
            
           // messageInDB(msg.getMessageID(), creator);
  
        	  Db.update("insert into email (subject,content,attachment,sender,receiver,status,create_time,creator,messageId,flag,type,sender_name,boxId) values "
   					+ "(?,?,?,?,?,?,?,?,?,?,?,?,?)",mp.get("subject"),mp.get("content"),mp.get("attachment"),mp.get("sender_address"),mp.get("receiver"),mp.get("status"),sfDate.format(mp.get("send_date")),creator,mp.get("messageId"),mp.get("flag"),mp.get("type"),mp.get("sender_showname"),mp.get("type"));
//        	  System.out.println(c);
//        	  System.out.println("------------------第" + msg.getMessageNumber()
//                    + "封邮件解析结束-------------------- ");
           }
        }
    }
    private static boolean messageInDB(String messageID, int creator) {
    	Record message = Db.findFirst("select * from email where creator="+creator+" and messageId = '"+messageID+"'");
		return(( message == null)|| message.equals(null));
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
    public static byte[] getSubject(MimeMessage msg)
            throws UnsupportedEncodingException, MessagingException {
        return msg.getSubject() != null?MimeUtility.decodeText(msg.getSubject()).getBytes():new byte[] {};
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
    public static ArrayList<HashMap<String,String>> getReceiveAddress(MimeMessage msg,
            Message.RecipientType type) throws MessagingException {
        Address[] addresss = null;
        ArrayList<HashMap<String,String>> receList = new ArrayList<HashMap<String,String>>();
        if (type == null) {
            addresss = msg.getAllRecipients();
        } else {
            addresss = msg.getRecipients(type);
        }
        
        if (addresss == null || addresss.length < 1) {
            throw new MessagingException("没有收件人!");
        }else {
	        for (Address address : addresss) {
	        	HashMap<String,String> map = new HashMap<String,String>();
	            InternetAddress internetAddress = (InternetAddress) address;
	            String name = (internetAddress.getPersonal() != null && internetAddress.getPersonal() != "") ? internetAddress.getPersonal(): internetAddress.getAddress();
	            map.put("receiver", name);
	            map.put("address", internetAddress.getAddress());
	//            receiveAddress.append(internetAddress.getPersonal()+internetAddress.getAddress())
	//                    .append(",");
	            receList.add(map);
	        }
        }

//        receiveAddress.deleteCharAt(receiveAddress.length() - 1); // 删除最后一个逗号

        return receList;
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
        	if (part.isMimeType("text/html") && (content.size() == 0)) {
				content.add(toPlainText(part.getContent().toString()));
			}
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

    public static ArrayList<HashMap<String,String>> replacePicturePth(MimeMessage msg, ArrayList<String> content, String path, String rpath) throws Exception {
    	MimeMessageParser parser = new MimeMessageParser(msg).parse();
    	ArrayList<HashMap<String,String>> attaList = new ArrayList<HashMap<String,String>>();
    	
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
// 	      System.out.println(ds.getContentType()+" | type for attachments!");
 	      String fileName = ds.getName();
 	      String replaceStr = rpath+path+fileName;
// 	      System.out.println(fName.get(replaceStr)+" | old | "+replaceStr);
 	      replaceStr = fName.get(replaceStr);
	 	     if(ds != null){
	 	    	attachments.remove(ds);
//	 	    	System.out.println(isAttachments(cid, content)+" | this is attachments!");
	 	    	 if (isAttachments(cid, content)) {
	 	    		replaceAllElement("cid:"+cid,replaceStr,content);
				}else {
					HashMap<String,String> map = new HashMap<String,String>();
					map.put("att_name", fileName);
					map.put("att_file", replaceStr);
					attaList.add(map);	
				}
	 	     }
 	    }
 	    return attaList;
	}
    private static boolean isAttachments(String reg, ArrayList<String> content) {
    	for (String str : content) {
			if (str.contains(reg)) {
				return true;
			}
		}
		return false;
	}
    
    public static void replaceAllElement(String reg,String where,ArrayList<String> content) {
    	for (int i = 0; i < content.size(); i++) {
    		if (content.get(i).contains(reg)) {
    			content.set(i, content.get(i).replace(reg, where));
			}
		}
	}
    
    private static String downloadAttachs(Part part, String destDir, String root, String fileName) throws MessagingException, IOException {
    	if (part.isMimeType("multipart/*")) {
//        	System.out.println("----------start save ******!-------");        	
            Multipart multipart = (Multipart) part.getContent(); // 复杂体邮件
            // 复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                // 获得复杂体邮件中其中一个邮件体
            	MimeBodyPart bodyPart = (MimeBodyPart) multipart.getBodyPart(i);
                // 某一个邮件体也有可能是由多个邮件体组成的复杂体
                String disp = bodyPart.getDisposition();
//                System.out.println(disp+" | "+bodyPart.getContentType());
                boolean isHasAttachment = (disp != null && (disp
                        .equalsIgnoreCase(Part.ATTACHMENT)));
                if (isHasAttachment) { // save all attachment in local place.    
                    System.out.println("------------start download----------------");
                    if (decodeText(bodyPart.getFileName()).equals(fileName)) {
                    	 return saveFile(bodyPart.getInputStream(), destDir, root, decodeText(bodyPart.getFileName()));
					}
                    System.out.println("------------end download----------------");
                } else if (bodyPart.isMimeType("multipart/*")) {
//                	System.out.println("go to next!");
                	downloadAttachs(bodyPart, destDir, root, fileName);
                }else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("name") != -1
                            || contentType.indexOf("application") != -1) {
                        saveFile(bodyPart.getInputStream(), destDir, root,
                        		decodeText(bodyPart.getFileName()));
//                        System.out.println("not picturesss ");
                    }
                }
//                System.out.println("---------------one set finish at "+i+"--------------");
            }
        } else if (part.isMimeType("message/rfc822")) {
        	downloadAttachs((Part) part.getContent(), destDir, root, fileName);
        }
    	return null;
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
    public static void saveAttachment(Part part, String destDir, String root, ArrayList<String> content, ArrayList<String> attaList)
            throws UnsupportedEncodingException, MessagingException,
            FileNotFoundException, IOException {
//    	System.out.println("----------start save!-------");
//    	System.out.println(part.getContentType());
        if (part.isMimeType("multipart/*")) {
//        	System.out.println("----------start save ******!-------");
            Multipart multipart = (Multipart) part.getContent(); // 复杂体邮件
            // 复杂体邮件包含多个邮件体
            int partCount = multipart.getCount();
            for (int i = 0; i < partCount; i++) {
                // 获得复杂体邮件中其中一个邮件体
            	MimeBodyPart bodyPart = (MimeBodyPart) multipart.getBodyPart(i);
                // 某一个邮件体也有可能是由多个邮件体组成的复杂体
                String disp = bodyPart.getDisposition();
//                System.out.println(disp+" | "+bodyPart.getContentType());
                boolean isHasAttachment = (disp != null && (disp
                        .equalsIgnoreCase(Part.ATTACHMENT) || disp
                        .equalsIgnoreCase(Part.INLINE)));
                if (isHasAttachment) { // save all attachment in local place.    
//                    System.out.println("------------start download----------------");
                    if (disp.equalsIgnoreCase(Part.INLINE)) {//replace inline attachment path to real.
                    	String newUrl = saveFile(bodyPart.getInputStream(), destDir, root, decodeText(bodyPart.getFileName()));
	                	 String cid = bodyPart.getContentID();
	                     cid = cid.substring(1, cid.length()-1);
                    	replaceAllElement("cid:"+cid,newUrl,content);
    				}
                    if (disp.equalsIgnoreCase(Part.ATTACHMENT)) {//save attachment path.
    					attaList.add(decodeText(bodyPart.getFileName()));	
    				}
//                    System.out.println("------------end download----------------");
                } else 
                	if (bodyPart.isMimeType("multipart/*")) {
                    saveAttachment(bodyPart, destDir, root, content, attaList);
                }else if (bodyPart.isMimeType("text/*") && !(bodyPart.getContentType().indexOf("name") > 0)) {
                	if (bodyPart.isMimeType("text/html") && (content.size() == 0)) {
        				content.add(toPlainText(bodyPart.getContent().toString()));
        			}
                	content.add(bodyPart.getContent().toString());
				}else {
                    String contentType = bodyPart.getContentType();
                    if (contentType.indexOf("name") != -1
                            || contentType.indexOf("application") != -1) {
                        saveFile(bodyPart.getInputStream(), destDir, root,
                        		decodeText(bodyPart.getFileName()));
                    }
                }
//                System.out.println("---------------one set finish at "+i+"--------------");
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttachment((Part) part.getContent(), destDir, root, content, attaList);
        }else if(part.isMimeType("text/*") && !(part.getContentType().indexOf("name") > 0)){
        	if (part.isMimeType("text/html") && (content.size() == 0)) {
				content.add(toPlainText(part.getContent().toString()));
			}
        	content.add(part.getContent().toString());
		}
    }
    public static String toPlainText(final String html)
	{
		if (html == null)
		{
			return "";
		}

		final Document document = Jsoup.parse(html);
		final OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
		document.outputSettings(outputSettings);
		document.select("br").append("\\n");
		document.select("p").prepend("\\n");
		document.select("p").append("\\n");
		final String newHtml = document.html().replaceAll("\\\\n", "\n");
		final String plainText = Jsoup.clean(newHtml, "", Whitelist.none(), outputSettings);
		final String result = StringEscapeUtils.unescapeHtml(plainText.trim());
		return result;
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
    private static String saveFile(InputStream is, String destDir, String root, String fileName)
            throws FileNotFoundException, IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        String realFileName = destDir + sf.format(new Date())+StrUtil.randomNum(3)+ 
        		fileName.substring(fileName.lastIndexOf("."));
        File downLoad = new File(root+realFileName);
        BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(downLoad));
        int len = -1;
        while ((len = bis.read()) != -1) {
            bos.write(len);
            bos.flush();
        }
        bos.close();
        bis.close();
        return realFileName;
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
