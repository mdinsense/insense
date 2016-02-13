package com.ensense.insense.services.scheduler;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cts.mint.analytics.common.AnalyticsDataReportingUtils;
import com.cts.mint.analytics.common.AnalyticsReportsSerializeUtils;
import com.cts.mint.analytics.common.WebAnalyticsUtils;
import com.cts.mint.analytics.model.AnalyticsDetails;
import com.cts.mint.analytics.model.AnalyticsSummaryReport;
import com.cts.mint.analytics.model.AnalyticsSummaryReportUi;
import com.cts.mint.analytics.model.AnalyticsTagDetail;
import com.cts.mint.analytics.model.BrokenUrlData;
import com.cts.mint.analytics.model.SamePagesBrokenUrlDataStore;
import com.cts.mint.analytics.model.SamePagesDataStore;
import com.cts.mint.analytics.model.WebAnalyticsPageData;
import com.cts.mint.analytics.model.WebAnalyticsTagData;
import com.cts.mint.analytics.service.AnalyticsDashboardService;
import com.cts.mint.common.JsonReaderWriter;
import com.cts.mint.common.MintFileUtils;
import com.cts.mint.common.model.Link;
import com.cts.mint.common.service.HomeService;
import com.cts.mint.common.utils.CommonUtils;
import com.cts.mint.common.utils.Constants;
import com.cts.mint.common.utils.Constants.BrokenReportSelection;
import com.cts.mint.common.utils.Constants.FILE;
import com.cts.mint.common.utils.Constants.ReportPattern;
import com.cts.mint.common.utils.Constants.SCHEDULER;
import com.cts.mint.common.utils.ExecutionStatus;
import com.cts.mint.common.utils.FileDirectoryUtil;
import com.cts.mint.crawler.model.CrawlConfig;
import com.cts.mint.generated.jaxb.analytics.detailedview.DetailedViewWebAnalyticsTagData.DetailedViewTags;
import com.cts.mint.generated.jaxb.analytics.detailedview.DetailedViewWebAnalyticsTagData.DetailedViewTags.TagVariablesData;
import com.cts.mint.reports.model.TextAndImageDetail;
import com.cts.mint.reports.service.ScheduledService;
import com.cts.mint.reports.util.ReportsUtil;
import com.cts.mint.uitesting.entity.Schedule;
import com.cts.mint.uitesting.entity.ScheduleExecution;
import com.cts.mint.uitesting.entity.Suit;
import com.cts.mint.uitesting.entity.SuitBrokenReportsXref;
import com.cts.mint.uitesting.model.ScheduleDetails;
import com.cts.mint.uitesting.model.ScheduleExecutionDetail;
import com.cts.mint.uitesting.model.UiTestingConstants;
import com.cts.mint.uitesting.service.ReportsGenerationService;
import com.cts.mint.util.DateTimeUtil;
import com.cts.mint.util.EmailUtil;
import com.cts.mint.util.ExcelWriter;
import com.cts.mint.util.Pair;
import com.cts.mint.util.ReportUtil;


@Service
public class ReportsScheduler {
	
	private static Logger logger = Logger.getLogger(ReportsScheduler.class);
	
	@Autowired
	private MessageSource schedulerProperties;
	
	@Autowired
	private MessageSource configProperties;
	
	@Autowired
	private ReportsGenerationService reportsGenerationService; 
	
	@Autowired
	private ScheduledService scheduledService;
	
	@Autowired
	private HomeService homeService;
	
	@Autowired
	private AnalyticsDashboardService analyticsDashboard;
	
