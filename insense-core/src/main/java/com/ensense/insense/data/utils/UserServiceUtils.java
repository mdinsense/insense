package com.ensense.insense.data.utils;

import com.ensense.insense.data.common.model.FunctionalityForGroup;
import com.ensense.insense.data.common.entity.FunctionalityPermission;
import com.ensense.insense.data.common.entity.Users;
import com.ensense.insense.data.common.model.ManageUsers;
import com.ensense.insense.data.common.model.MenuAccess;
import com.ensense.insense.data.common.model.MintMenu;
import com.ensense.insense.services.common.MenuService;
import com.ensense.insense.services.common.UserService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class UserServiceUtils {
	private static Logger logger = Logger.getLogger(UserServiceUtils.class);

	public static List<ManageUsers> removeActiveUser(
			List<ManageUsers> manageUsersList, String userName) {

		if (null == manageUsersList || null == userName) {
			return manageUsersList;
		}

		for (ManageUsers manageUsers : manageUsersList) {
			if (manageUsers.getUserName().equals(userName)) {
				manageUsersList.remove(manageUsers);
				break;
			}
		}
		return manageUsersList;
	}

	public static List<MenuAccess> updateNonAccessibleAction(
			List<MenuAccess> menuAccess) {
		for (MenuAccess menu : menuAccess) {
			if (!menu.isAccess()) {
				menu.setMenuAction("AccessDenied.ftl");
			}
		}
		return menuAccess;
	}

	public static List<MenuAccess> addFunctionalityForEachMenu(
			List<MenuAccess> menuAccess,
			List<FunctionalityPermission> functionalityList) {
		
		for (MenuAccess menu : menuAccess) {
			List<FunctionalityPermission> updatedfunctionalityList = new ArrayList<FunctionalityPermission>();
			for (FunctionalityPermission functionality : functionalityList) {
				if (menu.getMenuId() == functionality.getMenuId()) {
					updatedfunctionalityList.add(functionality);
				}
			}
			menu.setFunctionality(updatedfunctionalityList);
		}
		return menuAccess;
	}

	public static List<MintMenu> organizeMenuByParent(
			List<MenuAccess> menuAccess) {
		List<MintMenu> mintMenuList = new ArrayList<MintMenu>();

		// Find all Parent Menus
		for (MenuAccess menu : menuAccess) {
			if (menu.getParentMenuId() == 0) {
				MintMenu mintMenu = new MintMenu();
				mintMenu.setMenu(menu);
				mintMenuList.add(mintMenu);
			}
		}

		mintMenuList = normalizeParentMenu(mintMenuList);

		for (MenuAccess menu : menuAccess) {
			for (MintMenu mintMenu : mintMenuList) {
				if (menu.getParentMenuId() != 0
						&& menu.getParentMenuId() == mintMenu.getMenu()
								.getMenuId()) {
					mintMenu.getChildMenus().add(menu);
					break;
				}
			}
		}
		return mintMenuList;
	}

	private static List<MintMenu> normalizeParentMenu(
			List<MintMenu> mintMenuList) {
		// int addBefore = (Constants.PARENT_MENU_COUNT -
		// mintMenuList.size())/2;
		int addBefore = Constants.PARENT_MENU_COUNT - mintMenuList.size();

		for (int i = 0; i < addBefore; i++) {
			MintMenu menu = getEmptyMenu();

			// mintMenuList.add(0, menu);
			mintMenuList.add(menu); // Add the dumy menu only at the end.
		}
		return mintMenuList;
	}

	private static MintMenu getEmptyMenu() {
		MintMenu menu = new MintMenu();
		MenuAccess menuAccess = new MenuAccess();
		menuAccess.setMenuName("");
		menuAccess.setAccess(false);
		menuAccess.setMenuAction("");
		menuAccess.setMenuId(-1);
		menuAccess.setParentMenuId(0);
		menu.setMenu(menuAccess);
		return menu;
	}

	public static void addUserDetailsToSession(HttpServletRequest request,
											   UserService userService, MenuService menuService) {
		String userName = getUserNameFromSession(request);
		String userGroupName = "";
		Users mintUser = userService.getMintUser(userName);

		String aliasName = "";
		String aliasGroupName = "";
		Users aliasUser = null;

		if (mintUser.getUserId() > 0) {

			if (request.getParameter("aliasField") != null
					&& request.getParameter("aliasField").trim().length() > 0) {
				aliasName = request.getParameter("aliasField");
				aliasUser = userService.getMintUser(aliasName);
				aliasGroupName = userService.getGroupName(aliasUser
						.getGroupId());

				request.getSession().setAttribute("aliasName", aliasName);

				request.getSession().setAttribute("aliasGroupName",
						aliasGroupName);
				request.getSession().setAttribute("currentMintUser", aliasUser);
				request.getSession().setAttribute("currentUserGroupName",
						aliasGroupName);
			}else {
				request.getSession().setAttribute("currentMintUser", mintUser);
				request.getSession().setAttribute("currentUserGroupName",
						userGroupName);
			}

			request.getSession().setAttribute("mintUser", mintUser);
			
			userGroupName = userService.getGroupName(mintUser.getGroupId());
			request.getSession().setAttribute("userGroupName", userGroupName);

			List<ManageUsers> manageUsersList = userService
					.getAllMintUsersWithGroup();
			manageUsersList = UserServiceUtils.removeActiveUser(
					manageUsersList, userName);
			request.getSession().setAttribute("mintUsers", manageUsersList);

			List<MenuAccess> menuAccess = new ArrayList<MenuAccess>();
			//menuAccess = userService.reteiveMenusAccessForGroup(mintUser
					//.getGroupId());
			Users currentMintUser = (Users)request.getSession().getAttribute("currentMintUser");
			
			menuAccess =  userService.reteiveMenusAccessForGroup(currentMintUser.getGroupId());
			List<FunctionalityPermission> functionalityPermissionList = menuService
			.getFunctionalityPermissionList();
			List<FunctionalityForGroup> functionality = menuService
					.getFunctionalityForGroup(currentMintUser.getGroupId());

			request.getSession().setAttribute("userFunctionality",
					functionality);
			
			menuAccess = UserServiceUtils.updateNonAccessibleAction(menuAccess);
			menuAccess = UserServiceUtils.addFunctionalityForEachMenu(
					menuAccess, functionalityPermissionList);

			List<MintMenu> mintMenuList = UserServiceUtils
					.organizeMenuByParent(menuAccess);

			request.getSession().setAttribute("menuList", mintMenuList);

			request.getSession().setAttribute("functionalityPermissionList", functionalityPermissionList);
			request.getSession().setAttribute("userMenuAccess", menuAccess);
			request.getSession().setAttribute("currentUserMenuAccess",
					menuAccess);
		}
	}

	public static void addAliasUserDetailsToSession(HttpServletRequest request,
			UserService userService) {
		String aliasUserName = getUserNameFromSession(request);

		Users mintUser = userService.getMintUser(aliasUserName);

		if (mintUser.getUserId() > 0) {
			request.getSession().setAttribute("aliasUser", mintUser);
			request.getSession().setAttribute("currentUser", mintUser);
		}

	}

	public static String getUserNameFromSession(HttpServletRequest request) {
		String userName = "";
		if (null != request.getSession().getAttribute("loginUserName")
				&& request.getSession().getAttribute("loginUserName").toString()
						.trim().length() > 0) {
			userName = request.getSession().getAttribute("loginUserName").toString();
		}

		return userName;
	}

	public static String getAliasUserNameFromSession(HttpServletRequest request) {
		String aliasUserName = "";
		if (null != request.getSession().getAttribute("aliasUserName")
				&& request.getSession().getAttribute("aliasUserName")
						.toString().trim().length() > 0) {
			aliasUserName = request.getSession().getAttribute("aliasUserName")
					.toString();
		}

		return aliasUserName;
	}

	public static boolean isUserDetailsAvailableInSession(
			HttpServletRequest request) {
		Users mintUser = new Users();
		Users currentMintUser = new Users();

		if ( unsetAliasUser(request) && null != request.getSession().getAttribute("aliasName") ){
			request.getSession().setAttribute("aliasName", null);
			return false;
		}
		
		mintUser = (Users) request.getSession().getAttribute("mintUser");
		currentMintUser = (Users) request.getSession().getAttribute(
				"currentMintUser");

		if ( isAliasUserSet(request) ){
			//Check current user is Alias User otherwise need to Load Alias user details to session.
			if ( ! request.getParameter("aliasField").equals(currentMintUser.getUserName()) ){
				return false;
			} 
		} 
		if (null != mintUser && mintUser.getUserId() > 0
				&& null != currentMintUser && currentMintUser.getUserId() > 0) {
			return true;
		}
		return false;
	}

	private static boolean unsetAliasUser(HttpServletRequest request) {
		if ( null != request.getParameter("unsetAliasField") && "true".equals(request.getParameter("unsetAliasField")) ){
			return true;
		}
		return false;
	}

	private static boolean isAliasUserSet(HttpServletRequest request) {
		try {
			if (request.getParameter("aliasField") != null) {
				if ( request.getParameter("aliasField").trim().length() > 0 ){
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public static boolean isAliasUserDetailsAvailableInSession(
			HttpServletRequest request) {

		Users currentMintUser = new Users();
		currentMintUser = (Users) request.getSession().getAttribute(
				"currentMintUser");

		if (null != currentMintUser && currentMintUser.getUserId() > 0) {
			return true;
		}
		return false;
	}

	public static int getCurrentUserGroupIdFromRequest(
			HttpServletRequest context) {
		int groupId = 0;
		Users currentMintUser = new Users();
		currentMintUser = (Users) context.getSession().getAttribute(
				"currentMintUser");
		if (currentMintUser != null) {
			groupId = currentMintUser.getGroupId();
		}
		return groupId;
	}
	
	public static boolean isConfigPage(HttpServletRequest request) {
		if (request.getRequestURL().toString().endsWith("config.ftl")
				|| request.getRequestURL().toString().endsWith("Config.ftl")) {
			return true;
		}
		return false;
	}

	public static boolean guardianPage(HttpServletRequest httpRequest) {
		if (httpRequest.getRequestURL().toString().endsWith("GuardianHome.ftl")) {
			return true;
		}
		return false;
	}
	
	public static boolean mintAccessPage(HttpServletRequest httpRequest) {
		if (httpRequest.getRequestURL().toString().endsWith("/GetAccess.ftl")) {
			return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean isUserHasAccessToFunctionality(HttpServletRequest request, int functionalityId) {
		boolean hasAccess = false;
		List<FunctionalityForGroup> functionalityForGroup = (ArrayList<FunctionalityForGroup>) request
				.getSession().getAttribute("userFunctionality");
		for (FunctionalityForGroup func : functionalityForGroup) {
			if (func.getFunctionalityId() == functionalityId) {
				hasAccess = true;
			}
		}
		return hasAccess;
	}
}
