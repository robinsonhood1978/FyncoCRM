package com.cms.admin.dept;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class DeptValidator extends Validator {
	
	protected void validate(Controller controller) {
		validateRequiredString("dept.name", "nameMsg", "请输入产品目录名称!");
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Dept.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/dept/save"))
			controller.render("add.html");
		else if (actionKey.equals("/dept/update"))
			controller.render("edit.html");
	}
}
