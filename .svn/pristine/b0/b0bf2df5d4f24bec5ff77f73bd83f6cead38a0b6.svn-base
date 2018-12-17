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
@Table(name = "RE_TABLE_COLUMNS")
public class MetaTableColumns implements Serializable {

	private static final long serialVersionUID = 8499424683418736722L;
	private String id;
	private Integer orderNum;
	private String name;
	private String ChineseName;
	private MetaTable table;
	private String type;
	private String defaultValue;
	private String length;
	private String autoGenerationId;
	private int idNumber;
	private boolean isUniquekey = false;
	private boolean isCompositeKey = false;

	/**
	 * Default constructor - creates a new instance with no values set.
	 */
	public MetaTableColumns() {
	}

	public MetaTableColumns(Integer orderNum,String name, String ChineseName, String type, String length, String defaultValue, String autoGenerationId, boolean isUniquekey, boolean isCompositeKey) {
		this.orderNum = orderNum;
		this.name = name;
		this.ChineseName = ChineseName;
		this.type = type;
		this.length = length;
		this.defaultValue = defaultValue;
		this.autoGenerationId = autoGenerationId;
		this.isUniquekey = isUniquekey;
		this.isCompositeKey = isCompositeKey;
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

	@Column(name = "NAME", length = 100)
	public String getName() {
		return name.toUpperCase().replaceAll(" ", "_");
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "COLUMN_TYPE", length = 100)
	public String getType() {
		return type.toUpperCase().replaceAll(" ", "_");
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "LENGTH", length = 100)
	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TABLE_ID", nullable = false)
	public MetaTable getTable() {
		return table;
	}

	public void setTable(MetaTable table) {
		this.table = table;
	}

	@Column(name = "Chinese_NAME", length = 100)
	public String getChineseName() {
		return ChineseName;
	}

	public void setChineseName(String ChineseName) {
		this.ChineseName = ChineseName;
	}

	@Column(name = "DEFAULT_VALUE", length = 100)
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Column(name = "AUTO_GENERATION_ID")
	public String getAutoGenerationId() {
		return autoGenerationId;
	}

	public void setAutoGenerationId(String autoGenerationId) {
		this.autoGenerationId = autoGenerationId;
	}

	@Column(name = "ID_NUMBER", columnDefinition = "int default 0")
	public int getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(int idNumber) {
		this.idNumber = idNumber;
	}

	@Column(name = "IS_UNIQUE_KEY")
	public boolean getIsUniquekey() {
		return isUniquekey;
	}

	public void setIsUniquekey(boolean isUniquekey) {
		this.isUniquekey = isUniquekey;
	}

	@Column(name = "IS_COMPOSITE_KEY")
	public boolean getIsCompositeKey() {
		return isCompositeKey;
	}

	public void setIsCompositeKey(boolean isCompositeKey) {
		this.isCompositeKey = isCompositeKey;
	}
	
	@Column(name="ORDER_NUM")
	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
}
