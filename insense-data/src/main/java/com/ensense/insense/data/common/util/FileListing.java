package com.ensense.insense.data.common.util;

import java.util.*;
import java.io.*;

public final class FileListing {

	private static File rootDirectory;

  public static void main(String... aArgs) throws FileNotFoundException {
	  try{
		  String rootDirectoryPath = "C:\\Dhinakar\\Project\\SecurityVulnarability\\AutomatedTesting\\Results\\Sep262013_11-03-34_AT_participant_PostUpgrade";
	    
	    FileListing listing = new FileListing();
	    List<String> files = listing.getFileListing(rootDirectoryPath, "jpeg");
	
	    //print out all file names, in the the order of File.compareTo()
	    for(String file : files){
	      System.out.println(file);
	    }
	  }catch(Exception e){
		  
	  }
  }
  
	public static List<String> listFilesForFolder(final File folder) {
		List<String> filesInDir = new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				// System.out.println(fileEntry.getName());
				// convert from windows file path format to java
				filesInDir.add(fileEntry.getPath().replace("\\", "/"));
				// System.out.println(fileEntry.getPath().replace("\\", "/"));
			}
		}
		return filesInDir;
	}

  public List<String> getFileListing(String rootDirectoryPath, String fileType) throws FileNotFoundException, IOException {
	  rootDirectory = new File(rootDirectoryPath);
    validateDirectory(rootDirectory);
    List<String> result = getFileListingNoSort(rootDirectory, fileType);
    Collections.sort(result);
    return result;
  }

  public List<String> getFilePathListing(String rootDirectoryPath, String filter) throws FileNotFoundException, IOException {
	rootDirectory = new File(rootDirectoryPath);
	validateDirectory(rootDirectory);
	List<String> result = getFilePathListingNoSort(rootDirectory, filter);
	Collections.sort(result);
	return result;
  }
  // PRIVATE

  private List<String> getFileListingNoSort(File aStartingDir, String fileType) throws FileNotFoundException, IOException {
    List<String> result = new ArrayList<String>();
    File[] filesAndDirs = aStartingDir.listFiles();
    List<File> filesDirs = Arrays.asList(filesAndDirs);
    for(File file : filesDirs) {
    	if ( file.isFile()) {
    		String fileName = file.getCanonicalPath();
    		fileName = fileName.replace(rootDirectory.getCanonicalPath()+"\\", "");
    		if ( fileType != null && fileType.length() > 0 ) {
    			
    			if ( fileName.endsWith(fileType) && !fileName.endsWith("Changes.jpeg")){
    				result.add(fileName); //always add, even if directory
    			}
    		}else{
    			result.add(fileName);
    		}
    	} else if (! file.isFile()) {
        //must be a directory
        //recursive call!
        List<String> deeperList = getFileListingNoSort(file, fileType);
        result.addAll(deeperList);
      }
    }
    return result;
  }

  private List<String> getFilePathListingNoSort(File aStartingDir, String filter) throws FileNotFoundException, IOException {
	    List<String> result = new ArrayList<String>();
	    File[] filesAndDirs = aStartingDir.listFiles();
	    List<File> filesDirs = Arrays.asList(filesAndDirs);
	    for(File file : filesDirs) {
	    	if ( file.isFile() && file.getName().toLowerCase().endsWith(filter)) {
	    		String fileName = file.getCanonicalPath();
	    		result.add(fileName); //always add, even if directory
	    	} else if (! file.isFile()) {
	        //must be a directory
	        //recursive call!
	        List<String> deeperList = getFilePathListingNoSort(file, filter);
	        result.addAll(deeperList);
	      }
	    }
	    return result;
	  }
  
  /**
  * Directory is valid if it exists, does not represent a file, and can be read.
  */
  private void validateDirectory (
    File aDirectory
  ) throws FileNotFoundException {
    if (aDirectory == null) {
      throw new IllegalArgumentException("Directory should not be null.");
    }
    if (!aDirectory.exists()) {
      throw new FileNotFoundException("Directory does not exist: " + aDirectory);
    }
    if (!aDirectory.isDirectory()) {
      throw new IllegalArgumentException("Is not a directory: " + aDirectory);
    }
    if (!aDirectory.canRead()) {
      throw new IllegalArgumentException("Directory cannot be read: " + aDirectory);
    }
  }
} 
 
