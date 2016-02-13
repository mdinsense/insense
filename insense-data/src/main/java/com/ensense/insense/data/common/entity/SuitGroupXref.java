package com.ensense.insense.data.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "SuitGroupXref")
public class SuitGroupXref implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="suitGroupXrefId")
	private int suitGroupXrefId;
	
	@Column(name = "suitId")
	private int suitId;

	@Column(name = "groupId")
	private int groupId;
	
	@OneToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "groupId", insertable = false, updatable = false, nullable = true, unique = false)
	private Groups groups;

	public int getSuitGroupXrefId() {
		return suitGroupXrefId;
	}

	public void setSuitGroupXrefId(int suitGroupXrefId) {
		this.suitGroupXrefId = suitGroupXrefId;
	}

	public int getSuitId() {
		return suitId;
	}

	public void setSuitId(int suitId) {
		this.suitId = suitId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public Groups getGroups() {
		return groups;
	}

	public void setGroups(Groups groups) {
		this.groups = groups;
	}

	@Override
	public String toString() {
		return "SuitGroupXref [suitGroupXrefId=" + suitGroupXrefId
				+ ", suitId=" + suitId + ", groupId=" + groupId + "]";
	}

	
}
