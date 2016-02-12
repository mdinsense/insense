package com.ensense.insense.core.crawler.model.executer;

import com.ensense.insense.core.transaction.model.jaxb.LinkNavigation;
import com.ensense.insense.core.utils.Constants;
import com.ensense.insense.core.utils.scheduler.SmartUserV2;
import com.ensense.insense.data.common.model.*;
import com.ensense.insense.data.uitesting.entity.ExcludeUrl;
import com.ensense.insense.data.uitesting.entity.IncludeUrl;
import com.ensense.insense.data.uitesting.entity.ScheduleScriptXref;
import com.ensense.insense.data.uitesting.model.ErrorType;
import com.ensense.insense.services.common.utils.CommonUtils;
import com.ensense.insense.services.common.utils.FileDirectoryUtil;
import com.ensense.insense.services.crawler.WebDriverListener;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.context.MessageSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class CrawlerThread implements Runnable {
	private static Logger logger = Logger.getLogger(CrawlerThread.class);
	private int threadCount;
	private int threadRestartCount;
	private static CrawlConfig crawlConfig = new CrawlConfig();
	private static CrawlStatus crawlStatus = new CrawlStatus();
	MessageSource messageSource;
	MessageSource configProperties;
	private static ScheduleDetails appConfig;
	
	public CrawlerThread(int threadCount, MessageSource messageSource,
			MessageSource configProperties, ScheduleDetails appConfig) {
		this.threadCount = threadCount;
		this.configProperties = configProperties;
		this.messageSource = messageSource;
	}

	public CrawlerThread(int threadCount, MessageSource messageSource,
			MessageSource configProperties, ScheduleDetails appConfig, CrawlStatus crawlStatus,
			CrawlConfig crawlConfig) {
		this.threadCount = threadCount;
		this.configProperties = configProperties;
		this.messageSource = messageSource;
		
		String executionTimeStamp = getCurrentTimeStamp();
		
		appConfig.setExecutionTimeStamp(executionTimeStamp);
		CrawlerThread.crawlStatus = crawlStatus;
		CrawlerThread.crawlConfig = crawlConfig;
		CrawlerThread.appConfig = appConfig;
		CrawlerThread.crawlConfig.setAppConfig(appConfig);
		CrawlerThread.crawlConfig.setUserId(appConfig.getUserId());
		CrawlerThread.crawlConfig.setCrawlStatus(crawlStatus);
	}
	
	public void setCrawlConfig(CrawlConfig crawlConfig){
		CrawlerThread.crawlConfig = crawlConfig;
	}
	
	public int getThreadCount(){
		return this.threadCount;
	}
	
	public void run() {
		logger.info("Starting the thread :" + this.threadCount);
		
		WebDriver webDriver = null;
		try {
			webDriver = startThreadCrawl();
		} catch (Exception e) {
			logger.error("Thread :"+this.threadCount + ", Exception in run.");
			logger.error("Thread :"+this.threadCount + "Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}


		closeDriver(webDriver);
		logger.info("Thread :"+this.threadCount + ", current Stack size:"+crawlStatus.getQueue().size());
		
		/*if ( crawlStatus.getQueue().size() > 0 && this.threadRestartCount < 2 ){
			try {
				logger.info("Thread :"+this.threadCount + ", restarting the Thread, since stack count greater than zero. Restart Count :"+this.threadRestartCount);
				this.threadRestartCount++;
				webDriver = startThreadCrawl();
			} catch (Exception e) {
				logger.error("Thread :"+this.threadCount + ", Exception in startThreadCrawl.");
				logger.error("Thread :"+this.threadCount + "Stack Trace :"+ExceptionUtils.getStackTrace(e));
			}
			closeDriver(webDriver);
		}*/
		
		if ( !appConfig.isTransactionTesting() && crawlStatus.getCurrentlyProcessingMap().size() > 0 ){
			logger.info("Yet to be processed link count :"+crawlStatus.getCurrentlyProcessingMap().size());
			for (Map.Entry<String, Link> entry : crawlStatus.getCurrentlyProcessingMap().entrySet()) {
			    //String key = entry.getKey();
			    Link value = entry.getValue();
			    logger.info("Yet to be processed link :"+value);
			}
		}

		logger.info("Ending the Thread :" + this.threadCount);
	}

	private void closeDriver(WebDriver webDriver) {
		if ( null != webDriver ){
			try{
				logger.info("webDriver.getWindowHandles().size() :"+webDriver.getWindowHandles().size());
				if ( webDriver.getWindowHandles().size() > 0 ){
					Thread.sleep(5000);

					webDriver.get(webDriver.getCurrentUrl());
					
					webDriver.close();
				}
			}catch(Exception e){
				logger.warn("Thread :"+this.threadCount + ", Exception while closing the driver.");
				//logger.error("Thread :"+this.threadCount + "Stack Trace :"+ExceptionUtils.getStackTrace(e));
			}
		}
		
	}

	public CrawlStatus getCrawlStatus(){
		return CrawlerThread.crawlStatus;
	}
	
	public CrawlConfig getCrawlConfig(){
		return CrawlerThread.crawlConfig;
	}
	


	/*private void doRebrandingSetup(ScheduleDetails appConfig2,
			MessageSource configProperties2, String executionTimeStamp) {
		String rebrandingDirectory = "";
		rebrandingDirectory = configProperties.getMessage(
				"mint.rebranding.directory.name", null, Locale.getDefault());

		String rebrandingDirectoryPath = appConfig.getResultsBaseDirectory()
				+ File.separator + appConfig.getApplicationName() + "_" + appConfig.getEnvironmentName()+"_"+executionTimeStamp + File.separator
				+ rebrandingDirectory + File.separator;
		String rebrandingDirectoryWithSlash = appConfig.getResultsBaseDirectory()
				+ "\\" + appConfig.getApplicationName() + "_" + appConfig.getEnvironmentName()+"_"+executionTimeStamp + "\\"
				+ rebrandingDirectory;

		FileDirectoryUtil.createDirectories(rebrandingDirectoryPath);

		appConfig.setBrandingReportsPath(rebrandingDirectoryWithSlash);
		
	}*/

	protected String getSkipUrl(List<ExcludeUrl> skipURL) {
		String skipUrls = "";
		
		if ( null == skipURL || skipURL.size() < 1){
			return skipUrls;
		}
		int count = 0;
		for (ExcludeUrl skipUrl : skipURL) {
			count++;
			skipUrls += skipUrl.getExcludeUrl();

			if (count < skipURL.size()) {
				skipUrls += ",";
			}
		}
		return skipUrls;
	}

	protected String getCrawlUrl(List<IncludeUrl> crawlURL) {
		String crawlUrls = "";

		if (null == crawlURL || crawlURL.size() < 1) {
			return crawlUrls;
		}
		int count = 0;
		for (IncludeUrl crawlUrl : crawlURL) {
			count++;
			crawlUrls += crawlUrl.getIncludeUrl();

			if (count < crawlURL.size()) {
				crawlUrls += ",";
			}

		}
		return crawlUrls;
	}


	public String getResultsAsXMLString(
			List<LinkNavigation.NavigationDetails> navigationlistDetails, String xmlFileName)
			throws Exception {
		logger.info("Entry: getResultsAsXMLString");
		ByteArrayOutputStream baos = null;
		try {
			LinkNavigation results = new LinkNavigation();

			results.getNavigationDetails().addAll(navigationlistDetails);
			JAXBContext context = JAXBContext
					.newInstance("com.cts.mint.transaction.model.jaxb");
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);
			baos = new ByteArrayOutputStream();
			marshaller.marshal(results, baos);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		logger.info("Exit: getResultsAsXMLString");
		return baos.toString();
	}

	private EventFiringWebDriver startThreadCrawl() throws Exception {
		int urlCountForThread = 0;
		EventFiringWebDriver webDriver = null;
		WebDriver driver = null;
		WebDriverListener eventListener = null;
		List<File> testCasefiles = null;

		try {
			driver = CrawlerSetup.getWebDriver(this, appConfig,
					configProperties);
			eventListener = CrawlerSetup.getWebDriverListener(driver,
					"Thread :" + this.getThreadCount(), crawlConfig, appConfig,configProperties);
			webDriver = CrawlerSetup.getEventFiringWebDriver(driver,
					eventListener, appConfig);
		} catch (Exception e) {
			logger.info("Thread :" + this.threadCount
					+ "Exception while getting webdriver. Exception :" + e);
			logger.error("Thread :" + this.threadCount + "Stack Trace :"
					+ ExceptionUtils.getStackTrace(e));
			throw e;
		}
		
		if(appConfig.isTransactionTesting()) {
			logger.debug("Transaction testing :"+appConfig.isTransactionTesting());
			testCasefiles = new ArrayList<File>();
			for(ScheduleScriptXref script: appConfig.getScheduleScriptList()) {
				try{
					String path ="";//FileDirectoryUtil.getTransactionTestCaseRootPath(configProperties);
					path= path + script.getScheduleScript().getTransactionTestCase().getTestCasePath() + Constants.FILE.CLASS;
					logger.info("Executing the Transaction script :"+path);
					script.getScheduleScript().setExecutionStartDate(new Date());
					//testCasefiles.add(new File(path));
					if(!CrawlerSetup.executeTransactionTestCase(webDriver, appConfig, configProperties, path)) {
						script.getScheduleScript().setErrorMessage("Error while executing the script.");
						script.getScheduleScript().setExecutionStatus(ExecutionStatus.FAILED.getStatusCode());
					}else{
						script.getScheduleScript().setExecutionStatus(ExecutionStatus.COMPLETE.getStatusCode());
					}
				}catch(Exception e){
					script.getScheduleScript().setErrorMessage("Exception while executing the script.");
					script.getScheduleScript().setExecutionStatus(ExecutionStatus.FAILED.getStatusCode());
				}
			}
			return webDriver;
		}

		try {
			if (!CrawlerSetup.checkLoginSuccessful(webDriver, this, appConfig,
					configProperties)) {
				// appConfig.setError("Exception while doing login for the Thread :"+"Thread "+this.threadCount);
				return webDriver;
			}

		} catch (Exception e) {
			// appConfig.setError("Exception while doing login.");
			logger.error("Exception while doing login for the Thread :"
					+ "Thread " + this.threadCount);
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
		}
		// if module contains transaction test cases... Transaction testcase beging here

		try {
			Link newAddress = null;

			logger.info("Thread :" + this.threadCount
					+ ", crawlStatus.getQueue().size() :"
					+ crawlStatus.getQueue().size());

			// If Stack is empty sleep for a minute.
			if (crawlStatus.getQueue().size() < 1) {
				logger.info("Thread :" + this.threadCount
						+ " put on sleep for 60 seconds. Stack is empty.");
				Thread.sleep(60000);
			}

			while (crawlStatus.getQueue().size() > 0
					&& (newAddress = crawlStatus.getQueue().poll()) != null) {
				
				newAddress.setThread("Thread " + this.threadCount);

				urlCountForThread++;
				
				//Check whether the URL is already 2 times, if yes mark as error and skip it.
				if ( CrawlerUtils.isUrlReachMaxTestAttempts(newAddress) ){
					newAddress.setPageAccessible(false);
					newAddress.setErrorPage(true);
					newAddress.setErrorType(ErrorType.UNABLE_TO_TEST.getErrorType());
					
					if ( !newAddress.isIncludeUrlPattern() ){
						crawlStatus.addToNavigationList(newAddress);
					}
					continue;
				}
				// Check browser is in open status.
				boolean allWindowsClosed = true;
				if (null != webDriver) {
					allWindowsClosed = webDriver.getWindowHandles().isEmpty();
				}else{
					return webDriver;
				}

				if (allWindowsClosed) {
					logger.warn("Thread :" + this.threadCount
							+ ", Browser NOT Found for the thread :"
							+ "Thread " + threadCount + ". Closing the Driver.");
					return webDriver;
				}

				if ( appConfig.isStaticUrlTesting() || (null != newAddress.getUrl()
						&& isAddressValid(appConfig, newAddress,
								appConfig.isProcessChildUrls())) ) {
					boolean processPageStatus = true;
					try {
						// Add to processing list Just to make sure processing
						// is completed for this URL. Once its complete it will
						// be removed from the Map.
						crawlStatus.addToCurrentlyProcessingMap(
								newAddress.getUrl() + newAddress.getParentUrl()
										+ newAddress.getNavigationPath(),
								newAddress);
						newAddress = processPage(newAddress, webDriver,
								eventListener, appConfig.isProcessChildUrls());
					}catch(org.openqa.selenium.UnhandledAlertException uae){
						processPageStatus = false;
						newAddress.setPageAccessible(false);
						newAddress.setErrorPage(true);
						crawlStatus.addToStack(newAddress);
						
						logger.error("Thread :" + this.threadCount
								+ ", Unable to close the popup, while processing the URL :"
								+ newAddress.getUrl());
						if (null != webDriver) {
							logger.info("Thread :"
									+ this.threadCount
									+ ", Closing the webdriver because of the session count reached.");
							webDriver.close();
							
							webDriver = startThreadCrawl();
							return webDriver;
						}

					}catch (Exception e) {
						logger.error("Thread :" + this.threadCount
								+ ", Exception while processing the URL :"
								+ newAddress.getUrl());
						logger.error("Thread :" + this.threadCount
								+ ", Details of the Exception occurrent URL :"
								+ newAddress.getParentUrl()
								+ ", and page tile :" + newAddress);
						logger.error("Thread :" + this.threadCount
								+ "Stack Trace :"
								+ ExceptionUtils.getStackTrace(e));
						// newAddress.setException(ExceptionUtils.getStackTrace(e));
						newAddress.setException(e.getMessage());
						processPageStatus = false;
						newAddress.setPageAccessible(false);
						newAddress.setErrorPage(true);
						crawlStatus.addToStack(newAddress);
						
					}
					finally{
					// URL is processed, so remove it.
						try{
							crawlStatus.removeFromCurrentlyProcessingMap(newAddress
									.getUrl()
									+ newAddress.getParentUrl()
									+ newAddress.getNavigationPath());
		
							if ( ! newAddress.isIncludeUrlPattern() ){
								crawlStatus.addToNavigationList(newAddress);
							}
						}catch(Exception e){
							logger.error("Exception while removing Link from CurrentlyProcessingMap.");
						}
					}
					
					// TODO, check whether its correct to comment below
					// increment statement.
					// crawlStatus.incrementFoundUrlCount();
					if (processPageStatus) {
						// logger.info("Thread :" + this.threadCount +
						// ", Ended URL:"+newAddress.getEndedUrl());
						// logger.info("Thread :" + this.threadCount +
						// ", Parent URL:"+newAddress.getParentUrl());

						// TODO, if newAddress.getEndedUrl() is null, do we need
						// to do anything.

						
						//Check whether we need to scroll the page, to collect the Analytics data.
						scrollPageAndWaitUntilAnalyticsDataFires(webDriver);
						
						newAddress.setPageAccessible(true);
						newAddress.setErrorPage(false);
						
						if (!appConfig.isStaticUrlTesting()
								&& !crawlConfig.getAppConfig().isPublicSite()
								&& null != newAddress.getEndedUrl()
								&& (newAddress.getEndedUrl().contains(
										"/login") || newAddress
										.getEndedUrl().contains("userLogout?"))) {
							crawlStatus.addToStack(newAddress);
							
							if (null != webDriver) {
								logger.info("Thread :"
										+ this.threadCount
										+ ", Closing the webdriver because of login page reached.");
								try{
									webDriver.close();
								}catch(Exception e){
								}

								webDriver = startThreadCrawl();
								return webDriver;
							}
						}
						try {
							if ( ( appConfig.isStaticUrlTesting() && appConfig.isProcessChildUrls() ) || ( !appConfig.isStaticUrlTesting() && CrawlerUtils.isValidUrlLevel(newAddress, appConfig.getUrlLevel()))) {
								collectNewLinkInCurrentPage(webDriver,
										eventListener, newAddress,
										appConfig.isProcessChildUrls(), 0);
							}
						} catch (Exception e) {
							logger.error("Thread :"
									+ this.threadCount
									+ ", Exception while finding child URLs(url from web driver) for :"
									+ webDriver.getCurrentUrl());
							// logger.error("Thread :" + this.threadCount +
							// ", Exception while finding child URLs(url from Link) for :"+newAddress);
							// logger.error("Thread :" + this.threadCount +
							// "Stack Trace :"+ExceptionUtils.getStackTrace(e));
						}
						
						if (appConfig.isRobotClicking()) {
							SmartUserV2 smartUserForAutoRobot = new SmartUserV2(
									crawlConfig.getJqueryFilePath(), "Thread :"+this.threadCount);
							try{
								newAddress.setAutoRoboClicking(true);
								eventListener.setProcessingPage(false);
								eventListener.setDoingRobotClicking(true);
								
								doAutoRobot(webDriver, smartUserForAutoRobot);
							}catch(Exception e){
								
							}
						}

					}
				}
				// }

				if( CrawlerUtils.isProcessNeedToBePausedOrStopped(crawlStatus) ){
					logger.info("Thread :"
							+ this.threadCount
							+ "Process is STOPPED/Paused.");
					break;
				}
				if (appConfig.getBrowserRestartCount() != 0
						&& urlCountForThread != 0
						&& urlCountForThread
								% appConfig.getBrowserRestartCount() == 0) {
					if (null != webDriver) {
						logger.info("Thread :"
								+ this.threadCount
								+ ", Closing the webdriver because of the session count reached.");
						try{
							webDriver.get(webDriver.getCurrentUrl());
							webDriver.close();
						}catch(Exception e){
							
						}
						
						webDriver = startThreadCrawl();
						return webDriver;
					}

				}
			}
		} catch (Exception e) {
			logger.error("Thread :" + this.threadCount
					+ ", Exception in startThreadCrawl.");
			logger.error("Thread :" + this.threadCount + "Stack Trace :"
					+ ExceptionUtils.getStackTrace(e));
		} finally {
		}
		return webDriver;
	}
	
	private void scrollPageAndWaitUntilAnalyticsDataFires(
			EventFiringWebDriver webDriver) {
		if ( appConfig.isAnalyticsTesting() ){
			
			String pageScroll = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.analytics.pageScroll");
			String pageScrollWaitTime = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.analytics.pageScrollWaitTime");
			
			int scrollWaitTime = 0;
			
			if ( StringUtils.isNotEmpty(pageScrollWaitTime)){
				scrollWaitTime = Integer.parseInt(pageScrollWaitTime);
			}
			
			if ( StringUtils.isNotEmpty(pageScroll) && "true".equals(pageScroll)){
				JavascriptExecutor jse = (JavascriptExecutor) webDriver;
		
				try {
					Thread.sleep(scrollWaitTime);
				} catch (Exception e) {
	
				}
				
				Object screensize = jse
						.executeScript("return Math.max(document.documentElement.scrollHeight,document.body.scrollHeight,document.documentElement.clientHeight);");
		
				long ss = (long) screensize;
				long screenHeight = webDriver.manage().window().getSize().height;
				int noOfScreens = (int) (ss / screenHeight);
		
				System.out.println("screenHeight :"+ screenHeight);
				System.out.println("screensize :" + screensize);
				System.out.println("noOfScreens :" + noOfScreens);
		
				long currentScreenPosition = screenHeight;
				
				for (int i = 0; i < noOfScreens; i++) {
					jse.executeScript("window.scrollTo(0,"
									+ currentScreenPosition + ");");
					currentScreenPosition += screenHeight;
		
					try {
						Thread.sleep(scrollWaitTime);
					} catch (Exception e) {
		
					}
				}
				
			    if ( ss > 0 ){
			    	ss = ss * -1;
			    }
			    jse.executeScript("window.scrollTo(0,"+ss+");");
			}
		}

	}

	protected List<WebElement> getAllElements(WebDriver driver,
			String elementName, int count) throws Exception{
		try {
			return driver.findElements(By.tagName("a"));
		} catch (StaleElementReferenceException e) {
			logger.error("Thread :" + this.threadCount + ", Exception while fetch anchor tags, no of times tried :"+count);
			count++;
			if ( count < 3 ){
				getAllElements(driver, elementName, count);
			}
			
		}
		return new ArrayList<WebElement>();
	}
	
	public Link processPage(Link currentLink, WebDriver driver,
			WebDriverListener eventListener, boolean processChildUrl) throws Exception {
		//logger.info("Entry :processPage");
		String currentHref = currentLink.getUrl();
		
		crawlConfig.incrementPageCount();

		/*logger.info(crawlConfig.getAppConfig().getTestScheduleId()
				+ " : Thread :" + this.threadCount
				+ ", navigationList.size :"
				+ crawlStatus.getNavigationList().size());*/
		logger.info(crawlConfig.getAppConfig().getTestScheduleId()
				+ " : Thread :" + this.threadCount
				+ ", URLs yet to be processed :"
				+ crawlStatus.getQueue().size());
		/*logger.info(crawlConfig.getAppConfig().getTestScheduleId()
				+ " : Thread :" + this.threadCount
				+ ", stopAfterUrlCount :"
				+ appConfig.getStopAfter());*/
		logger.info(crawlConfig.getAppConfig().getTestScheduleId()
				+ " : Thread :" + this.threadCount
				+ ", Opening the URL count :" + crawlConfig.getPageCount()
				+ " URL: " + currentLink.getUrl());

		// Opening the given page, event will be triggered to capture the
		// screen shot and html file
		try {
			currentLink.setUrlNo(crawlConfig.getPageCount());
			
			currentLink.setOpeningPage(true);
			currentLink.setCollectingChildUrl(false);
			
			eventListener.setLink(currentLink);
			eventListener.setProcessingPage(true);
			driver.manage().timeouts().pageLoadTimeout(crawlConfig.getAppConfig().getMaxWaitTime(), TimeUnit.SECONDS);
			//deleteCookies(driver);
			
			
			
			//Opening the Url.
			currentLink.setTimesTested(currentLink.getTimesTested() + 1);
			driver.get(currentLink.getUrl());
			
			checkIsAlertPresent(driver);
			currentLink.setPageTile(driver.getTitle());
			currentLink.setEndedUrl(driver.getCurrentUrl());
		} catch (UnreachableBrowserException e) {
			logger.error("Thread :" + this.threadCount +", UnreachableBrowserException while processing the URL :"+currentHref);
			logger.error("Thread :" + this.threadCount +", URL details :"+currentLink);
			logger.error("Thread :"+this.threadCount + "Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
			//return false;
		} catch(org.openqa.selenium.TimeoutException e){
			currentLink.setErrorPage(true);
			currentLink.setErrorType(ErrorType.TIME_OUT.getErrorType());
			logger.error("Thread :" + this.threadCount +", Timeout while accessing the URL :"+currentHref);
			logger.error("Thread :" + this.threadCount +", URL details :"+currentLink);
			logger.error("Thread :"+this.threadCount + "Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		}catch(org.openqa.selenium.UnhandledAlertException uae){
			boolean ableToCloseThePopup = false;
			try {
				/*WebDriverWait wait = new WebDriverWait(driver, 0);
				wait.until(ExpectedConditions.alertIsPresent());*/
				Alert alert = driver.switchTo().alert();
				alert.accept();
				ableToCloseThePopup = true;
			} catch (Exception e) {
			}
			
			if ( !ableToCloseThePopup ){
				throw new Exception(uae);
			}
		}
		catch (Exception e) {
			currentLink.setErrorType(ErrorType.UNKNOWN_EXCEPTION.toString());
			logger.error("Thread :" + this.threadCount +", Exceptin while processing the URL :"+currentHref);
			logger.error("Thread :" + this.threadCount +", URL details :"+currentLink);
			logger.error("Thread :"+this.threadCount + "Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		}

		/*if (appConfig.isRobotClicking()) {
			SmartUserV2 smartUserForAutoRobot = new SmartUserV2(
					crawlConfig.getJqueryFilePath(), "Thread :"+this.threadCount);
			try{
				currentLink.setAutoRoboClicking(true);
				eventListener.setProcessingPage(false);
				eventListener.setDoingRobotClicking(true);
				
				doAutoRobot(driver, smartUserForAutoRobot);
			}catch(Exception e){
				
			}
		}
*/
		if (driver.findElements(By.tagName("form")).size() > 0) {
			currentLink.setTransactionExists(true);
		}

		currentLink.setPageAccessible(true);
		// Add to navigation list

/*		logger.info(crawlConfig.getAppConfig().getTestScheduleId()
				+ " : stopAfterUrlCount :" + appConfig.getStopAfter()
				+ " navigationList.size() :"
				+ crawlStatus.getNavigationList().size());*/

		//logger.info("Exit :processPage");
		return currentLink;

	}
	
	private void checkIsAlertPresent(WebDriver driver) {
		try {
			driver.switchTo().alert();
		}// try
		catch (Exception e) {
			return;
		}
		try{
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("window.onbeforeunload = function() {};");
		}catch(Exception e){
			logger.error("Exception while closing the alert:"+e);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			
		}
	
	}

	public CrawlConfig collectNewLinkInCurrentPage(WebDriver webDriver, WebDriverListener eventListener,
			Link currentLink, boolean processChildUrl, int count) throws Exception {
		//logger.info("Entry :collectNewLinkInCurrentPage");
		String href = "";

		boolean hrefFound = true;
		
		List<WebElement> ancorTagList = getAllElements(webDriver, "a", 0);
		currentLink.setOpeningPage(false);
		currentLink.setCollectingChildUrl(true);
		
		eventListener.setProcessingPage(false);
		
		for (WebElement a : ancorTagList) {
			//if ( null == a || !a.isDisplayed() || !a.isEnabled() ){
			if ( null == a){
				continue;
			}
			try{
				href = a.getAttribute("href");
			}catch(Exception e){
				logger.error("Thread :" + this.threadCount + ", Exception while fetching child links for :"+webDriver.getCurrentUrl());
				
				try{
					logger.error("Thread :" + this.threadCount + ", Link Text :"+a.getText());
				}catch(Exception ex){
					//skip this exception
				}
				try{
					logger.error("Thread :" + this.threadCount + ", Link Text Content :"+a.getAttribute("textContent"));
				}catch(Exception ex1){
					//skip this one also.
				}
				hrefFound =  false;
				
				//logger.error("Thread :" + this.threadCount + ", Trying for "+count + " time.");
				//logger.error("Thread :" + this.threadCount + ", Stack Trace :"+ExceptionUtils.getStackTrace(e));

				
				
				//TODO, no use of below exception handling, its throwing because of invalid or hidden <a> tag.
				/*
				count++;
				//This might be stale data error, Sleep for 10 seconds and try couple more time.
				//Thread.sleep(10000);
				//try one more time if its stale error.

				if ( count < 3 ){
					collectNewLinkInCurrentPage(webDriver, currentLink, processChildUrl, count);
				}
				*/
			}
			//count = 0;
			// logger.info("Thread no :"+this.threadCount + ", href :"+href);
			
			if ( hrefFound){
				Link tempLink = new Link("", "", href, "", "", true, "", webDriver.getCurrentUrl(), false, appConfig.getPartialTextList(), currentLink.getUrlLevel() + 1);
				if (isAddressValid(crawlConfig.getAppConfig(), tempLink, processChildUrl)
						&& !crawlStatus.getAlreadyListed().contains(
								removeFromUrlForCompare(href,
										crawlConfig.getRemoveUrlPattern()))) {

					String imageDirectory = "";
					try{
						imageDirectory = CrawlerUtils.getImageDirectory(a.getAttribute("href"));
					}catch(Exception e){
						imageDirectory = "Unknown";
					}
	
					String linkText = "";
					try{
						linkText = a.getText();
					}catch(Exception e){
						linkText = "";
					}
					
					String linkAttribute ="";
					try{
						linkAttribute = a.getAttribute("title");
					}catch(Exception e){
						linkAttribute = "";
					}
					
					String imageName = "";
	
					// Create image with link name, if link name is not available
					// use pageLabel or
					
					if ( null != linkText && linkText.trim().length() > 0 ){
						//imageName = removeSpecialChars(linkText, true) + ".jpeg";
						imageName = linkText + ".jpeg";
					}else if ( null != linkAttribute && linkAttribute.trim().length() > 0 ){
						//imageName = removeSpecialChars(linkAttribute, true) + ".jpeg";
						imageName = linkAttribute + ".jpeg";
					}else if ( null == imageName || imageName.trim().length() < 1 ){
						String hiddenLinkText = CrawlerUtils.getHiddenLinkText(webDriver, a);
						
						hiddenLinkText = hiddenLinkText.trim();

						if ( null != hiddenLinkText && hiddenLinkText.trim().length() > 0 ){
							//imageName = removeSpecialChars(hiddenLinkText, true) + ".jpeg";
							imageName = hiddenLinkText + ".jpeg";
						}
					}else {
						// imageName = imageDirectory + ".jpeg";
						
						try {
							imageName = CrawlerUtils.getImageName(a.getAttribute("href")) + ".jpeg";
						} catch (Exception e) {
							imageName = "Unknown.jpeg";
						}
					}
					
					if (null == imageName || imageName.length() < 1) {
						imageName = "Unknown.jpeg";
					}
					
					String navigationPath = currentLink.getNavigationPath() + "->";
					String linkName = "";
					try{
						linkName = imageName.substring(0, imageName.indexOf(".jpeg"));
						navigationPath = navigationPath + linkName;
					}catch(Exception e){
						linkName = "UnknownLink";
						navigationPath = navigationPath + linkName;
						logger.error("Exception while finding navigation path for the URL :"+a.getAttribute("href"));
						logger.error("Parent Url :"+webDriver.getCurrentUrl());
					}
					// queue.add(new Link(removeSpecialChars(imageName),
					// removePound(imageDirectory), a.getAttribute("href"), false));
					Link newLink = new Link( CrawlerUtils.removeSpecialChars(imageName, true),
							FileDirectoryUtil.removePound( CrawlerUtils.removeSpecialChars(
									imageDirectory, false)),
							a.getAttribute("href"), navigationPath, linkName, true,
							webDriver.getTitle(), currentLink.getUrl(), false,
							appConfig.getPartialTextList(), currentLink.getUrlLevel() + 1);
					
					newLink.setIncludeUrlPattern(tempLink.isIncludeUrlPattern());
					// logger.info(appConfig.getTestScheduleId()+" :Link :"+currentLink);
	
					// Increase found url count
					
					if ( !newLink.isIncludeUrlPattern() ){
						crawlStatus.incrementFoundUrlCount();
					}
					
					if (appConfig.getStopAfter() == 0
							|| crawlStatus.getFoundUrlcount() <= appConfig.getStopAfter()) {
						crawlStatus.addToStack(newLink);
						crawlConfig.setCrawlStatus(crawlStatus);
						crawlStatus.addToAlreadyListed(removeFromUrlForCompare(href,
								crawlConfig.getRemoveUrlPattern()));
					} else {
						break;
					}
					
					logger.info("Thread :" + this.threadCount + ", Added new URLs, no of URLs to be processed :"
							+ crawlStatus.getQueue().size());
					logger.info("Url level :"+newLink.getUrlLevel());
/*					logger.info("Thread :" + this.threadCount + ", No of link yet to be processed :"
							+ crawlStatus.getStack().size());*/
				}
			}
		}
		//TODO:If there is any transaction part for this page execute it here. This logic is only for UD application, need to write generic logic.
		if ( StringUtils.startsWith(currentLink.getUrl(), "http://ke.glb.tiaa-cref.org/Lists/Articles/AllItems.aspx")){
			logger.info("Thread :" + this.threadCount + ", Before transaction URL:"+webDriver.getCurrentUrl());
			try{
				executeTransactionPart(webDriver, eventListener, currentLink);
			}catch(Exception e){
				logger.error("Thread :" + this.threadCount + ", Exception while executing Transaction for the URL :"+currentLink.getUrl());
				logger.error("Thread :" + this.threadCount + ", Stack Trace :"+ExceptionUtils.getStackTrace(e));
			}
			logger.info("Thread :" + this.threadCount + ", After transaction URL:"+webDriver.getCurrentUrl());
		}

		return crawlConfig;
	}

	private boolean executeTransactionPart(WebDriver webDriver,
			WebDriverListener eventListener, Link currentLink) throws Exception {
		Boolean isPresent = webDriver.findElements(
				By.cssSelector("img[alt=\"Next\"]")).size() > 0;

		if (isPresent) {
			Link transactionLink = new Link("next",
					 CrawlerUtils.getJ2eeAppName(webDriver.getCurrentUrl()),
					webDriver.getCurrentUrl(), currentLink.getNavigationPath()
							+ "->Next", "Next", true, webDriver.getTitle(),
					currentLink.getUrl(), false, appConfig.getPartialTextList(), currentLink.getUrlLevel() + 1);

			eventListener.setLink(transactionLink);
			webDriver
					.manage()
					.timeouts()
					.pageLoadTimeout(
							crawlConfig.getAppConfig().getMaxWaitTime(),
							TimeUnit.SECONDS);

			webDriver.findElement(By.cssSelector("img[alt=\"Next\"]")).click();
			Thread.sleep(10000);
			//webDriver.manage().timeouts().implicitlyWait(Long.parseLong("30"), TimeUnit.SECONDS);
			transactionLink.setUrl(webDriver.getCurrentUrl());
			transactionLink.setPageTile(webDriver.getTitle());
			
			if (!crawlStatus.getAlreadyListed().contains(
					removeFromUrlForCompare(transactionLink.getUrl(),
							crawlConfig.getRemoveUrlPattern()))) {
				crawlStatus.addToStack(transactionLink);
				crawlStatus.incrementFoundUrlCount();
				crawlConfig.setCrawlStatus(crawlStatus);
				logger.info("Thread :" + this.threadCount + ", Transaction URL added to the Stack :"+transactionLink);
			}
			
		} else {
			logger.error("Thread :" + this.threadCount + ", Next element not present in the URL :"
					+ currentLink.getUrl());
		}
		return isPresent;
	}

	private boolean isValidModule(ScheduleDetails appConfig, Link link, boolean processChildUrl) {
		
		for ( String testUrlPattern : appConfig.getTestModuleUrlPatternList() ){
			
			if ( null != link.getUrl() && link.getUrl().toLowerCase().contains(testUrlPattern.toLowerCase())){
				return true;
			}
			if ( processChildUrl ) {
				if ( null != link.getParentUrl() && link.getParentUrl().toLowerCase().contains(testUrlPattern.toLowerCase())){
					return true;
				}
			}
		}

		if ( null != appConfig.getIncludeModuleUrlPatternList() && appConfig.getIncludeModuleUrlPatternList().size() > 0 ){
			
			for ( String includeUrlPattern : appConfig.getIncludeModuleUrlPatternList() ){
				
				if ( link.getUrl().toLowerCase().contains(includeUrlPattern.toLowerCase())){
					link.setIncludeUrlPattern(true);
					return true;
				}
			}
		}

		return false;
	}



	private void doAutoRobot(WebDriver driver, SmartUserV2 smartUserForAutoRobot)
			throws Exception {
		logger.info("Entry : doAutoRobot");
		
		smartUserForAutoRobot.processUrl(driver, "");
		logger.info("Exit : doAutoRobot");
	}

	public boolean isAddressValid(ScheduleDetails appConfig, Link link, boolean processChildUrl) throws Exception {

		if (null == link || null == link.getUrl() || "".equals(link.getUrl())) {
			return false;
		}
		
		if ( !link.getUrl().trim().startsWith("http://") && !link.getUrl().trim().startsWith("https://") ){
			return false;
		}
		
		if (CrawlerUtils.isExcludeUrlPatternFound(link.getUrl(), appConfig.getExcludeUrl())) {
			return false;
		} 
		
		if ( null != appConfig.getTestModuleUrlPatternList() && appConfig.getTestModuleUrlPatternList().size() > 0 ){
			if ( isValidModule(appConfig, link, processChildUrl) ){
				return true;
			}else{
				return false;
			}
		}
		
		if (hasScanUrl(link, processChildUrl)) {
			return true;
		}
		
		return false;
	}

	private boolean hasScanUrl(Link link, boolean processChildUrl) throws Exception {
		boolean found = false;
		
		if ( CrawlerUtils.isIncludeUrlPatternFound(link.getUrl(), appConfig.getIncludeUrl()) ){
			found = true;
			return found;
		}
		if ( processChildUrl ) {
			found = CrawlerUtils.isIncludeUrlPatternFound(link.getParentUrl(), appConfig.getIncludeUrl());
		}
		
		return found;
	}

	private String removeFromUrlForCompare(String url, String removeUrlPattern)
			throws Exception {
		String newUrl = new String(url);

		if (null != removeUrlPattern && !"null".equals(removeUrlPattern)
				&& removeUrlPattern.length() > 0) {
			String[] tokens = removeUrlPattern.split(",");

			for (String token : tokens) {
				newUrl = url.replaceAll(token + "[^&]*&", "&");
			}

		}

		// logger.info("url :"+url);
		if (newUrl.indexOf("#") > 0) {
			newUrl = newUrl.substring(0, newUrl.lastIndexOf("#"));
		}

		return newUrl;
	}

	protected String getCurrentTimeStamp() {
		String dateFormat = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.crawler.dateFormat");
		return FileDirectoryUtil.getDate(dateFormat);
	}

	public static String generatePageRef(String url){
		URL newUrl = null;
		String pageRef=null;
		try {
			newUrl = new URL(url);
			pageRef=newUrl.getHost()+newUrl.getPath();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 	pageRef;
	}
}
