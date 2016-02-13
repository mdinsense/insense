package com.ensense.insense.core.crawler.model.executer;

import static org.junit.Assert.fail;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

public class TestCase {

	protected EventFiringWebDriver driver;
	protected String url;
	protected String userId;
	protected String passwd;
	protected String securityQuestion;
	protected String filePath;
	protected String fileName;
	private static final String DR_URL = "http://docit.ss.tiaa-cref.org/Standards%20and%20Procedures/OCC/healthcheck/HAWC-healthCheckBFLD.html";
	
	public void initialSetUp(EventFiringWebDriver driver, String url, String userId, String passwd, String securityQuestion){
		System.out.println("Initialsetup");
		this.driver = driver;
		this.url = url;
		this.userId = userId;
		this.passwd = passwd;
		this.securityQuestion = securityQuestion;
	}
	
	public void initialSetUpDriver(EventFiringWebDriver driver){
		System.out.println("Initialsetup for Transaction Test Cases");
		this.driver = driver;
	}
	
	protected void doDRSetup() throws Exception {
		System.out.println("In DR Setup.");
		driver.get(DR_URL);
		for (int second = 0;; second++) {
			if (second >= 60)
				fail("timeout");
			try {
				if ("Broomfield".equals(driver.findElement(By.xpath("//a"))
						.getText()))
					break;
			} catch (Exception e) {
			}
			Thread.sleep(1000);
		}

		driver.findElement(By.xpath("//a")).click();

	}
}
