package com.ensense.insense.data.common.model;

import java.util.Arrays;


public class ConfigProperty {
	
	private String[] propertyName;
	private String[] propertyDisplayName;
	private String[] propertyValue;
	
	public String[] getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String[] propertyName) {
		this.propertyName = propertyName;
	}
	public String[] getPropertyDisplayName() {
		return propertyDisplayName;
	}
	public void setPropertyDisplayName(String[] propertyDisplayName) {
		this.propertyDisplayName = propertyDisplayName;
	}
	public String[] getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(String[] propertyValue) {
		this.propertyValue = propertyValue;
	}
	
	@Override
	public String toString() {
		return "ConfigProperty [propertyName=" + Arrays.toString(propertyName)
				+ ", propertyDisplayName="
				+ Arrays.toString(propertyDisplayName) + ", propertyValue="
				+ Arrays.toString(propertyValue) + "]";
	}
}
