package com.ensense.insense.data.common.model;

import java.io.Serializable;

public class ManageUsers implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private String groupName;
	

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	@Override
	public String toString() {
		return "ManageUsers [userName=" + userName + ", groupName=" + groupName
				+ "]";
	}
}
