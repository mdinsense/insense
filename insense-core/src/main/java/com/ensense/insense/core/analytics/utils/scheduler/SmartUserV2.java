package com.ensense.insense.data.utils.scheduler;

import static org.junit.Assert.fail;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class SmartUserV2 {
	private static final Logger logger = Logger.getLogger(SmartUserV2.class);
	protected String batchID;
	protected String parentWindowHandle;
	private String threadName;
	// releated to Crawler
	protected Set<String> alreadyVisited = new java.util.HashSet<String>();;
	protected Set<String> abnormalUrl = new java.util.HashSet<String>();;
	protected Set<String> alreadyVisitedFromLog = new java.util.HashSet<String>();;

	protected List<WebElement> linksOnPageList = new LinkedList<WebElement>();

	protected java.util.Queue<String> queue = new LinkedList<String>();
	protected java.util.Stack<String> stack = new java.util.Stack<String>();

	protected WebDriver driver;

	// related to Smart user
	// private final String JQUERY_LOAD_SCRIPT =
	// "C:\\projects\\PortalCommonsV8A\\mintTools\\dist\\js\\dynamic-jquery.js";
	private final String JQUERY_START_FN_SNIPPET = "jQuery(function($) { ";
	private final String JQUERY_END_FN_SNIPPET = " }); ";
	private final String JQUERY_ONCLICK_PRVNT_DFLT = " $('a').click(function(event){console.log('mahesh :'+$(this).attr('href'));event.preventDefault();return false;}); console.log('a'+$(this).attr('href'));";
	private final String JQUERY_ASSIGN_ELE_ID = "var i=0;$('a').each(function(){ console.log(++i);   if(!$(this).attr('id')) {$(this).attr('id', i);} });";
	// private final String JQUERY_TRIGGER_CLICK =
	// "$('#"+link.getAttribute("id")+"').trigger('click');" ;
	private String jqueryFilePath;

	public SmartUserV2() {
		jqueryFilePath = "";
	}

	public SmartUserV2(String jqueryFilePath, String threadName) {
		this.jqueryFilePath = jqueryFilePath;
		this.threadName = threadName;
	}

	public List<WebElement> processUrl(WebDriver driver, String url)
			throws Exception {
		logger.info(threadName+", Entry: processUrl, url->"+url);
		// Url is already opened by the crawler, hence commenting driver.get
		// driver.get(url);

		File f = new File(jqueryFilePath);
		
		//logger.info("Jquery file path :"+f.getAbsolutePath());
		
		if (null != jqueryFilePath && jqueryFilePath.trim().length() < 10) {
			logger.error("Invalid JQuery file path");
			throw new Exception("Invalid JQuery file path :"+jqueryFilePath);
		}

		//logger.info("Before jquery file read.");
		String jQueryLoader = readFile(jqueryFilePath);
		//logger.info("After jquery file read.");
		// give jQuery time to load asynchronously
		driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
		//logger.info("Before js");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeAsyncScript(jQueryLoader /*
											 * ,
											 * http://localhost:8080/jquery-1.7
											 * .2.js
											 */);
		//logger.info("After asyncscript");
		try {
			//logger.info("Before executeScript.");
			js.executeScript(
			// "jQuery(function($) { " +
			JQUERY_START_FN_SNIPPET
					+

					" $('a').click(function(event){/*console.log('mahesh :'+$(this).attr('href'));*/ event.preventDefault();return false;}); /*console.log('a'+$(this).attr('href'));*/"
					+

					// " }); "
					JQUERY_END_FN_SNIPPET

			);
			//logger.info("After executeScript.");
		} catch (Exception e) {
			logger.error(threadName+", Exception while executing the Javascript in smartuser2."+e );
			logger.error(threadName+", Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}

		// checkAlert(driver);

		// assign id to each element which does not have an id

		try {
			//logger.info("Before execute script.");
			js.executeScript(
			// "jQuery(function($) { " +
			JQUERY_START_FN_SNIPPET
					+

					"var i=0;$('a').each(function(){ ++i;/*console.log(++i); */  if(!$(this).attr('id')) {$(this).attr('id', i);} });"
					+

					// " }); "
					JQUERY_END_FN_SNIPPET

			);
		} catch (Exception e) {
			logger.info("Exception in :"+threadName + " ,exception ->"+threadName);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}

		List<WebElement> Linklist = getAllElements(driver, "a");// driver.findElements(By.tagName("a"));
		logger.info(threadName+", LinkList size :" + Linklist.size());


			int i = 0;
			for (WebElement link : Linklist) {
				try {
					//logger.info("link :"+link.toString());
					//logger.info("Href :"+link.getAttribute("href"));
					i++;
					
					if (null != link && link.isDisplayed() && link.isEnabled() && !urlContainsSkipPattern(link)) {
						
	
						// if the link that is getting clicked is already in teh
						// address bar, dont click it.. this may be causing firefox
						// to crashing
	
						String clickableLink = link.getAttribute("href");
						String onClickValue = link.getAttribute("OnClick");
						String linkText = link.getText();
						String linkTextContent = link.getAttribute("textContent");
	
						//logger.info("clickableLink :"+clickableLink);
						//logger.info("onClickValue :"+onClickValue);
						//logger.info("linkText :"+linkText);
						//logger.info("linkTextContent :"+linkTextContent);
						
						// populateQueueWithValidUrls(clickableLink);
						populateStackWithValidUrls(clickableLink);
						// boolean doNotClick = url.endsWith(clickableLink);
	
						/*
						 * if(doNotClick){ System.out.println("url :"+ url);
						 * System.out.println("doNotClick :" + doNotClick +
						 * " ;"+clickableLink); }
						 */
						// if(!clickableLink.toLowerCase().contains("print")||
						// !clickableLink.toLowerCase().contains("log") ||
						// !clickableLink.toLowerCase().contains("logout") ||
						// !clickableLink.toLowerCase().contains("login"))// &&
						// !doNotClick ){
	
						// if(!clickableLink.contains("logout"))// &&
						// !clickableLink.contains("_pageLabel"))
						{
							{
								//logger.info("clickableLink :"+i +
								//": "+linkText+": " + linkTextContent +": "
								//+clickableLink);
								//logger.info("onClick Value :" +
								//onClickValue);
								/*if (onClickValue.contains("linkName=")) {
									logger.info("Link Name :"
											+ linkTextContent);
									logger.info("Link Tag Value :"
											+ onClickValue);
									logger.info("Link URL :" + clickableLink);
								}*/
	
								try {
									//logger.info("Before click :"+link.getAttribute("href"));
								boolean linkDisplayed = false;
								for (int second = 0;; second++) {
									if (second >= 3){
										break;
									}
									try {
										if (driver.findElement(
												By.id(link.getAttribute("id")))
												.isDisplayed()){
											linkDisplayed = true;
											break;
										}
									} catch (Exception e) {
									}
									Thread.sleep(1000);
								}
									
								if ( linkDisplayed ){
									js.executeScript(
									// "jQuery(function($) { " +
									JQUERY_START_FN_SNIPPET + "$('#"
											+ link.getAttribute("id")
											+ "').trigger('click');"
	
											// + " }); "
											+ JQUERY_END_FN_SNIPPET
	
									);
								}else {
									logger.error(threadName+", Link count :"+ i + ", is not visible. Unable to click the link with id :"+link.getAttribute("id"));
								}
	
								} catch (Exception e) {
									
									logger.error(threadName+", Exception in Smartuser. Exception->"+e);
									//e.printStackTrace();
									//return Linklist;
								}
								// }
							}
						}
	
						// close modal window here
	
						// close extra windows opened : happens when target=blank on
						// anchor
						//TODO, commented by dhinakar for testing with CrawlerThread. If focusing to parent is needed, need to modify restoreFocusOnParentWindow
						restoreFocusOnParentWindow(driver);
						
						// System.out.println("inc stack size :" + stack.size());
						// System.out.println("inc alreadyListed :" +
						// alreadyVisited.size());
					}// isDisplayed if block
				} catch (Exception e) {
					//logger.error("Exception in Smartuser. Exception ->"+e);
					//logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
					//return Linklist;
				}
			} // end for

			//logger.info("stack size :" + stack.size());
			//logger.info("alreadyListed :" + alreadyVisited.size());



		//logger.info("returning to crawler with elements size: "
				//+ Linklist.size());
		//logger.info("Date/time :" + DateTimeUtil.getCurrentDateTime());
		
		logger.info("Exit: processUrl");
		return Linklist;
	}

	private boolean urlContainsSkipPattern(WebElement link) {
		String url = link.getAttribute("href");
/*logger.info("Link url :"+url);
logger.info("Link tagname :"+link.getTagName());
logger.info("Link Text :"+link.getText());
logger.info("Link url :"+url);
logger.info("Link class :"+link.getAttribute("class"));
*/
		try{
			url = url.toLowerCase();
		}catch(Exception e){
			url = "";
		}
		String linkText = "";
		try{
			linkText = link.getText();
			linkText = linkText.toLowerCase();
		}catch(Exception e){
			linkText = "";
		}
		String linkTitle = "";
		
		try{
			linkTitle = link.getAttribute("title");
			linkTitle = linkTitle.toLowerCase();
		}catch(Exception e){
			linkTitle = "";
		}
		
		try{
			if ( StringUtils.isNotBlank(url) ){
				if ( url.contains("logout") || url.contains("print()") || url.contains("javascript:void") || StringUtils.equals(url, "#") || StringUtils.equalsIgnoreCase(linkText, "Back to Prior Site")
						|| url.contains("#emailpage") || url.contains("#printpage") || url.contains("mailto:") || url.contains("javascript:") || url.contains("#share-menu") || url.contains("logout") || url.contains("log out")
						|| url.contains("email") || url.contains("print") || StringUtils.containsIgnoreCase(url, "reloadHawc=") || StringUtils.containsIgnoreCase(url, "printclientlist")
						|| linkTitle.contains("print") || linkTitle.contains("logout") || linkTitle.contains("logout") || linkText.contains("log out") ){
					return true;
				}
			}
		}catch(Exception e){
			
		}
		return false;
	}

	public void populateQueueWithValidUrls(String href) {
		System.out.println("populateQeueue");
		if (isAddressValid(href)) {
			if (!alreadyVisited.contains(href)) {
				alreadyVisited.add(href);
				// String subHref= href.substring(0, href.indexOf("#"));
				queue.add(href);
				// System.out.println("queue size :" + queue.size());
			}
		}
	}

	public void populateStackWithValidUrls(String href) {
		// System.out.println("populateStack");
		if (isAddressValid(href)) {
			if (!alreadyVisited.contains(href)) {
				alreadyVisited.add(href);
				// String subHref= href.substring(0, href.indexOf("#"));
				stack.push(href);
				// System.out.println("stack size :" + stack.size());
			}
		}
	}

	public void restoreFocusOnParentWindow(WebDriver driver) {
		// restore focus on parent window
		// close windows that opened

		// String
		try{
			parentWindowHandle = driver.getWindowHandle();
			String popWindowHandle;
	
			Set windowHandles = driver.getWindowHandles();
	
			Iterator it = windowHandles.iterator();
			while (it.hasNext()) {
	
				popWindowHandle = it.next().toString();
				
				if (!popWindowHandle.equals(parentWindowHandle)) {
					driver.switchTo().window(popWindowHandle).close();
					windowHandles.remove(popWindowHandle);
					driver.switchTo().window(parentWindowHandle);
				}
	
				if (!popWindowHandle.equals(parentWindowHandle)) {
					Robot r = new Robot();
					r.keyPress(KeyEvent.VK_ESCAPE);
					r.keyRelease(KeyEvent.VK_ESCAPE);
				}
			}
		}catch(Exception e){
			
		}

	}

	// http://stackoverflow.com/questions/8244723/alert-handling-in-selenium-2-webdriver-with-java

	public boolean isAlertPresent(WebDriver driver) {
		try {
			driver.switchTo().alert();
			return true;
		}// try
		catch (Exception e) {
			return false;
		}// catch
	}

	public void handleAlert(WebDriver driver) {
		try{
			driver.switchTo().alert();
			driver.switchTo().alert().accept();
			driver.switchTo().defaultContent();
		}catch(Exception e){
			
		}
	}

	public void checkAlert(WebDriver driver) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 0);
			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} catch (Exception e) {
			// exception handling
		}
	}

	public void invokeESCRobot() {// robot will close any popup dialogs ex:
									// printer dialog
		try {
			Robot r = new Robot();

			r.keyPress(KeyEvent.VK_RIGHT);
			r.keyRelease(KeyEvent.VK_RIGHT);

			r.keyPress(KeyEvent.VK_ENTER);
			// r.mousePress(InputEvent.BUTTON1_MASK);
			// r.mouseRelease(InputEvent.BUTTON1_MASK);
			// r.keyPress(KeyEvent.VK_ESCAPE);
			// r.keyRelease(KeyEvent.VK_ESCAPE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// selenium sometimes does not find an element due to race condition
	// this method keeps polling the element until it's no longer considered
	// stale
	protected List<WebElement> getAllElements(WebDriver driver,
			String elementName) {
		try {
			return driver.findElements(By.tagName("a"));
		} catch (StaleElementReferenceException e) {
			logger.error("Attempting to recover from StaleElementReferenceException ...");
			return getAllElements(driver, "a");
		}

	}

	// helper method
	private String readFile(String file) throws IOException {
		Charset cs = Charset.forName("UTF-8");
		FileInputStream stream = new FileInputStream(file);
		try {
			Reader reader = new BufferedReader(
					new InputStreamReader(stream, cs));
			StringBuilder builder = new StringBuilder();
			char[] buffer = new char[8192];
			int read;
			while ((read = reader.read(buffer, 0, buffer.length)) > 0) {
				builder.append(buffer, 0, read);
			}
			return builder.toString();
		} finally {
			stream.close();
		}
	}

	protected boolean isAddressValid(String href) {
		boolean isHrefValid = true;
		if (href != null) {
			isHrefValid =
			// href.startsWith("https://shared.tiaa-cref.org/private/participantretirementtransactions")
			// href.startsWith("http://www-mgmtweb-st4.test.tiaa-cref.org/public/brand-central")
			href.startsWith("http://tiaapublishing.ops.tiaa-cref.org/ciws/brand_central")
					&& !href.toLowerCase().contains("print")
					&& !href.contains("#")
			// && !href.toLowerCase().contains("logout")
			// && !href.toLowerCase().contains("Log")
			// && !href.toLowerCase().contains("error")
			// && !href.toLowerCase().contains("pop")//for popup
			// && !href.toLowerCase().contains("symbol") //for ticker
			// && !href.toLowerCase().contains("tick") //for ticker
			;

			// mahesh : make this dynamically populated
			// properties loaded at run time
			// smart loaded: like based on if there is redirect to error page,
			// skip the url sequence
			// like if the browser url does not change for 10 loops of crawler
			// -figure how
			// like the browser url does not match the url being opened..skip
			// the url pattern

		}
		// System.out.println("isHrefValid :" +isHrefValid + "  href="+href);
		return isHrefValid;
	}

}
