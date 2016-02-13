package com.ensense.insense.core.crawler.model.executer;

import com.ensense.insense.data.common.model.*;
import com.ensense.insense.data.utils.scheduler.SmartUserV2;
import com.ensense.insense.data.common.utils.FileDirectoryUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.util.ArrayList;


public class WebPageProcessor {

	private static final Logger logger = Logger.getLogger(WebPageProcessor.class);
	
	//public boolean processPage(TestCaseExecution appConfig, String applicationName, WebDriver driver, WebDriverListener eventListener, String skipUrl, String crawlUrl, String removeUrlPattern, boolean robotEnabled, String jqueryFilePath, int stopAfterUrlCount) throws Exception{
	public boolean processPage(CrawlConfig crawlConfig) throws Exception{
		logger.info("Entry: processPage");

		Link currentLink = crawlConfig.getCrawlStatus().getCurrentLink();
		if (null != currentLink.getUrl() && isAddressValid(crawlConfig, currentLink.getUrl())) {

			crawlConfig.getEventListener().setLink(currentLink);
			crawlConfig.setPageCount(crawlConfig.getPageCount() + 1);

			logger.info(crawlConfig.getAppConfig().getTestScheduleId()+" :navigationList.size :"+crawlConfig.getCrawlStatus().getNavigationList().size());
			logger.info(crawlConfig.getAppConfig().getTestScheduleId()+" :URLs yet to be processed :"+crawlConfig.getCrawlStatus().getQueue().size());
			logger.info(crawlConfig.getAppConfig().getTestScheduleId()+" :stopAfterUrlCount :"+crawlConfig.getStopAfterUrlCount());
			logger.info(crawlConfig.getAppConfig().getTestScheduleId()+" :Opening the URL count :"+ crawlConfig.getPageCount() + " URL: "+currentLink.getUrl());
			
			//Opening the given page, event will be triggered to capture the screen shot and html file
			try{
				crawlConfig.getDriver().get(currentLink.getUrl());
				currentLink.setPageTile(crawlConfig.getDriver().getTitle());
			}catch (UnreachableBrowserException e){
				logger.error("ERROR :Browser might be closed.");
				currentLink.setPageAccessible(false);
				e.printStackTrace();
				return false;
			}

			//Do robot clicking to capture the analytics data.
			if ( crawlConfig.isRobotEnabled() ) {
				SmartUserV2 smartUserForAutoRobot = new SmartUserV2(crawlConfig.getJqueryFilePath(), "Thread");
				doAutoRobot(crawlConfig.getDriver(), smartUserForAutoRobot);
			}
			
			if ( crawlConfig.getDriver().findElements(By.tagName("form")).size() > 0 ){
				currentLink.setTransactionExists(true);
			}
			
			//Add to navigation list
			crawlConfig.getCrawlStatus().getNavigationList().add(currentLink);
		}
		

		logger.info(crawlConfig.getAppConfig().getTestScheduleId()+" : stopAfterUrlCount :"+crawlConfig.getStopAfterUrlCount() +" navigationList.size() :"+crawlConfig.getCrawlStatus().getNavigationList().size());
		
		return true;
	}
	
