package com.cms.front.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import com.cms.admin.user.User;
import com.cms.front.entity.Application;
import com.cms.front.entity.ApplicationAtt;
import com.cms.front.entity.Client;
import com.cms.front.entity.Task;
import com.cms.util.DateFmt;
import com.cms.util.FileUtil;
import com.cms.util.PdfUtil;
import com.cms.util.StrUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Before(MInterceptor.class)
public class ApplicationController extends Controller {
	public void deldoc() throws JsonParseException, JsonMappingException, IOException {
		int code = 0;
		String ids= getPara("id");
		int appid = getParaToInt("appid");
		String realPath = this.getRequest().getRealPath("/");
		
		Application app = Application.dao.findById(appid);
		
		String docJson = app.getStr("documents");
		ObjectMapper mapper = new ObjectMapper();    
		Map<String,Object> oriMap = new HashMap<String,Object>();
		
		if(docJson!=null) {
			oriMap = mapper.readValue(docJson, Map.class);  
		}
		
		String[] idArr = ids.split(",");
		for(String idstr:idArr) {
			int id = Integer.parseInt(idstr);
			String classification = ApplicationAtt.dao.findById(id).getStr("classification");
			JSONArray oriMapArray = JSONArray.fromObject(oriMap.get(classification));
			int arrSize = oriMapArray.size();
			
			Iterator<Object> o = oriMapArray.iterator();
			while (o.hasNext()) {
			    JSONObject jo = (JSONObject) o.next();
			    if(jo.getInt("attid") == id) {
			    	String url = jo.getString("url");
			    	FileUtil.deleteAll(realPath+url);
			        o.remove();  
			    }
			}
			
			if(arrSize>1) {
				oriMap.put(classification, oriMapArray);
			}
			else {
				oriMap.remove(classification);
			}
			ApplicationAtt.dao.deleteById(id);	
		}

		String documentStr = mapper.writeValueAsString(oriMap);
		//System.out.println(documentStr);
		getModel(Application.class).set("id", appid).set("documents", documentStr).update();
//		Boolean b = ApplicationAtt.dao.deleteById(id);
//		if(b)code=0;

		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("code",code);
		renderJson(map);
	}
	public void savedoc() throws IOException {
		int code = 0;
		String name = getPara("name");
		String classification = getPara("classy");
		int id = getParaToInt("id");
		ApplicationAtt att = ApplicationAtt.dao.findById(id);
		String oldClass = att.getStr("classification");
		int appid = att.getInt("appid");
		Application app = Application.dao.findById(appid);
		
		String docJson = app.getStr("documents");
		ObjectMapper mapper = new ObjectMapper();    
		Map<String,Object> oriMap = new HashMap<String,Object>();
		
		if(docJson!=null) {
			oriMap = mapper.readValue(docJson, Map.class);  
		}
		JSONObject newObj = null;
		JSONArray newMapArray = new JSONArray();
		
		JSONArray oriMapArray = null;

		if(oriMap.get(oldClass)!=null) {
			oriMapArray = JSONArray.fromObject(oriMap.get(oldClass));
			int arrSize = oriMapArray.size();
			System.out.println("arr:"+oriMapArray.toString());
			
			Iterator<Object> o = oriMapArray.iterator();
			
			
			while (o.hasNext()) {
			    JSONObject jo = (JSONObject) o.next();
			    if(jo.getInt("attid") == id) {
			    	newObj = jo;
			    	newObj.put("classification", classification);
			    	newObj.put("filename", name);
			    	o.remove();
			    }
			}
			if(arrSize>1) {
				oriMap.put(oldClass, oriMapArray);
			}
			else {
				oriMap.remove(oldClass);
			}
		}
		if(oriMap.get(classification)!=null) {
			newMapArray = JSONArray.fromObject(oriMap.get(classification));
		}
		newMapArray.add(newObj);
		oriMap.put(classification, newMapArray);
		

		att.set("name", name).set("classification", classification).update();
		String documentStr = mapper.writeValueAsString(oriMap);
		//System.out.println(documentStr);
		app.set("documents", documentStr).update();
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("code",code);
		renderJson(map);
	}
	public void rename() {
		setAttr("action","rename");
		setAttr("att",ApplicationAtt.dao.findById(getPara("attid")));
		render("/t/rename_doc.html");
	}
	public void classy() {
		setAttr("action","classy");
		setAttr("att",ApplicationAtt.dao.findById(getPara("attid")));
		render("/t/rename_doc.html");
	}
	public void documents() throws JsonParseException, JsonMappingException, IOException {
		int code = 0;
		int appid = getParaToInt("application.id");
		String doc = getPara("documents");
		Application app = Application.dao.findById(appid);
		String docJson = app.getStr("documents");
		ObjectMapper mapper = new ObjectMapper();    
		Map<String,Object> oriMap = new HashMap<String,Object>();
		if(docJson!=null) {
			oriMap = mapper.readValue(docJson, Map.class);  
		}
		Map<String,Object> docMap = mapper.readValue(doc, Map.class);  
		int j = 1;
		for (String key : docMap.keySet()) {
			JSONArray oriMapArray = null;
			if(!oriMap.isEmpty()) {
				if(oriMap.get(key)!=null) {
					oriMapArray = JSONArray.fromObject(oriMap.get(key));
				}
			}
			JSONArray jsonArray = JSONArray.fromObject(docMap.get(key));
			
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				String filename = obj.getString("filename");
				String classification = obj.getString("classification");
				String filesize = obj.getString("filesize");
				String url = obj.getString("url");
//				Db.update("insert into application_att (appid,classification,name,filesize,url,priority) values (?,?,?,?,?,?)",
//						appid,classification,filename,filesize,url,j++);
				
				ApplicationAtt att = new ApplicationAtt().set("appid", appid).set("classification", classification).set("name",filename).set("filesize",filesize).set("url",url).set("priority",j++);
				att.save();
				obj.put("attid", att.getInt("id"));
				jsonArray.set(i, obj);
			}
			if(!oriMap.isEmpty()&&oriMap.get(key)!=null) {
				oriMapArray.addAll(jsonArray);
			}
			else {
				oriMapArray = jsonArray;
			}
			oriMap.put(key, oriMapArray);
			
		}

