package com.cms.admin.trade;

import com.cms.util.StrUtil;
import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;


public class TradeInterceptor implements Interceptor {
	
	public void intercept(ActionInvocation ai) {
		if(StrUtil.exist(ai.getController().getSessionAttr("menu").toString(), ai.getActionKey())){
			ai.getController().setAttr("ap", ai.getViewPath());
			//System.out.println(ai.getMethodName());
			ai.getController().setAttr("ak", ai.getActionKey());
			ai.getController().setAttr("method", ai.getMethodName());
			ai.invoke();
		}
		else{
			ai.getController().forwardAction("/noright");
		}
	}
}
