package com.cms.admin.dept;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

@Before(DeptInterceptor.class)
public class AdminDeptController extends Controller {
	public void index() {
		setAttr("deptPage", Dept.dao.paginate(getParaToInt(0, 1), 10, "select d.*,p.name as pname",
				"from dept d left join dept p on d.pid=p.id order by d.id desc"));
		render("list.html");
	}
	
	public void add() {
		setAttr("deptList", Dept.dao.find("SELECT node.* FROM dept node, dept parent WHERE node.lft >= parent.lft AND node.rgt <= parent.rgt and parent.pid is null ORDER BY node.lft"));
	}
	
	@Before(DeptValidator.class)
	public void save() {
		String name = getPara("dept.name");
		List<Dept> list = Dept.dao.find("select * from dept where name=?",name);
		if(list.size()>0){
			setAttr("nameMsg", "产品目录重名！请更正。");
			forwardAction("/admin/dept/add");
		}
		else{
			boolean succeed = Db.tx(
				new IAtom(){
					public boolean run() throws SQLException {
						boolean count = false;
						boolean boo = false;
						String parentId = getPara("dept.pid");
						if(parentId==null){
							count = getModel(Dept.class).set("lft", 1).set("rgt", 2).set("depth", 0)
							.set("priority", ("".equals(getPara("dept.priority")) || getPara("dept.priority")==null)?1:getPara("dept.priority"))
							.save();
							boo = true;
						}
						else{
							//思路：操作相当于把左侧的NodeId=B之后的所有节点后移2个位置，然后插入新节点Q.
							Dept parentCtg = getModel(Dept.class).findById(parentId);
							int lft = parentCtg.getInt("lft"); 
							int depth = parentCtg.getInt("depth"); 
							int iRgt = Db.update("UPDATE dept SET rgt = rgt + 2 WHERE rgt >?",lft);
							int iLft = Db.update("UPDATE dept SET lft = lft + 2 WHERE lft >?",lft);
							count = getModel(Dept.class).set("lft", lft+1)
							.set("rgt", lft+2).set("depth", depth+1)
							.set("priority", ("".equals(getPara("dept.priority")) || getPara("dept.priority")==null)?1:getPara("dept.priority"))
							.save();
							if(iRgt>0 || iLft>0)
								boo = true;
						}
						return count && boo;
					}
				}
			);
			if(succeed){
				setAttr("resultMsg", "数据保存成功。");
				forwardAction("/admin/dept");
			}
			else{
				setAttr("resultMsg", "数据保存失败，请重新录入。");
				forwardAction("/admin/dept/add");
			}
		}
	}
	
	public void edit() {
		Dept cat = Dept.dao.findById(getParaToInt());
		setAttr("deptList", Dept.dao.find("SELECT node.* FROM dept node, dept parent WHERE node.lft >= parent.lft AND node.rgt <= parent.rgt and parent.pid is null and node.lft not between ? and ? ORDER BY node.lft",cat.getInt("lft"),cat.getInt("rgt")));
		setAttr("dept", cat);
	}
	
