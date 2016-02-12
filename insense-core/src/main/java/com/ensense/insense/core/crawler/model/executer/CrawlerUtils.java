package com.ensense.insense.core.crawler.model.executer;

import com.ensense.insense.core.utils.Constants;
import com.ensense.insense.data.common.model.CrawlStatus;
import com.ensense.insense.data.common.model.ExecutionStatus;
import com.ensense.insense.data.common.model.Link;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CrawlerUtils {
	private static final Logger logger = Logger
			.getLogger(CrawlerUtils.class);

	public static boolean isExcludeUrlPatternFound(String url, List<String> excludeUrlList){
		boolean found = false;
		
		try{
			if ( null == excludeUrlList || excludeUrlList.size() < 1 ){
				return found;
			}
			
			for ( String excludeUrlPattern : excludeUrlList ){
				
				if ( StringUtils.isNotEmpty(url) 
						&& StringUtils.isNotEmpty(excludeUrlPattern) 
						&& url.toLowerCase().contains(excludeUrlPattern.trim().toLowerCase())){
					found = true;
					return found;
				}
			}
		}catch(Exception e){
			
		}
		return found;
	}
	
	public static boolean isIncludeUrlPatternFound(String url, List<String> includeUrlList){
		boolean found = false;
		try{
			if ( null == includeUrlList || includeUrlList.size() < 1 ){
				return found;
			}
			
			for ( String includeUrlPattern : includeUrlList ){
				if ( StringUtils.isNotEmpty(url) 
						&& StringUtils.isNotEmpty(includeUrlPattern) ) {
					String tempUrl = url;
					if (  tempUrl.indexOf('?') > 0 ){
						tempUrl = tempUrl.substring(0, tempUrl.indexOf('?')); 
					}
						
					if ( tempUrl.toLowerCase().contains(includeUrlPattern.trim().toLowerCase()) ){
						found = true;
						return found;
					}
				}
			}
		}catch(Exception e){
			found = false;
		}
		return found;
	}
	
	public static String getHiddenLinkText(WebDriver webDriver, WebElement element) {
		String hiddenLinkText = "";
		try{
			hiddenLinkText =  (String) ((JavascriptExecutor) webDriver).executeScript(
		        "return jQuery(arguments[0]).text();", element);
			hiddenLinkText = hiddenLinkText.trim();
		}catch(Exception e){
			logger.error("Exception while getting linkText from hidden link");
		}
		return hiddenLinkText;
	}
	
	public static String getImageName(String href) throws Exception {
		String tempHref = href;
		String imageName = "";
		try{
			if (tempHref.indexOf("?") > 0) {
				tempHref = tempHref.substring(0, tempHref.indexOf("?"));
			}
	
			imageName = tempHref.substring(tempHref.lastIndexOf("/"),
					tempHref.length());
			imageName = removeSpecialChars(imageName, true);
			
			if (imageName.indexOf(".") > 0) {
				imageName = imageName.substring(0, imageName.indexOf("."));
			}
			if ( null == imageName || imageName.trim().length() < 1 ){
				imageName = "Unknown";
			}
		}catch(Exception e){
			logger.error("Exception while getting the image name");
		}
		return imageName;
	}
	
	public static String removeSpecialChars(String imageName,
			boolean removeDirectory) throws Exception {

		String fileName = new String(imageName);
		try {
			// directory = directory.replaceAll("\\s+", "");
			fileName = fileName.replaceAll("\\s","");
			fileName = fileName.replaceAll(">", "");
			fileName = fileName.replaceAll("<", "");
			fileName = fileName.replaceAll(":", "");
			fileName = fileName.replaceAll("\"", "");
			fileName = fileName.replace("\\", "");

			if (removeDirectory) {
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
			logger.error("Exception while removing special characters.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		fileName = fileName.replaceAll("[^a-zA-Z0-9.-]", "_");
		return fileName;
	}
	
	public static String getImageDirectory(String href) throws Exception {
		String imageName = "";

		// TODO get the directory name from URL
		// imageName = getPageLabel(a.getAttribute("href"));

		if (null == imageName || imageName.length() < 1) {
			try {
				imageName = getJ2eeAppName(href);// Might be
																	// j2ee apps
																	// link
				imageName = imageName.replaceAll("[^a-zA-Z0-9.-]", "_");
			} catch (Exception e) {
				imageName = "UnknownLink";
			}
			// imageName = removeTags(a.getAttribute("href"));
			// imageName = removeSpecialChars(imageName);
			// return imageName;
			if (imageName.length() < 1) {
				imageName = "UnknownLink";
			}

		}
		
		return imageName;
	}

	public static String getJ2eeAppName(String href) {
		String imageName = "";
		String tempHref = href;
		
		try{
			if (null != tempHref && tempHref.indexOf("/private/") > 0) {
				// for jee
				// imageName = href.substring(href.indexOf("/private/") +
				// "/private/".length());
				// imageName = imageName.substring(0, imageName.indexOf("/"));
				// logger.info("tempHref :"+tempHref);
				if (tempHref.indexOf("?") > 0) {
					tempHref = tempHref.substring(0, tempHref.indexOf("?"));
				}
				imageName = tempHref.substring(tempHref.indexOf("/private/")
						+ "/private/".length(), tempHref.lastIndexOf("/"));
			} else if (null != tempHref && tempHref.indexOf("/public/") > 0) {
				if (tempHref.indexOf("?") > 0) {
					tempHref = tempHref.substring(0, tempHref.indexOf("?"));
				}
				
				int index = tempHref.indexOf("/public/");
				index = index + "/public/".length();
				
				if ( index < tempHref.lastIndexOf("/") ){
					imageName = tempHref.substring(index, tempHref.lastIndexOf("/"));
				}else{
					imageName = tempHref.substring(index, tempHref.length());
				}
			} else {
				if (tempHref.indexOf("?") > 0) {
					tempHref = tempHref.substring(0, tempHref.indexOf("?"));
				}
				if (tempHref.indexOf("//") > 0) {
					tempHref = tempHref.substring(tempHref.indexOf("//") + 2);
				}
	
				// logger.info("tempHref :"+tempHref);
				imageName = tempHref.substring(0, tempHref.lastIndexOf("/"));
			}
		}catch(Exception e){
			imageName = "Unknown";
		}
		imageName = imageName.replaceAll("[^a-zA-Z0-9.-]", "_");
		return imageName;
	}

	public static boolean isUrlReachMaxTestAttempts(Link newAddress) {
		if ( null != newAddress && newAddress.getTimesTested() >= Constants.MAX_NO_OF_TIME_PAGE_TESTED ){
			return true;
		}
		return false;
	}

	public static boolean isProcessNeedToBePausedOrStopped(
			CrawlStatus crawlStatus) {
		
		try {
			if ( crawlStatus.getRunStatus() == ExecutionStatus.PAUSED.getStatusCode() || crawlStatus.getRunStatus() == ExecutionStatus.STOPPED.getStatusCode() ){
				logger.info("STOP/Pause");
				return true;
			}
		}catch(Exception e){
			
		}
		return false;
	}

	public static boolean isValidUrlLevel(Link newAddress, int urlLevel) {
		if ( urlLevel != 0  && newAddress.getUrlLevel() >= urlLevel ){
			return false;
		}
		return true;
	}
}
