package com.ensense.insense.services.common.utils;

import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CommonUtils {
	public static String getListAsString(List<String> stringList){
		StringBuilder sb = new StringBuilder();
		if ( null == stringList || stringList.size() < 1 ){
			return "";
		}
		for (String s : stringList){
		    sb.append(s);
		    sb.append(",");
		}
		return sb.toString().substring(0, (sb.toString().length()-1));
	}
	
	public static String getPropertyFromPropertyFile(MessageSource configProperties, String propertyName){
		String propertyValue = "";
		
		try{
			propertyValue = configProperties.getMessage(propertyName, null,Locale.getDefault());
		}catch(Exception e){
			propertyValue = "";
		}

		return propertyValue;
	}
	
	public static List<String> getStringAsList(String value){
		List<String> listValue = new ArrayList<String>();
		try{
			String[] stringArray = value.split(",");
			
			listValue = Arrays.asList(stringArray);
		}catch(Exception e){
			
		}
		
		return listValue;
	}
	
}
