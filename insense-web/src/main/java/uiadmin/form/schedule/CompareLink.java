package uiadmin.form.schedule;

import java.util.ArrayList;
import java.util.List;

import com.cts.mint.common.model.PartialText;

public class CompareLink {
	int urlNo;
	String url;
	String baselineFilePath;
	String currentFilePath;
	long baselineFileSize;
	long currentFileSize;
	boolean urlFoundInBaseline;
	boolean urlFoundInCurrentRun;
	int percentageMatch;
	boolean textFileMatched;
	String compareDifferenceTextFilePath;
	boolean comparisonComplete;
	String baselineHtmlFilePath;
	String currentHtmlFilePath;
	boolean htmlMatched;
	String baselineImageFilePath;
	String currentImageFilePath;
	boolean imageMatched;
	String htmlPageViewFilePath;
	String htmlDomFilePath;
	String imageDifferenceFilePath;
	boolean passed;
	private String pageTitle;
	private String linkName;
	private String navigationPath;
	List<PartialText> currentRunPartialTextList;
	List<PartialText> baselinePartialTextList;
	private String parentUrl;
	
	public int getUrlNo() {
		return urlNo;
	}
	public void setUrlNo(int urlNo) {
		this.urlNo = urlNo;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getBaselineFilePath() {
		return baselineFilePath;
	}
	public void setBaselineFilePath(String baselineFilePath) {
		this.baselineFilePath = baselineFilePath;
	}
	public String getCurrentFilePath() {
		return currentFilePath;
	}
	public void setCurrentFilePath(String currentFilePath) {
		this.currentFilePath = currentFilePath;
	}
	public long getBaselineFileSize() {
		return baselineFileSize;
	}
	public void setBaselineFileSize(long baselineFileSize) {
		this.baselineFileSize = baselineFileSize;
	}
	public long getCurrentFileSize() {
		return currentFileSize;
	}
	public void setCurrentFileSize(long currentFileSize) {
		this.currentFileSize = currentFileSize;
	}
	public boolean isUrlFoundInBaseline() {
		return urlFoundInBaseline;
	}
	public void setUrlFoundInBaseline(boolean urlFoundInBaseline) {
		this.urlFoundInBaseline = urlFoundInBaseline;
	}
	public boolean isUrlFoundInCurrentRun() {
		return urlFoundInCurrentRun;
	}
	public void setUrlFoundInCurrentRun(boolean urlFoundInCurrentRun) {
		this.urlFoundInCurrentRun = urlFoundInCurrentRun;
	}
	public int getPercentageMatch() {
		return percentageMatch;
	}
	public void setPercentageMatch(int percentageMatch) {
		this.percentageMatch = percentageMatch;
	}
	public String getCompareDifferenceTextFilePath() {
		return compareDifferenceTextFilePath;
	}
	public void setCompareDifferenceTextFilePath(
			String compareDifferenceTextFilePath) {
		this.compareDifferenceTextFilePath = compareDifferenceTextFilePath;
	}
	public boolean isComparisonComplete() {
		return comparisonComplete;
	}
	public void setComparisonComplete(boolean comparisonComplete) {
		this.comparisonComplete = comparisonComplete;
	}
	public boolean isTextFileMatched() {
		return textFileMatched;
	}
	public void setTextFileMatched(boolean textFileMatched) {
		this.textFileMatched = textFileMatched;
	}
	public String getBaselineHtmlFilePath() {
		return baselineHtmlFilePath;
	}
	public void setBaselineHtmlFilePath(String baselineHtmlFilePath) {
		this.baselineHtmlFilePath = baselineHtmlFilePath;
	}
	public String getCurrentHtmlFilePath() {
		return currentHtmlFilePath;
	}
	public void setCurrentHtmlFilePath(String currentHtmlFilePath) {
		this.currentHtmlFilePath = currentHtmlFilePath;
	}
	public boolean isHtmlMatched() {
		return htmlMatched;
	}
	public void setHtmlMatched(boolean htmlMatched) {
		this.htmlMatched = htmlMatched;
	}
	public String getBaselineImageFilePath() {
		return baselineImageFilePath;
	}
	public void setBaselineImageFilePath(String baselineImageFilePath) {
		this.baselineImageFilePath = baselineImageFilePath;
	}
	public String getCurrentImageFilePath() {
		return currentImageFilePath;
	}
	public void setCurrentImageFilePath(String currentImageFilePath) {
		this.currentImageFilePath = currentImageFilePath;
	}
	public boolean isImageMatched() {
		return imageMatched;
	}
	public void setImageMatched(boolean imageMatched) {
		this.imageMatched = imageMatched;
	}
	
	public String getHtmlPageViewFilePath() {
		return htmlPageViewFilePath;
	}
	public void setHtmlPageViewFilePath(String htmlPageViewFilePath) {
		this.htmlPageViewFilePath = htmlPageViewFilePath;
	}
	public String getHtmlDomFilePath() {
		return htmlDomFilePath;
	}
	public void setHtmlDomFilePath(String htmlDomFilePath) {
		this.htmlDomFilePath = htmlDomFilePath;
	}
	public String getImageDifferenceFilePath() {
		return imageDifferenceFilePath;
	}
	public void setImageDifferenceFilePath(String imageDifferenceFilePath) {
		this.imageDifferenceFilePath = imageDifferenceFilePath;
	}
	
	public boolean isPassed() {
		return passed;
	}
	public void setPassed(boolean passed) {
		this.passed = passed;
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	public String getNavigationPath() {
		return navigationPath;
	}
	public void setNavigationPath(String navigationPath) {
		this.navigationPath = navigationPath;
	}
	public List<PartialText> getCurrentRunPartialTextList() {
		return currentRunPartialTextList;
	}
	public void setCurrentRunPartialTextList(
			List<PartialText> currentRunPartialTextList) {
		this.currentRunPartialTextList = currentRunPartialTextList;
	}
	public List<PartialText> getBaselinePartialTextList() {
		return baselinePartialTextList;
	}
	public void setBaselinePartialTextList(List<PartialText> baselinePartialTextList) {
		this.baselinePartialTextList = baselinePartialTextList;
	}
	public String getParentUrl() {
		return parentUrl;
	}
	public void setParentUrl(String parentUrl) {
		this.parentUrl = parentUrl;
	}
	@Override
	public String toString() {
		return "CompareLink [urlNo=" + urlNo + ", url=" + url
				+ ", baselineFilePath=" + baselineFilePath
				+ ", currentFilePath=" + currentFilePath
				+ ", baselineFileSize=" + baselineFileSize
				+ ", currentFileSize=" + currentFileSize
				+ ", urlFoundInBaseline=" + urlFoundInBaseline
				+ ", urlFoundInCurrentRun=" + urlFoundInCurrentRun
				+ ", percentageMatch=" + percentageMatch + ", textFileMatched="
				+ textFileMatched + ", compareDifferenceTextFilePath="
				+ compareDifferenceTextFilePath + ", comparisonComplete="
				+ comparisonComplete + ", baselineHtmlFilePath="
				+ baselineHtmlFilePath + ", currentHtmlFilePath="
				+ currentHtmlFilePath + ", htmlMatched=" + htmlMatched
				+ ", baselineImageFilePath=" + baselineImageFilePath
				+ ", currentImageFilePath=" + currentImageFilePath
				+ ", imageMatched=" + imageMatched + ", htmlPageViewFilePath="
				+ htmlPageViewFilePath + ", htmlDomFilePath=" + htmlDomFilePath
				+ ", imageDifferenceFilePath=" + imageDifferenceFilePath
				+ ", passed=" + passed + ", pageTitle=" + pageTitle
				+ ", linkName=" + linkName + ", navigationPath="
				+ navigationPath + ", currentRunPartialTextList="
				+ currentRunPartialTextList + ", baselinePartialTextList="
				+ baselinePartialTextList + ", parentUrl=" + parentUrl + "]";
	}
}
