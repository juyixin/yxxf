package com.eazytec.dao.hibernate.config;

import java.sql.Types;

import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.type.Type;

/**
 * Nchar and Nvarchar for chinese character support. 
 * So converting char and nvarchar data types   
 */
public class SQLServerNativeDialect extends SQLServerDialect{
	public SQLServerNativeDialect() {
	    super();
	    try {
			registerColumnType(Types.CHAR, "nchar(1)");
			registerColumnType(Types.VARCHAR, "nvarchar($l)");
			 registerColumnType(Types.CLOB, "ntext" );
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
}
