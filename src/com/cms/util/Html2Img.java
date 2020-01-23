package com.cms.util;

import java.io.IOException;

public class Html2Img {
	//wkhtmltoimage在系统中的路径
    private static final String win = "C:\\Program Files\\wkhtmltopdf\\bin\\wkhtmltoimage.exe --crop-w 860 --quality 100 ";
	private static final String linux = "/usr/local/bin/wkhtmltoimage --crop-w 860 --quality 100 ";
	public static String getCommand() {
		String cmd = "";
		String system = System.getProperty("os.name");
		System.out.println(system);
		if(system.contains("Windows")) {
			cmd = win;
		}
		else if(system.contains("Linux")) {
			cmd = linux;
		}
		return cmd;
	}
	public static String getCommand(String sourceFilePath, String targetFilePath) {
		return getCommand() + sourceFilePath + " " + targetFilePath;
	}
	public static void createImg(String url, String file) {
		String command = getCommand(url, file);
		Process process;
		try {
			process = Runtime.getRuntime().exec(command);
			process.waitFor();  //这个调用比较关键，就是等当前命令执行完成后再往下执行
			System.out.println("执行完成");
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		createImg("http://127.0.0.1:81/d2s7/2-2-1094", "d:/123.jpg");
	}
}
