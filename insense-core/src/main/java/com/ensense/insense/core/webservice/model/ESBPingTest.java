package com.ensense.insense.core.webservice.model;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class ESBPingTest {

	private int serviceId;
	private String serviceName;
	private List<MultipartFile> file;
	private String recurrence;
	private String scheduleType;
	private String schDateTime;
	private String environment;
	private String[] serviceNames;
	private String buttonType;
	private String emailTriggerOption;
	private Integer setupTabNumber;
	
	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
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

	public String getEmailTriggerOption() {
		return emailTriggerOption;
	}

	public void setEmailTriggerOption(String emailTriggerOption) {
		this.emailTriggerOption = emailTriggerOption;
	}

	@Override
	public String toString() {
		return "ESBPingTest [serviceId=" + serviceId + ", serviceName="
				+ serviceName + ", file=" + file + ", recurrence=" + recurrence
				+ ", scheduleType=" + scheduleType + ", schDateTime="
				+ schDateTime + ", environment=" + environment
				+ ", serviceNames=" + Arrays.toString(serviceNames)
				+ ", buttonType=" + buttonType + ", emailTriggerOption="
				+ emailTriggerOption + "]";
	}

	public void setSetupTabNumber(Integer setupTabNumber) {
		this.setupTabNumber = setupTabNumber;
	}

	public Integer getSetupTabNumber() {
		return setupTabNumber;
	}

}
