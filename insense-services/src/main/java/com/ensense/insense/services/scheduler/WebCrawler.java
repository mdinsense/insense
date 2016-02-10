package com.ensense.insense.services.scheduler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.context.MessageSource;

import com.cts.mint.common.model.Link;
import com.cts.mint.common.utils.ExecutionStatus;
import com.cts.mint.crawler.CrawlerSetup;
import com.cts.mint.crawler.CrawlerThread;
import com.cts.mint.crawler.CrawlerThreadExecutor;
import com.cts.mint.crawler.TransactionThreadExecutor;
import com.cts.mint.crawler.WebDriverListener;
import com.cts.mint.crawler.model.CrawlConfig;
import com.cts.mint.crawler.model.CrawlStatus;
import com.cts.mint.crawler.model.ReportsConfig;
import com.cts.mint.reports.service.TestScheduleService;
import com.cts.mint.uitesting.model.ScheduleDetails;
import com.cts.mint.uitesting.model.UiTestingConstants;
import com.cts.mint.util.DateTimeUtil;

public class WebCrawler {
	private static final Logger logger = Logger
			.getLogger(WebCrawler.class);
	
	private MessageSource messageSource;
	private MessageSource configProperties;
	
	public WebCrawler(MessageSource messageSource, MessageSource configProperties){
		this.messageSource = messageSource;
		this.configProperties = configProperties;
	}
	
	public ScheduleDetails executeUiTesting(ScheduleDetails appConfig,TestScheduleService testScheduleService) throws Exception{
		logger.info("Entry: In RunDemnand Invoking Crawler, testCase->"+appConfig);
		

		//String useBMP = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.useBMPProxy");
		
		/*if ( useBMP.equals("true") ){
			if ( startBMPServer(appConfig) ){
				return appConfig;
			}
		}*/
		
		CrawlerThread initialCrawlerThread = null;
		//Setup directories
		try{
			if ( appConfig.getTestExecutionStatusRefId() == ExecutionStatus.IN_PROGRESS.getStatusCode() ) {
				initialCrawlerThread = new CrawlerThread(-1, messageSource, configProperties, appConfig, new CrawlStatus(), new CrawlConfig());
	
				if ( ! doPendingScheduleSetup(appConfig, testScheduleService, initialCrawlerThread) ){
					return appConfig;
				}
				
				//Initialize the queue for Crawling, for static URL testing add all the URLs to the QUEUE.
				if ( appConfig.isStaticUrlTesting() ){
					if ( ! initializeForStaticUrlTesting(appConfig, initialCrawlerThread) ) {
						return appConfig;
					}
				}else{
					if ( !appConfig.isTransactionTesting() && !initializeForCrawling(appConfig, initialCrawlerThread, configProperties) ){
						return appConfig;
					}
				}
			} else if ( appConfig.getTestExecutionStatusRefId() == ExecutionStatus.RESTART.getStatusCode()  ){
				CrawlConfig crawlConfig = CrawlerSetup.readCrawlConfig(appConfig);
				logger.info("RESTARTING crawlConfig :"+crawlConfig);
				
				if ( null == crawlConfig.getAppConfig().getDirectory() || crawlConfig.getAppConfig().getDirectory().length() < 1 ){
					appConfig.setError("In valid Results directory path. Path :"+crawlConfig.getAppConfig().getDirectory());
				}else{
					appConfig.setDirectory(crawlConfig.getAppConfig().getDirectory());	
				}
				
				if ( null == crawlConfig.getAppConfig().getSerializePath() ){
					appConfig.setError("Invalid Serialize path. Path :"+crawlConfig.getAppConfig().getSerializePath());
				}else{
					appConfig.setSerializePath(crawlConfig.getAppConfig().getSerializePath());
				}
				
				initialCrawlerThread = new CrawlerThread(-1, messageSource, configProperties, appConfig, crawlConfig.getCrawlStatus(), crawlConfig);
			}
			
			if ( ! StringUtils.isEmpty(appConfig.getError()) ){
				logger.error("appConfig.getError() :"+appConfig.getError());
				return appConfig;
			}
		}catch(Exception e){
			appConfig.setError("Exception while Setting up Directories.");
			return appConfig;
		}
		
		if ( !appConfig.isTransactionTesting() ){
			try{
	
				//check whether Include Url pattern set, otherwise set domain name as include Url pattern
				if ( initialCrawlerThread.getCrawlStatus().getQueue().size() > 0 ){
					if ( null == appConfig.getIncludeUrl() || appConfig.getIncludeUrl().size() < 1 ){
						List<String> includeUrlList = getDomainNameFromHomeUrl(initialCrawlerThread.getCrawlStatus().getQueue().peek());
						appConfig.setIncludeUrl(includeUrlList);
					}
				}else{
					appConfig.setError("Exception while login/Unable to reach Home page.");
					return appConfig;
				}
				
				boolean hasValidUrlToProcess = false;
				
				logger.info("Queue Size :"+ initialCrawlerThread.getCrawlStatus().getQueue().size() );
				
				for(Link link : initialCrawlerThread.getCrawlStatus().getQueue()) {
					logger.info("Check whether the URL is valid, Url :"+link.getUrl());
					hasValidUrlToProcess = initialCrawlerThread.isAddressValid(appConfig, link, false);
					if ( hasValidUrlToProcess){
						break;
					}
				}
	
				if ( ! hasValidUrlToProcess ){
					appConfig.setError("Queue does have any Valid URLs to process.");
					return appConfig;
				}
			}catch(Exception e){
				appConfig.setError("Exception while login/Unable to reach Home page.");
				return appConfig;
			}
			//Update schedule start status so that comparison will start
			if ( ! initiateComparison(appConfig, testScheduleService, initialCrawlerThread) ){
				return appConfig;
			}
		}
		
		if ( appConfig.isTransactionTesting() ){
			//Start Transaction testing.
			logger.debug("Starting the Transaction Testing.");
			return startTransactionTestingExecution(appConfig, testScheduleService, initialCrawlerThread);
		}else{
			//Start Crawling.
			logger.debug("Starting the Crawl");
			return startCrawling(appConfig, testScheduleService, initialCrawlerThread);
		}

	}
	
