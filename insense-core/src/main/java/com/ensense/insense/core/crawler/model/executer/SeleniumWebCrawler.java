package com.ensense.insense.core.crawler.model.executer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.cts.mint.common.CRUDObjectsToFileUtil;
import com.cts.mint.common.FireFoxUtils;
import com.cts.mint.smartuser.SmartUserV2;



public class SeleniumWebCrawler extends SmartUserV2{
	
	

	//Mahesh : change this to WebDriver..WebDriverBackedSelenium is an older api
	//protected WebDriverBackedSelenium selenium = new WebDriverBackedSelenium(driver, baseUrl);
	int urlCount=0;
	

	
	 public void crawl(WebDriver driver1,String startingUrl, String lastUrl, String batchID, boolean restartBrowser) throws Exception
	 {
		 this.driver=driver1;
		 if(CRUDObjectsToFileUtil.fileExists("C:\\workspace\\CaptureNetworkTraffic\\"+batchID+"\\alreadyVisited.ser")){
		System.out.println("reading alreadyVisited queue from alreadyVisited.ser");
			 alreadyVisited = (Set<String>) CRUDObjectsToFileUtil.readObjectFromFile("C:\\workspace\\CaptureNetworkTraffic\\"+batchID+"\\alreadyVisited.ser");
		 }
    	if(CRUDObjectsToFileUtil.fileExists("C:\\workspace\\CaptureNetworkTraffic\\"+batchID+"\\stack.ser")){
    		System.out.println("reading  stack from stack.ser");
    		stack=(java.util.Stack<String>) CRUDObjectsToFileUtil.readObjectFromFile("C:\\workspace\\CaptureNetworkTraffic\\"+batchID+"\\stack.ser");
		 
    	}
    	
		 this.batchID=batchID;
		
		stack.push(lastUrl);
	    stack.push(startingUrl);
		 // queue.add(startingUrl);
		// queue.add(lastUrl); // the last url for the site to open, this will allow export of anlaytics by netexport for the last desired functional page

	     String newAddress;
	     while (!stack.isEmpty() &&(newAddress = stack.pop())!=null)  {
	    	 
			 //indicates session timedout and user logged out.
        	 //execute login again
	    	if(driver.getCurrentUrl().startsWith("https://publictools.tiaa-cref.org/private/selfservices/sso/login.do")){
	
	    	reLogin(driver);
	    	 }
	     
			    	 
	   
	    	 try{
	    		// if(!newAddress.contains("#")){
	         processPage(driver,newAddress, restartBrowser);
	    	//	 }
	    	 }catch(Exception e){
	    		 e.printStackTrace();
	    	 }
	         //here you may add a code to do anything you wish with the page
	     }
	 }
	 
	 
	 public void reLogin(WebDriver driver){

		 System.out.println("session timedout, trying to relogin and continue where left off");
    	
    	 try {
    
 		  	String baseUrl = "https://www.tiaa-cref.org/";
		    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

		    driver.get(baseUrl + "/public/index.html");
		    driver.findElement(By.id("userId")).click();
		    driver.findElement(By.id("userId")).clear();
		    driver.findElement(By.id("userId")).sendKeys("webtest1");
		    driver.findElement(By.xpath("//input[@value='Login']")).click();
		    driver.findElement(By.id("password")).sendKeys("DCowboys21");
		    driver.findElement(By.cssSelector("#loginButton > span")).click();
	  
	  //invoke the crawler
		    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    	 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
 }
	     
	 protected void processPage(WebDriver driver1, String url, boolean restartBrowser)
	     {
	        
		 
	         System.out.println("\n");
	         System.out.println("Processing Page "+ (urlCount++) +" :" +url);
	    	// selenium.open(url);
	     
	         //save the state every after 20 urls crawling. this will ensure any out of memory issues on browser side , session timeouts etc.
	         //url with # leads to element not found in the cache. this page will be reloaded
	         
	         //save state every 5 urls
	         if((urlCount % 1)==0){
		        	//writeObjectToFile(quarks, "c:/testmystuff/quarks.ser");
		        	CRUDObjectsToFileUtil.writeObjectToFile(alreadyVisited,"C:\\workspace\\CaptureNetworkTraffic\\"+batchID+"\\alreadyVisited.ser");
		        	CRUDObjectsToFileUtil.writeObjectToFile(stack,"C:\\workspace\\CaptureNetworkTraffic\\"+batchID+"\\stack.ser");
		        	//CRUDObjectsToFileUtil.writeObjectToFile("C:\\workspace\\CaptureNetworkTraffic\\"+batchID);
	         }
	         
	         //restart firefox every 20 urls
	         if(restartBrowser){
		        if((urlCount % 20)==0){
		        //	driver.switchTo().window(parentWindowHandle).close();
		        	driver.quit();
		        	driver = new FirefoxDriver(FireFoxUtils.getCapabilities(batchID));
		        }
	         }
	         
	         try {
	        	 //smart user feature
	        	
	        	 linksOnPageList	= 	processUrl(driver, url);
	        //	 driver.get(url);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	         
			/* this might be causing a crash of firefox
	         String currentUrlInBrowser =driver.getCurrentUrl(); //possible that there was repeated redirect due to error condition, need to make sure what the opened url is same as the url in browser to avoid a repeated redirect situation due to error
	         //add the url to skip patter to stop continuing working it
	         
	         if(!url.equals(currentUrlInBrowser)){
	        	 //add to already visited
	        	 System.out.println("url did not match browser url.. detected an abnormal condition, skipping url");
	        	 abnormalUrl.add(url); //add the url currently scanned to the list, so that it is not repeate crawled.
	         }
	         
	         */
	    	
	         
	       //  selenium.windowMaximize();
	         //selenium.waitForPageToLoad("120");
	         try {
				Thread.sleep(0000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

	         
	     }
  
	  

}
