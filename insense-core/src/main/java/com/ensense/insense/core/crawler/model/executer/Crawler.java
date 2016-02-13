package com.ensense.insense.core.crawler.model.executer;

import com.ensense.insense.core.proxy.BMPServer;
import com.ensense.insense.core.transaction.model.jaxb.LinkNavigation;
import com.ensense.insense.data.common.model.CrawlConfig;
import com.ensense.insense.data.common.model.Link;
import com.ensense.insense.data.common.model.PartialText;
import com.ensense.insense.data.common.model.ScheduleDetails;
import com.ensense.insense.data.common.utils.WebDriverListener;
import com.ensense.insense.data.uitesting.entity.ApplicationConfig;
import com.ensense.insense.data.uitesting.entity.ExcludeUrl;
import com.ensense.insense.data.uitesting.entity.IncludeUrl;
import com.ensense.insense.data.utils.BrowserDriverLoaderUtil;
import com.ensense.insense.data.utils.RemoteWebDriverConfiguration;
import com.ensense.insense.data.common.utils.CommonUtils;
import com.ensense.insense.data.common.utils.FileDirectoryUtil;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.proxy.LegacyProxyServer;
import org.apache.log4j.Logger;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.context.MessageSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.Map.Entry;



public class Crawler {
	
	private String harReportsPath;
	private MessageSource messageSource;
	private MessageSource configProperties;
	private static final Logger logger = Logger.getLogger(Crawler.class);
	int MOB_PROXY_PORT=8105;
	
	public Crawler(MessageSource messageSource, MessageSource configProperties){
		this.messageSource = messageSource;
		this.configProperties = configProperties;
	}
	
	
	public ScheduleDetails initializeSetup(ScheduleDetails appConfig, MessageSource configProperties) {

		String executionTimeStamp = getCurrentTimeStamp();

		// Create snap shot directory
		try {
			doInitialSetup(appConfig, configProperties, executionTimeStamp);
		} catch (Exception e) {
			logger.info("Exception while doing initial setup.");
			e.printStackTrace();
			appConfig.setError(appConfig.getTestScheduleId()
					+ " :Exception while doing initial setup");
			return appConfig;
		}

			
		// Create Analytics report path.

		try {
			// String generateHarReport =
			// messageSource.getMessage("mint.crawler.generateHarReports", null,
			// Locale.getDefault());

			if (appConfig.isAnalyticsTesting()) {
				doHarReportsSetup(appConfig, configProperties,
						executionTimeStamp);
			}

		} catch (Exception e) {
			appConfig
					.setError(appConfig.getTestScheduleId()
							+ " :Error while creating Analytics report directory. Directory path :"
							+ appConfig.getDirectory());
			return appConfig;
		}
			
		// String browserType =
		// messageSource.getMessage("mint.crawler.browserType", null,
		// Locale.getDefault());
		// String browserType = appConfig.getBrowserType();

		logger.info(appConfig.getTestScheduleId()
				+ " :Starting Crawler for the application :"
				+ appConfig.getApplicationName() + " Login ID:"
				+ appConfig.getLoginId());
		BMPServer bmpServer = new BMPServer();
		//ProxyServer mobProxyServer = null;
		LegacyProxyServer mobProxyServer = new BrowserMobProxyServer();

		boolean enableAkamaiTesting = false;
		Proxy proxyClient = null;
		CrawlConfig crawlConfig = new CrawlConfig();
		logger.info(appConfig.getTestScheduleId()
				+ " :Akamai testing enabled :" + appConfig.isAkamaiTesting());
		try {
			if (appConfig.isAkamaiTesting()) {
				enableAkamaiTesting = true;
				// Commenting code for BMP Proxy
				/*
				 * startBMPProxyServer(bmpServer, mobProxyServer,
				 * appConfig.getAkamaiMapping(),
				 * appConfig.getProxyAuthentication());
				 * 
				 * int MOB_PROXY_PORT=8105; String MOB_PROXY_HOST="localhost";
				 * 
				 * proxyClient = bmpServer.getProxyClient(MOB_PROXY_HOST,
				 * MOB_PROXY_PORT);
				 */
			}

			try {
				crawlConfig = initializeCrawl(appConfig);
			} catch (Exception e) {
				appConfig
						.setError(appConfig.getTestScheduleId()
								+ " :Exception while crawling the pages. Schedule details :"
								+ appConfig);
				logger.error(appConfig.getTestScheduleId()
						+ " :Exception while crawling the pages. Schedule details :"
						+ appConfig);
				e.printStackTrace();
				return appConfig;
			} finally {
				logger.info("Closing the Driver.");
				crawlConfig.getDriver().close();

			}

			//Generate Navigation list details.
			generateNavigationList(crawlConfig);

			logger.info(appConfig.getTestScheduleId()
					+ " :Ending Crawler for the applicatoion :"
					+ appConfig.getApplicationName() + " Login ID:"
					+ appConfig.getLoginId());
			// logger.info("Stopping Proxy server...");

		} catch (Exception e) {
			logger.error("Exception in Crawler :" + e);
			e.printStackTrace();
		} finally {
			if (appConfig.isAkamaiTesting()) {
				// TODO: Commenting code for proxy server
				// bmpServer.stopProxyServer(mobProxyServer);
			}
		}
		return appConfig;
	}
	
