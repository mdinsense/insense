package com.ensense.insense.data.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyReader {
	Properties properties;

	public PropertyReader() {
		if (properties == null) {
			properties = new Properties();

			try {
				System.out.println("PropertyReader current dir :"+new File(".").getAbsolutePath());
				FileInputStream fis = new FileInputStream("config\\config.properties");
				// loading properites from properties file
				properties.load(fis);
			} catch (IOException e) {
				System.out.println("PropertyReader :Config file config.properties NOT FOUND...");
				//System.exit(0);
			} catch (Exception e) {
				System.out.println("PropertyReader :Config file config.properties NOT FOUND..."	+ e);
				e.printStackTrace();
				//System.exit(0);
			}
		}

	}

	public String getProperty(String key) {
		String property = properties.getProperty(key);
		return property;
	}

	public String[] getPropertyAsArray(String key) {

		String property = properties.getProperty(key);
		String[] propertyArray = property.split(",");

		return propertyArray;
	}

}
