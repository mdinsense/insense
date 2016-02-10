package com.ensense.insense.core.reports.model;

import java.io.Serializable;

public class TextToFind implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String text;
	private int findCount;
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getFindCount() {
		return findCount;
	}
	public void setFindCount(int findCount) {
		this.findCount = findCount;
	}
	@Override
	public String toString() {
		return "TextFind [text=" + text + ", findCount=" + findCount + "]";
	}
}
