package com.ensense.insense.data.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/** JDK before version 7. */
public class CRUDObjectsToFileUtil {

	private static Object object;
	private static Logger logger = Logger.getLogger(CRUDObjectsToFileUtil.class);
	
	public static void writeObjectToFile(Object obj, String filePath) {

		object = obj;
		// serialize the List
		// note the use of abstract base class references

		synchronized (object) {
			try {
				// use buffering c:/testmystuff/quarks.ser
				OutputStream file = new FileOutputStream(filePath);
				OutputStream buffer = new BufferedOutputStream(file);
				ObjectOutput output = new ObjectOutputStream(buffer);
				try {
					output.writeObject(object);
				} finally {
					output.close();
				}
			} catch (IOException ex) {
				logger.error("Exception while writing Object to the file :"+filePath);
				logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(ex));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static boolean fileExists(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}

	}

	public static Object readObjectFromFile(String filePath) throws Exception{

		// deserialize the quarks.ser file
		// note the use of abstract base class references

		InputStream file;
		InputStream buffer;
		ObjectInput input;
		
		if ( null != object ){
			synchronized (object) {

				try {
					// use buffering
					file = new FileInputStream(filePath);
					buffer = new BufferedInputStream(file);
					input = new ObjectInputStream(buffer);
					try {
						// deserialize the List
						object = null;
						object = input.readObject();
					} finally {
						input.close();
					}
				} catch (ClassNotFoundException ex) {
					logger.warn("Exception while Reading the Object from file :"+filePath);
					throw(ex);
					//logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(ex));
				} catch (IOException ex) {
					logger.warn("Exception while Reading the Object from file :"+filePath);
					throw(ex);
					//logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(ex));
				} catch(Exception e){
					logger.warn("Exception while Reading the Object from file :"+filePath);
					throw(e);
					//logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
				}
			}
		}else{
			try {
				// use buffering
				file = new FileInputStream(filePath);
				buffer = new BufferedInputStream(file);
				input = new ObjectInputStream(buffer);
				try {
					// deserialize the List
					object = input.readObject();
				} finally {
					input.close();
				}
			} catch (ClassNotFoundException ex) {
				logger.warn("Exception while Reading the Object from file :"+filePath);
				throw(ex);
				//logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(ex));
			} catch (IOException ex) {
				logger.warn("Exception while Reading the Object from file :"+filePath);
				throw(ex);
				//logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(ex));
			} catch(Exception e){
				logger.warn("Exception while Reading the Object from file :"+filePath);
				//logger.error("Stack Trace :"+ExceptionUtils.getStackTrace(e));
				throw(e);
			}
		}

		return object;
	}
	
	public static Object readObjectFromFile1(String filePath) {

		// deserialize the quarks.ser file
		// note the use of abstract base class references
		Object object1 = null;

		try {
			if (MintFileUtils.isFileExists(filePath)) {
				InputStream file;
				InputStream buffer;
				ObjectInput input;

				// use buffering
				file = new FileInputStream(filePath);
				buffer = new BufferedInputStream(file);
				input = new ObjectInputStream(buffer);
				try {
					// deserialize the List
					object1 = input.readObject();
				} finally {
					input.close();
				}
			}
		} catch (ClassNotFoundException ex) {
			logger.warn("Exception while Reading the Object from file :"
					+ filePath);
			logger.warn("Stack Trace :" + ExceptionUtils.getStackTrace(ex));
		} catch (IOException ex) {
			logger.warn("Exception while Reading the Object from file :"
					+ filePath);
			logger.warn("Stack Trace :" + ExceptionUtils.getStackTrace(ex));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return object1;
	}

	public static void main(String... aArguments) throws Exception{
		// create a Serializable List
		List<String> quarks = Arrays.asList("up", "down", "strange", "charm",
				"top", "bottom", "hello");

		System.out.println("hello");
		writeObjectToFile(quarks, "c:/testmystuff/quarks.ser");
		readObjectFromFile("c:/testmystuff/quarks.ser");

	}

	// PRIVATE

	// Use Java's logging facilities to record exceptions.
	// The behavior of the logger can be configured through a
	// text file, or programmatically through the logging API.
	private static final Logger fLogger = Logger
			.getLogger(CRUDObjectsToFileUtil.class.getPackage().getName());
}
