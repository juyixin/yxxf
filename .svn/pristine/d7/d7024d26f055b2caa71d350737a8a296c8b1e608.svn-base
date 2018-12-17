package com.eazytec.alxxgl.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eazytec.alxxgl.dao.AllxDao;
import com.eazytec.alxxgl.model.Allx;
import com.eazytec.alxxgl.service.AllxService;
import com.eazytec.bpm.common.util.StringUtil;
import com.eazytec.common.util.CommonUtil;
import com.eazytec.dto.AllxDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.Department;
import com.eazytec.model.LabelValue;
import com.eazytec.model.User;
import com.eazytec.util.PageBean;

@Service("allxService")
public class AllxServiceImpl implements AllxService{

	@Autowired
	private AllxDao allxDao;

 
	@Override
	public List<Allx> getAllxs(String lxmc) {
		return allxDao.getAllAllxList(lxmc);
	}

	@Override
	public PageBean<Allx> getAllxs(PageBean<Allx> pageBean, String num, String orderName, String orderType) {
		int totalrecords =  allxDao.getAllAllxCount(pageBean, num);
		pageBean.setTotalrecords(totalrecords);
		List<Allx> result =  allxDao.getAllAllx(pageBean, num, orderName, orderType);
		pageBean.setResult(result);
		return pageBean;
	}

	@Override
	public Allx saveAllx(Allx allx) {
		Date date = new Date();
		if(StringUtil.isEmptyString(allx.getId())) {
			allx.setCreatedTime(date);
			allx.setLastModifyTime(date);
		} else { 
			allx.setLastModifyTime(date);
		}
		
		return allxDao.saveAllx(allx);
	}

	@Override
	public String removeAllxs(List<String> allxIds) {
	List<Allx> allxList=allxDao.getAllxsByIds(allxIds);
		
		for (Allx allx : allxList) {
			allxDao.deleteAllx(allx);
		}
		return null;
	}

	@Override
	public Allx getAllxById(String id) {
		return allxDao.get(id);
	}
	
	@Override
	public Allx getAllxByLxdm(String lxdm) {
		return allxDao.getByLxdm(lxdm);
	}

	@Override
	public Allx updateAllx(Allx allx) {
		return allxDao.updateAllx(allx);
	}

	@Override
	public List<AllxDTO> getAllAllxDTO() throws BpmException {
		return allxDao.getAllAllxDTO();
	}
	
	@Override
	public List<Allx> getAllAllx() throws BpmException {
		return allxDao.getAllAllx();
	}

	@Override
	public List<Allx> getAllxBySuperDepartmentId(String superDepartment) throws Exception {
    	return allxDao.getAllxBySuperDepartmentId(superDepartment);
  
	}
	
	@Override
	public List<Allx> getAllxBySuperDepartmentId1(String name) throws Exception {
    	return allxDao.getAllxBySuperDepartmentId1(name);
  
	}
	
	@Override
	public List<Allx> getAllxBySuperDepartmentId2(String id, String name) throws Exception {
    	return allxDao.getAllxBySuperDepartmentId2(id,name);
  
	}

	@Override
	public List<Allx> getAllxTeam3(String superDepartment) {
		return allxDao.getAllxTeam3(superDepartment);
	}

	@Override
	public List<LabelValue> getOrganizationAsLabelValue() throws BpmException {
		return allxDao.getOrganizationAsLabelValue();
	}

	@Override
	public List<LabelValue> getAllxsAsLabelValueByParent(String parentDepartment) throws BpmException {
		return allxDao.getAllxsAsLabelValueByParent(parentDepartment);
	}
	
	
	@Override
	public boolean removeDepartments(List<String> departmentIds, boolean isPermitToDelete) throws Exception{
	    System.out.println(departmentIds);	
	    if(isPermitToDelete){
			List<Allx> allxList=allxDao.getAllxByIds(departmentIds);
	    	for (Allx allx : allxList) {
					List<Allx> allxs =null;
						allxs = getAllxBySuperDepartmentId(allx.getLxdm());
						System.out.println(allxs);
						if(allxs!=null){
			    			updateAllxs(allxs,allx.getSuperDepartment());
			    		} 
			    		allxDao.removeAllx(allx.getLxdm());
					 
	    	}
	    }
    	return isPermitToDelete;
	}
	 
	   public void updateAllxs(List<Allx> allxs,String superDepartment){
	    	try {
	    		for(Allx allx : allxs){
	        		if(!superDepartment.isEmpty()){
	        			allx.setSuperDepartment(superDepartment);
	        		}
	        		allxDao.save(allx);
	        	}
			} catch (Exception e) {
				throw new BpmException(""+e.getMessage(),e);
			}
	    }

	@Override
	public List<LabelValue> getAllAllxs() {
		   List<Allx> allxs = allxDao.getAllxs();
	        List<LabelValue> list = new ArrayList<LabelValue>();

	        for (Allx allx : allxs) {
	            list.add(new LabelValue(allx.getLxmc(), allx.getId()));
	        }

	        return list;
	}

	@Override
	public List<Allx> getAllxBydepartment(String id) {
		return allxDao.getAllxBydepartment(id);
	}

	@Override
	public Allx searchlxmc(String lxdm) {
		// TODO Auto-generated method stub
		return allxDao.searchlxmc(lxdm);
	}
}