	@Scheduled(fixedDelayString = "${mint.scheduler.uischedule.reports.delaytime}")
	public void executePendingReportsGeneration() {

		try {
			if (! Boolean.parseBoolean(CommonUtils.getPropertyFromPropertyFile(
					schedulerProperties, SCHEDULER.UI_REPORTS_SCHEDULER_ENABLE))) {
				
				//Schedule Job is disabled. Do nothing.
				return;
			}
		} catch (Exception e) {
			logger.error("Unable to start the UI Reports Scheduler.");
			e.printStackTrace();
		}

		ScheduleExecutionDetail scheduleExecutionDetail = reportsGenerationService.getPendingReportGeneration();
		
		if ( null != scheduleExecutionDetail && null != scheduleExecutionDetail.getScheduleExecution() && null != scheduleExecutionDetail.getScheduleExecution().getScheduleExecutionId() ){
			logger.info("New Scheudle found for reports generation.");
			logger.info("Generate Analytics report :"+scheduleExecutionDetail.getSchedule().isAnalyticsTesting());
			logger.info("Generate Broken link report :"+scheduleExecutionDetail.getSchedule().isBrokenUrlReport());
			
			logger.info("Generate Broken link report For app:"+scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getApplication().getApplicationName());
			logger.info("Generate Broken link report For Env:"+scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getEnvironment().getEnvironmentCategory().getEnvironmentCategoryName());
			
			String emailSMTPHost = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.email.smtp.host");
			String emailFrom = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.email.sender");
			
			EmailUtil emailUtil = new EmailUtil(emailSMTPHost, emailFrom);
			
			String recepient = scheduleExecutionDetail.getSchedule().getEmailIds();
			List<String> recepientList = CommonUtils.getStringAsList(recepient);
			
			ScheduleExecution scheduleExecution = new ScheduleExecution();
			scheduleExecution.setScheduleExecutionId(scheduleExecutionDetail.getScheduleExecution().getScheduleExecutionId());
			 String applicationName = scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getApplication().getApplicationName();
            // String environmentName = scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getEnvironment().getEnvironmentName();
             String environmentCategoryName = scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getEnvironment().getEnvironmentCategory().getEnvironmentCategoryName();
             
			if(scheduleExecutionDetail.getSchedule().isBrandingUrlReport()){
		
                 String tempDirectory = configProperties.getMessage("mint.crawler.tempDirectory", null, Locale.getDefault());
				 String excelReportFilePath =  scheduleExecutionDetail.getScheduleExecution().getReportsDirectory() + File.separator 
                 + ReportPattern.IMAGE_TEXT_COMPARISON_REPORT_PATTERN.getReportPatternName()+"_" + applicationName.toUpperCase() + "_" + environmentCategoryName.toUpperCase() + "_" + DateTimeUtil.getCurrentDateTime() + ".xlsx";
				 
				 List<Pair> matchingList = new ArrayList<Pair>();
				 if ( ! scheduledService.updateScheduleExecution(scheduleExecution, Constants.TEXTORIMAGE_REPORT_STATUS_START) ){
						logger.error("Exception while updating the ScheduleExecution for Text Or Image Report start time.");
					}
				 if ( scheduleExecutionDetail.getSchedule().getBaselineScheduleExecutionId() > 0 ){
					 String excelReportFilePathwithbaseline = excelReportFilePath.replace("MINT_TEXT_IMAGE_REPORTS_DATA_", "MINT_TEXT_IMAGE_REPORTS_BASELINE_DATA_");
					 matchingList = generateTextAndImageBaselineReport(scheduleExecutionDetail, excelReportFilePathwithbaseline);
					 //matchingList = generateTextAndImageBaselineReport(scheduleExecutionDetail, excelReportFilePath);
				 }else{
					 matchingList = generateTextAndImageReport(scheduleExecutionDetail, excelReportFilePath);
				 }
				 
				 if (null != matchingList && matchingList.size() > 0 ){
					 
					 scheduleExecution.setTextOrImageReportStatusId(ExecutionStatus.COMPLETE.getStatusCode());
					 scheduleExecution.setReportStatusRefId(ExecutionStatus.COMPLETE.getStatusCode());

					 
						String emailContent = CommonUtils.getPropertyFromPropertyFile(
								configProperties,
								SCHEDULER.SCHEDULER_REPORTS_NOTIFICATION_MSG);
						String subject = CommonUtils.getPropertyFromPropertyFile(configProperties,
								SCHEDULER.REPORTS_COMPLETE_NOTIFICATION_MSG);
						subject = subject + ExecutionStatus.COMPLETE.getStatus();
						List<String> emailAttachmentList = new ArrayList<String>();
						
						try {
							if ( MintFileUtils.isFileExists(excelReportFilePath) ){
								emailAttachmentList.add(excelReportFilePath);
								logger.info("excelReportFilePath :"+excelReportFilePath);
								//String analyticsFailureReportExcelFilePath = excelReportFilePath.replace(ReportPattern.IMAGE_TEXT_COMPARISON_REPORT_PATTERN.getReportPatternName()+"_", "MINT_TEXT_IMAGE_ERROR_REPORTS_DATA_");
								//emailAttachmentList.add(analyticsFailureReportExcelFilePath);
							}
							
						}catch(Exception e){
							logger.error("Exception while adding the email attachment file.");
						}
						
						if ( scheduleExecutionDetail.getSuitId() > 0 ){
							Suit suit = homeService.getSavedSuits(scheduleExecutionDetail.getSuitId());
							emailContent = this.addTextOrImageReportSummaryToMailContent(suit, matchingList, emailContent);
							emailUtil.sendEmailWithAttachement(recepientList, subject, emailContent, emailAttachmentList);
						}
					
				}else{
				
					scheduleExecution.setReportStatusRefId(ExecutionStatus.ERROR.getStatusCode());
					
					scheduleExecution.setTextOrImageReportStatusId(ExecutionStatus.ERROR.getStatusCode());
					
					if ( null != recepient && recepient.trim().length() > 0 ){
						String emailContent = CommonUtils.getPropertyFromPropertyFile(
								configProperties,
								SCHEDULER.SCHEDULER_REPORTS_NOTIFICATION_ERROR_MSG);
						String subject = CommonUtils.getPropertyFromPropertyFile(configProperties,
								SCHEDULER.REPORTS_COMPLETE_NOTIFICATION_MSG);
						subject = subject + ExecutionStatus.ERROR.getStatus();
						
						emailUtil.sendEmail(recepientList, subject, emailContent);
					}
				}
				 
				 
				 scheduleExecution.setTextOrImageReportGenEndTime(new Date());
				    if ( ! scheduledService.updateScheduleExecution(scheduleExecution, Constants.TEXTORIMAGE_REPORT_STATUS_END) ){
						logger.error("Exception while updating the ScheduleExecution for Text Or Image Report End time.");
					}
			
			}
			
			if(scheduleExecutionDetail.getSchedule().isBrandingUrlReport()){
				try {	
			        ScheduleExecution baselineExecution = null;
			        CrawlConfig crawlConfig = null, baselineCrawlConfig = null;
			        JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();
			        crawlConfig = jsonReaderWriter.readJsonObjectFromFile(new CrawlConfig(), scheduleExecutionDetail.getScheduleExecution().getCrawlStatusDirectory()+ File.separator + UiTestingConstants.CRAWL_CONFIG);
			        List<Link> currentRunNavigationList = crawlConfig.getCrawlStatus().getNavigationList(); 
			              
			        if(scheduleExecutionDetail.getSchedule().getBaselineScheduleExecutionId() != null && scheduleExecutionDetail.getSchedule().getBaselineScheduleExecutionId() > 0 ) {
			           logger.debug("Started normalization for textImage comparison with baseLine");
			           baselineExecution =  scheduledService.getScheduleExecution(scheduleExecutionDetail.getSchedule().getBaselineScheduleExecutionId() );
			           baselineCrawlConfig = jsonReaderWriter.readJsonObjectFromFile(new CrawlConfig(), baselineExecution.getCrawlStatusDirectory()+ File.separator + UiTestingConstants.CRAWL_CONFIG);
				       List<Link> baseLineRunNavigationList = baselineCrawlConfig.getCrawlStatus().getNavigationList();
				       String imageTextDirectory = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.crawler.imageTextDirectory");
				       boolean isCopied = ReportUtil.normalizeTextImageForBaseline(scheduleExecutionDetail.getScheduleExecution().getReportsDirectory(), baselineExecution.getReportsDirectory(), baseLineRunNavigationList, currentRunNavigationList, imageTextDirectory);
				       //boolean isCopied = FileDirectoryUtil.copyAllPageImagesFromSubdirectories(scheduleExecutionDetail.getScheduleExecution().getReportsDirectory());
				       
				       if ( isCopied ){
				    	   List<File> fileList = new ArrayList<File>();
				    	  
				    	   String normalizedImageTextdirectoryPath = scheduleExecutionDetail.getScheduleExecution().getReportsDirectory() + "\\NormalizedImageTextdirectory";
				    	   
				    	   fileList.add(new File(normalizedImageTextdirectoryPath));
				    	   
				    	   logger.info("Source directory path :"+normalizedImageTextdirectoryPath + FILE.ZIP);
				    	   logger.info("normalizedImageTextdirectoryPath :"+normalizedImageTextdirectoryPath);
				    	   
				    	   //MintFileUtils.zipDirectory(normalizedImageTextdirectoryPath, normalizedImageTextdirectoryPath + FILE.ZIP);
				       }
				       
				       logger.debug("Completed normalization for textImage comparison with baseLine");
			         }
			 
				} catch (Exception e) {
					logger.error("Unable to normalize textimage folders.");
					e.printStackTrace();
				}
			}
			if ( scheduleExecutionDetail.getSchedule().isBrokenUrlReport() ){
				//ScheduleExecution scheduleExecution = scheduledService.getScheduleExecution(scheduleExecutionDetail.getScheduleExecution().getScheduleExecutionId());
				
				scheduleExecution.setBrokenUrlReportGenStartTime(DateTimeUtil.getCurrentDateTime());
				
				if ( ! scheduledService.updateScheduleExecution(scheduleExecution, Constants.BROKEN_URL_STATUS_START) ){
					logger.error("Exception while updating the ScheduleExcution for Broken Link Report start time.");
				}
				
				/*String applicationName = scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getApplication().getApplicationName();
				String environmentCategoryName = scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getEnvironment().getEnvironmentCategory().getEnvironmentCategoryName();
				*/
				String brokenUrlReportFilePath = scheduleExecutionDetail.getScheduleExecution().getReportsDirectory()
						+ "\\" + "MINT_BROKEN_URL_REPORT_" + applicationName.toUpperCase() + "_" + environmentCategoryName.toUpperCase() + "_" + DateTimeUtil.getCurrentDateTime() + ".xlsx";

				String brokenUrlWithResourceReportFilePath = scheduleExecutionDetail.getScheduleExecution().getReportsDirectory()
                        + "\\" + "MINT_BROKEN_URL_REPORT_WITH_RESOURCE_" + applicationName.toUpperCase() + "_" + environmentCategoryName.toUpperCase();

				brokenUrlWithResourceReportFilePath = brokenUrlWithResourceReportFilePath + "_" + DateTimeUtil.getCurrentDateTime() + ".xlsx";
				
				String brokenUrlReportPageLoadAttributeFilePath = scheduleExecutionDetail.getScheduleExecution().getReportsDirectory()
				                        + "\\" + "MINT_PAGE_LOAD_TIME_ATTRIBUTES_REPORT_" + applicationName.toUpperCase() + "_" + environmentCategoryName.toUpperCase() + "_" + DateTimeUtil.getCurrentDateTime() + ".xlsx";
				
				logger.info("brokenUrlWithResourceReportFilePath :"+brokenUrlWithResourceReportFilePath);

				boolean status = generateBrokenUrlReport(scheduleExecutionDetail, brokenUrlReportFilePath, brokenUrlWithResourceReportFilePath, brokenUrlReportPageLoadAttributeFilePath);
				
				//TODO: Name and logo details.
				//boolean textAndImageReportStatus = generateTextAndImageReportStatus(scheduleExecutionDetail);
				
				status = updateBrokenLinkGenerationStatus(status, scheduleExecutionDetail);
				
				if ( status ){
					if ( null != recepient && recepient.trim().length() > 0 ){
						String emailContent = CommonUtils.getPropertyFromPropertyFile(
								configProperties,
								SCHEDULER.SCHEDULER_REPORTS_NOTIFICATION_MSG);
						String subject = CommonUtils.getPropertyFromPropertyFile(configProperties,
								SCHEDULER.REPORTS_COMPLETE_NOTIFICATION_MSG);
						subject = subject + ExecutionStatus.COMPLETE.getStatus();
						
						List<String> emailAttachmentList = new ArrayList<String>();
						
						try {
							if ( MintFileUtils.isFileExists(brokenUrlReportFilePath) ){
								emailAttachmentList.add(brokenUrlReportFilePath);
							}
							if ( MintFileUtils.isFileExists(brokenUrlWithResourceReportFilePath)){
								emailAttachmentList.add(brokenUrlWithResourceReportFilePath);
							}
							if ( MintFileUtils.isFileExists(brokenUrlReportPageLoadAttributeFilePath)){
								emailAttachmentList.add(brokenUrlReportPageLoadAttributeFilePath);
							}
						}catch(Exception e){
							logger.error("Exception while adding the email attachment file.");
						}
						
						emailUtil.sendEmailWithAttachement(recepientList, subject, emailContent, emailAttachmentList);
					}
				}else{
					if ( null != recepient && recepient.trim().length() > 0 ){
						String emailContent = CommonUtils.getPropertyFromPropertyFile(
								configProperties,
								SCHEDULER.SCHEDULER_REPORTS_NOTIFICATION_ERROR_MSG);
						String subject = CommonUtils.getPropertyFromPropertyFile(configProperties,
								SCHEDULER.REPORTS_COMPLETE_NOTIFICATION_MSG);
						subject = subject + ExecutionStatus.ERROR.getStatus();
						
						emailUtil.sendEmail(recepientList, subject, emailContent);
					}
				}
			}
			
			
			if ( scheduleExecutionDetail.getSchedule().isAnalyticsTesting() ) {
				
				scheduleExecution.setAnalyticsReportGenStartTime(new Date());
				if ( ! scheduledService.updateScheduleExecution(scheduleExecution, Constants.ANALYTICS_REPORT_STATUS_START) ){
					logger.error("Exception while updating the ScheduleExcution for Analytics Report start time.");
				}
				String analyticsReportFilePath = scheduleExecutionDetail.getScheduleExecution().getReportsDirectory()
	                    						+ "\\" + "MINT_ANALYTICS_REPORT_" + applicationName.toUpperCase() + "_" + environmentCategoryName.toUpperCase();
				analyticsReportFilePath = analyticsReportFilePath + "_" + DateTimeUtil.getCurrentDateTime() + ".xlsx";
				
				String analyticsObjectFilePath = scheduleExecutionDetail.getScheduleExecution().getCrawlStatusDirectory();
				
				boolean status = generateAnalyticsReport(scheduleExecutionDetail, analyticsReportFilePath, analyticsObjectFilePath);
				scheduleExecution = scheduledService.getScheduleExecution(scheduleExecutionDetail.getScheduleExecution().getScheduleExecutionId());
				if ( status ){
					scheduleExecution.setAnalyticsReportStatusId(ExecutionStatus.COMPLETE.getStatusCode());
					if ( ! scheduleExecutionDetail.getSchedule().isBrokenUrlReport() ){
						scheduleExecution.setReportStatusRefId(ExecutionStatus.COMPLETE.getStatusCode());
					}
				}else{
					if ( ! scheduleExecutionDetail.getSchedule().isBrokenUrlReport() ){
						scheduleExecution.setReportStatusRefId(ExecutionStatus.ERROR.getStatusCode());
					}
					scheduleExecution.setAnalyticsReportStatusId(ExecutionStatus.ERROR.getStatusCode());
				}
				
				scheduleExecution.setAnalyticsReportGenEndTime(new Date());
				if ( ! scheduledService.updateScheduleExecution(scheduleExecution, Constants.ANALYTICS_REPORT_STATUS_END) ){
					logger.error("Exception while updating the ScheduleExcution for Analytics Report Generation End Time.");
					status = false;
				}
				
				if ( status ){
					if ( null != recepient && recepient.trim().length() > 0 ){
						String emailContent = CommonUtils.getPropertyFromPropertyFile(
								configProperties,
								SCHEDULER.SCHEDULER_REPORTS_NOTIFICATION_MSG);
						String subject = CommonUtils.getPropertyFromPropertyFile(configProperties,
								SCHEDULER.REPORTS_COMPLETE_NOTIFICATION_MSG);
						subject = subject + ExecutionStatus.COMPLETE.getStatus();
						
						List<String> emailAttachmentList = new ArrayList<String>();
						
						try {
							if ( MintFileUtils.isFileExists(analyticsReportFilePath) ){
								emailAttachmentList.add(analyticsReportFilePath);
								String analyticsFailureReportExcelFilePath = analyticsReportFilePath.replace("MINT_ANALYTICS_REPORT_", "MINT_ANALYTICS_ERROR_REPORT_");
								emailAttachmentList.add(analyticsFailureReportExcelFilePath);
							}
							
						}catch(Exception e){
							logger.error("Exception while adding the email attachment file.");
						}
						emailContent = this.addReportSummaryToMailContent(scheduleExecution, emailContent);
						emailUtil.sendEmailWithAttachement(recepientList, subject, emailContent, emailAttachmentList);
					}
				}else{
					if ( null != recepient && recepient.trim().length() > 0 ){
						String emailContent = CommonUtils.getPropertyFromPropertyFile(
								configProperties,
								SCHEDULER.SCHEDULER_REPORTS_NOTIFICATION_ERROR_MSG);
						String subject = CommonUtils.getPropertyFromPropertyFile(configProperties,
								SCHEDULER.REPORTS_COMPLETE_NOTIFICATION_MSG);
						subject = subject + ExecutionStatus.ERROR.getStatus();
						
						emailUtil.sendEmail(recepientList, subject, emailContent);
					}
				}
			}
			
		}
	
	}

