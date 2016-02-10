package com.ensense.insense.data.uitesting.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

/**
 * The persistent class for the ScheduleScript database table.
 * 
 */
@Entity
@Table(name = "ScheduleScriptXref")
public class ScheduleScriptXref implements Serializable {

	public ScheduleScriptXref() {
	}

	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "scheduleScriptXrefId")
	private Integer scheduleScriptXrefId;
	
	@Column(name = "scheduleScriptId")
	private Integer scheduleScriptId;
	
	@Column(name = "scheduleId")
	private Integer scheduleId;
	
	@Column(name = "scheduleExecutionId", nullable=true)
	private Integer scheduleExecutionId;
	
	@OneToOne(fetch = FetchType.EAGER)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "scheduleScriptId", insertable = false, updatable = false, nullable = true, unique = false)
	private ScheduleScript scheduleScript;

	public Integer getScheduleScriptXrefId() {
		return scheduleScriptXrefId;
	}

	public void setScheduleScriptXrefId(Integer scheduleScriptXrefId) {
		this.scheduleScriptXrefId = scheduleScriptXrefId;
	}

	public Integer getScheduleScriptId() {
		return scheduleScriptId;
	}

	public void setScheduleScriptId(Integer scheduleScriptId) {
		this.scheduleScriptId = scheduleScriptId;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getScheduleExecutionId() {
		return scheduleExecutionId;
	}

	public void setScheduleExecutionId(Integer scheduleExecutionId) {
		this.scheduleExecutionId = scheduleExecutionId;
	}

	public ScheduleScript getScheduleScript() {
		return scheduleScript;
	}

	public void setScheduleScript(ScheduleScript scheduleScript) {
		this.scheduleScript = scheduleScript;
	}

	@Override
	public String toString() {
		return "ScheduleScriptXref [scheduleScriptXrefId="
				+ scheduleScriptXrefId + ", scheduleScriptId="
				+ scheduleScriptId + ", scheduleId=" + scheduleId
				+ ", scheduleExecutionId=" + scheduleExecutionId + "]";
	}


}