package com.ensense.insense.core.crawler.model.executer;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;

public class PublicBankingLogin extends TestCase{
  private String baseUrl = "http://denitb3tccsm01.test.tiaa-cref.org:8080/";
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testPublicBankingLogin() throws Exception {
    driver.get(baseUrl + "/cs/ContentServer?d=&pagename=TCPublic%2FSetDomainNameInCookie");
    new Select(driver.findElement(By.name("domainName"))).selectByVisibleText("Banking");
    driver.findElement(By.cssSelector("input[type=\"button\"]")).click();
    driver.get(url);
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
