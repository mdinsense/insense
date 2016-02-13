package com.ensense.insense.data.webservice.entity;

import java.sql.Clob;
import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="WSPingResults")
public class WSPingResults {

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable") 
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="wsPingResultsId")
	private int wsPingResultsId;
	
	@Column(name="wsPingScheduleId")
	private int wsPingScheduleId;
	
	@Column(name="environmentId")
	private String environmentId;
	
	@Column(name="startTime")
	private Time startTime;
	
	@Column(name="startDate")
	private Date startDate;
	
	@Lob
	@Column(name = "resultsXml")
	private Clob resultsXml;

	public int getWsPingResultsId() {
		return wsPingResultsId;
	}

	public void setWsPingResultsId(int wsPingResultsId) {
		this.wsPingResultsId = wsPingResultsId;
	}

	
	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Clob getResultsXml() {
		return resultsXml;
	}

	public void setResultsXml(Clob resultsXml) {
		this.resultsXml = resultsXml;
	}

	public void setEnvironmentId(String environmentId) {
		this.environmentId = environmentId;
	}

	public String getEnvironmentId() {
		return environmentId;
	}

	public void setWsPingScheduleId(int wsPingScheduleId) {
		this.wsPingScheduleId = wsPingScheduleId;
	}

	public int getWsPingScheduleId() {
		return wsPingScheduleId;
	}

	
}
