package com.ensense.insense.core.analytics.model;

import java.util.HashSet;
import java.util.Set;

public class AnalyticsTagDetail {
	private String tagName;
	private Set<String> tagPresentUrl;
	private Set<String> tagNotPresentUrl;
	private Set<String> urlHasErrorTag;
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public Set<String> getTagPresentUrl() {
		if ( null == tagPresentUrl ){
			tagPresentUrl = new HashSet<String>();
		}
		return tagPresentUrl;
	}
	public void setTagPresentUrl(Set<String> tagPresentUrl) {
		this.tagPresentUrl = tagPresentUrl;
	}
	public Set<String> getTagNotPresentUrl() {
		if ( null == tagNotPresentUrl ){
			tagNotPresentUrl = new HashSet<String>();
		}
		return tagNotPresentUrl;
	}
	public void setTagNotPresentUrl(Set<String> tagNotPresentUrl) {
		this.tagNotPresentUrl = tagNotPresentUrl;
	}
	
	public Set<String> getUrlHasErrorTag() {
		if ( null == urlHasErrorTag ){
			urlHasErrorTag = new HashSet<String>();
		}
		return urlHasErrorTag;
	}
	public void setUrlHasErrorTag(Set<String> urlHasErrorTag) {
		this.urlHasErrorTag = urlHasErrorTag;
	}
	
	@Override
	public String toString() {
		return "AnalyticsTagDetail [tagName=" + tagName + ", tagPresentUrl="
				+ tagPresentUrl + ", tagNotPresentUrl=" + tagNotPresentUrl
				+ ", urlHasErrorTag=" + urlHasErrorTag + "]";
	}
}
