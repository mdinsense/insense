package com.ensense.insense.data.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.cts.mint.analytics.model.TextImageReportData;

public class Link implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String imageName;
	private String imageDirectory;
	private String url;
	private String navigationPath;
	private boolean transactionExists;
	private boolean pageAccessible;
	private String pageTile;
	private String parentUrl;
	private boolean errorPage;
	private String errorType;
	private boolean includeUrlPattern;
	
    private String endedUrl;
    private String thread;
    private String exception;
    private String imageFullPath;
    private String htmlFileFullPath;
    private String textFileFullPath;
    private String harFilePath;
    private int urlNo;
    private String harReprotsDirectory;
    private int timesTested;
    private boolean openingPage;
    private boolean collectingChildUrl;
    private boolean autoRoboClicking;
    private String linkName;
    private int urlLevel;
    private TextImageReportData textImageReportData;
	private List<PartialText> partialTextList;
	
    public Link(){
    	
    }
    public Link(String url){
    	this.url = url;
    	
    	this.imageName = "";
		this.imageDirectory = "";
		this.navigationPath = "";
		this.pageAccessible = true;
		this.pageTile = "";
		this.parentUrl = "";
		this.errorPage = false;
		this.includeUrlPattern = false;
		this.imageFullPath = "";
		this.htmlFileFullPath = "";
		this.textFileFullPath = "";
    }
	public Link(String imageName, String imageDirectory, String url, String navigationPath, String linkName, boolean pageAccessible, String pageTile, String parentUrl, boolean errorPage, List<PartialText> partialTextList, int urlLevel){
		this.imageName = imageName;
		this.imageDirectory = imageDirectory;
		this.url = url;
		this.navigationPath = navigationPath;
		this.linkName = linkName;
		this.pageAccessible = pageAccessible;
		this.pageTile = pageTile;
		this.parentUrl = parentUrl;
		this.errorPage = errorPage;
		this.imageFullPath = "";
		this.htmlFileFullPath = "";
		this.textFileFullPath = "";
		this.partialTextList = getPartialTextListForThisLink(partialTextList);
		this.urlLevel = urlLevel;
	}
	
	private List<PartialText> getPartialTextListForThisLink(
			List<PartialText> partialTextList2) {
		List<PartialText> partialTextList = new ArrayList<PartialText>();
		for( PartialText PartialText: partialTextList2) {
			PartialText PartialTextTemp = new PartialText();
			
			PartialTextTemp.setContentName(PartialText.getContentName());
			PartialTextTemp.setHtmlTagPath(PartialText.getHtmlTagPath());
			partialTextList.add(PartialTextTemp);
		}
		return partialTextList;
	}
	public String getNavigationPath() {
		return navigationPath;
	}

	public void setNavigationPath(String navigationPath) {
		this.navigationPath = navigationPath;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageDirectory() {
		return imageDirectory;
	}

	public void setImageDirectory(String imageDirectory) {
		this.imageDirectory = imageDirectory;
	}
	
	public boolean isTransactionExists() {
		return transactionExists;
	}

	public void setTransactionExists(boolean transactionExists) {
		this.transactionExists = transactionExists;
	}

	
	public boolean isPageAccessible() {
		return pageAccessible;
	}

	public void setPageAccessible(boolean pageAccessible) {
		this.pageAccessible = pageAccessible;
	}

	public String getPageTile() {
		return pageTile;
	}

	public void setPageTile(String pageTile) {
		this.pageTile = pageTile;
	}
	public String getParentUrl() {
		return parentUrl;
	}

	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}
	
	public boolean isErrorPage() {
		return errorPage;
	}

	public void setErrorPage(boolean errorPage) {
		this.errorPage = errorPage;
	}

	public boolean isIncludeUrlPattern() {
		return includeUrlPattern;
	}
	public void setIncludeUrlPattern(boolean includeUrlPattern) {
		this.includeUrlPattern = includeUrlPattern;
	}
	public String getEndedUrl() {
		return endedUrl;
	}

	public void setEndedUrl(String endedUrl) {
		this.endedUrl = endedUrl;
	}

	public String getThread() {
		return thread;
	}

	public void setThread(String thread) {
		this.thread = thread;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getImageFullPath() {
		return imageFullPath;
	}

	public void setImageFullPath(String imageFullPath) {
		this.imageFullPath = imageFullPath;
	}

	public String getHtmlFileFullPath() {
		return htmlFileFullPath;
	}

	public void setHtmlFileFullPath(String htmlFileFullPath) {
		this.htmlFileFullPath = htmlFileFullPath;
	}

	public String getTextFileFullPath() {
		return textFileFullPath;
	}

	public void setTextFileFullPath(String textFileFullPath) {
		this.textFileFullPath = textFileFullPath;
	}

	public int getUrlNo() {
		return urlNo;
	}

	public void setUrlNo(int urlNo) {
		this.urlNo = urlNo;
	}
	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getHarReprotsDirectory() {
		return harReprotsDirectory;
	}
	public void setHarReprotsDirectory(String harReprotsDirectory) {
		this.harReprotsDirectory = harReprotsDirectory;
	}
	@Override
	public boolean equals(Object obj){
		if (!(obj instanceof Link))
            return false;
        if (obj == this)
            return true;
        
        Link link = (Link)obj;
        
        return new EqualsBuilder().append(url, link.getUrl()).append(parentUrl, link.getParentUrl()).append(navigationPath, link.getNavigationPath()).isEquals();
	}

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(url).append(parentUrl).append(navigationPath).toHashCode();
    }

	public String getHarFilePath() {
		return harFilePath;
	}
	public void setHarFilePath(String harFilePath) {
		this.harFilePath = harFilePath;
	}
	public int getTimesTested() {
		return timesTested;
	}
	public void setTimesTested(int timesTested) {
		this.timesTested = timesTested;
	}
	public boolean isOpeningPage() {
		return openingPage;
	}
	public void setOpeningPage(boolean openingPage) {
		this.openingPage = openingPage;
		
		this.collectingChildUrl = !openingPage;
		this.autoRoboClicking = !openingPage;
	}
	public boolean isCollectingChildUrl() {
		return collectingChildUrl;
	}
	public void setCollectingChildUrl(boolean collectingChildUrl) {
		this.collectingChildUrl = collectingChildUrl;
		
		this.openingPage = !collectingChildUrl;
		this.autoRoboClicking = !collectingChildUrl;
	}
	public boolean isAutoRoboClicking() {
		return autoRoboClicking;
	}
	public void setAutoRoboClicking(boolean autoRoboClicking) {
		this.autoRoboClicking = autoRoboClicking;
		
		this.collectingChildUrl = !autoRoboClicking;
		this.openingPage = !autoRoboClicking;
	}
	
	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	public List<PartialText> getPartialTextList() {
		return partialTextList;
	}
	public void setPartialTextList(List<PartialText> partialTextList) {
		this.partialTextList = partialTextList;
	}
	
	public int getUrlLevel() {
		return urlLevel;
	}
	public void setUrlLevel(int urlLevel) {
		this.urlLevel = urlLevel;
	}
	public TextImageReportData getTextImageReportData() {
		return textImageReportData;
	}
	public void setTextImageReportData(TextImageReportData textImageReportData) {
		this.textImageReportData = textImageReportData;
	}
	@Override
	public String toString() {
		return "Link [imageName=" + imageName + ", imageDirectory="
				+ imageDirectory + ", url=" + url + ", navigationPath="
				+ navigationPath + ", transactionExists=" + transactionExists
				+ ", pageAccessible=" + pageAccessible + ", pageTile="
				+ pageTile + ", parentUrl=" + parentUrl + ", errorPage="
				+ errorPage + ", errorType=" + errorType
				+ ", includeUrlPattern=" + includeUrlPattern + ", endedUrl="
				+ endedUrl + ", thread=" + thread + ", exception=" + exception
				+ ", imageFullPath=" + imageFullPath + ", htmlFileFullPath="
				+ htmlFileFullPath + ", textFileFullPath=" + textFileFullPath
				+ ", harFilePath=" + harFilePath + ", urlNo=" + urlNo
				+ ", harReprotsDirectory=" + harReprotsDirectory
				+ ", timesTested=" + timesTested + ", openingPage="
				+ openingPage + ", collectingChildUrl=" + collectingChildUrl
				+ ", autoRoboClicking=" + autoRoboClicking + ", linkName="
				+ linkName + ", urlLevel=" + urlLevel
				+ ", textImageReportData=" + textImageReportData
				+ ", partialTextList=" + partialTextList + "]";
	}
}
