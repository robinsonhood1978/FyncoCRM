package com.cms.front.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.util.ajax.JSON;

import com.cms.admin.user.User;
import com.cms.front.entity.Client;
import com.cms.front.entity.Task;
import com.cms.util.DateFmt;
import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Before(MInterceptor.class)
public class ClientController extends Controller {
	public void editrefix() {
		if(getPara()!=null){
			int client_id = getParaToInt();
			Client c = Client.dao.findById(client_id);
			setAttr("client",c);
		}
		render("/t/addrefix.html");
	}
	public void addrefix() {
		render("/t/addrefix.html");
	}
	@ClearInterceptor(ClearLayer.ALL)
	public void get_applicants() {
		String ids = getPara("ids");
		int code = 0;
		List<Record> l = Db.find("select id,first_name,last_name,avatar from client where id in ("+ids+")");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code",code);
		map.put("data",l);
		renderJson(map);
		
	}
	public void get_applicants_name() {
		String ids = getPara("ids");
		int code = 0;
		List<Record> l = Db.find("select id,CONCAT_WS(' ',first_name,last_name) as client_name from client where id in ("+ids+")");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code",code);
		map.put("data",l);
		renderJson(map);
	}
	
	public void from_applicant() {
		applicant(2);
	}
	public void applicantfromlead() {
		applicant(0);
	}
	public void applicantfromclient() {
		applicant(1);
	}
	public void applicant(int type) {
		//页数
		int pageNum = 1;
		if (getParaToInt("p") != null) {
			pageNum = getParaToInt("p");
		}
		String keyword = getPara("keyword");
		String field = getPara("field");
		if(field!=null&&!"".equals(keyword)) {
			setSessionAttr("client_keyword",keyword);
			setSessionAttr("client_field",field);
		}
		else {
			if (getParaToInt("p") == null) {
				removeSessionAttr("client_field");
				removeSessionAttr("client_keyword");
			}
		}
		
		User u = getSessionAttr("user");
		Page<Client> page = null;
		// 当前页
		StringBuffer sql =new StringBuffer("from client c where c.type="+type+" and c.creator="+u.getInt("id"));
		if(getSessionAttr("client_field")!=null) {
			keyword = getSessionAttr("client_keyword");
			field = getSessionAttr("client_field");
			if(field.equals("name")) {
				sql.append(" and (c.first_name like '%"+keyword+"%' or c.last_name like '%"+keyword+"%')");
			}
			else {
				sql.append(" and c."+field+" like '%"+keyword+"%'");
			}
		}
		
		
		page = Client.dao.paginate(pageNum, 1000, "select c.*",sql.toString());
		setAttr("contentPage", page);
		render("/t/search_client.html");
	}
	public void index() {
		//页数
		int pageNum = 1;
		if (getParaToInt("p") != null) {
			pageNum = getParaToInt("p");
		}
		String keyword = getPara("keyword");
		String field = getPara("field");
		if(field!=null&&!"".equals(keyword)) {
			setSessionAttr("client_keyword",keyword);
			setSessionAttr("client_field",field);
		}
		else {
			if (getParaToInt("p") == null) {
				removeSessionAttr("client_field");
				removeSessionAttr("client_keyword");
			}
		}
		
		User u = getSessionAttr("user");
		Page<Client> page = null;
		// 当前页
//		StringBuffer sql =new StringBuffer("from client c where c.type=1 and c.creator="+u.getInt("id")); dannel modify
		StringBuffer sql =new StringBuffer("from client c INNER JOIN user u ON c.creator=u.id where c.type=1 and c.company_id="+u.getInt("company_id"));
		
		if(getSessionAttr("client_field")!=null) {
			keyword = getSessionAttr("client_keyword");
			field = getSessionAttr("client_field");
			if(field.equals("name")) {
				sql.append(" and (c.first_name like '%"+keyword+"%' or c.last_name like '%"+keyword+"%')");
			}
			else {
				sql.append(" and c."+field+" like '%"+keyword+"%'");
			}
		}
		sql.append(" ORDER BY c.create_time DESC");
		
//		page = Client.dao.paginate(pageNum, 10, "select c.*",sql.toString()); dannel modify
		page = Client.dao.paginate(pageNum, 10, "select c.*, concat(u.first_name, ' ', u.last_name) as broker",sql.toString());
		setAttr("contentPage", page);
		render("/t/clients.html");
	}
	public void add() {
		int type= 1;
		String layout = "c";
		int ilay = 0;
		int iframe = 0;
		int tb = 0;
		if(getPara("type")!=null) {
			type= getParaToInt("type");
		}
		if(getPara("l")!=null) {
			layout = getPara("l");
			ilay = 1;
		}
		if(getPara("i")!=null) {
			iframe =  getParaToInt("i");
		}
		if(getPara("tb")!=null) {
			tb =  getParaToInt("tb");
		}
		setAttr("type",type);
		setAttr("iframe",iframe);
		setAttr("tb",tb);
		setAttr("layout","_"+layout+".html");
		setAttr("ilay",ilay);
		render("/t/addclient.html");
	}
	public void view() {
		int id = getParaToInt("id");
		int type= 1;
		String layout = "c";
		int ilay = 0;
		if(getPara("type")!=null) {
			type= getParaToInt("type");
		}
		if(getPara("l")!=null) {
			layout = getPara("l");
			ilay = 1;
		}
		setAttr("type",type);
		setAttr("layout","_"+layout+".html");
		setAttr("ilay",ilay);
		setAttr("client",Client.dao.findById(id));
		ArrayList<HashMap<String,Object>> arrlist = new ArrayList<HashMap<String,Object>>();
		
		List<Record> list = Db.find("select a.id,a.application_date,a.lending_purpose,a.purpose,a.documents "
				+ "from application a left join application_client ac on a.id=ac.application_id "
				+ "where ac.client_id=?",id);
		String appids = "";
		int i=0;
		//HashMap<String,Object> documentMap = new HashMap<String,Object>();
		for(Record r:list) {
			i++;
			int appid = r.getInt("id");
			if(i>1)appids+=",";
			appids+=appid;
			HashMap<String,Object> single = new HashMap<String,Object>();
			List<Record> clients = Db.find("select c.id,concat(c.first_name,' ',c.last_name) name "
					+ "from application_client ac join client c on ac.client_id=c.id where ac.application_id=?",appid);
			single.put("r",r);
			single.put("clients",clients);
			arrlist.add(single);
			//documentMap.put("id_"+String.valueOf(appid), r.getStr("documents"));
		}
		//JSONObject obj = JSONObject.fromObject(documentMap);
		//setAttr("jsonMap",obj.toString());
		setAttr("apps",arrlist);
		setAttr("appids",appids);
		render("/t/client_detail.html");
	}
	public void edit() {
		int id = getParaToInt("id");
		int type= 1;
		String layout = "c";
		int ilay = 0;
		if(getPara("type")!=null) {
			type= getParaToInt("type");
		}
		if(getPara("l")!=null) {
			layout = getPara("l");
			ilay = 1;
		}
		setAttr("type",type);
		setAttr("layout","_"+layout+".html");
		setAttr("ilay",ilay);
		setAttr("client",Client.dao.findById(id));
		render("/t/addclient.html");
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
		String loanJson = getPara("client.loan");
		int type = getParaToInt("client.type");
		
		int id = Db.queryLong("select ifnull(max(id),0) from client").intValue()+1;
		System.out.println("id="+id);
		int message_type=5;
		String type_name = "Refix";
		System.out.println(u.getInt("company_id")+"yyyyyyyyyyyyyyyy");
		JSONArray jsonArray = JSONArray.fromObject(loanJson);
		JSONArray ja = new JSONArray();
		for (int i = 0; i < jsonArray.size(); i++) {
		    JSONObject loan = jsonArray.getJSONObject(i);
		    int loan_reminder_days = loan.getInt("loan_reminder_days");
		    String loan_review_date = loan.getString("loan_review_date");
		    String loan_notes = loan.getString("loan_notes");
		    if(!"".equals(loan_review_date)&& type==1) {
			    String reminder_date = DateFmt.addDays(loan_review_date,"dd/MM/yyyy","yyyy-MM-dd", (-1)*loan_reminder_days);
			    
			    getModel(Task.class)
			    .set("client_id",id)
			    .set("type",1)
			    .set("title",getPara("client.first_name")+" "+getPara("client.last_name"))
			    .set("description",loan_notes)
			    .set("due_date",DateFmt.formatDate(loan_review_date,"dd/MM/yyyy","yyyy-MM-dd"))
			    .set("reminder_date", reminder_date)
			    .set("reminder_days", loan_reminder_days)
			    .set("user_id", u.getInt("id"))
			    .set("creator", u.getInt("id"))
			    .save();
				int taskId = Db.queryInt("select max(id) from task");
				loan.put("task_id", taskId);
				
				Db.update("insert into message (name,link_id,type,type_name,sender,receiver,create_time,alert_time) values "
						+ "(?,?,?,?,?,?,?,?)","You have an upcoming Refix with client ["+getPara("client.first_name")
						+" "+getPara("client.last_name")+"] due on "+loan_review_date,taskId,message_type,type_name,
						u.getInt("id"),u.getInt("id"),DateFmt.addLongDays(0),reminder_date);
		    }
		    ja.add(loan);
		    
		}
		//System.out.println(ja.toString());

		boolean b = getModel(Client.class).set("id", id).set("loan", ja.toString()).set("birthday", DateFmt.USDate(birthday)).set("creator", u.getInt("id")).set("company_id", u.getInt("company_id")).save();
		if(b)code=0;
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("code",code);
		map.put("id",id);
		renderJson(map);
	}
	public void update() {
		int code = 1;
		User u = getSessionAttr("user");
		int type = getParaToInt("client.type");
		String birthday = getPara("birthday");
		int client_id = getParaToInt("client.id");
		String loanJson = getPara("client.loan");
		String deltasks = getPara("deltasks");
		System.out.println(deltasks);
		String oldtasks = getPara("oldtasks");
		if(!"".equals(deltasks)) {
			String[] delArr = deltasks.split(",");
			for(String del:delArr) {
				Db.update("delete from task where id=?",Integer.parseInt(del));
				Db.update("delete from message where link_id=? and type=5",Integer.parseInt(del));
			}
		}
		String[] oldArr = null;
		HashSet<String> set = null;
		if(!"".equals(oldtasks)) {
			oldArr =oldtasks.split(",");
			set = new HashSet<String>(Arrays.asList(oldArr));
		}

		int message_type=5;
		String type_name = "Refix";
		JSONArray jsonArray = JSONArray.fromObject(loanJson);
		JSONArray ja = new JSONArray();
		for (int i = 0; i < jsonArray.size(); i++) {
		    JSONObject loan = jsonArray.getJSONObject(i);
			int taskId = loan.getInt("task_id");
		    int loan_reminder_days = loan.getInt("loan_reminder_days");
		    String loan_review_date = loan.getString("loan_review_date");
		    String loan_notes = loan.getString("loan_notes");
		    String reminder_date = DateFmt.addDays(loan_review_date,"dd/MM/yyyy","yyyy-MM-dd", (-1)*loan_reminder_days);
		    if(type==1) {
		    	if(set!=null && set.contains(String.valueOf(taskId))) {
				    getModel(Task.class)
				    .set("id",taskId)
				    .set("type",1)
				    .set("title",getPara("client.first_name")+" "+getPara("client.last_name"))
				    .set("description",loan_notes)
				    .set("due_date",DateFmt.formatDate(loan_review_date,"dd/MM/yyyy","yyyy-MM-dd"))
				    .set("reminder_date", reminder_date)
				    .set("reminder_days", loan_reminder_days)
				    .update();
				    Db.update("update message set name=?,alert_time=? where link_id=? and type=? and sender=? and receiver=?",
							"You have an upcoming Refix with client ["+getPara("client.first_name")
							+" "+getPara("client.last_name")+"] due on "+loan_review_date,reminder_date,taskId,message_type,u.getInt("id"),u.getInt("id"));
			    }
			    else {
			    	getModel(Task.class)
				    .set("client_id",client_id)
				    .set("type",1)
				    .set("title",getPara("client.first_name")+" "+getPara("client.last_name"))
				    .set("description",loan_notes)
				    .set("due_date",DateFmt.formatDate(loan_review_date,"dd/MM/yyyy","yyyy-MM-dd"))
				    .set("reminder_date", reminder_date)
				    .set("reminder_days", loan_reminder_days)
				    .set("user_id", u.getInt("id"))
				    .set("creator", u.getInt("id"))
				    .save();
					taskId = Db.queryInt("select max(id) from task");
					loan.put("task_id", taskId);
					
					Db.update("insert into message (name,link_id,type,type_name,sender,receiver,create_time,alert_time) values "
							+ "(?,?,?,?,?,?,?,?)","You have an upcoming Refix with client ["+getPara("client.first_name")
							+" "+getPara("client.last_name")+"] due on "+loan_review_date,taskId,message_type,type_name,
							u.getInt("id"),u.getInt("id"),DateFmt.addLongDays(0),reminder_date);
			    }
		    }
		    ja.add(loan);
			
		    
		}
		boolean b = getModel(Client.class).set("loan", ja.toString()).set("birthday", DateFmt.USDate(birthday)).update();
		if(b)code=0;
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("code",code);
		renderJson(map);
	}
}
