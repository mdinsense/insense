package com.ensense.insense.core.crawler.model.executer;

import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BehindTheScene
{
	private static final String wellnessUrl = "http://docit.ss.tiaa-cref.org/Standards%20and%20Procedures/OCC/healthcheck/HAWC-healthCheck.html";

	public Object setDataCenter(WebDriver driver, String dataCenter)
	{
		Object result = null;
		try
		{
			driver.get(wellnessUrl);

			driver.findElement(By.linkText(dataCenter)).click();
			driver.findElement(By.linkText("Clear Data Center Cookie")).click();
			driver.findElement(By.linkText("Clear Session Cookies")).click();
			if(isAlert(driver))
			{
				Alert alert = driver.switchTo().alert();
				alert.accept();
			}
		    driver.findElement(By.linkText(dataCenter)).click();
		    driver.findElement(By.linkText("Launch Web Center")).click();

		    Thread.sleep(4000);

			String parentWindowHandle = driver.getWindowHandle();
			Set<String> windowHandles = driver.getWindowHandles();

			driver.close();
			for (String winHandle : windowHandles) {
				if (!winHandle.equalsIgnoreCase(parentWindowHandle)) {
					driver.switchTo().window(winHandle);
					break;
				}
			}
			result = driver;
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return result; 
	}
	
	private boolean isAlert(WebDriver driver)
	{
		boolean isAlert = false;
		try
		{
			WebDriverWait wait = new WebDriverWait(driver, 0);
			if (wait.until(ExpectedConditions.alertIsPresent()) != null)
				isAlert = true;
		} catch (TimeoutException e)
		{
			isAlert = false;
		}

		return isAlert;
	}
}
