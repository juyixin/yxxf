package com.eazytec.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eazytec.dao.LookupDao;
import com.eazytec.model.AgencySetting;
import com.eazytec.model.Classification;
import com.eazytec.model.Department;
import com.eazytec.model.Group;
import com.eazytec.model.LabelValue;
import com.eazytec.model.Menu;
import com.eazytec.model.Module;
import com.eazytec.model.Role;
import com.eazytec.model.SysConfig;
import com.eazytec.model.TaskRole;
import com.eazytec.model.User;
import com.eazytec.service.LookupManager;

/**
 * Implementation of LookupManager interface to talk to the persistence layer.
 *
 * @author madan
 */
@Service("lookupManager")
public class LookupManagerImpl implements LookupManager {
    @Autowired
    LookupDao dao;

    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllRoles() {
        List<Role> roles = dao.getRoles();
        List<LabelValue> list = new ArrayList<LabelValue>();

        for (Role role1 : roles) {
        	if(!role1.getName().equalsIgnoreCase("ROLE_DEFAULT")){
        		list.add(new LabelValue(role1.getViewName(), role1.getId()));
        	}
        }

        return list;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllGroups() {
        List<Group> groups = dao.getGroups();
        List<LabelValue> list = new ArrayList<LabelValue>();

        for (Group group1 : groups) {
            list.add(new LabelValue(group1.getViewName(), group1.getId()));
        }

        return list;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllDepartments() {
        List<Department> departments = dao.getDepartments();
        List<LabelValue> list = new ArrayList<LabelValue>();

        for (Department department : departments) {
            list.add(new LabelValue(department.getViewName(), department.getId()));
        }

        return list;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllUsers() {
        List<User> users = dao.getUsers();
        List<LabelValue> list = new ArrayList<LabelValue>();

        for (User user : users) {
            list.add(new LabelValue(user.getFullName(), user.getId()));
        }

        return list;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllTaskRoles() {
        List<TaskRole> taskRoles = dao.getTaskRoles();
        List<LabelValue> list = new ArrayList<LabelValue>();

        for (TaskRole taskRole : taskRoles) {
            list.add(new LabelValue(taskRole.getName(), taskRole.getId()));
        }

        return list;
    }
    
    public List<LabelValue> getTaskRolesApplicableForRuntime() {
        List<com.eazytec.bpm.engine.TaskRole> taskRoles = new ArrayList<com.eazytec.bpm.engine.TaskRole>();
        taskRoles.add(com.eazytec.bpm.engine.TaskRole.ORGANIZER);
        taskRoles.add(com.eazytec.bpm.engine.TaskRole.COORDINATOR);
        List<LabelValue> list = new ArrayList<LabelValue>();
        
        for (com.eazytec.bpm.engine.TaskRole taskRole : taskRoles) {
            list.add(new LabelValue(taskRole.getName(), taskRole.getName()));
        }

        return list;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<LabelValue> getAllProcessClassifications() {
        List<Classification> classifications = dao.getProcessClassifications() ;
        List<LabelValue> list = new ArrayList<LabelValue>();

        for (Classification classification : classifications) {
            list.add(new LabelValue(classification.getName(), classification.getId()));
        }

        return list;
    }
    
    /**
     * {@inheritDoc}
     */
    public Map<String,AgencySetting> getAllAgencySetting(){
    	List<AgencySetting> agencyList = dao.getAgencySetting();
    	Map<String,AgencySetting> agensySetting = new HashMap<String,AgencySetting>();
    	for(AgencySetting al : agencyList){
    		agensySetting.put(al.getUserId(), al);
    	}
    	return agensySetting;
    }
    
	public Role getRoleByName(String roleName) {
		Role role = dao.getRoleByName(roleName);
		return role;
	}

    /**
     * {@inheritDoc}
     */
    public List<Menu> getAllMenus() {
        List<Menu> menus = dao.getMenus();
        return menus;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Menu> getAllActiveMenus() {
        List<Menu> menus = dao.getAllActiveMenus();
        return menus;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Menu> getAllTopMenus() {
        List<Menu> menus = dao.getTopMenus();
        return menus;
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Menu> getMenus(List<Role> roles) {
        return dao.getMenus(roles);
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Menu> getGlobalMenus() {
        return dao.getGlobalMenus();
    }
    
    /**
     * {@inheritDoc}
     */
    public List<Module> getModulesByRoles(List<Role> roles) {
    	 return dao.getModulesByRoles(roles);
    }


	public List<String> getAllSysConfig() {
		return dao.getSysConfigs();
	}


	public List<SysConfig> getSysConfigsByType(String configType) {
		return dao.getSysConfigsByType(configType);
	}
	
	public User getUser(String username){
		return dao.getUser(username);
	}
	
	/**
     * {@inheritDoc}
     */
	public void updateTimeZonetoSysconfig(){
		List<SysConfig> sysConfigs = dao.getSysConfigByKey("system.app.timezone.id");
		if(!sysConfigs.isEmpty()){
			SysConfig sysConfig = sysConfigs.get(0);
			sysConfig.setSelectValue(TimeZone.getDefault().getID());
		}
	}
}
