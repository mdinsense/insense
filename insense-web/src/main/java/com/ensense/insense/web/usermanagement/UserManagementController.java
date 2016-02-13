package com.ensense.insense.web.usermanagement;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cts.mint.common.entity.ApplicationGroupReference;
import com.cts.mint.common.entity.EnvironmentCategoryGroupXref;
import com.cts.mint.common.entity.FunctionalityGroupRef;
import com.cts.mint.common.entity.FunctionalityPermission;
import com.cts.mint.common.entity.Groups;
import com.cts.mint.common.entity.UserGroupMenuReference;
import com.cts.mint.common.entity.Users;
import com.cts.mint.common.model.MenuAccess;
import com.cts.mint.common.model.MintMenu;
import com.cts.mint.common.service.MenuService;
import com.cts.mint.common.service.UserService;
import com.cts.mint.common.utils.Constants.UserAccess;
import com.cts.mint.uitesting.entity.Application;
import com.cts.mint.uitesting.entity.EnvironmentCategory;
import com.cts.mint.uitesting.service.ApplicationService;
import com.cts.mint.uitesting.service.EnvironmentService;
import com.cts.mint.usermanagement.form.UserManagementForm;
import com.cts.mint.usermanagement.service.UserManagementService;

@Controller
public class UserManagementController {

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private EnvironmentService environmentService;

	@Autowired
	UserService userService;

	@Autowired
	UserManagementService userManagementService;

	@Autowired
	MenuService menuService;

	private static Logger logger = Logger
			.getLogger(UserManagementController.class);

