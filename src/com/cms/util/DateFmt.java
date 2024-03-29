package com.cms.util;
import java.io.File;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFmt {
	private DateFmt(){}
	/**
     * Java将Unix时间戳转换成指定格式日期字符串
     * @param timestampString 时间戳 如："1578796500000";
     * @param formats 要格式化的格式 默认："yyyy-MM-dd HH:mm:ss";
     *
     * @return 返回结果 如："2016-09-05 16:06:42";
     */
    public static String TimeStamp2Date(String timestampString, String formats) {
        if ("".equals(formats))
            formats = "yyyy-MM-dd HH:mm:ss";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        //Long timestamp = Long.parseLong(timestampString);
        String date = new SimpleDateFormat(formats, Locale.ENGLISH).format(new Date(timestamp));
        return date;
    }
	public static Date USDate(String d) {
		Date dt=null;
		if(!"".equals(d)) {
			String pattern = "dd/MM/yyyy";
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			try {
				dt = df.parse(d);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return dt;
	}
	public static String formatDate(Date date){
		return DateFormat.getDateInstance().format(date);
	}
	public static String formatDate(String dt,String format1,String format2){
		SimpleDateFormat df = new SimpleDateFormat(format1);
		SimpleDateFormat df2 = new SimpleDateFormat(format2);
		Date dt1=null;
		try {
			dt1 = df.parse(dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return df2.format(dt1);
	}
	//获取短日期，如"2012-12-12"
	public static String addDays(int days){
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar cal=Calendar.getInstance(); 
		Date date=cal.getTime(); 
		cal.add(Calendar.DATE, days); 
		date=cal.getTime(); 
		return df.format(date);
	}
	public static String addDays(String dt,String format1,String format2,int days){
		SimpleDateFormat df = new SimpleDateFormat(format1);
		SimpleDateFormat df2 = new SimpleDateFormat(format2);
		Date dt1=null;
		try {
			dt1 = df.parse(dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal=Calendar.getInstance(); 
		cal.setTime(dt1);
		cal.add(Calendar.DATE, days); 
		Date date=cal.getTime(); 
		return df2.format(date);
	}
	public static String addDays(String dt,int days){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dt1=null;
		try {
			dt1 = df.parse(dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal=Calendar.getInstance(); 
		cal.setTime(dt1);
		cal.add(Calendar.DATE, days); 
		Date date=cal.getTime(); 
		return df.format(date);
	}
	public static String addLongDays(String dt,int days){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date dt1=null;
		try {
			dt1 = df.parse(dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar cal=Calendar.getInstance(); 
		cal.setTime(dt1);
		cal.add(Calendar.DATE, days); 
		Date date=cal.getTime(); 
		return df.format(date);
	}
	//获取长日期，如"2012-12-12 12:12:12"
	public static String addLongDays(int days){
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Calendar cal=Calendar.getInstance(); 
		Date date=cal.getTime(); 
		cal.add(Calendar.DATE, days); 
		date=cal.getTime(); 
		return df.format(date);
	}
	//获取长日期，如"2012-12-12 12:12:12"
	public static String getCurrentTimeName(int days){
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd_HHmmss"); 
		Calendar cal=Calendar.getInstance(); 
		Date date=cal.getTime(); 
		cal.add(Calendar.DATE, days); 
		date=cal.getTime(); 
		return df.format(date);
	}
	public static String min(String DATE1, String DATE2,String format) {
        DateFormat df = new SimpleDateFormat(format);
        String str = DATE2;
        if ("".equals(DATE2)) {
        	str = DATE1;
     	}
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() < dt2.getTime()) {
                str= DATE1;
            } 
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return str;
    }
	public static Date getTime(){
		return new Date(System.currentTimeMillis()); 
	}
	public static String getTime(String time){
		String[] mytime = time.split("-");
		if(mytime[1].length()==1){
			time = mytime[0]+"-0"+mytime[1]+"-"+mytime[2];
		}
		return time;
	}
	public static String getTime(Date time,String format){
		return getTime(Date2Str(time,format));
	}
	public static String Date2Str(Date d,String format){
		SimpleDateFormat sdf=new SimpleDateFormat(format);  
		return sdf.format(d);
	}
	public static String Now2Str(){
		Calendar cal=Calendar.getInstance(); 
		Date date=cal.getTime(); 
		SimpleDateFormat sdf=new SimpleDateFormat(DEFAULT_PATTERN);  
		return sdf.format(date);
	}
	public static String getValidMonth(String time){
		String[] mytime = time.split("-");
		if(mytime[1].length()==1){
			time = mytime[0]+"-0"+mytime[1];
		}
		return time;
	}
	private static final String DEFAULT_PATTERN = "yyyyMMddHHmmss";

	 /**
	  * @param time
	  * @param pattern
	  * @return
	  */
	 public static String timestamp2str(Timestamp time, String pattern) {
	  if (time == null) {
	   //throw new IllegalArgumentException("Timestamp is null");
		  return "";
	  }
	  if (pattern != null && !"".equals(pattern)) {
	   if (!"yyyyMMddHHmmss".equals(pattern)
	     && !"yyyy-MM-dd HH:mm:ss".equals(pattern)
	     && !"HH:mm:ss".equals(pattern)
	     && !"yyyy-MM-dd".equals(pattern)
	     && !"yyyy-M-d".equals(pattern)
	     && !"hh:mm a dd/MM/yyyy".equals(pattern)
	     && !"MM/dd/yyyy".equals(pattern)){
	    throw new IllegalArgumentException("Date format ["+pattern+"] is invalid");
	   }
	  }else{
	   pattern = DEFAULT_PATTERN;
	  }
	  
	  Calendar cal = Calendar.getInstance(TimeZone.getDefault());
	  cal.setTime(time);
	  SimpleDateFormat sdf = new SimpleDateFormat(pattern,Locale.ENGLISH);
	  return sdf.format(cal.getTime());
	 }
	 public static String format2str(String timeStr,String pattern1,String pattern2) {
		 return timestamp2str(str2Timestamp( timeStr, pattern1),pattern2);
	 }

	 public static Timestamp str2Timestamp(String timeStr,String pattern)
	 {
	  Timestamp result = null;
	  if (timeStr == null) {
	   throw new IllegalArgumentException("Timestamp is null");
	  }
	  if (pattern != null && !"".equals(pattern)) {
	   if (!"yyyyMMddHHmmss".equals(pattern)
	     && !"yyyy-MM-dd HH:mm:ss".equals(pattern)
	     && !"MM/dd/yyyy HH:mm:ss".equals(pattern)
	     && !"yyyy-MM-dd".equals(pattern)
	     && !"MM/dd/yyyy".equals(pattern)) {
	    throw new IllegalArgumentException("Date format ["+pattern+"] is invalid");
	   }
	  }else{
	   pattern = DEFAULT_PATTERN;
	  }
	  
	  Date d = null;
	  SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	  try {
	   d = sdf.parse(timeStr);
	   result = new Timestamp(d.getTime());
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	  return result;
	 }
	/*public static String getShortTime(Timestamp timestamp){
		String[] mytime = ((String) timestamp).split(" ");
		return mytime[0];
	}*/
	public static String getStampTime(Timestamp time){
		if(time==null)return "";
		return time.toString().split("\\.")[0];
	}
	public static String getSTime(String time){
		return time.split("\\.")[0];
	}
	 public static int compare(String DATE1, String DATE2,String format) {
	        DateFormat df = new SimpleDateFormat(format);
	        try {
	            Date dt1 = df.parse(DATE1);
	            Date dt2 = df.parse(DATE2);
	            if (dt1.getTime() > dt2.getTime()) {
	                return 1;
	            } else if (dt1.getTime() < dt2.getTime()) {
	                return -1;
	            } else {
	                return 0;
	            }
	        } catch (Exception exception) {
	            exception.printStackTrace();
	        }
	        return 0;
	    }
	public static String getTimeName(String realPath){
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmsss");
		SimpleDateFormat ym = new SimpleDateFormat("yyyyMM");
		String downroot = "/down/";
		String path=ym.format(new Date());
		File dir = new File(realPath+downroot+path);
		if(!dir.exists()){
			dir.mkdir();
		}
		String file = sf.format(new Date());
		dir = new File(realPath+downroot+path+"/"+file);
		if(!dir.exists()){
			dir.mkdir();
		}
		return downroot+path+"/"+file+"/"+file+".xls";
	}
	public static long diff(String date1, String date2) 
	{ 
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date dt1=null;
		Date dt2=null;
		try {
			dt1 = df.parse(date1);
			dt2 = df.parse(date2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    //return date1.getTime() / (24*60*60*1000) - date2.getTime() / (24*60*60*1000); 
	    return dt1.getTime() / 86400000 - dt2.getTime() / 86400000;  //用立即数，减少乘法计算的开销
	}
	public static int weekDay(String s) {
        //final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四","星期五", "星期六" };

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1=null;
		try {
			dt1 = df.parse(s);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt1);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
        //return dayNames[dayOfWeek - 1];
	}
	public static boolean ifWorkDay(String s){
		if(weekDay(s)==7||weekDay(s)==1){
			return false;
		}
		return true;
	}
	public static void main(String[] args) {
		//long i = 1578796500000l;
		//Timestamp t = new Timestamp(1578796500000l);
		System.out.println(timestamp2str(new Timestamp(1578796500000l),"hh:mm a dd/MM/yyyy"));
	}
}
