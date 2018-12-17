package com.eazytec.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "RE_TABLE_RELATION")
public class MetaTableRelation implements Serializable{

	private static final long serialVersionUID = 5797222355335329016L;
	private String id;
	private MetaTable table;
	private MetaTable parentTable;
	private MetaTable childTable;
	private String foreignKeyColumnId;
	private String foreignKeyColumnName;
	private String childKeyColumnId;
	private String childKeyColumnName;
	
	
	/**
     * Default constructor - creates a new instance with no values set.
     */
    public MetaTableRelation() {
    }
    
	public MetaTableRelation(MetaTable table ,MetaTable parentTable,MetaTable childTable, String foreignKeyColumnId, String foreignKeyColumnName,String childKeyColumnId,String childKeyColumnName){
		this.table=table;
		this.parentTable=parentTable;
		this.childTable=childTable;
		this.foreignKeyColumnId = foreignKeyColumnId;
		this.foreignKeyColumnName = foreignKeyColumnName;
		this.childKeyColumnId = childKeyColumnId;
		this.childKeyColumnName = childKeyColumnName;
	}
    
    @Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}
    
	public void setId(String id) {
		this.id = id;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "TABLE_ID", nullable = false)
    public MetaTable getTable() {
		return table;
	}
	public void setTable(MetaTable table) {
		this.table = table;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "PARENT_TABLE_ID")
	public MetaTable getParentTable() {
		return parentTable;
	}
	public void setParentTable(MetaTable parentTable) {
		this.parentTable = parentTable;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "CHILD_TABLE_ID")
	public MetaTable getChildTable() {
		return childTable;
	}
	
	public void setChildTable(MetaTable childTable) {
		this.childTable = childTable;
	}
	
	@Column(name = "FOREIGN_KEY_COLUMN_ID", length = 100)
	public String getForeignKeyColumnId() {
		return foreignKeyColumnId;
	}

	public void setForeignKeyColumnId(String foreignKeyColumnId) {
		this.foreignKeyColumnId = foreignKeyColumnId;
	}
	
	@Column(name = "FOREIGN_KEY_COLUMN_NAME", length = 100)
	public String getForeignKeyColumnName() {
		return foreignKeyColumnName;
	}

	public void setForeignKeyColumnName(String foreignKeyColumnName) {
		this.foreignKeyColumnName = foreignKeyColumnName;
	}
	
	@Column(name = "CHILD_KEY_COLUMN_ID", length = 100)
	public String getChildKeyColumnId() {
		return childKeyColumnId;
	}

	public void setChildKeyColumnId(String childKeyColumnId) {
		this.childKeyColumnId = childKeyColumnId;
	}

	@Column(name = "CHILD_KEY_COLUMN_NAME", length = 100)
	public String getChildKeyColumnName() {
		return childKeyColumnName;
	}

	public void setChildKeyColumnName(String childKeyColumnName) {
		this.childKeyColumnName = childKeyColumnName;
	}
	
}
