package com.ensense.insense.services.common.utils;

import org.apache.log4j.Logger;
import org.springframework.context.MessageSource;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileDirectoryUtil {
	private static final Logger logger = Logger
			.getLogger(FileDirectoryUtil.class);

	public static boolean createDirectories(String directoryPath) {
		File directory = new File(directoryPath);

		try {
			if (!directory.exists()) {
				directory.mkdirs();
			}
		} catch (Exception e) {
			return false;
		}

		return true;

	}

	public static String getAbsolutePath(String directory,
			String mintRootDirectory) {
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
			imageDirectory = imageDirectory.substring(0,
					imageDirectory.lastIndexOf("#"));
		}
		return imageDirectory;
	}

	public static String getMintRootDirectory(MessageSource configProperties) {
		return configProperties.getMessage("mint.rootDirectory", null,
				Locale.getDefault());
	}

	public static String getWSDLFilePath(MessageSource configProperties) {
		String wsdlPath = configProperties.getMessage("wsdlFilePath", null,
				Locale.getDefault());
		wsdlPath = FileDirectoryUtil.getAbsolutePath(wsdlPath,
				FileDirectoryUtil.getMintRootDirectory(configProperties));
		return wsdlPath;
	}

	public static String getWADLFilePath(MessageSource configProperties) {
		String wsdlPath = configProperties.getMessage("wadlFilePath", null,
				Locale.getDefault());
		wsdlPath = FileDirectoryUtil.getAbsolutePath(wsdlPath,
				FileDirectoryUtil.getMintRootDirectory(configProperties));
		return wsdlPath;
	}

	public static String getLoginScriptPath(MessageSource configProperties) {
		return FileDirectoryUtil.getAbsolutePath(CommonUtils
				.getPropertyFromPropertyFile(configProperties,
						"mintv4.loginscript.path"), FileDirectoryUtil
				.getMintRootDirectory(configProperties));
	}
	
	public static String getStaticUrlPath(MessageSource configProperties) {
		return FileDirectoryUtil.getAbsolutePath(CommonUtils
				.getPropertyFromPropertyFile(configProperties,
						"mintv4.staticUrlFile.path"), FileDirectoryUtil
				.getMintRootDirectory(configProperties));
	}
}
