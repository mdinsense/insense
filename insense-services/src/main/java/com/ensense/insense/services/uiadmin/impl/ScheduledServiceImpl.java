package com.ensense.insense.services.uiadmin.impl;

import java.io.File;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.common.JsonReaderWriter;
import com.cts.mint.common.MintFileUtils;
import com.cts.mint.common.dao.HomeDAO;
import com.cts.mint.common.dao.UserDAO;
import com.cts.mint.common.entity.UsageReport;
import com.cts.mint.common.entity.Users;
import com.cts.mint.common.model.Link;
import com.cts.mint.common.model.PartialText;
import com.cts.mint.common.model.ScheduleStatus;
import com.cts.mint.common.model.UsageReportForm;
import com.cts.mint.common.service.UserService;
import com.cts.mint.common.utils.CommonUtils;
import com.cts.mint.common.utils.Constants;
import com.cts.mint.common.utils.Constants.FileSuitSchedulerJob;
import com.cts.mint.common.utils.Constants.KEYS;
import com.cts.mint.common.utils.ExecutionStatus;
import com.cts.mint.crawler.CrawlerSetup;
import com.cts.mint.crawler.model.CompareConfig;
import com.cts.mint.crawler.model.CrawlConfig;
import com.cts.mint.crawler.model.UiReportsSummary;
import com.cts.mint.dao.ScheduledDAO;
import com.cts.mint.miscellaneous.model.ArchiveData;
import com.cts.mint.reports.service.ScheduledService;
import com.cts.mint.uitesting.dao.ScheduledServiceDAO;
import com.cts.mint.uitesting.entity.LoginUserDetails;
import com.cts.mint.uitesting.entity.Schedule;
import com.cts.mint.uitesting.entity.ScheduleExecution;
import com.cts.mint.uitesting.entity.Suit;
import com.cts.mint.uitesting.entity.SuitTextImageXref;
import com.cts.mint.uitesting.model.CompareLink;
import com.cts.mint.uitesting.model.ScheduleDetails;
import com.cts.mint.uitesting.model.ScheduleExecutionDetail;
import com.cts.mint.uitesting.model.UiTestingConstants;
import com.cts.mint.util.DateTimeUtil;
import com.cts.mint.util.DateUtil;
import com.cts.mint.util.MailSender;

@Service
public class ScheduledServiceImpl implements ScheduledService {
	private static Logger logger = Logger.getLogger(ScheduledServiceImpl.class);

	private int waitCount = 1;

	@Autowired
	ScheduledServiceDAO scheduledServiceDao;

	@Autowired
	ScheduledDAO scheduledDAO;

	@Autowired
	private MessageSource configProperties;

	@Autowired
	UserDAO userDao;

	@Autowired
	HomeDAO homeDAO;

	@Autowired
	UserService userService;

	@Override
	@Transactional
	public ScheduleExecutionDetail getPendingComparison() {
		return scheduledServiceDao.getPendingComparison();
	}
	
	@Override
	@Transactional
	public List<SuitTextImageXref> getTextAndImageDetailsFortheSuitId(int suitId) {
		return scheduledServiceDao.getTextAndImageDetailsFortheSuitId(suitId);
	}

	@Override
	@Transactional
	public ScheduleExecutionDetail getPendingOnDemandWebApplicationSchedule() {

		ScheduleExecutionDetail scheduleExecutionDetail = scheduledServiceDao
				.getPendingOnDemandWebApplicationSchedule();
		ScheduleExecution scheduleExecution = scheduleExecutionDetail
				.getScheduleExecution();
		if (null != scheduleExecution
				&& scheduleExecution.getScheduleExecutionId() > 0 && scheduleExecution.getTestExecutionStatusRefId() != ExecutionStatus.RESTART.getStatusCode() ) {
			scheduleExecution.setTestExecutionStatusRefId(2);
			scheduleExecution.setTestExecutionStartTime(new Date());
			scheduledServiceDao
					.updateScheduleExecutionAsProcessing(scheduleExecution);
		}

		// For securesites keep only planned to run loginUserDetails, remove
		// others.
		removeNotUserLoginUserDetails(scheduleExecutionDetail);

		//TODO, make a call to suittextimagexref table and fetch all the image and text name as a list and then process the list and store all the imagenames in 
		//ScheduleExecutionDetail.imageNames  and find text in ScheduleExecutionDetail.searchTextNames variables.
		
		if ( scheduleExecutionDetail.getSuitId() > 0 ){
			List<SuitTextImageXref> suitTextImageList = scheduledServiceDao.getTextAndImageDetailsFortheSuitId(scheduleExecutionDetail.getSuitId());
			scheduleExecutionDetail.setSuitTextImageList(suitTextImageList);
		}
			
		return scheduleExecutionDetail;
	}

	private void removeNotUserLoginUserDetails(
			ScheduleExecutionDetail scheduleExecutionDetail) {
		int scheduleloginUserDetailId = 0;

		try {
			scheduleloginUserDetailId = scheduleExecutionDetail.getSchedule()
					.getLoginUserDetailId();
		} catch (Exception e) {

		}

		LoginUserDetails loginUserDetail = null;

		try {
			for (LoginUserDetails loginUserDetails : scheduleExecutionDetail
					.getAppEnvEnvironmentCategoryXref()
					.getLoginUserDetailsList()) {
				if (loginUserDetails.getLoginUserDetailId() == scheduleloginUserDetailId) {
					loginUserDetail = loginUserDetails;
				}
			}
		} catch (Exception e) {

		}

		if (null != loginUserDetail
				&& null != scheduleExecutionDetail
						.getAppEnvEnvironmentCategoryXref()
						.getLoginUserDetailsList()) {
			scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref()
					.getLoginUserDetailsList().clear();
			scheduleExecutionDetail.getAppEnvEnvironmentCategoryXref()
					.getLoginUserDetailsList().add(loginUserDetail);
		}
	}

	@Override
	@Transactional
	public void schedulePendingTestSuits() {
		scheduledServiceDao.schedulePendingTestSuits();
	}

	@Override
	@Transactional
	public boolean setCompletion(ScheduleDetails appConf, boolean runStatus) {
		return scheduledDAO.setCompletion(appConf, runStatus);
	}

