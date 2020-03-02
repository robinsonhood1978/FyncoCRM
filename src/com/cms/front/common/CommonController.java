package com.cms.front.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import com.cms.admin.channel.Channel;
import com.cms.admin.content.Content;
import com.cms.admin.content.ContentCount;
import com.cms.admin.user.User;
import com.cms.front.entity.Application;
import com.cms.front.entity.Email;
import com.cms.util.DateFmt;
import com.cms.util.FileUtil;
import com.cms.util.Md5;
import com.cms.util.Paginable;
import com.cms.util.SendMail;
import com.cms.util.StringUtils;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.i18n.I18N;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cms.util.SimplePage;
import com.cms.util.StrUtil;


public class CommonController extends Controller {
	private static SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmsssSSS");
	private static SimpleDateFormat ym = new SimpleDateFormat("yyyyMM");
	private static String uploadroot = "/upload/";
	public void jdeldoc() {
		String doc_url = getPara("url");
		String realPath = this.getRequest().getRealPath("/");
		String dest = realPath+doc_url;
		FileUtil.deleteAll(dest);
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("code",0);
		renderJson(map);
	}
	public void file() {
		String savefilename="";
		String filedataFileName ="";
		StringBuffer files=new StringBuffer();
		List<UploadFile> upfilelist = this.getFiles();
		if(upfilelist!=null&&upfilelist.isEmpty()==false){
			String realPath = this.getRequest().getRealPath("/");
			String path=ym.format(new Date());
			File dir = new File(realPath+uploadroot+path);
			if(!dir.exists()){
				dir.mkdir();
			}
			int fsize=upfilelist.size();
			int i=1;
			for(UploadFile upfile:upfilelist){
				if(upfile!=null){
					
					File file = upfile.getFile();
					filedataFileName = upfile.getOriginalFileName();
					savefilename = uploadroot+path+"/"+ sf.format(new Date())+StrUtil.randomNum(3)+ filedataFileName.substring(filedataFileName.lastIndexOf("."));
					if(file != null){
						upfile.getFile().renameTo(new File(realPath + savefilename));
						files.append(savefilename);
						if(i<fsize)
							files.append(",");
					}
				}
				i++;
			}
		}
		
		String[] strArray=files.toString().split(",");
		Map map = new HashMap();
		map.put("code",1);
		map.put("url",strArray[0]);
		renderJson(map);
	}
	@Before(MInterceptor.class)
	public void email_content() {
		String content = getPara("email_content");
		setSessionAttr("email_content", StrUtil.replaceBlank(content));
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("code",0);
		renderJson(map);
	}
	@Before(MInterceptor.class)
	public void sendmail() {
		int code = 1;
		User u = getSessionAttr("user");
		String to = getPara("email.receiver");
		String from = getPara("email.sender");
		String passwd = u.getStr("sendmail_password");
		String host = u.getStr("mail_host");
		String subject = getPara("email.subject");
		String content = getPara("email.content");
		String atts = getPara("atts");
		String jatts = getPara("jatts");
		//System.out.println(atts);
		//System.out.println(jatts);
		String[] att_arr = atts.split(",");
		String realPath = this.getRequest().getRealPath("/");
		int i=0;
		for(String att:att_arr) {
			att_arr[i]=realPath+att;
			i++;
		}
		Record r = Db.findFirst("select * from mailserver where name=?",host);
		//JSONObject jr = JSONObject.fromObject(r.getColumns(););
		
		//System.out.println(jr.toString());
		SendMail.send(r.getColumns(),from, passwd, to, subject, content,att_arr);
		boolean b = getModel(Email.class).set("status", 1).set("attachment", jatts)
				.set("creator", u.getInt("id")).save();
		if(b)code=0;
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("code",code);
		renderJson(map);
	}
	@Before(MInterceptor.class)
	public void amail() {
		String to = "";
		ArrayList<HashMap<String,String>> attachments = new ArrayList<HashMap<String,String>>();
		if(getPara("t")!=null) {
			to = getPara("t");
		}
		String atts = "";
		if(getPara("type")!=null) {
			String type=getPara("type");
			if(type.equals("Send2ClientApplicationForm")) {
				int appid = getParaToInt("appid");
				String pdf = Application.dao.findById(appid).getStr("pdf");
				if(pdf!=null) {
					atts = pdf;
					HashMap<String,String> map = new HashMap<String,String>();
					map.put("att_name", "Application Form");
					map.put("att_file", pdf);
					attachments.add(map);
				}
			}
			if(type.equals("Send2Bank")) {
				int appid = getParaToInt("appid");
				String documents = Application.dao.findById(appid).getStr("documents");
				JSONObject obj = JSONObject.fromObject(documents);
				Iterator iterator = obj.keys();
		        int i=0;
		        
		        while(iterator.hasNext()){
		        		
		                String key = (String) iterator.next();
		                Object value = obj.get(key);
		                JSONObject valueObj = JSONObject.fromObject(value);
		                if (!"".equals(valueObj.get("url").toString())) {
		                	i++;
		                	if(i>1)atts += ",";
		                	atts += valueObj.get("url").toString();
		                	HashMap<String,String> map = new HashMap<String,String>();
							map.put("att_name", valueObj.get("name").toString());
							map.put("att_file", valueObj.get("url").toString());
							attachments.add(map);
		                	System.out.println(i+":"+valueObj.get("url").toString());
		                }
		        }
		        
			}
		}
		//System.out.println("to:"+to);
		setAttr("to",to);
		setAttr("atts",attachments);
		JSONArray arr = JSONArray.fromObject(attachments);
		//System.out.println(arr.toString());
		setAttr("atts_str",atts);
		setAttr("atts_jstr",arr.toString());
		render("/t/amail.html");
	}
	public void msg() {
		render("/t/msg.html");
	}
	
