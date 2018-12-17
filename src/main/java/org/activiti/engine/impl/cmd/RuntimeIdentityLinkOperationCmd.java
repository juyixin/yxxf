package org.activiti.engine.impl.cmd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.IdentityLinkEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.IdentityLinkType;
import org.activiti.engine.task.OperatingFunction;
import org.springframework.security.core.context.SecurityContextHolder;

import com.eazytec.bpm.engine.TransactorType;
import com.eazytec.exceptions.BpmException;
import com.eazytec.exceptions.EazyBpmException;
import com.eazytec.model.AgencySetting;
import com.eazytec.model.User;


/**
 * @author Joram Barrez
 */
public class RuntimeIdentityLinkOperationCmd extends NeedsActiveTaskCmd<Set<String>> {
  
  private static final long serialVersionUID = 1L;

  protected String userId;
  
  protected String groupId;
  
  protected String operationType;
  
  protected String identityType;
  
  protected String groupType;
  
  protected List<String>userIds;
  
  protected List<Map<String,String>> userDetails;
  
  protected String parentId;
  
  protected int order;
  
  protected List<AgencySetting> agencySettings;
  
  protected String processInstanceId;
  
  public RuntimeIdentityLinkOperationCmd(String taskId, String userId, String groupId, String operationType, String identityType, String groupType, List<AgencySetting> agencySettings) {
	    super(taskId);
	    this.taskId = taskId;
	    this.userId = userId;
	    this.groupId = groupId;
	    this.operationType = operationType;
	    this.identityType = identityType;
	    this.groupType = groupType;
	    this.agencySettings = agencySettings;
	    validateParams(userId, groupId, operationType, taskId);
	  }
  
  public RuntimeIdentityLinkOperationCmd(String taskId, String userId, String groupId, String operationType, String identityType, String groupType,int order, List<AgencySetting> agencySettings) {
	    super(taskId);
	    this.taskId = taskId;
	    this.userId = userId;
	    this.groupId = groupId;
	    this.operationType = operationType;
	    this.identityType = identityType;
	    this.groupType = groupType;
	    this.order=order;
	    this.agencySettings = agencySettings;
	    validateParams(userId, groupId, operationType, taskId);
  }
  
  public RuntimeIdentityLinkOperationCmd(String taskId, List<String> userIds, String groupId, String operationType, String identityType, String groupType, List<AgencySetting> agencySettings) {
	    super(taskId);
	    this.taskId = taskId;
	    this.userIds = userIds;
	    this.groupId = groupId;
	    this.operationType = operationType;
	    this.identityType = identityType;
	    this.groupType = groupType;
	    this.agencySettings = agencySettings;
	    validateParams(userId, groupId, operationType, taskId);
	  }
  
  public RuntimeIdentityLinkOperationCmd(String taskId, List<Map<String,String>> userDetails, String groupId, String operationType, String identityType, String groupType,String functionName, List<AgencySetting> agencySettings,String processInstanceId) {
		 
	    super(taskId);
	    this.taskId = taskId;
	    this.userDetails = userDetails;
	    this.groupId = groupId;
	    this.operationType = operationType;
	    this.identityType = identityType;
	    this.groupType = groupType;
	    this.agencySettings = agencySettings;
	    this.processInstanceId = processInstanceId;
	    validateParams(userId, groupId, operationType, taskId);
  }
  
  protected void validateParams(String userId, String groupId, String operationType, String taskId) {
      if(taskId == null) {
        throw new ActivitiException("taskId is null");
      }
      if (operationType == null) {
        throw new ActivitiException("operation type is required for any operating function");
      }   
      if (userId == null && (userDetails == null||userDetails.size()==0) && (userIds == null||userIds.size()==0) && groupId == null) {
          throw new ActivitiException("userId and groupId cannot both be null");
      }
      if (userId == null && (userIds == null||userIds.size()==0) && (userDetails == null||userDetails.size()==0)) {
          throw new ActivitiException("Provide either a single user id or the user id list");
      }
}
  
