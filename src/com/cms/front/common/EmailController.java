package com.cms.front.common;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.MessagingException;
import javax.mail.Store;
import javax.mail.internet.NewsAddress;

import com.cms.admin.user.User;
import com.cms.front.entity.Client;
import com.cms.front.entity.Email;
import com.cms.util.DateFmt;
import com.cms.util.ReadMail;
import com.cms.util.TestMail;
import com.itextpdf.kernel.xmp.impl.Base64;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

@Before(MInterceptor.class)
public class EmailController extends Controller {
	public void avatar() {
		String mails = getPara("mails");
		String[] arr_mail = mails.split(",");
		HashMap<String,String> m = new HashMap<String,String>();
		for(String mail:arr_mail) {
			String avatar = Db.queryStr("select avatar from client where personal_email=? or work_email=? limit 1",mail,mail);
			if(avatar==null)avatar="";
			m.put(mail, avatar);
		}
		renderJson(m);
	}
	public void index() {
		render("/t/email.html");
	}
	public void setUpEmails() {
		User u = getSessionAttr("user");
		String from = u.getStr("sendmail_account");
    	String passwd = u.getStr("sendmail_password");
    	String host = u.getStr("mail_host");
    	Record r = Db.findFirst("select * from mailserver where name=?",host);
    	String realPath = this.getRequest().getRealPath("/");
    	System.out.println(realPath);
		Store store = null;
		System.out.println(Base64.decode(passwd));
		try {
			store = ReadMail.getStoreConnect(r.getColumns(), from,Base64.decode(passwd));
			ReadMail.getMails(r.getColumns(),store,realPath,u.getInt("id"),1);
	    	ReadMail.getMails(r.getColumns(),store,realPath,u.getInt("id"),0);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
            try {
                store.close();
                getModel(User.class).set("id", u.getInt("id")).set("email_set", 2).update();
                setSessionAttr("user", User.dao.findById(u.getInt("id")));
                System.out.println("stroe closed!");
                renderJson(1);
            } catch (MessagingException e) {
            	e.printStackTrace();
                // TODO Auto-generated catch block
            }
        }
	}
	public void autoEmailLoad() {
		final Timer timer = new Timer(); 
        TimerTask tt = new TimerTask() { 

            public void run() 
            { 
            	for (int i = 1; i <= 10; i++) { 
		            System.out.println("working on the task"); 
		            if (i >= 7) { 
		                System.out.println("stop the task"+getSessionAttr("emailKey")); 
		                // loop stops after 7 iterations 
		                timer.cancel(); 
		                break;
		            } 
            	}
                
            }; 
        }; 
        timer.schedule(tt, 1000, 5*1000); 
	}
	public void resetEmail() {
		renderJson(getModel(User.class).update());
	}
	public void mails() {
		setSessionAttr("emailKey","start");
		User u = getSessionAttr("user");
		System.out.println(getSessionAttr("emailKey"));
		autoEmailLoad();
		renderJson(u.getInt("email_set"));
	}
	public void getEmailDetails() {
		int recoedID = getParaToInt("id");
		Record emailDb = Db.findFirst("select messageId, boxId  from email where id=?", recoedID);
		System.out.println(emailDb.getStr("messageId")+"---MessageID");
		User u = getSessionAttr("user");
		String from = u.getStr("sendmail_account");
    	String passwd = u.getStr("sendmail_password");
    	String host = u.getStr("mail_host");
    	Record r = Db.findFirst("select * from mailserver where name=?",host);
    	String realPath = this.getRequest().getRealPath("/");
    	System.out.println(realPath);
		String fileName = getPara("fileName");
    	String localUrl = ReadMail.getEmailDetails(r.getColumns(),realPath,u.getInt("id"),fileName,recoedID,emailDb.getStr("messageId"),from,Base64.decode(passwd),emailDb.getInt("boxId"));
    	Map<String, Object> map = new HashMap<String, Object>();
    	if (localUrl.equals(null)) {
    		map.put("url","");
		}else {
			map.put("url",localUrl);
		}
    	renderJson(map);
	}
	public void removeFiles() {
		String realPath = this.getRequest().getRealPath("/");
		File file = new File(realPath+getPara("fileUrl"));
        renderJson(file.delete());
	}
	public void getEmails() {
		
//		try{  
//			
//			Connection con=(Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1/fynco?useUnicode=true&characterEncoding=UTF8&zeroDateTimeBehavior=convertToNull","root","deng123456");  
//			//here sonoo is database name, root is username and password  
//			Statement stmt=(Statement) con.createStatement();  
//			ResultSet rs=stmt.executeQuery("select * from email where id = 19");  
//			while(rs.next()) {
//				byte[] a =rs.getBytes("content");
//				String b = new String(a, StandardCharsets.UTF_8);
//				System.out.println(b);
//			}
//			
//			con.close();  
//			}catch(Exception e){ System.out.println(e);}  
		User u = getSessionAttr("user");
		List<Record> r = Db.find("select * from email where creator=? and type=? ORDER BY create_time DESC",u.getInt("id"),getPara("type"));
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("list",r);
		renderJson(map);
	}
	public void recoverEmail() {
		int base = Db.queryInt("select boxId from email where id=?",getParaToInt("email.id"));
		renderJson(getModel(Email.class).set("type", base).update());
	}
	public void deleteEmail() {
		int type = getParaToInt("email.type");
		if(type != 3) {
			renderJson(getModel(Email.class).update());
		}else {
			renderJson(getModel(Email.class).delete());
		}
	}
	public void changeFlag() {
		renderJson(getModel(Email.class).update());
	}
	public void setFlagToServer() {
		User u = getSessionAttr("user");
		String from = u.getStr("sendmail_account");
    	String passwd = u.getStr("sendmail_password");
    	String host = u.getStr("mail_host");
    	Record r = Db.findFirst("select * from mailserver where name=?",host);
    	Record emailDb = Db.findFirst("select messageId, boxId from email where id=?", getParaToInt("email.id"));
		boolean setFlag = ReadMail.setMailFlag(r.getColumns(), getPara("email.flag"), emailDb.getStr("messageId"), from,Base64.decode(passwd), emailDb.getInt("boxId"));
		renderJson(setFlag);
	}
	public void changeStatus() {
		renderJson(getModel(Email.class).update());
	}
	public void add() {
		render("/t/addlead.html");
	}
	public void edit() {
		setAttr("client",Client.dao.findById(getParaToInt()));
		render("/t/addlead.html");
	}
	public void update() {
		int code = 1;
		User u = getSessionAttr("user");
		String birthday = getPara("birthday");
		
		boolean b = getModel(Client.class).set("birthday", DateFmt.USDate(birthday)).update();
		if(b)code=0;
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("code",code);
		renderJson(map);
	}
	public void del() {
		Integer[] ids = getParaValuesToInt("ids");
		String strId = "";
		if(ids!=null && ids.length>0){
			for(Integer id:ids){
				strId += id+",";
			}
			strId = strId.substring(0, strId.length()-1);
			Db.update("delete from client where id in ("+strId+")");
		}
		else{
			if(getParaToInt()>0)
				Client.dao.deleteById(getParaToInt());
		}
		index();
	}
	public void save() {
		int code = 1;
		User u = getSessionAttr("user");
		String birthday = getPara("birthday");
		
		boolean b = getModel(Client.class).set("birthday", DateFmt.USDate(birthday)).set("creator", u.getInt("id")).save();
		if(b)code=0;
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("code",code);
		renderJson(map);
	}
}
