package com.ensense.insense.core.crawler.model;

import java.io.Serializable;

import com.cts.mint.analytics.model.AnalyticsData;

public class FormElements implements Serializable{

	private static final long serialVersionUID = 1L;
	private String elementId;
	private String elementName;
	private String elementType;
	private String elementValue;
	private String event;
	
	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getElementId() {
		return elementId;
	}
	
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public void setElementType(String elementType) {
		this.elementType = elementType;
	}
	public String getElementType() {
		return elementType;
	}

	public void setElementValue(String elementValue) {
		this.elementValue = elementValue;
	}

	public String getElementValue() {
		return elementValue;
	}


	
	@Override
	public String toString() {
		return "FormElements [elementId=" + elementId + ", elementName="
				+ elementName + ", elementType=" + elementType
				+ ", elementValue=" + elementValue + ", event=" + event + "]";
	}

	@Override
	public int hashCode() {
		 return getElementId().hashCode() + getElementName().hashCode() + getElementType().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		
		FormElements formElements = (FormElements) obj;
		
		return elementId.equals(formElements.getElementId()) && elementName.equals(formElements.getElementName()) && elementType.equals(formElements.getElementType());
	}
	
	
}
