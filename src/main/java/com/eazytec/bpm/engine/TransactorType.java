package com.eazytec.bpm.engine;

import com.eazytec.bpm.engine.task.impl.TransactorIdentityLinkEntity;

/**
 * Contains a predefined set of states for sign off types for a task by transactors.
 * 
 * @author madan
 */
public interface TransactorType {
  
  /**
   * <p>Will be signed off by only one user and once he does that, the task will be completed</p>
   */
  TransactorType SINGLE_PLAYER_SINGLE_SIGNOFF = new TransactorTypeImpl(1, "single-paler-single-signoff");
  
  /**
   * <p>Will have more than one user and task will be completed only if all of them signs off.
   * Sign off will be done only in particular order, like a manager can sign off only if asst. manager
   * signs off.</p>
   */
  TransactorType MULTI_PROCESS_SIGNOFF = new TransactorTypeImpl(2, "multi-process-signoff");
  
  /**
   * <p>Will have more than one user and task will be completed only if all of them signs off.
   * But they can sign off in any order</p>
   */
  TransactorType MULTI_SEQUENCE_SIGNOFF = new TransactorTypeImpl(3, "multi-sequence-signoff");
  
  /**
   * <p>Task will have more than one user to sign off but if any one of the user signs off, the task will
   * be completed. Others no need to get the task for sign off.</p>
   */
  TransactorType MULTI_PLAYER_SINGLE_SIGNOFF = new TransactorTypeImpl(4, "multi-player-single-signoff");
  
  int getStateCode();
  
  String getStateCodeStr();
  
  ///////////////////////////////////////////////////// default implementation 
  
  static class TransactorTypeImpl implements TransactorType {

    public final int stateCode;
    protected final String name;   

    public TransactorTypeImpl(int suspensionCode, String string) {
      this.stateCode = suspensionCode;
      this.name = string;
    }    
   
    public int getStateCode() {     
      return stateCode;
    }
    
    public String getStateCodeStr() {     
        return String.valueOf(stateCode);
      }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + stateCode;
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
      TransactorTypeImpl other = (TransactorTypeImpl) obj;
      if (stateCode != other.stateCode)
        return false;
      return true;
    }
    
    @Override
    public String toString() {
      return name;
    }
  }
  
  /////////////////////////////////////////// helper class
  
  public static class TransactorTypeUtil{
    
    public static void setSuspensionState(TransactorIdentityLinkEntity transactorIdentity, TransactorType state) {
    	transactorIdentity.setType(state.getStateCode());
    } 
    
  }
  
}
