package com.ensense.insense.core.crawler.model.executer;

import com.ensense.insense.core.crawler.model.CompareConfig;
import com.ensense.insense.core.crawler.model.ReportsConfig;
import com.ensense.insense.core.utils.*;
import com.ensense.insense.data.common.model.CrawlConfig;
import com.ensense.insense.data.common.model.ExecutionStatus;
import com.ensense.insense.data.common.model.Link;
import com.ensense.insense.data.common.model.ScheduleDetails;
import com.ensense.insense.data.common.util.MintFileUtils;
import com.ensense.insense.data.uitesting.entity.ScheduleScript;
import com.ensense.insense.services.common.utils.CommonUtils;
import com.ensense.insense.services.common.utils.FileDirectoryUtil;
import com.ensense.insense.services.common.utils.UiTestingConstants;
import com.ensense.insense.services.crawler.WebDriverListener;
import com.ensense.insense.services.reports.TestScheduleService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.context.MessageSource;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class CrawlerSetup {
	private static final Logger logger = Logger.getLogger(CrawlerSetup.class);
	
	public static ScheduleDetails doInitializeSetup(MessageSource messageSource, MessageSource configProperties,
													ScheduleDetails appConfig, TestScheduleService testScheduleService, CrawlerThread initialCrawlerThread) throws Exception{
		logger.debug("Entry: doInitializeSetup");
		appConfig = setupDirectories(appConfig, configProperties,testScheduleService);
		initialCrawlerThread.getCrawlConfig().setStartTime(DateTimeUtil.getCurrentDateTimeAsString(DateTimeUtil.DATE_TIME_FORMAT));
		initialCrawlerThread.getCrawlConfig().getCrawlStatus().setRunStatus(ExecutionStatus.IN_PROGRESS.getStatusCode());
		
		logger.debug("Exit: doInitializeSetup");
		return appConfig;
	}
	
	public static boolean writeCrawlConfig(CrawlConfig crawlConfig, String crawlConfigPath) {
		logger.debug("Entry: writeCrawlConfig");
		boolean status = true;
		
		try{
			crawlConfig.setLastRecordedTime(DateTimeUtil.getCurrentDateTimeAsString(DateTimeUtil.DATE_TIME_FORMAT));

			JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();
			jsonReaderWriter.writeJsonObjectToFile(crawlConfig, crawlConfigPath);
			//CRUDObjectsToFileUtil.writeObjectToFile(initialCrawlerThread.getCrawlConfig(), appConfig.getSerializePath()+File.separator + CRAWL_CONFIG);
		}catch(Exception e){
			status = false;
			logger.error("Exception while reading Crawlconfig Object from file :"+crawlConfigPath);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		
		logger.debug("Exit: writeCrawlConfig");
		return status;
	}
	
	public static boolean writeReportsConfig(CrawlerThread initialCrawlerThread, ReportsConfig reportsConfig){
		boolean status = true;
		
		try{
			JsonReaderWriter<ReportsConfig> jsonReaderWriter = new JsonReaderWriter<ReportsConfig>();
			jsonReaderWriter.writeJsonObjectToFile(reportsConfig, initialCrawlerThread.getCrawlConfig().getAppConfig().getSerializePath()+File.separator + UiTestingConstants.REPORTS_CONFIG);
			//CRUDObjectsToFileUtil.writeObjectToFile(initialCrawlerThread.getCrawlConfig(), appConfig.getSerializePath()+File.separator + CRAWL_CONFIG);
		}catch(Exception e){
			status = false;
			logger.error("Exception while reading Crawlconfig Object from file :"+initialCrawlerThread.getCrawlConfig().getAppConfig().getSerializePath()+File.separator + UiTestingConstants.REPORTS_CONFIG);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		
		return status;
	}
	
	public static boolean writeCompareConfig(CrawlerThread initialCrawlerThread, CompareConfig compareConfig){
		boolean status = true;
		
		try{
			JsonReaderWriter<CompareConfig> jsonReaderWriter = new JsonReaderWriter<CompareConfig>();
			jsonReaderWriter.writeJsonObjectToFile(compareConfig, initialCrawlerThread.getCrawlConfig().getAppConfig().getSerializePath()+File.separator + UiTestingConstants.COMPARE_CONFIG);
		}catch(Exception e){
			status = false;
			logger.error("Exception while writing CompareConfig Object to file :"+initialCrawlerThread.getCrawlConfig().getAppConfig().getSerializePath()+File.separator + UiTestingConstants.COMPARE_CONFIG);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		
		return status;
	}
	
	public static boolean writeCompareConfig(String serializePath, CompareConfig compareConfig){
		boolean status = true;
		
		try{
			JsonReaderWriter<CompareConfig> jsonReaderWriter = new JsonReaderWriter<CompareConfig>();
			jsonReaderWriter.writeJsonObjectToFile(compareConfig, serializePath + File.separator + UiTestingConstants.COMPARE_CONFIG);
		}catch(Exception e){
			status = false;
			logger.error("Exception while writing CompareConfig Object to file :"+serializePath + File.separator + UiTestingConstants.COMPARE_CONFIG);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		
		return status;
	}
	
	public static int initializeForStaticUrlTesting(
			ScheduleDetails appConfig, CrawlerThread initialCrawlerThread) throws Exception{
		int staticUrlCount = 0;
		int urlLevel = 0;
		
		if ( CollectionUtils.isNotEmpty(appConfig.getStaticUrlList())){
			for(String url: appConfig.getStaticUrlList()){
				staticUrlCount++;
				Link link = new Link(CrawlerUtils.getImageName(url)+".jpeg", CrawlerUtils.getImageDirectory(url), url, "", "", false, "", "", false, appConfig.getPartialTextList(), urlLevel);
				initialCrawlerThread.getCrawlStatus().addToStack(link);
			}
		}
		return staticUrlCount;
	}
	
	public static WebDriver getWebDriver(CrawlerThread initialCrawlerThread, ScheduleDetails appConfig, MessageSource configProperties) throws Exception{
		logger.debug("Entry : getWebDriver");
		//WebDriver webDriver = BrowserUtils.getWebDriverForInitialLogin(appConfig.getBrowserType());
		WebDriver webDriver = BrowserUtils.getWebDriverForCrawling(appConfig, configProperties);
		
		logger.debug("Exit : getWebDriver");
		return webDriver;
	}
	
	public static WebDriver getWebDriver(ScheduleDetails appConfig, MessageSource configProperties) throws Exception{
		logger.debug("Entry : getWebDriver");
		//WebDriver webDriver = BrowserUtils.getWebDriverForInitialLogin(appConfig.getBrowserType());
		WebDriver webDriver = BrowserUtils.getWebDriverForCrawling(appConfig, configProperties);
		
		logger.debug("Exit : getWebDriver");
		return webDriver;
	}
	
	public static WebDriverListener getWebDriverListener(WebDriver webDriver, String threadCount, CrawlConfig crawlConfig, ScheduleDetails appConfig, MessageSource configProperties) throws Exception{
		WebDriverListener eventListener = new WebDriverListener(webDriver, threadCount, crawlConfig, appConfig, configProperties);
		
		return eventListener;
	}
	
	public static WebDriverTransactionListener getWebDriverTransactionListener(WebDriver webDriver, String threadCount, CrawlConfig crawlConfig, ScheduleDetails appConfig, ScheduleScript scheduleScript) throws Exception{
		WebDriverTransactionListener eventListener = new WebDriverTransactionListener(webDriver, threadCount, crawlConfig, appConfig, scheduleScript);
		
		return eventListener;
	}
	
	public static EventFiringWebDriver getEventFiringWebDriver(WebDriver webDriver, WebDriverListener eventListener, ScheduleDetails appConfig) throws Exception{
		logger.debug("Entry : getEventFiringWebDriver");
		EventFiringWebDriver eventFiringWebDriver = null;
		try {
			
			eventFiringWebDriver = getEventFiringWebDriver(webDriver, eventListener);
		}catch(Exception e){
			appConfig.setError("Exception while adding Listener to WebDriver.");
			throw new Exception("Exception while adding Listener to WebDriver.");
		}
		logger.debug("Exit : getEventFiringWebDriver");
		return eventFiringWebDriver;
	}
	
	public static EventFiringWebDriver getEventFiringTransactionWebDriver(WebDriver webDriver, WebDriverTransactionListener eventListener, ScheduleDetails appConfig) throws Exception{
		logger.debug("Entry : getEventFiringWebDriver");
		EventFiringWebDriver eventFiringWebDriver = null;
		try {
			
			eventFiringWebDriver = getEventFiringTransactionWebDriver(webDriver, eventListener);
		}catch(Exception e){
			appConfig.setError("Exception while adding Listener to WebDriver.");
			throw new Exception("Exception while adding Listener to WebDriver.");
		}
		logger.debug("Exit : getEventFiringWebDriver");
		return eventFiringWebDriver;
	}
	
	public static boolean checkLoginSuccessful(EventFiringWebDriver webDriver, CrawlerThread initialCrawlerThread, ScheduleDetails appConfig, MessageSource configProperties) throws Exception{
		boolean status = true;
		try{
			if ( ! isLoginValid(webDriver, initialCrawlerThread, appConfig, configProperties) ){
				status = false;
			}
		}catch(Exception e){
			status = false;
		}
		
		
		logger.debug("Exit : initializeForCrawling");
		return status;
	}
	
	private static EventFiringWebDriver getEventFiringWebDriver(WebDriver webDriver, WebDriverListener eventListener) throws Exception{
		EventFiringWebDriver eventFiringWebDriver = null;
		
		try {
			eventFiringWebDriver = new EventFiringWebDriver(webDriver);
			
			eventFiringWebDriver = eventFiringWebDriver.register(eventListener);
		} catch (Exception e) {
			logger.error("Exception while adding event firing listener to webdriver. Exception :"+e);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return eventFiringWebDriver;
	}
	
	private static EventFiringWebDriver getEventFiringTransactionWebDriver(WebDriver webDriver, WebDriverTransactionListener eventListener) throws Exception{
		EventFiringWebDriver eventFiringWebDriver = null;
		
		try {
			eventFiringWebDriver = new EventFiringWebDriver(webDriver);
			
			eventFiringWebDriver = eventFiringWebDriver.register(eventListener);
		} catch (Exception e) {
			logger.error("Exception while adding event firing listener to webdriver. Exception :"+e);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw e;
		}
		return eventFiringWebDriver;
	}
	
	public static String getJqueryFilePath(MessageSource configProperties) throws Exception {
		String jqueryFilePath = "";

		jqueryFilePath = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.crawler.jqueryFilePath");
		
		if ( StringUtils.isEmpty(jqueryFilePath) ){
			throw new Exception("Jquery file path not defined.");
		}
		jqueryFilePath = FileDirectoryUtil.getAbsolutePath(jqueryFilePath,
				FileDirectoryUtil.getMintRootDirectory(configProperties));
		return jqueryFilePath;
	}

	public static boolean isLoginValid(EventFiringWebDriver webDriver, CrawlerThread initialCrawlerThread, ScheduleDetails appConfig, MessageSource configProperties)
			throws Exception {
		logger.debug("Entry : isLoginValid");
		boolean loginSuccessful = true;

		String loginUrl = appConfig.getUrl();
		if ( appConfig.isAkamaiTesting() ) {
			loginUrl = initialCrawlerThread.getCrawlConfig().getAppConfig().getAkamaiUrl();
		}

		logger.info( initialCrawlerThread.getCrawlConfig().getAppConfig().getTestScheduleId()
				+ " :loginUrl :" + loginUrl);
		
		if (!appConfig.isPublicSite()) {
			loginSuccessful = doLogin(webDriver, appConfig, configProperties);
		} else {
			
			//loginUrl = loginUrl.replace("//", "//"+getUserid() +":"+getPasswd()+"@");
			//logger.info("loginUrl :"+loginUrl);
			//deleteCookies(webDriver);
			try{
				webDriver.get(loginUrl);
			}catch(Exception e){
				loginSuccessful = false;
			}
		}
		logger.debug("Exit : isLoginValid");
		return loginSuccessful;
	}
	
	public static boolean doLogin(EventFiringWebDriver webDriver, ScheduleDetails appConfig, MessageSource configProperties) throws Exception {
		logger.info("Entry -> doLogin, Schedule Id :"+appConfig.getTestScheduleId());

		try {
			if ( executeLoginScript(webDriver, appConfig, configProperties ) ){
				
			}

			logger.info("Start URL :" + webDriver.getCurrentUrl());

			if (null == webDriver.getCurrentUrl()
					|| webDriver.getCurrentUrl().contains("login.do")) {
				logger.error("Invalid login, closing the driver.");
				//webDriver.get(webDriver.getCurrentUrl());
				webDriver.close();
				return false;
			}
		} catch (Exception e) {
			logger.info("Schdule ID: "+ appConfig.getTestScheduleId()
					+ ", Unsuccessful login.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			webDriver.close();
			return false;
		}
		logger.info("Exit -> doLogin, Schedule Id :"+appConfig.getTestScheduleId());
		return true;
	}
	
	public static boolean executeLoginScript(EventFiringWebDriver driver, ScheduleDetails appConfig, MessageSource configProperties) throws Exception {
		logger.info(appConfig.getTestScheduleId() + " :Entry -> getStartUrl");

		boolean status = true;
		
		String applicationName = appConfig.getApplicationName();
		String packageDirectory = "";
		String classNameWithPackage = "";
		String environmentName = appConfig.getEnvironmentName();

		boolean rsaLogin = appConfig.isRsaEnabled();

		boolean enableAkamaiTesting = appConfig.isAkamaiTesting();
		String url = appConfig.getUrl();
		String login = appConfig.getLoginId();
		String passwd = EncrypDecryptUtil.decryptPassword(appConfig.getPassword(), EncryptionUtil.secretKey);
		String securityAnswer = appConfig.getSecurityAnswer();
		File file = null;
		Class<?> aClass = null;
		Method testMethod = null;
		try {
			packageDirectory = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.loginScript.packageDirectory");
			
			if ( StringUtils.isEmpty(packageDirectory) ){
				status = false;
				appConfig.setError("Invalid Login Script Directory.");
				return status;
			}
			packageDirectory = FileDirectoryUtil.getAbsolutePath(
					packageDirectory, FileDirectoryUtil
							.getMintRootDirectory(configProperties));
			packageDirectory = packageDirectory + "\\" + applicationName +"_"+ environmentName;
			classNameWithPackage = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.loginScript.packageName");
			
			if ( StringUtils.isEmpty(classNameWithPackage) ){
				status = false;
				appConfig.setError("Invalid Login Script package name or class name.");
				return status;
			}
			
			classNameWithPackage = classNameWithPackage + "." + applicationName +"_"+ environmentName;
			String[] filePathSplit = packageDirectory.split("\\\\com");
			File packageDir = new File(filePathSplit[0]);

			File[] files = MintFileUtils.listFilesOfGivenExtension(packageDirectory, ".class");
			if(files != null &&  files.length > 0) {
				file = files[0];
			}
			
			if(file != null && file.exists() && file.isFile()) {
				URL url1 = packageDir.toURI().toURL();
				URL[] urls = new URL[] { url1 };
				ClassLoader classLoader = new URLClassLoader(urls, CrawlerSetup.class
						.getClassLoader());
				String className = classNameWithPackage + "." + StringUtils.substringBeforeLast(file.getName(), ".");
				aClass = Class.forName(className, true, classLoader);
				
				for ( Method method : aClass.getMethods()){
					
					if ( method.getName().startsWith("test")){
						testMethod = method;
					}
				}

			}else if (applicationName.equals("IFA")) {
				aClass = Class
						.forName("com.cts.mint.crawler.IFALogin");
				testMethod = aClass.getMethod("testIFALogin");
			}      
			else if (environmentName.equals("ST_PUBLIC_BANKING")) {
				aClass = Class
						.forName("com.cts.mint.crawler.PublicBankingLogin");
				testMethod = aClass.getMethod("testPublicBankingLogin");
			}else if (environmentName.equals("UD_PRODFIX")) {
				aClass = Class
						.forName("com.cts.mint.crawler.UDProdfixLogin");
				testMethod = aClass.getMethod("testUDProdfixLogin");
			}else if (environmentName.equals("ST_PUBLIC_INSTITUTE")) {
				aClass = Class
						.forName("com.cts.mint.crawler.PublicInstituteLogin");
				testMethod = aClass.getMethod("testPublicInstituteLogin");
			}else if (applicationName.startsWith("RetailPortal")) {
				aClass = Class
						.forName("com.cts.mint.crawler.LifeInsuranceLogin");
				testMethod = aClass.getMethod("testLifeInsuranceLogin");
			} else if (applicationName.startsWith("ProducerPortal")) {
				aClass = Class
						.forName("com.cts.mint.crawler.ProducerPortalLogin");
				testMethod = aClass.getMethod("testProducerPortalLogin");
			} else if (applicationName.equals("UD")) {
				aClass = Class.forName("com.cts.mint.crawler.UDSt2Login");
				testMethod = aClass.getMethod("testUDSt2Login");
			} else if (applicationName.equals("IWC") && enableAkamaiTesting) {
				aClass = Class.forName("com.cts.mint.crawler.IWCAkamaiLogin");
				testMethod = aClass.getMethod("testIWCAkamaiLogin");
			} else if (rsaLogin && null != appConfig.getDataCenter() && "Broomfield".equals(appConfig.getDataCenter())) {
				logger.info("Login script ->RSADRLogin, with dataCenter");						
				aClass = Class.forName("com.cts.mint.crawler.RSADRLogin");
				testMethod = aClass.getMethod("testRSADRLogin");
			}else if (rsaLogin) {
				logger.info("Login script ->RSALogin");					
				aClass = Class.forName("com.cts.mint.crawler.RSALogin");
				testMethod = aClass.getMethod("testRSALogin");
			} else if (null != appConfig.getDataCenter() && "Broomfield".equals(appConfig.getDataCenter())){
				logger.info("Login script ->LoginDR");					
				aClass = Class.forName("com.cts.mint.crawler.LoginDR");
				testMethod = aClass.getMethod("testLoginDR");
			}else {
				logger.info("Login script ->Login");
				aClass = Class.forName("com.cts.mint.crawler.Login");
				testMethod = aClass.getMethod("testLogin");
			}
			Object obj = aClass.newInstance();
			logger.info("Login class :"+obj.getClass());
			
			Method method = aClass.getMethod("initialSetUp",
					EventFiringWebDriver.class, String.class, String.class,
					String.class, String.class);
			method.invoke(obj, driver, url, login, passwd, securityAnswer);

			method = aClass.getMethod("setUp");
			method.invoke(obj);

			testMethod.invoke(obj);

			logger.info(appConfig.getTestScheduleId() + " :Exit -> getStartUrl");
		} catch (ClassNotFoundException e) {
			status = false;
			logger.error("Exception while executing login script.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		} catch (InstantiationException e) {
			status = false;
			logger.error("Exception while executing login script.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		} catch (IllegalAccessException e) {
			status = false;
			logger.error("Exception while executing login script.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		} catch (SecurityException e) {
			status = false;
			logger.error("Exception while executing login script.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		} catch (NoSuchMethodException e) {
			status = false;
			logger.error("Exception while executing login script.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		} catch (IllegalArgumentException e) {
			status = false;
			logger.error("Exception while executing login script.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		} catch (InvocationTargetException e) {
			status = false;
			logger.error("Exception while executing login script.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		} catch (Exception e) {
			status = false;
			logger.error("Exception while executing login script.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		}

		return status;
	}
	
	public static boolean executeTransactionTestCase(EventFiringWebDriver driver, ScheduleDetails appConfig, MessageSource configProperties, String testCaseFilePath) throws Exception {
		logger.info(appConfig.getTestScheduleId() + " : Transactionscript execution started.");

		boolean status = true;
		String packageDirectory = "";
		String classNameWithPackage = "";
		String url = appConfig.getUrl();
		String login = appConfig.getLoginId();
		String passwd = EncrypDecryptUtil.decryptPassword(appConfig.getPassword(), EncryptionUtil.secretKey);
		String securityAnswer = appConfig.getSecurityAnswer();
		File file = null;
		Class<?> aClass = null;
		Method testMethod = null;
		
		try {
			packageDirectory = testCaseFilePath;
			if ( StringUtils.isEmpty(packageDirectory) ){
				status = false;
				appConfig.setError("Invalid Transaction Script Directory.");
				return status;
			}
			classNameWithPackage = testCaseFilePath.split("\\\\com")[0];
			if ( StringUtils.isEmpty(classNameWithPackage) ){
				status = false;
				appConfig.setError("Invalid Login Script package name or class name.");
				return status;
			}
			File packageDir = new File(testCaseFilePath.split("\\\\com")[0]);
			file = new File(packageDirectory);
			if(file != null && file.exists() && file.isFile()) {
				URL url1 = packageDir.toURI().toURL();
				URL[] urls = new URL[] { url1 };
				ClassLoader classLoader = new URLClassLoader(urls, CrawlerSetup.class
						.getClassLoader());
				String className = StringUtils.substringBeforeLast(file.getAbsolutePath(), ".");
				className = className.replace(packageDir.getAbsolutePath() + "\\", "");
				className = StringUtils.replaceChars(className, "\\", ".");
				className = StringUtils.removeEnd(className, ".");
				aClass = Class.forName(className, true, classLoader);
				for ( Method method : aClass.getMethods()){
					if(method.getAnnotation(Test.class) != null) {
						testMethod = method;
						break;
					}
				}
			}
			if(testMethod == null) {
				status = false;
				appConfig.setError("Invalid Test case.");
				return status;
			}
			Object obj = aClass.newInstance();
			logger.info("Login class :"+obj.getClass());
			
			Method method = aClass.getMethod("initialSetUp",
					EventFiringWebDriver.class, String.class, String.class,
					String.class, String.class);
			method.invoke(obj, driver, url, login, passwd, securityAnswer);
			method = aClass.getMethod("setUp");
			method.invoke(obj);
			logger.info("After Initial setup Method.");
			testMethod.invoke(obj);
			logger.info(appConfig.getTestScheduleId() + " :Exit -> getStartUrl");
		} catch (ClassNotFoundException e) {
			status = false;
			logger.error("Exception while executing script.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		} catch (InstantiationException e) {
			status = false;
			logger.error("Exception while executing script.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		} catch (IllegalAccessException e) {
			status = false;
			logger.error("Exception while executing script.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		} catch (SecurityException e) {
			status = false;
			logger.error("Exception while executing script.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		} catch (NoSuchMethodException e) {
			status = false;
			logger.error("Exception while executing script.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		} catch (IllegalArgumentException e) {
			status = false;
			logger.error("Exception while executing script.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		} catch (InvocationTargetException e) {
			status = false;
			logger.error("Exception while executing script.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		} catch (Exception e) {
			status = false;
			logger.error("Exception while executing script.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			throw new Exception(e);
		}
		logger.info(appConfig.getTestScheduleId() + " : Transactionscript execution completed, status ->"+status);
		return status;
	}
	
	//Creates Directory and sets appconfig properties.
	public static ScheduleDetails setupDirectories(ScheduleDetails appConfig,
			MessageSource configProperties,TestScheduleService testScheduleService) throws Exception{
		logger.debug("Entry: setupDirectories");

		// Create snap shot directory
		try {
			if ( ! createBaseDirectory(appConfig, configProperties) ){
				throw new Exception("Exception while creating Crawl reports directory.");
			}
		} catch (Exception e) {
			logger.info("Exception while doing initial setup."+e);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			appConfig.setError(appConfig.getTestScheduleId()
					+ " :Exception while doing initial setup");
			throw new Exception("Exception while creating Crawl reports directory.");
		}

		// Create HAR report path.

		try {
			doHarReportsSetup(appConfig, configProperties);
		} catch (Exception e) {
			appConfig
					.setError(appConfig.getTestScheduleId()
							+ " :Error while creating HAR report directory. Directory path :"
							+ appConfig.getDirectory());
			logger.info("Exception while creating har reports directory."+e);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			
			throw new Exception("Exception while creating har reports directory.");
			//return appConfig;
		}

		try {
			doReportsSetup(appConfig, configProperties);
		} catch (Exception e) {
			appConfig
					.setError(appConfig.getTestScheduleId()
							+ " :Error while creating report directory. Directory path :"
							+ appConfig.getDirectory());
			logger.info("Exception while creating har reports directory."+e);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			
			throw new Exception("Exception while creating har reports directory.");
			//return appConfig;
		}
		
		//Create Rebranding directory
		/*try {
			if (appConfig.isBrandingUrlReport() || appConfig.isTextCompare() ) {
				doRebrandingSetup(appConfig, configProperties,
						executionTimeStamp);
			}

		} catch (Exception e) {
			appConfig
					.setError(appConfig.getTestScheduleId()
							+ " :Error while creating Rebranding directory. Directory path :"
							+ appConfig.getDirectory());
			logger.info("Exception while creating Rebranding directory."+e);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			return appConfig;
		}
		*/
		//Create serialize objects directory.
		
		try{
			if ( ! createSerializeobjectsDirectory(appConfig, configProperties,
					appConfig.getExecutionTimeStamp())){
				return appConfig;
			}

			boolean result = testScheduleService.updateExecutionSeralizePath(appConfig);
			if(result == false){
				appConfig
				.setError("Exception while updating the Serialize path.");
				logger.error("Exception while updating the Serialize path.");
			}
			
		}catch(Exception e){
			appConfig
			.setError("Schedule Id :"+appConfig.getTestScheduleId()
					+ " :Error while cserialize objects directory. Directory path :"
					+ appConfig.getDirectory());
			logger.error("Exception while creating serialized Object directory.");
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			return appConfig;
		}

		logger.info("Schedule Id :"+appConfig.getTestScheduleId()
				+ " :Starting Crawler for the application :"
				+ appConfig.getApplicationName() + " Login ID:"
				+ appConfig.getLoginId());


		logger.debug("Exit: setupDirectories");
		return appConfig;
	}
	
	
	private static boolean createBaseDirectory(ScheduleDetails appConfig,
			MessageSource configProperties) {
		String directory = "";
		String resultsBaseDirectory = "";
		boolean status = true;
		try {
			try{
				resultsBaseDirectory = getResultsBaseDirectory(appConfig,
					configProperties);
			}catch(Exception e){
				appConfig.setError("Invalid Results Directory.");
				status = false;
				
				return status;
			}

			appConfig.setResultsBaseDirectory(resultsBaseDirectory);

			directory = resultsBaseDirectory + File.separator
					+ appConfig.getApplicationName() + "_" + appConfig.getEnvironmentName()+"_" + appConfig.getExecutionTimeStamp() + File.separator
					+ appConfig.getBrowserType();
			String directoryWithSlash = resultsBaseDirectory + "\\"
					+ appConfig.getApplicationName() + "_" + appConfig.getEnvironmentName()+"_" + appConfig.getExecutionTimeStamp() + "\\"
					+ appConfig.getBrowserType();

			if (!appConfig.isPublicSite()) {
				directory += File.separator + appConfig.getLoginId();
				directoryWithSlash += "\\" + appConfig.getLoginId();
			}else {
				directory += File.separator + appConfig.getApplicationName();
				directoryWithSlash += "\\" + appConfig.getApplicationName();
			}

			appConfig.setSnapShotPath(directoryWithSlash);
			appConfig.setDirectory(directory);
			createDirectory(directory);
		} catch (Exception e) {
			appConfig.setError(appConfig.getTestScheduleId()
					+ " :Error while creating directory. Directory path :"
					+ directory);
			status = false;
		}
		
		return status;

	}

	private static void createDirectory(String directory) throws Exception {
		FileDirectoryUtil.createDirectories(directory);
	}

	private static String getResultsBaseDirectory(ScheduleDetails appConfig,
			MessageSource configProperties) throws Exception{

		String resultsBaseDirectory = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.crawler.resultsBaseDirectory");
		
		if ( StringUtils.isEmpty(resultsBaseDirectory) ){
			throw new Exception("Invalid Results Directory");
		}
		resultsBaseDirectory = FileDirectoryUtil.getAbsolutePath(
				resultsBaseDirectory,
				FileDirectoryUtil.getMintRootDirectory(configProperties));

		return resultsBaseDirectory;
	}

	private static boolean doHarReportsSetup(ScheduleDetails appConfig,
			MessageSource configProperties) throws Exception{
		boolean status = true;
		
		try{
			String harReportsDirectory = "";
			harReportsDirectory = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.crawler.harReportsDirectory");
	
			String harReportsPath = appConfig.getResultsBaseDirectory()
					+ File.separator + appConfig.getApplicationName() + "_" + appConfig.getEnvironmentName()+"_" + appConfig.getExecutionTimeStamp() + File.separator
					+ appConfig.getBrowserType() + File.separator
					+ harReportsDirectory + File.separator
					+ appConfig.getApplicationName();
			String harDirectoryWithSlash = appConfig.getResultsBaseDirectory()
					+ "\\" + appConfig.getApplicationName() + "_" + appConfig.getEnvironmentName()+"_" + appConfig.getExecutionTimeStamp() + "\\" + appConfig.getBrowserType()
					+ "\\" + harReportsDirectory + "\\"
					+ appConfig.getApplicationName();
	
			if (!appConfig.isPublicSite()) {
				harReportsPath += File.separator + appConfig.getLoginId();
				harDirectoryWithSlash += "\\" + appConfig.getLoginId();
			}
			FileDirectoryUtil.createDirectories(harReportsPath);
			
			FileDirectoryUtil.createDirectories(harReportsPath + File.separator + Constants.HAR_MAPPED_FILE_PATH);
	
			appConfig.setHarReportsPath(harDirectoryWithSlash);
		}catch(Exception e){
			appConfig.setError(appConfig.getTestScheduleId()
					+ " :Error while creating HarReportsDirectory.");
			status = false;
		}
		return status;
	}
	
	private static boolean doReportsSetup(ScheduleDetails appConfig,
			MessageSource configProperties) throws Exception{
		boolean status = true;
		
		try{
			String reportsDirectory = "";
			reportsDirectory = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.crawler.reportsDirectory");
	
			String reportsPath = appConfig.getResultsBaseDirectory()
					+ File.separator + appConfig.getApplicationName() + "_" + appConfig.getEnvironmentName()+"_" + appConfig.getExecutionTimeStamp() + File.separator
					+ appConfig.getBrowserType() + File.separator
					+ reportsDirectory;
			String reportsDirectoryWithSlash = appConfig.getResultsBaseDirectory()
					+ "\\" + appConfig.getApplicationName() + "_" + appConfig.getEnvironmentName()+"_" + appConfig.getExecutionTimeStamp() + "\\" + appConfig.getBrowserType()
					+ "\\" + reportsDirectory;

			FileDirectoryUtil.createDirectories(reportsPath);
	
			FileDirectoryUtil.createDirectories(reportsPath + File.separator + CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.crawler.imageTextDirectory"));
			appConfig.setReportsPath(reportsDirectoryWithSlash);
		}catch(Exception e){
			appConfig.setError(appConfig.getTestScheduleId()
					+ " :Error while creating Reprots Directory.");
			status = false;
		}
		return status;
	}
	
	private static boolean createSerializeobjectsDirectory(ScheduleDetails appConfig,
			MessageSource configProperties, String executionTimeStamp) throws Exception{
		boolean status = true;
		
		try{
			String serializeObjectDirectory = "";
			serializeObjectDirectory = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.crawler.serializeObjectsDirectory");
	
			String serializeObjectDirectoryPath = appConfig.getResultsBaseDirectory()
					+ File.separator + appConfig.getApplicationName() + "_" + appConfig.getEnvironmentName()+"_"+executionTimeStamp + File.separator
					+ serializeObjectDirectory;
			String serializeObjectDirectoryPathWithSlash = appConfig.getResultsBaseDirectory()
					+ "\\" + appConfig.getApplicationName() + "_" + appConfig.getEnvironmentName()+"_"+executionTimeStamp + "\\" + serializeObjectDirectory;
	
			FileDirectoryUtil.createDirectories(serializeObjectDirectoryPath);
	
			appConfig.setSerializePath(serializeObjectDirectoryPathWithSlash);
		}catch(Exception e){
			appConfig.setError("Error while creating Serialize directory.");
			status = false;
		}
		return status;
	}
	
	public static List<String> getErrorPageIdentifiers(MessageSource configProperties) {
		String errorPageString = "";
		List<String> errorPageIdentifiers = new ArrayList<String>();
		try {
			errorPageString = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.errorpage");
		} catch (Exception e) {
			logger.error("mint.errorpage not found in config.properties.");
		}
		try{
			if (null != errorPageString && errorPageString.trim().length() > 0) {
				String[] errorPageStringSplit = errorPageString.split("\\|");
	
				logger.info("errorPageStringSplit length :"
						+ errorPageStringSplit.length);
	
				for (String temp : errorPageStringSplit) {
					errorPageIdentifiers.add(temp);
				}
			}
		}catch(Exception e){
			
		}

		logger.info("errorPageIdentifiers :" + errorPageIdentifiers);
		return errorPageIdentifiers;
	}

	public static CrawlConfig readCrawlConfig(ScheduleDetails appConfig) {
		CrawlConfig crawlConfig = null;
		try{
			
			JsonReaderWriter<CrawlConfig> jsonReaderWriter = new JsonReaderWriter<CrawlConfig>();
			crawlConfig = jsonReaderWriter.readJsonObjectFromFile(new CrawlConfig(), appConfig.getCrawlStatusDirectory()+ File.separator + UiTestingConstants.CRAWL_CONFIG);
		}catch(Exception e){
			logger.warn(UiTestingConstants.CRAWL_CONFIG + ", is not available yet. File path :"+appConfig.getCrawlStatusDirectory()+ File.separator + UiTestingConstants.CRAWL_CONFIG);
		}
		return crawlConfig;
	}
}
