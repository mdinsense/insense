package com.ensense.insense.core.crawler.model.executer;

import com.ensense.insense.data.miscellaneous.entity.ClearCache;
import com.ensense.insense.data.utils.BrowserDriverLoaderUtil;
import com.ensense.insense.services.common.utils.CommonUtils;
import com.ensense.insense.services.common.utils.FileDirectoryUtil;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.springframework.context.MessageSource;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class ClearCacheTestCaseExecutor {

	private static final Logger logger = Logger
			.getLogger(ClearCacheTestCaseExecutor.class);

	private MessageSource messageSource;
	private EventFiringWebDriver driver;
	private WebDriverTransactionListener eventListener;

	public ClearCacheTestCaseExecutor(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	private WebDriver getWebDriver(String browserType,
			MessageSource configProperties) throws Exception {
		BrowserDriverLoaderUtil driverUtil = new BrowserDriverLoaderUtil();
		String firebug = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.crawler.firebug");
		firebug = FileDirectoryUtil.getAbsolutePath(firebug,
				FileDirectoryUtil.getMintRootDirectory(configProperties));
		String netExport = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.crawler.netExport.full");
		netExport = FileDirectoryUtil.getAbsolutePath(netExport,
				FileDirectoryUtil.getMintRootDirectory(configProperties));
		WebDriver driver;
		driver = driverUtil.getFirefoxDriver();
		return driver;
	}

	public boolean executeCacheClearJoB(ClearCache cacheJob,
			MessageSource configProperties, MessageSource messageSource) {
		String resultsBaseDirectory = "";
		try {
			resultsBaseDirectory = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.crawler.resultsBaseDirectory");
			resultsBaseDirectory = FileDirectoryUtil.getAbsolutePath(
					resultsBaseDirectory,
					FileDirectoryUtil.getMintRootDirectory(configProperties));
			String browserType = CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.crawler.browserType");
			return this.doClearCacheExcecution(browserType,
					resultsBaseDirectory, cacheJob, configProperties);
		} catch (Exception e) {
			logger.error("Exception while execution of clear cache job page :"
					+ e);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
			return false;
		}
	}
	
	private boolean doClearCacheExcecution(String browserType,
			String directory, ClearCache cacheJob,
			MessageSource configProperties) throws Exception {
		logger.debug("Loading webdriver");
		WebDriver webDriver = new FirefoxDriver();
		driver = new EventFiringWebDriver(webDriver);
		try {
			driver = getStartUrl(driver, cacheJob);
			driver.close();
		} catch (Exception e) {
			logger.error("Exception while execution of clear cache job page :"
					+ e);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean doTransactionTestcaseFileExcecution(String browserType,
			String directory, ClearCache cacheJob,
			MessageSource configProperties) throws Exception {
		logger.debug("Loading webdriver");
		WebDriver webDriver = getWebDriver(browserType, configProperties);
		driver = addListenrer(webDriver, directory, configProperties);
		try {
			driver = getStartUrl(driver, cacheJob);

		} catch (Exception e) {
			logger.error("Exception while execution of clear cache job page :"
					+ e);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private EventFiringWebDriver getStartUrl(EventFiringWebDriver driver,
			ClearCache cacheJob) throws Exception {
		Class<?> aClass = null;
		try {
			logger.debug("Initializing testcase details begins");
			String[] filePathSplit = cacheJob.getScriptPath().split("\\\\com");
			String fileDirectory = filePathSplit[0];
			String[] packageAndClassName = filePathSplit[1].split("\\.");
			String className = "com"
					+ packageAndClassName[0].replaceAll("\\\\", ".");
			File file = new File(fileDirectory);
			URL url = file.toURI().toURL();
			URL[] urls = new URL[] { url };
			ClassLoader cl = new URLClassLoader(urls, this.getClass()
					.getClassLoader());
			aClass = Class.forName(className, true, cl);
			Object obj = aClass.newInstance();
			Method[] methods = aClass.getMethods();
			System.out.println(methods[4]);
			Method method = aClass.getMethod("initialSetUpDriver",
					EventFiringWebDriver.class);
			method.invoke(obj, driver);
			method = aClass.getMethod("initialSetUp",
					EventFiringWebDriver.class, String.class, String.class,
					String.class, String.class);
			String fileFullPath = cacheJob.getScriptPath();
			String methodName = fileFullPath.substring(
					fileFullPath.lastIndexOf("\\") + 1,
					fileFullPath.lastIndexOf("."));
			String firstChar = methodName;
			/*
			 * Below string store Method name excluding the Last four char, if
			 * the method name contains "test" at the last
			 */
			String classLastName = methodName.substring(
					methodName.length() - 4, methodName.length());
			if (classLastName.equalsIgnoreCase("test")) {
				methodName = methodName.substring(0, methodName.length() - 4);
			}
			/*
			 * Below boolean value returns true if the class name has the "test"
			 * in the beginning of the class name
			 */
			boolean classFirstName = methodName.regionMatches(true, 0, "test",
					0, 3);
			if (!classFirstName) {
				// This condition executes class name does not contains "test".
				methodName = "test" + firstChar;
			} else {
				String fullMethodName = methodName.substring(0, 1);
				// Below condition executes if the class name contains "T" in
				// the beginning
				if (fullMethodName.equals("T")) {
					methodName = firstChar.toLowerCase()
							+ methodName.substring(1, methodName.length());
				}
			}
			logger.debug("test method execution begins :"+methodName);
			method = aClass.getMethod(methodName);
			method.invoke(obj);
			logger.debug("Test case execution Completed");

		} catch (ClassNotFoundException e) {
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
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}

		return driver;
	}

	private EventFiringWebDriver addListenrer(WebDriver webDriver,
			String resultsDirectory, MessageSource configProperties) {
		EventFiringWebDriver driver = new EventFiringWebDriver(webDriver);
		eventListener = new WebDriverTransactionListener(webDriver,
				resultsDirectory, configProperties);
		driver = driver.register(eventListener);

		return driver;
	}
}
