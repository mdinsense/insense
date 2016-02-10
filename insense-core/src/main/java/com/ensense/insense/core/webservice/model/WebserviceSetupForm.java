package com.ensense.insense.core.webservice.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class WebserviceSetupForm implements Serializable {

	private static final long serialVersionUID = 1L;

	private int setupTabNumber;
	private String endPoint;
	private int serviceId;
	private int environmentId;
	private int operationId;
	private Integer wsSuiteId;

	private String wsSuiteName;
	private String visibleToOtherUsers;
	private String[] operations;
	private String reqInputType;
	private String elementsArray;
	private String requestXML;
	private String paramSetId;
	private String xmlInput;
	private String rawInput;
	private String resultDate;
	private String serviceName;
	private List<MultipartFile> file;
	private String sendEmail;
	private int wsResultsId;
	private int wsReportsId;
	private String datasetName;
	private String[] params;
	private String[] parametersets;
	private WsSuite[] wsSuiteArray;

	public WsSuite[] getWsSuiteArray() {
		return wsSuiteArray;
	}

	public void setWsSuiteArray(WsSuite[] wsSuiteArray) {
		this.wsSuiteArray = wsSuiteArray;
	}

	private String recurrence;
	private String scheduleType;
	private String schDateTime;
	private String environment;
	private String[] serviceNames;
	private String buttonType;
	private String[] emailTriggerOption;
	private int wsScheduleId;
	private boolean privateSuit;
	private String emailRecepients;
	private String editOrViewMode;
	private int userId;
	
	
	public String getSendEmail() {
		return sendEmail;
	}

	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public List<MultipartFile> getFile() {
		return file;
	}

	public void setFile(List<MultipartFile> file) {
		this.file = file;
	}

	public String getRecurrence() {
		return recurrence;
	}

	public void setRecurrence(String recurrence) {
		this.recurrence = recurrence;
	}

	public String getScheduleType() {
		return scheduleType;
	}

	public void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}

	public String getSchDateTime() {
		return schDateTime;
	}

	public void setSchDateTime(String schDateTime) {
		this.schDateTime = schDateTime;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String[] getServiceNames() {
		return serviceNames;
	}

	public void setServiceNames(String[] serviceNames) {
		this.serviceNames = serviceNames;
	}

	public String getButtonType() {
		return buttonType;
	}

	public void setButtonType(String buttonType) {
		this.buttonType = buttonType;
	}

	public String[] getEmailTriggerOption() {
		return emailTriggerOption;
	}

	public void setEmailTriggerOption(String[] emailTriggerOption) {
		this.emailTriggerOption = emailTriggerOption;
	}

	public String getElementsArray() {
		return elementsArray;
	}

	public void setElementsArray(String elementsArray) {
		this.elementsArray = elementsArray;
	}

	public String getWsSuiteName() {
		return wsSuiteName;
	}

	public void setWsSuiteName(String wsSuiteName) {
		this.wsSuiteName = wsSuiteName;
	}

	public String getVisibleToOtherUsers() {
		return visibleToOtherUsers;
	}

	public void setVisibleToOtherUsers(String visibleToOtherUsers) {
		this.visibleToOtherUsers = visibleToOtherUsers;
	}

	public String[] getOperations() {
		return operations;
	}

	public void setOperations(String[] operations) {
		this.operations = operations;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public int getEnvironmentId() {
		return environmentId;
	}

	public void setEnvironmentId(int environmentId) {
		this.environmentId = environmentId;
	}

	public int getSetupTabNumber() {
		return setupTabNumber;
	}

	public void setSetupTabNumber(int setupTabNumber) {
		this.setupTabNumber = setupTabNumber;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getEndPoint() {
		return endPoint;
	}

	public void setReqInputType(String reqInputType) {
		this.reqInputType = reqInputType;
	}

	public String getReqInputType() {
		return reqInputType;
	}

	public void setOperationId(int operationId) {
		this.operationId = operationId;
	}

	public int getOperationId() {
		return operationId;
	}

	public void setRequestXML(String requestXML) {
		this.requestXML = requestXML;
	}

	public String getRequestXML() {
		return requestXML;
	}

	public void setParamSetId(String paramSetId) {
		this.paramSetId = paramSetId;
	}

	public String getParamSetId() {
		return paramSetId;
	}

	public void setXmlInput(String xmlInput) {
		this.xmlInput = xmlInput;
	}

	public String getXmlInput() {
		return xmlInput;
	}

	public void setRawInput(String rawInput) {
		this.rawInput = rawInput;
	}

	public String getRawInput() {
		return rawInput;
	}

	public void setResultDate(String resultDate) {
		this.resultDate = resultDate;
	}

	public String getResultDate() {
		return resultDate;
	}

	public void setWsResultsId(Integer wsResultsId) {
		this.wsResultsId = wsResultsId;
	}

	public Integer getWsResultsId() {
		return wsResultsId;
	}

	public void setWsReportsId(int wsReportsId) {
		this.wsReportsId = wsReportsId;
	}

	public int getWsReportsId() {
		return wsReportsId;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setParams(String[] params) {
		this.params = params;
	}

	public String[] getParams() {
		return params;
	}

	public Integer getWsSuiteId() {
		return wsSuiteId;
	}

	public void setWsSuiteId(Integer wsSuiteId) {
		this.wsSuiteId = wsSuiteId;
	}

	public int getWsScheduleId() {
		return wsScheduleId;
	}

	public void setWsScheduleId(int wsScheduleId) {
		this.wsScheduleId = wsScheduleId;
	}

	public void setWsResultsId(int wsResultsId) {
		this.wsResultsId = wsResultsId;
	}

	public void setPrivateSuit(boolean privateSuit) {
		this.privateSuit = privateSuit;
	}

	public boolean isPrivateSuit() {
		return privateSuit;
	}

	public void setEmailRecepients(String emailRecepients) {
		this.emailRecepients = emailRecepients;
	}

	public String getEmailRecepients() {
		return emailRecepients;
	}
	
	public String[] getParametersets() {
		return parametersets;
	}

	public void setParametersets(String[] parametersets) {
		this.parametersets = parametersets;
	}
	
	@Override
	public String toString() {
		return "WebserviceSetupForm [setupTabNumber=" + setupTabNumber
				+ ", endPoint=" + endPoint + ", serviceId=" + serviceId
				+ ", environmentId=" + environmentId + ", operationId="
				+ operationId + ", wsSuiteId=" + wsSuiteId + ", wsSuiteName="
				+ wsSuiteName + ", visibleToOtherUsers=" + visibleToOtherUsers
				+ ", operations=" + Arrays.toString(operations)
				+ ", reqInputType=" + reqInputType + ", elementsArray="
				+ elementsArray + ", requestXML=" + requestXML
				+ ", paramSetId=" + paramSetId + ", xmlInput=" + xmlInput
				+ ", rawInput=" + rawInput + ", resultDate=" + resultDate
				+ ", serviceName=" + serviceName + ", file=" + file
				+ ", sendEmail=" + sendEmail + ", wsResultsId=" + wsResultsId
				+ ", wsReportsId=" + wsReportsId + ", datasetName="
				+ datasetName + ", params=" + Arrays.toString(params)
				+ ", recurrence=" + recurrence + ", scheduleType="
				+ scheduleType + ", schDateTime=" + schDateTime
				+ ", environment=" + environment + ", serviceNames="
				+ Arrays.toString(serviceNames) + ", buttonType=" + buttonType
				+ ", emailTriggerOption=" + Arrays.toString(emailTriggerOption)
				+ ", wsScheduleId=" + wsScheduleId + "]";
	}

	public void setEditOrViewMode(String editOrViewMode) {
		this.editOrViewMode = editOrViewMode;
	}

	public String getEditOrViewMode() {
		return editOrViewMode;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

	

}
