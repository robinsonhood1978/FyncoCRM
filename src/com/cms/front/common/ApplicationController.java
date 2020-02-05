package com.cms.front.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.cms.admin.user.User;
import com.cms.front.entity.Application;
import com.cms.front.entity.Client;
import com.cms.util.DateFmt;
import com.cms.util.FileUtil;
import com.cms.util.PdfUtil;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

@Before(MInterceptor.class)
public class ApplicationController extends Controller {
	public void view() {
		int id = getParaToInt("id");
		String layout = "c";
		int ilay = 0;
		
		if(getPara("l")!=null) {
			layout = getPara("l");
			ilay = 1;
		}
		setAttr("layout","_"+layout+".html");
		setAttr("ilay",ilay);
		setAttr("application",Application.dao.findById(id));
		setAttr("clients",Db.queryStr("select group_concat(client_id) clientlist from application_client where application_id=?",id));
		render("/t/application_detail.html");
	}
	public void index() {
		int status = 0;
		if(getPara("st")!=null) {
			status = getParaToInt("st");
			//setSessionAttr("application_status",status);
		}
		//页数
		int pageNum = 1;
		if (getParaToInt("p") != null) {
			pageNum = getParaToInt("p");
		}
		
//		if(getPara("st")==null&&getSessionAttr("application_status")!=null)
//			status = getSessionAttr("application_status");
		
		String keyword = getPara("keyword");
		String field = getPara("field");
		if(field!=null&&!"".equals(keyword)) {
			setSessionAttr("application_keyword",keyword);
			setSessionAttr("application_field",field);
		}
		else {
			if (getParaToInt("p") == null) {
				removeSessionAttr("application_field");
				removeSessionAttr("application_keyword");
			}
		}
		
		User u = getSessionAttr("user");
		Page<Record> page = null;
		// 当前页
		StringBuffer sql =new StringBuffer("from application c INNER JOIN application_client AS a ON a.application_id = c.id INNER JOIN client AS b ON a.client_id = b.id where c.status="+status+" and c.creator="+u.getInt("id")+" ORDER BY c.create_time DESC");
		if(getSessionAttr("application_field")!=null) {
			keyword = getSessionAttr("application_keyword");
			field = getSessionAttr("application_field");
			sql.append(" and c."+field+" like '%"+keyword+"%'");
		}
		
		
		page = Db.paginate(pageNum, 20, "select c.*, CONCAT_WS(' ', b.first_name, b.last_name) AS whole_name",sql.toString());
		setAttr("contentPage", page);
		setAttr("status", status);
		render("/t/application.html");
	}
	public void add() {
		render("/t/addapplication.html");
	}
	public void pdf() throws Exception {
		int id = getParaToInt("id");
		int code=0;
		String realPath = this.getRequest().getRealPath("/");
		String dest1 = "/Users/robin/eclipse-workspace/Fynco/WebRoot/upload/sm_4.pdf";
		String dest2 = "/Users/robin/eclipse-workspace/Fynco/WebRoot/upload/";
		//String src = "/Users/robin/pdf/sm_1.pdf";
		
		//PdfUtil.pdf_1(dest1,Application.dao.findById(id));
		String clients = Db.queryStr("select group_concat(client_id) clientids from application_client where application_id=?",id);
		String[] cs = clients.split(",");
		Client[] client = new Client[cs.length];
		for(int i=0;i<cs.length;i++) {
			client[i]=Client.dao.findById(cs[i]);
		}
		
		//PdfUtil.pdf_2(dest2,client,Application.dao.findById(id));
		//PdfUtil.pdf_4(dest1,Application.dao.findById(id));
		Application app = Application.dao.findById(id);
		String url = PdfUtil.pdf(realPath,client,app);
		if(!"".equals(url)) {
			//delete the old pdf,release the disk space
			String old_url = app.getStr("pdf");
			if(old_url!=null) {
				FileUtil.deleteAll(realPath+old_url);
			}
			app.set("pdf", url).update();
		}
		else {
			code=1;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code",code);
		map.put("url",url);
		renderJson(map);
	}
	public void changeStatus() {
		int code=0;
		int id = getParaToInt("id");
		int status = getParaToInt("status");
		int i = Db.update("update application set status=? where id=?",status,id);
		if(status==5) {
			List<Record> clients = Db.find("select c.id "
					+ "from application_client ac join client c on ac.client_id=c.id where ac.application_id=?",id);
			for(Record client:clients) {
				int clientid = client.getInt("id");
				Db.update("update client set type=1 where id=?",clientid);
			}
		}
		if(i==0) {
			code=1;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code",code);
		renderJson(map);
	}
	
	public void edit() {
		int id = getParaToInt();
		setAttr("application",Application.dao.findById(id));
		setAttr("clients",Db.queryStr("select group_concat(client_id) clientlist from application_client where application_id=?",id));
		render("/t/addapplication.html");
	}
	public void del() {
		Integer[] ids = getParaValuesToInt("ids");
		String strId = "";
		if(ids!=null && ids.length>0){
			for(Integer id:ids){
				strId += id+",";
			}
			strId = strId.substring(0, strId.length()-1);
			Db.update("delete from application where id in ("+strId+")");
			Db.update("delete from application_client where application_id in ("+strId+")");
		}
		else{
			if(getParaToInt()>0) {
				Application.dao.deleteById(getParaToInt());
				Db.update("delete from application_client where application_id =?",getParaToInt());
			}
		}
		index();
	}
	public void save() {
		int code = 1;
		User u = getSessionAttr("user");
		String application_date = getPara("application_date");
		String finance_date = getPara("finance_date");
		String settlement_date = getPara("settlement_date");
		String applicants_ids = getPara("applicants_ids");
		String[] ids = applicants_ids.split(",");
		System.out.println(applicants_ids);
		
		//boolean b = getModel(Application.class).set("type", 1).set("birthday", DateFmt.USDate(birthday)).set("creator", u.getInt("id")).save();
		boolean b = getModel(Application.class)
				.set("application_date", DateFmt.USDate(application_date))
				.set("finance_date", DateFmt.USDate(finance_date))
				.set("settlement_date", DateFmt.USDate(settlement_date))
				.set("creator", u.getInt("id")).save();
		if(b)code=0;
		int app_id = Db.queryInt("select max(id) from application");
		for(String id:ids) {
			Db.update("insert into application_client (application_id,client_id) values (?,?)",app_id,id);
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("code",code);
		renderJson(map);
	}
	public void update() {
		int code = 1;
		User u = getSessionAttr("user");
		int app_id = getParaToInt("application.id");
		String application_date = getPara("application_date");
		String finance_date = getPara("finance_date");
		String settlement_date = getPara("settlement_date");
		String applicants_ids = getPara("applicants_ids");
		String[] ids = applicants_ids.split(",");
		System.out.println(applicants_ids);
		
		//boolean b = getModel(Application.class).set("type", 1).set("birthday", DateFmt.USDate(birthday)).set("creator", u.getInt("id")).save();
		boolean b = getModel(Application.class)
				.set("application_date", DateFmt.USDate(application_date))
				.set("finance_date", DateFmt.USDate(finance_date))
				.set("settlement_date", DateFmt.USDate(settlement_date))
				.set("creator", u.getInt("id")).update();
		
		Db.update("delete from application_client where application_id=?",app_id);
		for(String id:ids) {
			Db.update("insert into application_client (application_id,client_id) values (?,?)",app_id,id);
		}
		if(b)code=0;
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("code",code);
		renderJson(map);
	}
}
