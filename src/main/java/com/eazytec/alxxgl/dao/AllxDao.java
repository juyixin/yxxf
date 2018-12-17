/*
 * Powered By [rapid-framework]
 * Web Site: http://www.rapid-framework.org.cn
 * Google Code: http://code.google.com/p/rapid-framework/
 * Since 2008 - 2015
 */

package com.eazytec.alxxgl.dao;

import java.util.List;
import java.util.Map;

import com.eazytec.alxxgl.model.Allx;
import com.eazytec.dao.GenericDao;
import com.eazytec.dto.AllxDTO;
import com.eazytec.exceptions.BpmException;
import com.eazytec.model.Department;
import com.eazytec.model.LabelValue;
import com.eazytec.util.PageBean;
/**
 * @author easybpm
 *
 */
public interface AllxDao extends GenericDao<Allx,String>{

	List<Allx> getAllAllxList(String lxmc);

	Allx saveAllx(Allx allx);

	List<Allx> getAllxsByIds(List<String> allxIds);

	void deleteAllx(Allx allx);

	int getAllAllxCount(PageBean<Allx> pageBean, String num);

	List<Allx> getAllAllx(PageBean<Allx> pageBean, String num, String orderName, String orderType);

	Allx updateAllx(Allx allx);

	List<AllxDTO> getAllAllxDTO()throws BpmException;
	
	List<Allx> getAllAllx()throws BpmException;

	List<Allx> getAllxBySuperDepartmentId(String superDepartment);
	
	List<Allx> getAllxBySuperDepartmentId1(String name);
	
	List<Allx> getAllxBySuperDepartmentId2(String id, String name);

	List<Allx> getAllxTeam3(String superDepartment);

	List<LabelValue> getOrganizationAsLabelValue()throws BpmException;

	List<LabelValue> getAllxsAsLabelValueByParent(String parentDepartment)throws BpmException;

	List<Allx> getAllxByIds(List<String> ids);

	void removeAllx(String lxdm);

	List<Allx> getAllxs();

	Allx getByLxdm(String lxdm);
	
	List<Allx> getAllxBydepartment(String id);
	
	Allx searchlxmc(String lxdm);


}
