package com.cms.front.common;

import java.util.HashMap;
import java.util.Map;


import com.cms.admin.user.User;
import com.cms.front.entity.Application;
import com.cms.util.DateFmt;
import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

@Before(MInterceptor.class)
public class ApplicationController extends Controller {
	public void index() {
		//页数
		int pageNum = 1;
		if (getParaToInt("p") != null) {
			pageNum = getParaToInt("p");
		}
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
		StringBuffer sql =new StringBuffer("from application c where c.creator="+u.getInt("id"));
		if(getSessionAttr("application_field")!=null) {
			keyword = getSessionAttr("application_keyword");
			field = getSessionAttr("application_field");
			sql.append(" and c."+field+" like '%"+keyword+"%'");
		}
		
		
		page = Db.paginate(pageNum, 10, "select c.*",sql.toString());
		setAttr("contentPage", page);
		render("/t/application.html");
	}
	public void add() {
		render("/t/addapplication.html");
	}
	
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