	private List<Pair> generateTextAndImageReport(
			ScheduleExecutionDetail scheduleExecutionDetail,
			String excelReportFilePath) {
		boolean status = true;
		List<Link> navigationDetails = new ArrayList<Link>();
		List<Pair> matchingList = new ArrayList<Pair>();
		try {
			if (null != scheduleExecutionDetail.getScheduleExecution()
					.getCrawlStatusDirectory()) {
				navigationDetails = ReportUtil.getNavigationList(scheduleExecutionDetail
						.getScheduleExecution().getCrawlStatusDirectory()+ File.separator + UiTestingConstants.CRAWL_CONFIG);
				for (Link cLink : navigationDetails) {
					Pair pair = new Pair();
					pair.setFirst(null);
					pair.setSecond(cLink);
					matchingList.add(pair);
				}

			}
		} catch (Exception e) {
			status = false;
			logger.error("Exception while getting Link details for the current run.");
		}

		if ( status && matchingList.size() > 0 ){
			String tempDirectory = configProperties.getMessage(
					"mint.crawler.tempDirectory", null, Locale.getDefault());
			tempDirectory = FileDirectoryUtil.getAbsolutePath(tempDirectory,
					FileDirectoryUtil.getMintRootDirectory(configProperties));
			logger.info("*** Started generating TEXT and Image Data Excel Reprot.");
			boolean writeBaselineDate = false;
			ExcelWriter.writeTextOrImageReportDataToExcel(matchingList,
					excelReportFilePath, tempDirectory, writeBaselineDate);
			logger.info("*** Completed generating TEXT and Image Data Excel Report.");
		}
		return matchingList;
	}

