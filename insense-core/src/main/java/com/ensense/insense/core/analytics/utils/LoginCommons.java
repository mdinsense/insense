package com.ensense.insense.core.analytics.utils;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

public class LoginCommons {
	public  String batchID;
	public String siteLandingUrl;
	public String siteStartsWithUrl;
	
	  public void main(WebDriver driver, String startingUrl, String siteBaseUrl,String batchID){
			//String startingUrl = startingUrl1;//"https://iwc.tiaa-cref.org/private/participants/appmanager/indvportal/desktop?_nfpb=true&_pageLabel=home_page";//"https://ifa.tiaa-cref.org/private/advisors/appmanager/ifaportal/legacy?_nfpb=true&_pageLabel=legacy_home_page";//"https://ifa.tiaa-cref.org/public/index.html";
			//SeleniumWebCrawler swc= new SeleniumWebCrawler();
			//swc.crawl(driver,startingUrl, siteBaseUrl, batchID);
	}

	  
	  public DesiredCapabilities getCapabilities(String batchID){
		  //start configuration for exporting network traffic capture from firebug.net tab to a har file
			
		  File firebug = new File("C:\\tools\\mint\\mint-light\\bin\\Firefox_Addons\\firebug-1.11.4b1.xpi");
		  File netExport = new File("C:\\tools\\mint\\mint-light\\bin\\Firefox_Addons\\netExport-0.9b3.xpi");

		  FirefoxProfile profile = new FirefoxProfile();
		  try {
		  profile.addExtension(firebug);
		  profile.addExtension(netExport);
		  } catch (IOException e) {
		  e.printStackTrace();
		  }

		  profile.setPreference("app.update.enabled", false);

		  //Setting Firebug preferences
		  profile.setPreference("extensions.firebug.currentVersion", "2.0");
		  profile.setPreference("extensions.firebug.addonBarOpened", true);
		  profile.setPreference("extensions.firebug.console.enableSites", true);
		  profile.setPreference("extensions.firebug.script.enableSites", true);
		  profile.setPreference("extensions.firebug.net.enableSites", true);
		  profile.setPreference("extensions.firebug.previousPlacement", 1);
		  profile.setPreference("extensions.firebug.allPagesActivation", "on");
		  profile.setPreference("extensions.firebug.onByDefault", true);
		  profile.setPreference("extensions.firebug.defaultPanelName", "net");
		//  profile.setPreference("extensions.firebug.net.defaultPersist", "true");
		  profile.setPreference("extensions.firebug.net.logLimit", 0);
		  // Setting netExport preferences
		  profile.setPreference("extensions.firebug.netexport.alwaysEnableAutoExport", true);
		  profile.setPreference("extensions.firebug.netexport.autoExportToFile", true);
		  profile.setPreference("extensions.firebug.netexport.Automation", true);
		  profile.setPreference("extensions.firebug.netexport.showPreview", false);
		  profile.setPreference("extensions.firebug.netexport.compress", false);
		  profile.setPreference("extensions.firebug.netexport.defaultLogDir", "C:\\workspace\\CaptureNetworkTraffic\\"+ batchID);

		  DesiredCapabilities capabilities = new DesiredCapabilities();
		  capabilities.setBrowserName("firefox");
		  capabilities.setPlatform(org.openqa.selenium.Platform.ANY);
		  capabilities.setCapability(FirefoxDriver.PROFILE, profile);

		//end configuration for exporting network traffic capture from firebug.net tab to a har file
		return capabilities;  
	}
	
}
