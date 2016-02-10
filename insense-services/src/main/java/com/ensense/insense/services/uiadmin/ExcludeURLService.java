package com.ensense.insense.services.uiadmin;

import java.util.List;

import com.cts.mint.uitesting.entity.AnalyticsExcludeLink;
import com.cts.mint.uitesting.entity.ExcludeLinkType;
import com.cts.mint.uitesting.entity.ExcludeUrl;

public interface ExcludeURLService {
	
	List<ExcludeUrl> getExcludeURLList();

	boolean addExcludeURL(ExcludeUrl excludeUrl);

	ExcludeUrl getExcludeURLBO(ExcludeUrl excludeUrl);
	
	boolean deleteExcludeURLDetails(ExcludeUrl excludeUrl);

	List<ExcludeUrl> getExcludeURLList(Integer applicationId,
									   Integer environmentId);
	
	public List<ExcludeLinkType> getExcludeLinkType();
	
	boolean addExcludeLink(AnalyticsExcludeLink analyticsExcludeLink);
	
	AnalyticsExcludeLink getAnalyticsExcludeSetupModule(AnalyticsExcludeLink analyticsExcludeLink);
	
	boolean deleteAnalyticsExcludeSetup(AnalyticsExcludeLink analyticsExcludeLink);
	
	public List<AnalyticsExcludeLink> getAnalyticsExcludeLinksetup();
}
