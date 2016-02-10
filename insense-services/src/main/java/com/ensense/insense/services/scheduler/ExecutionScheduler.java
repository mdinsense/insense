package com.ensense.insense.services.scheduler;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cts.mint.common.JsonReaderWriter;
import com.cts.mint.common.entity.Users;
import com.cts.mint.common.model.Link;
import com.cts.mint.common.model.PartialText;
import com.cts.mint.common.service.HomeService;
import com.cts.mint.common.service.UserService;
import com.cts.mint.common.utils.BrowserEnum;
import com.cts.mint.common.utils.CommonUtils;
import com.cts.mint.common.utils.Constants;
import com.cts.mint.common.utils.Constants.FileSuitSchedulerJob;
import com.cts.mint.common.utils.Constants.KEYS;
import com.cts.mint.common.utils.Constants.SCHEDULER;
import com.cts.mint.common.utils.ExecutionStatus;
import com.cts.mint.common.utils.PropertyFileReader;
import com.cts.mint.crawler.model.CrawlConfig;
import com.cts.mint.reports.entity.LinkDetails;
import com.cts.mint.reports.service.ScheduledService;
import com.cts.mint.reports.service.TestResultsService;
import com.cts.mint.reports.service.TestScheduleService;
import com.cts.mint.uitesting.entity.ApplicationModuleXref;
import com.cts.mint.uitesting.entity.ExcludeUrl;
import com.cts.mint.uitesting.entity.IncludeUrl;
import com.cts.mint.uitesting.entity.ScheduleExecution;
import com.cts.mint.uitesting.entity.ScheduleScriptXref;
import com.cts.mint.uitesting.entity.Suit;
import com.cts.mint.uitesting.entity.SuitTextImageXref;
import com.cts.mint.uitesting.model.ScheduleDetails;
import com.cts.mint.uitesting.model.ScheduleExecutionDetail;
import com.cts.mint.uitesting.model.UiTestingConstants;
import com.cts.mint.uitesting.service.ApplicationModuleService;
import com.cts.mint.uitesting.service.ExcludeURLService;
import com.cts.mint.uitesting.service.IncludeURLService;
import com.cts.mint.util.EmailUtil;
import com.cts.mint.util.FileDirectoryUtil;

@Service
public class ExecutionScheduler {
	private static Logger logger = Logger
			.getLogger(ExecutionScheduler.class);
	@Autowired
	private MessageSource schedulerProperties;
	
	@Autowired
	ScheduledService scheduledService;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private MessageSource configProperties;
	
	@Autowired
	private TestScheduleService testScheduleService;
	
	@Autowired
	private TestResultsService testResultsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private IncludeURLService includeURLService;
	
	@Autowired
	private ExcludeURLService excludeURLService;
	
	@Autowired
	private HomeService homeService;
	
	@Autowired
	private ApplicationModuleService applicationModuleService;
	
	@Scheduled(fixedDelayString = "${mint.scheduler.uischedule.execution.delaytime}")
	public void executePendingOndemandTests(){
		try {
			if (! Boolean.parseBoolean(CommonUtils.getPropertyFromPropertyFile(
					schedulerProperties, SCHEDULER.UI_EXECUTION_SCHEDULER_ENABLE))) {
				
				//Schedule Job is disabled. Do nothing.
				return;
			}
		} catch (Exception e) {
			logger.error("Unable to start the UI Execution Scheduler.");
			e.printStackTrace();
		}

		ScheduleExecutionDetail scheduleExecutionDetail = scheduledService.getPendingOnDemandWebApplicationSchedule();
		
		if ( null != scheduleExecutionDetail && scheduleExecutionDetail.getSuitId() > 0 ) {
			logger.info("Executing the schedule  :"+scheduleExecutionDetail);
			logger.info("Processing Ondemand testcases");
			
			this.sendNotification(scheduleExecutionDetail, ExecutionStatus.IN_PROGRESS.getStatus(), new ScheduleDetails());
			boolean status = processTestCases(scheduleExecutionDetail);
			
			logger.info("Completed ondemand execution for the application.");
		} 
	}

