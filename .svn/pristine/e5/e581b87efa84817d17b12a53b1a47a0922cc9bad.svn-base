package org.activiti.engine.impl.cmd;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.form.TaskFormHandler;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.beanutils.BeanUtils;

import com.eazytec.exceptions.BpmException;


/**
 * <p>For describing an entity entirely with all its associations</p>
 * @author madan
 * @author karthick
 */
public class DescribeEntityCmd implements Command<List<Map<String, Object>>>, Serializable {

  private static final long serialVersionUID = 1L;
  
  protected List<?> entities;
  
  public DescribeEntityCmd(List<?>entities) {
    this.entities = entities;
  } 

@Override
public List<Map<String, Object>> execute(CommandContext commandContext) {
	List<Map<String, Object>> describedEntityList = new ArrayList<Map<String, Object>>();
	  for (Object entity : entities) {
		try {
			describedEntityList.add(BeanUtils.describe(entity));
		} catch (Exception e) {
			throw new BpmException("cannot describe entity", e);
		}
	}
	  return describedEntityList;
}
  
}
