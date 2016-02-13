package com.ensense.insense.services.scheduler.webservice;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.pojoxml.core.PojoXml;
import org.pojoxml.core.PojoXmlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cts.mint.common.entity.Users;
import com.cts.mint.common.service.UserService;
import com.cts.mint.common.utils.CommonUtils;
import com.cts.mint.common.utils.Constants.SCHEDULER;
import com.cts.mint.common.utils.ExecutionStatus;
import com.cts.mint.reports.service.ScheduledService;
import com.cts.mint.uitesting.service.EnvironmentService;
import com.cts.mint.util.BrowserDriverLoaderUtil;
import com.cts.mint.util.DateTimeUtil;
import com.cts.mint.util.EmailUtil;
import com.cts.mint.util.FileDirectoryUtil;
import com.cts.mint.webservice.entity.WSExecutionStatus;
import com.cts.mint.webservice.entity.WSPingResults;
import com.cts.mint.webservice.entity.WSPingSchedule;
import com.cts.mint.webservice.entity.WSReports;
import com.cts.mint.webservice.entity.WSResults;
import com.cts.mint.webservice.entity.WSSchedule;
import com.cts.mint.webservice.entity.WebserviceOperations;
import com.cts.mint.webservice.entity.WebserviceSuite;
import com.cts.mint.webservice.entity.WebserviceSuiteService;
import com.cts.mint.webservice.entity.Webservices;
import com.cts.mint.webservice.entity.WsOperationHeaderParameters;
import com.cts.mint.webservice.entity.WsOperationParameter;
import com.cts.mint.webservice.entity.WsOperationXmlParameter;
import com.cts.mint.webservice.model.ServiceDetails;
import com.cts.mint.webservice.model.ServiceStatus;
import com.cts.mint.webservice.model.ServiceUrl;
import com.cts.mint.webservice.model.WsDataset;
import com.cts.mint.webservice.service.WebserviceTestingService;
import com.cts.mint.webservice.util.ESBWebServiceCrawler;
import com.cts.mint.webservice.util.RestServiceUtil;
import com.cts.mint.webservice.util.WebserviceUtil;
import com.eviware.soapui.support.types.StringToStringMap;
@Service
public class WebserviceExecutionScheduler {
	private static Logger logger = Logger
			.getLogger(WebserviceExecutionScheduler.class);
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
	private UserService userService;
	
	@Autowired
	private MessageSource schedulerProperties;
	
	@Scheduled(fixedDelayString = "${mint.scheduler.webservice.execution.delaytime}")
	public void executeWSPendingTests(){
		//logger.debug("Entry: executeWSPendingTests");
		
		try {
			if (! Boolean.parseBoolean(CommonUtils.getPropertyFromPropertyFile(
					schedulerProperties, SCHEDULER.WEBSERVICE_EXECUTION_SCHEDULER_ENABLE))) {
				//Schedule Job is disabled. Do nothing.
				return;
			}
		} catch (Exception e) {
			logger.error("Unable to start the Webservice execution Scheduler.");
			e.printStackTrace();
		}
		
		boolean anyPendingWSSuites = false;
		List<WSExecutionStatus> wSExecutionStatusList = new ArrayList<WSExecutionStatus>();
		anyPendingWSSuites = webserviceTestingService.isWSSchedulePresent();
		if (anyPendingWSSuites) {
			wSExecutionStatusList = webserviceTestingService.getWSExecutionStatusList(); 
			List<WSSchedule> wsSchedules = webserviceTestingService.pendingSchedules();
			processTestSuites(wSExecutionStatusList, wsSchedules);
		}
		//logger.debug("Exit: executeWSPendingTests");
	}

