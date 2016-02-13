package com.ensense.insense.data.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="SolutionTypeApplnXref")
public class SolutionTypeApplnXref {
	
	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "solutionTypeApplnXrefId")
	private int solutionTypeApplnXrefId;
	
	@Column(name = "solutionTypeId")
	private int solutionTypeId;
	
	@Column(name = "applicationId")
	private int applicationId;

	public int getSolutionTypeApplnXrefId() {
		return solutionTypeApplnXrefId;
	}

	public void setSolutionTypeApplnXrefId(int solutionTypeApplnXrefId) {
		this.solutionTypeApplnXrefId = solutionTypeApplnXrefId;
	}

	public int getSolutionTypeId() {
		return solutionTypeId;
	}

	public void setSolutionTypeId(int solutionTypeId) {
		this.solutionTypeId = solutionTypeId;
	}

	public int getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}

	@Override
	public String toString() {
		return "SolutionTypeApplnXref [solutionTypeApplnXrefId="
				+ solutionTypeApplnXrefId + ", solutionTypeId="
				+ solutionTypeId + ", applicationId=" + applicationId + "]";
	}

}
