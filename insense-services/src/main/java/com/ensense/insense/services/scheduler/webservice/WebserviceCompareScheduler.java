package com.ensense.insense.services.scheduler.webservice;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.Difference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.cts.mint.common.entity.Users;
import com.cts.mint.common.service.UserService;
import com.cts.mint.common.utils.CommonUtils;
import com.cts.mint.common.utils.ExecutionStatus;
import com.cts.mint.common.utils.Constants.FileSuitSchedulerJob;
import com.cts.mint.common.utils.Constants.SCHEDULER;
import com.cts.mint.reports.service.ScheduledService;
import com.cts.mint.uitesting.entity.EnvironmentCategory;
import com.cts.mint.uitesting.service.EnvironmentService;
import com.cts.mint.util.EmailUtil;
import com.cts.mint.webservice.entity.WSBaseline;
import com.cts.mint.webservice.entity.WSReports;
import com.cts.mint.webservice.entity.WSResults;
import com.cts.mint.webservice.entity.WSSchedule;
import com.cts.mint.webservice.entity.WebserviceOperations;
import com.cts.mint.webservice.entity.WebserviceSuite;
import com.cts.mint.webservice.entity.Webservices;
import com.cts.mint.webservice.model.ReportsEmail;
import com.cts.mint.webservice.service.WebserviceTestingService;
import com.cts.mint.webservice.util.WebserviceUtil;

@Service
public class WebserviceCompareScheduler {
	private static Logger logger = Logger
			.getLogger(WebserviceCompareScheduler.class);
	@Autowired
	ScheduledService scheduledService;

	@Autowired
	private WebserviceTestingService webserviceTestingService;

	@Autowired
	private MessageSource configProperties;

	@Autowired
	private EnvironmentService environmentService;

	@Autowired
	private MessageSource esbpingUrlProperty;

	@Autowired
	private MessageSource schedulerProperties;

	@Autowired
	private UserService userService;

