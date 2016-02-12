package com.ensense.insense.services.common;


import com.ensense.insense.core.usermanagement.FunctionalityForGroup;
import uiadmin.form.UserManagementForm;

import java.util.List;

public interface MenuService {
	public int getMenuIdForAction(String menuAction);

	public boolean checkGroupHasAccessToMenu(int groupId, int menuIdForAction);

	public List<FunctionalityForGroup> getFunctionalityForGroup(int groupId);

	boolean addNewMenu(UserManagementForm userManagementForm);

	public List<FunctionalityPermission> getFunctionalityPermissionList();
	
	public Boolean addFunctionality(FunctionalityPermission functionality);
	
	public List<FunctionalityPermission> getFunctionality(int menuId);
	
	public List<Menu> getSubMenuList();
	
	public List<MiscellaneousTool> getToolsList();
	
	public List<MiscellaneousTool> getToolsListForGroup(int userGroupId);
	
	public List<Menu> getSubMenuList(int parentMenuId);
}
