package com.ensense.insense.services.uiadmin;

import com.ensense.insense.data.uitesting.entity.HtmlReportsConfig;

import java.util.List;


public interface HtmlReportConfigService {
	boolean addHtmlReportsConfig(HtmlReportsConfig htmlReportsConfig);

	List<HtmlReportsConfig>  getHtmlReportsConfigForApplicationEnvironment(int applicationId, int environmentId);
	
	HtmlReportsConfig getHtmlReportsConfig(int htmlReportsConfigId);
	
	List<HtmlReportsConfig> getAllHtmlReportsConfig();

	boolean deleteHtmlReportsConfig(HtmlReportsConfig htmlReportsConfig);

	boolean isHtlmConfigExistForAppEnv(HtmlReportsConfig htmlReportsConfig);
}
