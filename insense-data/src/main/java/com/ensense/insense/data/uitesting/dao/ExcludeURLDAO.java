package com.ensense.insense.data.uitesting.dao;

import com.ensense.insense.data.uitesting.entity.AnalyticsExcludeLink;
import com.ensense.insense.data.uitesting.entity.ExcludeLinkType;
import com.ensense.insense.data.uitesting.entity.ExcludeUrl;

import java.util.List;


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
