package com.ensense.insense.core.crawler.model.executer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import com.cts.mint.common.JsonReaderWriter;
import com.cts.mint.common.model.Link;
import com.cts.mint.common.utils.CommonUtils;
import com.cts.mint.common.utils.ExecutionStatus;
import com.cts.mint.common.utils.SerializeStatus;
import com.cts.mint.crawler.model.CrawlConfig;
import com.cts.mint.reports.service.TestScheduleService;
import com.cts.mint.uitesting.entity.ApplicationConfig;
import com.cts.mint.uitesting.model.ScheduleDetails;
import com.cts.mint.uitesting.model.UiTestingConstants;
import com.cts.mint.util.DateTimeUtil;
import com.cts.mint.util.EmailUtil;


public class CrawlerThreadExecutor {
	private static Logger logger = Logger.getLogger(CrawlerThreadExecutor.class);
	private static int NO_OF_URLS_TO_SERIALIZE = 10;
	private static int NO_OF_URLS_FOR_EACH_THREAD = 10;
	
	/*public ScheduleDetails restartThreadCrawling(MessageSource messageSource,
			MessageSource configProperties, ScheduleDetails appConfig) {

		//read appConfig from serialized location.
		CrawlConfig crawlConfig = readCrawlConfigFromSerializedPath(appConfig.getSerializePath());
		logger.info("In restartThreadCrawling crawlConfig :"+crawlConfig);
		
		// Initialize
		CrawlerThread initialCrawlerThread = new CrawlerThread(-1,
				messageSource, configProperties, crawlConfig.getAppConfig());
		initialCrawlerThread.setCrawlConfig(crawlConfig);
		
		//If already visited list is empty, do a fresh start.
		//TODO
		
		BMPProxyServer bmpProxyUtil = null;
		ProxyServer proxyServer = null;
		
		String useBMP = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.useBMPProxy");
		if ( useBMP.equals("true") ){
			appConfig.setUseBMPProxy(true);
		}
		try {
			//Check whether we need to start proxy server

			if ( appConfig.isUseBMPProxy()){
				appConfig.setUseBMPProxy(true);
				bmpProxyUtil = new BMPProxyServer();
				proxyServer = bmpProxyUtil.getBMPProxyServer(getBMPProxyHost(configProperties), getBMPProxyPort(configProperties));
				bmpProxyUtil.stopBMPProxyServer(proxyServer);
			}
		}catch(Exception e){
			logger.error("Exception while starting the BMP Proxy server.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		
		//Do some validation before re-starting crawler.
		try {
			logger.info("Before executeThreads");
			executeThreads(messageSource, configProperties,initialCrawlerThread, appConfig);
			logger.info("After executeThreads");
		} catch (Exception e1) {
			logger.error("Exception while executing the Threads :"+e1);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e1));
		}finally{
			if ( appConfig.isUseBMPProxy() ){
				if ( null != bmpProxyUtil && null != proxyServer ){
					bmpProxyUtil.stopBMPProxyServer(proxyServer);
				}
			}
		}
		
		appConfig = completeTheProcess(initialCrawlerThread, appConfig);
		
		return appConfig;
	}*/

