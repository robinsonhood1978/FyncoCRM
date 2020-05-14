package com.cms.util;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class StrUtil {
	public static JSONArray joinJSONArray(JSONArray array1, JSONArray array2) {
        StringBuffer sbf = new StringBuffer();
        
        try {
            int len = array1.size();
            for (int i = 0; i < len; i++) {
                JSONObject obj1 = (JSONObject) array1.get(i);
                if (i == len - 1)
                    sbf.append(obj1.toString());
                else
                    sbf.append(obj1.toString()).append(",");
            }
            len = array2.size();
            if (len > 0)
                sbf.append(",");
            for (int i = 0; i < len; i++) {
                JSONObject obj2 = (JSONObject) array2.get(i);
                if (i == len - 1)
                    sbf.append(obj2.toString());
                else
                    sbf.append(obj2.toString()).append(",");
            }
            
            sbf.insert(0, "[").append("]");
            JSONArray jSONArray = JSONArray.fromObject(sbf.toString());
            return jSONArray;
        } catch (Exception e) {
        }
        return null;
    }
	public static String formatString(Double data) {
		if(data!=0) {
			DecimalFormat df = new DecimalFormat("#,###.00"); 
			return df.format(data);
		}
		else {
			return "0";
		}
    }
	public static String formatString(String data) {
		String str = "";
		if(data!=null&&!"".equals(data)) {
			DecimalFormat df = new DecimalFormat("#,###.00"); 
			str = df.format(Double.parseDouble(data));
		}
        return str;
    }
	public static String getRandom(int len) {
	     int rs = (int) ((Math.random() * 9 + 1) * Math.pow(10, len - 1));
	     return String.valueOf(rs);
	}
	public static String randomNum(int n){
		int i=1;
		
		for(int j=1;j<n;j++) {
			i=i*10;
		}
		
		Random random = new Random();
		int number=random.nextInt(9*i)+i;
		//System.out.println("n:"+number);
		return String.format("%0"+n+"d", number); 
	}
	public static boolean exist(String menu,String s){		
		String uris = "/back/manage/,/back/score/";
		String urlPrefix = "";
		if(uris.indexOf(s)>-1){
			urlPrefix = s;
		}
		else{
			if(s.lastIndexOf("/")>0){
				urlPrefix = s.substring(0, s.indexOf("/", 1));
			}
			else{
				urlPrefix = s;
			}
		}
		String exclude = "/taskin/,/bugin/";
		if(exclude.indexOf(urlPrefix)>-1)return true;
		if(menu.indexOf(urlPrefix)>-1){
			return true;
		}
		else{
			return false;
		}
	}
	public static boolean isBlank(String s){
		if(s!=null){
			if("".equals(s.trim())){
				return true;
			}
		}
		return false;
	}
	public static String null2Blank(Integer s){
		if(s!=null){
			return String.valueOf(s);
		}
		return "";
	}
	public static String null2Blank(String s){
		if(s!=null){
			//if(s.equals("0"))s="";
			return s;
		}
		return "";
	}
	public static int null2Zero(Integer s){
		if(s!=null){
			return s.intValue();
		}
		return 0;
	}
	public static int null2Zero(String s){
		if(s!=null && !"".equals(s)){
			return Integer.parseInt(s);
		}
		return 0;
	}
	public static String getRouteByController(String str) {
		String route="";
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if(Character.isUpperCase(ch)){
				route+=("/"+Character.toLowerCase(ch));
			}else{
				route+=ch;
			}
		}	
		return route;
	}
	public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
	    Map<String, Object> retMap = new HashMap<String, Object>();

	    if(!json.isNullObject()) {
	        retMap = toMap(json);
	    }
	    return retMap;
	}

	public static Map<String, Object> toMap(JSONObject object) throws JSONException {
	    Map<String, Object> map = new HashMap<String, Object>();

	    Iterator<String> keysItr = object.keys();
	    while(keysItr.hasNext()) {
	        String key = keysItr.next();
	        Object value = object.get(key);

	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        map.put(key, value);
	    }
	    return map;
	}

	public static List<Object> toList(JSONArray array) throws JSONException {
	    List<Object> list = new ArrayList<Object>();
	    for(int i = 0; i < array.size(); i++) {
	        Object value = array.get(i);
	        if(value instanceof JSONArray) {
	            value = toList((JSONArray) value);
	        }

	        else if(value instanceof JSONObject) {
	            value = toMap((JSONObject) value);
	        }
	        list.add(value);
	    }
	    return list;
	}
	 public static void main(String[] args) throws UnsupportedEncodingException {
		 System.out.println(StrUtil.replaceBlank("just do it!"));
	 }
	 public static String replaceBlank(String str) {
	        String dest = "";
	        if (str!=null) {
	            Pattern p = Pattern.compile("\\t|\r|\n");
	            Matcher m = p.matcher(str);
	            dest = m.replaceAll("");
	        }
	        return dest;
	    }
	    
	    /*-----------------------------------
	 
	    笨方法：String s = "你要去除的字符串";
	 
	            1.去除空格：s = s.replace('\\s','');
	 
	            2.去除回车：s = s.replace('\n','');
	    这样也可以把空格和回车去掉，其他也可以照这样做。
	 
	    注：\n 回车(\u000a)
	    \t 水平制表符(\u0009)
	    \s 空格(\u0008)
	    \r 换行(\u000d)*/

}
