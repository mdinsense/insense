package com.ensense.insense.core.crawler.model.executer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.fail;

public class IFALogin extends TestCase{
	private StringBuffer verificationErrors = new StringBuffer();
  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testIFALogin() throws Exception {
	  System.out.println("In IFA login");
		driver.get(url);
		
		driver.findElement(By.id("userId")).clear();
		driver.findElement(By.id("userId")).sendKeys(userId);
		driver.findElement(By.id("loginbuttonnew")).click();
		driver.manage().timeouts()
				.implicitlyWait(Long.parseLong("30"), TimeUnit.SECONDS);
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(passwd);
		driver.findElement(By.id("securityQuestionAnswer")).clear();
	    driver.findElement(By.id("securityQuestionAnswer")).sendKeys(securityQuestion);
		// driver.findElement(By.id("loginsmallbtn")).click();
	    try{
	    	driver.findElement(By.id("loginsmallbtn")).click();
	    }catch(Exception e){
	    	driver.findElement(By.cssSelector("input[type='submit'][value='Log In']")).click();
	    }
		driver.manage().timeouts()
				.implicitlyWait(Long.parseLong("0"), TimeUnit.SECONDS);
	
		for (int second = 0;; second++) {
	    	if (second >= 60) fail("timeout");
	    	try { if ("MIXED".equals(driver.findElement(By.xpath("//div[2]/table/tbody/tr[4]/td")).getText())) break; } catch (Exception e) {}
	    	Thread.sleep(1000);
	    }

	    driver.findElement(By.xpath("//div[2]/table/tbody/tr[4]/td")).click();

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
}