	private void sendNotification(
			ScheduleExecutionDetail scheduleExecutionDetail, String status, ScheduleDetails scheduleDetails) {
		String emailMessage = KEYS.BLANK;
		String sender = KEYS.BLANK;
		String subject = KEYS.BLANK;
		String applicationName = KEYS.BLANK;
		String smtpHost = KEYS.BLANK;
		String envrionmentName = KEYS.BLANK;
		String recepient = KEYS.BLANK;
		String suitName = KEYS.BLANK;
		Suit suit = null;

		try {
			recepient = scheduleExecutionDetail.getSchedule().getEmailIds();
			
			if ( null != recepient && recepient.trim().length() > 0 ){
				String emailSMTPHost = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.email.smtp.host");
				String emailFrom = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.email.sender");
				
				EmailUtil emailUtil = new EmailUtil(emailSMTPHost, emailFrom);

				List<String> recepientList = CommonUtils.getStringAsList(recepient);
				
				if ( status.equals(ExecutionStatus.IN_PROGRESS.getStatus())){
					emailMessage = CommonUtils.getPropertyFromPropertyFile(
							configProperties,
							SCHEDULER.SCHEDULER_STARTED_NOTIFICATION_MSG);
				}else if ( status.equals(ExecutionStatus.COMPLETE.getStatus()) ){
					emailMessage = CommonUtils.getPropertyFromPropertyFile(
							configProperties,
							SCHEDULER.SCHEDULER_COMPLETE_NOTIFICATION_MSG);
				}else if (status.equals(ExecutionStatus.FAILED.getStatus()) ){
					emailMessage = CommonUtils.getPropertyFromPropertyFile(
							configProperties,
							SCHEDULER.SCHEDULER_FAILED_NOTIFICATION_MSG);
				}
				
				sender = CommonUtils.getPropertyFromPropertyFile(configProperties,
						FileSuitSchedulerJob.CONFIGKEY_SENDER);
				subject = CommonUtils.getPropertyFromPropertyFile(configProperties,
						FileSuitSchedulerJob.CONFIGKEY_SUBJECT);
				subject = subject + status;
				smtpHost = CommonUtils.getPropertyFromPropertyFile(
						configProperties, FileSuitSchedulerJob.CONFIGKEY_HOST);
				suit = homeService.getSavedSuits(scheduleExecutionDetail
						.getSuitId());
				applicationName = scheduleExecutionDetail
						.getAppEnvEnvironmentCategoryXref().getApplication()
						.getApplicationName();
				envrionmentName = scheduleExecutionDetail
						.getAppEnvEnvironmentCategoryXref().getEnvironment()
						.getEnvironmentName();
				// recepient = suit.getUsers().getEmailId();
	
				suitName = suit.getSuitName();
				emailMessage = emailMessage.replace(
						SCHEDULER.SUIT_NAME_PLACE_HOLDER, suitName);
				emailMessage = emailMessage.replace(
						SCHEDULER.APPLICATION_NAME_PLACE_HOLDER, applicationName);
				emailMessage = emailMessage.replace(
						SCHEDULER.ENVIRONMENT_NAME_PLACE_HOLDER, envrionmentName);
				
				if ( scheduleDetails.isTransactionTesting() ){
					updateScheduleScript(scheduleDetails);
					emailMessage = generateTransactionMessageContent(scheduleDetails);
				}
				emailUtil.sendEmail(recepientList, subject, emailMessage);
			}else{
				logger.error("Email Id not found, Unable to send email notification.");
			}
		} catch (Exception e) {
			logger.error("Unable to sent notification email.");
			e.printStackTrace();
		}
	}

