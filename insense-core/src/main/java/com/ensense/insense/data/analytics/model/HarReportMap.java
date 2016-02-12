package com.ensense.insense.data.analytics.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HarReportMap {

	private Date generatedDate;
	private List<AnalyticsData> harReports;
	private List<AnalyticsData> baselineHarReports;
	
	public HarReportMap(){
		harReports = new ArrayList<AnalyticsData>();
		baselineHarReports = new ArrayList<AnalyticsData>();
	}
	
	public Date getGeneratedDate() {
		return generatedDate;
	}
	public void setGeneratedDate(Date generatedDate) {
		this.generatedDate = generatedDate;
	}

	public List<AnalyticsData> getHarReports() {
		return harReports;
	}

	public void setHarReports(List<AnalyticsData> harReports) {
		this.harReports = harReports;
	}

	public List<AnalyticsData> getBaselineHarReports() {
		return baselineHarReports;
	}

	public void setBaselineHarReports(List<AnalyticsData> baselineHarReports) {
		this.baselineHarReports = baselineHarReports;
	}

	@Override
	public String toString() {
		return "HarReportMap [generatedDate=" + generatedDate + ", harReports="
				+ harReports + ", baselineHarReports=" + baselineHarReports
				+ "]";
	}
	
}
