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

import com.cts.mint.uitesting.entity.Application;
import com.cts.mint.uitesting.entity.EnvironmentCategory;


/**
 * The persistent class for the EnvironmentReferenceTable database table.
 * 
 */
@Entity
@Table(name="EnvironmentCategoryGroupXref")
public class EnvironmentCategoryGroupXref implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="environmentReferenceId")
	private int environmentReferenceId;
	
	@Column(name="groupId")
	private int groupId;
	
	@Column(name="environmentCategoryId")
	private int environmentCategoryId;

	@OneToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "environmentCategoryId", insertable = false, updatable = false, nullable = true, unique = false)
	private EnvironmentCategory environmentCategory;
	
	public int getEnvironmentReferenceId() {
		return environmentReferenceId;
	}

	public void setEnvironmentReferenceId(int environmentReferenceId) {
		this.environmentReferenceId = environmentReferenceId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getEnvironmentCategoryId() {
		return environmentCategoryId;
	}

	public void setEnvironmentCategoryId(int environmentCategoryId) {
		this.environmentCategoryId = environmentCategoryId;
	}

	@Override
	public String toString() {
		return "EnvironmentCategoryGroupXref [environmentReferenceId="
				+ environmentReferenceId + ", groupId=" + groupId
				+ ", environmentCategoryId=" + environmentCategoryId + "]";
	}

	public void setEnvironmentCategory(EnvironmentCategory environmentCategory) {
		this.environmentCategory = environmentCategory;
	}

	public EnvironmentCategory getEnvironmentCategory() {
		return environmentCategory;
	}

}