package com.ensense.insense.data.uitesting.model;


import com.ensense.insense.data.common.model.HtmlFileDetails;
import com.ensense.insense.data.common.model.ImageProperty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class TextImageReportData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int urlNo;
	private String url;
	private String appName;
	private String urlMatched;
	private String pageTitle;
	private String errorpage;
	private int allImageCount;
	private Set<ImageProperty> allImageUrls;
	private List<String>allImageList;
	private List<String>tiaaImageList;
	private int tiaaImageCount;
	private Set<ImageProperty> tiaaImageUrls;
	private int tiaaCrefStringCount;
	private int tiaaStringCount;
	private int percentageTextMatch;
	private int percentageWordMatch;
	private int percentageSentenceMatch;
	private int levenshteinDistance;
	private List<HtmlFileDetails> htmlFileDetails;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getUrlMatched() {
		return urlMatched;
	}
	public void setUrlMatched(String urlMatched) {
		this.urlMatched = urlMatched;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getErrorpage() {
		return errorpage;
	}
	public void setErrorpage(String errorpage) {
		this.errorpage = errorpage;
	}
	public int getAllImageCount() {
		return allImageCount;
	}
	public void setAllImageCount(int allImageCount) {
		this.allImageCount = allImageCount;
	}
	public int getTiaaImageCount() {
		return tiaaImageCount;
	}
	public void setTiaaImageCount(int tiaaImageCount) {
		this.tiaaImageCount = tiaaImageCount;
	}
	public Set<ImageProperty> getAllImageUrls() {
		return allImageUrls;
	}
	public void setAllImageUrls(Set<ImageProperty> allImageUrls) {
		this.allImageUrls = allImageUrls;
	}
	public Set<ImageProperty> getTiaaImageUrls() {
		if( tiaaImageUrls == null ){
			tiaaImageUrls = new HashSet<ImageProperty>();
		}
		
		return tiaaImageUrls;
	}
	public void setTiaaImageUrls(Set<ImageProperty> tiaaImageUrls) {
		this.tiaaImageUrls = tiaaImageUrls;
	}
	public int getTiaaCrefStringCount() {
		return tiaaCrefStringCount;
	}
	public void setTiaaCrefStringCount(int tiaaCrefStringCount) {
		this.tiaaCrefStringCount = tiaaCrefStringCount;
	}
	public int getTiaaStringCount() {
		return tiaaStringCount;
	}
	public void setTiaaStringCount(int tiaaStringCount) {
		this.tiaaStringCount = tiaaStringCount;
	}
	
	public int getUrlNo() {
		return urlNo;
	}
	public void setUrlNo(int urlNo) {
		this.urlNo = urlNo;
	}

	public List<String> getAllImageList() {
		return allImageList;
	}
	public void setAllImageList(List<String> allImageList) {
		this.allImageList = allImageList;
	}
	public int getPercentageTextMatch() {
		return percentageTextMatch;
	}
	public void setPercentageTextMatch(int percentageTextMatch) {
		this.percentageTextMatch = percentageTextMatch;
	}
	public int getPercentageWordMatch() {
		return percentageWordMatch;
	}
	public void setPercentageWordMatch(int percentageWordMatch) {
		this.percentageWordMatch = percentageWordMatch;
	}
	public int getPercentageSentenceMatch() {
		return percentageSentenceMatch;
	}
	public void setPercentageSentenceMatch(int percentageSentenceMatch) {
		this.percentageSentenceMatch = percentageSentenceMatch;
	}
	public int getLevenshteinDistance() {
		return levenshteinDistance;
	}
	public void setLevenshteinDistance(int levenshteinDistance) {
		this.levenshteinDistance = levenshteinDistance;
	}
	public List<HtmlFileDetails> getHtmlFileDetails() {
		return htmlFileDetails;
	}
	public void setHtmlFileDetails(List<HtmlFileDetails> htmlFileDetails) {
		this.htmlFileDetails = htmlFileDetails;
	}
	public List<String> getTiaaImageList() {
		return tiaaImageList;
	}
	public void setTiaaImageList(List<String> tiaaImageList) {
		this.tiaaImageList = tiaaImageList;
	}
	@Override
	public String toString() {
		return "TextImageReportData [urlNo=" + urlNo + ", url=" + url
				+ ", appName=" + appName + ", urlMatched=" + urlMatched
				+ ", pageTitle=" + pageTitle + ", errorpage=" + errorpage
				+ ", allImageCount=" + allImageCount + ", allImageUrls="
				+ allImageUrls + ", allImageList=" + allImageList
				+ ", tiaaImageList=" + tiaaImageList + ", tiaaImageCount="
				+ tiaaImageCount + ", tiaaImageUrls=" + tiaaImageUrls
				+ ", tiaaCrefStringCount=" + tiaaCrefStringCount
				+ ", tiaaStringCount=" + tiaaStringCount
				+ ", percentageTextMatch=" + percentageTextMatch
				+ ", percentageWordMatch=" + percentageWordMatch
				+ ", percentageSentenceMatch=" + percentageSentenceMatch
				+ ", levenshteinDistance=" + levenshteinDistance
				+ ", htmlFileDetails=" + htmlFileDetails + "]";
	}
}
