package com.ensense.insense.core.analytics.model;

import java.io.Serializable;
import java.util.List;

import com.cts.mint.common.model.Link;


public class SummaryReport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String diffType;
	private List<Link> links;
	
	public String getDiffType() {
		return diffType;
	}

	public void setDiffType(String diffType) {
		this.diffType = diffType;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	@Override
	public String toString() {
		return "SummaryReport [diffType=" + diffType + ", links=" + links + "]";
	}
}
