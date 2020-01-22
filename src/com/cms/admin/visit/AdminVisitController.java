package com.cms.admin.visit;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;

@Before(VisitInterceptor.class)
public class AdminVisitController extends Controller {
	public void index() {
		setAttr("visitPage", Visits.dao.paginate(getParaToInt(0, 1), 10, "select *", 
				"from visits order by id desc"));
		render("list.html");
	}
	public void ip() {
		setAttr("ipPage", Visits.dao.paginate(getParaToInt(0, 1), 10, "select count(distinct ip) as ip_num,date_format(visit_time,'%Y-%m-%d') thedate", 
				"from visits group by date_format(visit_time,'%Y-%m-%d') order by date_format(visit_time,'%Y-%m-%d') desc"));
		render("ip.html");
	}
}


