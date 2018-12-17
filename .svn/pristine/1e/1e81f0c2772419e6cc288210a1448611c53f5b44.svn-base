package com.eazytec.bpm.engine;

import com.eazytec.bpm.common.util.StringUtil;


/**
 * Contains a predefined set of states for sign off types for a task by transactors.
 * 
 * @author madan
 */
public interface TaskRole {  
  
  TaskRole ORGANIZER = new TaskRoleImpl(1, "organizer");  
  
  TaskRole COORDINATOR = new TaskRoleImpl(2, "coordinator");
  
  TaskRole READER = new TaskRoleImpl(3, "reader");
  
  TaskRole CREATOR = new TaskRoleImpl(4, "creator");
  
  TaskRole PROCESSED_USER = new TaskRoleImpl(5, "processeduser");
  
  TaskRole WORKFLOW_ADMINISTRATOR = new TaskRoleImpl(6, "workflowadministrator");
  
  TaskRole GET_TASK_ROLE = new TaskRoleImpl();
  
  int getRoleCode();
  
  String getRoleCodeStr();
  
  String getName();
  
  String getRawName();
  
  TaskRole getTaskRoleByName(String name);
  
  ///////////////////////////////////////////////////// default implementation 
  
  public static class TaskRoleImpl implements TaskRole {

    public final int roleCode;
    protected final String name;   

    public TaskRoleImpl() {
        this.roleCode=0;
        this.name=null;
     } 
    
    public TaskRoleImpl(int roleCode, String string) {
      this.roleCode = roleCode;
      this.name = string;
    }  
    
    public TaskRole getTaskRoleByName(String name){
    	if(!StringUtil.isEmptyString(name)){
    		if(name.equalsIgnoreCase("organizer")){
    			return TaskRole.ORGANIZER;
    		}else if(name.equalsIgnoreCase("coordinator")){
    			return TaskRole.COORDINATOR;
    		}else if(name.equalsIgnoreCase("reader")){
    			return TaskRole.READER;
    		}else if(name.equalsIgnoreCase("creator")){
    			return TaskRole.CREATOR;
    		}else if(name.equalsIgnoreCase("processeduser")){
    			return TaskRole.PROCESSED_USER;
    		}else if(name.equalsIgnoreCase("workflowadministrator")){
    			return TaskRole.WORKFLOW_ADMINISTRATOR;
    		}else{
    			return null;
    		}
    	}else{
    		return null;
    	}
    	
    }
   
    public int getRoleCode() {     
      return roleCode;
    }
    
    public String getRoleCodeStr() {     
        return String.valueOf(roleCode);
      }
    
    public String getName() {     
        return name.toLowerCase();
      }
    
    public String getRawName() {    
        return name;
      }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + roleCode;
      return result;
    }
    
    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      TaskRoleImpl other = (TaskRoleImpl) obj;
      if (roleCode != other.roleCode)
        return false;
      return true;
    }
    
    @Override
    public String toString() {
      return name;
    }
  }
  
}
