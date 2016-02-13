package com.ensense.insense.data.common.entity;

import com.ensense.insense.data.uitesting.entity.Application;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;


@Entity
@Table(name = "ApplicationGroupReference")
public class ApplicationGroupReference implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "applicationGroupReferenceId")
	private int applicationGroupReferenceId;

	@Column(name = "groupId")
	private int groupId; 

	
	@Column(name = "applicationId")
	private int applicationId;

	@OneToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "applicationId", insertable = false, updatable = false, nullable = true, unique = false)
	private Application application;
	
	
	public int getApplicationGroupReferenceId() {
		return applicationGroupReferenceId;
	}

	public void setApplicationGroupReferenceId(int applicationGroupReferenceId) {
		this.applicationGroupReferenceId = applicationGroupReferenceId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}

	@Override
	public String toString() {
		return "ApplicationGroupReference [applicationGroupReferenceId="
				+ applicationGroupReferenceId + ", groupId=" + groupId
				+ ", applicationId=" + applicationId + "]";
	}

	public void setApplication(Application application) {
		this.application = application;
	}

	public Application getApplication() {
		return application;
	}
}
