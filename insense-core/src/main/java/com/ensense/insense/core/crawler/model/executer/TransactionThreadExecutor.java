package com.ensense.insense.core.crawler.model.executer;

import com.ensense.insense.data.common.model.CrawlConfig;
import com.ensense.insense.data.common.model.ScheduleDetails;
import com.ensense.insense.data.uitesting.entity.ScheduleScript;
import com.ensense.insense.data.uitesting.entity.ScheduleScriptXref;
import com.ensense.insense.data.utils.SerializeStatus;
import com.ensense.insense.services.reports.TestScheduleService;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class TransactionThreadExecutor {
	private static Logger logger = Logger.getLogger(TransactionThreadExecutor.class);
	
	public ScheduleDetails executeTransactionThreads(MessageSource messageSource, MessageSource configProperties, CrawlerThread initialCrawlerThread,
													 ScheduleDetails appConfig, TestScheduleService testScheduleService) throws Exception {
		logger.info("Entry :executeTransactionThreads");
		int maxThreadCount = appConfig.getNoOfThreads();
		maxThreadCount = 10;
		logger.info("maxThreadCount :"+maxThreadCount);
		List<TransactionThread> transactionThreadList = new ArrayList<TransactionThread>();
		
		//transactionThreadList = getThreadForProcess(maxThreadCount, messageSource,
				//configProperties, appConfig);
		
		StringBuffer emailContent = new StringBuffer();

		int stackSize = 0;
		String emailNotificationRecepients = getNotificationEmailRecepients(messageSource);
		
		boolean shutdownCalled = false;
		int loginSuccessfulWaitCount = 0;
		SerializeStatus serializeStatus = new SerializeStatus();
		boolean atleastOneThreadAlive = false;
		
		int activeThreadCount = 0;
		
		List<Future> futureList = new ArrayList<Future>();
		
		int processCount = 0;
		
		java.util.Queue<ScheduleScript> scheduleScriptQueue = new java.util.LinkedList<ScheduleScript>();
		
		for ( ScheduleScriptXref scheduleScript : appConfig.getScheduleScriptList() ){
			scheduleScriptQueue.add(scheduleScript.getScheduleScript());
		}
		
		logger.info("Initial scheduleScriptQueue.size() :"+scheduleScriptQueue.size());
		
		ExecutorService executor = Executors
				.newFixedThreadPool(maxThreadCount);
		
		while ( scheduleScriptQueue.size() > 0 || activeThreadCount > 0 ){
			if ( activeThreadCount < maxThreadCount && scheduleScriptQueue.size() > 0 ){
				ScheduleScript scheduleScript = scheduleScriptQueue.poll();
				CrawlConfig crawlConfig = new CrawlConfig();
				TransactionThread ct = new TransactionThread(processCount, messageSource,
						configProperties, appConfig, initialCrawlerThread.getCrawlConfig(), scheduleScript);
				
				try{
					Future f = executor.submit(ct);
					logger.info("Creating new Job number:"+processCount);
					futureList.add(f);
				}catch(Exception e){
					logger.error("Exception :"+e);
				}
				processCount++;
				
				try{
					Thread.sleep(40000);
				}catch(Exception e){
					
				}
			}else if ( !shutdownCalled ){
				logger.info("Shutdown called for Transaction Process");
				executor.shutdown();
				shutdownCalled = true;
			}
			
			if ( executor.isTerminated() && scheduleScriptQueue.size() > 0 && activeThreadCount == 0 ){
				executor = Executors
						.newFixedThreadPool(maxThreadCount);
				shutdownCalled = false;
				logger.info("Threads completed. Creating next set or threads.");
			}
			
			activeThreadCount = 0;
			if ( futureList.size() > 0 ){
				for ( int i=0; i < futureList.size(); i++ ){
					if ( !futureList.get(i).isDone() ){
						activeThreadCount++;
					}
				}
			}
		}
		logger.info("activeThreadCount :"+activeThreadCount);
		if ( futureList.size() > 0 ){
			logger.info("futureList.size() :"+futureList.size());
			for ( int i=0; i < futureList.size(); i++ ){
				logger.info("futureList.get(i).isCancelled() :"+futureList.get(i).isCancelled());
				logger.info("futureList.get(i).isDone() :"+futureList.get(i).isDone());
				if ( !futureList.get(i).isDone() ){
					activeThreadCount++;
				}
			}
			logger.info("activeThreadCount :"+activeThreadCount);
		}
		logger.info("appConfig :"+appConfig);
		
		logger.info("Exit :executeTransactionThreads");
		
		return appConfig;
	}
	
	/*private List<TransactionThread> getThreadForProcess(int threadCount,
			MessageSource messageSource, MessageSource configProperties,
			ScheduleDetails appConfig) throws Exception {
		List<TransactionThread> transactionThreadList = new ArrayList<TransactionThread>();
		
		for (int i = 0; i < threadCount; i++) {
			TransactionThread ct = new TransactionThread(i, messageSource,
					configProperties, appConfig);

			transactionThreadList.add(ct);
		}
	
		return transactionThreadList;
	}*/
	
	private String getNotificationEmailRecepients(MessageSource messageSource){
		String emailNotificationRecepients = "";
		try {
			emailNotificationRecepients = messageSource.getMessage(
				"mint.notification.emaillist", null, Locale.getDefault());
		}catch(Exception e){
			
		}
		return emailNotificationRecepients;
	}
}
