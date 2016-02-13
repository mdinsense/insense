package com.ensense.insense.data.common.model;

import java.io.Serializable;

public class HtmlFileDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fileName;
	private String contentName;
	private String tagValue;
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getContentName() {
		return contentName;
	}
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	public String getTagValue() {
		return tagValue;
	}
	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}
	
	@Override
	public String toString() {
		return "HtmlSplitContent [fileName=" + fileName + ", contentName="
				+ contentName + ", tagValue=" + tagValue + "]";
	}
}
