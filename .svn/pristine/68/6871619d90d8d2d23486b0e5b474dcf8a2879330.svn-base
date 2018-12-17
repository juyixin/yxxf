package org.activiti.engine.impl.persistence.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.DbSqlSession;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.AbstractManager;
import org.activiti.engine.impl.task.CustomOperatingFunction;


/**
 * @author madan
 */
public class CustomOperatingFunctionManager extends AbstractManager {

  public CustomOperatingFunction createNewOperatingFunction(String id) {
    return new CustomOperatingFunction(id);
  }

  public void insertOperatingFunction(CustomOperatingFunction operatingFunction) {
    getDbSqlSession().insert((PersistentObject) operatingFunction);
  }
  
  public void updateOperatingFunction(UserEntity operatingFunction) {
    CommandContext commandContext = Context.getCommandContext();
    DbSqlSession dbSqlSession = commandContext.getDbSqlSession();
    dbSqlSession.update(operatingFunction);
  }

  public CustomOperatingFunction findOperatingFunctionById(String operatingFunctionId) {
	return (CustomOperatingFunction) getDbSqlSession().selectOne("selectCustomOperationById", operatingFunctionId);
  } 
  
  @SuppressWarnings("unchecked")
  public List<CustomOperatingFunction> selectAllCustomOperations() {
	    return (List<CustomOperatingFunction>) getDbSqlSession().selectList("selectAllCustomOperations");
  } 
  
  @SuppressWarnings("unchecked")
  public List<CustomOperatingFunction> findOperatingFunctionsByIds(List<String>ids) {
	Map<String, Object>params = new HashMap<String, Object>();
	params.put("ids", ids);
    return getDbSqlSession().selectList("selectCustomOperationsByIds", params);
  }
  
  @SuppressWarnings("unchecked")
  public CustomOperatingFunction saveCustomOperatingFunction(CustomOperatingFunction operatingFunction,boolean isCustomOperatingUpdate) {
	  if(isCustomOperatingUpdate){
		  return (CustomOperatingFunction)getDbSqlSession().updateAndReturn((PersistentObject) operatingFunction);
	  }else{
		  return (CustomOperatingFunction)getDbSqlSession().insertAndReturn((PersistentObject) operatingFunction);
	  }
  }
  
  @SuppressWarnings("unchecked")
  public void deleteCustomFunctionById(CustomOperatingFunction operatingFunction) {
	  getDbSqlSession().delete((PersistentObject) operatingFunction);
  }
  
}
