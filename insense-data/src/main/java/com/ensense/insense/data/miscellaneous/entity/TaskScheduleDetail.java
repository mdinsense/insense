package com.ensense.insense.data.miscellaneous.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TaskScheduleDetail")
public class TaskScheduleDetail implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "taskScheduleDetailId")
	private int taskScheduleDetailId;

	 @Column(name = "taskScheduleId")
	 private int taskScheduleId;

	@Column(name = "taskAttributeName")
	private String taskAttributeName;

	@Column(name = "taskAttributeValue")
	private String taskAttributeValue;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dateCreated")
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dateModified")
	private Date dateModified;

	@Column(name = "userId")
	private String userId;

	public int getTaskScheduleId()
	{
		return taskScheduleId;
	}

	public void setTaskScheduleId(int taskScheduleId)
	{
		this.taskScheduleId = taskScheduleId;
	}


	public int getTaskScheduleDetailId()
	{
		return taskScheduleDetailId;
	}

	public void setTaskScheduleDetailId(int taskScheduleDetailId)
	{
		this.taskScheduleDetailId = taskScheduleDetailId;
	}

	public String getTaskAttributeName()
	{
		return taskAttributeName;
	}

	public void setTaskAttributeName(String taskAttributeName)
	{
		this.taskAttributeName = taskAttributeName;
	}

	public String getTaskAttributeValue()
	{
		return taskAttributeValue;
	}

	public void setTaskAttributeValue(String taskAttributeValue)
	{
		this.taskAttributeValue = taskAttributeValue;
	}

	public Date getDateCreated()
	{
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated)
	{
		this.dateCreated = dateCreated;
	}

	public Date getDateModified()
	{
		return dateModified;
	}

	public void setDateModified(Date dateModified)
	{
		this.dateModified = dateModified;
	}

	

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString()
	{
		return "TaskScheduleDetail [taskScheduleDetailId=" + taskScheduleDetailId
				+ ", taskAttributeName=" + taskAttributeName + ", taskAttributeValue="
				+ taskAttributeValue + ", dateCreated=" + dateCreated + ", dateModified="
				+ dateModified + ", mintUserId=" + userId + "]";
	}
}