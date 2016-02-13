package com.ensense.insense.data.utils;


import com.ensense.insense.data.common.model.ScheduleDetails;
import com.ensense.insense.data.common.utils.CommonUtils;
import com.ensense.insense.data.common.utils.FileDirectoryUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.context.MessageSource;

import java.io.File;
import java.net.URL;



public class BrowserDriverLoaderUtil {
	private static final Logger logger = Logger.getLogger(BrowserDriverLoaderUtil.class);
	public WebDriver getRemoteWebDriver(RemoteWebDriverConfiguration config) throws Exception {
		logger.info("Entry -> getRemoteWebDriver");
		WebDriver driver = null;
		
		try {
			DesiredCapabilities capabilities = getDesiredBrowserType(config.getBrowserType());
			capabilities.setCapability("version", config.getBrowserVersion());
			capabilities.setCapability("platform", config.getOperatingSystem());
			//capabilities.setCapability("max-duration", "10790");
			capabilities.setCapability("idle-timeout", "90");

			driver = new RemoteWebDriver(
					  new URL("http://"+config.getSauceLabUserId() + ":" + config.getSauceLabAccessKey() + config.getSauceLabRemoteUrl()),
					  capabilities);
		} catch (Exception e) {
			logger.error("Exception while getting remote webdriver:"+e);
			e.printStackTrace();
		}
        
		logger.info("Exit -> getRemoteWebDriver");
		return driver;
	}
	
	private DesiredCapabilities getRemoteCapabilities( WebDriver driver, String osPlatform, String browserType, String browserVersion, String userId, String accessKey, String url ) throws Exception{
        DesiredCapabilities capabillities = getDesiredBrowserType(browserType);
        //capabillities.setCapability("version", "5.0");
        //capabillities.setCapability("platform", Platform.MAC);
        //driver = new RemoteWebDriver(
					  //new URL("http://TCONLINE2:595ce069-3d12-49de-88d1-98e902af2767@chalbpocsl01.se.tiaa-cref.org:4445/wd/hub"),
					  //capabillities);
        capabillities = getDesiredBrowserType(browserType);
        capabillities.setCapability("version", browserVersion);
        capabillities.setCapability("platform", osPlatform);
        driver = new RemoteWebDriver(
				  new URL("http://TCONLINE2:595ce069-3d12-49de-88d1-98e902af2767@chalbpocsl01.se.tiaa-cref.org:4445/wd/hub"),
				  capabillities);
        
		return capabillities;
	}

	private DesiredCapabilities getDesiredBrowserType(String browserType) {
		DesiredCapabilities capabillities = DesiredCapabilities.firefox();
		
		if ( browserType.equalsIgnoreCase("firefox") ){
			capabillities = DesiredCapabilities.firefox();
		}else if ( browserType.equalsIgnoreCase("ie") ) {
			capabillities = DesiredCapabilities.internetExplorer();
		}else if ( browserType.equalsIgnoreCase("chrome") ){
			capabillities = DesiredCapabilities.chrome();
		}
		return capabillities;
	}

	private void setProfileForUsingMobProxy(FirefoxProfile profile,
			DesiredCapabilities capabilities, Proxy proxyClient, String mobProxyHost, int mobProxyPort, ScheduleDetails appConfig) {
		logger.info("Entry : setProfileForUsingMobProxy, mobProxyHost->"+mobProxyHost+ " mobProxyPort->"+mobProxyPort);
        
		profile.setAcceptUntrustedCertificates(true);
		profile.setAssumeUntrustedCertificateIssuer(true);
		
		Proxy proxyManual = new Proxy();
		proxyManual.setProxyType(Proxy.ProxyType.MANUAL);
		proxyManual.setHttpProxy(mobProxyHost + ":" + mobProxyPort);
		proxyManual.setSslProxy(mobProxyHost + ":" + mobProxyPort);

		capabilities.setCapability(CapabilityType.PROXY, proxyManual);
		capabilities.setCapability(FirefoxDriver.PROFILE, profile);
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

		logger.info("Exit : setProfileForUsingMobProxy");
	}

