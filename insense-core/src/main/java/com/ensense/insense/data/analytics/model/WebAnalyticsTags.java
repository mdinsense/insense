package com.ensense.insense.data.analytics.model;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;


public class WebAnalyticsTags {
		protected String tagId;
	   protected String tagtype;
	    
  
        protected String tagUrl;    
        protected XMLGregorianCalendar startedDateTime; 
     
		protected List<TagVarNameValueDetailed> tagVarNameValue;
	   public String getTagtype() {
		return tagtype;
	}
	public void setTagtype(String tagtype) {
		this.tagtype = tagtype;
	}
	public List<TagVarNameValueDetailed> getTagVarNameValue() {
		return tagVarNameValue;
	}
	public void setTagVarNameValue(List<TagVarNameValueDetailed> tagVarNameValue) {
		this.tagVarNameValue = tagVarNameValue;
	}
	public String getTagId() {
		return tagId;
	}
	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
	public String getTagUrl() {
		return tagUrl;
	}
	public void setTagUrl(String tagUrl) {
		this.tagUrl = tagUrl;
	}
	public XMLGregorianCalendar getStartedDateTime() {
		return startedDateTime;
	}
	public void setStartedDateTime(XMLGregorianCalendar startedDateTime) {
		this.startedDateTime = startedDateTime;
	}

}
