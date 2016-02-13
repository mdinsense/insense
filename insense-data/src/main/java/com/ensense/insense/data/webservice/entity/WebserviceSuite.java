package com.ensense.insense.data.webservice.entity;

import com.ensense.insense.data.common.entity.Users;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Time;
import java.util.Date;


@Entity
@Table(name="WebserviceSuite")
public class WebserviceSuite {

	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="webserviceSuiteId")
	private int webserviceSuiteId;

	@Column(name="webserviceSuiteName")
	private String webserviceSuiteName;
	
	@Column(name="environmentId")
	private int environmentId;
	
	@Column(name="userId")
	private int userId;
	
	@Column(name="privateSuit")
	private boolean privateSuit;

	@Column(name="createdDate")
	private Date createdDate;
	
	@Column(name="createdTime")
	private Time createdTime;
	
	@OneToOne
	@LazyCollection(LazyCollectionOption.FALSE)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "userId", insertable = false, updatable = false, nullable = true, unique = false)
	private Users users;
	
	public int getWebserviceSuiteId() {
		return webserviceSuiteId;
	}

	public void setWebserviceSuiteId(int webserviceSuiteId) {
		this.webserviceSuiteId = webserviceSuiteId;
	}

	public String getWebserviceSuiteName() {
		return webserviceSuiteName;
	}

	public void setWebserviceSuiteName(String webserviceSuiteName) {
		this.webserviceSuiteName = webserviceSuiteName;
	}

	public int getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(int environmentId) {
		this.environmentId = environmentId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Time getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Time createdTime) {
		this.createdTime = createdTime;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public Users getUsers() {
		return users;
	}

	public void setPrivateSuit(boolean privateSuit) {
		this.privateSuit = privateSuit;
	}

	public boolean isPrivateSuit() {
		return privateSuit;
	}


}
