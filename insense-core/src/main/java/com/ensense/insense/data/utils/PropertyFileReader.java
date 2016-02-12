package com.ensense.insense.data.utils;

import java.util.Locale;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

public class PropertyFileReader {
	private static Logger logger = Logger
			.getLogger(PropertyFileReader.class);
	
	public static String getProperty(MessageSource propertyFile, String property){

		String propertyValue = "";
		
		try {
			propertyValue = propertyFile.getMessage(
					property, null, Locale.getDefault());
		}catch(Exception e){
			logger.error("Exception while reading the property :"+property);
			logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
		}
		
		return propertyValue;
	}

}
