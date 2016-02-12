package com.ensense.insense.services.common.utils;

import com.ensense.insense.services.common.model.FileDownloader;
import com.ensense.insense.data.uitesting.model.TextImageReportData;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.File;
import java.util.*;

public class TextImageCaptureUtil {
	private static final Logger logger = Logger.getLogger(TextImageCaptureUtil.class);
	
	public static TextImageReportData downloadAllImagesAndGetDetails(WebDriver driver, TextImageReportData brandingReportData, String imageDirectory, String tiaaImageDirectory, String threadNumber, List<String>tiaaImageNameList) {
		List<WebElement> imageElements = driver.findElements(By.tagName("img"));
		int imageCount = 0;
		String imageUrls = "";

		Set<String> imageSet = new HashSet<String>();
		List<String> allImageList = new ArrayList<String>();
		List<String> tiaaImageList = new ArrayList<String>();
		for ( WebElement element: imageElements){
			String imageSrc = element.getAttribute("src");
			allImageList.add(imageSrc);
			imageSet.add(imageSrc);

		}
		imageElements = driver.findElements(ByXPath.xpath("//li[contains(@style, 'background-image:')]"));
		
		for ( WebElement element: imageElements){
			//String imageSrc = element.getAttribute("src");
			String imageSrc = element.getCssValue("background-image");
			imageSrc = imageSrc.toLowerCase().replace("url", "");
			imageSrc = imageSrc.replace("(", "");
			imageSrc = imageSrc.replace(")", "");
			imageSrc = imageSrc.replaceAll("\"", "");
			imageSet.add(imageSrc);
			allImageList.add(imageSrc);
		}
		
		brandingReportData.setAllImageCount(imageSet.size());
		
		FileDownloader fd = new FileDownloader(driver);
		int tiaaImageCount = 0;
		try {
			Iterator iterator = imageSet.iterator(); 
			while (iterator.hasNext()){
				String imageSrc = (String) iterator.next();
				
				//logger.info("Downloading the Image :"+imageSrc);
				//logger.info("Image Directory path :"+imageDirectory + File.separator + MintFileUtils.getFileNameInUrl(imageSrc));
				fd.imageDownloader(imageSrc,
						imageDirectory + File.separator + MintFileUtils.getFileNameInUrl(imageSrc));
				imageUrls =  imageUrls + imageSrc + ",";
				
				if ( isTiaaImage(MintFileUtils.getFileNameInUrl(imageSrc), tiaaImageNameList)){
					try{
						tiaaImageList.add(imageSrc);
						fd.imageDownloader(imageSrc,
							tiaaImageDirectory + File.separator + MintFileUtils.getFileNameInUrl(imageSrc));
						tiaaImageList.add(imageSrc);
						tiaaImageCount++;
					}catch(Exception e){
						logger.error("Exception while downloading the image :"+imageSrc);
					}
				}
			}
			brandingReportData.setTiaaImageCount(tiaaImageCount);
		} catch (Exception e) {
			logger.error("Exception while downloading the image.");
		}
		if ( imageUrls.length() > 1 ){
			imageUrls = imageUrls.substring(0, imageUrls.length()-1);
		}
		brandingReportData.setAllImageList(allImageList);
		brandingReportData.setTiaaImageList(tiaaImageList);
		return brandingReportData;
	}

	private static boolean isTiaaImage(String fileName, List<String>tiaaImageNameList) {
		if ( tiaaImageNameList.contains(fileName.toLowerCase())){
			return true;
		}
		return false;
	}
	
	public static int getStringCount(String htmlSource, String stringToMatch) {
		int matchFound = 0;
		try{
			matchFound = StringUtils.countMatches(StringUtils.lowerCase(htmlSource), StringUtils.lowerCase(stringToMatch));
		}catch(Exception e){
			matchFound = 0;
		}
		
		return matchFound;
	}
}
