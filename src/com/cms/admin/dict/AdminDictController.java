package com.cms.admin.dict;

import java.util.List;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

@Before(DictInterceptor.class)
public class AdminDictController extends Controller {
	public void index() {
		setAttr("dictPage", Dict.dao.paginate(getParaToInt(0, 1), 10, "select *", 
				"from dict_base order by id desc"));
		render("base_list.html");
	}
	public void son() {
		int pageNo = getParaToInt(0, 1);
		int baseId = getParaToInt(1); 
		setAttr("baseId",baseId);
		setAttr("sonPage", Dict.dao.paginate(pageNo, 10, "select d.*", 
				"from dict d,dict_base b where d.fname=b.name and d.code=b.code and " +
				"b.id="+baseId+" order by d.id desc"));
		render("list.html");
	}
	
	public void add() {
		setAttr("base",DictBase.dao.findById(getPara()));
	}
	public void addbase() {
		
	}
	
	@Before(DictValidator.class)
	public void save() {
		String baseId = getPara("base.id");
		String name = getPara("dict.name");
		String ename = getPara("dict.ename");
		List<Dict> list = Dict.dao.find("select * from dict where name=? and code=?",name,getPara("dict.code"));
		if(list.size()>0){
			setAttr("nameMsg", "字典名称在此类别名称下已存在！请更正。");
			forwardAction("/admin/dict/add/"+baseId);
		}
		else if(Dict.dao.find("select * from dict where ename=? and code=?",ename,getPara("dict.code")).size()>0){
			setAttr("enameMsg", "字典英文代码在此类别名称下已存在！请更正。");
			forwardAction("/admin/dict/add/"+baseId);
		}
		else{
			getModel(Dict.class).save();
			setAttr("resultMsg", "数据字典保存成功。");
			forwardAction("/admin/dict/son/1-"+baseId);
		}
	}
	public void savebase() {
		String name = getPara("dictbase.name");
		String code = getPara("dictbase.code");
		List<Dict> list = Dict.dao.find("select * from dict where name=? and code=?",name,code);
		if(list.size()>0){
			setAttr("nameMsg", "字典名称已存在！请更正。");
			forwardAction("/admin/dict/addbase");
		}
		else{
			Db.update("insert into dict_base (name,code) values (?,?)",name,code);
			setAttr("resultMsg", "数据字典保存成功。");
			forwardAction("/admin/dict");
		}
	}
	
	public void edit() {
		setAttr("dict", Dict.dao.findById(getParaToInt(0,1)));
		setAttr("base",DictBase.dao.findById(getPara(1)));
	}
	public void editbase() {
		setAttr("dictbase", DictBase.dao.findById(getParaToInt()));
	}
	
	@Before(DictValidator.class)
	public void update() {
		String baseId = getPara("base.id");
		String id = getPara("dict.id");
		String name = getPara("dict.name");
		String ename = getPara("dict.ename");
		String code = getPara("base.code");
		List<Dict> list = Dict.dao.find("select * from dict where name=? and code=? and id!=?",
				name,code,id);
		if(list.size()>0){
			setAttr("nameMsg", "字典名称在此类别名称下已存在！请更正。");
			forwardAction("/admin/dict/edit/"+id+"-"+baseId);
		}
		else if(Dict.dao.find("select * from dict where ename=? and code=? and id!=?",
				ename,code,id).size()>0){
			setAttr("enameMsg", "字典英文代码在此类别名称下已存在！请更正。");
			forwardAction("/admin/dict/edit/"+id+"-"+baseId);
		}
		else{
			getModel(Dict.class).update();
			setAttr("resultMsg", "数据字典编辑成功。");
			forwardAction("/admin/dict/son/1-"+baseId);
		}
	}
	public void updatebase() {
		String id = getPara("dictbase.id");
		String name = getPara("dictbase.name");
		String code = getPara("dictbase.code");
		List<DictBase> list = DictBase.dao.find("select * from dict_base where name=? and code=? and id!=?",name,code,id);
		if(list.size()>0){
			setAttr("nameMsg", "字典名称已存在！请更正。");
			forwardAction("/admin/dict/edit/"+id);
		}
		else{
			Db.update("update dict_base set name=? ,code=? where id=?",name,code,id);
			setAttr("resultMsg", "数据字典编辑成功。");
			forwardAction("/admin/dict");
		}
	}
	
	public void delete() {
		int baseId = getParaToInt(1);
		Integer[] ids = getParaValuesToInt("ids");
		String strId = "";
		if(ids!=null && ids.length>0){
			for(Integer id:ids){
				strId += id+",";
			}
			strId = strId.substring(0, strId.length()-1);
			int i = Db.update("delete from dict where id in ("+strId+")");
		}
		else{
			if(getParaToInt(0)>0)
				Dict.dao.deleteById(getParaToInt(0));
		}
		setAttr("resultMsg", "数据字典删除成功。");
		forwardAction("/admin/dict/son/1-"+baseId);
	}
	public void deletebase() {
		Integer[] ids = getParaValuesToInt("ids");
		String strId = "";
		if(ids!=null && ids.length>0){
			for(Integer id:ids){
				strId += id+",";
			}
			strId = strId.substring(0, strId.length()-1);
			int i = Db.update("delete from dict_base where id in ("+strId+")");
		}
		else{
			if(getParaToInt()>0)
				DictBase.dao.deleteById(getParaToInt());
		}
		setAttr("resultMsg", "数据字典删除成功。");
		forwardAction("/admin/dict");
	}
}


