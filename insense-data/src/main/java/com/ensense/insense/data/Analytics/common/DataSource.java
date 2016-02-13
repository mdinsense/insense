package com.ensense.insense.data.analytics.common;

import com.ensense.insense.data.utils.PropertyReader;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

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
