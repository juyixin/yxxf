package com.eazytec.common.util;

import java.util.Comparator;
import java.util.HashMap;

public class HashMapComparator implements Comparator {
	
	 public int compare ( Object object1 , Object object2 )
     {
             Integer obj1Value = ( Integer ) ( ( HashMap ) object1 ).get ( "orderBy" );
             Integer obj2Value = ( Integer ) ( ( HashMap ) object2 ).get ( "orderBy" );
             return obj1Value.compareTo ( obj2Value ) ;
     }
}
