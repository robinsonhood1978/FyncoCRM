package com.cms.front.common;

import java.util.HashMap;
import java.util.Map;


import com.cms.admin.user.User;
import com.cms.front.entity.Client;
import com.cms.util.DateFmt;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;

@Before(MInterceptor.class)
public class LeadController extends Controller {
	public void index() {
		//页数
		int pageNum = 1;
		if (getParaToInt("p") != null) {
			pageNum = getParaToInt("p");
		}
		String keyword = getPara("keyword");
		String field = getPara("field");
		if(field!=null&&!"".equals(keyword)) {
			setSessionAttr("lead_keyword",keyword);
			setSessionAttr("lead_field",field);
		}
		else {
			if (getParaToInt("p") == null) {
				removeSessionAttr("lead_field");
				removeSessionAttr("lead_keyword");
			}
		}
		
		User u = getSessionAttr("user");
		Page<Client> page = null;
		// 当前页
		StringBuffer sql =new StringBuffer("from client c where c.type=0 and c.creator="+u.getInt("id"));
		if(getSessionAttr("lead_field")!=null) {
			keyword = getSessionAttr("lead_keyword");
			field = getSessionAttr("lead_field");
			if(field.equals("name")) {
				sql.append(" and (c.first_name like '%"+keyword+"%' or c.last_name like '%"+keyword+"%')");
			}
			else {
				sql.append(" and c."+field+" like '%"+keyword+"%'");
			}
		}
		
		
		page = Client.dao.paginate(pageNum, 10, "select c.*",sql.toString());
		setAttr("contentPage", page);
		render("/t/leads.html");
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