	private String addTextOrImageReportSummaryToMailContent(Suit suit,
			List<Pair>  matchingList, String emailContent) {
		

		String textOrImageReportSummary = "";
		try {
			/*analyticsSummaryData = homeService
					.getAnalyticsSummaryReportData(scheduleExecution
							.getScheduleExecutionId());
			schedule = scheduledService.getSchedule(scheduleExecution.getScheduleId());*/
		//	suit = homeService.getSavedSuits(schedule.getSuitId());
			if (matchingList != null && matchingList.size() > 0) {
				textOrImageReportSummary = textOrImageReportSummary + "<table style='display: table; padding: 5px 15px;'>";
				textOrImageReportSummary = textOrImageReportSummary + "<tr><td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>URL No</td>";
				textOrImageReportSummary = textOrImageReportSummary + "<td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>URL</td>";
				textOrImageReportSummary = textOrImageReportSummary + "<td align='center'style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>TotaImagesCount</td>";
				textOrImageReportSummary = textOrImageReportSummary + "<td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>BaselineTotaImagesCount</td>";
				textOrImageReportSummary = textOrImageReportSummary + "<td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>ImageDetails</td>";
				textOrImageReportSummary = textOrImageReportSummary + "<td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>BaseImageDetails</td>";
				textOrImageReportSummary = textOrImageReportSummary + "<td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>TiaaImageCount</td>";
				textOrImageReportSummary = textOrImageReportSummary + "<td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>BaseTiaaImageCount</td>";
				textOrImageReportSummary = textOrImageReportSummary + "<td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>TiaaStringCount</td>";
				textOrImageReportSummary = textOrImageReportSummary + "<td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>BaseTiaaStringCount</td>";
				textOrImageReportSummary = textOrImageReportSummary + "<td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>TiaaCrefStringCount</td>";
				textOrImageReportSummary = textOrImageReportSummary + "<td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>BaseTiaaCrefStringCount</td></tr>";
					
				for (Pair pair : matchingList) {
					  Link baselineLink = (Link) pair.getFirst();
		        	  Link currentLink = (Link) pair.getSecond();
					textOrImageReportSummary = textOrImageReportSummary + "<tr><td align='center'>" + currentLink.getUrlNo() + "</td>";
					textOrImageReportSummary = textOrImageReportSummary + "<td align='center'>" + currentLink.getUrl() + "</td>";
					textOrImageReportSummary = textOrImageReportSummary + "<td align='center'>" + currentLink.getTextImageReportData().getAllImageCount() + "</td>";
					textOrImageReportSummary = textOrImageReportSummary + "<td align='center'>" + baselineLink.getTextImageReportData().getAllImageCount() + "</td>";
					textOrImageReportSummary = textOrImageReportSummary + "<td align='center'>" + currentLink.getTextImageReportData().getAllImageList() + "</td>";
					textOrImageReportSummary = textOrImageReportSummary + "<td align='center'>" + baselineLink.getTextImageReportData().getAllImageList() + "</td>";
					textOrImageReportSummary = textOrImageReportSummary + "<td align='center'>" + currentLink.getTextImageReportData().getTiaaImageCount() + "</td>";
					textOrImageReportSummary = textOrImageReportSummary + "<td align='center'>" + baselineLink.getTextImageReportData().getTiaaImageCount() + "</td>";
					textOrImageReportSummary = textOrImageReportSummary + "<td align='center'>" + currentLink.getTextImageReportData().getTiaaStringCount() + "</td>";
					textOrImageReportSummary = textOrImageReportSummary + "<td align='center'>" + baselineLink.getTextImageReportData().getTiaaStringCount() + "</td>";
					textOrImageReportSummary = textOrImageReportSummary + "<td align='center'>" + currentLink.getTextImageReportData().getTiaaCrefStringCount() + "</td>";
					textOrImageReportSummary = textOrImageReportSummary + "<td align='center'>" + baselineLink.getTextImageReportData().getTiaaCrefStringCount() + "</td></tr>";
				}
				textOrImageReportSummary = textOrImageReportSummary + "</table>";
				//emailContent = emailContent.replace(SCHEDULER.REPORT_SUMMARY_PLACE_HOLDER, textOrImageReportSummary);
				emailContent = emailContent.replace(SCHEDULER.SUIT_NAME_PLACE_HOLDER, suit.getSuitName());
			}
		} catch (Exception ex) {
			logger.error("Unable to add report summary content to mail");
		}
		logger.info("emailContent :" + emailContent);

		return emailContent;
	
	}

	private boolean generateTextAndImageReportStatus(
			ScheduleExecutionDetail scheduleExecutionDetail) {
		List<Link> navigationDetails = new ArrayList<Link>();
		List<String> textToFindList = new ArrayList<String>();
		
		boolean status = true;
		try{
			if ( null != scheduleExecutionDetail.getScheduleExecution().getCrawlStatusDirectory() ){
				CrawlConfig crawlConfig = null;
				JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();
				
				crawlConfig = jsonReaderWriter.readJsonObjectFromFile(new CrawlConfig(), scheduleExecutionDetail.getScheduleExecution().getCrawlStatusDirectory()+ File.separator + UiTestingConstants.CRAWL_CONFIG);
				
				navigationDetails = crawlConfig.getCrawlStatus().getNavigationList();
				
				logger.info("navigationDetails :"+navigationDetails);
			}
		}catch(Exception e){
			status = false;
			logger.error(UiTestingConstants.CRAWL_CONFIG + ", is not available yet for the ScheduleExecutionId :"+scheduleExecutionDetail.getScheduleExecution().getScheduleExecutionId());
		}

		if ( status && null != navigationDetails && navigationDetails.size() > 0 ){
			try{
				textToFindList = getTextToFindList();
				
				Map<String, TextAndImageDetail> textAndImageDetailMap = new LinkedHashMap<String, TextAndImageDetail>();
				
				String imageDirectory = scheduleExecutionDetail.getScheduleExecution().getReportsDirectory() + File.separator + UiTestingConstants.IMAGE_DIRECTORY;
				
				textAndImageDetailMap = ReportsUtil.generateTextFoundCountMap(textToFindList, navigationDetails);
				textAndImageDetailMap = ReportsUtil.collectImages(textAndImageDetailMap, getImagesFileTypes(), getImagesToSkip(), scheduleExecutionDetail.getScheduleExecution().getHarReportsDirectory(), getTiaaImageNameList(), imageDirectory);
				
				JsonReaderWriter<Map<String, TextAndImageDetail>> jsonReaderWriter = new JsonReaderWriter<Map<String, TextAndImageDetail>>();
				jsonReaderWriter.writeJsonObjectToFile(textAndImageDetailMap, scheduleExecutionDetail.getScheduleExecution().getCrawlStatusDirectory()+ File.separator + UiTestingConstants.TEXT_IMAGE_DETAILS);
				
				String applicatioName = scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getApplication().getApplicationName();
				String environmentName = scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getEnvironment().getEnvironmentName();
				
				String tempDirectory = configProperties.getMessage("mint.crawler.tempDirectory", null, Locale.getDefault());
				tempDirectory = FileDirectoryUtil.getAbsolutePath(tempDirectory, FileDirectoryUtil.getMintRootDirectory(configProperties));
				
				String excelReportFilePath =  scheduleExecutionDetail.getScheduleExecution().getReportsDirectory() + File.separator 
						+ "MINT_TEXT_IMAGE_COUNT_REPORTS_" + applicatioName.toUpperCase() + "_" + environmentName.toUpperCase() + "_" + DateTimeUtil.getCurrentDateTime() + ".xlsx";
				
				logger.info("*** Started generating TEXT and Image Count Excel Reprot.");
				ExcelWriter.writeTextCountAndImageDetailsToExcel(textAndImageDetailMap, excelReportFilePath, tempDirectory);
				
				logger.info("*** Completed generating TEXT and Image Count Excel Reprot.");

			}catch(Exception e){
				logger.error("Exception while generating Text count and Image count report");
				logger.error("Stack Trace :"+ExceptionUtils.getMessage(e));
				status = false;
			}
		}

		return status;
	}

