package com.ensense.insense.data.uitesting.dao;

import java.util.List;

import com.cts.mint.uitesting.entity.HtmlReportsConfig;

public interface HtmlReportConfigDAO {
	boolean addHtmlReportsConfig(HtmlReportsConfig htmlReportsConfig);

	List<HtmlReportsConfig>  getHtmlReportsConfigForApplicationEnvironment(int applicationId, int environmentId);
	
	HtmlReportsConfig getHtmlReportsConfig(int htmlReportsConfigId);
	
	List<HtmlReportsConfig> getAllHtmlReportsConfig();

	boolean deleteHtmlReportsConfig(HtmlReportsConfig htmlReportsConfig);

	boolean isHtmlConfigExist(HtmlReportsConfig htmlReportsConfig);
}
