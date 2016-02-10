package com.ensense.insense.core.utils.reports;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipFile;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.cts.mint.analytics.common.HarFileConstants;
import com.cts.mint.analytics.common.WebAnalyticsConstants;
import com.cts.mint.analytics.common.WebAnalyticsUtils;
import com.cts.mint.common.MintFileUtils;
import com.cts.mint.common.ZipFileUtils;
import com.cts.mint.common.model.Link;
import com.cts.mint.common.utils.FileDirectoryUtil;
import com.cts.mint.common.utils.LevenshteinDistanceCalculator;
import com.cts.mint.reports.model.ImageProperty;
import com.cts.mint.reports.model.TextAndImageDetail;
import com.cts.mint.reports.model.TextCompareReport;
import com.cts.mint.reports.model.TextToFind;
import com.cts.mint.util.FileDownloader;
import com.fasterxml.jackson.core.JsonParseException;

import edu.umass.cs.benchlab.har.HarEntries;
import edu.umass.cs.benchlab.har.HarEntry;
import edu.umass.cs.benchlab.har.HarLog;
import edu.umass.cs.benchlab.har.HarPage;
import edu.umass.cs.benchlab.har.HarWarning;
import edu.umass.cs.benchlab.har.tools.HarFileReader;

public class ReportsUtil {
	private static Logger logger = Logger.getLogger(ReportsUtil.class);
	
	public static List<TextCompareReport> generateTextComparisonExcelFile(ArrayList<Link> baseLineLinks,
			ArrayList<Link> currentRunLinks) {
		List<TextCompareReport> textCompareReportList = new ArrayList<TextCompareReport>();
		int totalFileCount = baseLineLinks.size();
		int index = 0;
		for ( Link baseLineLink : baseLineLinks ){
			index++;
			logger.info("Generating text comparison report for the file no :"+index +" of :"+totalFileCount);
			TextCompareReport textCompareReport = new TextCompareReport();
			textCompareReport.setUrlNo(baseLineLink.getUrlNo());
			textCompareReport.setUrl(baseLineLink.getUrl());
			textCompareReport.setUrlfound(false);
			textCompareReport.setPageTitle(baseLineLink.getPageTile());
			textCompareReport.setLinkName(getLinkName(baseLineLink.getNavigationPath()));
			try{
				Link currentLink = getCurrentLink(baseLineLink, currentRunLinks);
				if ( null != currentLink && StringUtils.isNotEmpty(currentLink.getTextFileFullPath()) && StringUtils.isNotEmpty(baseLineLink.getTextFileFullPath()) ){

					textCompareReport.setUrlfound(true);
					
					if( MintFileUtils.isFileExists(baseLineLink.getTextFileFullPath()) && MintFileUtils.isFileExists(currentLink.getTextFileFullPath()) ){
						logger.info("Starting LevenshteinDistanceCalculator");
						int levResults = LevenshteinDistanceCalculator.pecentageOfTextMatch(MintFileUtils.readFromFile(baseLineLink.getTextFileFullPath()), MintFileUtils.readFromFile(currentLink.getTextFileFullPath()));
						textCompareReport.setPercentagematch(levResults);
						logger.info("Ending LevenshteinDistanceCalculator, percentage match :"+levResults);
						
					}
					/*
					if (MintFileUtils.isFileExists(baseLineLink
							.getTextFileFullPath())
							&& MintFileUtils.isFileExists(currentLink
									.getTextFileFullPath())) {
						String baselineFileContent = MintFileUtils
								.readFromFile(baseLineLink
										.getTextFileFullPath());
						String currentRunFileContent = MintFileUtils.readFromFile(currentLink
												.getTextFileFullPath());
												
						LinkedList<Diff> diffs = TextCompareUtils
								.getDifferencesBetweenTextFile(baselineFileContent,
										currentRunFileContent);

						double differenceLength = 0;
						for (Diff aDiff : diffs) {
							String text = aDiff.text;
							switch (aDiff.operation) {
							case INSERT:
								differenceLength += text.length();
								break;
							case DELETE:
								differenceLength += text.length();
								break;
							case EQUAL:

								break;
							}
						}

						double percentageMatch = (1.0 - (differenceLength / (currentRunFileContent.length() + baselineFileContent.length())) * 100;
						textCompareReport.setPercentagematch((int)percentageMatch);
					}*/
				}
			}catch(Exception e){
				logger.error("Exception while calculating Difference percentage, Exception :"+e);
			}
			textCompareReportList.add(textCompareReport);
		}
		return textCompareReportList;
	}
	
