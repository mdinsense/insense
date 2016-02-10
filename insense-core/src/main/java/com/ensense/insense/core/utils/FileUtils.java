package com.ensense.insense.core.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FilenameUtils;

import com.cts.mint.util.DateTimeUtil;
import com.cts.mint.util.FileListing;

public class FileUtils {

	String BATCH_ID;
	String DELIMITER=", ";
	
	public void backupLogFile(String filePath){
		File oldfile = new File(filePath);
		File newfile =new File(filePath+"_"+DateTimeUtil.getCurrentDateTime());
		if(oldfile.exists() && !oldfile.isDirectory()) { 
		//rename the file with current date time stamp
			
		if(oldfile.renameTo(newfile)){
			System.out.println("Renamed " + filePath + " to "+ newfile.getName());
			if(oldfile.delete()){
				System.out.println("Deleted "+ filePath);
			}
		}else{
			System.out.println("Rename of "+filePath + " failed");
		}
		}
	}
	
	public void closeFile(PrintWriter printWriter){
		printWriter.close();
	}
	
	
	public void writeLineToFile(PrintWriter printWriter, String line){
		
		printWriter.println(BATCH_ID+DELIMITER+line);
	}
	
	public void readFromFile(PrintWriter printWriter){
		
	}
	public PrintWriter openFile(String filePath, boolean appendToFile,String batchID){
		
		PrintWriter printWriter=null;
		BATCH_ID=batchID;
		try {
			backupLogFile(filePath);//backup the file before writing the new data to log
			printWriter = new PrintWriter(new FileWriter(filePath,appendToFile),true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return printWriter;
	}
	
	
	public String convertWinToJavaFilePath(String filePath){
		return filePath.replace("\\", "/");
	}
	
	//Unit test code
	public static void main(String args[]){
		FileUtils fileUtils = new FileUtils();
		PrintWriter printWriter =fileUtils.openFile("c:/testmystuff/fileWriter.txt",true,"someString");
		for(int i=0;i<10;i++){
		fileUtils.writeLineToFile(printWriter, "Hello Mahesh!!");
		}
		
		fileUtils.closeFile(printWriter);
	}
	
	// write ReportFile To response Stream
	public void writeReportToStream(String harReportsDirectoryPath, String reportName, HttpServletResponse response, ServletContext servletContext) {
			int BUFFER_SIZE = 9216;
			FileListing fileList = new FileListing();
			List<String> files = new ArrayList<String>();
			String harReportFile = "";
			harReportsDirectoryPath = harReportsDirectoryPath + "\\..\\";
			try {
				files = fileList.getFileListing(harReportsDirectoryPath, ".xlsx");

				for (String file : files) {
					if (file.startsWith(reportName)) {
						harReportFile = harReportsDirectoryPath + file;
						break;
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// String fullPath = filePath;
			File downloadFile = new File(harReportFile);
			FileInputStream inputStream = null;
			try {
				if(downloadFile != null && downloadFile.isFile()) {
					inputStream = new FileInputStream(downloadFile);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// get MIME type of the file
			String mimeType = servletContext.getMimeType(harReportFile);
			if (mimeType == null) {
				// set to binary type if MIME mapping not found
				mimeType = "application/octet-stream";
			}
			System.out.println("MIME type: " + mimeType);

			// set content attributes for the response
			response.setContentType(mimeType);
			response.setContentLength((int) downloadFile.length());

			// set headers for the response
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"",
					downloadFile.getName());
			response.setHeader(headerKey, headerValue);

			// get output stream of the response
			OutputStream outStream = null;
			try {
				outStream = response.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;

			// write bytes read from the input stream into the output stream
			try {
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, bytesRead);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {

				try {
					if(inputStream!=null) {
						inputStream.close();
					}
					if(outStream!=null) {
						outStream.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	// check if the report file exists for the gievn report name
	public static boolean checkReportExists(String filepath, String reportName) {
		boolean isExists = false;
		FileListing fileList = new FileListing();
		List<String> files = new ArrayList<String>();
		String harReportFile = "";
		filepath = filepath + "\\..\\";
		try {
			if(new File(filepath).isDirectory()) {
				files = fileList.getFileListing(filepath, ".xlsx");
				for (String file : files) {
					if (file.startsWith(reportName)) {
						harReportFile = filepath + file;
						break;
					}
				}
				File file = new File(harReportFile);
				if(file != null && file.isFile()) {
					isExists = true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isExists;
	}

	
	  public static BufferedWriter writeToFile(String fileName, boolean appendToFile){
		  BufferedWriter bw=null;
		  try {
			  
				File file = new File(fileName);

				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}

				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				bw = new BufferedWriter(fw);
				//bw.write(content);
				//bw.close();

				//System.out.println("Done");

			} catch (IOException e) {
				e.printStackTrace();
			}
			return bw;
	  }
	  
	  public static String getFileNameInUrl(String url){
		  String baseName = FilenameUtils.getBaseName(url);
	        String extension = FilenameUtils.getExtension(url);
	        return baseName+"."+extension;
	  }
	  
	  
	  //does a recursive folder list
	  public static Map<String,String> getFilesInFolderMap(final File folder, boolean recurse) {
		 
		  Map<String, String> filesMap=new LinkedHashMap<String, String>();
		  
		    for (final File fileEntry : folder.listFiles()) {
		        if (fileEntry.isDirectory() && recurse) {
		        	getFilesInFolderMap(fileEntry,recurse);
		        } else {
		           // System.out.println(fileEntry.getName());
		            filesMap.put(fileEntry.getName(), fileEntry.getPath().replace("\\", "/"));
		           // System.out.println(fileEntry.getPath().replace("\\", "/"));
		        }
		    }
		    return filesMap;
		}
	  
	  public static List<String> getFilesInFolderList(final File folder, boolean recurse) {
		  List<String> filesList = new ArrayList<String>();
		    for (final File fileEntry : folder.listFiles()) {
		        if (fileEntry.isDirectory()) {
		        	getFilesInFolderList(fileEntry,recurse);
		        } else {
		            System.out.println(fileEntry.getName());
		        	filesList.add(fileEntry.getPath().replace("\\", "/"));
		            System.out.println(fileEntry.getPath().replace("\\", "/"));
		        }
		    }
		    return filesList;
		}
	  
	  public static void createDirectory(String dirPath){
		 // System.out.println(dirPath);
          //String dirPath1=dirPath.replaceAll("/", "\\");
		//System.out.println(dirPath);
		  //String dirPath1="C:\\testmystuff\\intranet-testing\\output\\abc";
		  File files = new File(dirPath);
		 // System.out.println("creating dir :"+dirPath );
			if (!files.exists()) {
				if (files.mkdirs()) {
					System.out.println("Multiple directories are created! :" +dirPath);
				} else {
					System.out.println("Failed to create multiple directories!" + dirPath);
				}
			}
	  }

	public static Properties getProperties(String configFilePath){
		Properties prop = new Properties();
		InputStream input = null;
	 
		try {
	 
			input = new FileInputStream(configFilePath);
	 
			// load a properties file
			prop.load(input);
	
	 
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	 
	}
	public static void copyFile(File source, File dest) {
		// TODO Auto-generated method stub
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(source);
			output = new FileOutputStream(dest);
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				input.close();
				output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static String readFromFile(String fileName){
		BufferedReader br=null;
		String line="";
		String everything="";
		
	    try {
	    	br = new BufferedReader(new FileReader(fileName));
	    	StringBuilder sb = new StringBuilder();
	        line = br.readLine();

	        while (line != null) {
	        	//System.out.println(line);
	            sb.append(line);
	            line = br.readLine();
	        }
	        everything = sb.toString();
	    } catch(Exception e){e.printStackTrace();}
	    	finally {
	        try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
    	return everything;
	}
	
	public static String encodeFileToBase64Binary(String fileName)
			throws IOException {

		File file = new File(fileName);
		byte[] bytes = loadFile(file);
		byte[] encoded = Base64.encodeBase64(bytes);
		String encodedString = new String(encoded);

		return encodedString;
	}

	private static byte[] loadFile(File file) throws IOException {
	    InputStream is = new FileInputStream(file);

	    long length = file.length();
	    if (length > Integer.MAX_VALUE) {
	        // File is too large
	    }
	    byte[] bytes = new byte[(int)length];
	    
	    int offset = 0;
	    int numRead = 0;
	    while (offset < bytes.length
	           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	        offset += numRead;
	    }

	    if (offset < bytes.length) {
	        throw new IOException("Could not completely read file "+file.getName());
	    }

	    is.close();
	    return bytes;
	}
}
