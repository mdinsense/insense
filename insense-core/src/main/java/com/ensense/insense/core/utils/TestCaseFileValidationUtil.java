package com.ensense.insense.core.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

public class TestCaseFileValidationUtil {
	private static Logger logger = Logger
			.getLogger(TestCaseFileValidationUtil.class);

	public static boolean validateFile(File sourceFile, File destinationFile, String packageName, String jarLocation) {
		boolean validateStatus = false;
		try {
			String line = null;
			ArrayList<String> lines = new ArrayList<String>();

			// FileUtils.copyFile(sourceFile, destinationFile);

			FileReader fr = new FileReader(destinationFile);
			BufferedReader br = new BufferedReader(fr);
			int linePos = 0;
			while ((line = br.readLine()) != null) {

				if (linePos == 0 && line.contains("package")) {

					// Assuming file will not be there in Same package where
					// TestCase class is available so TestCase class is
					// imported.
					line = "package " + packageName + ";"
							+ "\n" + "import com.cts.mint.crawler.TestCase;";
				} else if (linePos == 0 && !line.contains("package")) {
					lines.add("package " + packageName + ";" + "\n"
							+ "import com.cts.mint.crawler.TestCase;");
					lines.add("\n");
				} 

				if (line.contains("private WebDriver driver")) {
					line = "";
				}

				if (line.contains("new FirefoxDriver();")) {
					line = "";
				}

				if (line.contains("class")) {

					String[] data = line.split("class");
					StringBuffer sb = new StringBuffer();
					sb.append(data[0]);
					sb.append("class");
					if (!data[1].contains("extends")) {
						if (data[1].contains("{")) {
							String className = data[1].replace("{", "");
							sb.append(className + " extends TestCase {");
							line = sb.toString();
						}
					}

				}

				if (line.contains("baseUrl =")) {

					String tUrl = line.substring(line.length() - 4,
							line.length() - 1);

					if (tUrl.indexOf(";\"") > 0) {
						line = line.replace(";\"", "\"");
					}

				}

				if (line.contains("driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS)")) {

					line = "driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);";
				}

				if (line.contains("driver.get(baseUrl)")) {
					line = line.replace("baseUrl", "url");
				}
				
				if (line.contains("\"userId\")).sendKeys("))
	                {
	                	int epos = line.lastIndexOf("\"");
	                	int spos = line.lastIndexOf("\"", epos - 1);
	                	String subStr = line.substring(spos, epos + 1);
	                	line = line.replace(subStr, "userId");
	                }

	                if (line.contains("\"password\")).sendKeys("))
	                {
	                	int epos = line.lastIndexOf("\"");
	                	int spos = line.lastIndexOf("\"", epos - 1);
	                	String subStr = line.substring(spos, epos + 1);
	                	line = line.replace(subStr, "passwd");
	                }

	                if (line.contains("\"securityQuestionAnswer\")).sendKeys("))
	                {
	                	int epos = line.lastIndexOf("\"");
	                	int spos = line.lastIndexOf("\"", epos - 1);
	                	String subStr = line.substring(spos, epos + 1);
	                	line = line.replace(subStr, "securityQuestion");
	                }
	                
				/*if (line.contains("\"securityQuestionAnswer\")).sendKeys(")) {
					int epos = line.lastIndexOf("\"");
					int spos = line.lastIndexOf("\"", epos - 1);
					String subStr = line.substring(spos, epos + 1);
					line = line.replace(subStr, "securityQuestion");
				}*/

				lines.add(line);
				lines.add("\n");
				linePos++;
			}
			fr.close();
			br.close();

			FileWriter fw = new FileWriter(destinationFile);
			BufferedWriter out = new BufferedWriter(fw);
			for (String s : lines)
				out.write(s);
			out.flush();
			out.close();

			String actualClassPath = TestCaseFileValidationUtil.class
					.getProtectionDomain().getCodeSource().getLocation()
					.getPath();
			String splitActualPath = actualClassPath.split("com")[0];

			// splitActualPath = splitActualPath.substring(1,
			// splitActualPath.length()-1);

			String jarClassPath = splitActualPath + ";" + jarLocation;

			logger.info("jarClassPath :" + jarClassPath);

			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
			StandardJavaFileManager fileManager = compiler
					.getStandardFileManager(diagnostics, null, null);
			Iterable<? extends JavaFileObject> compilationUnits = fileManager
					.getJavaFileObjects(destinationFile);
			List<String> optionList = new ArrayList<String>();
			optionList.addAll(Arrays.asList("-classpath", jarClassPath));
			logger.info("jarClassPath :" + jarClassPath);
			StringWriter outputWriter = new StringWriter();
			JavaCompiler.CompilationTask task = compiler.getTask(outputWriter,
					fileManager, diagnostics, optionList, null,
					compilationUnits);
			boolean status = task.call();

			if (!status) {// If compilation error occurs
				/* Iterate through each compilation problem and print it */
				for (@SuppressWarnings("rawtypes")
				Diagnostic diagnostic : diagnostics.getDiagnostics()) {
					System.out.format("Error on line %d in %s \n",
							diagnostic.getLineNumber(), diagnostic);
					logger.error("Error on line :" + diagnostic.getLineNumber()
							+ " in " + diagnostic);
				}
			}
			String s = outputWriter.toString();

			logger.info("Compilation Errors :" + s);
			fileManager.close();

			if (status) {
				validateStatus = true;
			} else {
				validateStatus = false;
			}

		} catch (Exception ex) {
			logger.error("Exception while validating the uploaded File.");
			logger.error("Stack Trace :" + ExceptionUtils.getFullStackTrace(ex));
			validateStatus = false;
		}

		return validateStatus;
	}

