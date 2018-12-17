package com.eazytec.bpm.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.activiti.engine.impl.form.FormPropertyHandler;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.OperatingFunction;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.orm.jdo.support.OpenPersistenceManagerInViewInterceptor;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.Constants;
import com.eazytec.bpm.engine.TaskRole;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.exceptions.EazyBpmException;


/**
 * 
 * @author madan
 *
 */
public class OperatingFunctionUtil {
	
	public static ModelMap setOperatingFunctionTypes(ModelMap model){
		
		model.addAttribute("add", OperatingFunction.ADD);
		model.addAttribute("submit",OperatingFunction.SUBMIT);
		model.addAttribute("save",OperatingFunction.SAVE);
		model.addAttribute("returnOperation",OperatingFunction.RETURN);
		model.addAttribute("recall",OperatingFunction.RECALL);
		model.addAttribute("withdraw",OperatingFunction.WITHDRAW);
		model.addAttribute("transfer",OperatingFunction.TRANSFER);
		model.addAttribute("urge",OperatingFunction.URGE);
		model.addAttribute("confluentSignature",OperatingFunction.CONFLUENT_SIGNATURE);
		model.addAttribute("circulatePerusal",OperatingFunction.CIRCULATE_PERUSAL);
		model.addAttribute("jump",OperatingFunction.JUMP);
		model.addAttribute("transactorReplacement",OperatingFunction.TRANSACTOR_REPLACEMENT);
		model.addAttribute("terminate",OperatingFunction.TERMINATE);
		model.addAttribute("suspend",OperatingFunction.SUSPEND);
		
		model.addAttribute("jointConduction",OperatingFunction.JOINT_CONDUCTION);
		return model;
	}
	
}
