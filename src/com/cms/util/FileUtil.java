package com.cms.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
	public static String readfile(String filePath){
		 File file = new File(filePath);  
		 InputStream input = null;
          try {
              input = new FileInputStream(file);
          } catch (FileNotFoundException e) {
              e.printStackTrace();
          }  
          StringBuffer buffer = new StringBuffer();  
         byte[] bytes = new byte[1024];
         try {
             for(int n ; (n = input.read(bytes))!=-1 ; ){ 
                 buffer.append(new String(bytes,0,n,"UTF-8"));  
             }
         } catch (IOException e) {
             e.printStackTrace();
         }
         return buffer.toString();  
	}
	public static void deleteDir(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles())
                deleteDir(f);
        }
        file.delete();
    }
	public static void deleteAll(File file) {
		if(file.exists()) {
	        if (file.isFile() || file.list().length == 0) {
	            file.delete();
	        } else {
	            for (File f : file.listFiles()) {
	                deleteAll(f); // 递归删除每一个文件
	
	            }
	            file.delete(); // 删除文件夹
	        }
		}
    }
	public static void deleteAll(String file) {
		deleteAll(new File(file));
    }
}
