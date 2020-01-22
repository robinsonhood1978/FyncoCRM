package com.cms.front.common;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cms.admin.content.Content;
import com.cms.admin.user.User;
import com.cms.util.DateFmt;
import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

@Before(MInterceptor.class)
public class CalendarController extends Controller {
	public void otheruser() {
		User u = getSessionAttr("user");
		List<Record> l = Db.find("select id,concat(first_name,' ',last_name) name from user where id!=?",u.getInt("id"));
		renderJson(l);
	}
	public void index() {
		render("/t/fullcalendar.html");
	}
	public void del() {
		int id = getParaToInt("id");
		Db.update("delete from message where type=1 and link_id in (select id from alert where parent_id=?)",id);
		Db.update("delete from message where type=1 and link_id=?",id);
		Db.update("delete from alert where parent_id=?",id);
		int i = Db.update("delete from alert where id=?",id);
		Map map = new HashMap();
		map.put("code",i);
		renderJson(map);
	}
	public void add() {
		User u = getSessionAttr("user");
		//allDay=0  start=1575244800000  end=1575331200000  className=bg-danger  title=33  
		String title = getPara("title");
		long start = getParaToLong("start")/1000;
		long end = getParaToLong("end")/1000;
		String class_name = getPara("className");
		
		int type=20;
		String type_name = "";
		//消息类型 0-News,1-Notice,20-Meeting,21-Interview,22-Appointment,23-Event,24-Activity,25-Other,4-Task,5-Refix,6-Birthday
		switch(class_name) 
        { 
            case "bg-danger": 
                type=20;
                type_name="Meeting";
                break; 
            case "bg-success": 
            	type=21;
            	type_name="Interview";
                break; 
            case "bg-primary": 
            	type=22;
            	type_name="Appointment";
                break; 
            case "bg-info": 
            	type=23;
            	type_name="Event";
                break; 
            case "bg-dark": 
            	type=24;
            	type_name="Activity";
                break; 
            case "bg-warning": 
            	type=25;
            	type_name="Other";
                break; 
            default: 
                System.out.println("no match"); 
        } 
		int all_day = getParaToInt("allDay");
		String others = getPara("others");
		int reminder = getParaToInt("reminder");
		String start_date=DateFmt.TimeStamp2Date(String.valueOf(start), "");
		String reminder_date = DateFmt.addLongDays(start_date, (-1)*reminder);
		String local_time = DateFmt.timestamp2str(new Timestamp(start*1000),"hh:mm a dd/MM/yyyy").replace("AM", "am").replace("PM", "pm");
		
		
		
		int i = Db.update("insert into alert (title,type,type_name,start_time,end_time,class_name,allday,creator,user_id,alert_time) values "
				+ "(?,?,?,from_unixtime(?),from_unixtime(?),?,?,?,?,?)",title,type,type_name,start,end,class_name,all_day,u.getInt("id"),u.getInt("id"),reminder_date);
		int id = Db.queryInt("select max(id) from alert");
		Db.update("insert into message (name,link_id,type,type_name,sender,receiver,create_time,alert_time) values (?,?,?,?,?,?,?,?)",
				"You have an upcoming "+type_name+" ["+title+"] at "+local_time,id,type,type_name,u.getInt("id"),u.getInt("id"),DateFmt.addLongDays(0),reminder_date);
		int link_id = id;
		if(!others.equals("")) {
			String[] arr_others = others.split(",");
			for(String other:arr_others){
				Db.update("insert into alert (parent_id,title,type,type_name,start_time,end_time,class_name,allday,creator,user_id,alert_time) values "
						+ "(?,?,?,?,from_unixtime(?),from_unixtime(?),?,?,?,?,?)",id,title,type,type_name,start,end,class_name,all_day,u.getInt("id"),other,reminder_date);
				Db.update("insert into message (name,link_id,type,type_name,sender,receiver,create_time,alert_time) values (?,?,?,?,?,?,?,?)",
						"You have an upcoming "+type_name+" ["+title+"] at "+local_time,++link_id,type,type_name,u.getInt("id"),other,DateFmt.addLongDays(0),reminder_date);
			
			}
		}
		Map map = new HashMap();
		map.put("code",i);
		map.put("id",id);
		renderJson(map);
	}
	public void update() {
		//User u = getSessionAttr("user");
		//allDay=0  start=1575244800000  end=1575331200000  className=bg-danger  title=33  
		int id = getParaToInt("id");
		String title = getPara("title");
//		long start = getParaToLong("start")/1000;
//		long end = getParaToLong("end")/1000;
//		int i = Db.update("update alert set title=?,start_time=from_unixtime(?),end_time=from_unixtime(?) where id=?",title,start,end,id);
		int i = Db.update("update alert set title=? where id=?",title,id);
		Map map = new HashMap();
		map.put("code",i);
		renderJson(map);
	}
	public void detail() {
		int id = Db.queryInt("select max(id) from alert");
		Map map = new HashMap();
		map.put("id",id);
		renderJson(map);
	}
	@ClearInterceptor(ClearLayer.ALL)
	public void user() {
		User u = getSessionAttr("user");
		List<Record> l = Db.find("select 0 ifnew,id alertid,title,UNIX_TIMESTAMP(start_time)*1000+13*3600000 start,UNIX_TIMESTAMP(end_time)*1000+13*3600000 end,class_name className from alert where user_id=?",u.getInt("id"));
		
		renderJson(l);
		//renderText("[{title: 'Hey!',start: 1576833300000,className: 'bg-warning'}, {title: 'See John Deo',start: 1576833300000,end: 1576833300000,className: 'bg-success'}, {title: 'Meet John Deo',start: 1576833300000,className: 'bg-info'}, {title: 'Buy a Theme',start: 1576833300000,className: 'bg-primary'}]");
	}
}
