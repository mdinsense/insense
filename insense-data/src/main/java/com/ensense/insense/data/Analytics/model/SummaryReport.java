package com.ensense.insense.data.analytics.model;

import com.ensense.insense.data.common.model.Link;

import java.io.Serializable;
import java.util.List;


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
