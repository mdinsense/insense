package com.ensense.insense.data.uitesting.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SuitScheduleXref")
public class SuitScheduleXref implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "suitScheduleXrefId")
	private int suitScheduleXrefId;

	@Column(name = "scheduleId")
	private int scheduleId;

	@Column(name = "suitId")
	private int suitId;

	public int getSuitScheduleXrefId() {
		return suitScheduleXrefId;
	}

	public void setSuitScheduleXrefId(int suitScheduleXrefId) {
		this.suitScheduleXrefId = suitScheduleXrefId;
	}

	public int getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}

	public int getSuitId() {
		return suitId;
	}

	public void setSuitId(int suitId) {
		this.suitId = suitId;
	}

	
}