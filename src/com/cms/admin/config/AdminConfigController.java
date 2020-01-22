package com.cms.admin.config;

import java.util.List;

import com.cms.admin.company.Company;
import com.cms.admin.company.CompanyValidator;
import com.cms.admin.user.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

@Before(ConfigInterceptor.class)
public class AdminConfigController extends Controller {
	public void index() {
		setAttr("configPage", Config.dao.paginate(getParaToInt(0, 1), 10, "select c.*", 
				"from config c order by c.id asc"));
		render("list.html");
	}
	
	
	public void edit() {
		setAttr("config", Config.dao.findById(getParaToInt()));
	}
	
	public void update() {
		//String id = getPara("config.id");
		//String value = getPara("config.value");
		
		getModel(Config.class).update();
		forwardAction("/admin/config");
	}
	public void add() {
	}
	
	@Before(ConfigValidator.class)
	public void save() {
		String name = getPara("config.name");
		List<Config> list = Config.dao.find("select * from config where name=?",name);
		if(list.size()>0){
			setAttr("nameMsg", "已存在！请更正。");
			forwardAction("/admin/config/add");
		}
		else{
			getModel(Config.class).save();
			setAttr("resultMsg", "数据保存成功。");
			forwardAction("/admin/config");
		}
	}
}