	@Before(DeptValidator.class)
	public void update() {
		final String id = getPara("dept.id");
		String name = getPara("dept.name");
		List<Dept> list = Dept.dao.find("select * from dept where name=? and id!=?",name,id);
		if(list.size()>0){
			setAttr("nameMsg", "存在重名！请更正。");
			forwardAction("/admin/dept/edit/"+id);
		}
		else{
			boolean succeed = Db.tx(
				new IAtom(){
					public boolean run() throws SQLException {
						/*以移动D节点到H节点下I节点后为例。思路：操作相当于先为子树腾出空间，
						即移动右侧H跨度=（D.rgt-D.lft+1=6）个位置；然后调整D节点，
						移动偏移量=(H原来的右位置-D.lft=18-4)，然后，删除D移动后产生的空白位置。*/
						
						// 获得节点跨度
						
						Dept cat = Dept.dao.findById(id);
						int span = cat.getInt("rgt") - cat.getInt("lft") + 1;
						//System.out.println("span======="+span+"---------");
						
						// 获得当前父节点右位置
						String parentId = getPara("dept.pid");
						Dept curParentCat = getModel(Dept.class).findById(parentId);
						if(curParentCat==null)return true;
						int curParentRgt = curParentCat.getInt("rgt");
						//System.out.println("curParentRgt======="+curParentRgt+"---------");
						
						// 先空出位置
						
						int i = Db.update("update dept set rgt=rgt+? where rgt>=?",span,curParentRgt);
						int j = Db.update("update dept set lft=lft+? where lft>=?",span,curParentRgt);
						
						// 再调整自己
						cat = Dept.dao.findById(id);
						int offset = curParentRgt - cat.getInt("lft");
						int k = Db.update("update dept set lft=lft+?, rgt=rgt+? WHERE lft between ? and ?",offset,offset,cat.getInt("lft"),cat.getInt("rgt"));
						
						// 最后删除（清空位置）
						int p = Db.update("update dept set rgt=rgt-? where rgt>?",span,cat.getInt("rgt"));
						int q = Db.update("update dept set lft=lft-? where lft>?",span,cat.getInt("rgt"));
						
						boolean boo = getModel(Dept.class).set("depth", curParentCat.getInt("depth")+1).update();
						return boo && (i>0||j>0) && k>0 && (p>0||q>0);
					}
				}
			);
			if(succeed){
				getModel(Dept.class).findById(id)
				.set("name", name)
				.set("priority", ("".equals(getPara("dept.priority")) || getPara("dept.priority")==null)?1:getPara("dept.priority"))
				.set("intro", getPara("dept.intro"))
				.update();
				setAttr("resultMsg", "数据保存成功。");
				forwardAction("/admin/dept");
			}
			else{
				setAttr("resultMsg", "数据保存失败，请重新录入。");
				forwardAction("/admin/dept/edit");
			}
		}
	}
	
	public void delete() {
		final Integer[] ids = getParaValuesToInt("ids");
		if(ids!=null && ids.length>0){
			boolean succeed = Db.tx(
				new IAtom(){
					public boolean run() throws SQLException {
						Arrays.sort(ids);
						for(int i=ids.length-1;i>=0;i--){
							if(!delOne(ids[i])){
								return false;
							}
						}
						return true;
					}
				}
			);
			if(succeed){
				setAttr("resultMsg", "数据删除成功。");
			}
			else{
				if(!"1".equals(getAttr("status")) && !"2".equals(getAttr("status")))
					setAttr("resultMsg", "数据删除失败，请重试。");
			}
		}
		else{
			if(getParaToInt()>0){
				boolean succeed = Db.tx(
					new IAtom(){
						public boolean run() throws SQLException {
							return delOne(getParaToInt());
						}
					}
				);
				if(succeed){
					setAttr("resultMsg", "数据删除成功。");
				}
				else{
					if(!"1".equals(getAttr("status")) && !"2".equals(getAttr("status")))
						setAttr("resultMsg", "数据删除失败，请重试。");
				}
			}
		}
		forwardAction("/admin/dept");
	}
	private boolean delOne(int catId){
		//思路：操作先删D的所有子节点和自己；然后更新左右位置。
		Dept cat = Dept.dao.findById(catId);
		List<Integer> idList = Db.query("select id from dept where lft between ? and ?",cat.getInt("lft"),cat.getInt("rgt"));
		String strId = "";
		for(Integer id:idList){
			strId += id+",";
		}
		strId = strId.substring(0, strId.length()-1);
		Long num = Db.queryLong("select count(*) from user where company_id in ("+strId+")"); 
		
		if(num>0){
			setAttr("resultMsg", "此机构或其子机构下有人员存在，删除失败，请删除人员关联后重试。");
			setAttr("status","1");
			return false;
		}
		else{
			Long num2 = Db.queryLong("select count(*) from dept where pid = ?",cat.getInt("id"));
			if(num2>0){
				setAttr("resultMsg", "此机构包含下级机构，删除失败，请删除下级机构后重试。");
				setAttr("status","2");
				return false;
			}
		}
		
		int width = cat.getInt("rgt")-cat.getInt("lft")+1;
//		int i = Db.update("delete from dept where lft between ? and ?",cat.getInt("lft"),cat.getInt("rgt"));
		int i = Db.update("delete from dept where id = ?",catId);//delete specify company dannel modify
		int j = Db.update("update dept set rgt=rgt-? where rgt>?",width,cat.getInt("rgt"));
		int k = Db.update("update dept set lft=lft-? where lft>?",width,cat.getInt("rgt"));
		return i>0 ;
	}
}