	protected void doHarReportsSetup(ScheduleDetails appConfig,
			MessageSource configProperties, String executionTimeStamp) {
		String harReportsDirectory = "";
		harReportsDirectory = messageSource.getMessage(
				"mint.crawler.harReportsDirectory", null, Locale.getDefault());

		this.harReportsPath = appConfig.getResultsBaseDirectory() + File.separator
				+ executionTimeStamp + File.separator
				+ appConfig.getBrowserType() + File.separator
				+ harReportsDirectory + File.separator
				+ appConfig.getApplicationName();
		String harDirectoryWithSlash = appConfig.getResultsBaseDirectory() + "\\"
				+ executionTimeStamp + "\\" + appConfig.getBrowserType() + "\\"
				+ harReportsDirectory + "\\" + appConfig.getApplicationName();

		if (!appConfig.isPublicSite()) {
			this.harReportsPath += File.separator + appConfig.getLoginId();
			harDirectoryWithSlash += "\\" + appConfig.getLoginId();
		}
		FileDirectoryUtil.createDirectories(harReportsPath);

		appConfig.setHarReportsPath(harDirectoryWithSlash);

	}

	protected void doInitialSetup(ScheduleDetails appConfig, MessageSource configProperties, String executionTimeStamp) {
		String directory = "";
		String resultsBaseDirectory = "";
		
		try {
			resultsBaseDirectory = getResultsBaseDirectory(appConfig, configProperties);
			
			appConfig.setResultsBaseDirectory(resultsBaseDirectory);
			
			directory = resultsBaseDirectory + File.separator + executionTimeStamp + File.separator + appConfig.getApplicationName() + File.separator + appConfig.getBrowserType();
			String directoryWithSlash = resultsBaseDirectory + "\\" + executionTimeStamp + "\\" + appConfig.getApplicationName() + "\\" + appConfig.getBrowserType();
			
			if ( ! appConfig.isPublicSite() ){
				directory += File.separator + appConfig.getLoginId(); 
				directoryWithSlash += "\\" + appConfig.getLoginId();
			}
			
			appConfig.setSnapShotPath(directoryWithSlash);
			appConfig.setDirectory(directory);
			createDirectory( directory );
		}catch(Exception e){
			appConfig.setError(appConfig.getTestScheduleId()+" :Error while creating directory. Directory path :"+directory);
		}
		
	}


	protected String getResultsBaseDirectory(ScheduleDetails appConfig,
			MessageSource configProperties) {
		String resultsBaseDirectory = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.crawler.resultsBaseDirectory");
		resultsBaseDirectory = FileDirectoryUtil.getAbsolutePath(resultsBaseDirectory, FileDirectoryUtil.getMintRootDirectory(configProperties));
		
		return resultsBaseDirectory;
	}