	public ScheduleDetails startThreadCrawling(MessageSource messageSource, MessageSource configProperties,
			ScheduleDetails appConfig,TestScheduleService testScheduleService, CrawlerThread initialCrawlerThread) {
		String emailNotificationRecepients = getNotificationEmailRecepients(messageSource);
				
		if ( appConfig.getApplicationName().equals("UD_INTRANET")){
			/*sendEmail(
					messageSource,
					configProperties,
					appConfig, "Scheduled By :"+appConfig.getUserId()+
					", CrawlerThread Started for the application :"
							+ appConfig.getApplicationName() + "_" + appConfig.getEnvironmentName(),
					emailNotificationRecepients,
					"CrawlerThread Started for the application :"
							+ appConfig.getApplicationName() + "_" + appConfig.getEnvironmentName());*/
		}

		// Initialize
		
		try {
			logger.debug("Creating Threads for Crawling");
			executeThreads(messageSource, configProperties,initialCrawlerThread, appConfig, testScheduleService);
		} catch (Exception e1) {
			logger.error("Exception while executing the Threads :"+e1);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e1));
		}finally{

		}
		
		logger.info("All the mint Threads are completed.");
		
		logger.info("Processing Map Size :"+initialCrawlerThread.getCrawlStatus().getCurrentlyProcessingMap().size());
		
		
		appConfig = completeTheProcess(initialCrawlerThread, appConfig);
		
		// logger.info("Navigation Report :"+appConfig.getNavigationList());

		return appConfig;
	}

	private String  getBMPProxyHost(MessageSource configProperties) {
		String bmpProxyHost = "localhost";
		try{
			bmpProxyHost = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.bmpproxy.proxyhost");
		}catch(Exception e){
			bmpProxyHost = "localhost";
		}
		return bmpProxyHost;
	}

	private int getBMPProxyPort(MessageSource configProperties) {
		int bmpProxyPort = 8989;
		try{
			bmpProxyPort = Integer.parseInt(CommonUtils.getPropertyFromPropertyFile(configProperties, 
					"mint.bmpproxy.proxyport"));
		}catch(Exception e){
			bmpProxyPort = 8989;
		}
		return bmpProxyPort;
	}

	private ScheduleDetails completeTheProcess(
			CrawlerThread initialCrawlerThread, ScheduleDetails appConfig) {
		try {
			initialCrawlerThread.getCrawlConfig().setEndTime(DateTimeUtil.getCurrentDateTimeAsString(DateTimeUtil.DATE_TIME_FORMAT));
			
			if ( null != initialCrawlerThread.getCrawlConfig().getCrawlStatus().getQueue() ){
				if ( initialCrawlerThread.getCrawlConfig().getCrawlStatus().getQueue().size() > 1 ){
					initialCrawlerThread.getCrawlConfig().getCrawlStatus().setRunStatus(ExecutionStatus.PAUSED.getStatusCode());
				}else {
					initialCrawlerThread.getCrawlConfig().getCrawlStatus().setRunStatus(ExecutionStatus.COMPLETE.getStatusCode());
				}
			}else{
				initialCrawlerThread.getCrawlConfig().getCrawlStatus().setRunStatus(ExecutionStatus.FAILED.getStatusCode());
			}
			//JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();
			//jsonReaderWriter.writeJsonObjectToFile(initialCrawlerThread.getCrawlConfig(), appConfig.getSerializePath()+File.separator + UiTestingConstants.CRAWL_CONFIG);
			logger.info("Updating completion status to Crawl Config.");
			appConfig.setRunStaus(initialCrawlerThread.getCrawlConfig().getCrawlStatus().getRunStatus());
			CrawlerSetup.writeCrawlConfig(initialCrawlerThread.getCrawlConfig(), appConfig.getSerializePath()+File.separator + UiTestingConstants.CRAWL_CONFIG);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Store Navigation detial to disk.
		writeNavigationListDetails(initialCrawlerThread, appConfig);
		
		// Generate Navigation list details.
		//logger.info("Started generating Navigation report.");
		//appConfig = initialCrawlerThread.generateNavigationList();
		//logger.info("Completed generating Navigation report.");
		return appConfig;
	}

	private void executeThreads(MessageSource messageSource, MessageSource configProperties, CrawlerThread initialCrawlerThread,
			ScheduleDetails appConfig, TestScheduleService testScheduleService) throws Exception {
		int maxThreadCount = appConfig.getNoOfThreads();
		List<CrawlerThread> crawlerThreadList = new ArrayList<CrawlerThread>();
		
		int noOfUrlsSerialized = 0;
		crawlerThreadList = getThreadForProcess(maxThreadCount, messageSource,
				configProperties, appConfig);
		
		ExecutorService executor = Executors
				.newFixedThreadPool(maxThreadCount + 5);


		//Create Total threads.
		//List<Future> futureList = startAllThreadsForCrawling(threadCount, executor, crawlerThreadList);
		List<Future> futureList = startFirstThreadForCrawling(maxThreadCount, executor, crawlerThreadList);

		try{
			Thread.sleep(2000);
		}catch(Exception e){
			
		}
		
		int noOfTimestackBecameZero = 0;
		StringBuffer emailContent = new StringBuffer();

		int stackSize = 0;
		String emailNotificationRecepients = getNotificationEmailRecepients(messageSource);
		
		boolean shutdownCalled = false;
		int loginSuccessfulWaitCount = 0;
		SerializeStatus serializeStatus = new SerializeStatus();
		boolean atleastOneThreadAlive = false;
		
		int activeThreadCount = 0;
		
		if ( futureList.size() > 0 ){
			for ( int i=0; i < futureList.size(); i++ ){
				if ( !futureList.get(i).isDone() ){
					atleastOneThreadAlive = true;
					activeThreadCount++;
				}
			}
		}
		
		logger.info("activeThreadCount :"+activeThreadCount);
		
		while (!executor.isTerminated()) {
			atleastOneThreadAlive = false;
			try{
				//if ( maxThreadCount >= futureList.size() && isNewTheadNeeded(initialCrawlerThread, appConfig, maxThreadCount, futureList) ){
				if ( maxThreadCount >= activeThreadCount && isNewTheadNeeded(initialCrawlerThread, appConfig, maxThreadCount, activeThreadCount) ){
					
					if ( !serializeStatus.isStopOrPauseCalled() ){
						addNewThread(maxThreadCount, executor, crawlerThreadList,futureList, initialCrawlerThread,
								appConfig, activeThreadCount);
					}
				}

				serializeStatus = serializeCrawlConfig(serializeStatus, initialCrawlerThread, appConfig);
				
				//Check whether atleast one thread is running
				activeThreadCount = 0;
				if ( futureList.size() > 0 ){
					for ( int i=0; i < futureList.size(); i++ ){
						if ( !futureList.get(i).isDone() ){
							atleastOneThreadAlive = true;
							activeThreadCount++;
						}
					}
				}else if ( initialCrawlerThread.getCrawlStatus().getNavigationList().size() > 0 ){
					executor.shutdownNow();
					shutdownCalled = true;
					logger.info("Shutdown called here");
				}else{
					logger.info("Navigation list is ZERO");
				}
				
				//if ( serializeStatus.isStopOrPauseCalled() || !atleastOneThreadAlive ){
				if(initialCrawlerThread.getCrawlStatus().getNavigationList().size() > 0 && !atleastOneThreadAlive ){
					executor.shutdownNow();
					shutdownCalled = true;
					logger.info("Shutdown called here");
				}
				
				stackSize = initialCrawlerThread.getCrawlStatus().getQueue().size();
				
				if ( initialCrawlerThread.getCrawlStatus().getNavigationList().size() > 0 && stackSize < 1) {
					noOfTimestackBecameZero++;
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if ( noOfTimestackBecameZero > 2 && !shutdownCalled ){
						executor.shutdown();
						shutdownCalled = true;
						logger.info("Shutdown called here");
					}
	
					if (noOfTimestackBecameZero > 0) {
						logger.info("Crawling stack size became ZERO for "
								+ noOfTimestackBecameZero
								+ " times. Close all the browsers if they are still open.");
	
						emailContent = new StringBuffer();
						
						//TODO, if futureList.get(i).isCancelled() is false, thread is still running.
						for (int i = 0; i < futureList.size(); i++) {
							logger.info("Is Still running Thread " + i + ": "
									+ futureList.get(i).isDone());
							logger.info("Is cancelled Thread " + i + ": "
									+ futureList.get(i).isCancelled());
	
							if (noOfTimestackBecameZero < 6) {
								emailContent.append("Current state for the Thread "
										+ i + ": " + futureList.get(i).isDone()
										+ "\n");
								emailContent.append("Is cancelled Thread " + i
										+ ": " + futureList.get(i).isCancelled()
										+ "\n");
							}
						}
	
						// Send email notification for closing Firefox browsers, if
						// they are still open.
						if (noOfTimestackBecameZero < 6) {
							emailNotificationRecepients = messageSource.getMessage(
									"mint.notification.emaillist", null,
									Locale.getDefault());
							/*sendEmail(
									messageSource,
									configProperties,
									appConfig,
									"Crawling stack size became ZERO for "
											+ noOfTimestackBecameZero
											+ " times for the app:"
											+ appConfig.getApplicationName(),
									emailNotificationRecepients,
									emailContent.toString());*/
							
						}else{
							executor.shutdownNow();
							shutdownCalled = true;
							logger.info("Shutdown called here. Some of the browser might be still in open status.");
							
							break;
						}
	
					}
				}
			}catch(Exception e){
				
			}
		}
			
		if ( executor.isTerminated() ){
			logger.info("Terminated");
			if ( !shutdownCalled ){
				executor.shutdown();
				shutdownCalled = true;
				logger.info("Shutdown called here");
			}
		}else{
			logger.info("Not terminated");
		}
	}

	@SuppressWarnings("rawtypes")
	private void addNewThread(int maxThreadCount, ExecutorService executor,
			List<CrawlerThread> crawlerThreadList, List<Future> futureList, CrawlerThread initialCrawlerThread,
			ScheduleDetails appConfig, int activeThreadCount) {
		int newThreadNumber = futureList.size();

		if ( newThreadNumber > 0 && newThreadNumber < maxThreadCount && newThreadNumber < crawlerThreadList.size() ){
			int stackSize = initialCrawlerThread.getCrawlStatus().getQueue().size();
			logger.info("Queue size :"+stackSize + ", creating thread :"+ activeThreadCount);
			Future f = executor.submit(crawlerThreadList.get(newThreadNumber));
			futureList.add(f);
			
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private boolean isNewTheadNeeded(CrawlerThread initialCrawlerThread,
			ScheduleDetails appConfig, int maxThreadCount, int activeThreadCount) {
		boolean createNewThread = false;
		int stackSize = initialCrawlerThread.getCrawlStatus().getQueue().size();

		if ( activeThreadCount >= maxThreadCount || activeThreadCount == 0 ){
			return createNewThread;
		}
		
		if ( stackSize / ( activeThreadCount * NO_OF_URLS_FOR_EACH_THREAD ) >= 1 ){
			createNewThread = true;
		}
		return createNewThread;
	}

	@SuppressWarnings("rawtypes")
	private List<Future> startFirstThreadForCrawling(int threadCount,
			ExecutorService executor, List<CrawlerThread> crawlerThreadList) {
		List<Future> futureList = new ArrayList<Future>();
		Future f = executor.submit(crawlerThreadList.get(0));
		futureList.add(f);
		return futureList;
	}

	private SerializeStatus serializeCrawlConfig(SerializeStatus serializeStatus,
			CrawlerThread initialCrawlerThread, ScheduleDetails appConfig) {
		int processedUrlCount = initialCrawlerThread.getCrawlStatus().getNavigationList().size();
		
		if ( serializeStatus.getNoOfUrlsSerialized() + NO_OF_URLS_TO_SERIALIZE < processedUrlCount ){

			CrawlConfig crawlConfigRead = null;
			CrawlConfig crawlConfigWrite = null;
			
			try{
				/*if ( noOfUrlsSerialized == 0 ){
					initialCrawlerThread.getCrawlConfig().setStatus("STARTED");
				}else{
					initialCrawlerThread.getCrawlConfig().setStatus("PROCESSING");
				}*/

				//JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();
				
				//Read the craw config object before write, check whether execution status is update to either pause/stop.
				long time = System.currentTimeMillis();
				crawlConfigRead = readCrawlConfigFromSerializedPath(appConfig.getSerializePath());
				
				crawlConfigWrite = initialCrawlerThread.getCrawlConfig();
				
				if ( null != crawlConfigRead && null != crawlConfigRead.getCrawlStatus() && 
						crawlConfigRead.getCrawlStatus().getRunStatus() == ExecutionStatus.PAUSED.getStatusCode() ){
						
					crawlConfigWrite.setExecutionAction(ExecutionStatus.PAUSED);
					crawlConfigWrite.getCrawlStatus().setRunStatus(ExecutionStatus.PAUSED.getStatusCode());
					serializeStatus.setStopOrPauseCalled(true);
					logger.info("Pause Added, CrawlConfig :"+crawlConfigWrite);
				}else if ( null != crawlConfigRead && null != crawlConfigRead.getCrawlStatus() && 
						crawlConfigRead.getCrawlStatus().getRunStatus() == ExecutionStatus.STOPPED.getStatusCode() ){
					crawlConfigWrite.setExecutionAction(ExecutionStatus.STOPPED);
					crawlConfigWrite.getCrawlStatus().setRunStatus(ExecutionStatus.STOPPED.getStatusCode());
					serializeStatus.setStopOrPauseCalled(true);
					logger.info("Stop Added, CrawlConfig :"+crawlConfigWrite);
				}

				crawlConfigWrite.setLastRecordedTime(DateTimeUtil.getCurrentDateTimeAsString(DateTimeUtil.DATE_TIME_FORMAT));
				
				CrawlerSetup.writeCrawlConfig(initialCrawlerThread.getCrawlConfig(), appConfig.getSerializePath()+File.separator + UiTestingConstants.CRAWL_CONFIG);
				
				logger.info("Time taken to read & write CrawlConfig Object :"+(System.currentTimeMillis() - time));
				
				//crawlConfigRead = readCrawlConfigFromSerializedPath(appConfig.getSerializePath());

				serializeStatus.setNoOfUrlsSerialized(processedUrlCount);
			}catch(Exception e){
				logger.error("Exception while reading Crawlconfig Object from file :"+appConfig.getSerializePath() + File.separator + UiTestingConstants.CRAWL_CONFIG);
				logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			}
			
			if ( null != crawlConfigWrite ){
				logger.info("crawlConfig, currently processing Map :"+crawlConfigWrite.getCrawlStatus().getCurrentlyProcessingMap());
			}
			logger.info("Serialized Url data, count :"+processedUrlCount);
		}
		return serializeStatus;
	}


	
	private void sendEmail(MessageSource messageSource,
			MessageSource configProperties, ApplicationConfig appConfig,
			String subject, String emailRecepients, String emailContent) {
		String smtpHost = configProperties.getMessage(
				"mint.webservice.smtphost", null, Locale.getDefault());
		String emailFrom = configProperties.getMessage(
				"mint.crawler.emailfrom", null, Locale.getDefault());
		EmailUtil emailUtil = new EmailUtil(smtpHost, emailFrom);

		List<String> emailTo = emailUtil.getRecepientsList(emailRecepients);

		emailUtil.sendEmail(emailTo, subject, emailContent);
	}
	
	protected boolean writeNavigationListDetails(CrawlerThread initialCrawlerThread, ScheduleDetails appConfig){
		try{
			JsonReaderWriter<List<Link>> jsonReaderWriter = new JsonReaderWriter<List<Link>>();
			jsonReaderWriter.writeJsonObjectToFile(initialCrawlerThread.getCrawlStatus().getNavigationList(), appConfig.getSerializePath()+File.separator + UiTestingConstants.NAVIGATION_DETAILS_OBJ);
			List<Link> linkList = appConfig.getNavigationList();
		}catch(Exception e){
			logger.error("Exception while writing navigation details as serialize obj."+e);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			return false;
		}
		return true;
	}
	
	private List<CrawlerThread> getThreadForProcess(int threadCount,
			MessageSource messageSource, MessageSource configProperties,
			ScheduleDetails appConfig) throws Exception {
		List<CrawlerThread> crawlerThreadList = new ArrayList<CrawlerThread>();
		
		for (int i = 0; i < threadCount; i++) {
			CrawlerThread ct = new CrawlerThread(i, messageSource,
					configProperties, appConfig);

			crawlerThreadList.add(ct);
		}
	
		return crawlerThreadList;
	}
	
	private String getNotificationEmailRecepients(MessageSource messageSource){
		String emailNotificationRecepients = "";
		try {
			emailNotificationRecepients = messageSource.getMessage(
				"mint.notification.emaillist", null, Locale.getDefault());
		}catch(Exception e){
			
		}
		return emailNotificationRecepients;
	}
	
	private CrawlConfig readCrawlConfigFromSerializedPath(
			String path) {
		CrawlConfig crawlConfig = new CrawlConfig();
		try{
			crawlConfig = null;
			JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();
			logger.info("Reading CrawlConfig from the path :"+path + File.separator + UiTestingConstants.CRAWL_CONFIG);
			crawlConfig = jsonReaderWriter.readJsonObjectFromFile(new CrawlConfig(), path + File.separator + UiTestingConstants.CRAWL_CONFIG);
			
		}catch(Exception e){
			logger.error("Exception while reading Crawl Config :"+e);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		return crawlConfig;
	}
}
