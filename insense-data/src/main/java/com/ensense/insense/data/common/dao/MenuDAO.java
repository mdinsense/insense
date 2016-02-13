package com.ensense.insense.data.common.dao;

import com.ensense.insense.data.common.entity.FunctionalityPermission;
import com.ensense.insense.data.common.entity.Menu;
import com.ensense.insense.data.common.model.FunctionalityForGroup;
import com.ensense.insense.data.common.model.MenuAccess;
import com.ensense.insense.data.common.model.uiadmin.form.UserManagementForm;
import com.ensense.insense.data.miscellaneous.entity.MiscellaneousTool;

import java.util.List;

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
