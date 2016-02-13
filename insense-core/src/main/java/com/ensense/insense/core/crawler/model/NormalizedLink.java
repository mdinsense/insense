package com.ensense.insense.core.crawler.model;


import com.ensense.insense.data.common.model.Link;

public class NormalizedLink {

	private boolean found;
	private Link link;
	
	public boolean isFound() {
		return found;
	}
	public void setFound(boolean found) {
		this.found = found;
	}
	public Link getLink() {
		return link;
	}
	public void setLink(Link link) {
		this.link = link;
	}
	
	@Override
	public String toString() {
		return "NormalizedLink [found=" + found + ", link=" + link + "]";
	}
}
