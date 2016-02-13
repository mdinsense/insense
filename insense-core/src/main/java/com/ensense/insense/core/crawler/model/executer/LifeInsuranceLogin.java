package com.ensense.insense.core.crawler.model.executer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

public class LifeInsuranceLogin extends TestCase{
  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testLifeInsuranceLogin() throws Exception {
    driver.get(url);
    driver.findElement(By.id("userId")).clear();
    driver.findElement(By.id("userId")).sendKeys(userId);
    driver.findElement(By.cssSelector("a.btn.btnSmall > span")).click();
    driver.findElement(By.id("password1")).clear();
    driver.findElement(By.id("password1")).sendKeys(passwd);
    driver.findElement(By.id("securityId")).clear();
    driver.findElement(By.id("securityId")).sendKeys(securityQuestion);
    driver.findElement(By.cssSelector("#loginBtn > span")).click();
  }

  @After
  public void tearDown() throws Exception {
  }

}

