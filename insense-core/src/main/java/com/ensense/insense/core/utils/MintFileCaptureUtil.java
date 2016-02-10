package com.ensense.insense.core.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.cts.mint.crawler.model.UrlFormElement;
import com.cts.mint.htmlParser.HtmlParserUtil;
import com.cts.mint.uitesting.entity.HtmlReportsConfig;
import com.cts.mint.uitesting.model.ScheduleDetails;
import com.cts.mint.util.FileDirectoryUtil;
import com.cts.mint.util.ImageCreationUtil;

public class MintFileCaptureUtil {
	private static final Logger logger = Logger
			.getLogger(MintFileCaptureUtil.class);
	
	public static synchronized String writeHtmlSourceToFile(String imageDirectory, String imageName, String htmlSource){
		boolean status = true;

		status = FileDirectoryUtil.createDirectories(FileDirectoryUtil.removePound(imageDirectory));
		
		try{
			if ( status ){
				imageName = getImageFileName(imageName, imageDirectory);
			}
		}catch(Exception e){
			status = false;
		}
		
		imageDirectory = imageDirectory.replace("/", "\\");
		
		String htmlFileName = imageDirectory + File.separator + imageName.replace(".jpeg", ".html");

		try{
			MintFileUtils.writeHtmlContentToFile(htmlSource, htmlFileName, false);
		}catch(Exception e){
			logger.error("Exception while writing HTML file "+htmlFileName);
		}
		
		return htmlFileName;
	}
	
	public static synchronized String captureHtmlContentAsText(String imageDirectory, String imageName, String htmlSource, String url, int urlNo, ScheduleDetails appConfig) throws Exception{
		imageName = getImageFileName(imageName, imageDirectory);
		imageDirectory = imageDirectory.replace("/", "\\");
		logger.info("Html Tag removal prcoss started  :");
		String htmlTextFilePath = imageName.replace(".jpeg", ".txt");
		//htmlTextFilePath = imageDirectory + File.separator + urlNo + "_" + htmlTextFilePath;
		//removed url number from textFile
		htmlTextFilePath = imageDirectory + File.separator + htmlTextFilePath;

		try{
			String htmlTextContent = "";
			String htmlModifiedContent = htmlSource;
			try{
				/*
				htmlTextContent = HtmlParserUtil.getUnwantedTagRemovedHtmlSource("div.lastlogin", htmlSource);
				htmlTextContent = HtmlParserUtil.getUnwantedTagRemovedHtmlSource("div.copyright", htmlTextContent);
				htmlTextContent = HtmlParserUtil.getUnwantedTagRemovedHtmlSource("div.disclaimer", htmlTextContent);
				htmlTextContent = HtmlParserUtil.getUnwantedTagRemovedHtmlSource("div.logo", htmlTextContent);
				htmlTextContent = HtmlParserUtil.getUnwantedTagRemovedHtmlSource("div.footer-legal", htmlTextContent);
				htmlTextContent = htmlTextContent.replaceAll("tiaa-cref.org", "");
				htmlTextContent = htmlTextContent.replaceAll("TIAA-CREF Trust Company", "");
				*/
				if(appConfig != null && appConfig.getHtmlReportsConfigList() != null && appConfig.getHtmlReportsConfigList().size() > 0) {
					for(HtmlReportsConfig html : appConfig.getHtmlReportsConfigList()) {
						if(html.getRemoveTag()) {
							logger.info("Tag To be Removed :"+html.getRemoveTagName());
							htmlModifiedContent = HtmlParserUtil.getUnwantedTagRemovedHtmlSource(html.getRemoveTagName(), htmlModifiedContent);
						}
					}
					htmlTextContent = HtmlParserUtil.getHtmlContentWithoutHtmlTags(htmlModifiedContent);
					
					for(HtmlReportsConfig html : appConfig.getHtmlReportsConfigList()) {
						if( null != html.getRemoveText() && html.getRemoveText()) {
							logger.info("Text To be Removed :"+html.getRemoveText());
							String needToBeRemovedText[] = html.getRemoveTextContent().split(",");
							for(String removeText : needToBeRemovedText) {
								htmlTextContent = htmlTextContent.replaceAll("\\b(?i)"+removeText+"\\b", "");
							}
						}
					}
				}else {
					htmlTextContent = HtmlParserUtil.getHtmlContentWithoutHtmlTags(htmlSource);
				}
			}catch(Exception e){
				logger.error("Exception while removing Tag/Text "+ExceptionUtils.getStackTrace(e));
			}
			
			
			if (StringUtils.isNotEmpty(htmlTextContent)){
				MintFileUtils.writeHtmlContentToFile(htmlTextContent, htmlTextFilePath, true);
			}
		}catch(Exception e){
			logger.error("Exception while writing HTML as text file :"+htmlTextFilePath);
			//logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw e;
		}
		
		return htmlTextFilePath;
	}
	
	public static synchronized String captureScreenShot(String imageDirectory, String imageName, WebDriver webDriver) throws Exception{
		String imageFullPath = "";
		try{
			FileDirectoryUtil.createDirectories(FileDirectoryUtil.removePound(imageDirectory));
	
			//Make sure image name is unique, not to override the existing one. 
			imageName = getImageFileName(imageName, imageDirectory);
	
			File screenshotCompleted = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
			imageDirectory = imageDirectory.replace("/", "\\");
			imageFullPath = imageDirectory + File.separator + imageName;
	
			ImageCreationUtil.convertPngtoJpeg(screenshotCompleted, imageFullPath);
		}catch(Exception e){
			logger.error("Exception while writing the Image file :"+imageFullPath);
		}
		
		return imageFullPath;
	}
	
	public static synchronized String captureScreenShot(String imageFullPath, WebDriver webDriver) throws Exception{
		try{
			FileDirectoryUtil.createDirectories(FileDirectoryUtil.removePound(imageFullPath));
			File screenshotCompleted = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
	
			ImageCreationUtil.convertPngtoJpeg(screenshotCompleted, imageFullPath);
		}catch(Exception e){
			logger.error("Exception while writing the Image file :"+imageFullPath);
		}
		
		return imageFullPath;
	}
	
	private static String getImageFileName(String imageName, String imageDirectory) throws Exception{
		File dir = new File(imageDirectory);
		String[] files = dir.list();

		if ((files == null) || (files.length == 0)) {
			return imageName;
		}
		
		List<String> filesList = Arrays.asList(files);
		
		String latestfile = getLatestFileName(filesList, imageDirectory, imageName);

		return latestfile;
	}
	
	private static String getLatestFileName(List<String> filesList, String imageDirectory, String fileName) {
		int newFileNumber = 0;
		String newFileName = fileName;
		try{
			if (filesList.size() > 0) {
				
				while (true) {
					if (filesList.contains(newFileName)) {
						newFileNumber++;
						newFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "_" + newFileNumber + ".jpeg";
					} else {
						return newFileName;
					}
				}
			}
		}catch(Exception e){
			logger.error("Exception while getLatestFileName, fileName :"+fileName);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}

		return newFileName;
	}

	public static UrlFormElement captureFormElements(String url, WebDriver driver, String pageSource, String path)  {
		UrlFormElement urlFormElement = new UrlFormElement();
		urlFormElement.setUrl(url);
		urlFormElement.setUrlForms(HtmlParserUtil.captureInputElements(driver, pageSource, url, path));
		return urlFormElement;
	}
}