		String documentStr = mapper.writeValueAsString(oriMap);
		System.out.println(documentStr);
		getModel(Application.class).set("documents", documentStr).update();
		
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("code",code);
		renderJson(map);
	}
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
	public void uploadManager() {
		setAttr("application",Application.dao.findById(getPara("appid")));
		setAttr("docList",Db.find("select * from application_att where appid=?",getPara("appid")));
		render("/t/upload_manager.html");
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
		String realPath = this.getRequest().getRealPath("/");
		Integer[] ids = getParaValuesToInt("ids");
		String strId = "";
		if(ids!=null && ids.length>0){
			for(Integer id:ids){
				strId += id+",";
			}
			strId = strId.substring(0, strId.length()-1);
			Db.update("delete from application where id in ("+strId+")");
			Db.update("delete from application_client where application_id in ("+strId+")");
			Record r = Db.findFirst("select group_concat(url) urls from application_att where appid in ("+strId+")");
			String urls = r.getStr("urls");
			if(r.getStr("urls")!=null) {
				String[] urlArr = urls.split(",");
				for(String url:urlArr) {
					if(url!=null) {
						FileUtil.deleteAll(realPath+url);
					}
				}
				Db.update("delete from application_att where appid in ("+strId+")");
			}
		}
		else{
			if(getParaToInt()>0) {
				Application.dao.deleteById(getParaToInt());
				Db.update("delete from application_client where application_id =?",getParaToInt());
				Record r = Db.findFirst("select group_concat(url) urls from application_att where appid=?",getParaToInt());
				String urls = r.getStr("urls");
				if(r.getStr("urls")!=null) {
					String[] urlArr = urls.split(",");
					for(String url:urlArr) {
						if(url!=null) {
							FileUtil.deleteAll(realPath+url);
						}
					}
					Db.update("delete from application_att where appid =?",getParaToInt());
				}
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
