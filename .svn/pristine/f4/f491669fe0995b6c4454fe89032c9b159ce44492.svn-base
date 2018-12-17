package com.eazytec.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "RE_PROCESS_MAPPING")
public class ProcessFormName {
	private static final long serialVersionUID = 3644859655330969141L;
	private String id;
	private String formId;
	private String processKey;
	/**
     * Default constructor - creates a new instance with no values set.
     */
    public ProcessFormName() {
    }
    
    public ProcessFormName(String formId,String processKey) {
    	this.formId = formId;
    	this.processKey = processKey;
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
	
	@Column(name = "FORM_ID",length = 100)
	public String getFormId() {
		return formId;
	}
	public void setFormId(String formId) {
		this.formId = formId;
	}
	
	@Column(name = "PROCESS_KEY",length = 100)
	public String getProcessKey() {
		return processKey;
	}
	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}
}
