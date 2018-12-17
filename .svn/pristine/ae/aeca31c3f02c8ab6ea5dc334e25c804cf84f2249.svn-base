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
@Table(name = "RE_LV_GROUP_SETTING")
public class ListViewGroupSetting implements Serializable {
	
	private static final long serialVersionUID = -7889240646014255887L;
	private String id;
	private String groupName;
	private String groupFieldsName;
	private String parentGroup;
	private String sortType;
	private String comment;
	private String groupType; // Group by fields as single or multi column
	private ListView listView;
	private int orderBy = 0;
	private int version;
	private boolean active;
	private String columnId;// Transient variable for id

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

	@Column(name = "GROUP_NAME")
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Column(name = "GROUP_FIELDS_NAME")
	public String getGroupFieldsName() {
		return groupFieldsName;
	}

	public void setGroupFieldsName(String groupFieldsName) {
		this.groupFieldsName = groupFieldsName;
	}

	@Column(name = "PARENT_GROUP")
	public String getParentGroup() {
		return parentGroup;
	}

	public void setParentGroup(String parentGroup) {
		this.parentGroup = parentGroup;
	}

	@Column(name = "SORT_TYPE")
	public String getSortType() {
		return sortType;
	}

	public void setSortType(String sortType) {
		this.sortType = sortType;
	}

	@Column(name = "GROUP_TYPE")
	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	@Column(name = "GROUP_SETTING_COMMENT")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "LIST_VIEW_ID", nullable = false)
	public ListView getListView() {
		return listView;
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}

	@Column(name = "ORDER_BY")
	public int getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "VERSION", columnDefinition = "int default 1")
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Column(name = "IS_ACTIVE", length = 40)
	public boolean isActive() {
		return active;
	}

	@Column(name = "column_id", length = 255)
	public String getColumnId() {
		columnId = this.id;
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

}
