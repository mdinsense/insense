package com.ensense.insense.web.common;


import com.ensense.insense.core.analytics.utils.*;
import com.ensense.insense.core.analytics.utils.JsonReaderWriter;
import com.ensense.insense.data.common.utils.*;
import com.ensense.insense.data.crawler.model.CompareConfig;
import com.ensense.insense.core.webservice.model.WSSuiteParams;
import com.ensense.insense.data.analytics.model.AnalyticsDetails;
import com.ensense.insense.data.analytics.model.WebAnalyticsTagData;
import com.ensense.insense.data.common.entity.*;
import com.ensense.insense.data.common.model.*;
import com.ensense.insense.data.uitesting.entity.*;
import com.ensense.insense.data.utils.*;
import com.ensense.insense.data.utils.DateTimeUtil;
import com.ensense.insense.data.webservice.entity.*;
import com.ensense.insense.data.webservice.model.WSReportsData;
import com.ensense.insense.data.webservice.model.WebserviceSetupForm;
import com.ensense.insense.data.webservice.model.WsDataset;
import com.ensense.insense.services.analytics.model.AnalyticsSummaryReportUi;
import com.ensense.insense.services.common.HomeService;
import com.ensense.insense.services.common.MenuService;
import com.ensense.insense.services.common.UserService;
import com.ensense.insense.services.common.utils.MintFileUtils;
import com.ensense.insense.services.crawler.model.UiReportsSummary;
import com.ensense.insense.data.model.uiadmin.form.schedule.CompareLink;
import com.ensense.insense.services.reports.ScheduledService;
import com.ensense.insense.services.reports.TestScheduleService;
import com.ensense.insense.services.uiadmin.ApplicationModuleService;
import com.ensense.insense.services.uiadmin.ApplicationService;
import com.ensense.insense.services.uiadmin.EnvironmentService;
import com.ensense.insense.data.uiadmin.form.schedule.mintv4.TestSuitDetails;
import com.ensense.insense.services.uiadmin.mintv4.TestSuitService;
import com.ensense.insense.services.webservice.WebserviceTestingService;
import com.ensense.insense.web.uiadmin.form.schedule.FeedBackForm;
import com.ensense.insense.web.uiadmin.form.schedule.RegressionTestExecutionForm;
import com.ensense.insense.web.uiadmin.form.schedule.TestingForm;
import com.ensense.insense.web.uiadmin.form.schedule.TransactionSummaryReport;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.StringWriter;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class HomeController {
	private static Logger logger = Logger.getLogger(HomeController.class);
	
	@Autowired
	private ServletContext servletContext;

	@Autowired
	private HttpServletRequest httpRequest;
	
	@Autowired
	private HttpServletRequest context;
	
	@Autowired
	UserService userService;
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	HomeService homeService;

	@Autowired
	ScheduledService scheduledService;
	
	@Autowired
	ApplicationService applicationService;
	
	@Autowired
	private ApplicationModuleService applicationModuleService;
	
	@Autowired
	EnvironmentService environmentService;
	
	@Autowired
	TestScheduleService testScheduleService;
	
	@Autowired
	WebserviceTestingService webserviceTestingService;
	
	@Autowired
	TestSuitService testSuitService;
	
	@Autowired
	private MessageSource configProperties;
	
	@RequestMapping(value = {"/login"}, method = RequestMethod.GET)
	public ModelAndView LoginHome(@ModelAttribute("model") ModelMap model) {
		logger.debug("Entry and Exit: LoginHome");
		if(UserServiceUtils.isConfigPage(httpRequest)) {
			return mintPropertyconfig(model);
		}

		return new ModelAndView("common/Login", model);
	}
	
	@RequestMapping(value = {"/login"}, method = RequestMethod.POST)
	public ModelAndView LoginHomePost(@ModelAttribute("model") ModelMap model) {
		logger.debug("Entry and Exit: LoginHomePost");
		return LoginHome(model);
	}
	
	@RequestMapping(value = {"/redirectUrl"}, method = RequestMethod.GET)
	public String redirectUrl(@ModelAttribute("model") ModelMap model) {
		logger.debug("Entry and Exit: redirectUrl");
		
		logger.info("redirectUrl :"+httpRequest.getAttribute("redirectUrl"));
		String redirectUrl = "";
		if ( null != httpRequest.getAttribute("redirectUrl") ){
			httpRequest.getAttribute("redirectUrl").toString();
		}
		return "redirect:"+redirectUrl;
	}
	
    @RequestMapping(value="/PageNotFound")
    public String handlePageNotFound() {
        return "common/PageNotFound";
    }
    
	@RequestMapping(value = {"/logout"}, method = RequestMethod.GET)
	public ModelAndView Logout(@ModelAttribute("model") ModelMap model) {
		logger.debug("Entry and Exit: Logout");
		
		HttpSession session = httpRequest.getSession(false);
		if (session != null){
		  session.setMaxInactiveInterval(1); // so it expires immediatly
		}
		
		if ( homeService.isLoginFromLocal(httpRequest)){
			return new ModelAndView("common/Login", model);
		}else{
			String logoutUrl = PropertyFileReader.getProperty(configProperties, "mint.logout.url");
			return new ModelAndView("redirect:"+logoutUrl, model);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView defaultHome(@ModelAttribute("model") ModelMap model) {
		logger.debug("Entry and Exit: defaultHome");
		if(UserServiceUtils.isConfigPage(httpRequest)) {
			return mintPropertyconfig(model);
		}
		if(UserServiceUtils.guardianPage(httpRequest)) {
			return new ModelAndView("guardian/GuardianHome", model);
		}
		return LoginHome(model);
	}
	
	@RequestMapping(value = "/UiMonitorJob", method = RequestMethod.POST)
	public String monitorJobs(RedirectAttributes redirAttr,
							  Model model, @ModelAttribute RegressionTestExecutionForm regressionTestExecutionForm) {
		logger.debug("Entry: monitorJobs");
		ScheduleExecution scheduleExecution = new ScheduleExecution();
		List<ScheduleExecutionDetail> scheduleExecutionDetailList = new ArrayList<ScheduleExecutionDetail>();
		
		if ( null != regressionTestExecutionForm.getScheduleExecutionId() && regressionTestExecutionForm.getScheduleExecutionId() > 0 ){
			scheduleExecution = scheduledService.getScheduleExecution(regressionTestExecutionForm.getScheduleExecutionId());
			
			if ( null != regressionTestExecutionForm.getScheduleAction() && regressionTestExecutionForm.getScheduleAction().trim().length() > 0 ){
				if ( scheduledService.updateCrawlStatus(scheduleExecution, regressionTestExecutionForm.getScheduleAction()) ){
					scheduleExecution = scheduledService.getScheduleExecution(scheduleExecution.getScheduleExecutionId());
					scheduleExecution.setTestExecutionStatusRefId(Integer.parseInt(regressionTestExecutionForm.getScheduleAction()));
					scheduledService.updateScheduleExecution(scheduleExecution, Constants.EXECUTION_STATUS_UPDATE);
					
					logger.info("Update the Crawl Status for the Schedule Execution Id :"+scheduleExecution.getScheduleExecutionId() 
							+ " to :"+ ExecutionStatus.getExecutionStatus(Integer.parseInt(regressionTestExecutionForm.getScheduleAction())).getStatus());
				}else{
					int scheduleAction = 0;
					try{
						scheduleAction = Integer.parseInt(regressionTestExecutionForm.getScheduleAction());
					}catch(Exception e){
						logger.error("Invalid Schedule Action :"+regressionTestExecutionForm.getScheduleAction());
					}
					scheduleExecution = scheduledService.getScheduleExecution(scheduleExecution.getScheduleExecutionId());
					
					if ( scheduleAction == ExecutionStatus.RESTART.getStatusCode() ){
						scheduleExecution.setTestExecutionStatusRefId(ExecutionStatus.PENDING.getStatusCode());
					}else {
						scheduleExecution.setTestExecutionStatusRefId(scheduleAction);
					}
					scheduledService.updateScheduleExecution(scheduleExecution, Constants.EXECUTION_STATUS_UPDATE);
					
					logger.info("Update the Crawl Status for the Schedule Execution Id :"+scheduleExecution.getScheduleExecutionId() 
							+ " to :"+ scheduleAction);
				}
			}
		}
		
		scheduleExecutionDetailList = scheduledService.getPendingInProgressScheduleDetails();
		
		List<ScheduleStatus> scheduleStatusList = new ArrayList<ScheduleStatus>();

		scheduleStatusList = scheduledService.getScheduleStatusList(scheduleExecutionDetailList);
		logger.info("scheduleStatusList :"+scheduleStatusList);
		model.addAttribute("scheduleStatusList", scheduleStatusList);
		model.addAttribute("isShowUpdateJobStatus", UserServiceUtils.isUserHasAccessToFunctionality(httpRequest, Constants.Functionality.UPDATE_JOB_STATUS));
		logger.debug("Exit: monitorJobs");
		return "common/UiMonitorJobs";
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = {"/Home"})
	public ModelAndView Home(@ModelAttribute("model") ModelMap model, 
			@ModelAttribute RegressionTestExecutionForm regressionTestExecutionForm) {
		logger.debug("Entry: Home, regressionTestExecutionForm->"+regressionTestExecutionForm);
		int currentUserId = 0;
		int currentGroupId = 0;
		Groups group = new Groups();
		int setupTabNumber = 0;
		
		if ( null != regressionTestExecutionForm && null != regressionTestExecutionForm.getSetupTabNumber() ){
			setupTabNumber = regressionTestExecutionForm.getSetupTabNumber();
		}
		model.addAttribute("solutionType", regressionTestExecutionForm.getSolutionType());
		if(regressionTestExecutionForm.getSolutionType()<=0){
			regressionTestExecutionForm.setSolutionType(Constants.SolutionType.UI_TESTING.getSolutionTypeId());
			model.addAttribute("solutionType", Constants.SolutionType.UI_TESTING.getSolutionTypeId());
		
		}
		List<ScheduleExecutionDetail> scheduleExecutionDetailList = new ArrayList<ScheduleExecutionDetail>();
		
		try{
			switch (setupTabNumber) {
			case 2:
				model.addAttribute("moduleList",applicationModuleService.getTransactionalModulesForSuit(Integer.parseInt(regressionTestExecutionForm.getSuitId())));
				List<ScheduleExecution> scheduleExecutionList = homeService.getReportBaselineScheduleExcutionList(Integer.parseInt(regressionTestExecutionForm.getSuitId()));
				if(scheduleExecutionList != null && scheduleExecutionList.size() > 0){
					model.addAttribute("lastRunDateforScheduleExecution",scheduleExecutionList.get(0).getTestExecutionStartTime());
					model.addAttribute("ScheduleExecutionIdforlastRunDate",scheduleExecutionList.get(0).getScheduleExecutionId());
				}else{
					model.addAttribute("lastRunDateforScheduleExecution","");
				}
				
				break;
			case 3:
				model.addAttribute("reportBaselineScheduleExecutionList", 
						homeService.getReportBaselineScheduleExcutionList(Integer.parseInt(regressionTestExecutionForm.getSuitId())));
				
				break;
			case 4:
				break;
			default:
				break;
			}	
				
		}catch(Exception exp){
			logger.error("Exception occured in Home, while fetching the details for the tab ->"+regressionTestExecutionForm.getSetupTabNumber());
			logger.error("Stack Trace :"+ ExceptionUtils.getStackTrace(exp));
		}
		
		try {
					
			if(null == regressionTestExecutionForm.getSetupTabNumber()) {
				regressionTestExecutionForm.setSetupTabNumber(1);
			}
			Users currentUser = (Users) httpRequest.getSession().getAttribute("currentMintUser");
			currentGroupId = currentUser.getGroupId();
			currentUserId = currentUser.getUserId();
			
			List<String> uiSuitsCategoryList = new ArrayList<String>();
			uiSuitsCategoryList.add("Private Suits - "+currentUser.getUserName());
			uiSuitsCategoryList.add("Assigned to Group - "+currentUser.getGroup().getGroupName());
			httpRequest.getSession().setAttribute("showSetAlias", false);
			
			if(UserServiceUtils.isUserHasAccessToFunctionality(httpRequest, Constants.Functionality.SHOW_ALL_UI_SUITS)) {
				uiSuitsCategoryList.add("Created By All");	
			}
			model.addAttribute("isManageSuitAccessAvailable",UserServiceUtils.isUserHasAccessToFunctionality(httpRequest, Constants.Functionality.MANAGE_SUITS));
			model.addAttribute("isMonitorJobAccessAvailable",UserServiceUtils.isUserHasAccessToFunctionality(httpRequest, Constants.Functionality.MONITOR_JOB_STATUS));
			
			//checking if mintv4 testsuits enabled
			boolean isMintV4SuitAccess = UserServiceUtils.isUserHasAccessToFunctionality(httpRequest, Constants.Functionality.MINTV4_SUIT);
			
			List<SolutionType> solutionTypeList = homeService.getAllSolutionTypes();
			
			// for adding mintv4 testsuits
			if(isMintV4SuitAccess) {
				if(!model.containsAttribute("regressionTestSuitV4")) {
						model.addAttribute("regressionTestSuitV4",
								testSuitService.getAllTestSuitDetails());
						regressionTestExecutionForm.setTestSuitDetails(testSuitService.getAllTestSuitDetails());
				}
			}
			
			group = userService.getGroup(currentGroupId);
			if(group.isGroupAdminRights() == true){
				model.addAttribute("GroupAdminAccess",
						"YES");
			}else{
				model.addAttribute("GroupAdminAccess",
						"NO");
			}
			model.addAttribute("currentUserId",
					currentUserId);
			model.addAttribute("uiSuitsCategoryList",
					uiSuitsCategoryList);
			model.addAttribute("solutionTypeList",
					solutionTypeList);
			
				if(!model.containsAttribute("regressionTestSuit")) {
					if(regressionTestExecutionForm.getSolutionType() == Constants.SolutionType.UI_TESTING.getSolutionTypeId()){
						model.addAttribute("regressionTestSuit",
								homeService.getUiRegressionSuitForGroup(currentGroupId));
					}
					if(regressionTestExecutionForm.getUiSuitsCategory() == null) {
						model.addAttribute("selectedUiSuitCategory","Assigned to Group - "+currentUser.getGroup().getGroupName());
					} 
				}
				
				if(!model.containsAttribute("webserviceSuits")) {
					if(regressionTestExecutionForm.getSolutionType()== Constants.SolutionType.WEBSERVICE.getSolutionTypeId()){
						model.addAttribute("webserviceSuits",
								webserviceTestingService.getWSTestSuitesForGroup(currentGroupId));
					}
					if(regressionTestExecutionForm.getUiSuitsCategory() == null) {
						model.addAttribute("selectedUiSuitCategory","Assigned to Group - "+currentUser.getGroup().getGroupName());
					}
				}
				
				if(regressionTestExecutionForm.getUiSuitsCategory() !=null){
					if(regressionTestExecutionForm.getUiSuitsCategory().equals("Private Suits - "+currentUser.getUserName())) {
						if(regressionTestExecutionForm.getSolutionType() == Constants.SolutionType.UI_TESTING.getSolutionTypeId()){
							model.addAttribute("regressionTestSuit",
									homeService.getUiRegressionSuitForUser(currentUserId));
						} else if(regressionTestExecutionForm.getSolutionType() == Constants.SolutionType.WEBSERVICE.getSolutionTypeId()){
							model.addAttribute("webserviceSuits",
									webserviceTestingService.getWSTestSuitesForUser(currentUserId));
						}
						if(model.get("selectedUiSuitCategory") == null) {
						model.addAttribute("selectedUiSuitCategory","Private Suits - "+currentUser.getUserName());
						}
					}  else if(regressionTestExecutionForm.getUiSuitsCategory().equals("Created By All")) {
						if(regressionTestExecutionForm.getSolutionType() == Constants.SolutionType.UI_TESTING.getSolutionTypeId()){
							model.addAttribute("regressionTestSuit",homeService.getSavedSuitsCreatedByAll());
						} else if(regressionTestExecutionForm.getSolutionType() == Constants.SolutionType.WEBSERVICE.getSolutionTypeId()){
							model.addAttribute("webserviceSuits",webserviceTestingService.getWSTestSuitesCreatedByAll());
						}
						if(regressionTestExecutionForm.getUiSuitsCategory() == null) {
						model.addAttribute("selectedUiSuitCategory","Created By All");
						}
					} 	
				}
		
			
			
			model.addAttribute("applicationList",
					applicationService.getApplicationListForGroup(currentGroupId));
			model.addAttribute("environmentCategoryList",
					environmentService.getEnvironmentCategoryList());
			model.addAttribute("regressionTestExecutionForm", regressionTestExecutionForm);
			
			if ( regressionTestExecutionForm.getSetupTabNumber() == 2 ){
				model.addAttribute("baselineScheduleExecutionList", homeService.getBaselineScheduleExcutionList(Integer.parseInt(regressionTestExecutionForm.getSuitId())));
			}
			
			if ( regressionTestExecutionForm.getSetupTabNumber() == 3 ){
				model.addAttribute("reportBaselineScheduleExecutionList", homeService.getReportBaselineScheduleExcutionList(Integer.parseInt(regressionTestExecutionForm.getSuitId())));
			}
			if(model.get("selectedUiSuitCategory") == null){
				model.addAttribute("selectedUiSuitCategory","Assigned to Group - "+currentUser.getGroup().getGroupName());
			}
		} catch (Exception e) {
			logger.error("Exception in MintHome :"+e);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: Home");
		
		return new ModelAndView("common/Home", model);
	}
	
	@RequestMapping(value = "/GetAccess")
	public ModelAndView GetAccess(@ModelAttribute("model") ModelMap model) {		
		logger.debug("Entry and Exit: GetAccess");
		return new ModelAndView("common/GetMintAccess", model);
	}
	
	@RequestMapping(value = "/AccessDenied")
	public ModelAndView AccessDenied(@ModelAttribute("model") ModelMap model) {
		logger.debug("Entry and Exit: AccessDenied");
		return new ModelAndView("common/AccessDenied", model);
	}
	
	@RequestMapping(value = "/PageUnderProgress")
	public ModelAndView PageUnderProgress(@ModelAttribute("model") ModelMap model) {
		logger.debug("Entry and Exit: PageUnderProgress");
		return new ModelAndView("common/PageUnderProgress", model);
	}
	
	@RequestMapping(value = {"/Config"})
	public ModelAndView mintPropertyconfig(@ModelAttribute("model") ModelMap model) {
		logger.debug("Entry: mintPropertyconfig");
		try {
			model.addAttribute("mintPropertiesList",
					FileDirectoryUtil.readMintPropertiesFromFile());
			
		} catch (Exception e) {
			logger.error("Exception in Config :"+e);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: mintPropertyconfig");
		return new ModelAndView("common/config", model);
	}
	
	@RequestMapping(value = "/SaveConfig", method = RequestMethod.POST)
	public ModelAndView saveMintPropertyconfig(@ModelAttribute("model") ModelMap model, ConfigProperty configProperty) {
		logger.debug("Entry: mintPropertyconfig"+configProperty);
		try {
			if(configProperty.getPropertyName().length > 0) {
				if(homeService.saveMintConfigProperties(configProperty)) {
					model.addAttribute("Success",
							"Mint Properties Saved successfully");
				}
			}
			
		} catch (Exception e) {
			logger.error("Exception in Config :"+e);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			model.addAttribute("error",
					"Error Occured while saving mint properties");
		}
		logger.debug("Exit: mintPropertyconfig");
		return mintPropertyconfig(model);
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/SaveRegressionTestSuits", method = RequestMethod.POST)
	public String saveRegressionTestSuits(RedirectAttributes redirAttr,
			Model model, @ModelAttribute RegressionTestExecutionForm regressionTestExecutionForm) {

		logger.debug("Entry: SaveRegressionTestSuits, RegressionTestExecutionForm->"
				+ regressionTestExecutionForm);
		
		String dateval = DataConstants.KEYS.BLANK;
		DateFormat formatter;
		DateFormat fromUser = null;
		String reccurencedateval = DataConstants.KEYS.BLANK;String stringTime = DataConstants.KEYS.BLANK;
		int suitId = Integer.parseInt(regressionTestExecutionForm.getSuitId());
		ScheduleScript scheduleScript = null;
		ScheduleScriptXref scheduleScriptXref = null;
		boolean mintV4Result = false;
		boolean result = false;
		try {
			regressionTestExecutionForm.getRefreshtext(); // for Pagination - Home page textbox next to refresh button		
			Suit suit = homeService.getSavedSuits(suitId);
			if(suit.getSuitId() == null || suit.getSuitId().equals("")){
				TestSuitDetails testSuitDetails = testSuitService.getTestSuitDetails(suitId);
				mintV4Result = saveRegressionTestSuitsV4(regressionTestExecutionForm, testSuitDetails);
			}
			
			if(mintV4Result == false){
			Users user = (Users)httpRequest.getSession().getAttribute("currentMintUser");
			
			Schedule ts = new Schedule();
			ts.setSuitId(suit.getSuitId());
			ts.setApplicationId(suit.getApplicationId());
			ts.setEnvironmentId(suit.getEnvironmentId());
			ts.setStopAfter(0);
			ts.setEmailIds(user.getEmailId());
			
			if ( null != regressionTestExecutionForm.getNotes() ){
				ts.setNotes(regressionTestExecutionForm.getNotes());
			}else {
				ts.setNotes("");
			}
			if ( null != regressionTestExecutionForm.getStopAfter() ){
				ts.setStopAfter(regressionTestExecutionForm.getStopAfter());
			}
			
			ts.setBaselineScheduleExecutionId(regressionTestExecutionForm.getBaselineScheduleExecutionId());

			if( suit.getLoginId() != null ) {
				ts.setLoginUserDetailId(suit.getLoginId());
			}
			
			if(StringUtils.equals(suit.getType(), "TextOrImage")) {
				ts.setBrandingUrlReport(true);
				ts.setBrokenUrlReport(false);
			}
			
			ts.setUrlLevel(suit.getUrlLevel());
			ts.setUserId(user.getUserId());
			ts.setTransactionTestcase(false);
			ts.setStartDate(new Date());
			if(suit.getModuleId()!=null){
				ts.setModuleIds(suit.getModuleId());
			}
			ts.setTextCompare(suit.isTextCompare());
			ts.setHtmlCompare(suit.isHtmlCompare());
			ts.setScreenCompare(suit.isScreenCompare());
			ts.setBrowserTypeId(suit.getBrowserTypeId());
			ts.setSolutionTypeId(Constants.WEB_APPLICATION_TESTING);
			ts.setRegressionTesting(suit.isRegressionTesting());
			ts.setAnalyticsTesting(suit.isAnalyticsTesting());
			ts.setRobotClicking(suit.isRobotClicking());
			ts.setBrokenUrlReport(suit.isBrokenUrlReport());
			ts.setProcessChildUrls(suit.isProcessChildUrls());
			formatter = new SimpleDateFormat("HH:mm");
			fromUser = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			
			if (regressionTestExecutionForm.getScheduleType().equalsIgnoreCase(Constants.SCHEDULER.SCHEDULED_RUN)){
				ts.setScheduled(true);
				dateval = regressionTestExecutionForm.getSchDateTime();

				Date t = (Date) formatter.parse(dateval);
				Time timeValue = new Time(formatter.parse(dateval).getTime());
				ts.setStartTime(timeValue);
				
				if (regressionTestExecutionForm.getExecuteBy().equals(Constants.SCHEDULER.WEEKLY_SCHEDULE)) {
					if ( regressionTestExecutionForm.getRecurrence() != null &&  regressionTestExecutionForm.getRecurrence().equals("on") ) {
						ts.setRecurrence(true);
					} else {
						ts.setRecurrence(false);
					}
					
					ts.setScheduleType(Constants.SCHEDULER.BY_WEEKLY);//Weekwise
					
					String weekwise = DataConstants.KEYS.BLANK;
					if (regressionTestExecutionForm.getRecurrenceidweekly1() != null && regressionTestExecutionForm.getRecurrenceidweekly1().equals("on")) {
						weekwise += "Monday,";
					}
					if (regressionTestExecutionForm.getRecurrenceidweekly2() != null && regressionTestExecutionForm.getRecurrenceidweekly2().equals("on")) {
						weekwise += "Tuesday,";
					}
					if (regressionTestExecutionForm.getRecurrenceidweekly3() != null && regressionTestExecutionForm.getRecurrenceidweekly3().equals("on")) {
						weekwise += "Wednesday,";
					}
					if (regressionTestExecutionForm.getRecurrenceidweekly4() != null && regressionTestExecutionForm.getRecurrenceidweekly4().equals("on")) {
						weekwise += "Thursday,";
					}
					if (regressionTestExecutionForm.getRecurrenceidweekly5() != null && regressionTestExecutionForm.getRecurrenceidweekly5().equals("on")) {
						weekwise += "Friday,";
					}
					if (regressionTestExecutionForm.getRecurrenceidweekly6() != null && regressionTestExecutionForm.getRecurrenceidweekly6().equals("on")) {
						weekwise += "Saturday,";
					}
					if (regressionTestExecutionForm.getRecurrenceidweekly7() != null && regressionTestExecutionForm.getRecurrenceidweekly7().equals("on")) {
						weekwise += "Sunday,";
					}
					ts.setRecurrenceDateWise(null);
					ts.setScheduleDDay(weekwise.substring(0, weekwise.length()-1));
					
					
				}	else if (regressionTestExecutionForm.getExecuteBy().equals(Constants.SCHEDULER.DATE_SCHEDULE)) {
					ts.setScheduleType(Constants.SCHEDULER.BY_DATE);//Datewise
					reccurencedateval= regressionTestExecutionForm.getScheduledDate().substring(0,11) + " " + timeValue;
					Date nowDate=fromUser.parse(reccurencedateval);
					ts.setRecurrenceDateWise(nowDate);
					ts.setScheduleDDay("NA");
				}
				
			} else {
				ts.setScheduled(false);
				stringTime = formatter.format(new Date());
				Date t = (Date) formatter.parse(stringTime);
				Time timeValue = new Time(formatter.parse(stringTime).getTime());
				ts.setStartTime(timeValue);
			}
			if(regressionTestExecutionForm.getTestCases() != null && regressionTestExecutionForm.getTestCases().length > 0) {
				ts.setTransactionTestcase(true);
			}
			result = homeService.saveSchedule(ts);
			if(result && ts.isTransactionTestcase()) {
				for(int i :regressionTestExecutionForm.getTestCases()){
					scheduleScript = new ScheduleScript();
					scheduleScript.setTestCaseId(i);
					scheduleScript.setTestCaseStatusId(ExecutionStatus.PENDING.getStatusCode());
					
					scheduleScriptXref =  new ScheduleScriptXref();
					scheduleScriptXref.setScheduleId(ts.getScheduleId());
					homeService.saveScheduleScript(scheduleScript,scheduleScriptXref);
				}
				
			}
			}
			if(result==true || mintV4Result==true){
				redirAttr.addFlashAttribute("Success", "Testsuit has been scheduled");
				redirAttr.addFlashAttribute("scheduledSuitId", suitId);
				redirAttr.addFlashAttribute("scheduledSuitType", regressionTestExecutionForm.getSuitType());
				redirAttr.addFlashAttribute("checkScheduledStatus", "ON");
				redirAttr.addFlashAttribute("paginateCurrentPageNo", regressionTestExecutionForm.getCurrentPageNo());
				
				Integer currentPageNo = regressionTestExecutionForm.getCurrentPageNo();
				RegressionTestExecutionForm regressionTestExecutionForm2 = new RegressionTestExecutionForm();
				regressionTestExecutionForm2.setCurrentPageNo(currentPageNo);
				
				redirAttr.addFlashAttribute("regressionTestExecutionForm", regressionTestExecutionForm2);
			}else{
				redirAttr.addFlashAttribute("error",
				"Error while scheduling the testsuit");
			}
		} catch (Exception exception) {
			logger.error("Exception :Unexpected error occured while Saving Group and User details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(exception));
			redirAttr.addFlashAttribute("error",
			"Error while scheduling the testsuit");
			
		}
		logger.debug("Exit: SaveRegressionTestSuits");
		return "redirect:/Home.ftl";
	}
	
	private boolean saveRegressionTestSuitsV4(RegressionTestExecutionForm regressionTestExecutionForm, TestSuitDetails testSuitDetails){
		boolean isSaved =false;
		String dateval = DataConstants.KEYS.BLANK;
		DateFormat formatter;
		DateFormat fromUser = null;
		String reccurencedateval = DataConstants.KEYS.BLANK;String stringTime = DataConstants.KEYS.BLANK;
		Users user = (Users)httpRequest.getSession().getAttribute("currentMintUser");
		try{
			Schedule ts = new Schedule();
			ts.setSuitId(testSuitDetails.getTestSuit().getSuitId());
			ts.setApplicationId(-1);
			ts.setEnvironmentId(-1);
			ts.setStopAfter(0);
			ts.setEmailIds(user.getEmailId());
			if ( null != regressionTestExecutionForm.getNotes() ){
				ts.setNotes(regressionTestExecutionForm.getNotes());
			}else {
				ts.setNotes("");
			}
			if ( null != regressionTestExecutionForm.getStopAfter() ){
				ts.setStopAfter(regressionTestExecutionForm.getStopAfter());
			}
			
			ts.setBaselineScheduleExecutionId(regressionTestExecutionForm.getBaselineScheduleExecutionId());

			if( testSuitDetails.getTestLoginDetail().getLoginId() != null ) {
				ts.setLoginUserDetailId(Integer.parseInt(testSuitDetails.getTestLoginDetail().getLoginId()));
			}
			
			if(StringUtils.equals(testSuitDetails.getSuitType(), "MINTV4")) {
				ts.setBrandingUrlReport(true);
				ts.setBrokenUrlReport(false);
			}
			
			ts.setUrlLevel(0);
			ts.setUserId(user.getUserId());
			ts.setTransactionTestcase(false);
			ts.setStartDate(new Date());
			//if(testSuitDetails.get.getModuleId()!=null){
				ts.setModuleIds("0");
			//}
			ts.setTextCompare(testSuitDetails.getTestSuitCompareConfigXref().isTextCompare());
			ts.setHtmlCompare(testSuitDetails.getTestSuitCompareConfigXref().isHtmlCompare());
			ts.setScreenCompare(testSuitDetails.getTestSuitCompareConfigXref().isScreenCompare());
			ts.setBrowserTypeId(testSuitDetails.getTestSuitBrowserConfig().getBrowserTypeId());
			ts.setSolutionTypeId(Constants.WEB_APPLICATION_TESTING);
			ts.setRegressionTesting(false);
			ts.setAnalyticsTesting(false);
			ts.setRobotClicking(false);
			ts.setBrokenUrlReport(false);
			ts.setProcessChildUrls(false);
			
			formatter = new SimpleDateFormat("HH:mm");
			fromUser = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			
			if (regressionTestExecutionForm.getScheduleType().equalsIgnoreCase(Constants.SCHEDULER.SCHEDULED_RUN)){
				ts.setScheduled(true);
				dateval = regressionTestExecutionForm.getSchDateTime();

				Date t = (Date) formatter.parse(dateval);
				Time timeValue = new Time(formatter.parse(dateval).getTime());
				ts.setStartTime(timeValue);
				
				if (regressionTestExecutionForm.getExecuteBy().equals(Constants.SCHEDULER.WEEKLY_SCHEDULE)) {
					if ( regressionTestExecutionForm.getRecurrence() != null &&  regressionTestExecutionForm.getRecurrence().equals("on") ) {
						ts.setRecurrence(true);
					} else {
						ts.setRecurrence(false);
					}
					
					ts.setScheduleType(Constants.SCHEDULER.BY_WEEKLY);//Weekwise
					
					String weekwise = DataConstants.KEYS.BLANK;
					if (regressionTestExecutionForm.getRecurrenceidweekly1() != null && regressionTestExecutionForm.getRecurrenceidweekly1().equals("on")) {
						weekwise += "Monday,";
					}
					if (regressionTestExecutionForm.getRecurrenceidweekly2() != null && regressionTestExecutionForm.getRecurrenceidweekly2().equals("on")) {
						weekwise += "Tuesday,";
					}
					if (regressionTestExecutionForm.getRecurrenceidweekly3() != null && regressionTestExecutionForm.getRecurrenceidweekly3().equals("on")) {
						weekwise += "Wednesday,";
					}
					if (regressionTestExecutionForm.getRecurrenceidweekly4() != null && regressionTestExecutionForm.getRecurrenceidweekly4().equals("on")) {
						weekwise += "Thursday,";
					}
					if (regressionTestExecutionForm.getRecurrenceidweekly5() != null && regressionTestExecutionForm.getRecurrenceidweekly5().equals("on")) {
						weekwise += "Friday,";
					}
					if (regressionTestExecutionForm.getRecurrenceidweekly6() != null && regressionTestExecutionForm.getRecurrenceidweekly6().equals("on")) {
						weekwise += "Saturday,";
					}
					if (regressionTestExecutionForm.getRecurrenceidweekly7() != null && regressionTestExecutionForm.getRecurrenceidweekly7().equals("on")) {
						weekwise += "Sunday,";
					}
					ts.setRecurrenceDateWise(null);
					ts.setScheduleDDay(weekwise.substring(0, weekwise.length()-1));
					
					
				}	else if (regressionTestExecutionForm.getExecuteBy().equals(Constants.SCHEDULER.DATE_SCHEDULE)) {
					ts.setScheduleType(Constants.SCHEDULER.BY_DATE);//Datewise
					reccurencedateval= regressionTestExecutionForm.getScheduledDate().substring(0,11) + " " + timeValue;
					Date nowDate=fromUser.parse(reccurencedateval);
					ts.setRecurrenceDateWise(nowDate);
					ts.setScheduleDDay("NA");
				}
				
			} else {
				ts.setScheduled(false);
				stringTime = formatter.format(new Date());
				Date t = (Date) formatter.parse(stringTime);
				Time timeValue = new Time(formatter.parse(stringTime).getTime());
				ts.setStartTime(timeValue);
			}
			if(regressionTestExecutionForm.getTestCases() != null && regressionTestExecutionForm.getTestCases().length > 0) {
				ts.setTransactionTestcase(true);
			}
			
			isSaved = homeService.saveSchedule(ts);
		}catch(Exception e){
			logger.error("Exception :Unexpected error occured while Saving Group and User details");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return isSaved;
	}
	
	@RequestMapping(value = "/RetrieveSuits", method = RequestMethod.POST)
	public ModelAndView retrieveSuits(@ModelAttribute("model") ModelMap model,
			RegressionTestExecutionForm regressionTestExecutionForm) {
		logger.debug("Entry: retrieveSuits");
		int currentUserId = 0;
		try {	
			
			/*if(regressionTestExecutionForm.getUiSuitsCategory().equals("Created By Group")) {
				model.addAttribute("regressionTestSuit",
						homeService.getUiRegressionSuitForGroup(UserServiceUtils
								.getCurrentUserGroupIdFromRequest(httpRequest)));
				model.addAttribute("selectedUiSuitCategory","Created By Group");
			}  else if(regressionTestExecutionForm.getUiSuitsCategory().equals("Created By All")) {
				model.addAttribute("regressionTestSuit",homeService.getSavedSuitsCreatedByAll());
				model.addAttribute("selectedUiSuitCategory","Created By All");
			} else {
				model.remove("regressionTestSuit");
			}*/
			Users currentUser = (Users) httpRequest.getSession().getAttribute("currentMintUser");
			currentUserId = currentUser.getUserId();
			if (regressionTestExecutionForm.getSolutionType() == Constants.SolutionType.UI_TESTING.getSolutionTypeId()) {
				if(regressionTestExecutionForm.getUiSuitsCategory().equals("Private Suits - "+currentUser.getUserName())) {
					model.addAttribute("regressionTestSuit",
							homeService.getUiRegressionSuitForUser(currentUserId));
					model.addAttribute("selectedUiSuitCategory","Private Suits - "+currentUser.getUserName());
				}  else if(regressionTestExecutionForm.getUiSuitsCategory().equals("Created By All")) {
					model.addAttribute("regressionTestSuit",homeService.getSavedSuitsCreatedByAll());
					model.addAttribute("selectedUiSuitCategory","Created By All");
				} else {
					model.remove("regressionTestSuit");
				}
				model.addAttribute("SuitsCreatedByAll",homeService.getSavedSuitsCreatedByAll());
			} else {
				if(regressionTestExecutionForm.getUiSuitsCategory().equals("Private Suits - "+currentUser.getUserName())) {
					model.addAttribute("webserviceSuits",
							webserviceTestingService.getWSTestSuitesForUser(currentUserId));
					model.addAttribute("selectedUiSuitCategory","Private Suits - "+currentUser.getUserName());
				}  else if(regressionTestExecutionForm.getUiSuitsCategory().equals("Created By All")) {
					model.addAttribute("webserviceSuits", webserviceTestingService.getWSTestSuitesCreatedByAll());
					model.addAttribute("selectedUiSuitCategory","Created By All");
				} else {
					model.remove("webserviceSuits");
				}
				
			}
			regressionTestExecutionForm.setSuitId("0");
		} catch (Exception e) {
			logger.error("Exception :getCreatedByAll");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: retrieveSuits");
		return Home(model, regressionTestExecutionForm);
	}
	
	@RequestMapping(value = "/RetrieveTestingType", method = RequestMethod.POST)
	public ModelAndView retrieveTestingType(@ModelAttribute("model") ModelMap model,
			RegressionTestExecutionForm regressionTestExecutionForm) {
		logger.debug("Entry: retrieveTestingType");
		try {	
			if(regressionTestExecutionForm.getSolutionType() == Constants.SolutionType.WEBSERVICE.getSolutionTypeId()) {
				//regressionTestExecutionForm.setSetupTabNumber(5);
				
				return WebserviceSuitHome(model, regressionTestExecutionForm);
				//return PageUnderProgress(model);
			}
			regressionTestExecutionForm.setSuitId("0");
		} catch (Exception e) {
			logger.error("Exception :getCreatedByAll");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: retrieveTestingType");
		return Home(model, regressionTestExecutionForm);
	}
	
	
	@SuppressWarnings("unused")
	@RequestMapping(value = {"/WebserviceSuitHome"})
	public ModelAndView WebserviceSuitHome(@ModelAttribute("model") ModelMap model, 
			@ModelAttribute RegressionTestExecutionForm regressionTestExecutionForm) {
		logger.debug("Entry: Home, regressionTestExecutionForm->"+regressionTestExecutionForm);
		int currentUserId = 0;
		int currentGroupId = 0;
		Groups group = new Groups();
		int setupTabNumber = 0;
		if ( null != regressionTestExecutionForm && null != regressionTestExecutionForm.getSetupTabNumber() ){
			setupTabNumber = regressionTestExecutionForm.getSetupTabNumber();
		}
		model.addAttribute("applicationList",
				applicationService.getApplicationListForGroup(currentGroupId));
		model.addAttribute("environmentCategoryList",
				environmentService.getEnvironmentCategoryList());
		//model.addAttribute("webserviceSuits", webserviceTestingService.getWSTestSuitesCreatedByAll());
		
		Users currentUser = (Users) httpRequest.getSession().getAttribute("currentMintUser");
		currentUserId = currentUser.getUserId();
		
		if(regressionTestExecutionForm.getUiSuitsCategory().equals("Private Suits - "+currentUser.getUserName())) {
			model.addAttribute("webserviceSuits",
					webserviceTestingService.getWSTestSuitesForUser(currentUserId));
		}  else if(regressionTestExecutionForm.getUiSuitsCategory().equals("Created By All")) {
			model.addAttribute("webserviceSuits", webserviceTestingService.getWSTestSuitesCreatedByAll());
		} else {
			model.remove("webserviceSuits");
		}
		
		model.addAttribute("isManageSuitAccessAvailable",UserServiceUtils.isUserHasAccessToFunctionality(httpRequest, Constants.Functionality.MANAGE_SUITS));
		model.addAttribute("isMonitorJobAccessAvailable",UserServiceUtils.isUserHasAccessToFunctionality(httpRequest, Constants.Functionality.MONITOR_JOB_STATUS));
		model.addAttribute("regressionTestExecutionForm",regressionTestExecutionForm);
		logger.debug("Exit: Home");
		return Home(model, regressionTestExecutionForm);
	//	return new ModelAndView("common/Home", model);
	}
	
	
	@RequestMapping(value = "/getBaselineDates", method = RequestMethod.POST)
	public @ResponseBody
	String getBaselineDates(
			@RequestParam(value = "suitId") int suitId) {
		
		logger.debug("Entry: getBaselineDates");

		StringBuilder respStr = new StringBuilder();
		
		
		try {
			

		} catch (Exception exp) {
			logger.error(
					"Exception occured during getBaselineDates execution.",
					exp);
		}
		logger.debug("Exist: getBaselineDates");
		return respStr.toString();
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "/getCompletedSuitScheduleStatus", method = RequestMethod.POST)
	public @ResponseBody
	List<ScheduleStatus> getSuitScheduleStatus(@RequestParam(value = "suitId") int suitId, @RequestParam(value = "refreshtext") int refreshtext){
		logger.debug("Entry: getCompletedSuitScheduleStatus, suitId->"+suitId);
		ScheduleStatus scheduleStatus = new ScheduleStatus();
		List<ScheduleStatus> completedScheduleStatusList = new ArrayList<ScheduleStatus>();
		List<Link> crawledLinks = new ArrayList<Link>();
		List<ScheduleStatus> scheduleStatusList = new ArrayList<ScheduleStatus>();
		ReportStatus reportStatus = new ReportStatus();
		try{
			completedScheduleStatusList = homeService.getCompletedScheduleStatus(suitId, refreshtext);
			
			if ( null != completedScheduleStatusList && refreshtext > 0 && completedScheduleStatusList.size() > refreshtext ){
				completedScheduleStatusList.subList(refreshtext, completedScheduleStatusList.size()).clear();
			}
			
			for(ScheduleStatus completedScheduleStatus:completedScheduleStatusList) {
				
				reportStatus = homeService.getComparisonReportsList(completedScheduleStatus.getScheduleId());
				completedScheduleStatus.setAnalyticsReportAvailable(reportStatus.isAnalyticsReportAvailable());
				
				Link crawledLink = new Link();
				crawledLink.setUrl("https://www.tiaa-cref.org/public/index.html");
				crawledLink.setImageName("homepage");
				crawledLink.setImageFullPath("C:\\screenshot216135326588176919.jpg");
				crawledLinks.add(crawledLink);
				
				crawledLink = new Link();
				crawledLink.setUrl("https://www.tiaa-cref.org/public/products-services");
				crawledLink.setImageName("products");
				crawledLink.setImageFullPath("C:\\screenshot7255548881492392");
				crawledLinks.add(crawledLink);
				completedScheduleStatus.setCrawledLink(crawledLinks);
				scheduleStatusList.add(completedScheduleStatus);
			}
			
		}catch(Exception e){
			
		}
		logger.debug("Exit: getCompletedSuitScheduleStatus");
		return scheduleStatusList;
	}

	@RequestMapping(value = "/getInProgressSuitScheduleStatus", method = RequestMethod.POST)
	public @ResponseBody
	List<ScheduleStatus> getInProgressSuitScheduleStatus(@RequestParam(value = "suitId") int suitId, @RequestParam(value = "refreshtext") int refreshtext){
		logger.debug("Entry: getInProgressSuitScheduleStatus, suitId->"+suitId);
		List<ScheduleStatus> inProgressScheduleStatusList = new ArrayList<ScheduleStatus>();
		List<ScheduleStatus>  scheduleStatusList = new ArrayList<ScheduleStatus>();
		List<Link> crawledLinks = new ArrayList<Link>();
		try{
			inProgressScheduleStatusList = homeService.getInProgressScheduleStatus(suitId);
			
			if ( null != inProgressScheduleStatusList && refreshtext > 0 && inProgressScheduleStatusList.size() > refreshtext ){
				inProgressScheduleStatusList.subList(refreshtext, inProgressScheduleStatusList.size()).clear();
			}
			
			for(ScheduleStatus inProgressScheduleStatus:inProgressScheduleStatusList) {
				Link crawledLink = new Link();
				crawledLink.setUrl("https://www.tiaa-cref.org/public/index.html");
				crawledLink.setImageName("homepage");
				crawledLink.setImageFullPath("C:\\screenshot216135326588176919.jpg");
				crawledLinks.add(crawledLink);
				
				crawledLink = new Link();
				crawledLink.setUrl("https://www.tiaa-cref.org/public/products-services");
				crawledLink.setImageName("products");
				crawledLink.setImageFullPath("C:\\screenshot7255548881492392");
				crawledLinks.add(crawledLink);
				inProgressScheduleStatus.setCrawledLink(crawledLinks);
				scheduleStatusList.add(inProgressScheduleStatus);
			}
		}catch(Exception e){
			
		}

		logger.debug("Exit: getInProgressSuitScheduleStatus");
		return scheduleStatusList;
	}
	
	@RequestMapping(value = "/GetFutureSuitScheduleStatus", method = RequestMethod.POST)
	public @ResponseBody
	List<ScheduleStatus> getFutureSuitScheduleStatus(@RequestParam(value = "suitId") int suitId, @RequestParam(value = "refreshtext") int refreshtext){
		logger.debug("Entry: getFutureSuitScheduleStatus, suitId->"+suitId+", refreshtext->"+refreshtext);
		List<ScheduleStatus> futureScheduleStatusList = new ArrayList<ScheduleStatus>();
		
		if ( null != futureScheduleStatusList && refreshtext > 0 && futureScheduleStatusList.size() > refreshtext ){
			futureScheduleStatusList.subList(refreshtext, futureScheduleStatusList.size()).clear();
		}
		try{
			futureScheduleStatusList = homeService.getFutureSuitScheduleStatus(suitId);
		}catch(Exception e){
			logger.error("Exception :GetFutureSuitScheduleStatus");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: getFutureSuitScheduleStatus");
		return futureScheduleStatusList;
	}
	
	@RequestMapping(value = "/DeleteFutureSchedule", method = RequestMethod.POST)
	public @ResponseBody
	String deleteFutureSchedule(@RequestParam(value = "scheduleId") int scheduleId, @RequestParam(value = "scheduleDay") String scheduleDay) {
		logger.debug("Entry: DeleteFutureSchedule, scheduleId, scheduleDay->"+scheduleId +"," + scheduleDay);
		String message = "Error";
		try{
			if(scheduledService.deleteSchedule(scheduleId, scheduleDay)) {
				message = "Success";
			}
		}catch(Exception e){
			logger.error("Exception :DeleteFutureSchedule");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: DeleteFutureSchedule");
		return message;
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/removeSuitDetails", method = RequestMethod.POST)
	public String removeSuitDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute RegressionTestExecutionForm regressionTestExecutionForm) {
		logger.debug("Entry: removeSuitDetails");
		StringBuilder respStr = new StringBuilder();
		Suit suit = new Suit();
		boolean result=false;
		try{
			suit = homeService.getSavedSuits( Integer.parseInt(regressionTestExecutionForm.getSuitId()));
			
			if(suit.getSuitId()>0){
				result = homeService.removeSuitDetails(suit);
				if(result==true){
					redirAttr.addFlashAttribute("Success", "Selelected suit deleted successfully");
				}else{
					redirAttr.addFlashAttribute("error",
					"Error while deleting suit details");
				}
			}
		}catch(Exception exp){
			logger.error(
					"Exception occured during getResultsExecutionDates execution.",
					exp);
		}
		logger.debug("Exist: removeSuitDetails");
		return "redirect:/Home.ftl";
	}
	
	@RequestMapping(value = "/ChoosingOnEditSuitDetails", method = RequestMethod.POST)
	public String editSuitDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute RegressionTestExecutionForm regressionTestExecutionForm,TestingForm testingForm) {
		logger.debug("Entry: EditSuitDetails");
		Suit suit = new Suit();
		List<SuitTextImageXref> suitTextImageXref = new ArrayList<SuitTextImageXref>();
		List<SuitBrokenReportsXref> suitBrokenReportsXrefList = new ArrayList<SuitBrokenReportsXref>();
		try{
			suit = homeService.getSavedSuits( Integer.parseInt(regressionTestExecutionForm.getSuitId()));
			redirAttr.addFlashAttribute("regressionTestExecutionForm", regressionTestExecutionForm);
			testingForm.setEditOrViewMode(regressionTestExecutionForm.getEditOrViewMode());
			if( null != suit && null != suit.getSuitId() && suit.getSuitId() > 0){
				if(suit.getType().equals("TextOrImage")){
					logger.info("Entry: TextOrImage for Edit Option");
					
					testingForm.setSuitId(suit.getSuitId());
					
					testingForm.setSuitName(suit.getSuitName());
					testingForm.setPrivateSuit(suit.isPrivateSuit());
					
					testingForm.setApplicationId(suit.getApplicationId());
					testingForm.setEnvironmentCategoryId(suit.getEnvironmentCategoryId());
					testingForm.setEnvironmentId(suit.getEnvironmentId());
					testingForm.setLoginId(suit.getLoginId());
					
					suitTextImageXref = homeService.getSuitTextImageXref(suit.getSuitId());
					
					redirAttr.addFlashAttribute("suitTextImageXrefList", suitTextImageXref);
					redirAttr.addFlashAttribute("testingForm", testingForm);
					return "redirect:/UiFindImageOrText.ftl";
				}else if(suit.getType().equals("Broken")){
					logger.info("Entry: Broken Link URL Report for Edit Option");
					
					testingForm.setSuitId(suit.getSuitId());
					testingForm.setApplicationId(suit.getApplicationId());
					testingForm.setEnvironmentCategoryId(suit.getEnvironmentCategoryId());
					testingForm.setEnvironmentId(suit.getEnvironmentId());
					testingForm.setLoginId(suit.getLoginId());
					testingForm.setModules(suit.getModuleId().split(","));
					testingForm.setBrowserType(suit.getBrowserTypeId());
					testingForm.setProcessUrl(suit.isBrokenUrlReport());
					testingForm.setSuitName(suit.getSuitName());
					testingForm.setPrivateSuit(suit.isPrivateSuit());
					testingForm.setUrlLevel(suit.getUrlLevel());
					if(suit.isTransactionTestcase()){
						testingForm.setTransactionOrCrawlerType("1");	
					}else{
						testingForm.setTransactionOrCrawlerType("0");
					}
					
					suitBrokenReportsXrefList = homeService.getSuitBrokenReportsXref(suit.getSuitId());
					
					ListIterator<SuitBrokenReportsXref> suitBrokenReportsXref= suitBrokenReportsXrefList.listIterator();
					while(suitBrokenReportsXref.hasNext()){
						String reportName =suitBrokenReportsXref.next().getReportName().toString();
						if(reportName.equals("BrokenLink")){
							testingForm.setBrokenLinks(true);
						}
						
						if(reportName.equals("BrokenLinkResources")){
							testingForm.setBrokenLinksResources(true);					
						}
						if(reportName.equals("LoadTimeAttributes")){
							testingForm.setLoadAttributes(true);
						}
								
					}
					/*testingForm.setBrokenLinks(suitBrokenReportsXref.isBrokenLinks());
					testingForm.setBrokenLinksResources(suitBrokenReportsXref.isBrokenLinksResources());
					testingForm.setLoadAttributes(suitBrokenReportsXref.isLoadTimeAttributes());*/
					
					redirAttr.addFlashAttribute("testingForm", testingForm);
					return "redirect:/BrokenLinkCheck.ftl";
				}else if(suit.getType().equals("Analytics")){
					logger.info("Entry: Analytics Report for Edit Option");
					
					testingForm.setSuitId(suit.getSuitId());
					testingForm.setApplicationId(suit.getApplicationId());
					testingForm.setEnvironmentId(suit.getEnvironmentId());
					testingForm.setEnvironmentCategoryId(suit.getEnvironmentCategoryId());
					testingForm.setLoginId(suit.getLoginId());
					testingForm.setModules(suit.getModuleId().split(","));
					testingForm.setBrowserType(suit.getBrowserTypeId());
					testingForm.setSmartUser(suit.isAnalyticsTesting());
					testingForm.setSuitName(suit.getSuitName());
					testingForm.setPrivateSuit(suit.isPrivateSuit());
					testingForm.setUrlLevel(suit.getUrlLevel());
					testingForm.setScrollPage(suit.isScrollPage());
					testingForm.setWaitTime(suit.getWaitTime());
					if(suit.isTransactionTestcase()){
						testingForm.setTransactionOrCrawlerType("1");	
					}else{
						testingForm.setTransactionOrCrawlerType("0");
					}
					redirAttr.addFlashAttribute("testingForm", testingForm);
					return "redirect:/AnalyticsTesting.ftl";
				}else{
					logger.info("Entry: Functionality Report for Edit Option");
					
					testingForm.setSuitId(suit.getSuitId());
					testingForm.setApplicationId(suit.getApplicationId());
					testingForm.setEnvironmentId(suit.getEnvironmentId());
					testingForm.setEnvironmentCategoryId(suit.getEnvironmentCategoryId());
					testingForm.setLoginId(suit.getLoginId());
					
					if ( null != suit.getModuleId() && suit.getModuleId().trim().length() > 0){
						testingForm.setModules(suit.getModuleId().split(","));
					}else{
						testingForm.setModules(new String[]{});
					}
					
					testingForm.setBrowserType(suit.getBrowserTypeId());
					testingForm.setTextCompare(suit.isTextCompare());
					testingForm.setHtmlCompare(suit.isHtmlCompare());
					testingForm.setScreenCompare(suit.isScreenCompare());
					testingForm.setSuitName(suit.getSuitName());
					testingForm.setPrivateSuit(suit.isPrivateSuit());
					testingForm.setUrlLevel(suit.getUrlLevel());
					if(suit.isTransactionTestcase()){
						testingForm.setTransactionOrCrawlerType("1");	
					}else{
						testingForm.setTransactionOrCrawlerType("0");
					}
					
					redirAttr.addFlashAttribute("testingForm", testingForm);
					
				}
			}
		}catch(Exception e){
			logger.error("Exception :EditSuitDetails" +e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exist: EditSuitDetails");
		return "redirect:/FunctionalityTesting.ftl";
	}
	
	@RequestMapping(value = "/ChoosingEditWebserviceSuitDetails", method = RequestMethod.POST)
	public String editWsSuitDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute RegressionTestExecutionForm regressionTestExecutionForm,WebserviceSetupForm webserviceSetupForm) {
		logger.debug("Entry: ChoosingEditWebserviceSuitDetails.ftl");
		WebserviceSuite webserviceSuite = new WebserviceSuite();
		try {
			webserviceSetupForm.setSetupTabNumber(Constants.WebserviceTest.CREATE_WS_TESTSUITE);
			webserviceSuite = homeService.getSavedWsSuits( Integer.parseInt(regressionTestExecutionForm.getWebserviceSuiteId()));
			redirAttr.addFlashAttribute("regressionTestExecutionForm", regressionTestExecutionForm);
			webserviceSetupForm.setEditOrViewMode(regressionTestExecutionForm.getEditOrViewMode());
			if( null != webserviceSuite && webserviceSuite.getWebserviceSuiteId() > 0){
				webserviceSetupForm.setWsSuiteId(webserviceSuite.getWebserviceSuiteId());
				webserviceSetupForm.setEnvironmentId(webserviceSuite.getEnvironmentId());
				webserviceSetupForm.setUserId(webserviceSuite.getUserId());
				//webserviceSetupForm.setSetupTabNumber(WebserviceTesting.CREATE_WS_TESTSUITE);
				webserviceSetupForm.setWsSuiteName(webserviceSuite.getWebserviceSuiteName());
				webserviceSetupForm.setPrivateSuit(webserviceSuite.isPrivateSuit());
				webserviceSetupForm.setEditOrViewMode(regressionTestExecutionForm.getEditOrViewMode());
				
				List<WebserviceSetupForm> listWebserviceSetupForm = homeService.getSavedWsSuitesParams(webserviceSuite.getWebserviceSuiteId());
				//context.getSession().setAttribute("listWebserviceSetupForm", listWebserviceSetupForm);
				Webservices webservice = new Webservices();
				WebserviceOperations webserviceOperations = new WebserviceOperations();
				WSSuiteParams wsSuiteParams = null;
				List<WSSuiteParams> listWSSuiteParams = new ArrayList<WSSuiteParams>();
				for(WebserviceSetupForm wsSetupForm : listWebserviceSetupForm){
					if(wsSetupForm.getReqInputType().equalsIgnoreCase("rawinput")){
						webserviceSetupForm.setXmlInput("false");
						webserviceSetupForm.setRawInput("true");
					} else {
						webserviceSetupForm.setXmlInput("true");
						webserviceSetupForm.setRawInput("false");
					}
					webservice = webserviceTestingService.getServicefromServiceId(wsSetupForm.getServiceId());
					String serviceName = webservice.getServiceName();
					webserviceOperations.setOperationId(wsSetupForm.getOperationId());
					String operationName = webserviceTestingService
							.getOperationfromOperationId(webserviceOperations).getOperationName();
					String environmentName = environmentService
					.getEnvironmentCategoryById(webserviceSuite.getEnvironmentId()).getEnvironmentCategoryName();
					
					
					for(int i=0;i<wsSetupForm.getParams().length;i++) {
						wsSuiteParams = new WSSuiteParams();
						wsSuiteParams.setServiceId(wsSetupForm.getServiceId());
						wsSuiteParams.setServiceName(serviceName);
						wsSuiteParams.setOperationId(wsSetupForm.getOperationId());
						wsSuiteParams.setEnvironmentId(wsSetupForm.getEnvironmentId());
						wsSuiteParams.setOperationName(operationName);
						wsSuiteParams.setEnvironmentName(environmentName);
						wsSuiteParams.setParameterSetId(Integer.parseInt(wsSetupForm.getParams()[i]));
						Integer reqdOprId = webserviceTestingService.getOperationIdOfNullEnv(wsSetupForm.getServiceId(),
								wsSetupForm.getOperationId());
						WsDataset wsDatasets = new WsDataset();
						if(wsSetupForm.getReqInputType().equalsIgnoreCase("rawinput")){
						wsDatasets = webserviceTestingService.getParameterValues(webserviceSuite.getEnvironmentId(), 
					 		Integer.parseInt(wsSetupForm.getParams()[i]), reqdOprId);
						} else {
						wsDatasets = webserviceTestingService.getXmlParameterValues(webserviceSetupForm.getEnvironmentId(), 
						 		Integer.parseInt(wsSetupForm.getParams()[i]), reqdOprId);
						}
						wsSuiteParams.setWsDataset(wsDatasets);
						listWSSuiteParams.add(wsSuiteParams);
					}
			
				}
				redirAttr.addFlashAttribute("wsSuiteId", webserviceSetupForm.getWsSuiteId());
				context.getSession().setAttribute("serviceMap",
						listWSSuiteParams);
				redirAttr.addFlashAttribute("serviceMapUI", context.getSession()
						.getAttribute("serviceMap"));
				redirAttr.addFlashAttribute("addAnother", "Selected Service-Operation(s)");
				redirAttr.addFlashAttribute("editOrViewMode", regressionTestExecutionForm.getEditOrViewMode());
				redirAttr.addFlashAttribute("webserviceSetupForm", webserviceSetupForm);
			}
		} catch(Exception e){
			logger.error("Exception in editWsSuitDetails.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: ChoosingEditWebserviceSuitDetails.ftl");
		return "redirect:/WebserviceSetup.ftl";
	}
	
	@RequestMapping(value = "/UiChooseSuitType", method = RequestMethod.POST)
	public String uiChooseSuitType(RedirectAttributes redirAttr,
			Model model, @ModelAttribute RegressionTestExecutionForm regressionTestExecutionForm) {
		logger.debug("Entry: UiChooseSuitType");
		redirAttr.addFlashAttribute("suitTypes",
				menuService.getSubMenuList(Constants.ParentMenu.CONFIGURE_TEST));
		logger.debug("Entry: UiChooseSuitType");
		return "redirect:/ChooseSuitType.ftl";
	}
	
	@RequestMapping(value = "/UiManageSuitType", method = RequestMethod.POST)
	public String UiManageSuitType(RedirectAttributes redirAttr,
			Model model, @ModelAttribute RegressionTestExecutionForm regressionTestExecutionForm) {
		logger.debug("Entry: UiManageSuitType");
		
		logger.debug("Entry: UiManageSuitType");
		return "redirect:/ManageSuits.ftl";
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/ManageSuits", method = RequestMethod.GET)
	public String manageSuitsType(Model model) {
		logger.debug("Entry: manageSuitsType");
		Map<String,String> uiManageSuitsList=new HashMap<String,String>();  
		int currentUserId = 0;
		int currentGroupId = 0;
		RegressionTestExecutionForm regressionTestExecutionForm = null;

		if (null != model.asMap()
				&& model.asMap().containsKey("regressionTestExecutionForm")) {
			regressionTestExecutionForm = (RegressionTestExecutionForm) model.asMap().get(
					"regressionTestExecutionForm");
		}else {
			regressionTestExecutionForm = new RegressionTestExecutionForm();
		}
		
		try{
			Users currentUser = (Users) httpRequest.getSession().getAttribute("currentMintUser");
			currentGroupId = currentUser.getGroupId();
			currentUserId = currentUser.getUserId();
			
			List<Groups> groupList = userService.getAllGroupsOrderByName();
			uiManageSuitsList.put("ALL", "All Suits");
			for(Groups group : groupList){
				uiManageSuitsList.put(String.valueOf(group.getGroupId()), "Created By Group - "+group.getGroupName());
			}
			
			List<SolutionType> solutionTypeList = homeService.getAllSolutionTypes();
			
			model.addAttribute("uiManageSuitsList",
					uiManageSuitsList);
			model.addAttribute("solutionTypeList",
					solutionTypeList);
			if(regressionTestExecutionForm.getGroupId() != null 
					&& regressionTestExecutionForm.getGroupId().equals("ALL")){
				model.addAttribute("selectedShowSuits","ALL");
				if(!model.containsAttribute("regressionTestSuit")) {
					model.addAttribute("regressionTestSuit",
							homeService.getManageSuitsForAllSuits());
				}
			}else if(regressionTestExecutionForm.getGroupId() == null ||
					Integer.parseInt(regressionTestExecutionForm.getGroupId()) == currentGroupId ){
				model.addAttribute("selectedShowSuits",String.valueOf(currentGroupId));
				
				if(!model.containsAttribute("regressionTestSuit")) {
					model.addAttribute("regressionTestSuit",
							homeService.getManageSuitsForGroup(currentGroupId));
				}
			}else{
				model.addAttribute("selectedShowSuits",regressionTestExecutionForm.getGroupId());
				
				if(!model.containsAttribute("regressionTestSuit")) {
					model.addAttribute("regressionTestSuit",
							homeService.getManageSuitsForGroup(Integer.parseInt(regressionTestExecutionForm.getGroupId())));
				}
			}
			
			model.addAttribute("applicationList",
					applicationService.getApplicationListForGroup(currentGroupId));
			model.addAttribute("environmentCategoryList",
					environmentService.getEnvironmentCategoryList());
			model.addAttribute("regressionTestExecutionForm", regressionTestExecutionForm);
		}catch(Exception e){
			logger.error(
					"Exception occured during manageSuitsType ."+e);
		}
		logger.debug("Exist: manageSuitsType");
		return "common/ManageSuits";
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/RetrieveManageSuits", method = RequestMethod.POST)
	public String retrieveManageSuits(RedirectAttributes redirAttr,
			Model model, @ModelAttribute RegressionTestExecutionForm regressionTestExecutionForm) {
		logger.debug("Entry: RetrieveManageSuits");
		int currentUserId = 0;
		int currentGroupId = 0;
		try {	
			Users currentUser = (Users) httpRequest.getSession().getAttribute("currentMintUser");
			currentGroupId = currentUser.getGroupId();
			currentUserId = currentUser.getUserId();
			
			if(regressionTestExecutionForm.getGroupId() !=null 
					&& !regressionTestExecutionForm.getGroupId().equals("ALL")) {
				if(regressionTestExecutionForm.getGroupId() == null ||
						Integer.parseInt(regressionTestExecutionForm.getGroupId()) == currentGroupId ){
					redirAttr.addFlashAttribute("regressionTestSuit",
							homeService.getManageSuitsForGroup(currentGroupId));
				}else{
					redirAttr.addFlashAttribute("regressionTestSuit",
							homeService.getManageSuitsForGroup(Integer.parseInt(regressionTestExecutionForm.getGroupId())));	
				}
				
			} else if(regressionTestExecutionForm.getGroupId().equals("ALL")){
				redirAttr.addFlashAttribute("regressionTestSuit",
						homeService.getManageSuitsForAllSuits());
			}
			redirAttr.addFlashAttribute("selectedShowSuits",regressionTestExecutionForm.getGroupId());
			redirAttr.addFlashAttribute("regressionTestExecutionForm", regressionTestExecutionForm);
		} catch (Exception e) {
			logger.error("Exception :getCreatedByAll");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: RetrieveManageSuits");
		return "redirect:/ManageSuits.ftl";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetAllorPendingGroupsForManageSuits", method = RequestMethod.POST)
	public @ResponseBody
	String getAllorPendingGroupsForManageSuits(
			@RequestParam(value = "suitId") int suitId) {
		logger.debug("Entry: GetAllorPendingGroupsForManageSuits ");
		
		List<SuitGroupXref> suitGroupXrefList = new ArrayList<SuitGroupXref>();
		List<Groups> groupsList = new ArrayList<Groups>();
		List<Groups> removedGroupList = new ArrayList<Groups>();
		
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();

		try {
			if (suitId > 0) {
				groupsList = userService
						.getAllGroupsDetails();
				
				suitGroupXrefList = homeService
						.getManageSuitDetails(suitId);
				
				if (groupsList != null && suitGroupXrefList!=null) {
					for (Groups group : groupsList) {
						for (SuitGroupXref suitGroupXref : suitGroupXrefList) {
							if(group.getGroupId() == suitGroupXref.getGroupId() ) {
								if(!removedGroupList.contains(group)){
									removedGroupList.add(group);
								}
								
					        }
						}
					}
					
					groupsList.removeAll(removedGroupList);
					
					for (Groups groupDetails : groupsList) {
						obj.put(groupDetails.getGroupId(),
								groupDetails.getGroupName());
					}
					
				}else if(groupsList != null && suitGroupXrefList == null){
					for (Groups groupDetails : groupsList) {
						obj.put(groupDetails.getGroupId(),
								groupDetails.getGroupName());
					}	
				}
				
			} else {
				obj.put("", "");
			}
			
			obj.writeJSONString(out);
		} catch (Exception e) {
			logger.error("Exception in GetAllorPendingGroupsForManageSuits error", e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: GetAllorPendingGroupsForManageSuits");
		return out.toString();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/GetGroupDetailsForManageSuits", method = RequestMethod.POST)
	public @ResponseBody
	String getGroupDetailsForManageSuits(
			@RequestParam(value = "suitId") int suitId) {
		logger.debug("Entry: GetGroupDetailsForManageSuits ");

		Groups group = new Groups();
		List<SuitGroupXref> suitGroupXrefList = new ArrayList<SuitGroupXref>();
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();

		try {
			if (suitId > 0) {
				suitGroupXrefList = homeService
						.getManageSuitDetails(suitId);

				if (suitGroupXrefList != null) {
					for (SuitGroupXref SuitGroups : suitGroupXrefList) {
						group = userService.getGroup(SuitGroups.getGroupId());
						obj.put(group.getGroupId(),
								group.getGroupName());
					}
				} else {
					obj.put("", "");
				}

				obj.writeJSONString(out);
			}

		} catch (Exception e) {
			logger.error("Exception in GetGroupDetailsForManageSuits error", e);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: GetGroupDetailsForManageSuits");
		return out.toString();
	}
	
	@RequestMapping(value = "/SaveManageSuits", method = RequestMethod.POST)
	public String SaveManageSuits(RedirectAttributes redirAttr,
			Model model, @ModelAttribute RegressionTestExecutionForm regressionTestExecutionForm) {

		logger.debug("Entry: SaveManageSuits");
		
		String groupIds[] = {};
		List<SuitGroupXref> SuitGroupXrefList = null;
		try {
			SuitGroupXrefList = new ArrayList<SuitGroupXref>();
			
			if (regressionTestExecutionForm.getAssignGroupId() != null) {
				groupIds = regressionTestExecutionForm.getAssignGroupId();
			}
			for (String group : groupIds) {
				SuitGroupXref suitGroup = new SuitGroupXref();
				suitGroup.setGroupId(Integer.parseInt(group));
				suitGroup.setSuitId(Integer.parseInt(regressionTestExecutionForm.getSuitId()));
				SuitGroupXrefList.add(suitGroup);
			}
			
			if (this.homeService.addManageSuitsGroup(regressionTestExecutionForm.getSuitId(),SuitGroupXrefList)) {
				regressionTestExecutionForm.setGroupId(regressionTestExecutionForm.getUiSuitsCategory());
				redirAttr.addFlashAttribute("Success",
						"ManageSuits Details saved successfully");

			} else {
				redirAttr.addFlashAttribute("error",
						"Error while saving ManageSuits details");
			}
			
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while Saving ManageSuits details");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(e));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while Saving SaveManageSuits details");
		}
		redirAttr.addFlashAttribute("regressionTestExecutionForm", regressionTestExecutionForm);

		logger.debug("Exit: SaveManageSuits");
		return "redirect:/ManageSuits.ftl";
	}
	@SuppressWarnings("unused")
	@RequestMapping(value = "/ChooseSuitType", method = RequestMethod.GET)
	public String chooseSuitType(RedirectAttributes redirAttr,
			Model model, @ModelAttribute RegressionTestExecutionForm regressionTestExecutionForm) {
		logger.debug("Entry: showSuitTypePage");
		StringBuilder respStr = new StringBuilder();
		Suit suit = new Suit();
		boolean result=false;
		try{
			model.addAttribute("suitTypes",
					menuService.getSubMenuList(Constants.ParentMenu.CONFIGURE_TEST));
		}catch(Exception exp){
			logger.error(
					"Exception occured during getResultsExecutionDates execution.",
					exp);
		}
		logger.debug("Exist: removeSuitDetails");
		return "uitesting/UiChooseSuitType";
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/ChooseSuitTypeSubmit", method = RequestMethod.POST)
	public String chooseSuitTypeSubmit(RedirectAttributes redirAttr,
			Model model, @ModelAttribute TestingForm testingForm) {
		logger.debug("Entry: showSuitTypePage");
		StringBuilder respStr = new StringBuilder();
		Suit suit = new Suit();
		boolean result=false;
		int showType = Integer.parseInt(testingForm.getSuitType());
		RegressionTestExecutionForm regressionTestExecutionForm = new RegressionTestExecutionForm();
		try{
			switch (showType) {
			case Constants.Menu.FUNCTIONAL:
				regressionTestExecutionForm.setSetupTabNumber(1);
				return "redirect:/FunctionalityTesting.ftl";
				
			case Constants.Menu.ANALYTICS:
				regressionTestExecutionForm.setSetupTabNumber(2);
				return "redirect:/AnalyticsTesting.ftl";
				
			case Constants.Menu.BROKEN_LINK_CHECK:
				return "redirect:/BrokenLinkCheck.ftl";
				
			case Constants.Menu.FINDTEXTIMAGE:
				return "redirect:/UiFindImageOrText.ftl";
			default:
				return "redirect:/Home.ftl";
			}	
				
		}catch(Exception exp){
			logger.error(
					"Exception occured during getResultsExecutionDates execution.",
					exp);
		}
		logger.debug("Exist: removeSuitDetails");
		return "redirect:/FunctionalityTesting.ftl";
	}
	
	@RequestMapping(value = { "/FeedBack" }, method = RequestMethod.GET)
	public String feedBackGet(Model model) {
		logger.debug("Entry: feedBackGet");
		try{
			
		}catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading Application home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: feedBackGet");
		return "common/FeedBack";
	}
	
	/**
	 * Save new application details to application table
	 * 
	 * @param model
	 * @param
	 */
	@RequestMapping(value = "/SaveFeedBack", method = RequestMethod.POST)
	public String saveFeedBackDetails(RedirectAttributes redirAttr,
									  Model model, @ModelAttribute FeedBackForm feedBackForm, BindingResult result) {

		logger.debug("Entry: SaveFeedBack, feedBackForm->"
				+ feedBackForm);
		FeedBack feedBack = new FeedBack();
		try {
			if (result.hasErrors()) {
				List<ObjectError> error = result.getAllErrors();
				for(ObjectError errors:error){
					logger.info(errors.getCodes());
					logger.info(errors.getDefaultMessage());
					logger.info(errors.getObjectName());
				}
				
			}
			Users currentMintUser =  (Users) httpRequest.getSession().getAttribute("currentMintUser");
			
			feedBack.setUserId(currentMintUser.getUserId());
			feedBack.setUserEmailId(currentMintUser.getEmailId());
			feedBack.setText(null);
			feedBack.setMenuId(Integer.parseInt(feedBackForm.getMenuId()));
			
			if (this.homeService.addFeedBack(feedBack)) {
				
				String filePath = FileDirectoryUtil.getAbsolutePath(CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.jsonclob"), FileDirectoryUtil.getMintRootDirectory(configProperties));
				boolean status = feedBack.setTextpath(feedBackForm.getComments(), filePath + feedBack.getFeedBackId()+"_"+Constants.FEEDBACK_DET);
				
				if ( status ){
					status = homeService.addFeedBack(feedBack);
					if ( status ){
						redirAttr.addFlashAttribute("success",
								"FeedBack saved successfully");
						redirAttr.addFlashAttribute("feedBackForm",
								feedBackForm);
					}
				}else{
					redirAttr.addFlashAttribute("error",
							"Error while saving FeedBack details");
				}
			} else {
				redirAttr.addFlashAttribute("error",
						"Error while saving FeedBack details");
			}

		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while Saving FeedBack details");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(e));
			redirAttr
					.addFlashAttribute("error",
							"Unexpected error occured while Saving FeedBack details");
		}

		logger.debug("Exit: SaveFeedBack");
		return "redirect:/FeedBack.ftl";
	}
	
	@RequestMapping(value = { "/UiViewReports" }, method = RequestMethod.POST)
	public String UiViewReports(RedirectAttributes redirAttr, 
			Model model, @ModelAttribute RegressionTestExecutionForm rteForm) {
		logger.debug("Entry: UiViewReports, rteForm->"+rteForm);
		List<CompareLink> compareResults = new ArrayList<CompareLink>();
		boolean compareText = false, compareHtml = false, compareImage = false;
		try{
			ScheduleExecution scheduleExecution = scheduledService.getScheduleExecution(rteForm.getScheduleExecutionId());

			if(scheduleExecution.getTextComparisonStatusId() != 5) {
				compareText = true;
			}
			if(scheduleExecution.getImageComparisonStatusId() != 5) {
				compareImage = true;
			}
			if(scheduleExecution.getHtmlComparisonStatusId() != 5) {
				compareHtml = true;
			}
			
			
			CompareConfig compareConfig = new CompareConfig();
			
			if ( null != scheduleExecution.getScheduleExecutionId() && scheduleExecution.getScheduleExecutionId() > 0 ){
				if ( null != scheduleExecution.getCrawlStatusDirectory() && scheduleExecution.getCrawlStatusDirectory().length() > 0 ){
					JsonReaderWriter<CompareConfig> jsonReaderWriterCompareConfig = new JsonReaderWriter<CompareConfig>();
					compareConfig = jsonReaderWriterCompareConfig.readJsonObjectFromFile(new CompareConfig(), 
							scheduleExecution.getCrawlStatusDirectory()+ File.separator + UiTestingConstants.COMPARE_CONFIG);
					compareResults = compareConfig.getCompareLinkList();
					
					scheduledService.updateFilePath(compareResults);
					
					logger.info("compare List:"+compareConfig.getCompareLinkList());
				}
			}

			System.out.println(rteForm);
		}catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading Image Comparison results home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		redirAttr.addFlashAttribute("regressionTestExecutionForm", rteForm);
		redirAttr.addFlashAttribute("compareResults", compareResults);
		redirAttr.addFlashAttribute("compareText", compareText);
		redirAttr.addFlashAttribute("compareImage", compareImage);
		redirAttr.addFlashAttribute("compareHtml", compareHtml);
		
		logger.debug("Exit: UiViewReports");
		return "redirect:/UiViewReportsGet.ftl"; //TODO: do redirect to /UiViewReports, it will go to get.
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = { "/UiViewReportsGet" }, method = RequestMethod.GET)
	public String UiViewReportsGet(Model model, @ModelAttribute RegressionTestExecutionForm rteForm) {
		logger.debug("Entry: UiViewReportsGet, rteForm->"+rteForm);
		
		
		try{
			
			RegressionTestExecutionForm regressionTestExecutionForm = new RegressionTestExecutionForm();
			if (null != model.asMap()
					&& model.asMap().containsKey("regressionTestExecutionForm")) {
				regressionTestExecutionForm = (RegressionTestExecutionForm) model.asMap().get(
						"regressionTestExecutionForm");
			}
			
		}catch (Exception e) {
			logger.error("Exception :Unexpected error occured while loading Image Comparison results home");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: UiViewReportsGet");
		return "common/UiViewReports";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getImage", method = RequestMethod.POST)
	@ResponseBody
	public String getImage(@RequestParam(value = "imagePath") String imagePath) {
		//String fileDir = "C:\\Tools\\mint\\Application\\ResultsDirectory\\IWC_ST4_SSO_2015-Aug-18_11-41-17\\firefox\\PH1005447\\";
		//imagePath= "IWC_home.jpeg";
		//logger.info("imagePath :"+imagePath);
		logger.debug("Entry :getImage, imagePath->"+imagePath);
		if ( null == imagePath || imagePath.trim().length() < 1 ){
			//imagePath = "C:\\Tools\\mint\\Application\\Mint\\CompareNotAvilable.JPG";
		}
		String imagedata = null;
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		try {
			if ( MintFileUtils.isFileExists(imagePath) ){
				imagedata = FileUtils.encodeFileToBase64Binary(imagePath);
				obj.put(1, imagedata);
				obj.writeJSONString(out);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		logger.debug("Exit :getImage");
		return  out.toString();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/getHtmlFileSource", method = RequestMethod.POST)
	@ResponseBody
	public String getHtmlFileSource(HttpServletResponse response, @RequestParam(value = "htmlFilePath") String htmlFilePath) {
		//logger.info("htmlFilePath :"+htmlFilePath);

		String fileContent = "";
		JSONObject obj = new JSONObject();
		StringWriter out = new StringWriter();
		try{
			if ( MintFileUtils.isFileExists(htmlFilePath) ){
				
				fileContent = FileUtils.readFromFile(htmlFilePath);
				obj.put(1, fileContent);
				obj.writeJSONString(out);
			}else{
				logger.info("File not exists :"+htmlFilePath);
			}
		}catch(Exception e){
			logger.error("Exception while reading the HTML File :"+htmlFilePath);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}

		return  out.toString();
	}
	
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/getTransactionScreens", method = RequestMethod.POST)
	public @ResponseBody
	TransactionSummaryReport getTransactionScreens(@RequestParam(value = "scheduleExecutionId") int scheduleExecutionId, @RequestParam(value = "scheduleScriptId") int scheduleScriptId){
		logger.debug("Entry: getTransactionScreens, scheduleExecutionId->"+scheduleExecutionId+", scheduleScriptId->"+scheduleScriptId);
		
		List<TransactionSummaryReport> transactionSummaryReportList = new ArrayList<TransactionSummaryReport>();
		List<ScheduleScript> scheduleScriptList = new ArrayList<ScheduleScript>();
		TransactionSummaryReport transactionSummaryReport = new TransactionSummaryReport();
		
		try{
			
			if ( scheduleExecutionId > 0 ){
				scheduleScriptList = homeService.getScheduleScript(scheduleExecutionId);
				ScheduleExecution scheduleExecution = scheduledService.getScheduleExecution(scheduleExecutionId);
	
				logger.info("scheduleExecution :"+scheduleExecution);
				
				for ( ScheduleScript scheduleScript: scheduleScriptList){
					
					
					if ( scheduleScript.getScheduleScriptId() == scheduleScriptId) {
						
						transactionSummaryReport.setTestcaseName(scheduleScript.getTransactionTestCase().getTransactionName());
						transactionSummaryReport.setTestcasePath(scheduleScript.getTransactionTestCase().getTestCasePath());
						
						CrawlConfig crawlconfig = null;
						
						try{
							JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();
							crawlconfig = jsonReaderWriter.readJsonObjectFromFile(new CrawlConfig(), scheduleExecution.getCrawlStatusDirectory() +
									File.separator + scheduleScript.getTransactionTestCase().getTestCasePath() + File.separator
									+ UiTestingConstants.CRAWL_CONFIG);
							transactionSummaryReport.setScreenList(crawlconfig.getCrawlStatus().getNavigationList());

						}catch(Exception e){
							logger.error("Exception while reading the Crawl Config object :"+crawlconfig);
						}
						
						//transactionSummaryReportList.add(transactionSummaryReport);
					}
					
					
				}
			}
		}catch(Exception e){
			
		}
		logger.debug("Exit: getTransactionScreens, scheduleExecutionId->"+scheduleExecutionId);
		
		return transactionSummaryReport;
	}
	
	@RequestMapping(value = "/getTransactionSummaryReport", method = RequestMethod.POST)
	public @ResponseBody
	List<TransactionSummaryReport> getTransactionSummaryReport(@RequestParam(value = "scheduleExecutionId") int scheduleExecutionId){
		logger.debug("Entry: getTransactionSummaryReport, scheduleExecutionId->"+scheduleExecutionId);
		
		List<TransactionSummaryReport> transactionSummaryReportList = new ArrayList<TransactionSummaryReport>();
		List<ScheduleScript> scheduleScriptList = new ArrayList<ScheduleScript>();
		
		try{
			
			if ( scheduleExecutionId > 0 ){
				scheduleScriptList = homeService.getScheduleScript(scheduleExecutionId);
				
				logger.info("scheduleScriptList :"+scheduleScriptList);
				
				for ( ScheduleScript scheduleScript: scheduleScriptList){
					TransactionSummaryReport transactionSummaryReport = new TransactionSummaryReport();
					
					if ( scheduleScript.getExecutionStatus() == ExecutionStatus.COMPLETE.getStatusCode() ){
						transactionSummaryReport.setExecutionStatus("Success");
					}else{
						transactionSummaryReport.setExecutionStatus("Failed");
					}
					transactionSummaryReport.setScheduleScriptId(scheduleScript.getScheduleScriptId());
					transactionSummaryReport.setScheduleExecutionId(scheduleExecutionId); //TODO change it to scheduleExecutionID;
					
					transactionSummaryReport.setExecutionStartTime(DateTimeUtil.getDateFormat(scheduleScript.getExecutionStartDate()));
					transactionSummaryReport.setTestcaseName(scheduleScript.getTransactionTestCase().getTransactionName());
					
					ApplicationModuleXref applicationModuleXref = new ApplicationModuleXref();
					applicationModuleXref.setApplicationModuleXrefId(scheduleScript.getTransactionTestCase().getApplicationModuleXrefId());
					applicationModuleXref = applicationModuleService.getApplicationModule(applicationModuleXref);
					
					transactionSummaryReport.setModuleName(applicationModuleXref.getModuleName());
					
					transactionSummaryReportList.add(transactionSummaryReport);
				}
			}
		}catch(Exception e){
			
		}
		logger.info("transactionSummaryReportList :"+transactionSummaryReportList);
		logger.debug("Exit: getTransactionSummaryReport, scheduleExectionId->"+scheduleExecutionId);
		
		return transactionSummaryReportList;
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/getCompletionSummaryForScheduleStatus", method = RequestMethod.POST)
	public @ResponseBody
	UiReportsSummary getCompletionSummaryForScheduleStatus(@RequestParam(value = "scheduleExecutionId") int scheduleExecutionId){
		logger.debug("Entry: getCompletionSummaryForScheduleStatus, scheduleExecutionId->"+scheduleExecutionId);
		List<CompareLink> compareResults = new ArrayList<CompareLink>();
		UiReportsSummary uiReportsSummary = new UiReportsSummary();
		CompareConfig compareConfig = new CompareConfig();
		try{
			
			if ( scheduleExecutionId > 0 ){
				ScheduleExecution scheduleExecution = scheduledService.getScheduleExecution(scheduleExecutionId);
				
				logger.info("scheduleExecution :"+scheduleExecution);
				
				if ( null != scheduleExecution.getCrawlStatusDirectory() ){

					ScheduleExecution baselineScheduleExcecution = scheduledService.getScheduleExecution(scheduleExecution.getBaselineScheduleExecutionId());

					logger.info("baselineScheduleExcecution :"+baselineScheduleExcecution);

					uiReportsSummary = scheduledService.getUiReportSummary(scheduleExecution, baselineScheduleExcecution);
					
					/*if ( scheduleExecution.getBaselineScheduleExecutionId() < 1 ){
						JsonReaderWriter<CrawlConfig> jsonReaderWriterCrawlConfig = new JsonReaderWriter<CrawlConfig>();
						CrawlConfig crawlConfig = jsonReaderWriterCrawlConfig.readJsonObjectFromFile(new CrawlConfig(), 
								scheduleExecution.getCrawlStatusDirectory() + File.separator + UiTestingConstants.CRAWL_CONFIG);
						
						if ( null != crawlConfig.getCrawlStatus().getNavigationList() ){
							uiReportsSummary.setCurrentRunTotalUrls(crawlConfig.getCrawlStatus().getNavigationList().size());
						}
					}*/

				}
			}
			logger.info("uiReportsSummary :"+uiReportsSummary);
		}catch(Exception e){
			logger.error("Exception in getCompletionSummaryForScheduleStatus");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getCompletionSummaryForScheduleStatus");
		return uiReportsSummary;
	}
	
	@RequestMapping(value = "/getComparisonReportsDetails", method = RequestMethod.POST)
	public @ResponseBody
	ReportStatus getComparisonReportsDetail(@RequestParam(value = "scheduleExecutionId") int scheduleExecutionId){
		logger.debug("Entry: getComparisonReportsDetailList, scheduleExecutionId->"+scheduleExecutionId);
		ReportStatus reportStatus = new ReportStatus();
		
		try{
			reportStatus = homeService.getComparisonReportsList(scheduleExecutionId);
			
		}catch(Exception e){
			logger.error("Exception in getComparisonReportsDetailList");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: getComparisonReportsDetailList");
		return reportStatus;
	}
	
	
	
	@RequestMapping(value = "/getReportSelectionStatus", method = RequestMethod.POST)
	public @ResponseBody
	BrokenReportSelection getReportSelectionStatus(@RequestParam(value = "scheduleExecutionId") int scheduleExecutionId){
		logger.debug("Entry: getReportSelectionStatus, scheduleExecutionId->"+scheduleExecutionId);
		BrokenReportSelection reportStatus = new BrokenReportSelection();
		
		try{
			reportStatus.setAnalyticsReport(this.isReportSelectionExist(
					scheduleExecutionId, Constants.ReportPattern.ANALYTICS_REPORT_PATTERN.getReportPatternName()).equals("FileExist"));
			reportStatus.setAnalyticsErrorReport(this.isReportSelectionExist(
					scheduleExecutionId, Constants.ReportPattern.ANALYTICS_ERROR_REPORT_PATTERN.getReportPatternName()).equals("FileExist"));
			reportStatus.setBrokenLinkAvailable(this.isReportSelectionExist
					(scheduleExecutionId, Constants.ReportPattern.BROKEN_URL_REPORT_PATTERN.getReportPatternName()).equals("FileExist"));
			reportStatus.setBrokenLinkResourcesAvailable(this.isReportSelectionExist(
					scheduleExecutionId, Constants.ReportPattern.BROKEN_URL_WITH_RESOURCES_REPORT_PATTERN.getReportPatternName()).equals("FileExist"));
			reportStatus.setLoadTimeAttributeAvailable(this.isReportSelectionExist(
					scheduleExecutionId, Constants.ReportPattern.LOAD_TIME_ATTRIBUTES_REPROT_PATTERN.getReportPatternName()).equals("FileExist"));
			reportStatus.setComparisonReport(this.isReportSelectionExist(
					scheduleExecutionId, Constants.ReportPattern.REGRESSION_COMPARISON_REPORT_PATTERN.getReportPatternName()).equals("FileExist"));
			reportStatus.setImageTextReport(this.isReportSelectionExist(
					scheduleExecutionId, Constants.ReportPattern.IMAGE_TEXT_COMPARISON_REPORT_PATTERN.getReportPatternName()).equals("FileExist"));
			reportStatus.setImageTextBaslineReport(this.isReportSelectionExist(
					scheduleExecutionId, Constants.ReportPattern.IMAGE_TEXT_COMPARISON_REPORT_BASELINE_PATTERN.getReportPatternName()).equals("FileExist"));
			reportStatus.setAllImageZipFile(this.isReportSelectionExist(
					scheduleExecutionId, Constants.ReportPattern.NORMALIZED_ZIP.getReportPatternName()).equals("FileExist"));
			
			
		}catch(Exception e){
			logger.error("Exception in getReportSelectionStatus");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: getReportSelectionStatus");
		return reportStatus;
	}
	
	
	@RequestMapping(value = "/downloadAllBrokenLinkReport", method = RequestMethod.POST)
	public void DownloadAllBrokenLinkReport(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "scheduleExecutionId", required = true) int scheduleExecutionId) {
		logger.debug("Entry: DownloadAllBrokenLinkReport");
		try{
			ScheduleExecution scheduleExecution = scheduledService.getScheduleExecution(scheduleExecutionId);
			String reportsDirectoryPath = scheduleExecution.getReportsDirectory();
			
			this.downloadAllBrokenReportsLinks(reportsDirectoryPath, response,
					servletContext);
			
			reportsDirectoryPath = this.downloadAllBrokenReportsLinks(reportsDirectoryPath, response,
					servletContext);
		
			new MintFileUtils().writeFileToStream(reportsDirectoryPath, response,
					servletContext);
		}catch(Exception e){
			logger.error("Exception in DownloadAllBrokenLinkReport");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: DownloadAllBrokenLinkReport");
	}
	


	@SuppressWarnings("unused")
	private String downloadAllBrokenReportsLinks(String filePath,
			HttpServletResponse response, ServletContext servletContext) {
		logger.debug("Entry: downloadAllBrokenReportsLinks, filePath->" + filePath);
		
		List<File> specificFiles = new ArrayList<File>();
		String moudleFolderPath = StringUtils.substringBeforeLast(filePath,
				"\\");
		String zipFilePath = moudleFolderPath + "\\AllBrokenLinkReports.zip";
		String[] extensions = new String[] { "xlsx" , "xls" };
		File dir = new File(moudleFolderPath);

		//TODO: commenting it because of exception
		//List<File> files = (List<File>) FileUtils.listFiles(dir, extensions,
				//true);
		
		//MintFileUtils.zipIt(zipFilePath, files);
		logger.debug("Entry: downloadAllBrokenReportsLinks, zipFilePath->" + zipFilePath);
		
		return zipFilePath;
	}
	
	@RequestMapping(value = "/isAnalyticsFailureReportExist", method = RequestMethod.POST)
	public @ResponseBody
	String isAnalyticsFailureReportExist(
			@RequestParam(value = "scheduleExecutionId") int scheduleExecutionId) {
		logger.debug("Entry: isAnalyticsFailureReportExist ");
		String isExist =  "NotExist";
		try{
			logger.info("scheduleExecutionId :" + scheduleExecutionId);
			
			ScheduleExecution scheduleExecution = scheduledService.getScheduleExecution(scheduleExecutionId);
			String reportsDirectoryPath = scheduleExecution.getReportsDirectory();
			logger.info("reportsDirectoryPath :" +reportsDirectoryPath);
			if(MintFileUtils.checkFileExists(reportsDirectoryPath, "MINT_Analytics_FAILURE_Report", Constants.FILE.XLSX)) {
				isExist = "FileExist";
			} 
		
		}catch(Exception e){
			logger.error("Exception in DownloadAnalyticsFailureReport");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: isAnalyticsFailureReportExist");
		return isExist;
	}	
	
	@RequestMapping(value = "/isAnalyticsReportExist", method = RequestMethod.POST)
	public @ResponseBody
	String isAnalyticsReportExist(
			@RequestParam(value = "scheduleExecutionId") int scheduleExecutionId) {
		logger.debug("Entry: isAnalyticsReportExist ");
		String isExist =  "NotExist";
		try{
			logger.info("scheduleExecutionId :" + scheduleExecutionId);
			
			ScheduleExecution scheduleExecution = scheduledService.getScheduleExecution(scheduleExecutionId);
			String reportsDirectoryPath = scheduleExecution.getReportsDirectory();
			logger.info("reportsDirectoryPath :" +reportsDirectoryPath);
			if(MintFileUtils.checkFileExists(reportsDirectoryPath, "MINT_Analytics_Report", Constants.FILE.XLSX)) {
				isExist = "FileExist";
			} 
		
		}catch(Exception e){
			logger.error("Exception in DownloadBrokenLinkReport");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: isAnalyticsReportExist");
		return isExist;
	}	
	
	@RequestMapping(value = "/isAnalyticsSummaryReportExist", method = RequestMethod.POST)
	public @ResponseBody
	String isAnalyticsSummaryReportExist(
			@RequestParam(value = "scheduleExecutionId") int scheduleExecutionId) {
		logger.debug("Entry: isAnalyticsSummaryReportExist ");
		String isExist =  "NotExist";
		try{
			logger.info("scheduleExecutionId :" + scheduleExecutionId);
			
			ScheduleExecution scheduleExecution = scheduledService.getScheduleExecution(scheduleExecutionId);
			String reportsDirectoryPath = scheduleExecution.getReportsDirectory();
			logger.info("reportsDirectoryPath :" +reportsDirectoryPath);
			if(MintFileUtils.isFileExists(reportsDirectoryPath + File.separator +  UiTestingConstants.ANALYTICS_SUMMARY_REPORTS)) {
				isExist = "FileExist";
			} 
		
		}catch(Exception e){
			logger.error("Exception in isAnalyticsSummaryReportExist");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: isAnalyticsSummaryReportExist");
		return isExist;
	}

	@RequestMapping(value = { "/viewAnalyticsSummaryReport" }, method = RequestMethod.POST)
	public String viewAnalyticsSummaryReport(RedirectAttributes redirAttr, 
			Model model, @ModelAttribute RegressionTestExecutionForm rteForm) {

		logger.debug("Entry: viewAnalyticsSummaryReport");
		List<AnalyticsSummaryReportUi> analyticsSummaryReportUiList = new ArrayList<AnalyticsSummaryReportUi>();
		try{
			logger.info("scheduleExecutionId :" + rteForm.getScheduleExecutionId());
			analyticsSummaryReportUiList = homeService.getAnalyticsSummaryReportData(rteForm.getScheduleExecutionId());
		}catch(Exception e){
			logger.error("Exception in viewAnalyticsSummaryReport");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: viewAnalyticsSummaryReport");
		rteForm.setReportTabNumber(1);
		redirAttr.addFlashAttribute("regressionTestExecutionForm", rteForm);
		redirAttr.addFlashAttribute("analyticsSummaryList", analyticsSummaryReportUiList);
		
		return "redirect:/ReportLinks.ftl";
	}
	
	@RequestMapping(value = { "/viewAnalyticsDetailsReport" }, method = RequestMethod.POST)
	public String viewAnalyticsDetailsReport(RedirectAttributes redirAttr, 
			Model model, @ModelAttribute RegressionTestExecutionForm rteForm) {

		logger.debug("Entry: viewAnalyticsDetailsReport");
		List<String> webAnalyticsTagDataUiList = new ArrayList<String>();
		List<AnalyticsDetails> analyticsDetailsUiList = new ArrayList<AnalyticsDetails>();
		try{
			logger.info("scheduleExecutionId :" + rteForm.getScheduleExecutionId());
			analyticsDetailsUiList = homeService.getAnalyticsDetailsReportData(rteForm.getScheduleExecutionId(),"Detail");
			
			webAnalyticsTagDataUiList = tagNameUiList(analyticsDetailsUiList,"Detail");
			
		}catch(Exception e){
			logger.error("Exception in viewAnalyticsDetailsReport");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: viewAnalyticsDetailsReport");
		rteForm.setReportTabNumber(2);
		rteForm.setTagNameDataList(webAnalyticsTagDataUiList);
		redirAttr.addFlashAttribute("regressionTestExecutionForm", rteForm);
		redirAttr.addFlashAttribute("specificTagName", "");

		return "redirect:/ReportLinks.ftl";
	}
	
	private List<String> tagNameUiList(List<AnalyticsDetails> analyticsDetailsUiList,String detailOrError){
		List<String> webAnalyticsTagDataUiList = new ArrayList<String>();
		TreeSet<String> tagNameUiList=new TreeSet<String>();  
		Map<String, WebAnalyticsTagData> currentlineTagDataMap;
		Map<String, WebAnalyticsTagData> baselineTagDataMap;
		String currentLineTagName;// ="currentLineTagName";
		
		
		String baselineTagVariableKey;
		String baseLineTagVarName;
		String baseLineTagVarValue;
		String testDataTagVarValue;
		String isError;
		Map<String, String> baseLineTagVariableDataMap;
		Map<String, String> testDataTagVariableDataMap;
		String testResult = "Passed";
		
		try{
			for (AnalyticsDetails analytic :analyticsDetailsUiList){
				currentlineTagDataMap = analytic.getCurrentTagDataMap();
				baselineTagDataMap = analytic.getBaselineTagDataMap();
				Iterator<String> currentlineTagDataMapIterator = currentlineTagDataMap
						.keySet().iterator();
				
				String currentlineKey;
				while (currentlineTagDataMapIterator.hasNext()) {
					currentlineKey = currentlineTagDataMapIterator.next();
					
					currentLineTagName = currentlineTagDataMap.get(currentlineKey)
							.getTagName();
					
					
					
					if(detailOrError.equals("Detail")){
						tagNameUiList.add(currentLineTagName);	
					}else{
						isError = "Failed";
						
						
						baseLineTagVariableDataMap = baselineTagDataMap
								.get(currentlineKey).getTagVariableData();
						testDataTagVariableDataMap = currentlineTagDataMap
								.get(currentlineKey).getTagVariableData();
						
						Iterator tagVariableDataMapIterator = baseLineTagVariableDataMap
								.keySet().iterator();
						
						while (tagVariableDataMapIterator.hasNext()) {
							baselineTagVariableKey = tagVariableDataMapIterator.next()
									.toString();
							
							baseLineTagVarName = baselineTagVariableKey;
							baseLineTagVarValue = baseLineTagVariableDataMap
									.get(baselineTagVariableKey);
							testDataTagVarValue = testDataTagVariableDataMap
									.get(baselineTagVariableKey);
							
							if (currentLineTagName.equals("SiteCatalyst")
									&& baseLineTagVarName.equals("pageName")) {
								if (baseLineTagVarValue.startsWith(":")
										|| baseLineTagVarValue.endsWith(":")
										|| baseLineTagVarValue.equals("null")
										|| baseLineTagVarValue.equals("undefined")
										|| baseLineTagVarValue.equals(null)
										|| baseLineTagVarValue
												.equals("NoTagVarValueFound")
										|| baseLineTagVarValue.equals("")) {
									testResult = "Failed";
								} else {
									testResult = (baseLineTagVarValue
											.equalsIgnoreCase(testDataTagVarValue)) ? "Passed"
											: "Failed";
								}

							} // validate pageName

							else if ((baseLineTagVarValue == null) || (baseLineTagVarValue.equals("undefined")
									|| baseLineTagVarValue.equals("null") ||
							// baseLineTagVarValue.equals("NoTagVarValueFound") ||
							baseLineTagVarValue.equals(""))) {
								testResult = "Failed";
							} // other sitecatalyst variables

							else {
								testResult = (baseLineTagVarValue
										.equalsIgnoreCase(testDataTagVarValue)) ? "Passed"
										: "Failed";
							}
							
							
							if(isError.equals("Failed") && testResult.equals(isError)){
								tagNameUiList.add(currentLineTagName);	
							}
							
							
						}
					}
					
				}
					
			}
			
			webAnalyticsTagDataUiList.addAll(tagNameUiList);
		}catch(Exception e){
			logger.error("Exception in viewAnalyticsDetailsReport");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		
		return webAnalyticsTagDataUiList;
	}
	
	@RequestMapping(value = { "/getAllDetailedReportLinksData" }, method = RequestMethod.POST)
	public String getAllDetailedReportLinksData(RedirectAttributes redirAttr, 
			Model model, @ModelAttribute RegressionTestExecutionForm rteForm) {

		logger.debug("Entry: getAllDetailedReportLinksData");
		List<String> webAnalyticsTagDataUiList = new ArrayList<String>();
		List<AnalyticsDetails> analyticsDetailedList = new ArrayList<AnalyticsDetails>();
		List<AnalyticsDetails> analyticsDetailedUiList = new ArrayList<AnalyticsDetails>();
		Map<String, WebAnalyticsTagData> currentlineTagDataMap;
		String currentLineTagName;// ="currentLineTagName";
		 
		try{
			
			if(rteForm.getDetailOrError().equals("Error") &&
					rteForm.getTagName().equals("ALL")){
				logger.info("scheduleExecutionId :" + rteForm.getScheduleExecutionId());
				analyticsDetailedList = homeService.getAnalyticsDetailsReportData(rteForm.getScheduleExecutionId(),rteForm.getDetailOrError());
				webAnalyticsTagDataUiList = tagNameUiList(analyticsDetailedList,rteForm.getDetailOrError());
				
				for (AnalyticsDetails analytic :analyticsDetailedList){
					currentlineTagDataMap = analytic.getCurrentTagDataMap();
					
					Iterator<String> currentlineTagDataMapIterator = currentlineTagDataMap
							.keySet().iterator();
					
					String currentlineKey;
					while (currentlineTagDataMapIterator.hasNext()) {
						currentlineKey = currentlineTagDataMapIterator.next();
						
						currentLineTagName = currentlineTagDataMap.get(currentlineKey)
								.getTagName();
						
					}
					analyticsDetailedUiList.add(analytic);
				}
			}
			
		}catch(Exception e){
			logger.error("Exception in getAllDetailedReportLinksData");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getAllDetailedReportLinksData");
		
		rteForm.setTagNameDataList(webAnalyticsTagDataUiList);
		
		redirAttr.addFlashAttribute("analyticsErrorList", analyticsDetailedUiList);
		rteForm.setReportTabNumber(3);
		
		redirAttr.addFlashAttribute("regressionTestExecutionForm", rteForm);
		redirAttr.addFlashAttribute("specificTagName", "ALL");
		
		return "redirect:/ReportLinks.ftl";
	}
	
	@RequestMapping(value = { "/getDetailedReportLinksData" }, method = RequestMethod.POST)
	public String getDetailedReportLinksData(RedirectAttributes redirAttr, 
			Model model, @ModelAttribute RegressionTestExecutionForm rteForm) {

		logger.debug("Entry: getDetailedReportLinksData");
		List<String> webAnalyticsTagDataUiList = new ArrayList<String>();
		List<AnalyticsDetails> analyticsDetailedList = new ArrayList<AnalyticsDetails>();
		List<AnalyticsDetails> analyticsDetailedUiList = new ArrayList<AnalyticsDetails>();
		Map<String, WebAnalyticsTagData> currentlineTagDataMap;
		String currentLineTagName;// ="currentLineTagName";
		String status;
		 
		try{
			
			logger.info("scheduleExecutionId :" + rteForm.getScheduleExecutionId());
			analyticsDetailedList = homeService.getAnalyticsDetailsReportData(rteForm.getScheduleExecutionId(),rteForm.getDetailOrError());
			webAnalyticsTagDataUiList = tagNameUiList(analyticsDetailedList,rteForm.getDetailOrError());
			
			for (AnalyticsDetails analytic :analyticsDetailedList){
				status = "NO";
				currentlineTagDataMap = analytic.getCurrentTagDataMap();
				
				Iterator<String> currentlineTagDataMapIterator = currentlineTagDataMap
						.keySet().iterator();
				
				String currentlineKey;
				while (currentlineTagDataMapIterator.hasNext()) {
					currentlineKey = currentlineTagDataMapIterator.next();
					
					currentLineTagName = currentlineTagDataMap.get(currentlineKey)
							.getTagName();
					
					if(currentLineTagName.equals(rteForm.getTagName())){
						status = "YES";
					}
				}
				if(status.equals("YES")){
					analyticsDetailedUiList.add(analytic);
				}
			}
		}catch(Exception e){
			logger.error("Exception in getDetailedReportLinksData");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: getDetailedReportLinksData");
		
		rteForm.setTagNameDataList(webAnalyticsTagDataUiList);
		
		
		if(rteForm.getDetailOrError().equals("Detail")){
			redirAttr.addFlashAttribute("analyticsDetailsList", analyticsDetailedUiList);
			rteForm.setReportTabNumber(2);
		}else{
			redirAttr.addFlashAttribute("analyticsErrorList", analyticsDetailedUiList);
			rteForm.setReportTabNumber(3);
		}
		redirAttr.addFlashAttribute("regressionTestExecutionForm", rteForm);
		redirAttr.addFlashAttribute("specificTagName", rteForm.getTagName());
		
		return "redirect:/ReportLinks.ftl";
	}
	
	@RequestMapping(value = { "/viewAnalyticsErrorReport" }, method = RequestMethod.POST)
	public String viewAnalyticsErrorReport(RedirectAttributes redirAttr, 
			Model model, @ModelAttribute RegressionTestExecutionForm rteForm) {

		logger.debug("Entry: viewAnalyticsErrorReport");
		List<String> webAnalyticsTagDataUiList = new ArrayList<String>();
		List<AnalyticsDetails> analyticsDetailsUiList = new ArrayList<AnalyticsDetails>();
		
		try{
			logger.info("scheduleExecutionId :" + rteForm.getScheduleExecutionId());
			analyticsDetailsUiList = homeService.getAnalyticsDetailsReportData(rteForm.getScheduleExecutionId(),"Error");
			
			webAnalyticsTagDataUiList = tagNameUiList(analyticsDetailsUiList,"Error");
		}catch(Exception e){
			logger.error("Exception in viewAnalyticsErrorReport");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: viewAnalyticsErrorReport");
		rteForm.setReportTabNumber(3);
		rteForm.setTagNameDataList(webAnalyticsTagDataUiList);
		redirAttr.addFlashAttribute("regressionTestExecutionForm", rteForm);
		redirAttr.addFlashAttribute("specificTagName", "");
		
		return "redirect:/ReportLinks.ftl";
	}
	/*@RequestMapping(value = "/viewAnalyticsSummaryReport", method = RequestMethod.POST)
	public @ResponseBody List<AnalyticsSummaryReportUi> viewAnalyticsSummaryReport(@RequestParam(value = "scheduleExecutionId", required = true) int scheduleExecutionId) {
		logger.debug("Entry: viewAnalyticsSummaryReport");
		List<AnalyticsSummaryReportUi> analyticsSummaryReportUiList = new ArrayList<AnalyticsSummaryReportUi>();
		try{
			logger.info("scheduleExecutionId :" + scheduleExecutionId);
			analyticsSummaryReportUiList = homeService.getAnalyticsSummaryReportData(scheduleExecutionId);
		}catch(Exception e){
			logger.error("Exception in viewAnalyticsSummaryReport");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: viewAnalyticsSummaryReport");
		return analyticsSummaryReportUiList;
	}*/
	
	
	@RequestMapping(value = "/downloadAnalyticsFailureReport", method = RequestMethod.POST)
	public void DownloadAnalyticsFailureReport(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "scheduleExecutionId", required = true) int scheduleExecutionId) {
		logger.debug("Entry: DownloadAnalyticsFailureReport");
		try{
			logger.info("scheduleExecutionId :" + scheduleExecutionId);
			
			ScheduleExecution scheduleExecution = scheduledService.getScheduleExecution(scheduleExecutionId);
			String harReportsDirectoryPath = scheduleExecution.getReportsDirectory();
			logger.info("harReportsDirectoryPath :" +harReportsDirectoryPath);
			if(MintFileUtils.checkFileExists(harReportsDirectoryPath, "MINT_ANALYTICS_ERROR_REPORT", Constants.FILE.XLSX)) {
				logger.info("DownloadAnalyticsFailureReport : Report exists" );
				new MintFileUtils().writeReportToStream(harReportsDirectoryPath, "MINT_ANALYTICS_ERROR_REPORT", response,servletContext, Constants.FILE.XLSX);
			} else {
				logger.info("DownloadAnalyticsFailureReport : Report not found in the path" );
			}
		
		}catch(Exception e){
			logger.error("Exception in DownloadAnalyticsFailureReport");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: DownloadAnalyticsFailureReport");
	}
	
	@RequestMapping(value = "/downloadAnalyticsReport", method = RequestMethod.POST)
	public void DownloadAnalyticsReport(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "scheduleExecutionId", required = true) int scheduleExecutionId) {
		logger.debug("Entry: DownloadAnalyticsReport");
		try{
			logger.info("scheduleExecutionId :" + scheduleExecutionId);
			
			ScheduleExecution scheduleExecution = scheduledService.getScheduleExecution(scheduleExecutionId);
			String harReportsDirectoryPath = scheduleExecution.getReportsDirectory();
			logger.info("harReportsDirectoryPath :" +harReportsDirectoryPath);
			if(MintFileUtils.checkFileExists(harReportsDirectoryPath, "MINT_ANALYTICS_REPORT", Constants.FILE.XLSX)) {
				logger.info("DownloadAnalyticsReport : Report exists" );
				new MintFileUtils().writeReportToStream(harReportsDirectoryPath, "MINT_ANALYTICS_REPORT", response,servletContext, Constants.FILE.XLSX);
			} else {
				logger.info("DownloadAnalyticsReport : Report not found in the path" );
			}
		
		}catch(Exception e){
			logger.error("Exception in DownloadAnalyticsReport");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: DownloadAnalyticsReport");
	}
	
	@RequestMapping(value = "/isComparisonReportExist", method = RequestMethod.POST)
	public @ResponseBody
	String isComparisonReportExist(
			@RequestParam(value = "scheduleExecutionId") int scheduleExecutionId) {
		logger.debug("Entry: isComparisonReportExist ");
		String isExist =  "NotExist";
		try{
			logger.info("scheduleExecutionId :" + scheduleExecutionId);
			
			ScheduleExecution scheduleExecution = scheduledService.getScheduleExecution(scheduleExecutionId);
			String reportsDirectoryPath = scheduleExecution.getReportsDirectory();
			logger.info("reportsDirectoryPath :" +reportsDirectoryPath);
			if(MintFileUtils.checkFileExists(reportsDirectoryPath, "MINT_REGRESSION_COMPARISON_REPORT", Constants.FILE.XLSX)) {
				isExist = "FileExist";
			} 
		
		}catch(Exception e){
			logger.error("Exception in DownloadBrokenLinkReport");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: isComparisonReportExist");
		return isExist;
	}	
	
	@RequestMapping(value = "/downloadComparisonReport", method = RequestMethod.POST)
	public void DownloadComparisonReport(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "scheduleExecutionId", required = true) int scheduleExecutionId) {
		logger.debug("Entry: DownloadComparisonReport");
		try{
			logger.info("scheduleExecutionId :" + scheduleExecutionId);
			
			ScheduleExecution scheduleExecution = scheduledService.getScheduleExecution(scheduleExecutionId);
			String reportsDirectoryPath = scheduleExecution.getReportsDirectory();
			logger.info("harReportsDirectoryPath :" +reportsDirectoryPath);
			if(MintFileUtils.checkFileExists(reportsDirectoryPath, "MINT_REGRESSION_COMPARISON_REPORT", Constants.FILE.XLSX)) {
				logger.info("DownloadComparisonReport : Report exists" );
				new MintFileUtils().writeReportToStream(reportsDirectoryPath, "MINT_REGRESSION_COMPARISON_REPORT", response,servletContext, Constants.FILE.XLSX);
			} else {
				logger.info("DownloadComparisonReport : Report not found in the path" );
			}
		
		}catch(Exception e){
			logger.error("Exception in DownloadComparisonReport");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: DownloadComparisonReport");
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/SaveWebserviceSuites", method = RequestMethod.POST)
	public String saveWebserviceSuites(RedirectAttributes redirAttr,
			Model model, @ModelAttribute RegressionTestExecutionForm regressionTestExecutionForm) {
		logger.debug("Entry : saveWebserviceSuites");
		WSSchedule wsSchedule = new WSSchedule();
		Boolean result = false;
		String emailaddress="";
		int insertId = 0;
		Integer webserviceSuiteId = 0;
		List<Integer> serviceIdList = new ArrayList<Integer>();
		List<Boolean> endPointCheckList = new ArrayList<Boolean>();
		int envId = 0;
		Boolean endPointAvailable = false;
		String dateval = DataConstants.KEYS.BLANK;
		DateFormat formatter;
		DateFormat fromUser = null;
		String reccurencedateval = DataConstants.KEYS.BLANK;String stringTime = DataConstants.KEYS.BLANK;
		try {
			fromUser = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			if (regressionTestExecutionForm.getRecurrence() != null
					&& regressionTestExecutionForm.getRecurrence().equals("on")) {
				wsSchedule.setReocurrence(true);
			} else {
				wsSchedule.setReocurrence(false);
			}
			Users currentUser = (Users) httpRequest.getSession().getAttribute("currentMintUser");
			wsSchedule.setUserId(currentUser.getUserName());
			wsSchedule.setStartDate(new Date());
			Integer wsSuiteId = Integer.parseInt(regressionTestExecutionForm.getWebserviceSuiteId());
			if (regressionTestExecutionForm.getWebserviceSuiteId() != null
					&&  wsSuiteId> 0) {
				wsSchedule.setWebserviceSuiteId(wsSuiteId);


				webserviceSuiteId = wsSchedule.getWebserviceSuiteId();
				
				List<WebserviceSuiteService> webserviceSuiteServicelist = webserviceTestingService.
				getWebserviceSuiteService(webserviceSuiteId);
				for ( WebserviceSuiteService wsSuiteService :webserviceSuiteServicelist){
					serviceIdList.add(wsSuiteService.getServiceId());
				}
				envId = webserviceTestingService.getEnvironment(wsSuiteId);
	
				Map<Integer, Boolean> endPointCheckMap = new LinkedHashMap<Integer, Boolean>();

				for (Integer serviceId : serviceIdList) {
					endPointAvailable = webserviceTestingService.getEndPointForService(
							serviceId, envId);
					endPointCheckList.add(endPointAvailable);
					endPointCheckMap.put(serviceId, endPointAvailable);
				}
			}
			
			
			
			formatter = new SimpleDateFormat("HH:mm");
			if (regressionTestExecutionForm.getScheduleType().equalsIgnoreCase(Constants.SCHEDULER.SCHEDULED_RUN)){
				wsSchedule.setScheduled(true);
				dateval = regressionTestExecutionForm.getSchDateTime();

				Date t = (Date) formatter.parse(dateval);
				Time timeValue = new Time(formatter.parse(dateval).getTime());
				wsSchedule.setStartTime(timeValue);
				
				
				stringTime = DateUtil.convertToString(new Date(), DateUtil.WS_DATE_PICKER_FORMAT) + " "+ dateval;
				/*Date t = (Date) formatter.parse(stringTime);
				Time timeValue = new Time(formatter.parse(stringTime).getTime());*/
				wsSchedule.setStartTime(DateUtil.convertToDate(stringTime, DateUtil.UI_DATE_PICKER_FORMAT));
				
				
				
				if (regressionTestExecutionForm.getExecuteBy().equals(Constants.SCHEDULER.WEEKLY_SCHEDULE)) {
					if ( regressionTestExecutionForm.getRecurrence() != null &&  regressionTestExecutionForm.getRecurrence().equals("on") ) {
						wsSchedule.setReocurrence(true);
					} else {
						wsSchedule.setReocurrence(false);
						
					}
					
					wsSchedule.setScheduleType(Constants.SCHEDULER.BY_WEEKLY);//Weekwise
					
					String weekwise = DataConstants.KEYS.BLANK;
					if (regressionTestExecutionForm.getRecurrenceidweekly1() != null && regressionTestExecutionForm.getRecurrenceidweekly1().equals("on")) {
						weekwise += "Monday,";
					}
					if (regressionTestExecutionForm.getRecurrenceidweekly2() != null && regressionTestExecutionForm.getRecurrenceidweekly2().equals("on")) {
						weekwise += "Tuesday,";
					}
					if (regressionTestExecutionForm.getRecurrenceidweekly3() != null && regressionTestExecutionForm.getRecurrenceidweekly3().equals("on")) {
						weekwise += "Wednesday,";
					}
					if (regressionTestExecutionForm.getRecurrenceidweekly4() != null && regressionTestExecutionForm.getRecurrenceidweekly4().equals("on")) {
						weekwise += "Thursday,";
					}
					if (regressionTestExecutionForm.getRecurrenceidweekly5() != null && regressionTestExecutionForm.getRecurrenceidweekly5().equals("on")) {
						weekwise += "Friday,";
					}
					if (regressionTestExecutionForm.getRecurrenceidweekly6() != null && regressionTestExecutionForm.getRecurrenceidweekly6().equals("on")) {
						weekwise += "Saturday,";
					}
					weekwise += "Sunday,";
					if (regressionTestExecutionForm.getRecurrenceidweekly7() != null && regressionTestExecutionForm.getRecurrenceidweekly7().equals("on")) {
					}
					wsSchedule.setRecurrenceDateWise(null);
					wsSchedule.setScheduledDay(weekwise.substring(0, weekwise.length()-1));
					
					
				}	else if (regressionTestExecutionForm.getExecuteBy().equals(Constants.SCHEDULER.DATE_SCHEDULE)) {
					wsSchedule.setScheduleType(Constants.SCHEDULER.BY_DATE);//Datewise
					reccurencedateval= regressionTestExecutionForm.getScheduledDate().substring(0,11) + " " + timeValue;
					Date nowDate=fromUser.parse(reccurencedateval);
					wsSchedule.setRecurrenceDateWise(nowDate);
					wsSchedule.setScheduledDay("NA");
				}
				
			}else {
				wsSchedule.setScheduled(false);
				stringTime = DateUtil.convertToString(new Date(), DateUtil.WS_DATE_PICKER_FORMAT) + " "+ formatter.format(new Date());
				/*Date t = (Date) formatter.parse(stringTime);
				Time timeValue = new Time(formatter.parse(stringTime).getTime());*/
				wsSchedule.setStartTime(DateUtil.convertToDate(stringTime, DateUtil.UI_DATE_PICKER_FORMAT));
			}
			
			
			/*if (regressionTestExecutionForm.getScheduleType().equals("Scheduled")) {
				String dateval = regressionTestExecutionForm.getSchDateTime();
				Date t = (Date) formatter.parse(dateval);
				Time timeValue = new Time(formatter.parse(dateval).getTime());
				wsSchedule.setStartTime(timeValue);
			} else {
				String stringTime = formatter.format(new Date());
				Date t = (Date) formatter.parse(stringTime);
				Time timeValue = new Time(formatter.parse(stringTime).getTime());
				wsSchedule.setStartTime(timeValue);
			}
*/
			if ( null != regressionTestExecutionForm.getEmailRecepients() && regressionTestExecutionForm.getEmailRecepients().trim().length() > 0 ){
				wsSchedule.setEmailIds(currentUser.getEmailId() + "," + regressionTestExecutionForm.getEmailRecepients());
			}else{
				wsSchedule.setEmailIds(currentUser.getEmailId());
			}
			
			if (!endPointCheckList.contains(false)) {
				//wsSchedule.setScheduled(true);
				insertId = webserviceTestingService
						.insertWSSchedule(wsSchedule); 
				if (insertId > 0) {
					result = true;
				}

				if (regressionTestExecutionForm.getWsResultsId() > 0) {
					WSBaseline wsBaseline = new WSBaseline();
					Date now = new Date();
					wsBaseline.setCreatedDateTime(now);
					int wsBaselineScheduleId = regressionTestExecutionForm
							.getWsResultsId();
					wsBaseline.setWsBaselineScheduleId(wsBaselineScheduleId);
				
					wsBaseline.setWsScheduleId(insertId);
					result = result
							&& webserviceTestingService.wsSaveBaseline(wsBaseline);
				}

				if (result) {
					redirAttr.addFlashAttribute("Success", "Web Service Suite Has been scheduled");
				} else {
					redirAttr.addFlashAttribute("error","Error while scheduling the webservice Suite");
				}
			}
			else {
				redirAttr.addFlashAttribute(
						"error", "Sorry! End Point is not available for some service(s) in the selected Test Suite. "
								+ "Please enter the endpoint details in the Web Service Add details page");
			}
			regressionTestExecutionForm.setSetupTabNumber(5);
			regressionTestExecutionForm.setSolutionType(Constants.SolutionType.WEBSERVICE.getSolutionTypeId());
			model.addAttribute("solutionType", Constants.SolutionType.WEBSERVICE.getSolutionTypeId());
			redirAttr.addFlashAttribute("regressionTestExecutionForm", regressionTestExecutionForm);
		} catch (Exception e) {
			logger.error("Exception in wsReportsRetrieve.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit : saveWebserviceSuites");
		return "redirect:/Home.ftl";
	}
	
	@RequestMapping(value = "/WSReportsHome", method = RequestMethod.POST)
	public String wsReportsRetrieve(RedirectAttributes redirAttr,
			Model model, @ModelAttribute RegressionTestExecutionForm regressionTestExecutionForm) {
		logger.info("Entry: wsReportsRetrieve, regressionTestExecutionForm->"+regressionTestExecutionForm);
		List<WSReportsData> wsReportsDataList = null;
		try {
			String webserviceSuiteId = regressionTestExecutionForm.getWebserviceSuiteId();
			regressionTestExecutionForm.setSetupTabNumber(6);
			regressionTestExecutionForm.setSolutionType(Constants.SolutionType.WEBSERVICE.getSolutionTypeId());
			model.addAttribute("solutionType", Constants.SolutionType.WEBSERVICE.getSolutionTypeId());
			redirAttr.addFlashAttribute("webserviceSuiteId", webserviceSuiteId);
			wsReportsDataList = webserviceTestingService.getWSReports(
					Integer.parseInt(webserviceSuiteId), regressionTestExecutionForm.getWsScheduleId());
			redirAttr.addFlashAttribute("wsReportsDataList", wsReportsDataList);
			redirAttr.addFlashAttribute("webserviceSuits", webserviceTestingService.getWSTestSuitesCreatedByAll());
			redirAttr.addFlashAttribute("regressionTestExecutionForm",regressionTestExecutionForm);
			
		} catch (Exception e) {
			logger.error("Exception in wsReportsRetrieve.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.info("Exit: wsReportsRetrieve");
		return "redirect:/Home.ftl";
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/removeWsSuitDetails", method = RequestMethod.POST)
	public String removeWsSuitDetails(RedirectAttributes redirAttr,
			Model model, @ModelAttribute RegressionTestExecutionForm regressionTestExecutionForm) {
		logger.debug("Entry: removeSuitDetails");
		StringBuilder respStr = new StringBuilder();
		WebserviceSuite suit = new WebserviceSuite();
		boolean result=false;
		try{
			suit = homeService.getSavedWsSuits( Integer.parseInt(regressionTestExecutionForm.getWebserviceSuiteId()));
			if(suit.getWebserviceSuiteId() > 0){
				result = webserviceTestingService.removeSuitDetails(suit);
				if(result==true){
					redirAttr.addFlashAttribute("Success", "Selelected webservice suit deleted successfully");
				}else{
					redirAttr.addFlashAttribute("error",
					"Error while deleting suit details");
				}
			}
		}catch(Exception exp){
			logger.error(
					"Exception occured during getResultsExecutionDates execution.",
					exp);
		}
		logger.debug("Exist: removeSuitDetails");
		return "redirect:/Home.ftl";
	}
	
	@SuppressWarnings("unused")
	@RequestMapping(value = "/CreateWebserviceSuit", method = RequestMethod.POST)
	public String createWebserviceSuit(RedirectAttributes redirAttr,
			Model model, @ModelAttribute RegressionTestExecutionForm regressionTestExecutionForm,WebserviceSetupForm webserviceSetupForm) {
		logger.debug("Entry: createWebserviceSuit");
		WebserviceSuite webserviceSuite = new WebserviceSuite();
		try {
			webserviceSetupForm.setSetupTabNumber(Constants.WebserviceTest.CREATE_WS_TESTSUITE);
			
			redirAttr.addFlashAttribute("regressionTestExecutionForm", regressionTestExecutionForm);
			
				redirAttr.addFlashAttribute("addAnother", "Selected Service-Operation(s)");
				
				redirAttr.addFlashAttribute("webserviceSetupForm", webserviceSetupForm);
		
		} catch(Exception e){
			logger.error("Exception in editWsSuitDetails.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: CreateWebserviceSuit");
		return "redirect:/WebserviceSetup.ftl";
	}
	
	@RequestMapping(value = "/isReportSelectionExist", method = RequestMethod.POST)
	public @ResponseBody
	String isReportSelectionExist(
			@RequestParam(value = "scheduleExecutionId") int scheduleExecutionId,
			@RequestParam(value = "reportName") String reportName) {
		logger.debug("Entry: isReportSelectionExist ");
		
		String isExist =  "NotExist";
		String reportNameStartsWith = null;
		String reportType = Constants.FILE.XLSX;
		
		try{
			
			ScheduleExecution scheduleExecution = scheduledService.getScheduleExecution(scheduleExecutionId);
			String reportsDirectoryPath = scheduleExecution.getReportsDirectory();
			if(reportName.equals(Constants.ReportPattern.ANALYTICS_REPORT_PATTERN.getReportPatternName())){
				reportNameStartsWith = Constants.ReportPattern.ANALYTICS_REPORT_PATTERN.getReportPatternName();
			}else if(reportName.equals(Constants.ReportPattern.ANALYTICS_ERROR_REPORT_PATTERN.getReportPatternName())){
				reportNameStartsWith = Constants.ReportPattern.ANALYTICS_ERROR_REPORT_PATTERN.getReportPatternName();
			}else if(reportName.equals(Constants.ReportPattern.BROKEN_URL_REPORT_PATTERN.getReportPatternName())){
				reportNameStartsWith = Constants.ReportPattern.BROKEN_URL_REPORT_PATTERN.getReportPatternName();
			}else if(reportName.equals(Constants.ReportPattern.BROKEN_URL_WITH_RESOURCES_REPORT_PATTERN.getReportPatternName())){
				reportNameStartsWith = Constants.ReportPattern.BROKEN_URL_WITH_RESOURCES_REPORT_PATTERN.getReportPatternName();
			}else if(reportName.equals(Constants.ReportPattern.LOAD_TIME_ATTRIBUTES_REPROT_PATTERN.getReportPatternName())){
				reportNameStartsWith = Constants.ReportPattern.LOAD_TIME_ATTRIBUTES_REPROT_PATTERN.getReportPatternName();
			}else if(reportName.equals(Constants.ReportPattern.REGRESSION_COMPARISON_REPORT_PATTERN.getReportPatternName())){
				reportNameStartsWith = Constants.ReportPattern.REGRESSION_COMPARISON_REPORT_PATTERN.getReportPatternName();
			}else if(reportName.equals(Constants.ReportPattern.IMAGE_TEXT_COMPARISON_REPORT_PATTERN.getReportPatternName())){
				reportNameStartsWith = Constants.ReportPattern.IMAGE_TEXT_COMPARISON_REPORT_PATTERN.getReportPatternName();
			} else if(reportName.equals(Constants.ReportPattern.IMAGE_TEXT_COMPARISON_REPORT_BASELINE_PATTERN.getReportPatternName())){
				reportNameStartsWith = Constants.ReportPattern.IMAGE_TEXT_COMPARISON_REPORT_BASELINE_PATTERN.getReportPatternName();
			}
			else if(reportName.equals(Constants.ReportPattern.NORMALIZED_ZIP.getReportPatternName())){
				//reportsDirectoryPath += com.cts.mint.common.utils.Constants.BrokenReportSelection.ALL_IMAGE_DIR;
				reportNameStartsWith = Constants.ReportPattern.NORMALIZED_ZIP.getReportPatternName();
				reportType = Constants.FILE.ZIP;
			}
			if(MintFileUtils.checkFileExists(reportsDirectoryPath, reportNameStartsWith, reportType)) {
				isExist = "FileExist";
			} 
		}catch(Exception e){
			logger.error("Exception in isReportSelectionExist");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: isReportSelectionExist");
		return isExist;
	}	
	
	@RequestMapping(value = "/downloadReportSelection", method = RequestMethod.POST)
	public void DownloadReportSelection(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "scheduleExecutionId", required = true) int scheduleExecutionId,
			@RequestParam(value = "reportPatternName") int reportPatternName) {
		logger.debug("Entry: DownloadReportSelection");
		String reportPattern = null;
		String fileType = Constants.FILE.XLSX;
		
		try{
			
			logger.info("scheduleExecutionId :" + scheduleExecutionId);
			ScheduleExecution scheduleExecution = scheduledService.getScheduleExecution(scheduleExecutionId);
			String harReportsDirectoryPath = scheduleExecution.getReportsDirectory();
			logger.info("harReportsDirectoryPath :" +harReportsDirectoryPath);
			
			if(reportPatternName == Constants.ReportPattern.ANALYTICS_REPORT_PATTERN.getReportPatternId()){
				reportPattern = Constants.ReportPattern.ANALYTICS_REPORT_PATTERN.getReportPatternName();
			}else if(reportPatternName == Constants.ReportPattern.ANALYTICS_ERROR_REPORT_PATTERN.getReportPatternId()){
				reportPattern = Constants.ReportPattern.ANALYTICS_ERROR_REPORT_PATTERN.getReportPatternName();
			}else if(reportPatternName == Constants.ReportPattern.BROKEN_URL_REPORT_PATTERN.getReportPatternId()){
				reportPattern = Constants.ReportPattern.BROKEN_URL_REPORT_PATTERN.getReportPatternName();
			}else if(reportPatternName == Constants.ReportPattern.BROKEN_URL_WITH_RESOURCES_REPORT_PATTERN.getReportPatternId()){
				reportPattern = Constants.ReportPattern.BROKEN_URL_WITH_RESOURCES_REPORT_PATTERN.getReportPatternName();
			}else if(reportPatternName == Constants.ReportPattern.LOAD_TIME_ATTRIBUTES_REPROT_PATTERN.getReportPatternId()){
				reportPattern = Constants.ReportPattern.LOAD_TIME_ATTRIBUTES_REPROT_PATTERN.getReportPatternName();
			}else if(reportPatternName == Constants.ReportPattern.REGRESSION_COMPARISON_REPORT_PATTERN.getReportPatternId()){
				reportPattern = Constants.ReportPattern.REGRESSION_COMPARISON_REPORT_PATTERN.getReportPatternName();
			}else if(reportPatternName == Constants.ReportPattern.IMAGE_TEXT_COMPARISON_REPORT_PATTERN.getReportPatternId()){
				reportPattern = Constants.ReportPattern.IMAGE_TEXT_COMPARISON_REPORT_PATTERN.getReportPatternName();
			}else if(reportPatternName == Constants.ReportPattern.IMAGE_TEXT_COMPARISON_REPORT_BASELINE_PATTERN.getReportPatternId()){
				reportPattern = Constants.ReportPattern.IMAGE_TEXT_COMPARISON_REPORT_BASELINE_PATTERN.getReportPatternName();
			} else if(reportPatternName == Constants.ReportPattern.NORMALIZED_ZIP.getReportPatternId()){
				reportPattern = Constants.ReportPattern.NORMALIZED_ZIP.getReportPatternName();
				//harReportsDirectoryPath += com.cts.mint.common.utils.Constants.BrokenReportSelection.ALL_IMAGE_DIR;
				fileType = Constants.FILE.ZIP;
			}
			
			if(MintFileUtils.checkFileExists(harReportsDirectoryPath, reportPattern, fileType)) {
				logger.info("DownloadReportSelection : Report exists" +reportPattern );
				
				new MintFileUtils().writeReportToStream(harReportsDirectoryPath, reportPattern, response,servletContext, fileType);
			} else {
				logger.info("DownloadReportSelection : Report not found in the path"+reportPattern  );
			}
		
		}catch(Exception e){
			logger.error("Exception in DownloadReportSelection"+reportPattern );
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: DownloadReportSelection");
	}
	
	@RequestMapping(value = "/ReportLinks", method = RequestMethod.POST)
	public String reportLinks(RedirectAttributes redirAttr,
			Model model, @ModelAttribute RegressionTestExecutionForm regressionTestExecutionForm) {
		logger.debug("Entry: reportLinks"); 
		try {
			redirAttr.addFlashAttribute("regressionTestExecutionForm", regressionTestExecutionForm);
		} catch(Exception e){
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: reportLinks");
		return "common/ReportLinks";
	}
	
	@RequestMapping(value = "/ReportLinks", method = RequestMethod.GET)
	public String reportLinksGet(RedirectAttributes redirAttr,
			Model model, @ModelAttribute RegressionTestExecutionForm regressionTestExecutionForm) {
		logger.debug("Entry: reportLinksGet"); 
		try {
			redirAttr.addFlashAttribute("regressionTestExecutionForm", regressionTestExecutionForm);
		} catch(Exception e){
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: reportLinksGet");
		return "common/ReportLinks";
	}
	
	@RequestMapping(value = "/getSiteCatalystTagDataMapValues", method = RequestMethod.POST)
	public @ResponseBody
	List<Pair> getSiteCatalystTagDataMapValues(@RequestParam(value = "scheduleExecutionId") Integer scheduleExecutionId,
											   @RequestParam(value = "baseLinePageUrl") String baseLinePageUrl,
											   @RequestParam(value = "pageTitle") String pageTitle,
											   @RequestParam(value = "tagName") String tagName,
											   @RequestParam(value = "detailsOrError") String detailsOrError){
		logger.debug("Entry: getSiteCatalystTagDataMapValues, scheduleExecutionId->"+scheduleExecutionId);

		Map<String, String> baseLineTagVariableDataMap;
		Map<String, String> testDataTagVariableDataMap;
		String baselineTagVariableKey;
		String baseLineTagVarName;
		String baseLineTagVarValue;
		String actualTagVarValue;
		String testDataTagVarValue;
		String pageUrlCheck = new String();
		String pageTitleCheck = new String();
		String testResult = "Passed";
		boolean flag= false;
		String isError;
		String baseLineTagName;
		List<Pair> siteCatalystPair = new ArrayList<Pair>();
		Pair pairEntry;
		List<AnalyticsDetails> analyticsDetailsUiList = new ArrayList<AnalyticsDetails>();
		Map<String, WebAnalyticsTagData> baselineTagDataMap;
		Map<String, WebAnalyticsTagData> testDataTagDataMap;
		try{
			
			if(detailsOrError.equals("Detail")){
				analyticsDetailsUiList = homeService.getAnalyticsDetailsReportData(scheduleExecutionId,"Detail");	
				isError = "Passed";
			}else{
				analyticsDetailsUiList = homeService.getAnalyticsDetailsReportData(scheduleExecutionId,"Error");
				isError = "Failed";
			}
			
			
			for (AnalyticsDetails analytic :  analyticsDetailsUiList ){
				
				if(analytic.getBaseLinePageUrl()!=null){
					pageUrlCheck = analytic.getBaseLinePageUrl();	
					pageTitleCheck = "";
				}else{
					pageUrlCheck="";
					pageTitleCheck = analytic.getPageTitle();
				}
				
				
				if(pageUrlCheck.equals(baseLinePageUrl) || pageTitleCheck.equals(pageTitle)){
					baselineTagDataMap = analytic.getBaselineTagDataMap();
					testDataTagDataMap = analytic.getCurrentTagDataMap();
					Iterator<String> baselineTagDataMapIterator = baselineTagDataMap
							.keySet().iterator();
					String baselineKey;
					while (baselineTagDataMapIterator.hasNext()) {
						flag = false;
						baselineKey = baselineTagDataMapIterator.next();
						
						baseLineTagName = baselineTagDataMap.get(baselineKey)
								.getTagName();
						baseLineTagVariableDataMap = baselineTagDataMap
								.get(baselineKey).getTagVariableData();
						testDataTagVariableDataMap = testDataTagDataMap
								.get(baselineKey).getTagVariableData();
						Iterator tagVariableDataMapIterator = baseLineTagVariableDataMap
								.keySet().iterator();
						
						while (tagVariableDataMapIterator.hasNext()) {
							baselineTagVariableKey = tagVariableDataMapIterator.next()
									.toString();
							
							baseLineTagVarName = baselineTagVariableKey;
							baseLineTagVarValue = baseLineTagVariableDataMap
									.get(baselineTagVariableKey);
							testDataTagVarValue = testDataTagVariableDataMap
									.get(baselineTagVariableKey);
							
							if (baseLineTagName.equals("SiteCatalyst")
									&& baseLineTagVarName.equals("pageName")) {
								if (baseLineTagVarValue.startsWith(":")
										|| baseLineTagVarValue.endsWith(":")
										|| baseLineTagVarValue.equals("null")
										|| baseLineTagVarValue.equals("undefined")
										|| baseLineTagVarValue.equals(null)
										|| baseLineTagVarValue
												.equals("NoTagVarValueFound")
										|| baseLineTagVarValue.equals("")) {
									testResult = "Failed";
								} else {
									testResult = (baseLineTagVarValue
											.equalsIgnoreCase(testDataTagVarValue)) ? "Passed"
											: "Failed";
								}

							} // validate pageName

							else if ((baseLineTagVarValue.equals("undefined")
									|| baseLineTagVarValue.equals("null") ||
							// baseLineTagVarValue.equals("NoTagVarValueFound") ||
							baseLineTagVarValue.equals(""))) {
								testResult = "Failed";
							} // other sitecatalyst variables

							else {
								testResult = (baseLineTagVarValue
										.equalsIgnoreCase(testDataTagVarValue)) ? "Passed"
										: "Failed";
							}
							
							if(tagName.equals(baseLineTagName)){
								if(isError.equals("Failed") && testResult.equals(isError)){
									pairEntry = new Pair(baseLineTagVarName, baseLineTagVarValue, testDataTagVarValue);
									siteCatalystPair.add(pairEntry);
									flag = true;
								}else if(isError.equals("Passed")){
									pairEntry = new Pair(baseLineTagVarName, baseLineTagVarValue, testDataTagVarValue);
									siteCatalystPair.add(pairEntry);
									flag = true;
								}
							}
							
						}
						if(flag){
							pairEntry = new Pair("**", "**", "**");
							siteCatalystPair.add(pairEntry);	
						}
						
					}
				}
			}
			
		}catch(Exception e){
			logger.error("Exception in getSiteCatalystTagDataMapValues");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}

		logger.debug("Exit: getSiteCatalystTagDataMapValues");
		return siteCatalystPair;
	}
}

