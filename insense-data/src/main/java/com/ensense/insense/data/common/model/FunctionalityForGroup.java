package com.ensense.insense.data.common.model;

import java.io.Serializable;



public class FunctionalityForGroup implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int functionalityId;

	private String functionalityName;

	private int menuId;
	
	private int groupId;

	public void setFunctionalityId(int functionalityId) {
		this.functionalityId = functionalityId;
	}

	public int getFunctionalityId() {
		return functionalityId;
	}

	public void setFunctionalityName(String functionalityName) {
		this.functionalityName = functionalityName;
	}

	public String getFunctionalityName() {
		return functionalityName;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getGroupId() {
		return groupId;
	}

	@Override
	public String toString() {
		return "FunctionalityForGroup [functionalityId=" + functionalityId
				+ ", functionalityName=" + functionalityName + ", menuId="
				+ menuId + ", groupId=" + groupId + "]";
	}

	
}
