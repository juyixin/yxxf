package com.eazytec.bpm.runtime.agency.service;

import java.util.List;

import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.AgencySetting;
import com.eazytec.service.GenericManager;

public interface AgencyService extends GenericManager<AgencySetting, String>  {
	
	AgencySetting saveOrUpdate(AgencySetting agencySetting, List<String> processList);
	
	AgencySetting getAgencySettingByUser(String userid);
	
	List<String> removeUnassignedAgency()throws EazyBpmException;

	List<AgencySetting> getAgencySettingForUserByProcessId(String processDefinitionId);
	
	/**
	 * To remove the agent by ids
	 * @param agentIdList
	 * @throws EazyBpmException
	 */
	void removeAgentByIds(List<String> agentIdList)throws EazyBpmException;

}
