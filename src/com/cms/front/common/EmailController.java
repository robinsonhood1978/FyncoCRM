package com.cms.front.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
	public void mails() {
// 		String from = "robin@taijicoin.nz";
//     	String passwd = "111111";
// 		//String from = "fynco.storage@gmail.com";
//     	//String passwd = "fynco321";
// //    	TestMail m = new TestMail();
// //    	m.read();
// 		ReadMail mail = new ReadMail();
//     	Map<String, Object> map = mail.getMails(from,passwd);
// 		renderJson(map);
//dannel modify
		User u = getSessionAttr("user");
		if (u.getInt("email_set") == 1) {
			String from = u.getStr("sendmail_account");
	    	String passwd = u.getStr("sendmail_password");
	    	String host = u.getStr("mail_host");
	    	Record r = Db.findFirst("select * from mailserver where name=?",host);
	    	String realPath = this.getRequest().getRealPath("/");
	    	System.out.println(realPath);
			ReadMail mail = new ReadMail();
			mail.getMails(r.getColumns(),realPath,u.getInt("id"),1,from,passwd);
	    	mail.getMails(r.getColumns(),realPath,u.getInt("id"),0,from,passwd);
	    	// System.out.println("888gdsajhdgj888");
	    	//System.out.println(from+"----"+passwd+"----");
			renderJson(1);
		}else {
			renderJson(0);
		}
    	
	}
	public void getEmailDetails() {
		int recoedID = getParaToInt("id");
		String messageId = Db.queryColumn("select messageId from email where id=?", recoedID);
		System.out.println(messageId+"---MessageID");
		User u = getSessionAttr("user");
		String from = u.getStr("sendmail_account");
    	String passwd = u.getStr("sendmail_password");
    	String host = u.getStr("mail_host");
    	Record r = Db.findFirst("select * from mailserver where name=?",host);
    	String realPath = this.getRequest().getRealPath("/");
    	System.out.println(realPath);
		String fileName = getPara("fileName");
    	String localUrl = ReadMail.getEmailDetails(r.getColumns(),realPath,u.getInt("id"),fileName,recoedID,messageId,from,passwd);
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
			renderJson(1);// unfinsh coding
		}
		
	}
	public void changeFlag() {
		renderJson(getModel(Email.class).update());
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
