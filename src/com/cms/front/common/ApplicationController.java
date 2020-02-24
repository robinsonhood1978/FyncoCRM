package com.cms.front.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.cms.admin.user.User;
import com.cms.front.entity.Application;
import com.cms.front.entity.Client;
import com.cms.front.entity.Task;
import com.cms.util.DateFmt;
import com.cms.util.FileUtil;
import com.cms.util.PdfUtil;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Before(MInterceptor.class)
public class ApplicationController extends Controller {
	public void saverate() {
		int code = 0;
		String loanJson = getPara("loan");
		int appid = getParaToInt("appid");
		Application app = Application.dao.findById(appid);
		String oriLoanJson = app.getStr("loan");
		JSONArray jsonArray = JSONArray.fromObject(loanJson);
		JSONArray oriJsonArray = JSONArray.fromObject(oriLoanJson);
		JSONArray ja = new JSONArray();
		for (int i = 0; i < oriJsonArray.size(); i++) {
		    JSONObject oriloan = oriJsonArray.getJSONObject(i);
		    JSONObject loan = jsonArray.getJSONObject(i);
		    if(loan.getInt("order_id")==oriloan.getInt("order_id")) {
		    	oriloan.put("commission_rate", loan.getString("commission_rate"));	
		    	if(!loan.getString("commission_rate").equals("")) {
		    		double commission_fee = oriloan.getInt("loan_amount")* Double.parseDouble(loan.getString("commission_rate"));
		    		oriloan.put("commission_fee", String.format("%.2f", commission_fee));	
		    	}
		    }
		    ja.add(oriloan);
		    
		}
		getModel(Application.class).set("id", appid).set("loan", ja.toString()).update();
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("code",code);
		renderJson(map);
	}
	public void editrate() {
		setAttr("application",Application.dao.findById(getPara("appid")));
		render("/t/edit_rate.html");
	}
	public void saveloan() {
		int code = 0;

		String loanJson = getPara("loan");
		int appid = getParaToInt("appid");
		

		getModel(Application.class).set("id", appid).set("loan", loanJson).update();
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("code",code);
		renderJson(map);
	}
	public void addloan() {
		setAttr("application",Application.dao.findById(getPara("appid")));
		render("/t/addloan.html");
	}
	public void editloan() {
		setAttr("application",Application.dao.findById(getPara("appid")));
		render("/t/addloan.html");
	}
	public void manager() {
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
		setAttr("clients",Db.find("select c.* from client c join application_client ac on c.id=ac.client_id where ac.application_id=?",id));
		render("/t/application_manager.html");
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
		StringBuffer sql =new StringBuffer("from application a left join application_client ac on a.id=ac.application_id left join client c on ac.client_id=c.id where a.status="+status+" and a.creator="+u.getInt("id"));
		if(getSessionAttr("application_field")!=null) {
			keyword = getSessionAttr("application_keyword");
			field = getSessionAttr("application_field");
			if(field.equals("name")) {
				sql.append(" and (c.first_name like '%"+keyword+"%' or c.last_name like '%"+keyword+"%')");
			}
			else {
				sql.append(" and a."+field+" like '%"+keyword+"%'");
			}
		}
		sql.append(" group by a.id ORDER BY a.create_time DESC");
		
		
		page = Db.paginate(pageNum, 20, "select GROUP_CONCAT(ac.client_id,'-',ifnull(c.first_name,''),' ',ifnull(c.last_name,'')) as dealstr,a.id,a.loan,a.status,a.lender,a.lending_purpose,a.lending_amount_required,a.application_date",sql.toString());
//		System.out.println(page.getTotalPage());
//		System.out.println(page.getTotalRow());
		setAttr("contentPage", page);
		setAttr("status", status);
		setAttr("totals",Db.queryBigDecimal("select SUM(a.lending_amount_required) AS lending_totals from application a where a.status=? and a.creator=?",status,u.getInt("id")));
		render("/t/application.html");
	}
	public void add() {
		render("/t/addapplication.html");
	}
	public void pdf() throws Exception {
		int id = getParaToInt("id");
		User u = getSessionAttr("user");
		String logo = u.getStr("logo");
		int useLogo = u.getInt("uselogo");
		int code=1;
		String realPath = this.getRequest().getRealPath("/");
		String dest1 = "/Users/robin/eclipse-workspace/Fynco/WebRoot/upload/sm_4.pdf";
		String dest2 = "/Users/robin/eclipse-workspace/Fynco/WebRoot/upload/";
		//String src = "/Users/robin/pdf/sm_1.pdf";
		
		//PdfUtil.pdf_1(dest1,Application.dao.findById(id));
		String clients = Db.queryStr("select group_concat(client_id) clientids from application_client where application_id=?",id);
		String url ="";
		if(clients!=null) {
			String[] cs = clients.split(",");
			Client[] client = new Client[cs.length];
			for(int i=0;i<cs.length;i++) {
				client[i]=Client.dao.findById(cs[i]);
			}
			
			//PdfUtil.pdf_2(dest2,client,Application.dao.findById(id));
			//PdfUtil.pdf_4(dest1,Application.dao.findById(id));
			Application app = Application.dao.findById(id);
			url = PdfUtil.pdf(realPath,client,app,logo,useLogo);
			if(!"".equals(url)) {
				//delete the old pdf,release the disk space
				String old_url = app.getStr("pdf");
				if(old_url!=null) {
					FileUtil.deleteAll(realPath+old_url);
				}
				app.set("pdf", url).update();
				code=0;
			}
			
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("code",code);
		map.put("url",url);
		renderJson(map);
	}
	public void changeStatus() {
		int code=0;
		int appid = getParaToInt("id");
		int status = getParaToInt("status");
		
		Application app = Application.dao.findById(appid);
		if(status==5) {
			User u = getSessionAttr("user");
			
			String loanJson = app.getStr("loan");
			List<Record> clients = Db.find("select c.id,c.first_name,c.last_name "
					+ "from application_client ac join client c on ac.client_id=c.id where ac.application_id=?",appid);
			for(Record client:clients) {
				int id = client.getInt("id");
				
//				String cids = Db.queryStr("select group_concat(client_id) from application_client where application_id=? group by application_id",appid); 
//				System.out.println("cid:"+cids);
//				String[] cidArr = cids.split(",");
//				for(String cid:cidArr) {
//					int id = Integer.parseInt(cid);
					String originLoan = Db.queryStr("select loan from client where id=?",id);
					JSONArray ja = JSONArray.fromObject(originLoan);
					
					int message_type=5;
					String type_name = "Refix";
					JSONArray jsonArray = JSONArray.fromObject(loanJson);

					for (int i = 0; i < jsonArray.size(); i++) {
					    JSONObject loan = jsonArray.getJSONObject(i);
					    int loan_reminder_days = loan.getInt("loan_reminder_days");
					    String loan_review_date = loan.getString("loan_review_date");
					    String loan_notes = loan.getString("loan_notes");
					    if(!"".equals(loan_review_date)) {
						    String reminder_date = DateFmt.addDays(loan_review_date,"dd/MM/yyyy","yyyy-MM-dd", (-1)*loan_reminder_days);
						    
						    getModel(Task.class)
						    .set("client_id",id)
						    .set("type",1)
						    .set("title",client.getStr("first_name")+" "+client.getStr("last_name"))
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
									+ "(?,?,?,?,?,?,?,?)","You have an upcoming Refix with client ["+client.getStr("first_name")+" "+client.getStr("last_name")+"] due on "+loan_review_date,taskId,message_type,type_name,
									u.getInt("id"),u.getInt("id"),DateFmt.addLongDays(0),reminder_date);
					    }
					    ja.add(loan);
					    
					}
			
					getModel(Client.class).set("type", 1).set("id", id).set("loan", ja.toString()).update();
				//}
				
				//Db.update("update client set type=1 where id=?",clientid);
			}
		}
		String state_history = app.getStr("state_history");
		JSONArray json = JSONArray.fromObject(state_history);
		JSONObject obj = new JSONObject();
		obj.put("time", DateFmt.USDate());
		String[] statusStr = {"Prosessing","Submitted","Pre-approved","Conditionally Approved","Pending Settlement","Completed"};
		obj.put("msg", statusStr[status]);
		json.add(obj);
		int b = Db.update("update application set status=?,state_history=? where id=?",status,json.toString(),appid);
		if(b==0) {
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
		
		
		//boolean b = getModel(Application.class).set("type", 1).set("birthday", DateFmt.USDate(birthday)).set("creator", u.getInt("id")).save();
		JSONArray json = new JSONArray();
		JSONObject obj = new JSONObject();
		obj.put("time", DateFmt.USDate());
		obj.put("msg", "Created");
		json.add(obj);
		
		boolean b = getModel(Application.class)
				.set("application_date", DateFmt.USDate(application_date))
				.set("finance_date", DateFmt.USDate(finance_date))
				.set("settlement_date", DateFmt.USDate(settlement_date))
				.set("state_history",json.toString())
				.set("creator", u.getInt("id")).save();
		if(b)code=0;
		int app_id = Db.queryInt("select max(id) from application");
		if(!applicants_ids.equals("")) {
			String[] ids = applicants_ids.split(",");
			for(String id:ids) {
				Db.update("insert into application_client (application_id,client_id) values (?,?)",app_id,id);
			}
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("code",code);
		map.put("appid",app_id);
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

		boolean b = getModel(Application.class)
				.set("application_date", DateFmt.USDate(application_date))
				.set("finance_date", DateFmt.USDate(finance_date))
				.set("settlement_date", DateFmt.USDate(settlement_date))
				.set("creator", u.getInt("id")).update();
		if(!applicants_ids.equals("")) {
			String[] ids = applicants_ids.split(",");
			Db.update("delete from application_client where application_id=?",app_id);
			for(String id:ids) {
				Db.update("insert into application_client (application_id,client_id) values (?,?)",app_id,id);
			}
		}
		if(b)code=0;
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("code",code);
		map.put("appid",app_id);
		renderJson(map);
	}
}
