package com.ensense.insense.core.crawler.model.executer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

public class UDProdfixLogin extends TestCase{
  
  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testUDProdfixLogin() throws Exception {
    driver.get(url);
    driver.findElement(By.id("ContentPlaceHolder1_Login1_UserName")).clear();
    driver.findElement(By.id("ContentPlaceHolder1_Login1_UserName")).sendKeys(userId);
    driver.findElement(By.id("ContentPlaceHolder1_Login1_Password")).clear();
    driver.findElement(By.id("ContentPlaceHolder1_Login1_Password")).sendKeys(passwd);
    driver.findElement(By.id("ContentPlaceHolder1_Login1_LoginButton")).click();
    //driver.get("https://ud-pf.test.tiaa-cref.org/ud/participant/");
    driver.get("https://ud-pf.test.tiaa-cref.org/ud/participant/3736901");
  }

  @After
  public void tearDown() throws Exception {
  }

}
