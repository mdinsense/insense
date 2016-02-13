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
@Table(name = "TaskSchedule")
public class TaskSchedule implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "taskScheduleId")
	private int taskScheduleId;

	@Column(name = "taskScheduleName")
	private String taskScheduleName;

	@Column(name = "taskStatus")
	private int taskStatus;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dateCreated")
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dateModified")
	private Date dateModified;

	@Column(name = "userId")
	private String userId;

	@Column(name = "taskType")
	private int taskType;



	public int getTaskType()
	{
		return taskType;
	}

	public void setTaskType(int taskType)
	{
		this.taskType = taskType;
	}

	

	public int getTaskScheduleId()
	{
		return taskScheduleId;
	}

	public void setTaskScheduleId(int taskScheduleId)
	{
		this.taskScheduleId = taskScheduleId;
	}

	public String getTaskScheduleName()
	{
		return taskScheduleName;
	}

	public void setTaskScheduleName(String taskScheduleName)
	{
		this.taskScheduleName = taskScheduleName;
	}

	public int getTaskStatus()
	{
		return taskStatus;
	}

	public void setTaskStatus(int taskStatus)
	{
		this.taskStatus = taskStatus;
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
		return "TaskSchedule [taskScheduleId=" + taskScheduleId + ", taskScheduleName="
				+ taskScheduleName + ", taskStatus=" + taskStatus + ", dateCreated=" + dateCreated
				+ ", dateModified=" + dateModified + ", userId=" + userId + ", TaskType=" + taskType + "]";
	}
}
