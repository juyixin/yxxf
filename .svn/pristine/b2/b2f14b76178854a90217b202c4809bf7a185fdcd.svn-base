package com.eazytec.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;


/**
 * Hold functions commonly used for sorting utility functions.
 *  
 * @author mathi
 *
 */

public final class SortHelper {

    /**
     * This method will return the element from the input list with respect to the sort field(s).
     *
     * @param input
     *            input
     * @param sortFileds
     *            sortfield
     *
     * @return Object of the highest order.
     */
    private SortHelper() {
    }

    public static Object getMaxValueObject(final List input,
        final String sortFileds) {
        List result = doSorting(input, sortFileds);

        // the size of the list
        int size = result.size();

        // the element having the max value will be the last,therefore return
        // the last one.
        return result.get(size - 1);
    }

    /**
     * This method will return the element from the input list with respect to the sort field(s).
     *
     * @param input
     *            input
     * @param sortFileds
     *            sortfield
     *
     * @return Object of the lowest order.
     */
    public static Object getMinValueObject(final List input,
        final String sortFileds) {
        return doSorting(input, sortFileds).get(0);
    }

    /**
     * This method will do sorting of the given list ,based on the sort fileds
     * and the result will be stored in another list.
     *
     * @param input
     *            input
     * @param sortFiledNames
     *            the fields based on which the sorting has to be done.If
     *            sorting has to be done based on more than one sort field , in
     *            that case the the srort fields has to be supplied to this
     *            method in comma separated fashion.
     *
     * @return a list containg sorted values in the ascending order.
     */
    public static List doSorting(final List input, final String sortFiledNames) {
        ArrayList sortFields = new ArrayList();
        StringTokenizer tokens = new StringTokenizer(sortFiledNames, ",");

        while (tokens.hasMoreTokens()) {
            String sFiled = tokens.nextElement().toString();
            sortFields.add(new BeanComparator(sFiled));
        }

        ComparatorChain mutiSort = new ComparatorChain(sortFields);
        Collections.sort(input, mutiSort);

        return input;
    }
    
    public static List doSortingInReverse(final List input, final String sortFiledNames) {
        ArrayList sortFields = new ArrayList();
        StringTokenizer tokens = new StringTokenizer(sortFiledNames, ",");

        while (tokens.hasMoreTokens()) {
            String sFiled = tokens.nextElement().toString();
            sortFields.add(new BeanComparator(sFiled));
        }

        ComparatorChain mutiSort = new ComparatorChain(sortFields);
        Collections.sort(input, mutiSort);
        Collections.reverse(input);
        return input;
    }    

    /**
     *
     * @param inputSet
     *           
     * @param sortFiledNames
     *           
     *
     * @return 
     */
    public static List doSorting(final Set inputSet, final String sortFiledNames) {
        return doSorting(new ArrayList(inputSet), sortFiledNames);
    }
}
