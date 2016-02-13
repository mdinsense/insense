package com.ensense.insense.core.reports.model;

import com.ensense.insense.data.common.model.ImageProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TextAndImageDetail implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;
	private int urlNo;
	private String url;
	private String urlMatched;
	private String pageTitle;
	private String errorpage;
	private int allImageCount;
	private int baselineAllImageCount;
	private Set<ImageProperty> allImageUrls;
	private Set<ImageProperty> baselineAllImageUrls;
	private int tiaaImageCount;
	private int baselineTiaaImageCount;
	private Set<ImageProperty> tiaaImageUrls;
	private Set<ImageProperty> baselineTiaaImageUrls;
	private int tiaaCrefStringCount;
	private int baselineTiaaCrefStringCount;
	private int tiaaStringCount;
	private int baselineTiaaStringCount;
	private int percentageTextMatch;
	private int percentageWordMatch;
	private int percentageSentenceMatch;
	private int levenshteinDistance;
	private List<TextToFind> textToFindList;
	private boolean contentFound;
	private String textFilePath;
	private String navigationPath;
	private String parentUrl;
	private String linkName;
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
		if (tiaaImageUrls == null) {
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

	public int getBaselineAllImageCount() {
		return baselineAllImageCount;
	}

	public void setBaselineAllImageCount(int baselineAllImageCount) {
		this.baselineAllImageCount = baselineAllImageCount;
	}

	public int getBaselineTiaaImageCount() {
		return baselineTiaaImageCount;
	}

	public void setBaselineTiaaImageCount(int baselineTiaaImageCount) {
		this.baselineTiaaImageCount = baselineTiaaImageCount;
	}

	public Set<ImageProperty> getBaselineAllImageUrls() {
		return baselineAllImageUrls;
	}

	public void setBaselineAllImageUrls(Set<ImageProperty> baselineAllImageUrls) {
		this.baselineAllImageUrls = baselineAllImageUrls;
	}

	public Set<ImageProperty> getBaselineTiaaImageUrls() {
		if (baselineTiaaImageUrls == null) {
			baselineTiaaImageUrls = new HashSet<ImageProperty>();
		}
		return baselineTiaaImageUrls;
	}

	public void setBaselineTiaaImageUrls(
			Set<ImageProperty> baselineTiaaImageUrls) {
		this.baselineTiaaImageUrls = baselineTiaaImageUrls;
	}

	public int getBaselineTiaaCrefStringCount() {
		return baselineTiaaCrefStringCount;
	}

	public void setBaselineTiaaCrefStringCount(int baselineTiaaCrefStringCount) {
		this.baselineTiaaCrefStringCount = baselineTiaaCrefStringCount;
	}

	public int getBaselineTiaaStringCount() {
		return baselineTiaaStringCount;
	}

	public void setBaselineTiaaStringCount(int baselineTiaaStringCount) {
		this.baselineTiaaStringCount = baselineTiaaStringCount;
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

	public List<TextToFind> getTextToFindList() {
		if ( null == textToFindList ){
			textToFindList = new ArrayList<TextToFind>();
		}
		return textToFindList;
	}

	public void setTextToFindList(List<TextToFind> textToFindList) {
		this.textToFindList = textToFindList;
	}

	public boolean isContentFound() {
		return contentFound;
	}

	public void setContentFound(boolean contentFound) {
		this.contentFound = contentFound;
	}

	public String getTextFilePath() {
		return textFilePath;
	}

	public void setTextFilePath(String textFilePath) {
		this.textFilePath = textFilePath;
	}

	public String getNavigationPath() {
		return navigationPath;
	}

	public void setNavigationPath(String navigationPath) {
		this.navigationPath = navigationPath;
	}

	public String getParentUrl() {
		return parentUrl;
	}

	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	@Override
	public String toString() {
		return "TextAndImageDetail [urlNo=" + urlNo + ", url=" + url
				+ ", urlMatched=" + urlMatched + ", pageTitle=" + pageTitle
				+ ", errorpage=" + errorpage + ", allImageCount="
				+ allImageCount + ", baselineAllImageCount="
				+ baselineAllImageCount + ", allImageUrls=" + allImageUrls
				+ ", baselineAllImageUrls=" + baselineAllImageUrls
				+ ", tiaaImageCount=" + tiaaImageCount
				+ ", baselineTiaaImageCount=" + baselineTiaaImageCount
				+ ", tiaaImageUrls=" + tiaaImageUrls
				+ ", baselineTiaaImageUrls=" + baselineTiaaImageUrls
				+ ", tiaaCrefStringCount=" + tiaaCrefStringCount
				+ ", baselineTiaaCrefStringCount="
				+ baselineTiaaCrefStringCount + ", tiaaStringCount="
				+ tiaaStringCount + ", baselineTiaaStringCount="
				+ baselineTiaaStringCount + ", percentageTextMatch="
				+ percentageTextMatch + ", percentageWordMatch="
				+ percentageWordMatch + ", percentageSentenceMatch="
				+ percentageSentenceMatch + ", levenshteinDistance="
				+ levenshteinDistance + ", textToFindList=" + textToFindList
				+ ", contentFound=" + contentFound + ", textFilePath="
				+ textFilePath + ", navigationPath=" + navigationPath
				+ ", parentUrl=" + parentUrl + ", linkName=" + linkName + "]";
	}
}
