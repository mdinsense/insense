package com.ensense.insense.services.uiadmin.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.uitesting.dao.HtmlReportConfigDAO;
import com.cts.mint.uitesting.entity.HtmlReportsConfig;
import com.cts.mint.uitesting.service.HtmlReportConfigService;

@Service
public class HtmlReportConfigServiceImpl implements HtmlReportConfigService{

	private static Logger logger = Logger
			.getLogger(HtmlReportConfigService.class);
	
	@Autowired
	HtmlReportConfigDAO htmlReportConfigDAO;

	@Override
	@Transactional
	public boolean addHtmlReportsConfig(HtmlReportsConfig htmlReportsConfig) {
		logger.debug("Entry And Exit : addHtmlReportsConfig");
		return htmlReportConfigDAO.addHtmlReportsConfig(htmlReportsConfig);
	}

	@Override
	@Transactional
	public List<HtmlReportsConfig> getHtmlReportsConfigForApplicationEnvironment(int applicationId,int environmentId) {
		logger.debug("Entry And Exit : getHtmlReportsConfig");
		return htmlReportConfigDAO.getHtmlReportsConfigForApplicationEnvironment(applicationId,environmentId);
	}
	
	@Override
	@Transactional
	public List<HtmlReportsConfig> getAllHtmlReportsConfig() {
		logger.debug("Entry And Exit : getAllHtmlReportsConfig");
		return htmlReportConfigDAO.getAllHtmlReportsConfig();
	}
	
	@Override
	@Transactional
	public HtmlReportsConfig getHtmlReportsConfig(int htmlReportsConfigId) {
		logger.debug("Entry And Exit : getHtmlReportsConfig");
		return htmlReportConfigDAO.getHtmlReportsConfig(htmlReportsConfigId);
	}

	@Override
	@Transactional
	public boolean deleteHtmlReportsConfig(HtmlReportsConfig htmlReportsConfig) {
		logger.debug("Entry And Exit : deleteHtmlReportsConfig");
		return htmlReportConfigDAO.deleteHtmlReportsConfig(htmlReportsConfig);
	}

	@Override
	@Transactional
	public boolean isHtlmConfigExistForAppEnv(
			HtmlReportsConfig htmlReportsConfig) {
		logger.debug("Entry And Exit : isHtlmConfigExistForAppEnv");
		return htmlReportConfigDAO.isHtmlConfigExist(htmlReportsConfig);
	}

}
