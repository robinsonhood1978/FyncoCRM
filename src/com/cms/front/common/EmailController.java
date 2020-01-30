package com.cms.front.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.cms.admin.user.User;
import com.cms.front.entity.Client;
import com.cms.util.DateFmt;
import com.cms.util.ReadMail;
import com.cms.util.TestMail;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

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
    	String from = u.getStr("sendmail_account");
    	String passwd = u.getStr("sendmail_password");
    	String mail_host = u.getStr("mail_host");
		ReadMail mail = new ReadMail();
    	Map<String, Object> map = mail.getMails(mail_host,from,passwd);
    	// System.out.println("888gdsajhdgj888");
    	// System.out.println(from+"----"+passwd+"----"+mail_host);
		renderJson(map);
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
