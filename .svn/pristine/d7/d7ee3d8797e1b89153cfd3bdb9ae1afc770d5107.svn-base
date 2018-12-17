package com.eazytec.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.GenericGenerator;

/**
 * This class represents the basic email contact object
 *
 * @author mathi
 */
@Entity
@Table(name = "EMAIL_CONTACT")
public class EmailContact extends BaseObject implements Serializable {
    private static final long serialVersionUID = 3832626162173359411L;

    private String id;
    private String name;                   
    private String englishName;
    private String email;
    private String mobile;
    private String workPhone;
    private String homePhone;
    private String fax;
    private String website;
    private String createdBy;

	/**
     * Default constructor - creates a new instance with no values set.
     */
    public EmailContact() {
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
	}

    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    @Column(name = "WORK_PHONE")
    public String getWorkPhone() {
        return workPhone;
    }

    @Column(name = "WEBSITE")
    public String getWebsite() {
        return website;
    }
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public void setEmail(String email) {
        this.email = email;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}


	@Column(name = "HOME_PHONE")
	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	@Column(name = "FAX")
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

      /**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}


	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmailContact)) {
            return false;
        }

        final EmailContact emailContact = (EmailContact) o;

        return !(email != null ? !email.equals(emailContact.getEmail()) : emailContact.getEmail() != null);

    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return (email != null ? email.hashCode() : 0);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(this.email)
                .toString();
    }
    

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobile() {
		return mobile;
	}

}
