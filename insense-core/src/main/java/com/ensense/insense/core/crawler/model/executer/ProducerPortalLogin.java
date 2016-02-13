package com.ensense.insense.core.crawler.model.executer;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

public class ProducerPortalLogin extends TestCase{

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testProducerPortalLogin() throws Exception {
    driver.get(url);
    driver.manage().timeouts().implicitlyWait(Long.parseLong("30"), TimeUnit.SECONDS);
    driver.findElement(By.id("userId")).clear();
    driver.findElement(By.id("userId")).sendKeys(userId);
    driver.findElement(By.cssSelector("a.btn.btnSmall > span")).click();
    driver.findElement(By.id("password1")).clear();
    driver.findElement(By.id("password1")).sendKeys(passwd);
    driver.findElement(By.id("securityId")).clear();
    driver.findElement(By.id("securityId")).sendKeys(securityQuestion);
    driver.findElement(By.cssSelector("#loginBtn > span")).click();
    driver.manage().timeouts().implicitlyWait(Long.parseLong("0"), TimeUnit.SECONDS);
  }

  @After
  public void tearDown() throws Exception {
  }
}
