package com.eazytec.dao.hibernate.config;

import org.hibernate.cfg.ImprovedNamingStrategy;

/**
 * <p>
 * The custom Naming Strategy necessary for the BPM with respect to hibernate DB
 * functionalities are included here, for instance, prefixing a global string
 * for all table names
 * </p>
 *
 * @author madan
 */
public class CustomNamingStrategy extends ImprovedNamingStrategy {
	private static final long serialVersionUID = 1L;
	private static final String PREFIX = "ETEC_";

	@Override
	public String classToTableName(final String className) {
		return this.addPrefix(super.classToTableName(className));
	}

	@Override
	public String collectionTableName(final String ownerEntity, final String ownerEntityTable, final String associatedEntity, final String associatedEntityTable, final String propertyName) {
		return this.addPrefix(super.collectionTableName(ownerEntity, ownerEntityTable, associatedEntity, associatedEntityTable, propertyName));
	}

	@Override
	public String logicalCollectionTableName(final String tableName, final String ownerEntityTable, final String associatedEntityTable, final String propertyName) {
		return this.addPrefix(super.logicalCollectionTableName(tableName, ownerEntityTable, associatedEntityTable, propertyName));
	}

	private String addPrefix(final String composedTableName) {

		return PREFIX + composedTableName.toUpperCase();

	}

	@Override
	public String tableName(String tableName) {
		if (tableName.startsWith("crm_")) {
			return tableName;
		}
		return this.addPrefix(tableName).toUpperCase();
	}
}