	/*@SuppressWarnings("rawtypes")
	public String validateFile(File sourceFile, File destinationFile,
			String application, String environment, String packageName,
			String jarLocation) {
		try {
			String line = null;
			ArrayList<String> lines = new ArrayList<String>();

			// FileUtils.copyFile(sourceFile, destinationFile);

			FileReader fr = new FileReader(destinationFile);
			BufferedReader br = new BufferedReader(fr);
			int linePos = 0;
			while ((line = br.readLine()) != null) {

				if (linePos == 0 && line.contains("package")) {

					// Assuming file will not be there in Same package where
					// TestCase class is available so TestCase class is
					// imported.
					line = "package " + packageName + "." + application + "."
							+ environment + ";" + "\n"
							+ "import com.cts.mint.crawler.TestCase;";
				} else if (linePos == 0 && !line.contains("package")) {
					lines.add("package " + packageName + "." + application
							+ "." + environment + ";" + "\n"
							+ "import com.cts.mint.crawler.TestCase;");
					lines.add("\n");
				}

				if (line.contains("private WebDriver driver")) {
					line = "";
				}

				if (line.contains("new FirefoxDriver();")) {
					line = "";
				}

				if (line.contains("class")) {

					String[] data = line.split("class");
					StringBuffer sb = new StringBuffer();
					sb.append(data[0]);
					sb.append("class");
					if (!data[1].contains("extends")) {
						if (data[1].contains("{")) {
							String className = data[1].replace("{", "");
							sb.append(className + " extends TestCase {");
							line = sb.toString();
						}
					}

				}

				if (line.contains("baseUrl =")) {

					String tUrl = line.substring(line.length() - 4,
							line.length() - 1);

					if (tUrl.indexOf(";\"") > 0) {
						line = line.replace(";\"", "\"");
					}

				}

				if (line.contains("driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS)")) {

					line = "driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);";
				}

				if (line.contains("driver.get(baseUrl)")) {
					line = line.replace("baseUrl", "url");
				}

				if (line.contains("\"userId\")).sendKeys(")) {
					int epos = line.lastIndexOf("\"");
					int spos = line.lastIndexOf("\"", epos - 1);
					String subStr = line.substring(spos, epos + 1);
					line = line.replace(subStr, "userId");
				}

				if (line.contains("\"password\")).sendKeys(")) {
					int epos = line.lastIndexOf("\"");
					int spos = line.lastIndexOf("\"", epos - 1);
					String subStr = line.substring(spos, epos + 1);
					line = line.replace(subStr, "passwd");
				}

				if (line.contains("\"securityQuestionAnswer\")).sendKeys(")) {
					int epos = line.lastIndexOf("\"");
					int spos = line.lastIndexOf("\"", epos - 1);
					String subStr = line.substring(spos, epos + 1);
					line = line.replace(subStr, "securityQuestion");
				}

				lines.add(line);
				lines.add("\n");
				linePos++;
			}
			fr.close();
			br.close();

			FileWriter fw = new FileWriter(destinationFile);
			BufferedWriter out = new BufferedWriter(fw);
			for (String s : lines)
				out.write(s);
			out.flush();
			out.close();

			String actualClassPath = TestCaseFileValidationUtil.class
					.getProtectionDomain().getCodeSource().getLocation()
					.getPath();
			String splitActualPath = actualClassPath.split("com")[0];

			// splitActualPath = splitActualPath.substring(1,
			// splitActualPath.length()-1);

			String jarClassPath = splitActualPath + ";" + jarLocation;

			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
			StandardJavaFileManager fileManager = compiler
					.getStandardFileManager(diagnostics, null, null);
			Iterable<? extends JavaFileObject> compilationUnits = fileManager
					.getJavaFileObjects(destinationFile);
			List<String> optionList = new ArrayList<String>();
			optionList.addAll(Arrays.asList("-classpath", jarClassPath));
			logger.info("jarClassPath :" + jarClassPath);
			StringWriter outputWriter = new StringWriter();
			JavaCompiler.CompilationTask task = compiler.getTask(outputWriter,
					fileManager, diagnostics, optionList, null,
					compilationUnits);
			boolean status = task.call();

			if (!status) {// If compilation error occurs
				// Iterate through each compilation problem and print it
				for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
					System.out.format("Error on line %d in %s \n",
							diagnostic.getLineNumber(), diagnostic);
					logger.error("Error on line :" + diagnostic.getLineNumber()
							+ " in " + diagnostic);
				}
			}
			String s = outputWriter.toString();

			logger.info("Compilation Errors :" + s);
			fileManager.close();

			if (status) {
				return "success";
			} else {
				return "failure";
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return "success";
	}*/

}
