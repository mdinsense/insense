package com.ensense.insense.core.webservice.model;

import com.ensense.insense.data.webservice.model.WsSuite;

import java.util.Arrays;



public class SuiteObj {
	
	
	private WsSuite[] wsSuiteArray;
	private int wsSuiteId;
	
	private String wsSuiteName;
	private String privateSuit;
	private int environmentId;
	private String reqInputType;
	
	public String getReqInputType() {
		return reqInputType;
	}
	public void setReqInputType(String reqInputType) {
		this.reqInputType = reqInputType;
	}
	public int getEnvironmentId() {
		return environmentId;
	}
	public void setEnvironmentId(int environmentId) {
		this.environmentId = environmentId;
	}
	public String getWsSuiteName() {
		return wsSuiteName;
	}
	public void setWsSuiteName(String wsSuiteName) {
		this.wsSuiteName = wsSuiteName;
	}
	public String getPrivateSuit() {
		return privateSuit;
	}
	public void setPrivateSuit(String privateSuit) {
		this.privateSuit = privateSuit;
	}
	
	public WsSuite[] getWsSuiteArray() {
		return wsSuiteArray;
	}
	public void setWsSuiteArray(WsSuite[] wsSuiteArray) {
		this.wsSuiteArray = wsSuiteArray;
	}
	
	public int getWsSuiteId() {
		return wsSuiteId;
	}
	public void setWsSuiteId(int wsSuiteId) {
		this.wsSuiteId = wsSuiteId;
	}
	@Override
	public String toString() {
		return "SuiteObj [wsSuiteArray=" + Arrays.toString(wsSuiteArray)
				+ ", wsSuiteId=" + wsSuiteId + ", wsSuiteName=" + wsSuiteName
				+ ", privateSuit=" + privateSuit + ", environmentId="
				+ environmentId + ", reqInputType=" + reqInputType + "]";
	}
	
}