	private boolean updateBrokenLinkGenerationStatus(boolean status,
			ScheduleExecutionDetail scheduleExecutionDetail) {
		//ScheduleExecution scheduleExecution = scheduledService.getScheduleExecution(scheduleExecutionDetail.getScheduleExecution().getScheduleExecutionId());
		ScheduleExecution scheduleExecution = new ScheduleExecution();
		scheduleExecution.setScheduleExecutionId(scheduleExecutionDetail.getScheduleExecution().getScheduleExecutionId());
		
		if ( status ){
			if ( ! scheduleExecutionDetail.getSchedule().isAnalyticsTesting() ){
				scheduleExecution.setReportStatusRefId(ExecutionStatus.COMPLETE.getStatusCode());
			}
			scheduleExecution.setBrokenUrlReportStatusId(ExecutionStatus.COMPLETE.getStatusCode());
		}else{
			if ( ! scheduleExecutionDetail.getSchedule().isAnalyticsTesting() ){
				scheduleExecution.setReportStatusRefId(ExecutionStatus.ERROR.getStatusCode());
			}
			scheduleExecution.setBrokenUrlReportStatusId(ExecutionStatus.ERROR.getStatusCode());
		}
		
		scheduleExecution.setBrokenUrlReportGenEndTime(DateTimeUtil.getCurrentDateTime());
		if ( ! scheduledService.updateScheduleExecution(scheduleExecution, Constants.BROKEN_URL_STATUS_END) ){
			logger.error("Exception while updating the ScheduleExcution for Broken Link Report Generation End Time.");
			status = false;
		}
		return status;
	}

	private boolean generateAnalyticsReport(
			ScheduleExecutionDetail scheduleExecutionDetail, String analyticsExcelFilePath, String analyticsObjectFilePath) {
		boolean status = true;
		
		try{
			ScheduleExecution scheduleExecution = scheduleExecutionDetail.getScheduleExecution();
			
			ScheduleExecution baselineScheduleExecution = new ScheduleExecution();
			
			String baselineExecutionDate = "";
			String currentRunExecutionDate = "";
			
			currentRunExecutionDate = DateTimeUtil.getDateFormatyyyyMMdd(scheduleExecution.getTestExecutionStartTime()) + 
					"_" + DateTimeUtil.getTimeInFormatHHSS(scheduleExecution.getTestExecutionStartTime());
			
			if ( null != scheduleExecution && null != scheduleExecution.getBaselineScheduleExecutionId() 
					&& scheduleExecution.getBaselineScheduleExecutionId() > 0 ){
				baselineScheduleExecution = scheduledService.getScheduleExecution(scheduleExecution.getBaselineScheduleExecutionId());
				
				baselineExecutionDate = DateTimeUtil.getDateFormatyyyyMMdd(baselineScheduleExecution.getTestExecutionStartTime()) + 
						"_" + DateTimeUtil.getTimeInFormatHHSS(baselineScheduleExecution.getTestExecutionStartTime());
			}else{
				baselineExecutionDate = currentRunExecutionDate;
			}
			
			String applicationName = scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getApplication().getApplicationName();
			//String environmentCategoryName = scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getEnvironment().getEnvironmentCategory().getEnvironmentCategoryName();
			
			String sheetName = applicationName;
			
			ScheduleDetails applicationConfig = new ScheduleDetails();
			applicationConfig.setApplicationName(scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getApplication().getApplicationName());
			String tempDirectory = configProperties.getMessage("mint.crawler.tempDirectory", null, Locale.getDefault());
			tempDirectory = FileDirectoryUtil.getAbsolutePath(tempDirectory, FileDirectoryUtil.getMintRootDirectory(configProperties));
			
			WebAnalyticsUtils wau = new WebAnalyticsUtils();
			
			Map<String, WebAnalyticsPageData> baseLinePageDataMap = null;
			
			logger.debug("*** Started Reading Current Run HAR reports for Analytics Audit. ****");
			Map<String, WebAnalyticsPageData> testPageDataMap = wau
					.getWebAnlyticsPageDataFromHarLogs(scheduleExecution.getHarReportsDirectory(), applicationName, getAnalyticsTagSignaturePath());
			logger.debug("*** Completed Reading Current Run HAR reports for Analytics Audit. ****");
			
			if ( null != baselineScheduleExecution.getScheduleExecutionId() && baselineScheduleExecution.getScheduleExecutionId() > 0 ){
				logger.debug("*** Started Reading Baseline HAR reports for Analytics Audit. ****");
				baseLinePageDataMap = wau
						.getWebAnlyticsPageDataFromHarLogs(baselineScheduleExecution.getHarReportsDirectory(), applicationName, getAnalyticsTagSignaturePath());
				logger.debug("*** Completed Reading Baseline HAR reports for Analytics Audit. ****");
			}else{
				baseLinePageDataMap = testPageDataMap;
			}
			
			List<SamePagesDataStore> samePagesDataStoreList = wau
					.normalizePageDataMaps(baseLinePageDataMap, testPageDataMap);
			boolean collectOnlyFailure = false;
			AnalyticsReportsSerializeUtils.writeAnalyticsSummaryDetails(samePagesDataStoreList, analyticsObjectFilePath, null, collectOnlyFailure); 
			
			AnalyticsDataReportingUtils excelWriter = new AnalyticsDataReportingUtils(
					analyticsExcelFilePath, sheetName);
			
			boolean beautify = true;
			excelWriter.writeAuditAndTestDetailsToExcel(samePagesDataStoreList,
					analyticsExcelFilePath, tempDirectory,beautify, baselineExecutionDate, currentRunExecutionDate, collectOnlyFailure);
			
			String analyticsFailureReportExcelFilePath = analyticsExcelFilePath.replace("MINT_ANALYTICS_REPORT_", "MINT_ANALYTICS_ERROR_REPORT_");
			
			collectOnlyFailure = true;
			AnalyticsReportsSerializeUtils.writeAnalyticsSummaryDetails(samePagesDataStoreList, null, analyticsObjectFilePath, collectOnlyFailure); 
			excelWriter.writeAuditAndTestDetailsToExcel(samePagesDataStoreList,
					analyticsFailureReportExcelFilePath, tempDirectory,beautify, baselineExecutionDate, currentRunExecutionDate, collectOnlyFailure);
			//Generate Analytics Summary Report for the current run.
			try{
				AnalyticsSummaryReport analyticsSummaryReport = generateAnalyticsSummaryView(testPageDataMap, scheduleExecution);
				
				analyticsSummaryReport.setTotalUrl(analyticsSummaryReport.getAllUrls().size());
				logger.info("AnalyticsSummaryReport :"+analyticsSummaryReport);
				
				/*for (Map.Entry<String, AnalyticsTagDetail> entry : analyticsSummaryReport.getEachAnalyticsTagMap().entrySet()) {
		            String key = entry.getKey();
		            AnalyticsTagDetail values = entry.getValue();
		            logger.info("Analytics Tag Name :" + key);
		            logger.info("Found in URL count :" + values.getTagPresentUrl().size());
		            logger.info("Not found in Url Count :"+values.getTagNotPresentUrl().size());
		            logger.info("Has error count :"+values.getUrlHasErrorTag().size());
		        }*/
				
				analyticsSummaryReport = updateAnalyticsTagNotFoundUrl(analyticsSummaryReport);
				
				JsonReaderWriter<AnalyticsSummaryReport> jsonReaderWriter = new JsonReaderWriter<AnalyticsSummaryReport>();
				jsonReaderWriter.writeJsonObjectToFile(analyticsSummaryReport, scheduleExecution.getReportsDirectory() + File.separator + UiTestingConstants.ANALYTICS_SUMMARY_REPORTS);
				
				/*logger.info("After AnalyticsSummaryReport :"+analyticsSummaryReport);
				
				for (Map.Entry<String, AnalyticsTagDetail> entry : analyticsSummaryReport.getEachAnalyticsTagMap().entrySet()) {
		            String key = entry.getKey();
		            AnalyticsTagDetail values = entry.getValue();
		            logger.info("After Analytics Tag Name :" + key);
		            logger.info("After Found in URL count :" + values.getTagPresentUrl().size());
		            logger.info("After Not found in Url Count :"+values.getTagNotPresentUrl().size());
		            logger.info("After Has error count :"+values.getUrlHasErrorTag().size());
		        }*/
				
			}catch(Exception e){
				logger.error("Exception while generating Analytics Summary view.");
				logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			}
		}catch(Exception e){
			logger.error("Exception while generating Analytics Reports");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			status = false;
		}
		
		return status;
	}