	protected List<String> getErrorPageIdentifiers() {
		String errorPageString = "";
		try{
			errorPageString = messageSource.getMessage("mint.errorpage", null, Locale.getDefault());
		}catch(Exception e){
			logger.error("mint.errorpage not found in messages.properties.");
		}
		List<String>errorPageIdentifiers = new ArrayList<String>();
		
		if ( null != errorPageString && errorPageString.trim().length() > 0 ){
			String[] errorPageStringSplit = errorPageString.split("\\|");
			
			logger.info("errorPageStringSplit length :"+errorPageStringSplit.length);
			
			for ( String temp : errorPageStringSplit){
				errorPageIdentifiers.add(temp);
			}
		}
		
		logger.info("errorPageIdentifiers :"+errorPageIdentifiers);
		return errorPageIdentifiers;
	}

	protected void startBMPProxyServer(BMPServer bmpServer, LegacyProxyServer mobProxyServer, Properties akamaiMapping, Properties proxyAthentication) {
		logger.info("Entry :startBMPProxyServer");

		try {
			bmpServer.startServer(getUserName(proxyAthentication), getPassword(proxyAthentication), "localhost", 8989);
			bmpServer.addRequestIntercentor(mobProxyServer, getHeaderSpoofMap(akamaiMapping));
			logger.info("Exit :startBMPProxyServer");
		}catch (Exception e){
			bmpServer.stopProxyServer(mobProxyServer);
		}
	}

	protected Map<String, String> getHeaderSpoofMap(Properties akamaiMapping) {
		logger.info("Entry : getHeaderSpoofMap");
		Map<String, String> hostHeaderSpoofMap = new LinkedHashMap<String, String>();
		
		try {
			for(Entry<Object, Object> e : akamaiMapping.entrySet()) {
				String[] property = e.toString().split("\\|");
				hostHeaderSpoofMap.put(property[0], property[1].replace("=", ""));
			}
			
			logger.info("Akamail hostHeaderSpoofMap :"+hostHeaderSpoofMap);
		}catch(Exception e ){
			logger.error("Error while reading Amamai Mapping from the propertyfile :"+e);
			e.printStackTrace();
		}
		logger.info("Exit :getHeaderSpoofMap, hostHeaderSpoofMap->"+hostHeaderSpoofMap);
		return hostHeaderSpoofMap;
	}

	protected String getUserName(Properties proxyAthentication) {
		String userName = "";
		try {
			for(Entry<Object, Object> e : proxyAthentication.entrySet()) {
				String prop = e.toString();
				String[] userDetail = prop.split("\\|");
				
				userName = userDetail[0];
				logger.info("Akamail proxy login userName :"+userName);
			}
		}catch(Exception e){
			logger.error("Error while reading user name for proxy server, from the propertyfile for Akamai Testing:"+e);
			e.printStackTrace();
		}
		return userName;
	}

	protected String getPassword(Properties proxyAthentication) {
		String password = "";
		try {
			for(Entry<Object, Object> e : proxyAthentication.entrySet()) {
				String[] userDetail = e.toString().split("\\|");
				password = userDetail[1];
				
				password = password.replace("=", "");
			}
		}catch(Exception e){
			logger.error("Error while reading password for proxy server, from the propertyfile for Akamai Testing:"+e);
			e.printStackTrace();
		}
		return password;
	}

	protected void createDirectory( String directory ) throws Exception{
		FileDirectoryUtil.createDirectories(directory);
	}
	
	protected boolean doLogin(CrawlConfig crawlConfig) throws Exception{
		logger.info(crawlConfig.getAppConfig().getTestScheduleId()+" :Entry -> doLogin");

		try{
			WebDriver driver = getStartUrl(crawlConfig);
			
			crawlConfig.setDriver(driver);
			logger.info("Start URL :"+driver.getCurrentUrl());
			
			if ( null == driver.getCurrentUrl() || driver.getCurrentUrl().contains("login.do") ){
				crawlConfig.getDriver().close();
				return false;
			}
		} catch(Exception e){
			logger.error(crawlConfig.getAppConfig().getTestScheduleId()+" :Unsuccessful login");
			e.printStackTrace();
			logger.info(crawlConfig.getAppConfig().getTestScheduleId()+" :Exit -> doLogin");
			return false;
		} 
		logger.info(crawlConfig.getAppConfig().getTestScheduleId()+" :Exit -> doLogin");
		return true;
	}
	
