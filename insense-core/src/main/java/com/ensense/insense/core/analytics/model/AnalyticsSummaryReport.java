package com.ensense.insense.core.analytics.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnalyticsSummaryReport {
	private int totalUrl;
	private Map<String, AnalyticsTagDetail> eachAnalyticsTagMap;
	private Set<String> allUrls;
	
	public int getTotalUrl() {
		return totalUrl;
	}
	public void setTotalUrl(int totalUrl) {
		this.totalUrl = totalUrl;
	}
	public Map<String, AnalyticsTagDetail> getEachAnalyticsTagMap() {
		if ( null == eachAnalyticsTagMap ){
			eachAnalyticsTagMap = new HashMap<String, AnalyticsTagDetail>();
		}
		return eachAnalyticsTagMap;
	}
	public void setEachAnalyticsTagMap(
			Map<String, AnalyticsTagDetail> eachAnalyticsTagMap) {
		this.eachAnalyticsTagMap = eachAnalyticsTagMap;
	}

	public Set<String> getAllUrls() {
		if ( null == allUrls ){
			allUrls = new HashSet();
		}
		return allUrls;
	}
	public void setAllUrls(Set<String> allUrls) {
		this.allUrls = allUrls;
	}
	@Override
	public String toString() {
		return "AnalyticsSummaryReport [totalUrl=" + totalUrl
				+ ", eachAnalyticsTagMap=" + eachAnalyticsTagMap + ", allUrls="
				+ allUrls + "]";
	}
}
