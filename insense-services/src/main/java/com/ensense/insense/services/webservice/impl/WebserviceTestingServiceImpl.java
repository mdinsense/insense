package com.ensense.insense.services.webservice.impl;

import com.ensense.insense.data.webservice.dao.WebserviceTestingDAO;
import com.ensense.insense.data.webservice.entity.*;
import com.ensense.insense.data.webservice.model.WSReportsData;
import com.ensense.insense.data.webservice.model.WSSuiteDetails;
import com.ensense.insense.data.webservice.model.WebserviceSetupForm;
import com.ensense.insense.data.webservice.model.WsDataset;
import com.ensense.insense.services.webservice.WebserviceTestingService;
import com.eviware.soapui.support.types.StringToStringMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
public class WebserviceTestingServiceImpl implements WebserviceTestingService {

	private static Logger logger = Logger.getLogger(WebserviceTestingServiceImpl.class);
	
	@Autowired
	WebserviceTestingDAO webserviceTestingDAO;
	
	@Override
	@Transactional
	public List<Webservices> getAllWebservices() {
		logger.debug("Entry And Exit : getAllWebservices");
		return webserviceTestingDAO.getAllWebservices();
	}

	@Override
	@Transactional
	public Webservices getServicefromServiceName(String serviceName) {
		logger.debug("Entry And Exit : getServicefromServiceName");
		return webserviceTestingDAO.getServicefromServiceName(serviceName);
	}
	
	@Override
	@Transactional
	public Boolean insertServiceNames(String serviceName, String serviceType,
			String filePath) {
		logger.debug("Entry And Exit : insertServiceNames");
		return webserviceTestingDAO.insertServiceNames(serviceName, serviceType, filePath );
	}

	@Override
	@Transactional
	public Boolean insertOperationNames(String operationName,
			Integer serviceId, String methodType, String contentType) {
		logger.debug("Entry And Exit : insertOperationNames");
		return webserviceTestingDAO.insertOperationNames(operationName, serviceId, methodType, contentType );
	}

	@Override
	@Transactional
	public WebserviceOperations getOperationIdfromOperationName(
			Integer serviceId, String operationName, String methodType) {
		logger.debug("Entry And Exit : getOperationIdfromOperationName");
		return webserviceTestingDAO.getOperationIdfromOperationName(serviceId, operationName, methodType);
	}

	@Override
	@Transactional
	public Boolean insertRequestParameterName(
			WsOperationParameter wsOperationParameter, String sampleXML) {
		logger.debug("Entry And Exit : insertRequestParameterName");
		return webserviceTestingDAO.insertRequestParameterName(wsOperationParameter, sampleXML);
	}

	@Override
	@Transactional
	public List<WebserviceOperations> getOperationsByService(
			Webservices webservice) {
		logger.debug("Entry And Exit : getOperationsByService");
		return webserviceTestingDAO.getOperationsByService(webservice);
	}

	@Override
	@Transactional
	public Boolean insertEndpointDetails(WsEndpointDetails wsEndpointDetails) {
		logger.debug("Entry And Exit : insertEndpointDetails");
		return webserviceTestingDAO.insertEndpointDetails(wsEndpointDetails);
	}

	@Override
	@Transactional
	public Integer insertOperationWithEnvId(int operationId, int serviceId,
			int environmentId, String methodType, String contentType) {
		logger.debug("Entry And Exit : insertOperationWithEnvId");
		return webserviceTestingDAO.insertOperationWithEnvId(operationId,
				serviceId, environmentId, methodType, contentType);
	}

	@Override
	@Transactional
	public WebserviceOperations getOperationfromOperationId(
			WebserviceOperations webserviceOperations) {
		logger.debug("Entry And Exit : getOperationfromOperationId");
		return webserviceTestingDAO.getOperationfromOperationId(webserviceOperations);
	}

	@Override
	@Transactional
	public Integer getOperationIdFromNameAndEnvId(Integer operationId,
			int serviceId, int environmentId) {
		logger.debug("Entry And Exit : getOpearationIdFromNameAndEnvId");
		return webserviceTestingDAO.getOperationIdFromNameAndEnvId(operationId, serviceId, environmentId);
	}

	@Override
	@Transactional
	public Boolean deleteOperations(Integer reqdOprId, int environmentId) {
		logger.debug("Entry And Exit : deleteOperations");
		return webserviceTestingDAO.deleteOperations(reqdOprId, environmentId);
	}

