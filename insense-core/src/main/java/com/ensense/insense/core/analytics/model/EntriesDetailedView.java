package com.ensense.insense.core.analytics.model;

import com.ensense.insense.data.analytics.model.WebAnalyticsPageData;

import java.util.List;

public class EntriesDetailedView {

	 protected List<WebAnalyticsPageData> webAnalyticsPageData;

	public List<WebAnalyticsPageData> getWebAnalyticsPageData() {
		return webAnalyticsPageData;
	}

	public void setWebAnalyticsPageData(
			List<WebAnalyticsPageData> webAnalyticsPageData) {
		this.webAnalyticsPageData = webAnalyticsPageData;
	}
}