	/*
	private void processPage(TestCaseExecution appConfig, WebDriver driver, WebDriverListener eventListener, Link link, String removeUrlPattern, boolean robotEnabled, SmartUser2 smartUserForAutoRobot, int stopAfterUrlCount) throws Exception{
		logger.info(appConfig.getTestScheduleId()+" :Entry: processPage");
		String href = "";
		String navigationPath = "";
		
		try {
			
				
				for (WebElement a : driver.findElements(By.tagName("a"))) {
					href = a.getAttribute("href");
					
					if ( isValidModule(appConfig, href) && isAddressValid( href ) && !alreadyListed.contains(removeFromUrlForCompare(href, removeUrlPattern))) {
						linkCount++;
						alreadyListed.add(removeFromUrlForCompare(href, removeUrlPattern));
						String imageDirectory = getImageDirectory(a);
						
						String imageName = "";
						
						//Create image with link name, if link name is not available use pageLabel or 
						if (null != a.getText() && a.getText().length() > 0) {
							imageName = a.getText() + ".jpeg";
						} else {
							//imageName = imageDirectory + ".jpeg";
							try{
								imageName = getImageName(a) + ".jpeg";
								
								if ( null == imageName || imageName.length() < 1 ){
									imageName = "Unknown.jpeg";
								}
							}catch(Exception e){
								imageName = "Unknown.jpeg";
							}
						}
						navigationPath = link.getNavigationPath() + "->" + imageName.substring(0, imageName.indexOf(".jpeg"));
						//queue.add(new Link(removeSpecialChars(imageName), removePound(imageDirectory), a.getAttribute("href"), false));
						Link currentLink = new Link(removeSpecialChars(imageName, true), FileDirectoryUtil.removePound(removeSpecialChars(imageDirectory, false)), a.getAttribute("href"), navigationPath, true, driver.getTitle(), link.getUrl(), false, appConfig.getTestUrlPattern());
						//logger.info(appConfig.getTestScheduleId()+" :Link :"+currentLink);
						if ( stopAfterUrlCount == 0 || linkCount <= stopAfterUrlCount ){
							stack.push(currentLink);
							
						} else {
							break;
						}
						
						logger.info(appConfig.getTestScheduleId()+" :No of link yet to be processed :"+stack.size());
					}
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("Exception in processPage :"+e);
			e.printStackTrace();
		}

		logger.info("Exit: processPage");
	}
	*/
	
	public CrawlConfig collectNewLinkInCurrentPage(CrawlConfig crawlConfig) throws Exception{
		String href = "";
		String navigationPath = "";
		String linkName = "";
		CrawlStatus crawlStatus = crawlConfig.getCrawlStatus();
		Link currentLink = crawlStatus.getCurrentLink();
		
		for (WebElement a : crawlConfig.getDriver().findElements(By.tagName("a"))) {
			href = a.getAttribute("href");
			
			if ( isValidModule(crawlConfig.getAppConfig(), href) && isAddressValid( crawlConfig, href ) && !crawlStatus.getAlreadyListed().contains(removeFromUrlForCompare(href, crawlConfig.getRemoveUrlPattern()))) {
				//Increase found url count
				crawlStatus.setFoundUrlcount(crawlStatus.getFoundUrlcount()+1);
				crawlStatus.getAlreadyListed().add(removeFromUrlForCompare(href, crawlConfig.getRemoveUrlPattern()));
				String imageDirectory = getImageDirectory(a);
				
				String imageName = "";
				
				//Create image with link name, if link name is not available use pageLabel or 
				if (null != a.getText() && a.getText().length() > 0) {
					imageName = a.getText() + ".jpeg";
				} else {
					//imageName = imageDirectory + ".jpeg";
					try{
						imageName = getImageName(a) + ".jpeg";
						
						if ( null == imageName || imageName.length() < 1 ){
							imageName = "Unknown.jpeg";
						}
					}catch(Exception e){
						imageName = "Unknown.jpeg";
					}
				}
				
				try{
					navigationPath = currentLink.getNavigationPath() + "->" + imageName.substring(0, imageName.indexOf(".jpeg"));
				}catch(Exception e){
					navigationPath = currentLink.getNavigationPath() + "->" + "Unknown";
				}
				//queue.add(new Link(removeSpecialChars(imageName), removePound(imageDirectory), a.getAttribute("href"), false));
				Link newLink = new Link(removeSpecialChars(imageName, true), FileDirectoryUtil.removePound(removeSpecialChars(imageDirectory, false)),
						a.getAttribute("href"), navigationPath, "", true, 
						crawlConfig.getDriver().getTitle(), currentLink.getUrl(), false, new ArrayList<PartialText>(), currentLink.getUrlLevel() + 1);
				//logger.info(appConfig.getTestScheduleId()+" :Link :"+currentLink);
				if ( crawlConfig.getStopAfterUrlCount() == 0 || crawlStatus.getFoundUrlcount() <= crawlConfig.getStopAfterUrlCount() ){
					crawlStatus.getQueue().add(newLink);
				} else {
					break;
				}
				
				logger.info(crawlConfig.getAppConfig().getTestScheduleId()+" :No of link yet to be processed :"+crawlStatus.getQueue().size());
			}
		}
		
		return crawlConfig;
	}

