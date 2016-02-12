package com.ensense.insense.core.crawler.model.executer;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

public class IWCRSALoginProdFix extends TestCase{
  private StringBuffer verificationErrors = new StringBuffer();

  @Before
  public void setUp() throws Exception {
    
  }

  @Test
  public void testIWCRSALoginProdFix() throws Exception {
    driver.get(url);
    // ERROR: Caught exception [ERROR: Unsupported command [selectFrame | bu=https%3A%2F%2Fjbmd.tiaa-cref.org%2F858640&e=https%3A%2F%2Forigin-publictools-pf.test.tiaa-cref.org&eu=https%3A%2F%2Forigin-publictools-pf.test.tiaa-cref.org%2Fprivate%2Fselfservices%2Fsso%2Flogin.do | ]]
    driver.manage().timeouts().implicitlyWait(Long.parseLong("30"), TimeUnit.SECONDS);

    driver.findElement(By.id("userId")).clear();
    driver.findElement(By.id("userId")).sendKeys(userId);
    driver.findElement(By.id("loginbuttonnew")).click();
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys(passwd);
    driver.findElement(By.xpath("//a[2]/span")).click();
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
