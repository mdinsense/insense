package com.ensense.insense.data.analytics.dao;

import com.ensense.insense.data.analytics.entity.AnalyticsAuditSummary;

public interface AnalyticsAuditSummaryTableDAO {
	public  boolean populateSummaryView(AnalyticsAuditSummary analyticsAuditSummary);
}
