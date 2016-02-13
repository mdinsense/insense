package com.ensense.insense.web.common;

import com.ensense.insense.data.common.entity.Users;
import com.ensense.insense.data.common.model.IdNamePair;
import com.ensense.insense.data.common.model.UsageReportConstants;
import com.ensense.insense.data.common.model.UsageReportForm;
import com.ensense.insense.data.common.model.UsageReportResult;
import com.ensense.insense.data.common.utils.Constants;
import com.ensense.insense.core.analytics.utils.ExcelWriter;
import com.ensense.insense.core.analytics.utils.UIFunctionalityType;
import com.ensense.insense.services.common.HomeService;
import com.ensense.insense.services.common.MenuService;
import com.ensense.insense.services.common.UserService;
import com.ensense.insense.services.reports.ScheduledService;
import com.ensense.insense.services.reports.TestScheduleService;
import com.ensense.insense.services.uiadmin.ApplicationService;
import com.ensense.insense.services.uiadmin.EnvironmentService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class UsageReportsController {
	private static Logger logger = Logger
			.getLogger(UsageReportsController.class);

	@Autowired
	private ServletContext servletContext;

	@Autowired
	private HttpServletRequest httpRequest;

	@Autowired
	private UserService userService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private HomeService homeService;

	@Autowired
	private ScheduledService scheduledService;

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private EnvironmentService environmentService;

	@Autowired
	private TestScheduleService testScheduleService;

	@Autowired
	private MessageSource configProperties;

	@RequestMapping(value = { "/UsageReports" }, method = RequestMethod.GET)
	public String usageReportsGet(Model model) {

		logger.debug("Entry: usageReportsGet");
		UsageReportForm reportForm = null;
		if (null != model.asMap() && model.asMap().containsKey("reportForm")) {
			reportForm = (UsageReportForm) model.asMap().get("reportForm");
		} else {
			reportForm = new UsageReportForm();
			reportForm.setTab(getUiSetupTabNumber(model.asMap()));
		}
		model.addAttribute("reportForm", reportForm);
		try {
			switch (reportForm.getTab()) {
			
			case UsageReportConstants.REPORT_CHART: {

			}
			case UsageReportConstants.DETAILED_REPORT: {
				model.addAttribute("usersDetails",
						userService.getAllUsersDetails());
				model.addAttribute("groupsDetails",
						userService.getAllGroupsDetails());
				model.addAttribute("functionalities",
						UIFunctionalityType.values());
				model.addAttribute("solutionTypes", Constants.SolutionType.values());
				model.addAttribute("environmentList",
						environmentService.getEnvironmentCategoryList());
				break;
			}
			}

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading Application home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: usageReportsGet, ");
		return Constants.USAGEREPORTS.VIEW;
	}

	@RequestMapping(value = { "/UsageReports" }, method = RequestMethod.POST)
	public String usageReportsForm(@ModelAttribute UsageReportForm reportForm,
			Model model, RedirectAttributes redirAttr) {
		logger.debug("Entry: usageReportsForm, functionalityTesting->"
				+ reportForm);
		try {
			switch (reportForm.getTab()) {
				
				case UsageReportConstants.REPORT_CHART: {
					break;
				}
				case UsageReportConstants.DETAILED_REPORT: {
					redirAttr.addFlashAttribute("usersDetails",
							userService.getAllUsersDetails());
					redirAttr.addFlashAttribute("groupsDetails",
							userService.getAllGroupsDetails());
					redirAttr.addFlashAttribute("functionalities",
							UIFunctionalityType.values());
					redirAttr.addFlashAttribute("solutionTypes",
							Constants.SolutionType.values());
					redirAttr.addFlashAttribute("environmentList",
							environmentService.getEnvironmentCategoryList());
					break;
				}
			}

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading Application home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		redirAttr.addFlashAttribute("reportForm", reportForm);
		logger.debug("Exit: usageReportsForm, uiTestingSetupForm->");
		// redirAttr.addFlashAttribute("testingForm", testingForm);
		return Constants.USAGEREPORTS.REDIRECT_VIEW;
	}

	@RequestMapping(value = { "/GetReportList" }, method = RequestMethod.POST)
	public String getReportList(@ModelAttribute UsageReportForm form,
			Model model, RedirectAttributes redirAttr) {
		logger.debug("Entry: usageReportsForm, getReportList->" + form);
		try {
			redirAttr.addFlashAttribute("reportList",
					homeService.getUsageReportList(form));
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading Application home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		redirAttr.addFlashAttribute("reportForm", form);
		redirAttr.addFlashAttribute("searchResult", true);
		logger.debug("Exit: usageReportsForm, uiTestingSetupForm->");
		return Constants.USAGEREPORTS.REDIRECT_VIEW;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GeneratePieChart", method = RequestMethod.POST)
	public @ResponseBody
	String generatePieChart(
			@RequestParam(value = "criteriaId") int criteriaId,
			@RequestParam(value = "fromDate") String fromDate,
			@RequestParam(value = "toDate") String toDate) {
		logger.debug("Entry: usageReportsForm, generatePieChart->");
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		List<IdNamePair> chartData = new ArrayList<IdNamePair>();
		try {
			chartData = homeService.getChartData(criteriaId, fromDate, toDate);
			for(IdNamePair data : chartData) {
				obj.put(data.getName(), data.getId());
			}
			obj.writeJSONString(out);
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading Application home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
	
		logger.debug("Exit: usageReportsForm, generatePieChart->");
		return out.toString();
	}

	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GenerateBarChart", method = RequestMethod.POST)
	public @ResponseBody
	String generateBarChart(
			@RequestParam(value = "criteriaId") int criteriaId,
			@RequestParam(value = "id") String id,
			@RequestParam(value = "fromDate") String fromDate,
			@RequestParam(value = "toDate") String toDate,
			@RequestParam(value = "position") int position) 
		 {
		logger.debug("Entry: usageReportsForm, generatePieChart->");
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		List<IdNamePair> chartData = new ArrayList<IdNamePair>();
		try {
			chartData = homeService.getBarChartData(criteriaId, Integer.parseInt(id), fromDate, toDate,position);
			for(IdNamePair data : chartData) {
				obj.put(data.getName(), data.getId());
			}
			obj.writeJSONString(out);
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading Application home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
	
		logger.debug("Exit: usageReportsForm, generatePieChart->");
		return out.toString();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetUsersDetailForUsageReports", method = RequestMethod.POST)
	public @ResponseBody
	String getUsersDetailForUsageReports(
			@RequestParam(value = "groupName") String groupName) {
		logger.debug("Entry: getUsersDetailForUsageReports ");

		List<Users> usersList = new ArrayList<Users>();
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		String[] groupArray = null;
		try {
			System.out.println("groupName" + groupName);

			if (groupName.equals("0")) {
				usersList = userService.getAllUsersDetails();
			} else {
				if (groupName.contains(",")) {
					groupArray = groupName.split(",");
					for (String groupId : groupArray) {
						usersList.addAll(userService
								.getUsersDetailsWithGroupId(Integer
										.parseInt(groupId)));
					}
				} else {
					usersList.addAll(userService
							.getUsersDetailsWithGroupId(Integer
									.parseInt(groupName)));
				}

			}
			if (usersList != null) {
				for (Users user : usersList) {

					obj.put(user.getUserId(), user.getUserName());
				}
			} else {
				obj.put("", "");
			}
			obj.writeJSONString(out);
		} catch (Exception e) {
			logger.error("Exception in getUsersDetailForUsageReports error", e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getUsersDetailForUsageReports");
		return out.toString();
	}

	@RequestMapping(value = "/GetFunctionalityForSolutionType", method = RequestMethod.POST)
	public @ResponseBody
	List<IdNamePair> GetFunctionalityForSolutionType(
			@RequestParam(value = "solutionTypeId") String solutionTypeId) {
		logger.debug("Entry: GetFunctionalityForSolutionType ");
		List<UIFunctionalityType> functionalityTypeList = new ArrayList<UIFunctionalityType>();
		List<IdNamePair> pairList = new ArrayList<IdNamePair>();
		IdNamePair pair = null;
		String[] solutionArray = null;
		try {
			if (solutionTypeId.contains(",")) {
				solutionArray = solutionTypeId.split(",");
			} else {
				solutionArray = new String[1];
				solutionArray[0] = solutionTypeId;
			}
			for (String solTypeId : solutionArray) {
				int solnTypeId = Integer.parseInt(solTypeId);
				if (Constants.SolutionType.UI_TESTING.getSolutionTypeId() == solnTypeId) {
					functionalityTypeList
							.add(UIFunctionalityType.FUNCTIONAL_TESTING);
					functionalityTypeList
							.add(UIFunctionalityType.ANALYTICS_TESTING);
					functionalityTypeList.add(UIFunctionalityType.BROKENLINKS);
					functionalityTypeList
							.add(UIFunctionalityType.AKAMAI_TESTING);
					functionalityTypeList
					.add(UIFunctionalityType.FIND_TEXT_IMAGE);
				} else if (Constants.SolutionType.WEBSERVICE.getSolutionTypeId() == solnTypeId) {
					functionalityTypeList
							.add(UIFunctionalityType.WEBSERVICE_TESTING);
				} else if (Constants.SolutionType.MISCELANEOUS.getSolutionTypeId() == solnTypeId) {
					functionalityTypeList.add(UIFunctionalityType.OPRA_RESET);
					functionalityTypeList
							.add(UIFunctionalityType.MVC_OPRA_RESET);
				} else {
					for (UIFunctionalityType funct : UIFunctionalityType
							.values()) {
						functionalityTypeList.add(funct);
					}
				}
			}
			for (UIFunctionalityType funct : functionalityTypeList) {
				pair = new IdNamePair();
				pair.setId(funct.getFunctionalityTypeId());
				pair.setName(funct.getFunctionalityName());
				pairList.add(pair);
			}
		} catch (Exception e) {
			logger.error("Exception in GetFunctionalityForSolutionType error",
					e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: GetFunctionalityForSolutionType");
		return pairList;
	}

	@RequestMapping(value = "/DownloadUsageReport", method = RequestMethod.POST)
	public void DownloadUsageReport(@ModelAttribute UsageReportForm form,
			HttpServletRequest request, HttpServletResponse response) {
		logger.debug("Entry: DownloadUsageReport");
		List<UsageReportResult> reportResults = null;
		try {
			reportResults = homeService.getUsageReportList(form);
			ExcelWriter.downloadUsageReport(reportResults, response);
		} catch (Exception e) {
			logger.error("Exception in DownloadUsageReport");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: DownloadUsageReport");
	}

	private int getUiSetupTabNumber(Map<?, ?> map) {
		int setUpTabNumber = 1;

		if (map.containsKey("tab")) {
			UsageReportForm usageReportForm = (UsageReportForm) map
					.get("tab");
			setUpTabNumber = usageReportForm.getTab();
		}

		return setUpTabNumber;
	}

}
