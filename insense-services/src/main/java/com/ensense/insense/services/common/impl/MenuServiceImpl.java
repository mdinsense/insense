package com.ensense.insense.services.common.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.common.dao.MenuDAO;
import com.cts.mint.common.entity.FunctionalityPermission;
import com.cts.mint.common.entity.Menu;
import com.cts.mint.common.service.MenuService;
import com.cts.mint.miscellaneous.entity.MiscellaneousTool;
import com.cts.mint.usermanagement.form.UserManagementForm;
import com.cts.mint.usermanagement.model.FunctionalityForGroup;

@Service
public class MenuServiceImpl implements MenuService {
	private static Logger logger = Logger.getLogger(MenuServiceImpl.class);
	
	@Autowired
	MenuDAO menuDAO;

	@Override
	@Transactional
	public int getMenuIdForAction(String menuAction) {
		logger.debug("Entry and Exit : checkMenuforAction");
		return menuDAO.getMenuIdForAction(menuAction);
	}

	@Override
	@Transactional
	public boolean checkGroupHasAccessToMenu(int groupId, int menuId) {
		logger.debug("Entry and Exit : checkGroupHasAccessToMenu");
		return menuDAO.checkGroupHasAccessToMenu(groupId, menuId);
	}

	@Override
	@Transactional
	public List<FunctionalityForGroup> getFunctionalityForGroup(int groupId) {
		logger.debug("Entry and Exit : getFunctionalityForGroup");
		return menuDAO.getFunctionalityForGroup(groupId);
	}

	@Override
	@Transactional
	public boolean addNewMenu(UserManagementForm userManagementForm) {
		logger.debug("Entry and Exit : addNewMenu");
		return menuDAO.addNewMenu(userManagementForm);
	}

	@Override
	@Transactional
	public List<FunctionalityPermission> getFunctionalityPermissionList() {
		logger.debug("Entry and Exit : addNewMenu");
		return menuDAO.getFunctionalityPermissionList();
	}

	@Override
	@Transactional
	public Boolean addFunctionality(FunctionalityPermission functionality){
		logger.debug("Entry and Exit : addFunctionality");
		return menuDAO.addFunctionality(functionality);
	}
	
	@Override
	@Transactional
	public List<FunctionalityPermission> getFunctionality(int menuId){
		logger.debug("Entry and Exit : getFunctionality");
		return menuDAO.getFunctionality(menuId);
	}
	
	@Override
	@Transactional
	public List<Menu> getSubMenuList(){
		logger.debug("Entry and Exit : getSubMenuList");
		return menuDAO.getSubMenuList();
	}

	@Override
	@Transactional
	public List<MiscellaneousTool> getToolsList() {
		logger.debug("Entry and Exit : getToolsList");
		return menuDAO.getToolsList();
	}

	@Override
	@Transactional
	public List<Menu> getSubMenuList(int parentMenuId) {
		logger.debug("Entry and Exit : getSubMenuList");
		return menuDAO.getSubMenuList(parentMenuId);
	}

	@Override
	@Transactional
	public List<MiscellaneousTool> getToolsListForGroup(int userGroupId) {
		logger.debug("Entry and Exit : getToolsListForGroup");
		return menuDAO.getToolsListForGroup(userGroupId);
	}
}
