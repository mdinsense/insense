package com.ensense.insense.core.analytics.utils;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import com.cts.mint.common.MintFileUtils;
import com.cts.mint.common.ZipFileUtils;
import com.cts.mint.htmlParser.HtmlParserUtil;

import edu.umass.cs.benchlab.har.HarEntries;
import edu.umass.cs.benchlab.har.HarEntry;
import edu.umass.cs.benchlab.har.HarLog;
import edu.umass.cs.benchlab.har.HarPage;
import edu.umass.cs.benchlab.har.HarWarning;
import edu.umass.cs.benchlab.har.tools.HarFileReader;

public class HarReportsUtils {
	private static final Logger logger = Logger
			.getLogger(HarReportsUtils.class);
	
	public static boolean findHarFileForUrl(String url, String htmlTextFilePath, List<String> harLogFiles, String harMappedFileDirectory){
		boolean found = false;
		if ( null == url || url.length() < 1 ){
			return found;
		}
		
		HarFileReader harFileReader = null;
		InputStream inputStream = null;
		ZipFile zipFile = null;
		WebAnalyticsUtils wau = new WebAnalyticsUtils();
		boolean htmlTextContentFound = false;
		
		try{

			for (String filePath : harLogFiles) {
				File f = new File(filePath);
				
				// HarFileReader
				harFileReader = new HarFileReader();
				String refererUrl = WebAnalyticsConstants.noDataFound;
				
				List<HarWarning> warnings = new ArrayList<HarWarning>();
	
				HarLog harLog = null;// r.readHarFile(f, warnings);
				
				try {
					if (filePath.endsWith(HarFileConstants.harFileExtension)) {
						harLog = harFileReader.readHarFile(f, warnings);
					} else if (filePath
							.endsWith(HarFileConstants.harZipFileExtension)) {
						zipFile = new ZipFile(filePath);
						inputStream = ZipFileUtils.readFromHarZipFile(zipFile);
						harLog = harFileReader.readHarFile(inputStream,
								warnings);
					}
				}catch(Exception e){
					logger.error("Exception while reading the Har file.");
				}
	
				if ( null == harLog ){
					continue;
				}
				
				HarEntries entries = harLog.getEntries();
	
				// Used for loops
				List<HarPage> pages = harLog.getPages().getPages();
				List<HarEntry> hentry = entries.getEntries();
				
				Map<String, String> refererUrlMap = wau.getRefererUrlPerPage(hentry, pages, true);
				
				int index = -1;
				htmlTextContentFound = false;
				for (HarEntry entry : hentry) {
					index++;
					
					refererUrl = wau.getRefererUrl(hentry.get(index));
					
					
					if( index == 0 && refererUrl == null ){ //if referer header is missing for the first entry: entry.getrequest.geturl is the pageurl
						//pageTitle = wau.getPageTitle(pages, hentry.get(index));
						refererUrl = hentry.get(index).getRequest().getUrl();
						
						//Create textfile From har
						String htmlTextContent = "";
						try{
							htmlTextContent = HtmlParserUtil.getHtmlContentWithoutHtmlTags(hentry.get(index)
									.getResponse().getContent().getText().toString());
							if (StringUtils.isNotEmpty(htmlTextContent)){
								
								MintFileUtils.writeHtmlContentToFile(htmlTextContent, htmlTextFilePath, true);
							}
						}catch(Exception e){
							logger.error("Exception while writing HTML as text file :"+htmlTextFilePath);
							//logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
						}
					}
				}
				
				/*if ( StringUtils.isNotEmpty(refererUrl) && refererUrl.equals(url) ){
					found = true;
				}
				
				found = moveHarFileToMappedDirectory(filePath, harMappedFileDirectory);*/
			}
		}catch(Exception e){
			logger.error("Exception in findHarFileForUrl");
			logger.error("stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		return found;
	}
	/*
	private static boolean moveHarFileToMappedDirectory(String filePath,
			String harMappedFileDirectory) throws Exception {
		boolean status = true;
		try{
			logger.info("filePath :"+filePath);
			Path p = Paths.get(filePath);
			String fileName = p.getFileName().toString();
			logger.info("fileName :"+fileName);
			FileUtils.moveFileToDirectory(
				      FileUtils.getFile(filePath), 
				      FileUtils.getFile(harMappedFileDirectory), true);
		}catch(Exception e){
			status = false;
			logger.error("Exception while moving Har file to Mapped Directory");
			logger.error("stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		return status;
		
	}*/
}
