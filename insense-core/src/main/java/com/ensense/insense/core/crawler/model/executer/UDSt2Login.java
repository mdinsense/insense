package com.ensense.insense.core.crawler.model.executer;

import java.util.Iterator;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

public class UDSt2Login extends TestCase{
  
  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testUDSt2Login() throws Exception {
	 deleteCookies(driver);
    driver.get(url);
    driver.findElement(By.id("ContentPlaceHolder1_Login1_UserName")).clear();
    driver.findElement(By.id("ContentPlaceHolder1_Login1_UserName")).sendKeys(userId);
    driver.findElement(By.id("ContentPlaceHolder1_Login1_Password")).clear();
    driver.findElement(By.id("ContentPlaceHolder1_Login1_Password")).sendKeys(passwd);
    driver.findElement(By.id("ContentPlaceHolder1_Login1_LoginButton")).click();
    //driver.get("https://ud-st2.test.tiaa-cref.org/ud/enrollment/7012534");
    deleteCookies(driver);
    driver.get("https://ud-st2.test.tiaa-cref.org/ud/participant/3536170");
  }

  @After
  public void tearDown() throws Exception {
  }

	public void deleteCookies(WebDriver driver) {
        Set<Cookie> cookies = driver.manage().getCookies();
        driver.manage().deleteAllCookies();
        if (!cookies.isEmpty()) {
            System.out.println(cookies.size());
            System.out.println(cookies.toString());
            Iterator<Cookie> iter= driver.manage().getCookies().iterator();
            while(iter.hasNext()){
                Cookie C = iter.next();
                System.out.println(C.getName()+"\n" + C.getPath()+"\n"+ C.getDomain()+"\n"+C.getValue()+"\n"+C.getExpiry());
            }
            cookies.clear();
            System.out.println(cookies.size());
            System.out.println(cookies.toString());
        }
    }
}
