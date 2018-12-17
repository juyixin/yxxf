package com.eazytec.bpm.runtime.agency.dao;

import java.util.List;
import java.util.Set;

import com.eazytec.dao.GenericDao;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.AgencySetting;

public interface AgencyDao extends GenericDao<AgencySetting, String> {
	
	AgencySetting getAgencySettingByUser(String userId);
	
	List<AgencySetting> getAgencySettingForUserByProcessId(String processDefinitionId);

	List<String> getExpiredAgencyUsers(String todaysDate)throws EazyBpmException;
	
	List<String> getExpiredIdentityLinkIds(Set<String> expiredUsersSet)throws EazyBpmException;
	
	void deleteExpiredIdentityLinkIds(List<String> expiredIdentityLinkIds)throws EazyBpmException;

}
