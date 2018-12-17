 
package com.eazytec.alxxgl.service;

import java.util.Collection;
import java.util.List;

import javax.jws.WebService;

import com.eazytec.alxxgl.model.Allx;
import com.eazytec.dto.AllxDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.Department;
import com.eazytec.model.LabelValue;
import com.eazytec.model.User;
import com.eazytec.util.PageBean;
/**
 * @author easybpm
 *
 */

@WebService
public interface AllxService{
	List<Allx> getAllxs(String lxmc);
	
	PageBean<Allx> getAllxs(PageBean<Allx> pageBean,String num,String orderName,String orderType);

	Allx saveAllx(Allx allx);

	String removeAllxs(List<String> allxIdList);

	Allx getAllxById(String id);

	Allx getAllxByLxdm(String lxdm);

	Allx updateAllx(Allx allx);

	List<AllxDTO> getAllAllxDTO() throws BpmException;
	
	List<Allx> getAllAllx() throws BpmException;

	List<Allx> getAllxBySuperDepartmentId(String superDepartment)throws Exception;
	 
	List<Allx> getAllxBySuperDepartmentId1(String name)throws Exception;
	
	List<Allx> getAllxBySuperDepartmentId2(String id ,String name)throws Exception;

	List<Allx> getAllxTeam3(String superDepartment);

	List<LabelValue> getOrganizationAsLabelValue()throws BpmException;

	List<LabelValue> getAllxsAsLabelValueByParent(String parentDepartment)throws BpmException;

	boolean removeDepartments(List<String> departmentIds, boolean isPermitToDelete)throws Exception;

	public List<LabelValue> getAllAllxs();
	
	List<Allx> getAllxBydepartment(String id);
	
	Allx searchlxmc(String lxdm);
    
}
