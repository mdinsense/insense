package com.ensense.insense.core.webservice.model;

public class WebServiceReports {
	private int wsScheduleId;
	private String generatedTime;
	public int getWsScheduleId() {
		return wsScheduleId;
	}
	public void setWsScheduleId(int wsScheduleId) {
		this.wsScheduleId = wsScheduleId;
	}
	public String getGeneratedTime() {
		return generatedTime;
	}
	public void setGeneratedTime(String generatedTime) {
		this.generatedTime = generatedTime;
	}
	@Override
	public String toString() {
		return "WebServiceReports [wsScheduleId=" + wsScheduleId
				+ ", generatedTime=" + generatedTime + "]";
	}
}
