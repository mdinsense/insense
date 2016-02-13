package com.ensense.insense.data.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "FunctionalityGroupRef")
public class FunctionalityGroupRef {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "functionalityGroupRefId")
	private int functionalityGroupRefId;

	@Column(name = "group_id")
	private int groupId; 

	@Column(name = "functionality_id")
	private int functionalityId;

	public int getFunctionalityGroupRefId() {
		return functionalityGroupRefId;
	}

	public void setFunctionalityGroupRefId(int functionalityGroupRefId) {
		this.functionalityGroupRefId = functionalityGroupRefId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getFunctionalityId() {
		return functionalityId;
	}

	public void setFunctionalityId(int functionalityId) {
		this.functionalityId = functionalityId;
	}

	@Override
	public String toString() {
		return "FunctionalityGroupRef [functionalityGroupRefId="
				+ functionalityGroupRefId + ", groupId=" + groupId
				+ ", functionalityId=" + functionalityId + "]";
	}

}
