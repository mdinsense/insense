package com.ensense.insense.core.crawler.model.executer;

import com.ensense.insense.data.common.model.CrawlConfig;
import com.ensense.insense.data.common.model.HtmlFileDetails;
import com.ensense.insense.data.common.model.Link;
import com.ensense.insense.data.common.model.ScheduleDetails;
import com.ensense.insense.data.common.util.MintFileCaptureUtil;
import com.ensense.insense.data.uitesting.entity.ScheduleScript;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.springframework.context.MessageSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class WebDriverTransactionListener implements WebDriverEventListener {
	private static final Logger logger = Logger.getLogger(WebDriverTransactionListener.class);
	
	private WebDriver webDriver;
	private Link pageLink;
	private String applicationDirectory;
	private boolean doRegressionTesting;
	private List<String> errorPageidentifier;
	private String threadNumber;
	private List<HtmlFileDetails> removedHtmlTags;
	private List<String>tiaaImageNameList;
	private boolean htmlCompare;
	private boolean textCompare;
	private boolean screenCompare;
	private String harReportsDirectory;
	private boolean processingPage;
	private boolean doingRobotClicking;
	private boolean brokenLinkReport;
	private CrawlConfig crawlConfig;
	private ScheduleScript scheduleScript;
	private MessageSource configProperties;
	
	public WebDriverTransactionListener(WebDriver webDriver){		
		this.webDriver = webDriver;	
	} 	
	
	public WebDriverTransactionListener(WebDriver webDriver, String resultsDirectory, MessageSource configProperties){		
		this.webDriver = webDriver;	
		this.configProperties = configProperties;
	} 
	public WebDriverTransactionListener(WebDriver driver, String threadNumber, CrawlConfig crawlConfig, ScheduleDetails appConfig, ScheduleScript scheduleScript){
		this.webDriver = driver;
		this.applicationDirectory = appConfig.getDirectory();
		this.doRegressionTesting = appConfig.isRegressionTesting();
		this.errorPageidentifier = crawlConfig.getErrorpageIdentifiers();
		this.threadNumber = threadNumber;
		
		this.removedHtmlTags = new ArrayList<HtmlFileDetails>();
		this.tiaaImageNameList = appConfig.getTiaaImageNameList();
		this.htmlCompare = appConfig.isHtmlCompare();
		this.textCompare = appConfig.isTextCompare();
		this.screenCompare = appConfig.isScreenCompare();
		this.harReportsDirectory = appConfig.getHarReportsPath();
		this.brokenLinkReport = appConfig.isBrokenUrlReport();
		this.crawlConfig = crawlConfig;
		this.scheduleScript = scheduleScript;
		
	}
	
	public boolean isProcessingPage() {
		return processingPage;
	}

	public void setProcessingPage(boolean processingPage) {
		this.processingPage = processingPage;
	}

	public boolean isDoingRobotClicking() {
		return doingRobotClicking;
	}

	public void setDoingRobotClicking(boolean doingRobotClicking) {
		this.doingRobotClicking = doingRobotClicking;
	}

	public void setLink(Link link){
		this.pageLink = link;
	}
	
	public void beforeNavigateTo(String url, WebDriver driver) {		
	} 	
	
	public void afterNavigateTo(String url, WebDriver driver) {
		if ( isNewURL(driver) ){
			Link link = createNewLink(driver);
			takeScreenShot(driver, link);
			addToProcessedList(link);
		}
	}

	private Link createNewLink(WebDriver driver) {
		int pageCount = 1;
		Link link = new Link();
		try{
			if ( this.crawlConfig.getCrawlStatus().getAlreadyListed().size() > 0 ){
				pageCount = this.crawlConfig.getCrawlStatus().getAlreadyListed().size() + 1;
			}
			
			link.setUrlNo(pageCount);
			link.setUrl(driver.getCurrentUrl());
			String screenShotPath = applicationDirectory + File.separator + this.scheduleScript.getTransactionTestCase().getTestCasePath() + File.separator + "page"+pageCount+".jpeg";
			logger.info("screenShotPath :"+screenShotPath);
			
			link.setImageFullPath(screenShotPath);
		}catch(Exception e){
			logger.error("Exception while creating new link");
		}
		return link;
	}

	private void addToProcessedList(Link link) {
		this.crawlConfig.getCrawlStatus().addToAlreadyListed(link.getUrl());
		this.crawlConfig.getCrawlStatus().addToNavigationList(link);
	}

	private void takeScreenShot(WebDriver driver, Link link) {
		
		try{
			MintFileCaptureUtil.captureScreenShot(link.getImageFullPath(), driver);
		}catch(Exception e){
			logger.error("Exception while capturing the screen shot.");
		}
		
	}

	private boolean isNewURL(WebDriver driver) {
		try{
			return !this.crawlConfig.getCrawlStatus().getAlreadyListed().contains(driver.getCurrentUrl());
		}catch(Exception e){
			return true;
		}
	}

	@Override
	public void afterChangeValueOf(WebElement arg0, WebDriver arg1) {
		//logger.info("afterChangeValueOf, WebElement :"+arg0);
		//logger.info("pageLink.getUrl() :"+pageLink.getUrl());
		//logger.info("afterChangeValueOf, WebElement :"+arg1.getCurrentUrl());
		
	}
	@Override
	public void afterClickOn(WebElement arg0, WebDriver driver) {
		logger.info("In afterClickOn, WebElement :"+arg0);
		if ( isNewURL(driver) ){
			Link link = createNewLink(driver);
			takeScreenShot(driver, link);
			addToProcessedList(link);
		}
	}
	@Override
	public void afterFindBy(By arg0, WebElement arg1, WebDriver arg2) {
		//logger.info("afterFindBy, WebElement :"+arg1);
	}
	
	@Override
	public void afterNavigateBack(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void afterNavigateForward(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void afterScript(String arg0, WebDriver arg1) {
		//logger.info("afterScript, arg0 :"+arg0);
		
	}
	@Override
	public void beforeChangeValueOf(WebElement arg0, WebDriver arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void beforeClickOn(WebElement arg0, WebDriver arg1) {
		//logger.info("In beforeClickOn, WebElement :"+arg0);
		
	}
	@Override
	public void beforeFindBy(By arg0, WebElement arg1, WebDriver arg2) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void beforeNavigateBack(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void beforeNavigateForward(WebDriver arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void beforeScript(String arg0, WebDriver arg1) {
		//logger.info("beforeScript, arg0 :"+arg0);
		
	}
	@Override
	public void onException(Throwable e, WebDriver driver) {
		try{
			logger.error("Exception while processing the transaction testcase");
			throw new Exception("Exception while process the transaction testcase.");
		}catch(Exception ex){
			
		}
	}
}