	private AnalyticsSummaryReport updateAnalyticsTagNotFoundUrl(
			AnalyticsSummaryReport analyticsSummaryReport) {
		for (String url : analyticsSummaryReport.getAllUrls()) {
			for (Map.Entry<String, AnalyticsTagDetail> entry : analyticsSummaryReport.getEachAnalyticsTagMap().entrySet()) {
				AnalyticsTagDetail values = entry.getValue();
				if ( ! values.getTagPresentUrl().contains(url)){
					values.getTagNotPresentUrl().add(url);
				}
			}
		}
		return analyticsSummaryReport;
	}

	private AnalyticsSummaryReport generateAnalyticsSummaryView(
			Map<String, WebAnalyticsPageData> pageDataMap,
			ScheduleExecution scheduleExecution) {
		logger.info("Total Url Count :"+pageDataMap.size());
		AnalyticsSummaryReport analyticsSummaryReport = new AnalyticsSummaryReport();
		
		Iterator urlIterator = pageDataMap.keySet().iterator();
		String urlkey;

		while (urlIterator.hasNext()) {
			urlkey = urlIterator.next().toString();
			
			WebAnalyticsPageData pageData = pageDataMap
					.get(urlkey);

			pageData
					.getApplicationName();
			pageData.getPageUrl();
			pageData.getPageTitle();
			pageData
					.getHarLogFileName();

			//Adding all the URLs
			analyticsSummaryReport.getAllUrls().add(pageData.getPageUrl());

			Map<String, WebAnalyticsTagData> tagData = pageData
					.getWebAnalyticsTagDataMap();
			
			Iterator tagDataIterator = tagData.keySet().iterator();
			
			String tagDataKey;
			while (tagDataIterator.hasNext()) {
				tagDataKey = tagDataIterator.next().toString();
				
				WebAnalyticsTagData webAnalyticsTagData = tagData.get(tagDataKey);
				
				if ( null == analyticsSummaryReport.getEachAnalyticsTagMap().get(webAnalyticsTagData.getTagName()) ){
					AnalyticsTagDetail analyticsTagDetail = new AnalyticsTagDetail();
					
					analyticsSummaryReport.getEachAnalyticsTagMap().put(webAnalyticsTagData.getTagName(), analyticsTagDetail);
				}
				
				AnalyticsTagDetail analyticsTagDetail = analyticsSummaryReport.getEachAnalyticsTagMap().get(webAnalyticsTagData.getTagName());
				
				//Adding to set so don't take duplicates
				analyticsTagDetail.getTagPresentUrl().add(pageData.getPageUrl());
				
				
				DetailedViewTags entrylist = new DetailedViewTags();
				entrylist.setTagtype(webAnalyticsTagData.getTagName());
				entrylist.setTagUrl(webAnalyticsTagData.getTagUrl() );
				entrylist.setTagDataKey(webAnalyticsTagData.getTagDataKey());
				entrylist.setStartedDateTime(webAnalyticsTagData.getStartedDateTime());
				
				Map<String, String> tagname = webAnalyticsTagData.getTagVariableData();
				Iterator it1 = tagname.keySet().iterator();
				while (it1.hasNext()) {
					
					String data=it1.next().toString();
					
					TagVariablesData tagVarNameValue = new TagVariablesData();
					tagVarNameValue.setName(data);
					tagVarNameValue.setValue(tagname.get(data));
					
					entrylist.getTagVariablesData().add(tagVarNameValue);
					
					if(tagname.equals("SiteCatalyst") && tagVarNameValue.getName().equals("pageName")){
						if(tagVarNameValue.getValue().startsWith(":") 
								|| tagVarNameValue.getValue().endsWith(":") 
								|| tagVarNameValue.getValue().equals("null") 
								|| tagVarNameValue.getValue().equals("undefined")
								|| tagVarNameValue.getValue().equals(null)
								|| tagVarNameValue.getValue().equals("NoTagVarValueFound")
								|| tagVarNameValue.getValue().equals("")){
							analyticsTagDetail.getUrlHasErrorTag().add(pageData.getPageUrl());
						}
						
					} else if(
							tagVarNameValue.getValue().equals("undefined") ||
							tagVarNameValue.getValue().equals("null") ||
							//baseLineTagVarValue.equals("NoTagVarValueFound") ||
							tagVarNameValue.getValue().equals("")){
						analyticsTagDetail.getUrlHasErrorTag().add(pageData.getPageUrl());
					}
					
				}
			}
		}
		
		return analyticsSummaryReport;
		
	}

