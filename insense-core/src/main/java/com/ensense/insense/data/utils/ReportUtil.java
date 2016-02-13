package com.ensense.insense.data.utils;

import com.ensense.insense.data.common.model.CrawlConfig;
import com.ensense.insense.data.common.model.Link;
import com.ensense.insense.data.common.utils.Constants;
import com.ensense.insense.data.common.utils.ZipFileUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReportUtil {
	private static Logger logger = Logger.getLogger(ReportUtil.class);
	public static boolean normalizeTextImageForBaseline(String currentRunPath, String baseLinePath, List<Link> baseLineNavigationList, List<Link> currentNavigationList, String imageTextDirectory) {
		boolean normalized = false;
		try {
			
			
			String normalizedImageTextdirectoryPath = currentRunPath + "\\NormalizedImageTextdirectory";
			File normalizedBaselineDir = new File(normalizedImageTextdirectoryPath+"\\baseline");
			File normalizedImageTextdirectory = new File(normalizedImageTextdirectoryPath+"\\currentRun");
			normalizedBaselineDir.mkdirs();
			normalizedImageTextdirectory.mkdirs();
			File baseLineDirectory = new File(baseLinePath + File.separator + imageTextDirectory);
			FileUtils.copyDirectory(baseLineDirectory, normalizedBaselineDir);
			List<Pair> matchingFolderList = getMatchingPair(baseLineNavigationList, currentNavigationList, true);
			for(Pair pair : matchingFolderList) {
				String currentRunFolder = currentRunPath + File.separator + imageTextDirectory + File.separator + pair.getFirst();
				String currentRunNormalizedFolder = normalizedImageTextdirectory + File.separator + pair.getSecond();
				File currentDirectory = new File(currentRunFolder);
				File currentNormalizedDirectory = new File(currentRunNormalizedFolder);
				currentNormalizedDirectory.mkdirs();
				
				try {
					FileUtils.copyDirectory(currentDirectory, currentNormalizedDirectory);
				} catch (Exception e) {
					logger.error("Exception while copying folder "+currentDirectory);
				}
			}
			/*File ZipFileFolder = new File(currentRunPath + BrokenReportSelection.ALL_IMAGE_DIR);
			if(!ZipFileFolder.exists()) {
				ZipFileFolder.mkdirs();
			}*/
			ZipFileUtils.zipFolder(normalizedImageTextdirectoryPath, normalizedImageTextdirectoryPath + Constants.FILE.ZIP);
			normalized = true;
		} catch (Exception e) {
           e.printStackTrace();
			logger.error("Exception while normalizing textImage");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		return normalized;
	}
	
	public static List<Pair> getMatchingPair(List<Link> baseLineNavigationList, List<Link> currentNavigationList, boolean urlFlag) {
		List<Pair> matchingFolderList = new ArrayList<Pair>();
		Pair pair = null;
			for(Link bLink : baseLineNavigationList) {
				for(Link cLink : currentNavigationList) {
					URL urlTemp = null;
					String baselineUrl = "";
					String currentRunUrl = "";
					try {
						urlTemp = new URL(bLink.getUrl());
						baselineUrl = urlTemp.getFile();
						
						urlTemp = new URL(cLink.getUrl());
						currentRunUrl = urlTemp.getFile();
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					if(StringUtils.equals(baselineUrl, currentRunUrl)) {
						pair = new Pair(); 
						
						if( urlFlag) {
							pair.setFirst(bLink.getUrlNo());
							pair.setSecond(cLink.getUrlNo());
						}	else {
							pair.setFirst(bLink);
							pair.setSecond(cLink);
						}
						
						matchingFolderList.add(pair);
						
						break;
					}
				}
			}
		return matchingFolderList;
	}
	
	public static List<Link> getNavigationList(String crawlConfigPath) throws Exception{
		 List<Link> navigationDetails = new ArrayList<Link>();
	    CrawlConfig crawlConfig = null;
		JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();
       crawlConfig = jsonReaderWriter.readJsonObjectFromFile(new CrawlConfig(), crawlConfigPath);
       navigationDetails = crawlConfig.getCrawlStatus().getNavigationList();
       return navigationDetails;
	}
}
