package com.ensense.insense.data.common.dao;

import java.util.List;

import com.cts.mint.common.entity.FunctionalityPermission;
import com.cts.mint.common.entity.Menu;
import com.cts.mint.common.model.MenuAccess;
import com.cts.mint.miscellaneous.entity.MiscellaneousTool;
import com.cts.mint.usermanagement.form.UserManagementForm;
import com.cts.mint.usermanagement.model.FunctionalityForGroup;

public interface MenuDAO {
	public int getMenuIdForAction(String menuAction);
	public boolean checkGroupHasAccessToMenu(int groupId, int menuId);
	public List<FunctionalityForGroup> getFunctionalityForGroup(int groupId);
	public List<MenuAccess> reteiveMenusAccessForGroup(int groupId);
	public boolean addNewMenu(UserManagementForm userManagementForm);
	public List<FunctionalityPermission> getFunctionalityPermissionList();
	public Boolean addFunctionality(FunctionalityPermission functionality);
	public List<FunctionalityPermission> getFunctionality(int menuId);
	public List<Menu> getSubMenuList();
	public List<MiscellaneousTool> getToolsList();
	public List<Menu> getSubMenuList(int parentMenuId);
	public List<MiscellaneousTool> getToolsListForGroup(int userGroupId);
}