	private boolean updateScheduleScript(ScheduleDetails scheduleDetails){
		if(scheduleDetails != null){
			return homeService.updateScheduleScript(scheduleDetails.getScheduleScriptList());
		}
		
		return true;
	}
	private String generateTransactionMessageContent(ScheduleDetails scheduleDetails) {
		String emailContent = "";
		emailContent = emailContent + "<table colspan='1' colspace='1'><tr><td scope='row' align='right'>Application Name</td><td>Environment Name</td><td>Module Name</td><td>TestCase name</td><td>Execution Status</td><td>Error details</td></tr>";
		if(scheduleDetails != null){
			ApplicationModuleXref applicationModuleXref = new ApplicationModuleXref();
			for ( ScheduleScriptXref scheduleScript : scheduleDetails.getScheduleScriptList() ) {
				logger.info("scheduleScript :"+scheduleScript);
				emailContent = emailContent + "<tr><td scope='row' align='center'>" + scheduleDetails.getApplicationName() + "</td>";
				emailContent = emailContent + "<td scope='row' align='center'>"+ scheduleDetails.getEnvironmentName() + "</td>";
				
				
				applicationModuleXref.setApplicationModuleXrefId(scheduleScript.getScheduleScript().getTransactionTestCase().getApplicationModuleXrefId());
				applicationModuleXref = applicationModuleService.getApplicationModule(applicationModuleXref);
				
				emailContent = emailContent + "<td scope='row' align='center'>"+ applicationModuleXref.getModuleName() + "</td>";
				emailContent = emailContent + "<td scope='row' align='center'>"+ scheduleScript.getScheduleScript().getTransactionTestCase().getTransactionName() + "</td>";
				String executionStatus = "Failed";
				if ( scheduleScript.getScheduleScript().getExecutionStatus() == ExecutionStatus.COMPLETE.getStatusCode()){
					executionStatus = "Passed";
				}
				emailContent = emailContent + "<td scope='row' align='center'>"+ executionStatus + "</td>";
				String errorLog = "N/A";
				
				if ( StringUtils.isNotBlank(scheduleScript.getScheduleScript().getExecutionLog())){
					errorLog = scheduleScript.getScheduleScript().getExecutionLog();
				}
				emailContent = emailContent + "<td scope='row' align='right'>"+errorLog+ "</td></tr>";


			}
		
		}
		emailContent = emailContent + "</table>";
		return emailContent;
	}
	
