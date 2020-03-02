package com.cms.front.common;

import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

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
		StringBuffer sql =new StringBuffer("from message m join user u on m.sender=u.id where m.receiver="+u.getInt("id") +" and (MONTH(m.alert_time) < MONTH(CURRENT_DATE()) OR (MONTH(m.alert_time) = MONTH(CURRENT_DATE()) and DAY(m.alert_time)<=DAY(CURRENT_DATE())))");
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
		page = Db.paginate(pageNum, 10, "select m.id,(YEAR(CURRENT_DATE())-YEAR(m.alert_time)) age, m.name title,m.content,m.create_time,m.type_name,m.status,m.type,if(m.type=0,'announcement','notification') ntype,m.link_id,u.avatar,u.first_name,u.last_name,u.email",sql.toString());
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
		Record r = Db.findFirst("select DATE_ADD(a.start_time, INTERVAL 13 HOUR) start_time, DATE_ADD(a.end_time, INTERVAL 13 HOUR) end_time, DATE_ADD(a.alert_time, INTERVAL 13 HOUR) alert_time,a.class_name,a.content,a.status alert_status,"
				+ "m.name notification_title,m.status message_status,a.title alert_title,m.type,"
				+ "if(m.type=0,'announcement','notification') ntype,m.link_id,u.avatar,u.first_name,u.last_name "
				+ "from message m join alert a on m.link_id=a.id join user u on m.sender=u.id where m.id=?",id);
		setAttr("rd",r);
		render("/t/not_detail.html");
	}
	public void changeToRead() {
		int code = 0;
		String model = getPara("m");
		String id = getPara("id");
		System.out.println(id);
		if (model.equals("mutiple")) {
			for (int i : ArrayUtils.toPrimitive(extractIntegersFromText(id).toArray(new Integer[0]))) {
				Db.update("update message set status=1 where id=?",i);
			}
			code = 1;
		}else {
			code = Db.update("update message set status=1 where id=?",getParaToInt("id"));
			System.out.println("notttttdstadtsta");
		}
		System.out.println(model);
		renderJson(code);
	}
	/**
	 * Convert String to Integer Array.
	 * @param source a given String.
	 * @return Integer array.
	 */
	private static HashSet<Integer> extractIntegersFromText( final String source) {
		HashSet<Integer> results = new HashSet<Integer>();
		if(source != "" && source!=null) {
			String[] integersAsText = source.split(",");
			for ( String textValue : integersAsText ) {
				results.add(Integer.parseInt( textValue )); 
			} 
		}
		return results ; 
	}
}
