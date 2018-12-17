package com.eazytec.bpm.runtime.agency.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.eazytec.bpm.runtime.agency.dao.AgencyDao;
import com.eazytec.dao.hibernate.GenericDaoHibernate;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.AgencySetting;
import com.eazytec.model.Role;

@Repository("agencyDao")
public class AgencyDaoImpl extends GenericDaoHibernate<AgencySetting, String> implements AgencyDao {

	public AgencyDaoImpl() {
		super(AgencySetting.class);
	}
	
	public AgencySetting getAgencySettingByUser(String userId) {
		List agencySettings = getSession().createCriteria(AgencySetting.class).add(Restrictions.eq("userId", userId)).list();
        if (agencySettings.isEmpty()) {
            return null;
        } else {
            return (AgencySetting) agencySettings.get(0);
        }
	}
	
	public List<AgencySetting> getAgencySettingForUserByProcessId(String processDefinitionId){
		List<AgencySetting> agencySettings = new ArrayList<AgencySetting>();
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime( new Date() );
		    calendar.set(Calendar.HOUR_OF_DAY, 0);
		    calendar.set(Calendar.MINUTE, 0);
		    calendar.set(Calendar.SECOND, 0);
		    calendar.set(Calendar.MILLISECOND, 0);
		agencySettings = getSession().createCriteria(AgencySetting.class).add(Restrictions.eq("processId", processDefinitionId))
																				.add(Restrictions.le("startDate",calendar.getTime()))
																			   .add( Restrictions.ge("endDate", calendar.getTime())).list();
        return agencySettings;
	}

	public List<String> getExpiredAgencyUsers(String today)throws EazyBpmException{
		List<String> expiredAgencyUsers=(List<String>)getSession().createQuery("select agencySetting.agent from AgencySetting as agencySetting where agencySetting.endDate < '"+today+"'").list();
		if (expiredAgencyUsers.isEmpty()) {
            return null;
        } else {
            return expiredAgencyUsers;
        }
	}
	
	public List<String> getExpiredIdentityLinkIds(Set<String> expiredUsersSet)throws EazyBpmException{
		String expiredIdentityQuerys="SELECT childLink.id_ FROM ETEC_ACT_RU_IDENTITYLINK AS parentLink,ETEC_ACT_RU_IDENTITYLINK AS childLink WHERE parentLink.USER_ID_ IN (:list) AND parentLink.id_=childLink.PARENT_ID_";
		List<String> expiredAgencyUsers=(List<String>)getSession().createSQLQuery(expiredIdentityQuerys).setParameterList("list", expiredUsersSet).list();
		return expiredAgencyUsers;
	}
	
	public void deleteExpiredIdentityLinkIds(List<String> expiredIdentityLinkIds)throws EazyBpmException{
		Query query = getSession().createSQLQuery("DELETE FROM ETEC_ACT_RU_IDENTITYLINK WHERE id_ IN (:list)").setParameterList("list", expiredIdentityLinkIds);
		int result = query.executeUpdate();
	}
}
