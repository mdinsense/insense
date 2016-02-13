package com.ensense.insense.data.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="Groups", uniqueConstraints = {@UniqueConstraint(columnNames={"groupName"})})
public class Groups implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable")
	@GeneratedValue(generator = "sequenceGen")
	@Column(name="groupId")
	private int groupId;
	
	@Column(name="groupName")
	private String groupName;
	
	@Column(name="groupDescription")
	private String groupDescription;
	
	@Column(name="groupAdminRights", nullable = false)
	private boolean groupAdminRights;

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupDescription() {
		return groupDescription;
	}

	public void setGroupDescription(String groupDescription) {
		this.groupDescription = groupDescription;
	}

	public boolean isGroupAdminRights() {
		return groupAdminRights;
	}

	public void setGroupAdminRights(boolean groupAdminRights) {
		this.groupAdminRights = groupAdminRights;
	}

	@Override
	public String toString() {
		return "Groups [groupId=" + groupId + ", groupName=" + groupName
				+ ", groupDescription=" + groupDescription
				+ ", groupAdminRights=" + groupAdminRights + "]";
	}
}
