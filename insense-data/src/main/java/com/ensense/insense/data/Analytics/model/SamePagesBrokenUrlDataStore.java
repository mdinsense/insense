package com.ensense.insense.data.analytics.model;

import com.ensense.insense.data.analytics.model.BrokenUrlData;

public class SamePagesBrokenUrlDataStore {

	private BrokenUrlData curentRunBrokenUrlData;
	private BrokenUrlData baselineBrokenUrlData;
	
	public BrokenUrlData getCurentRunBrokenUrlData() {
		return curentRunBrokenUrlData;
	}
	public void setCurentRunBrokenUrlData(BrokenUrlData curentRunBrokenUrlData) {
		this.curentRunBrokenUrlData = curentRunBrokenUrlData;
	}
	public BrokenUrlData getBaselineBrokenUrlData() {
		return baselineBrokenUrlData;
	}
	public void setBaselineBrokenUrlData(BrokenUrlData baselineBrokenUrlData) {
		this.baselineBrokenUrlData = baselineBrokenUrlData;
	}
	@Override
	public String toString() {
		return "SamePagesBrokenUrlDataStore [curentRunBrokenUrlData="
				+ curentRunBrokenUrlData + ", baselineBrokenUrlData="
				+ baselineBrokenUrlData + "]";
	}
	
	
}
