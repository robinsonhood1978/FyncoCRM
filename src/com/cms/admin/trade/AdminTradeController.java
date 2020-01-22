package com.cms.admin.trade;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.cms.admin.category.Category;
import com.cms.admin.company.Company;
import com.cms.admin.goods.GoodsAtt;
import com.cms.admin.user.User;
import com.cms.util.DateFmt;
import com.cms.util.Md5;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;


@Before(TradeInterceptor.class)
public class AdminTradeController extends Controller {
	private static SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmsss");
	private static SimpleDateFormat ym = new SimpleDateFormat("yyyyMM");
	private static String uploadroot = "/upload/";
	
	public void fileup() {
		
	}
	public void index() {
		setAttr("tradePage", Trade.dao.paginate(getParaToInt(0, 1), 30, "select u.name,u.kaihu_status,u2.name as actor,t.*", "from trade t,user u,user u2 where t.uid=u.id and t.create_actor=u2.id order by id desc"));
		render("trade.html");
	}
	public void statistics() {
		setAttr("statPage", Trade.dao.paginate(getParaToInt(0, 1), 30, "select a.*,b.sum_sale_btc,(a.sum_buy_btc-b.sum_sale_btc) as diff", "from (select u.id,u.name,u.kaihu_status,u.register_time,sum(t.withdraw_total) as sum_buy_btc from trade t,user u where t.uid=u.id and t.withdraw_cy='BTC' group by t.uid) as a,(select u.id,sum(t.pay_total) as sum_sale_btc from trade t,user u where t.uid=u.id and t.pay_cy='BTC' group by t.uid) as b where a.id=b.id"));
		render("statistics.html");
	}
	public void buybtc() {
		int uid = getParaToInt(0);
		User u = User.dao.findById(uid);
		Record r = Db.findFirst("select a.sum_buy_btc,b.sum_sale_btc,(a.sum_buy_btc-b.sum_sale_btc) as diff from (select t.uid,sum(t.withdraw_total) as sum_buy_btc from trade t where t.withdraw_cy='BTC' group by t.uid) as a,(select t.uid,sum(t.pay_total) as sum_sale_btc from trade t where t.pay_cy='BTC' group by t.uid) as b where a.uid=b.uid and a.uid=?",uid);
		setAttr("btc",r);
		setAttr("serv_fee_rate",Db.queryFirst("select value from config where code='serv_fee_rate'"));
		setAttr("user",u);
		setAttr("cyList",Db.find("select code,name from currency_code where is_show=1"));
		setAttr("payMethodList",Db.find("select ename,name from dict where code='pay_method'"));
		//setAttr("tradePage", Trade.dao.paginate(getParaToInt(0, 1), 30, "select u.*,r.name as role_name", "from user u left join role r on u.role_id=r.id order by u.id desc"));
		render("add.html");
	}
	public void att() {
		String savefilename="";
		String filedataFileName ="";
		StringBuffer files=new StringBuffer();
		UploadFile upfile = this.getFile();
		int num = getParaToInt("attachmentNum");
		System.out.println(num+"#");
		boolean suc=false;
		if(upfile!=null){
			String realPath = this.getRequest().getRealPath("/");
			int i=1;
			System.out.println(realPath+"#");
			
			String path=ym.format(new Date());
			File dir = new File(realPath+uploadroot+path);
			if(!dir.exists()){
				dir.mkdir();
			}
			File file = upfile.getFile();
			filedataFileName = upfile.getOriginalFileName();
			savefilename = uploadroot+path+"/"+ sf.format(new Date())+ filedataFileName.substring(filedataFileName.lastIndexOf("."));
			if(file != null){
				upfile.getFile().renameTo(new File(realPath + savefilename));
				suc=true;
				files.append(realPath+savefilename);
			}
				
		}else{
			setAttr("error", "请选择需要上传的文档");
		}
		setAttr("attachmentNum", num);
		setAttr("attachmentPath", savefilename);
		setAttr("attachmentName", filedataFileName);
		render("att.html");
	}
	
	
	public void add() {
		setAttr("catList", Category.dao.find("SELECT node.* FROM category node, category parent WHERE node.lft >= parent.lft AND node.rgt <= parent.rgt and parent.parent_id is null ORDER BY node.lft"));
		setAttr("companyList", Company.dao.find("select * from company"));
	}
	
	
	public void save() {
		int actor = (getSessionAttr("user")==null)?1:((User)getSessionAttr("user")).getInt("id");
		String final_proof = getPara("final_proof");
		String transfer_screenshots = getPara("transfer_screenshots");
		
		//自定义发布日期（没填写默认当前日期）
		Date customDate = null ;
		String customTime = getPara("trade.custom_time");
		if(customTime==null || customTime.trim().equals("")){
			customDate = new Date();
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			try {
				customDate = sdf.parse(customTime);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		boolean succeed = getModel(Trade.class).set("trade_no", DateFmt.Now2Str()).set("create_actor", actor).set("custom_time", customDate).save();
		int primary_id = Db.queryLong("select max(id) from trade").intValue();
		if(!final_proof.equals("")) {
			String[] arr_final_proof = final_proof.split(",");
			int i = 1;
			for(String f:arr_final_proof) {
				Db.update("insert into general_att (table_name,column_name,primary_id,name,url,priority) values ('trade','final_proof',?,?,?,?)",
						primary_id,f.substring(f.lastIndexOf("/")+1),f,i);
				i++;
			}
		}
		if(!transfer_screenshots.equals("")) {
			String[] arr_transfer_screenshots = transfer_screenshots.split(",");
			int j = 1;
			for(String f:arr_transfer_screenshots) {
				Db.update("insert into general_att (table_name,column_name,primary_id,name,url,priority) values ('trade','transfer_screenshots',?,?,?,?)",
						primary_id,f.substring(f.lastIndexOf("/")+1),f,j);
				j++;
			}
		}
		if(succeed){
			setAttr("resultMsg", "数据保存成功。");
			forwardAction("/admin/trade");
		}
		else{
			setAttr("resultMsg", "数据保存失败，请重新录入。");
			forwardAction("/admin/trade/buybtc");
		}

	}
	public void editor() {
		
	}
	
	public void edit() {
		int id = getParaToInt(0);
		Trade t = Trade.dao.findById(id);
		User u = User.dao.findById(t.getInt("uid"));
		setAttr("serv_fee_rate",Db.queryFirst("select value from config where code='serv_fee_rate'"));
		setAttr("user",u);
		Record rd = Db.findFirst("select a.sum_buy_btc,b.sum_sale_btc,(a.sum_buy_btc-b.sum_sale_btc) as diff from (select t.uid,sum(t.withdraw_total) as sum_buy_btc from trade t where t.withdraw_cy='BTC' group by t.uid) as a,(select t.uid,sum(t.pay_total) as sum_sale_btc from trade t where t.pay_cy='BTC' group by t.uid) as b where a.uid=b.uid and a.uid=?",u.getInt("id"));
		setAttr("btc",rd);
		setAttr("trade",t);
		setAttr("cyList",Db.find("select code,name from currency_code where is_show=1"));
		setAttr("payMethodList",Db.find("select ename,name from dict where code='pay_method'"));
		List<Record> finalProofList = Db.find("select name,url from general_att where primary_id=? and column_name='final_proof'",id);
		String finalProof = "";
		int i=0;
		for(Record r:finalProofList) {
			if(i>0)finalProof += ",";
			finalProof += r.getStr("url"); 
			i++;
		}
		setAttr("finalProof",finalProof);
		setAttr("finalProofList",finalProofList);
		List<Record> transferScreenshotsList = Db.find("select name,url from general_att where primary_id=? and column_name='transfer_screenshots'",id);
		String transferScreenshots = "";
		int j=0;
		for(Record r:transferScreenshotsList) {
			if(j>0)transferScreenshots += ",";
			transferScreenshots += r.getStr("url"); 
			j++;
		}
		setAttr("transferScreenshots",transferScreenshots);
		setAttr("transferScreenshotsList",transferScreenshotsList);
		render("edit.html");
	}
	public void view() {
		int id = getParaToInt(0);
		Trade t = Trade.dao.findById(id);
		User u = User.dao.findById(t.getInt("uid"));
		Record r = Db.findFirst("select a.sum_buy_btc,b.sum_sale_btc,(a.sum_buy_btc-b.sum_sale_btc) as diff from (select t.uid,sum(t.withdraw_total) as sum_buy_btc from trade t where t.withdraw_cy='BTC' group by t.uid) as a,(select t.uid,sum(t.pay_total) as sum_sale_btc from trade t where t.pay_cy='BTC' group by t.uid) as b where a.uid=b.uid and a.uid=?",u.getInt("id"));
		setAttr("btc",r);
		setAttr("serv_fee_rate",Db.queryFirst("select value from config where code='serv_fee_rate'"));
		setAttr("user",u);
		setAttr("trade",t);
		setAttr("cyList",Db.find("select code,name from currency_code where is_show=1"));
		setAttr("payMethodList",Db.find("select ename,name from dict where code='pay_method'"));
		//setAttr("tradePage", Trade.dao.paginate(getParaToInt(0, 1), 30, "select u.*,r.name as role_name", "from user u left join role r on u.role_id=r.id order by u.id desc"));
		render("view.html");
	}

	public void update() {
		int id = getParaToInt("trade.id");
		String final_proof = getPara("final_proof");
		String transfer_screenshots = getPara("transfer_screenshots");
		
		//自定义发布日期（没填写默认当前日期）
		Date customDate = null ;
		String customTime = getPara("trade.custom_time");
		if(customTime==null || customTime.trim().equals("")){
			customDate = new Date();
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			try {
				customDate = sdf.parse(customTime);
			} catch (java.text.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		boolean succeed = getModel(Trade.class).set("custom_time", customDate).update();
		int primary_id = id;
		if(!final_proof.equals("")) {
			Db.update("delete from general_att where primary_id=? and column_name='final_proof'",primary_id);
			String[] arr_final_proof = final_proof.split(",");
			int i = 1;
			for(String f:arr_final_proof) {
				Db.update("insert into general_att (table_name,column_name,primary_id,name,url,priority) values ('trade','final_proof',?,?,?,?)",
						primary_id,f.substring(f.lastIndexOf("/")+1),f,i);
				i++;
			}
		}
		if(!transfer_screenshots.equals("")) {
			Db.update("delete from general_att where primary_id=? and column_name='transfer_screenshots'",primary_id);
			String[] arr_transfer_screenshots = transfer_screenshots.split(",");
			int j = 1;
			for(String f:arr_transfer_screenshots) {
				Db.update("insert into general_att (table_name,column_name,primary_id,name,url,priority) values ('trade','transfer_screenshots',?,?,?,?)",
						primary_id,f.substring(f.lastIndexOf("/")+1),f,j);
				j++;
			}
		}
		
		if(succeed){
			setAttr("resultMsg", "数据保存成功。");
			forwardAction("/admin/trade");
		}
		else{
			setAttr("resultMsg", "数据保存失败，请重新录入。");
			forwardAction("/admin/trade/edit/"+id);
		}
	}
	
	public void delete() {
		final Integer[] ids = getParaValuesToInt("ids");
		if(ids!=null && ids.length>0){
			boolean succeed = Db.tx(
				new IAtom(){
					public boolean run() throws SQLException {
						String strId = "";
						for(Integer id:ids){
							strId += id+",";
							DelPic(id);
						}
						strId = strId.substring(0, strId.length()-1);
						int j = Db.update("delete from trade where id in ("+strId+")");
						return j>0 ;
					}
				}
			);
			if(succeed){
				setAttr("resultMsg", "数据删除成功。");
			}
			else{
				if(!"0".equals(getAttr("result")))
					setAttr("resultMsg", "数据删除失败，请重试或联系管理员。");
			}
		}
		else{
			if(getParaToInt()>0){
				boolean succeed = Db.tx(
					new IAtom(){
						public boolean run() throws SQLException {
							DelPic(getParaToInt());
							Db.update("delete from trade where id in ("+getParaToInt()+")");
							boolean boogoods = true;
							return boogoods ;
						}
					}
				);
				if(succeed){
					setAttr("resultMsg", "数据删除成功。");
				}
				else{
					if(!"0".equals(getAttr("result")))
						setAttr("resultMsg", "数据删除失败，请重试或联系管理员。");
				}
			}
		}
		forwardAction("/admin/trade");
	}
	public void DelPic(int id){
		String realPath = this.getRequest().getRealPath("/");
		List<String> list= Db.query("select url from general_att where primary_id=?", id);
		for(String url:list ) {
			if(!"".equals(url)){
				File f = new File(realPath+url);
				if(f.exists()){
					f.delete();
				}
			}
		}
		Db.update("delete from general_att where primary_id=? and table_name='trade'",id);
	}
}


