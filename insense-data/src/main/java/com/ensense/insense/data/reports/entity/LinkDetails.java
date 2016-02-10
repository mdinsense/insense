package com.ensense.insense.data.reports.entity;

import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "LinkDetails")
public class LinkDetails implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "linkDetailsId")
	private int linkDetailsId;

	@Column(name = "testScheduleId")
	private int scheduleId;

	@Lob
	@Column(name = "linkFileContent")
	Clob linkFileContent;

	// @Temporal( TemporalType.DATE)
	@Column(name = "date_created")
	Date dateCreated;

	// @Temporal( TemporalType.DATE)
	@Column(name = "time_created")
	Date timeCreated;

	// @Temporal( TemporalType.DATE)
	@Column(name = "date_modified")
	Date dateModified;

	// @Temporal( TemporalType.DATE)
	@Column(name = "time_modified")
	Date timeModified;
	
	@Transient
	private String SuitNameWithDt;

	public String getSuitNameWithDt() {
		return SuitNameWithDt;
	}

	public void setSuitNameWithDt(String suitNameWithDt) {
		SuitNameWithDt = suitNameWithDt;
	}

	public int getLinkDetailsId() {
		return linkDetailsId;
	}

	public void setLinkDetailsId(int linkDetailsId) {
		this.linkDetailsId = linkDetailsId;
	}

	public int getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Clob getLinkFileContent() {
		return linkFileContent;
	}

	public void setLinkFileContent(Clob linkFileContent) {
		this.linkFileContent = linkFileContent;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	public Date getTimeModified() {
		return timeModified;
	}

	public void setTimeModified(Date timeModified) {
		this.timeModified = timeModified;
	}

	@Override
	public String toString() {
		return "LinkDetails [linkDetailsId=" + linkDetailsId + ", scheduleId="
				+ scheduleId + ", linkFileContent=" + linkFileContent
				+ ", dateCreated=" + dateCreated + ", timeCreated="
				+ timeCreated + ", dateModified=" + dateModified
				+ ", timeModified=" + timeModified + ", SuitNameWithDt="
				+ SuitNameWithDt + "]";
	}

}
