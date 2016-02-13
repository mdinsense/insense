package com.ensense.insense.data.analytics.model;

public class TagSignaturesObject {

	String tagName;
	String tagType;
	String[] tagSignatures;
	/**
	 * @return the tagName
	 */
	public String getTagName() {
		return tagName;
	}
	/**
	 * @param tagName the tagName to set
	 */
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	/**
	 * @return the tagType
	 */
	public String getTagType() {
		return tagType;
	}
	/**
	 * @param tagType the tagType to set
	 */
	public void setTagType(String tagType) {
		this.tagType = tagType;
	}
	/**
	 * @return the tagSignatures
	 */
	public String[] getTagSignatures() {
		return tagSignatures;
	}
	/**
	 * @param tagSignatures the tagSignatures to set
	 */
	public void setTagSignatures(String[] tagSignatures) {
		this.tagSignatures = tagSignatures;
	}
	
}
