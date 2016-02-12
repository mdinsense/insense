package com.ensense.insense.core.crawler.model.executer;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;

public class RSALogin extends TestCase{
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {

  }

  @Test
  public void testRSALogin() throws Exception {
	  System.out.println("userId :"+userId);
    driver.get(url);

    driver.manage().timeouts().implicitlyWait(Long.parseLong("30"), TimeUnit.SECONDS);
    driver.findElement(By.id("userId")).clear();
    driver.findElement(By.id("userId")).sendKeys(userId);
    driver.findElement(By.id("loginbuttonnew")).click();
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys(passwd);
    driver.findElement(By.cssSelector("#loginButton > span")).click();
    driver.manage().timeouts().implicitlyWait(Long.parseLong("0"), TimeUnit.SECONDS);
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

}