	@Override
	@Transactional
	public void findFileSchedularTests() throws Exception {
		String fileName = KEYS.BLANK;
		String content = KEYS.BLANK;
		String suitName = KEYS.BLANK;
		String message = KEYS.BLANK;
		String[] suitData = null;
		String extension = KEYS.BLANK;
		String userName = KEYS.BLANK;
		String baseLinedate = KEYS.BLANK;
		boolean isCompare = false;
		boolean executeTestCase = false;
		boolean isScheduled = false;
		Users users = null;
		Suit suit = null;

		String directory = CommonUtils.getPropertyFromPropertyFile(
				configProperties, FileSuitSchedulerJob.CONFIGKEY_DIRECTORY);
		String sender = CommonUtils.getPropertyFromPropertyFile(
				configProperties, FileSuitSchedulerJob.CONFIGKEY_SENDER);
		String subject = CommonUtils.getPropertyFromPropertyFile(
				configProperties, FileSuitSchedulerJob.CONFIGKEY_SUBJECT);
		String smtpHost = CommonUtils.getPropertyFromPropertyFile(
				configProperties, FileSuitSchedulerJob.CONFIGKEY_HOST);
		subject = subject + FileSuitSchedulerJob.ERROR;
		// getting the file for schedule
		File fileForSchedule = MintFileUtils
				.getTheOldestModifiedFile(MintFileUtils.getFilesForGivenString(
						directory, FileSuitSchedulerJob.FOR_SCHEDULE));

		if (fileForSchedule != null && fileForSchedule.isFile()) {
			String path = fileForSchedule.getAbsolutePath();
			userName = fileForSchedule.getName().split(KEYS.UNDERSCORE)[0];
			users = userDao.getMintUser(userName);
			// releasing file for checking if file is ready to be read.
			fileForSchedule = null;
			// checking if file is ready to be read
			if (this.checkFileUnlocked(path)) {
					try {
						fileForSchedule = new File(path);
						fileName = fileForSchedule.getAbsolutePath();
						
						logger.info("File found for schedule :" + fileName);
						
						content = MintFileUtils.readFile(fileName);
						extension = KEYS.DOT + FilenameUtils.getExtension(fileName);
						fileName = fileForSchedule.getName().replace(extension,	KEYS.BLANK);
						suitData = content.split(KEYS.COMMA);

						// file content validations
						try {
							suitName = suitData[0];
						} catch (Exception e) {
							message = CommonUtils.getPropertyFromPropertyFile(configProperties,
											FileSuitSchedulerJob.CONFIGKEY_INVALID_SUIT);
						}

						try {
							
							if ( message.length() < 1 ){
								executeTestCase = Boolean.parseBoolean(suitData[6]);
	
								if (!executeTestCase) {
									message = CommonUtils.getPropertyFromPropertyFile(configProperties,
													FileSuitSchedulerJob.CONFIGKEY_INVALID_SCHEDULE);
								}
							}
						} catch (Exception e) {
							message = CommonUtils.getPropertyFromPropertyFile(configProperties,
											FileSuitSchedulerJob.CONFIGKEY_INVALID_SCHEDULE);
						}

						try {
							if ( message.length() < 1 ){
								isCompare = Boolean.parseBoolean(suitData[7]);
							}
						} catch (Exception e) {
							//message = CommonUtils.getPropertyFromPropertyFile(configProperties,
											//FileSuitSchedulerJob.CONFIGKEY_INVALID_BASELINE_SCHEDULE);
						}
						
						fileName = fileName.replace(FileSuitSchedulerJob.FOR_SCHEDULE, KEYS.BLANK);

						try {
							if ( message.length() < 1 ){
								if ( isCompare){
									baseLinedate = suitData[8];
								}
							}
						} catch (Exception e) {
							message = CommonUtils.getPropertyFromPropertyFile(configProperties,
											FileSuitSchedulerJob.CONFIGKEY_INVALID_BASELINE_DATE);
						}

						// if Parsing issue occurs message will not be blank
						if (!StringUtils.isNotBlank(message)) {
							if (users != null && users.getUserName() != null) {
								suit = homeDAO.getSuitByName(suitName);
								if (suit != null && suit.getSuitId() != null) {
									// checking is user has access to the suit
									if (homeDAO.isUserHaveAccessToSuit(users.getGroupId(), suit.getSuitId())) {
										// scheduling suit
										if (this.scheduleSuit(suit, isCompare,baseLinedate)) {
											isScheduled = true;
											MintFileUtils.renameFile(directory,	
															fileName + FileSuitSchedulerJob.FOR_SCHEDULE,
															fileName + FileSuitSchedulerJob.PROCESSING,	extension);
											message = CommonUtils.getPropertyFromPropertyFile(configProperties,
															FileSuitSchedulerJob.CONFIGKEY_SCHEDULED);
											subject = CommonUtils.getPropertyFromPropertyFile(
													configProperties, FileSuitSchedulerJob.CONFIGKEY_SUBJECT) + FileSuitSchedulerJob.PROCESSING;
										} else {
											message = CommonUtils.getPropertyFromPropertyFile(configProperties,
															FileSuitSchedulerJob.CONFIGKEY_NOTSCHEDULED);
										}
									} else {
										message = CommonUtils.getPropertyFromPropertyFile(
														configProperties,FileSuitSchedulerJob.CONFIGKEY_NOACCESS);
									}
								} else {
									message = CommonUtils.getPropertyFromPropertyFile(configProperties,
													FileSuitSchedulerJob.CONFIGKEY_NOTEXISTS);
								}
								// sending mail
								MailSender.sendMail(sender, users.getEmailId(),
										subject, message, smtpHost);
							} else {
								message = CommonUtils.getPropertyFromPropertyFile(configProperties,
												FileSuitSchedulerJob.CONFIGKEY_INVALID_USER);
							}
						}
					} catch (Exception e) {
						isScheduled = false;
					}
					if (!isScheduled) {
						MintFileUtils.renameFile(directory, fileName
								+ FileSuitSchedulerJob.FOR_SCHEDULE, fileName
								+ FileSuitSchedulerJob.ERROR, extension);
						MintFileUtils.writeHtmlContentToFile(message,
								directory + fileName + FileSuitSchedulerJob.ERROR_LOG
										+ extension, false);
					}
			} else {  // file is locked or used by another program
				logger.error("File is locked");
				if(users != null) {
					message = CommonUtils.getPropertyFromPropertyFile(configProperties,	FileSuitSchedulerJob.CONFIGKEY_FILE_LOCKED);
					MailSender.sendMail(sender, users.getEmailId(), subject, message, smtpHost);
				}
			}
		}
	}

