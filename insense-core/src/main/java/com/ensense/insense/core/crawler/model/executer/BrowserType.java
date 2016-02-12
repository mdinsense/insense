package com.ensense.insense.core.crawler.model.executer;

public enum BrowserType {

	FIREFOX("firefox", 1), IE("ie", 2), CHROME("chrome", 3), SAFARI("safari", 4);
	@SuppressWarnings("unused")
	private String browserName;
	private int browserCode;
	
	private BrowserType(String browserName, int browserCode) {
		this.browserName = browserName;
		this.browserCode = browserCode;
	}

	public int getBrowserCode(){
		return browserCode;
	}
	
	public String getBrowserName(){
		return browserName;
	}
}
