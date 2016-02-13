package com.ensense.insense.core.crawler.model.executer;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

public class IWCAkamaiLogin extends TestCase{
	private StringBuffer verificationErrors = new StringBuffer();
	
	@Before
	public void setUp() throws Exception {

	}
	
	@Test
	public void testIWCAkamaiLogin() throws Exception {
		driver.get(url);
		driver.findElement(By.id("userId")).clear();
		driver.findElement(By.id("userId")).sendKeys(userId);
		driver.findElement(By.xpath("//div[2]/input")).click();
		driver.findElement(By.id("password")).sendKeys(passwd);
		driver.findElement(By.xpath("//a[2]/span")).click();
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
