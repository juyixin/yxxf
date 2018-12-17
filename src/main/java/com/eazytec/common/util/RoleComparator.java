package com.eazytec.common.util;

import java.util.Comparator;

import com.eazytec.model.Department;
import com.eazytec.model.Role;

/**
 * Compares order no from a role
 * @author mahesh
 */
public class RoleComparator implements Comparator<Role>{
	
	 public int compare ( Role roleFrom , Role roleTo ) {
		 	 Integer orderNoFrom = roleFrom.getOrderNo();
             Integer orderNoTo = roleTo.getOrderNo();
             return orderNoFrom.compareTo ( orderNoTo ) ;
     }
}
