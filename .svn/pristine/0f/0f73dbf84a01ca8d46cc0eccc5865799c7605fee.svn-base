package com.eazytec.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.activiti.engine.impl.db.PersistentObject;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;

/**
 * This class represents the email config object
 *
 * @author mathi
 */
@Entity
@Table(name = "EMAIL_CONFIG")
@XmlRootElement
public class EmailConfiguration extends BaseObject implements Serializable, PersistentObject {
	
	private static final long serialVersionUID = 3832626162173359411L;

	private String id;
	    
	//mta attributes
	private String mtaName;
	private String mtaAddress;
	private int mtaPort;
	private boolean mtaSecure;
	private String mtaProtocol;
	private boolean mtaAuthenticated;
	  
	//postoffice attributes
	private String poName;
	private String poAddress;
	private int poPort; 
	private String poRootFolder;
	private boolean poSecure;
	private String poType;
	private String poProtocol;
	private boolean poDefault;
	private String poReplyToDomain;
	  
	/**
	 * Default constructor - creates a new instance with no values set.
	 */
	public EmailConfiguration() {
	}
	
	
	@Id
	@GeneratedValue(generator="system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	@Column(name = "ID")
	public String getId() {
	    return id;
	}
	
	public void setId(String id) {
		this.id = id;
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @return the mtaName
	 */
	public String getMtaName() {
		return mtaName;
	}


	/**
	 * @param mtaName the mtaName to set
	 */
	public void setMtaName(String mtaName) {
		this.mtaName = mtaName;
	}


	/**
	 * @return the mtaAddress
	 */
	public String getMtaAddress() {
		return mtaAddress;
	}


	/**
	 * @param mtaAddress the mtaAddress to set
	 */
	public void setMtaAddress(String mtaAddress) {
		this.mtaAddress = mtaAddress;
	}


	/**
	 * @return the mtaPort
	 */
	public int getMtaPort() {
		return mtaPort;
	}


	/**
	 * @param mtaPort the mtaPort to set
	 */
	public void setMtaPort(int mtaPort) {
		this.mtaPort = mtaPort;
	}


	/**
	 * @return the mtaSecure
	 */
	public boolean isMtaSecure() {
		return mtaSecure;
	}


	/**
	 * @param mtaSecure the mtaSecure to set
	 */
	public void setMtaSecure(boolean mtaSecure) {
		this.mtaSecure = mtaSecure;
	}


	/**
	 * @return the mtaProtocol
	 */
	public String getMtaProtocol() {
		return mtaProtocol;
	}


	/**
	 * @param mtaProtocol the mtaProtocol to set
	 */
	public void setMtaProtocol(String mtaProtocol) {
		this.mtaProtocol = mtaProtocol;
	}


	/**
	 * @return the mtaAuthenticated
	 */
	public boolean isMtaAuthenticated() {
		return mtaAuthenticated;
	}


	/**
	 * @param mtaAuthenticated the mtaAuthenticated to set
	 */
	public void setMtaAuthenticated(boolean mtaAuthenticated) {
		this.mtaAuthenticated = mtaAuthenticated;
	}


	/**
	 * @return the poName
	 */
	public String getPoName() {
		return poName;
	}


	/**
	 * @param poName the poName to set
	 */
	public void setPoName(String poName) {
		this.poName = poName;
	}


	/**
	 * @return the poAddress
	 */
	public String getPoAddress() {
		return poAddress;
	}


	/**
	 * @param poAddress the poAddress to set
	 */
	public void setPoAddress(String poAddress) {
		this.poAddress = poAddress;
	}


	/**
	 * @return the poPort
	 */
	public int getPoPort() {
		return poPort;
	}


	/**
	 * @param poPort the poPort to set
	 */
	public void setPoPort(int poPort) {
		this.poPort = poPort;
	}


	/**
	 * @return the poRootFolder
	 */
	public String getPoRootFolder() {
		return poRootFolder;
	}


	/**
	 * @param poRootFolder the poRootFolder to set
	 */
	public void setPoRootFolder(String poRootFolder) {
		this.poRootFolder = poRootFolder;
	}


	/**
	 * @return the poSecure
	 */
	public boolean isPoSecure() {
		return poSecure;
	}


	/**
	 * @param poSecure the poSecure to set
	 */
	public void setPoSecure(boolean poSecure) {
		this.poSecure = poSecure;
	}


	/**
	 * @return the poType
	 */
	public String getPoType() {
		return poType;
	}


	/**
	 * @param poType the poType to set
	 */
	public void setPoType(String poType) {
		this.poType = poType;
	}


	/**
	 * @return the poProtocol
	 */
	public String getPoProtocol() {
		return poProtocol;
	}


	/**
	 * @param poProtocol the poProtocol to set
	 */
	public void setPoProtocol(String poProtocol) {
		this.poProtocol = poProtocol;
	}


	/**
	 * @return the poDefault
	 */
	public boolean isPoDefault() {
		return poDefault;
	}


	/**
	 * @param poDefault the poDefault to set
	 */
	public void setPoDefault(boolean poDefault) {
		this.poDefault = poDefault;
	}


	/**
	 * @return the poReplyToDomain
	 */
	public String getPoReplyToDomain() {
		return poReplyToDomain;
	}


	/**
	 * @param poReplyToDomain the poReplyToDomain to set
	 */
	public void setPoReplyToDomain(String poReplyToDomain) {
		this.poReplyToDomain = poReplyToDomain;
	}


	@Transient
	  public Object getPersistentState() {
		    Map<String, Object> persistentState = new HashMap<String, Object>();
		    persistentState.put("id", id);
		    return persistentState;
	  }
	  
	
	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
	    if (this == o) {
	        return true;
	    }
	    if (!(o instanceof EmailConfiguration)) {
	        return false;
	    }
	
	    final EmailConfiguration config = (EmailConfiguration) o;
	
	    return !(id != null ? !id.equals(config.getId()) : config.getId() != null);
	
	}
	
	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
	    return (id != null ? id.hashCode() : 0);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
	    return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
	            .append(this.id)
	            .toString();
	}
	    

}