	private ScheduleDetails startTransactionTestingExecution(
			ScheduleDetails appConfig, TestScheduleService testScheduleService,
			CrawlerThread initialCrawlerThread) throws Exception{
		TransactionThreadExecutor transactionThreadExecutor = new TransactionThreadExecutor();
		return transactionThreadExecutor.executeTransactionThreads(messageSource, configProperties, initialCrawlerThread, appConfig, testScheduleService);
	}

	private List<String> getDomainNameFromHomeUrl(Link link) {
		logger.info("Entry : getDomainNameFromHomeUrl, link->"+link.getUrl());
		String domainName = "";
		List<String> includeUrlList = new ArrayList<String>();
		try{
			if ( null != link && null != link.getUrl() && link.getUrl().trim().length() > 0 ){
				domainName = link.getUrl().substring(link.getUrl().indexOf("//")+2, link.getUrl().length());
				domainName = domainName.substring(0, domainName.indexOf("/"));
			}
		}catch(Exception e){
			domainName = "";
		}
		includeUrlList.add(domainName);
		
		logger.info("Exit : getDomainNameFromHomeUrl, domainName->"+domainName);
		return includeUrlList;
	}

	private boolean initiateComparison(ScheduleDetails appConfig,
			TestScheduleService testScheduleService,
			CrawlerThread initialCrawlerThread) {
		/*try{
			
			CompareConfig compareConfig = new CompareConfig();
			if ( appConfig.isRegressionTesting() ){
				compareConfig.setCompareEndTime(ExecutionStatus.PENDING.getStatus());
				compareConfig.setCompareStartTime(ExecutionStatus.PENDING.getStatus());
				compareConfig.setLastUpdatedTime(DateTimeUtil.getCurrentDateTime());
			}else{
				compareConfig.setCompareEndTime(ExecutionStatus.NOT_APPLICABLE.getStatus());
				compareConfig.setCompareStartTime(ExecutionStatus.NOT_APPLICABLE.getStatus());
			}
			CrawlerSetup.writeCompareConfig(initialCrawlerThread, compareConfig);
logger.info("compareConfig :"+compareConfig);	
		}catch(Exception e){
			logger.error("Exception while Serializing CompareConfig Object.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			return false;
		}*/
		
		try{
			if ( appConfig.isRegressionTesting() ){
				logger.info("Updating Schedule Execution :"+appConfig);
				testScheduleService.insertComparisonStart(appConfig);
			}
		}catch(Exception e){
			logger.error("Exception while inserting comparions start status.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			return false;
		}
		
		try{
			ReportsConfig reportsConfig = new ReportsConfig();
			if ( appConfig.isBrokenUrlReport() || appConfig.isAnalyticsTesting() ){
				reportsConfig.setReportsStartTime(ExecutionStatus.PENDING.getStatus());
				reportsConfig.setLastUpdatedTime(DateTimeUtil.getCurrentDateTime());
				reportsConfig.setReportsEndTime("");
			}else{
				reportsConfig.setReportsStartTime(ExecutionStatus.NOT_APPLICABLE.getStatus());
				reportsConfig.setReportsEndTime(ExecutionStatus.NOT_APPLICABLE.getStatus());
				reportsConfig.setLastUpdatedTime(ExecutionStatus.NOT_APPLICABLE.getStatus());
			}
			CrawlerSetup.writeReportsConfig(initialCrawlerThread, reportsConfig);
		}catch(Exception e){
			logger.error("Exception while Serializing ReportsConfig Object.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			return false;
		}

		return true;
	}

	private ScheduleDetails startCrawling(ScheduleDetails appConfig,
			TestScheduleService testScheduleService, CrawlerThread initialCrawlerThread) {
		logger.debug("Starting the Crawling, for the application :"+appConfig.getApplicationName()+", Environment :"+appConfig.getEnvironmentName());
		CrawlerThreadExecutor crawlerThreadExecutor = new CrawlerThreadExecutor();
		ScheduleDetails testCaseExecution = crawlerThreadExecutor.startThreadCrawling(messageSource, configProperties, appConfig, testScheduleService, initialCrawlerThread);
		
		logger.debug("Completed the Crawling, for the application :"+appConfig.getApplicationName()+", Environment :"+appConfig.getEnvironmentCategory());
		return testCaseExecution;
		
	}

	/*private boolean startBMPServer(ScheduleDetails appConfig) {
		BMPProxyServer bmpProxySer = new BMPProxyServer();
		
		//TODO
		appConfig.setError("Exception while starting BMP Proxy server.");
		return false;
	}*/

	private boolean initializeForCrawling(ScheduleDetails appConfig,
			CrawlerThread initialCrawlerThread, MessageSource configProperties) {
		logger.debug("Entry : initializeForCrawling");
		EventFiringWebDriver webDriver = null;
		WebDriver driver = null;
		boolean status = true;
		WebDriverListener eventListener = null;
		try {
			driver = CrawlerSetup.getWebDriver(initialCrawlerThread, appConfig, configProperties);
			eventListener = CrawlerSetup.getWebDriverListener(driver, "Thread :"+initialCrawlerThread.getThreadCount(), initialCrawlerThread.getCrawlConfig(), appConfig, configProperties);
			webDriver =	CrawlerSetup.getEventFiringWebDriver(driver, eventListener, appConfig);
		}catch(Exception e){
			appConfig.setError("Error while initial Setup. Might be Browser issue.");
			status = false;
			return status;
		}
		
		try{
			if ( !appConfig.isTransactionTesting() && ! CrawlerSetup.checkLoginSuccessful(webDriver, initialCrawlerThread, appConfig, configProperties) ){
				appConfig.setError("Exception while doing login.");
				logger.error("Exception while doing login.");
				initialCrawlerThread.getCrawlConfig().getCrawlStatus().setRunStatus(ExecutionStatus.FAILED.getStatusCode());
				
				status = false;
			}
			
			if ( status ){
				initialCrawlerThread.getCrawlConfig().getCrawlStatus().setRunStatus(ExecutionStatus.IN_PROGRESS.getStatusCode());
				
				String homeImage = initialCrawlerThread.getCrawlConfig().getAppConfig().getApplicationName()
						+ "_home.jpeg";
				String homePageTitle = "HomePage";
				
				if ( null != webDriver.getTitle() && webDriver.getTitle().trim().length() > 0 ){
					homePageTitle = webDriver.getTitle();
				}
				
				if ( null != webDriver && null != webDriver.getCurrentUrl() ){
					Link homePageLink = new Link(homeImage, "", webDriver.getCurrentUrl(),
							homePageTitle, homePageTitle, true, webDriver.getTitle(), "", false, appConfig.getPartialTextList(), 0);
					
					initialCrawlerThread.getCrawlStatus().addToStack(homePageLink);
					initialCrawlerThread.getCrawlStatus().addToAlreadyListed(homePageLink.getUrl());
					initialCrawlerThread.getCrawlConfig().setErrorpageIdentifiers(CrawlerSetup.getErrorPageIdentifiers(configProperties));
					try{
						initialCrawlerThread.getCrawlConfig().setJqueryFilePath(CrawlerSetup.getJqueryFilePath(configProperties));
					}catch(Exception e){
						status = false;
					}
				}
				
				initialCrawlerThread.getCrawlConfig().setAppConfig(appConfig);
			}
		}catch(Exception e){
			appConfig.setError("Exception while doing login.");
			logger.error("Exception while doing login.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			initialCrawlerThread.getCrawlConfig().getCrawlStatus().setRunStatus(ExecutionStatus.FAILED.getStatusCode());
			
			status = false;
		}finally{
			if ( webDriver != null ){
				try{
					webDriver.close();
				}catch(Exception e){
					
				}
			}
		}
		
		try{
			if ( status && initialCrawlerThread.getCrawlStatus().getQueue().size() < 1){
				appConfig.setError("Ulr, no URLs found to process. EXITING the CRAWLER.");
				logger.error("STACK size is empty, nothing to process. EXITING the CRAWLER.");
				initialCrawlerThread.getCrawlConfig().getCrawlStatus().setRunStatus(ExecutionStatus.FAILED.getStatusCode());
				
				status = false;
			}
			
		}catch(Exception e){
			initialCrawlerThread.getCrawlConfig().getCrawlStatus().setRunStatus(ExecutionStatus.FAILED.getStatusCode());
			appConfig.setError("Ulr, no URLs found to process. EXITING the CRAWLER.");
			status = false;
		}
		logger.info("Updating In progress status to Crawl Config.");
		CrawlerSetup.writeCrawlConfig(initialCrawlerThread.getCrawlConfig(), appConfig.getSerializePath() + File.separator + UiTestingConstants.CRAWL_CONFIG);
		logger.debug("Exit : initializeForCrawling");
		return status;
	}

	private boolean initializeForStaticUrlTesting(ScheduleDetails appConfig, CrawlerThread initialCrawlerThread) {
		boolean status = true;
		int staticUrlCount = 0;
		try {
			initialCrawlerThread.getCrawlConfig().getCrawlStatus().setRunStatus(ExecutionStatus.IN_PROGRESS.getStatusCode());
			staticUrlCount = CrawlerSetup.initializeForStaticUrlTesting(appConfig, initialCrawlerThread);
			
			initialCrawlerThread.getCrawlConfig().setErrorpageIdentifiers(CrawlerSetup.getErrorPageIdentifiers(configProperties));
			
			try{
				initialCrawlerThread.getCrawlConfig().setJqueryFilePath(CrawlerSetup.getJqueryFilePath(configProperties));
			}catch(Exception e){
				status = false;
			}
		} catch (Exception e) {
			appConfig.setError("Exception while getting Static URLs. No of Urls found :"+staticUrlCount);
			logger.error("Exception while getting Static URLs. No of Urls found :"+staticUrlCount);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			initialCrawlerThread.getCrawlConfig().getCrawlStatus().setRunStatus(ExecutionStatus.FAILED.getStatusCode());
			
			status = false;
		}
		
		if ( initialCrawlerThread.getCrawlStatus().getQueue().size() < 1){
			appConfig.setError("STACK size is empty, no URLs found to process. EXITING the CRAWLER.");
			logger.error("STACK size is empty, nothing to process. EXITING the CRAWLER.");
			initialCrawlerThread.getCrawlConfig().getCrawlStatus().setRunStatus(ExecutionStatus.FAILED.getStatusCode());
			
			status = false;
		}
		logger.info("Serializing the Crawl Status for static urls.");
		CrawlerSetup.writeCrawlConfig(initialCrawlerThread.getCrawlConfig(), appConfig.getSerializePath() + File.separator + UiTestingConstants.CRAWL_CONFIG);
		return status;
	}

	private boolean doPendingScheduleSetup(ScheduleDetails appConfig, TestScheduleService testScheduleService, CrawlerThread initialCrawlerThread) {
		logger.debug("Entry: doPendingScheduleSetup");
		boolean status = true;
		try{
			CrawlerSetup.doInitializeSetup(messageSource, configProperties, appConfig, testScheduleService, initialCrawlerThread);
			initialCrawlerThread.getCrawlConfig().getCrawlStatus().setRunStatus(ExecutionStatus.IN_PROGRESS.getStatusCode());
		}catch(Exception e){
			appConfig.setError("Error while setting up directories.");
			initialCrawlerThread.getCrawlConfig().getCrawlStatus().setRunStatus(ExecutionStatus.FAILED.getStatusCode());
			status = false;
		}
		logger.info("Serializing the Crawl Status.");
		CrawlerSetup.writeCrawlConfig(initialCrawlerThread.getCrawlConfig(), appConfig.getSerializePath() + File.separator + UiTestingConstants.CRAWL_CONFIG);
		
		logger.debug("Exit: doPendingScheduleSetup");
		return status;
	}

	public ScheduleDetails restartTestCases(ScheduleDetails testCase,TestScheduleService testScheduleService, CrawlerThread initialCrawlerThread) throws Exception{
		logger.info("Entry: In restartTestCases Invoking Crawler, testCase->"+testCase);
		CrawlerThreadExecutor crawlerThreadExecutor = new CrawlerThreadExecutor();
		ScheduleDetails testCaseExecution = crawlerThreadExecutor.startThreadCrawling(messageSource, configProperties, testCase,testScheduleService, initialCrawlerThread);
		logger.info("Exit: In restartTestCases Invoking Crawler, testCase->"+testCase);
		
		return testCaseExecution;
	}
}
