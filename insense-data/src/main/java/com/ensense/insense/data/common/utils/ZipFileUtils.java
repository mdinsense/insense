package com.ensense.insense.data.common.utils;

import edu.umass.cs.benchlab.har.HarEntries;
import edu.umass.cs.benchlab.har.HarEntry;
import edu.umass.cs.benchlab.har.HarLog;
import edu.umass.cs.benchlab.har.HarWarning;
import edu.umass.cs.benchlab.har.tools.HarFileReader;
import org.codehaus.jackson.JsonParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipFileUtils {
	
	//unit testing
public static void main(String args[]){
	
	InputStream inputStream=null;
	ZipFile file=null;
	String zipFilePath="C:\\workspace\\CaptureNetworkTraffic\\public-advice-guidance\\www.tiaa-cref.org+2014-03-30+09-51-43.har.zip";
	
	
	try{
	try {
		file = new ZipFile(zipFilePath );
		inputStream = readFromHarZipFile(file);
		
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	
	HarFileReader r = new HarFileReader();
	List<HarWarning> warnings = new ArrayList<HarWarning>();
	try {
		HarLog harLog = r.readHarFile(inputStream, warnings);

		HarEntries entries = harLog.getEntries();
		List<HarEntry> hentry = entries.getEntries();
		
		for (HarEntry entry : hentry) {
			System.out.println(entry.getRequest());
		}
		
	} catch (JsonParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	
	}
	finally
	{
	    try {
			file.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	
}

public static InputStream readFromHarZipFile(ZipFile file){

		InputStream inputStream=null;

		try
		{
		    Enumeration<? extends ZipEntry> entries = file.entries();
		    while ( entries.hasMoreElements() )
		    {
		        ZipEntry entry = entries.nextElement();
		        System.out.println( entry.getName() );
		        //use entry input stream:
		        try {//there is just going to one file inside .har.zip
					inputStream =file.getInputStream( entry );
				//	System.out.println(inputStream.toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}	catch(Exception e){
			e.printStackTrace();
		}
		
	return inputStream;
	}


private static int readInputStream( final InputStream is ) throws IOException {
    final byte[] buf = new byte[ 8192 ];
    int read = 0;
    int cntRead;
    while ( ( cntRead = is.read( buf, 0, buf.length ) ) >=0  )
    {
        read += cntRead;
    }
    System.out.println("read "+ read);
    return read;
}

  public static void zipFolder(String srcFolder, String destZipFile) throws Exception {
    ZipOutputStream zip = null;
    FileOutputStream fileWriter = null;

    fileWriter = new FileOutputStream(destZipFile);
    zip = new ZipOutputStream(fileWriter);

    addFolderToZip("", srcFolder, zip);
    zip.flush();
    zip.close();
  }

  private static void addFileToZip(String path, String srcFile, ZipOutputStream zip)
      throws Exception {
    File folder = new File(srcFile);
    if (folder.isDirectory()) {
      addFolderToZip(path, srcFile, zip);
    } else {
      byte[] buf = new byte[1024];
      int len;
      FileInputStream in = new FileInputStream(srcFile);
      zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
      while ((len = in.read(buf)) > 0) {
        zip.write(buf, 0, len);
      }
    }
  }

  private static void addFolderToZip(String path, String srcFolder, ZipOutputStream zip)
      throws Exception {
    File folder = new File(srcFolder);

    for (String fileName : folder.list()) {
      if (path.equals("")) {
        addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
      } else {
        addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip);
      }
    }
  }

}
