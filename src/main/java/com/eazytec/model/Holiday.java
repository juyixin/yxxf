package com.eazytec.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * This class represents the basic "user" object with authentication
 * and user management implementing Acegi Security's UserDetails interface.
 *
 * @author sangeetha
 */
@Entity
@Table(name = "HOLIDAY")
public class Holiday implements Serializable{
    private static final long serialVersionUID = 3832626162173359411L;

    private String id;
    private Date holiday;
	private String description;
	private String weekEnd;
	private int year;
	/**
     * Default constructor - creates a new instance with no values set.
     */
	public Holiday(){
	}
	
	public Holiday(Date holiday,String description,int year,String selectedWeekends){
		this.holiday=holiday;
		this.description=description;
		this.year=year;
		this.weekEnd=selectedWeekends;
	}
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
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

	@Column(name = "HOLIDAY", columnDefinition = "date")
	public Date getHoliday() {
		return holiday;
	}

	public void setHoliday(Date holiday) {
		this.holiday = holiday;
	}

	@Column(name = "DESCRIPTION", length = 1000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWeekEnd() {
		return weekEnd;
	}

	public void setWeekEnd(String weekEnd) {
		this.weekEnd = weekEnd;
	}
	
	
}