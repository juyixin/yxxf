package com.eazytec.bpm.engine.task;

public interface ReferenceRelation {
	
	ReferenceRelation HISTORY_ORGANIZERS_OF_TARGET_NODE =new ReferenceRelationImpl(1,"history-organizers-of-target-node");
	ReferenceRelation CREATOR =new ReferenceRelationImpl(2,"creator");
	ReferenceRelation PEOPLE_OF_THE_SAME_DEPARTMENT =new ReferenceRelationImpl(3,"people-of-the-same-department");
	ReferenceRelation DIRECT_SUPERIOR =new ReferenceRelationImpl(4,"direct-superior");
	ReferenceRelation ALL_SUPERIOR =new ReferenceRelationImpl(5,"all-superior");
	ReferenceRelation DIRECT_SUBORDINATE =new ReferenceRelationImpl(6,"direct-subordinate");
	ReferenceRelation ALL_SUBORDIANTE =new ReferenceRelationImpl(7,"all-subordinate");
	ReferenceRelation LEADERS_IN_CHARGE =new ReferenceRelationImpl(8,"leaders-in-charge");
	ReferenceRelation LEADER_TO_SECRETARY =new ReferenceRelationImpl(9,"leader-to-secretary");
	ReferenceRelation SECRETARY_TO_LEADER =new ReferenceRelationImpl(10,"secretary-to-leader");
	ReferenceRelation PARENT_DEPARTMENT_PERSON_INCLUDING_SUB_DEPARTMENT=new ReferenceRelationImpl(11,"parent-department-person-including-sub-deparment");
	ReferenceRelation PARENT_DEPARTMENT_PERSON_EXCLUDING_SUB_DEPARTMENT =new ReferenceRelationImpl(12,"parent-department-person-excluding-sub-department");
	ReferenceRelation DEPARTMENT_INTREFACE_PEOPLE =new ReferenceRelationImpl(13,"department-interface-people");
	
	
	
	
	int ReferenceRelationCode();
	String ReferenceRelationCodeString();
	
	static class ReferenceRelationImpl implements ReferenceRelation{
		
		public final int referenceRelationCode;
	    protected final String name;
	    
		public int ReferenceRelationCode() {
			return referenceRelationCode;
		}
		public String ReferenceRelationCodeString() {
			return name;
		} 
		
		public ReferenceRelationImpl(){
			this.referenceRelationCode = 0;
			this.name = null;
		}
		
		public ReferenceRelationImpl(int referenceRelationCode, String name) {
		      this.referenceRelationCode = referenceRelationCode;
		      this.name = name;
		    }  
		
	}
	
	

}
