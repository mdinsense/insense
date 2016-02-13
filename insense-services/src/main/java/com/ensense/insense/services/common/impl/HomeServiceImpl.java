package com.ensense.insense.services.common.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.analytics.model.AnalyticSummaryDetails;
import com.cts.mint.analytics.model.AnalyticsDetails;
import com.cts.mint.analytics.model.AnalyticsSummaryReport;
import com.cts.mint.analytics.model.AnalyticsSummaryReportUi;
import com.cts.mint.analytics.model.AnalyticsTagDetail;
import com.cts.mint.common.JsonReaderWriter;
import com.cts.mint.common.dao.HomeDAO;
import com.cts.mint.common.dao.UserDAO;
import com.cts.mint.common.entity.BrowserType;
import com.cts.mint.common.entity.FeedBack;
import com.cts.mint.common.entity.MintProperties;
import com.cts.mint.common.entity.SolutionType;
import com.cts.mint.common.entity.SuitGroupXref;
import com.cts.mint.common.entity.UsageReport;
import com.cts.mint.common.entity.Users;
import com.cts.mint.common.model.ConfigProperty;
import com.cts.mint.common.model.IdNamePair;
import com.cts.mint.common.model.ReportStatus;
import com.cts.mint.common.model.ScheduleStatus;
import com.cts.mint.common.model.UsageReportForm;
import com.cts.mint.common.model.UsageReportResult;
import com.cts.mint.common.service.HomeService;
import com.cts.mint.common.utils.Constants;
import com.cts.mint.common.utils.Constants.EnvironmentCategoryENUM;
import com.cts.mint.common.utils.Constants.KEYS;
import com.cts.mint.common.utils.Constants.SCHEDULER;
import com.cts.mint.common.utils.Constants.UsageReportConstants;
import com.cts.mint.common.utils.ExecutionStatus;
import com.cts.mint.common.utils.UIFunctionalityType;
import com.cts.mint.crawler.model.CompareConfig;
import com.cts.mint.crawler.model.CrawlConfig;
import com.cts.mint.crawler.model.ReportsConfig;
import com.cts.mint.reports.service.ScheduledService;
import com.cts.mint.uitesting.dao.ApplicationDAO;
import com.cts.mint.uitesting.dao.UiTestingDAO;
import com.cts.mint.uitesting.entity.Application;
import com.cts.mint.uitesting.entity.Schedule;
import com.cts.mint.uitesting.entity.ScheduleExecution;
import com.cts.mint.uitesting.entity.ScheduleScript;
import com.cts.mint.uitesting.entity.ScheduleScriptXref;
import com.cts.mint.uitesting.entity.Suit;
import com.cts.mint.uitesting.entity.SuitBrokenReportsXref;
import com.cts.mint.uitesting.entity.SuitTextImageXref;
import com.cts.mint.uitesting.model.CompareLink;
import com.cts.mint.uitesting.model.ScheduleExecutionDetail;
import com.cts.mint.uitesting.model.UiTestingConstants;
import com.cts.mint.util.DateTimeUtil;
import com.cts.mint.util.DateUtil;
import com.cts.mint.webservice.dao.WebserviceTestingDAO;
import com.cts.mint.webservice.entity.WebserviceSuite;
import com.cts.mint.webservice.model.WebserviceSetupForm;
//import com.cts.mint.common.utils.Constants.Application;

@Service
public class HomeServiceImpl implements HomeService{
	
	private static Logger logger = Logger
			.getLogger(HomeServiceImpl.class);

	private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	@Autowired
	HomeDAO homeDao;
	
	@Autowired
	UserDAO userDao;
	
	@Autowired
	ScheduledService scheduledServiceDao;
	
	@Autowired
	ApplicationDAO applicationDao;
	
	@Autowired
	WebserviceTestingDAO webserviceTestingDAO;
	
	@Autowired
	UiTestingDAO uiTestingDAO;
	
