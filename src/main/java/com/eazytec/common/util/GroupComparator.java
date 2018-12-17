package com.eazytec.common.util;

import java.util.Comparator;

import com.eazytec.model.Group;

/**
 * Compares order no from a group
 * @author mahesh
 */
public class GroupComparator implements Comparator<Group>{
	
	 public int compare ( Group groupFrom , Group groupTo )
     {
		 	 Integer orderNoFrom = groupFrom.getOrderNo();
             Integer orderNoTo = groupTo.getOrderNo();
             return orderNoFrom.compareTo ( orderNoTo ) ;
     }

}
