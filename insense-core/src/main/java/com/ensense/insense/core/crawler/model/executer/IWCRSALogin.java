package com.ensense.insense.core.crawler.model.executer;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

public class IWCRSALogin extends TestCase{


  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testIWCRSALogin() throws Exception {
    driver.get(url);
    driver.findElement(By.id("userId")).clear();
    driver.findElement(By.id("userId")).sendKeys(userId);
    driver.findElement(By.id("loginbuttonnew")).click();
    
    driver.manage().timeouts().implicitlyWait(Long.parseLong("30"), TimeUnit.SECONDS);
    
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys(passwd);
    driver.findElement(By.xpath("//a[2]/span")).click();
    driver.manage().timeouts().implicitlyWait(Long.parseLong("0"), TimeUnit.SECONDS);
  }

  @After
  public void tearDown() throws Exception {
    
  }
}
