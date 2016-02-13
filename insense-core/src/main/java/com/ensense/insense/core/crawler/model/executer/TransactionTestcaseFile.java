package com.ensense.insense.core.crawler.model.executer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;

public class TransactionTestcaseFile extends TestCase{


	  @Before
	  public void setUp() throws Exception {

	  }

	  @Test
	  public void testTransactionTestcaseFile() throws Exception {
		  
		  driver.get(url);
		  String filePath = driver.findElement(By.id("filePath")).getText();
		  String fileName = driver.findElement(By.id("fileName")).getText();
		  System.out.println(filePath);
		  System.out.println(fileName);
		  
		  File file = new File(filePath);

		  try {
		      URL url = file.toURI().toURL();
		      URL[] urls = new URL[]{url};

		      ClassLoader cl = new URLClassLoader(urls);
		      Class cls = cl.loadClass("com.mycompany.MyClass");
		      
		  } catch (MalformedURLException e) {
		  } catch (ClassNotFoundException e) {
		  }
		  
		  
	  }

	  @After
	  public void tearDown() throws Exception {

	  }
	  
	  public static void main(String args[]){
		  
	  }


}
