package com.ensense.insense.core.crawler.model.executer;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

public class LoginDR extends TestCase {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testLoginDR() throws Exception {
		System.out.println("testLoginDR, userId :" + userId);
		driver.manage().timeouts()
				.implicitlyWait(Long.parseLong("30"), TimeUnit.SECONDS);
		doDRSetup();

		driver.get(url);
		driver.findElement(By.id("userId")).clear();
		driver.findElement(By.id("userId")).sendKeys(userId);
		driver.findElement(By.id("loginbuttonnew")).click();
		driver.manage().timeouts()
				.implicitlyWait(Long.parseLong("30"), TimeUnit.SECONDS);
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(passwd);
		driver.findElement(By.name("securityQuestionAnswer")).clear();
		driver.findElement(By.name("securityQuestionAnswer")).sendKeys(
				securityQuestion);
		// driver.findElement(By.id("loginsmallbtn")).click();
		driver.findElement(
				By.cssSelector("input[type='submit'][value='Log In']")).click();
		driver.manage().timeouts()
				.implicitlyWait(Long.parseLong("0"), TimeUnit.SECONDS);
	}

	@After
	public void tearDown() throws Exception {

	}
}
