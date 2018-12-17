package com.eazytec.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class GroupDTO {
	
    private String id;
    private String name;
    private String superGroup;
    private boolean isParent;
    private String viewName;
    
    public GroupDTO(String id,String name,String superGroup,boolean isParent,String viewName) {
    	this.id=id;
    	this.name=name;
    	this.superGroup=superGroup;
    	this.isParent=isParent;
    	this.viewName = viewName;
	}
    
    public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
    
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getSuperGroup() {
		return superGroup;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSuperGroup(String superGroup) {
		this.superGroup = superGroup;
	}
	public boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(this.name)
                .toString();
    }

}