	private boolean scheduleSuit(Suit suit, boolean isCompare,
			String baseLinedateString) {
		boolean isScheduled = false;
		DateFormat formatter;
		Date baselineDate = null;
		Schedule ts = new Schedule();
		try {

			ts.setSuitId(suit.getSuitId());
			ts.setApplicationId(suit.getApplicationId());
			ts.setEnvironmentId(suit.getEnvironmentId());

			if (isCompare) {
				baselineDate = DateTimeUtil.convertToDate(baseLinedateString,
						"MMM/dd/yyyy");
				ts.setBaselineScheduleExecutionId(scheduledServiceDao
						.getMaxScheduleIdForSuit(suit.getSuitId(), baselineDate));
			}

			if (suit.getLoginId() != null) {
				ts.setLoginUserDetailId(1);
			}

			ts.setUserId(suit.getUserId());
			ts.setTransactionTestcase(false);
			ts.setStartDate(new Date());

			if (suit.getModuleId() != null) {
				ts.setModuleIds(suit.getModuleId());
			}

			ts.setTextCompare(suit.isTextCompare());
			ts.setHtmlCompare(suit.isHtmlCompare());
			ts.setScreenCompare(suit.isScreenCompare());
			ts.setBrowserTypeId(suit.getBrowserTypeId());
			ts.setSolutionTypeId(Constants.WEB_APPLICATION_TESTING);
			formatter = new SimpleDateFormat("HH:mm");
			ts.setScheduled(false);
			String stringTime = formatter.format(new Date());
			Time timeValue = new Time(formatter.parse(stringTime).getTime());
			ts.setStartTime(timeValue);
			isScheduled = homeDAO.saveSchedule(ts);

		} catch (Exception e) {
			logger.error("Exception in scheduleSuit");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return isScheduled;
	}

	@Override
	@Transactional
	public boolean deleteSchedule(int scheduleId, String dayName) {
		logger.debug("Entry: deleteSchedule");
		boolean isDeleted = false;
		/*
		 * String scheduledDay = ""; Schedule schedule =
		 * scheduledServiceDao.getSchedule(scheduleId); if(schedule != null) {
		 * if (schedule.getScheduleType() == SCHEDULER.BY_WEEKLY) { if
		 * (StringUtils.isNotBlank(dayName) && dayName != "NA") { scheduledDay =
		 * schedule.getScheduleDDay();
		 * if(scheduledDay.equalsIgnoreCase(dayName)) { scheduledDay = dayName;
		 * } else if (scheduledDay.endsWith(dayName)) { scheduledDay =
		 * scheduledDay.replace(KEYS.COMMA + dayName, KEYS.BLANK); } else {
		 * scheduledDay = scheduledDay.replace(dayName + KEYS.COMMA,
		 * KEYS.BLANK); } schedule.setScheduleDDay(scheduledDay); isDeleted =
		 * homeDAO.saveSchedule(schedule); } } else { isDeleted =
		 * scheduledServiceDao.deleteSchedule(scheduleId); }
		 * 
		 * 
		 * }
		 */
		isDeleted = scheduledServiceDao.deleteSchedule(scheduleId);
		logger.debug("Exit: deleteSchedule");
		return isDeleted;
	}

	/*
	 * @Override
	 * 
	 * @Transactional public ScheduleExecution
	 * getBaselineScheduleExecution(Integer suitId) { return
	 * scheduledServiceDao.getBaselineScheduleExecution(suitId); }
	 */
	@Override
	@Transactional
	public ScheduleExecution getScheduleExecution(Integer scheduleExecutionId) {
		logger.debug("Entry and Exit : getScheduleExecution");
		return scheduledServiceDao.getScheduleExecution(scheduleExecutionId);
	}

	@Override
	@Transactional
	public synchronized boolean updateScheduleExecution(
			ScheduleExecution updateScheduleExecution, String updateType) {
		logger.debug("Entry : updateScheduleExecution");
		ScheduleExecution scheduleExecution = scheduledServiceDao.getScheduleExecution(updateScheduleExecution.getScheduleExecutionId());
		
		switch(updateType){
			case Constants.EXECUTION_STATUS_UPDATE:
				logger.info("For EXECUTION_STATUS_UPDATE");
				scheduleExecution.setTestExecutionStatusRefId(updateScheduleExecution.getTestExecutionStatusRefId());
				break;
			case Constants.HTML_COMPARE_START:
				logger.info("For HTML_COMPARE_START");
				scheduleExecution.setHtmlComparisonStartTime(updateScheduleExecution.getHtmlComparisonStartTime());
				scheduleExecution.setComparisonStatusRefId(updateScheduleExecution.getComparisonStatusRefId());
				scheduleExecution.setHtmlComparisonStatusId(updateScheduleExecution.getHtmlComparisonStatusId());
				break;
			case Constants.HTML_COMPARE_END:
				logger.info("For HTML_COMPARE_END");
				scheduleExecution.setHtmlComparisonStatusId(updateScheduleExecution.getHtmlComparisonStatusId());
				scheduleExecution.setHtmlComparisonEndTime(updateScheduleExecution.getHtmlComparisonEndTime());
				scheduleExecution.setComparisonStatusRefId(updateScheduleExecution.getComparisonStatusRefId());
				break;
			case Constants.TEXT_COMPARE_START:
				logger.info("For TEXT_COMPARE_START");
				scheduleExecution.setTextComparisonStartTime(updateScheduleExecution.getTextComparisonStartTime());
				scheduleExecution.setComparisonStatusRefId(updateScheduleExecution.getComparisonStatusRefId());
				scheduleExecution.setTextComparisonStatusId(updateScheduleExecution.getTextComparisonStatusId());
				break;
			case Constants.TEXT_COMPARE_END:
				logger.info("For TEXT_COMPARE_END");
				scheduleExecution.setTextComparisonStatusId(updateScheduleExecution.getTextComparisonStatusId());
				scheduleExecution.setTextComparisonEndTime(updateScheduleExecution.getTextComparisonEndTime());
				scheduleExecution.setComparisonStatusRefId(updateScheduleExecution.getComparisonStatusRefId());
				break;
			case Constants.IMAGE_COMPARE_START:
				logger.info("For IMAGE_COMPARE_START");
				scheduleExecution.setImageComparisonStartTime(updateScheduleExecution.getImageComparisonStartTime());
				scheduleExecution.setComparisonStatusRefId(updateScheduleExecution.getComparisonStatusRefId());
				scheduleExecution.setImageComparisonStatusId(updateScheduleExecution.getImageComparisonStatusId());
				break;
			case Constants.IMAGE_COMPARE_END:
				logger.info("For IMAGE_COMPARE_END");
				scheduleExecution.setHtmlComparisonEndTime(updateScheduleExecution.getHtmlComparisonEndTime());
				scheduleExecution.setImageComparisonStatusId(updateScheduleExecution.getImageComparisonStatusId());
				scheduleExecution.setImageComparisonEndTime(updateScheduleExecution.getImageComparisonEndTime());
				scheduleExecution.setComparisonStatusRefId(updateScheduleExecution.getComparisonStatusRefId());
				break;
			case Constants.TEST_EXECUTION_UPDATE:
				logger.info("For TEST_EXECUTION_UPDATE");
				scheduleExecution.setTestExecutionStatusRefId(updateScheduleExecution.getTestExecutionStatusRefId());
				scheduleExecution.setTestExecutionEndTime(updateScheduleExecution.getTestExecutionEndTime());
				break;
			case Constants.BROKEN_URL_STATUS_START:
				scheduleExecution.setBrokenUrlReportGenStartTime(updateScheduleExecution.getBrokenUrlReportGenStartTime());
				break;
			case Constants.ANALYTICS_REPORT_STATUS_START:
				scheduleExecution.setAnalyticsReportGenStartTime(updateScheduleExecution.getAnalyticsReportGenStartTime());
				break;
			case Constants.ANALYTICS_REPORT_STATUS_END:
				scheduleExecution.setAnalyticsReportStatusId(updateScheduleExecution.getAnalyticsReportStatusId());
				scheduleExecution.setReportStatusRefId(updateScheduleExecution.getReportStatusRefId());
				scheduleExecution.setAnalyticsReportGenEndTime(updateScheduleExecution.getAnalyticsReportGenEndTime());
				break;
			case Constants.BROKEN_URL_STATUS_END:
				scheduleExecution.setReportStatusRefId(updateScheduleExecution.getReportStatusRefId());
				scheduleExecution.setBrokenUrlReportStatusId(updateScheduleExecution.getBrokenUrlReportStatusId());
				break;
			case Constants.COMPARE_NOT_APPLICABLE:
				scheduleExecution.setComparisonStatusRefId(updateScheduleExecution.getComparisonStatusRefId());
				scheduleExecution.setHtmlComparisonStatusId(updateScheduleExecution.getHtmlComparisonStatusId());
				scheduleExecution.setImageComparisonStatusId(updateScheduleExecution.getImageComparisonStatusId());
				scheduleExecution.setTextComparisonStatusId(updateScheduleExecution.getTextComparisonStatusId());
				break;
				
			case Constants.TEXTORIMAGE_REPORT_STATUS_START:
				scheduleExecution.setTextOrImageReportGenStartTime(updateScheduleExecution.getTextOrImageReportGenStartTime());
				break;
				
			case Constants.TEXTORIMAGE_REPORT_STATUS_END:
				scheduleExecution.setTextOrImageReportStatusId(updateScheduleExecution.getTextOrImageReportStatusId());
				scheduleExecution.setReportStatusRefId(updateScheduleExecution.getReportStatusRefId());
				scheduleExecution.setTextOrImageReportGenEndTime(updateScheduleExecution.getTextOrImageReportGenEndTime());
				break;

		}
		
		logger.debug("Entry : updateScheduleExecution");
		return scheduledServiceDao
				.updateScheduleExecution(scheduleExecution);
	}

	@Override
	public void updateFilePath(List<CompareLink> compareResults) {
		for (CompareLink compareLink : compareResults) {
			if (null != compareLink.getCompareDifferenceTextFilePath()
					&& compareLink.getCompareDifferenceTextFilePath().length() > 0) {
				compareLink.setCompareDifferenceTextFilePath(compareLink
						.getCompareDifferenceTextFilePath().replace("\\",
								"\\\\"));
			}

			if (null != compareLink.getHtmlPageViewFilePath()
					&& compareLink.getHtmlPageViewFilePath().length() > 0) {
				compareLink.setHtmlPageViewFilePath(compareLink
						.getHtmlPageViewFilePath().replace("\\", "\\\\"));
			}

			if (null != compareLink.getHtmlDomFilePath()
					&& compareLink.getHtmlDomFilePath().length() > 0) {
				compareLink.setHtmlDomFilePath(compareLink.getHtmlDomFilePath()
						.replace("\\", "\\\\"));
			}

			if (null != compareLink.getImageDifferenceFilePath()
					&& compareLink.getImageDifferenceFilePath().length() > 0) {
				compareLink.setImageDifferenceFilePath(compareLink
						.getImageDifferenceFilePath().replace("\\", "\\\\"));
			}

			if (null != compareLink.getBaselineImageFilePath()
					&& compareLink.getBaselineImageFilePath().length() > 0) {
				compareLink.setBaselineImageFilePath(compareLink
						.getBaselineImageFilePath().replace("\\", "\\\\"));
			}

			if (null != compareLink.getCurrentImageFilePath()
					&& compareLink.getCurrentImageFilePath().length() > 0) {
				compareLink.setCurrentImageFilePath(compareLink
						.getCurrentImageFilePath().replace("\\", "\\\\"));
			}
		}
	}

	@Override
	@Transactional
	public ScheduleExecutionDetail getPendingHtmlComparison() {
		// logger.debug("Entry and Exit : getPendingHtmlComparison");
		return scheduledServiceDao.getPendingHtmlComparison();
	}

	@Override
	@Transactional
	public ScheduleExecutionDetail getPendingImageComparison() {
		return scheduledServiceDao.getPendingImageComparison();
	}

	private UiReportsSummary getAnalyticsForReportSummary(
			ScheduleExecution scheduleExecution,
			ScheduleExecution baselineScheduleExecution) {
		UiReportsSummary uiReportsSummary = new UiReportsSummary();
		
		uiReportsSummary.setAnalyticsReportStatusId(true);
		
		// Current Analytics Start Date and Time
		if ( null != scheduleExecution.getAnalyticsReportGenStartTime()
				&&  DateTimeUtil
				.convertToString(
						scheduleExecution.getAnalyticsReportGenStartTime(),
						DateTimeUtil.DATE_TIME_FORMAT).trim().length() > 0
						) {
			uiReportsSummary
					.setCurrentAnalyticsReportStartDateTime(DateTimeUtil
							.convertToString(
									scheduleExecution.getAnalyticsReportGenStartTime(),
									DateTimeUtil.DATE_TIME_FORMAT));
		} else {
			uiReportsSummary
					.setCurrentAnalyticsReportStartDateTime("Not Applicable");
		}
		
		// Baseline Analytics Start Date and Time
		if ( null != scheduleExecution.getBaselineScheduleExecutionId() && scheduleExecution.getBaselineScheduleExecutionId() > 0
				&& null != baselineScheduleExecution.getAnalyticsReportGenStartTime()
				&& DateTimeUtil
						.convertToString(
								baselineScheduleExecution
										.getAnalyticsReportGenStartTime(),
								DateTimeUtil.DATE_TIME_FORMAT).trim().length() > 0) {
			uiReportsSummary.setBaselineAnalyticsReportStartDateTime(DateTimeUtil
					.convertToString(baselineScheduleExecution
							.getAnalyticsReportGenStartTime(),
							DateTimeUtil.DATE_TIME_FORMAT));
		} else {
			uiReportsSummary.setBaselineAnalyticsReportStartDateTime("Not Applicable");
		}
		
		// Current Analytics End Date and Time
		if ( null != scheduleExecution.getAnalyticsReportGenEndTime()
				&&  DateTimeUtil
				.convertToString(
						scheduleExecution.getAnalyticsReportGenEndTime(),
						DateTimeUtil.DATE_TIME_FORMAT).trim().length() > 0
						) {
			uiReportsSummary
					.setCurrentAnalyticsReportEndDateTime(DateTimeUtil
							.convertToString(
									scheduleExecution.getAnalyticsReportGenEndTime(),
									DateTimeUtil.DATE_TIME_FORMAT));
		} else {
			uiReportsSummary
					.setCurrentAnalyticsReportEndDateTime("Not Applicable");
		}
		
		// Baseline Analytics End Date and Time
		if ( null != scheduleExecution.getBaselineScheduleExecutionId() && scheduleExecution.getBaselineScheduleExecutionId() > 0
				&& null != baselineScheduleExecution.getAnalyticsReportGenEndTime()
				&& DateTimeUtil
						.convertToString(
								baselineScheduleExecution
										.getAnalyticsReportGenEndTime(),
								DateTimeUtil.DATE_TIME_FORMAT).trim().length() > 0) {
			uiReportsSummary.setBaselineAnalyticsReportEndDateTime(DateTimeUtil
					.convertToString(baselineScheduleExecution
							.getAnalyticsReportGenEndTime(),
							DateTimeUtil.DATE_TIME_FORMAT));
		} else {
			uiReportsSummary.setBaselineAnalyticsReportEndDateTime("Not Applicable");
		}
		return uiReportsSummary;
	}
	
	private UiReportsSummary getBrokenForReportSummary(
			ScheduleExecution scheduleExecution,
			ScheduleExecution baselineScheduleExecution) {
		UiReportsSummary uiReportsSummary = new UiReportsSummary();
		
		uiReportsSummary.setBrokenUrlReportStatusId(true);
		
		// Current Analytics Start Date and Time
		if ( null != scheduleExecution.getBrokenUrlReportGenStartTime()
				&&  scheduleExecution.getBrokenUrlReportGenStartTime().trim()
				.length() > 0) {
			uiReportsSummary
					.setCurrentBrokenUrlReportStartDateTime(
									scheduleExecution.getBrokenUrlReportGenStartTime());
		} else {
			uiReportsSummary
					.setCurrentBrokenUrlReportStartDateTime("Not Applicable");
		}
		
		// Baseline Analytics Start Date and Time
		if ( null != scheduleExecution.getBaselineScheduleExecutionId() && scheduleExecution.getBaselineScheduleExecutionId() > 0
				&& null != baselineScheduleExecution.getBrokenUrlReportGenStartTime()
				&& baselineScheduleExecution.getBrokenUrlReportGenStartTime().trim()
										.length() > 0) {
			uiReportsSummary.setBaselineBrokenUrlReportStartDateTime(
					baselineScheduleExecution.getBrokenUrlReportGenStartTime());
		} else {
			uiReportsSummary.setBaselineBrokenUrlReportStartDateTime("Not Applicable");
		}
		
		// Current Analytics End Date and Time
		if ( null != scheduleExecution.getBrokenUrlReportGenEndTime()
				&& scheduleExecution.getBrokenUrlReportGenEndTime().trim()
						.length() > 0) {
			uiReportsSummary
					.setCurrentBrokenUrlReportEndDateTime(
									scheduleExecution.getBrokenUrlReportGenEndTime());
		} else {
			uiReportsSummary
					.setCurrentBrokenUrlReportEndDateTime("Not Applicable");
		}
		
		// Baseline Analytics End Date and Time
		if ( null != scheduleExecution.getBaselineScheduleExecutionId() && scheduleExecution.getBaselineScheduleExecutionId() > 0
				&& null != baselineScheduleExecution.getBrokenUrlReportGenEndTime()
				&& baselineScheduleExecution
										.getBrokenUrlReportGenEndTime().trim().length() > 0) {
			uiReportsSummary.setBaselineBrokenUrlReportEndDateTime(baselineScheduleExecution
							.getBrokenUrlReportGenEndTime());
		} else {
			uiReportsSummary.setBaselineBrokenUrlReportEndDateTime("Not Applicable");
		}
		return uiReportsSummary;
	}
	
	private UiReportsSummary getComparisonForReportSummary(
			ScheduleExecution scheduleExecution,
			ScheduleExecution baselineScheduleExecution) {
		UiReportsSummary uiReportsSummary = new UiReportsSummary();
		
		uiReportsSummary.setComparisonStatusRefId(true);
		if(scheduleExecution.getTextComparisonStatusId() == 3){
			uiReportsSummary.setTextComparison(true);
		}
		
		if(scheduleExecution.getHtmlComparisonStatusId() == 3){
			uiReportsSummary.setHtmlComparison(true);
		}
		
		if(scheduleExecution.getImageComparisonStatusId() == 3){
			uiReportsSummary.setImageComparison(true);
		}
		// Current Text Comparison Start/End Date and time
		
		if ( null != scheduleExecution.getBaselineScheduleExecutionId() && scheduleExecution.getBaselineScheduleExecutionId() > 0
				&& null != scheduleExecution.getTextComparisonStartTime()
				&& scheduleExecution.getTextComparisonStartTime().trim()
						.length() > 0) {
			uiReportsSummary
					.setCurrentTextCompareRunStartDateTime(scheduleExecution
							.getTextComparisonStartTime());
		} else {
			uiReportsSummary
					.setCurrentTextCompareRunStartDateTime("Not Applicable");
		}

		if ( null != scheduleExecution.getBaselineScheduleExecutionId() && scheduleExecution.getBaselineScheduleExecutionId() > 0
				&& null != scheduleExecution.getTextComparisonEndTime()
				&& scheduleExecution.getTextComparisonEndTime().trim().length() > 0) {
			uiReportsSummary
					.setCurrentTextCompareRunEndDateTime(scheduleExecution
							.getTextComparisonEndTime());
		} else {
			uiReportsSummary
					.setCurrentTextCompareRunEndDateTime("Not Applicable");
		}
		
		// Current Html Comparison Start/End Date and time

		if ( null != scheduleExecution.getBaselineScheduleExecutionId() && scheduleExecution.getBaselineScheduleExecutionId() > 0
				&& null != scheduleExecution.getHtmlComparisonStartTime()
				&& scheduleExecution.getHtmlComparisonStartTime().trim()
						.length() > 0) {
			uiReportsSummary
					.setCurrentHtmlCompareRunStartDateTime(scheduleExecution
							.getHtmlComparisonStartTime());
		} else {
			uiReportsSummary
					.setCurrentHtmlCompareRunStartDateTime("Not Applicable");
		}

		if ( null != scheduleExecution.getBaselineScheduleExecutionId() && scheduleExecution.getBaselineScheduleExecutionId() > 0
				&& null != scheduleExecution.getHtmlComparisonEndTime()
				&& scheduleExecution.getHtmlComparisonEndTime().trim().length() > 0) {
			uiReportsSummary
					.setCurrentHtmlCompareRunEndDateTime(scheduleExecution
							.getHtmlComparisonEndTime());
		} else {
			uiReportsSummary
					.setCurrentHtmlCompareRunEndDateTime("Not Applicable");
		}

		// Current Image Comparison Start/End Date and time
		if ( null != scheduleExecution.getBaselineScheduleExecutionId() && scheduleExecution.getBaselineScheduleExecutionId() > 0
				&& null != scheduleExecution.getImageComparisonStartTime()
				&& scheduleExecution.getImageComparisonStartTime().trim()
						.length() > 0) {
			uiReportsSummary
					.setCurrentImageCompareRunStartDateTime(scheduleExecution
							.getImageComparisonStartTime());
		} else {
			uiReportsSummary
					.setCurrentImageCompareRunStartDateTime("Not Applicable");
		}

		if ( null != scheduleExecution.getBaselineScheduleExecutionId() && scheduleExecution.getBaselineScheduleExecutionId() > 0
				&& null != scheduleExecution.getImageComparisonEndTime()
				&& scheduleExecution.getImageComparisonEndTime().trim()
						.length() > 0) {
			uiReportsSummary
					.setCurrentImageCompareRunEndDateTime(scheduleExecution
							.getImageComparisonEndTime());
		} else {
			uiReportsSummary
					.setCurrentImageCompareRunEndDateTime("Not Applicable");
		}
		
		// Baseline Text Comparison Start/End Date and time
		if ( null != scheduleExecution.getBaselineScheduleExecutionId() && scheduleExecution.getBaselineScheduleExecutionId() > 0
				&& null != baselineScheduleExecution
						.getTextComparisonStartTime()
				&& baselineScheduleExecution.getTextComparisonStartTime()
						.trim().length() > 0) {
			uiReportsSummary
					.setBaselineTextCompareRunStartDateTime(baselineScheduleExecution
							.getTextComparisonStartTime());
		} else {
			uiReportsSummary
					.setBaselineTextCompareRunStartDateTime("Not Applicable");
		}

		if ( null != scheduleExecution.getBaselineScheduleExecutionId() && scheduleExecution.getBaselineScheduleExecutionId() > 0
				&& null != baselineScheduleExecution.getTextComparisonEndTime()
				&& baselineScheduleExecution.getTextComparisonEndTime().trim()
						.length() > 0) {
			uiReportsSummary
					.setBaselineTextCompareRunEndDateTime(baselineScheduleExecution
							.getTextComparisonEndTime());
		} else {
			uiReportsSummary
					.setBaselineTextCompareRunEndDateTime("Not Applicable");
		}
		
		// Baseline HTML Comparison Start/End Date and time
		if ( null != scheduleExecution.getBaselineScheduleExecutionId() && scheduleExecution.getBaselineScheduleExecutionId() > 0
				&& null != baselineScheduleExecution
						.getHtmlComparisonStartTime()
				&& baselineScheduleExecution.getHtmlComparisonStartTime()
						.trim().length() > 0) {
			uiReportsSummary
					.setBaselineHtmlCompareRunStartDateTime(baselineScheduleExecution
							.getHtmlComparisonStartTime());
		} else {
			uiReportsSummary
					.setBaselineHtmlCompareRunStartDateTime("Not Applicable");
		}

		if ( null != scheduleExecution.getBaselineScheduleExecutionId() && scheduleExecution.getBaselineScheduleExecutionId() > 0
				&& null != baselineScheduleExecution.getHtmlComparisonEndTime()
				&& baselineScheduleExecution.getHtmlComparisonEndTime().trim()
						.length() > 0) {
			uiReportsSummary
					.setBaselineHtmlCompareRunEndDateTime(baselineScheduleExecution
							.getHtmlComparisonEndTime());
		} else {
			uiReportsSummary
					.setBaselineHtmlCompareRunEndDateTime("Not Applicable");
		}

		// Baseline Image Comparison Start/End Date and time
		if ( null != scheduleExecution.getBaselineScheduleExecutionId() && scheduleExecution.getBaselineScheduleExecutionId() > 0
				&& null != baselineScheduleExecution
						.getImageComparisonStartTime()
				&& baselineScheduleExecution.getImageComparisonStartTime()
						.trim().length() > 0) {
			uiReportsSummary
					.setBaselineImageCompareRunStartDateTime(baselineScheduleExecution
							.getImageComparisonStartTime());
		} else {
			uiReportsSummary
					.setBaselineImageCompareRunStartDateTime("Not Applicable");
		}

		if ( null != scheduleExecution.getBaselineScheduleExecutionId() && scheduleExecution.getBaselineScheduleExecutionId() > 0
				&& null != baselineScheduleExecution
						.getImageComparisonEndTime()
				&& baselineScheduleExecution.getImageComparisonEndTime().trim()
						.length() > 0) {
			uiReportsSummary
					.setBaselineImageCompareRunEndDateTime(baselineScheduleExecution
							.getImageComparisonEndTime());
		} else {
			uiReportsSummary
					.setBaselineImageCompareRunEndDateTime("Not Applicable");
		}
		
		return uiReportsSummary;
	}
		
	@Override
	public UiReportsSummary getUiReportSummary(
			ScheduleExecution scheduleExecution,
			ScheduleExecution baselineScheduleExecution) {
		UiReportsSummary uiReportsSummary = new UiReportsSummary();

		if ( null != scheduleExecution.getComparisonStatusRefId() 
				&& scheduleExecution.getComparisonStatusRefId() > 0
				&& scheduleExecution.getComparisonStatusRefId() == 3){
			
			uiReportsSummary = getComparisonForReportSummary(scheduleExecution, baselineScheduleExecution);
		}else if( null != scheduleExecution.getBrokenUrlReportStatusId()
				&& scheduleExecution.getBrokenUrlReportStatusId() > 0
				&& scheduleExecution.getBrokenUrlReportStatusId() == 3){
			
			uiReportsSummary = getBrokenForReportSummary(scheduleExecution, baselineScheduleExecution);
		}else if( null != scheduleExecution.getAnalyticsReportStatusId() 
				&& scheduleExecution.getAnalyticsReportStatusId() > 0
				&& scheduleExecution.getAnalyticsReportStatusId() == 3){
			
			uiReportsSummary = getAnalyticsForReportSummary(scheduleExecution, baselineScheduleExecution);
		}
			
	//Common details for Comparison/Broken links/Analytics reports 
		
		if (null != scheduleExecution.getTestExecutionStartTime()
				&& DateTimeUtil
						.convertToString(
								scheduleExecution.getTestExecutionStartTime(),
								DateTimeUtil.DATE_TIME_FORMAT).trim().length() > 0) {
			uiReportsSummary.setCurrentCrawlRunStartDateTime(DateTimeUtil
					.convertToString(
							scheduleExecution.getTestExecutionStartTime(),
							DateTimeUtil.DATE_TIME_FORMAT));
		} else {
			uiReportsSummary.setCurrentCrawlRunStartDateTime("Not Applicable");
		}

		if (null != scheduleExecution.getTestExecutionEndTime()
				&& DateTimeUtil
						.convertToString(
								scheduleExecution.getTestExecutionEndTime(),
								DateTimeUtil.DATE_TIME_FORMAT).trim().length() > 0) {
			uiReportsSummary.setCurrentCrawlRunEndDateTime(DateTimeUtil
					.convertToString(
							scheduleExecution.getTestExecutionEndTime(),
							DateTimeUtil.DATE_TIME_FORMAT));
		} else {
			uiReportsSummary.setCurrentCrawlRunEndDateTime("Not Applicable");
		}

		if ( null != scheduleExecution.getBaselineScheduleExecutionId() && scheduleExecution.getBaselineScheduleExecutionId() > 0
				&& null != baselineScheduleExecution
						.getTestExecutionStartTime()
				&& DateTimeUtil
						.convertToString(
								baselineScheduleExecution
										.getTestExecutionStartTime(),
								DateTimeUtil.DATE_TIME_FORMAT).trim().length() > 0) {
			uiReportsSummary.setBaselineCrawlRunStartDateTime(DateTimeUtil
					.convertToString(baselineScheduleExecution
							.getTestExecutionStartTime(),
							DateTimeUtil.DATE_TIME_FORMAT));
		} else {
			uiReportsSummary.setBaselineCrawlRunStartDateTime("Not Applicable");
		}

		if ( null != scheduleExecution.getBaselineScheduleExecutionId() && scheduleExecution.getBaselineScheduleExecutionId() > 0
				&& null != baselineScheduleExecution.getTestExecutionEndTime()
				&& DateTimeUtil
						.convertToString(
								baselineScheduleExecution
										.getTestExecutionEndTime(),
								DateTimeUtil.DATE_TIME_FORMAT).trim().length() > 0) {
			uiReportsSummary
					.setBaselineCrawlRunEndDateTime(DateTimeUtil
							.convertToString(baselineScheduleExecution
									.getTestExecutionEndTime(),
									DateTimeUtil.DATE_TIME_FORMAT));
		} else {
			uiReportsSummary.setBaselineCrawlRunEndDateTime("Not Applicable");
		}

		JsonReaderWriter<List<Link>> jsonReaderWriterNavigationList = new JsonReaderWriter<List<Link>>();
		List<Link> navigationList = null;

		try {
			navigationList = jsonReaderWriterNavigationList
					.readJsonObjectFromFile(new ArrayList<Link>(),
							scheduleExecution.getCrawlStatusDirectory()
									+ File.separator
									+ UiTestingConstants.NAVIGATION_DETAILS_OBJ);
			
			if ( null != navigationList ){
				uiReportsSummary.setCurrentRunTotalUrls(navigationList.size());
			}

		} catch (Exception e) {
			logger.warn("Error while finding current run total url count.");
		}
		
		try{
			
			if ( null != baselineScheduleExecution && null != baselineScheduleExecution.getScheduleExecutionId() && baselineScheduleExecution.getScheduleExecutionId() > 0 ){
				navigationList = jsonReaderWriterNavigationList
						.readJsonObjectFromFile(new ArrayList<Link>(),
								baselineScheduleExecution.getCrawlStatusDirectory()
										+ File.separator
										+ UiTestingConstants.NAVIGATION_DETAILS_OBJ);
				if ( null != navigationList ){
					uiReportsSummary.setBaselineTotalUrlCount(navigationList.size());
				}
			}
		}catch(Exception e){
			logger.warn("Error while finding baseline total url count.");
		}
		
		JsonReaderWriter<CompareConfig> jsonReaderWriterCompareConfig = new JsonReaderWriter<CompareConfig>();
		CompareConfig compareConfig = new CompareConfig();

		try {
			compareConfig = jsonReaderWriterCompareConfig
					.readJsonObjectFromFile(new CompareConfig(),
							scheduleExecution.getCrawlStatusDirectory()
									+ File.separator
									+ UiTestingConstants.COMPARE_CONFIG);
		} catch (Exception e) {
			logger.warn("Error while reading Compare config. ");
		}

		if (null != compareConfig.getCompareLinkList()) {
			for (CompareLink compareLink : compareConfig.getCompareLinkList()) {
				if (compareLink.isUrlFoundInBaseline()
						&& compareLink.isUrlFoundInCurrentRun()) {
					uiReportsSummary
							.setUrlMatchedWithBaselineCount(uiReportsSummary
									.getUrlMatchedWithBaselineCount() + 1);

					if (compareLink.isTextFileMatched()
							|| compareLink.isHtmlMatched()
							|| compareLink.isImageMatched()) {
						compareLink.setPassed(true);
						uiReportsSummary.setMatchedCount(uiReportsSummary
								.getMatchedCount() + 1);
					} else {
						uiReportsSummary.setFailedCount(uiReportsSummary
								.getFailedCount() + 1);
					}
					if (compareLink.isTextFileMatched()) {
						uiReportsSummary
								.setTextComparisonPassedCount(uiReportsSummary
										.getTextComparisonPassedCount() + 1);
					} else {
						uiReportsSummary
								.setTextComparisonFailedCount(uiReportsSummary
										.getTextComparisonFailedCount() + 1);
					}
					if (compareLink.isHtmlMatched()) {
						uiReportsSummary
								.setHtmlComparisonPassedCount(uiReportsSummary
										.getHtmlComparisonPassedCount() + 1);
					} else {
						uiReportsSummary
								.setHtmlComparisonFailedCount(uiReportsSummary
										.getHtmlComparisonFailedCount() + 1);
					}
					if (compareLink.isImageMatched()) {
						uiReportsSummary
								.setImageComparisonPassedCount(uiReportsSummary
										.getImageComparisonPassedCount() + 1);
					} else {
						uiReportsSummary
								.setImageComparisonFailedCount(uiReportsSummary
										.getImageComparisonFailedCount() + 1);
					}
				}
				if (compareLink.isUrlFoundInBaseline()
						&& !compareLink.isUrlFoundInCurrentRun()) {
					uiReportsSummary
							.setUrlNotMatchedWithBaselineCount(uiReportsSummary
									.getUrlNotMatchedWithBaselineCount() + 1);
					uiReportsSummary.setFailedCount(uiReportsSummary
							.getFailedCount() + 1);
				}
				if (!compareLink.isUrlFoundInBaseline()
						&& compareLink.isUrlFoundInCurrentRun()) {
					uiReportsSummary
							.setNewUrlFoundInCurrentRunCount(uiReportsSummary
									.getNewUrlFoundInCurrentRunCount() + 1);
				}

				if (compareLink.isUrlFoundInBaseline()
						&& compareLink.isUrlFoundInCurrentRun()
						&& compareLink.getPercentageMatch() <= 20) {
					uiReportsSummary
							.setZeroTotwentyPercentageMatchCount(uiReportsSummary
									.getZeroTotwentyPercentageMatchCount() + 1);
				}

				if (compareLink.getPercentageMatch() >= 21
						&& compareLink.getPercentageMatch() <= 60) {
					uiReportsSummary
							.setTwentyToSixtyPercentageMatchCount(uiReportsSummary
									.getTwentyToSixtyPercentageMatchCount() + 1);
				}

				if (compareLink.getPercentageMatch() >= 61
						&& compareLink.getPercentageMatch() <= 99) {
					uiReportsSummary
							.setSixtyToNintyNinePercentageMatchCount(uiReportsSummary
									.getSixtyToNintyNinePercentageMatchCount() + 1);
				}

				/*
				 * if( compareLink.getPercentageMatch() == 100 ){
				 * uiReportsSummary
				 * .setMatchedCount(uiReportsSummary.getMatchedCount() + 1); }
				 */
			}
		}
		return uiReportsSummary;
	}

	@Override
	@Transactional
	public List<ScheduleExecutionDetail> getPendingInProgressScheduleDetails() {
		logger.debug("Entry and Exit : getPendingInProgressScheduleDetails");
		return scheduledDAO.getPendingInProgressScheduleDetails();
	}

	@Override
	public List<ScheduleStatus> getScheduleStatusList(
			List<ScheduleExecutionDetail> scheduleExecutionDetailList) {
		List<ScheduleStatus> scheduleStatusList = new ArrayList<ScheduleStatus>();
		ScheduleStatus scheduleStatus = null;
		for (ScheduleExecutionDetail scheduleExecutionDetail : scheduleExecutionDetailList) {
			scheduleStatus = new ScheduleStatus();
			try {
				scheduleStatus.setScheduleExecutionId(scheduleExecutionDetail
						.getScheduleExecution().getScheduleExecutionId());
				scheduleStatus.setExecutionStatus(ExecutionStatus
						.getStatus(scheduleExecutionDetail
								.getScheduleExecution()
								.getTestExecutionStatusRefId()));

				if (null != scheduleExecutionDetail.getScheduleExecution()
						&& null != scheduleExecutionDetail
								.getScheduleExecution()
								.getTestExecutionStartTime()) {
					scheduleStatus.setStartTime(scheduleExecutionDetail
							.getScheduleExecution().getTestExecutionStartTime()
							.toString());
				} else {
					scheduleStatus.setStartTime("");
				}
				scheduleStatus.setApplicationName(scheduleExecutionDetail
						.getAppEnvEnvironmentCategoryXref().getApplication()
						.getApplicationName());
				scheduleStatus.setEnvironmentCategory(scheduleExecutionDetail
						.getAppEnvEnvironmentCategoryXref().getEnvironment()
						.getEnvironmentCategory().getEnvironmentCategoryName());
				Users user = userService.getUser(scheduleExecutionDetail
						.getSchedule().getUserId());

				scheduleStatus.setScheduledBy(user.getUserName());
				scheduleStatus.setGroup(user.getGroup().getGroupName());

				List<ExecutionStatus> action = getPosibleAction(scheduleExecutionDetail
						.getScheduleExecution().getTestExecutionStatusRefId());
				scheduleStatus.setAction(action);
			} catch (Exception e) {

			}
			scheduleStatusList.add(scheduleStatus);
		}

		return scheduleStatusList;
	}

	private List<ExecutionStatus> getPosibleAction(int testExecutionStatusRefId) {
		List<ExecutionStatus> action = new ArrayList<ExecutionStatus>();
		switch (testExecutionStatusRefId) {
		case 1:
			action.add(ExecutionStatus.PAUSED);
			action.add(ExecutionStatus.STOPPED);
			break;
		case 2:
			action.add(ExecutionStatus.PAUSED);
			action.add(ExecutionStatus.STOPPED);
			break;
		case 6:
			action.add(ExecutionStatus.RESTART);
			action.add(ExecutionStatus.STOPPED);
			break;
		case 8:
			action.add(ExecutionStatus.PAUSED);
			action.add(ExecutionStatus.STOPPED);
			break;
		default:
			action.add(ExecutionStatus.NOT_APPLICABLE);
		}
		return action;
	}

	private boolean checkFileUnlocked(String file) {
		boolean isUnlocked = false;
		if (MintFileUtils.isFileReady(file)) {
			isUnlocked = true;
		} else {
			try {
				if (waitCount <= 2) {
					logger.debug("File is locked, will attempt again after 30 seconds");
					Thread.sleep(30000);
					waitCount++;
					isUnlocked = this.checkFileUnlocked(file);
				} else {
					isUnlocked = false;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		waitCount = 1;
		return isUnlocked;
	}

	@Override
	@Transactional
	public List<PartialText> getPartialTextCompareList(int applicationId,
			int environmentId) {
		logger.debug("Entry and Exit : getPartialTextCompareList");
		return scheduledDAO.getPartialTextCompareList(applicationId, environmentId);
	}

	@Override
	public boolean updateCrawlStatus(ScheduleExecution scheduleExecution,
			String scheduleAction) {
		logger.debug("Extry : updateCrawlStatus, scheduleAction->"+scheduleAction);
		CrawlConfig crawlConfig = new CrawlConfig(); 
		boolean status = true;
		try{
			JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();
			
			if ( null != scheduleExecution.getCrawlStatusDirectory() && scheduleExecution.getCrawlStatusDirectory().trim().length() > 0 ){
				crawlConfig = jsonReaderWriter.readJsonObjectFromFile(new CrawlConfig(), scheduleExecution.getCrawlStatusDirectory()+ File.separator + UiTestingConstants.CRAWL_CONFIG);
			}
			
			if ( Integer.parseInt(scheduleAction) == ExecutionStatus.RESTART.getStatusCode()){
				logger.info("Restart Called");
				if ( null == crawlConfig || null == crawlConfig.getCrawlStatus() ){
					status = false;
				}
				if (  status && ( null == crawlConfig.getCrawlStatus().getQueue() 
					|| null == crawlConfig.getCrawlStatus().getNavigationList() || crawlConfig.getCrawlStatus().getNavigationList().size() < 1) ){
					crawlConfig.getCrawlStatus().setRunStatus(ExecutionStatus.PENDING.getStatusCode());
				}else{
					crawlConfig.getCrawlStatus().setRunStatus(Integer.parseInt(scheduleAction));
				}
			}else{
				crawlConfig.getCrawlStatus().setRunStatus(Integer.parseInt(scheduleAction));
			}
			
			if ( null != crawlConfig && null != scheduleExecution.getCrawlStatusDirectory() && scheduleExecution.getCrawlStatusDirectory().trim().length() > 0 ){
				logger.info("Updating RunStaus in Crawl Config, with status :"+crawlConfig.getCrawlStatus().getRunStatus());
				CrawlerSetup.writeCrawlConfig(crawlConfig, scheduleExecution.getCrawlStatusDirectory()+ File.separator + UiTestingConstants.CRAWL_CONFIG);
			}
		}catch(Exception e){
			logger.error("Exception while updating Crawling Status to " + ExecutionStatus.getExecutionStatus(Integer.parseInt(scheduleAction)).getAction() + ", "
					+ "for the ScheduleExecutionId :"+scheduleExecution.getScheduleExecutionId() + " Crawl Config Directory :"+scheduleExecution.getCrawlStatusDirectory());
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			status = false;
		}
		logger.debug("Exit : updateCrawlStatus, scheduleAction->"+scheduleAction);
		return status;
	}

	@Override
	@Transactional
	public Schedule getSchedule(int scheduleId) {
		logger.debug("Entry and Exit : getSchedule");
		return scheduledServiceDao.getSchedule(scheduleId);
	}


	@Override
	@Transactional
	public Map<Integer, ScheduleExecution> getScheduleExecutionMap(
			String scheduleExecutionIdStr) {
		logger.debug("Entry and Exit : getSchedule");
		return scheduledServiceDao.getScheduleExecutionMap(scheduleExecutionIdStr);
	}

	@Override
	@Transactional
	public Map<Integer, Schedule> getScheduleMap(String scheduleIdStr) {
		logger.debug("Entry and Exit : getSchedule");
		return scheduledServiceDao.getScheduleMap(scheduleIdStr);
	}

	@Override
	@Transactional
	public List<ArchiveData> getScheduleDetails(UsageReportForm form) {
		logger.debug("Entry: getScheduleDetails");
		
		List<ArchiveData> archieveDataList = new ArrayList<ArchiveData>();
		ArchiveData archieveData = null;
		Date fromDate = null;
		Date toDate = null;
		if(form != null) {
			if(StringUtils.isNotBlank(form.getFromDate()) && StringUtils.isNotBlank(form.getToDate())) {
				fromDate = DateUtil.convertToDate(form.getFromDate(), DateUtil.UI_DATE_PICKER_FORMAT);
				toDate = DateUtil.convertToDate(form.getToDate(), DateUtil.UI_DATE_PICKER_FORMAT);
			}
		}
		List<ScheduleExecutionDetail> scheduleExecutionDetailList = scheduledServiceDao.getScheduleDetails(form, fromDate, toDate);
		
		String userIds = "0";
		
		for(ScheduleExecutionDetail detail : scheduleExecutionDetailList) {
			userIds = userIds + "," + detail.getSchedule().getUserId();
		}
		Map<Integer, Users> userMap = userDao.getMintUserMapById(userIds);
		for(ScheduleExecutionDetail scheduleExecutionDetail : scheduleExecutionDetailList ){
			archieveData = new ArchiveData();
			archieveData.setScheduleId(scheduleExecutionDetail.getSchedule().getScheduleId());
			archieveData.setScheduledBy(userMap.get(scheduleExecutionDetail.getSchedule().getUserId()).getUserName());
			archieveData.setGroupName(userMap.get(scheduleExecutionDetail.getSchedule().getUserId()).getGroup().getGroupName());
			archieveData.setScheduleExecutionId(scheduleExecutionDetail.getScheduleExecution().getScheduleExecutionId());
			if(scheduleExecutionDetail.getScheduleExecution().getTestExecutionStartTime()!=null){
				archieveData.setStartDate(scheduleExecutionDetail.getScheduleExecution().getTestExecutionStartTime().toString());
			}
			if(scheduleExecutionDetail.getScheduleExecution().getTestExecutionEndTime()!=null){
				archieveData.setEndDate(scheduleExecutionDetail.getScheduleExecution().getTestExecutionEndTime().toString());
			}
			archieveDataList.add(archieveData);
		}
		logger.debug("Exit: getScheduleDetails");
		return archieveDataList;
	}

	@Override
	@Transactional
	public List<String> deleteScheduleExecutions(String scheduleExecutionId) {
		logger.debug("Entry and Exit : deleteScheduleExecutions");
		return scheduledServiceDao.deleteScheduleExecutions(scheduleExecutionId);
	}

	

}
