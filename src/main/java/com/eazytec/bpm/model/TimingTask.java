package com.eazytec.bpm.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class TimingTask implements Serializable {
	private static final long serialVersionUID = 3690197650654049849L;
	//private Long id;
	private String name;
	private String description;
	private String jobName;
	private String jRunAt;
	private String every;
	private String day;
	private String jobRunAt;
	private Long repeatInterval;
	private String jobRunOn;
	private Date startDate;
	private boolean concurrency;
	private String nextFireTime = null;
    private String previousFireTime = null;
    private String intervel;
    private String triggerName;
    
    private String jobClassName;
    private String parameter;
	/**
	 * Default constructor - creates a new instance with no values set.
	 */
	public TimingTask() {
	}

	/**
	 * Create a new instance and set the name.
	 * 
	 * @param name
	 *            name of the Trigger.
	 */
	public TimingTask(final String name) {
		this.name = name;
	}

	public TimingTask(final String name,final String desc) {
		this.name = name;
		this.description=desc;
		
	} 
/*	public Long getId() {
		return id;
	}*/

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	/*public void setId(Long id) {
		this.id = id;
	}*/

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof TimingTask)) {
			return false;
		}

		final TimingTask timingTask = (TimingTask) o;

		return !(name != null ? !name.equals(timingTask.name) : timingTask.name != null);

	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return (name != null ? name.hashCode() : 0);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		
		
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
		.append(this.name)
		.append(this.description)
		.append(this.jobName)
		.append(this.jRunAt)
		.append(this.every)
		.append(this.day)
		.append(this.jobRunAt)
		.append(this.repeatInterval)
		.append(this.jobRunOn)
		.append(this.startDate)
		.append(this.concurrency)
		.append(this.jobClassName)
		.append(this.parameter).toString();
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobName() {
		return jobName;
	}



	
	public void setConcurrency(boolean concurrency) {
		this.concurrency = concurrency;
	}

	public boolean isConcurrency() {
		return concurrency;
	}	

	public void setEvery(String every) {
		this.every = every;
	}

	public String getEvery() {
		return every;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getDay() {
		return day;
	}

	public void setJobRunAt(String jobRunAt) {
		this.jobRunAt = jobRunAt;
	}

	public String getJobRunAt() {
		return jobRunAt;
	}

	public void setRepeatInterval(Long repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	public Long getRepeatInterval() {
		return repeatInterval;
	}

	public void setjRunAt(String jRunAt) {
		this.jRunAt = jRunAt;
	}

	public String getjRunAt() {
		return jRunAt;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setJobRunOn(String jobRunOn) {
		this.jobRunOn = jobRunOn;
	}

	public String getJobRunOn() {
		return jobRunOn;
	}

	  /**
     * <p>
     * Returns the next time at which the <code>Trigger</code> is scheduled to fire. If
     * the trigger will not fire again, <code>null</code> will be returned.  Note that
     * the time returned can possibly be in the past, iStringf the time that was computed
     * for the trigger to next fire has already arrived, but the scheduler has not yet
     * been able to fire the trigger (which would likely be due to lack of resources
     * e.g. threads).
     * </p>
     *
     * <p>The value returned is not guaranteed to be valid until after the <code>Trigger</code>
     * has been added to the scheduler.
     * </p>
     *
     * @see TriggerUtils#computeFireTimesBetween(Trigger, Calendar, Date, Date)
     */
    public String getNextFireTime() {
        return nextFireTime;
    }

    /**
     * <p>
     * Returns the previous time at which the <code>SimpleTrigger</code> 
     * fired. If the trigger has not yet fired, <code>null</code> will be
     * returned.</p>
     */
    public String getPreviousFireTime() {
        return previousFireTime;
    }

    /**
     * <p>
     * Set the next time at which the <code>SimpleTrigger</code> should fire.
     * </p>
     * 
     * <p>
     * <b>This method should not be invoked by client code.</b>
     * </p>
     */
    public void setNextFireTime(String nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    /**
     * <p>
     * Set the previous time at which the <code>SimpleTrigger</code> fired.
     * </p>
     * 
     * <p>
     * <b>This method should not be invoked by client code.</b>
     * </p>
     */
    public void setPreviousFireTime(String previousFireTime) {
        this.previousFireTime = previousFireTime;
    }

	public void setIntervel(String intervel) {
		this.intervel = intervel;
	}

	public String getIntervel() {
		return intervel;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getTriggerName() {
		return triggerName;
	}

	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}

	public String getJobClassName() {
		return jobClassName;
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}

	public String getParameter() {
		return parameter;
	}

	
}