	@Scheduled(fixedDelayString = "${mint.scheduler.webservice.compare.delaytime}")
	public void comparedWSTestResults() {
		// logger.debug("Entry: comparedWSTestResults");
		try {
			if (!Boolean.parseBoolean(CommonUtils.getPropertyFromPropertyFile(
					schedulerProperties,
					SCHEDULER.WEBSERVICE_COMPARE_SCHEDULER_ENABLE))) {

				// Schedule Job is disabled. Do nothing.
				return;
			}
		} catch (Exception e) {
			logger.error("Unable to start the Webservice execution Scheduler.");
			e.printStackTrace();
		}

		String emailSMTPHost = CommonUtils.getPropertyFromPropertyFile(
				configProperties, "mint.email.smtp.host");
		String emailFrom = CommonUtils.getPropertyFromPropertyFile(
				configProperties, "mint.email.sender");

		EmailUtil emailUtil = new EmailUtil(emailSMTPHost, emailFrom);
		String webserviceSuitName = "";

		List<WSBaseline> wsSchedules = webserviceTestingService
				.getSchedulesToBeCompared();

		for (WSBaseline wsBaseline : wsSchedules) {
			logger.debug("Web Service Schedule Found for Compare :"+wsSchedules);
			Integer wsScheduleId = wsBaseline.getWsScheduleId();
			Integer wsBaselineId = wsBaseline.getWsBaselineId();
			Integer wsBaselineScheduleId = wsBaseline.getWsBaselineScheduleId();

			List<WSResults> currentScheduleResults = webserviceTestingService
					.getSchedulesFromScheduleId(wsScheduleId);

			WebserviceSuite webserviceSuite = webserviceTestingService
					.getWebserviceSuite(currentScheduleResults.get(0)
							.getWebserviceSuiteId());

			List<WSResults> baselineScheduleResults = webserviceTestingService
					.getSchedulesFromScheduleId(wsBaselineScheduleId);

			Integer environmentId = webserviceTestingService
					.getEnvironment(currentScheduleResults.get(0)
							.getWebserviceSuiteId());

			// current map
			Map<String, WSResults> currentMap = new LinkedHashMap<String, WSResults>();
			// Map<String, String> currentReqMap = new LinkedHashMap<String,
			// String>();

			// baseline map
			Map<String, WSResults> baselineMap = new LinkedHashMap<String, WSResults>();

			List<String> resultIdList = new ArrayList<String>();

			for (WSResults current : currentScheduleResults) {
				String mapKey = current.getServiceId() + "|"
						+ current.getOperationId() + "|"
						+ current.getParameterSetId();
				resultIdList.add(mapKey);
				currentMap.put(mapKey, current);
			}

			for (WSResults baseline : baselineScheduleResults) {
				String mapKey = baseline.getServiceId() + "|"
						+ baseline.getOperationId() + "|"
						+ baseline.getParameterSetId();
				baselineMap.put(mapKey, baseline);
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
			SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");

			String messageContent = " Hi </br></br>";
			messageContent = messageContent
					+ "Below are the test results</br></br>";

			boolean isTestsuiteMatchechedWithBaseline = true;
			String[] emailTriggerOption = null;
			WSSchedule wsSchedule = null;
			int count = 0;
			List<ReportsEmail> reportsEmailList = new ArrayList<ReportsEmail>();
			String recepient = "";
			List<String> recepientList = null;

			for (Map.Entry<String, WSResults> entry : currentMap.entrySet()) {
				String key = entry.getKey();
				if (count == 0) {
					wsSchedule = webserviceTestingService
							.getWSSchedule(currentScheduleResults.get(0)
									.getWsScheduleId());
					logger.info("wsSchedule :" + wsSchedule);
					recepient = wsSchedule.getEmailIds();
					recepientList = CommonUtils.getStringAsList(recepient);
					webserviceSuitName = webserviceSuite
							.getWebserviceSuiteName();
				}
				
				WSReports wsReport = getWebServiceResponseCompareDifference(
						key, currentMap, baselineMap);

				wsReport.setWsBaselineId(wsBaselineId);

				String date = "";
				String time = "";

				WSReports existingWsReport = webserviceTestingService
						.getWSReports(wsReport);

				wsReport.setWsReportsId(existingWsReport.getWsReportsId());
				Boolean insertStatus = webserviceTestingService
						.wsSaveComparisonResults(wsReport);

				WSResults wsTestResults = entry.getValue();

				try {

					Webservices webservice = new Webservices();
					String serviceName = webserviceTestingService
							.getServicefromServiceId(
									wsTestResults.getServiceId())
							.getServiceName();

					WebserviceOperations webserviceOperations = new WebserviceOperations();
					webserviceOperations.setOperationId(wsReport
							.getOperationId());
					String operationName = webserviceTestingService
							.getOperationfromOperationId(webserviceOperations)
							.getOperationName();

					EnvironmentCategory environmentCategory = new EnvironmentCategory();
					String environmentName = environmentService
							.getEnvironmentCategoryById(environmentId)
							.getEnvironmentCategoryName();

					if (insertStatus) {
						if (null != recepient && recepient.trim().length() > 0) {
							ReportsEmail reportsEmail = new ReportsEmail();
							reportsEmail.setServiceName(serviceName);
							reportsEmail.setOperationName(operationName);
							reportsEmail.setEnvironmentName(environmentName);
							reportsEmail.setCurrentRunExecutionTime(entry
									.getValue().getCreatedDateTime());
							reportsEmail
									.setBaseLineRunExecutionTime(baselineScheduleResults
											.get(0).getCreatedDateTime());
							reportsEmail
									.setWebserviceSuitName(webserviceSuitName);

							if (wsReport.isMatchedWithBaseline()) {
								reportsEmail.setMatchedwithBasedline("Yes");
							} else {
								reportsEmail.setMatchedwithBasedline("No");
							}
							reportsEmailList.add(reportsEmail);
						}
					} else {
						messageContent = CommonUtils
								.getPropertyFromPropertyFile(
										configProperties,
										SCHEDULER.WEBSERVICE_REPORTS_NOTIFICATION_ERROR_MSG);
						String subject = CommonUtils
								.getPropertyFromPropertyFile(configProperties,
										SCHEDULER.WEBSERVICE_SUIT_SUBJECT);
						subject = subject + ExecutionStatus.FAILED.getStatus();
						emailUtil.sendEmail(recepientList, subject,
								messageContent);
					}
				} catch (Exception e) {
					logger.error("Exception in comparedWSTestResults.");
					logger.error("Stack Trace :"
							+ ExceptionUtils.getStackTrace(e));
				}
				count++;
			}

			if (reportsEmailList.size() > 0) {

				messageContent = CommonUtils.getPropertyFromPropertyFile(
						configProperties,
						SCHEDULER.WEBSERVICE_REPORTS_NOTIFICATION_MSG);
				logger.info("reportsEmailList :"+reportsEmailList);
				
				messageContent = this.addWSReportSummaryToMessageContent(
						messageContent, reportsEmailList);
				String subject = CommonUtils.getPropertyFromPropertyFile(
						configProperties, SCHEDULER.WEBSERVICE_SUIT_SUBJECT);
				subject = subject + ExecutionStatus.COMPLETE.getStatus();

				emailUtil.sendEmail(recepientList, subject, messageContent);
			}
		}
	}

	private String addWSReportSummaryToMessageContent(String messageContent,
			List<ReportsEmail> reportsEmailList) {

		String reportSummaryContent = "<table style='display: table; padding: 5px 15px;'>";
		reportSummaryContent = reportSummaryContent
				+ "<tr><td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>Suit Name</td>";
		reportSummaryContent = reportSummaryContent
				+ "<td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>Service Name</td>";
		reportSummaryContent = reportSummaryContent
				+ "<td align='center'style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>Operation Name</td>";
		reportSummaryContent = reportSummaryContent
				+ "<td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>Environment Name</td>";
		reportSummaryContent = reportSummaryContent
				+ "<td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>Matched with Baseline</td>";
		reportSummaryContent = reportSummaryContent
				+ "<td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>Current Run Date Time</td>";
		reportSummaryContent = reportSummaryContent
				+ "<td align='center' style='background-color: #C3C7CA;color: #3A3C3E;padding: 5px 15px;vertical-align: top;font-size: 10px;'>Baseline Run Date Time</td></tr>";
		String suitName = "";
		for (ReportsEmail reportsEmail : reportsEmailList) {
			reportSummaryContent = reportSummaryContent
					+ "<tr><td align='center'>"
					+ reportsEmail.getWebserviceSuitName() + "</td>";
			reportSummaryContent = reportSummaryContent + "<td align='center'>"
					+ reportsEmail.getServiceName() + "</td>";
			reportSummaryContent = reportSummaryContent + "<td align='center'>"
					+ reportsEmail.getOperationName() + "</td>";
			reportSummaryContent = reportSummaryContent + "<td align='center'>"
					+ reportsEmail.getEnvironmentName() + "</td>";
			reportSummaryContent = reportSummaryContent + "<td align='center'>"
					+ reportsEmail.getMatchedwithBasedline() + "</td>";
			reportSummaryContent = reportSummaryContent + "<td align='center'>"
					+ reportsEmail.getCurrentRunExecutionTime() + "</td>";
			reportSummaryContent = reportSummaryContent + "<td align='center'>"
					+ reportsEmail.getBaseLineRunExecutionTime() + "</td></tr>";
			
			suitName = reportsEmail.getWebserviceSuitName();
		}
		reportSummaryContent = reportSummaryContent + "</table>";
		messageContent = messageContent
				.replace(SCHEDULER.REPORT_SUMMARY_PLACE_HOLDER,
						reportSummaryContent);
		messageContent = messageContent.replace(
				SCHEDULER.SUIT_NAME_PLACE_HOLDER, suitName
				);
		return messageContent;
	}

	private boolean sendEmail(boolean runStatus, String webserviceSuiteName,
			String emailIds, String[] emailTriggerOption, String messageContent) {
		boolean status = false;
		try {
			if (null != emailIds && emailIds.length() > 0) {
				EmailUtil emailUtil = null;
				emailUtil = WebserviceUtil.configureEmailForWebserviceTesting(
						webserviceSuiteName, runStatus, emailIds,
						configProperties, messageContent);

				emailUtil.sendEmail();
				status = true;
			}

		} catch (Exception e) {
			logger.error("Exception in sendEmail.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		return status;

	}

	private WSReports getWebServiceResponseCompareDifference(String key,
			Map<String, WSResults> currentMap,
			Map<String, WSResults> baselineMap) {
		logger.debug("Entry: getWebServiceResponseCompareDifference");

		WSReports wsReport = new WSReports();
		WSResults currentResults = currentMap.get(key);
		WSResults baselineResults = baselineMap.get(key);

		Date now = new Date();
		wsReport.setGeneratedDateTime(now);
		wsReport.setGeneratedDateTime(now);
		wsReport.setOperationId(currentResults.getOperationId());
		wsReport.setParameterSetId(currentResults.getParameterSetId());
		wsReport.setWsScheduleId(currentResults.getWsScheduleId());
		wsReport.setWebserviceSuiteId(currentResults.getWebserviceSuiteId());
		if (null != baselineResults) {
			wsReport.setWsBaselineScheduleId(baselineResults.getWsScheduleId());

			if (null != baselineResults.getResultXml()) {
				Document currentXML = stringToDom(currentResults.getResultXml());
				Document baselineXML = stringToDom(baselineResults
						.getResultXml());

				String differences = printDifferences(currentXML, baselineXML);
				if (differences.equalsIgnoreCase("")) {
					differences = "-";
				}
				logger.info("differences :" + differences);
				wsReport.setExecutionStatusRefId(3);
				wsReport.setComparisonResultsContent(differences);
				wsReport.setMatchedWithBaseline(true);
				if (!differences.equalsIgnoreCase("-")) {
					wsReport.setMatchedWithBaseline(false);
				}

			} else {
				wsReport.setExecutionStatusRefId(5);
				wsReport.setComparisonResultsContent("Baseline Response NOT Available");
			}
		} else {
			logger.error("Baseline schedule not available for currentResults :"
					+ currentResults);
		}

		logger.debug("Exit: getWebServiceResponseCompareDifference");
		return wsReport;
	}

	private String printDifferences(Document doc1, Document doc2) {
		StringBuffer diffinString = new StringBuffer();
		logger.debug("Entry: printDifferences ");
		// list of differences
		StringBuilder respStr = new StringBuilder();
		String finaldiff = "";

		try {
			Diff diff = new Diff(doc1, doc2);
			DetailedDiff detailedDiff = new DetailedDiff(diff);
			List<Difference> differenceList = detailedDiff.getAllDifferences();
			Set<String> diffSet = new HashSet<String>();

			logger.info("" + differenceList.size()
					+ " differences including header");
			Iterator<Difference> differences = differenceList.iterator();

			while (differences.hasNext()) {
				try {
					Difference difference = (Difference) differences.next();

					// logger.info("difference.getDescription() :"+difference.getDescription());
					// logger.info("difference.getControlNodeDetail().getValue() :"+difference.getControlNodeDetail().getValue());
					// logger.info("difference.getControlNodeDetail().getNode().getParentNode().getNodeName() :"+difference.getControlNodeDetail().getNode().getParentNode().getNodeName());

					// below if condition is to exclude header in the comparison
					if (null != difference.getControlNodeDetail()
							&& null != difference.getControlNodeDetail()
									.getValue()
							&& null != difference.getControlNodeDetail()
									.getXpathLocation()
							&& !difference.getControlNodeDetail()
									.getXpathLocation().toString()
									.contains("Header")) {

						// values of tags:
						String node1Value = null;
						try {
							node1Value = difference.getControlNodeDetail()
									.getNode().getNodeValue();
						} catch (NullPointerException e) {
							logger.info("Null pointer exception node1Value :"
									+ node1Value);
						}
						String node2Value = null;

						try {
							node2Value = difference.getTestNodeDetail()
									.getNode().getNodeValue();
						} catch (NullPointerException e) {
							logger.info("Null pointer exception node2Value :"
									+ node2Value);
						}

						// node names - like PIN /ssn
						String parentTagName = difference
								.getControlNodeDetail().getNode()
								.getParentNode().getNodeName();
						String tagName = parentTagName.substring(
								parentTagName.lastIndexOf(":") + 1,
								parentTagName.length());

						if (null != tagName) {
							diffinString.append("\nValue for the :" + tagName
									+ " different from baseline.");
							diffSet.add("Value for the :" + tagName
									+ " different from baseline.");
						}

						if (null != node2Value
								&& node2Value.trim().length() > 0) {
							diffSet.add("Baseline Value :" + node2Value + ". ");
							diffinString.append("\nBaseline Value :"
									+ node2Value);
						}
						if (null != node1Value
								&& node1Value.trim().length() > 0) {
							diffSet.add("Current Value  :" + node1Value);
							diffinString.append("\nCurrent Value  :"
									+ node1Value);
						}

						if (null != diffinString) {
							respStr.append(diffinString);
							respStr.append(System.getProperty("line.separator"));

						}

					}
				} catch (Exception e) {
					logger.error("Exception in printDifferences.");
					logger.error("Stack Trace :"
							+ ExceptionUtils.getStackTrace(e));
				}
			}

			logger.info("respStr :" + respStr);
			logger.info("diffinString :" + diffinString);
			logger.info("diffSet :" + diffSet);

			int i = 0;
			for (String s : diffSet) {
				if (i == 0) {
					finaldiff = s;
				} else {
					finaldiff += "\n" + s;
				}
				i++;
			}
			logger.info("finaldiff :" + finaldiff);

		} catch (Exception e) {
			logger.error("Exception in printDifferences.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			finaldiff = "";
		}

		logger.debug("Exit: printDifferences ");
		return finaldiff;
	}

	public Document stringToDom(String xmlSource) {
		//logger.info("Entry : stringToDom");
		Document doc = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new InputSource(new StringReader(xmlSource)));

		} catch (Exception e) {
			logger.error("Exception in stringToDom.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));

		}
		//logger.info("Exit : stringToDom");
		return doc;
	}

}