package com.cms.util;

import java.io.File;

/** 
 * @ClassName: HtmlToPdf 
 * @Description: TODO() 
 * @author xsw
 * @date 2016-12-8 上午10:14:54 
 *  
 */

public class HtmlToPdf {
    //wkhtmltopdf在系统中的路径
    private static final String win_toPdfTool = "C:\\Program Files\\wkhtmltopdf\\bin\\wkhtmltopdf.exe";
	private static final String linux_toPdfTool = "/usr/local/bin/wkhtmltopdf";
	public static String getCommand() {
		String cmd = "";
		String system = System.getProperty("os.name");
		if(system.contains("Windows")) {
			cmd = win_toPdfTool;
		}
		else if(system.contains("Linux")) {
			cmd = linux_toPdfTool;
		}
		else if(system.contains("Mac")){
			//Mac OS X
			cmd = linux_toPdfTool;
		}
		return cmd;
	}
	/**
     * html转pdf
     * @param srcPath html路径，可以是硬盘上的路径，也可以是网络路径
     * @param destPath pdf保存路径
     * @return 转换成功返回true
     */
    public static boolean convert(String headerUrl,String footerUrl,String srcPath, String destPath){
        File file = new File(destPath);
        File parent = file.getParentFile();
        //如果pdf保存路径不存在，则创建路径
        if(!parent.exists()){
            parent.mkdirs();
        }
        
        StringBuilder cmd = new StringBuilder();
        cmd.append(getCommand());
        cmd.append(" ");
        cmd.append("  -T 29mm -B 25mm -L 10mm -R 10mm --header-html ");
        cmd.append(headerUrl);
        cmd.append(" --header-spacing 5 --footer-spacing 3 --footer-font-size 10 --footer-html ");
        cmd.append(footerUrl);
        cmd.append(" ");
        cmd.append(srcPath);
        cmd.append(" ");
        cmd.append(destPath);
        
        boolean result = true;
        try{
            Process proc = Runtime.getRuntime().exec(cmd.toString());
            HtmlToPdfInterceptor error = new HtmlToPdfInterceptor(proc.getErrorStream());
            HtmlToPdfInterceptor output = new HtmlToPdfInterceptor(proc.getInputStream());
            error.start();
            output.start();
            proc.waitFor();
        }catch(Exception e){
            result = false;
            e.printStackTrace();
        }
        
        return result;
    }
    public static boolean convert(String srcPath, String destPath){
        File file = new File(destPath);
        File parent = file.getParentFile();
        //如果pdf保存路径不存在，则创建路径
        if(!parent.exists()){
            parent.mkdirs();
        }
        
        StringBuilder cmd = new StringBuilder();
        cmd.append(getCommand());
        cmd.append(" ");
        cmd.append("  -T 29mm -B 25mm -L 10mm -R 10mm ");
        cmd.append(" --header-spacing 5 --footer-spacing 3 --footer-font-size 10 ");
        cmd.append(srcPath);
        cmd.append(" ");
        cmd.append(destPath);
        
        boolean result = true;
        try{
            Process proc = Runtime.getRuntime().exec(cmd.toString());
            HtmlToPdfInterceptor error = new HtmlToPdfInterceptor(proc.getErrorStream());
            HtmlToPdfInterceptor output = new HtmlToPdfInterceptor(proc.getInputStream());
            error.start();
            output.start();
            proc.waitFor();
        }catch(Exception e){
            result = false;
            e.printStackTrace();
        }
        
        return result;
    }
    public static void main(String[] args) {
    	//System.out.println(System.getProperty("os.name"));
        HtmlToPdf.convert("http://127.0.0.1:82/application/view?l=p&id=31", "/Users/robin/demo/test.pdf");
    }
}