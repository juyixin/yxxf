package org.activiti.engine.impl.task;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.identity.Picture;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.db.PersistentObject;
import org.activiti.engine.impl.persistence.entity.ByteArrayEntity;

public class CustomOperatingFunction implements Serializable, PersistentObject {
	private String id;
	private String name;
	private String callFunction;
	private String jsFunction;
	private Picture picture;
	private String htmlProperty;
	private String description;
	protected String pictureByteArrayId;
	protected ByteArrayEntity pictureByteArray;
	
	
	/**
     * Default constructor - creates a new instance with no values set.
     */
    public CustomOperatingFunction() {
    }
    
    public CustomOperatingFunction(String id) {
    	this.id=id;
    }


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCallFunction() {
		return callFunction;
	}


	public void setCallFunction(String callFunction) {
		this.callFunction = callFunction;
	}


	public String getJsFunction() {
		return jsFunction;
	}


	public void setJsFunction(String jsFunction) {
		this.jsFunction = jsFunction;
	}


	public Picture getPicture() {
		return picture;
	}


	public void setPicture(Picture picture) {
	    if (pictureByteArrayId!=null) {
	      Context
	        .getCommandContext()
	        .getByteArrayEntityManager()
	        .deleteByteArrayById(pictureByteArrayId);
	    }
	    if (picture!=null) {
	      pictureByteArray = new ByteArrayEntity(picture.getMimeType(), picture.getBytes());
	      Context
	        .getCommandContext()
	        .getDbSqlSession()
	        .insert(pictureByteArray);
	      pictureByteArrayId = pictureByteArray.getId();
	    } else {
	      pictureByteArrayId = null;
	      pictureByteArray = null;
	    }
	  }


	public String getHtmlProperty() {
		return htmlProperty;
	}


	public void setHtmlProperty(String htmlProperty) {
		this.htmlProperty = htmlProperty;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getPictureByteArrayId() {
		return pictureByteArrayId;
	}


	public void setPictureByteArrayId(String pictureByteArrayId) {
		this.pictureByteArrayId = pictureByteArrayId;
	}


	public ByteArrayEntity getPictureByteArray() {
		return pictureByteArray;
	}


	public void setPictureByteArray(ByteArrayEntity pictureByteArray) {
		this.pictureByteArray = pictureByteArray;
	}
    
	public Object getPersistentState() {
	    Map<String, Object> persistentState = new HashMap<String, Object>();
	    persistentState.put("name", name);
	    persistentState.put("callFunction", callFunction);
	    persistentState.put("jsFunction", jsFunction);
	    persistentState.put("htmlProperty", htmlProperty);
	    persistentState.put("description", description);
	    persistentState.put("pictureByteArrayId", pictureByteArrayId);
	    return persistentState;
	  }

}