	protected CrawlConfig initializeCrawl(ScheduleDetails appConfig) throws Exception{
		String applicationName = appConfig.getApplicationName();
		CrawlConfig crawlConfig = new CrawlConfig();
		String  crawlUrl = "";//getCrawlUrl(appConfig.getIncludeUrl());
		logger.info(appConfig.getTestScheduleId()+" :crawlUrl :"+crawlUrl);
		//crawlConfig.setCrawlUrl(crawlUrl);
		String skipUrl = "";//getSkipUrl(appConfig.getExcludeUrl());

		//crawlConfig.setSkipUrl(skipUrl);
		int stopAfterUrlCount = 0;
		
		logger.info(appConfig.getTestScheduleId()+" :skipUrl :"+skipUrl);

		String removeUrlPattern = "";
		
		try {
			removeUrlPattern = messageSource.getMessage("mint.url.remove."+applicationName, null, Locale.getDefault());
		}catch (Exception e){
			removeUrlPattern = "";
		}
		
		crawlConfig.setRemoveUrlPattern(removeUrlPattern);
		try {
			//String temp = messageSource.getMessage("mint.crawler.stopAfterUrlCount", null, Locale.getDefault());
			stopAfterUrlCount = appConfig.getStopAfter();
		} catch (Exception e){
			stopAfterUrlCount = 0;
		}
		
		crawlConfig.setStopAfterUrlCount(stopAfterUrlCount);
		
		/*Robot enable, this is to collect Analytics data */
		boolean robotEnabled = false;
		
		if ( isRobotEnabled() && appConfig.isAnalyticsTesting() ) {
			robotEnabled = true;
		}
		crawlConfig.setRobotEnabled(robotEnabled);
		crawlConfig.setJqueryFilePath(getJqueryFilePath());
		crawlConfig.setAppConfig(appConfig);
		logger.info(appConfig.getTestScheduleId()+" :robotEnabled :"+ robotEnabled);
		
		List<String> errorpageIdentifiers = getErrorPageIdentifiers();
		crawlConfig.setErrorpageIdentifiers(errorpageIdentifiers);
		
		crawlConfig = startCrawl(crawlConfig);

		logger.info(crawlConfig.getAppConfig().getTestScheduleId()+" :Crawl completed.");
		
		return crawlConfig;
	}
	