	@Before(MInterceptor.class)
	public void write() {
		render("/t/write.html");
	}
	@Before(MInterceptor.class)
	public void setemail() {
		render("/t/setemail.html");
	}
	@Before(MInterceptor.class)
	public void upavatar() {
		render("/t/upavatar.html");
	}
	@Before(MInterceptor.class)
	public void add_avatar() {
		String avatar_url = getPara("url");
		setAttr("avatar_url",avatar_url);
		render("/t/add_avatar.html");
	}
	@Before(MInterceptor.class)
	public void add_avatar2() {
		String avatar_url = getPara("url");
		setAttr("avatar_url",avatar_url);
		render("/t/add_avatar2.html");
	}
	@Before(MInterceptor.class)
	public void add_avatar3() {
		String avatar_url = getPara("url");
		setAttr("avatar_url",avatar_url);
		render("/t/add_avatar3.html");
	}
	@Before(MInterceptor.class)
	public void avatar() {
		render("/t/avatar.html");
	}
	@Before(MInterceptor.class)
	public void logo() {
		render("/t/logo.html");
	}
	@Before(MInterceptor.class)
	public void uplogo() {
		render("/t/uplogo.html");
	}
	@Before(MInterceptor.class)
	public void person() {
		//render("/WEB-INF/m/person.html");
		render("/t/person.html");
	}
	@Before(MInterceptor.class)
	public void pwd() {
		render("/t/pwd.html");
	}
	public void jsetemailbyverifycode() {
		int code=0;
		String msg="";
		User u = getSessionAttr("user");
		String email = getSessionAttr("email");
		String verifycode = getPara("verifycode");
		String type = getPara("type");
		System.out.println("type:"+type);
		List<Record> l = Db.find("select id from user where id=? and access_code=? and ac_status=1",u.getInt("id"),verifycode);
		if(l.size()==0){
			code=1;
		}
		else {
			if(getSessionAttr("email")==null) {
				System.out.println("email is null");
			}
			else {
				int i = Db.update("update user set email=?,ac_status=0 where id=? and access_code=? and ac_status=1",email,u.getInt("id"),verifycode);
				System.out.println("influenced rows:"+i);
				setSessionAttr("user", User.dao.findById(u.getInt("id")));
			}
		}
		Map map = new HashMap();
		map.put("code",code);
		map.put("msg",msg);
		renderJson(map);
	}
	public void jverifynewemail() {
		int code=0;
		String msg="";
		String email = getPara("email");
		User u = getSessionAttr("user");
		String verifycode = StrUtil.getRandom(8);
		System.out.println(verifycode);
		Db.update("update user set access_code=?,ac_status=1 where id=?",verifycode,u.getInt("id"));
		if(getPara("email")==null) {
			System.out.println("email is null");
			email = u.getStr("email");
			System.out.println(email);
		}
		else {
			setSessionAttr("email",email);	
		}
		SendMail.send(email, verifycode,0);
		Map map = new HashMap();
		map.put("code",code);
		map.put("msg",msg);
		renderJson(map);
	}
	public void jverifycode() {
		int code=0;
		String msg="Valid verify code^-^";
		String email = getSessionAttr("email");
		System.out.println("email:"+email);
		String verifycode = getPara("verifycode");
		List<User> list = User.dao.find("select u.* from user u where email=? and access_code=? and ac_status=1",email,verifycode);
		if(list.size()==1){
		}
		else {
			code=1;
			msg = "The access code is incorrect or expired. Please correct it and try again.^-^";
		}
		
		Map map = new HashMap();
		map.put("code",code);
		map.put("msg",msg);
		renderJson(map);
	}
	public void verifycode() {
		render("/t/verifycode.html");
	}
	public void forgotpwd() {
		render("/t/forgotpwd.html");
	}
	public void jverifyemail() {
		int code=0;
		String msg="Valid Email^-^";
		String email = getPara("email");
		List<User> list = null;
		
		list = User.dao.find("select * from user where email=?",email);
		if(list.size()==0){
			code=1;
			msg = "Invalid Email^-^";
		}
		else {
			String verifycode = StrUtil.getRandom(8);
			System.out.println(verifycode);
			setSessionAttr("email",email);
			Db.update("update user set access_code=?,ac_status=1 where email=?",verifycode,email);
			SendMail.send(email, verifycode,0);
		}
		
		Map map = new HashMap();
		map.put("code",code);
		map.put("msg",msg);
		renderJson(map);
	}
	public void noemailcheck() {
		int code=0;
		String msg="Valid Email^-^";
		String email = getPara("email");
		List<User> list = null;
		
		list = User.dao.find("select * from user where email=?",email);
		if(list.size()>0){
			code=1;
			msg = "Invalid Email^-^";
		}
		Map map = new HashMap();
		map.put("code",code);
		map.put("msg",msg);
		renderJson(map);
	}
	public void emailcheck() {
		int code=0;
		String msg="Valid Email^-^";
		String email = getPara("email");
		List<User> list = null;
		
		list = User.dao.find("select * from user where email=?",email);
		if(list.size()==0){
			code=1;
			msg = "Invalid Email^-^";
		}
		else {
			String pwd = Db.queryStr("select pwd from user where email=?",email);
			if(pwd==null || pwd.equals("")) {
				code=2;
			}
		}
		
		Map map = new HashMap();
		map.put("code",code);
		map.put("msg",msg);
		renderJson(map);
	}
	public void setpwd() {
		String email = getSessionAttr("email");
		if(email!=null) {
			render("/t/setpwd.html");
		}
		else {
			setAttr("code", 0);
			setAttr("msg", "Unauthorized access");
			render("/t/msg.html");
		}
	}
	public void juselogo() {
		int code=0;
		int use = getParaToInt("use");
		System.out.println("use:"+use);
		User u = getSessionAttr("user");
		int i = Db.update("update user set uselogo=? where id=?",use,u.getInt("id"));
		if(i>0) {
			u.set("uselogo", use);
			setSessionAttr("user",u);
			code=1;
		}
		Map map = new HashMap();
		map.put("code",code);
		renderJson(map);
	}
	public void jsetpwd() {
		int code=0;
		String email = getSessionAttr("email");
		String pwd = getPara("pwd");
		pwd = Md5.encodePassword(pwd, "fynco");
		if(email!=null) {
			Db.update("update user set pwd=?,ac_status=0 where email=?",pwd,email);
			removeSessionAttr("email");
		}
		Map map = new HashMap();
		map.put("code",code);
		renderJson(map);
	}
	public void jlogin(){
		int code=0;
		String msg="";
		String email = getPara("email");
		String pwd = getPara("pwd");
		String logintype = getPara("logintype");
		System.out.println(email+":"+pwd+":"+logintype);
		List<User> list = null;
		if(logintype.equals("access_code")) {
			list = User.dao.find("select u.* from user u where email=? and access_code=? and ac_status=1",email,pwd);
			if(list.size()==1){
				setSessionAttr("email",email);
				code=2;
			}
			else {
				code=1;
				msg = "The access code is incorrect or expired. Please correct it and try again.^-^";
			}
		}
		else {
			list = User.dao.find("select u.* from user u where email=? and pwd=?",email,Md5.encodePassword(pwd,"fynco"));
			if(list.size()==1){
				// 放数据至session
				setSessionAttr("user", list.get(0));
			}
			else {
				code=1;
				msg = "The login information is incorrect. Please correct it and try again.^-^";
			}
		}
		Map map = new HashMap();
		map.put("code",code);
		map.put("msg",msg);
		renderJson(map);
	}
	public void vst() {
		/*p = "ua=" + vlstatUA + "&ip=" + vlstatIPAddress+ "&cid=" + cid+ "&cname=" + cname + "&refurl="
	            + vlstatREFURL + "&url=" + vlstatURL + "&screenX=" + vlstatScreenX + "&screenY=" + vlstatScreenY
	            + "&os=" + vlstatOS + "&brower=" + vlstatBrower + "&browerLang=" + vlstatBrowerLanguage
	            + "&title=" + vlstatPageTitle ;
	    ua=Mozilla%2F5.0%20(Macintosh%3B%20Intel%20Mac%20OS%20X%2010_14_5)%20AppleWebKit%2F537.36%20(KHTML%2C%20like%20Gecko)%20Chrome%2F77.0.3865.90%20Safari%2F537.36&ip=27.252.63.211&cid=NZ&cname=NEW%20ZEALAND&refurl=&url=http%3A%2F%2Flocalhost%3A81%2F&screenX=1440&screenY=900&os=MacIntel&brower=Chrome&browerLang=
	            */
		String ua = getPara("ua");
		String ip = getPara("ip");
		String cid = getPara("cid");
		String cname = getPara("cname");
		String refurl = getPara("refurl");
		String url = getPara("url");
		String screenX = getPara("screenX");
		String screenY = getPara("screenY");
		String os = getPara("os");
		String brower = getPara("brower");
		Db.update("insert into visits (ua,ip,cid,cname,refurl,url,screenX,screenY,os,brower) values(?,?,?,?,?,?,?,?,?,?)",
				ua,ip,cid,cname,refurl,url,screenX,screenY,os,brower);
		renderNull();
	} 
	public void login(){
		boolean login=false;
		/*String cpatcha = getPara("vcode");
		String kaptcha = getSessionAttr(Constants.KAPTCHA_SESSION_KEY);
		if(!cpatcha.equals(kaptcha)){
			System.out.println(kaptcha+"====+++++++"+cpatcha);
			setAttr("resultMsg", "验证码错误");
			forwardAction("/index");
		}
		else{*/
			String user = getPara("user");
			//登录时候防止sql注入 替换字符
			if(user!=null && !"".equals(user)){
				user = user.replaceAll("'", "‘");
				user = user.replaceAll(";", "；");
			}
			String pwd = getPara("pwd");
			List<User> list = User.dao.find("select u.* from user u where (nick=? or mobile=? or email=?) and pwd=?",user,user,user,Md5.encodePassword(pwd));
			if(list.size()==1){
				// 放数据至session
				setSessionAttr("user", list.get(0));
				login=true;
				/*// 取数据于session
				User loginUser = getSessionAttr("user");
				System.out.println(loginUser.getInt("role_id"));
				// 删除session中的属性
				removeSessionAttr("loginUser");
				// 得到HttpSession
				HttpSession session = getSession();*/
			}
			//String vcode = (getPara("vcode")==null)?"":getPara("vcode").toUpperCase();
			//boolean vcodeOk = CaptchaRender.validate(this, vcode, RANDOM_CODE_KEY);
			if (login) {
				String furl = (getSessionAttr("furl")==null)?"/m":getSessionAttr("furl").toString();
				redirect(furl);
			}else{
				setAttr("code", 0);
				setAttr("msg", "The login information is incorrect. Please correct it and try again.^-^");
				render("/WEB-INF/f/c/msg.html");
			}
		//}
	}
	
