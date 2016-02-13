package com.ensense.insense.data.common.utils;

import com.ensense.insense.data.common.entity.MintProperties;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;



public class FileDirectoryUtil {
	@SuppressWarnings("unused")
	private static final Logger logger = Logger
			.getLogger(FileDirectoryUtil.class);
	private static List<File> folderList = new ArrayList<File>();
	
	public static boolean createDirectories(String directoryPath){
		File directory = new File(directoryPath);

		try{
			if (!directory.exists()) {
				directory.mkdirs();
			}
		} catch(Exception e){
			return false;
		}
		
		return true;
		
	}

	public static String getAbsolutePath(String directory, String mintRootDirectory){
		String absoluteDirectory = mintRootDirectory + directory;
		File file = new File(absoluteDirectory);
		
		if (!file.isAbsolute()) {
			try {
				absoluteDirectory = file.getCanonicalPath();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return absoluteDirectory;
	}
	
	public static String getDate(String datePattern) {
		DateFormat dateFormat = new SimpleDateFormat(datePattern);
		Date date = new Date();
		return dateFormat.format(date).toString().toString();
	}
	
	public static String removePound(String imageDirectory) {
		if (null != imageDirectory && imageDirectory.indexOf("#") > 1) {
			imageDirectory = imageDirectory.substring(0, imageDirectory.lastIndexOf("#"));
		}
		return imageDirectory;
	}

	public static String getMintRootDirectory(MessageSource configProperties) {
		return CommonUtils.getPropertyFromPropertyFile(configProperties, "mint.rootDirectory");
	}

	public static String getWSDLFilePath(MessageSource configProperties) {
		String wsdlPath = CommonUtils.getPropertyFromPropertyFile(configProperties, "wsdlFilePath");
		wsdlPath = FileDirectoryUtil.getAbsolutePath(wsdlPath, FileDirectoryUtil.getMintRootDirectory(configProperties));
		return wsdlPath;
	}
	public static String getWADLFilePath(MessageSource configProperties) {
		String wsdlPath = CommonUtils.getPropertyFromPropertyFile(configProperties,"wadlFilePath");
		wsdlPath = FileDirectoryUtil.getAbsolutePath(wsdlPath, FileDirectoryUtil.getMintRootDirectory(configProperties));
		return wsdlPath;
	}
	
	public static String getTransactionTestCaseRootPath(MessageSource configProperties) {
		String path = FileDirectoryUtil.getAbsolutePath(
				CommonUtils.getPropertyFromPropertyFile(configProperties,"mint.transactional.testcase.packageDirectory"),
				FileDirectoryUtil.getMintRootDirectory(configProperties));
		return path;
	}
	
	public static List<MintProperties> readMintPropertiesFromFile() throws IOException {
		List<String> propertiesStringList = null;
		List<MintProperties> mintPropertiesList = new ArrayList<MintProperties>();
		MintProperties mintProperties = null;
		propertiesStringList = IOUtils.readLines(
				new ClassPathResource("com/cts/mint/common/mintproperty.txt")
						.getInputStream());
		for(String props: propertiesStringList) {
			mintProperties = new MintProperties();
			mintProperties.setPropertyName(props.split(DataConstants.KEYS.EQUALS)[0]);
			mintProperties.setPropertyDisplayName(props.split(DataConstants.KEYS.EQUALS)[1]);
			mintPropertiesList.add(mintProperties);
		}
		return mintPropertiesList;
	}
	
	// does a recursive folder list
	public static List<File> getFoldersWithGivenName(final File folder, final String filter,
			boolean recurse) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				if(fileEntry.getName().contains(filter)) {
					folderList.add(fileEntry);
				}
				getFoldersWithGivenName(fileEntry, filter, recurse);
			} 
		}
		return folderList;
	}
	
	public static boolean copyAllPageImagesFromSubdirectories(String reportsFolder) {
		boolean isCopied = false;
		//TODO: commented because of compilation issue
		String textImageDir	 = "";//reportsFolder + BrokenReportSelection.ALL_IMAGE_DIR;;
		File folder = new File(reportsFolder);
		File allImagesDir = new File(textImageDir);
		allImagesDir.mkdirs();
		List<File> folderList = getFoldersWithGivenName(folder, "AllPageImages", true);
		List<File> fileList = new ArrayList<File>();
		for(File dir : folderList) {
			List<File> filesss = Arrays.asList(dir.listFiles());
			fileList.addAll(filesss);
		}
		//isCopied = MintFileUtils.zipIt(textImageDir + ReportPattern.ALL_IMAGES_ZIP.getReportPatternName() + FILE.ZIP,  fileList);
		return isCopied;
	}

	public static void deleteDirectoryList(List<String> directoriesToBeDeleted) {
		File directory = null;
		for(String dir : directoriesToBeDeleted) {
			directory = new File(dir);
			if(!directory.exists()){
					System.out.println("Directory does not exist.");
				
		        }else{
		           try{
		               delete(directory);
		           }catch(IOException e){
		               e.printStackTrace();
		           }
		        }
		 
		}
	}
	
	public static void delete(File file)
	throws IOException{

	if(file.isDirectory()){

		//directory is empty, then delete it
		if(file.list().length==0){
			
		   file.delete();
		   logger.debug("Directory is deleted : " 
                                             + file.getAbsolutePath());
			
		}else{
			
		   //list all the directory contents
    	   String files[] = file.list();
 
    	   for (String temp : files) {
    	      //construct the file structure
    	      File fileDelete = new File(file, temp);
    		 
    	      //recursive delete
    	     delete(fileDelete);
    	   }
    		
    	   //check the directory again, if empty then delete it
    	   if(file.list().length==0){
       	     file.delete();
    	     logger.debug("Directory is deleted : " 
                                              + file.getAbsolutePath());
    	   }
		}
		
	}else{
		//if file, then delete it
		file.delete();
		logger.debug("File is deleted : " + file.getAbsolutePath());
	}
}

}
