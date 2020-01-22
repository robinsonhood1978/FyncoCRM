package com.cms.admin.visit;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.cms.util.StrUtil;


public class VisitInterceptor implements Interceptor {
	
	public void intercept(ActionInvocation ai) {
		if(StrUtil.exist(ai.getController().getSessionAttr("menu").toString(), ai.getActionKey())){
			System.out.println(ai.getActionKey());
			ai.getController().setAttr("ap", ai.getViewPath());
			ai.getController().setAttr("ak", ai.getActionKey());
			ai.getController().setAttr("method", ai.getMethodName());
			ai.invoke();
		}
		else{
			ai.getController().forwardAction("/noright");
		}
	}
}