	private boolean processTestCases(
			ScheduleExecutionDetail scheduleExecutionDetail) {
		ScheduleDetails testExecutionStatus = new ScheduleDetails();
		ScheduleDetails appConfig = new ScheduleDetails();
		WebCrawler webCrawler = new WebCrawler(messageSource, configProperties);
		boolean runStatus = true;
		
		try {
			CrawlConfig baseLineCrawlConfig = null;
			appConfig = getAppConfig(scheduleExecutionDetail);
			
			if(appConfig.getBaselineScheduleId() > 0){
				baseLineCrawlConfig  = new CrawlConfig();
			ScheduleExecution baselineScheduleExcecution = scheduledService
					.getScheduleExecution(appConfig.getBaselineScheduleId());
			new CrawlConfig();
			JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();
			baseLineCrawlConfig = jsonReaderWriter.readJsonObjectFromFile(
					new CrawlConfig(),
					baselineScheduleExcecution
							.getCrawlStatusDirectory()
							+ File.separator
							+ UiTestingConstants.CRAWL_CONFIG);
			
			}
			appConfig.setBaseLineCrawlConfig(baseLineCrawlConfig);
			testExecutionStatus = webCrawler.executeUiTesting(appConfig, testScheduleService);
			
			List<Link> navigationList = testExecutionStatus.getNavigationList();

			if (testExecutionStatus.getError() != null
					&& testExecutionStatus.getError().length() > 0) {
				runStatus = false;
			}
			else {
				DateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateString=fromUser.format(new Date());
				Date nowDate=fromUser.parse(dateString);
				
				
				LinkDetails linkDetails = new LinkDetails(); 
				linkDetails.setScheduleId(testExecutionStatus.getTestScheduleId());
				
				linkDetails.setDateCreated(nowDate);
				linkDetails.setDateModified(new Date());
				linkDetails.setTimeModified(new Date());
				linkDetails.setTimeCreated(new Date());
				//End code
				
				try{
					//linkDetails = linkService.saveLinkDetail(linkDetails, testExecutionStatus.getNavigationXmlFileContent());
				}catch(Exception e){
					logger.error("Exception while saving the link detials:"+e);
					e.printStackTrace();
				}
				
				//TODO
				/*
				Schedule  testSchedule = new Schedule();
				testSchedule.setScheduleId(appConfig.getTestScheduleId());
				testSchedule = testScheduleService.getSchedule(testSchedule);
				//testSchedule.setReportFlag(false);
				testScheduleService.updateTestSchedule(testSchedule);
				Results testResult = new Results();
				testResult.setLoginId(testExecutionStatus.getTestLoginId());
				testResult.setExecutionStatusId(testExecutionStatus
						.getExecutionId());
				testResult.setScheduleId(testExecutionStatus
						.getTestScheduleId());
				testResult.setSnapShotPath(testExecutionStatus
						.getSnapShotPath());
				testResult.setHarReportsPath(testExecutionStatus.getHarReportsPath());
				testResult.setTextFilePath(testExecutionStatus.getBrandingReportsPath());
				testResult.setExecutionStatusRefId(3);
				//testResult.setLinksObj((ArrayList<Link>) navigationList);
				testResult.setExecutionTimestamp(appConfig.getExecutionTimeStamp());
				testResultsService.insertTestResults(testResult);
				*/
			}
		
		}catch (Exception e) {
			logger.error("Exception while running the scheudle: "
					+ testExecutionStatus, e);
		}
		
		try{
			boolean result =false;
			//result = scheduledService.setCompletion(testExecutionStatus, runStatus);
			logger.info("Updating Schedule Execution Status.");
			ScheduleExecution scheduleExecution = scheduledService.getScheduleExecution(testExecutionStatus.getExecutionId());
			
			if ( runStatus ){
				if ( null != appConfig && ( appConfig.getRunStaus() == 0 || appConfig.getRunStaus() == ExecutionStatus.COMPLETE.getStatusCode() ) ){
					scheduleExecution.setTestExecutionStatusRefId(ExecutionStatus.COMPLETE.getStatusCode());
				}else{
					scheduleExecution.setTestExecutionStatusRefId(appConfig.getRunStaus());
				}
			}else{
				scheduleExecution.setTestExecutionStatusRefId(ExecutionStatus.FAILED.getStatusCode());
			}
			scheduleExecution.setTestExecutionEndTime(new Date());
			//Update the Schedule Execution status
			if ( !scheduledService.updateScheduleExecution(scheduleExecution, Constants.TEST_EXECUTION_UPDATE) ){
				logger.error("Exception while updating the ScheduelExecution.");
			}
			
			this.sendNotification(scheduleExecutionDetail, ExecutionStatus.getExecutionStatus(scheduleExecution.getTestExecutionStatusRefId()).getStatus(), testExecutionStatus);
		}catch(Exception e){
			logger.error("Exception while updating test_results_table with completetion details: "
					+ testExecutionStatus, e);
			runStatus = false;
		}
		
		return runStatus;
	}

