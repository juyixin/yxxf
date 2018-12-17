package com.eazytec.common.util;

import java.util.Comparator;

import com.eazytec.model.Classification;

/**
 * Compares order no from a group
 * 
 * @author mahesh
 */
public class ClassificationComparator implements Comparator<Classification> {

	public int compare(Classification from, Classification to) {
		Integer orderNoFrom = from.getOrderNo();
		Integer orderNoTo = to.getOrderNo();
		return orderNoFrom.compareTo(orderNoTo);
	}

}
