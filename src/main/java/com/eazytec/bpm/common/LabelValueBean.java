/*
 *========================================
 * File:      LabelValueBean.java
 * Project:   Radaptive
 *
 * Author:    Murali and Selva
 * Revision:  1.0
 *----------------------------------------
 * Copyright 2006, 2007 Radaptive Inc.
 *========================================
 */
package com.eazytec.bpm.common;

import java.io.Serializable;

/**
 * <p>
 * A simple JavaBean to represent label-value pairs.<br>
 * This is most commonly used when constructing user interface elements which
 * have a label to be displayed to the user, and a corresponding value to be
 * returned to the server.
 * </p>
 *
 */
public class LabelValueBean implements Serializable {

    // Instance variables
    /**
     * <p>
     * The property which supplies the option label visible to the end user.
     * </p>
     */
    private String label = null;

    /**
     * <p>
     * The property which supplies the value returned to the server.
     * </p>
     */
    private String value = null;
    
    /**
     * <p>
     * The property which supplies the support value returned to the server.
     * </p>
     */
    private String supportValue = null;

    // Constructors
    /**
     * <p>
     * Construct a new LabelValueBean with the specified values.
     * </p>
     *
     * @param label1
     *                The label to be displayed to the user - a String value
     * @param value1
     *                The value to be returned to the server - a int value
     */
    public LabelValueBean(final String label1, final int value1) {
	this.label = label1;
	this.value = value1 + "";
    }

    /**
     * <p>
     * Construct a new LabelValueBean with the specified values.
     * </p>
     *
     * @param label1
     *                The label to be displayed to the user - a String Value
     * @param value1
     *                The value to be returned to the server - a String
     *                Value
     */
    public LabelValueBean(final String label1, final String value1) {
	this.label = label1;
	this.value = value1;
    } 

    /**
     * <p>
     * Construct a new LabelValueBean with the specified values.
     * </p>
     *
     * @param label1
     *                The label to be displayed to the user - a String value
     * @param value1
     *                The value to be returned to the server - a int value
     * @param supportValue1
     *                The support value to be returned to the server - a String value
     */
    public LabelValueBean(final String label1, final int value1, final String supportValue1) {
	this.label = label1;
	this.value = value1 + "";
	this.supportValue = supportValue1;
    } 
    
    /**
     * <p>
     * Construct a new LabelValueBean with the specified values.
     * </p>
     *
     * @param label1
     *                The label to be displayed to the user - a String Value
     * @param value1
     *                The value to be returned to the server - a String Value
     * @param supportValue1
     *                The support value to be returned to the server - a String value
     */
    public LabelValueBean(final String label1, final String value1, final String supportValue1) {
	this.label = label1;
	this.value = value1;
	this.supportValue = supportValue1;
    }
    
    /**
     * <p>
     * Construct a new LabelValueBean with the specified values.
     * </p>
     *
     * @param label1
     *                The label to be displayed to the user - a String Value
     * @param value1
     *                The value to be returned to the server - a String
     *                Value
     * @param supportValue1
     *                The support value to be returned to the server - a int value
     */
    public LabelValueBean(final String label1, final String value1, final int supportValue1) {
	this.label = label1;
	this.value = value1;
	this.supportValue = supportValue1 + "";
    }
    
    // Methods
    /**
     * <p>
     * To get the label to be displayed to the user.
     * </p>
     *
     * @return label - a String value
     */
    public final String getLabel() {
	return (this.label);
    }

    /**
     * <p>
     * To get the value to be returned to the server.
     * </p>
     *
     * @return value - a String value
     */
    public final String getValue() {
	return (this.value);
    }
    
    /**
     * <p>
     * To get the value to be returned to the server.
     * </p>
     *
     * @return value - a String value
     */
    public final String getSupportValue() {
	return (this.supportValue);
    }
}
