package com.eazytec.bpm.runtime.agency.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.bpm.runtime.agency.dao.AgencyDao;
import com.eazytec.bpm.runtime.agency.service.AgencyService;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.AgencySetting;
import com.eazytec.service.impl.GenericManagerImpl;
import com.eazytec.util.DateUtil;

@Service("agencyService")
public class AgencyServiceImpl extends GenericManagerImpl<AgencySetting, String> implements AgencyService {

	
	private AgencyDao agencyDao;
	
	
	@Autowired
	public AgencyServiceImpl(AgencyDao agencyDao) {
		super(agencyDao);
		this.agencyDao = agencyDao;
	}
	
	 @Autowired
	public void setUserDao(AgencyDao agencyDao) {
		this.dao = agencyDao;
		this.agencyDao = agencyDao;
	}
	
	@Override
	public AgencySetting getAgencySettingByUser(String id) {
		return agencyDao.getAgencySettingByUser(id);
	}

	@Override
	public AgencySetting saveOrUpdate(AgencySetting agencySetting,List<String> processList) {
		for(String processId:processList){
			agencySetting.setProcessId(processId);
			agencyDao.save(agencySetting);
		}
		
		return agencySetting;
	}

	public List<AgencySetting> getAgencySettingForUserByProcessId(String processDefinitionId){
		return agencyDao.getAgencySettingForUserByProcessId(processDefinitionId);
	}

	@Override
	public List<String> removeUnassignedAgency()throws EazyBpmException{
		List<String> expiredIdentityLinkIds=new ArrayList<String>();
		Date todayDate = new Date();
		String[] todayDateAndTime=DateUtil.convertDateToString(todayDate).split(" ");
		String todaysDate=todayDateAndTime[0];
		List<String> expiredUsers=agencyDao.getExpiredAgencyUsers(todaysDate);
		if(expiredUsers!=null && !expiredUsers.isEmpty()){
			Set<String> expiredUsersSet=new HashSet<String>();
			for(String expiredUser:expiredUsers){
				String[] expiredUserArray=expiredUser.split(",");
				for(String userId:expiredUserArray){
					expiredUsersSet.add(userId);
				}
			}
			expiredIdentityLinkIds=agencyDao.getExpiredIdentityLinkIds(expiredUsersSet);
			if(expiredIdentityLinkIds!=null && !expiredIdentityLinkIds.isEmpty()){
				agencyDao.deleteExpiredIdentityLinkIds(expiredIdentityLinkIds);
			}
		}
		return expiredIdentityLinkIds;
	}
	
	@Override
	public void removeAgentByIds(List<String> agentIdList)throws EazyBpmException{
		for(String agentId:agentIdList){
			agencyDao.remove(agentId);
		}
	}

}