	private void processTestSuites(List<WSExecutionStatus> wsExecutionList, List<WSSchedule> wsSchedules) {
		
		
		Webservices webservice = new Webservices();
		WebserviceOperations webserviceOperations = new WebserviceOperations();
		Map<String, String> parametersMap = new LinkedHashMap<String, String>();
		String requestxml = null, responseXml = null;
		StringEntity stringEntity = null; 
		WSSchedule wsSchedule = null; String filepath = "";
		int testExecutionStatusRefId = 5; // 5 - failed
		boolean runStatus = false;
		boolean baseLineFound = false;String environmentName = null;
		try {
			boolean testExecutionResult = true, runStatusResult = true;
			WSResults wsResult = null;
			WebserviceSuite webserviceSuite = null;
			String emailSMTPHost = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.email.smtp.host");
			String emailFrom = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.email.sender");
			
			EmailUtil emailUtil = new EmailUtil(emailSMTPHost, emailFrom);
			
			String messageContent = " Hi, </br></br>";
			WSReports wsReport = null;
			for(WSSchedule wsScheduleObj: wsSchedules){
				
				messageContent = CommonUtils.getPropertyFromPropertyFile(
						configProperties,
						SCHEDULER.WEBSERVICE_REPORTS_NOTIFICATION_MSG);
				int count = 1; 
				for (WSExecutionStatus wstestsuite : wsExecutionList) {
					if(wstestsuite.getWsScheduleId() == wsScheduleObj.getWsScheduleId()){
						
				wsSchedule = webserviceTestingService.getWSSchedule(wstestsuite.getWsScheduleId());
				Integer environmentId = webserviceTestingService.getEnvironment(wstestsuite.getWebserviceSuiteId());
				webserviceSuite = webserviceTestingService.
				getWebserviceSuite(wstestsuite.getWebserviceSuiteId());
				/*if (count == 1) {
					messageContent = messageContent + "<table><tr><th><b>Testsuite Name</b></th><th>" + webserviceSuite.getWebserviceSuiteName() +"</th></tr>";
				} else {
					messageContent = messageContent + "<table>";
				}*/
				
				String resultXml = null;
				webservice = webserviceTestingService.getServicefromServiceId(
						wstestsuite.getServiceId());
				String serviceName = webservice.getServiceName();
				String serviceType = webservice.getServiceType();
				webserviceOperations.setOperationId(wstestsuite
						.getOperationId());
				webserviceOperations = webserviceTestingService
				.getOperationfromOperationId(webserviceOperations);
				String methodType = webserviceOperations.getMethodType();
				String contentType = webserviceOperations.getContentType();
				
				
				String operationName = webserviceOperations.getOperationName();

				Integer reqdOprId = webserviceTestingService.getOperationIdOfNullEnv(
						wstestsuite.getServiceId(),
						wstestsuite.getOperationId());
		
				Integer environmentOperationId = webserviceTestingService
						 		.getOperationIdFromNameAndEnvId(
								wstestsuite.getOperationId(),
								wstestsuite.getServiceId(), environmentId);
				StringToStringMap customHeaders = new StringToStringMap();
				List<WebserviceSuiteService> testWebserviceSuiteService = 
					webserviceTestingService.getWebserviceSuiteService(wstestsuite.getWebserviceSuiteId());
				
				if(wstestsuite.getInputType().equalsIgnoreCase("rawinput")){
					WsDataset datsets = webserviceTestingService.getParameterValues(
							environmentId, wstestsuite.getParameterSetId(),
							reqdOprId);
					parametersMap = datsets.getParameterValuesMap();
					
					customHeaders = webserviceTestingService.getCustomHeadersValues(
							environmentId, wstestsuite.getParameterSetId(),
							reqdOprId);
				} else {
					WsOperationXmlParameter wsOperationXmlParameter = 
						webserviceTestingService.getTestSoapParameters(wstestsuite.getParameterSetId());
					requestxml = wsOperationXmlParameter.getParameterValue();
					List<WsOperationHeaderParameters> headers = webserviceTestingService.
					getTestSoapParameterHeaders(wstestsuite.getParameterSetId());
					for(WsOperationHeaderParameters header :headers) {
						customHeaders.put(header.getParameterName(), header.getParameterValue());
					}
				}
				if (serviceType.equalsIgnoreCase("wsdl")) {
					filepath = FileDirectoryUtil.getWSDLFilePath(configProperties);
					
					String WSDLFilePath = filepath + File.separator
							+ serviceName + ".wsdl";
					if(wstestsuite.getInputType().equalsIgnoreCase("rawinput")){
						requestxml =  WebserviceUtil.createSOAPreq(WSDLFilePath,
								serviceName, operationName, parametersMap);
					}
					String endPoint = webserviceTestingService
							.getEndPoint(environmentId, serviceName).getEndpoint();

					responseXml = WebserviceUtil.executeWebServiceOperation(WSDLFilePath,
									requestxml, serviceName, operationName,
									endPoint, environmentId, customHeaders);
				} else {

					filepath = FileDirectoryUtil
					.getWADLFilePath(configProperties);

					String WADLFilePath = filepath + "\\"+ serviceName + ".wadl";
					Map<String, String> requestValuesMapRest = new LinkedHashMap<String, String>();
					HttpParams queryParams = new BasicHttpParams();
					String newPath = "";
					webserviceOperations.setOperationId(wstestsuite
							.getOperationId());
								
					
					Set mapSet = (Set) parametersMap.entrySet();
					Iterator mapIterator = mapSet.iterator();
					while (mapIterator.hasNext()) {
						Map.Entry mapEntry = (Map.Entry) mapIterator.next();
						String parameter = (String) mapEntry.getKey();
						String parameterVal = (String) mapEntry.getKey();
						logger.info("Parameter Name:" + parameter);
						logger.info("Parameter Value:" + parameterVal);
						if (parameter.equalsIgnoreCase("reqXML")) {
							stringEntity = new StringEntity(parameterVal);
						} else {
							newPath = operationName.replace("{" + parameter
									+ "}", parameterVal);
							if (newPath.equalsIgnoreCase(operationName)) {
								requestValuesMapRest.put(parameter,parameterVal);
								queryParams.setParameter(parameter,parameterVal);
							} else {
								operationName = newPath;
							}
						}
						
					}
					
					String endPoint = webserviceTestingService.getEndPoint(
							environmentId, serviceName).getEndpoint();
					// get http headers
					WsOperationParameter testHeaders = new WsOperationParameter();
					testHeaders.setServiceId(webservice.getServiceId());
					reqdOprId = webserviceTestingService.getOperationIdOfNullEnv(
							webservice.getServiceId(), webserviceOperations.getOperationId());
					testHeaders.setOperationId(reqdOprId);
					List<WsOperationParameter> headers = webserviceTestingService
							.getRequestHeaders(testHeaders);
					String requestPath= "";
					if (!operationName.startsWith("/") && !endPoint.endsWith("/"))
						requestPath = endPoint + "/" + operationName;
						else 
							requestPath = endPoint + operationName;	

					responseXml = RestServiceUtil.getRestServiceResponse(
							requestPath, methodType, queryParams, contentType,
							stringEntity, headers, environmentId, customHeaders);
				}
				
				wsResult = new WSResults();
				if (requestxml != null && !requestxml.isEmpty()) {
					wsResult.setRequestXml(requestxml);
				} else {
					wsResult.setRequestXml("-");

				}

				if (responseXml != null && !responseXml.isEmpty()) {
					wsResult.setResultXml(responseXml);
				} else {
					wsResult.setResultXml("-");

				}
				
				Date now = new Date();
				wsResult.setCreatedDateTime(now);
			//	wsResult.setCreatedTime(now);

				if (responseXml != null) {
					testExecutionStatusRefId = 3;// 3 - completed
					runStatus = true;
				}
				wsResult
						.setExecutionStatusRefId(testExecutionStatusRefId);
				wsResult.setWsExecutionStatusId(wstestsuite
						.getWsExecutionStatusId());
				wsResult.setWsScheduleId(wstestsuite
						.getWsScheduleId());
				wsResult.setWebserviceSuiteId(wstestsuite
						.getWebserviceSuiteId());
				wsResult.setServiceId(wstestsuite.getServiceId());
				wsResult.setOperationId(wstestsuite.getOperationId());
				wsResult.setParameterSetId(wstestsuite
						.getParameterSetId());
				
				boolean insertResult = webserviceTestingService
				.insertWSTestResults(wsResult);
				
				logger.info("insertResult: " + insertResult);
				testExecutionResult =insertResult;
				runStatusResult = runStatus;
				try{

				/*	SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
					SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
					String date = "";
					String time = ""*/;
					
					/*date = dateFormat.format(wsResult.getCreatedDate());
					time = timeFormat.format(wsResult.getCreatedTime());*/
					//String executionTime = date + " " + time;
					
					
				
						if(insertResult){
							wsReport =  new WSReports();
							wsReport.setWsBaselineScheduleId(0);
							wsReport.setComparisonResultsContent("-");
							wsReport.setMatchedWithBaseline(false);
							wsReport.setGeneratedDateTime(wsResult.getCreatedDateTime());
							//wsReport.setGeneratedTime(wsResult.getCreatedTime());					
							wsReport.setOperationId(wsResult.getOperationId());
							wsReport.setParameterSetId(wsResult.getParameterSetId());
							wsReport.setWsScheduleId(wsResult.getWsScheduleId());
							wsReport.setWebserviceSuiteId(wsResult.getWebserviceSuiteId());
							wsReport.setExecutionStatusRefId(wsResult.getExecutionStatusRefId());
							Boolean insertStatus = webserviceTestingService
							.wsSaveComparisonResults(wsReport);
							environmentName = environmentService.getEnvironmentCategoryById(environmentId).
							getEnvironmentCategoryName();
							messageContent = this.addWSReportSummaryToMessageContent(serviceName, operationName, environmentName,wsReport, messageContent);
						}
				}catch(Exception e){
					logger.error("Exception while sending mail with completetion details ");
					runStatus = false;
				}
			
				boolean setCompletionResult = webserviceTestingService.setWSCompletion(
						wstestsuite, runStatus);
							
				if(count == webserviceTestingService.getWSExecutionStatusListByScheduleId(wsSchedule.getWsScheduleId()).size()){
					wsSchedule.setCompareFlag(true);
					webserviceTestingService.updateWSSchedule(wsSchedule);	
				}
				count++;
				
				}
			}
				
				baseLineFound = webserviceTestingService.isBaseLineFound(wsScheduleObj
						.getWsScheduleId());
				if(!baseLineFound){
					Users user = userService.getMintUser(wsSchedule.getUserId());
					List<String> recepientList = new ArrayList<String>();
					String[] emailAddr = wsSchedule.getEmailIds().split(",");
					recepientList = Arrays.asList(emailAddr);
					//recepientList.add(user.getEmailId());
					if(runStatus){
						
						if ( null != recepientList && recepientList.size() > 0 ){
						String subject = CommonUtils.getPropertyFromPropertyFile(configProperties,
								SCHEDULER.WEBSERVICE_SUIT_SUBJECT);
						subject = subject + ExecutionStatus.COMPLETE.getStatus();
						emailUtil.sendEmail(recepientList, subject, messageContent);
						}
					} else {
						messageContent = "";
						messageContent = CommonUtils.getPropertyFromPropertyFile(
								configProperties,
								SCHEDULER.WEBSERVICE_REPORTS_NOTIFICATION_ERROR_MSG);
						String subject = CommonUtils.getPropertyFromPropertyFile(configProperties,
								SCHEDULER.WEBSERVICE_SUIT_SUBJECT);
						subject = subject + ExecutionStatus.FAILED.getStatus();
						emailUtil.sendEmail(recepientList, subject, messageContent);
					}
				}
			}
			
		} catch (Exception e) {
			logger.error("Exception in processTestSuites.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		
		
	}
	
	private String addWSReportSummaryToMessageContent(String serviceName,
			String operationName, String environmentName, WSReports wsReport, String messageContent) {
		String reportSummaryContent = "";
		if(wsReport!=null){
			WebserviceSuite webserviceSuite = webserviceTestingService.getWebserviceSuite(wsReport.getWebserviceSuiteId());
			reportSummaryContent = reportSummaryContent + "<table><tr><td scope='row' align='right'>Suit Name :</td><td>"+webserviceSuite.getWebserviceSuiteName()+"</td></tr>";
			reportSummaryContent = reportSummaryContent + "<tr><td scope='row' align='right'>Service Name :</td><td>"+ serviceName + "</td></tr>";
			reportSummaryContent = reportSummaryContent + "<tr><td scope='row' align='right'>Operation Name :</td><td>"+ operationName + "</td></tr>";
			reportSummaryContent = reportSummaryContent + "<tr><td scope='row' align='right'>environment Name :</td><td>"+ environmentName + "</td></tr>";
			reportSummaryContent = reportSummaryContent + "<tr><td scope='row' align='right'>Execution Time :</td><td>"+ wsReport.getGeneratedDateTime() + "</td></tr>";
			reportSummaryContent = reportSummaryContent + "<tr><td scope='row' align='right'>Baseline Run time :</td><td>No baseline Run</td></tr>";
			reportSummaryContent = reportSummaryContent + "</table>";
			messageContent = messageContent.replace(SCHEDULER.REPORT_SUMMARY_PLACE_HOLDER, reportSummaryContent);
			messageContent = messageContent.replace(SCHEDULER.SUIT_NAME_PLACE_HOLDER, webserviceSuite.getWebserviceSuiteName());
		
		}
		return messageContent;
	}

	@Scheduled(fixedDelay = 30000)
	public void executePingTest(){
		//logger.debug("Entry: executePingTest");
		WebDriver driver = null;
		try {
			String emailSMTPHost = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.email.smtp.host");
			String emailFrom = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.email.sender");
			
			EmailUtil emailUtil = new EmailUtil(emailSMTPHost, emailFrom);
			
			List<WSPingSchedule> listWSPingSchedule = webserviceTestingService
			.getPingSchedules();
			if ( null != listWSPingSchedule && listWSPingSchedule.size() > 0 ){
				
				WSPingSchedule wsPingTestSchedule = listWSPingSchedule.get(0);
				String serviceNames = wsPingTestSchedule.getServiceNames();

				String[] serviceNameArray = serviceNames.split(",");
				List<ServiceUrl> serviceUrlList = new ArrayList<ServiceUrl>();
				
				for (String serviceName : serviceNameArray) {
					ServiceUrl serviceUrl = getEsbPingServiceDetails(serviceName,
					wsPingTestSchedule);
					serviceUrl.setEnvironment(wsPingTestSchedule.getEnvironment());
					serviceUrlList.add(serviceUrl);
				}
				
				if ( serviceUrlList.size() > 0 ) {
					try {
						BrowserDriverLoaderUtil driverLoader = new BrowserDriverLoaderUtil();
						driver = driverLoader.getFirefoxDriver();
						ESBWebServiceCrawler esbWebServiceCrawler = new ESBWebServiceCrawler(
								driver);
						for (ServiceUrl service : serviceUrlList) {
							if (null != service.getEsbPingUrl()
									&& service.getEsbPingUrl().length() > 0) {
								String htmlSource = esbWebServiceCrawler
										.getHtmlSourceForEsbPingUrl(service
												.getEsbPingUrl());
								service.setHtmlSource(StringEscapeUtils
										.escapeXml(htmlSource));
		
								service.setStatus(WebserviceUtil
										.esbPingServiceStatus(htmlSource));
							} else {
								service.setServiceFound(false);
							}
						}
					} catch (Exception e) {
						logger.error("Exception in executePingTest.");
						logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
					} finally{
						if ( driver != null ){
							driver.close();
						}
					}
					
					insertPingResults(serviceUrlList, wsPingTestSchedule);
					
				
						/*String[] emailTriggerOption = null;
						if ( wsPingTestSchedule.isEmailOnlyOnFailure() ){
							emailTriggerOption[0] = "failure";
						}
						if ( wsPingTestSchedule.isEmailOnlyOnCompletion()){
							emailTriggerOption[1] = "completion";
						}*/
						List<String> recepientList = new ArrayList<String>();
						String[] emailAddr = wsPingTestSchedule.getEmailIds().split(",");
						recepientList = Arrays.asList(emailAddr);
						//recepientList.add(user.getEmailId());
						String messageContent = "";
						if ( null != recepientList && recepientList.size() > 0 ){
							String subject = CommonUtils.getPropertyFromPropertyFile(configProperties,
									SCHEDULER.WEBSERVICE_ESBPING_SUBJECT);
							subject = subject + ExecutionStatus.COMPLETE.getStatus();
							messageContent = CommonUtils.getPropertyFromPropertyFile(
									configProperties,
									SCHEDULER.WEBSERVICE_ESBPING_NOTIFICATION_MSG);
							messageContent = WebserviceUtil.getEmailContentForESBPingTest(serviceUrlList);				
							emailUtil.sendEmail(recepientList, subject, messageContent);
						}
					}
				
			}
			
		} catch (Exception e) {
			logger.error("Exception in executePingTest.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		
		//logger.debug("Exit: executePingTest");
	}
	
	
	private void insertPingResults(List<ServiceUrl> serviceUrlList,
			WSPingSchedule wsPingTestSchedule) {
		logger.info("Entry :insertPingResults");
		PojoXml pojoxml = PojoXmlFactory.createPojoXml();
		ServiceDetails serviceDetails = new ServiceDetails();
		List<ServiceStatus> serviceStatusList = new ArrayList<ServiceStatus>();
		WSPingResults wsPingResults = new WSPingResults();
		for ( ServiceUrl serviceUrl : serviceUrlList){
			ServiceStatus service = new ServiceStatus();
			service.setServiceName(serviceUrl.getServiceName());
			
			if ( serviceUrl.isStatus() ){
				service.setStatus("SUCCESS");
			}else if ( ! serviceUrl.isServiceFound() ){
				service.setStatus("SERVICE Not Found");
			}else {
				service.setStatus("FAILED");
			}
			service.setHtmlSource(serviceUrl.getHtmlSource());
			serviceStatusList.add(service);
		}
		
		serviceDetails.setService(serviceStatusList);
		String serviceDetailsXml = pojoxml.getXml(serviceDetails);
		wsPingResults.setWsPingScheduleId(wsPingTestSchedule.getWsPingScheduleId());
		wsPingResults.setEnvironmentId(wsPingTestSchedule.getEnvironment());
		wsPingResults.setStartDate(new Date());
		
		logger.info("Exit :insertPingResults");
		wsPingResults.setStartTime(DateTimeUtil.getTimeInFormatHHSS(new Date()));
		//wsPingResults.setResultsXml(serviceDetailsXml);
		
		boolean status = webserviceTestingService.insertWSPingResults(wsPingResults, serviceDetailsXml);
		
		if ( !status ){
			logger.error("Error while inserting row in ws_ping_results_table, wsPingResults->"+wsPingResults);
		}
	}

	public boolean sendEmail(boolean runStatus, String testWebserviceSuiteName, String emailAddresses,String messageContent) {
		boolean status = false;
		try{
			
			if(emailAddresses.length() > 0){
					
					EmailUtil emailUtil = null;
					emailUtil = WebserviceUtil.configureEmailForWebserviceTesting(testWebserviceSuiteName, runStatus, emailAddresses,  configProperties,  messageContent);
					emailUtil.sendEmail();
					status = true;
			}
		

		} catch (Exception e) {
			logger.error("Exception in sendEmail.");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}

		return status;
	}
	
	private ServiceUrl getEsbPingServiceDetails(String serviceName,
			WSPingSchedule wsPingSchedule) {
		logger.info("Entry : getEsbPingServiceDetails");
		ServiceUrl serviceUrl = new ServiceUrl();
		String esbPingUrl = "";
		String esbServiceProperty = "";

		if (null != wsPingSchedule.getEnvironment()
				&& wsPingSchedule.getEnvironment().length() > 0
				&& null != serviceName && serviceName.length() > 0) {
			serviceUrl.setServiceName(serviceName);
			esbServiceProperty = wsPingSchedule.getEnvironment() + "_"
					+ serviceName;
			logger.info("esbServiceProperty :" + esbServiceProperty);
			try {
				esbPingUrl = esbpingUrlProperty.getMessage(esbServiceProperty,
						null, Locale.getDefault());
				String[] property = esbPingUrl.split(",");
				serviceUrl.setEnvironment(wsPingSchedule.getEnvironment());
				serviceUrl.setEsbPingUrl(property[3]);
				serviceUrl.setServiceFound(true);
			} catch (Exception e) {
				serviceUrl.setServiceFound(false);
			}
		} else {

		}

		logger.info("Exit : getEsbPingServiceDetails, serviceUrl->"
				+ serviceUrl);
		return serviceUrl;
	}
}
