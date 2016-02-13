package com.ensense.insense.core.crawler.model.executer.browser;

import com.ensense.insense.data.common.model.ScheduleDetails;
import com.ensense.insense.core.analytics.utils.BrowserDriverLoaderUtil;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.springframework.context.MessageSource;

import java.util.Iterator;
import java.util.Set;


public class BrowserUtils {
	public static WebDriver getWebDriverForInitialLogin(String browserType){
		BrowserDriverLoaderUtil driverUtil = new BrowserDriverLoaderUtil();
		WebDriver driver = null;

		if ( browserType.equals("firefox") ){
			driver = driverUtil.getFirefoxDriver();
		}
		
		return driver;
	}
	
	public static WebDriver getWebDriverForCrawling(ScheduleDetails appConfig, MessageSource configProperties) throws Exception{
		BrowserDriverLoaderUtil driverUtil = new BrowserDriverLoaderUtil();
		WebDriver driver = null;

		if ( appConfig.getBrowserType().equals("firefox") ){
			driver = driverUtil.getFirefoxDriverWithHar(appConfig);//(firefoxPath, firebug, netExport, harReportsPath, enableAkamaiTesting, proxyClient, seleniumFirefoxPort, mobProxyHost, mobProxyPort, appConfig);
		}else if ( appConfig.getBrowserType().equals("chrome") ){
			driver = driverUtil.getChromeDriver(appConfig, configProperties);
		} else if(appConfig.getBrowserType().equals("ie") ){
			driver = driverUtil.getIEDriver(appConfig, configProperties);
		} else if(appConfig.getBrowserType().equals("safari") ){
			driver = driverUtil.getSafariDriver();
		} 
		deleteCookies(driver);
		return driver;
	}
	
	public static void deleteCookies(WebDriver driver) {
        Set<Cookie> cookies = driver.manage().getCookies();
        driver.manage().deleteAllCookies();
        if (!cookies.isEmpty()) {
            System.out.println(cookies.size());
            System.out.println(cookies.toString());
            Iterator<Cookie> iter= driver.manage().getCookies().iterator();
            while(iter.hasNext()){
                Cookie C = iter.next();
                System.out.println(C.getName()+"\n" + C.getPath()+"\n"+ C.getDomain()+"\n"+C.getValue()+"\n"+C.getExpiry());
            }
            cookies.clear();
            System.out.println(cookies.size());
            System.out.println(cookies.toString());
        }
    }
}