	private boolean  isValidModule(ScheduleDetails appConfig,
			String href) {
		String includeUrlPattern = "";
		
		/*if ( null != appConfig.getApplicationModule() && null != appConfig.getApplicationModule().getIncludeUrlPattern() ){
			includeUrlPattern = appConfig.getApplicationModule().getIncludeUrlPattern();
		}*/
		
		if ( StringUtils.isEmpty(includeUrlPattern) ){
			return true;
		}
		boolean status = false;
		
		if ( StringUtils.isNotEmpty(href) && StringUtils.isNotEmpty(includeUrlPattern) ){
			String[] includeUrl = includeUrlPattern.split(",");
			
			for( String temp: includeUrl) {
				if ( href.contains(temp) ){
					status = true;
				}
			}
		}

		return status;
	}

	private boolean isStopUrlCountReached( CrawlConfig crawlConfig ) {
		if ( crawlConfig.getStopAfterUrlCount() == 0 || crawlConfig.getCrawlStatus().getNavigationList().size() <= crawlConfig.getStopAfterUrlCount() ){
			logger.info("stopAfterUrlCount :"+crawlConfig.getStopAfterUrlCount());
			return false;
		} else {
			logger.info("STOP AFTER URL COUNT :"+crawlConfig.getStopAfterUrlCount() + " REACHED, HENCE STOP PROCESSING NEW URLs.");
		}
		
		return true;
	}

	private void doAutoRobot(WebDriver driver, SmartUserV2 smartUserForAutoRobot) throws Exception{
		logger.info("Entry : doAutoRobot");
		smartUserForAutoRobot.processUrl(driver, "");
		logger.info("Exit : doAutoRobot");
	}

	private String getImageName(WebElement a) throws Exception{
		String tempHref = a.getAttribute("href");
		String imageName = "";
		if ( tempHref.indexOf("?") > 0 ) {
			tempHref = tempHref.substring(0, tempHref.indexOf("?"));
		}
		imageName = tempHref.substring(tempHref.lastIndexOf("/"), tempHref.length());
		imageName = removeSpecialChars(imageName, true);
		return imageName;
	}

	protected boolean isAddressValid(CrawlConfig crawlConfig, String href) throws Exception {

		if (null == href || "".equals(href)) {
			return false;
		}

		if (hasSkipUrl(crawlConfig, href)) {
			return false;
		} else if (hasScanUrl(crawlConfig, href)) {
			return true;
		}
		return false;
	}
	
	private boolean hasSkipUrl(CrawlConfig crawlConfig, String href) throws Exception{
		if (CrawlerUtils.isExcludeUrlPatternFound(href, crawlConfig.getAppConfig().getExcludeUrl())) {
			return true;
		}
		return false;
	}
	
	private boolean hasScanUrl(CrawlConfig crawlConfig, String href) throws Exception{
		
		if (CrawlerUtils.isIncludeUrlPatternFound(href, crawlConfig.getAppConfig().getIncludeUrl())) {
			return true;
		}
		return false;
	}
	
	private String removeFromUrlForCompare(String url, String removeUrlPattern) throws Exception{
		String newUrl = new String(url);
		
		if ( null != removeUrlPattern && !"null".equals(removeUrlPattern) && removeUrlPattern.length() > 0 ){
			String[] tokens = removeUrlPattern.split(",");
	
			for( String token: tokens){
				newUrl = url.replaceAll(token + "[^&]*&", "&");
			}
			
		}
		
		//logger.info("url :"+url);
		if ( newUrl.indexOf("#") > 0 ) {
			newUrl = newUrl.substring(0, newUrl.lastIndexOf("#"));
		}
		
		return newUrl;
	}
	
