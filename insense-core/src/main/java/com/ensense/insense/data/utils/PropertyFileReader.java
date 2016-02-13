package com.ensense.insense.data.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import java.util.Locale;

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
			logger.error("Stack Trace :"+ ExceptionUtils.getStackTrace(e));
		}
		
		return propertyValue;
	}

}
