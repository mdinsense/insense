package com.ensense.insense.data.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrawlStatus implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5510341313131624445L;
	
	private Integer crawledUrlCount = new Integer(0);
	private Integer foundUrlcount = new Integer(0); //linkCount
	private java.util.Queue<Link> queue = new java.util.LinkedList<Link>();
	private java.util.HashSet<String> alreadyListed = new java.util.HashSet<String>();
	private List<Link> navigationList = new ArrayList<Link>();
	private Link currentLink; //newAddress
	private Map<String, Link> currentlyProcessingMap = new HashMap<String, Link>();
	private int runStatus;
	
	public void addToCurrentlyProcessingMap(String key, Link link){
		synchronized(this.currentlyProcessingMap){
			currentlyProcessingMap.put(key, link);
		}
	}
	
	public void removeFromCurrentlyProcessingMap(String key){
		synchronized(this.currentlyProcessingMap){
			currentlyProcessingMap.remove(key);
		}
	}
	
	public int getCrawledUrlCount() {
			return crawledUrlCount;
	}
	public void setCrawledUrlCount(int crawledUrlCount) {
		synchronized(this.crawledUrlCount){
			this.crawledUrlCount = crawledUrlCount;
		}
	}
	public java.util.Queue<Link> getQueue() {
		synchronized(this.queue){
			if ( null == queue ){
				queue = new java.util.LinkedList<Link>();
			}
			return queue;
		}
	}
	public void setStack(java.util.LinkedList<Link> queue) {
		synchronized(this.queue){
			this.queue = queue;
		}
	}
	
	public void addToStack(Link link){
		synchronized(queue){
			getQueue().add(link);
		}
	}
	
	public java.util.HashSet<String> getAlreadyListed() {
		synchronized(alreadyListed){
			if ( null == alreadyListed ){
				alreadyListed = new java.util.HashSet<String>();
			}
			return alreadyListed;
		}
	}
	public void setAlreadyListed(java.util.HashSet<String> alreadyListed) {
		synchronized(this.alreadyListed){
			this.alreadyListed = alreadyListed;
		}
	}
	
	public boolean addToAlreadyListed(String url){
		synchronized(alreadyListed){
			return getAlreadyListed().add(url);
		}
	}
	
	public List<Link> getNavigationList() {
		synchronized(navigationList){
			if ( null == navigationList ) {
				navigationList = new ArrayList<Link>();
			}
			return navigationList;
		}
	}
	
	public void addToNavigationList(Link link){
		synchronized(navigationList){
			getNavigationList().add(link);
		}
	}
	
	public void setNavigationList(List<Link> navigationList) {
		synchronized(this.navigationList){
			this.navigationList = navigationList;
		}
	}
	public Link getCurrentLink() {
		return currentLink;
	}
	public void setCurrentLink(Link currentLink) {
		this.currentLink = currentLink;
	}
	
	public int getFoundUrlcount() {
		return foundUrlcount;
	}
	public synchronized void setFoundUrlcount(int foundUrlcount) {
		this.foundUrlcount = foundUrlcount;
	}
	
	public synchronized void incrementFoundUrlCount(){
		this.foundUrlcount++;
	}
	
	public Map<String, Link> getCurrentlyProcessingMap() {
		return currentlyProcessingMap;
	}
	public void setCurrentlyProcessingMap(Map<String, Link> currentlyProcessingMap) {
		this.currentlyProcessingMap = currentlyProcessingMap;
	}
	
	public int getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(int runStatus) {
		this.runStatus = runStatus;
	}

	@Override
	public String toString() {
		return "CrawlStatus [crawledUrlCount=" + crawledUrlCount
				+ ", foundUrlcount=" + foundUrlcount + ", queue=" + queue
				+ ", alreadyListed=" + alreadyListed + ", navigationList="
				+ navigationList + ", currentLink=" + currentLink
				+ ", currentlyProcessingMap=" + currentlyProcessingMap
				+ ", runStatus=" + runStatus + "]";
	}


}