	public void logout(){
		// 删除session中的属性
		removeSessionAttr("user");
		HttpSession session = this.getRequest().getSession(false);
		if (session != null)
			session.invalidate();
		//String furl = (getSessionAttr("furl")==null)?"/":getSessionAttr("furl").toString();
		//redirect(furl);
		forwardAction("/");
	}
	@Before(MInterceptor.class)
	public void index() {
		//URL参数样式 channelID_pageNo-contentID-contentPageNo
		String channelPara = getPara(0);
		String channelPath = null;
		String pageNo = null;
			
		//channelID_pageNo处理
		if(channelPara != null){
			if(channelPara.indexOf("_") == -1){
				channelPath = channelPara;
			}
			else{
				channelPath = channelPara.substring(0,channelPara.indexOf('_'));
				pageNo = channelPara.substring(channelPara.indexOf('_')+1,channelPara.length());
			}
		}
		System.out.println("channelPara:"+channelPara);
		System.out.println("channelPath:"+channelPath);
		String contentId = getPara(1);
		String contentPageNo=getPara(2);
		//处理翻页数据
		if(contentPageNo == null){
			contentPageNo = "1";
		}else {
			if(contentPageNo.indexOf("_") == -1){
			}
			else{
				contentPageNo = contentPageNo.split("_")[1] == null? "1":contentPageNo.split("_")[1];
			}
		} 
		
		int cpNo = Integer.parseInt(contentPageNo);
		//非首页
		if(channelPath!=null && !channelPath.equals("index")){
			//取得栏目对象
			Channel channel = Channel.dao.findFirst("select * from channel where path=?",channelPath);
			//System.out.println("channelPath:"+channelPath);
			setAttr("channel",channel);
			if(pageNo != null){
				setAttr("pageNo",pageNo);
			}
			/*if(channelPath.equals("dckf") && getPara("contentId") !=null){
				setAttr("contentId",getPara("contentId"));
				render("/WEB-INF/t/content/land_detail.html");
				return;
			}*/
			//栏目页
			if(contentId==null){
				//取得栏目属性中的栏目模板地址
				String tplChannel = channel.getStr("tpl_channel");
				if(!"".equals(tplChannel) && tplChannel!=null){
					//处理访问路径所带的参数  方便前台展示
					if(getParaMap().size() > 0) {
						Iterator<String> it = getParaMap().keySet().iterator();
						for(int i=0;i<getParaMap().size();i++){
							String paraName = it.next();
							setAttr(paraName,getParaMap().get(paraName)[0]); 
						}
					}

					render("/WEB-INF/f"+tplChannel);
				}
				else{
					renderText("未设定栏目模板");
				}
			}
			//内容详细页
			else{
					//点击文章内容时候要增加点击量
					if(contentId != null){
						contentClickUp(Integer.valueOf(contentId));
					}
					//取得内容对象
					Content content = Content.dao.findById(contentId);
					setAttr("content",content);
					//取得作者信息
					User owner = User.dao.findById(content.getInt("creator"));
					setAttr("creator_name", owner.getStr("nick"));
					setAttr("owner",owner);
					//取得评论列表
					List<Record> comments = Db.find("select a.id,a.uid,a.content_id,a.txt,a.release_date,b.avatar,b.nick from comment a,user b " +
							"where a.uid=b.id and a.content_id=?",contentId);
					setAttr("comments",comments);
					
					
					setAttr("channelPath",channelPath);
					setAttr("channelName", channel.getStr("name"));
					
					procPreorNextContent(channelPath,contentId);
					
					//获取文章内容
					String txt = content.getStr("txt");
					Paginable pagination = new SimplePage(cpNo, 1, StringUtils.countMatches(txt, "<p>[NextPage]") + 1);
					setAttr("pagination",pagination);
					txt = StringUtils.getTxtByNo(txt, cpNo);
					setAttr("txt",txt);

				//取得栏目属性中的内容模板地址  
				//这段代码公用     查找模板转向模板页的  不能写在分支语句里
				String tplContent = channel.getStr("tpl_content");
				if(!"".equals(tplContent) && tplContent!=null){
					System.out.println(tplContent);
					render("/WEB-INF/f"+tplContent);
				}
				else{
					//待内容详细页出来后，此处需要修改，未设定内容模板则显示默认模板
					renderText("未设定内容模板");
				}
			}
		}
		//首页
		else
		{   
			//render("/WEB-INF/f/c/i.html");
			render("/t/index.html");
		}
		
	}
	public void owner() {
		User owner = User.dao.findById(getPara());
		setAttr("owner",owner);
		setAttr("articles",
				Db.find("select a.id,b.path,a.title,a.release_date from content a,channel b where a.channel_id=b.id and a.creator=? order by a.id desc",
						owner.getInt("id")));
		render("/WEB-INF/f/c/owner.html");
	}
	public void kaihu() {
		render("/WEB-INF/f/c/kaihu.html");
	}
	public void r() {
		render("/WEB-INF/f/c/r.html");
	}
	public void terms() {
		render("/WEB-INF/f/c/terms.html");
	}
	@Before(GlobalInterceptor.class)
	public void l() {
		//render("/WEB-INF/f/c/l.html");
		render("/t/login.html");
	}
	public void rs() throws IOException {
		String nick = getPara("user.nick");
		String mobile = getPara("user.mobile");
		//String qq = getPara("user.qq");
		String email = getPara("user.email");
		String pwd = getPara("user.pwd");
		List<User> list = User.dao.find("select * from user where nick=? or mobile=? or email=?",nick,mobile,email);
		if(list.size()>0){
			setAttr("code", 0);
			setAttr("msg", I18N.getText("email_exist"));
			render("/WEB-INF/f/c/msg.html");
		}
		else{
			getModel(User.class).set("pwd", Md5.encodePassword(pwd)).save();
			/*
			 * String realPath = this.getRequest().getRealPath("/"); //加载邮件html模板 String
			 * fileName = realPath+"/WEB-INF/f/c/verify_email.html"; String content =
			 * FileUtil.readfile(fileName); //填充html模板中的五个参数 String htmlText =
			 * MessageFormat.format(content, "Robin","12121");
			 * //System.out.println(content); String to = "robinsonhood1978@gmail.com";
			 * String subject = "Hi Robin,"+"Verify Account"; Mail.sendmail(to,subject,
			 * htmlText);
			 */
			setAttr("code", 1);
			setAttr("msg", I18N.getText("signup_success"));
			render("/WEB-INF/f/c/msg.html");
		}
	}
	public void select() {
		render("/t/select.html");
	}
	public void email_temp() {
		render("/WEB-INF/f/c/verify_email.html");
	}
	@Before(MInterceptor.class)
	public void comment() {
		int code=0;
		String msg="";
		int uid = getParaToInt("uid");
		String cid = getPara("cid");
		String txt = getPara("txt");
		List<User> list = null;
		User loginUser = getSessionAttr("user");
		if(uid!=loginUser.getInt("id")){
			msg = "非法访问";
		}
		else{
			int count = Db.update("insert into comment (content_id,uid,txt,release_date) values (?,?,?,?)",
					cid,uid,txt,DateFmt.addLongDays(0));
			if(count>0){
				code=1;
				msg = "评论成功。^-^";
			}
			else{
				msg = "未知错误，请联系系统管理员。^-^";
			}
		}
		Map map = new HashMap();
		  map.put("code",code);
		  map.put("msg",msg);
		  renderJson(map);
	}
	public void uid() {
		int code=0;
		String msg="";
		String uid = getPara("uid");
		String validId = getPara("validId");
		List<User> list = null;
		User loginUser = getSessionAttr("user");
		//int id = loginUser.getInt("id");
		
		if(loginUser==null){
			list = User.dao.find("select * from user where valid_id=? and uid=?",validId,uid);
		}
		else{
//			System.out.println("id:"+id);
//			System.out.println("valid_id:"+validId);
//			System.out.println("uid:"+uid);
			list = User.dao.find("select * from user where valid_id=? and uid=? and id!=?",validId,uid,loginUser.getInt("id"));
		}
		if(list.size()>0){
			code=1;
			msg = "The same ID number already exists^-^";
			//System.out.println("size=1");
		}
		else {
			//System.out.println("size=0");
		}
		Map map = new HashMap();
		map.put("code",code);
		map.put("msg",msg);
		renderJson(map);
	}
	public void card() {
		int code=0;
		String msg="";
		String card = getPara("card");
		List<User> list = null;
		User loginUser = getSessionAttr("user");
		if(loginUser==null){
			list = User.dao.find("select * from user where card=?",card);
		}
		else{
			list = User.dao.find("select * from user where card=? and id!=?",card,loginUser.getInt("id"));
		}
		if(list.size()>0){
			code=1;
			msg = "已存在相同的银行卡号^-^";
		}
		Map map = new HashMap();
		map.put("code",code);
		map.put("msg",msg);
		renderJson(map);
	}
	public void mobile() {
		int code=0;
		String msg="";
		String mobile = getPara("mobile");
		List<User> list = null;
		User loginUser = getSessionAttr("user");
		if(loginUser==null){
			list = User.dao.find("select * from user where mobile=?",mobile);
		}
		else{
			list = User.dao.find("select * from user where mobile=? and id!=?",mobile,loginUser.getInt("id"));
		}
		if(list.size()>0){
			code=1;
			msg = "已存在相同的手机号^-^";
		}
		Map map = new HashMap();
		map.put("code",code);
		map.put("msg",msg);
		renderJson(map);
	}
	public void skype() {
		int code=0;
		String msg="";
		String skype = getPara("skype");
		List<User> list = null;
		User loginUser = getSessionAttr("user");
		if(loginUser==null){
			list = User.dao.find("select * from user where skype=?",skype);
		}
		else{
			list = User.dao.find("select * from user where skype=? and id!=?",skype,loginUser.getInt("id"));
		}
		if(list.size()>0){
			code=2;
			msg = "The same Skype number already exists^-^";
		}
		Map map = new HashMap();
		map.put("code",code);
		map.put("msg",msg);
		renderJson(map);
	}
	public void whatsapp() {
		int code=0;
		String msg="";
		String whatsapp = getPara("whatsapp");
		List<User> list = null;
		User loginUser = getSessionAttr("user");
		if(loginUser==null){
			list = User.dao.find("select * from user where whatsapp=?",whatsapp);
		}
		else{
			list = User.dao.find("select * from user where whatsapp=? and id!=?",whatsapp,loginUser.getInt("id"));
		}
		if(list.size()>0){
			code=2;
			msg = "The same whatsapp number already exists^-^";
		}
		Map map = new HashMap();
		map.put("code",code);
		map.put("msg",msg);
		renderJson(map);
	}
	public void wechat() {
		int code=0;
		String msg="";
		String wechat = getPara("wechat");
		List<User> list = null;
		User loginUser = getSessionAttr("user");
		if(loginUser==null){
			list = User.dao.find("select * from user where wechat=?",wechat);
		}
		else{
			list = User.dao.find("select * from user where wechat=? and id!=?",wechat,loginUser.getInt("id"));
		}
		if(list.size()>0){
			code=2;
			msg = "The same wechat number already exists^-^";
		}
		Map map = new HashMap();
		map.put("code",code);
		map.put("msg",msg);
		renderJson(map);
	}
	public void email_check() {
		int code=0;
		String msg="";
		String email = getPara("email");
		List<User> list = null;
		User loginUser = getSessionAttr("user");
		if(loginUser==null){
			list = User.dao.find("select * from user where email=?",email);
		}
		else{
			list = User.dao.find("select * from user where email=? and id!=?",email,loginUser.getInt("id"));
		}
		if(list.size()>0){
			code=3;
			msg = "已存在相同的Email^-^";
		}
		Map map = new HashMap();
		map.put("code",code);
		map.put("msg",msg);
		renderJson(map);
	}
	
