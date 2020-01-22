package com.cms.front.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.cms.util.XssShieldUtil;

public final class AvicRequestWrapper extends HttpServletRequestWrapper {
    public AvicRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }
    public String getParameter(String str){
    	String mystr = super.getParameter(str);
    	if(mystr!=null)
    		mystr = mystr.replaceAll("'", "").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("`", "");
    	 // 返回值之前 先进行过滤
        return XssShieldUtil.stripXss(super.getParameter(XssShieldUtil.stripXss(mystr)));
    }
    public String[] getParameterValues(String parameter) {
    	// 返回值之前 先进行过滤
        String[] values = super.getParameterValues(XssShieldUtil.stripXss(parameter));
        if(values != null){
            for (int i = 0; i < values.length; i++) {
            	values[i] = values[i].replaceAll("'", "").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("`", "");
                values[i] = XssShieldUtil.stripXss(values[i]);
            }
        }
        return values;
        
//        String[] results = super.getParameterValues(XssShieldUtil.stripXss(parameter));
//        if (results == null)
//            return null;
//        int count = results.length;
//
//        String[] trimResults = new String[count];
//        for (int i = 0; i < count; i++) {
//            trimResults[i] = results[i].replaceAll("'", "").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("`", "");
//        }
//        return trimResults;
    }
}
