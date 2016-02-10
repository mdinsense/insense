package com.ensense.insense.services.uiadmin;

import java.util.List;

import com.cts.mint.uitesting.entity.HtmlReportsConfig;

public interface HtmlReportConfigService {
	boolean addHtmlReportsConfig(HtmlReportsConfig htmlReportsConfig);

	List<HtmlReportsConfig>  getHtmlReportsConfigForApplicationEnvironment(int applicationId, int environmentId);
	
	HtmlReportsConfig getHtmlReportsConfig(int htmlReportsConfigId);
	
	List<HtmlReportsConfig> getAllHtmlReportsConfig();

	boolean deleteHtmlReportsConfig(HtmlReportsConfig htmlReportsConfig);

	boolean isHtlmConfigExistForAppEnv(HtmlReportsConfig htmlReportsConfig);
}