	@RequestMapping(value = { "/UserAccessSetup" }, method = RequestMethod.GET)
	public String UserAccessSetupGet(Model model, HttpServletRequest request) {
		logger.debug("Entry: UserAccessSetupGet");
		UserManagementForm userManagementForm = new UserManagementForm();

		if (null != model.asMap()
				&& model.asMap().containsKey("userManagementForm")) {
			userManagementForm = (UserManagementForm) model.asMap().get(
					"userManagementForm");
		} else {
			userManagementForm = new UserManagementForm();
			userManagementForm
					.setSetupTabNumber(getUserAccessSetupTabNumber(model
							.asMap()));
		}
		try {

			switch (userManagementForm.getSetupTabNumber()) {
			case 1:
				if (!isUserSetupDetailsAvailable(model.asMap())) {
					model.addAttribute("usersDetails",
							userService.getAllUsersDetails());
					model.addAttribute("groupsDetails",
							userService.getAllGroupsDetails());
				}
				break;
			case 2:
				model.addAttribute("groupsDetails",
						userService.getAllGroupsDetails());
				break;
			case 3:
				if (!isMenuAccessSetupDetailsAvailable(model.asMap())) {
					model.addAttribute("groupList",
							userManagementService.getGroups());
				}
				break;
			case 4:
				break;
			case 5:
				model.addAttribute("subMenuList", menuService.getSubMenuList());
				break;
			}
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading UserSetup home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		model.addAttribute("userManagementForm", userManagementForm);
		logger.debug("Exit: UserAccessSetupGet, userManagementForm->"
				+ userManagementForm);

		return UserAccess.VIEW;
	}

	@RequestMapping(value = { "/UserAccessSetup" }, method = RequestMethod.POST)
	public String UserAccessSetupForm(
			@ModelAttribute UserManagementForm userManagementForm, Model model,
			RedirectAttributes redirAttr, HttpServletRequest request) {
		logger.debug("Entry: UserAccessSetupForm, userManagementForm->"
				+ userManagementForm);
		try {

			switch (userManagementForm.getSetupTabNumber()) {
			case 1:
				redirAttr.addFlashAttribute("usersDetails",
						userService.getAllUsersDetails());
				redirAttr.addFlashAttribute("groupsDetails",
						userService.getAllGroupsDetails());
				break;
			case 2:
				redirAttr.addFlashAttribute("groupsDetails",
						userService.getAllGroupsDetails());
				break;
			case 3:
				List<MintMenu> mintMenuList = (List<MintMenu>) request
						.getSession().getAttribute("menuList");
				logger.info("mintMenuList :" + mintMenuList);
				redirAttr.addFlashAttribute("menuPaginationCount",
						getMenuPaginateCount(mintMenuList));
				break;
			case 4:
				List<MintMenu> mintMenuList1 = (List<MintMenu>) request
						.getSession().getAttribute("menuList");
				logger.info("mintMenuList :" + mintMenuList1);
				redirAttr.addFlashAttribute("menuPaginationCount",
						getMenuPaginateCount(mintMenuList1));
				break;
			case 5:
				redirAttr.addFlashAttribute("subMenuList",
						menuService.getSubMenuList());
				break;
			}

		} catch (Exception e) {
			logger.error("Exception in UserAccessSetupForm :" + e);
			logger.error("Stack Trace :"
					+ ExceptionUtils.getRootCauseMessage(e));
		}

		logger.debug("Exit: UserAccessSetupForm");
		redirAttr.addFlashAttribute("userManagementForm", userManagementForm);
		return UserAccess.REDIRECT_VIEW;
	}

	// User Setup Tab - Begin

	/**
	 * Save new UserSetup details to Users and Groups table
	 * 
	 * @param model
	 * @param userManagementForm
	 */
	@RequestMapping(value = "/SaveUserSetup", method = RequestMethod.POST)
	public String SaveUserSetupDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UserManagementForm userManagementForm) {

		logger.debug("Entry: SaveUserSetup, userManagementForm->"
				+ userManagementForm);
		Users user = new Users();
		Groups group = new Groups();

		try {
			userManagementForm.setSetupTabNumber(UserAccess.MANAGE_USER_SETUP);
			redirAttr.addFlashAttribute("userManagementForm",
					userManagementForm);

			if (this.userService.isUserNameExists(userManagementForm
					.getUserName())) {
				redirAttr.addFlashAttribute("error",
						"User details already exists");
				return UserAccess.REDIRECT_VIEW;
			}

			user.setUserName(userManagementForm.getUserName());
			user.setEmailId(userManagementForm.getEmailId());

			if (userManagementForm.getGroupId() != null
					&& userManagementForm.getGroupId() > 0) {

				user.setGroupId(userManagementForm.getGroupId());

				if (this.userService.addUserDetails(user)) {
					updateUserManagementFormForUsers(userManagementForm, user);
					redirAttr.addFlashAttribute("Success",
							"User Details saved successfully");

				} else {
					redirAttr.addFlashAttribute("error",
							"Error while saving User details");
				}

			} else {
				group.setGroupName(userManagementForm.getGroupName());
				group.setGroupDescription(userManagementForm
						.getGroupDescription());
				group.setGroupAdminRights(userManagementForm.isGroupAdminRights());	
				
				user.setGroup(group);

				if (this.userService.addGroupAndUserDetails(user)) {
					updateUserManagementFormForUsers(userManagementForm, user);
					redirAttr.addFlashAttribute("Success",
							"Group and User Details saved successfully");

				} else {
					redirAttr.addFlashAttribute("error",
							"Error while saving Group and User details");
				}
			}

		} catch (Exception exception) {
			logger.error("Exception :Unexpected error occured while Saving Group and User details");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(exception));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while Saving Group and User details");
		}
		redirAttr.addFlashAttribute("userManagementForm", userManagementForm);