	@Override
	@Transactional
	public Webservices getServicefromServiceId(int serviceId) {
		logger.debug("Entry And Exit : getServicefromServiceId");
		return webserviceTestingDAO.getServicefromServiceId(serviceId);
	}

	@Override
	@Transactional
	public WsEndpointDetails getEndPoint(int environmentId, String serviceName) {
		logger.debug("Entry And Exit : getEndPoint");
		return webserviceTestingDAO.getEndPoint(environmentId, serviceName);
	}

	@Override
	@Transactional
	public List<WebserviceOperations> getOperationsFromServiceEnv(
			int serviceId, int environmentId) {
		logger.debug("Entry And Exit : getEndPoint");
		return webserviceTestingDAO.getOperationsFromServiceEnv(serviceId, environmentId);
	}

	@Override
	@Transactional
	public int getOperationIdOfNullEnv(int serviceId, int operationId) {
		logger.debug("Entry And Exit : getOperationIdOfNullEnv");
		return webserviceTestingDAO.getOperationIdOfNullEnv(serviceId, operationId);
	}

	@Override
	@Transactional
	public Map<String, Map<String, String>> getParameterSets(int reqdOprId,
			int environmentId) {
		logger.debug("Entry And Exit : getParameterSets");
		return webserviceTestingDAO.getParameterSets(reqdOprId, environmentId);
	}

	@Override
	@Transactional
	public   List<WsDataset> getTestSoapParameterSets(
			int reqdOprId, int environmentId) {
		logger.debug("Entry And Exit : getTestSoapParameterSets");
		return webserviceTestingDAO.getTestSoapParameterSets(reqdOprId, environmentId);
	}

	@Override
	@Transactional
	public List<WsEndpointDetails> getServiceByEnvId(int environmentId) {
		logger.debug("Entry And Exit : getServiceByEnvId");
		return webserviceTestingDAO.getServiceByEnvId( environmentId);
	}

	@Override
	@Transactional
	public List<WsOperationParameter> getRequestParamNames(
			WsOperationParameter wsOperationParameter) {
		logger.debug("Entry And Exit : getRequestParamNames");
		return webserviceTestingDAO.getRequestParamNames( wsOperationParameter);
	}

	@Override
	@Transactional
	public Integer getMaxTestParameterSetId() {
		logger.debug("Entry And Exit : getRequestParamNames");
		return webserviceTestingDAO.getMaxTestParameterSetId();
	}

	@Override
	@Transactional
	public boolean addTestOperationData(WsOperationParameter wsOperationParameter,
										Map<String, String> requestValuesMap,
										WsParameterAndSetId wsParameterAndSetId, Integer environmentId,
										Integer operationId, String datasetName) {
		logger.debug("Entry And Exit : getRequestParamNames");
		return webserviceTestingDAO.addTestOperationData(wsOperationParameter, requestValuesMap, wsParameterAndSetId,
				environmentId,operationId, datasetName );
	}

	@Override
	@Transactional
	public WsOperationXmlParameter submitParameters(String requestXML,
			Integer environmentId, int operationId, Integer serviceId, String datasetName) {
		logger.debug("Entry And Exit : getRequestParamNames");
		return webserviceTestingDAO.submitParameters(requestXML, environmentId, operationId,
				serviceId,datasetName);
	}

	@Override
	@Transactional
	public Boolean insertRequestParameterHeaders(
			WsOperationHeaderParameters wsOperationHeaderParameters) {
		logger.debug("Entry And Exit : insertRequestParameterHeaders");
		return webserviceTestingDAO.insertRequestParameterHeaders(wsOperationHeaderParameters);
	}

	@Override
	@Transactional
	public List<WsOperationParameter> getRequestParamNamesForDataset(
			WsOperationParameter wsOperationParameter) {
		logger.debug("Entry And Exit : getRequestParamNamesForDataset");
		return webserviceTestingDAO.getRequestParamNamesForDataset(wsOperationParameter);
	}

	@Override
	@Transactional
	public WsOperationParameterValue getRequestParameterValue(
			WsParameterAndSetId wsParameterAndSetId) {
		logger.debug("Entry And Exit : getRequestParameterValue");
		return webserviceTestingDAO.getRequestParameterValue(wsParameterAndSetId);
	}

