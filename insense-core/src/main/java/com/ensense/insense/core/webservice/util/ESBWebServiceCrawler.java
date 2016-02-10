package com.ensense.insense.core.webservice.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.cts.mint.util.BrowserDriverLoaderUtil;
import com.cts.mint.webservice.model.PingTestStatus;

public class ESBWebServiceCrawler {
	private static final Logger logger = Logger.getLogger(ESBWebServiceCrawler.class);
	
	private WebDriver driver;
	
	public ESBWebServiceCrawler(WebDriver driver){
		this.driver = driver;
	}
	
	public String getHtmlSourceForEsbPingUrl(String url){
		String htmlSource = "";
		try {
			driver.get(url);
			htmlSource = driver.getPageSource();
					
		}catch(Exception e){
		}
		
		return htmlSource;
	}
	
	public Map<String, List<PingTestStatus>> processPingTest(List<String> urls, List<String> lookupPattern, List<String> serviceNames){
		
		Stack<String> stack = addUrlsToStack(urls);
		List<String> alreadyListed = addUrlsToList(urls);
		
		System.out.println("lookupPattern :"+lookupPattern);
		System.out.println("serviceNames :"+serviceNames);
		
		Map<String, List<PingTestStatus>> pingTestResults = getPingTestResult(stack, alreadyListed, lookupPattern, serviceNames);
		return pingTestResults;
	}

	private List<String> addUrlsToList(List<String> urls) {
		List<String> alreadyListed = new ArrayList<String>();
		
		for (String url : urls ){
			alreadyListed.add(url);
		}
		return alreadyListed;
	}

	private Map<String, List<PingTestStatus>> getPingTestResult(
			Stack<String> stack, List<String> alreadyListed,
			List<String> lookupPattern,
			List<String> serviceNames) {
		
		List<PingTestStatus> passedServices = new ArrayList<PingTestStatus>();
		List<PingTestStatus> failedServices = new ArrayList<PingTestStatus>();
		while (stack.size() > 0) {
			System.out.println("In while stack size :"+stack.size());
			stack = processPage(stack, alreadyListed, lookupPattern, serviceNames, passedServices, failedServices);
			
		}
		
		//TODO
		return null;
	}


	private Stack<String> processPage(Stack<String> stack, List<String> alreadyListed, 
			List<String> lookupPattern, List<String> serviceNames, 
			List<PingTestStatus> passedServices, 
			List<PingTestStatus> failedServices) {
		String currentHref = (String)stack.pop();
		System.out.println("Stack size after pop :"+stack.size());
		PingTestStatus pingTestStatus = new PingTestStatus();
		
		System.out.println("Opening the URL :"+currentHref);
		logger.info("Opening the URL :"+currentHref);
		
		alreadyListed.add(currentHref);
		driver.get(currentHref);
		
		//if ( isAddressValid( currentHref, lookupPattern, serviceNames )) {
		if ( isServicePingPage( currentHref, lookupPattern, serviceNames )) {
			String htmlSource = driver.getPageSource();
			pingTestStatus = getPingTestStatus(htmlSource);
			
			//logger.info("htmlSource :"+htmlSource);
			System.out.println("htmlSource :"+htmlSource);
			return stack;
		}
		
		for (WebElement a : driver.findElements(By.tagName("a"))) {
			
			String href = a.getAttribute("href");
			if (isAddressValid( href, lookupPattern, serviceNames ) ){
				System.out.println("Valid address :"+href);
				try {
				if ( null != href && !alreadyListed.contains( href ) && !href.contains("@tiaa-cref.org") && !href.contains("javascript:") ){
					stack.push(href);
					System.out.println("Stack size after push :"+stack.size());
				}
				}catch(Exception e){
					e.printStackTrace();
					System.out.println("Got exception in href :"+href);
					System.out.println("alreadyListed :"+alreadyListed);
				}
			}
		}
		
		System.out.println("Stack size :"+stack.size());
		return stack;
		
	}

	private boolean isServicePingPage(String currentHref,
			List<String> lookupPattern, List<String> serviceNames) {
		boolean status = false;
		if ( isAddressValid( currentHref, lookupPattern, serviceNames )) {
			
			if ( currentHref.contains("pingFileName=esbping-v1-ping.xml") ){
				status = true;
			}
		}
		return status;
	}

	private PingTestStatus getPingTestStatus(String htmlSource) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean isAddressValid(String href, List<String> lookupPattern, List<String> serviceNames) {
		System.out.println("Checking URL valid :"+href);
		if (null == href || "".equals(href) ) {
			System.out.println("Status false");
			return false;
		}
		
		boolean status = false;
		
		status = checkPatternFound(lookupPattern, href);

		if ( status ) {
			status = checkValidService(serviceNames, href);
		}

		System.out.println("Status :"+status);
		return status;
	}

	private boolean checkValidService(List<String> serviceNames, String href) {
		for ( String serviceName : serviceNames ) {
			if ( href.contains(serviceName)){
				System.out.println("Service Staus true, href :"+href);
				return true;
			}
		}
		System.out.println("Service Staus false, href :"+href);
		return false;
	}

	private boolean checkPatternFound(List<String> lookupPattern, String href) {
		for ( String pattern : lookupPattern ) {
			
			if ( href.contains(pattern)){
				return true;
			}
		}
		return false;
	}

	private Stack<String> addUrlsToStack(List<String> urls) {
		Stack<String> stack = new Stack<String>();
		
		for (String url : urls ){
			stack.push(url);
		}
		return stack;
	}
	
	public static void main(String[] args){
		System.out.println("Start time :"+new Date());
		List<String> urls = new ArrayList<String>();
		urls.add("http://clab.glb.tiaa-cref.org/tno/InIAS/esb/prod%20support/ESB_Pings_Site/HAWC_PROD_Domains/esb-ping-pd-a3-inter-d003.html");
		
		List<String> lookupPattern = new ArrayList<String>();
		List<String> serviceNames = new ArrayList<String>();
		lookupPattern.add("ha-int3-dpw.ops.tiaa-cref.org:80");
		lookupPattern.add("http://clab.glb.tiaa-cref.org/tno/InIAS/esb/prod%20support/ESB_Pings_Site");
		lookupPattern.add("chapda3esbdpw30-pd7.ops.tiaa-cref.org");
				
		serviceNames.add("advisor-fee-billing-v1");
		serviceNames.add("advisor-profile-v1");
		serviceNames.add("after-tax-annuities-v1");
		serviceNames.add("brokerage-enrollment-v1");
		serviceNames.add("brokerage-transaction-v1");
		serviceNames.add("customer-assignment-rs-v1");
		serviceNames.add("customer-transaction-pin-rs-v1");
		
		BrowserDriverLoaderUtil driverLoader = new BrowserDriverLoaderUtil();
		WebDriver driver = driverLoader.getFirefoxDriver();
		ESBWebServiceCrawler esbWebServiceCrawler = new ESBWebServiceCrawler(driver);
		/*
		new ESBWebServiceCrawler(driver).isAddressValid("http://chapda3esbdpw30-pd7.ops.tiaa-cref.org:5380/pingdp?backendHostPort=ha-int3-dpw.ops.tiaa-cref.org:80&pingFileName=esbping-v1-ping.xml&serviceURI=advisor-profile-v1",
				lookupPattern, serviceNames);
	
		System.exit(0);*/

		esbWebServiceCrawler.processPingTest(urls, lookupPattern, serviceNames);
		
		driver.close();
		System.out.println("end time :"+new Date());
	}
}
