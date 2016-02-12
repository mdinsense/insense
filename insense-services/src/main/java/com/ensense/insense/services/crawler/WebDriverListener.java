package com.ensense.insense.services.crawler;


import com.ensense.insense.core.utils.MintConstants;
import com.ensense.insense.data.common.model.*;
import com.ensense.insense.data.common.util.MintFileCaptureUtil;
import com.ensense.insense.data.uitesting.model.ErrorType;
import com.ensense.insense.data.uitesting.model.TextImageReportData;
import com.ensense.insense.services.common.utils.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.springframework.context.MessageSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class WebDriverListener implements WebDriverEventListener {	
	private WebDriver webDriver;
	private Link pageLink;
	private String applicationDirectory;
	private static final Logger logger = Logger.getLogger(WebDriverListener.class);
	private boolean doRegressionTesting;
	private List<String> errorPageidentifier;
	private String threadNumber;
	private List<HtmlFileDetails> removedHtmlTags;
	private List<String>tiaaImageNameList;
	private List<String>textSearchList;
	private boolean htmlCompare;
	private boolean textCompare;
	private boolean screenCompare;
	private String harReportsDirectory;
	private boolean processingPage;
	private boolean doingRobotClicking;
	private boolean brokenLinkReport;
	private ScheduleDetails appConfig;
	private String textImageDirectoryPath;
	private List<UrlFormElement> urlFormElementList;
	private CrawlConfig crawlConfig;
	private CrawlConfig baseLineCrawlConfig;
	private MessageSource configProperties;
	private String applicationName;
	private String environmentName;
	public WebDriverListener(WebDriver webDriver){		
		this.webDriver = webDriver;	
	} 	
	
	public WebDriverListener(WebDriver driver, String threadNumber, CrawlConfig crawlConfig, ScheduleDetails appConfig, MessageSource configProperties){
		this.webDriver = driver;
		this.applicationDirectory = appConfig.getDirectory();
		this.doRegressionTesting = appConfig.isRegressionTesting();
		this.errorPageidentifier = crawlConfig.getErrorpageIdentifiers();
		this.threadNumber = threadNumber;
		
		this.removedHtmlTags = new ArrayList<HtmlFileDetails>();
		this.tiaaImageNameList = appConfig.getTiaaImageNameList();
		this.textSearchList =  appConfig.getTextSearchList();
		this.htmlCompare = appConfig.isHtmlCompare();
		this.textCompare = appConfig.isTextCompare();
		this.screenCompare = appConfig.isScreenCompare();
		this.harReportsDirectory = appConfig.getHarReportsPath();
		this.brokenLinkReport = appConfig.isBrokenUrlReport();
		this.appConfig = appConfig;
		this.baseLineCrawlConfig = appConfig.getBaseLineCrawlConfig();
		this.textImageDirectoryPath = appConfig.getReportsPath() + File.separator + appConfig.getTextImageDirectory();
		this.crawlConfig = crawlConfig;
		this.configProperties = configProperties;
		this.applicationName = appConfig.getApplicationName();
		this.environmentName = appConfig.getEnvironmentName();
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
		//logger.info("Thread :"+threadNumber + ", Ended URL:"+driver.getCurrentUrl());
		this.urlFormElementList = new ArrayList<UrlFormElement>();
	
		if ( null != pageLink) {
			pageLink.setEndedUrl(driver.getCurrentUrl());
		}
		
		try {
			//if ( doRegressionTesting  &&  null != pageLink && pageLink.getImageDirectory() != null && pageLink.getImageName() != null && !pageLink.isIncludeUrlPattern()){
			if (  null != pageLink && pageLink.getImageDirectory() != null && pageLink.getImageName() != null && !pageLink.isIncludeUrlPattern()){
				try{
					Thread.sleep(2000);
				}catch(Exception e){
					
				}
				
				if ( brokenLinkReport ){
					boolean errorPageContent = isPageHavingErrorContent(driver.getPageSource());
					
					if ( errorPageContent ){
						pageLink.setErrorPage(errorPageContent);
						pageLink.setErrorType(ErrorType.ERROR_PAGE.getErrorType());
					}
				}
				//TODO
				//if(htmlCompare || textCompare) {
				String htmlFileFullPath = MintFileCaptureUtil.writeHtmlSourceToFile(applicationDirectory + File.separator + pageLink.getImageDirectory(), pageLink.getImageName(), driver.getPageSource());
				pageLink.setHtmlFileFullPath(htmlFileFullPath);
					//TODO
					//if ( textCompare ){
						String textFileFullPath = MintFileCaptureUtil.captureHtmlContentAsText(applicationDirectory + File.separator + pageLink.getImageDirectory(), pageLink.getImageName(), driver.getPageSource(), url, pageLink.getUrlNo(), this.appConfig);
						pageLink.setTextFileFullPath(textFileFullPath);
						if ( null != pageLink.getPartialTextList() && pageLink.getPartialTextList().size() > 0 ){
							updatePartialHtmlContents(driver.getPageSource());
						}
					//}
				//}

				//TODO, Capturing screen shot for all the cases.
				screenCompare = true;
				String imageFullPath = "";
				if(screenCompare){
					imageFullPath = MintFileCaptureUtil.captureScreenShot(applicationDirectory + File.separator + pageLink.getImageDirectory(), pageLink.getImageName(), webDriver);
					pageLink.setImageFullPath(imageFullPath);
				}  
				
				if ( checkErrorPage(driver)){
					pageLink.setErrorPage(true);
					pageLink.setErrorType(ErrorType.ERROR_PAGE.toString());
				}
				
				if ( ( null != tiaaImageNameList && tiaaImageNameList.size() > 0 ) || ( null != textSearchList && textSearchList.size() > 0 ) ){
					String pageDirectory = textImageDirectoryPath + File.separator + pageLink.getUrlNo();
					String allPageImages = pageDirectory + File.separator + "AllPageImages";
					String tiaaImages = pageDirectory + File.separator + "TiaaImages";
					FileDirectoryUtil.createDirectories( allPageImages);
					FileDirectoryUtil.createDirectories(tiaaImages);
					
					TextImageReportData textImageReportData = new TextImageReportData();
					textImageReportData.setUrlNo(pageLink.getUrlNo());
					textImageReportData.setUrl(pageLink.getUrl());
					
					if ( tiaaImageNameList.size() > 0 ){
						textImageReportData = TextImageCaptureUtil.downloadAllImagesAndGetDetails(driver, textImageReportData, allPageImages,
							    tiaaImages, threadNumber, tiaaImageNameList);
					}
					
					String htmlAsText = "";
					
					try {
						MintFileUtils.writeHtmlContentToFile(url, pageDirectory + File.separator + "Url.txt", true);
						MintFileUtils.copyFile(new File(pageLink.getTextFileFullPath()), new File(pageDirectory + File.separator + MintFileUtils.getFileNameInUrl(pageLink.getTextFileFullPath())) );
						htmlAsText = MintFileUtils.readFromFile(pageLink.getTextFileFullPath());
					} catch(Exception ex) {
						ex.printStackTrace();
					}
					
					textImageReportData.setTiaaCrefStringCount(TextImageCaptureUtil.getStringCount(htmlAsText, MintConstants.TIAA_CREF_STRING));
					textImageReportData.setTiaaStringCount(TextImageCaptureUtil.getStringCount(htmlAsText, MintConstants.TIAA_STRING) - TextImageCaptureUtil.getStringCount(htmlAsText, MintConstants.TIAA_CREF_STRING));
					
					pageLink.setTextImageReportData(textImageReportData);
				}
			}
			
		} catch (Exception e) {
			logger.error(threadNumber + ", Exception while capturing screen shot or Html");
			logger.error(threadNumber + "Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	 private boolean isElementPresent(WebDriver driver, By by) {
		    try {
		      driver.findElement(by);
		      return true;
		    } catch (NoSuchElementException e) {
		      return false;
		    }
		  }

	private boolean isPageHavingErrorContent(String pageSource) {
		try{
			String pageSourcetemp = pageSource.toLowerCase();
			for ( String errorString : errorPageidentifier){
				String errorStringTemp = errorString.trim().toLowerCase();
				if ( pageSourcetemp.contains(errorStringTemp)){
					return true;
				}
			}
			return false;
		}catch(Exception e){
			
		}
		
		return false;
	}

	private void updatePartialHtmlContents(String htmlSource) {
		String htmlcontent = "";
		if ( ! CollectionUtils.sizeIsEmpty(pageLink.getPartialTextList())){
			
			for (PartialText partialText : pageLink.getPartialTextList()) {
		    
			    if ( StringUtils.isNotEmpty(partialText.getContentName())  ){
			    	try{
			    		htmlcontent = HtmlParserUtil.getHtmlContent(htmlSource, partialText.getHtmlTagPath());
			    		htmlcontent = HtmlParserUtil.getHtmlContentWithoutHtmlTags(htmlcontent);
			    	}catch(Exception e){
			    		logger.error("Error while getting HTML content for the TAG :"+partialText.getHtmlTagPath());
			    	}
			    	String partialTextPath = "";
			    	try{
			    		if ( null != htmlcontent && htmlcontent.length() > 0 && null != pageLink.getTextFileFullPath() && pageLink.getTextFileFullPath().length() > 0 ){
				    		partialTextPath = pageLink.getTextFileFullPath();
				    		partialTextPath = partialTextPath.replace(".txt", partialText.getContentName() + "_"+".html");
				    		logger.info("Writing Partial Text file :"+partialTextPath);
				    		MintFileUtils.writeHtmlContentToFile(htmlcontent, partialTextPath, false);
				    		
				    		partialText.setPartialTextPath(partialTextPath);
			    		}
			    	}catch(Exception e){
			    		logger.error("Error while writing into file :"+partialTextPath);
			    	}
			    }
			}
		}
	
	}

	private boolean checkErrorPage(WebDriver driver) {
		try{
			if ( null != errorPageidentifier && driver !=null && driver.getPageSource() != null ){
				for (String errorPage : errorPageidentifier ){
					if ( driver.getPageSource().toLowerCase().contains(errorPage.toLowerCase().trim())){
						return true;
					}
				}
			}
		}catch(Exception e){
			logger.error(threadNumber + ", Exception while checking for error page.");
			logger.error(threadNumber + ", Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public void afterChangeValueOf(WebElement arg0, WebDriver arg1) {
		//logger.info("afterChangeValueOf, WebElement :"+arg0);
		
	}
	@Override
	public void afterClickOn(WebElement arg0, WebDriver arg1) {
		//logger.info("In afterClickOn, WebElement :"+arg0);
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
			if ( null == pageLink ) {
				pageLink = new Link("", "", driver.getCurrentUrl(), "", "", false, "", "", true, new ArrayList<PartialText>(), 0);
			}
			
			if ( isProcessingPage() ){
				pageLink.setEndedUrl(driver.getCurrentUrl());
				pageLink.setPageAccessible(false);
				pageLink.setErrorPage(true);
				
				logger.error(threadNumber + ", Exception while opening the URL:"+webDriver.getCurrentUrl());
				//logger.error("Thread :"+threadNumber + ", Stack Trace :"+ExceptionUtils.getStackTrace(e));
			}else if ( isDoingRobotClicking() ){
				logger.error(threadNumber + ", Exception while clicking the element");
			}
		}catch(Exception ex){
			logger.error("Exception in onException of Listener."+e);
		}
	}
}