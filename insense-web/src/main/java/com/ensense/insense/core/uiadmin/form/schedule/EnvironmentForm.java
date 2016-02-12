package com.ensense.insense.core.uiadmin.form.schedule;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

@SuppressWarnings("serial")
public class EnvironmentForm extends UiTestingSetupForm {

	private String environmentName;
	private String loginUrl;
	private String isPublicSite;
	private String akamaiUrl;
	private String sourceFolderName;
	private CommonsMultipartFile file;
	private String environmentLoginScriptId;
	private String fileUrlPath;
	private CommonsMultipartFile excelStaticUrlFile;
	private Integer columnPosition;
	private int staticUrlCount;
	
	/**
	 * @return the environmentName
	 */
	public String getEnvironmentName() {
		return environmentName;
	}
	/**
	 * @param environmentName the environmentName to set
	 */
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}
	/**
	 * @return the loginUrl
	 */
	public String getLoginUrl() {
		return loginUrl;
	}
	/**
	 * @param loginUrl the loginUrl to set
	 */
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}
	/**
	 * @return the isPublicSite
	 */
	public String getIsPublicSite() {
		return isPublicSite;
	}
	/**
	 * @param isPublicSite the isPublicSite to set
	 */
	public void setIsPublicSite(String isPublicSite) {
		this.isPublicSite = isPublicSite;
	}
	/**
	 * @return the akamaiUrl
	 */
	public String getAkamaiUrl() {
		return akamaiUrl;
	}
	/**
	 * @param akamaiUrl the akamaiUrl to set
	 */
	public void setAkamaiUrl(String akamaiUrl) {
		this.akamaiUrl = akamaiUrl;
	}
	/**
	 * @return the sourceFolderName
	 */
	public String getSourceFolderName() {
		return sourceFolderName;
	}
	/**
	 * @param sourceFolderName the sourceFolderName to set
	 */
	public void setSourceFolderName(String sourceFolderName) {
		this.sourceFolderName = sourceFolderName;
	}
	/**
	 * @return the file
	 */
	public CommonsMultipartFile getFile() {
		return file;
	}
	/**
	 * @param file the file to set
	 */
	public void setFile(CommonsMultipartFile file) {
		this.file = file;
	}
	/**
	 * @return the environmentLoginScriptId
	 */
	public String getEnvironmentLoginScriptId() {
		return environmentLoginScriptId;
	}
	/**
	 * @param environmentLoginScriptId the environmentLoginScriptId to set
	 */
	public void setEnvironmentLoginScriptId(String environmentLoginScriptId) {
		this.environmentLoginScriptId = environmentLoginScriptId;
	}
	/**
	 * @return the fileUrlPath
	 */
	public String getFileUrlPath() {
		return fileUrlPath;
	}
	/**
	 * @param fileUrlPath the fileUrlPath to set
	 */
	public void setFileUrlPath(String fileUrlPath) {
		this.fileUrlPath = fileUrlPath;
	}
	/**
	 * @return the excelStaticUrlFile
	 */
	public CommonsMultipartFile getExcelStaticUrlFile() {
		return excelStaticUrlFile;
	}
	/**
	 * @param excelStaticUrlFile the excelStaticUrlFile to set
	 */
	public void setExcelStaticUrlFile(CommonsMultipartFile excelStaticUrlFile) {
		this.excelStaticUrlFile = excelStaticUrlFile;
	}
	/**
	 * @return the columnPosition
	 */
	public Integer getColumnPosition() {
		return columnPosition;
	}
	/**
	 * @param columnPosition the columnPosition to set
	 */
	public void setColumnPosition(Integer columnPosition) {
		this.columnPosition = columnPosition;
	}
	/**
	 * @return the staticUrlCount
	 */
	public int getStaticUrlCount() {
		return staticUrlCount;
	}
	/**
	 * @param staticUrlCount the staticUrlCount to set
	 */
	public void setStaticUrlCount(int staticUrlCount) {
		this.staticUrlCount = staticUrlCount;
	}
	
}
