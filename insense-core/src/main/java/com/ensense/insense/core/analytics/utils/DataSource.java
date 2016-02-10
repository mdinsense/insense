package com.ensense.insense.core.analytics.utils;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.cts.mint.util.PropertyReader;


public class DataSource {
	
	public static DriverManagerDataSource getDataSource() {
		
		  DriverManagerDataSource dataSource = new DriverManagerDataSource();
          PropertyReader proReader = new PropertyReader();
		  dataSource.setDriverClassName(proReader.getProperty("driverClassName"));
	
		  dataSource.setUrl(proReader.getProperty("urlString"));
	
		  dataSource.setUsername(proReader.getProperty("dbusername"));
	
		  dataSource.setPassword(proReader.getProperty("dbpwd"));
	
		  return dataSource;
		
		    }
		
		         

}
