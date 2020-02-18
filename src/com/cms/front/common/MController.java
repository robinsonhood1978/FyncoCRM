package com.cms.front.common;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cms.admin.channel.Channel;
import com.cms.admin.content.ContentCount;
import com.cms.admin.trade.Trade;
import com.cms.admin.user.User;
import com.cms.front.entity.Content;
import com.cms.util.DateFmt;
import com.cms.util.ImageUtils;
import com.cms.util.Md5;
import com.cms.util.StrUtil;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.i18n.I18N;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

@Before(MInterceptor.class)
public class MController extends Controller {
	private static SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmsss");
	private static SimpleDateFormat ym = new SimpleDateFormat("yyyyMM");
	private static String uploadroot = "/upload/";
	public void jaddavatar() {
		int code=0;
		String msg = "Avatar modified successfull！";
		String avatar = getPara("avatar");
		System.out.println("avatar:"+avatar);
		String realPath = this.getRequest().getRealPath("/");
		
		String path=ym.format(new Date());
		File dir = new File(realPath+uploadroot+path);
		if(!dir.exists()){
			dir.mkdir();
		}
		String file=uploadroot+path+"/"+ sf.format(new Date())+".jpg";
		ImageUtils.basePngtoJpg(avatar,realPath+file);

		
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("code",code);
			map.put("msg",msg);
			map.put("url",file);
			renderJson(map);
	}
	public void jsaveavatar() {
		int code=0;
		String msg = "";
		User loginUser = getSessionAttr("user");
		String avatar = getPara("avatar");
		String realPath = this.getRequest().getRealPath("/");
		
		String path=ym.format(new Date());
		File dir = new File(realPath+uploadroot+path);
		if(!dir.exists()){
			dir.mkdir();
		}
		String file=uploadroot+path+"/"+ sf.format(new Date())+".jpg";
		ImageUtils.basePngtoJpg(avatar,realPath+file);
		
			boolean boo = loginUser.set("avatar", file).update();
			if(boo){
				setSessionAttr("user",loginUser);
				msg="Avatar modified successfull！";
			}
			else{
				code=1;
				msg = "Avatar modified failure，Please try again or contact the administrator.";
			}
		
			Map map = new HashMap();
			map.put("code",code);
			map.put("msg",msg);
			renderJson(map);
	}
	public void jsavelogo() {
		int code=0;
		String msg = "";
		User loginUser = getSessionAttr("user");
		String logo = getPara("logo");
		String realPath = this.getRequest().getRealPath("/");
		
		String path=ym.format(new Date());
		File dir = new File(realPath+uploadroot+path);
		if(!dir.exists()){
			dir.mkdir();
		}
		String file=uploadroot+path+"/"+ sf.format(new Date())+".jpg";
		ImageUtils.basePngtoJpg(logo,realPath+file);
		
			boolean boo = loginUser.set("logo", file).update();
			if(boo){
				setSessionAttr("user",loginUser);
				msg="Logo modified successfull！";
			}
			else{
				code=1;
				msg = "Logo modified failure，Please try again or contact the administrator.";
			}
		
			Map map = new HashMap();
			map.put("code",code);
			map.put("msg",msg);
			renderJson(map);
	}
	public void email() {
		render("/WEB-INF/m/email.html");
	}
	public void juppwd() {
		int code=0;
		String msg = "";
		User loginUser = getSessionAttr("user");
		String oldpwd = getPara("user.oldpwd");
		String pwd = getPara("user.pwd");
		System.out.println(Md5.encodePassword(oldpwd,"fynco"));
		System.out.println(loginUser.getStr("pwd"));
		if(loginUser.getStr("pwd").equals(Md5.encodePassword(oldpwd,"fynco"))){
			boolean boo = loginUser.set("pwd", Md5.encodePassword(pwd,"fynco")).update();
			if(boo){
				msg="Successful password modification！";
			}
			else{
				code=1;
				msg = "Password modification failed, please contact the administrator.";
			}
		}
		else{
			code=1;
			msg = "The original password is incorrect. Please re-enter it.";
		}
		Map map = new HashMap();
		map.put("code",code);
		map.put("msg",msg);
		renderJson(map);
	}
	public void jupinfo() {
		int code=0;
		String msg="";
		String mobile = getPara("user.mobile");
		User loginUser = getSessionAttr("user");
		List<User> list = User.dao.find("select * from user where mobile=? and id!=?",mobile,loginUser.getInt("id"));
		if(list.size()>0){
			msg=I18N.getText("mobile_exist");
		}
		else{
			boolean boo = getModel(User.class).set("id", loginUser.getInt("id")).update();
			if(boo){
				setSessionAttr("user", User.dao.findById(loginUser.getInt("id")));
				msg="Register information modified success！";
			}
			else{
				code=1;
				msg = "Information modified failure，please contact system administrator。";
			}
		}
		Map map = new HashMap();
		map.put("code",code);
		map.put("msg",msg);
		renderJson(map);
	}
	public void index() {
		User loginUser = getSessionAttr("user");
		if(loginUser==null){
			forwardAction("/l");
		}
		else{
			
			render("/WEB-INF/m/dashboard.html");
			
			
		}
	}
	public void blogs() {
		User loginUser = getSessionAttr("user");
		if(loginUser==null){
			forwardAction("/l");
		}
		else{
			Page<Content> page = null;
			// 当前页
			StringBuffer sql =new StringBuffer("from content a,channel b where a.channel_id=b.id and a.creator="+loginUser.getInt("id"));
			//页数
			int pageNum = 1;
			if (getParaToInt("pageNum") != null) {
				pageNum = getParaToInt("pageNum");
			}
			page = Content.dao.paginate(pageNum, 10, "select a.*,b.name as channel_name",sql.toString());
			setAttr("contentPage", page);
			render("/WEB-INF/m/m.html");
		}
	}
	public void write() {
		render("/WEB-INF/m/write.html");
	}
	public void view() {
		int id = getParaToInt("id");
		Content c = Content.dao.findById(id);
		setAttr("content_att", Db.find(
				" select * from content_att where content_id = ? ",id));
		setAttr("creator_name", User.dao.findById(c.getInt("creator")).getStr("nick"));
		setAttr("content", c);
		
		render("/WEB-INF/m/view.html");
	}
	public void tview() {
		int id = getParaToInt("id");
		Trade t = Trade.dao.findById(id);
		User u = User.dao.findById(t.getInt("uid"));
		setAttr("serv_fee_rate",Db.queryFirst("select value from config where code='serv_fee_rate'"));
		setAttr("user",u);
		setAttr("trade",t);
		setAttr("cyList",Db.find("select code,name from currency_code where is_show=1"));
		setAttr("payMethodList",Db.find("select ename,name from dict where code='pay_method'"));
		render("/WEB-INF/m/tview.html");
	}
	public void balance() {
		User u = getSessionAttr("user");
		setAttr("user",u);
		render("/WEB-INF/m/balance.html");
	}
	public void edit() {
		int id = getParaToInt("id");
		Content c = Content.dao.findById(id);
		setAttr("content", c);
		
		render("/WEB-INF/m/edit.html");
	}
	public void del() {
		
		boolean succeed = Db.tx(new IAtom() {
			public boolean run() throws SQLException {
				int id = getParaToInt("id");
				Db.update("delete from content_channel where content_id=?",id);
				Db.update("delete from content_count where content_id=?",id);
				Content.dao.deleteById(id);
				return true;
			}
		});
		int code=0;
		String msg = "";
		if (succeed) {
			code=1;
			msg="信息删除成功！";
		}
		else{
			msg = "信息删除失败，请重试或联系管理员。";
		}
		setAttr("code", code);
		setAttr("msg", msg);
		render("/WEB-INF/m/msg.html");
	}
	public void person() {
		//render("/WEB-INF/m/person.html");
		render("/t/person.html");
	}
	public void upinfo() {
		String mobile = getPara("user.mobile");
		String email = getPara("user.email");
		User loginUser = getSessionAttr("user");
		List<User> list = User.dao.find("select * from user where (mobile=? or email=?) and id!=?",mobile,email,loginUser.getInt("id"));
		int code=0;
		String msg = "";
		if(list.size()>0){
			msg=I18N.getText("email_exist");
		}
		else{
			boolean boo = getModel(User.class).set("id", loginUser.getInt("id")).update();
			if(boo){
				setSessionAttr("user", User.dao.findById(loginUser.getInt("id")));
				code=1;
				msg="Register information modified success！";
			}
			else{
				msg = "Information modified failure，please contact system administrator。";
			}
		}
		setAttr("code", code);
		setAttr("msg", msg);
		render("/WEB-INF/m/msg.html");
	}
	public void save_kaihu() {
//		String uid = getPara("user.uid");
//		String name = getPara("user.name");
//		String alias = getPara("user.alias");
//		String birthday = getPara("user.birthday");
//		String uid_pic = getPara("user.uid_pic");
//		String back_uid_pic = getPara("user.back_uid_pic");
//		String face_pic = getPara("user.face_pic");
//		String skype = getPara("user.skype");
//		String whatsapp = getPara("user.whatsapp");
//		String wechat = getPara("user.wechat");
//		String address = getPara("user.address");
		
		User loginUser = getSessionAttr("user");
		//int id = loginUser.getInt("id");
//		List<User> list = User.dao.find("select * from user where uid=? or and id!=?",uid,loginUser.getInt("id"));
		int code=0;
		String msg = "";
//		if(list.size()>0){
//			msg="身份证/银行卡已存在！请更正。";
//		}
//		else{
			boolean boo = getModel(User.class).set("valid_id", getParaToInt("user.valid_id")).set("id", loginUser.getInt("id")).set("kaihu_status", 1).update();
			//int boo = Db.update("update user set uid=?,name=?,alias=?,birthday=?,uid_pic=?,back_uid_pic=?,face_pic=?,skype=?,whatsapp=?,wechat=?,address=? where id=?",uid,name,alias,birthday,uid_pic,back_uid_pic,face_pic,skype,whatsapp,wechat,address,id);
			if(boo){
				setSessionAttr("user", User.dao.findById(loginUser.getInt("id")));
				code=1;
				msg="Your account information has been successfully saved.";
			}
			else{
				msg = "The account information modification failed. Please contact the administrator.";
			}
		//}
		setAttr("code", code);
		setAttr("msg", msg);
		render("/WEB-INF/m/msg.html");
	}
	public void pwd() {
		render("/WEB-INF/m/pwd.html");
	}
	public void uppwd() {
		int code=0;
		String msg = "";
		User loginUser = getSessionAttr("user");
		String oldpwd = getPara("user.oldpwd");
		String pwd = getPara("user.pwd");
		if(loginUser.getStr("pwd").equals(Md5.encodePassword(oldpwd))){
			boolean boo = loginUser.set("pwd", Md5.encodePassword(pwd)).update();
			if(boo){
				code=1;
				msg="Successful password modification！";
			}
			else{
				msg = "Password modification failed, please contact the administrator.";
			}
		}
		else{
			msg = "The original password is incorrect. Please re-enter it.";
		}
		
		setAttr("code", code);
		setAttr("msg", msg);
		render("/WEB-INF/m/msg.html");
	}
	public void publish() {
		int code=0;
		String msg = "";
		final User loginUser = getSessionAttr("user");
		boolean succeed = Db.tx(new IAtom() {
			public boolean run() throws SQLException {
				getModel(Content.class)
				.set("release_date",DateFmt.addLongDays(0))
				.set("creator", loginUser.getInt("id"))
				.set("author", loginUser.getStr("first_name")+" "+loginUser.getStr("last_name")).save();
				//拿到刚刚添加文字的ID
				int contentId = Db.queryInt("select max(id) from content");
				int channelId = getParaToInt("content.channel_id");
				int type=(channelId==21)?0:1;
				String type_name=(type==0)?"Announcement":"Notice";
				Db.save("content_channel", new Record()
				.set("content_id", contentId)
				.set("channel_id",channelId));
				//增添文章的点击量
				new ContentCount().set("content_id", contentId).save();
				//增添系统通知
				List<Record> ul = Db.find("select id from user where status=1");
				String titles = getPara("content.title");
				titles = titles.length() <= 20? titles: titles.substring(0, 19) + "...";
				for(Record r:ul) {
					Db.update("insert into message (name,link_id,type,type_name,sender,receiver,create_time,alert_time) values (?,?,?,?,?,?,?,?)",
							"New Announcement &nbsp; <B>"+titles+"</B> &nbsp; has published",contentId,type,type_name,loginUser.getInt("id"),r.getInt("id"),DateFmt.addLongDays(0),DateFmt.addLongDays(0));
				}
				return true;
			}
		});
		if(succeed){
			code=1;
			msg="Publish success！";
		}
		else{
			msg = "Publish failure，Please try again or contact the administrator.";
		}
		
		//setAttr("code", code);
		//setAttr("msg", msg);
		//render("/t/msg.html");
		renderJson(code);
	}
	public void update_blog() {
		int code=0;
		String msg = "";
		boolean succeed = Db.tx(new IAtom() {
			public boolean run() throws SQLException {
				int content_id=getParaToInt("content.id");
				int channel_id=getParaToInt("content.channel_id");
				Db.update("update content_channel set channel_id=? where content_id=?",channel_id,content_id);
				getModel(Content.class).update();
				return true;
			}
		});
		if(succeed){
			code=1;
			msg="博文编辑成功！";
		}
		else{
			msg = "博文编辑失败，请重试或联系管理员。";
		}
		
		setAttr("code", code);
		setAttr("msg", msg);
		render("/WEB-INF/m/msg.html");
	}
	public void save_avatar() {
		int code=0;
		String msg = "";
		User loginUser = getSessionAttr("user");
		String avatar = getPara("avatar");
		String realPath = this.getRequest().getRealPath("/");
		
		String path=ym.format(new Date());
		File dir = new File(realPath+uploadroot+path);
		if(!dir.exists()){
			dir.mkdir();
		}
		String file=uploadroot+path+"/"+ sf.format(new Date())+".jpg";
		ImageUtils.basePngtoJpg(avatar,realPath+file);
		
			boolean boo = loginUser.set("avatar", file).update();
			if(boo){
				code=1;
				msg="Avatar modified successfull！";
			}
			else{
				msg = "Avatar modified failure，Please try again or contact the administrator.";
			}
		
		
		setAttr("code", code);
		setAttr("msg", msg);
		render("/WEB-INF/m/msg.html");
	}
	public void file() {
		String savefilename="";
		String filedataFileName ="";
		StringBuffer files=new StringBuffer();
		UploadFile upfile = this.getFile();
		if(upfile!=null){
			String realPath = this.getRequest().getRealPath("/");
			
			String path=ym.format(new Date());
			File dir = new File(realPath+uploadroot+path);
			if(!dir.exists()){
				dir.mkdir();
			}
			File file = upfile.getFile();
			filedataFileName = upfile.getOriginalFileName();
			savefilename = uploadroot+path+"/"+ sf.format(new Date())+ filedataFileName.substring(filedataFileName.lastIndexOf("."));
			if(file != null){
				upfile.getFile().renameTo(new File(realPath + savefilename));
				files.append(realPath+savefilename);
			}
			
		}
		Map map = new HashMap();
		map.put("status","ok");
		map.put("url",savefilename);
		renderJson(map);

	}
	public void avatar() {
		render("/WEB-INF/m/avatar.html");
	}
	public void kaihu() {
		render("/WEB-INF/m/kaihu.html");
	}
}
