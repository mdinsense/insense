package com.ensense.insense.services.uiadmin.impl;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cts.mint.common.dao.HomeDAO;
import com.cts.mint.common.entity.SuitGroupXref;
import com.cts.mint.common.utils.CommonUtils;
import com.cts.mint.common.utils.Constants;
import com.cts.mint.uitesting.dao.ApplicationConfigDAO;
import com.cts.mint.uitesting.dao.EnvironmentDAO;
import com.cts.mint.uitesting.dao.UiTestingDAO;
import com.cts.mint.uitesting.entity.ApplicationConfig;
import com.cts.mint.uitesting.entity.Environment;
import com.cts.mint.uitesting.entity.Suit;
import com.cts.mint.uitesting.entity.SuitBrokenReportsXref;
import com.cts.mint.uitesting.entity.SuitTextImageXref;
import com.cts.mint.uitesting.service.UiTestingService;

@Service
public class UiTestingServiceImpl implements UiTestingService {
	private static Logger logger = Logger.getLogger(UiTestingServiceImpl.class);
	@Autowired
	private UiTestingDAO uiTestingDAO;

	@Autowired
	private EnvironmentDAO environmentDAO;

	@Autowired
	ApplicationConfigDAO applicationConfigDAO;

	@Autowired
	private MessageSource configProperties;

	@Autowired
	private HomeDAO homeDAO;

	@Override
	@Transactional
	public boolean saveFunctionalityTestingSuit(Suit suit) {
		logger.debug("Entry And Exit: SaveFunctionalityTestingSuit");
		boolean saved = false;
		Environment environment = environmentDAO
				.getEnvironmentForApplicationCategory(suit.getApplicationId(),
						suit.getEnvironmentCategoryId());
		ApplicationConfig applicationConfig = null;
		suit.setEnvironmentId(environment.getEnvironmentId());
		applicationConfig = applicationConfigDAO.getApplicationConfig(
				suit.getApplicationId(), suit.getEnvironmentId());
		if (applicationConfig != null) {
			suit.setBrowserRestartCount(applicationConfig.getBrowserRestart()
					.toString());
			suit.setNoOfThreads(applicationConfig.getNoOfBrowsers().toString());
			suit.setMaxWaitTime(applicationConfig.getBrowserTimeout()
					.toString());
		}
		saved = uiTestingDAO.saveFunctionalityTestingSuit(suit);
		return saved;
	}

	@Override
	@Transactional
	public boolean saveSuitGroupXref(SuitGroupXref suitGroup) {
		logger.debug("Entry And Exit: saveSuitGroupXref");
		return uiTestingDAO.saveSuitGroupXref(suitGroup);
	}
	
	@Override
	public boolean syncBatchFileData() {
		logger.debug("Entry: synchronizeBatchFile");
		boolean isComplete = false;
		String batchFilePath = null;
		try {
			batchFilePath = CommonUtils.getPropertyFromPropertyFile(
					configProperties, Constants.BATCH_FILE_KEY);
			batchFilePath = batchFilePath + Constants.BATCH_FILE_NAME + ".txt";
			Path out = Paths.get(batchFilePath);
			List<String> arr = new ArrayList<String>();
			arr.add(CommonUtils.getPropertyFromPropertyFile(configProperties,
					Constants.BATCH_FILE_HEADERS));
			arr.addAll(uiTestingDAO.getBatchFileData());
			Files.write(out, arr, Charset.defaultCharset());
			isComplete = true;
		} catch (Exception e) {
			logger.error("Error while creating Batchfile, batchFilePath->"
					+ batchFilePath);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		logger.debug("Exit: synchronizeBatchFile");
		return isComplete;
	}
	
	@Override
	@Transactional
	public boolean saveTextOrImageSuit(Suit suit) {
		logger.debug("Entry And Exit: SaveFunctionalityTestingSuit");
		boolean saved = false;
		Environment environment = environmentDAO
				.getEnvironmentForApplicationCategory(suit.getApplicationId(),
						suit.getEnvironmentCategoryId());
		ApplicationConfig applicationConfig = null;
		suit.setEnvironmentId(environment.getEnvironmentId());
		applicationConfig = applicationConfigDAO.getApplicationConfig(
				suit.getApplicationId(), suit.getEnvironmentId());
		if (applicationConfig != null) {
			suit.setBrowserRestartCount(applicationConfig.getBrowserRestart()
					.toString());
			suit.setNoOfThreads(applicationConfig.getNoOfBrowsers().toString());
			suit.setMaxWaitTime(applicationConfig.getBrowserTimeout()
					.toString());
		}
		saved = uiTestingDAO.saveTextOrImageSuit(suit);
		return saved;
	}

	@Override
	@Transactional
	public boolean saveSuitTextImageXref(SuitTextImageXref suitTextImage) {
		logger.debug("Entry And Exit: saveSuitGroupXref");
		//uiTestingDAO.deleteSuitTextImageXref(suitTextImage.getSuitId());
		return uiTestingDAO.saveSuitTextImageXref(suitTextImage);
	}

	@Override
	@Transactional
	public List<SuitTextImageXref> getSuitTextImageXref(Integer suitId){
		logger.debug("Entry And Exit: getSuitTextImageXref");
		return uiTestingDAO.getSuitTextImageXref(suitId);
	}
	
	@Override
	@Transactional
	public boolean deleteSuitTextImageXref(int suitId){
		logger.debug("Entry And Exit: deleteSuitTextImageXref");
		return uiTestingDAO.deleteSuitTextImageXref(suitId);
	}
	
	@Override
	@Transactional
	public boolean saveSuitBrokenReportsXref(ArrayList<SuitBrokenReportsXref> suitBrokenReportsXrefList){
		logger.debug("Entry And Exit: saveSuitBrokenReportsXref");
		//uiTestingDAO.deleteSuitBrokenReportsXref(suitBrokenReportsXref.getSuitId());
		return uiTestingDAO.saveSuitBrokenReportsXref(suitBrokenReportsXrefList);
	}
	
	@Override
	@Transactional
	public boolean deleteSuitBrokenReportsXref(int suitId){
		logger.debug("Entry And Exit: deleteSuitBrokenReportsXref");
		return uiTestingDAO.deleteSuitBrokenReportsXref(suitId);
	}
}