	@Override
	@Transactional
	public List<WsOperationParameter> getRequestHeaders(
			WsOperationParameter wsOperationParameter) {
		logger.debug("Entry And Exit : getRequestHeaders");
		return webserviceTestingDAO.getRequestHeaders(wsOperationParameter);
	}

	@Override
	@Transactional
	public WsOperationXmlParameter getTestSoapParameters(int paramSetId) {
		logger.debug("Entry And Exit : getRequestHeaders");
		return webserviceTestingDAO.getTestSoapParameters(paramSetId);
	}

	@Override
	@Transactional
	public List<WsOperationHeaderParameters> getTestSoapParameterHeaders(
			Integer parameterId) {
		logger.debug("Entry And Exit : getTestSoapParameterHeaders");
		return webserviceTestingDAO.getTestSoapParameterHeaders(parameterId);
	}

	@Override
	@Transactional
	public List<Integer> getTestParamSetIDsforOperation(int operationId) {
		logger.debug("Entry And Exit : getTestParamSetIDsforOperation");
		return webserviceTestingDAO.getTestParamSetIDsforOperation(operationId);
	}

	@Override
	@Transactional
	public Boolean insertTestWSSuite(WebserviceSuite webserviceSuite,
			List<WebserviceSetupForm> listWebserviceSetupForm) {
		logger.debug("Entry And Exit : insertTestWSSuite");
		return webserviceTestingDAO.insertTestWSSuite(webserviceSuite, listWebserviceSetupForm);
	}

	@Override
	@Transactional
	public Boolean insertWSSuiteService(Integer webserviceSuiteId,
			Integer serviceId, Integer operationId, String inputType) {
		logger.debug("Entry And Exit : insertTestWSSuite");
		return webserviceTestingDAO.insertWSSuiteService(webserviceSuiteId, serviceId,operationId, inputType);
	}

	@Override
	@Transactional
	public Boolean deleteServiceDetails(Integer serviceId) {
		logger.debug("Entry And Exit : deleteServiceDetails");
		return webserviceTestingDAO.deleteServiceDetails(serviceId);
	}

	@Override
	@Transactional
	public List<WebservicesPingTest> getWebservicesPingTestList() {
		logger.debug("Entry And Exit : deleteServiceDetails");
		return webserviceTestingDAO.getWebservicesPingTestList();
	}

	@Override
	@Transactional
	public boolean deletePingServices() {
		logger.debug("Entry And Exit : deleteServiceDetails");
		return webserviceTestingDAO.deletePingServices();
	}

	@Override
	@Transactional
	public List<WebservicesPingTest> getDuplicateService(String serviceName) {
		logger.debug("Entry And Exit : getDuplicateService");
		return webserviceTestingDAO.getDuplicateService(serviceName);
	}

	@Override
	@Transactional
	public boolean insertExcelServiceData(WebservicesPingTest webservicesPingTest) {
		logger.debug("Entry And Exit : insertExcelServiceData");
		return webserviceTestingDAO.insertExcelServiceData(webservicesPingTest);
	}

	@Override
	@Transactional
	public int insertWSPingSchedules(WSPingSchedule wSPingSchedule) {
		logger.debug("Entry And Exit : insertExcelServiceData");
		return webserviceTestingDAO.insertWSPingSchedules(wSPingSchedule);
	}

	@Override
	@Transactional
	public List<WSPingResults> getESBPingServiceDetails(String environmentId,
			String resultsId) {
		logger.debug("Entry And Exit : getESBPingServiceDetails");
		return webserviceTestingDAO.getESBPingServiceDetails(environmentId, resultsId);
	}

	@Override
	@Transactional
	public List<WSPingResults> getESBPingDates(String environment) {
		logger.debug("Entry And Exit : getESBPingDates");
		return webserviceTestingDAO.getESBPingDates(environment);
	}

	@Override
	@Transactional
	public List<WebserviceSuite> getWSTestSuites(int userid,
			int environment) {
		logger.debug("Entry And Exit : getWSTestSuites");
		return webserviceTestingDAO.getWSTestSuites(userid, environment);
	}

	@Override
	@Transactional
	public boolean isWSSchedulePresent() {
		//logger.debug("Entry And Exit : isWSSchedulePresent");
		return webserviceTestingDAO.isWSSchedulePresent();
	}

	@Override
	@Transactional
	public List<WSExecutionStatus> getWSExecutionStatusList() {
		logger.debug("Entry And Exit : getWSExecutionStatusList");
		return webserviceTestingDAO.getWSExecutionStatusList();
	}