	private ScheduleDetails getAppConfig(
			ScheduleExecutionDetail scheduleExecutionDetail) {
		ScheduleDetails scheduleDetails = new ScheduleDetails();
		//TODO: includeUrl, crawlUrl, removeUrlPattern, JqueryFilePath
		
		//scheduleDetails.setAnalyticsTesting(analyticsTesting);
		scheduleDetails.setApplicationId(scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getApplication().getApplicationId());
		scheduleDetails.setApplicationName(scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getApplication().getApplicationName());
		
		if ( null != scheduleExecutionDetail.getSchedule().getBaselineScheduleExecutionId() )
			scheduleDetails.setBaselineScheduleId(scheduleExecutionDetail.getSchedule().getBaselineScheduleExecutionId());
		
		//String browserType = scheduleExecutionDetail.getSchedule().getBrowserTypeId();
		scheduleDetails.setBrowserType(getBrowserType(scheduleExecutionDetail.getSchedule().getBrowserTypeId()));
		//scheduleDetails.setDataCenter(dataCenter);
		//scheduleDetails.setDirectory(directory);
		scheduleDetails.setEmailIds(scheduleExecutionDetail.getSchedule().getEmailIds());

		scheduleDetails.setEnvironmentId(scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getEnvironment().getEnvironmentId());
		scheduleDetails.setEnvironmentName(scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getEnvironment().getEnvironmentName());
		scheduleDetails.setUrl(scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getEnvironment().getLoginOrHomeUrl());
		scheduleDetails.setPublicSite(!scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getEnvironment().getSecureSite());

		scheduleDetails.setExecutionId(scheduleExecutionDetail.getScheduleExecution().getScheduleExecutionId());
		
		//These are needed for restart
		logger.info("scheduleExecutionDetail.getScheduleExecution().getHarReportsDirectory() :"+scheduleExecutionDetail.getScheduleExecution().getHarReportsDirectory());
		scheduleDetails.setHarReportsPath(scheduleExecutionDetail.getScheduleExecution().getHarReportsDirectory());
		//scheduleDetails.setExecutionTimeStamp(scheduleExecutionDetail.getScheduleExecution().getTestExecutionStartTime().toString());
		//scheduleDetails.setHarReportsPath(harReportsPath);
		scheduleDetails.setHtmlCompare(scheduleExecutionDetail.getSchedule().isHtmlCompare());
		scheduleDetails.setRegressionTesting(scheduleExecutionDetail.getSchedule().isRegressionTesting());
		
		scheduleDetails.setRobotClicking(scheduleExecutionDetail.getSchedule().isRobotClicking());
		scheduleDetails.setAnalyticsTesting(scheduleExecutionDetail.getSchedule().isAnalyticsTesting());
		scheduleDetails.setBrokenUrlReport(scheduleExecutionDetail.getSchedule().isBrokenUrlReport());
		
		if ( scheduleExecutionDetail.getSchedule().getUserId() > 0 ) {
			Users user = userService.getUser(scheduleExecutionDetail.getSchedule().getUserId());
			scheduleDetails.setUserId(user.getUserName());
		}else{
			scheduleDetails.setUserId("");
		}
		
		if ( scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getEnvironment().getSecureSite() ){
			scheduleDetails.setLoginId(scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getLoginUserDetailsList().get(0).getLoginId());
			scheduleDetails.setPassword(scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getLoginUserDetailsList().get(0).getPassword());
			scheduleDetails.setSecurityAnswer(scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getLoginUserDetailsList().get(0).getSecurityAnswer());
			scheduleDetails.setRsaEnabled(scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getLoginUserDetailsList().get(0).isRsaEnabled());
		}
		
		scheduleDetails.setNoOfThreads(scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getApplicationConfig().getNoOfBrowsers());
		scheduleDetails.setMaxWaitTime(scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getApplicationConfig().getBrowserTimeout());
		scheduleDetails.setBrowserRestartCount(scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getApplicationConfig().getBrowserRestart());
		
		scheduleDetails.setMobProxyHost(PropertyFileReader.getProperty(configProperties, "mint.mobProxyHost"));
		scheduleDetails.setMobProxyPort(PropertyFileReader.getProperty(configProperties, "mint.mobProxyPort"));

		scheduleDetails.setTextImageDirectory(CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.crawler.imageTextDirectory"));
		
		scheduleDetails.setScreenCompare(scheduleExecutionDetail.getSchedule().isScreenCompare());
		
		scheduleDetails.setStopAfter(scheduleExecutionDetail.getSchedule().getStopAfter());
		scheduleDetails.setUrlLevel(scheduleExecutionDetail.getSchedule().getUrlLevel());
		//scheduleDetails.setTestCaseId(testCaseId);
		scheduleDetails.setTestExecutionStatusRefId(scheduleExecutionDetail.getScheduleExecution().getTestExecutionStatusRefId());
		
		scheduleDetails.setTiaaImageNameList(getTiaaImageNameList(scheduleExecutionDetail));
		scheduleDetails.setTextSearchList(getTextSearchList(scheduleExecutionDetail));
		
		if ( null != scheduleExecutionDetail.getScheduleExecution().getCrawlStatusDirectory() && scheduleExecutionDetail.getScheduleExecution().getCrawlStatusDirectory().trim().length() > 0 ){
			scheduleDetails.setCrawlStatusDirectory(scheduleExecutionDetail.getScheduleExecution().getCrawlStatusDirectory());
		}

		scheduleDetails.setTestScheduleId(scheduleExecutionDetail.getSchedule().getScheduleId());
		scheduleDetails.setTextCompare(scheduleExecutionDetail.getSchedule().isTextCompare());
		
		if ( scheduleDetails.isTextCompare()){
			List<PartialText> partialTextList = scheduledService.getPartialTextCompareList(scheduleDetails.getApplicationId(), scheduleDetails.getEnvironmentId());
			logger.info("partialTextList :"+partialTextList);
			scheduleDetails.setPartialTextList(partialTextList);
		}
		if(scheduleExecutionDetail.getSchedule() != null && scheduleExecutionDetail.getSchedule().isTransactionTestcase()) {
			scheduleDetails.setScheduleScriptList(scheduleExecutionDetail.getScheduleExecution().getScheduleScriptXrefList());
			scheduleDetails.setTransactionTesting(true);
		}
		scheduleDetails.setNetExportPath(getNetExportPath());
		scheduleDetails.setFireBugPath(getFireBugPath());
		// html report config list for removetag and split content
		scheduleDetails.setHtmlReportsConfigList(scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getHtmlReportsConfigList());
		List<ApplicationModuleXref> applicationModuleUrlList = scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getApplicationModuleXrefList();
		logger.info("applicationModuleUrlList :"+applicationModuleUrlList);
		
		if ( null != applicationModuleUrlList && applicationModuleUrlList.size() > 0 ){
			
			if ( isModuleHavingStaticUrl(applicationModuleUrlList, scheduleExecutionDetail.getSchedule().getModuleIds()) ){
				scheduleDetails.setStaticUrlTesting(true);
				scheduleDetails.setStaticUrlList((ArrayList<String>)getStaticUrlList(applicationModuleUrlList, scheduleExecutionDetail.getSchedule().getModuleIds()));
			}else{
				scheduleDetails.setIncludeModuleUrlPatternList(getIncludeModuleUrlForModuleAsList(applicationModuleUrlList, scheduleExecutionDetail.getSchedule().getModuleIds()));
				scheduleDetails.setTestModuleUrlPatternList(getTestModuleUrlForModuleAsList(applicationModuleUrlList, scheduleExecutionDetail.getSchedule().getModuleIds()));
			}
		}
		logger.info("scheduleExecutionDetail.getSchedule().getModuleIds() :"+scheduleExecutionDetail.getSchedule().getModuleIds());
		
		if ( ! isHavingModule(scheduleExecutionDetail.getSchedule().getModuleIds()) ){
			logger.info("Suit doesn't have module, taking Include URL from Application/Environment");
			scheduleDetails.setIncludeUrl(getIncludeUrlAsList(scheduleExecutionDetail));
			logger.info("Include URL pattern :"+scheduleDetails.getIncludeUrl());
		}
		scheduleDetails.setExcludeUrl(getExcludeUrlAsList(scheduleExecutionDetail));
		
		Suit suit = homeService.getSavedSuits(scheduleExecutionDetail
				.getSuitId());
		scheduleDetails.setScrollPage(suit.isScrollPage());
		scheduleDetails.setWaitTime(suit.getWaitTime());
		return scheduleDetails;
	}

	private List<String> getTextSearchList(
			ScheduleExecutionDetail scheduleExecutionDetail) {
		List<String> textList = new ArrayList<String>();
		for ( SuitTextImageXref suitTextImage : scheduleExecutionDetail.getSuitTextImageList()){
			
			if ( suitTextImage.isText() ){
				textList.add(suitTextImage.getTextOrImageName());
			}
		}
		return textList;
	}

	private List<String> getTiaaImageNameList(
			ScheduleExecutionDetail scheduleExecutionDetail) {
		List<String> imageList = new ArrayList<String>();
		for ( SuitTextImageXref suitTextImage : scheduleExecutionDetail.getSuitTextImageList()){
			
			if ( suitTextImage.isImage() ){
				imageList.add(suitTextImage.getTextOrImageName());
			}
		}
		return imageList;
	}

	private boolean isHavingModule(String moduleIds) {
		List<String> moduleIdList = new ArrayList<String>();
		moduleIdList = getCommaSeperatedStringAsArrayList(moduleIds);
		
		try{
			if ( null != moduleIdList && moduleIdList.size() > 0 ){
				for(String moduleId : moduleIdList){
					if ( Integer.parseInt(moduleId) > 0 ){
						return true;
					}
				}
			}
		}catch(Exception e){
			logger.error("Exception while reading Module IDs"+e);
		}
		return false;
	}

	private List<String> getStaticUrlList(
		List<ApplicationModuleXref> applicationModuleUrlList,
		String moduleIds) {
		List<String> moduleIdList = new ArrayList<String>();
		List<String> staticUrlList = new ArrayList<String>();
		
		moduleIdList = getCommaSeperatedStringAsArrayList(moduleIds);
		
		if ( null != moduleIdList && moduleIdList.size() > 0 ){
			for( ApplicationModuleXref applicationModule :applicationModuleUrlList ){
				for(String moduleId : moduleIdList){
					if ( moduleId.equals(new Integer(applicationModule.getApplicationModuleXrefId()).toString())){
						//staticUrlList.addAll(applicationModule.getStaticUrls())
						List<String> staticUrls = applicationModule.getStaticUrlJsonData();
						staticUrlList.addAll(staticUrls);
					}
				}
			}
		}
		
		return staticUrlList;
	}

	private List<String> getCommaSeperatedStringAsArrayList(String commaSeperatedString) {
		List<String> stringAsList = new ArrayList<String>();
		if ( null != commaSeperatedString && commaSeperatedString.length() > 0 ){
			
			try{
				String[] stringArray = commaSeperatedString.split(",");
				stringAsList = Arrays.asList(stringArray);
			}catch(Exception e){
				
			}
		}
		return stringAsList;
	}

	private boolean isModuleHavingStaticUrl(
			List<ApplicationModuleXref> applicationModuleUrlList, String moduleIds) {
		
		List<String> moduleIdList = new ArrayList<String>();
		moduleIdList = getCommaSeperatedStringAsArrayList(moduleIds);
		
		for( ApplicationModuleXref applicationModule :applicationModuleUrlList ){
			
			for(String moduleId : moduleIdList){
				if ( moduleId.equals(new Integer(applicationModule.getApplicationModuleXrefId()).toString())){
					if ( applicationModule.getStaticUrlCount() > 0 ){
						return true;
					}
				}
			}
		}
		return false;
	}

	private List<String> getIncludeModuleUrlForModuleAsList(List<ApplicationModuleXref> applicationModuleUrlList, String moduleIds) {
		logger.debug("Extry: getIncludeModuleUrlForModuleAsList");
		List<String> includeModuleUrlList = new ArrayList<String>();
		List<String> moduleIdList = new ArrayList<String>();
		
		moduleIdList = getCommaSeperatedStringAsArrayList(moduleIds);
		
		if ( null != moduleIdList && moduleIdList.size() > 0 ){
			for( ApplicationModuleXref applicationModule :applicationModuleUrlList ){
				for(String moduleId : moduleIdList){
					if ( moduleId.equals(new Integer(applicationModule.getApplicationModuleXrefId()).toString())){
						try{
							String includeUrlPattern = applicationModule.getIncludeUrlPattern();
							String[] includeUrlPatternArray = includeUrlPattern.split(",");
							
							includeModuleUrlList.addAll(Arrays.asList(includeUrlPatternArray));
						}catch(Exception e){
							
						}
					}
				}
				
			}
		}
		logger.debug("Extry: getIncludeModuleUrlForModuleAsList, includeModuleUrlList->"+includeModuleUrlList);
		return includeModuleUrlList;
	}

	private List<String> getTestModuleUrlForModuleAsList(List<ApplicationModuleXref> applicationModuleUrlList, String moduleIds) {
		logger.debug("Extry: getTestModuleUrlForModuleAsList");
		List<String> testModuleUrlList = new ArrayList<String>();
		List<String> moduleIdList = new ArrayList<String>();
		moduleIdList = getCommaSeperatedStringAsArrayList(moduleIds);
		
		if ( null != moduleIdList && moduleIdList.size() > 0 ){
			for( ApplicationModuleXref applicationModule :applicationModuleUrlList ){
				for(String moduleId : moduleIdList){
					if ( moduleId.equals(new Integer(applicationModule.getApplicationModuleXrefId()).toString())){
						try{
							String testUrlPattern = applicationModule.getTestUrlPattern();
							String[] testUrlPatternArray = testUrlPattern.split(",");
							
							testModuleUrlList.addAll(Arrays.asList(testUrlPatternArray));
						}catch(Exception e){
							
						}
					}
				}
				
			}
		}
		logger.debug("Extry: getTestModuleUrlForModuleAsList, testModuleUrlList->"+testModuleUrlList);
		return testModuleUrlList;
	}
	
	private List<String> getExcludeUrlAsList(
			ScheduleExecutionDetail scheduleExecutionDetail) {
		logger.debug("Extry: getExcludeUrlAsList");
		List<ExcludeUrl> excludeUrl = excludeURLService.getExcludeURLList(scheduleExecutionDetail.getSchedule().getApplicationId(), scheduleExecutionDetail.getSchedule().getEnvironmentId());
		List<String> excludeUrlList = new ArrayList<String>();
		if ( null != excludeUrl && excludeUrl.size() > 0 ){
			for ( ExcludeUrl excludeUrlElement : excludeUrl ){
				
				if ( null != excludeUrlElement && excludeUrlElement.getExcludeUrl().trim().length() > 0 ){
					
					try{
						String[] elements = excludeUrlElement.getExcludeUrl().split(",");
						
						for ( String str : elements){
							excludeUrlList.add(str.trim());
						}
					}catch(Exception e){
						logger.error("Exception while parsing Exclude Url");
						logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
					}
				}
			}
		}
		logger.debug("Exit: getExcludeUrlAsList, excludeUrlList->"+excludeUrlList);
		return excludeUrlList;
	}

	private  List<String> getIncludeUrlAsList(ScheduleExecutionDetail scheduleExecutionDetail) {
		logger.debug("Extry: getIncludeUrlAsList");
		List<IncludeUrl> includeUrl = includeURLService.getIncludeURLList(scheduleExecutionDetail.getSchedule().getApplicationId(), scheduleExecutionDetail.getSchedule().getEnvironmentId());
		List<String> includeUrlList = new ArrayList<String>();
		
		if ( null != includeUrl && includeUrl.size() > 0 ){
			for ( IncludeUrl includeUrlElement : includeUrl ){
				if ( null != includeUrlElement && includeUrlElement.getIncludeUrl().trim().length() > 0 ){
					try{
						String[] elements = includeUrlElement.getIncludeUrl().split(",");
						
						for ( String str : elements){
							includeUrlList.add(str.trim());
						}
					}catch(Exception e){
						logger.error("Exception while parsing Include Url");
						logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
					}
				}
			}
		}
		
		logger.debug("Exit: getIncludeUrlAsList, excludeUrlList->"+includeUrlList);
		return includeUrlList;
	}

	private String getBrowserType(Integer browserTypeId) {
		String browserTypeName = "";

		for ( BrowserEnum benum : BrowserEnum.values() ){
			browserTypeName = benum.getBrowserForTypeId(browserTypeId).getBrowserTypeName().toLowerCase();
			break;
		}
		return browserTypeName;
	}

	private String getFireBugPath(){
		String firebug = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.crawler.firebug");
		firebug = FileDirectoryUtil.getAbsolutePath(firebug,
				FileDirectoryUtil.getMintRootDirectory(configProperties));
		return firebug;
	}
	
	private String getNetExportPath(){
		String netExport = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.crawler.netExport");
		netExport = FileDirectoryUtil.getAbsolutePath(netExport,
				FileDirectoryUtil.getMintRootDirectory(configProperties));
		return netExport;
	}
}
