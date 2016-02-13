package com.ensense.insense.data.crawler.model;


import com.ensense.insense.data.model.uiadmin.form.schedule.CompareLink;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class CompareConfig implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String compareStartTime;
	private String compareEndTime;
	private Integer compareProcessedUrlCount;
	private Integer comparePendingUrlCount;
	private String lastUpdatedTime;
	private List<CompareLink> compareLinkList;
	
	public String getCompareStartTime() {
		return compareStartTime;
	}
	public void setCompareStartTime(String compareStartTime) {
		this.compareStartTime = compareStartTime;
	}
	public String getCompareEndTime() {
		return compareEndTime;
	}
	public void setCompareEndTime(String compareEndTime) {
		this.compareEndTime = compareEndTime;
	}
	public Integer getCompareProcessedUrlCount() {
		return compareProcessedUrlCount;
	}
	public void setCompareProcessedUrlCount(Integer compareProcessedUrlCount) {
		this.compareProcessedUrlCount = compareProcessedUrlCount;
	}
	public Integer getComparePendingUrlCount() {
		return comparePendingUrlCount;
	}
	public void setComparePendingUrlCount(Integer comparePendingUrlCount) {
		this.comparePendingUrlCount = comparePendingUrlCount;
	}
	public String getLastUpdatedTime() {
		return lastUpdatedTime;
	}
	public void setLastUpdatedTime(String lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}
	
	public List<CompareLink> getCompareLinkList() {
		if ( null == compareLinkList ){
			compareLinkList = new ArrayList<CompareLink>();
		}
		return compareLinkList;
	}
	public void setCompareLinkList(List<CompareLink> compareLinkList) {
		this.compareLinkList = compareLinkList;
	}
	
	public void addCompareLink(CompareLink compareLink){
		if ( null == compareLinkList ){
			compareLinkList = new ArrayList<CompareLink>();
		}
		compareLinkList.add(compareLink);
	}
	@Override
	public String toString() {
		return "CompareConfig [compareStartTime=" + compareStartTime
				+ ", compareEndTime=" + compareEndTime
				+ ", compareProcessedUrlCount=" + compareProcessedUrlCount
				+ ", comparePendingUrlCount=" + comparePendingUrlCount
				+ ", lastUpdatedTime=" + lastUpdatedTime + ", compareLinkList="
				+ compareLinkList + "]";
	}
}
