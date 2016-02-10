
package com.ensense.insense.data.analytics.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;


/**
 * The persistent class for the detailed_view_table database table.
 * 
 */
@Entity
@Table(name="DetailedView")
public class DetailedView {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="detailedViewId")
	private int detailedViewId;
	
	@Column(name="scheduleId")
	private int scheduleId;
	
	@Column(name="applicationName")
	private String applicationName;
	
	@Column(name="pageURL")
	private String pageURL;
	
	@Column(name="pageTitle")
	private String pageTitle;
	
	@Column(name="harLogFileName")
	private String harLogFileName;
	
	@Lob
	@Column(name="webAnalyticsPageDataXml")
	private String webAnalyticsPageDataXml;
	
	public int getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getPageURL() {
		return pageURL;
	}
	public void setPageURL(String pageURL) {
		this.pageURL = pageURL;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getHarLogFileName() {
		return harLogFileName;
	}
	public void setHarLogFileName(String harLogFileName) {
		this.harLogFileName = harLogFileName;
	}
	public String getWebAnalyticsPageDataXml() {
		return webAnalyticsPageDataXml;
	}
	public void setWebAnalyticsPageDataXml(String webAnalyticsPageDataXml) {
		this.webAnalyticsPageDataXml = webAnalyticsPageDataXml;
	}
	public int getDetailedViewId() {
		return detailedViewId;
	}
	public void setDetailedViewId(int detailedViewId) {
		this.detailedViewId = detailedViewId;
	}

	
	@Override
	public String toString() {
		return "DetailedView [detailedViewId=" + detailedViewId
				+ ", scheduleId=" + scheduleId + ", applicationName="
				+ applicationName + ", pageURL=" + pageURL + ", pageTitle="
				+ pageTitle + ", harLogFileName=" + harLogFileName
				+ ", webAnalyticsPageDataXml=" + webAnalyticsPageDataXml + "]";
	}
}