	private boolean generateBrokenUrlReport(
			ScheduleExecutionDetail scheduleExecutionDetail, String brokenUrlReportFilePath, String brokenUrlWithResourceReportFilePath, String brokenUrlReportPageLoadAttributesFilePath) {
		boolean status = true;
		try{
			boolean collectAllUrls = true;
			boolean isBaselineCompare = false;
			ScheduleExecution scheduleExecution = scheduleExecutionDetail.getScheduleExecution();
			ScheduleExecution baseLineScheduleExecution = null;
			List<SamePagesBrokenUrlDataStore> samePagesBrokenUrlDataStore = null;
			List<Link> navigationList = new ArrayList<Link>();
			List<SuitBrokenReportsXref>  suitBrokenXrefList = scheduleExecutionDetail.getSuitBrokenTypeXref();
			
			if(scheduleExecution.getBaselineScheduleExecutionId() != null) {
				isBaselineCompare = true;
				baseLineScheduleExecution = scheduledService.getScheduleExecution(scheduleExecution.getBaselineScheduleExecutionId());
			}
			
			String applicationName = scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getApplication().getApplicationName();
			String environmentCategoryName = scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getEnvironment().getEnvironmentCategory().getEnvironmentCategoryName();
			
			boolean generateReportsOnlyForRefererUrl = false;
			boolean generatePageloadAttributes = false;
			boolean generateBrokenLinkOnly = false;
			boolean generateBrokenLinkWithResources = false;
			AnalyticsDataReportingUtils excelWriter = new AnalyticsDataReportingUtils(brokenUrlReportFilePath,"BrokenUrlData");
			WebAnalyticsUtils wau= new WebAnalyticsUtils();
			
			CrawlConfig crawlconfig = null;
			
			String tempDirectory = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.crawler.tempDirectory");
			tempDirectory = FileDirectoryUtil.getAbsolutePath(tempDirectory, FileDirectoryUtil.getMintRootDirectory(configProperties));
			
			try{
				JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();
				crawlconfig = jsonReaderWriter.readJsonObjectFromFile(new CrawlConfig(), scheduleExecution.getCrawlStatusDirectory()+ File.separator + UiTestingConstants.CRAWL_CONFIG);
			}catch(Exception e){
				logger.warn(UiTestingConstants.CRAWL_CONFIG + ", is not available yet for the ScheduleExecutionId :"+scheduleExecution.getScheduleExecutionId());
			}
			
			/*
			logger.info("brokenUrlReportFilePath :"+brokenUrlReportFilePath);
			wau.generateBrokenUrlReport(wau, scheduleExecution.getHarReportsDirectory(), applicationName, brokenUrlReportResourceFilePath, excelWriter, applicationName, 
					collectAllUrls, tempDirectory, crawlconfig.getCrawlStatus().getNavigationList(), generateReportsOnlyForRefererUrl);
			*/
			
			Map<String, BrokenUrlData> brokenUrlDataMap = new LinkedHashMap<String, BrokenUrlData>();
			Map<String, BrokenUrlData> baselineBrokenUrlDataMap = new LinkedHashMap<String, BrokenUrlData>();
			
			generateReportsOnlyForRefererUrl = true;
			
			for( SuitBrokenReportsXref reportType : suitBrokenXrefList ){
				logger.info("reportType.getReportName() :"+reportType.getReportName());
				if ( BrokenReportSelection.LoadTimeAttribute.equals(reportType.getReportName())){
					generatePageloadAttributes = true;
				} else if ( BrokenReportSelection.BrokenLink.equals(reportType.getReportName())){
					generateBrokenLinkOnly = true;
				} else if ( BrokenReportSelection.BrokenLinkResources.equals(reportType.getReportName())){
					generateBrokenLinkWithResources = true;
				}
			}
			
			excelWriter = new AnalyticsDataReportingUtils(brokenUrlReportFilePath,"BrokenUrlReport");
			boolean beautify = true;
			
			if(isBaselineCompare && generatePageloadAttributes) {
				logger.debug("*** Started Generating page load time reports with baseline. ***");
				logger.info("brokenUrlReportPageLoadAttributesFilePath :"+brokenUrlReportPageLoadAttributesFilePath);
				baselineBrokenUrlDataMap = wau.getBrokenUrlDataFromHarLogs(baseLineScheduleExecution.getHarReportsDirectory(), applicationName, collectAllUrls, navigationList, generateReportsOnlyForRefererUrl);
				samePagesBrokenUrlDataStore = wau.normalizeBrokenUrlDataMap(brokenUrlDataMap, baselineBrokenUrlDataMap);
				AnalyticsDataReportingUtils adru = new AnalyticsDataReportingUtils();
				adru.writeBrokenUrlAuditDetailsToExcel(samePagesBrokenUrlDataStore, brokenUrlReportPageLoadAttributesFilePath, tempDirectory);
				logger.debug("*** Completed Generating page load time reports with baseline. ***");
			} 
			
			//  for report with page load attributes (if selected by user in suit) 
			if(generatePageloadAttributes) {
				logger.debug("*** Started Generating page load time reports. ***");
				logger.info("brokenUrlReportPageLoadAttributesFilePath :"+brokenUrlReportPageLoadAttributesFilePath);
				wau.generateBrokenUrlReport(wau, scheduleExecution.getHarReportsDirectory(), applicationName, brokenUrlReportPageLoadAttributesFilePath, excelWriter, applicationName, collectAllUrls, tempDirectory, 
					crawlconfig.getCrawlStatus().getNavigationList(), generateReportsOnlyForRefererUrl, generatePageloadAttributes, beautify);
				logger.debug("*** Completed Generating page load time reports. ***");
			}
			
			// 	 for report with broken url (if selected by user in suit) 
			if(generateBrokenLinkOnly) {
				logger.debug("*** Started Generating Broken Url reports. ***");
				logger.debug("Broken Url reports file path :"+brokenUrlReportFilePath);
				wau.generateBrokenUrlReport(wau, scheduleExecution.getHarReportsDirectory(), applicationName, brokenUrlReportFilePath, excelWriter, applicationName, collectAllUrls, tempDirectory, 
						crawlconfig.getCrawlStatus().getNavigationList(), generateReportsOnlyForRefererUrl, false, beautify);
			}
			
			if ( generateBrokenLinkWithResources ){
				logger.debug("Broken Url reports with resources file path :"+brokenUrlWithResourceReportFilePath);
				generateReportsOnlyForRefererUrl = false;
				wau.generateBrokenUrlReport(wau, scheduleExecution.getHarReportsDirectory(), applicationName, brokenUrlWithResourceReportFilePath, excelWriter, applicationName, collectAllUrls, tempDirectory, 
						crawlconfig.getCrawlStatus().getNavigationList(), generateReportsOnlyForRefererUrl, false, beautify);
				
				logger.debug("*** Completed Generating Broken Url reports. ***");
			}
		    
		}catch(Exception e){
			logger.error("Exception while generating the Broken Url Reprots.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			status = false;
		}

		return status;
	}
	
	private String getAnalyticsTagSignaturePath() {
		String analyticsTagSignatureFilePath = configProperties.getMessage("mint.analytics.tag.file.Path", null, Locale.getDefault());
		analyticsTagSignatureFilePath = FileDirectoryUtil.getAbsolutePath(analyticsTagSignatureFilePath, FileDirectoryUtil.getMintRootDirectory(configProperties));
		return analyticsTagSignatureFilePath;
	}
	
	private List<String> getImagesFileTypes() {
		List<String> imageFileTypeList = new ArrayList<String>();
		try {
			String imagesFileTypes = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.image.file.type");
			if(StringUtils.isNotBlank(imagesFileTypes)) {
				imagesFileTypes = imagesFileTypes.toLowerCase();
				imageFileTypeList = CommonUtils.getStringAsList(imagesFileTypes);
			}
		} catch (Exception e){
			logger.error("Exception occured while getting image file types from properties file");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		return imageFileTypeList;
	}
	
	private List<String> getImagesToSkip(){
		List<String> imageFilesToSkip = new ArrayList<String>();

		try {
			String imagesToSkip = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.skip.download.images");
			if(StringUtils.isNotBlank(imagesToSkip)) {
				imageFilesToSkip = CommonUtils.getStringAsList(imagesToSkip);
			}
		} catch (Exception e){
			logger.error("Exception occured while getting image files for skipping from properties file");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		return imageFilesToSkip;
	}
	
	private List<String> getTiaaImageNameList(){
		List<String> tiaaImageFiles = new ArrayList<String>();
		
		try {
			String tiaaImages = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.tiaacref.images");
			if(StringUtils.isNotBlank(tiaaImages)) {
				tiaaImageFiles = CommonUtils.getStringAsList(tiaaImages);
			}
		} catch (Exception e){
			logger.error("Exception occured while getting tiaa cref image names from properties file");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		return tiaaImageFiles;
	}
	
	private List<String> getTextToFindList(){
		List<String> textToFindCountList = new ArrayList<String>();
		
		try {
			String textToFindCount = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.text.to.find.count");
			if(StringUtils.isNotBlank(textToFindCount)) {
				textToFindCountList = CommonUtils.getStringAsList(textToFindCount);
			}
		} catch (Exception e){
			logger.error("Exception occured while getting text to find count details from properties file");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		return textToFindCountList;
	}
	
	private String addReportSummaryToMailContent(
			ScheduleExecution scheduleExecution, String emailContent) {
		String analyticSummary = "";
		String analyticSummaryError = "";
		String suitName = "";
		Suit suit = null;
		Schedule schedule = null;
		List<AnalyticsSummaryReportUi> analyticsSummaryData = null;
		List<AnalyticsDetails> analyticsDetailsUiList = new ArrayList<AnalyticsDetails>();
		try {
			analyticsSummaryData = homeService
					.getAnalyticsSummaryReportData(scheduleExecution
							.getScheduleExecutionId());
			schedule = scheduledService.getSchedule(scheduleExecution.getScheduleId());
			suit = homeService.getSavedSuits(schedule.getSuitId());
			
			if (analyticsSummaryData != null && analyticsSummaryData.size() > 0) {
				analyticSummary = analyticSummary + "<table style='display: table; padding: 5px 15px;'>";
				analyticSummary = analyticSummary + "<tr><td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>TAG NAME</td>";
				analyticSummary = analyticSummary + "<td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>#PAGES AUDITED</td>";
				analyticSummary = analyticSummary + "<td align='center'style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>#PAGES WITH TAG</td>";
				//analyticSummary = analyticSummary + "<td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>TAG NOT FOUND URL COUNT</td>";
				analyticSummary = analyticSummary + "<td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>#PAGES WITH ERROR</td></tr>";
				for (AnalyticsSummaryReportUi analytics : analyticsSummaryData) {
					if(analytics.getTagName().equals("Adobe Analytics")){
						analyticSummary = analyticSummary + "<tr><td align='center'>" + analytics.getTagName() + "</td>";
						analyticSummary = analyticSummary + "<td align='center'>" + analytics.getTotalUrl() + "</td>";
						analyticSummary = analyticSummary + "<td align='center'>" + analytics.getTagPresentUrlCount() + "</td>";
						analyticSummary = analyticSummary + "<td align='center'>" + analytics.getTagHasErrorUrlCount() + "</td></tr>";	
					}
				}
				for (AnalyticsSummaryReportUi analytics : analyticsSummaryData) {
					if(analytics.getTagName().equals("TIAA-CREF Lazy JS")){
						analyticSummary = analyticSummary + "<tr><td align='center'>" + analytics.getTagName() + "</td>";
						analyticSummary = analyticSummary + "<td align='center'>" + analytics.getTotalUrl() + "</td>";
						analyticSummary = analyticSummary + "<td align='center'>" + analytics.getTagPresentUrlCount() + "</td>";
						analyticSummary = analyticSummary + "<td align='center'>" + analytics.getTagHasErrorUrlCount() + "</td></tr>";	
					}
				}
				for (AnalyticsSummaryReportUi analytics : analyticsSummaryData) {
					if(!analytics.getTagName().equals("Adobe Analytics")){
						if(!analytics.getTagName().equals("TIAA-CREF Lazy JS")) {
							analyticSummary = analyticSummary + "<tr><td align='center'>" + analytics.getTagName() + "</td>";
							analyticSummary = analyticSummary + "<td align='center'>" + analytics.getTotalUrl() + "</td>";
							analyticSummary = analyticSummary + "<td align='center'>" + analytics.getTagPresentUrlCount() + "</td>";
							analyticSummary = analyticSummary + "<td align='center'>" + analytics.getTagHasErrorUrlCount() + "</td></tr>";
						}
					}
				}

				analyticSummary = analyticSummary + "</table>";
				
				// Appending Analytics error report details in mail content
				
				try{
					analyticSummaryError = analyticSummaryError + "<br>";
					analyticsDetailsUiList = homeService.getAnalyticsDetailsReportData(Integer.valueOf(scheduleExecution.getScheduleId()),"Error");
					if (analyticsDetailsUiList != null && analyticsDetailsUiList.size() > 0) {
						analyticSummaryError = analyticSummaryError + "<table style='display: table; padding: 5px 15px;'>";
						analyticSummaryError = analyticSummaryError + "<tr><td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>PAGE URL</td>";
						analyticSummaryError = analyticSummaryError + "<td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>TAG TYPE</td>";
						analyticSummaryError = analyticSummaryError + "<td align='center'style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>TAG VARIABLE NAME</td>";
						analyticSummaryError = analyticSummaryError + "<td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>TAG VARIABLE VALUE</td></tr>";
					for (AnalyticsDetails analytic : analyticsDetailsUiList) {
						analyticSummaryError = analyticSummaryError + "<tr><td align='center'>" + analytic.getCurrentRunPageUrl() + "</td>";
						analyticSummaryError = analyticSummaryError + "<td align='center'>" + analytic.getExpectedTagName() + "</td>";
						analyticSummaryError = analyticSummaryError + "<td align='center'>" + analytic.getExpectedTagVarName() + "</td>";
						analyticSummaryError = analyticSummaryError + "<td align='center'>" + analytic.getExpectedTagVarValue() + "</td></tr>";
					}
					analyticSummaryError = analyticSummaryError + "</table>";
					}
					
					analyticSummary = analyticSummary + analyticSummaryError;
				} catch (Exception ex) {
					logger.error("Unable to add report error content to mail");
				}	
				
				suitName = suit.getSuitName();
			}
			
		} catch (Exception ex) {
			logger.error("Unable to add report summary content to mail");
		}
		emailContent = emailContent.replace(SCHEDULER.REPORT_SUMMARY_PLACE_HOLDER, analyticSummary);
		emailContent = emailContent.replace(SCHEDULER.SUIT_NAME_PLACE_HOLDER, suitName);
		logger.info("emailContent :" + emailContent);
		return emailContent;
	}
	

	
	private  List<Pair> generateTextAndImageBaselineReport(ScheduleExecutionDetail scheduleExecutionDetail, String path){
        List<Link> navigationDetails = new ArrayList<Link>();
        List<Link> baseNavigationDetails = new ArrayList<Link>();
        List<Pair> matchingList =  null;
        try{
               if ( null != scheduleExecutionDetail.getScheduleExecution().getCrawlStatusDirectory() ){
            	   	navigationDetails = ReportUtil.getNavigationList(scheduleExecutionDetail.getScheduleExecution().getCrawlStatusDirectory()+ File.separator + UiTestingConstants.CRAWL_CONFIG);
                    logger.info("navigationDetails :"+navigationDetails);
                   
                    ScheduleExecution  baselineExecution =  scheduledService.getScheduleExecution(scheduleExecutionDetail.getSchedule().getBaselineScheduleExecutionId() );
                    baseNavigationDetails =  ReportUtil.getNavigationList(baselineExecution.getCrawlStatusDirectory()+ File.separator + UiTestingConstants.CRAWL_CONFIG);
                    logger.info("baseNavigationDetails :"+baseNavigationDetails);
                	matchingList = ReportUtil.getMatchingPair(baseNavigationDetails, navigationDetails, false);
                    
               }
               if (null != matchingList && matchingList.size() > 0 ){
            	   
            	   	String tempDirectory = configProperties.getMessage("mint.crawler.tempDirectory", null, Locale.getDefault());
            	   	tempDirectory = FileDirectoryUtil.getAbsolutePath(tempDirectory, FileDirectoryUtil.getMintRootDirectory(configProperties));
                    logger.info("*** Started generating TEXT and Image Data Excel Reprot.");
                    boolean writeBaselineData = true;
                    ExcelWriter.writeTextOrImageReportDataToExcel(matchingList, path, tempDirectory, writeBaselineData);
                    logger.info("*** Completed generating TEXT and Image Data Excel Report.");
               }
        }catch(Exception e){
               logger.error(UiTestingConstants.CRAWL_CONFIG + ", is not available yet for the ScheduleExecutionId :"+scheduleExecutionDetail.getScheduleExecution().getScheduleExecutionId());
               e.printStackTrace();
        }
        return matchingList;
        
	}
}
