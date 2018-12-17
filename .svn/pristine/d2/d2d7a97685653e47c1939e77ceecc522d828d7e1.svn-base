package com.eazytec.common.util;

import java.util.Comparator;

import com.eazytec.model.ListViewColumns;
import com.eazytec.model.Role;

/**
 * Compares order no from a role
 * @author mahesh
 */
public class ListViewColumnComparator implements Comparator<ListViewColumns>{
	
	 public int compare ( ListViewColumns columnFrom , ListViewColumns columnTo ) {
		 	 Integer orderNoFrom = columnFrom.getOrderNo();
             Integer orderNoTo = columnTo.getOrderNo();
             return orderNoFrom.compareTo ( orderNoTo ) ;
     }
}