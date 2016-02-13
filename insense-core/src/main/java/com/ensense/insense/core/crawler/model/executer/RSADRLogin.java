package com.ensense.insense.core.crawler.model.executer;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

public class RSADRLogin extends TestCase {
	private StringBuffer verificationErrors = new StringBuffer();
	//private static final String DR_URL = "http://docit.ss.tiaa-cref.org/Standards%20and%20Procedures/OCC/healthcheck/HAWC-healthCheckBFLD.html";

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testRSADRLogin() throws Exception {
		System.out.println("testRSADRLogin, userId :" + userId);
		driver.manage().timeouts()
				.implicitlyWait(Long.parseLong("30"), TimeUnit.SECONDS);
		doDRSetup();
		
		driver.get(url);

		driver.findElement(By.id("userId")).clear();
		driver.findElement(By.id("userId")).sendKeys(userId);
		driver.findElement(By.id("loginbuttonnew")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(passwd);
		driver.findElement(By.cssSelector("#loginButton > span")).click();
		driver.manage().timeouts()
				.implicitlyWait(Long.parseLong("0"), TimeUnit.SECONDS);
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
