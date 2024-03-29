package com.cms.front.common;

import java.util.List;

import com.cms.admin.user.User;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;


public class MInterceptor implements Interceptor {
	
	public void intercept(ActionInvocation ai) {
		/*String url = ai.getController().getRequest().getRequestURL().toString();
		url = url.substring(7);
		url = url.substring(url.indexOf("/"));
		System.out.println(url);*/
		//ai.getController().setAttr("menu", new MenuDirective());
		User loginUser = ai.getController().getSessionAttr("user");
		//开发用
		//--start--
		/*if(loginUser==null){
			loginUser = User.dao.findById(1);
			List<String> uris = Db.query("select uri from user u,role_uri ru where u.role_id = ru.role_id and u.id=1");
			StringBuffer sb = new StringBuffer();
			for(String uri:uris){
				sb.append(uri+",");
			}
			// 放数据至session
			ai.getController().setSessionAttr("user", loginUser);
			ai.getController().setSessionAttr("userId", 1);
			ai.getController().setSessionAttr("storeId", 1);
			ai.getController().setSessionAttr("menu", sb.toString());
		}*/
		//--end---
		if(loginUser!=null){
			List<Record> ns = Db.find("select c.title,m.type,if(m.type=0,'announcement','notification') ntype,m.link_id,u.avatar,u.first_name,u.last_name from message m join content c on m.link_id=c.id join user u on m.sender=u.id where m.receiver=? and m.status=0",loginUser.getInt("id"));
			ai.getController().setAttr("ns",ns);
			ai.getController().setAttr("ap", ai.getViewPath());
			ai.getController().setAttr("ak", ai.getActionKey());
			ai.getController().setAttr("method", ai.getMethodName());
			System.out.println(ai.getViewPath());
			System.out.println(ai.getActionKey());
			System.out.println(ai.getMethodName());
			ai.invoke();
		}
		else{
			ai.getController().forwardAction("/l");
		}
	}
}