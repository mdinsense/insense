package com.ensense.insense.data.common.model.uiadmin.form;

import java.io.Serializable;
import java.util.Arrays;

public class UserManagementForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer setupTabNumber;
	private Integer userId;
	private String userName;
	private String emailId;
	private Integer groupId;	
	private String groupName;
	private String groupDescription;
	private String[] applicationId;
	private String[] environmentCategoryId;
	private Integer isUserSetupEdit;
	private Integer parentMenuId;
	private String actionUrl;
	private String menuName;
	private String pageName;
	private String functionalityName;
	private Integer functionalityId;
	private Integer[] menuId;
	private String[] applicationIdSecond;
	private String[] environmentCategoryIdSecond;
	private Integer[] menuIdList;
	private Integer[] functionalityIdList;
	private boolean groupAdminRights;
	private String editMode;
	
	public String getEditMode() {
		return editMode;
	}
	public void setEditMode(String editMode) {
		this.editMode = editMode;
	}
	public boolean isGroupAdminRights() {
		return groupAdminRights;
	}
	public void setGroupAdminRights(boolean groupAdminRights) {
		this.groupAdminRights = groupAdminRights;
	}
	public Integer getSetupTabNumber() {
		return setupTabNumber;
	}
	public void setSetupTabNumber(Integer setupTabNumber) {
		this.setupTabNumber = setupTabNumber;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
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
	public String[] getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String[] applicationId) {
		this.applicationId = applicationId;
	}
	public String[] getEnvironmentCategoryId() {
		return environmentCategoryId;
	}
	public void setEnvironmentCategoryId(String[] environmentCategoryId) {
		this.environmentCategoryId = environmentCategoryId;
	}
	public Integer getIsUserSetupEdit() {
		return isUserSetupEdit;
	}
	public void setIsUserSetupEdit(Integer isUserSetupEdit) {
		this.isUserSetupEdit = isUserSetupEdit;
	}
	public Integer getParentMenuId() {
		return parentMenuId;
	}
	public void setParentMenuId(Integer parentMenuId) {
		this.parentMenuId = parentMenuId;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getPageName() {
		return pageName;
	}
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	public String getFunctionalityName() {
		return functionalityName;
	}
	public void setFunctionalityName(String functionalityName) {
		this.functionalityName = functionalityName;
	}
	public Integer getFunctionalityId() {
		return functionalityId;
	}
	public void setFunctionalityId(Integer functionalityId) {
		this.functionalityId = functionalityId;
	}
	public Integer[] getMenuId() {
		return menuId;
	}
	public void setMenuId(Integer[] menuId) {
		this.menuId = menuId;
	}
	public String[] getApplicationIdSecond() {
		return applicationIdSecond;
	}
	public void setApplicationIdSecond(String[] applicationIdSecond) {
		this.applicationIdSecond = applicationIdSecond;
	}
	public String[] getEnvironmentCategoryIdSecond() {
		return environmentCategoryIdSecond;
	}
	public void setEnvironmentCategoryIdSecond(String[] environmentCategoryIdSecond) {
		this.environmentCategoryIdSecond = environmentCategoryIdSecond;
	}
	public Integer[] getMenuIdList() {
		return menuIdList;
	}
	public void setMenuIdList(Integer[] menuIdList) {
		this.menuIdList = menuIdList;
	}
	public Integer[] getFunctionalityIdList() {
		return functionalityIdList;
	}
	public void setFunctionalityIdList(Integer[] functionalityIdList) {
		this.functionalityIdList = functionalityIdList;
	}
	@Override
	public String toString() {
		return "UserManagementForm [setupTabNumber=" + setupTabNumber
				+ ", userId=" + userId + ", userName=" + userName
				+ ", emailId=" + emailId + ", groupId=" + groupId
				+ ", groupName=" + groupName + ", groupDescription="
				+ groupDescription + ", applicationId="
				+ Arrays.toString(applicationId) + ", environmentCategoryId="
				+ Arrays.toString(environmentCategoryId) + ", isUserSetupEdit="
				+ isUserSetupEdit + ", parentMenuId=" + parentMenuId
				+ ", actionUrl=" + actionUrl + ", menuName=" + menuName
				+ ", pageName=" + pageName + ", functionalityName="
				+ functionalityName + ", functionalityId=" + functionalityId
				+ ", menuId=" + Arrays.toString(menuId)
				+ ", applicationIdSecond="
				+ Arrays.toString(applicationIdSecond)
				+ ", environmentCategoryIdSecond="
				+ Arrays.toString(environmentCategoryIdSecond)
				+ ", menuIdList=" + Arrays.toString(menuIdList)
				+ ", functionalityIdList="
				+ Arrays.toString(functionalityIdList) + ", groupAdminRights="
				+ groupAdminRights + ", editMode=" + editMode +"]";
	}
}