  protected Set<String> execute(CommandContext commandContext, TaskEntity task) {
	  Set<String> userInfo = new HashSet<String>();
	  Set<String> alreadyAddedOrganizerIds = new HashSet<String>(); // added to prevent a user to act as organizer if he is already a agent organizer. 
	  //one who sets agent will act as co-ordinator
	  
    if (OperatingFunction.TRANSFER.equals(operationType)) {
   	  User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        identityType = IdentityLinkType.ORGANIZER;
        task.setAssignee(userId);
       
	    int order = 1;
        for(IdentityLinkEntity indentityLink : task.getIdentityLinks()){
        	// restrict transfer to same user
        	if( indentityLink.getType().equals("organizer") && indentityLink.getUserId() != null) {
        			if( userId.contains(indentityLink.getUserId()) ) {
        				throw new EazyBpmException("Cannot transfer to same user");
        			}
        	}
        	 //Deleting the Identity link for the user who is transfer his task
    	    if(!userId.contains(indentityLink.getUserId()) && indentityLink.getType().equals(identityType)){
    	    	order = indentityLink.getOrder();
    		    task.deleteIdentityLink(currentUser.getId(), groupId, identityType, false);
    		    break;
    	    }
        }//end
        // change orginal organizer to co-ordinator if agency user is set
        if(agencySettings == null || agencySettings.isEmpty()) {
        	task.addIdentityLink(userId, groupId, identityType,order, groupType, null);
        } else {
            IdentityLinkEntity identityEntity = null ;
        	for(AgencySetting agencySetting:agencySettings){
        		if(agencySetting.getUserId().equals(userId)){
        			identityEntity = task.addIdentityLink(userId, groupId, IdentityLinkType.COORDINATOR, 1, groupType,null);
      				if(!alreadyAddedOrganizerIds.contains(agencySetting.getAgent())) {
            			task.addIdentityLinkWithParentId(agencySetting.getAgent(), groupId, IdentityLinkType.ORGANIZER, 1, groupType,identityEntity.getId(), null);
	             		alreadyAddedOrganizerIds.add(agencySetting.getAgent()); 
      				}
        		} 
        	}
        	if(identityEntity == null) {
  				if(!alreadyAddedOrganizerIds.contains(userId)) {
  	        		task.addIdentityLink(userId, groupId, IdentityLinkType.ORGANIZER, 1, groupType,null);
  	    			alreadyAddedOrganizerIds.add(userId); 
  				}
        	}
        }
    	
    } else if (OperatingFunction.ADD.equals(operationType)){ 
        // change orginal organizer to co-ordinator if agency user is set
    	 if(agencySettings == null || agencySettings.isEmpty()) {
    		 task.addIdentityLink(userId, groupId, identityType,order, groupType, null);
        } else {
            IdentityLinkEntity identityEntity = null ;
        	for(AgencySetting agencySetting:agencySettings){
        		// if agent is set, then he will acts as co-ordinator
        		if(agencySetting.getUserId().equals(userId)){
        			identityEntity = task.addIdentityLink(userId, groupId, IdentityLinkType.COORDINATOR, order, groupType,null);
      				if(!alreadyAddedOrganizerIds.contains(agencySetting.getAgent())) {
      					task.addIdentityLinkWithParentId(agencySetting.getAgent(), groupId, IdentityLinkType.ORGANIZER, order, groupType,identityEntity.getId(), null);
	             		alreadyAddedOrganizerIds.add(agencySetting.getAgent()); 
      				}
//            		task.setNotificationForAgency(agencySetting, agentIds);
        		} 
        	}
        	if(identityEntity == null) {
        		if(!alreadyAddedOrganizerIds.contains(userId)) {
        			task.addIdentityLink(userId, groupId, identityType , order, groupType,null);
  	    			alreadyAddedOrganizerIds.add(userId);
        		}
        	}
        }
    } else if (OperatingFunction.CONFLUENT_SIGNATURE.equals(operationType)){
        identityType = IdentityLinkType.ORGANIZER;	
        //Deleting the Identity link for old order
	    List<Map<String,String>> oldOrganizerDetails = new ArrayList<Map<String,String>>();
		Map<String,String> oldOrganizerDetail = null;
        for(IdentityLinkEntity indentityLink : task.getIdentityLinks()){
    	    if(indentityLink.getType().equals(identityType)){
    	    	/**checking whether collabrative operator are old organizer also
    	    		if he is we dont need to put identity links 
    	    		because for collabration itself we put links
    	    	*/
    	    	if(!userIds.contains(indentityLink.getUserId())){
        	    	oldOrganizerDetail = new HashMap<String,String>();
        	    	oldOrganizerDetail.put("operationType", indentityLink.getOperationType());
        	    	oldOrganizerDetail.put("userId", indentityLink.getUserId());
        	    	oldOrganizerDetail.put("order",String.valueOf(indentityLink.getOrder()));
        	    	oldOrganizerDetails.add(oldOrganizerDetail);
    	    	} else {
    	    		throw new BpmException("Same users cannot be collaborative users");
    	    	}
    	    	task.deleteIdentityLink(indentityLink.getUserId(), groupId, identityType, false);
    		   
    	    }
        }//end
        
        //Add New Records
        int maximumOrder = 1;
        //All collaborative organisers or in same order becoz there is no priority between them
        if(agencySettings == null || agencySettings.isEmpty()) {
            for(String userId : userIds){
            	task.addIdentityLinkWithOperationType(userId, groupId, identityType, maximumOrder, groupType,operationType,null);
            }
        } else {
            for(String userId : userIds){
                IdentityLinkEntity identityEntity = null ;
             	for(AgencySetting agencySetting:agencySettings){
            		if(agencySetting.getUserId().equals(userId)){
            			identityEntity = task.addIdentityLinkWithOperationType(userId, groupId, IdentityLinkType.COORDINATOR, maximumOrder, groupType,operationType,null);
 	            		if(!alreadyAddedOrganizerIds.contains(userId)) {
 	                		task.addIdentityLinkWithOperationType(agencySetting.getAgent(), groupId, IdentityLinkType.ORGANIZER, maximumOrder, groupType,operationType,identityEntity.getId());
  	             			alreadyAddedOrganizerIds.add(agencySetting.getAgent()); 
 	            		}
            		} 
            	}
             	if(identityEntity == null) {
	            		if(!alreadyAddedOrganizerIds.contains(userId)) {
	            			identityEntity = task.addIdentityLinkWithOperationType(userId, groupId, IdentityLinkType.ORGANIZER, maximumOrder, groupType,operationType,null);
	            			alreadyAddedOrganizerIds.add(userId); //to prevent duplicate entry in identity link table

	            		}
             	}
            }
        }
        
	    if(task.getSignOffType() == TransactorType.MULTI_PLAYER_SINGLE_SIGNOFF
				.getStateCode()){
	        maximumOrder += 1;
	        //updating old organiser links with new order
	        for(Map<String,String> userDetail : oldOrganizerDetails){
	        	task.addIdentityLink(userDetail.get("userId"), groupId, identityType, maximumOrder, null, null);
	        }
	    }else {
	        //sort the map list by order for multi sequence task only
	    	if(task.getSignOffType() == TransactorType.MULTI_SEQUENCE_SIGNOFF
					.getStateCode()){
		        if(oldOrganizerDetails.size() >1){
		            Collections.sort(oldOrganizerDetails, new Comparator<Map<String, String>>() {
		                public int compare(final Map<String, String> map1, final Map<String, String> map2) {
		                    return map1.get("order").compareTo(map2.get("order"));
		                }
		            });
		        }
	    	}
	    	
	    	int oldOrder = 0;
	        //updating old organiser links with new order
	        for(Map<String,String> userDetail : oldOrganizerDetails){
	        	if(Integer.parseInt(userDetail.get("order")) != oldOrder){
	        		maximumOrder += 1;
	        	}
	        	task.addIdentityLinkWithOperationType(userDetail.get("userId"), groupId, identityType, maximumOrder, null, userDetail.get("operationType") , null);
	        	//for agency setting user we have to put same order
	        	oldOrder = Integer.parseInt(userDetail.get("order"));
	        }
	    }
    } else if (OperatingFunction.CIRCULATE_PERUSAL.equals(operationType)){
    	identityType = IdentityLinkType.READER;
        for(String userId : userIds){
        	task.addIdentityLink(userId, groupId, identityType, groupType);
         	/*for(AgencySetting agencySetting:agencySettings){
        		if(agencySetting.getUserId().equals(userId)){
        			String[] agentIds = agencySetting.getAgent().split(","); 
            		for(String agentId : agentIds){
            		 task.addIdentityLinkWithParentId(agentId, groupId, identityType, 1, groupType,idEntity.getId(), null);
            		}
        		}
        	}*/
        }
    } else if (OperatingFunction.TRANSACTOR_REPLACEMENT.equals(operationType)){
    	identityType = IdentityLinkType.ORGANIZER;
    	task.setAssignee(userId);
    	User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			for (IdentityLinkEntity indentityLink : task.getIdentityLinks()) {
				// restrict transfer to same user
		    	if(userId.contains(indentityLink.getUserId()) && (indentityLink.getType().equals("organizer") || indentityLink.getType().equals("coordinator")) ) {
		    		throw new EazyBpmException("Cannot transfer to same user");
		    	}
		    	//Deleting the Identity link for all previous organizer
				if (indentityLink.getType().equals(identityType) && !userId.contains(indentityLink.getUserId())) {
					task.deleteIdentityLink(currentUser.getId(), groupId,identityType, false);
				} 
			} 
	 	    	
    	//end
    	 int order = 1;
         // change orginal organizer to co-ordinator if agency user is set
    		 String[] users = userId.split(",");
        	 for(String user : users){
            	 IdentityLinkEntity idEntity = null ;
     	             if(agencySettings == null || agencySettings.isEmpty()) {
         	    		 idEntity = task.addIdentityLink(user, groupId, identityType, order ,groupType, null);
     	             } else {
     	             	for(AgencySetting agencySetting:agencySettings){
     	             		idEntity = null;
     	             		if(agencySetting.getUserId().equals(user)){
     	             			//changing original organizer to coordinator if agent user is set
     	             			idEntity = task.addIdentityLink(user, groupId, IdentityLinkType.COORDINATOR, order, groupType,null);
     	             				if(!alreadyAddedOrganizerIds.contains(agencySetting.getAgent())) {
     	     	             			task.addIdentityLinkWithParentId(agencySetting.getAgent(), groupId, IdentityLinkType.ORGANIZER, order, groupType,idEntity.getId(), null);
     	     	             			alreadyAddedOrganizerIds.add(agencySetting.getAgent()); 
     	             				}

     	             		} 
     	             	}
     	             	if(idEntity == null) {
     	            		if(!alreadyAddedOrganizerIds.contains(user)) {
     	             			idEntity = task.addIdentityLink(user, groupId, IdentityLinkType.ORGANIZER, order, groupType,null);
     	            			alreadyAddedOrganizerIds.add(user); //to prevent duplicate entry in identity link table
     	            		}	
     	             	}
     	             } 
     	    		 order += 1;
        	 }
	    	/*for(AgencySetting agencySetting:agencySettings){
	    		if(agencySetting.getUserId().equals(userId)){
	    			String[] agentIds = agencySetting.getAgent().split(","); 
	        		for(String agentId : agentIds){
	        		 task.addIdentityLinkWithParentId(agentId, groupId, IdentityLinkType.COORDINATOR, 1, groupType,idEntity.getId(), null);
	        		}
	    		}
	    	}*/

    }else if (OperatingFunction.ADDORDER.equals(operationType)) {
        identityType = IdentityLinkType.ORGANIZER;
        /*for(IdentityLinkEntity indentityLink : task.getIdentityLinks()){
			if(indentityLink.getType().equals(identityType)){
				 task.deleteIdentityLink(indentityLink.getUserId(), groupId, identityType); 
	 	    }
        }*/
        for(Map<String,String> userDetail : userDetails){
           IdentityLinkEntity idEntity = null ;
        	userInfo.add(userDetail.get("userId"));
            if(agencySettings == null || agencySettings.isEmpty()) {
            	task.addIdentityLink(userDetail.get("userId"), groupId, identityType, Integer.parseInt(userDetail.get("order")), groupType, processInstanceId);
            } else {
    			//changing original organizer to coordinator if agent user is set
            	for(AgencySetting agencySetting:agencySettings){
            		if(agencySetting.getUserId().equals(userDetail.get("userId"))){
            			idEntity = task.addIdentityLink(userDetail.get("userId"), groupId, IdentityLinkType.COORDINATOR, Integer.parseInt(userDetail.get("order")), groupType,null);
            			if(!alreadyAddedOrganizerIds.contains(agencySetting.getAgent())) {
                			task.addIdentityLinkWithParentId(agencySetting.getAgent(), groupId, IdentityLinkType.ORGANIZER, Integer.parseInt(userDetail.get("order")), groupType,idEntity.getId(), null);
                			alreadyAddedOrganizerIds.add(agencySetting.getAgent()); 
            			}
            		} 
            	} 
            	// 
            	if(idEntity == null) {
            		if(!alreadyAddedOrganizerIds.contains(userDetail.get("userId"))) {
            			task.addIdentityLink(userDetail.get("userId"), groupId, IdentityLinkType.ORGANIZER, Integer.parseInt(userDetail.get("order")), groupType,null);
            			alreadyAddedOrganizerIds.add(userDetail.get("userId")); //to prevent duplicate entry in identity link table
            		}
            	}
            }
        	// Added superior subordinate functionality
        	 /*if(userDetail.get("superior") != null){
        		 task.addIdentityLinkWithParentId(userDetail.get("superior"), groupId, identityType, Integer.parseInt(userDetail.get("order")), groupType,idEntity.getId());
        	 }
        	 if(userDetail.get("subordinate") != null){
        		 task.addIdentityLinkWithParentId(userDetail.get("subordinate"), groupId, identityType, Integer.parseInt(userDetail.get("order")), groupType,idEntity.getId());
        	 }*/
        }
    }/*else if (OperatingFunction.REFERENCE.equals(operationType)){
    	identityType = IdentityLinkType.ORGANIZER;
    	
    	task.addIdentityLinkWithParentId(userId, groupId, identityType, 1, groupType,parentId);
    }*/
    commandContext.getHistoryManager().createIdentityLinkComment(taskId, userId, groupId, identityType, true);
    return userInfo;  
  }
  
}

class ValueComparator implements Comparator<String> {

    Map<String, Integer> base;
    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
