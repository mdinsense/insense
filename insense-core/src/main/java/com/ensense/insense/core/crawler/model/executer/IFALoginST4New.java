package com.ensense.insense.core.crawler.model.executer;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;

public class IFALoginST4New extends TestCase {

  private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
	}

  @Test
  public void testIFALoginST4New() throws Exception {
    driver.get(url);
	driver.manage().timeouts()
				.implicitlyWait(Long.parseLong("30"), TimeUnit.SECONDS);
    driver.findElement(By.id("userId")).clear();
    driver.findElement(By.id("userId")).sendKeys(userId);
	
    driver.findElement(By.xpath("//div[4]/a/span")).click();
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys(passwd);
    driver.findElement(By.id("securityQuestionAnswer")).clear();
    driver.findElement(By.id("securityQuestionAnswer")).sendKeys(securityQuestion);
    driver.findElement(By.id("loginsmallbtn")).click();
	
	driver.manage().timeouts()
				.implicitlyWait(Long.parseLong("0"), TimeUnit.SECONDS);
	for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ( driver.findElement(
						By.xpath("//div[3]/div/div[2]/table/tbody/tr[3]/td")) != null )
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
	}
	
	driver.findElement(
				By.xpath("//div[3]/div/div[2]/table/tbody/tr[3]/td"))
				.click();
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

      return "";
    } finally {

    }
  }
}