	public WebDriver getIEDriver(String IEDriverPath) throws Exception{
		logger.info("Entry -> getIEDriver");
		try {
			
			WebDriver driver = null;
			System.setProperty("webdriver.ie.driver", IEDriverPath);
			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			ieCapabilities.setCapability("ignoreZoomSetting", true);
			//ieCapabilities.setCapability(InternetExplorerDriver., value)
			
			driver = new InternetExplorerDriver(ieCapabilities);
			driver.manage().window().maximize();
			//driver.manage().timeouts().implicitlyWait(Long.parseLong(maxWaitTime), TimeUnit.SECONDS);
			
			logger.info("Exit -> getIEDriver");
			return driver;
		} catch (Exception e){
			e.printStackTrace();
			throw new Exception("Error while loading IE driver", e);
		}
		
	}
	
	
	public WebDriver getIEDriver(ScheduleDetails appConfig, MessageSource configProperties) throws Exception{
		logger.info("Entry -> getIEDriver");
		try {
			
			WebDriver driver = null;
		//	File file = new File("C:\\Program Files\\Internet Explorer\\IEDriverServer.exe");
			File file = new File(getIEDriverPath(configProperties));
			System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
			DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();
			ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			ieCapabilities.setCapability("ignoreZoomSetting", true);
			ieCapabilities.setCapability("ignoreProtectedModeSettings", true);
			//ieCapabilities.setCapability(InternetExplorerDriver., value)
			//driver = new InternetExplorerDriver(ieCapabilities);
			driver = new InternetExplorerDriver(ieCapabilities);
		//	driver.get("http://www.google.com"); 
			driver.manage().window().maximize();
			
			logger.info("Exit -> getIEDriver");
			return driver;
		} catch (Exception e){
			e.printStackTrace();
			throw new Exception("Error while loading IE driver", e);
		}
		
	}
	
	public WebDriver getSafariDriver() throws Exception{
		logger.info("Entry -> getIEDriver");
		try {
			
			WebDriver driver = null;
			driver = new SafariDriver();
			driver.manage().window().maximize();
			 driver.get("http://google.com");
			return driver;
		} catch (Exception e){
			e.printStackTrace();
			throw new Exception("Error while loading Safari driver", e);
		}
		
	}
	
	
	
	public WebDriver getFirefoxDriver(){
		logger.info("Entry -> getFirefoxDriver");
		WebDriver driver = null;
		//File pathToBinary = new File("C:\\Dhinakar\\Software\\Mozilla Firefox_current\\firefox.exe");
		
		//FirefoxBinary ffBinary = new FirefoxBinary(pathToBinary);
		//System.setProperty("webdriver.firefox.bin","C:\\Dhinakar\\Software\\MozillaFirefox_current\\firefox.exe");
		//System.setProperty("webdriver.firefox.port", "11000"); 
		FirefoxProfile firefoxProfile = new FirefoxProfile();
		//firefoxProfile.setPreference("webdriver.firefox.bin", "C:\\Dhinakar\\Software\\MozillaFirefox_current\\firefox.exe");
		//firefoxProfile.setPreference("webdriver.firefox.port", seleniumFirefoxPort);
		
		firefoxProfile.setPreference("network.automatic-ntlm-auth.trusted-uris", "tiaa-cref.org");
		firefoxProfile.setPreference("network.automatic-ntlm-auth.allow-non-fqdn", false);
        
		driver = new FirefoxDriver(firefoxProfile);
		driver.manage().window().maximize();
		//driver.manage().timeouts().implicitlyWait(Long.parseLong(maxWaitTime), TimeUnit.SECONDS);
		
		logger.info("Exit -> getFirefoxDriver");
		return driver;
	}
	
    private static WebDriver createWebDriver(Proxy proxy){
        // configure it as a desired capability
        DesiredCapabilities capabilities = new DesiredCapabilities();
       
        FirefoxProfile profile = new FirefoxProfile();
        profile.setAcceptUntrustedCertificates(true);
        profile.setAssumeUntrustedCertificateIssuer(true);       
        profile.setPreference("network.proxy.http", "localhost");
        profile.setPreference("network.proxy.http_port", 8105);
       proxy.setProxyType(Proxy.ProxyType.MANUAL);
   
       
      capabilities.setCapability(FirefoxDriver.PROFILE,profile);
      capabilities.setCapability(CapabilityType.PROXY, proxy);     
      capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
    
        // start the browser up
        WebDriver driver = new FirefoxDriver(capabilities);
        return driver;
    }

