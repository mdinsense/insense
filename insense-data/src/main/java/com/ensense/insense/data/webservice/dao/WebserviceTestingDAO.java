package com.ensense.insense.data.webservice.dao;

import com.ensense.insense.data.webservice.entity.*;
import com.ensense.insense.data.webservice.model.WSReportsData;
import com.ensense.insense.data.webservice.model.WSSuiteDetails;
import com.ensense.insense.data.webservice.model.WebserviceSetupForm;
import com.ensense.insense.data.webservice.model.WsDataset;
import com.eviware.soapui.support.types.StringToStringMap;

import java.util.List;
import java.util.Map;

public interface WebserviceTestingDAO {

	public List<Webservices> getAllWebservices();

	public Webservices getServicefromServiceName(String serviceName);

	public Boolean insertServiceNames(String serviceName, String serviceType,
									  String filePath);

	public Boolean insertOperationNames(String operationName,
										Integer serviceId, String methodType, String contentType);

	public WebserviceOperations getOperationIdfromOperationName(
			Integer serviceId, String operationName, String methodType);

	public Boolean insertRequestParameterName(
			WsOperationParameter wsOperationParameter, String sampleXML);

	public List<WebserviceOperations> getOperationsByService(
			Webservices webservice);

	public Boolean insertEndpointDetails(WsEndpointDetails wsEndpointDetails);

	public Integer insertOperationWithEnvId(int operationId, int serviceId,
											int environmentId, String methodType, String contentType);

	public WebserviceOperations getOperationfromOperationId(
			WebserviceOperations webserviceOperations);

	public Integer getOperationIdFromNameAndEnvId(Integer operationId,
												  int serviceId, int environmentId);

	public Boolean deleteOperations(Integer reqdOprId, int environmentId);

	public Webservices getServicefromServiceId(int serviceId);

	public WsEndpointDetails getEndPoint(int environmentId, String serviceName);

	public List<WebserviceOperations> getOperationsFromServiceEnv(
			int serviceId, int environmentId);

	public int getOperationIdOfNullEnv(int serviceId, int operationId);

	public Map<String, Map<String, String>> getParameterSets(int reqdOprId,
															 int environmentId);

	public List<WsDataset> getTestSoapParameterSets(
			int reqdOprId, int environmentId);

	public List<WsOperationHeaderParameters> getTestSoapParameterHeaders(
			Integer parameterId);

	public List<WsEndpointDetails> getServiceByEnvId(int environmentId);

	public List<WsOperationParameter> getRequestParamNames(
			WsOperationParameter wsOperationParameter);

	public Integer getMaxTestParameterSetId();

	public boolean addTestOperationData(
			WsOperationParameter wsOperationParameter,
			Map<String, String> requestValuesMap,
			WsParameterAndSetId wsParameterAndSetId, Integer environmentId,
			Integer operationId, String datasetName);

	public WsOperationXmlParameter submitParameters(String requestXML,
													Integer environmentId, int operationId, Integer serviceId, String datasetName);

	public Boolean insertRequestParameterHeaders(
			WsOperationHeaderParameters wsOperationHeaderParameters);

	public List<WsOperationParameter> getRequestParamNamesForDataset(
			WsOperationParameter wsOperationParameter);

	public WsOperationParameterValue getRequestParameterValue(
			WsParameterAndSetId wsParameterAndSetId);

	public List<WsOperationParameter> getRequestHeaders(
			WsOperationParameter wsOperationParameter);

	public WsOperationXmlParameter getTestSoapParameters(int paramSetId);

	public List<Integer> getTestParamSetIDsforOperation(int operationId);

	public Boolean insertTestWSSuite(WebserviceSuite webserviceSuite,
									 List<WebserviceSetupForm> listWebserviceSetupForm);
	
	public Boolean insertWSSuiteService(Integer webserviceSuiteId,
										Integer serviceId, Integer operationId, String inputType);

	public Boolean deleteServiceDetails(Integer serviceId);

	public List<WebservicesPingTest> getWebservicesPingTestList();

	public boolean deletePingServices();

	public List<WebservicesPingTest> getDuplicateService(String serviceName);

	public boolean insertExcelServiceData(WebservicesPingTest userData);

	public int insertWSPingSchedules(WSPingSchedule wSPingSchedule);

	public List<WSPingResults> getESBPingServiceDetails(String environmentId,
														String resultsId);

	public List<WSPingResults> getESBPingDates(String environment);

	public List<WebserviceSuite> getWSTestSuites(int userid,
												 int environment);

	public boolean isWSSchedulePresent();

	public List<WSExecutionStatus> getWSExecutionStatusList();

	public WSSchedule getWSSchedule(int wsScheduleId);

	public Integer getEnvironment(Integer webserviceSuiteId);

	public WebserviceSuite getWebserviceSuite(Integer webserviceSuiteId);

	public List<WebserviceSuiteService> getWebserviceSuiteService(
			Integer webserviceSuiteId);

	public WsDataset getParameterValues(Integer environmentId,
										int parameterSetId, Integer reqdOprId);

	public StringToStringMap getCustomHeadersValues(Integer environmentId,
													int parameterSetId, Integer operationId);

	public boolean insertWSTestResults(WSResults wsResult);

	public Boolean wsSaveComparisonResults(WSReports wsReport);

	public boolean isBaseLineFound(int wsScheduleId);

	public boolean setWSCompletion(WSExecutionStatus wstestsuite, boolean runStatus);

	public boolean updateWSSchedule(WSSchedule wsSchedule);

	public List<WSPingSchedule> getPingSchedules();

	public boolean insertWSPingResults(WSPingResults wsPingResults, String resultXml);

	public List<WSBaseline> getSchedulesToBeCompared();

	public WSResults getScheduleIdfromResultId(Integer wsResultId);

	public List<WSResults> getSchedulesFromScheduleId(Integer wsScheduleId);

	public int pickUpSchedules();

	public List<WSReports> getWSReportDatesFromSuiteId(int wsSuiteId);

	public List<WSReportsData> getWSReports(int webserviceSuiteId,
											int wsReportsId);

	public List<WSSuiteDetails> getWSSuiteDetails(int wsSuiteId);

	public Boolean getEndPointForService(Integer serviceId, int envId);

	public int insertWSSchedule(WSSchedule wsSchedule);

	public boolean wsSaveBaseline(WSBaseline wsBaseline);

	public List<WSResults> getWSBaselineDates(int wsSuiteId);

	public WsDataset getXmlParameterValues(Integer environmentId, int parameterSetId,
										   Integer operationId);

	public WSReports getWSReports(WSReports wsReport);

	public List<WebserviceSuite> getWSTestSuitesCreatedByAll();

	public List<WSSchedule> pendingSchedules();

	public List<WebserviceSuite> getAllWsTestSuites(int environmentId);

	public List<WebserviceSuite> getWSTestSuitesForUser(int currentUserId);

	public List<WebserviceSuite> getWSTestSuitesForGroup(int currentGroupId);

	public boolean removeSuitDetails(WebserviceSuite suit);

	public List<WSExecutionStatus> getWSExecutionStatusListByScheduleId(int scheduleId);
}
