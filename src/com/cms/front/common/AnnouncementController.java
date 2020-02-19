package com.cms.front.common;

import java.util.List;

import com.cms.admin.content.Content;
import com.cms.admin.user.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

@Before(MInterceptor.class)
public class AnnouncementController extends Controller {
	public void index() {
		User u = getSessionAttr("user");
		List<Record> as = Db.find("select c.id,c.priority,c.author,c.txt,c.title,c.creator,c.release_date,ch.title_img,ch.name channel_name,ifnull(cv.viewer_num,0) viewer_num,v.avatars,v.usernames,ifnull((v.users_num-3),0) as usernum from content c join channel ch on c.channel_id=ch.id left join (select count(id) viewer_num,content_id from content_view group by content_id) cv on c.id=cv.content_id left join (select GROUP_CONCAT(u.avatar) avatars,GROUP_CONCAT(CONCAT_WS(' ',u.first_name,u.last_name)) usernames,ctv.content_id,count(u.id) users_num from user u join content_view ctv on u.id=ctv.user_id group by ctv.content_id) v on c.id=v.content_id ORDER BY c.priority DESC, c.release_date DESC");
		setAttr("ans",as);
		setAttr("publisher", u.getInt("id"));
		render("/t/announcement.html");
	}
	public void add() {
		render("/t/addnot.html");
	}
	public void detail() {
		int id = getParaToInt();
		User u = getSessionAttr("user");
		Content c = Content.dao.findById(id);
		User author = User.dao.findById(c.getInt("creator"));
		List<Record> l = Db.find("select id from content_view where content_id=? and user_id=?",id,u.getInt("id"));
		if(l.size()==0) {
			Db.update("insert into content_view (content_id,user_id) values (?,?)",id,u.getInt("id"));
		}
		setAttr("creator_name", author.getStr("first_name")+" "+author.getStr("last_name"));
		setAttr("author",author);
		setAttr("content", c);
		setAttr("views",Db.queryLong("select count(id) from content_view where content_id=?",id));
		render("/t/ann-detail.html");
	}
}
