package com.eazytec.model;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

/**
 * This class is used to schedule the events.
 *
 * @author mathi
 */
@Entity
@Table(name = "SCHEDULE")
public class Schedule extends BaseObject{

	private static final long serialVersionUID = 1L;
	private String id;
	private String eventName;
	private String description;
	private String location;
	private String eventType;
	private String scheduleEventId;
	private String scheduleEventName;
	private Date startDate;
	private Date endDate;
	private String startTime;
	private String endTime;
	private String userNames;
	//private Date remindTime;
	private int version;
	private Date createdTime;
	private Date updatedTime;
	private String createdBy;
	private String updatedBy;
	//private User eventOwner;
	private Integer remindBefore;
	private String remindTimeType;
	private String assignedUser;
	private String from;
	private Set<User> users = new HashSet<User>();
	private Days days = new Days();
	private Integer repeatEvery;
	
	/**
     * Default constructor - creates a new instance with no values set.
     */
    public Schedule() {
		// TODO Auto-generated constructor stub
	}

    /**
     * constructor - creates a new instance.
     * 
     * @param eventName
	 * @param description
  	 * @param location
   	 * @param eventType
  	 * @param startDate
 	 * @param endDate
  	 * @param startTime
  	 * @param endTime
  	 * @param remindBefore
 	 * @param remindTimeType
 	 * @param assignedUser
     */
    public Schedule(String id, String eventName, String description,
			String location, String eventType, Date startDate, Date endDate,
			String startTime, String endTime, Integer remindBefore, 
			String remindTimeType, Integer repeatEvery, boolean sunday, 
			boolean monday, boolean tuesday, boolean wednesday, 
			boolean thursday, boolean friday, boolean saturday,String assignedUser) {
		this.id = id;
		this.eventName = eventName;
		this.description = description;
		this.location = location;
		this.eventType = eventType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.remindBefore = remindBefore;
		this.remindTimeType = remindTimeType;
		this.assignedUser = assignedUser;
		//this.scheduleEventId = scheduleEventId;
		//this.scheduleEventName = scheduleEventName;
		this.repeatEvery = repeatEvery;
		days.setSunday(sunday);
		days.setMonday(monday);
		days.setTuesday(tuesday);
		days.setWednesday(wednesday);
		days.setThursday(thursday);
		days.setFriday(friday);
		days.setSaturday(saturday);
	}
    
    public Schedule(String id, String eventName, String description,
			String location, String eventType, Date startDate, Date endDate,
			String startTime, String endTime, Integer remindBefore, 
			String remindTimeType, Integer repeatEvery, boolean sunday, 
			boolean monday, boolean tuesday, boolean wednesday, 
			boolean thursday, boolean friday, boolean saturday,String userNames,String createdBy,String updatedBy,Date createdTime,Date updatedTime) {
		this.id = id;
		this.eventName = eventName;
		this.description = description;
		this.location = location;
		this.eventType = eventType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.remindBefore = remindBefore;
		
		this.remindTimeType = remindTimeType;
		this.userNames = userNames;
		this.repeatEvery = repeatEvery;
		days.setSunday(sunday);
		days.setMonday(monday);
		days.setTuesday(tuesday);
		days.setWednesday(wednesday);
		days.setThursday(thursday);
		days.setFriday(friday);
		days.setSaturday(saturday);
		this.createdBy=createdBy;
		this.updatedBy=updatedBy;
		this.createdTime=createdTime;
		this.updatedTime=updatedTime;
		//this.version=version;
	}
    
