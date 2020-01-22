package com.cms.front.common;


import com.cms.admin.directive.MenuDirective;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Const;
import com.jfinal.core.Controller;
import com.jfinal.i18n.I18N;


public class GlobalInterceptor implements Interceptor {
	
	public void intercept(ActionInvocation ai) {
//		String current_url = ai.getController().getRequest().getRequestURL().toString();
//		current_url = current_url.substring(7);
//		String vurl = current_url;
//		vurl = vurl.substring(0,current_url.indexOf("/"));
//		
//			current_url = current_url.substring(current_url.indexOf("/"));
//			
//			String referer_url = ai.getController().getRequest().getHeader("referer");
//			if(referer_url!=null){
//				referer_url = referer_url.substring(7);
//				referer_url = referer_url.substring(referer_url.indexOf("/"));
//				if(current_url.equals("/l")){
//					if(referer_url.equals("/")||referer_url.equals("/logout")||referer_url.equals("/login"))referer_url="/m";
//					ai.getController().setSessionAttr("furl", referer_url);
//				}
//			}
//			
			//System.out.println(referer_url);
			ai.getController().setAttr("menu", new MenuDirective());
			//User loginUser = ai.getController().getSessionAttr("user");
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
			/*Cookie cookie = new Cookie(Const.I18N_LOCALE,null); 
		      cookie.setMaxAge(0); 
		     //cookie.setPath("/");//根据你创建cookie的路径进行填写 
		     response.addCookie(cookie);*/
			
			Controller c = ai.getController();
			String tmp = c.getCookie(Const.I18N_LOCALE);
			System.out.println("tmp---"+tmp);
			String i18n = c.getRequest().getLocale().toString();
			System.out.println("i18n---"+i18n);
			if (!i18n.equals(tmp)) {
				ai.getController().setCookie(Const.I18N_LOCALE, i18n,
						Const.DEFAULT_I18N_MAX_AGE_OF_COOKIE);
				System.out.println("setCookie");
				// 创建“英文/美国”的Locale
			    //Locale localeUS = new Locale("en", "US");
				// 创建“简体中文”的Locale
			    //Locale localeCN = Locale.SIMPLIFIED_CHINESE;
			    
				//I18N.init("i18n", c.getRequest().getLocale(), 999999999);
			}
			else {
				System.out.println("dont set");
			}
			ai.invoke();
		
	}
}