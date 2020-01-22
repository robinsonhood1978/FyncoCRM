package com.cms.admin.dict;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

public class DictValidator extends Validator {
	
	protected void validate(Controller controller) {
		validateRequiredString("dict.name", "nameMsg", "请输入字典名称!");
		validateRequiredString("dict.ename", "enameMsg", "请输入字典英文代码!");
		validateRequiredString("dict.code", "codeMsg", "请输入类别编码!");
		validateRequiredString("dict.fname", "fnameMsg", "请输入类别名称!");
	}
	
	protected void handleError(Controller controller) {
		controller.keepModel(Dict.class);
		
		String actionKey = getActionKey();
		if (actionKey.equals("/dict/save"))
			controller.render("add.html");
		else if (actionKey.equals("/dict/update"))
			controller.render("edit.html");
	}
}
