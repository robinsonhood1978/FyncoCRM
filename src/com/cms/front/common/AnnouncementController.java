package com.cms.front.common;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.cms.admin.content.Content;
import com.cms.admin.content.ContentAtt;
import com.cms.admin.content.ContentChannel;
import com.cms.admin.content.ContentCount;
import com.cms.admin.content.ContentKeywords;
import com.cms.admin.content.ContentView;
import com.cms.admin.user.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;

@Before(MInterceptor.class)
public class AnnouncementController extends Controller {
	private static IKAnalyzer analyzer = new IKAnalyzer(true);
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
		int id = getParaToInt("id");
		User u = getSessionAttr("user");
		Content c = Content.dao.findById(id);
		User author = User.dao.findById(c.getInt("creator"));
		List<Record> l = Db.find("select id from content_view where content_id=? and user_id=?",id,u.getInt("id"));
		if(l.size()==0) {
			Db.update("insert into content_view (content_id,user_id) values (?,?)",id,u.getInt("id"));
		}
		setAttr("creator_name", author.getStr("first_name")+" "+author.getStr("last_name"));
		setAttr("edit",u.getInt("id"));
		setAttr("author",author);
		setAttr("content", c);
		if (getParaToInt("d") == 0) {
			setAttr("direct", "/announcement");
		}else {
			setAttr("direct", "/notification");
		}
		setAttr("views",Db.queryLong("select count(id) from content_view where content_id=?",id));
		render("/t/ann-detail.html");
	}
	public void edit() {
		int id = getParaToInt();
		Content c = Content.dao.findById(id);
		setAttr("content", c);
		render("/t/write.html");
	}
	public void update() {
//		System.out.println(getModel(Content.class).update());
		int id = getParaToInt("content.id");
		int channelId = getParaToInt("content.channel_id");
		int preType = (getParaToInt("preChannelId")==21)?0:1;
		int type=(channelId==21)?0:1;
		String type_name=(type==0)?"Announcement":"Notice";
		String titles = getPara("content.title");
		titles = titles.length() <= 20? titles: titles.substring(0, 19) + "...";
		Db.update("update message set name=?, type=?, type_name=? where type=? and link_id=?","New Announcement &nbsp; <B>"+titles+"</B> &nbsp; has published",type,type_name,preType,id);
		System.out.println(id+"thsi asdsadsa");
		renderJson(getModel(Content.class).update());
	}
	public void delete() {
		final int id = getParaToInt();
		boolean succeed = Db.tx(new IAtom() {
			public boolean run() throws SQLException {
				boolean flag = false;
				try {
					flag = delOne(id);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return flag;
			}
		});
		renderJson(1);
	}
	
	private boolean delOne(int contentId) throws IOException {
		// 删除前获得对象的status状态  如果是‘已通过2’就要删除对应的lucene索引
		int status = Db.queryInt(" select status from content where id ="+contentId);
		// 删除关联表content_channel中数据
		boolean count1 = delChannel(contentId);
		// 删除关键字关联表对应的content记录
		boolean count4 = delKeyByContent(contentId);
		count4 = delView(contentId);
		count4 = delNotification(contentId);
		// 删除content记录中对应的图片
		delImg(contentId);
		// 删除对应附件表的数据和实体文件
		boolean count2 = delAtt(contentId);
		// 删除内容对应的点击量（centent_count表）
		ContentCount conCun = ContentCount.dao.findFirst(" select id from content_count where content_id = ? ", contentId);
		boolean count5 = conCun.delete();
		// 最后删除内容content表中的数据
		boolean count3 = Content.dao.deleteById(contentId);
		// 如果都正确的时候再删除对应的lucene索引
		if (count1 && count2 && count3 && count4) {
			if(status==2){
				deleteIndex(contentId);
			}
		}
		return count1 && count2 && count3 && count4;
	}
	
	/**
	 * 删除对应contentId下的notification
	 * 
	 * @param contentId
	 * @return
	 */
	private boolean delNotification(int contentId) {
		int i = Db.update("delete from message where (type=0 or type=1) and link_id=?",contentId);
		System.out.println(i+"deleleele  hwo");
		return true;
	}
	
	/**
	 * 删除对应contentId下的栏目
	 * 
	 * @param contentId
	 * @return
	 */
	private boolean delChannel(int contentId) {
		List<ContentChannel> list = ContentChannel.dao.find(
				" select id from content_channel where content_id = ? ",
				contentId);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				boolean flag = ContentChannel.dao.deleteById(list.get(i).get(
						"id"));
				if (!flag) {
					return flag;
				}
			}
		}
		return true;
	}
	
	/**
	 * 删除对应contentId下的View
	 * 
	 * @param contentId
	 * @return
	 */
	private boolean delView(int contentId) {
		List<ContentView> list = ContentView.dao.find(
				" select id from content_view where content_id = ? ",
				contentId);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				boolean flag = ContentView.dao.deleteById(list.get(i).get(
						"id"));
				if (!flag) {
					return flag;
				}
			}
		}
		return true;
	}
	
	/**
	 * 删除对应内容下的图片
	 * 
	 * @param contentId
	 */
	private void delImg(int contentId) {
		Content content = Content.dao.findById(contentId);
		if (content != null) {
			if (content.getStr("title_img") != null
					&& content.getStr("title_img").trim() != "") {
				delFile(content.getStr("title_img"));
			}
			if (content.getStr("content_img") != null
					&& content.getStr("content_img").trim() != "") {
				delFile(content.getStr("content_img"));
			}
			if (content.getStr("type_img") != null
					&& content.getStr("type_img").trim() != "") {
				delFile(content.getStr("type_img"));
			}
		}
	}
	
	/**
	 * 删除对应内容下的附件
	 * 
	 * @param contentId
	 * @return
	 */
	private boolean delAtt(int contentId) {
		String realPath = this.getRequest().getRealPath("/");
		List<ContentAtt> list = ContentAtt.dao.find(
				" select * from content_att where content_id = ? ", contentId);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				// 这里是删除数据库中的记录
				boolean flag = ContentAtt.dao.deleteById(list.get(i).get("id"));
				if (!flag) {
					return flag;
				}
				// 这里开始是 删除对应的实体文件
				delFile(list.get(i).getStr("url"));
			}
		}
		return true;
	}
	
	/**
	 * 基于项目文件命名规则 写了传入文件名称删除对应文件的函数
	 * 
	 * @param fileName
	 */
	private void delFile(String fileName) {
		String realPath = getRequest().getRealPath("/");
		File file = new File(realPath + fileName);
		if (file.exists() && file.isFile()) {
			file.delete();
		}
	}
	
	/**
	 * 根据文章ID删除在文章和关键字的关联表中的数据
	 * 
	 * @param contentId
	 * @return
	 */
	public boolean delKeyByContent(int contentId) {
		boolean flag = false;
		List<ContentKeywords> list = ContentKeywords.dao.find(
				"select id from content_keywords where content_id = ? ",
				contentId);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				flag = ContentKeywords.dao.deleteById(list.get(i).get("id"));
				if (!flag) {
					return flag;
				}
			}
		} else {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 根据传过来的contentID来删除对应lucene目录下的索引
	 * 
	 * @param contentId
	 * @throws IOException
	 */
	public void deleteIndex(int contentId) throws IOException {
		String indexPath = this.getRequest().getRealPath("/") + "/luceneIndex";
		// 设定lucene索引存放目录，当存在这个目录时候才能执行删除，不存在就什么都不做
		File indexFile = new File(indexPath);
		if (indexFile.exists()) {
			Directory dir = FSDirectory.open(indexFile);
			boolean exist = IndexReader.indexExists(dir);
			if (exist) {
				IndexWriterConfig con = new IndexWriterConfig(
						Version.LUCENE_36, analyzer);
				IndexWriter writer = new IndexWriter(dir, con);
				try {
					writer.deleteDocuments(new Term("id", String
							.valueOf(contentId)));
				} finally {
					writer.close();
				}
			}
		}
	}
}
