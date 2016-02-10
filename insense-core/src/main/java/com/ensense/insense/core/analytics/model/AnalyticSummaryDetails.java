/**
 * 
 */
package com.ensense.insense.core.analytics.model;

import java.io.Serializable;
import java.util.Map;

/**
 * @author 361494
 *
 */
public class AnalyticSummaryDetails implements Serializable{
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	private String lastRecordedTime;
	private Map<String, AnalyticsDetails> eachAnalyticsDetails;
	
	public String getLastRecordedTime() {
		return lastRecordedTime;
	}
	public void setLastRecordedTime(String lastRecordedTime) {
		this.lastRecordedTime = lastRecordedTime;
	}
	public Map<String, AnalyticsDetails> getEachAnalyticsDetails() {
		return eachAnalyticsDetails;
	}
	public void setEachAnalyticsDetails(
			Map<String, AnalyticsDetails> eachAnalyticsDetails) {
		this.eachAnalyticsDetails = eachAnalyticsDetails;
	}
	@Override
	public String toString() {
		return "AnalyticSummaryDetails [lastRecordedTime=" + lastRecordedTime
				+ ", eachAnalyticsDetails=" + eachAnalyticsDetails + "]";
	}
	
	
}
