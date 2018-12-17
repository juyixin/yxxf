package com.eazytec.common.util;

import java.util.Comparator;

import com.eazytec.model.DataDictionary;
import com.eazytec.model.Department;

/**
 * Compares order no from a department
 * @author mahesh
 */
public class DepartmentComparator implements Comparator<Department>{
	
	 public int compare ( Department departmentFrom , Department departmentTo ) {
		 	 Integer orderNoFrom = departmentFrom.getOrderNo();
             Integer orderNoTo = departmentTo.getOrderNo();
             return orderNoFrom.compareTo ( orderNoTo ) ;
     }
}
