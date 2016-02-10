package com.ensense.insense.services.scheduler;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cts.mint.common.JsonReaderWriter;
import com.cts.mint.common.MintFileUtils;
import com.cts.mint.common.model.Link;
import com.cts.mint.common.model.PartialText;
import com.cts.mint.common.service.HomeService;
import com.cts.mint.common.utils.CommonUtils;
import com.cts.mint.common.utils.Constants;
import com.cts.mint.common.utils.ExecutionStatus;
import com.cts.mint.common.utils.FileDirectoryUtil;
import com.cts.mint.common.utils.ImageCompare;
import com.cts.mint.common.utils.LevenshteinDistanceCalculator;
import com.cts.mint.common.utils.TextCompareUtils;
import com.cts.mint.common.utils.Constants.FileSuitSchedulerJob;
import com.cts.mint.common.utils.Constants.SCHEDULER;
import com.cts.mint.crawler.CrawlerSetup;
import com.cts.mint.crawler.model.CompareConfig;
import com.cts.mint.crawler.model.CrawlConfig;
import com.cts.mint.crawler.model.UiReportsSummary;
import com.cts.mint.crawler.model.UrlForm;
import com.cts.mint.crawler.model.UrlFormElement;
import com.cts.mint.dom.comparator.ApplicationComparator;
import com.cts.mint.reports.service.ScheduledService;
import com.cts.mint.uitesting.entity.Schedule;
import com.cts.mint.uitesting.entity.ScheduleExecution;
import com.cts.mint.uitesting.entity.Suit;
import com.cts.mint.uitesting.model.CompareLink;
import com.cts.mint.uitesting.model.ScheduleExecutionDetail;
import com.cts.mint.uitesting.model.UiTestingConstants;
import com.cts.mint.util.DateTimeUtil;
import com.cts.mint.util.EmailUtil;
import com.cts.mint.util.DiffMatchPatch.Diff;
import com.cts.mint.util.ExcelWriter;

@Service
public class ComparisonScheduler {
	private static Logger logger = Logger.getLogger(ComparisonScheduler.class);
	private static int NO_OF_TIMES_WAIT_FOR_CONFIG = 10;

	@Autowired
	private MessageSource schedulerProperties;
	
	@Autowired
	ScheduledService scheduledService;

	@Autowired
	private MessageSource configProperties;
	
	@Autowired
	private HomeService homeService;
	
	@Scheduled(fixedDelayString = "${mint.scheduler.uischedule.compare.delaytime}")
	public void executePendingImageComparison() {
		try {
			if (! Boolean.parseBoolean(CommonUtils.getPropertyFromPropertyFile(
					schedulerProperties, SCHEDULER.UI_COMPARE_SCHEDULER_ENABLE))) {
				
				//Schedule Job is disabled. Do nothing.
				return;
			}
		} catch (Exception e) {
			logger.error("Unable to start the UI Compare Scheduler.");
			e.printStackTrace();
		}

		ScheduleExecutionDetail scheduleExecutionDetail = scheduledService.getPendingImageComparison();
		ScheduleExecution baselineScheduleExcecution = new ScheduleExecution();
		List<Link> baselineLinks = new ArrayList<Link>();

		if (scheduleExecutionDetail.getScheduleExecution() != null
				&& scheduleExecutionDetail.getScheduleExecution()
						.getScheduleExecutionId() > 0) {
			
			ScheduleExecution scheduleExecution = scheduleExecutionDetail
					.getScheduleExecution();
			
			baselineScheduleExcecution = scheduledService
					.getScheduleExecution(scheduleExecution.getBaselineScheduleExecutionId());
			
			

			boolean isBaselineFoundForCompare = false;
			boolean isCurrenFoundForCompare = false;

			if (!isBaselineScheduleExecutionExists(baselineScheduleExcecution)) {
				// If no baseline found, make the comparison status for the
				// schedule execution as not applicable.
				// updateScheduleExecution(scheduleExecution);
			} else {
				isBaselineFoundForCompare = true;
			}

			if (isBaselineFoundForCompare) {
				// check baseline Crawlconfig exists.
				isBaselineFoundForCompare = checkBaselineWithCrawlConfigExists(baselineScheduleExcecution);
			}

			if (isBaselineFoundForCompare) {
				baselineLinks = checkImageCompareBaselineLinks(baselineScheduleExcecution,baselineLinks);
				logger.info("Image compare baselineLinks :" + baselineLinks);
			}

			if (isBaselineFoundForCompare) {
				isCurrenFoundForCompare = checkCurrentRunFileExists(scheduleExecution);
			}

			int imageComparisonStatus = ExecutionStatus.IN_PROGRESS
					.getStatusCode();
			
			if (isBaselineFoundForCompare && isCurrenFoundForCompare) {
				startImageCompare(ExecutionStatus.IN_PROGRESS.getStatusCode(),
						scheduleExecution);

				CompareConfig compareConfig = new CompareConfig();

				compareConfig = UpdatingCompareStartingStatus(scheduleExecution);

				imageComparisonStatus = toReadCrawlConfig(scheduleExecution,imageComparisonStatus);

				// Process comparison
				if ( imageComparisonStatus == ExecutionStatus.IN_PROGRESS.getStatusCode() ){
					doImageComparison(compareConfig, baselineLinks, scheduleExecution);
				}

				compareConfig
						.setLastUpdatedTime(DateTimeUtil
								.getCurrentDateTimeAsString(DateTimeUtil.DATE_TIME_FORMAT));
				
				if(updatingCompareconfig(compareConfig, scheduleExecution)){
					if ( imageComparisonStatus == ExecutionStatus.IN_PROGRESS.getStatusCode() ){
						imageComparisonStatus = ExecutionStatus.COMPLETE.getStatusCode();
					}	
				}
				logger.info("Image comparison Completed");
			}else{
				imageComparisonStatus = ExecutionStatus.NOT_APPLICABLE.getStatusCode();
			}
			//Update Image Comparison status.
			completeImageCompare(imageComparisonStatus, scheduleExecution);
		}
	}
	
