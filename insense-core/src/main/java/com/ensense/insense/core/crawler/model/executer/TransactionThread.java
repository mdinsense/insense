package com.ensense.insense.core.crawler.model.executer;

import com.ensense.insense.data.common.model.CrawlConfig;
import com.ensense.insense.data.common.model.ExecutionStatus;
import com.ensense.insense.data.common.model.ScheduleDetails;
import com.ensense.insense.data.common.utils.Constants;
import com.ensense.insense.data.common.utils.FileDirectoryUtil;
import com.ensense.insense.data.common.utils.UiTestingConstants;
import com.ensense.insense.data.uitesting.entity.ScheduleScript;
import com.ensense.insense.data.utils.BrowserDriverLoaderUtil;
import com.ensense.insense.data.utils.BrowserEnum;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.context.MessageSource;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class TransactionThread implements Runnable {

	private static Logger logger = Logger.getLogger(TransactionThread.class);
	
	private MessageSource messageSource;
	private MessageSource configProperties;
	private ScheduleDetails appConfig;
	private int processCount;
	private ScheduleScript scheduleScript;
	private CrawlConfig crawlConfig;
	
	public TransactionThread(int processCount, MessageSource messageSource,
							 MessageSource configProperties, ScheduleDetails appConfig, CrawlConfig crawlConfig, ScheduleScript scheduleScript) {
		this.appConfig = appConfig;
		this.messageSource = messageSource;
		this.configProperties = configProperties;
		this.scheduleScript = scheduleScript;
		this.processCount = processCount;
		this.crawlConfig = crawlConfig;
	}

	@Override
	public void run() {
		logger.info("Starting the Thread");
		startTransactionTesting();
		logger.info("Ending the Thread");
	}

	private EventFiringWebDriver getWebDriver(ScheduleDetails appConfig, CrawlConfig crawlConfig, MessageSource configProperties, int processCount) {
		EventFiringWebDriver webDriver = null;
		WebDriver driver = null;
		WebDriverTransactionListener eventListener = null;

		try {
			driver = CrawlerSetup.getWebDriver(appConfig, configProperties);
			eventListener = CrawlerSetup.getWebDriverTransactionListener(driver,
					"Process :" + processCount, crawlConfig, appConfig, scheduleScript);
			webDriver = CrawlerSetup.getEventFiringTransactionWebDriver(driver,
					eventListener, appConfig);
			logger.info("webDriver :"+webDriver);
			webDriver.manage().timeouts().implicitlyWait(Long.parseLong("0"), TimeUnit.SECONDS);
		} catch (Exception e) {
			logger.info("Process :" + processCount
					+ "Exception while getting webdriver. Exception :" + e);
			logger.error("Process :" + processCount + "Stack Trace :"
					+ ExceptionUtils.getStackTrace(e));
		}
		
		return webDriver;
	}
	
	private boolean startTransactionTesting(){
		logger.info("Starting the Process :"+processCount);
		boolean status = true;
		EventFiringWebDriver webDriver = null;
		WebDriverTransactionListener eventListener = null;
		WebDriver driver = null;
		CrawlConfig localcrawlConfig = new CrawlConfig();
		
		try{
			logger.info("appConfig getBrowserType() :"+appConfig.getBrowserType());
			
			BrowserDriverLoaderUtil browserDriverLoaderUtil = new BrowserDriverLoaderUtil();
			if ( appConfig.getBrowserType().equals(BrowserEnum.CHROME.getBrowserTypeName())){
				driver = browserDriverLoaderUtil.getChromeDriver(appConfig, configProperties);
				eventListener = CrawlerSetup.getWebDriverTransactionListener(driver,
						"Process :" + processCount, localcrawlConfig, appConfig, scheduleScript);
				webDriver = CrawlerSetup.getEventFiringTransactionWebDriver(driver,
						eventListener, appConfig);
				logger.info("Chrome Driver");
				
			}else{
				webDriver = getWebDriver(appConfig, localcrawlConfig, configProperties, processCount);
			}
			String path =FileDirectoryUtil.getTransactionTestCaseRootPath(configProperties);
			path= path + scheduleScript.getTransactionTestCase().getTestCasePath() + Constants.FILE.CLASS;
			logger.info("Executing the Transaction script :"+path);
			scheduleScript.setExecutionStartDate(new Date());
			//testCasefiles.add(new File(path));
			if(!CrawlerSetup.executeTransactionTestCase(webDriver, appConfig, configProperties, path)) {
				scheduleScript.setErrorMessage("Error while executing the script.");
				scheduleScript.setExecutionStatus(ExecutionStatus.FAILED.getStatusCode());
				logger.info("Process Failed.");
			}else{
				scheduleScript.setExecutionStatus(ExecutionStatus.COMPLETE.getStatusCode());
				logger.info("Process completed successfully");
			}
		}catch(Exception e){
			status = false;
			scheduleScript.setErrorMessage(e.getMessage());
			scheduleScript.setExecutionLog(e.getMessage());
			scheduleScript.setExecutionStatus(ExecutionStatus.FAILED.getStatusCode());
		}
		
		if ( null != webDriver ){
			try{
				logger.info("Closing the webdriver");
				webDriver.close();
				logger.info("Webdriver closed");
			}catch(Exception e){
				logger.error("Exception while closing the webdriver."+e);
			}
		}
		
		//Write CrawlConfig.
		logger.info("Serialize directory :"+appConfig.getSerializePath() + File.separator + scheduleScript.getTransactionTestCase().getTestCasePath());
		logger.info("Script Name :"+scheduleScript.getTransactionTestCase().getTestCasePath());
		logger.info("URLs :"+localcrawlConfig.getCrawlStatus().getAlreadyListed());
		logger.info("crawlConfig :"+localcrawlConfig);
		logger.info("Serializing the Crawl Status.");
		FileDirectoryUtil.createDirectories(appConfig.getSerializePath() + File.separator + scheduleScript.getTransactionTestCase().getTestCasePath());
		CrawlerSetup.writeCrawlConfig(localcrawlConfig, appConfig.getSerializePath() + File.separator + scheduleScript.getTransactionTestCase().getTestCasePath() + File.separator + UiTestingConstants.CRAWL_CONFIG);
		
		logger.info("Completed the Process :"+processCount);
		return status;
	}
}
