package com.eazytec.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "RE_LIST_VIEW_COLUMNS")
public class ListViewColumns implements Serializable {
	
	private static final long serialVersionUID = 8214779706605781262L;
	private String id;
	private String columnTitle;
	private String dataFields;
	private String width;
	private String textAlign;
	private String onRenderEvent;
	private String onRenderEventName;
	private boolean isHidden = false;
	private ListView listView;
	private int orderBy = 0;
	private String otherName;
	private String replaceWords;
	private boolean isSort = false;
	private boolean isAdvancedSearch = false;
	private boolean isSimpleSearch = false;
	private String comment;
	private boolean isGroupBy = false;
	private String dataFieldType;
	private int version;
	private boolean active = false;
	private String dataDictionary;
	private boolean isRange = false;
	private String columnType;
	private Integer orderNo;
	private String columnId;// Transient variable for id

	/**
	 * Default constructor - creates a new instance with no values set.
	 */
	public ListViewColumns() {
	}

	public ListViewColumns(List<Object> listViewDetails) {
		// listViewDetails has in this order
		// columnTitle,dataFields,width,textAlign,onRenderEvent,otherName,replaceWords,comment,orderBy,isHidden,isSort,isAdvancedSearch,isSimpleSearch
		this.columnTitle = (String) listViewDetails.get(0);
		this.dataFields = (String) listViewDetails.get(1);
		this.width = (String) listViewDetails.get(2);
		this.textAlign = (String) listViewDetails.get(3);
		this.onRenderEvent = (String) listViewDetails.get(4);
		this.otherName = (String) listViewDetails.get(5);
		this.replaceWords = (String) listViewDetails.get(6);
		this.comment = (String) listViewDetails.get(7);
		if (listViewDetails.get(8) != "" && listViewDetails.get(8) != null) {
			this.orderBy = Integer.parseInt(listViewDetails.get(8).toString());
		}

		if (listViewDetails.get(9) != "" && listViewDetails.get(9) != null) {
			this.isHidden = Boolean.parseBoolean(listViewDetails.get(9).toString());
		}

		if (listViewDetails.get(10) != "" && listViewDetails.get(10) != null) {
			this.isSort = Boolean.parseBoolean(listViewDetails.get(10).toString());
		}

		if (listViewDetails.get(11) != "" && listViewDetails.get(11) != null) {
			this.isAdvancedSearch = Boolean.parseBoolean(listViewDetails.get(11).toString());
		}

		if (listViewDetails.get(12) != "" && listViewDetails.get(12) != null) {
			this.isSimpleSearch = Boolean.parseBoolean(listViewDetails.get(12).toString());
		}

		this.onRenderEventName = (String) listViewDetails.get(13);

		if (listViewDetails.get(14) != "" && listViewDetails.get(14) != null) {
			this.isGroupBy = Boolean.parseBoolean(listViewDetails.get(14).toString());
		}
		// this.orderNo = Integer.parseInt(listViewDetails.get(15).toString());

	}

	@Id
	@JsonIgnore
	@Column(name = "ID")
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "column_id", length = 255)
	public String getColumnId() {
		columnId = this.id;
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	@JsonIgnore
	@Column(name = "COLUMN_TITLE", length = 255)
	public String getColumnTitle() {
		return columnTitle;
	}

	public void setColumnTitle(String columnTitle) {
		this.columnTitle = columnTitle;
	}

	@JsonIgnore
	@Column(name = "DATA_FIELDS", length = 8000)
	@Lob
	public String getDataFields() {
		return dataFields;
	}

	public void setDataFields(String dataFields) {
		this.dataFields = dataFields;
	}

	@Column(name = "WIDTH", length = 10)
	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	@Column(name = "TEXT_ALIGN", length = 50)
	public String getTextAlign() {
		return textAlign;
	}

	public void setTextAlign(String textAlign) {
		this.textAlign = textAlign;
	}

	@Column(name = "ON_RENDER_EVENT", length = 8000)
	@Lob
	public String getOnRenderEvent() {
		return onRenderEvent;
	}

	public void setOnRenderEvent(String onRenderEvent) {
		this.onRenderEvent = onRenderEvent;
	}

	@Column(name = "ON_RENDER_EVENT_NAME", length = 100)
	public String getOnRenderEventName() {
		return onRenderEventName;
	}

	public void setOnRenderEventName(String onRenderEventName) {
		this.onRenderEventName = onRenderEventName;
	}

	@Column(name = "IS_HIDDEN")
	public boolean getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "LIST_VIEW_ID", nullable = false)
	public ListView getListView() {
		return listView;
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}

	@JsonIgnore
	@Column(name = "ORDER_BY", length = 10)
	public int getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "OTHER_NAME")
	public String getOtherName() {
		return otherName;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	@Column(name = "REPLACE_WORDS")
	public String getReplaceWords() {
		return replaceWords;
	}

	public void setReplaceWords(String replaceWords) {
		this.replaceWords = replaceWords;
	}

	@Column(name = "IS_SORT")
	public boolean getIsSort() {
		return isSort;
	}

	public void setIsSort(boolean isSort) {
		this.isSort = isSort;
	}

	@Column(name = "IS_ADVANCED_SEARCH")
	public boolean getIsAdvancedSearch() {
		return isAdvancedSearch;
	}

	public void setIsAdvancedSearch(boolean isAdvancedSearch) {
		this.isAdvancedSearch = isAdvancedSearch;
	}

	@Column(name = "IS_SIMPLE_SEARCH")
	public boolean getIsSimpleSearch() {
		return isSimpleSearch;
	}

	public void setIsSimpleSearch(boolean isSimpleSearch) {
		this.isSimpleSearch = isSimpleSearch;
	}

	@Column(name = "LISTVIEW_COMMENT")
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Column(name = "IS_GROUP_BY")
	public boolean getIsGroupBy() {
		return isGroupBy;
	}

	public void setIsGroupBy(boolean isGroupBy) {
		this.isGroupBy = isGroupBy;
	}

	@Column(name = "DATA_FIELD_TYPE")
	public String getDataFieldType() {
		return dataFieldType;
	}

	public void setDataFieldType(String dataFieldType) {
		this.dataFieldType = dataFieldType;
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

	@Column(name = "DATA_DICTIONARY")
	public String getDataDictionary() {
		return dataDictionary;
	}

	public void setDataDictionary(String dataDictionary) {
		this.dataDictionary = dataDictionary;
	}

	@Column(name = "IS_RANGE")
	public boolean getIsRange() {
		return isRange;
	}

	public void setIsRange(boolean isRange) {
		this.isRange = isRange;
	}

	@Column(name = "COLUMN_TYPE", nullable = false, columnDefinition = " varchar(255) default 'text'")
	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}

	@JsonIgnore
	@Column(name = "ORDER_NO")
	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

}
