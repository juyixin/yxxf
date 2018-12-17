package com.eazytec.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


/**
 * This class is used to represent available Quartzjobs in the database.
 *
 * @author Sangeetha G
 */
@Entity
@Table(name = "QRTZ_JOB_AUDIT")
public class QuartzJobAudit extends BaseObject {
    private static final long serialVersionUID = 3690197650654049848L;
    private String id;
    private String taskName;
    private Date startDate;
    private Date endDate;
    private boolean status;
    private String message;
    
    /**
     * Default constructor - creates a new instance with no values set.
     */
    public QuartzJobAudit(String taskName,Date staDate,Date endDate,boolean status,String message) {
    	this.taskName=taskName;
    	this.startDate=staDate;
    	this.endDate=endDate;
    	this.status=status;
    	this.message=message;
    }

    /**
     * Create a new instance and set the name.
     *
     * @param name name of the trigger.
     */
    public QuartzJobAudit(final String taskName) {
        this.taskName = taskName;
    }
    

	/**
     * Create a new instance and set the name.
     *
     * @param id id of the trigger
     * @param name name of the trigger.
     */
/*    public QuartzJobAudit(String id, String triggerName) {
    	this.id = id;
        this.triggerName = triggerName;
    }

*/
  
	/*@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
		.append(this.id)
		.append(this.triggerName)
		.append(this.startDate)	
		.append(this.endDate).toString();
		
	}
	@Override
	public boolean equals(Object o) {
		   if (this == o) {
	            return true;
	        }
	        if (!(o instanceof QuartzJobAudit)) {
	            return false;
	        }

	        final QuartzJobAudit quartzJobAudit = (QuartzJobAudit) o;

	        return !(triggerName != null ? !triggerName.equals(quartzJobAudit.triggerName) : quartzJobAudit.triggerName != null);
}
	@Override
	public int hashCode() {
	    return (triggerName != null ? triggerName.hashCode() : 0);
	}
	public void setId(String id) {
		this.id = id;
	}
	*/
    
	 @Id
	 @GeneratedValue(generator="system-uuid")
	 @GenericGenerator(name="system-uuid", strategy = "uuid")
	 @Column(name="ID",length = 50)
	public String getId() {
		return id;
	}
	 
	public void setId(String id) {
		this.id = id;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	@Column(name="TASK_NAME",length = 50)
	public String getTaskName() {
		return taskName;
	}
	public Date setStartDate(Date startDate) {
		return this.startDate = startDate;
	}
	
	 @Column(name="START_DATE",length = 50)
	public Date getStartDate() {
		return startDate;
	}
	public Date setEndDate(Date endDate) {
		return this.endDate = endDate;
	}
	
	 @Column(name="END_DATE",length = 50)
	public Date getEndDate() {
		return endDate;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	@Column(name="STATUS")
	public boolean getStatus() {
		return status;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	@Column(name="MESSAGE",  length=8000)  @Lob
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "QuartzJobAudit [endDate=" + endDate + ", id=" + id
				+ ", startDate=" + startDate + ", status=" + status
				+ ", taskName=" + taskName + "]";
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}