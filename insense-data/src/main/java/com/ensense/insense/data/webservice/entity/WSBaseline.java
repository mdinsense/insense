package com.ensense.insense.data.webservice.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="WSBaseline")
public class WSBaseline{

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="wsBaselineId")
	private int wsBaselineId;
				
	@Column(name="wsScheduleId")
	private int wsScheduleId;

	@Column(name="wsBaselineScheduleId")
	private int wsBaselineScheduleId;	
	
	@Column(name="createdDateTime")
	private Date createdDateTime;

	public int getWsBaselineId() {
		return wsBaselineId;
	}

	public void setWsBaselineId(int wsBaselineId) {
		this.wsBaselineId = wsBaselineId;
	}

	public int getWsScheduleId() {
		return wsScheduleId;
	}

	public void setWsScheduleId(int wsScheduleId) {
		this.wsScheduleId = wsScheduleId;
	}

	public int getWsBaselineScheduleId() {
		return wsBaselineScheduleId;
	}

	public void setWsBaselineScheduleId(int wsBaselineScheduleId) {
		this.wsBaselineScheduleId = wsBaselineScheduleId;
	}

	public Date getCreatedDateTime() {
		return createdDateTime;
	}

	public void setCreatedDateTime(Date createdDateTime) {
		this.createdDateTime = createdDateTime;
	}

	@Override
	public String toString() {
		return "WSBaseline [wsBaselineId=" + wsBaselineId + ", wsScheduleId="
				+ wsScheduleId + ", wsBaselineScheduleId="
				+ wsBaselineScheduleId + ", createdDateTime=" + createdDateTime
				+ "]";
	}	
}