	@Override
	@Transactional
	public List<Suit> getMintHomeCreatedByGroup(String userid,
			int groupid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public List<Suit> getMintHomeCreatedByAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public List<Suit> getUiRegressionSuitForGroup(int groupId) {
		return homeDao.getUiRegressionSuitForGroup(groupId);
	}

	@Override
	@Transactional
	public List<Suit> getUiRegressionSuitForUser(int userId) {
		return homeDao.getUiRegressionSuitForUser(userId);
	}

	@Override
	@Transactional
	public boolean saveMintConfigProperties(ConfigProperty configProperty) throws Exception {
		logger.debug("Entry : saveMintConfigProperties");
		MintProperties mintProperties = null;
		homeDao.deleteAllMintPropertuies(mintProperties);
		for(int i =0; i<configProperty.getPropertyName().length;i++) {
			mintProperties = new MintProperties();
			mintProperties.setPropertyName(configProperty.getPropertyName()[i]);
			mintProperties.setPropertyDisplayName(configProperty.getPropertyDisplayName()[i]);
			mintProperties.setPropertyValue(configProperty.getPropertyValue()[i]);
			homeDao.saveMintProperty(mintProperties);
		}
		logger.debug("Exit : saveMintConfigProperties");
		return true;
	}

	@Override
	@Transactional
	public List<SolutionType> getAllSolutionTypes() {
		return homeDao.getAllSolutionTypes();
	}

	@Override
	@Transactional
	public List<BrowserType> getBrowserTypes() {
		logger.debug("Entry And Exit : getBrowserTypes");
		return homeDao.getBrowserTypes();
	}
@Override
	@Transactional
	public Suit getSavedSuits(int suitId) {
		return homeDao.getSavedSuits(suitId);
	}

	@Override
	@Transactional
	public List<Suit> getSavedSuitsCreatedByAll() {
		return homeDao.getSavedSuitsCreatedByAll();
	}

	@Override
	@Transactional
	public boolean saveSchedule(Schedule ts) {
		logger.debug("Entry: saveSchedule");
		boolean isSaved = false;
		try {
			if (ts.getScheduleType() == SCHEDULER.BY_WEEKLY && ts.getScheduleDDay() != null) {
				if (ts.getScheduleDDay().contains(KEYS.COMMA)) {
					String[] weeks = ts.getScheduleDDay().split(KEYS.COMMA);
					for (String week : weeks) {
						Schedule sc = new Schedule();
						sc = (Schedule) ts.clone();
						sc.setScheduleDDay(week);
						homeDao.saveSchedule(sc);
					} 
					isSaved = true;
				} else {
					isSaved = homeDao.saveSchedule(ts);
				}
			} else {
				isSaved = homeDao.saveSchedule(ts);
			}
		} catch (Exception e) {
			logger.error("Exception in saveSchedule");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: saveSchedule");
		return isSaved;
	}

	@Override
	@Transactional
	public boolean removeSuitDetails(Suit suit) {
		return homeDao.removeSuitDetails(suit);
	}
	
	@Override
	@Transactional
	public boolean addFeedBack(FeedBack feedBack){
		return homeDao.addFeedBack(feedBack);
	}

	@Override
	@Transactional
	public List<SuitGroupXref> getManageSuitDetails(int suitId){
		logger.debug("Entry And Exit : getManageSuitDetails");
		return homeDao.getManageSuitDetails(suitId);
	}
	
	@Override
	@Transactional
	public boolean addManageSuitsGroup(String SuitId,
			List<SuitGroupXref> suitGroupXrefList
			){
		logger.debug("Entry: addManageSuitsGroup");
		boolean result = false;
		try {
			
			homeDao.deleteSuitGroupXref(Integer.parseInt(SuitId));
			
			for (SuitGroupXref suitGroup : suitGroupXrefList) {
				homeDao.addSuitGroupXref(suitGroup);
			}
			result = true;
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while Saving addManageSuitsGroup details");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(e));
			return false;
		}
		logger.debug("Exit: addManageSuitsGroup");
		return result;
		
	}
	
	@Override
	@Transactional
	public List<Suit> getManageSuitsForGroup(int groupId){
		logger.debug("Entry And Exit : getManageSuitsForGroup");
		return homeDao.getManageSuitsForGroup(groupId);
	}
	
	@Override
	@Transactional
	public List<Suit> getManageSuitsForGroupUser(int groupId,int userId){
		logger.debug("Entry And Exit : getManageSuitsForGroupUser");
		return homeDao.getManageSuitsForGroupUser(groupId, userId);
	}
	
	@Override
	@Transactional
	public List<Suit> getManageSuitsForAllSuits(){
		logger.debug("Entry And Exit : getManageSuitsForAllSuits");
		return homeDao.getManageSuitsForAllSuits();
	}

	@Override
	@Transactional
	public List<ScheduleStatus> getInProgressScheduleStatus(int suitId) {
		logger.debug("Entry: getInProgressScheduleStatus");
		List<ScheduleStatus> scheduleStatusList = new ArrayList<ScheduleStatus>();
		
		List<ScheduleExecution> scheduleExecutionList = homeDao.getInProgressScheduleDetailsForSuit(suitId);

		CrawlConfig crawlConfig = null;
		ReportsConfig reportsConfig = null;;
		CompareConfig compareConfig = null;
		String lastUpdatedTime = "";
		
		for ( ScheduleExecution scheduleExecution: scheduleExecutionList ){
			ScheduleStatus scheduleStatus = new ScheduleStatus();
			initializeScheduleStatus(scheduleStatus);
			
			scheduleStatus.setScheduleExecutionId(scheduleExecution.getScheduleExecutionId());
			
			try{
				crawlConfig = null;
				JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();
				crawlConfig = jsonReaderWriter.readJsonObjectFromFile(new CrawlConfig(), scheduleExecution.getCrawlStatusDirectory()+ File.separator + UiTestingConstants.CRAWL_CONFIG);
			}catch(Exception e){
				//logger.warn(UiTestingConstants.CRAWL_CONFIG + ", is not available yet for the ScheduleExecutionId :"+scheduleExecution.getScheduleExecutionId());
			}

			try{
				addCrawlConfigDetailsToScheduleStatus(crawlConfig, scheduleStatus);
			}catch(Exception e){
				
			}

			try{
				reportsConfig = null;
				JsonReaderWriter<ReportsConfig> jsonReaderWriter = new JsonReaderWriter<ReportsConfig>();
				reportsConfig = jsonReaderWriter.readJsonObjectFromFile(new ReportsConfig(), scheduleExecution.getCrawlStatusDirectory()+ File.separator + UiTestingConstants.REPORTS_CONFIG);
			}catch(Exception e){
				//logger.warn(UiTestingConstants.REPORTS_CONFIG + ", is not available yet for the ScheduleExecutionId :"+scheduleExecution.getScheduleExecutionId());
			}
			
			try{
				addReportsConfigDetailsToScheduleStatus(reportsConfig, scheduleStatus);
			}catch(Exception e){
				
			}

			try{
				compareConfig = null;
				JsonReaderWriter<CompareConfig> jsonReaderWriter = new JsonReaderWriter<CompareConfig>();
				compareConfig = jsonReaderWriter.readJsonObjectFromFile(new CompareConfig(), scheduleExecution.getCrawlStatusDirectory()+ File.separator + UiTestingConstants.COMPARE_CONFIG);
				
			}catch(Exception e){
				//logger.warn(UiTestingConstants.COMPARE_CONFIG + ", is not available yet for the ScheduleExecutionId :"+scheduleExecution.getScheduleExecutionId());
			}

			try{
				addCompareConfigDetailsToScheduleStatus(compareConfig, scheduleStatus);
			}catch(Exception e){
				
			}
			scheduleStatusList.add(scheduleStatus);
		}
		
		logger.debug("Exit: getInProgressScheduleStatus");
		return scheduleStatusList;
	}
	
	@Override
	@Transactional
	public List<ScheduleStatus> getCompletedScheduleStatus(int suitId, int count) {
		logger.debug("Entry : getScheduleStatus");
		List<ScheduleStatus> scheduleStatusList = new ArrayList<ScheduleStatus>();
		
		List<ScheduleExecutionDetail> scheduleExecutionDetailList = homeDao.getCompletedScheduleDetailsForSuit(suitId);

		CrawlConfig crawlconfig = null;
		ReportsConfig reportsConfig = null;
		CompareConfig compareConfig = null;
		int index = 0;
		for ( ScheduleExecutionDetail scheduleExecutionDetail: scheduleExecutionDetailList ){
			index++;
			ScheduleStatus scheduleStatus = new ScheduleStatus();
			initializeScheduleStatus(scheduleStatus);
			
			if ( scheduleExecutionDetail.getSchedule().isTransactionTestcase() ){
				scheduleStatus.setScheduleScriptList(homeDao.getScheduleScript(scheduleExecutionDetail.getSchedule().getScheduleId()));
				scheduleStatus.setTransactionTesting(true);
			}
			scheduleStatus.setScheduleExecutionId(scheduleExecutionDetail.getScheduleExecution().getScheduleExecutionId());
			scheduleStatus.setExecutionStatus(ExecutionStatus.getStatus(scheduleExecutionDetail.getScheduleExecution().getTestExecutionStatusRefId()));
			scheduleStatus.setScheduleId(scheduleExecutionDetail.getSchedule().getScheduleId());
			try{
				crawlconfig = null;
				JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();
				crawlconfig = jsonReaderWriter.readJsonObjectFromFile(new CrawlConfig(), scheduleExecutionDetail.getScheduleExecution().getCrawlStatusDirectory()+ File.separator + UiTestingConstants.CRAWL_CONFIG);
				addCrawlConfigDetailsToScheduleStatus(crawlconfig, scheduleStatus);
			}catch(Exception e){
				//logger.warn(UiTestingConstants.CRAWL_CONFIG + ", is not available yet for the ScheduleExecutionId :"+scheduleExecution.getScheduleExecutionId());
			}
			try{
				reportsConfig = null;
			
				JsonReaderWriter<ReportsConfig> jsonReaderWriter = new JsonReaderWriter<ReportsConfig>();
				reportsConfig = jsonReaderWriter.readJsonObjectFromFile(new ReportsConfig(), scheduleExecutionDetail.getScheduleExecution().getCrawlStatusDirectory()+ File.separator + UiTestingConstants.REPORTS_CONFIG);
				addReportsConfigDetailsToScheduleStatus(reportsConfig, scheduleStatus);
			}catch(Exception e){
				//logger.warn(UiTestingConstants.REPORTS_CONFIG + ", is not available yet for the ScheduleExecutionId :"+scheduleExecution.getScheduleExecutionId());
			}
			try{
				compareConfig = null;
				JsonReaderWriter<CompareConfig> jsonReaderWriter = new JsonReaderWriter<CompareConfig>();
				compareConfig = jsonReaderWriter.readJsonObjectFromFile(new CompareConfig(), scheduleExecutionDetail.getScheduleExecution().getCrawlStatusDirectory()+ File.separator + UiTestingConstants.COMPARE_CONFIG);
				addCompareConfigDetailsToScheduleStatus(compareConfig, scheduleStatus);
			}catch(Exception e){
				//logger.warn(UiTestingConstants.COMPARE_CONFIG + ", is not available yet for the ScheduleExecutionId :"+scheduleExecution.getScheduleExecutionId());
			}
		
			scheduleStatusList.add(scheduleStatus);
			if ( count > 0 && index >= count ){
				break;
			}
		}
		logger.debug("Exit : getScheduleStatus");
		return scheduleStatusList;
	}
	
	@Override
	@Transactional
	public List<ScheduleStatus> getFutureSuitScheduleStatus(int suitId) {
		logger.debug("Entry And Exit : getFutureSuitScheduleStatus");
		
		List<ScheduleStatus> scheduleStatusList = new ArrayList<ScheduleStatus>();
		List<Schedule> scheduleExecutionList = homeDao.getFutureScheduleDetailsForSuit(suitId);
		ScheduleStatus scheduleStatus = null;
		
		for ( Schedule schedule: scheduleExecutionList ){
			//if(schedule.isRecurrence()) {
				if('W' == schedule.getScheduleType()) {
					String[] weeks = schedule.getScheduleDDay().split(",");
					for(String week: weeks) {
						scheduleStatus = new ScheduleStatus();
						initializeScheduleStatus(scheduleStatus);
						scheduleStatus.setScheduleId(schedule.getScheduleId());
						//scheduleStatus.setScheduleExecutionId(schedule.getScheduleId());
						scheduleStatus.setScheduledBy(userDao.getUser(schedule.getUserId()).getUserName());
						scheduleStatus.setStartTime(DateTimeUtil.convertToString(schedule.getStartTime(), DateTimeUtil.TIME_FORMAT));
						scheduleStatus.setScheduledDay(week);
						scheduleStatus.setScheduledDate("NA");
						scheduleStatus.setReccurrence(schedule.isRecurrence());
						scheduleStatus.setPendingForThisWeek("");
						/*scheduleStatus.setPendingForThisWeek("Pending For the Week");
						if(DateTimeUtil.isBeforeCurrentDay(week)) {
							if(DateTimeUtil.isBeforeCurrentTime(schedule.getStartTime())) {
								scheduleStatus.setPendingForThisWeek("Execution Completed For the week");
							}
						}*/
						scheduleStatusList.add(scheduleStatus);
					}
				}  
				else if('D' == schedule.getScheduleType()) {
					scheduleStatus = new ScheduleStatus();
					initializeScheduleStatus(scheduleStatus);
					scheduleStatus.setScheduleId(schedule.getScheduleId());
					scheduleStatus.setScheduledBy(userDao.getUser(schedule.getUserId()).getUserName());
					scheduleStatus.setStartTime(DateTimeUtil.convertToString(schedule.getStartTime(), DateTimeUtil.TIME_FORMAT));
					//scheduleStatus.setScheduledDate(DateTimeUtil.convertToString(schedule.getStartDate(), DateTimeUtil.DATE_FORMAT));
					scheduleStatus.setScheduledDate(DateTimeUtil.convertToString(schedule.getRecurrenceDateWise(), DateTimeUtil.DATE_FORMAT));
					scheduleStatus.setScheduledDay("NA");
					scheduleStatus.setReccurrence(schedule.isRecurrence());
					scheduleStatus.setPendingForThisWeek("NA");
					scheduleStatusList.add(scheduleStatus);
				}else{
					scheduleStatus = new ScheduleStatus();
					initializeScheduleStatus(scheduleStatus);
					scheduleStatus.setScheduleId(schedule.getScheduleId());
					scheduleStatus.setScheduledBy(userDao.getUser(schedule.getUserId()).getUserName());
					scheduleStatus.setStartTime(DateTimeUtil.convertToString(schedule.getStartTime(), DateTimeUtil.TIME_FORMAT));
					scheduleStatus.setScheduledDate(DateTimeUtil.convertToString(schedule.getStartDate(), DateTimeUtil.DATE_FORMAT));
					scheduleStatus.setScheduledDay("NA");
					scheduleStatus.setReccurrence(schedule.isRecurrence());
					scheduleStatus.setPendingForThisWeek("NA");
					scheduleStatusList.add(scheduleStatus);

				}
			//}
		}

		return scheduleStatusList;
	}

	private void addReportsConfigDetailsToScheduleStatus(
			ReportsConfig reportsConfig, ScheduleStatus scheduleStatus) {
		if ( null != reportsConfig ){
			scheduleStatus.setReportsStartTime(reportsConfig.getReportsStartTime());
			scheduleStatus.setReportsEndTime(reportsConfig.getReportsEndTime());
			scheduleStatus.setReportsProcessedUrlCount(reportsConfig.getReportsProcessedUrlCount());
			scheduleStatus.setReportsPendingUrlCount(reportsConfig.getReportsPendingUrlCount());
		}
	}

	private void initializeScheduleStatus(ScheduleStatus scheduleStatus){
		scheduleStatus.setCrawlStartTime(ExecutionStatus.PENDING.getStatus());
		scheduleStatus.setCrawlEndTime(ExecutionStatus.PENDING.getStatus());
		scheduleStatus.setPendingUrlCount(0);
		scheduleStatus.setProcessedUrlCount(0);
		scheduleStatus.setComparisonProcessedUrlCount(0);
		scheduleStatus.setComparisonPendingUrlCount(0);
		scheduleStatus.setLastUpdatedTime(ExecutionStatus.PENDING.getStatus());
		scheduleStatus.setScheduledBy(ExecutionStatus.PENDING.getStatus());
		scheduleStatus.setCrawlStatus("");
		
		scheduleStatus.setReportsStartTime(ExecutionStatus.PENDING.getStatus());
		scheduleStatus.setReportsEndTime(ExecutionStatus.PENDING.getStatus());
		scheduleStatus.setReportsProcessedUrlCount(0);
		scheduleStatus.setReportsPendingUrlCount(0);
		
		scheduleStatus.setComparisonStartTime(ExecutionStatus.PENDING.getStatus());
		scheduleStatus.setComparisonEndTime(ExecutionStatus.PENDING.getStatus());
		scheduleStatus.setComparisonPendingUrlCount(0);
		scheduleStatus.setComparisonProcessedUrlCount(0);
	}
	
	private void addCompareConfigDetailsToScheduleStatus(
			CompareConfig compareConfig, ScheduleStatus scheduleStatus) {
		if ( null != compareConfig ){
			scheduleStatus.setComparisonStartTime(compareConfig.getCompareStartTime());
			scheduleStatus.setComparisonEndTime(compareConfig.getCompareEndTime());
			
			if ( null == compareConfig.getComparePendingUrlCount() ){
				scheduleStatus.setComparisonPendingUrlCount(0);
			}else{
				scheduleStatus.setComparisonPendingUrlCount(compareConfig.getComparePendingUrlCount());
			}
			if ( null == compareConfig.getCompareProcessedUrlCount() ){
				scheduleStatus.setComparisonProcessedUrlCount(0);
			}else {
				scheduleStatus.setComparisonProcessedUrlCount(compareConfig.getCompareProcessedUrlCount());
			}
			if ( null != compareConfig.getLastUpdatedTime() ){
				scheduleStatus.setLastUpdatedTime(DateTimeUtil.formatDateString(compareConfig.getLastUpdatedTime(), DATE_TIME_FORMAT));
			}
			scheduleStatus.setCompareConfig(compareConfig);
		}
	}

	private void addCrawlConfigDetailsToScheduleStatus(CrawlConfig crawlconfig,
			ScheduleStatus scheduleStatus) {
		try{
			if ( null != crawlconfig ){
				scheduleStatus.setCrawlStartTime(DateTimeUtil.formatDateString(crawlconfig.getStartTime(), DATE_TIME_FORMAT));
				scheduleStatus.setCrawlEndTime(DateTimeUtil.formatDateString(crawlconfig.getEndTime(), DATE_TIME_FORMAT));
				scheduleStatus.setPendingUrlCount(crawlconfig.getCrawlStatus().getQueue().size());
				scheduleStatus.setProcessedUrlCount(crawlconfig.getCrawlStatus().getNavigationList().size());
				scheduleStatus.setLastUpdatedTime(DateTimeUtil.formatDateString(crawlconfig.getLastRecordedTime(), DATE_TIME_FORMAT));
				scheduleStatus.setScheduledBy(crawlconfig.getUserId());
				scheduleStatus.setCrawlStatus(ExecutionStatus.getExecutionStatus(crawlconfig.getCrawlStatus().getRunStatus()).getStatus());
			}
		}catch(Exception e){
			logger.error("Exception while fetching in progress schedule details.");
		}
	}

	@Override
	@Transactional
	public List<ScheduleExecution> getBaselineScheduleExcutionList(int suitId) {
		return homeDao.getBaselineScheduleExcutionList(suitId);
	}
	
	@Override
	@Transactional
	public List<ScheduleExecution> getReportBaselineScheduleExcutionList(int suitId) {
		return homeDao.getReportBaselineScheduleExcutionList(suitId);
	}
	
	@Override
	@Transactional
	public ReportStatus getComparisonReportsList(int scheduleExecutionId) {
		logger.debug("Entry: getComparisonReportsList");
		ReportStatus reportStatus = new ReportStatus();
		
		reportStatus = initializeReportStatus(reportStatus);
		
		ScheduleExecution scheduleExecution = scheduledServiceDao.getScheduleExecution(scheduleExecutionId); 
		CompareConfig compareConfig = null;
		
		if(scheduleExecution != null){
			reportStatus.setScheduleStartDate(scheduleExecution.getTestExecutionStartTime().toString());
			reportStatus.setScheduleExecutionId(scheduleExecutionId);

			if ( null != scheduleExecution.getAnalyticsReportStatusId() && scheduleExecution.getAnalyticsReportStatusId() == 3 ){
				reportStatus.setAnalyticsReportAvailable(true);
			}
			if ( null != scheduleExecution.getBrokenUrlReportStatusId() && scheduleExecution.getBrokenUrlReportStatusId() == 3 ){
				reportStatus.setBrokenUrlReportAvailable(true);
			}
			if ( null != scheduleExecution.getTextOrImageReportStatusId() && scheduleExecution.getTextOrImageReportStatusId() == 3 ){
				reportStatus.setTextOrImageReportAvailable(true);
				reportStatus.setReportsAvailable(true);
			}
			
			if ( scheduleExecution.getTestExecutionStatusRefId() == 3){
				try{
					CrawlConfig crawlConfig = new CrawlConfig();
					JsonReaderWriter<CrawlConfig> jsonReaderWriterCrawlConfig = new JsonReaderWriter<CrawlConfig>();
					crawlConfig = jsonReaderWriterCrawlConfig.readJsonObjectFromFile(new CrawlConfig(), scheduleExecution.getCrawlStatusDirectory()+ File.separator + UiTestingConstants.CRAWL_CONFIG);
					
					reportStatus.setNoOfurls(crawlConfig.getCrawlStatus().getNavigationList().size());
				}catch(Exception e){
					//logger.warn(UiTestingConstants.CRAWL_CONFIG + ", is not available yet for the ScheduleExecutionId :"+scheduleExecution.getScheduleExecutionId());
				}
			}
			
			if ( null != scheduleExecution.getComparisonStatusRefId() && scheduleExecution.getComparisonStatusRefId() == 3){
				reportStatus.setReportsAvailable(true);
				compareConfig = null;
				JsonReaderWriter<CompareConfig> jsonReaderWriter = new JsonReaderWriter<CompareConfig>();
				try {
					compareConfig = jsonReaderWriter.readJsonObjectFromFile(new CompareConfig(), scheduleExecution.getCrawlStatusDirectory()+ File.separator + UiTestingConstants.COMPARE_CONFIG);
				} catch (Exception e) {
					//logger.warn(UiTestingConstants.COMPARE_CONFIG + ", is not available yet for the ScheduleExecutionId :"+scheduleExecution.getScheduleExecutionId());
				}
				if (  null != compareConfig && null != compareConfig.getCompareLinkList() ){
					reportStatus.setTotalComparisonMatched("0");
					reportStatus.setTotalComparisonNotMatched("0");
					reportStatus.setTotalUrlsFailed("0");
					
					for ( CompareLink compareLink : compareConfig.getCompareLinkList() ){
						if ( compareLink.isUrlFoundInBaseline() && compareLink.isUrlFoundInCurrentRun() ){
							
							if ( compareLink.isTextFileMatched() || compareLink.isHtmlMatched() || compareLink.isImageMatched() ){
								reportStatus.setTotalComparisonMatched( new Integer(Integer.parseInt(reportStatus.getTotalComparisonMatched()) + 1).toString());
							}else{
								reportStatus.setTotalComparisonNotMatched(new Integer(Integer.parseInt(reportStatus.getTotalComparisonNotMatched()) + 1).toString());
							}
						}else if ( compareLink.isUrlFoundInBaseline() && !compareLink.isUrlFoundInCurrentRun() ){
							reportStatus.setTotalUrlsFailed(new Integer(Integer.parseInt(reportStatus.getTotalUrlsFailed()) + 1).toString());
						}
					}
				}
			}
		}
		
		logger.debug("Exit: getComparisonReportsList");
		return reportStatus;
	}

	private ReportStatus initializeReportStatus(ReportStatus reportStatus) {
		reportStatus.setAnalyticsReportAvailable(false);
		reportStatus.setBrokenUrlReportAvailable(false);
		reportStatus.setNoOfurls(0);
		reportStatus.setReportsAvailable(false);
		reportStatus.setScheduleStartDate("");
		reportStatus.setTotalComparisonMatched("N/A");
		reportStatus.setTotalComparisonNotMatched("N/A");
		reportStatus.setTotalUrlsFailed("N/A");
		return reportStatus;
	}

	@Override
	public boolean isLoginFromLocal(HttpServletRequest request) {
		if ( null != request && null != request.getRequestURL() &&  request.getRequestURL().toString().contains("/localhost:")){
			return true;
		}
		return false;
	}
		
	@Override
	@Transactional
	public List<AnalyticsSummaryReportUi> getAnalyticsSummaryReportData(
			int scheduleExecutionId) throws Exception {
		logger.debug("Entry : getAnalyticsSummaryReportData");
		AnalyticsSummaryReport analyticsSummaryReport = new AnalyticsSummaryReport();
		List<AnalyticsSummaryReportUi> analyticsSummaryReportUiList = new ArrayList<AnalyticsSummaryReportUi>();
		ScheduleExecution scheduleExecution = scheduledServiceDao
				.getScheduleExecution(scheduleExecutionId);
		String reportsDirectoryPath = scheduleExecution.getReportsDirectory();
		String analyticsSummaryReportFile = reportsDirectoryPath
				+ File.separator + UiTestingConstants.ANALYTICS_SUMMARY_REPORTS;
		JsonReaderWriter<AnalyticsSummaryReport> jsonReaderWriter = new JsonReaderWriter<AnalyticsSummaryReport>();
		analyticsSummaryReport = jsonReaderWriter.readJsonObjectFromFile(
				new AnalyticsSummaryReport(), analyticsSummaryReportFile);
		
		for (Map.Entry<String, AnalyticsTagDetail> entry : analyticsSummaryReport
				.getEachAnalyticsTagMap().entrySet()) {
			String key = entry.getKey();
			AnalyticsTagDetail values = entry.getValue();
			AnalyticsSummaryReportUi analyticsSummaryReportUi = new AnalyticsSummaryReportUi();
			analyticsSummaryReportUi.setTotalUrl(analyticsSummaryReport
					.getTotalUrl());
			analyticsSummaryReportUi.setTagName(key);
			analyticsSummaryReportUi.setTagPresentUrlCount(values
					.getTagPresentUrl().size());
			analyticsSummaryReportUi.setTagNotPresentUrlCount(values
					.getTagNotPresentUrl().size());
			analyticsSummaryReportUi.setTagHasErrorUrlCount(values
					.getUrlHasErrorTag().size());
			analyticsSummaryReportUiList.add(analyticsSummaryReportUi);
		}
		
		logger.debug("Exit : getAnalyticsSummaryReportData");
		return analyticsSummaryReportUiList;
	}

	@Override
	@Transactional
	public List<AnalyticsDetails> getAnalyticsDetailsReportData(
			int scheduleExecutionId, String detailOrErrorFormate)  throws Exception {
		logger.debug("Entry : getAnalyticsDetailsReportData");
		String analyticsDetailsReportFile =new String();
		AnalyticSummaryDetails analyticsSummaryDetails = new AnalyticSummaryDetails();
		List<AnalyticsDetails> analyticsDetailsList = new ArrayList<AnalyticsDetails>();
		ScheduleExecution scheduleExecution = scheduledServiceDao
				.getScheduleExecution(scheduleExecutionId);
		String reportsDirectoryPath = scheduleExecution.getCrawlStatusDirectory();
		
		if(detailOrErrorFormate.equals("Detail")){
			analyticsDetailsReportFile = reportsDirectoryPath
					+ File.separator + UiTestingConstants.ANALYTICS_SUMMARY_DETAILS_REPORTS;	
		}else if(detailOrErrorFormate.equals("Error")){
			analyticsDetailsReportFile = reportsDirectoryPath
					+ File.separator + UiTestingConstants.ANALYTICS_SUMMARY_ERROR_DETAILS_REPORTS;
		}
		
		JsonReaderWriter<AnalyticSummaryDetails> jsonReaderWriter = new JsonReaderWriter<AnalyticSummaryDetails>();
		analyticsSummaryDetails = jsonReaderWriter.readJsonObjectFromFile(
				new AnalyticSummaryDetails(), analyticsDetailsReportFile);
		
		
		for (Map.Entry<String, AnalyticsDetails> entry : analyticsSummaryDetails
				.getEachAnalyticsDetails().entrySet()) {
			String key = entry.getKey();
			AnalyticsDetails values = entry.getValue();
			
			AnalyticsDetails analyticsDetailsUi = new AnalyticsDetails();
			analyticsDetailsUi.setBaseLineAppName(values.getBaseLineAppName());
			analyticsDetailsUi.setCurrentRunAppName(values.getCurrentRunAppName());
			analyticsDetailsUi.setBaseLinePageUrl(values.getBaseLinePageUrl());
			analyticsDetailsUi.setCurrentRunPageUrl(values.getCurrentRunPageUrl());
			analyticsDetailsUi.setPageTitle(values.getPageTitle());
			
			analyticsDetailsUi.setExpectedTagName(values.getExpectedTagName());
			analyticsDetailsUi.setActualTagName(values.getActualTagName());
			analyticsDetailsUi.setExpectedTagVarName(values.getExpectedTagVarName());
			analyticsDetailsUi.setExpectedTagVarValue(values.getExpectedTagVarValue());
			analyticsDetailsUi.setActualTagVarValue(values.getActualTagVarValue());
			analyticsDetailsUi.setTestResult(values.getTestResult());
			
			analyticsDetailsUi.setBaselineTagDataMap(values.getBaselineTagDataMap());
			analyticsDetailsUi.setCurrentTagDataMap(values.getCurrentTagDataMap());
			analyticsDetailsUi.setScheduleExecutionId(scheduleExecutionId);
			analyticsDetailsList.add(analyticsDetailsUi);
		}
		
		logger.debug("Exit : getAnalyticsDetailsReportData");
		return analyticsDetailsList;
	}
	
	@Override
	@Transactional
	public boolean saveUsageReport(UsageReport usageReport) throws Exception {
		logger.debug("Entry And Exit : saveUsageReport");
		return homeDao.saveUsageReport(usageReport);
	}

	@Override
	@Transactional
	public boolean updateUsageReport(UsageReport usageReport) throws Exception {
		logger.debug("Entry And Exit : updateUsageReport");
		return homeDao.updateUsageReport(usageReport);
	}

	@Override
	@Transactional
	public UsageReport getUsageReport(Integer scheduleExecutionId) throws Exception {
		logger.debug("Entry And Exit : getUsageReport");
		return homeDao.getUsageReport(scheduleExecutionId);
	}

	@Override
	@Transactional
	public List<UsageReportResult> getUsageReportList(UsageReportForm form) throws Exception {
		List<UsageReportResult> usageReportResultList = new ArrayList<UsageReportResult>();
		List<UsageReport> usageReportList = new ArrayList<UsageReport>();
		Date fromDate = null;
		Date toDate = null;
		UsageReportResult reportResult = null;
		Users user = null;
		Schedule schedule = null;
		int count = 1;
		if(form != null) {
			if(StringUtils.isNotBlank(form.getFromDate()) && StringUtils.isNotBlank(form.getToDate())) {
				fromDate = DateUtil.convertToDate(form.getFromDate(), DateUtil.UI_DATE_PICKER_FORMAT);
				toDate = DateUtil.convertToDate(form.getToDate(), DateUtil.UI_DATE_PICKER_FORMAT);
			}
		}

		usageReportList = homeDao.getUsageReportList(form, fromDate, toDate);
		String userIds = "0";
		String scheduleExecutionId = "0";
		String scheduleId = "0";
		String applicationId = "0";
		for(UsageReport report : usageReportList) {
			userIds = userIds + "," + report.getUserId();
			scheduleExecutionId = scheduleExecutionId + "," + report.getReferenceId();
			applicationId = applicationId + "," + report.getApplicationId();
		}
		Map<Integer, Users> userMap = userDao.getMintUserMapById(userIds);
		Map<Integer, ScheduleExecution> scheduleExecutionMap = scheduledServiceDao.getScheduleExecutionMap(scheduleExecutionId);
		
		for (Map.Entry<Integer, ScheduleExecution> entry : scheduleExecutionMap.entrySet()) {
			scheduleId = scheduleId + "," +entry.getValue().getScheduleId();
		}
		Map<Integer, Schedule> scheduleMap = scheduledServiceDao.getScheduleMap(scheduleId);
		Map<Integer, Application> applicationMap = applicationDao.getApplicationMap(applicationId);
		
		for(UsageReport report : usageReportList) {
			user = userMap.get(report.getUserId());
			reportResult = new UsageReportResult();
			reportResult.setSlno(count);
			reportResult.setEnvironmentCategoryName(EnvironmentCategoryENUM.getEnvironmentCategory(report.getEnvironmentId()).getEnvironmentCategoryName());
			reportResult.setFunctionalityName(UIFunctionalityType.getUIFunctionalityTypeName(report.getFunctionalityTypeId()));
			reportResult.setGroupName(user.getGroup().getGroupName());
			reportResult.setUserName(user.getUserName());
			reportResult.setSolutionTypeName(Constants.SolutionType.getSolutionTypeName(report.getSolutionTypeId()));
			reportResult.setStartDate(DateUtil.convertToString(report.getStartDateTime(), DateUtil.UI_DATE_PICKER_FORMAT));
			reportResult.setEndDate(DateUtil.convertToString(report.getEndDateTime(), DateUtil.UI_DATE_PICKER_FORMAT));
			reportResult.setNotes("  ");
			if(report.getSolutionTypeId() == Constants.SolutionType.UI_TESTING.getSolutionTypeId()) {
				//int schId = scheduledServiceDao.getScheduleExecution(report.getReferenceId()).getScheduleId();
				ScheduleExecution scheduleExecution = scheduleExecutionMap.get(report.getReferenceId());
				//schedule = scheduledServiceDao.getSchedule(schId);
				if(scheduleExecution!=null){
					schedule = scheduleMap.get(scheduleExecution.getScheduleId());
				}
				if(applicationMap.get(report.getApplicationId()) != null){
					reportResult.setApplicationName(applicationMap.get(report.getApplicationId()).getApplicationName());
				}
				reportResult.setNotes(schedule.getNotes());
			} else if(report.getSolutionTypeId() == Constants.SolutionType.WEBSERVICE.getSolutionTypeId()) {
				
			} else if(report.getSolutionTypeId() == Constants.SolutionType.MISCELANEOUS.getSolutionTypeId()) {
				reportResult.setApplicationName(applicationMap.get(report.getApplicationId()).getApplicationName());
			}
			usageReportResultList.add(reportResult);
			count++;
		}
		return usageReportResultList;
	}

	@Override
	@Transactional
	public WebserviceSuite getSavedWsSuits(int webserviceSuiteId) {
		logger.debug("Entry And Exit : getUsageReport");
		return homeDao.getSavedWsSuits(webserviceSuiteId);
	}

	@Override
	@Transactional
	public List<WebserviceSetupForm> getSavedWsSuitesParams(
			int webserviceSuiteId) {
		logger.debug("Entry And Exit : getUsageReport");
		return homeDao.getSavedWsSuitesParams(webserviceSuiteId);
	}

	@Override
	@Transactional
	public boolean saveScheduleScript(ScheduleScript scheduleScript,ScheduleScriptXref scheduleScriptXref) {
		logger.debug("Entry And Exit : saveScheduleScript");
		boolean isSaved =  false;
		
		isSaved =  homeDao.saveScheduleScript(scheduleScript);
		if(isSaved){
			scheduleScriptXref.setScheduleScriptId(scheduleScript.getScheduleScriptId());
			homeDao.saveScheduleScriptXref(scheduleScriptXref);
		}
		return isSaved;
	}
	
	
	@Override
	@Transactional
	public boolean updateScheduleScript(List<ScheduleScriptXref> scheduleScriptList) {
		
		for ( ScheduleScriptXref scheduleScript : scheduleScriptList ){
			homeDao.updateScheduleScript(scheduleScript.getScheduleScript());
		}
		return true;
	}

	@Override
	@Transactional
	public List<ScheduleScript> getScheduleScript(Integer scheduleExectionId) {
		return homeDao.getScheduleScript(scheduleExectionId);
	}

	@Override
	@Transactional
	public List<ScheduleExecution> getScheduleExecution(Integer scheduleId) {
		return homeDao.getScheduleExecution(scheduleId);
	}

	@Override
	@Transactional
	public List<IdNamePair> getChartData(Integer criteriaId, String from, String to) {
		String criteria = "";
		List<Object[]> objList = null;
		List<IdNamePair> pairList = new ArrayList<IdNamePair>();
		IdNamePair pair = null;
		Date fromDate = null;
		Date toDate = null;
		
		if(criteriaId == UsageReportConstants.FUNCTIONALITY) {
			criteria = UsageReportConstants.FUNCTIONALITY_ID;
		}
		if(criteriaId == UsageReportConstants.GROUP) {
			criteria = UsageReportConstants.GROUP_ID;
		}
		if(criteriaId == UsageReportConstants.ENVIRONMENT) {
			criteria = UsageReportConstants.ENVIRONMENT_ID;
		}


		if(StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to)) {
			fromDate = DateUtil.convertToDate(from, DateUtil.WS_DATE_PICKER_FORMAT);
			toDate = DateUtil.convertToDate(to, DateUtil.WS_DATE_PICKER_FORMAT);
		} else if(StringUtils.isBlank(from) && StringUtils.isNotBlank(to)) {
			fromDate = null;
			toDate = DateUtil.convertToDate(to, DateUtil.WS_DATE_PICKER_FORMAT);
		} else if(StringUtils.isNotBlank(from) && StringUtils.isBlank(to)) {
			fromDate = DateUtil.convertToDate(from, DateUtil.WS_DATE_PICKER_FORMAT);
			toDate = null;
		} else {
			fromDate = null;
			toDate = null;
		}
		
		objList =  homeDao.getChartData(criteria, criteriaId, fromDate, toDate);
		for(Object[] obj : objList) {
			pair  = new IdNamePair();
			pair.setId(Integer.parseInt(obj[1].toString()));
			if(criteriaId == UsageReportConstants.FUNCTIONALITY) {
				pair.setName(obj[0].toString()+ ":"+UIFunctionalityType.getUIFunctionalityTypeName(Integer.parseInt(obj[0].toString())));
			}
			if(criteriaId == UsageReportConstants.GROUP) {
				pair.setName(obj[0].toString()+ ":"+userDao.getGroupName(Integer.parseInt(obj[0].toString())));
			}
			if(criteriaId == UsageReportConstants.ENVIRONMENT) {
				pair.setName(obj[0].toString()+ ":" +EnvironmentCategoryENUM.getEnvironmentName(Integer.parseInt(obj[0].toString())));
			}
			pairList.add(pair);
		}
		return pairList;
	}

	@Override
	@Transactional
	public List<SuitTextImageXref> getSuitTextImageXref(int suitId){
		logger.debug("Entry: getSuitTextImageXref");
		return uiTestingDAO.getSuitTextImageXref(suitId);
	}
	
	@Override
	@Transactional
	public List<IdNamePair> getBarChartData(Integer criteriaId,Integer id, String from, String to, int position) {
		String criteria = "";
		String requiredCriteria = "";
		List<Object[]> objList = null;
		List<IdNamePair> pairList = new ArrayList<IdNamePair>();
		IdNamePair pair = null;
		Date fromDate = null;
		Date toDate = null;
		Integer requiredCriteriaId = 0;
		if(position == 1){
			if(criteriaId == UsageReportConstants.FUNCTIONALITY) {
				requiredCriteriaId = UsageReportConstants.ENVIRONMENT;
				requiredCriteria = UsageReportConstants.ENVIRONMENT_ID;
			}
			if(criteriaId == UsageReportConstants.GROUP) {
				requiredCriteriaId = UsageReportConstants.FUNCTIONALITY;
				requiredCriteria = UsageReportConstants.FUNCTIONALITY_ID;
			}
			if(criteriaId == UsageReportConstants.ENVIRONMENT) {
				requiredCriteriaId = UsageReportConstants.FUNCTIONALITY;
				requiredCriteria = UsageReportConstants.FUNCTIONALITY_ID;
			}
	
			
		} else {
			if(criteriaId == UsageReportConstants.FUNCTIONALITY) {
				requiredCriteriaId = UsageReportConstants.GROUP;
				requiredCriteria = UsageReportConstants.GROUP_ID;
			}
			if(criteriaId == UsageReportConstants.GROUP) {
				requiredCriteriaId = UsageReportConstants.ENVIRONMENT;
				requiredCriteria = UsageReportConstants.ENVIRONMENT_ID;
			}
			if(criteriaId == UsageReportConstants.ENVIRONMENT) {
				requiredCriteriaId = UsageReportConstants.GROUP;
				requiredCriteria = UsageReportConstants.GROUP_ID;
			}
		}
		if(criteriaId == UsageReportConstants.FUNCTIONALITY) {
			criteria = UsageReportConstants.FUNCTIONALITY_ID;
		}
		if(criteriaId == UsageReportConstants.GROUP) {
			criteria = UsageReportConstants.GROUP_ID;
		}
		if(criteriaId == UsageReportConstants.ENVIRONMENT) {
			criteria = UsageReportConstants.ENVIRONMENT_ID;
		}

		if(StringUtils.isNotBlank(from) && StringUtils.isNotBlank(to)) {
			fromDate = DateUtil.convertToDate(from, DateUtil.WS_DATE_PICKER_FORMAT);
			toDate = DateUtil.convertToDate(to, DateUtil.WS_DATE_PICKER_FORMAT);
		} else if(StringUtils.isBlank(from) && StringUtils.isNotBlank(to)) {
			fromDate = null;
			toDate = DateUtil.convertToDate(to, DateUtil.WS_DATE_PICKER_FORMAT);
		} else if(StringUtils.isNotBlank(from) && StringUtils.isBlank(to)) {
			fromDate = DateUtil.convertToDate(from, DateUtil.WS_DATE_PICKER_FORMAT);
			toDate = null;
		} else {
			fromDate = null;
			toDate = null;
		}
		objList =  homeDao.getBarChartData(requiredCriteria, requiredCriteriaId, fromDate, toDate,  criteria, id);
		for(Object[] obj : objList) {
			pair  = new IdNamePair();
			pair.setId(Integer.parseInt(obj[1].toString()));
			if(requiredCriteriaId == UsageReportConstants.FUNCTIONALITY) {
				pair.setName(UIFunctionalityType.getUIFunctionalityTypeName(Integer.parseInt(obj[0].toString())));
			}
			if(requiredCriteriaId == UsageReportConstants.GROUP) {
				pair.setName(userDao.getGroupName(Integer.parseInt(obj[0].toString())));
			}
			if(requiredCriteriaId == UsageReportConstants.ENVIRONMENT) {
				pair.setName(EnvironmentCategoryENUM.getEnvironmentName(Integer.parseInt(obj[0].toString())));
			}
			pairList.add(pair);
		}
		return pairList;
	}
	
	/*private void addCompareConfigLinkDetailsToScheduleStatus(
			CompareConfig compareConfig, ScheduleStatus scheduleStatus) {
		if ( null != compareConfig ){
			
			Iterator iterateValue = compareConfig.getCompareLinkList().iterator();
			
			while(iterateValue.hasNext()){
				if(Integer.parseInt(iterateValue.next()) == 100){
					
				}
			}
			if ( null == compareConfig.getCompareLinkList()){
				scheduleStatus.setComparisonPendingUrlCount(0);
			}else{
				scheduleStatus.setComparisonPendingUrlCount(compareConfig.getComparePendingUrlCount());
			}
			if ( null == compareConfig.getCompareProcessedUrlCount() ){
				scheduleStatus.setComparisonProcessedUrlCount(0);
			}else {
				scheduleStatus.setComparisonProcessedUrlCount(compareConfig.getCompareProcessedUrlCount());
			}
			
			scheduleStatus.setCompareConfig(compareConfig);
		}
	}*/
	
	@Override
	@Transactional
	public List<SuitBrokenReportsXref> getSuitBrokenReportsXref(int suitId){
		logger.debug("Entry: getSuitBrokenReportsXref");
		return uiTestingDAO.getSuitBrokenReportsXref(suitId);
	}

}