	@Override
	@Transactional
	public WSSchedule getWSSchedule(int wsScheduleId) {
		logger.debug("Entry And Exit : getWSSchedule");
		return webserviceTestingDAO.getWSSchedule(wsScheduleId);
	}

	@Override
	@Transactional
	public Integer getEnvironment(Integer webserviceSuiteId) {
		logger.debug("Entry And Exit : getWSSchedule");
		return webserviceTestingDAO.getEnvironment(webserviceSuiteId);
	}

	@Override
	@Transactional
	public WebserviceSuite getWebserviceSuite(Integer webserviceSuiteId) {
		logger.debug("Entry And Exit : getWebserviceSuite");
		return webserviceTestingDAO.getWebserviceSuite(webserviceSuiteId);
	}

	@Override
	@Transactional
	public List<WebserviceSuiteService> getWebserviceSuiteService(
			Integer webserviceSuiteId) {
		logger.debug("Entry And Exit : getWebserviceSuiteService");
		return webserviceTestingDAO.getWebserviceSuiteService(webserviceSuiteId);
	}

	@Override
	@Transactional
	public WsDataset  getParameterValues(Integer environmentId,
			int parameterSetId, Integer operationId) {
		logger.debug("Entry And Exit : getWebserviceSuiteService");
		return webserviceTestingDAO.getParameterValues(environmentId, parameterSetId, operationId);
	}

	@Override
	@Transactional
	public StringToStringMap getCustomHeadersValues(Integer environmentId,
													int parameterSetId, Integer operationId) {
		logger.debug("Entry And Exit : getCustomHeadersValues");
		return webserviceTestingDAO.getCustomHeadersValues(environmentId, parameterSetId, operationId);
	}

	@Override
	@Transactional
	public boolean insertWSTestResults(WSResults wsResult) {
		logger.debug("Entry And Exit : insertWSTestResults");
		return webserviceTestingDAO.insertWSTestResults(wsResult);
	}

	@Override
	@Transactional
	public Boolean wsSaveComparisonResults(WSReports wsReport) {
		logger.debug("Entry And Exit : wsSaveComparisonResults, wsReport->"+wsReport);
		return webserviceTestingDAO.wsSaveComparisonResults(wsReport);
	}

	@Override
	@Transactional
	public boolean isBaseLineFound(int wsScheduleId) {
		logger.debug("Entry And Exit : isBaseLineFound");
		return webserviceTestingDAO.isBaseLineFound(wsScheduleId);
	}

	@Override
	@Transactional
	public boolean setWSCompletion(WSExecutionStatus wstestsuite,
			boolean runStatus) {
		logger.debug("Entry And Exit : setWSCompletion");
		return webserviceTestingDAO.setWSCompletion(wstestsuite, runStatus);
	}

	@Override
	@Transactional
	public boolean updateWSSchedule(WSSchedule wsSchedule) {
		logger.debug("Entry And Exit : updateWSSchedule");
		return webserviceTestingDAO.updateWSSchedule(wsSchedule);
	}

	@Override
	@Transactional
	public List<WSPingSchedule> getPingSchedules() {
		//logger.debug("Entry And Exit : getPingSchedules");
		return webserviceTestingDAO.getPingSchedules();
	}

	@Override
	@Transactional
	public boolean insertWSPingResults(WSPingResults wsPingResults, String resultXml) {
		logger.debug("Entry And Exit : insertWSPingResults");
		return webserviceTestingDAO.insertWSPingResults(wsPingResults, resultXml);
	}

	@Override
	@Transactional
	public List<WSBaseline> getSchedulesToBeCompared() {
		//logger.debug("Entry And Exit : getSchedulesToBeCompared");
		return webserviceTestingDAO.getSchedulesToBeCompared();
	}

	@Override
	@Transactional
	public WSResults getScheduleIdfromResultId(Integer wsResultId) {
		logger.debug("Entry And Exit : getScheduleIdfromResultId");
		return webserviceTestingDAO.getScheduleIdfromResultId(wsResultId);
	}

	@Override
	@Transactional
	public List<WSResults> getSchedulesFromScheduleId(Integer wsScheduleId) {
		logger.debug("Entry And Exit : getSchedulesFromScheduleId");
		return webserviceTestingDAO.getSchedulesFromScheduleId(wsScheduleId);
	}