	protected ScheduleDetails generateNavigationList(CrawlConfig crawlConfig){
		//TODO add it to AppConfig
		
		ScheduleDetails appConfig = crawlConfig.getAppConfig();
		List<Link> navigationlistDetails = crawlConfig.getCrawlStatus().getNavigationList();
		String tempXmlFile="";
		String xmlFileContents = null;
		List<LinkNavigation.NavigationDetails> navigationResults = new ArrayList<LinkNavigation.NavigationDetails>();
		
		for(Link link : navigationlistDetails){
			LinkNavigation.NavigationDetails navigationDetails =new LinkNavigation.NavigationDetails();
			navigationDetails.setLinkTitle(link.getImageName());
			navigationDetails.setHref(link.getUrl());
			navigationDetails.setPageTitle(link.getPageTile());
			navigationDetails.setNavigationPath(link.getNavigationPath());
			navigationDetails.setTransactionExists(link.isTransactionExists());
			navigationDetails.setAccessibility(link.isPageAccessible());
			navigationDetails.setErrorPage(link.isErrorPage());
			navigationDetails.setParentUrl(link.getParentUrl());
		
			navigationResults.add(navigationDetails);
		}
		
		try {
			xmlFileContents = getResultsAsXMLString(navigationResults, tempXmlFile);
		} catch (Exception e) {
			appConfig.setError("Exception while generating getResultsAsXMLString.");
			e.printStackTrace();
		}
		
		appConfig.setNavigationXmlFileContent(xmlFileContents);
		appConfig.setNavigationList(crawlConfig.getCrawlStatus().getNavigationList());
		
		return appConfig;
	}
	protected CrawlConfig startCrawl(CrawlConfig crawlConfig) {
		WebPageProcessor pageProcessor = new WebPageProcessor();
		if ( crawlConfig.getCrawlStatus().getCrawledUrlCount() == 0 || crawlConfig.getSessionRestart() ==  crawlConfig.getCrawlStatus().getCrawledUrlCount() ) {
			
			if ( null != crawlConfig.getDriver() ) {
				crawlConfig.getDriver().close();
			}
			getHomePage(crawlConfig);
			
			String homeImage = crawlConfig.getAppConfig().getApplicationName() + "_home.jpeg";
			Link homePageLink = new Link(homeImage, "", crawlConfig.getDriver().getCurrentUrl(), "HomePage", "", true, crawlConfig.getDriver().getTitle(), "", 
					false,new ArrayList<PartialText>(), 0);
			
			crawlConfig.getCrawlStatus().setCurrentLink(homePageLink);
			crawlConfig.getCrawlStatus().getQueue().add(homePageLink);
			crawlConfig.getCrawlStatus().getNavigationList().add(homePageLink);
			crawlConfig.getCrawlStatus().getAlreadyListed().add(crawlConfig.getDriver().getCurrentUrl());
			synchronized(crawlConfig.getCrawlStatus()){
				crawlConfig.getCrawlStatus().setFoundUrlcount(crawlConfig.getCrawlStatus().getFoundUrlcount() + 1);
			}
		}
			
		//pageProcessor.processPage(crawlConfig, applicationName, driver, eventListener, skipUrl, crawlUrl, removeUrlPattern, robotEnabled, getJqueryFilePath(), stopAfterUrlCount);
		
		//TODO to check whether browser is closed.
		boolean allWindowsClosed = crawlConfig.getDriver().getWindowHandles().isEmpty();
		try {
			Link newAddress = null;
			while (crawlConfig.getCrawlStatus().getQueue().size() > 0 && (newAddress = crawlConfig.getCrawlStatus().getQueue().poll()) != null) {
				//TODO move this hard coded values to property file.
				if( crawlConfig.getCrawlStatus().getNavigationList().size() % 100 == 0 ){
					//Close the browser and do login
					if ( null != crawlConfig.getDriver() ) {
						crawlConfig.getDriver().close();
					}
					getHomePage(crawlConfig);
				}
				crawlConfig.getCrawlStatus().setCurrentLink(newAddress);
				pageProcessor.processPage(crawlConfig);
				//TODO check the opened page status
				try{
					pageProcessor.collectNewLinkInCurrentPage(crawlConfig);
				}catch(Exception e){
					e.printStackTrace();
				}
				
				//TODO serialize CrawlConfig
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return crawlConfig;
	}

	protected String getJqueryFilePath() {
		String jqueryFilePath = "";
		
		if ( isRobotEnabled() ){
			jqueryFilePath = messageSource.getMessage("mint.crawler.jqueryFilePath", null, Locale.getDefault());
			jqueryFilePath = FileDirectoryUtil.getAbsolutePath(jqueryFilePath, FileDirectoryUtil.getMintRootDirectory(configProperties));
		}
		return jqueryFilePath;
	}

	protected boolean isRobotEnabled() {
		String robot = messageSource.getMessage("mint.crawler.robot", null, Locale.getDefault());
		if ( null != robot && robot.trim().equals("true")) {
			return true;
		}
		return false;
	}

	/*
	private boolean crawl(String applicationName, String resultsBaseDirectory, String browserType, String url, boolean generateHarReport) throws Exception{
		this.resultsDirectory = resultsBaseDirectory + File.separator + executionTimeStamp + File.separator + applicationName;
		FileDirectoryUtil.createDirectories(resultsDirectory);
		
		WebDriver webDriver = getWebDriver(browserType, generateHarReport);
		driver = addListenrer(webDriver, resultsDirectory);
		
		driver.get(url);
		
		return false;
	}
	*/
	
	protected EventFiringWebDriver getStartUrl(CrawlConfig crawlConfig ) throws Exception {
		
		ScheduleDetails appConfig = crawlConfig.getAppConfig();
		EventFiringWebDriver driver = (EventFiringWebDriver)crawlConfig.getDriver();
		String applicationName = crawlConfig.getAppConfig().getApplicationName();
		boolean rsaLogin = crawlConfig.getAppConfig().isRsaEnabled(); 
		boolean enableAkamaiTesting = crawlConfig.getAppConfig().isAkamaiTesting();
		String url = crawlConfig.getAppConfig().getUrl();
		String login = crawlConfig.getAppConfig().getLoginId();
		String passwd = crawlConfig.getAppConfig().getPassword();
		String securityAnswer = crawlConfig.getAppConfig().getSecurityAnswer();
		
		logger.info(appConfig.getTestScheduleId()+" :Entry -> getStartUrl");
		Class<?> aClass = null;
		Method testMethod;
		try{
			if ( applicationName.equals("RetailPortal")){
				aClass = Class.forName("com.cts.mint.crawler.LifeInsuranceLogin");
				testMethod = aClass.getMethod("testLifeInsuranceLogin");
			} else if ( applicationName.equals("ProducerPortal")){
				aClass = Class.forName("com.cts.mint.crawler.ProducerPortalLogin");
				testMethod = aClass.getMethod("testProducerPortalLogin");
			}else if (applicationName.equals("UD")){
				aClass = Class.forName("com.cts.mint.crawler.UDSt2Login");
				testMethod = aClass.getMethod("testUDSt2Login");
			}else if ( applicationName.equals("IWC") && enableAkamaiTesting){
				aClass = Class.forName("com.cts.mint.crawler.IWCAkamaiLogin");
				testMethod = aClass.getMethod("testIWCAkamaiLogin");
			} else if ( rsaLogin ){
				aClass = Class.forName("com.cts.mint.crawler.RSALogin");
				testMethod = aClass.getMethod("testRSALogin");
			}else{
				aClass = Class.forName("com.cts.mint.crawler.Login");
				testMethod = aClass.getMethod("testLogin");
			}
			Object obj = aClass.newInstance();
			Method method = aClass.getMethod("initialSetUp", EventFiringWebDriver.class, String.class, String.class, String.class, String.class);
			method.invoke(obj, driver, url, login, passwd, securityAnswer);
			
			method = aClass.getMethod("setUp");
			method.invoke(obj);

			testMethod.invoke(obj);
			
			logger.info(appConfig.getTestScheduleId()+" :Exit -> getStartUrl");
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new Exception(e);
		} catch ( Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}

		return driver;
	}

	protected WebDriver getWebDriver(ScheduleDetails appConfig, Proxy proxyClient, MessageSource configProperties) throws Exception{
		BrowserDriverLoaderUtil driverUtil = new BrowserDriverLoaderUtil();
		String firebug = messageSource.getMessage("mint.crawler.firebug", null, Locale.getDefault());
		firebug = FileDirectoryUtil.getAbsolutePath(firebug, FileDirectoryUtil.getMintRootDirectory(configProperties));
		String netExport = messageSource.getMessage("mint.crawler.netExport", null, Locale.getDefault());
		netExport = FileDirectoryUtil.getAbsolutePath(netExport, FileDirectoryUtil.getMintRootDirectory(configProperties));

		WebDriver driver;
		String firefoxPath = messageSource.getMessage("mint.crawler.firefoxPath", null, Locale.getDefault());
		String seleniumFirefoxPort = configProperties.getMessage("mint.selenium.firefoxport", null,
				Locale.getDefault());
		
		if ( appConfig.getBrowserType().equals("firefox") && appConfig.isAnalyticsTesting() ){
			//driver = driverUtil.getFirefoxDriverWithHar(firefoxPath, firebug, netExport, appConfig.getHarReportsPath(), appConfig.isAkamaiTesting(), proxyClient, seleniumFirefoxPort,"localhost", 8989, appConfig);
			driver = driverUtil.getFirefoxDriver();
		} else if ( appConfig.getBrowserType().equals("firefox") ){
			
			driver = driverUtil.getFirefoxDriver();
		} else if ( appConfig.getBrowserType().equals("ie") ) {
			String iePath = messageSource.getMessage("mint.crawler.iepath", null, Locale.getDefault());
			iePath = FileDirectoryUtil.getAbsolutePath(iePath, FileDirectoryUtil.getMintRootDirectory(configProperties));
			driver = driverUtil.getIEDriver(iePath);
		}else if ( appConfig.getBrowserType().equals("safari") ) {
			
			driver = driverUtil.getSafariDriver();
		} else {
			throw new Exception(appConfig.getTestScheduleId()+" :Invalid Browser type.");
		}
		
		logger.info("Exit : getWebDriver");
		return driver;
	}
	
	protected WebDriver getRemoteWebDriver( ApplicationConfig appConfig ){
		logger.info("Entry: getRemoteWebDriver");
		BrowserDriverLoaderUtil driverUtil = new BrowserDriverLoaderUtil();
		RemoteWebDriverConfiguration config = new RemoteWebDriverConfiguration();
		//IE
		/*config.setBrowserType(BrowserType.IE.name());
		config.setBrowserVersion("11");
		config.setOperatingSystem("Windows 7");
		*/
		
		//Firefox
		config.setBrowserType(BrowserType.FIREFOX.name());
		config.setBrowserVersion("24.0");
		config.setOperatingSystem("Windows 7");
		
		config.setSauceLabAccessKey("595ce069-3d12-49de-88d1-98e902af2767");
		config.setSauceLabUserId("TCONLINE2");
		config.setSauceLabRemoteUrl("@chalbpocsl01.se.tiaa-cref.org:4445/wd/hub");
		WebDriver webDriver = null;
		
		try {
			webDriver = driverUtil.getRemoteWebDriver(config);
		} catch (UnsupportedCommandException ex) {
			logger.info("Invalid Browser or Operation System:"+ex);
			ex.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		} 
		logger.info("Exit: getRemoteWebDriver");
		return webDriver;
	}
	
	protected synchronized EventFiringWebDriver addListener(CrawlConfig crawlConfig, WebDriver webDriver, String resultsDirectory, boolean regressionTesting, List<String>errorPageIdentifier, MessageSource configProperties){
		logger.info("webDriver :"+webDriver);
		EventFiringWebDriver driver = new EventFiringWebDriver(webDriver);
		WebDriverListener eventListener = new WebDriverListener(webDriver, "Thread", crawlConfig, crawlConfig.getAppConfig(), configProperties);
		//WebDriverListener eventListener = new WebDriverListener(webDriver, resultsDirectory, regressionTesting, errorPageIdentifier, "Thread", crawlConfig.getAppConfig().isBrandingUrlReport(), crawlConfig.getAppConfig().getBrandingReportsPath(), new ArrayList<HtmlFileDetails>(), new ArrayList<HtmlFileDetails>(),
			//	crawlConfig.getAppConfig().getTiaaImageNameList(), crawlConfig.getAppConfig().isHtmlCompare(), crawlConfig.getAppConfig().isTextCompare(), crawlConfig.getAppConfig().isScreenCompare());
		crawlConfig.setEventListener(eventListener);
		driver = driver.register(eventListener);
		return driver;
	}
	
	protected String getSkipUrl(List<ExcludeUrl> skipURL) {
		String skipUrls = "";
		int count = 0;
		for ( ExcludeUrl skipUrl : skipURL) {
			count++;
			skipUrls += skipUrl.getExcludeUrl();
			
			if ( count < skipURL.size() ){
				skipUrls += ",";
			}
		}
		return skipUrls;
	}

	protected String getCrawlUrl(List<IncludeUrl> crawlURL) {
		String crawlUrls = "";
		
		if ( null == crawlURL || crawlURL.size() < 1 ){
			return crawlUrls;
		}
		int count = 0;
		for ( IncludeUrl crawlUrl : crawlURL) {
			count++;
			crawlUrls += crawlUrl.getIncludeUrl();
			
			if ( count < crawlURL.size() ){
				crawlUrls += ",";
			}
				
		}
		return crawlUrls;
	}
	
	protected String getCurrentTimeStamp() {
		String dateFormat = messageSource.getMessage("mint.crawler.dateFormat", null, Locale.getDefault());
		return FileDirectoryUtil.getDate(dateFormat);
	}
	
	public static void main(String[] args) {
		//JUnitCore.runClasses(com.cts.mint.crawler.RSALogin.class);
		//Crawler cl = new Crawler("");
		try {
			//cl.crawl("IWC", "C:\\Dhinakar\\Project\\Mint\\ResultsDirectory", "firefox", "https://origin-publictools-pf.test.tiaa-cref.org/private/selfservices/sso/login.do", 
					//"webtest1", "1234567aA", "tiaa", true, true, "C:\\Dhinakar\\Project\\Mint\\ResultsDirectory");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Class<?> aClass = Class.forName("com.cts.mint.crawler.RSALoginNew");
			
			Method method;
			try {
				Object obj = aClass.newInstance();
				method = aClass.getMethod("initialSetUp", WebDriver.class, String.class, String.class, String.class);
				method.invoke(obj, new FirefoxDriver(), "url", "login", "passwd" );

				method = aClass.getMethod("setUp");
				method.invoke(obj);
				
				method = aClass.getMethod("testRSALogin");
				method.invoke(obj);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Class superclass = class1.getSuperclass();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String getResultsAsXMLString(List<LinkNavigation.NavigationDetails> navigationlistDetails, String xmlFileName) throws Exception{
		logger.info("Entry: getResultsAsXMLString, navigationlistDetails ->"+navigationlistDetails);
		FileOutputStream fos = null;
		ByteArrayOutputStream baos = null;
	    try {
	    	LinkNavigation results = new LinkNavigation();
	    	
	    	results.getNavigationDetails().addAll(navigationlistDetails);
		    JAXBContext context = JAXBContext.newInstance("com.cts.mint.transaction.model.jaxb");
	        Marshaller marshaller = context.createMarshaller();
	        marshaller.setProperty("jaxb.formatted.output",Boolean.TRUE);
	        baos = new ByteArrayOutputStream();
	        marshaller.marshal(results, baos);
	    } catch (JAXBException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		}
        
	    logger.info("Exit: getResultsAsXMLString");
	    return baos.toString();
	}
	
	protected CrawlConfig getHomePage(CrawlConfig crawlConfig){
		try {
			boolean loginSuccessful = false;
			
			//TODO uncomment below code
			WebDriver webDriver = getWebDriver(crawlConfig.getAppConfig(), new Proxy(), configProperties);
			//WebDriver webDriver = getRemoteWebDriver(appConfig);
			
			String loginUrl = crawlConfig.getAppConfig().getUrl();
			if ( crawlConfig.getAppConfig().isAkamaiTesting() ){
				loginUrl= crawlConfig.getAppConfig().getAkamaiUrl();
			}
			
			logger.info(crawlConfig.getAppConfig().getTestScheduleId()+" :loginUrl :"+loginUrl);
			

			
			WebDriver driver = addListener(crawlConfig, webDriver, crawlConfig.getAppConfig().getDirectory(), crawlConfig.getAppConfig().isRegressionTesting(), crawlConfig.getErrorpageIdentifiers(), configProperties);
			crawlConfig.setDriver(driver);
			
			if ( ! crawlConfig.getAppConfig().isPublicSite() ){
				loginSuccessful = doLogin( crawlConfig );
			}else {
				driver.get(loginUrl);
			}
			
			/*WebDriver webDriver = getWebDriver(browserType, harReport);
			driver = addListenrer(webDriver, directory);
			boolean loginSuccessful = true;*/
			if ( ! crawlConfig.getAppConfig().isPublicSite() && ! loginSuccessful ){
				crawlConfig.getAppConfig().setError(crawlConfig.getAppConfig().getTestScheduleId()+" :Exception while login, check url, credentials. Schedule details :"+crawlConfig.getAppConfig());
				logger.error(crawlConfig.getAppConfig().getTestScheduleId()+" :Unable to login, check url, credentials. Schedule details :"+crawlConfig.getAppConfig().toString());
			}
			

			//Link newAddress;
			//SmartUser2 smartUserForAutoRobot = new SmartUser2(crawlConfig.getJqueryFilePath());
			
			//TODO, set the current url status.
		}catch(Exception e){
			crawlConfig.getAppConfig().setError(crawlConfig.getAppConfig().getTestScheduleId()+" :Exception while login, check url, credentials. Schedule details :"+crawlConfig.getAppConfig());
			logger.error(crawlConfig.getAppConfig().getTestScheduleId()+" :Exception while login, check url, credentials. Schedule details :"+crawlConfig.getAppConfig());
			logger.error("Exception :"+e);
			e.printStackTrace();
			return crawlConfig;
		}finally{
			
		}
		
		return crawlConfig;
	}

}
