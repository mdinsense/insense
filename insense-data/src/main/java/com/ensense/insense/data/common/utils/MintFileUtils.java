package com.ensense.insense.data.common.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.comparator.LastModifiedFileComparator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class MintFileUtils {
	private static final Logger logger = Logger.getLogger(MintFileUtils.class);

	String BATCH_ID;
	String DELIMITER = ", ";

	public void backupLogFile(String filePath) {
		File oldfile = new File(filePath);
		File newfile = new File(filePath + "_"
				+ DateTimeUtil.getCurrentDateTime());
		if (oldfile.exists() && !oldfile.isDirectory()) {
			// rename the file with current date time stamp

			if (oldfile.renameTo(newfile)) {
				System.out.println("Renamed " + filePath + " to "
						+ newfile.getName());
				if (oldfile.delete()) {
					System.out.println("Deleted " + filePath);
				}
			} else {
				System.out.println("Rename of " + filePath + " failed");
			}
		}
	}

	public void closeFile(PrintWriter printWriter) {
		printWriter.close();
	}

	public void writeLineToFile(PrintWriter printWriter, String line) {

		printWriter.println(BATCH_ID + DELIMITER + line);
	}

	public void readFromFile(PrintWriter printWriter) {

	}

	public PrintWriter openFile(String filePath, boolean appendToFile,
			String batchID) {

		PrintWriter printWriter = null;
		BATCH_ID = batchID;
		try {
			backupLogFile(filePath);// backup the file before writing the new
									// data to log
			printWriter = new PrintWriter(
					new FileWriter(filePath, appendToFile), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return printWriter;
	}

	public String convertWinToJavaFilePath(String filePath) {
		return filePath.replace("\\", "/");
	}

	// write ReportFile To response Stream
	public void writeReportToStream(String harReportsDirectoryPath,
			String reportName, HttpServletResponse response,
			ServletContext servletContext, String fileType) {
		int BUFFER_SIZE = 9216;
		FileListing fileList = new FileListing();
		List<String> files = new ArrayList<String>();
		String harReportFile = "";
		harReportsDirectoryPath = harReportsDirectoryPath + "\\";
		try {
			files = fileList.getFileListing(harReportsDirectoryPath, fileType);

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
			if (downloadFile != null && downloadFile.isFile()) {
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
				if (inputStream != null) {
					inputStream.close();
				}
				if (outStream != null) {
					outStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	// check if the report file exists for the gievn report name
	public static boolean checkFileExists(String filepath, String name,
			String extention) {
		boolean isExists = false;
		FileListing fileList = new FileListing();
		List<String> files = new ArrayList<String>();
		String reportFilePath = "";

		try {
			if (new File(filepath).isDirectory()) {
				files = fileList.getFileListing(filepath, extention);
				for (String file : files) {
					if (file.startsWith(name)) {
						reportFilePath = filepath + File.separator + file;
						break;
					}
				}
				logger.info("reportFilePath :" + reportFilePath);
				File file = new File(reportFilePath);
				if (file != null && file.isFile()) {
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

	public static BufferedWriter writeToFile(String fileName,
			boolean appendToFile) {
		BufferedWriter bw = null;
		try {

			File file = new File(fileName);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);
			// bw.write(content);
			// bw.close();

			// System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return bw;
	}

	public static String getFileNameInUrl(String url) {
		String baseName = FilenameUtils.getBaseName(url);
		String extension = FilenameUtils.getExtension(url);
		return baseName + "." + extension;
	}

	// does a recursive folder list
	public static Map<String, String> getFilesInFolderMap(final File folder,
			boolean recurse) {

		Map<String, String> filesMap = new LinkedHashMap<String, String>();

		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory() && recurse) {
				getFilesInFolderMap(fileEntry, recurse);
			} else {
				// System.out.println(fileEntry.getName());
				filesMap.put(fileEntry.getName(),
						fileEntry.getPath().replace("\\", "/"));
				// System.out.println(fileEntry.getPath().replace("\\", "/"));
			}
		}
		return filesMap;
	}

	public static List<String> getFilesInFolderList(final File folder,
			boolean recurse) {
		List<String> filesList = new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				getFilesInFolderList(fileEntry, recurse);
			} else {
				System.out.println(fileEntry.getName());
				filesList.add(fileEntry.getPath().replace("\\", "/"));
				System.out.println(fileEntry.getPath().replace("\\", "/"));
			}
		}
		return filesList;
	}

	public static void createDirectory(String dirPath) {
		// System.out.println(dirPath);
		// String dirPath1=dirPath.replaceAll("/", "\\");
		// System.out.println(dirPath);
		// String dirPath1="C:\\testmystuff\\intranet-testing\\output\\abc";
		File files = new File(dirPath);
		// System.out.println("creating dir :"+dirPath );
		if (!files.exists()) {
			if (files.mkdirs()) {
				System.out.println("Multiple directories are created! :"
						+ dirPath);
			} else {
				System.out.println("Failed to create multiple directories!"
						+ dirPath);
			}
		}
	}

	public static Properties getProperties(String configFilePath) {
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

	public static String readFromFile(String fileName) {
		BufferedReader br = null;
		String line = "";
		String everything = "";

		try {
			br = new BufferedReader(new FileReader(fileName));
			StringBuilder sb = new StringBuilder();
			line = br.readLine();

			while (line != null) {
				// System.out.println(line);
				sb.append(line);
				line = br.readLine();
			}
			everything = sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return everything;
	}

	public static boolean isFileExists(String filePath) throws Exception {
		try {
			File f = new File(filePath);
			if (f.exists() && !f.isDirectory()) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public static boolean writeHtmlContentToFile(String content,
			String fileName, boolean append) throws Exception {
		BufferedWriter writer = null;
		try {
			File fileToWrite = new File(fileName);
			if (!fileToWrite.exists()) {
				fileToWrite.createNewFile();
			}
			writer = new BufferedWriter(new FileWriter(fileToWrite, append));
			writer.write(content);
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
				return false;
			} catch (Exception e) {
			}
		}
		return true;
	}

	public static String readFile(String filename) {
		StringBuffer strbuf = new StringBuffer();
		try {
			FileReader input = new FileReader(filename);
			BufferedReader bufRead = new BufferedReader(input);
			String line = bufRead.readLine();
			while (line != null) {
				strbuf.append(line);
				strbuf.append('\n');
				line = bufRead.readLine();
			}
			bufRead.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strbuf.toString();
	}

	public static boolean deleteFile(String filePath) {
		boolean isDeleted = true;
		try {
			File fileToDelete = new File(filePath);
			if (fileToDelete.exists()) {
				isDeleted = fileToDelete.delete();
			}
		} catch (Exception e) {

		}
		return isDeleted;
	}

	public static boolean deleteDir(File dir) {
		boolean isDeleted = false;
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
			isDeleted = dir.delete();
		}
		return isDeleted;
	}

	public static File[] listFilesOfGivenExtension(String directory,
			final String extension) {
		File file = new File(directory);
		File[] files = null;
		if (file.exists() && file.isDirectory()) {
			files = file.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String filename) {
					return filename.endsWith(extension);
				}
			});
		}
		return files;
	}

	public static boolean checkMintUsageReportExists(String filepath,
			String reportName) {
		boolean isExists = false;
		FileListing fileList = new FileListing();
		List<String> files = new ArrayList<String>();
		String harReportFile = "";
		filepath = filepath + "\\";
		try {
			if (new File(filepath).isDirectory()) {
				files = fileList.getFileListing(filepath, ".xlsx");
				for (String file : files) {
					if (file.startsWith(reportName)) {
						harReportFile = filepath + file;
						break;
					}
				}
				File file = new File(harReportFile);
				if (file != null && file.isFile()) {
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

	public static boolean renameFile(String directory, String oldFileName,
			String newFileName, String extension) {

		boolean renamed = false;
		int fileCount = 1;
		if (!directory.endsWith(DataConstants.KEYS.B_SLASH_DOUBLE)
				|| !directory.endsWith(DataConstants.KEYS.F_SLASH)) {
			directory = directory + File.separator;
		}
		File oldfile = new File(directory + oldFileName + extension);
		File newfile = new File(directory + newFileName + extension);

		while (newfile.exists()) {
			newfile = new File(directory + newFileName + fileCount + extension);
			fileCount++;
		}

		if (oldfile.renameTo(newfile)) {
			renamed = true;
		} else {
			renamed = false;
		}
		return renamed;
	}

	public static File[] getFilesForGivenString(String directory,
			final String stringToFilter) {
		File f = new File(directory); // current directory
		File[] files = null;
		try {
			FilenameFilter textFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (name.contains(stringToFilter)) {
						return true;
					} else {
						return false;
					}
				}
			};
			files = f.listFiles(textFilter);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return files;
	}

	public static File getTheOldestModifiedFile(File[] files) {

		File lastModifiedFile = null;
		try {
			if (null != files && files.length > 0) {
				/** The oldest file comes first **/
				Arrays.sort(files,
						LastModifiedFileComparator.LASTMODIFIED_COMPARATOR);
				lastModifiedFile = files[0];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lastModifiedFile;
	}

	public static boolean isFileUnlocked(File file) {
		try {
			FileInputStream in = new FileInputStream(file);
			if (in != null)
				in.close();
			return true;
		} catch (FileNotFoundException e) {
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

	public static boolean isFileReady(String fileName) {
		boolean isLocked = false;
		FileLock lock = null;
		FileChannel channel = null;
		File file = null;
		try {
			file = new File(fileName);
			channel = new RandomAccessFile(file, "rw").getChannel();
			// Get an exclusive lock on the whole file
			lock = channel.lock();
			lock = channel.tryLock();
			// Ok. You get the lock
		} catch (Exception e) {
			// File is open by someone else
		} finally {
			try {
				if (file != null && channel != null && lock != null) {
					lock.release();
					channel.close();
					isLocked = true;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return isLocked;
	}

	public static boolean deleteDir(String dir) {
		boolean isDeleted = false;
		File dirFile = new File(dir);
		try {
			if (dirFile != null && dirFile.exists() && dirFile.isDirectory()) {
				FileUtils.deleteDirectory(dirFile);
				isDeleted = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isDeleted;
	}

	// write ReportFile To response Stream
	public void writeFileToStream(String filepath,
			HttpServletResponse response, ServletContext servletContext) {
		int BUFFER_SIZE = 9216;
		// String fullPath = filePath;
		File downloadFile = new File(filepath);
		FileInputStream inputStream = null;
		try {
			if (downloadFile != null && downloadFile.isFile()) {
				inputStream = new FileInputStream(downloadFile);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// get MIME type of the file
		String mimeType = servletContext.getMimeType(filepath);
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
				if (inputStream != null) {
					inputStream.close();
				}
				if (outStream != null) {
					outStream.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static boolean zipIt(String zipFile, List<File> fileList) {
		boolean isCompleted = false;
		byte[] buffer = new byte[1024];
		try {
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			System.out.println("Output to Zip : " + zipFile);
			for (File file : fileList) {
				String fileName = StringUtils.substringAfterLast(
						file.getAbsolutePath(), "\\");
				System.out.println("File Added : " + file);
				ZipEntry ze = new ZipEntry(fileName);
				zos.putNextEntry(ze);
				FileInputStream in = new FileInputStream(file.getAbsolutePath());
				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
				in.close();
			}
			zos.closeEntry();
			zos.close();
			isCompleted = true;
			System.out.println("Done");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return isCompleted;
	}

	public static boolean uploadFile(MultipartFile file, String fileName) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			if (file != null && file.getSize() > 0) {
				inputStream = file.getInputStream();
			} else {
				return false;
			}
			outputStream = new FileOutputStream(fileName);
			int readBytes = 0;
			byte[] buffer = new byte[10000];
			while ((readBytes = inputStream.read(buffer, 0, 10000)) != -1) {
				outputStream.write(buffer, 0, readBytes);
			}
		} catch (Exception e) {
			logger.error("Exception :Unexpected error occured while creating destination file");
			logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			return false;
		} finally {
			try {
				outputStream.close();
				inputStream.close();
			} catch (IOException e) {
				logger.error("Exception :Unexpected error occured while uploading login user script");
				logger.error("Stack Trace :" + ExceptionUtils.getStackTrace(e));
			}
		}
		return true;
	}

	// Unit test code
	public static void main(String args[]) {
		MintFileUtils fileUtils = new MintFileUtils();

	}

}