	public WebDriver getFirefoxDriverWithHar(ScheduleDetails appConfig) throws Exception{
		logger.info("Entry -> getFirefoxDriverWithHar");
		WebDriver driver = null;
		
		try {
			//System.setProperty("webdriver.firefox.bin","C:\\Dhinakar\\Software\\MozillaFirefox_current\\firefox.exe");
			FirefoxProfile profile = new FirefoxProfile();
			DesiredCapabilities capabilities = new DesiredCapabilities();
			
			File fireBugFile = new File(appConfig.getFireBugPath());
			File netExportFile = new File(appConfig.getNetExportPath());
	
			logger.info("fireBugFile.getAbsoluteFile() :"+fireBugFile.getAbsoluteFile());
			logger.info("netExportFile.getAbsoluteFile() :"+netExportFile.getAbsoluteFile());
			
			//profile.setPreference("webdriver.firefox.port", seleniumFirefoxPort); 
			
			profile.addExtension(fireBugFile.getAbsoluteFile());
			profile.addExtension(netExportFile.getAbsoluteFile());
			
			//profile.setPreference("webdriver.firefox.bin", "C:\\Dhinakar\\Software\\MozillaFirefox_current\\firefox.exe");
			profile.setPreference("app.update.enabled", false);
	
			// Setting Firebug preferences
			//Stop Download
			profile.setPreference("browser.download.manager.showAlertOnComplete", false);
			profile.setPreference("browser.download.manager.showWhenStarting", false);
			profile.setPreference("extensions.firebug.currentVersion", "2.0");
			profile.setPreference("extensions.firebug.addonBarOpened", true);
			profile.setPreference("extensions.firebug.console.enableSites",
					true);
			profile.setPreference("extensions.firebug.script.enableSites", true);
			profile.setPreference("extensions.firebug.net.enableSites", true);
			profile.setPreference("extensions.firebug.previousPlacement", 1);
			profile.setPreference("extensions.firebug.allPagesActivation", "on");
			profile.setPreference("extensions.firebug.onByDefault", true);
			profile.setPreference("extensions.firebug.defaultPanelName", "net");
	
			//profile.setPreference("extensions.firebug.console.defaultPersist", true);
			//profile.setPreference("extensions.firebug.net.defaultPersist", true);
			profile.setPreference("extensions.firebug.net.logLimit", 0);
	
			// Setting netExport preferences
			profile.setPreference(
					"extensions.firebug.netexport.alwaysEnableAutoExport", true);
			profile.setPreference(
					"extensions.firebug.netexport.autoExportToFile", true);
			profile.setPreference("extensions.firebug.netexport.Automation",
					true);
			profile.setPreference("extensions.firebug.netexport.showPreview",
					false);
			profile.setPreference("extensions.firebug.showFirstRunPage",false);
			profile.setPreference("extensions.firebug.netexport.exportResponseText", false); //custom created by mahesh to not export the response text returned by server. Saves lot of disk space when exporting to har log
	        profile.setPreference("extensions.firebug.netexport.exportCookieValue", false);
	        profile.setPreference("extensions.firebug.netexport.exportResponseCookies", false);
	        profile.setPreference("extensions.firebug.netexport.exportRequestCookies", false);
	        //profile.setPreference("extensions.firebug.netexport.compress", true);
	        profile.setPreference("extensions.firebug.netexport.pageLoadedTimeout", 600000);
	        profile.setPreference("extensions.firebug.netexport.includeResponseBodies", false);
	        
	        profile.setPreference("network.automatic-ntlm-auth.trusted-uris", "tiaa-cref.org");
	        profile.setPreference("network.automatic-ntlm-auth.allow-non-fqdn", false);
	        
	        //Don't cache the resources
	        profile.setPreference("browser.cache.disk.enable", false);  
    		profile.setPreference("browser.cache.memory.enable", false);
	        		
			profile.setPreference("extensions.firebug.netexport.defaultLogDir", new File(appConfig.getHarReportsPath()).getAbsolutePath());
			
			//suppress firefox print dialog
		    profile.setPreference("print.extend_native_print_dialog", false);
		    //suppress all kinds of alert boxes
		    profile.setPreference("capability.policy.strict.Window.alert",  "noAccess");
		    //suppress download dialog, this may not be required as jquery preventdefault wont let the documents to open
		    profile.setPreference("browser.download.panel.shown", false);
			
			capabilities.setBrowserName("firefox");
			capabilities.setPlatform(org.openqa.selenium.Platform.ANY);
			capabilities.setCapability(FirefoxDriver.PROFILE, profile);
	
			// end configuration for exporting network traffic capture from
			// firebug.net tab to a har file
			
			driver = new FirefoxDriver(capabilities);
			driver.manage().window().maximize();
			//driver.manage().timeouts().implicitlyWait(Long.parseLong(maxWaitTime), TimeUnit.SECONDS);
			//driver.manage().timeouts().pageLoadTimeout(Long.parseLong(maxWaitTime), TimeUnit.SECONDS);
			
			//WebElement myDynamicElement = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.tagName("html")));
	
			logger.info("Exit: getFirefoxDriverWithHar");
		}catch (Exception e){
			logger.error("Exception while creating firefox driver.");
			//logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw e;
			
		}
		return driver;
	}
	
	public WebDriver getChromeDriver(ScheduleDetails appConfig, MessageSource configProperties) throws Exception{
		String chromeDriverPath =  CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.crawler.chromedriverpath");
		chromeDriverPath =  FileDirectoryUtil.getAbsolutePath(chromeDriverPath,
				FileDirectoryUtil.getMintRootDirectory(configProperties));
		System.setProperty("webdriver.chrome.driver", chromeDriverPath);
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		return driver;
	}
	
	private String getIEDriverPath(MessageSource configProperties ){
		String ieDriver = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.crawler.iepath");
		ieDriver = FileDirectoryUtil.getAbsolutePath(ieDriver,
				FileDirectoryUtil.getMintRootDirectory(configProperties));
		return ieDriver;
	}
	
}