	public static String getLinkName(String navigationPath) {
		String linkName = "";
		
		if ( null != navigationPath && navigationPath.lastIndexOf("->") > 0 ){
			try{
				linkName = navigationPath.substring(navigationPath.lastIndexOf("->") + 2, navigationPath.length());
				
				if ( null == linkName ){
					linkName = "";
				}
			}catch(Exception e){
				linkName = "";
			}
		}
		return linkName;
	}

	public static Link getCurrentLink(Link baseLineLink,
			ArrayList<Link> currentRunLinks) {
		Link currentLink = null;

		for( Link link : currentRunLinks){
			if ( baseLineLink.getUrl().equals(link.getUrl())){
				currentLink = link;
			}
		}
		return currentLink;
	}
	
	public static Map<String, TextAndImageDetail> collectImages(Map<String, TextAndImageDetail> textAndImageDetailMap, List<String> imageFileTypes, List<String> imageFilesToSkip,
			String harLogsDir, List<String> tiaaImagesFileList, String imageDirectory) {

		InputStream inputStream = null;
		ZipFile zipFile = null;
		HarFileReader harFileReader = null;

		Map<String, String>downloadedImageMap = new HashMap<String, String>();

		WebAnalyticsUtils wau = new WebAnalyticsUtils();
		try {
			
			File textAndImageDirectoryDirectoryPath = new File(imageDirectory);
			
	    	if(! textAndImageDirectoryDirectoryPath.exists()){
	    		FileDirectoryUtil.createDirectories(imageDirectory);
	    		
	    	}
	    	
			List<String> harLogFiles = wau.listFilesForFolder(new File(harLogsDir));
			int totalFile = harLogFiles.size();
			logger.info("Total files to process :" + totalFile);

			int harFileCount = 0;
			for (String filePath : harLogFiles) {
				harFileCount++;
				logger.info("Processing the Har file :" + harFileCount  + " of "+ totalFile);

				File f = new File(filePath);

				
				// HarFileReader
				harFileReader = new HarFileReader();

				String refererUrl = WebAnalyticsConstants.noDataFound;

				Set<ImageProperty> allImagesOfPageset = new HashSet<ImageProperty>();
				Set<ImageProperty> tiaaImagesOfPageset = new HashSet<ImageProperty>();
				
				TextAndImageDetail textAndImageDetail = new TextAndImageDetail();
				
				String allImagesDirectory = imageDirectory + File.separator + "AllImages";
				String tiaaImagesDirectory = imageDirectory + File.separator + "TiaaImages";

				FileDirectoryUtil.createDirectories(allImagesDirectory);
				FileDirectoryUtil.createDirectories(tiaaImagesDirectory);
				
				try {
					// All violations of the specification generate warnings
					List<HarWarning> warnings = new ArrayList<HarWarning>();

					HarLog harLog = null;// r.readHarFile(f, warnings);
					if (filePath.endsWith(HarFileConstants.harFileExtension)) {
						harLog = harFileReader.readHarFile(f, warnings);
					} else if (filePath
							.endsWith(HarFileConstants.harZipFileExtension)) {
						zipFile = new ZipFile(filePath);
						inputStream = ZipFileUtils.readFromHarZipFile(zipFile);
						harLog = harFileReader.readHarFile(inputStream,
								warnings);
					}

					if ( null == harLog ){
						continue;
					}
					
					HarEntries entries = harLog.getEntries();

					// Used for loops
					List<HarPage> pages = harLog.getPages().getPages();
					List<HarEntry> hentry = entries.getEntries();


					Map<String, String> refererUrlMap = wau.getRefererUrlPerPage(
							hentry, pages, true);
					
					int index = -1;
					for (HarEntry entry : hentry) {
						index++;

						//Download images
						allImagesOfPageset = checkForUniqueImageUrlAndAddIt(allImagesOfPageset, imageFileTypes, hentry.get(index));
						
						for (HarPage page : pages) {
							refererUrl = refererUrlMap
									.get(page.getId());
						}
					}

					logger.info("allImagesOfPageset :"+allImagesOfPageset);
					textAndImageDetail = textAndImageDetailMap.get(refererUrl);
					
					logger.info("textAndImageDetail :"+textAndImageDetail);
					logger.info("refererUrl :"+refererUrl);
					
					if ( null == textAndImageDetail ){
						continue;
					}
					
					tiaaImagesOfPageset = getTiaaImageSet(allImagesOfPageset, tiaaImagesFileList);
					
					int allImageCount = downloadImagesAndGetCount(allImagesOfPageset, imageFilesToSkip, allImagesDirectory, downloadedImageMap, textAndImageDetail);
					textAndImageDetail.setAllImageCount(allImageCount);
					textAndImageDetail.setAllImageUrls(allImagesOfPageset);
					
					String newImagePath = tiaaImagesDirectory + File.separator + textAndImageDetail.getUrlNo();
					FileDirectoryUtil.createDirectories(newImagePath);
					
					int tiaaImageCount = downloadImagesAndGetCount(tiaaImagesOfPageset, imageFilesToSkip, newImagePath, downloadedImageMap, textAndImageDetail);
					textAndImageDetail.setTiaaImageCount(tiaaImageCount);
					textAndImageDetail.setTiaaImageUrls(tiaaImagesOfPageset);
					
				
				} catch (JsonParseException e) {
					e.printStackTrace();
					// fail("Parsing error during test");
				} catch (IOException e) {
					e.printStackTrace();
					// fail("IO exception during test");
				}
				// added by Arun for branding report changes for adding tiaa-cref logo count and url
				/*
				brandingReportData.setBaseLineTiaaCrefLogoCount(tiaaImagePropertyList.size());
				brandingReportData.setBaseLineTiaaCrefLogoUrls(tiaaImagePropertyList.toString());
				
				brandingReportData.setBaseLineTiaaLogoCount(imagePropertyList.size());
				brandingReportData.setBaseLineTiaaLogoUrls(imagePropertyList.toString());
				brandingReportData.setBaselineImageDetails(getImageDetails(imagePropertyList));
				brandingReportData.setBaseLineTiaaCrefUrlCount(tiaaUrlList.size());
				brandingReportData.setBaseLineTiaaCrefUrls(tiaaUrlList.toString());
				brandingReportData.setBaseLineTiaaCrefStringInContentCount(contentCount);

				*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				// System.out.println("closing streams");
				if (inputStream != null) {
					inputStream.close();
					inputStream = null;
				}

				if (zipFile != null) {
					zipFile.close();
					zipFile = null;
				}

				if (harFileReader != null) {
					harFileReader = null;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return textAndImageDetailMap;
	}

	public static Map<String, TextAndImageDetail> generateTextFoundCountMap(
			List<String> textToFindList, List<Link> navigationDetails) {
		Map<String, TextAndImageDetail> textAndImageDetailMap = new LinkedHashMap<String, TextAndImageDetail>();
		
		for ( Link link : navigationDetails){

			try{
				if ( MintFileUtils.isFileExists(link.getTextFileFullPath()) ){
					TextAndImageDetail textAndImageDetail = new TextAndImageDetail();
					
					textAndImageDetail.setUrl(link.getUrl());
					textAndImageDetail.setUrlNo(link.getUrlNo());
					textAndImageDetail.setTextFilePath(link.getTextFileFullPath());
					textAndImageDetail.setPageTitle(link.getPageTile());
					textAndImageDetail.setParentUrl(link.getParentUrl());
					textAndImageDetail.setNavigationPath(link.getNavigationPath());
					textAndImageDetail.setLinkName(link.getLinkName());
					if ( link.isErrorPage() ){
						textAndImageDetail.setErrorpage("Yes");
					}else{
						textAndImageDetail.setErrorpage("No");
					}
					String fileContent = MintFileUtils.readFromFile(link.getTextFileFullPath());
					
					if ( null != fileContent ){
						fileContent = fileContent.toLowerCase();
						
						for( String textStringToFind : textToFindList ){
							int count = StringUtils.countMatches(fileContent, textStringToFind.toLowerCase());
							TextToFind textToFind = new TextToFind();
							textToFind.setText(textStringToFind);
							textToFind.setFindCount(count);
							
							textAndImageDetail.getTextToFindList().add(textToFind);
						}
					}
					textAndImageDetailMap.put(link.getUrl(), textAndImageDetail);
				}
			}catch(Exception e){
				logger.error("Exception while getting Text count");
				logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			}finally{
				
			}
		}
		return textAndImageDetailMap;
	}
	
	private static Set<ImageProperty> checkForUniqueImageUrlAndAddIt(Set<ImageProperty>allImagesOfPageset, List<String> imageFileTypes,
			HarEntry entry) {
		if ( CollectionUtils.isNotEmpty(imageFileTypes) && 
				StringUtils.isNotEmpty(FilenameUtils.getExtension(entry.getRequest().getUrl())) && 
				imageFileTypes.contains(FilenameUtils.getExtension(entry.getRequest().getUrl()).toLowerCase())){

			ImageProperty imageProperty = new ImageProperty();
			imageProperty.setImageName(FilenameUtils.getBaseName((entry.getRequest().getUrl()))+"."+FilenameUtils.getExtension(entry.getRequest().getUrl()));
			imageProperty.setSize(entry.getResponse().getContent().getSize());
			imageProperty.setImageUrl(entry.getRequest().getUrl());
			
			allImagesOfPageset.add(imageProperty);
		}
		return allImagesOfPageset;
	}
	
	private static Set<ImageProperty> getTiaaImageSet(
			Set<ImageProperty> allImagesOfPageset,
			List<String> tiaaImagesFileList) {
		Set<ImageProperty> tiaaImagesOfPageset = new HashSet<ImageProperty>();
		
		for ( ImageProperty imageProperty : allImagesOfPageset ){
			try {
				String imageName = MintFileUtils.getFileNameInUrl(imageProperty.getImageUrl());
				if( tiaaImagesFileList.contains(imageName.toLowerCase())){
					tiaaImagesOfPageset.add(imageProperty);
				}
			} catch (Exception e) {
				logger.error("Exception while finding TIAA image :"+imageProperty.getImageUrl());
			}
		}
		return tiaaImagesOfPageset;
	}
	
	private static int downloadImagesAndGetCount(Set<ImageProperty> imagesOfPageset,
			List<String> imageFilesToSkip, String imageDirectory,
			Map<String, String> downloadedImageMap, TextAndImageDetail textAndImageDetail) {
		FileDownloader fd = new FileDownloader();
		int downloadedImageCount = 0;
		for (ImageProperty imageProperty : imagesOfPageset) {
			try {
				if (!isSkipImage(imageProperty.getImageName(), imageFilesToSkip)) {
					String newImagePath = imageDirectory + File.separator + MintFileUtils.getFileNameInUrl(imageProperty.getImageUrl());
					
					if ( isImageAlreadyDownloaded(imageProperty.getImageUrl(), downloadedImageMap) ){
						String alreadyDownloadedpath = downloadedImageMap.get(imageProperty.getImageUrl());
						
						//copy file
						try{
							if ( ! MintFileUtils.isFileExists(newImagePath)){
								FileUtils.copyFile(new File(alreadyDownloadedpath), new File(newImagePath));
							}else{
								logger.info("File already exists :"+newImagePath);
							}
						}catch(Exception e){
							logger.error("Exception while copying the image :"
									+ imageProperty.getImageUrl());
							logger.info("alreadyDownloadedpath :"+alreadyDownloadedpath);
							logger.info("newImagePath :"+newImagePath);
							logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
						}
						
					}else{
						fd.imageDownloader(
								imageProperty.getImageUrl(),
								newImagePath);
						downloadedImageMap.put(imageProperty.getImageUrl(), newImagePath);
					}
					downloadedImageCount++;
				}
			} catch (Exception e) {
				logger.error("Exception while downloading the image :"
						+ imageProperty.getImageUrl());
			}
		}
		return downloadedImageCount;
	}
	
	private static boolean isSkipImage(String imageName, List<String> imageFilesToSkip) {
		try{
			if ( StringUtils.isNotEmpty(imageName) && CollectionUtils.isNotEmpty(imageFilesToSkip)){
				return imageFilesToSkip.contains(imageName.toLowerCase());
			}
		}catch(Exception e){
			
		}
		return false;
	}
	
	private static boolean isImageAlreadyDownloaded(String imageName,
			Map<String, String> downloadedImageMap) {
		if ( StringUtils.isNotEmpty(imageName) ){
			if( downloadedImageMap.get(imageName) != null ){
				return true;
			}
		}
		return false;
	}
}
