package com.eazytec.crm.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.eazytec.util.DateUtil;
import com.eazytec.model.BaseObject;
import com.eazytec.model.User;


@Entity
@Table(name = "crm_chance")
public class Chance extends BaseObject {
	private static final long serialVersionUID = 1L;

	private String id;
	private String customerName;
	private String salesStage;
	private String importance;
	private Double estimatedSales;
	private String success;
	private String detail;
	private String analysis;
	private String salesStrategy;
	private Date createdTime;
	private User createdBy;

	public Chance() {

	}

	@Id
	@GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getSalesStage() {
		return salesStage;
	}

	public void setSalesStage(String salesStage) {
		this.salesStage = salesStage;
	}

	public String getImportance() {
		return importance;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}

	public Double getEstimatedSales() {
		return estimatedSales;
	}

	public void setEstimatedSales(Double estimatedSales) {
		this.estimatedSales = estimatedSales;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAnalysis() {
		return analysis;
	}

	public void setAnalysis(String analysis) {
		this.analysis = analysis;
	}

	public String getSalesStrategy() {
		return salesStrategy;
	}

	public void setSalesStrategy(String salesStrategy) {
		this.salesStrategy = salesStrategy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "created_by")
	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	@Transient
	public String getCreatedTimeByString() {
		return DateUtil.convertDateToString(createdTime);
	}

	@Override
	public String toString() {
		return null;
	}

	@Override
	public boolean equals(Object o) {
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
	}

}
