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
}
