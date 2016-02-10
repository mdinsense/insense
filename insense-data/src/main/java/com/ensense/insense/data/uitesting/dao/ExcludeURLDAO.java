package com.ensense.insense.data.uitesting.dao;

import java.util.List;

import com.cts.mint.uitesting.entity.AnalyticsExcludeLink;
import com.cts.mint.uitesting.entity.ExcludeLinkType;
import com.cts.mint.uitesting.entity.ExcludeUrl;

public interface ExcludeURLDAO {
	
	List<ExcludeUrl> getExcludeURLList();

	boolean addExcludeURL(ExcludeUrl excludeUrl);
	
	ExcludeUrl getExcludeURLBO(ExcludeUrl excludeUrl);
	
	boolean deleteExcludeURLDetails(ExcludeUrl excludeUrl);

	ExcludeUrl getExcludeUrl(ExcludeUrl excludeUrl);

	List<ExcludeUrl> getExcludeURLList(Integer applicationId,
									   Integer environmentId);
	
	public List<ExcludeLinkType> getExcludeLinkType();
	
	public boolean addExcludeLink(AnalyticsExcludeLink analyticsExcludeLink);
	
	public AnalyticsExcludeLink getAnalyticsExcludeSetupModule(AnalyticsExcludeLink analyticsExcludeLink);
	
	public boolean deleteAnalyticsExcludeSetup(AnalyticsExcludeLink analyticsExcludeLink);
	
	public List<AnalyticsExcludeLink> getAnalyticsExcludeLinksetup();
}