  	/**
	 * @return the id
	 */
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "ID")
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	

	/**
	 * @return the eventName
	 */
	@Column(name = "EVENT_NAME", length=8000)  @Lob
	public String getEventName() {
		return eventName;
	}

	/**
	 * @param eventName the eventName to set
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * @return the description
	 */
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the location
	 */
	@Column(name = "LOCATION")
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return the eventType
	 */
	@Column(name = "EVENT_TYPE")
	public String getEventType() {
		return eventType;
	}

	/**
	 * @param eventType the eventType to set
	 */
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	/**
	 * @return the startDate
	 */
	@Column(name = "START_DATE")
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	@Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the startTime
	 */
	@Column(name = "START_TIME")
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	@Column(name = "END_TIME")
	public String getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the remindTime
	 *//*
	@Column(name = "REMIND_TIME")
	public Date getRemindTime() {
		return remindTime;
	}

	*//**
	 * @param remindTime the remindTime to set
	 *//*
	public void setRemindTime(Date remindTime) {
		this.remindTime = remindTime;
	}*/
	@JsonIgnore
	@Transient
	public Integer getVersion() {
		return version;
	}

	@Column(name = "VERSION", nullable = false, length = 50, columnDefinition = "int default 1")
    public int getVersionNext() {
   	   return version+1;
    }
	 
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public void setVersionNext(int version) {
		    this.version=version+1;
	}
	 
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Column(name = "CREATED_TIME")
	public Date getCreatedTime() {
		return createdTime;
	}
	
	/**
	 * @return the updatedTime
	 */
	@Column(name = "UPDATED_TIME")
	public Date getUpdatedTime() {
		return updatedTime;
	}

	/**
	 * @param updatedTime the updatedTime to set
	 */
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	
	
	/**
	 * @return the schedule event id
	 */
	@Column(name = "SCHEDULE_EVENT_ID")
	public String getScheduleEventId() {
		return scheduleEventId;
	}

	/**
	 * @param eventName the eventName to set
	 */
	public void setScheduleEventId(String scheduleEventId) {
		this.scheduleEventId = scheduleEventId;
	}
	
	
	/**
	 * @return the schedule event name
	 */
	@Column(name = "SCHEDULE_EVENT_NAME")
	public String getScheduleEventName() {
		return scheduleEventName;
	}

	/**
	 * @param eventName the eventName to set
	 */
	public void setScheduleEventName(String scheduleEventName) {
		this.scheduleEventName = scheduleEventName;
	}
	

	/**
	 * @return the createdBy
	 */
	@Column(name = "CREATED_BY")
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
	 * @return the updatedBy
	 */
	@Column(name = "UPDATED_BY")
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * @param updatedBy the updatedBy to set
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	/**
	 * @return the user
	 *//*
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "EVENT_OWNER")
	public User getEventOwner() {
		return eventOwner;
	}

	*//**
	 * @param user the user to set
	 *//*
	public void setEventOwner(User eventOwner) {
		this.eventOwner = eventOwner;
	}*/

	/**
	 * @return the assignedUser
	 */
	@Transient
	public String getAssignedUser() {
		return assignedUser;
	}

	/**
	 * @param assignedUser the assignedUser to set
	 */
	public void setAssignedUser(String assignedUser) {
		this.assignedUser = assignedUser;
	}

	/**
	 * @return the remindBefore
	 */
	@Column(name="REMIND_BEFORE", columnDefinition = "int default 0")
	public Integer getRemindBefore() {
		return remindBefore;
	}

	/**
	 * @param remindBefore the remindBefore to set
	 */
	public void setRemindBefore(Integer remindBefore) {
		this.remindBefore = remindBefore;
	}

	/**
	 * @return the remindTimeType
	 */
	public String getRemindTimeType() {
		return remindTimeType;
	}

	/**
	 * @param remindTimeType the remindTimeType to set
	 */
	public void setRemindTimeType(String remindTimeType) {
		this.remindTimeType = remindTimeType;
	}

	/**
	 * @return the from
	 */
	@Transient
	public String getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	
	//@ManyToMany(fetch=FetchType.LAZY,cascade={CascadeType.ALL})
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "EVENT_USERS",
            joinColumns = { @JoinColumn(name = "EVENT_ID") },
            inverseJoinColumns = @JoinColumn(name = "USER_ID")
    )
	public Set<User> getUsers() {
		return users;
	}
    
    
    public void setUsers(Set<User> users) {
		this.users = users;
	}
	    
	/**
	 * @param version the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the days
	 */
	 @Column(name = "DAYS")
    @Embedded
	public Days getDays() {
		return days;
	}

	/**
	 * @param days the days to set
	 */
	public void setDays(Days days) {
		this.days = days;
	}

	/**
	 * @return the repeatEverny
	 */
	public Integer getRepeatEvery() {
		return repeatEvery;
	}

	/**
	 * @param repeatEverny the repeatEverny to set
	 */
	public void setRepeatEvery(Integer repeatEvery) {
		this.repeatEvery = repeatEvery;
	}

	/**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Schedule)) {
            return false;
        }

        final Schedule schedule = (Schedule) o;

        return !(id != null ? !id.equals(schedule.id) : schedule.id != null);

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

    @Column(name = "User_Names")
	public String getUserNames() {
		return userNames;
	}

	public void setUserNames(String userNames) {
		this.userNames = userNames;
	}
    
    
    
}
