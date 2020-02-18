package com.cms.front.common;

import java.util.List;

import com.cms.admin.content.Content;
import com.cms.admin.user.User;
import com.cms.front.entity.Client;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

@Before(MInterceptor.class)
public class NotificationController extends Controller {
	public void index() {
		//页数
		int pageNum = 1;
		if (getParaToInt("p") != null) {
			pageNum = getParaToInt("p");
		}
		String keyword = getPara("keyword");
		String field = getPara("field");
		if(field!=null&&!"".equals(keyword)) {
			setSessionAttr("notify_keyword",keyword);
			setSessionAttr("notify_field",field);
		}
		else {
			if (getParaToInt("p") == null) {
				removeSessionAttr("notify_field");
				removeSessionAttr("notify_keyword");
			}
		}
		
		User u = getSessionAttr("user");
		Page<Record> page = null;
		// from message m join content c on m.link_id=c.id join user u on m.sender=u.id where m.receiver=? and m.status=0
		// 当前页
		StringBuffer sql =new StringBuffer("from message m join user u on m.sender=u.id where m.receiver="+u.getInt("id"));
		if(getSessionAttr("notify_field")!=null) {
			keyword = getSessionAttr("notify_keyword");
			field = getSessionAttr("notify_field");
			if(field.equals("name")) {
				sql.append(" and (u.first_name like '%"+keyword+"%' or u.last_name like '%"+keyword+"%')");
			}
			else {
				sql.append(" and m."+field+" like '%"+keyword+"%'");
			}
		}
		sql.append(" ORDER BY m.create_time DESC");
		page = Db.paginate(pageNum, 10, "select m.id,m.name title,m.content,m.create_time,m.type_name,m.status,m.type,if(m.type=0,'announcement','notification') ntype,m.link_id,u.avatar,u.first_name,u.last_name,u.email",sql.toString());
		setAttr("contentPage", page);
		render("/t/notification.html");
	}
	public void del() {
		Integer[] ids = getParaValuesToInt("ids");
		String strId = "";
		if(ids!=null && ids.length>0){
			for(Integer id:ids){
				strId += id+",";
			}
			strId = strId.substring(0, strId.length()-1);
			Db.update("delete from message where id in ("+strId+")");
		}
		else{
			if(getParaToInt()>0)
				Db.deleteById("message", getParaToInt());
		}
		index();
	}
	public void detail() {
		int id = getParaToInt("id");
		Record r = Db.findFirst("select a.start_time,a.end_time,a.alert_time,a.class_name,a.status alert_status,"
				+ "m.name notification_title,m.content,m.status message_status,a.title alert_title,m.type,"
				+ "if(m.type=0,'announcement','notification') ntype,m.link_id,u.avatar,u.first_name,u.last_name "
				+ "from message m join alert a on m.link_id=a.id join user u on m.sender=u.id where m.id=?",id);
		setAttr("rd",r);
		render("/t/not_detail.html");
	}
}
