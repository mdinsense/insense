package com.ensense.insense.data.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="SolutionType")
public class SolutionType {
	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "solutionTypeId")
	private int solutionTypeId;
	
	@Column(name="solutionTypeName")
	private String solutionTypeName;

	public int getSolutionTypeId() {
		return solutionTypeId;
	}

	public void setSolutionTypeId(int solutionTypeId) {
		this.solutionTypeId = solutionTypeId;
	}

	public String getSolutionTypeName() {
		return solutionTypeName;
	}

	public void setSolutionTypeName(String solutionTypeName) {
		this.solutionTypeName = solutionTypeName;
	}

	@Override
	public String toString() {
		return "SolutionType [solutionTypeId=" + solutionTypeId
				+ ", solutionTypeName=" + solutionTypeName + "]";
	}

}