	public void nick() {
		int code=0;
		String msg="";
		String nick = getPara("nick");
		List<User> list = User.dao.find("select * from user where nick=?",nick);
		if(list.size()>0){
			code=4;
			msg = "已存在相同的昵称^-^";
		}
		Map map = new HashMap();
		map.put("code",code);
		map.put("msg",msg);
		renderJson(map);
	}
	//处理上一篇下一篇的文章
	private void procPreorNextContent(String channelPath,String contentId){
		//取出上一篇 和下一篇文章
		List<Content> content_pre = Content.dao.find("select * from content where id < "+contentId+"" +
													" and channel_id in (select id from channel where " +
													" path = '"+channelPath+"')" +
													" order by id");
		setAttr("content_pre",(content_pre.size() == 0 ? null :content_pre.get(content_pre.size()-1)));
		
		List<Content> content_next = Content.dao.find("select * from content where id > "+contentId+"" +
													" and channel_id in (select id from channel where " +
													" path = '"+channelPath+"')" +
													" order by id");
		setAttr("content_next",(content_next.size()== 0 ? null: content_next.get(0)));
	}
	
	public void c() {
		render("/WEB-INF/t/c.html");
	}
	public void n() {
		render("/WEB-INF/t/n.html");
	}
	
	
	/**
	 * 下载文件
	 * @param filePath --文件完整路径
	 * @param response --HttpServletResponse对象
	 */
	private static void downloadFile(
			String filePath, 
			HttpServletResponse response) {

		String fileName = ""; //文件名，输出到用户的下载对话框
		//从文件完整路径中提取文件名，并进行编码转换，防止不能正确显示中文名
		try {
			if(filePath.lastIndexOf("/") > 0) {
				fileName = new String(filePath.substring(filePath.lastIndexOf("/")+1, filePath.length()).getBytes("UTF-8"), "ISO8859-1");
			}else if(filePath.lastIndexOf("\\") > 0) {
				fileName = new String(filePath.substring(filePath.lastIndexOf("\\")+1, filePath.length()).getBytes("UTF-8"), "ISO8859-1");
			}

		}catch(Exception e) {}
		//打开指定文件的流信息
		FileInputStream fs = null;
		try {
			fs = new FileInputStream(new File(filePath));
		}catch(FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "文件找不到");
			return;
		}
		//设置响应头和保存文件名 
		response.setContentType("APPLICATION/OCTET-STREAM"); 
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		//写出流信息
		int b = 0;
		try {
			PrintWriter out = response.getWriter();
			while((b=fs.read())!=-1) {
				out.write(b);
			}
			fs.close();
			out.close();
			System.out.println("文件下载完毕.");
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("下载文件失败!");
		}
	}
	
	//文章点击的时候对总点击量进行+1操作
	private boolean contentClickUp(int contentId){
		boolean flag=false;
		ContentCount contentCount = ContentCount.dao.findFirst(" select * from content_count where content_id = ? ",contentId);
		if(contentCount!=null){
			flag = contentCount.set("totalClick", contentCount.getInt("totalClick")+1).update();
		}
		return flag;
	}
}