	private String getImageDirectory(WebElement a) throws Exception{
		String imageName = "";

		//TODO get the directory name from URL
		//imageName = getPageLabel(a.getAttribute("href"));

		if (null == imageName || imageName.length() < 1) {
			try {
				imageName = getJ2eeAppName(a.getAttribute("href"));// Might be j2ee apps link
			}catch(Exception e){
				imageName = "UnknownLink";
			}
			// imageName = removeTags(a.getAttribute("href"));
			// imageName = removeSpecialChars(imageName);
			// return imageName;
			if ( imageName.length() < 1 ){
				imageName = "UnknownLink";
			}
			
		}
		return imageName;
	}

	private String getJ2eeAppName(String href) throws Exception{
		String imageName = "";
		String tempHref = href;
		if (null != tempHref && tempHref.indexOf("/private/") > 0 ) {
			//for jee
			//imageName = href.substring(href.indexOf("/private/") + "/private/".length());
			//imageName = imageName.substring(0, imageName.indexOf("/"));
			//logger.info("tempHref :"+tempHref);
			if ( tempHref.indexOf("?") > 0 ) {
				tempHref = tempHref.substring(0, tempHref.indexOf("?"));
			}
			imageName = tempHref.substring(tempHref.indexOf("/private/")  + "/private/".length(), tempHref.lastIndexOf("/"));
		} else if (null != tempHref && tempHref.indexOf("/public/") > 0 ) {
			if ( tempHref.indexOf("?") > 0 ) {
				tempHref = tempHref.substring(0, tempHref.indexOf("?"));
			}
			imageName = tempHref.substring(tempHref.indexOf("/public/")  + "/public/".length(), tempHref.lastIndexOf("/"));
		} else {
			if ( tempHref.indexOf("?") > 0 ) {
				tempHref = tempHref.substring(0, tempHref.indexOf("?"));
			}
			if ( tempHref.indexOf("//") > 0 ) {
				tempHref = tempHref.substring(tempHref.indexOf("//") + 2);
			}
			
			//logger.info("tempHref :"+tempHref);
			imageName = tempHref.substring(0, tempHref.lastIndexOf("/"));
		}

		return imageName;
	}
	
	private String getPageLabel(String href) {
		String imageName = "";

		if (null == href || href.length() == 0 || href.lastIndexOf("_pageLabel=") < 0) {
			return "";
		}
		imageName = href.substring(href.lastIndexOf("_pageLabel=") + "_pageLabel=".length());

		//Take image name as pageLabel, takePageLabel name until &
		if (imageName.indexOf("&") > 0) {
			imageName = imageName.substring(0, imageName.indexOf("&"));
		} else if ( imageName.indexOf("'") > 0){ //If href is in javascript function there might not be &, after pageLabel name you see function close " ') ", remove those.
			imageName = imageName.substring(0, imageName.indexOf("'"));
		}
		//System.out.println("ImageName in getPageLabel :" + imageName);
		return imageName;
	}

	protected String removeSpecialChars(String imageName, boolean removeDirectory) throws Exception{

		String fileName = new String(imageName);
		try {
			// directory = directory.replaceAll("\\s+", "");
			fileName = fileName.replaceAll(">", "");
			fileName = fileName.replaceAll("<", "");
			fileName = fileName.replaceAll(":", "");
			fileName = fileName.replaceAll("\"", "");
			fileName = fileName.replace("\\", "");
			
			if ( removeDirectory ) {
				fileName = fileName.replaceAll("\\/", "");
			}
			
			fileName = fileName.replaceAll("|", "");
			fileName = fileName.replace("?", "");
			fileName = fileName.replace("*", "");
			fileName = fileName.replace("'", "");
			fileName = fileName.replace("$", "");
			fileName = fileName.replace("%", "");
			
			fileName = fileName.replaceAll(",", "");

		} catch (Exception e) {
			System.out.println("Exception :" + e);
			e.printStackTrace();
		}
		return fileName;

	}
}
