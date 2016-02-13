package com.ensense.insense.services.uiadmin;

import com.ensense.insense.data.uitesting.entity.AnalyticsExcludeLink;
import com.ensense.insense.data.uitesting.entity.ExcludeLinkType;
import com.ensense.insense.data.uitesting.entity.ExcludeUrl;

import java.util.List;


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
