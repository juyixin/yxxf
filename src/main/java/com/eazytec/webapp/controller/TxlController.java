package com.eazytec.webapp.controller;


import java.util.ArrayList;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.activiti.engine.impl.util.json.JSONObject;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.eazytec.Constants;
import com.eazytec.bpm.admin.role.service.RoleService;
import com.eazytec.bpm.admin.txl.TxlService;
import com.eazytec.bpm.admin.txlry.TxlryService;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.common.util.GridUtil;
import com.eazytec.model.NoticeDocument;
import com.eazytec.model.NoticePlat;
import com.eazytec.model.Txl;
import com.eazytec.model.Txlry;
import com.eazytec.model.User;

@Controller
public class TxlController extends BaseFormController {
	public VelocityEngine velocityEngine;
	
	private RoleService roleService;
	
	private TxlService txlService;
	
	private TxlryService txlryService;

	@Autowired
	public void setTxlryService(TxlryService txlryService) {
		this.txlryService = txlryService;
	}

	@Autowired
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	@Autowired
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	
	@Autowired
	public void setTxlService(TxlService txlService) {
		this.txlService = txlService;
	}
	
	
	
	@RequestMapping(value = "bpm/admin/txl", method = RequestMethod.GET)
	public ModelAndView unitUnion(HttpServletRequest request, ModelMap model,String code) {
		String script = null;
		Locale locale = request.getLocale();
		//初始化给默认父节点编号
		String pd = "Organization";
		try {
			List<Txl> unitUnionList = txlService.getAllTxl();
			model.addAttribute(Constants.UNITUNION_LIST, unitUnionList);
			model.addAttribute("pd", pd);
			String[] fieldNames = { "id", "bm", "sjbmdm", "bmdm","bz"};
			script = GridUtil.generateScriptForUnitUnionGrid(CommonUtil.getMapListFromObjectListByFieldNames(unitUnionList, fieldNames, ""), velocityEngine);
			model.addAttribute("script", script);
			model.addAttribute("unitUnionList", unitUnionList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ModelAndView("admin/Txl", model);
	}
	
	
	
	@RequestMapping(value = "bpm/admin/getTxlGrid", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public @ResponseBody String getUnitUnionGrid(@RequestParam("id") String id, @RequestParam("parentNode") String parentNode, HttpServletRequest request, ModelMap model) {
		Locale locale = request.getLocale();
		String script = null;
		try {
			List<Txl> unitUnionList = txlService.getSjCode(id);
			String[] fieldNames = { "id", "bm", "sjbmdm", "bmdm","bz"};
			script = GridUtil.generateScriptForUnitUnionGrid(CommonUtil.getMapListFromObjectListByFieldNames(unitUnionList, fieldNames, ""), velocityEngine);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			saveError(request, getText("error.unitUnionMessage.delete", e.getMessage(), locale));
		}
		return script;
	}
	
	
	@RequestMapping(value = "bpm/admin/addTxl", method = RequestMethod.GET)
	public ModelAndView createUnitUnion(HttpServletRequest request, ModelMap model,@ModelAttribute("txlFrom") Txl txlFrom) {
	    String id = request.getParameter("id");
	    String code= "";
		try {
			if(id==null||id==""){
				 saveError(request, "请选择树节点新建");
				 
				 return unitUnion(request,model,code); 
			}
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Txl> unit= txlService.getBm(id); 
			
				model.addAttribute("bm", unit.get(0).getBm());
				model.addAttribute("bmdm", unit.get(0).getBmdm());
				model.addAttribute("sjbmdm", unit.get(0).getSjbmdm()); 
				model.addAttribute("sjbmdm", unit.get(0).getSjbmdm()); 
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return new ModelAndView("admin/TxlAdd", model);
	}

	

	@RequestMapping(value = "bpm/admin/saveTxlForm", method = RequestMethod.POST)
	public ModelAndView saveUnitUnion(@ModelAttribute("txlFrom") Txl txlFrom, BindingResult errors, ModelMap model, HttpServletRequest request) {
		User logInuser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String parentCode = request.getParameter("bmdmm");
		String bmdmm = request.getParameter("bmdmm");
		String type=bmdmm;
		try {
				
					txlFrom.setId(null);
			    	txlFrom.setCreateTime(new Date());
			    	txlFrom.setType("0");
			    	txlFrom.setSjbmdm(bmdmm);
			    	txlFrom.setBm(txlFrom.getBm());
			    	txlFrom.setBz(txlFrom.getBz());
				
				String code ="";
		    	String sum = "";
		    	String name="";
				List<Txl> listCode= txlService.getTxlTeam(parentCode);
				
				if(parentCode.equals("Organization")){
					type = "bm";
                }
				
		    	int i = 0;
		    	if(null == listCode || listCode.size() ==0 ){
		    		i=0;
		    	}else{
		    		i = listCode.size();
		    	}
		    	int m= i;
		    		int count = 0;
		    		if(i==0){
		    			sum=String.valueOf(i+1);
		    			code=type+"00"+sum;
		    		}else{
		    			while (i > 0) {
		    				i = i / 10;
		    				count++;
		    			}
		    			if(count==1){
		    			   if(i==9){
		    				  code=type+"010";
		    				}else{
		    					int n =1;
		    					for (int k=0;k<m;k++){
		    					name=listCode.get(k).getBmdm();			    				
			    				if(name.substring(name.length()-1).equals(String.valueOf(n))){
			    					n=n+1;
			    					sum=String.valueOf(n);
			    				}else{
			    					 sum=String.valueOf(n);
			    					 break;
			    				}
		    						
			    				}		    				 
		    				  code=type+"00"+sum;
		    				}
		    			}else if(count==2){
		    				if(i==99){
		    				  code=type+"100";
		    				}else{
		    				  sum=String.valueOf(m+1);
			    			  code=type+"0"+sum;
		    				}
		    			}else if(count==3){
		    				 sum=String.valueOf(m+1);
			    			 code=type+sum;
		    			}
		    		}
		    		
		    		txlFrom.setBmdm(code);
		    		
		    		txlFrom = txlService.saveOrUpdateTxlFrom(txlFrom);
					//Getting permissions for delete while editing document form
					
					model.addAttribute("txlFrom",txlFrom);
	
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		
		}
		
		return new ModelAndView("redirect:/bpm/admin/txl");
	}
	
	
	
	@RequestMapping(value = "bpm/admin/cxzczxcz", method = RequestMethod.GET)
	public ModelAndView deleteUnitUnion(String hrjInfoIds, HttpServletRequest request,ModelMap model, HttpServletResponse response) throws Exception {
		    Locale locale = request.getLocale();
		    String code="";
		    User logInuser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    JSONObject jsonResult = new JSONObject();
		    List<String> hrjInfoIdList = new ArrayList<String>();
		    if (hrjInfoIds.contains(",")) {
				  String[] ids = hrjInfoIds.split(",");
				  for(String id :ids){
					  hrjInfoIdList.add(id);
				  }
				} else {
					hrjInfoIdList.add(hrjInfoIds);	
		}
		    for(int i=1 ; i<hrjInfoIdList.size() ; i++){
		    List<Txl> listcodes= txlService.getTxlId(hrjInfoIdList.get(i));
		    	List<Txlry> listCode= txlryService.getTxlryTeam(listcodes.get(i-1).getBmdm());
		    	if(null == listCode || listCode.size() ==0 ){
		    		txlService.delId(hrjInfoIdList);
		    	}else{
		    		 saveError(request, "请先删除部门下的员工 ");
					 
					 return unitUnion(request,model,code);
		    	}	
		    
		   }
             

		    return new ModelAndView("redirect:/bpm/admin/txl");
	}
	
	
	
	@RequestMapping(value = "bpm/admin/txlForm", method = RequestMethod.GET)
	public ModelAndView unitUnionForm(HttpServletRequest request, ModelMap model,String messageId,String personalUnitCode,String ver) {
		try{
			Txl txlFrom=new Txl();
			if(!StringUtil.isEmptyString(request.getParameter("id"))){
				txlFrom=txlService.getTxlById(request.getParameter("id"));
			}
			 
	    	model.addAttribute("txlFrom",txlFrom);
	    	
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			e.printStackTrace();
		}
			return new ModelAndView("admin/Txledit", model);

	}
	
	
	
	@RequestMapping(value = "bpm/admin/saveTxleditForm", method = RequestMethod.POST)
	public ModelAndView savetxledit(@ModelAttribute("txlFrom") Txl txlFrom, BindingResult errors, ModelMap model, HttpServletRequest request) {
		User logInuser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String Id = request.getParameter("id");
		try {
			String  bm=txlFrom.getBm();
			String bz=txlFrom.getBz();
			txlFrom=txlService.getTxlById(Id);
			txlFrom.setBm(bm);	
			txlFrom.setBz(bz);	
			
		    		txlFrom = txlService.saveOrUpdateTxlFrom(txlFrom);
					//Getting permissions for delete while editing document form
					
					model.addAttribute("txlFrom",txlFrom);
	
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		
		}
		
		return new ModelAndView("redirect:/bpm/admin/txl");
	}
	
}

