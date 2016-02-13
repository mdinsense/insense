package com.ensense.insense.services.uiadmin.form.schedule.mintv4;

import java.io.Serializable;
import java.util.Arrays;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


public class TestSuitForm implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean findText;
	private boolean findImage;
	private boolean include;
	private boolean exclude;
	private boolean tag;
	private boolean text;
	private boolean textCompare;
	private boolean htmlCompare;
	private boolean screenCompare;
	private boolean secureFlag;
	private boolean staticFlag;
	private boolean rsaSecured;
	private Integer suitId;
	private Integer columnPosition;
	private Integer browserTypeId;
	private String suitName;
	private String applicationName;
	private String environmentName;
	private String loginOrHomeUrl;
	private String loginId;
	private String password;
	private String securityAnswer;
	private String[] removeTag;
	private String[] removeText;
	private String[] includeUrl;
	private String[] excludeUrl;
	private String[] textName;
	private String[] imageName;
	private CommonsMultipartFile loginScriptFile;
	private CommonsMultipartFile staticUrlFile;
	public boolean isFindText() {
		return findText;
	}
	public void setFindText(boolean findText) {
		this.findText = findText;
	}
	public boolean isFindImage() {
		return findImage;
	}
	public void setFindImage(boolean findImage) {
		this.findImage = findImage;
	}
	public boolean isInclude() {
		return include;
	}
	public void setInclude(boolean include) {
		this.include = include;
	}
	public boolean isExclude() {
		return exclude;
	}
	public void setExclude(boolean exclude) {
		this.exclude = exclude;
	}
	public boolean isTag() {
		return tag;
	}
	public void setTag(boolean tag) {
		this.tag = tag;
	}
	public boolean isText() {
		return text;
	}
	public void setText(boolean text) {
		this.text = text;
	}
	public boolean isTextCompare() {
		return textCompare;
	}
	public void setTextCompare(boolean textCompare) {
		this.textCompare = textCompare;
	}
	public boolean isHtmlCompare() {
		return htmlCompare;
	}
	public void setHtmlCompare(boolean htmlCompare) {
		this.htmlCompare = htmlCompare;
	}
	public boolean isScreenCompare() {
		return screenCompare;
	}
	public void setScreenCompare(boolean screenCompare) {
		this.screenCompare = screenCompare;
	}
	public boolean isSecureFlag() {
		return secureFlag;
	}
	public void setSecureFlag(boolean secureFlag) {
		this.secureFlag = secureFlag;
	}
	public boolean isStaticFlag() {
		return staticFlag;
	}
	public void setStaticFlag(boolean staticFlag) {
		this.staticFlag = staticFlag;
	}
	public boolean isRsaSecured() {
		return rsaSecured;
	}
	public void setRsaSecured(boolean rsaSecured) {
		this.rsaSecured = rsaSecured;
	}
	public Integer getSuitId() {
		return suitId;
	}
	public void setSuitId(Integer suitId) {
		this.suitId = suitId;
	}
	public Integer getColumnPosition() {
		return columnPosition;
	}
	public void setColumnPosition(Integer columnPosition) {
		this.columnPosition = columnPosition;
	}
	public Integer getBrowserTypeId() {
		return browserTypeId;
	}
	public void setBrowserTypeId(Integer browserTypeId) {
		this.browserTypeId = browserTypeId;
	}
	public String getSuitName() {
		return suitName;
	}
	public void setSuitName(String suitName) {
		this.suitName = suitName;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getEnvironmentName() {
		return environmentName;
	}
	public void setEnvironmentName(String environmentName) {
		this.environmentName = environmentName;
	}
	public String getLoginOrHomeUrl() {
		return loginOrHomeUrl;
	}
	public void setLoginOrHomeUrl(String loginOrHomeUrl) {
		this.loginOrHomeUrl = loginOrHomeUrl;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSecurityAnswer() {
		return securityAnswer;
	}
	public void setSecurityAnswer(String securityAnswer) {
		this.securityAnswer = securityAnswer;
	}
	public String[] getRemoveTag() {
		return removeTag;
	}
	public void setRemoveTag(String[] removeTag) {
		this.removeTag = removeTag;
	}
	public String[] getRemoveText() {
		return removeText;
	}
	public void setRemoveText(String[] removeText) {
		this.removeText = removeText;
	}
	public String[] getIncludeUrl() {
		return includeUrl;
	}
	public void setIncludeUrl(String[] includeUrl) {
		this.includeUrl = includeUrl;
	}
	public String[] getExcludeUrl() {
		return excludeUrl;
	}
	public void setExcludeUrl(String[] excludeUrl) {
		this.excludeUrl = excludeUrl;
	}
	public String[] getTextName() {
		return textName;
	}
	public void setTextName(String[] textName) {
		this.textName = textName;
	}
	public String[] getImageName() {
		return imageName;
	}
	public void setImageName(String[] imageName) {
		this.imageName = imageName;
	}
	public CommonsMultipartFile getLoginScriptFile() {
		return loginScriptFile;
	}
	public void setLoginScriptFile(CommonsMultipartFile loginScriptFile) {
		this.loginScriptFile = loginScriptFile;
	}
	public CommonsMultipartFile getStaticUrlFile() {
		return staticUrlFile;
	}
	public void setStaticUrlFile(CommonsMultipartFile staticUrlFile) {
		this.staticUrlFile = staticUrlFile;
	}
	
}