	private boolean checkBaselineWithCrawlConfigExists(ScheduleExecution baselineScheduleExcecution) {
		boolean isBaselineFoundForCompare = false;
		try {
			isBaselineFoundForCompare = MintFileUtils
					.isFileExists(baselineScheduleExcecution
							.getCrawlStatusDirectory()
							+ File.separator
							+ UiTestingConstants.CRAWL_CONFIG);
		} catch (Exception e1) {
			logger.error("Exception while reading Baseline CrawlConfig file.");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(e1));
		}
		return isBaselineFoundForCompare;
	}
	
	private List<Link> checkImageCompareBaselineLinks(ScheduleExecution baselineScheduleExcecution,List<Link> baselineLinks){
		CrawlConfig crawlConfig = new CrawlConfig();
		JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();

		try {
			crawlConfig = jsonReaderWriter.readJsonObjectFromFile(
					new CrawlConfig(),
					baselineScheduleExcecution
							.getCrawlStatusDirectory()
							+ File.separator
							+ UiTestingConstants.CRAWL_CONFIG);
			baselineLinks = crawlConfig.getCrawlStatus()
					.getNavigationList();

			//logger.info("Image compare baselineLinks :" + baselineLinks);

		} catch (Exception e) {
			logger.error("Exception while reading baseline CrawlConfig object.");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(e));
		}
		return baselineLinks;
	}
	private CompareConfig UpdatingCompareStartingStatus(ScheduleExecution scheduleExecution){
		CompareConfig compareConfig = new CompareConfig();
		JsonReaderWriter<CompareConfig> jsonReaderWriterCompareConfig = new JsonReaderWriter<CompareConfig>();
		try {
			
			compareConfig = jsonReaderWriterCompareConfig.readJsonObjectFromFile(
					new CompareConfig(),
					scheduleExecution
							.getCrawlStatusDirectory()
							+ File.separator
							+ UiTestingConstants.COMPARE_CONFIG);
			
			if ( null != compareConfig.getCompareLinkList() )
				logger.info("No of file for Image/HTML comparison :"+compareConfig.getCompareLinkList().size());
		} catch (Exception e) {
			logger.error("Exception while Updating CompareStarting Status.");
		}
		return compareConfig;
	}
	
	private int toReadCrawlConfig(ScheduleExecution scheduleExecution, int imageComparisonStatus){
		CrawlConfig currentRunCrawlConfig = new CrawlConfig();
		JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();
		try {
			currentRunCrawlConfig = jsonReaderWriter
					.readJsonObjectFromFile(new CrawlConfig(),
							scheduleExecution.getCrawlStatusDirectory()
									+ File.separator
									+ UiTestingConstants.CRAWL_CONFIG);

			if (null != currentRunCrawlConfig
					&& null != currentRunCrawlConfig.getCrawlStatus()
							.getNavigationList()) {

				// If CrawlConfig found read NavigationLinks and add the
				// CompareLink to InProgressComparison.obj file. If
				// InProgressComparison.obj not found create new one.
				// logger.info("currentRunLink :"+currentRunLink);
			} else {
				imageComparisonStatus = ExecutionStatus.FAILED
							.getStatusCode();
			}

		} catch (Exception e) {
			logger.warn("Unable to read CrawlConfig object.");
			imageComparisonStatus = ExecutionStatus.FAILED
					.getStatusCode();
		}
		
		return imageComparisonStatus;
	}
	
	private boolean updatingCompareconfig(CompareConfig compareConfig,ScheduleExecution scheduleExecution){
		JsonReaderWriter<CompareConfig> jsonReaderWriterCompareConfig = new JsonReaderWriter<CompareConfig>();
		boolean isUpdate = false;
		try {
			jsonReaderWriterCompareConfig.writeJsonObjectToFile(
					compareConfig,
					scheduleExecution.getCrawlStatusDirectory()
							+ File.separator
							+ UiTestingConstants.COMPARE_CONFIG);
			isUpdate = true;
		} catch (Exception e) {
			logger.error("Exception while updating Compare config for Image/HTML compare");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(e));
		}
		
		return isUpdate;
	}
	
	@Scheduled(fixedDelayString = "${mint.scheduler.uischedule.compare.delaytime}")
	public void executePendingHtmlComparison() {
		try {
			if (! Boolean.parseBoolean(CommonUtils.getPropertyFromPropertyFile(
					schedulerProperties, SCHEDULER.UI_COMPARE_SCHEDULER_ENABLE))) {
				
				//Schedule Job is disabled. Do nothing.
				return;
			}
		} catch (Exception e) {
			logger.error("Unable to start the UI Compare Scheduler.");
			e.printStackTrace();
		}

		ScheduleExecutionDetail scheduleExecutionDetail = scheduledService
				.getPendingHtmlComparison();
		ScheduleExecution baselineScheduleExcecution = new ScheduleExecution();
		List<Link> baselineLinks = new ArrayList<Link>();

		if (scheduleExecutionDetail.getScheduleExecution() != null
				&& scheduleExecutionDetail.getScheduleExecution()
						.getScheduleExecutionId() > 0) {
			ScheduleExecution scheduleExecution = scheduleExecutionDetail
					.getScheduleExecution();
			logger.info("Schedule found for HTML Compare, scheduleExecution->"+scheduleExecution);
			baselineScheduleExcecution = scheduledService
					.getScheduleExecution(scheduleExecution.getBaselineScheduleExecutionId());

			boolean isBaselineFoundForCompare = false;
			boolean isCurrenFoundForCompare = false;

			if (!isBaselineScheduleExecutionExists(baselineScheduleExcecution)) {
				// If no baseline found, make the comparison status for the
				// schedule execution as not applicable.
				// updateScheduleExecution(scheduleExecution);
			} else {
				isBaselineFoundForCompare = true;
			}

			if (isBaselineFoundForCompare) {
				// check baseline Crawlconfig exists.
				isBaselineFoundForCompare = checkBaselineWithCrawlConfigExists(baselineScheduleExcecution);
			}

			if (isBaselineFoundForCompare) {
				baselineLinks = checkImageCompareBaselineLinks(baselineScheduleExcecution,baselineLinks);
				logger.info("HTML compare baselineLinks :" + baselineLinks);
			}

			if (isBaselineFoundForCompare) {
				isCurrenFoundForCompare = checkCurrentRunFileExists(scheduleExecution);
			}

			logger.info("isCurrenFoundForCompare :"+isCurrenFoundForCompare);
			
			logger.info("isBaselineFoundForCompare :"+isBaselineFoundForCompare);
			
			int htmlComparisonStatus = ExecutionStatus.IN_PROGRESS
					.getStatusCode();
			
			if (isBaselineFoundForCompare && isCurrenFoundForCompare) {
				startHtmlCompare(ExecutionStatus.IN_PROGRESS.getStatusCode(),
						scheduleExecution);

				CompareConfig compareConfig = new CompareConfig();

				compareConfig = UpdatingCompareStartingStatus(scheduleExecution);

				htmlComparisonStatus = toReadCrawlConfig(scheduleExecution,htmlComparisonStatus);

				// Process comparison
				if ( htmlComparisonStatus == ExecutionStatus.IN_PROGRESS.getStatusCode()){
					doHtmlComparison(compareConfig, baselineLinks, scheduleExecution);
				}

				compareConfig
						.setLastUpdatedTime(DateTimeUtil
								.getCurrentDateTimeAsString(DateTimeUtil.DATE_TIME_FORMAT));
				if(updatingCompareconfig(compareConfig, scheduleExecution)){
					if ( htmlComparisonStatus == ExecutionStatus.IN_PROGRESS.getStatusCode()){
						htmlComparisonStatus = ExecutionStatus.COMPLETE.getStatusCode();
					}					
				}
				logger.info("Html comparison Completed");
				
				// Form elements comparison
				compareFormElements (scheduleExecution, baselineScheduleExcecution);
				
				
			}else{
				htmlComparisonStatus = ExecutionStatus.NOT_APPLICABLE.getStatusCode();
			}
			//Update Html Compare status.
			completeHtmlCompare(htmlComparisonStatus, scheduleExecution);
			
			logger.info("Update Html comparison Completed");
		}
	}

	private void compareFormElements(ScheduleExecution scheduleExecution,
			ScheduleExecution baselineScheduleExcecution) {
		
		CrawlConfig crawlConfig = new CrawlConfig();
		CrawlConfig baseLineCrawlConfig = new CrawlConfig();
		JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();
		boolean matched = false;
		try {
			baseLineCrawlConfig = jsonReaderWriter.readJsonObjectFromFile(
					new CrawlConfig(),
					baselineScheduleExcecution
							.getCrawlStatusDirectory()
							+ File.separator
							+ UiTestingConstants.CRAWL_CONFIG);
			
			crawlConfig = jsonReaderWriter.readJsonObjectFromFile(
					new CrawlConfig(),
					scheduleExecution
							.getCrawlStatusDirectory()
							+ File.separator
							+ UiTestingConstants.CRAWL_CONFIG);
			
			List<UrlFormElement> currentFomElements = crawlConfig.getUrlFormElementList();
			List<UrlFormElement> baseFormElements = baseLineCrawlConfig.getUrlFormElementList();
			for(UrlFormElement currentUrlFormElement :currentFomElements ){
				for(UrlFormElement baselineUrlFormElement :baseFormElements ){
					if(currentUrlFormElement.getUrl().equalsIgnoreCase(baselineUrlFormElement.getUrl())) {
							matched = currentUrlFormElement.equals(baselineUrlFormElement);
					}
				}
				
			}

			logger.info("UI Form Elements matched :" + matched);

		} catch (Exception e) {
			logger.error("Exception while reading baseline CrawlConfig object.");
			logger.error("Stack Trace :"
					+ ExceptionUtils.getStackTrace(e));
		}
	
	}

	private void compareUiForms(UrlFormElement currentUrlFormElement,
			UrlFormElement baselineUrlFormElement) {
		for(UrlForm urlForm : currentUrlFormElement.getUrlForms()) {
			
		}
	}

	private void doImageComparison(CompareConfig compareConfig,
			List<Link> baselineLinks, ScheduleExecution scheduleExecution) {
		List<CompareLink> compareLinkList = compareConfig.getCompareLinkList();
		int htmlFileListSize = compareLinkList.size();
		int fileCount = 0;
		for (CompareLink compareLink : compareLinkList) {
			fileCount++;
			try {
				if (MintFileUtils.isFileExists(compareLink
						.getCurrentImageFilePath())
						&& MintFileUtils.isFileExists(compareLink
								.getBaselineImageFilePath())) {
					logger.debug("Image Compare Start for File " + fileCount + " of "
							+ htmlFileListSize);
					
					
					ImageCompare imageCompare = new ImageCompare();
					if ( imageCompare.compareImage(compareLink
							.getBaselineImageFilePath(), compareLink
							.getCurrentImageFilePath())) {
						compareLink.setImageMatched(true);
					}else{
						compareLink.setImageMatched(false);
						compareLink.setImageDifferenceFilePath(getChageIndicatorFilePath( compareLink
								.getCurrentImageFilePath()));
						ImageCompare.saveJPG(
								imageCompare.getChangeIndicator(), compareLink.getImageDifferenceFilePath());
					}

					serializeCompareConfig(compareConfig, scheduleExecution);
					logger.debug("Image Compare completed for the File Count "
							+ fileCount);
				}
			} catch (Exception e) {
				compareLink.setHtmlMatched(false);
				logger.error("Exception while Comparing the Current Run Image File :"
						+ compareLink.getCurrentHtmlFilePath());
				logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			}
		}

	}
	
	private String getChageIndicatorFilePath(String currentImageFilePath) {
		return currentImageFilePath.replace(".jpeg", "Changes.jpeg");
	}

	private void doHtmlComparison(CompareConfig compareConfig,
			List<Link> baselineLinks, ScheduleExecution scheduleExecution) {
		List<CompareLink> compareLinkList = compareConfig.getCompareLinkList();
		int htmlFileListSize = compareLinkList.size();
		int fileCount = 0;
		for (CompareLink compareLink : compareLinkList) {
			fileCount++;
			try {
				if (MintFileUtils.isFileExists(compareLink
						.getCurrentHtmlFilePath())
						&& MintFileUtils.isFileExists(compareLink
								.getBaselineHtmlFilePath())) {
					logger.debug("HTML Compare File " + fileCount + " of "
							+ htmlFileListSize);
					boolean htmlComparisonMatched = !isDomHasDifference(
							compareLink.getBaselineHtmlFilePath(),
							compareLink.getCurrentHtmlFilePath());

					if (htmlComparisonMatched) {
						compareLink.setHtmlMatched(true);
					} else {
						compareLink.setHtmlMatched(false);
						compareLink
								.setHtmlDomFilePath(getChageIndicatorDomFilePath(compareLink
										.getCurrentHtmlFilePath()));
						compareLink
								.setHtmlPageViewFilePath(getPageViewFilePath(compareLink
										.getCurrentHtmlFilePath()));
					}
					serializeCompareConfig(compareConfig, scheduleExecution);
					logger.debug("HTML Compare completed for the File Count "
							+ fileCount);
				}
			} catch (Exception e) {
				compareLink.setHtmlMatched(false);
				logger.error("Exception while Comparing the Current Run HTML File :"
						+ compareLink.getCurrentHtmlFilePath());
				logger.error("Exception while Comparing the Baseline HTML File :"
						+ compareLink.getBaselineHtmlFilePath());
				logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			}
		}

	}

	private void serializeCompareConfig(CompareConfig compareConfig,
			ScheduleExecution scheduleExecution) {
		JsonReaderWriter<CompareConfig> jsonReaderWriterCompareConfig = new JsonReaderWriter<CompareConfig>();
		try {
			compareConfig.setLastUpdatedTime(DateTimeUtil
					.getCurrentDateTimeAsString(DateTimeUtil.DATE_TIME_FORMAT));
			compareConfig.setCompareEndTime("");
			jsonReaderWriterCompareConfig.writeJsonObjectToFile(
					compareConfig,
					scheduleExecution.getCrawlStatusDirectory()
							+ File.separator
							+ UiTestingConstants.COMPARE_CONFIG);
		} catch (Exception e) {
			logger.error("Exception while serializeCompareConfig CompareConfig Status.");
		}
		
	}

	private String getPageViewFilePath(String currentHtmlFilePath) {
		return currentHtmlFilePath.replace(".html", "_TagDiff.html");
	}

	private String getChageIndicatorDomFilePath(String currentHtmlFilePath) {
		return currentHtmlFilePath.replace(".html", "_DOMDelta.html");
	}

	private boolean isDomHasDifference(String baselineHtmlFilePath,
			String currentHtmlFilePath) throws Exception {
		ApplicationComparator appCompare = new ApplicationComparator();
		boolean hugeDifferenceFlag = true;

		try {
			hugeDifferenceFlag = appCompare.hasDifference(baselineHtmlFilePath,
					currentHtmlFilePath, true);
		} catch (Exception e) {
			logger.error("Exception while comparing the HTML file :"
					+ baselineHtmlFilePath);
			logger.error("Exception :" + e);
			throw e;
		}

		return hugeDifferenceFlag;
	}

	@Scheduled(fixedDelayString = "${mint.scheduler.uischedule.compare.delaytime}")
	public void executePendingTextComparison() {
		
		try {
			if (! Boolean.parseBoolean(CommonUtils.getPropertyFromPropertyFile(
					schedulerProperties, SCHEDULER.UI_COMPARE_SCHEDULER_ENABLE))) {
				
				//Schedule Job is disabled. Do nothing.
				return;
			}
		} catch (Exception e) {
			logger.error("Unable to start the UI Compare Scheduler.");
			e.printStackTrace();
		}

		ScheduleExecutionDetail scheduleExecutionDetail = scheduledService
				.getPendingComparison();
		ScheduleExecution baselineScheduleExcecution = new ScheduleExecution();
		List<Link> baselineLinks = new ArrayList<Link>();
		int textComparisonStatus = ExecutionStatus.IN_PROGRESS.getStatusCode();
		if (scheduleExecutionDetail.getScheduleExecution() != null
				&& scheduleExecutionDetail.getScheduleExecution()
						.getScheduleExecutionId() > 0) {
			logger.info("Process TEXT compare for scheduleExecutionDetail :"
					+ scheduleExecutionDetail);

			ScheduleExecution scheduleExecution = scheduleExecutionDetail
					.getScheduleExecution();
			
			baselineScheduleExcecution = scheduledService
					.getScheduleExecution(scheduleExecution.getBaselineScheduleExecutionId());

			boolean isBaselineFoundForCompare = false;
			boolean isCurrenFoundForCompare = false;

			if (!isBaselineScheduleExecutionExists(baselineScheduleExcecution)) {
				// If no baseline found, make the comparison status for the
				// schedule execution as not applicable.
				updateScheduleExecution(scheduleExecution);
			} else {
				isBaselineFoundForCompare = true;
			}

			if (isBaselineFoundForCompare) {
				// check baseline Crawlconfig exists.
				try {
					isBaselineFoundForCompare = MintFileUtils
							.isFileExists(baselineScheduleExcecution
									.getCrawlStatusDirectory()
									+ File.separator
									+ UiTestingConstants.CRAWL_CONFIG);
				} catch (Exception e1) {
					logger.error("Exception while reading Baseline CrawlConfig file.");
					logger.error("Stack Trace :"
							+ ExceptionUtils.getStackTrace(e1));
				}
			}

			if (isBaselineFoundForCompare) {
				CrawlConfig crawlConfig = new CrawlConfig();
				JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();

				try {
					crawlConfig = jsonReaderWriter.readJsonObjectFromFile(
							new CrawlConfig(),
							baselineScheduleExcecution
									.getCrawlStatusDirectory()
									+ File.separator
									+ UiTestingConstants.CRAWL_CONFIG);
					baselineLinks = crawlConfig.getCrawlStatus()
							.getNavigationList();

					logger.info("baselineLinks :" + baselineLinks);

				} catch (Exception e) {
					logger.error("Exception while reading baseline CrawlConfig object.");
					logger.error("Stack Trace :"
							+ ExceptionUtils.getStackTrace(e));
				}

			}

			if (isBaselineFoundForCompare) {
				isCurrenFoundForCompare = checkCurrentRunFileExists(scheduleExecution);
			}

			CompareConfig compareConfig = new CompareConfig();
			
			if (isBaselineFoundForCompare && isCurrenFoundForCompare) {
				// Update Text comparison start date and time.
				startTextCompare(ExecutionStatus.IN_PROGRESS.getStatusCode(),
						scheduleExecution);

				CrawlConfig currentRunCrawlConfig = new CrawlConfig();
				boolean crawlInprogress = true;

				JsonReaderWriter<CompareConfig> jsonReaderWriterCompareConfig = new JsonReaderWriter<CompareConfig>();
				
				JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();

				try {
					compareConfig
							.setCompareStartTime(DateTimeUtil
									.getCurrentDateTimeAsString(DateTimeUtil.DATE_TIME_FORMAT));
					compareConfig.setCompareEndTime("");
					jsonReaderWriterCompareConfig.writeJsonObjectToFile(
							compareConfig,
							scheduleExecution.getCrawlStatusDirectory()
									+ File.separator
									+ UiTestingConstants.COMPARE_CONFIG);
				} catch (Exception e) {
					logger.error("Exception while Updating CompareStarting Status.");
				}

				int noOfTimesCrawlConfigNull = 0;

				while (crawlInprogress) {
					try {
						currentRunCrawlConfig = jsonReaderWriter
								.readJsonObjectFromFile(
										new CrawlConfig(),
										scheduleExecution
												.getCrawlStatusDirectory()
												+ File.separator
												+ UiTestingConstants.CRAWL_CONFIG);

						if (null != currentRunCrawlConfig
								&& null != currentRunCrawlConfig
										.getCrawlStatus().getNavigationList()) {

							// If CrawlConfig found read NavigationLinks and add
							// the CompareLink to InProgressComparison.obj file.
							// If InProgressComparison.obj not found create new
							// one.
							// logger.info("currentRunLink :"+currentRunLink);
						} else {
							noOfTimesCrawlConfigNull++;

							if (noOfTimesCrawlConfigNull > NO_OF_TIMES_WAIT_FOR_CONFIG) {
								crawlInprogress = false;
								textComparisonStatus = ExecutionStatus.FAILED
										.getStatusCode();
							}
							Thread.sleep(5000);
						}

						// logger.info("currentRunCrawlConfig.getStatus() :"+currentRunCrawlConfig.getStatus());
						// logger.debug("Last updated time :"+currentRunCrawlConfig.getLastRecordedTime());
					} catch (Exception e) {
						logger.warn("Unable to read CrawlConfig object.");
						// logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
						textComparisonStatus = ExecutionStatus.FAILED
								.getStatusCode();
						noOfTimesCrawlConfigNull++;

						try {
							Thread.sleep(5000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					if (crawlInprogress) {
						try {
							try{
								compareConfig = jsonReaderWriterCompareConfig
										.readJsonObjectFromFile(
												new CompareConfig(),
												scheduleExecution
														.getCrawlStatusDirectory()
														+ File.separator
														+ UiTestingConstants.COMPARE_CONFIG);
							}catch(Exception e){
								logger.warn("Exception while reading CompareConfig:"+scheduleExecution.getCrawlStatusDirectory() + File.separator + UiTestingConstants.COMPARE_CONFIG);
							}

							boolean isCompareAdded = addNonComparedLinksToCompareLinks(
									currentRunCrawlConfig, compareConfig);

							// Process comparison
							doTextComparison(compareConfig, baselineLinks);

							if (isCompareAdded) {
								compareConfig
										.setLastUpdatedTime(DateTimeUtil
												.getCurrentDateTimeAsString(DateTimeUtil.DATE_TIME_FORMAT));
								compareConfig
										.setCompareProcessedUrlCount(compareConfig
												.getCompareLinkList().size());
								jsonReaderWriterCompareConfig
										.writeJsonObjectToFile(
												compareConfig,
												scheduleExecution
														.getCrawlStatusDirectory()
														+ File.separator
														+ UiTestingConstants.COMPARE_CONFIG);
								logger.info("Urls found to do Text comparison.");
							} else {
								if ( currentRunCrawlConfig.getCrawlStatus().getRunStatus() == ExecutionStatus.COMPLETE.getStatusCode() ||
										currentRunCrawlConfig.getCrawlStatus().getRunStatus() == ExecutionStatus.PAUSED.getStatusCode() || 
										currentRunCrawlConfig.getCrawlStatus().getRunStatus() == ExecutionStatus.STOPPED.getStatusCode() ) {
									crawlInprogress = false;
									Thread.sleep(10000);
									logger.debug("For Text comparison, crawl Status is Complete.");

									logger.debug("Last updated time :"
											+ currentRunCrawlConfig
													.getLastRecordedTime());
									textComparisonStatus = ExecutionStatus.COMPLETE
											.getStatusCode();
								}

								// TODO: last updated time is more than 3 mins,
								// exit
								if ( textComparisonStatus != ExecutionStatus.COMPLETE
											.getStatusCode()){
									if (!isLastUpdatedStatusValid(currentRunCrawlConfig
											.getLastRecordedTime())) {
										crawlInprogress = false;
										textComparisonStatus = ExecutionStatus.FAILED
												.getStatusCode();
										logger.debug("Exiting Text comparison, MAX wait time reached. Last Recorded Time :"+currentRunCrawlConfig.getLastRecordedTime());
									}
								}

							}
						} catch (Exception e) {
							logger.error("Exception while Process comparison");
							logger.error("Stack Trace :"
									+ ExceptionUtils.getStackTrace(e));
							crawlInprogress = false;
						}
					}
				}

				normalizeWithBaseline(compareConfig, baselineLinks);
				try {
					updateSummaryDetailsOfCompareConfig(compareConfig);
					jsonReaderWriterCompareConfig.writeJsonObjectToFile(
							compareConfig,
							scheduleExecution.getCrawlStatusDirectory()
									+ File.separator
									+ UiTestingConstants.COMPARE_CONFIG);
				} catch (Exception e) {
					logger.error("Exception while writing "
							+ UiTestingConstants.COMPARE_CONFIG);
				}
				
				compareFormElements (scheduleExecution, baselineScheduleExcecution);
			}else{
				textComparisonStatus = ExecutionStatus.NOT_APPLICABLE
						.getStatusCode();
			}

			// Complete the Text Comparison Status.
			completeTextCompare(textComparisonStatus, scheduleExecution);

			boolean status = true;
			if ( textComparisonStatus != ExecutionStatus.NOT_APPLICABLE
						.getStatusCode()){
				String applicationName = scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getApplication().getApplicationName();
				String environmentCategoryName = scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref().getEnvironment().getEnvironmentCategory().getEnvironmentCategoryName();
				
				String textComparisonExcelFilePath = scheduleExecutionDetail.getScheduleExecution().getReportsDirectory() + File.separator + "MINT_REGRESSION_COMPARISON_REPORT_"
													+ applicationName.toUpperCase() + "_" + environmentCategoryName.toUpperCase();
				textComparisonExcelFilePath = textComparisonExcelFilePath + "_" + DateTimeUtil.getCurrentDateTime() + ".xlsx";
				
				status = generateTextComparisonReport(compareConfig, scheduleExecutionDetail, textComparisonExcelFilePath);
				
				String emailSMTPHost = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.email.smtp.host");
				String emailFrom = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.email.sender");
				
				EmailUtil emailUtil = new EmailUtil(emailSMTPHost, emailFrom);
				
				String recepient = scheduleExecutionDetail.getSchedule().getEmailIds();
				List<String> recepientList = CommonUtils.getStringAsList(recepient);
				
				if ( status ){
					if ( null != recepient && recepient.trim().length() > 0 ){
						String emailContent = CommonUtils.getPropertyFromPropertyFile(
								configProperties,
								SCHEDULER.SCHEDULER_REPORTS_NOTIFICATION_MSG);
						String subject = CommonUtils.getPropertyFromPropertyFile(configProperties,
								FileSuitSchedulerJob.CONFIGKEY_SUBJECT);
						subject = subject + ExecutionStatus.COMPLETE.getStatus();
						
						List<String> emailAttachmentList = new ArrayList<String>();
						
						try {
							if ( MintFileUtils.isFileExists(textComparisonExcelFilePath) ){
								emailAttachmentList.add(textComparisonExcelFilePath);
							}
						}catch(Exception e){
							logger.error("Exception while adding the email attachment file.");
						}
						emailContent = this.addReportSummaryToMailContent(scheduleExecution, baselineScheduleExcecution, emailContent);
						emailUtil.sendEmailWithAttachement(recepientList, subject, emailContent, emailAttachmentList);
					}
				}else{
					if ( null != recepient && recepient.trim().length() > 0 ){
						String emailContent = CommonUtils.getPropertyFromPropertyFile(
								configProperties,
								SCHEDULER.SCHEDULER_REPORTS_NOTIFICATION_ERROR_MSG);
						String subject = CommonUtils.getPropertyFromPropertyFile(configProperties,
								FileSuitSchedulerJob.CONFIGKEY_SUBJECT);
						subject = subject + ExecutionStatus.ERROR.getStatus();
						
						emailUtil.sendEmail(recepientList, subject, emailContent);
					}
				}
			}
			// Read InProgressComparison.obj file one Link as a time and check
			// whether Comparison complete, of not do comparison and update the
			// CompareLink object.

			// Repeat the above steps until CrawlConfig.status is complete.
		}
	}

	private boolean generateTextComparisonReport(CompareConfig compareConfig, ScheduleExecutionDetail scheduleExecutionDetail, String textComparisonExcelFilePath) {

		String tempDirectory = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.crawler.tempDirectory");
		tempDirectory = FileDirectoryUtil.getAbsolutePath(tempDirectory, FileDirectoryUtil.getMintRootDirectory(configProperties));
		
		boolean status = ExcelWriter.writeTextComparisonDetailsToExcel(compareConfig.getCompareLinkList(), textComparisonExcelFilePath, tempDirectory);
		
		if ( status ){
			logger.debug("Text Comparison report generated successfully.");
		}else{
			logger.debug("Exception while generating Text Comparison report.");
		}
		return status;
	}

	private void updateSummaryDetailsOfCompareConfig(CompareConfig compareConfig) {
		compareConfig.setCompareEndTime(DateTimeUtil
				.getCurrentDateTimeAsString(DateTimeUtil.DATE_TIME_FORMAT));
		compareConfig
				.setCompareProcessedUrlCount(getProcessedUrlCount(compareConfig
						.getCompareLinkList()));
		compareConfig.setLastUpdatedTime(DateTimeUtil
				.getCurrentDateTimeAsString(DateTimeUtil.DATE_TIME_FORMAT));
	}

	private Integer getProcessedUrlCount(List<CompareLink> compareLinkList) {
		int comparisonCompletedCount = 0;
		try {
			for (CompareLink compareLink : compareLinkList) {
				if (compareLink.isComparisonComplete()) {
					comparisonCompletedCount++;
				}
			}
		} catch (Exception e) {

		}
		return comparisonCompletedCount;
	}

	private void completeTextCompare(int textComparisonStatusId,
			ScheduleExecution scheduleExecution) {
		ScheduleExecution scheduleExecutionNew = scheduledService
				.getScheduleExecution(scheduleExecution
						.getScheduleExecutionId());
		scheduleExecutionNew.setTextComparisonEndTime(DateTimeUtil
				.getCurrentDateTimeAsString(DateTimeUtil.DATE_TIME_FORMAT));
		scheduleExecutionNew.setTextComparisonStatusId(textComparisonStatusId);

		if (scheduleExecutionNew.getImageComparisonStatusId() == ExecutionStatus.NOT_APPLICABLE
				.getStatusCode()
				&& scheduleExecutionNew.getHtmlComparisonStatusId() == ExecutionStatus.NOT_APPLICABLE
						.getStatusCode()) {
			scheduleExecutionNew
					.setComparisonStatusRefId(textComparisonStatusId);
		}
		if (!scheduledService.updateScheduleExecution(scheduleExecutionNew, Constants.TEXT_COMPARE_END)) {
			logger.error("Exception while updating the scheduExecution for Text Compare, scheduleExecution :"
					+ scheduleExecution);
		}
	}

	private void completeHtmlCompare(int htmlComparisonStatusId,
			ScheduleExecution scheduleExecution) {
		ScheduleExecution scheduleExecutionNew = scheduledService
				.getScheduleExecution(scheduleExecution
						.getScheduleExecutionId());
		
		scheduleExecutionNew.setHtmlComparisonEndTime(DateTimeUtil
				.getCurrentDateTimeAsString(DateTimeUtil.DATE_TIME_FORMAT));

		if (scheduleExecutionNew.getImageComparisonStatusId() == ExecutionStatus.NOT_APPLICABLE
				.getStatusCode()
				&& scheduleExecutionNew.getTextComparisonStatusId() == ExecutionStatus.COMPLETE
						.getStatusCode()) {
			scheduleExecutionNew
					.setComparisonStatusRefId(htmlComparisonStatusId);
		}
		scheduleExecutionNew.setHtmlComparisonStatusId(htmlComparisonStatusId);
		if (!scheduledService.updateScheduleExecution(scheduleExecutionNew, Constants.HTML_COMPARE_END)) {
			logger.error("Exception while updating the scheduExecution for HTML Compare, scheduleExecution :"
					+ scheduleExecution);
		}
	}

	private void completeImageCompare(int imageComparisonStatusId,
			ScheduleExecution scheduleExecution) {
		/*ScheduleExecution scheduleExecutionNew = scheduledService
				.getScheduleExecution(scheduleExecution
						.getScheduleExecutionId());*/
		ScheduleExecution scheduleExecutionNew = new ScheduleExecution();
		scheduleExecutionNew.setScheduleExecutionId(scheduleExecution.getScheduleExecutionId());
		scheduleExecutionNew.setImageComparisonEndTime(DateTimeUtil
				.getCurrentDateTimeAsString(DateTimeUtil.DATE_TIME_FORMAT));
		scheduleExecutionNew.setImageComparisonStatusId(imageComparisonStatusId);

		scheduleExecutionNew
					.setComparisonStatusRefId(imageComparisonStatusId);

		scheduleExecutionNew.setHtmlComparisonStatusId(imageComparisonStatusId);
		if (!scheduledService.updateScheduleExecution(scheduleExecutionNew, Constants.IMAGE_COMPARE_END)) {
			logger.error("Exception while updating the scheduExecution for Image Compare, scheduleExecution :"
					+ scheduleExecution);
		}
	}
	
	private void startTextCompare(int textComparisonStatusId,
			ScheduleExecution scheduleExecution) {
		/*ScheduleExecution scheduleExecutionNew = scheduledService
				.getScheduleExecution(scheduleExecution
						.getScheduleExecutionId());*/
		ScheduleExecution scheduleExecutionNew = new ScheduleExecution();
		scheduleExecutionNew.setScheduleExecutionId(scheduleExecution.getScheduleExecutionId());
		scheduleExecutionNew.setTextComparisonStartTime(DateTimeUtil
				.getCurrentDateTimeAsString(DateTimeUtil.DATE_TIME_FORMAT));
		scheduleExecutionNew
				.setComparisonStatusRefId(ExecutionStatus.IN_PROGRESS
						.getStatusCode());
		scheduleExecutionNew.setTextComparisonStatusId(textComparisonStatusId);
		if (!scheduledService.updateScheduleExecution(scheduleExecutionNew, Constants.TEXT_COMPARE_START)) {
			logger.error("Exception while updating the scheduExecution for Text Compare, scheduleExecution :"
					+ scheduleExecution);
		}
	}

	private void startHtmlCompare(int htmlComparisonStatusId,
			ScheduleExecution scheduleExecution) {
		/*ScheduleExecution scheduleExecutionNew = scheduledService
				.getScheduleExecution(scheduleExecution
						.getScheduleExecutionId());*/
		ScheduleExecution scheduleExecutionNew = new ScheduleExecution();
		scheduleExecutionNew.setScheduleExecutionId(scheduleExecution.getScheduleExecutionId());
		scheduleExecutionNew.setHtmlComparisonStartTime(DateTimeUtil
				.getCurrentDateTimeAsString(DateTimeUtil.DATE_TIME_FORMAT));
		scheduleExecutionNew
				.setComparisonStatusRefId(ExecutionStatus.IN_PROGRESS
						.getStatusCode());
		scheduleExecutionNew.setHtmlComparisonStatusId(htmlComparisonStatusId);
		if (!scheduledService.updateScheduleExecution(scheduleExecutionNew, Constants.HTML_COMPARE_START)) {
			logger.error("Exception while updating the scheduExecution for Html Compare, scheduleExecution :"
					+ scheduleExecution);
		}
	}

	private void startImageCompare(int imageComparisonStatusId,
			ScheduleExecution scheduleExecution) {
		/*ScheduleExecution scheduleExecutionNew = scheduledService
				.getScheduleExecution(scheduleExecution
						.getScheduleExecutionId());*/
		ScheduleExecution scheduleExecutionNew = new ScheduleExecution();
		scheduleExecutionNew.setScheduleExecutionId(scheduleExecution.getScheduleExecutionId());
		scheduleExecutionNew.setImageComparisonStartTime(DateTimeUtil
				.getCurrentDateTimeAsString(DateTimeUtil.DATE_TIME_FORMAT));
		scheduleExecutionNew
				.setComparisonStatusRefId(ExecutionStatus.IN_PROGRESS
						.getStatusCode());
		scheduleExecutionNew.setImageComparisonStatusId(imageComparisonStatusId);
		if (!scheduledService.updateScheduleExecution(scheduleExecutionNew, Constants.IMAGE_COMPARE_START)) {
			logger.error("Exception while updating the scheduExecution for Image Compare, scheduleExecution :"
					+ scheduleExecution);
		}
	}
	
	private void normalizeWithBaseline(CompareConfig compareConfig,
			List<Link> baselineLinks) {
		List<CompareLink> compareLinkList = compareConfig.getCompareLinkList();
		boolean found = false;
		try {
			for (Link link : baselineLinks) {
				found = false;
				for (CompareLink compareLink : compareLinkList) {
					if (link.getUrl().equals(compareLink.getUrl())) {
						found = true;
						break;
					}
				}
				if (!found) {
					CompareLink compareLink = new CompareLink();
					compareLink.setUrl(link.getUrl());
					compareLink.setUrlNo(link.getUrlNo());
					compareLink.setComparisonComplete(true);
					compareLink.setPercentageMatch(0);
					compareLink.setUrlFoundInBaseline(true);
					compareLink.setUrlFoundInCurrentRun(false);
					compareLink.setBaselineFileSize(getFileSize(link
							.getTextFileFullPath()));
					compareLink.setBaselineFilePath(link.getTextFileFullPath());
					compareLink.setCurrentRunPartialTextList(new ArrayList<PartialText>());
					compareLink.setPageTitle(link.getPageTile());
					compareLink.setParentUrl(link.getParentUrl());
					compareLink.setNavigationPath(link.getNavigationPath());
					compareLink.setLinkName(link.getLinkName());
					compareConfig.addCompareLink(compareLink);
				}
			}
		} catch (Exception e) {
			logger.error("Exception while Normalizing with Baseline.");
		}

	}

	private void doTextComparison(CompareConfig compareConfig,
			List<Link> baselineLinks) {
		List<CompareLink> compareLinkList = compareConfig.getCompareLinkList();
		// logger.info("compareLink :"+compareLinkList);
		try {
			for (CompareLink compareLink : compareLinkList) {
				if (!compareLink.isComparisonComplete()) {
					compareLink.setComparisonComplete(true);
					compareLink.setUrlFoundInCurrentRun(true);
					Link link = getBaselineLink(baselineLinks, compareLink);

					if (link.getUrlNo() > 0) {
						compareLink.setBaselineFilePath(link
								.getTextFileFullPath());
						compareLink.setBaselineFileSize(getFileSize(link
								.getTextFileFullPath()));
						compareLink.setUrlFoundInBaseline(true);
						compareLink.setBaselineHtmlFilePath(link
								.getHtmlFileFullPath());
						compareLink.setBaselineImageFilePath(link
								.getImageFullPath());
						compareLink.setBaselinePartialTextList(link.getPartialTextList());
						
						try {
							if (MintFileUtils.isFileExists(compareLink
									.getCurrentFilePath())
									&& MintFileUtils.isFileExists(compareLink
											.getBaselineFilePath())) {
								logger.info("Starting LevenshteinDistanceCalculator, Url No: "+compareLink.getUrlNo());
								logger.info("Baseline Text File Path :"+compareLink
										.getBaselineFilePath());
								logger.info("Current Run Text File Path :"+compareLink
										.getCurrentFilePath());
								int levResults = LevenshteinDistanceCalculator
										.pecentageOfTextMatch(MintFileUtils.readFromFile(compareLink
												.getBaselineFilePath()),
												MintFileUtils.readFromFile(compareLink
														.getCurrentFilePath()));
								compareLink.setPercentageMatch(levResults);
								logger.info("Text File Percentage match :"+levResults);
								if ( levResults == 100 ){
									compareLink.setTextFileMatched(true);
								}else{
									
	
									LinkedList<Diff> difference = TextCompareUtils
											.getDifferencesBetweenTextFile(
													FileUtils
															.readFileToString(new File(
																	compareLink
																			.getCurrentFilePath())), FileUtils
																			.readFileToString(new File(
																					compareLink
																							.getBaselineFilePath())));
	
									if (null != difference && difference.size() > 1) {
										String differenceFilePath = compareLink
												.getCurrentFilePath().replace(
														".txt", "_diff.html");
										if (!TextCompareUtils.createTextDiffFile(
												difference, differenceFilePath)) {
											logger.error("Exception while creating Differences File");
										} else {
											compareLink
													.setCompareDifferenceTextFilePath(differenceFilePath);
										}
									}
								}

							} else {
								compareLink.setUrlFoundInBaseline(false);
							}
						} catch (Exception e) {

						}
						
						//Do Partial text Compare.
						try{
							List<PartialText> currentRunPartialTextList = compareLink.getCurrentRunPartialTextList();
							List<PartialText> baselinePartialTextList = link.getPartialTextList();
							
							if ( null != currentRunPartialTextList && currentRunPartialTextList.size() > 0 && null != baselinePartialTextList && baselinePartialTextList.size() > 0 ){
								for ( PartialText currentRunPartialText : currentRunPartialTextList ){
									for ( PartialText baselineRunPartialText : baselinePartialTextList){
										if ( currentRunPartialText.getContentName().equals(baselineRunPartialText.getContentName())){
											if (MintFileUtils.isFileExists(currentRunPartialText
													.getPartialTextPath())
													&& MintFileUtils.isFileExists(baselineRunPartialText
															.getPartialTextPath())) {
												int levResults = LevenshteinDistanceCalculator
														.pecentageOfTextMatch(MintFileUtils.readFromFile(currentRunPartialText
																.getPartialTextPath()),
																MintFileUtils.readFromFile(baselineRunPartialText
																		.getPartialTextPath()));
												
												logger.info("Current Run file path :"+currentRunPartialText.getPartialTextPath());
												logger.info("Baseine file path :"+baselineRunPartialText.getPartialTextPath());
												
												logger.info("Partial text Percentage Match :"+levResults);
												currentRunPartialText.setPercentageBaselineMatch(levResults);
												
											}
										}
									}
								}
							}
							
						}catch(Exception e){
							logger.error("Exception while comaparing partial compare file :");
						}
					} else {
						compareLink.setUrlFoundInBaseline(false);
					}
				}
			}
		} catch (Exception e) {

		}
	}

	private Link getBaselineLink(List<Link> baselineLinks,
			CompareLink compareLink) {
		Link baseLineLink = new Link();
		try {
			for (Link link : baselineLinks) {
				if (compareLink.getUrl().equals(link.getUrl())) {
					baseLineLink = link;
				}
			}
		} catch (Exception e) {

		}
		return baseLineLink;
	}

	// Check CrawlConfig file is available for current run, wait for max 5 mins,
	// if not created, exit and mark the process as Failed.
	private boolean checkCurrentRunFileExists(
			ScheduleExecution scheduleExecution) {
		boolean isFileExists = false;
		int noOftimesWaitedForCrawlConfig = 0;

		if (null != scheduleExecution
				&& null != scheduleExecution.getCrawlStatusDirectory()) {
			try {
				isFileExists = MintFileUtils.isFileExists(scheduleExecution
						.getCrawlStatusDirectory()
						+ File.separator
						+ UiTestingConstants.CRAWL_CONFIG);

				while (!isFileExists) {
					if (noOftimesWaitedForCrawlConfig > NO_OF_TIMES_WAIT_FOR_CONFIG) {
						break;
					}
					noOftimesWaitedForCrawlConfig++;

					Thread.sleep(30000);
				}
			} catch (Exception e1) {

			}
		}
		return isFileExists;
	}

	private void updateScheduleExecution(ScheduleExecution scheduleExecution) {
		try {
			scheduleExecution
					.setComparisonStatusRefId(ExecutionStatus.NOT_APPLICABLE
							.getStatusCode());
			scheduleExecution
					.setHtmlComparisonStatusId(ExecutionStatus.NOT_APPLICABLE
							.getStatusCode());
			scheduleExecution
					.setImageComparisonStatusId(ExecutionStatus.NOT_APPLICABLE
							.getStatusCode());
			scheduleExecution
					.setTextComparisonStatusId(ExecutionStatus.NOT_APPLICABLE
							.getStatusCode());

			if (scheduledService.updateScheduleExecution(scheduleExecution, Constants.COMPARE_NOT_APPLICABLE)) {

				CompareConfig compareConfig = new CompareConfig();
				compareConfig.setCompareEndTime(ExecutionStatus.NOT_APPLICABLE
						.getStatus());
				compareConfig
						.setCompareStartTime(ExecutionStatus.NOT_APPLICABLE
								.getStatus());
				compareConfig.setLastUpdatedTime(DateTimeUtil
						.getCurrentDateTime());
				CrawlerSetup.writeCompareConfig(
						scheduleExecution.getCrawlStatusDirectory(),
						compareConfig);
			} else {
				logger.error("Exception while update the ScheduleExecution");
			}
		} catch (Exception e) {
			logger.error("Exception while updating the scheudle.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

	}

	private boolean isBaselineScheduleExecutionExists(
			ScheduleExecution baselineScheduleExcecution) {
		if (null == baselineScheduleExcecution.getScheduleExecutionId()
				|| baselineScheduleExcecution.getScheduleExecutionId() == 0) {
			// logger.warn("Baseline Schedule Execution not found for the Schedule execution id :"+scheduleExecutionDetail.getSchedule().getBaselineScheduleExecutionId());
			return false;
		}
		return true;
	}

	private boolean isLastUpdatedStatusValid(String lastRecordedTime) {
		SimpleDateFormat format = new SimpleDateFormat(
				DateTimeUtil.DATE_TIME_FORMAT);

		try {
			Date lastUpdatedDate = format.parse(lastRecordedTime);
			Date currentDate = new Date();

			long diff = currentDate.getTime() - lastUpdatedDate.getTime();

			long diffSeconds = diff / 1000;
			// logger.info("diffSeconds :"+diffSeconds);

			if (diffSeconds > 900) {
				return false;
			}
		} catch (ParseException e) {
			return false;
		}

		return true;
	}

	private boolean addNonComparedLinksToCompareLinks(
			CrawlConfig currentRunCrawlConfig, CompareConfig compareConfig) {
		boolean linkFound = false;
		boolean isCompareAdded = false;

		if (null != currentRunCrawlConfig
				&& null != currentRunCrawlConfig.getCrawlStatus()
						.getNavigationList() && null != compareConfig) {
			for (Link link : currentRunCrawlConfig.getCrawlStatus()
					.getNavigationList()) {
				linkFound = false;

				if (null != compareConfig.getCompareLinkList()
						&& compareConfig.getCompareLinkList().size() > 0) {
					for (CompareLink compareLink : compareConfig
							.getCompareLinkList()) {
						if (link.getUrlNo() == compareLink.getUrlNo()) {
							linkFound = true;
							break;
						}
					}
				}

				if (!linkFound) {
					CompareLink compareLink = new CompareLink();
					compareLink.setUrlNo(link.getUrlNo());
					compareLink.setPageTitle(link.getPageTile());
					compareLink.setLinkName(link.getLinkName());
					compareLink.setNavigationPath(link.getNavigationPath());
					compareLink.setUrl(link.getUrl());
					compareLink.setCurrentFilePath(link.getTextFileFullPath());
					compareLink.setCurrentHtmlFilePath(link
							.getHtmlFileFullPath());
					compareLink.setParentUrl(link.getParentUrl());
					compareLink
							.setCurrentImageFilePath(link.getImageFullPath());
					compareLink.setCurrentFileSize(getFileSize(link
							.getTextFileFullPath()));
					compareLink.setUrlFoundInCurrentRun(true);
					compareLink.setCurrentRunPartialTextList(link.getPartialTextList());
					compareConfig.addCompareLink(compareLink);
					isCompareAdded = true;
				}
			}
		} else {
			logger.error("Unable to add new navigation status compareConfig");
		}
		return isCompareAdded;
	}

	private long getFileSize(String textFileFullPath) {
		File file = new File(textFileFullPath);
		long size = 0;
		if (file.exists()) {
			long bytes = file.length();
			size = (bytes / 1024);
		}
		return size;
	}
	
	private String addReportSummaryToMailContent(ScheduleExecution scheduleExecution,
			ScheduleExecution baselineScheduleExcecution, String emailContent) {
		String reportSumamryContent = "";
		UiReportsSummary uiReportsSummary = new UiReportsSummary();
		Suit suit = null;
		Schedule schedule = null;
		try {
			uiReportsSummary = scheduledService.getUiReportSummary(scheduleExecution, baselineScheduleExcecution);
			schedule = scheduledService.getSchedule(scheduleExecution.getScheduleId());
			suit = homeService.getSavedSuits(schedule.getSuitId());
			if(uiReportsSummary != null) {
				reportSumamryContent = reportSumamryContent + "<table><tr><td scope='row' align='right'>Current Run Testing Start Date :</td><td>"+ uiReportsSummary.getBaselineCrawlRunStartDateTime() + "</td></tr>";
				reportSumamryContent = reportSumamryContent + "<tr><td scope='row' align='right'>Baseline Run Testing Start Date :</td><td>"+ uiReportsSummary.getBaselineCrawlRunStartDateTime() + "</td></tr>";
				reportSumamryContent = reportSumamryContent + "<tr><td scope='row' align='right'>Current Run Total URL Count :</td><td>"+ uiReportsSummary.getCurrentRunTotalUrls() + "</td></tr>";
				reportSumamryContent = reportSumamryContent + "<tr><td scope='row' align='right'>Baseline Run Total URL Count :</td><td>"+ uiReportsSummary.getBaselineTotalUrlCount() + "</td></tr>";
				reportSumamryContent = reportSumamryContent + "<tr><td scope='row' align='right'>Total Text Comparison Matched(100%) :</td><td>"+ uiReportsSummary.getTextComparisonPassedCount() + "</td></tr>";
				reportSumamryContent = reportSumamryContent + "<tr><td scope='row' align='right'>Total Text Comparison 61% - 99% Matched :</td><td>"+ uiReportsSummary.getSixtyToNintyNinePercentageMatchCount() + "</td></tr>";
				reportSumamryContent = reportSumamryContent + "<tr><td scope='row' align='right'>Total Text Comparison 21% - 60% Matched :</td><td>"+ uiReportsSummary.getTwentyToSixtyPercentageMatchCount() + "</td></tr>";
				reportSumamryContent = reportSumamryContent + "<tr><td scope='row' align='right'>Total Text Comparison 0% - 20% Matched :</td><td>"+ uiReportsSummary.getZeroTotwentyPercentageMatchCount() + "</td></tr>";
				//reportSumamryContent = reportSumamryContent + "<tr><td scope='row' align='right'>Total Pages Partially Matched :</td><td>"+ uiReportsSummary.getFailedCount() + "</td></tr>";
				reportSumamryContent = reportSumamryContent + "<tr><td scope='row' align='right'>Total Urls Not Found in Current Run :</td><td>"+ uiReportsSummary.getUrlNotMatchedWithBaselineCount() + "</td></tr>";
				reportSumamryContent = reportSumamryContent + "<tr><td scope='row' align='right'>Total Urls Newly Added in Current Run :</td><td>"+ uiReportsSummary.getNewUrlFoundInCurrentRunCount() + "</td></tr>";

				reportSumamryContent = reportSumamryContent + "</table>";
				emailContent = emailContent.replace(SCHEDULER.REPORT_SUMMARY_PLACE_HOLDER, reportSumamryContent);
				emailContent = emailContent.replace(SCHEDULER.SUIT_NAME_PLACE_HOLDER, suit.getSuitName());
			}
		} catch(Exception ex) {
			logger.error("Unable to add report summary content to mail");
		}
		logger.info("emailContent :"+emailContent);

		return emailContent;
	}
}