	@Override
	@Transactional
	public int pickUpSchedules() {
		//logger.debug("Entry And Exit : pickUpSchedules");
		return webserviceTestingDAO.pickUpSchedules();
	}

	@Override
	@Transactional
	public List<WSReports> getWSReportDatesFromSuiteId(int wsSuiteId) {
		logger.debug("Entry And Exit : getWSReportDatesFromSuiteId");
		return webserviceTestingDAO.getWSReportDatesFromSuiteId(wsSuiteId);
	}

	@Override
	@Transactional
	public List<WSReportsData> getWSReports(int webserviceSuiteId,
											int wsScheduleId) {
		logger.debug("Entry And Exit : getWSReportDatesFromSuiteId");
		return webserviceTestingDAO.getWSReports(webserviceSuiteId, wsScheduleId);
	}

	@Override
	@Transactional
	public List<WSSuiteDetails> getWSSuiteDetails(int wsSuiteId) {
		logger.debug("Entry And Exit : getWSSuiteDetails");
		return webserviceTestingDAO.getWSSuiteDetails(wsSuiteId);
	}

	@Override
	@Transactional
	public Boolean getEndPointForService(Integer serviceId, int envId) {
		logger.debug("Entry And Exit : getEndPointForService");
		return webserviceTestingDAO.getEndPointForService(serviceId, envId);
	}

	@Override
	@Transactional
	public int insertWSSchedule(WSSchedule wsSchedule) {
		logger.debug("Entry And Exit : insertWSSchedule");
		return webserviceTestingDAO.insertWSSchedule(wsSchedule);
	}

	@Override
	@Transactional
	public boolean wsSaveBaseline(WSBaseline wsBaseline) {
		logger.debug("Entry And Exit : wsSaveBaseline");
		return webserviceTestingDAO.wsSaveBaseline(wsBaseline);
	}

	@Override
	@Transactional
	public List<WSResults> getWSBaselineDates(int wsSuiteId) {
		logger.debug("Entry And Exit : getWSBaselineDates");
		return webserviceTestingDAO.getWSBaselineDates(wsSuiteId);
	}

	@Override
	@Transactional
	public WsDataset getXmlParameterValues(Integer environmentId,
			int parameterSetId, Integer reqdOprId) {
		logger.debug("Entry And Exit : getXmlParameterValues");
		return webserviceTestingDAO.getXmlParameterValues(environmentId, parameterSetId, reqdOprId);
	}

	@Override
	@Transactional
	public WSReports getWSReports(WSReports wsReport) {
		logger.debug("Entry And Exit : getWSReports");
		return webserviceTestingDAO.getWSReports(wsReport);
	}

	@Override
	@Transactional
	public List<WebserviceSuite> getWSTestSuitesCreatedByAll() {
		logger.debug("Entry And Exit : getWSReports");
		return webserviceTestingDAO.getWSTestSuitesCreatedByAll();
	}

	@Override
	@Transactional
	public List<WSSchedule> pendingSchedules() {
		logger.debug("Entry And Exit : pendingSchedules");
		return webserviceTestingDAO.pendingSchedules();
	}

	@Override
	@Transactional
	public List<WebserviceSuite> getAllWsTestSuites(int environmentId) {
		logger.debug("Entry And Exit : getAllWsTestSuites");
		return webserviceTestingDAO.getAllWsTestSuites(environmentId);
	}

	@Override
	@Transactional
	public List<WebserviceSuite> getWSTestSuitesForUser(int currentUserId) {
		logger.debug("Entry And Exit : getWSTestSuitesForUser");
		return webserviceTestingDAO.getWSTestSuitesForUser(currentUserId);
	}

	@Override
	@Transactional
	public List<WebserviceSuite> getWSTestSuitesForGroup(int currentGroupId) {
		logger.debug("Entry And Exit : getWSTestSuitesForGroup");
		return webserviceTestingDAO.getWSTestSuitesForGroup(currentGroupId);
	}
	@Override
	@Transactional
	public boolean removeSuitDetails(WebserviceSuite suit) {
		logger.debug("Entry And Exit : getUsageReport");
		return webserviceTestingDAO.removeSuitDetails(suit);
	}

	@Override
	@Transactional
	public List<WSExecutionStatus> getWSExecutionStatusListByScheduleId(
			int scheduleId) {
		logger.debug("Entry And Exit : getUsageReport");
		return webserviceTestingDAO.getWSExecutionStatusListByScheduleId(scheduleId);
	}
	
}
