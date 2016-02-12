package com.ensense.insense.data.analytics.model;

import com.ensense.insense.data.common.model.Link;

import java.util.ArrayList;
import java.util.List;


public class DifferenceSummary {
	private List<Link> addList = null;
	private List<Link> deleteList = null;
	public List<Link> getAddList() {
		return addList;
	}
	public void setAddList(List<Link> addList) {
		this.addList = addList;
	}
	public List<Link> getDeleteList() {
		return deleteList;
	}
	public void setDeleteList(List<Link> deleteList) {
		this.deleteList = deleteList;
	}
	public void addToAddList(Link link){
		if ( null == addList ){
			addList = new ArrayList<Link>();
		}
		addList.add(link);
	}
	public void addToDeleteList(Link link){
		if ( null == deleteList ){
			deleteList = new ArrayList<Link>();
		}
		deleteList.add(link);
	}
	@Override
	public String toString() {
		return "DifferenceSummary [addList=" + addList + ", deleteList="
				+ deleteList + "]";
	}
	
	
}
