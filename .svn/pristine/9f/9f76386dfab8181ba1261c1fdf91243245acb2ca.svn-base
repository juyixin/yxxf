/*
 *========================================
 * File:      PropertyReader.java
 * Project:   EazyTec BPM
 *
 * Author:    Sangeetha.G
 * Revision:  1.0
 *----------------------------------------
 * Copyright 2013 EazyTec.com
 *========================================
 */
package com.eazytec.util;

import java.util.HashMap;
import java.util.Properties;

/**
 * <p>
 * Maintains the properties from database and property files as the Map(a name-value pair)
 * in the {@link PropertyReader} Object.
 * </p>
 */
public final class PropertyReader {

	// Static variables/initializers

	/**
	 * <p>
	 * An instance of PropertyReader.
	 * </p>
	 */
	private static PropertyReader propertyReader = null;

	private static HashMap<String, Object> mapProperties = null;
	
	// Constructor

	/**
	 * <p>
	 * Default Constructor. 
	 * </p>
	 * 
	 */
	private PropertyReader() {
	}


	// Methods
	/**
	 * <p>
	 * Returns the instance of {@link PropertyReader}.
	 * If instance of {@link PropertyReader} does not exists already 
	 * new instance for {@link PropertyReader} created.
	 * </p>
	 * 
	 * @return PropertyReader {@link PropertyReader} Object
	 */
	public static synchronized PropertyReader getInstance() {

		if ((propertyReader == null)) {
			mapProperties = new HashMap<String, Object>();
			propertyReader = new PropertyReader();
		}
		return propertyReader;
	}

	/**
	 * <p>
	 * Get a particular property from specific category.
	 * </p>
	 * 
	 * @param key
	 *            String value for category
	 * @param propertyName
	 *            String value for property name
	 * 
	 * @return String property value
	 */
	public String getPropertyFromFile(final String key,
			final String propertyName) {
		String property = null;

		if (mapProperties.get(key) != null) {
			Properties properties = (Properties) mapProperties.get(key);
			property = (String) properties.getProperty(propertyName);
		}
		return property;
	}

	/**
	 * <p>
	 * Sets properties into specific category.
	 * </p>
	 * 
	 * @param key
	 *            String category name
	 * @param properties
	 *            Properties object
	 */
	public void setProperties(final String key,
			final Properties properties) {
		mapProperties.put(key, properties);
	}

	/**
	 * <p>
	 * Add a property into specific category.
	 * </p>
	 * 
	 * @param fileKey
	 *            String category name
	 * @param propertyKey
	 *            String property name
	 * @param value
	 *            String - property value
	 */
	public void setProperty(final String fileKey,
			final String propertyKey, final String value) {
		Properties properties = (Properties) mapProperties.get(fileKey);
		if (properties == null) {
			properties = new Properties();
		}
		properties.put(propertyKey, value);
		setProperties(fileKey, properties);
	}

}
