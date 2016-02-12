package com.ensense.insense.core.crawler.model.executer;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class IFALoginNew {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://origin-publictools-pf.test.tiaa-cref.org";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testIFALoginNew() throws Exception {
    driver.get(baseUrl + "/private/selfservices/sso/login.do");
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (driver.findElement(By.id("userId")) != null ) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }

    driver.findElement(By.id("userId")).clear();
    driver.findElement(By.id("userId")).sendKeys("CLS1inves");
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if ("".equals(driver.findElement(By.id("loginbuttonnew")).getText())) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }

    driver.findElement(By.id("loginbuttonnew")).click();
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if ("".equals(driver.findElement(By.id("password")).getText())) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }

    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("1234567aA");
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.id("securityQuestionAnswer"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }

    driver.findElement(By.id("securityQuestionAnswer")).clear();
    driver.findElement(By.id("securityQuestionAnswer")).sendKeys("tiaa");
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if ("".equals(driver.findElement(By.id("loginsmallbtn")).getText())) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }

    driver.findElement(By.id("loginsmallbtn")).click();
    for (int second = 0;; second++) {
    	if (second >= 60) fail("timeout");
    	try { if (isElementPresent(By.xpath("//td[4]"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }

    driver.findElement(By.xpath("//td[4]")).click();
    // ERROR: Caught exception [ERROR: Unsupported command [selectFrame | bu=https%3A%2F%2Fjbmd.tiaa-cref.org%2F858640&e=https%3A%2F%2Forigin-publictools-pf.test.tiaa-cref.org&eu=https%3A%2F%2Forigin-publictools-pf.test.tiaa-cref.org%2Fprivate%2Fselfservices%2Fsso%2Flogin.do | ]]
    driver.findElement(By.id("mybutton")).click();
    driver.findElement(By.id("mybutton")).click();
    driver.findElement(By.id("t")).click();
  }

  @After
  public void tearDown() throws Exception {
    //driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
