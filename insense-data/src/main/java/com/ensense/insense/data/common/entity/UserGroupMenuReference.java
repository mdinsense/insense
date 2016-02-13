package com.ensense.insense.data.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "UserGroupMenuReference")
public class UserGroupMenuReference {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name = "sequenceGen", strategy = "com.cts.mint.common.entity.SequenceTable") 
	@GeneratedValue(generator = "sequenceGen")
	@Column(name = "userMenuGroupRefId")
	private int userMenuGroupRefId;

	@Column(name = "groupId")
	private int groupId;

	@Column(name = "menuId")
	private int menuId;

	public int getUserMenuGroupRefId() {
		return userMenuGroupRefId;
	}

	public void setUserMenuGroupRefId(int userMenuGroupRefId) {
		this.userMenuGroupRefId = userMenuGroupRefId;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	@Override
	public String toString() {
		return "UserGroupMenuRef [userMenuGroupRefId=" + userMenuGroupRefId
				+ ", groupId=" + groupId + ", menuId=" + menuId + "]";
	}

}
