package com.ensense.insense.data.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class JsonReaderWriter<T> {
	//private T type;
	private static ObjectMapper mapper = new ObjectMapper();

	static{
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	public void writeJsonObjectToFile(T t, String filePath) throws Exception{
		synchronized(mapper){
			if ( null != t ){
				 // convert Object to json string, and save to a file
			    mapper.writeValue(new File(filePath), t);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public T readJsonObjectFromFile(T t, String filePath) throws Exception{
		synchronized(mapper){
			
			return (T) mapper.readValue(new File(filePath), t.getClass());
		}
	}
	
	public synchronized T readOrWriteJsonObjectFromFile(T t, String filePath, boolean isWrite) throws Exception{
		
		if ( isWrite ){
			writeJsonObjectToFile(t, filePath);
			return t;
		}else{
			return readJsonObjectFromFile(t, filePath);
		}
	}
}
