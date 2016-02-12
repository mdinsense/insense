package com.ensense.insense.core.crawler.model.executer;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

public class Accounthomeiwc {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "https://origin-publictools-pf.test.tiaa-cref.org";
    driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
  }

  @Test
  public void testAccounthomeiwc() throws Exception {
    driver.get(baseUrl + "/private/selfservices/sso/login.do?");
    for (int second = 0;; second++) {
    	if (second >= 60) {
    		throw new Exception("Unable to find userId element");
    	}
    	try { if (isElementPresent(By.id("userId"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }

    driver.findElement(By.id("userId")).clear();
    driver.findElement(By.id("userId")).sendKeys("maisabel");
    driver.findElement(By.id("loginbuttonnew")).click();
    for (int second = 0;; second++) {
    	if (second >= 60) {
    		throw new Exception("Unable to find password element");
    	}
    	try { if (isElementPresent(By.id("password"))) break; } catch (Exception e) {}
    	Thread.sleep(1000);
    }

    driver.findElement(By.id("password")).sendKeys("1234567cC");
    driver.findElement(By.cssSelector("#continue > span")).click();
    
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
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
