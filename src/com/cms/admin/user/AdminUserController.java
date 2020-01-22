package com.cms.admin.user;

import java.util.List;


import com.cms.admin.role.Role;
import com.cms.util.Md5;
import com.cms.util.SendMail;
import com.jfinal.aop.Before;
import com.jfinal.aop.ClearInterceptor;
import com.jfinal.aop.ClearLayer;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

@Before(UserInterceptor.class)
public class AdminUserController extends Controller {
	public void index() {
		StringBuffer sql =new StringBuffer("from user u left join role r on u.role_id=r.id");
		//关键字
		if(getPara("keyword")!=null && !"".equals(getPara("keyword").trim())){
			String kw = getPara("keyword").trim();
			sql.append(" where u.nick like '%"+kw+"%' or u.name like '%"+kw+"%' or u.alias like '%"
			+kw+"%' or u.address like '%"+kw+"%' or u.mobile like '%"+kw+"%' or u.email like '%"+kw+"%'");
		}
		sql.append(" order by u.id desc ");
		setAttr("keyword", getPara("keyword"));
		setAttr("userPage", User.dao.paginate(getParaToInt(0, 1), 30, "select u.*,r.name as role_name",sql.toString() ));
		render("user.html");
	}
	
	public void add() {
		setAttr("roleList", Role.dao.find("select * from role"));
	}
	public void account() {
		int uid = getParaToInt(0);
		User u = User.dao.findById(uid);
		setAttr("user",u);
		setAttr("cyList",Db.find("select code,name from currency_code where is_show=1"));
		render("account.html");
	}
	@ClearInterceptor
	public void person() {
		setAttr("user", User.dao.findById(getSessionAttr("userId")));
		setAttr("pwdMsg", "不修改密码请留空。");
	}
	
	@ClearInterceptor
	@Before(PersonValidator.class)
	public void pwd() {
		String id = getPara("user.id");
		String pwd = getPara("user.pwd");
		String originPwd = getPara("origin_pwd");
		long i = Db.queryLong("select count(*) from user where id=? and pwd=?",
				id,Md5.encodePassword(originPwd)) ;
		if(i>0){
			if(pwd==""){
				pwd = User.dao.findById(id).getStr("pwd");
				getModel(User.class).set("pwd", pwd).update();
			}else{
				getModel(User.class).set("pwd", Md5.encodePassword(pwd)).update();
			}
			setAttr("resultMsg", "个人信息编辑成功！");
		}
		else{
			setAttr("resultMsg", "原密码错误，个人信息编辑失败！");
		}
		forwardAction("/admin/user/person");
	}
	@Before(UserValidator.class)
	public void save() {
		String email = getPara("user.email");
		String mobile = getPara("user.mobile");
		String verifycode = getPara("user.access_code");
		List<User> list = User.dao.find("select * from user where email=? or mobile=?",email,mobile);
		if(list.size()>0){
			setAttr("usernameMsg", "用户名/Email/手机号已存在！请更正。");
			forwardAction("/admin/user/add");
		}
		else{
			getModel(User.class).save();
			SendMail.send(email, verifycode,1);
			setAttr("resultMsg", "User added successful！");
			forwardAction("/admin/user");
		}
	}
	
	public void edit() {
		setAttr("roleList", Role.dao.find("select * from role"));
		setAttr("user", User.dao.findById(getParaToInt()));
		setAttr("pwdMsg", "不修改密码请留空。");
	}
	public void kaihu() {
		Db.update("update user set kaihu_status=2 where id=?",getParaToInt());
		setAttr("resultMsg", "开户申请状态变更成功！");
		forwardAction("/admin/user");
	}
	public void view() {
		User user = User.dao.findById(getParaToInt());
		setAttr("roleName", Role.dao.findById(user.getInt("role_id")).getStr("name"));
		setAttr("user", user);
	}
	@ClearInterceptor
	public void self() {
		User user = User.dao.findById(getParaToInt());
		setAttr("roleName", Role.dao.findById(user.getInt("role_id")).getStr("name"));
		setAttr("user", user);
	}
	
	@Before(UserEditValidator.class)
	public void update() {
		String id = getPara("user.id");
		String email = getPara("user.email");
		String mobile = getPara("user.mobile");
		
		List<User> list = User.dao.find("select * from user where (email=? or mobile=?) and id!=?",email,mobile,id);
		if(list.size()>0){
			setAttr("nameMsg", "Email or mobile has existed！Please change。");
			forwardAction("/admin/user/edit/"+id);
		}
		else{
			getModel(User.class).update();
			setAttr("resultMsg", "Success！");
			forwardAction("/admin/user");
		}
	}
	public void save_account() {
		getModel(User.class).update();
		setAttr("resultMsg", "用户编辑成功！");
		forwardAction("/admin/user");
	}
	
	public void delete() {
		Integer[] ids = getParaValuesToInt("ids");
		String strId = "";
		if(ids!=null && ids.length>0){
			for(Integer id:ids){
				strId += id+",";
			}
			strId = strId.substring(0, strId.length()-1);
			int i = Db.update("delete from user where id in ("+strId+")");
		}
		else{
			if(getParaToInt()>0)
				User.dao.deleteById(getParaToInt());
		}
		setAttr("resultMsg", "用户删除成功！");
		forwardAction("/admin/user");
	}
}


