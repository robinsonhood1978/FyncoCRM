package com.cms.front.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.cms.admin.user.User;
import com.cms.front.entity.Client;
import com.cms.front.entity.Task;
import com.cms.util.DateFmt;
import com.cms.util.ReadMail;
import com.cms.util.TestMail;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

@Before(MInterceptor.class)
public class TaskController extends Controller {
	public void index() {
		User u = getSessionAttr("user");
		List<Record> ts = Db.find("select * from task t where t.type=0 and t.user_id='"+u.getInt("id")+"'ORDER BY t.due_date ASC");
		setAttr("tasks",ts);
		List<Record> rs = Db.find("select * from task t where t.type=1 and t.user_id='"+u.getInt("id")+"'ORDER BY t.due_date ASC");
		setAttr("refixs",rs);
		render("/t/task.html");
	}
	
	public void add() {
		render("/t/addtask.html");
	}
	public void edit() {
		setAttr("task",Task.dao.findById(getParaToInt()));
		render("/t/addtask.html");
	}
	public void update() {
		int code = 1;
		User u = getSessionAttr("user");
		int reminder = getParaToInt("task.reminder_days");
		String due = getPara("task.due_date");
		String reminder_date = DateFmt.addLongDays(due, (-1)*reminder);
		String local_date = DateFmt.format2str( due,"yyyy-MM-dd HH:mm:ss","hh:mm a dd/MM/yyyy").replace("AM", "am").replace("PM", "pm");
		
		
		boolean b = getModel(Task.class).set("reminder_date", reminder_date).set("user_id", u.getInt("id")).set("creator", u.getInt("id")).update();
		int taskId = getParaToInt("task.id");;
		int type=4;
		Db.update("update message set name=?,alert_time=? where link_id=? and type=? and sender=? and receiver=?",
				"You have a task ["+getPara("task.title")+"] due at "+local_date,reminder_date,taskId,type,u.getInt("id"),u.getInt("id"));
		if(b)code=0;
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("code",code);
		renderJson(map);
	}
	public void del() {
		User u = getSessionAttr("user");
		Integer[] ids = getParaValuesToInt("ids");
		String strId = "";
		if(ids!=null && ids.length>0){
			for(Integer id:ids){
				strId += id+",";
			}
			strId = strId.substring(0, strId.length()-1);
			Db.update("delete from message where link_id in ("+strId+") and type=4 and sender=? and receiver=?",u.getInt("id"),u.getInt("id"));
			Db.update("delete from task where id in ("+strId+")");
		}
		else{
			if(getParaToInt()>0) {
				int id=getParaToInt();
				Db.update("delete from message where link_id =? and type=4 and sender=? and receiver=?",id,u.getInt("id"),u.getInt("id"));
				Task.dao.deleteById(id);
			}
				
		}
		renderJson(1);
	}
	public void delrefix() {
		User u = getSessionAttr("user");
		Integer[] ids = getParaValuesToInt("ids");
		String strId = "";
		if(ids!=null && ids.length>0){
			for(Integer id:ids){
				strId += id+",";
			}
			strId = strId.substring(0, strId.length()-1);
			Db.update("delete from message where link_id in ("+strId+") and type=5 and sender=? and receiver=?",u.getInt("id"),u.getInt("id"));
			Db.update("delete from task where id in ("+strId+")");
		}
		else{
			if(getParaToInt()>0) {
				int id=getParaToInt();
				Db.update("delete from message where link_id =? and type=5 and sender=? and receiver=?",id,u.getInt("id"),u.getInt("id"));
				Task.dao.deleteById(id);
			}
				
		}
		renderJson(1);
	}
	public void save() {
		int code = 1;
		User u = getSessionAttr("user");
		int reminder = getParaToInt("task.reminder_days");
		String due = getPara("task.due_date");
		String reminder_date = DateFmt.addLongDays(due, (-1)*reminder);
		String local_date = DateFmt.format2str( due,"yyyy-MM-dd HH:mm:ss","hh:mm a dd/MM/yyyy").replace("AM", "am").replace("PM", "pm");
		
		
		boolean b = getModel(Task.class).set("reminder_date", reminder_date).set("user_id", u.getInt("id")).set("creator", u.getInt("id")).save();
		int taskId = Db.queryInt("select max(id) from task");
		int type=4;
		String type_name = "Task";
		Db.update("insert into message (name,link_id,type,type_name,sender,receiver,create_time,alert_time) values (?,?,?,?,?,?,?,?)",
				"You have a task ["+getPara("task.title")+"] due at "+local_date,taskId,type,type_name,u.getInt("id"),u.getInt("id"),DateFmt.addLongDays(0),reminder_date);
		if(b)code=0;
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("code",code);
		renderJson(map);
	}
	public void detail() {
		int id = getParaToInt("id");
		Task t = Task.dao.findById(id);
		setAttr("task",t);
		render("/t/viewtask.html");
	}
	public void complete() {
		int id = getParaToInt();
		Db.update("update task set status=1 where id=?",id);
		renderJson(1);
	}
}
