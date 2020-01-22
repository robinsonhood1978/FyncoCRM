package com.cms.admin.common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cms.util.StrUtil;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

public class FileController extends Controller {
	private static SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmsssSSS");
	private static SimpleDateFormat ym = new SimpleDateFormat("yyyyMM");
	private static String uploadroot = "/upload/";
	private static String root;
	public void test() {
		String[] strArray={"/aa/1.jpg","/b/2.jpg","/c/3.jpg"};
		Map map = new HashMap();
		map.put("errno","0");
		map.put("data",strArray);
		renderJson(map);
	}
	public void file() {
		String savefilename="";
		String filedataFileName ="";
		StringBuffer files=new StringBuffer();
		List<UploadFile> upfilelist = this.getFiles();
		if(upfilelist!=null&&upfilelist.isEmpty()==false){
			String realPath = this.getRequest().getRealPath("/");
			
			String path=ym.format(new Date());
			File dir = new File(realPath+uploadroot+path);
			if(!dir.exists()){
				dir.mkdir();
			}
			int fsize=upfilelist.size();
			int i=1;
			for(UploadFile upfile:upfilelist){
				if(upfile!=null){
					
					File file = upfile.getFile();
					filedataFileName = upfile.getOriginalFileName();
					savefilename = uploadroot+path+"/"+ sf.format(new Date())+StrUtil.randomNum(3)+ filedataFileName.substring(filedataFileName.lastIndexOf("."));
					if(file != null){
						upfile.getFile().renameTo(new File(realPath + savefilename));
						files.append(savefilename);
						if(i<fsize)
							files.append(",");
					}
				}
				i++;
			}
		}
		
		String[] strArray=files.toString().split(",");
		Map map = new HashMap();
		map.put("errno","0");
		map.put("data",strArray);
		renderJson(map);

	}
	/**单个或多个文件上传      swfupload*/
	@SuppressWarnings("deprecation")
	public void up() {
		String errmsg ="", savefilename="";
		StringBuffer files=new StringBuffer();
		List<UploadFile> upfilelist = this.getFiles();
		boolean suc=false;
		if(upfilelist!=null&&upfilelist.isEmpty()==false){
			if(root==null)
				root=this.getRequest().getContextPath();
			String realPath = this.getRequest().getRealPath("/");
			int fsize=upfilelist.size();
			int i=1;
			for(UploadFile upfile:upfilelist){
				File file = upfile.getFile();
				String filedataFileName = upfile.getOriginalFileName();
				savefilename = uploadroot+ sf.format(new Date())+ filedataFileName.substring(filedataFileName.lastIndexOf("."));
				if(file != null){
					upfile.getFile().renameTo(new File(realPath + savefilename));
					suc=true;
					files.append(root+savefilename);
					if(i<fsize)
						files.append(",");
				}
				i++;
			}
		}else{
			errmsg="未上传文件";
		}
		this.renderText("{'err':'" + errmsg + "','msg':'" +files.toString()+ "','suc':"+suc+"}");
	}
	
	/**文件下载 */
	public void down() {
		
	}

}
