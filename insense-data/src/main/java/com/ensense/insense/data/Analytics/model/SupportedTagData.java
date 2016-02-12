package com.ensense.insense.data.analytics.model;

public class SupportedTagData {
TagSignaturesObject tagObject;	
boolean supportedTag;

/**
 * @return the tagSupported
 */
public boolean isSupportedTag() {
	return supportedTag;
}
/**
 * @param tagSupported the tagSupported to set
 */
public void setSupportedTag(boolean tagSupported) {
	this.supportedTag = tagSupported;
}



/**
 * @return the tagObject
 */
public TagSignaturesObject getTagObject() {
	return tagObject;
}
/**
 * @param tagObject the tagObject to set
 */
public void setTagObject(TagSignaturesObject tagObject) {
	this.tagObject = tagObject;
}
}