		logger.debug("Exit: SaveUserSetup");
		return UserAccess.REDIRECT_VIEW;
	}

	/**
	 * edit UserSetup details
	 * 
	 * @param model
	 * @param userManagementForm
	 */
	@RequestMapping(value = "/EditUserModule", method = RequestMethod.POST)
	public String editUserModuleDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UserManagementForm userManagementForm) {

		logger.debug("Entry: editUserModuleDetails");

		Users user = new Users();
		try {
			userManagementForm.setSetupTabNumber(UserAccess.MANAGE_USER_SETUP);
			redirAttr.addFlashAttribute("userManagementForm",
					userManagementForm);
			if (userManagementForm.getUserId() != null
					&& userManagementForm.getUserId() > 0) {
				user = userService.getUser(userManagementForm.getUserId());
				updateUserManagementFormForUsers(userManagementForm, user);
			}
		} catch (Exception e) {
			logger.error("Exception in editUserModuleDetails.", e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		redirAttr.addFlashAttribute("userManagementForm", userManagementForm);

		logger.debug("Exit: editUserModuleDetails");
		return UserAccess.REDIRECT_VIEW;
	}

	/**
	 * Update UserSetup details to Users and Groups table
	 * 
	 * @param model
	 * @param userManagementForm
	 */
	@RequestMapping(value = "/UpdateUserSetup", method = RequestMethod.POST)
	public String updateUserSetupDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UserManagementForm userManagementForm) {

		logger.debug("Entry: updateUserSetupDetails");
		Users user = new Users();
		Groups group = new Groups();
		try {

			userManagementForm.setSetupTabNumber(UserAccess.MANAGE_USER_SETUP);
			redirAttr.addFlashAttribute("userManagementForm",
					userManagementForm);
			if (userManagementForm.getUserId() != null
					&& userManagementForm.getUserId() > 0) {

				user = userService.getUser(userManagementForm.getUserId());
				user.setUserName(userManagementForm.getUserName());
				user.setEmailId(userManagementForm.getEmailId());
				user.setGroupId(userManagementForm.getGroupId());
				group = userService.getGroup(userManagementForm.getGroupId());
				user.setGroup(group);

				if (this.userService.addGroupAndUserDetails(user)) {
					updateUserManagementFormForUsers(userManagementForm, user);
					redirAttr.addFlashAttribute("Success",
							"Group and User Details updated successfully");

				} else {
					redirAttr.addFlashAttribute("error",
							"Error while updating Group and User details");
				}
			}

		} catch (Exception e) {
			logger.error("Exception in updateUserSetupDetails error", e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		redirAttr.addFlashAttribute("userManagementForm", userManagementForm);

		logger.debug("Exit: updateUserSetupDetails");
		return UserAccess.REDIRECT_VIEW;
	}

	/**
	 * delete UserSetup details to Users table
	 * 
	 * @param model
	 * @param userManagementForm
	 */
	@RequestMapping(value = "/DeleteUserModule", method = RequestMethod.POST)
	public String deleteUserModule(RedirectAttributes redirAttr, Model model,
			@ModelAttribute UserManagementForm userManagementForm) {

		logger.debug("Entry: deleteUserModule");
		Users user = new Users();
		try {

			userManagementForm.setSetupTabNumber(UserAccess.MANAGE_USER_SETUP);
			redirAttr.addFlashAttribute("userManagementForm",
					userManagementForm);

			if (userManagementForm.getUserId() != null
					&& userManagementForm.getUserId() > 0) {

				user = userService.getUser(userManagementForm.getUserId());
				if (userService.deleteUserModule(user)) {
					updateUserManagementFormForUserModule(userManagementForm,
							user);

					redirAttr.addFlashAttribute("Success",
							"User Module details deleted successfully");
				} else {
					redirAttr.addFlashAttribute("error",
							"Error while deleting User Module details");
				}
			}
		} catch (Exception e) {
			logger.error("Exception in deleteUserModule error", e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		redirAttr.addFlashAttribute("userManagementForm", userManagementForm);

		logger.debug("Exit: deleteUserModule");
		return UserAccess.REDIRECT_VIEW;
	}

	private void updateUserManagementFormForUsers(
			UserManagementForm userManagementForm, Users user) {
		userManagementForm.setUserName(user.getUserName());
		userManagementForm.setGroupId(user.getGroupId());
		userManagementForm.setEmailId(user.getEmailId());

		if (user.getGroup() != null) {
			userManagementForm.setGroupName(user.getGroup().getGroupName());
			userManagementForm.setGroupDescription(user.getGroup()
					.getGroupDescription());
		}
	}

	private void updateUserManagementFormForUserModule(
			UserManagementForm userManagementForm, Users user) {
		userManagementForm.setUserId(null);
		userManagementForm.setUserName("");
		userManagementForm.setGroupName("");
		userManagementForm.setGroupDescription("");
		userManagementForm.setEmailId("");
	}

	// User Setup Tab - Ends

	// GroupSetup Tab - Starts here

	/**
	 * Save new GroupSetup details to ApplicationGroupReference and
	 * EnvironmentCategoryGroupXref table
	 * 
	 * @param model
	 * @param userManagementForm
	 */
	@RequestMapping(value = "/SaveGroupSetup", method = RequestMethod.POST)
	public String saveGroupSetupDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute UserManagementForm userManagementForm) {

		logger.debug("Entry: SaveGroupSetup, userManagementForm->"
				+ userManagementForm);

		String applicationIds[] = {};
		String environmentCategoryIds[] = {};
		List<EnvironmentCategoryGroupXref> environmentGroupReferenceList = null;
		List<ApplicationGroupReference> applicationGroupReferenceList = null;
		try {
			userManagementForm.setSetupTabNumber(UserAccess.MANAGE_GROUP_SETUP);
			redirAttr.addFlashAttribute("userManagementForm",
					userManagementForm);

			environmentGroupReferenceList = new ArrayList<EnvironmentCategoryGroupXref>();
			applicationGroupReferenceList = new ArrayList<ApplicationGroupReference>();

			if (userManagementForm.getApplicationIdSecond() != null) {
				applicationIds = userManagementForm.getApplicationIdSecond();
			}
			for (String application : applicationIds) {
				ApplicationGroupReference applicationReference = new ApplicationGroupReference();
				applicationReference.setApplicationId(Integer
						.parseInt(application));
				applicationGroupReferenceList.add(applicationReference);
			}
			if (userManagementForm.getEnvironmentCategoryIdSecond() != null) {
				environmentCategoryIds = userManagementForm
						.getEnvironmentCategoryIdSecond();
			}
			for (String environment : environmentCategoryIds) {
				EnvironmentCategoryGroupXref environmentReference = new EnvironmentCategoryGroupXref();
				environmentReference.setEnvironmentCategoryId(Integer
						.parseInt(environment));
				environmentGroupReferenceList.add(environmentReference);
			}

			if (this.userService.addGroupMappingSetup(
					userManagementForm.getGroupId(),
					applicationGroupReferenceList,
					environmentGroupReferenceList)) {
				redirAttr.addFlashAttribute("Success",
						"GroupSetup Details saved successfully");

			} else {
				redirAttr.addFlashAttribute("error",
						"Error while saving GroupSetup details");
			}

		} catch (Exception exception) {
			logger.error("Exception :Unexpected error occured while Saving Group and User details");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(exception));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while Saving Group and User details");
		}
		redirAttr.addFlashAttribute("userManagementForm", userManagementForm);

		logger.debug("Exit: SaveGroupSetup");
		return UserAccess.REDIRECT_VIEW;
	}

	/**
	 * fetch applicationList details to application table for GroupSetup tab
	 * 
	 * @param model
	 * @param userManagementForm
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetAppliactionListByGroupName", method = RequestMethod.POST)
	public @ResponseBody
	String getAppliactionListByGroupName(
			@RequestParam(value = "groupId") int groupId) {
		logger.debug("Entry: GetAppliactionListByGroupName ");

		List<Application> applicationList = new ArrayList<Application>();
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();

		try {
			if (groupId > 0) {
				applicationList = applicationService.getAllApplicationDetails();

				if (applicationList != null) {
					for (Application application : applicationList) {
						obj.put(application.getApplicationId(),
								application.getApplicationName());
					}
				} else {
					obj.put("", "");
				}

				obj.writeJSONString(out);
			}

		} catch (Exception e) {
			logger.error("Exception in GetAppliactionListByGroupName error", e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: GetAppliactionListByGroupName");
		return out.toString();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetAllOrPendingApplicationForGroup", method = RequestMethod.POST)
	public @ResponseBody
	String getAllOrPendingApplicationForGroup(
			@RequestParam(value = "groupId") int groupId) {
		logger.debug("Entry: GetAllOrPendingApplicationForGroup ");

		List<Application> applicationList = new ArrayList<Application>();
		List<Application> removedApplicationList = new ArrayList<Application>();
		List<ApplicationGroupReference> applicationGroupReferencecList = new ArrayList<ApplicationGroupReference>();
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();

		try {
			if (groupId > 0) {
				applicationList = applicationService.getAllApplicationDetails();

				applicationGroupReferencecList = applicationService
						.getApplicationListForGroup(groupId);

				if (applicationList != null
						&& applicationGroupReferencecList != null) {
					for (Application application : applicationList) {
						for (ApplicationGroupReference applicationGroupReference : applicationGroupReferencecList) {
							if (application.getApplicationId() == applicationGroupReference
									.getApplicationId()) {
								if (!removedApplicationList
										.contains(application)) {
									removedApplicationList.add(application);
								}

							}
						}
					}

					applicationList.removeAll(removedApplicationList);

					for (Application application : applicationList) {
						obj.put(application.getApplicationId(),
								application.getApplicationName());
					}

				} else if (applicationList != null
						&& applicationGroupReferencecList == null) {
					for (Application application : applicationList) {
						obj.put(application.getApplicationId(),
								application.getApplicationName());
					}
				}

			} else {
				obj.put("", "");
			}

			obj.writeJSONString(out);

		} catch (Exception e) {
			logger.error(
					"Exception in GetAllOrPendingApplicationForGroup error", e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: GetAllOrPendingApplicationForGroup");
		return out.toString();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetAppliactionReferenceForGroupName", method = RequestMethod.POST)
	public @ResponseBody
	String getAppliactionReferenceForGroupName(
			@RequestParam(value = "groupId") int groupId) {
		logger.debug("Entry: GetAppliactionReferenceForGroupName ");

		Application application = new Application();
		List<ApplicationGroupReference> applicationGroupReferencecList = new ArrayList<ApplicationGroupReference>();
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();

		try {
			if (groupId > 0) {
				applicationGroupReferencecList = applicationService
						.getApplicationListForGroup(groupId);

				if (applicationGroupReferencecList != null) {
					for (ApplicationGroupReference applicationGroupReference : applicationGroupReferencecList) {
						application = applicationService
								.getApplicationForGroup(applicationGroupReference
										.getApplicationId());
						obj.put(application.getApplicationId(),
								application.getApplicationName());
					}
				} else {
					obj.put("", "");
				}

				obj.writeJSONString(out);
			}

		} catch (Exception e) {
			logger.error(
					"Exception in GetAppliactionReferenceForGroupName error", e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: GetAppliactionReferenceForGroupName");
		return out.toString();
	}

	/**
	 * fetch applicationList details to application table for GroupSetup tab
	 * 
	 * @param model
	 * @param userManagementForm
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetEnvironmentCategoryForGroup", method = RequestMethod.POST)
	public @ResponseBody
	String getEnvironmentCategoryForGroup(
			@RequestParam(value = "groupId") int groupId) {
		logger.debug("Entry: GetEnvironmentCategoryForGroup ");

		List<EnvironmentCategory> environmentCategoryList = new ArrayList<EnvironmentCategory>();
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();

		try {
			if (groupId > 0) {
				environmentCategoryList = environmentService
						.getEnvironmentCategoryList();

				if (environmentCategoryList != null) {
					for (EnvironmentCategory environmentCategory : environmentCategoryList) {
						if (environmentCategory.getEnvironmentCategoryId() != 1) {
							obj.put(environmentCategory
									.getEnvironmentCategoryId(),
									environmentCategory
											.getEnvironmentCategoryName());
						}
					}
				} else {
					obj.put("", "");
				}

				obj.writeJSONString(out);
			}

		} catch (Exception e) {
			logger.error("Exception in GetEnvironmentCategoryForGroup error", e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: GetEnvironmentCategoryForGroup");
		return out.toString();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetEnvironmentCategoryReferenceForGroup", method = RequestMethod.POST)
	public @ResponseBody
	String getEnvironmentCategoryReferenceForGroup(
			@RequestParam(value = "groupId") int groupId) {
		logger.debug("Entry: GetEnvironmentCategoryReferenceForGroup ");

		EnvironmentCategory environmentCategory = new EnvironmentCategory();
		List<EnvironmentCategoryGroupXref> environmentGroupReferenceList = new ArrayList<EnvironmentCategoryGroupXref>();
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();

		try {
			if (groupId > 0) {
				environmentGroupReferenceList = environmentService
						.getEnvironmentListForGroup(groupId);

				if (environmentGroupReferenceList != null) {
					for (EnvironmentCategoryGroupXref environmentCategoryGroupXref : environmentGroupReferenceList) {
						environmentCategory = environmentService
								.getEnvironmentCategoryForGroup(environmentCategoryGroupXref
										.getEnvironmentCategoryId());
						obj.put(environmentCategory.getEnvironmentCategoryId(),
								environmentCategory
										.getEnvironmentCategoryName());
					}
				} else {
					obj.put("", "");
				}

				obj.writeJSONString(out);
			}

		} catch (Exception e) {
			logger.error(
					"Exception in GetEnvironmentCategoryReferenceForGroup error",
					e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: GetEnvironmentCategoryReferenceForGroup");
		return out.toString();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetAllOrPendingEnvironmentCategoryForGroup", method = RequestMethod.POST)
	public @ResponseBody
	String getAllOrPendingEnvironmentCategoryForGroup(
			@RequestParam(value = "groupId") int groupId) {
		logger.debug("Entry: GetAllOrPendingEnvironmentCategoryForGroup ");

		List<EnvironmentCategory> environmentCategoryList = new ArrayList<EnvironmentCategory>();
		List<EnvironmentCategory> removedEnvironmentCategoryList = new ArrayList<EnvironmentCategory>();
		List<EnvironmentCategoryGroupXref> environmentGroupReferenceList = new ArrayList<EnvironmentCategoryGroupXref>();
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();

		try {
			if (groupId > 0) {
				environmentCategoryList = environmentService
						.getEnvironmentCategoryList();

				environmentGroupReferenceList = environmentService
						.getEnvironmentListForGroup(groupId);

				if (environmentCategoryList != null
						&& environmentGroupReferenceList != null) {
					for (EnvironmentCategory environmentCategory : environmentCategoryList) {
						for (EnvironmentCategoryGroupXref environmentCategoryGroupXref : environmentGroupReferenceList) {
							if (environmentCategory.getEnvironmentCategoryId() == environmentCategoryGroupXref
									.getEnvironmentCategoryId()) {
								if (!removedEnvironmentCategoryList
										.contains(environmentCategory)) {
									removedEnvironmentCategoryList
											.add(environmentCategory);
								}

							}
						}
					}

					environmentCategoryList
							.removeAll(removedEnvironmentCategoryList);

					for (EnvironmentCategory environmentCategory : environmentCategoryList) {
						//if (environmentCategory.getEnvironmentCategoryId() != 1) {
							obj.put(environmentCategory
									.getEnvironmentCategoryId(),
									environmentCategory
											.getEnvironmentCategoryName());
						//}
					}

				} else if (environmentCategoryList != null
						&& environmentGroupReferenceList == null) {
					for (EnvironmentCategory environmentCategory : environmentCategoryList) {
						//if (environmentCategory.getEnvironmentCategoryId() != 1) {
							obj.put(environmentCategory
									.getEnvironmentCategoryId(),
									environmentCategory
											.getEnvironmentCategoryName());
						//}
					}
				}

			} else {
				obj.put("", "");
			}

			obj.writeJSONString(out);

		} catch (Exception e) {
			logger.error(
					"Exception in GetAllOrPendingEnvironmentCategoryForGroup error",
					e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: GetAllOrPendingEnvironmentCategoryForGroup");
		return out.toString();
	}

	// GroupSetup Tab - ends here

	// Add New Menu - starts here
	@RequestMapping(value = { "/AddMenu" }, method = RequestMethod.POST)
	public String addMenu(
			@ModelAttribute UserManagementForm userManagementForm, Model model,
			RedirectAttributes redirAttr, HttpServletRequest request) {
		logger.debug("Entry: AddMenu");
		userManagementForm
				.setSetupTabNumber(UserAccess.MANAGE_ADDNEWMENU_SETUP);
		try {
			if (menuService.addNewMenu(userManagementForm)) {
				redirAttr
						.addFlashAttribute("Success",
								"Menu saved Successfully. Added menu will appear in the next login");
			} else {
				redirAttr.addFlashAttribute("Error",
						"Error while adding new menu");
			}
		} catch (Exception e) {
			logger.error("Exception in Class: AccessController, Method:accessControl(), Exception:"
					+ e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			redirAttr.addFlashAttribute("Error",
					"Unexpected Error while adding new menu");
		}
		redirAttr.addFlashAttribute("userManagementForm", userManagementForm);
		logger.info("Exit: addFunc");
		return UserAccess.REDIRECT_VIEW;

	}

	// Add New Menu - ends here

	// Add New Functionality - starts here
	@RequestMapping(value = { "/AddFunctionality" }, method = RequestMethod.POST)
	public String addFunctionality(
			@ModelAttribute UserManagementForm userManagementForm, Model model,
			RedirectAttributes redirAttr, HttpServletRequest request) {
		logger.debug("Entry: AddFunctionality");

		FunctionalityPermission functionality = null;
		try {
			userManagementForm
					.setSetupTabNumber(UserAccess.MANAGE_ADDNEWFUNCTIONALITY_SETUP);
			redirAttr.addFlashAttribute("userManagementForm",
					userManagementForm);

			functionality = new FunctionalityPermission();

			functionality.setFunctionalityName(userManagementForm
					.getFunctionalityName());
			functionality.setMenuId(Integer.parseInt(userManagementForm
					.getPageName()));

			if (menuService.addFunctionality(functionality)) {
				redirAttr.addFlashAttribute("Success",
						"New Functionality saved Successfully.");
			} else {
				redirAttr.addFlashAttribute("Error",
						"Error while adding New Functionality");
			}

		} catch (Exception e) {
			logger.error("Exception in Class: AddFunctionality, Exception:" + e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		redirAttr.addFlashAttribute("userManagementForm", userManagementForm);

		logger.debug("Exit: AddFunctionality");
		return UserAccess.REDIRECT_VIEW;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetFunctionalityListByPageName", method = RequestMethod.POST)
	public @ResponseBody
	String getFunctionalityListByPageName(
			@RequestParam(value = "functionalityId") int functionalityId) {
		logger.debug("Entry: GetFunctionalityListByPageName ");

		List<FunctionalityPermission> functionalityList = new ArrayList<FunctionalityPermission>();
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();

		try {
			if (functionalityId > 0) {
				functionalityList = menuService
						.getFunctionality(functionalityId);

				if (functionalityList != null) {
					for (FunctionalityPermission functionality : functionalityList) {
						obj.put(functionality.getFunctionalityId(),
								functionality.getFunctionalityName());
					}
				} else {
					obj.put("", "");
				}

				obj.writeJSONString(out);
			}

		} catch (Exception e) {
			logger.error("Exception in GetFunctionalityListByPageName error", e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: GetFunctionalityListByPageName");
		return out.toString();
	}

	// Add New Functionality - ends here

	/* User Access control starts here */

	@RequestMapping(value = "/SaveAccessControl", method = RequestMethod.POST)
	public String saveAccessControl(RedirectAttributes redirAttr, Model model,
			@ModelAttribute UserManagementForm userManagementForm,
			HttpServletRequest request) {

		logger.debug("Entry: SaveAccessControl, UserManagementForm->"
				+ userManagementForm);
		int groupId = 0;
		boolean insertStatus = false;
		boolean deleteStatus = false;
		List<Integer> menuIdList = null;
		List<Integer> functionalityIdList = null;
		
		if(userManagementForm.getMenuIdList() != null) {
			menuIdList = Arrays.asList(userManagementForm
					.getMenuIdList());
		}
		if(userManagementForm.getFunctionalityIdList() != null) {
			functionalityIdList = Arrays.asList(userManagementForm
					.getFunctionalityIdList());
		}

		try {
			userManagementForm.setSetupTabNumber(UserAccess.MANAGE_MENU_SETUP);
			groupId = userManagementForm.getGroupId();

			deleteStatus = userManagementService
					.deleteExistingUserMenuAccess(groupId);
			deleteStatus = userManagementService.deleteExistingFunctionalityAccess(groupId);
			List<MintMenu> menuList = (List<MintMenu>) request.getSession()
					.getAttribute("menuList");
			Integer menuId = 0;

			for (MintMenu menu : menuList) {
				menuId = menu.getMenu().getMenuId();
				insertStatus = userManagementService.saveUserMenuAccess(
							groupId, menuId);
				for (MenuAccess menuAccess : menu.getChildMenus()) {
					menuId = menuAccess.getMenuId();
					if (menuIdList != null && menuIdList.contains(menuId)) {
						insertStatus = userManagementService
								.saveUserMenuAccess(groupId, menuId);
					}
				}
			}

			List<FunctionalityPermission> functionalityPermissionList = (List<FunctionalityPermission>) request
					.getSession().getAttribute("functionalityPermissionList");
			Integer functionalityId = 0;

			for (FunctionalityPermission functionalityPermission : functionalityPermissionList) {
				functionalityId = functionalityPermission.getFunctionalityId();
				if (functionalityIdList != null && functionalityIdList.contains(functionalityId)) {
					insertStatus = userManagementService.saveFuncMenuAccess(
							groupId, functionalityId);

				}
			}
			redirAttr.addFlashAttribute("userManagementForm",
					userManagementForm);
			redirAttr.addFlashAttribute("menuPaginationCount",
					getMenuPaginateCount(menuList));
			if (insertStatus) {
				redirAttr.addFlashAttribute("Success",
						"Access Details have been submitted successfully");
			} else {
				redirAttr
						.addFlashAttribute("Success",
								"Some issues while assigning access to User/Roles. Please try again");
			}
		} catch (Exception e) {
			logger.error("Exception in SaveAccessControl.", e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.info("Exit: AccessControlSubmit");
		return UserAccess.REDIRECT_VIEW;

	}

	@RequestMapping(value = "/CheckSelectedMenu", method = RequestMethod.POST)
	public @ResponseBody
	String checkSelectedMenu(@Valid @RequestParam(value = "userId") int userId,
			@Valid @RequestParam(value = "groupId") int groupId,
			@Valid @RequestParam(value = "menuId") int menuId, boolean isFunctionality) {

		logger.debug("Entry: CheckSelectedMenu");
		StringBuilder respStr = new StringBuilder();

		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		List<UserGroupMenuReference> userGroupMenuReferenceList = null;
		List<FunctionalityGroupRef> functionalityGroupRefList = null;
		try {

			userGroupMenuReferenceList = userManagementService
					.getUserMenuAccessDetails(groupId);
			
			functionalityGroupRefList = userManagementService.getFunctionalityGroupRef(groupId);
			if (userGroupMenuReferenceList != null && isFunctionality == false) {
				for (UserGroupMenuReference userGroupMenuReference : userGroupMenuReferenceList) {
					if (userGroupMenuReference.getMenuId() == menuId) {
						obj.put(menuId, "M");
					}
				}
			}
			
			if (functionalityGroupRefList != null && isFunctionality == true) {
				for (FunctionalityGroupRef functionalityGroupRef : functionalityGroupRefList) {
					if (functionalityGroupRef.getFunctionalityId() == menuId) {
						obj.put(menuId, "F");
					}
				}
			}
			obj.writeJSONString(out);
		} catch (Exception exp) {
			logger.error(
					"Exception occured during checkSelectedMenu execution.",
					exp);
		}

		logger.info("checkSelectedMenu respStr :" + respStr);
		logger.info("Exit: checkSelectedMenu");
		return out.toString();
	}

	private int getMenuPaginateCount(List<MintMenu> mintMenuList) {
		int listsize = 0;
		int paginateSize = 0;
		if (null == mintMenuList || mintMenuList.size() < 1) {
			return 0;
		}

		for (MintMenu mintMenu : mintMenuList) {
			if (mintMenu.getMenu().getMenuId() > 0) {
				listsize++;
			}
		}
		paginateSize = (listsize / 2) + (listsize % 2);
		return paginateSize;
	}

	private int getUserAccessSetupTabNumber(Map<?, ?> map) {
		int setUpTabNumber = 1;

		if (map.containsKey("setupTab")) {
			UserManagementForm userManagementForm = (UserManagementForm) map
					.get("setupTab");
			setUpTabNumber = userManagementForm.getSetupTabNumber();
		}

		return setUpTabNumber;
	}

	private boolean isMenuAccessSetupDetailsAvailable(Map<String, Object> asMap) {
		if (!asMap.containsKey("groupList")) {
			return false;
		}
		return true;
	}

	private boolean isUserSetupDetailsAvailable(Map<String, Object> asMap) {
		if (asMap.containsKey("usersDetails")
				&& asMap.containsKey("groupsDetails")) {
			return true;
		}
		return false;
	}
}
